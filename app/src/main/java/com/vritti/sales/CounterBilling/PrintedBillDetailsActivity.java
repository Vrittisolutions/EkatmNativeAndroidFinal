package com.vritti.sales.CounterBilling;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.PrintBillDetailsAdapter;
import com.vritti.sales.beans.BillNoClass;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.vwb.CommonClass.AppCommon;

import java.util.ArrayList;

public class PrintedBillDetailsActivity extends AppCompatActivity {
    private Context parent;

    Button btn_print;
    ListView list_printedBills;
    TextView txtTotalincltax, txtbaseamount, txtcgstamt, txtsgstamt, txtNetAmt, txtTotalDiscount, txtpaybleamt,txtbillno,
            txtcustname,txtdate, txtcustgstn,txtcmpnygstn, txtsavers,txtroundoff,txttotal,titlecgst,titlesgst;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", custMOB = "", custname = "", date = "",
            custGSTN = "", cmpnyGSTN = "", custMob = "";

    String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
            sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
    float cgst = 0.0F;
    String CGST = "0", SGST = "0", IGST = "0", _taxClass = "";
    boolean isInclusiveTax = true;

    Toolbar toolbar;

    String billno, billDate, intentFrom;
    ArrayList<CounterbillingBean> listdetails;
    ArrayList<CounterbillingBean> cbillList;

    PrintBillDetailsAdapter billadapter;
    BillNoClass billNoClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_printed_bill_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Counter Billing");

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
        parent = PrintedBillDetailsActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Bill Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        list_printedBills = (ListView)findViewById(R.id.list_bill);
        txtTotalincltax = (TextView)findViewById(R.id.txtTotalincltax);
        txtbaseamount = (TextView)findViewById(R.id.txtbaseamount);
        txtcgstamt = (TextView)findViewById(R.id.txtcgstamt);
        txtsgstamt = (TextView)findViewById(R.id.txtsgstamt);
        txtNetAmt = (TextView)findViewById(R.id.txtNetAmt);
        txtTotalDiscount = (TextView)findViewById(R.id.txtTotalDiscount);
        txtpaybleamt = (TextView)findViewById(R.id.txtpaybleamt);
        btn_print = (Button)findViewById(R.id.btn_print);
        btn_print.setVisibility(View.GONE);
        txtbillno = (TextView)findViewById(R.id.txtbillno);
        txtcustname = (TextView)findViewById(R.id.txtcustname);
        txtdate = (TextView)findViewById(R.id.txtdate);
        txtcustgstn = (TextView)findViewById(R.id.txtcustgstn);
        txtcmpnygstn = (TextView)findViewById(R.id.txtgstn);
        txtroundoff = (TextView)findViewById(R.id.txtroundoff);
        txttotal = (TextView)findViewById(R.id.txttotal);
        txtsavers = (TextView)findViewById(R.id.txtsavers);
        titlecgst = (TextView)findViewById(R.id.titlecgst);
        titlesgst = (TextView)findViewById(R.id.titlesgst);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(PrintedBillDetailsActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        //FirmMobile = ut.getValue(this, WebUrlClass.GET_MOBILE_KEY, settingKey).trim();

        Intent intent = getIntent();
        intentFrom = intent.getStringExtra("IntentFrom");
        billno = intent.getStringExtra("BillNo");
        billDate = intent.getStringExtra("BillDate");

        txtbillno.setText(billno);

        if(intentFrom.equalsIgnoreCase("NonPrintedBills")){
            btn_print.setVisibility(View.VISIBLE);
        }else if(intentFrom.equalsIgnoreCase("PrintedBills")){
            btn_print.setVisibility(View.GONE);
        }

        listdetails = new ArrayList<CounterbillingBean>();
        cbillList = new ArrayList<CounterbillingBean>();
        billNoClass = new BillNoClass();

        if(isInclusiveTax){
            _taxClass = "SGST 2.5% + CGST 2.5% INPUT";

        }else {
            _taxClass = "";
        }

        String[] taxes = checkTaxTyep(_taxClass);

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
                igstVal = igstVal + ".0";
            }
        }
        titlecgst.setText("CGST "+cgstVal+" %:");
        titlesgst.setText("SGST "+sgstVal+" %:");

    }

    public void setListeners(){

        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getItmsListDataForBilling();
            }
        });
    }

    public void getDataFromDatabase(){
        listdetails.clear();

        String itemdesc,rate,mrp,qty, itemdiscamt, cgstLine, sgstLine, totalLineAmt, netAmount, itemCode, taxclass;
        Float lineamt = 0.0F, discount= 0.0F, discountedLineAmt = 0.0F;
        float baseAmount = 0.0F, totalCGST =0.0F, totalSGST = 0.0F, discAmount=0.0F, TaxinRupsTotalBill = 0.0F,total = 0.0F,roundOff = 0.0F,
            totMRP = 0.0F, _mrp = 0.0F, _qty =0.0F, _youSave = 0.0F, _rndOffDiff = 0.0F,payableAmount=0.0f,total_inclTax=0.0f;

        String qry = "Select CustomerName, CustMobno, Date, CustGSTN, CompanyGSTN FROM "+dbhandler.TABLE_BILL_CB + " WHERE BillPrintNo='"+billno+"'";
        Cursor cust = sql_db.rawQuery(qry,null);
        if(cust.getCount() > 0){
            cust.moveToFirst();
            do{
                custname = cust.getString(cust.getColumnIndex("CustomerName"));
                custMOB = cust.getString(cust.getColumnIndex("CustMobno"));
                date = cust.getString(cust.getColumnIndex("Date"));
                custGSTN = cust.getString(cust.getColumnIndex("CustGSTN"));
                cmpnyGSTN = cust.getString(cust.getColumnIndex("CompanyGSTN"));
            }while (cust.moveToNext());

            txtcustname.setText(custname);
            txtdate.setText(date);
            txtcustgstn.setText(custGSTN);
            txtcmpnygstn.setText(cmpnyGSTN);

        }else {

        }

        String query_ = "SELECT DISTINCT FinalTotalBill, TotalWithDiscountBill,TaxinRupsTotalBill, CGSTTotal, SGSTTotal,DiscOnNetAmtRS, " +
                "PayableAmt FROM "+ dbhandler.TABLE_BILL_CB + " WHERE BillPrintNo='"+billno+"'";
        Cursor c_ = sql_db.rawQuery(query_,null);
        if(c_.getCount() > 0){
            c_.moveToFirst();
            do{
                total_inclTax += Float.parseFloat(c_.getString(c_.getColumnIndex("FinalTotalBill")));
                netAmount = c_.getString(c_.getColumnIndex("FinalTotalBill"));
                baseAmount += Float.parseFloat(c_.getString(c_.getColumnIndex("TotalWithDiscountBill")));
                TaxinRupsTotalBill = Float.parseFloat(c_.getString(c_.getColumnIndex("TaxinRupsTotalBill")));
                discAmount = Float.parseFloat(c_.getString(c_.getColumnIndex("DiscOnNetAmtRS")));
                payableAmount += Float.parseFloat(c_.getString(c_.getColumnIndex("PayableAmt")));

                //totalCGST = TaxinRupsTotalBill/2;
                totalCGST += Float.parseFloat(String.format("%.2f",TaxinRupsTotalBill/2));
                //totalSGST = TaxinRupsTotalBill/2;
                totalSGST += Float.parseFloat(String.format("%.2f",TaxinRupsTotalBill/2));

            }while (c_.moveToNext());

           /* //totalCGST = TaxinRupsTotalBill/2;
            totalCGST += Float.parseFloat(String.format("%.2f",TaxinRupsTotalBill/2));
            //totalSGST = TaxinRupsTotalBill/2;
            totalSGST += Float.parseFloat(String.format("%.2f",TaxinRupsTotalBill/2));*/
            total = baseAmount + totalCGST + totalSGST;
            txttotal.setText(String.format("%.2f",total));

            txtTotalincltax.setText(String.format("%.02f",total_inclTax));
            //txtNetAmt.setText(total_inclTax);
            txtNetAmt.setText(roundOff(total_inclTax));
            txtbaseamount.setText(String.format("%.02f",baseAmount));
            txtcgstamt.setText(String.format("%.02f",totalCGST));
            txtsgstamt.setText(String.format("%.02f",totalSGST));
            txtTotalDiscount.setText(String.format("%.02f",discAmount));
            txtpaybleamt.setText(roundOff(payableAmount));

            _rndOffDiff = Float.parseFloat(roundOff(total_inclTax)) - total;
            if(String.format("%.2f",_rndOffDiff).equalsIgnoreCase("-0.00")){
                txtroundoff.setText("0.00");
            }else {
                txtroundoff.setText(String.format("%.2f",_rndOffDiff));
            }


        }else {

        }

        /* lineamt, taxinrups, disctotal*/
        String query = "SELECT Itemid, ItemName, Rate,MRP, Qty, cgstLine,sgstLine, DiscLineAmt,ItemDiscount, Amount,TaxClass, * FROM "
                + dbhandler.TABLE_BILL_CB + " WHERE BillPrintNo='"+billno+"'";
        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                itemCode =  c.getString(c.getColumnIndex("Itemid"));
                itemdesc = c.getString(c.getColumnIndex("ItemName"));
                qty = c.getString(c.getColumnIndex("Qty"));
                rate = c.getString(c.getColumnIndex("Rate"));
                mrp = c.getString(c.getColumnIndex("MRP"));

                _mrp = Float.parseFloat(mrp);
                _qty = Float.parseFloat(qty);

                totMRP = totMRP + (_mrp * _qty);

                float L_Amt = Float.parseFloat(qty) * Float.parseFloat(rate);
                lineamt = L_Amt;
                discount = Float.parseFloat(c.getString(c.getColumnIndex("ItemDiscount")));
                taxclass = c.getString(c.getColumnIndex("TaxClass"));

                totalLineAmt = c.getString(c.getColumnIndex("Amount"));
                itemdiscamt = c.getString(c.getColumnIndex("DiscLineAmt"));
                discountedLineAmt = lineamt - Float.parseFloat(itemdiscamt);            //getDicountedTotal
                cgstLine = c.getString(c.getColumnIndex("cgstLine"));
                sgstLine = c.getString(c.getColumnIndex("sgstLine"));

                //getTax_inRups
                Float tottax = Float.parseFloat(cgstLine) + Float.parseFloat(sgstLine);
                Float taxinRS =  discountedLineAmt * tottax/100;
                Float tot_incltax = discountedLineAmt + taxinRS;

                CounterbillingBean cbean = new CounterbillingBean();
                cbean.setItemCode(itemCode);
                cbean.setItemDesc(itemdesc);
                cbean.setQty(qty);
                cbean.setRate(Float.parseFloat(rate));
                cbean.setMRP(Float.parseFloat(mrp));
                cbean.setLineamt(lineamt);
                cbean.setDiscount(discount);
                cbean.setTaxclass(taxclass);
                cbean.setTax_inRups(String.valueOf(taxinRS));
                cbean.setTotAmt_incltax_lineamt(tot_incltax);
                cbean.setTotAmt_incltax_lineamt(Float.parseFloat(totalLineAmt));
                cbean.setDiscamt(Float.parseFloat(itemdiscamt));
                cbean.setDicountedTotal(discountedLineAmt);
                cbean.setCgstLine(cgstLine);
                cbean.setSgstLine(sgstLine);
                cbean.setCustGSTN(custGSTN);
                cbean.setCmpnyGSTN(cmpnyGSTN);
                cbean.setCustName(custname);
                cbean.setMobileNo(custMOB);
                listdetails.add(cbean);

            }while (c.moveToNext());

        }else {

        }

        _youSave = totMRP - Float.parseFloat(txtpaybleamt.getText().toString());
        txtsavers.setText(String.format("%.2f",_youSave));

        if(listdetails.isEmpty()){
            Toast.makeText(parent, "No pending bills to show",Toast.LENGTH_SHORT).show();
        }else {
            billadapter = new PrintBillDetailsAdapter(this, listdetails);
            list_printedBills.setAdapter(billadapter);
        }

    }

    public void getItmsListDataForBilling() {
        cbillList.clear();

        for(int i=0; i< listdetails.size(); i++){

            CounterbillingBean cbean = new CounterbillingBean();
            cbean.setItemCode(listdetails.get(i).getItemCode());
            cbean.setItemDesc(listdetails.get(i).getItemDesc());
            cbean.setQty(listdetails.get(i).getQty());
            cbean.setRate(listdetails.get(i).getRate());
            cbean.setMRP(listdetails.get(i).getMRP());
            cbean.setLineamt(listdetails.get(i).getLineamt());
            cbean.setDiscount(listdetails.get(i).getDiscount());
            cbean.setTaxclass(listdetails.get(i).getTaxclass());
            cbean.setTax_inRups(listdetails.get(i).getTax_inRups());
            //cbean.setDiscinrupees(listdetails.get(i).getDiscamt());
            cbean.setTotAmt_incltax_lineamt(listdetails.get(i).getTotAmt_incltax_lineamt());
            cbean.setDiscamt(listdetails.get(i).getDiscamt());
            cbean.setDicountedTotal(listdetails.get(i).getDicountedTotal());
            cbean.setCustName(txtcustname.getText().toString().trim());
            cbean.setMobileNo(custMOB);
            cbean.setCustGSTN(txtcustgstn.getText().toString().trim());
            cbean.setCmpnyGSTN(txtcmpnygstn.getText().toString().trim());
            cbillList.add(cbean);
        }

        billNoClass.setBillNo(billno);
        billNoClass.setCbillList(cbillList);
        String billingObj = new Gson().toJson(billNoClass);
        AppCommon.getInstance(this).setBillingObject(billingObj);

        Intent intent = new Intent(this, ItemListCB.class);
        intent.putExtra("intentFrom", "PrintedBillDetailsActivity");
        startActivity(intent);
        finish();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        super.finish();
    }
}
