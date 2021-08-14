package com.vritti.sales.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.vritti.crm.bean.City;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.activity.BatchSelectionActivity;
import com.vritti.inventory.physicalInventory.activity.ItemMasterSyncActivity;
import com.vritti.inventory.physicalInventory.activity.PIEntryPrintingActivity;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class NewSalesOrderBooking extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    TextView txtcustname, txtsono,txtprodqty, txtprodamt, txtprodtaxamt, txtchrgamt,txtchrgtaxamt, txtdiscamt, txttotamt, txt_custname, txt_sono;
    Button /*btnhdr,*/ btnitem, btncharge, btntc, btncomision, btnpayment,btnreimbursmnt, btnfinalsave,btnfinalcancel;
    LinearLayout btnhdr;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    String orderType = "";
    boolean isheaderFilled = false;
    public static final int SALES_HEADER_FILLED = 1;
    public static final int SALES_ITEM_FILLED = 2;
    public static final int SALES_CHARGE_FILLED = 3;
    public static final int SALES_TANDC_FILLED = 4;
    public static final int SALES_COMMISSION_FILLED = 5;
    public static final int SALES_PAYMENT_FILLED = 6;
    public static final int SALES_REIMBURSEMENT_FILLED = 7;

    String custName = "", sono = "", mode = "", task = "";
    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;
    boolean IsShipInvRequired;
    ProgressDialog progressDialog_1 = null;

    JSONObject jobj_so, jHDR_obj;
    JSONArray jArray, jHDRArr;
    String finalOBJ = "";

    JSONObject jobj_remb;
    String SuspectId="",SoHeaderId="",OrderTypeid="",SoNo="",NotifyId="",CurrencyId="",ExchangeRate="",ProjectId="",GracePeriod="",
            FinalProductAmt="0",FinalProductTaxAmt="0",FinalChargeAmt="0",FinalChargetaxAmt="0",lastDiscAmount="0",lastTotalamt="0",
            ProdMinusDisc="0",zoneId="",OrderValue="0", UOMMasterId="",ItemMasterId="",ExecutionDetails="",StoreUploadedFile="",
            MainfeaturedItemArray="",ReimbursementDetails="",PaymentsDetails="",CommisssionDetails="",ChargesDetails="",ItemDetails="",
            ScheduleDetails="";
    String TAndCDetails = "", HeaderDetails = "";
    String SoDate = "", CustomerId="",ConsigneeName="",ConsigneeId="",BilltoId="",CustPONo="",CustPoDate="",SENameId="",SEName="",StartDt="",
            EndDate="";
    String DeliveryTermId="",InvoiceDeliveryById="",PaymenttermsId="",FreightsId="",ModeOfDispatchId="",remark="",SecurityDepAmt="",
            PerfGarunteeMode="",PerfGarunteePer="0",RoadPermit="",LiqDamges="",PreDispatch="",InspectionChgs="";
    int Qty1=0;
    String shipToMasterId = "",CustOrderPONo = "", QuotationNo = "", CreditTerm = "", CreditDays = "", CreationLevel = "", AddedBy = "", AddedDt = "",
    CustOrderPODt = "", Approver = "", EventFrmDt = "", EventToDt = "",PriceListHdrID = "", ItemClassification = "",
    SOContractId = "",CustVendorName = "", notifiyTo = "",BillToName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sales_order_booking);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        setListeners();
    }

    public void init(){
        parent = NewSalesOrderBooking.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Sales Order");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        txtprodamt = findViewById(R.id.txtprodamt);
        txtprodqty = findViewById(R.id.txtprodqty);
        txtprodtaxamt = findViewById(R.id.txtprodtaxamt);
        txtchrgamt = findViewById(R.id.txtchrgamt);
        txtchrgtaxamt = findViewById(R.id.txtchrgtaxamt);
        txtdiscamt = findViewById(R.id.txtdiscamt);
        txttotamt = findViewById(R.id.txttotamt);
        txt_custname = findViewById(R.id.txt_custname);
        txt_sono = findViewById(R.id.txt_sono);

        btnhdr = findViewById(R.id.btnhdr);
        btnitem = findViewById(R.id.btnitem);
        btnitem.setAlpha((float) 0.3);
        btnitem.setEnabled(false);
        btncharge = findViewById(R.id.btncharge);
        btncharge.setAlpha((float) 0.3);
        btncharge.setEnabled(false);
        btntc = findViewById(R.id.btntc);
        btntc.setAlpha((float) 0.3);
        btntc.setEnabled(false);
        btncomision = findViewById(R.id.btncomision);
        btncomision.setAlpha((float) 0.3);
        btncomision.setEnabled(false);
        btnpayment = findViewById(R.id.btnpayment);
        btnpayment.setAlpha((float) 0.3);
        btnpayment.setEnabled(false);
        btnreimbursmnt = findViewById(R.id.btnreimbursmnt);
        btnreimbursmnt.setAlpha((float) 0.3);
        btnreimbursmnt.setEnabled(false);
        btnfinalsave= findViewById(R.id.btnfinalsave);
        btnfinalsave.setAlpha((float) 0.3);
        btnfinalsave.setEnabled(false);
        btnfinalcancel = findViewById(R.id.btnfinalcancel);
        progressDialog_1 = new ProgressDialog(NewSalesOrderBooking.this);

        ut = new Utility();
        cf = new CommonFunction(NewSalesOrderBooking.this);
        tcf = new Tbuds_commonFunctions(NewSalesOrderBooking.this);
        String settingKey = ut.getSharedPreference_SettingKey(NewSalesOrderBooking.this);
        String dabasename = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(NewSalesOrderBooking.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(NewSalesOrderBooking.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        Intent intent = getIntent();
        mode = intent.getStringExtra("Mode");

        if(mode.equalsIgnoreCase("Edit")){
            btnitem.setEnabled(true);
            btnitem.setAlpha((float)1);

            btntc.setEnabled(true);
            btntc.setAlpha((float)1);

            btncharge.setEnabled(true);
            btncharge.setAlpha((float)1);

            btncomision.setEnabled(true);
            btncomision.setAlpha((float)1);

            btnpayment.setEnabled(true);
            btnpayment.setAlpha((float)1);

            btnreimbursmnt.setEnabled(true);
            btnreimbursmnt.setAlpha((float)1);

            btnfinalsave.setEnabled(true);
            btnfinalsave.setAlpha((float)1);

            //call API to get SOheader data and set values to views
            SoHeaderId = intent.getStringExtra("SoHeaderID");
            SoHeaderId  = SoHeaderId.replace("\"","");
            callEditAPI();
        }else {
            orderType = intent.getStringExtra("OrderType");
            AnyMartData.Order_Type = intent.getStringExtra("OrderType");
            AnyMartData.OrderTypeMasterId = intent.getStringExtra("OrderTypeMasterId");
            IsShipInvRequired = Boolean.parseBoolean(intent.getStringExtra("IsShipInvRequired"));

            setInitValues();
        }

       /* final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        SoDate = String.format("%2d",day) + "/" + String.format("%02d", (month + 1)) + "/" + year;*/

    }

    public void setListeners(){

        btnfinalsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                task = "PostSO";
                if(PaymenttermsId.equalsIgnoreCase("")){
                    Toast.makeText(parent,"Please select Payment terms from terms and conditions.",Toast.LENGTH_SHORT).show();
                }else {
                    finalOBJ = createJSON().toString();
                    finalOBJ = finalOBJ.toString().replaceAll("\\\\", "");
                    finalOBJ = finalOBJ.replaceAll("^\"+ \"+$","");

                    //call final save API
                    if (isnet()) {

                        new StartSession(NewSalesOrderBooking.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                //call CreateSession API
                                new CreateSession().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(getApplicationContext(), msg);
                                progressDialog_1.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnfinalcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnhdr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parent,SalesHeaderActivity.class);
                intent.putExtra("OrderType",AnyMartData.Order_Type);
                intent.putExtra("OrderTypeMasterId",AnyMartData.OrderTypeMasterId);
                if(mode.equalsIgnoreCase("Edit")){
                    intent.putExtra("jHDRArray",jHDRArr.toString());
                    intent.putExtra("Mode","Edit");
                }else {
                    intent.putExtra("Mode","AddNew");
                }
                startActivityForResult(intent,SALES_HEADER_FILLED);

            }
        });

        btnitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(parent,SalesItemActivity.class);
                    intent.putExtra("jHeaderArray",HeaderDetails);
                    intent.putExtra("SoDate",SoDate);
                    intent.putExtra("CustomerId",CustomerId);
                    intent.putExtra("SoHeaderId",SoHeaderId);
                    if(mode.equalsIgnoreCase("Edit")){
                        intent.putExtra("Mode","Edit");
                    }else {
                        intent.putExtra("Mode","AddNew");
                    }
                    startActivityForResult(intent,SALES_ITEM_FILLED);
            }
        });

        btncharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float totval = 0.0f;

                totval = Float.parseFloat(txttotamt.getText().toString()) - Float.parseFloat(txtprodtaxamt.getText().toString());

                    Intent intent = new Intent(parent,SalesChargeActivity.class);
                    intent.putExtra("Qty",String.valueOf(Qty1));
                    intent.putExtra("TotValue",String.format("%.2f",totval));
                    startActivityForResult(intent,SALES_CHARGE_FILLED);
            }
        });

        btntc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(parent,TermsAndConditionsActivity.class);
                    startActivityForResult(intent,SALES_TANDC_FILLED);
            }
        });

        btncomision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(parent,SalesCommissionActivity.class);
                    intent.putExtra("ProductAmt",txtprodamt.getText().toString());
                    //intent.putExtra("ProductAmt","240.00");
                    startActivityForResult(intent,SALES_COMMISSION_FILLED);
            }
        });

        btnpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(parent,SalesPaymentActivity.class);
                    startActivityForResult(intent,SALES_PAYMENT_FILLED);
            }
        });

        btnreimbursmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent,ReimbursementActivity.class);
                startActivityForResult(intent,SALES_REIMBURSEMENT_FILLED);

                if(mode.equalsIgnoreCase("Edit")){

                }else {

                }
            }
        });
    }

    public void setInitValues(){
        //if IsShipInvRequired=Y then charge,commission,payment,reimbursement tab hide

        if(IsShipInvRequired == true){
            btncharge.setVisibility(View.GONE);
            btncomision.setVisibility(View.GONE);
            btnpayment.setVisibility(View.GONE);
            btnreimbursmnt.setVisibility(View.GONE);
        }else {
            btncharge.setVisibility(View.VISIBLE);
            btncomision.setVisibility(View.VISIBLE);
            btnpayment.setVisibility(View.VISIBLE);
            btnreimbursmnt.setVisibility(View.VISIBLE);
        }

        toolbar.setTitle("Sales Order -  "+ AnyMartData.Order_Type);
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

    public JSONObject createJSON(){

        UUID uuid = UUID.randomUUID();
       // SoHeaderId = uuid.toString();

        NotifyId = BilltoId;
        CurrencyId = "1";
        ExchangeRate = "1.00";
        GracePeriod = "0";

        try {
            jArray = new JSONArray();
            jobj_so = new JSONObject();

            jobj_so.put("SuspectId",SuspectId);
            jobj_so.put("SoHeaderId",SoHeaderId);  //guid
            jobj_so.put("OrderTypeid",AnyMartData.OrderTypeMasterId);
            jobj_so.put("SoNo",SoNo);
            //header
            jobj_so.put("SoDate",SoDate);
            jobj_so.put("CustomerId",CustomerId);
            jobj_so.put("ConsigneeName",ConsigneeName);
            jobj_so.put("ConsigneeId",ConsigneeId);
            jobj_so.put("BilltoId",BilltoId);
            jobj_so.put("CustPONo",CustPONo);
            jobj_so.put("CustPoDate",CustPoDate);
            jobj_so.put("NotifyId",NotifyId);       //pass billtoid
            jobj_so.put("CurrencyId",CurrencyId);
            jobj_so.put("ExchangeRate",ExchangeRate);   //pass value in it 1
            jobj_so.put("SENameId",SENameId);
            jobj_so.put("SEName",SEName);
            jobj_so.put("StartDt",StartDt);
            jobj_so.put("EndDate",EndDate);
            jobj_so.put("ProjectId",ProjectId);
            jobj_so.put("GracePeriod",GracePeriod);
            jobj_so.put("FinalProductAmt",FinalProductAmt);
            jobj_so.put("FinalProductTaxAmt",FinalProductTaxAmt);
            jobj_so.put("FinalChargeAmt",FinalChargeAmt);
            jobj_so.put("FinalChargetaxAmt",FinalChargetaxAmt);
            jobj_so.put("lastDiscAmount",lastDiscAmount);
            jobj_so.put("lastTotalamt",lastTotalamt);
            jobj_so.put("ProdMinusDisc",ProdMinusDisc);
            jobj_so.put("zoneId",zoneId);

            //terms&condition
            jobj_so.put("DeliveryTermId",DeliveryTermId);
            jobj_so.put("InvoiceDeliveryById",InvoiceDeliveryById);
            jobj_so.put("PaymenttermsId",PaymenttermsId);
            jobj_so.put("FreightsId",FreightsId);
            jobj_so.put("ModeOfDispatchId",ModeOfDispatchId);
            jobj_so.put("remark",remark);
            jobj_so.put("SecurityDepAmt",SecurityDepAmt);
            jobj_so.put("PerfGarunteeMode",PerfGarunteeMode);
            jobj_so.put("PerfGarunteePer",PerfGarunteePer);
            jobj_so.put("RoadPermit",RoadPermit);
            jobj_so.put("LiqDamges",LiqDamges);
            jobj_so.put("PreDispatch",PreDispatch);
            jobj_so.put("InspectionChgs",InspectionChgs);

            jobj_so.put("OrderValue",OrderValue);
            jobj_so.put("Qty1",Qty1);
            jobj_so.put("UOMMasterId",UOMMasterId);
            jobj_so.put("ItemMasterId",ItemMasterId);

            JSONArray jScheduleDtl, jItemDtl, jPaymentDtl, jChargeDtl,jCommissionDtl, jReimbDtl, jExeDtl, jStoreUpldDtl, jFeatureDtl;

            jScheduleDtl = new JSONArray(ScheduleDetails);
            jItemDtl = new JSONArray(ItemDetails);
            jobj_so.put("ScheduleDetails",jScheduleDtl);
            jobj_so.put("ItemDetails",jItemDtl);

            if(PaymentsDetails.equalsIgnoreCase("")){
                jPaymentDtl = new JSONArray();
                jobj_so.put("PaymentsDetails",jPaymentDtl);
            }else {
                jPaymentDtl = new JSONArray(PaymentsDetails);
                jobj_so.put("PaymentsDetails",jPaymentDtl);
            }

            if(ChargesDetails.equalsIgnoreCase("")){
                jChargeDtl = new JSONArray();
                jobj_so.put("ChargesDetails",jChargeDtl);
            }else {
                jChargeDtl = new JSONArray(ChargesDetails);
                jobj_so.put("ChargesDetails",jChargeDtl);
            }

            if(CommisssionDetails.equalsIgnoreCase("")){
                jCommissionDtl = new JSONArray();
                jobj_so.put("CommisssionDetails",jCommissionDtl);
            }else {
                jCommissionDtl = new JSONArray(CommisssionDetails);
                jobj_so.put("CommisssionDetails",jCommissionDtl);
            }

            if(ReimbursementDetails.equalsIgnoreCase("")){
                jReimbDtl = new JSONArray();
                jobj_so.put("ReimbursementDetails",jReimbDtl);
            }else {
                jReimbDtl = new JSONArray(ReimbursementDetails);
                jobj_so.put("ReimbursementDetails",jReimbDtl);
            }

            jExeDtl = new JSONArray();
            jStoreUpldDtl = new JSONArray();
            jFeatureDtl = new JSONArray();

            jobj_so.put("ExecutionDetails",jExeDtl);
            jobj_so.put("StoreUploadedFile",jStoreUpldDtl);
            jobj_so.put("MainfeaturedItemArray",jFeatureDtl);

            jArray.put(jobj_so);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jobj_so;
    }

    class CreateSession extends AsyncTask<Integer, Void, Integer> {
        String res;
        Boolean IsSessionActivate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SessionSO +"?CallId=&CustId='"+CustomerId+"'&Menu='SOMenu'";
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");

            } catch (Exception e) {
                e.printStackTrace();
                IsSessionActivate = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            // dismissProgressDialog();
            if(res.equalsIgnoreCase("")){
                //session activated
                IsSessionActivate = true;
                //call postSaveAPI
                if(task.equalsIgnoreCase("SessionActivate")){

                    //call another edit method
                    callEditSOHeader();

                }else if(task.equalsIgnoreCase("PostSO")){
                    new PostSOSaveAPI().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            }
        }
    }

    class PostSOSaveAPI extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog_1 == null) {
                    // progressDialog_1.setMessage("Sending data please wait...");
                    progressDialog_1.setTitle("Sending data please wait...");
                    progressDialog_1.setIndeterminate(false);
                    progressDialog_1.setCancelable(false);
                    progressDialog_1.setCanceledOnTouchOutside(false);

                }
                progressDialog_1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_postSaveSO;

                res = ut.OpenPostConnection(url,finalOBJ,parent);
                response = res.toString().replaceAll("\\\\", "");
                response = response.toString().replaceAll("^\"+ \"+$","");

            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressDialog_1.dismiss();

            //"Success,PDGSO19-20/051,14863F2A-CABB-4F3B-8EBC-B4FBB4FA8007"
            try{
                if(response.contains("Success")){

                    SoNo = response.split(",")[1];
                    SoHeaderId = response.split(",")[2];

                    if(mode.equalsIgnoreCase("Edit")){

                        ContentValues values = new ContentValues();
                        values.put("CustomerMasterId", CustomerId);
                        values.put("ConsigneeName", ConsigneeName);
                        values.put("NetAmt", lastTotalamt);
                        long a = sql.update(db.TABLE_SOHistory, values, "SOHeaderId=?", new String[]{SoHeaderId});

                        Toast.makeText(parent,"Your SO number "+SoNo+" updated successfully.",Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        //store data in table SODetail and SOHeader
                        tcf.insertBookedSO(SoHeaderId,SoNo,SoDate,CustomerId,ConsigneeName,txttotamt.getText().toString());
                        Toast.makeText(parent,"SO booked successfully. Your SO number is "+SoNo,Toast.LENGTH_LONG).show();
                        finish();
                    }

                }else if(response.equalsIgnoreCase("Error")){
                    Toast.makeText(parent,"Sorry, server error! failed to booked SO",Toast.LENGTH_SHORT).show();
                 //   finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /***********************Edit API****************/

    public void callEditAPI(){
        task = "SessionActivate";
        if (isnet()) {

            new StartSession(NewSalesOrderBooking.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //call CreateSession API
                    new CreateSession().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void callEditSOHeader(){
        if (isnet()) {
            new StartSession(NewSalesOrderBooking.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //call EditSOheader API
                    new GetEditData_SOHeader().execute();
                }
                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class GetEditData_SOHeader extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog_1.show();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getSOheaderEditData +"?CallId="+SoHeaderId;
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length()-1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog_1.dismiss();

            if(!res.equalsIgnoreCase("")){
                parseJsonData(res);
            }else {
                Toast.makeText(parent,"No data found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parseJsonData(String res){

        try {
            jHDRArr = new JSONArray(res);

            for(int i=0; i<jHDRArr.length(); i++){
                jHDR_obj = jHDRArr.getJSONObject(i);
                SoHeaderId = jHDR_obj.getString("SOHeaderId");
                sono = jHDR_obj.getString("SONo");
                SoNo = jHDR_obj.getString("SONo");
                SoDate = jHDR_obj.getString("SODate");
                CustomerId = jHDR_obj.getString("CustomerMasterId");
                shipToMasterId = jHDR_obj.getString("ShipToMasterId");
                ConsigneeId = shipToMasterId;
                ConsigneeName = jHDR_obj.getString("ConsigneeName");
                CustOrderPONo = jHDR_obj.getString("CustOrderPONo");
                QuotationNo = jHDR_obj.getString("QuotationNo");
                OrderTypeid = jHDR_obj.getString("OrderTypeMasterId");
                AnyMartData.OrderTypeMasterId = OrderTypeid;
                CurrencyId = jHDR_obj.getString("CurrencyMasterId");
                DeliveryTermId = jHDR_obj.getString("DeliveryId");
                CreditTerm = jHDR_obj.getString("CreditTerm");
                CreditDays = jHDR_obj.getString("CreditDays");
                OrderValue = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("TotalOrderValue")));
                FinalProductAmt = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("BasicAmt")));
                FinalProductAmt = OrderValue;
                Qty1 = (int)Float.parseFloat(jHDR_obj.getString("TotalOrderQty"));
                CreationLevel = jHDR_obj.getString("CreationLevel");
                AddedBy = jHDR_obj.getString("AddedBy");
                AddedDt = jHDR_obj.getString("AddedDt");
                CustOrderPODt = jHDR_obj.getString("CustOrderPODt");
                NotifyId = jHDR_obj.getString("NotifyId");
                Approver = jHDR_obj.getString("Approver");
                EventFrmDt = jHDR_obj.getString("EventFrmDt");
                EventToDt = jHDR_obj.getString("EventToDt");
                lastTotalamt = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("TotalGrossAmt")));
                lastTotalamt = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("NetAmt")));
                FinalProductTaxAmt = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("TotTaxAmt")));
                FinalChargeAmt = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("TotChargeAmt")));
                lastDiscAmount = String.format("%.2f",Float.parseFloat(jHDR_obj.getString("TotDiscAmt")));
                BilltoId = jHDR_obj.getString("BillToId");
                PriceListHdrID = jHDR_obj.getString("PriceListHdrID");
                ExchangeRate = jHDR_obj.getString("ExRate");
                ItemClassification = jHDR_obj.getString("ItemClassification");
                SOContractId = jHDR_obj.getString("SOContractId");
                GracePeriod = jHDR_obj.getString("GracePeriod");
                SecurityDepAmt = jHDR_obj.getString("SecurityDepAmt");
                PerfGarunteePer = jHDR_obj.getString("PerfGarunteePer");
                PerfGarunteeMode = jHDR_obj.getString("PerfGarunteeMode");
                LiqDamges = jHDR_obj.getString("LiqDamges");
                PreDispatch = jHDR_obj.getString("PreDispatch");
                RoadPermit = jHDR_obj.getString("RoadPermit");
                InspectionChgs = jHDR_obj.getString("InspectionChgs");
                CustVendorName = jHDR_obj.getString("CustVendorName");
                ConsigneeName = jHDR_obj.getString("ConsigneeName1");
                notifiyTo = jHDR_obj.getString("notifiyTo");
                BillToName = jHDR_obj.getString("BillTo");
                UserName = jHDR_obj.getString("UserName");
                AnyMartData.Order_Type = jHDR_obj.getString("Description");

                txtprodamt.setText(FinalProductAmt);
                txtprodqty.setText(jHDR_obj.getString("TotalOrderQty"));
                txtprodtaxamt.setText(FinalProductTaxAmt);
                txtdiscamt.setText(lastDiscAmount);
                txttotamt.setText(lastTotalamt);
                txt_custname.setText(CustVendorName);
                txt_sono.setText(sono);
            }
            setValueToHeaderArray(res);

            getIsShipEnvRequired();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getIsShipEnvRequired(){
        String countQuery = "SELECT IsShipInvRequired FROM "+ db.TABLE_OrderType+" WHERE OrderTypeMasterId="+AnyMartData.OrderTypeMasterId;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String _IsShipInvRequired = cursor.getString(cursor.getColumnIndex("IsShipInvRequired"));

                if(_IsShipInvRequired.equalsIgnoreCase("N")){
                    IsShipInvRequired = false;
                }else if(_IsShipInvRequired.equalsIgnoreCase("Y")){
                    IsShipInvRequired = true;
                }

            } while (cursor.moveToNext());
        }

        setInitValues();
    }

    public void setValueToHeaderArray(String json){
        json = json.replaceAll("\\\\", "");

        try{
                JSONArray jHDR = new JSONArray();
                JSONArray j_hdr_arr = new JSONArray(json);
                try {
                    for(int j=0; j<j_hdr_arr.length();j++){
                        JSONObject j_obj_hdr = new JSONObject();
                        JSONObject jj = new JSONObject();
                        j_obj_hdr = j_hdr_arr.getJSONObject(j);

                        SoDate = j_obj_hdr.getString("SODate").replace("/","");
                        String EventFrmDt = j_obj_hdr.getString("EventFrmDt").replace("/","");
                        String EventToDt = j_obj_hdr.getString("EventToDt").replace("/","");
                        String CustOrderPODt = j_obj_hdr.getString("CustOrderPODt").replace("/","");

                        CustPONo = j_obj_hdr.getString("CustOrderPONo");
                        CustPoDate = getDate(Long.parseLong(parseDate(CustOrderPODt)));
                        ConsigneeName = j_obj_hdr.getString("ConsigneeName");
                        ConsigneeId = j_obj_hdr.getString("BillToId");
                        BilltoId = ConsigneeId;
                        CustomerId = j_obj_hdr.getString("CustomerMasterId");
                        SoDate = getDate(Long.parseLong(parseDate(SoDate)));
                        StartDt = getDate(Long.parseLong(parseDate(EventFrmDt)));
                        EndDate = getDate(Long.parseLong(parseDate(EventToDt)));
                        SEName = UserName;
                        SENameId = UserMasterId;

                        jj.put("SoDate",SoDate);
                        jj.put("CustomerId",CustomerId);  //guid
                        jj.put("ConsigneeName",ConsigneeName);
                        jj.put("ConsigneeId",ConsigneeId);
                        jj.put("BilltoId",BilltoId);
                        jj.put("CustPONo",CustPONo);
                        jj.put("CustPoDate",CustPoDate);
                        jj.put("SENameId",UserMasterId);
                        jj.put("SEName",UserName);
                        jj.put("StartDt",StartDt);
                        jj.put("EndDate",EndDate);

                        jHDR.put(jj);
                    }

                    HeaderDetails = jHDR.toString();

                }catch (Exception e){
                    e.printStackTrace();
                }
        }catch (Exception e){
            e.printStackTrace();
        }

        callReimbursementDetails();
    }

    public void callReimbursementDetails(){
        if (isnet()) {
            new StartSession(NewSalesOrderBooking.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //call EditReimbursement API
                    new GetEditData_Reimbursement().execute();
                }
                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class GetEditData_Reimbursement extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog_1.show();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getReimbDtlEditData +"?HeaderId="+SoHeaderId;
                res = Utility.OpenConnection(url,parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length()-1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog_1.dismiss();

            if(res.equalsIgnoreCase("[]")){
                Toast.makeText(parent,"No reimbursement data found",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String parseDate(String date) {
        date = date.replace("(","");
        date = date.replace(")","");
        date = date.replace("Date","");
        return date;
    }

    private String getDate(long time) {
        String date = new SimpleDateFormat("dd/MM/yyyy").format(time);
        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SALES_HEADER_FILLED && resultCode == SALES_HEADER_FILLED) {
            //enable other buttons one by one
            custName = data.getStringExtra("Customer");
            sono = data.getStringExtra("SONO");

            txt_custname.setText(custName);
            txt_sono.setText(sono);

            isheaderFilled = true;
            btnitem.setEnabled(true);
            btnitem.setAlpha((float)1);

            String headerArray = data.getStringExtra("jHeaderArray");
            HeaderDetails = headerArray;

            try{
                JSONArray jdata = new JSONArray(HeaderDetails);

                for(int i =0; i< jdata.length(); i++){
                    JSONObject jsonObject = jdata.getJSONObject(i);

                    SoDate = jsonObject.getString("SoDate");
                    CustomerId = jsonObject.getString("CustomerId");
                    ConsigneeName = jsonObject.getString("ConsigneeName");
                    ConsigneeId = jsonObject.getString("ConsigneeId");
                    BilltoId = jsonObject.getString("BilltoId");
                    CustPONo = jsonObject.getString("CustPONo");
                    CustPoDate = jsonObject.getString("CustPoDate");
                    SENameId = jsonObject.getString("SENameId");
                    SEName = jsonObject.getString("SEName");
                    StartDt = jsonObject.getString("StartDt");
                    EndDate = jsonObject.getString("EndDate");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(requestCode == SALES_ITEM_FILLED && resultCode == SALES_ITEM_FILLED){

            btntc.setEnabled(true);
            btntc.setAlpha((float)1);

            btncharge.setEnabled(true);
            btncharge.setAlpha((float)1);

            btncomision.setEnabled(true);
            btncomision.setAlpha((float)1);

            btnpayment.setEnabled(true);
            btnpayment.setAlpha((float)1);

            btnreimbursmnt.setEnabled(true);
            btnreimbursmnt.setAlpha((float)1);

            btnfinalsave.setEnabled(true);
            btnfinalsave.setAlpha((float)1);

            Toast.makeText(parent,"Select payment terms from Terms & Conditions.",Toast.LENGTH_SHORT).show();

            String itemArray = data.getStringExtra("jItemArray");
            String scheduleArray = data.getStringExtra("jScheduleArray");
            //String headerArray = data.getStringExtra("jHeaderArray");
            FinalProductAmt = data.getStringExtra("FinalProductAmt");
            FinalProductTaxAmt = data.getStringExtra("FinalProductTaxAmt");
            lastDiscAmount = data.getStringExtra("lastDiscAmount");
            lastTotalamt = data.getStringExtra("lastTotalamt");
            OrderValue = lastTotalamt;
            UOMMasterId = data.getStringExtra("UOMMasterId");
            Qty1 = (int)Float.parseFloat(data.getStringExtra("finalQty"));

            ItemDetails = itemArray;
            ScheduleDetails = scheduleArray;

            txtprodamt.setText(FinalProductAmt);
            txtprodqty.setText(data.getStringExtra("finalQty"));
            txtprodtaxamt.setText(FinalProductTaxAmt);
            txtdiscamt.setText(lastDiscAmount);
            txttotamt.setText(lastTotalamt);

        }else if(requestCode == SALES_CHARGE_FILLED && resultCode == SALES_CHARGE_FILLED){
            String chargeArray = data.getStringExtra("jChargeArray");
            FinalChargeAmt = data.getStringExtra("FinalChargeAmt");
            FinalChargetaxAmt = data.getStringExtra("FinalChargetaxAmt");
            ChargesDetails = chargeArray;

            txtchrgamt.setText(FinalChargeAmt);
            txtchrgtaxamt.setText(FinalChargetaxAmt);

            float lastTotal = 0.0f, chargAmt = 0.0f, chargeTax = 0.0f;
            lastTotal = Float.parseFloat(lastTotalamt);
            chargAmt = Float.parseFloat(FinalChargeAmt);
            chargeTax = Float.parseFloat(FinalChargetaxAmt);

            lastTotalamt = String.format("%.2f",(lastTotal + chargAmt + chargeTax));

            txttotamt.setText(lastTotalamt);

        }else if(requestCode == SALES_TANDC_FILLED && resultCode == SALES_TANDC_FILLED){
            String trms_cndtn_Array = data.getStringExtra("jtcArray");
            TAndCDetails = trms_cndtn_Array;

            try{
                JSONArray jdata = new JSONArray(TAndCDetails);

                for(int i =0; i< jdata.length(); i++){
                    JSONObject jsonObject = jdata.getJSONObject(i);

                    DeliveryTermId = jsonObject.getString("DeliveryTermId");
                    InvoiceDeliveryById = jsonObject.getString("InvoiceDeliveryById");
                    PaymenttermsId = jsonObject.getString("PaymenttermsId");
                    FreightsId = jsonObject.getString("FreightsId");
                    ModeOfDispatchId = jsonObject.getString("ModeOfDispatchId");
                    remark = jsonObject.getString("remark");
                    SecurityDepAmt = jsonObject.getString("SecurityDepAmt");
                    PerfGarunteeMode = jsonObject.getString("PerfGarunteeMode");
                    PerfGarunteePer = jsonObject.getString("PerfGarunteePer");
                    RoadPermit = jsonObject.getString("RoadPermit");
                    LiqDamges = jsonObject.getString("LiqDamges");
                    PreDispatch = jsonObject.getString("PreDispatch");
                    InspectionChgs = jsonObject.getString("InspectionChgs");
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }else if(requestCode == SALES_COMMISSION_FILLED && resultCode == SALES_COMMISSION_FILLED){
            String commssnArrayArray = data.getStringExtra("jCommssnArray");
            CommisssionDetails = commssnArrayArray;
        }else if(requestCode == SALES_PAYMENT_FILLED && resultCode == SALES_PAYMENT_FILLED){
            String paymntArray = data.getStringExtra("jPaymentArray");
            PaymentsDetails = paymntArray;
        }else if(requestCode == SALES_REIMBURSEMENT_FILLED && resultCode == SALES_REIMBURSEMENT_FILLED){
            String rembArray = data.getStringExtra("jRembArray");
            ReimbursementDetails = rembArray;
        }
    }

}
