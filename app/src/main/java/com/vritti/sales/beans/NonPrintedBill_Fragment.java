package com.vritti.sales.beans;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.CounterBilling.PrintedBillDetailsActivity;
import com.vritti.sales.adapters.NonPrintedBillListAdapter;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class NonPrintedBill_Fragment extends Fragment {
    private static Context parent;

    SharedPreferences sharedpreferences;
    SearchView searchView;
    String jsonData;

    ListView list_non_printed_bills;
    ArrayList<CounterbillingBean> nonprintedBillList;
    ArrayList<CounterbillingBean> tempList;
    NonPrintedBillListAdapter billAdapter;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    static SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", json="";

    boolean isInclusiveTax = true;

    ArrayList<CounterbillingBean> cbillList;

    @SuppressLint("ValidFragment")
    public NonPrintedBill_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_non_ordered__fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tbuds_non_printed_bill, container, false);

        parent = getActivity();

        sharedpreferences = getActivity().getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        list_non_printed_bills = (ListView)view.findViewById(R.id.list_non_printed_bills);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);

       //get non-printed bills data from database
        nonprintedBillList = new ArrayList<CounterbillingBean>();
        tempList = new ArrayList<CounterbillingBean>();
        cbillList = new ArrayList<CounterbillingBean>();

        new StartSession_tbuds(parent, new CallbackInterface() {

            @Override
            public void callMethod() {

                new GetBillHistory().execute();
            }

            @Override
            public void callfailMethod(String s) {

            }
        });

        setListener();

        return view;
    }

    private void setListener() {
        list_non_printed_bills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String billNo = nonprintedBillList.get(position).getBillNo();
                String billDate = nonprintedBillList.get(position).getDateTime();
                Intent intent = new Intent(getActivity(), PrintedBillDetailsActivity.class);
                intent.putExtra("IntentFrom", "NonPrintedBills");
                intent.putExtra("BillNo",billNo);
                intent.putExtra("BillDate",billDate);
                startActivity(intent);
                //getActivity().finish();
            }
        });
    }

    public void getPrintedBillsList(){
        nonprintedBillList.clear();

        String billNo = "", dateTime = "", CustomerName="";
        long dateSTamp = 0;
        float billPaybleAmount=0.0f;

        String query = "SELECT DISTINCT BillPrintNo from "+ databaseHelper.TABLE_BILL_CB + " WHERE isPrinted='No' Order by Date DESC";
        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                billNo = c.getString(c.getColumnIndex("BillPrintNo"));

                    CounterbillingBean counterbillingBean = null;

                    String qry = "SELECT Date,PayableAmt,CustomerName from "+ databaseHelper.TABLE_BILL_CB +
                            " WHERE BillPrintNo='"+billNo+"' AND isPrinted='No' LIMIT 1";
                    Cursor c1 = sql_db.rawQuery(qry,null);

                    if(c1.getCount() > 0){
                        c1.moveToLast();
                        dateTime = c1.getString(c1.getColumnIndex("Date"));
                        dateSTamp = convertToTimestamp(dateTime);
                        CustomerName = c1.getString(c1.getColumnIndex("CustomerName"));

                        String q_pay = "Select SUM(PayableAmt) as totPayble from "+databaseHelper.TABLE_BILL_CB+" Where BillPrintNo='"+billNo+"'";
                        Cursor cqPay = sql_db.rawQuery(q_pay,null);
                        if(cqPay.getCount()>0){
                            cqPay.moveToFirst();
                            //   do{
                            billPaybleAmount = Float.parseFloat(cqPay.getString(cqPay.getColumnIndex("totPayble")));
                            //   }while (cqPay.moveToNext());

                        }

                        counterbillingBean = new CounterbillingBean();
                        counterbillingBean.setDateTime(dateTime);
                        counterbillingBean.setBillPaybleAmount(String.format("%.2f",billPaybleAmount));
                        counterbillingBean.setCustName(CustomerName);
                        counterbillingBean.setTimestamp(dateSTamp);

                    }

                    counterbillingBean.setBillNo(billNo);
                    nonprintedBillList.add(counterbillingBean);

            }while (c.moveToNext());

        }else {

        }

        Collections.sort(nonprintedBillList, new Comparator<CounterbillingBean>() {
            @Override
            public int compare(CounterbillingBean o1, CounterbillingBean o2) {
                long ltime = o1.getTimestamp();
                long rtime = o2.getTimestamp();
                return Float.compare(rtime, ltime);
            }
        });

        billAdapter = new NonPrintedBillListAdapter(parent, nonprintedBillList);
        list_non_printed_bills.setAdapter(billAdapter);
        //billAdapter.notifyDataSetChanged();
    }

    public class GetBillHistory extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_orderHistory = "";
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  mprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            String url_orderHistory = AnyMartData.MAIN_URL + AnyMartData.api_getCounterBillHistory;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_orderHistory, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                //res = res.substring(1, res.length() - 1);
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                resp_orderHistory = responseString.replaceAll("\\\\", "");
                System.out.println("rsep = " + resp_orderHistory);

            } catch (NullPointerException e) {
                resp_orderHistory = "empty";
                e.printStackTrace();
            } catch (Exception e) {
                resp_orderHistory = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (resp_orderHistory.equalsIgnoreCase("Session Expired")) {

            }else if (resp_orderHistory.equalsIgnoreCase("empty")) {
                Toast.makeText(parent, "No bills available", Toast.LENGTH_LONG).show();
            } else if (resp_orderHistory.equalsIgnoreCase("[]")) {
                Toast.makeText(parent, "No bills available", Toast.LENGTH_LONG).show();

                try{
                  /*  listSO.clear();
                    soAdapter = new SOApprListAdapter(NonPrintedBill_Fragment.this,listSO);
                    listso.setAdapter(soAdapter);
                    soAdapter.notifyDataSetChanged();*/
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (resp_orderHistory.equalsIgnoreCase("error")) {

                Toast.makeText(parent, "The server is taking too long to respond OR " +
                        "something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            } else {

                json = resp_orderHistory;
                parseJson_billData(json);
            }
        }
    }

    protected void parseJson_billData(String json) {
        tcf.clearTable(parent, DatabaseHandlers.TABLE_BILL_CB);
       // listSO.clear();

        try {
            JSONArray jsonArray = new JSONArray(json);

            String rate = "", MRP="", Qty="", subtotal="",TotalDiscount="",NetAmt="",Received="",balamt="",
                    CustName="",custMob="", paybleamt = "", custgstn = "",gstn = "", TaxClassMasterId ="", TaxClassDesc="",
                    discPc="",addedDt="",BillNo="";

            float discount_on_NetAmt = 0.0f, final_discountedTotal = 0.0f, final_taxinRupsTotal=0.0f, CGST_TOTAL = 0.0f,
                    SGST_TOTAL = 0.0f, frate = 0.0f, mrp=0.0f, disc_per =0,lineamt = 0.0f, discamt = 0.0f,
            discountedLineAmt=0f;

            String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
                    sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
            float cgst = 0.0F, taxInPer=0f;
            String CGST = "0", SGST = "0", IGST = "0";

            for (int i = 0; i < jsonArray.length(); i++) {

                OrderHistoryBean historybean = new OrderHistoryBean();
                // String a = jsonArray.getJSONObject(i).getString("status");
                BillNo = jsonArray.getJSONObject(i).getString("BillNo");
                CustName = jsonArray.getJSONObject(i).getString("CustName");
                String itemdesc = jsonArray.getJSONObject(i).getString("itemdesc");
                String itemcode = getItemCode(jsonArray.getJSONObject(i).getString("ItemPlantId"));
                MRP = jsonArray.getJSONObject(i).getString("MRP");
                Qty = jsonArray.getJSONObject(i).getString("Qty");
                TaxClassMasterId = jsonArray.getJSONObject(i).getString("TaxClassMasterId");
                TaxClassDesc = getTaxClass(TaxClassMasterId);
                discPc = jsonArray.getJSONObject(i).getString("discPc");
                disc_per = Float.parseFloat(discPc);
                //discountedLineAmt = Float.parseFloat(jsonArray.getJSONObject(i).getString("LineTotal"));
                discount_on_NetAmt = Float.parseFloat(roundOff(Float.parseFloat(jsonArray.getJSONObject(i).getString("DiscAmt"))));
                TotalDiscount = String.valueOf(discount_on_NetAmt);
                addedDt = jsonArray.getJSONObject(i).getString("addedDt");

                mrp = Float.parseFloat(MRP);

                if(isInclusiveTax){
                    taxInPer = checkTaxType(TaxClassDesc);  //get tax summation here

                    mrp = (mrp/((100 + taxInPer)/100));
                    frate = mrp;
                }else {

                    mrp = mrp;
                    frate = mrp;
                }

                lineamt = mrp * Float.parseFloat(String.valueOf(Qty));
                discamt = (lineamt * disc_per)/100;

                discountedLineAmt = lineamt - discamt;

                String[] taxes = checkTaxTyep(TaxClassDesc);

                if (taxes[0].equals("")) {
                    igstType = "";
                    igstVal = "0.0";

                    if (!(taxes[2].equals(""))) {
                        cgstType = taxes[2];
                        cgstVal = taxes[3];
                        if (!cgstVal.contains(".")) {
                            cgstVal = cgstVal + ".0";
                        }
                    } else {
                        cgstType = "";
                        cgstVal = "0.0";
                    }

                    if (!(taxes[4].equals(""))) {
                        sgstType = taxes[4];
                        sgstVal = taxes[5];

                        if (!sgstVal.contains(".")) {
                            sgstVal = sgstVal + ".0";
                        }
                    } else {
                        sgstType = "";
                        sgstVal = "0.0";
                    }

                    if (!(taxes[6].equals(""))) {
                        ugstType = taxes[6];
                        ugstVal = taxes[7];
                        if (!ugstVal.contains(".")) {
                            ugstVal = ugstVal + ".0";
                        }
                    } else {
                        ugstType = "";
                        ugstVal = "0.0";
                    }

                    if (!(taxes[8].equals(""))) {
                        vatType = taxes[8];
                        vatVal = taxes[9];
                        if (!vatVal.contains(".")) {
                            vatVal = vatVal + ".0";
                        }
                    } else {
                        vatType = "";
                        vatVal = "0.0";
                    }

                } else {
                    igstType = taxes[0];
                    igstVal = taxes[1];

                    if (!igstVal.contains(".")) {
                        igstVal = cgstVal + ".0";
                    }
                }

                float lineIncltax = discountedLineAmt+(discountedLineAmt * (taxInPer / 100));
                float taxAmnt = discountedLineAmt * (taxInPer / 100);
                subtotal = String.valueOf(lineIncltax);

                float finalBill = discountedLineAmt + taxAmnt;

                NetAmt = roundOff(finalBill);
                paybleamt = NetAmt;

                float _cgst = 0.0F, _CGST = 0.0F, _SGST = 0.0F;

                _cgst = taxAmnt;
                _CGST = Float.parseFloat(String.format("%.02f", _cgst / 2));
                _SGST = Float.parseFloat(String.format("%.02f", _cgst / 2));

                tcf.addBill_two(BillNo/*String.valueOf(BILLNO +1)*/,
                        itemdesc/*cbillList.get(i).getItemDesc()*/,
                        itemcode/*cbillList.get(i).getItemCode()*/,
                        String.valueOf(frate)/*String.valueOf(cbillList.get(i).getRate())*/,
                        MRP/*String.valueOf(cbillList.get(i).getMRP())*/,
                        Qty/*String.valueOf(cbillList.get(i).getQty())*/,
                        TaxClassDesc/*cbillList.get(i).getTaxclass()*/,
                        cgstVal, sgstVal,
                        String.valueOf(discamt)/*String.valueOf(cbillList.get(i).getDiscamt())*/,
                        disc_per/*cbillList.get(i).getDiscount()*/,
                        lineIncltax/*Float.valueOf(cbillList.get(i).getTotAmt_incltax_lineamt())*/,
                        subtotal/*txtsubtotal.getText().toString().trim()*/,
                        TotalDiscount/*edttxtTotalDiscount.getText().toString().trim()*/,
                        "0",
                        NetAmt/*txtNetAmt.getText().toString().trim()*/,
                        Received/*edttxtReceived.getText().toString().trim()*/,
                        balamt/*txtbalamt.getText().toString().trim()*/,
                        CustName, custMob,
                        String.valueOf(discountedLineAmt),
                        String.valueOf(taxAmnt),
                        String.valueOf(_CGST),
                        String.valueOf(_SGST), "",
                        paybleamt/*txtpaybleamt.getText().toString().trim()*/,
                        "Yes", "No", Convertdate(addedDt)/*getCurrentDate()*/,
                        custgstn/*edtcustgstn.getText().toString().trim()*/,
                        gstn/*edtgstn.getText().toString().trim()*/);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getPrintedBillsList();
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public String getItemCode(String ItemPlantId){
        String ItemCode="";
        String query = "Select ItemCode from "+ databaseHelper.TABLE_ADD_ITEMS_COUNTERBILL +
                " WHERE ItemPlantId='"+ItemPlantId/*ItemCode*/+"'";
        Cursor c = sql_db.rawQuery(query,null);

        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                ItemCode = c.getString(c.getColumnIndex("ItemCode"));
            }while (c.moveToNext());
        }else {
            ItemCode = "";
        }

        return ItemCode;
    }

    public String getTaxClass(String TaxClsId){
        String TaxClassDesc="";
        String query = "Select TaxClassDesc from "+ databaseHelper.TABLE_TAXCLASS +
                " WHERE TaxClassMasterId='"+TaxClsId/*ItemCode*/+"'";
        Cursor c = sql_db.rawQuery(query,null);

        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                TaxClassDesc = c.getString(c.getColumnIndex("TaxClassDesc"));
            }while (c.moveToNext());
        }else {
            TaxClassDesc = "SGST 2.5% + CGST 2.5% INPUT";
        }

        return TaxClassDesc;
    }

    public float checkTaxType(String taxClass){
        String igstType = "" , sgstType = "" , cgstType = "", ugstType = "", sgstVal = "", cgstVal = "", igstVal = "", ugstVal = "",
                vatVal = "", vatType = "";
        float pctgvalTocalc = 0.0F;

        if(taxClass.contains("IGST")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            // String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    igstVal = pcgval[0];

                    float IGST = Float.parseFloat(igstVal);

                    float i1 = IGST / 2;

                    cgstType = "CGST";
                    cgstVal = String.valueOf(i1);

                    sgstType = "SGST";
                    sgstVal = String.valueOf(i1);
                }
            }

            pctgvalTocalc = Float.parseFloat(igstVal);

        }else if(taxClass.contains("CGST") && taxClass.contains("SGST") ){

            String[] taxtypes = taxClass.split("\\+");         // types[0] = SGST 9% , types[1] =CGST 9% OUTPUT

            System.out.println(taxtypes[0]);       //CGST 9 % OUTPUT
            // System.out.println(types[1]);       //SGST 9 % OUTPUT

            for(int i = 0; i < taxtypes.length; i++){
                String TYPE = "";
                String[] data;
                if(taxtypes[i].contains(".")){
                    data = taxtypes[i].split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
                }else {
                    data = taxtypes[i].split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
                }

                if(taxtypes[i].contains("CGST")){
                    cgstType = "CGST";
                    TYPE = cgstType;

                }else if(taxtypes[i].contains("SGST")){
                    sgstType = "SGST";
                    TYPE = sgstType;
                }

                for(int j = 0 ; j < data.length ; j++){
                    String[] pcgval = new String[0];

                    String taxTYPE = data[j];
                    System.out.println(taxTYPE);        //CGST, 9 % OUTPUT

                    if(data[j].contains("%")){
                        pcgval = data[j].split("%");
                        System.out.println(pcgval[0]);

                        if(TYPE.equalsIgnoreCase("CGST")){
                            cgstVal = String.valueOf(pcgval[0]);
                        }else if(TYPE.equalsIgnoreCase("SGST")){
                            sgstVal = String.valueOf(pcgval[0]);
                        }
                    }
                }
            }

            pctgvalTocalc = Float.parseFloat(cgstVal) + Float.parseFloat(sgstVal);

        }else if(taxClass.contains("SGST") || taxClass.contains("CGST") || taxClass.contains("UGST") || taxClass.contains("VAT")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            //String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    if(taxClass.contains("SGST")){
                        sgstVal = pcgval[0];
                        sgstType = "SGST";
                        pctgvalTocalc = Float.parseFloat(sgstVal);
                    }else if(taxClass.contains("CGST")){
                        cgstVal = pcgval[0];
                        cgstType = "CGST";
                        pctgvalTocalc = Float.parseFloat(cgstVal);
                    }else if(taxClass.contains("UGST")){
                        ugstVal = pcgval[0];
                        ugstType = "UGST";
                        pctgvalTocalc = Float.parseFloat(ugstVal);
                    }else if(taxClass.contains("VAT")){
                        vatVal = pcgval[0];
                        vatType = "VAT";
                        pctgvalTocalc = Float.parseFloat(vatVal);
                    }
                }
            }

        }else if(taxClass.contains("SGCT/CGST/IGST INCLUSIV")) {

        }

        return pctgvalTocalc;
    }

    public String[] checkTaxTyep(String taxClass) {
        String igstType = "", sgstType = "", cgstType = "",ugstType = "", vatType = "",
                sgstVal = "0", cgstVal = "0", igstVal = "0",ugstVal = "0", vatVal = "0";

        if (taxClass.contains("IGST")) {
            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }
            //String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    igstVal = pcgval[0];

                    float IGST = Float.parseFloat(igstVal);

                    float i1 = IGST / 2;

                    cgstType = "CGST";
                    cgstVal = String.valueOf(i1);

                    sgstType = "SGST";
                    sgstVal = String.valueOf(i1);

                }

            }

        } else if (taxClass.contains("CGST") && taxClass.contains("SGST")) {

            String[] taxtypes = taxClass.split("\\+");         // types[0] = SGST 9% , types[1] =CGST 9% OUTPUT

            System.out.println(taxtypes[0]);       //CGST 9 % OUTPUT
            // System.out.println(types[1]);       //SGST 9 % OUTPUT

            for (int i = 0; i < taxtypes.length; i++) {
                String TYPE = "";

                String[] data;
                if(taxClass.contains(".")){
                    data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
                }else {
                    data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
                }

                //   String[] data = taxtypes[i].split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT

                if (taxtypes[i].contains("CGST")) {
                    cgstType = "CGST";
                    TYPE = cgstType;

                } else if (taxtypes[i].contains("SGST")) {
                    sgstType = "SGST";
                    TYPE = sgstType;
                }

                for (int j = 0; j < data.length; j++) {
                    String[] pcgval = new String[0];

                    String taxTYPE = data[j];
                    System.out.println(taxTYPE);        //CGST, 9 % OUTPUT

                    if (data[j].contains("%")) {
                        pcgval = data[j].split("%");
                        System.out.println(pcgval[0]);

                        if (TYPE.equalsIgnoreCase("CGST")) {
                            cgstVal = String.valueOf(pcgval[0]);
                        } else if (TYPE.equalsIgnoreCase("SGST")) {
                            sgstVal = String.valueOf(pcgval[0]);
                        }
                    }
                }
            }

        }else if(taxClass.contains("SGST") || taxClass.contains("CGST") || taxClass.contains("UGST") || taxClass.contains("VAT")){

            String[] data;
            if(taxClass.contains(".")){
                data = taxClass.split(" ");       //data[0] = CGST data[1] = 9% OUTPUT
            }else {
                data = taxClass.split("(?<=\\D)(?=\\d)");       //data[0] = CGST data[1] = 9% OUTPUT
            }

            //String[] data = taxClass.split("(?<=\\D)(?=\\d)");
            int sizeData = data.length;

            for (int i = 0; i < sizeData; i++) {
                String datanew = data[i];

                if (data[i].contains("%")) {
                    String[] pcgval = data[i].split("%");
                    System.out.println(pcgval[0]);

                    if(taxClass.contains("SGST")){
                        sgstVal = pcgval[0];
                        sgstType = "SGST";
                    }else if(taxClass.contains("CGST")){
                        cgstVal = pcgval[0];
                        cgstType = "CGST";
                    }else if(taxClass.contains("UGST")){
                        ugstVal = pcgval[0];
                        ugstType = "UGST";
                    }else if(taxClass.contains("VAT")){
                        vatVal = pcgval[0];
                        vatType = "VAT";
                    }
                }
            }

        }else if(taxClass.contains("SGCT/CGST/IGST INCLUSIV")) {

        }

        String[] taxes = {igstType, igstVal, cgstType, cgstVal, sgstType, sgstVal, ugstType, ugstVal, vatType, vatVal};

        return taxes;
    }

    public static String roundOff(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        String val = String.valueOf((n - c) % 2 == 0 ? (int) f : c);
        return val+".00";
    }

    public String Convertdate(String date){
        String DateToStr="";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat Format = new SimpleDateFormat("EEE dd MMM yyyy");//Feb 23 2016 12:16PM
//SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("dd MMM yyyy");
        Date d1 = null;
        try {
            d1 = format.parse(date.split("T")[0]);
            DateToStr = toFormat.format(d1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateToStr;
    }

    public long convertToTimestamp(String str_date){
        long _date = 0;
        DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).parse(str_date);
            _date = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return _date;
    }

}
