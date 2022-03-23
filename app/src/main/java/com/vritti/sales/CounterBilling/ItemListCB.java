package com.vritti.sales.CounterBilling;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googlecode.mp4parser.authoring.Edit;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.activity.ActivityLogIn;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.sales.adapters.AdapterCBillingList;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.BillNoClass;
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.zj.btsdk.BluetoothService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION;

public class ItemListCB extends AppCompatActivity {
    private Context parent;
    public static ArrayList<AllCatSubcatItems> arrayList;
    boolean isInternetPresent;
    private AllCatSubcatItems bean;
    private String json;
    private GridView listview;
    LinearLayout llscroll;

    Connectiondetector cd;
    EditText edRate;
    Button SaveAndPrint, savebill;
    ImageButton whatsapp;
    MenuItem m, refresh;
    ArrayAdapter<AllCatSubcatItems> myAdapter;
    public String restoredText;

    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;
    public static String Mobilenumber;
    String Subcatid, userId;
    SharedPreferences sharedpreferencesUserId;
    CustomAutoCompleteView myAutoComplete;
    MyCartBean myCartBean;
    ArrayList<MyCartBean> myCartBeanArrayList;
    private LinearLayout containerLayout_one;
    ImageButton btn_ok, btn_cancel;
    EditText edAmt, edQnty, edDiscount, edttxtTotalDiscount, edttxtRemaining, edttxtReceived, edtcustgstn, edtgstn,edtcustname,edtcustMob;
    Spinner spinnerunit;
    TextView txtTotal,txtFinalTotal, txtbaseamount, txtcgstamt, txtsgstamt,txtroundoff,txtyousave,txttotal,titlecgst,titlesgst;
    String custName, custMob, FirmName, FirmMobile;
    public String p_id;
    String txtBill = null;
    float amt = 0, FinalTotal = 0, dis_amt = 0, remaining = 0, TotInclTax = 0, DiscOnNetAmt = 0;
    Dialog dialog;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private boolean deviceConnected = false;
    public String xml1, xml2;
    private StringBuilder sb;
    boolean flag_selection = false;
    boolean flag_Amount = false;
    boolean flag_Qnty = false;
    String mrp = "", unit = "";
    float qnty = 0;
    float subtotal_dialog = 0;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;
    Button btnAdd;
    ImageView imgadditem;
    ListView listdtls;
    ArrayList<CounterbillingBean> cbillList;
    ArrayList<String> addressLines;
    BillNoClass billNoClass;

    TextView txtsubtotal, Totalincltax, txtNetAmt, txtbalamt, txtpaybleamt;
    EditText edttaxtotal;
    Switch swtchdisc;
    AdapterCBillingList cBillAdapter;
    float final_subtotal = 0.0F, final_discountedTotal = 0.0F, final_taxinRupsTotal = 0.0F, discount_on_NetAmt = 0.0F, _total = 0.0F;
    int BILLNO  = 0;
    float CGST_TOTAL = 0.0F, SGST_TOTAL = 0.0F, IGST_TOTAL = 0.0F, _cgstPrint = 0F, _sgst = 0F;
    String Cid = "", Cname = "", factoryAddress = "", regAddress = "", _taxClass = "", intentFrom = "",FSSAI="FSSAI : Lic.No.21519196000705";

    String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
            sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
    float cgst = 0.0F;
    String CGST = "0", SGST = "0", IGST = "0";

    public static final int REQUEST_ENABLE_BT = 4;
    public static final int REQUEST_CONNECT_DEVICE = 6;
    private static final int REQUEST_EXTERNAL_STORAGe = 10;
    private static String[] permissionstorage = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE};
    boolean isInclusiveTax = true;

    String ShipmentArr="",MainArr1="",removedarr="",Chargedt="",BoxData="",ChalanArray="";
    String SoScheduleId="",OrdeTypeId="",CommittedDt="",ItemMasterId="",ItemCode="",ItemDesc="",BatchNo="",LocationMasterId="",
            LocationDesc="",WarehouseMasterId="",WarehouseDescription="",SONo="",SalesQty="",SalesRate="",TaxClassMasterId="",
            TaxClassCode="",TaxClassDesc="",FLineAmt="",LineAmt="",FLineTaxes="",FLineTotal="",LineTaxes="",LineTotal="",DiscPC="",
            DiscAmntFC="",DiscAmt="",Qty="",Rate="",CustOrderPONo="",SOHeaderId="",ChargeAmntFC="",ChargeAmnt="0",BasicTaxAmntFC="",
            ChargeTaxAmntFC="",ChargeTaxAmnt="",TotalAmnt="",TotalTaxAmnt="",TotalTaxAmntFC="",TotalGrossAmnt="",TotalGrossAmntFC="",
            ItmClsDesc="",MarksNo="",ContDimensn="",ContGrossWt="",ContNetWt="",NoKindPkgs="",BoxNo="",SeqNo="",LicenseNo="",Remark="OK",
            ExemptMaterialAndNetWt="",QtyPerCont="0",NoofCont="0",SalesDtlId="",ItemClass="",TechDesc="",Transporter="",NOOfBoxes="",
            QtyPerBox="",TotalQty="",SalesHeaderId="",OrderTypeId="",ShipmentDt="",CustomerId="",DispatchNo="",DeliveryMode="",
            FreightPayMode="",ConsigneeName="",ShipToMasterId="",BillToId="",Dele_Transporter="",dispatchMode="",BasicAMNT="0",
            TaxAMT="0",GrossAMT="",DiscAMT="",DiscountAMNTFC="",ChargeTaxAmount="",LineCount="",NetWt="",GrossWt="",Shipper="",ApproverId="",
            remark="",SOHeaderid="",RoundOff="",RefDt="",SoScheduleid="",ChalanNo="1",ItemPlantId="",IssueQty="0",GrndtlId="",StockId="";

    JSONObject jMain = null;
    String finalData = "";
    boolean isTakePrint = false;

    MediaPlayer mediaPlayer;
    private String paid_amount="";
    private String Invoicenumber="",AppCode="";
    private AlertDialog b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_item_list_cb_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Counter Billing");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       // hasManageExternalStoragePermission();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGe); //permission request code is just an int
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGe); //permisison request code is just an int
        }


        initialize();

        sb = new StringBuilder();
        cd = new Connectiondetector(ItemListCB.this);
        isInternetPresent = cd.isConnectingToInternet();

        //   databaseHandler = new DatabaseHandler(ItemListCB.this);
        sharedpreferencesUserId = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userid", null);
        restoredText = sharedpreferences.getString("Mobileno", null);
        AppCode = sharedpreferences.getString(WebUrlClass.MyPREFERENCES_IS_APPCODE, null);

        final Intent intent = getIntent();
        intentFrom = intent.getStringExtra("intentFrom");
        //custName = ""/*intent.getStringExtra("CustomerName")*/;
        //custMob = ""/*intent.getStringExtra("CustomerMobno")*/;
        //custname.setText(custName);
        //custMobno.setText(custMob);

        if (isInclusiveTax) {
            _taxClass = "SGST 2.5% + CGST 2.5% INPUT";

        } else {
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

        if (cbillList.size() > 0) {
            titlecgst.setText("CGST " + cgstVal + " %:");
            titlesgst.setText("SGST " + sgstVal + " %:");
        }

        if (restoredText != null) {
            Mobilenumber = restoredText;
            //   AnyMartData.MOBILE = restoredText;
        }

        //Add items in list for billing. and display in listview. add bill in local table
        //getItmsListDataForBilling();
        if (AppCommon.getInstance(this).getBillingObject().equals("")) {

        } else {
            billNoClass = new Gson().fromJson(AppCommon.getInstance(this).getBillingObject(), BillNoClass.class);
            cbillList = billNoClass.getCbillList();
            cBillAdapter = new AdapterCBillingList(this, billNoClass.getCbillList());
            listdtls.setAdapter(cBillAdapter);
            setData(cbillList);
            setListViewHeightBasedOnChildren(listdtls,1);
            /*if(intentFrom.contains("PrintedBillDetailsActivity")){
                billNoClass = new Gson().fromJson(AppCommon.getInstance(this).getBillingObject(), BillNoClass.class);
                cbillList = billNoClass.getCbillList();
                cBillAdapter = new AdapterCBillingList(this, billNoClass.getCbillList());
                listdtls.setAdapter(cBillAdapter);
                setData(cbillList);
            }else {

            }*/
        }

        String getBillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
        Cursor c = sql_db.rawQuery(getBillNo, null);
        if (c.getCount() > 0) {
            c.moveToLast();
            String billNo = c.getString(c.getColumnIndex("BillPrintNo"));
        }

        String getprintcnt = "SELECT BillPrintNo FROM " + DatabaseHandlers.TABLE_BILL_CB + " WHERE isPrinted='Yes'";
        Cursor c1 = sql_db.rawQuery(getBillNo, null);
        if (c1.getCount() > 0) {
            c1.moveToLast();
            String billNo = c1.getString(c1.getColumnIndex("BillPrintNo"));
        }

        //get company details
        if (tcf.getCompanyDtlscount() > 0) {
            // new DownloadCompanyDetailsJSON().execute();     //testpurpose
            getCompanyDetailsData();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCompanyDetailsJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                    }
                });
            }
        }

        edtxtchnglistener();

        listdtls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemcode = cbillList.get(position).getItemCode();
                String itemdesc = cbillList.get(position).getItemDesc();
                String qty = cbillList.get(position).getQty();
                String mrp = String.valueOf(cbillList.get(position).getMRP());
                String lineamt = String.valueOf(cbillList.get(position).getLineamt());
                float discount = cbillList.get(position).getDiscount();
                String taxclass = cbillList.get(position).getTaxclass();
                String taxamtinrps = cbillList.get(position).getTax_inRups();
                String totinctax = String.valueOf(cbillList.get(position).getTotAmt_incltax_lineamt());
                boolean discinrups = cbillList.get(position).isDiscinrupees();
                String discamt = String.valueOf(cbillList.get(position).getDiscamt());

                Float totline_disc = Float.parseFloat(lineamt) - Float.parseFloat(discamt);

                if (tcf.getcount_tempTable() == 0) {
                    //add data in table
                    insertDataInTempTable();
                } else {

                }

                Intent intent1 = new Intent(ItemListCB.this, AddEditItemForCBilling.class);
                intent1.putExtra("CallFrom", "Edit");
                intent1.putExtra("itemcode", itemcode);
                intent1.putExtra("itemdesc", itemdesc);
                intent1.putExtra("qty", qty);
                intent1.putExtra("mrp", mrp);
                intent1.putExtra("lineamt", lineamt);
                intent1.putExtra("discount", String.valueOf(discount));
                intent1.putExtra("taxclass", taxclass);
                intent1.putExtra("taxamtinrps", taxamtinrps);
                intent1.putExtra("totinctax", totinctax);
                intent1.putExtra("discinrups", discinrups);
                intent1.putExtra("discamt", discamt);
                startActivity(intent1);

            }
        });

        swtchdisc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (swtchdisc.isChecked()) {
                    swtchdisc.setText("Discount on Total % :   ");
                    edttxtTotalDiscount.setText("0.00");
                    //    edtxtchnglistener();
                } else {
                    swtchdisc.setText("Discount on Total ₹ :   ");
                    edttxtTotalDiscount.setText("0.00");
                    //   edtxtchnglistener();
                }
            }
        });

        SaveAndPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BluetoothClass.isPrinterConnected(getApplicationContext(), ItemListCB.this)) {
                    mService = BluetoothClass.getServiceInstance();

                String igstType = "", sgstType = "", cgstType = "", ugstType = "", vatType = "",
                        sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0", tax_class = "";

                custName = edtcustname.getText().toString().trim();
                custMob = edtcustMob.getText().toString().trim();

                //connectDevice();

                // dailog
                dialog = new Dialog(parent);
                dialog.setContentView(R.layout.tbuds_dialog_message);
                TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
                Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
                //Button btnno = (Button) dialog.findViewById(R.id.btn_no);

                String get_BillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
                Cursor _c = sql_db.rawQuery(get_BillNo, null);
                if (_c.getCount() > 0) {
                    _c.moveToLast();
                    BILLNO = Integer.parseInt(_c.getString(_c.getColumnIndex("BillPrintNo")));
                    // billNoClass.setBillNo(String.valueOf(BILLNO));
                    // String billingObj = new Gson().toJson(billNoClass);
                    // AppCommon.getInstance(ItemListCB.this).setBillingObject(billingObj);
                }

                if (billNoClass.getBillNo().equals("")) {
                    //        if (validate()) {
                    if (cbillList.isEmpty()) {
                        Toast.makeText(parent, "No items in list", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < cbillList.size(); i++) {

                            tax_class = cbillList.get(i).getTaxclass();
                            String[] taxes = checkTaxTyep(tax_class);

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

                            tcf.addBill_two(String.valueOf(BILLNO + 1), cbillList.get(i).getItemDesc(),
                                    cbillList.get(i).getItemCode(),
                                    String.valueOf(cbillList.get(i).getRate()),
                                    String.valueOf(cbillList.get(i).getMRP()),
                                    String.valueOf(cbillList.get(i).getQty()),
                                    cbillList.get(i).getTaxclass(), cgstVal, sgstVal,
                                    String.valueOf(cbillList.get(i).getDiscamt()),
                                    cbillList.get(i).getDiscount(),
                                    Float.valueOf(cbillList.get(i).getTotAmt_incltax_lineamt()),
                                    txtsubtotal.getText().toString().trim(),
                                    edttxtTotalDiscount.getText().toString().trim(),
                                    String.valueOf(discount_on_NetAmt),
                                    txtNetAmt.getText().toString().trim(),
                                    edttxtReceived.getText().toString().trim(),
                                    txtbalamt.getText().toString().trim()
                                    , custName, custMob, String.valueOf(final_discountedTotal), String.valueOf(final_taxinRupsTotal),
                                    String.valueOf(CGST_TOTAL), String.valueOf(SGST_TOTAL), "",
                                    txtpaybleamt.getText().toString().trim(),
                                    "No", "No", getCurrentDate(),
                                    edtcustgstn.getText().toString().trim(), edtgstn.getText().toString().trim());

                        }

                        String getBillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
                        Cursor c = sql_db.rawQuery(getBillNo, null);
                        if (c.getCount() > 0) {
                            c.moveToLast();
                            //String billNo = c.getString(c.getColumnIndex("BillId"));
                            String billNo = c.getString(c.getColumnIndex("BillPrintNo"));
                            billNoClass.setBillNo(billNo);
                            String billingObj = new Gson().toJson(billNoClass);
                            AppCommon.getInstance(ItemListCB.this).setBillingObject(billingObj);

                        }

                        Toast.makeText(parent, "Bill saved successfully.", Toast.LENGTH_SHORT).show();
                    }
                    /*}else {
                        Toast.makeText(ItemListCB.this, "Fill all data", Toast.LENGTH_LONG).show();
                    }*/
                } else {
                    //        if (validate()) {
                    if (cbillList.isEmpty()) {
                        Toast.makeText(parent, "No items in list", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < cbillList.size(); i++) {

                            tax_class = cbillList.get(i).getTaxclass();
                            String[] taxes = checkTaxTyep(tax_class);

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

                            tcf.updateBill_two(cbillList.get(i).getItemDesc(),
                                    cbillList.get(i).getItemCode(),
                                    String.valueOf(cbillList.get(i).getRate()),
                                    String.valueOf(cbillList.get(i).getMRP()),
                                    String.valueOf(cbillList.get(i).getQty()),
                                    cbillList.get(i).getTaxclass(), cgstVal, sgstVal,
                                    String.valueOf(cbillList.get(i).getDiscamt()),
                                    cbillList.get(i).getDiscount(),
                                    Float.valueOf(cbillList.get(i).getTotAmt_incltax_lineamt()),
                                    txtsubtotal.getText().toString().trim(),
                                    edttxtTotalDiscount.getText().toString().trim(),
                                    String.valueOf(discount_on_NetAmt),
                                    txtNetAmt.getText().toString().trim(),
                                    edttxtReceived.getText().toString().trim(),
                                    txtbalamt.getText().toString().trim()
                                    , custName, custMob, String.valueOf(final_discountedTotal), String.valueOf(final_taxinRupsTotal),
                                    "", "", "", txtpaybleamt.getText().toString().trim(),
                                    "No", "No", getCurrentDate(), billNoClass.getBillNo());
                        }

                        Toast.makeText(parent, "Bill upated successfully.", Toast.LENGTH_SHORT).show();
                    }
                   /* }else {
                        Toast.makeText(ItemListCB.this, "Fill all data", Toast.LENGTH_LONG).show();
                    }*/
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    //printReceipt();        //old
                    if (cbillList.isEmpty()) {
                        Toast.makeText(parent, "No data to print", Toast.LENGTH_SHORT).show();
                    } else {
                        createXml();

                        isTakePrint = true;

                        createJSON();

                        //      print_CGST_SGST(cbillList);
                        //print_CGST_SGST_small(cbillList);
                        dialog.dismiss();
                    }
                }

                }else {
                    BluetoothClass.connectPrinter(getApplicationContext(), ItemListCB.this);
                }

                /*if (deviceConnected) {
                    txtMsg.setText("Your device is connected to printer.");
                    Toast.makeText(parent, "Device connected to printer", Toast.LENGTH_SHORT).show();

                } else {
                    txtMsg.setText("Your device is not connected to printer, do you want to try again?");
                //    createXml();
                    Toast.makeText(parent, "Device not connected to printer. Please try agin!", Toast.LENGTH_SHORT).show();
                    //   startService(new Intent(ItemListCB.this, MarchantService.class));
                }

                   btnyes.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Toast.makeText(parent, "Connecting to device...", Toast.LENGTH_SHORT).show();
                            if(cbillList.isEmpty()){
                                Toast.makeText(parent,"No data to print", Toast.LENGTH_SHORT).show();
                            }else {
                                createXml();
                                print_CGST_SGST(cbillList);
                                dialog.dismiss();
                            }
                            //new SaveBillDetails().execute();  //send bill details to server...
                            //startService(new Intent(ItemListCB.this, MarchantService.class));
                        }
                    });
                    dialog.show();*/
            }
        });

        savebill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String igstType = "", sgstType = "", cgstType = "", ugstType = "", vatType = "",
                        sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0", tax_class = "";

                String a = edttxtReceived.getText().toString();
                custName = edtcustname.getText().toString().trim();
                custMob = edtcustMob.getText().toString().trim();

                String get_BillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
                Cursor _c = sql_db.rawQuery(get_BillNo, null);
                if (_c.getCount() > 0) {
                    _c.moveToLast();
                    try {
                        BILLNO = Integer.parseInt(_c.getString(_c.getColumnIndex("BillPrintNo")));
                    } catch (Exception e) {
                        e.printStackTrace();
                        BILLNO = 1;
                    }
                    // billNoClass.setBillNo(String.valueOf(BILLNO));
                    // String billingObj = new Gson().toJson(billNoClass);
                    // AppCommon.getInstance(ItemListCB.this).setBillingObject(billingObj);
                }

                //if (validate()) {

                if (cbillList.isEmpty()) {
                    Toast.makeText(parent, "No items in list", Toast.LENGTH_SHORT).show();
                } else {

                    if (billNoClass.getBillNo().equals("")) {
                        for (int i = 0; i < cbillList.size(); i++) {

                            tax_class = cbillList.get(i).getTaxclass();
                            String[] taxes = checkTaxTyep(tax_class);

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

                            tcf.addBill_two(String.valueOf(BILLNO + 1), cbillList.get(i).getItemDesc(),
                                    cbillList.get(i).getItemCode(),
                                    String.valueOf(cbillList.get(i).getRate()),
                                    String.valueOf(cbillList.get(i).getMRP()),
                                    String.valueOf(cbillList.get(i).getQty()),
                                    cbillList.get(i).getTaxclass(), cgstVal, sgstVal,
                                    String.valueOf(cbillList.get(i).getDiscamt()),
                                    cbillList.get(i).getDiscount(),
                                    Float.valueOf(cbillList.get(i).getTotAmt_incltax_lineamt()),
                                    txtsubtotal.getText().toString().trim(),
                                    edttxtTotalDiscount.getText().toString().trim(),
                                    String.valueOf(discount_on_NetAmt),
                                    txtNetAmt.getText().toString().trim(),
                                    edttxtReceived.getText().toString().trim(),
                                    txtbalamt.getText().toString().trim(), custName, custMob,
                                    String.valueOf(final_discountedTotal), String.valueOf(final_taxinRupsTotal),
                                    String.valueOf(CGST_TOTAL), String.valueOf(SGST_TOTAL), "",
                                    txtpaybleamt.getText().toString().trim(),
                                    "No", "No", getCurrentDate(),
                                    edtcustgstn.getText().toString().trim(), edtgstn.getText().toString().trim());
                        }

                        String getBillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
                        Cursor c = sql_db.rawQuery(getBillNo, null);
                        if (c.getCount() > 0) {
                            c.moveToLast();
                            String billNo = c.getString(c.getColumnIndex("BillPrintNo"));
                            billNoClass.setBillNo(billNo);
                            String billingObj = new Gson().toJson(billNoClass);
                            AppCommon.getInstance(ItemListCB.this).setBillingObject(billingObj);

                        } else {

                        }
                    } else {
                        String bill_No = billNoClass.getBillNo();

                        for (int i = 0; i < cbillList.size(); i++) {

                            tax_class = cbillList.get(i).getTaxclass();
                            String[] taxes = checkTaxTyep(tax_class);

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

                            tcf.updateBill_two(cbillList.get(i).getItemDesc(),
                                    cbillList.get(i).getItemCode(),
                                    String.valueOf(cbillList.get(i).getRate()),
                                    String.valueOf(cbillList.get(i).getMRP()),
                                    String.valueOf(cbillList.get(i).getQty()),
                                    cbillList.get(i).getTaxclass(), cgstVal, sgstVal,
                                    String.valueOf(cbillList.get(i).getDiscamt()),
                                    cbillList.get(i).getDiscount(),
                                    Float.valueOf(cbillList.get(i).getTotAmt_incltax_lineamt()),
                                    txtsubtotal.getText().toString().trim(),
                                    edttxtTotalDiscount.getText().toString().trim(),
                                    String.valueOf(discount_on_NetAmt),
                                    txtNetAmt.getText().toString().trim(),
                                    edttxtReceived.getText().toString().trim(),
                                    txtbalamt.getText().toString().trim()
                                    , custName, custMob, String.valueOf(final_discountedTotal), String.valueOf(final_taxinRupsTotal),
                                    "", "", "", txtpaybleamt.getText().toString().trim(),
                                    "No", "No", getCurrentDate(), bill_No);
                        }
                    }
                   // Toast.makeText(parent, "Bill saved successfully.", Toast.LENGTH_SHORT).show();

                    createXml();

                    isTakePrint = false;

                    createJSON();

                       /*cbillList.clear();
                        tcf.clearTable(parent, dbhandler.TABLE_ADD_ITMDTLS_FORBILL);
                        AppCommon.getInstance(parent).setBillingObject("");
                        finish();*/
                    //startService(new Intent(ItemListCB.this, MarchantService.class));
                }
                //        }
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifystoragepermissions(ItemListCB.this);
                File filetosend = screenshot(getWindow().getDecorView().getRootView(), "Meeshna");

                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    filetosend = screenshot(getWindow().getDecorView().getRootView(), "Meeshna");
                   // Toast.makeText(ItemListCB.this, "Working above M", Toast.LENGTH_SHORT).show();
                }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    filetosend = screenshot(getWindow().getDecorView().getRootView(), "Meeshna");
                   // Toast.makeText(ItemListCB.this, "Working above lollipop", Toast.LENGTH_SHORT).show();
                }
                }
                catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ItemListCB.this, "Error due to - "+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

                try{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        Uri uri = Uri.fromFile(filetosend);
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("image/*");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        try {
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ItemListCB.this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                        }
                        mediaPlayer.start();
                    }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        Uri uri = Uri.fromFile(filetosend);
                        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                        whatsappIntent.setType("image/*");
                        whatsappIntent.setPackage("com.whatsapp");
                        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        try {
                            startActivity(whatsappIntent);
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ItemListCB.this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
                        }
                        mediaPlayer.start();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(ItemListCB.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgadditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent, AddEditItemForCBilling.class);
                i.putExtra("CallFrom", "AddNew");
                startActivity(i);
                // finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent, AddEditItemForCBilling.class);
                i.putExtra("CallFrom", "AddNew");
                startActivity(i);
                // finish();
            }
        });
    }

    private void createXml() {

        xml1 = "<Header>";
        xml1 += "<CompanyName>" + "Vritti Solutions Ltd." + "</CompanyName>";
        xml1 += "<BillNo>" + billNoClass.getBillNo() + "</BillNo>";
        xml1 += "<Date>" + getCurrentDate() + "</Date>";
        xml1 += "<Mobile>" + MobileNo + "</Mobile>";
        xml1 += "<TotalIncTax>" + txtsubtotal.getText().toString().trim() + "</TotalIncTax>";
        xml1 += "<BaseAmt>" + txtbaseamount.getText().toString().trim() + "</BaseAmt>";
        xml1 += "<CGST>" + txtcgstamt.getText().toString().trim() + "</CGST>";
        xml1 += "<SGST>" + txtsgstamt.getText().toString().trim() + "</SGST>";
        xml1 += "<NetAmt>" + txtNetAmt.getText().toString().trim() + "</NetAmt>";
        xml1 += "<DiscOnNetAmt>" + edttxtTotalDiscount.getText().toString().trim() + "</DiscOnNetAmt>";
        xml1 += "<PayableAmt>" + txtpaybleamt.getText().toString().trim() + "</PayableAmt>";
        xml1 += "<CustomerName>" + edtcustname.getText().toString().trim() + "</CustomerName>";
        xml1 += "<CustomerMobile>" + edtcustMob.getText().toString().trim() + "</CustomerMobile>";
        xml1 += "<CustGSTN>" + edtcustgstn.getText().toString().trim() + "</CustGSTN>";
        xml1 += "<CompanyGSTN>" + edtgstn.getText().toString().trim() + "</CompanyGSTN>";
        xml1 += "</Header>";

        sb.setLength(0);
        sb.append("<Detail>");
        int i;
        for (i = 0; i < cbillList.size(); i++) {

            float taxinrupsTotal = Float.parseFloat(cbillList.get(i).getTax_inRups());
            String taxinrups = String.valueOf(taxinrupsTotal);

            sb.append("<Table>");
            sb.append("<Itemid>" + cbillList.get(i).getItemCode() + "</Itemid>");
            sb.append("<Itemname>" + cbillList.get(i).getItemDesc() + "</Itemname>");
            sb.append("<Qty>" + cbillList.get(i).getQty() + "</Qty>");
            sb.append("<DiscAmount>" + cbillList.get(i).getDiscamt()+ "</DiscAmount>");
            sb.append("<TotalAmount>" + cbillList.get(i).getTotAmt_incltax_lineamt()+"</TotalAmount>");
            sb.append("<Rate>" + String.format("%.02f", cbillList.get(i).getMRP())+"</Rate>");
            sb.append("<TaxAmount>" + String.format("%.02f",taxinrupsTotal)+ "</TaxAmount>");
            sb.append("</Table>");
        }
        sb.append("</Detail>");
        xml2 = sb.toString();

       /* String BILL_xml = "";
        String xmlbillNo = "SELECT BillId FROM "+dbhandler.TABLE_BILL_DETAILS;// + " WHERE BillId='"+billNoClass.getBillNo()+"'";
        Cursor c = sql_db.rawQuery(xmlbillNo,null);
        if(c.getCount() > 0){
            c.moveToLast();
            BILL_xml = c.getString(c.getColumnIndex("BillId"));
        }

        if(billNoClass.getBillNo().equals(BILL_xml)){
            //do not add in table
        }else {
            tcf.addBillXml(billNoClass.getBillNo(),xml1, xml2, "No",getCurrentDate());
        }*/

    }

    public void edtxtchnglistener() {

        edttxtReceived.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (edttxtTotalDiscount.getText().toString() == "" || edttxtTotalDiscount.getText().toString() == "0"
                        || edttxtTotalDiscount.getText().toString() == null) {
                    FinalTotal = Float.parseFloat(txtNetAmt.getText().toString());

                    txtbalamt.setText(String.format("%.02f", FinalTotal));

                } else {

                    //   FinalTotal = Float.parseFloat(txtFinalTotal.getText().toString());

                    // txtNetAmt.setText("541.50");
                    FinalTotal = Float.parseFloat(txtNetAmt.getText().toString());

                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0) || (s.toString().trim() == " "))) {
                        edttxtRemaining.setText(String.format("%.02f", FinalTotal) + "");

                        //  txtbalamt.setText(FinalTotal + "");
                        txtbalamt.setText(String.format("%.02f", FinalTotal));
                    } else {

                        remaining = FinalTotal - Float.parseFloat(s.toString().trim());
                        edttxtRemaining.setText(String.format("%.02f", remaining) + "");

                        remaining = FinalTotal - Float.parseFloat(s.toString().trim());
                        //txtbalamt.setText( "- " + remaining + "");
                        txtbalamt.setText(String.format("%.02f", remaining));
                    }
                }
            }
        });

        edttxtTotalDiscount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //        if (amt != 0 && amt > 0) {

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {
                    FinalTotal = amt;
                    // txtFinalTotal.setText("" + String.format("%.02f",TotInclTax));

                    // TotInclTax = Float.parseFloat(Totalincltax.getText().toString().trim());
                    // txtNetAmt.setText(new DecimalFormat("##.##").format(TotInclTax));
                    // txtNetAmt.setText(String.format("%.02f",TotInclTax));

                    TotInclTax = Float.parseFloat(Totalincltax.getText().toString().trim());
                    txtpaybleamt.setText(String.format("%.02f", TotInclTax));

                } else {

                    //      dis_amt = (amt * Float.parseFloat(s.toString()) / 100);
                    //amt = Float.parseFloat(Totalincltax.getText().toString().trim());

                    amt = Float.parseFloat(txtNetAmt.getText().toString().trim());          //discount on NetAmt
                    if (amt > Float.parseFloat(s.toString())) {

                        if (swtchdisc.isChecked()) {
                            swtchdisc.setText("Discount on Total % :   ");
                           /*// float discamt = (amt * Float.parseFloat(s.toString()))/100;
                            float discamt = (Float.parseFloat(Totalincltax.getText().toString().trim()) * Float.parseFloat(s.toString()))/100;
                           // FinalTotal = amt - discamt;
                            FinalTotal = (Float.parseFloat(Totalincltax.getText().toString().trim()) - discamt);*/

                            float discamt = (Float.parseFloat(txtNetAmt.getText().toString().trim()) * Float.parseFloat(s.toString())) / 100;
                            discount_on_NetAmt = discamt;
                            FinalTotal = (Float.parseFloat(txtNetAmt.getText().toString().trim()) - discamt);

                            //discount on NetAmount
                        } else {
                            swtchdisc.setText("Discount on Total ₹ :   ");
                            discount_on_NetAmt = Float.parseFloat(s.toString());

                            //  FinalTotal = (amt - Float.parseFloat(s.toString()));
                            // FinalTotal = (Float.parseFloat(Totalincltax.getText().toString().trim()) - Float.parseFloat(s.toString()));

                            FinalTotal = (Float.parseFloat(txtNetAmt.getText().toString().trim()) - Float.parseFloat(s.toString()));
                        }

                        txtFinalTotal.setText("" + String.format("%.02f", FinalTotal));

                        //  Totalincltax.setText(String.format("%.02f",FinalTotal));
                        //  txtNetAmt.setText(String.format("%.02f",FinalTotal));
                        txtpaybleamt.setText(String.format("%.02f", FinalTotal));
                        txtbalamt.setText(String.format("%.02f", FinalTotal));

                    } else {
                        Toast.makeText(ItemListCB.this, "Discount amount should be less then total amount", Toast.LENGTH_SHORT).show();
                    }
                }
                //        }
            }
        });

        edttaxtotal.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                    Totalincltax.setText(txtsubtotal.getText().toString().trim());
                    txtNetAmt.setText(txtsubtotal.getText().toString().trim());

                } else {
                    float subtotal = Float.parseFloat(txtsubtotal.getText().toString().trim());
                    float taxontot = Float.parseFloat(s.toString());
                    TotInclTax = ((subtotal * taxontot) / 100) + subtotal;

                    Totalincltax.setText(String.format("%.02f", TotInclTax));
                    txtNetAmt.setText(String.format("%.02f", TotInclTax));
                    txtbalamt.setText(String.format("%.02f", TotInclTax));
                }
            }
        });

    }

    private void scanBluetooth() {

        startActivityForResult(new Intent(parent, DeviceListActivity.class),
                AnyMartData.REQUEST_CONNECT_DEVICE);
    }

    private boolean validate() {
        // TODO Auto-generated method stub

        if (txtsubtotal.getText().toString().equalsIgnoreCase("0.00")) {
            Toast.makeText(ItemListCB.this, "Fill all amount", Toast.LENGTH_LONG).show();
            edttxtReceived.setEnabled(false);
            return false;
        } else if ((edttxtReceived.getText().toString().equalsIgnoreCase("") ||
                edttxtReceived.getText().toString().equalsIgnoreCase(" ") ||
                edttxtReceived.getText().toString().equalsIgnoreCase(null))) {
            Toast.makeText(ItemListCB.this, "Fill received amount", Toast.LENGTH_LONG).show();

            return false;
        } else if (edttxtTotalDiscount.getText().toString().trim().equalsIgnoreCase("")) {
            edttxtTotalDiscount.setText("0.00");
            return true;
        } else {
            return true;
        }
    }

    @SuppressLint("WrongViewCast")
    private void addView(int i) {
        final int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_item_list_cb, null);

        myAutoComplete = (CustomAutoCompleteView) baseView.findViewById(R.id.myautocomplete);
        edRate = (EditText) baseView.findViewById(R.id.edRate);
        btn_ok = (ImageButton) baseView.findViewById(R.id.btn_ok);
        edAmt = (EditText) baseView.findViewById(R.id.edAmt);
        edQnty = (EditText) baseView.findViewById(R.id.edQnty);
        btn_cancel = (ImageButton) baseView.findViewById(R.id.btn_cancel);
        spinnerunit = (Spinner) baseView.findViewById(R.id.spinnerunit);
        edDiscount = (EditText) baseView.findViewById(R.id.edDiscount);
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));

        //   edQnty.setText("" + 0);
        // edAmt.setText("" + 0);
        AllCatSubcatItems myObject = null;
        AllCatSubcatItems[] ObjectItemData = new AllCatSubcatItems[1];
        myObject = new AllCatSubcatItems();
        ObjectItemData[0] = myObject;

        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.tbuds_list_view_row_item, ObjectItemData);
        myAutoComplete.setAdapter(myAdapter);

        int textLength = edQnty.getText().length();
        edQnty.setSelection(textLength, textLength);

        int textLength1 = myAutoComplete.getText().length();
        myAutoComplete.setSelection(textLength1, textLength1);
        myAutoComplete.setFocusable(true);

        if (tcf.getCartItems_AgainstCustomer(custName, custMob) > 0
                && i < tcf.getCartItems_AgainstCustomer(custName, custMob)) {
            getDataFromDataBase();
            if (i < myCartBeanArrayList.size()) {
                myAutoComplete.setText("" + myCartBeanArrayList.get(i).getProduct_name());
                myAutoComplete.setFocusable(false);
                myAutoComplete.setClickable(false);
                myAutoComplete.setEnabled(false);

                edRate.setText("" + myCartBeanArrayList.get(i).getPrice());
                edRate.setFocusable(false);
                edRate.setClickable(false);
                edRate.setEnabled(false);

                edAmt.setText("" + myCartBeanArrayList.get(i).getAmount());
                edAmt.setFocusable(false);
                edAmt.setClickable(false);
                edAmt.setEnabled(false);

                edQnty.setText("" + myCartBeanArrayList.get(i).getQnty());
                edQnty.setFocusable(false);
               /* edQnty.setFocusable(false);
                edQnty.setClickable(false);
                edQnty.setEnabled(false);*/

                edDiscount.setText("" + myCartBeanArrayList.get(i).getDISCOUNT());
                edDiscount.setFocusable(false);
                edDiscount.setClickable(false);
                edDiscount.setEnabled(false);

                int spinner_pos = 0;
                if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("gm")) {
                    spinner_pos = 1;
                } else if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("kg")) {
                    spinner_pos = 2;
                } else if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("item")) {
                    spinner_pos = 3;
                }
                spinnerunit.setSelection(spinner_pos);
                btn_cancel.setVisibility(View.VISIBLE);
            }
        }
        setListeners(i);

        //  containerLayout_one.addView(baseView);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getShipmntDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        //2020-11-18 12:00:00 AM
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getCommittedDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        //18-11-2020 12:00:00 AM
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        myCartBeanArrayList.clear();
        //  DatabaseHandler db1 = new DatabaseHandler(parent);
        //   SQLiteDatabase db = db1.getWritableDatabase();
        amt = 0;

        Cursor c11 = sql_db.rawQuery("Select distinct Cust_mob,Cust_Name,qnty,minqnty," +
                "offers,price," +
                "Product_name,Amount,Product_id,Freeitemqty , Freeitemname, " +
                "validfrom,validto,DISCOUNT ,UNIT ,UNITV from "
                + dbhandler.TABLE_CART_ITEM_CB +
                "  where Cust_Name ='" + custName + "' and " +
                " Cust_mob ='" + custMob + "'", null);
        if (c11.getCount() > 0) {
            c11.moveToFirst();
            do {
                String Cust_Name = c11.getString(c11.getColumnIndex("Cust_Name"));
                String Cust_mob = c11.getString(c11.getColumnIndex("Cust_mob"));
                String qnty = c11.getString(c11.getColumnIndex("qnty"));
                String offers = c11.getString(c11.getColumnIndex("offers"));
                String price = c11.getString(c11.getColumnIndex("price"));
                String Product_name = c11.getString(c11.getColumnIndex("Product_name"));
                String Amount = c11.getString(c11.getColumnIndex("Amount"));
                String Product_id = c11.getString(c11.getColumnIndex("Product_id"));
                String DISCOUNT = c11.getString(c11.getColumnIndex("DISCOUNT"));
                String UNIT = c11.getString(c11.getColumnIndex("UNIT"));

                amt = amt + Float.parseFloat(Amount);

                myCartBean = new MyCartBean();
                myCartBean.setCustMobno(Cust_mob);
                myCartBean.setCustomerName(Cust_Name);
                myCartBean.setQnty(Float.valueOf(qnty));
                myCartBean.setMinqnty(c11.getString(c11.getColumnIndex("minqnty")));
                myCartBean.setOffers(offers);
                myCartBean.setPrice(Float.valueOf(price));
                myCartBean.setProduct_name(Product_name);
                myCartBean.setAmount(Float.valueOf(Amount));
                myCartBean.setProduct_id(Product_id);
                myCartBean.setFree_item_name_trade(c11.getString(c11.getColumnIndex("Freeitemname")));
                myCartBean.setFree_item_qnty_trade(c11.getString(c11.getColumnIndex("Freeitemqty")));
                myCartBean.setValidfrom_trade(c11.getString(c11.getColumnIndex("validfrom")));
                myCartBean.setValidto_trade(c11.getString(c11.getColumnIndex("validto")));
                if (DISCOUNT.equalsIgnoreCase("")) {
                    myCartBean.setDISCOUNT("0");
                } else {
                    myCartBean.setDISCOUNT(DISCOUNT);
                }

                myCartBean.setUNIT(UNIT);
                myCartBean.setUnit_V(c11.getString(c11.getColumnIndex("UNITV")));
                myCartBeanArrayList.add(myCartBean);
            } while (c11.moveToNext());
        }

        txtTotal.setText("" + Math.round(amt));
        txtFinalTotal.setText("" + Math.round(amt));

    }

    private void print_CGST_SGST(ArrayList<CounterbillingBean> cbillList, String invoiceNo) {

        final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
        final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
        final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};
        final byte[] SMALLFONT = {0x1b,0x21,0x01}; //small font
        final byte[] DEFAULT = {0x1b,0x21,0x00};
        final byte[] NORMAL = new byte[]{0x1B,0x21,0x00};  // 0- normal size text

        String total = "";
        String finalbill = "", received = "", balance = "", subTotal = "", taxInPer = "", totInclTax = "", discAmt_item = "", tax_class = "",
                discTotal = "0", taxInRupsTotal = "0", disconNet = "0", payablAmt = "0", _total = "0",_roundOff = "0", _youSave = "0";
        String dis_amt = "";
        String datetime = getCurrentDate();
        String billNo = "";
        int billId = 0;
        String itemNameToPrint, itemQtyToPrint, itemRateToPrint,itemMRPToPrint, itemAmountToPrint, itemDiscToPrint,
                custNameToprint, custGSTNToprint;

        String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
                sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
        float cgst = 0.0F;
        String CGST = "0", SGST = "0", IGST = "0";

        subTotal = txtsubtotal.getText().toString().trim();
        taxInPer = edttaxtotal.getText().toString().trim();
        totInclTax = Totalincltax.getText().toString().trim();
        dis_amt = edttxtTotalDiscount.getText().toString().trim();
        finalbill = txtNetAmt.getText().toString().trim();
        received = edttxtReceived.getText().toString().trim();
        balance = txtbalamt.getText().toString().trim();
        discTotal = String.format("%.02f", final_discountedTotal);
        taxInRupsTotal = String.format("%.02f", final_taxinRupsTotal);
        disconNet = String.format("%.02f", discount_on_NetAmt);
        payablAmt = txtpaybleamt.getText().toString().trim();
        custNameToprint = edtcustname.getText().toString().trim();
        custGSTNToprint = edtcustgstn.getText().toString().trim();
        _total = txttotal.getText().toString().trim();
        _roundOff = txtroundoff.getText().toString().trim();
        _youSave = txtyousave.getText().toString().trim();

        String getBillNo = "SELECT BillPrintNo FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
        Cursor c = sql_db.rawQuery(getBillNo, null);
        if (c.getCount() > 0) {
            c.moveToLast();
            billNo = c.getString(c.getColumnIndex("BillPrintNo"));
        }

        String bill = billNoClass.getBillNo();
        try{
            billId = Integer.parseInt(bill);
        }catch (Exception e){
            e.printStackTrace();
        }

        String msg = null, company = "";
        //company = "\n" + "      Vritti Solutions Ltd      " +"\n\n";
        //SpannableString ss1=  new SpannableString(company);
        // ss1.setSpan(new RelativeSizeSpan(2f), 0,5, 0);

        Cname = "Meeshna Masale";
        //Address = "7, Second floor, Krishna Ganga Apartment, Parijat Corner, Gulmohar Road, Savedi,";

        String cNAME = Cname;
        int cnamelength = Cname.length();

        if(Cname.contains("Meeshna")){
            regAddress = "B-13, Shri Ganesh Appt., Gujrat Colony, Kothrud, Pune-411038";

            String l1 = " Soniya, Chaudapat, Shahabaug, ";
            String l2 = "      Wai, Satara - 412803     ";
            factoryAddress = l1 +"\n"+l2;
        }else {

        }

        String regaddr1 = "", regaddr2 = "";
        if(!regAddress.equalsIgnoreCase("") || !regAddress.equals(null)){
            regaddr1 = "B-13, Shri Ganesh Appt.,Gujrat";
            regaddr2 = "Colony, Kothrud, Pune-411038";
        }

        //splitAddressInLines(factoryAddress);
        //msg = "\n" + "      Vritti Solutions Ltd      " + "\n\n";
        msg = "\n         "+Cname+"         \n";
        msg = "\n  "+FSSAI+"  \n";
        msg += " "+regaddr1+"\n";
        msg += "   "+regaddr2+"  \n";

       /* String addrLine = "", printmsg = "";
        for(int i=0; i<addressLines.size(); i++){
            addrLine = addressLines.get(i).toString();

            if (addrLine.length() >= 24) {
                addrLine = addrLine.substring(0, 24);
                printmsg = "   "+addrLine+"   ";
                msg += printmsg;
            } else if (addrLine.length() < 24) {
                int diff = 24 - addrLine.length();  //24-8 = 16 16/2 = 8
                //        for (int j = 0; j < diff; i++) {
                printmsg = "    "+addrLine+"    ";
                msg += printmsg + "\n\n";
                //}
            }
        }*/

        //msg += "Bill No. : " + billId + "\n";
        msg += "Bill No. : " + invoiceNo + "\n";
        msg += "Date     : " + datetime + "\n";

        if(custNameToprint.equalsIgnoreCase("") || custNameToprint.equalsIgnoreCase(null)){
        }else {
            msg += "Customer : " + custNameToprint + "\n";
        }

        if(custMob.equalsIgnoreCase("") || custMob.equalsIgnoreCase(null)){
        }else {
            msg += "Mobile   : " + custMob + "\n";
        }

        if(custGSTNToprint.equalsIgnoreCase("") || custGSTNToprint.equalsIgnoreCase(null)){
        }else {
            msg += "Cust GSTN: " + custGSTNToprint + "\n";
        }

        msg += "--------------------------------\n";    //32
        msg += "ITEM       QTY  DIS.AMT  NET.AMT\n";    //32
        //msg += "RATE     CGST%    SGST%         \n";    //32
        msg += "MRP     RATE                   \n";    //32
        msg += "================================\n";
        try {
            for (int j = 0; j < cbillList.size(); j++) {

                itemNameToPrint = cbillList.get(j).getItemDesc();
                if (itemNameToPrint.length() > 10) {
                    itemNameToPrint = itemNameToPrint.substring(0, 10);
                } else if (itemNameToPrint.length() <= 10) {
                    int diff = 10 - itemNameToPrint.length();
                    for (int i = 0; i < diff; i++) {
                        itemNameToPrint += " ";
                    }
                }

                itemQtyToPrint = cbillList.get(j).getQty();
                itemQtyToPrint = itemQtyToPrint.replaceFirst("^0+(?!$)", "");
                if (itemQtyToPrint.contains(".")) {
                    {
                        itemQtyToPrint += "000";
                        itemQtyToPrint = itemQtyToPrint.substring(0, itemQtyToPrint.lastIndexOf(".") + 3);
                    }
                } else {
                    // itemQtyToPrint += ".00";
                }

                if (itemQtyToPrint.length() <= 3) {
                    int diff = 3 - itemQtyToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemQtyToPrint = " " + itemQtyToPrint;
                        }
                    }
                }

                tax_class = cbillList.get(j).getTaxclass();
                String[] taxes = checkTaxTyep(tax_class);

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

                if (cgstVal.length() <= 3) {
                    int diff = 3 - cgstVal.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            cgstVal = " " + cgstVal;
                        }
                    }
                }

                if (sgstVal.length() <= 7) {
                    int diff = 7 - sgstVal.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            sgstVal = " " + sgstVal;
                        }
                    }
                }

                itemMRPToPrint = String.format("%.02f", cbillList.get(j).getMRP());
                itemMRPToPrint = itemMRPToPrint.replaceFirst("^0+(?!$)", "");
                if (itemMRPToPrint.contains(".")) {
                    itemMRPToPrint += "000";
                    itemMRPToPrint = itemMRPToPrint.substring(0, itemMRPToPrint.lastIndexOf(".") + 3);
                } else {
                    itemMRPToPrint += ".00";
                }

                if (itemMRPToPrint.length() <= 6) {
                    int diff = 6 - itemMRPToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemMRPToPrint += " "; /*+ itemRateToPrint*/
                            ;
                        }
                    }
                }

                itemRateToPrint = String.format("%.02f", cbillList.get(j).getRate());
                itemRateToPrint = itemRateToPrint.replaceFirst("^0+(?!$)", "");
                if (itemRateToPrint.contains(".")) {
                    itemRateToPrint += "000";
                    itemRateToPrint = itemRateToPrint.substring(0, itemRateToPrint.lastIndexOf(".") + 3);
                } else {
                    itemRateToPrint += ".00";
                }

                if (itemRateToPrint.length() <= 10) {
                    int diff = 10 - itemRateToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemRateToPrint += " "; /*+ itemRateToPrint*/
                            ;
                        }
                    }
                }

                discAmt_item = String.format("%.02f", cbillList.get(j).getDiscamt());

                if (discAmt_item.equals("0.0") || discAmt_item.equals("0.00")) {
                    discAmt_item = "0.00";
                } else {
                    discAmt_item = discAmt_item.replaceFirst("^0+(?!$)", "");
                    if (discAmt_item.contains(".")) {
                        discAmt_item += "000";
                        discAmt_item = discAmt_item.substring(0, discAmt_item.lastIndexOf(".") + 3);
                    } else {
                        discAmt_item += "0.00";
                    }
                }

                if (discAmt_item.length() <= 7) {
                    int diff = 7 - discAmt_item.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            discAmt_item = " " + discAmt_item;
                        }
                    }
                }

                itemAmountToPrint = String.format("%.02f", cbillList.get(j).getDicountedTotal());
                itemAmountToPrint = itemAmountToPrint.replaceFirst("^0+(?!$)", "");

                if (itemAmountToPrint.contains(".")) {
                    itemAmountToPrint += "000";
                    itemAmountToPrint = itemAmountToPrint.substring(0, itemAmountToPrint.lastIndexOf(".") + 3);
                } else {
                    itemAmountToPrint += ".00";
                }

                if (itemAmountToPrint.length() <= 7) {
                    int diff = 7 - itemAmountToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemAmountToPrint = " " + itemAmountToPrint;
                        }
                    }
                }

                msg += itemNameToPrint + "  " + itemQtyToPrint + " " + discAmt_item + "  " + itemAmountToPrint + "\n";
                //msg += itemRateToPrint + "  " + cgstVal + " " + sgstVal + "\n";
                msg += itemMRPToPrint + "  " + itemRateToPrint+ " " + "" + " " +""+ "\n";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        subTotal = subTotal.replaceFirst("^0+(?!$)", "");
        subTotal = callCheckLenght(subTotal);
        taxInPer = callCheckLenght(taxInPer);
        totInclTax = callCheckLenght(totInclTax);
        dis_amt = callCheckLenght(dis_amt);
        finalbill = callCheckLenght(finalbill);
        received = callCheckLenght(received);
        balance = callCheckLenght(balance);
        discTotal = callCheckLenght(discTotal);
        taxInRupsTotal = callCheckLenght(taxInRupsTotal);
        cgst = Float.parseFloat(taxInRupsTotal);
        CGST = String.format("%.02f", cgst / 2);
        SGST = String.format("%.02f", cgst / 2);
        CGST = callCheckLenght(CGST);
        SGST = callCheckLenght(SGST);
        disconNet = callCheckLenght(disconNet);
        payablAmt = callCheckLenght(payablAmt);
        _total = callCheckLenght(_total);
        _roundOff = callCheckLenght(_roundOff);
        _youSave = callCheckLenght(_youSave);

        /******************************************/
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
        /******************************************/
        msg += "================================\n";
        //    msg += "          SubTotal:"+subTotal+"\n";
        //    msg += "             Tax %:"+ taxInPer+"\n";
        //msg += " Total (Incl. tax):" + totInclTax + "\n";
        //msg += "        ________________________\n";
        msg += "       Base Amount:" + discTotal + "\n";
        //    msg += "          Discount:"+dis_amt+"\n";
        msg += "        CGST "+cgstVal+" %:"+ CGST + "\n";
        msg += "        SGST "+sgstVal+" %:"+ CGST + "\n";
        msg += "             Total:" + _total + "\n";
        msg += "       .........................\n";
        msg += "         Round off:" + _roundOff + "\n";
        msg += "        Net Amount:" + finalbill + "\n";
        msg += "       .........................\n";
        //msg += "        ________________________\n";
        //    msg += "          Received:"+received+"\n";
        //    msg += "           Balance:"+balance+"\n";
        msg += "          Discount:" + disconNet + "\n";
        msg += "    Payable Amount:" + payablAmt + "\n";
        msg += "          You Save:" + _youSave + "\n";
        msg += "--------------------------------\n";
        msg += "     Thank You! Visit Again.    \n";
        msg += ""+factoryAddress+"\n";
        msg += "     Contact : "+FirmMobile+"\n";
        msg += "--------------------------------\n";

        for(int p=0; p<2;p++){
            if (msg.length() > 0) {
                mService.write(NORMAL);
                mService.write(ALIGN_LEFT);
                mService.sendMessage(msg + "\n\n\n", "GBK");
            }
        }

    //    for(int i =0; i<cbillList.size();i++){
            ContentValues values = new ContentValues();
            values.put("isUploaded", "No");
            values.put("isPrinted", "Yes");
            sql_db.update(dbhandler.TABLE_BILL_CB,values,"BillPrintNo=?",new String[]{billNoClass.getBillNo()});
    //    }

       // createJSON();
            cbillList.clear();
            tcf.clearTable(parent, dbhandler.TABLE_ADD_ITMDTLS_FORBILL);
            AppCommon.getInstance(this).setBillingObject("");
            finish();
    }

    private String callCheckLenght(String value) {
        if (value.length() <= 13) {
            int diff = 13 - value.length();
            if (diff > 0) {
                for (int i = 0; i < diff; i++) {
                    value = " " + value;
                }
            }
        }
        return value;
    }

    private void wapp(ArrayList<CounterbillingBean> cbillList) {

        final byte[] ALIGN_LEFT = {0x1B, 0x61, 0};
        final byte[] ALIGN_CENTER = {0x1B, 0x61, 1};
        final byte[] ALIGN_RIGHT = {0x1B, 0x61, 2};

        String total = "";
        String finalbill = "", received = "", balance = "", subTotal = "", taxInPer = "", totInclTax = "", discAmt_item = "", tax_class = "",
                discTotal = "0", taxInRupsTotal = "0", disconNet = "0", payablAmt = "0";
        String dis_amt = "";
        String datetime = getCurrentDate();
        String billNo = "";
        int billId = 0;
        String itemNameToPrint, itemQtyToPrint, itemRateToPrint, itemAmountToPrint, itemDiscToPrint;

        String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
                sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
        float cgst = 0.0F;
        String CGST = "0", SGST = "0", IGST = "0";

        subTotal = txtsubtotal.getText().toString().trim();
        taxInPer = edttaxtotal.getText().toString().trim();
        totInclTax = Totalincltax.getText().toString().trim();
        dis_amt = edttxtTotalDiscount.getText().toString().trim();
        finalbill = txtNetAmt.getText().toString().trim();
        received = edttxtReceived.getText().toString().trim();
        balance = txtbalamt.getText().toString().trim();
        discTotal = String.format("%.02f", final_discountedTotal);
        taxInRupsTotal = String.format("%.02f", final_taxinRupsTotal);
        disconNet = String.format("%.02f", discount_on_NetAmt);
        payablAmt = txtpaybleamt.getText().toString().trim();

        String getBillNo = "SELECT BillId FROM " + dbhandler.TABLE_BILL_CB /*+ " WHERE isPrinted='No'"*/;
        Cursor c = sql_db.rawQuery(getBillNo, null);
        if (c.getCount() > 0) {
            c.moveToLast();
            billNo = c.getString(c.getColumnIndex("BillId"));
        }

        billId = Integer.parseInt(billNo) + 1;

        String msg = null, company = "";
        //company = "\n" + "      Vritti Solutions Ltd      " +"\n\n";
        //SpannableString ss1=  new SpannableString(company);
        // ss1.setSpan(new RelativeSizeSpan(2f), 0,5, 0);
        msg = "\n" + "      Vritti Solutions Ltd      " + "\n\n";
        msg += "Bill No.: " + billNo + "\n";
        msg += "Date    : " + datetime + "\n";
        msg += "Mobile  : " + FirmMobile + "\n";
        msg += "--------------------------------\n";    //32
        msg += "ITEM       QTY  DIS.AMT  NET.AMT\n";    //32
        msg += "RATE     CGST%    SGST%         \n";    //32
        msg += "================================\n";
        try {

            for (int j = 0; j < cbillList.size(); j++) {

                itemNameToPrint = cbillList.get(j).getItemDesc();
                if (itemNameToPrint.length() > 10) {
                    itemNameToPrint = itemNameToPrint.substring(0, 10);
                } else if (itemNameToPrint.length() <= 10) {
                    int diff = 10 - itemNameToPrint.length();
                    for (int i = 0; i < diff; i++) {
                        itemNameToPrint += " ";
                    }
                }

                itemQtyToPrint = cbillList.get(j).getQty();
                itemQtyToPrint = itemQtyToPrint.replaceFirst("^0+(?!$)", "");
                if (itemQtyToPrint.contains(".")) {
                    {
                        itemQtyToPrint += "000";
                        itemQtyToPrint = itemQtyToPrint.substring(0, itemQtyToPrint.lastIndexOf(".") + 3);
                    }
                } else {
                    // itemQtyToPrint += ".00";
                }

                if (itemQtyToPrint.length() <= 3) {
                    int diff = 3 - itemQtyToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemQtyToPrint = " " + itemQtyToPrint;
                        }
                    }
                }

                tax_class = cbillList.get(j).getTaxclass();
                String[] taxes = checkTaxTyep(tax_class);

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

                if (cgstVal.length() <= 3) {
                    int diff = 3 - cgstVal.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            cgstVal = " " + cgstVal;
                        }
                    }
                }

                if (sgstVal.length() <= 7) {
                    int diff = 7 - sgstVal.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            sgstVal = " " + sgstVal;
                        }
                    }
                }

                itemRateToPrint = String.format("%.02f", cbillList.get(j).getMRP());
                itemRateToPrint = itemRateToPrint.replaceFirst("^0+(?!$)", "");
                if (itemRateToPrint.contains(".")) {
                    itemRateToPrint += "000";
                    itemRateToPrint = itemRateToPrint.substring(0, itemRateToPrint.lastIndexOf(".") + 3);
                } else {
                    itemRateToPrint += ".00";
                }

                if (itemRateToPrint.length() <= 10) {
                    int diff = 10 - itemRateToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemRateToPrint += " "; /*+ itemRateToPrint*/
                            ;
                        }
                    }
                }

                discAmt_item = String.format("%.02f", cbillList.get(j).getDiscamt());

                if (discAmt_item.equals("0.0") || discAmt_item.equals("0.00")) {
                    discAmt_item = "0.00";
                } else {
                    discAmt_item = discAmt_item.replaceFirst("^0+(?!$)", "");
                    if (discAmt_item.contains(".")) {
                        discAmt_item += "000";
                        discAmt_item = discAmt_item.substring(0, discAmt_item.lastIndexOf(".") + 3);
                    } else {
                        discAmt_item += "0.00";
                    }
                }

                if (discAmt_item.length() <= 7) {
                    int diff = 7 - discAmt_item.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            discAmt_item = " " + discAmt_item;
                        }
                    }
                }

                itemAmountToPrint = String.format("%.02f", cbillList.get(j).getTotAmt_incltax_lineamt());
                itemAmountToPrint = itemAmountToPrint.replaceFirst("^0+(?!$)", "");

                if (itemAmountToPrint.contains(".")) {
                    itemAmountToPrint += "000";
                    itemAmountToPrint = itemAmountToPrint.substring(0, itemAmountToPrint.lastIndexOf(".") + 3);
                } else {
                    itemAmountToPrint += ".00";
                }

                if (itemAmountToPrint.length() <= 7) {
                    int diff = 7 - itemAmountToPrint.length();
                    if (diff > 0) {
                        for (int i = 0; i < diff; i++) {
                            itemAmountToPrint = " " + itemAmountToPrint;
                        }
                    }
                }

                msg += itemNameToPrint + "  " + itemQtyToPrint + " " + discAmt_item + "  " + itemAmountToPrint + "\n";
                msg += itemRateToPrint + "  " + cgstVal + " " + sgstVal + "\n";

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        subTotal = subTotal.replaceFirst("^0+(?!$)", "");
        subTotal = callCheckLenght(subTotal);
        taxInPer = callCheckLenght(taxInPer);
        totInclTax = callCheckLenght(totInclTax);
        dis_amt = callCheckLenght(dis_amt);
        finalbill = callCheckLenght(finalbill);
        received = callCheckLenght(received);
        balance = callCheckLenght(balance);
        discTotal = callCheckLenght(discTotal);
        taxInRupsTotal = callCheckLenght(taxInRupsTotal);
        cgst = Float.parseFloat(taxInRupsTotal);
        CGST = String.format("%.02f", cgst / 2);
        SGST = String.format("%.02f", cgst / 2);
        CGST = callCheckLenght(CGST);
        SGST = callCheckLenght(SGST);
        disconNet = callCheckLenght(disconNet);
        payablAmt = callCheckLenght(payablAmt);

        msg += "================================\n";
        //    msg += "          SubTotal:"+subTotal+"\n";
        //    msg += "             Tax %:"+ taxInPer+"\n";
        msg += " Total (Incl. tax):" + totInclTax + "\n";
        msg += "        ________________________\n";
        msg += "       Base Amount:" + discTotal + "\n";
        //    msg += "          Discount:"+dis_amt+"\n";
        msg += "              CGST:" + CGST + "\n";
        msg += "              SGST:" + SGST + "\n";
        msg += "        Net Amount:" + finalbill + "\n";
        msg += "        ________________________\n";
        //    msg += "          Received:"+received+"\n";
        //    msg += "           Balance:"+balance+"\n";
        msg += "          Discount:" + disconNet + "\n";
        msg += "    Payable Amount:" + payablAmt + "\n";
        msg += "--------------------------------\n";
        msg += "     Thank You! Visit Again.    \n";
        msg += "--------------------------------\n";

        txtBill = msg;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if(intentFrom.contains("PrintedBillDetailsActivity")){
            String billingObj = "";
            AppCommon.getInstance(this).setBillingObject(billingObj);
            super.onBackPressed();
            finish();
        }else {
            finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialize() {
        parent = ItemListCB.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Counter Billing");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        imgadditem = (ImageView) findViewById(R.id.imgadditem);
        listdtls = findViewById(R.id.listdtls);
        swtchdisc = (Switch) findViewById(R.id.swtchdisc);
        edtcustgstn = findViewById(R.id.edtcustgstn);
        edtgstn = findViewById(R.id.edtgstn);
        titlecgst = findViewById(R.id.txtcgst);
        titlesgst = findViewById(R.id.txtsgst);

        txtsubtotal = findViewById(R.id.txtsubtotal);
        edttaxtotal = findViewById(R.id.edttaxtotal);
        Totalincltax = findViewById(R.id.txtTotalincltax);
        txtNetAmt = findViewById(R.id.txtNetAmt);
        txtbalamt = findViewById(R.id.txtbalamt);
        txtpaybleamt = findViewById(R.id.txtpaybleamt);

        containerLayout_one = (LinearLayout) findViewById(R.id.containerLayout_one);
        savebill = (Button) findViewById(R.id.savebill);
        myCartBeanArrayList = new ArrayList<MyCartBean>();
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        edtcustMob = findViewById(R.id.edtcustMob);
        edtcustname =  findViewById(R.id.edtcustname);
        edttxtTotalDiscount = (EditText) findViewById(R.id.edttxtTotalDiscount);
        txtFinalTotal = (TextView) findViewById(R.id.txtFinalTotal);
        txtbaseamount = (TextView) findViewById(R.id.txtbaseamount);
        txtcgstamt = (TextView) findViewById(R.id.txtcgstamt);
        txtsgstamt = (TextView) findViewById(R.id.txtsgstamt);
        txtroundoff = (TextView) findViewById(R.id.txtroundoff);
        txtyousave = (TextView) findViewById(R.id.txtyousave);
        txttotal = (TextView) findViewById(R.id.txttotal);
        llscroll =  findViewById(R.id.llscroll);
        SaveAndPrint = (Button) findViewById(R.id.SaveAndPrint);
        edttxtReceived = (EditText) findViewById(R.id.edttxtReceived);
        edttxtRemaining = (EditText) findViewById(R.id.edttxtRemaining);
        whatsapp = (ImageButton) findViewById(R.id.whatsapp);
        whatsapp.setClickable(true);
        mService = new BluetoothService(parent, mHandler);

        mService = new BluetoothService(ItemListCB.this, mHandler);
        if (mService.isAvailable() == false) {
            Toast.makeText(ItemListCB.this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ItemListCB.this);
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
        FirmMobile = ut.getValue(this, WebUrlClass.GET_MOBILE_KEY, settingKey).trim();
        // mprogress=findViewById(R.id.toolbar_progress_App_bar);

        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        cbillList = new ArrayList<CounterbillingBean>();
        addressLines = new ArrayList<String>();

        billNoClass = new BillNoClass();
        billNoClass.setBillNo("");
        billNoClass.setCbillList(cbillList);
    }

    private void setListeners(final int i) {

        edQnty.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(ItemListCB.this, "long click", Toast.LENGTH_LONG).show();
                final int pos = i;
                //  float subtotal_dialog=0;
                final EditText edt_qnty;
                Button btnok;
                if (pos < myCartBeanArrayList.size()) {
                    final Dialog dialog = new Dialog(ItemListCB.this);

                    dialog.setContentView(R.layout.tbuds_dialog_edt_qnty_cb);
                    dialog.setTitle("Update Quantity");
                    edt_qnty = (EditText) dialog.findViewById(R.id.editText_qnty);
                    btnok = (Button) dialog.findViewById(R.id.buttonok);
                    btnok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (((edt_qnty.getText().toString().trim() == "")
                                    || (edt_qnty.getText().toString() == null) || (edt_qnty.getText()
                                    .toString().length() == 0) || (edt_qnty.getText().toString().trim() == " "))) {
                                // edAmt.setText("0");
                                Toast.makeText(ItemListCB.this,
                                        "Enter quantity", Toast.LENGTH_LONG).show();

                            } else {

                                if (!myCartBeanArrayList.get(pos).getUNIT().equals("Select unit")) {
                                    if (myCartBeanArrayList.get(pos).getUnit_V().equalsIgnoreCase("kg")) {
                                        if (myCartBeanArrayList.get(pos).getUNIT().equals("kg")) {
                                            subtotal_dialog = (Float.parseFloat(edt_qnty.getText().toString().trim()))
                                                    * Float.parseFloat((myCartBeanArrayList.get(pos).getPrice()));

                                            //}
                                        } else if (myCartBeanArrayList.get(pos).getUNIT().equals("gm")) {

                                            subtotal_dialog = (Float.parseFloat(edt_qnty.getText().toString().trim())
                                                    * Float.parseFloat(myCartBeanArrayList.get(pos).getPrice())) / (1000 * qnty);


                                        } else {
                                            subtotal_dialog = 0;

                                            Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();

                                            // }
                                        }
                                    } else if (myCartBeanArrayList.get(pos).getUnit_V().equalsIgnoreCase("gm")) {


                                        if (myCartBeanArrayList.get(pos).getUNIT().equals("kg")) {
                                            subtotal_dialog = qnty * 1000 * Float.parseFloat(myCartBeanArrayList.get(pos).getPrice())
                                                    / (Float.parseFloat(edt_qnty.getText().toString().trim()));


                                        } else if (myCartBeanArrayList.get(pos).getUNIT().equals("gm")) {
                                            subtotal_dialog = (Float.parseFloat(edt_qnty.getText().toString().trim())
                                                    * Float.parseFloat(myCartBeanArrayList.get(pos).getPrice()));


                                        } else {
                                            subtotal_dialog = 0;

                                            Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();
                                            //}
                                        }

                                    } else if (myCartBeanArrayList.get(pos).getUnit_V().equalsIgnoreCase("item")) {
                                        if (myCartBeanArrayList.get(pos).getUNIT().equals("kg")) {
                                            subtotal_dialog = 0;

                                            Toast.makeText(ItemListCB.this, "kg unit not applicable", Toast.LENGTH_LONG).show();
                                            //  }
                                        } else if (myCartBeanArrayList.get(pos).getUNIT().equals("gm")) {
                                            subtotal_dialog = 0;

                                            Toast.makeText(ItemListCB.this, "gm unit not applicable", Toast.LENGTH_LONG).show();
                                            //  }

                                        } else {
                                            subtotal_dialog = (Float.parseFloat(edt_qnty.getText().toString().trim())
                                                    * Float.parseFloat(myCartBeanArrayList.get(pos).getPrice()));
                                        }
                                    }
                                }

                                //       SQLiteDatabase db = databaseHandler.getWritableDatabase();
                                ContentValues cv = new ContentValues();

                                cv.put("qnty", edt_qnty.getText().toString().trim());
                                cv.put("Amount", subtotal_dialog);


                                // cv.put("DISCOUNT", DISCOUNT);
                                // cv.put("UNIT", UNIT);
                                long q = sql_db.update(dbhandler.TABLE_CART_ITEM_CB, cv, "Product_id=?",
                                        new String[]{myCartBeanArrayList.get(pos).getProduct_id()});

                                dialog.dismiss();

                                containerLayout_one.removeAllViews();
                                for (int i = 0; i < tcf.getCartItems_AgainstCustomer(custName, custMob) + 1;
                                     i++) {
                                    addView(i);
                                }
                            }
                        }
                    });
                    dialog.show();
                }
                return false;
            }
        });

        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                flag_Amount = false;
                flag_selection = false;
                flag_Qnty = false;
                //    SQLiteDatabase db = databaseHandler.getWritableDatabase();
                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                myAutoComplete.setText(tv.getText().toString());
                String que = "SELECT price,Product_id,unit,qnty  FROM " + dbhandler.TABLE_PRODUCT_CB +
                        " WHERE Product_name='" + myAutoComplete.getText().toString() + "'";
                Cursor cur = sql_db.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    mrp = cur.getString(cur.getColumnIndex("price"));
                    p_id = cur.getString(cur.getColumnIndex("Product_id"));
                    unit = cur.getString(cur.getColumnIndex("unit"));
                    qnty = cur.getFloat(cur.getColumnIndex("qnty"));
                    edRate.setText(mrp);
                }
            }

        });
        spinnerunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                      if (!(edQnty.getText().toString().trim().equalsIgnoreCase("0"))
                                                              && !(edQnty.getText().toString().trim().equalsIgnoreCase(" "))
                                                              && !(edQnty.getText().toString().trim().equalsIgnoreCase(null))
                                                              && !(edQnty.getText().toString().trim().equalsIgnoreCase(""))) {
                                                          if (unit.equalsIgnoreCase("kg")) {
                                                              if (spinnerunit.getSelectedItem().equals("kg")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      float subtotal = (Float.parseFloat(edQnty.getText().toString().trim()))
                                                                              * Float.parseFloat(edRate.getText().toString().trim());
                                                                      edAmt.setText(Math.round(subtotal) + "");//₹
                                                                  }
                                                              } else if (spinnerunit.getSelectedItem().equals("gm")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      // float kg_to_gm = 1000 * Float.parseFloat(edRate.getText().toString().trim());
                                                                      float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                                              * Float.parseFloat(edRate.getText().toString().trim())) / (1000 * qnty);
                                                                      edAmt.setText(Math.round(subtotal) + "");//₹
                                                                  }

                                                              } else {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      edAmt.setText("0");
                                                                      Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();

                                                                  }
                                                              }
                                                          } else if (unit.equalsIgnoreCase("gm")) {


                                                              if (spinnerunit.getSelectedItem().equals("kg")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      float subtotal = qnty * 1000 * Float.parseFloat(edRate.getText().toString().trim())
                                                                              / (Float.parseFloat(edQnty.getText().toString().trim()));
                                                                      edAmt.setText(Math.round(subtotal) + "");//₹
                                                                  }

                                                              } else if (spinnerunit.getSelectedItem().equals("gm")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;

                                                                      float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                                              * Float.parseFloat(edRate.getText().toString().trim()));
                                                                      edAmt.setText(Math.round(subtotal) + "");//₹
                                                                  }

                                                              } else {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      edAmt.setText("0");
                                                                      Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();
                                                                  }
                                                              }

                                                          } else if (unit.equalsIgnoreCase("item")) {
                                                              if (spinnerunit.getSelectedItem().equals("kg")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      edAmt.setText("0");
                                                                      Toast.makeText(ItemListCB.this, "kg unit not applicable", Toast.LENGTH_LONG).show();
                                                                  }
                                                              } else if (spinnerunit.getSelectedItem().equals("gm")) {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      edAmt.setText("0");
                                                                      Toast.makeText(ItemListCB.this, "gm unit not applicable", Toast.LENGTH_LONG).show();
                                                                  }

                                                              } else {
                                                                  if (flag_Amount == true) {

                                                                  } else {
                                                                      flag_selection = true;
                                                                      float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                                              * Float.parseFloat(edRate.getText().toString().trim()));
                                                                      edAmt.setText(Math.round(subtotal) + "");//₹
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {

                                                  }
                                              }
        );

        edAmt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                int pos = i;
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {
                    edAmt.setText("0");
                } else {
                    if (flag_selection == true) {

                    } else {
                        flag_Amount = true;

                        edQnty.setText("");
                        spinnerunit.setSelection(0);
                        float subtotal = ((Float.parseFloat(s.toString()) * qnty)
                                / Float.parseFloat(mrp));
                        edQnty.setText("" + subtotal);
                        flag_Qnty = true;
                        if (unit.equalsIgnoreCase("kg")) {
                            spinnerunit.setSelection(2);
                        } else if (unit.equalsIgnoreCase("gm")) {
                            spinnerunit.setSelection(1);
                        } else if (unit.equalsIgnoreCase("item")) {
                            spinnerunit.setSelection(3);
                        }
                    }
                }
            }
        });

        int textLength = edQnty.getText().length();
        edQnty.setSelection(textLength, textLength);
        edQnty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int pos = i;
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {
                    //  edAmt.setText("0");
                } else {
                    if (pos >= myCartBeanArrayList.size() + 1) {

                    } else {
                        if (flag_Qnty == false) {
                            if (!spinnerunit.getSelectedItem().equals("Select unit")) {
                                if (unit.equalsIgnoreCase("kg")) {
                                    if (spinnerunit.getSelectedItem().equals("kg")) {
                              /*  if (flag_Amount == true) {

                                } else {*/
                                        // flag_selection = true;
                                        float subtotal = (Float.parseFloat(edQnty.getText().toString().trim()))
                                                * Float.parseFloat(edRate.getText().toString().trim());
                                        edAmt.setText(Math.round(subtotal) + "");//₹
                                        //}
                                    } else if (spinnerunit.getSelectedItem().equals("gm")) {
                               /* if (flag_Amount == true) {

                                } else {*/
                                        //   flag_selection = true;
                                        // float kg_to_gm = 1000 * Float.parseFloat(edRate.getText().toString().trim());
                                        float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                * Float.parseFloat(edRate.getText().toString().trim())) / (1000 * qnty);
                                        edAmt.setText(Math.round(subtotal) + "");//₹
                                        //  }

                                    } else {
                                /*if (flag_Amount == true) {

                                } else {*/
                                        //   flag_selection = true;
                                        edAmt.setText("0");
                                        Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();

                                        // }
                                    }
                                } else if (unit.equalsIgnoreCase("gm")) {


                                    if (spinnerunit.getSelectedItem().equals("kg")) {
                               /* if (flag_Amount == true) {

                                } else {*/
                                        //   flag_selection = true;
                                        float subtotal = qnty * 1000 * Float.parseFloat(edRate.getText().toString().trim())
                                                / (Float.parseFloat(edQnty.getText().toString().trim()));
                                        edAmt.setText(Math.round(subtotal) + "");//₹
                                        //   }

                                    } else if (spinnerunit.getSelectedItem().equals("gm")) {
                               /* if (flag_Amount == true) {

                                } else {*/
                                        //   flag_selection = true;

                                        float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                * Float.parseFloat(edRate.getText().toString().trim()));
                                        edAmt.setText(Math.round(subtotal) + "");//₹
                                        //  }

                                    } else {
                              /*  if (flag_Amount == true) {

                                } else {*/
                                        //  flag_selection = true;
                                        edAmt.setText("0");
                                        Toast.makeText(ItemListCB.this, "item unit not applicable", Toast.LENGTH_LONG).show();
                                        //}
                                    }

                                } else if (unit.equalsIgnoreCase("item")) {
                                    if (spinnerunit.getSelectedItem().equals("kg")) {
                              /*  if (flag_Amount == true) {

                                } else {*/
                                        //    flag_selection = true;
                                        edAmt.setText("0");
                                        Toast.makeText(ItemListCB.this, "kg unit not applicable", Toast.LENGTH_LONG).show();
                                        //  }
                                    } else if (spinnerunit.getSelectedItem().equals("gm")) {
                              /*  if (flag_Amount == true) {

                                } else {*/
                                        //    flag_selection = true;
                                        edAmt.setText("0");
                                        Toast.makeText(ItemListCB.this, "gm unit not applicable", Toast.LENGTH_LONG).show();
                                        //  }

                                    } else {
                              /*  if (flag_Amount == true) {

                                } else {*/
                                        //    flag_selection = true;
                                        float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                * Float.parseFloat(edRate.getText().toString().trim()));
                                        edAmt.setText(Math.round(subtotal) + "");//₹
                                        //   }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        int textLength1 = edDiscount.getText().length();
        edDiscount.setSelection(textLength1, textLength1);

        edDiscount.addTextChangedListener(new TextWatcher() {

                                              @Override
                                              public void onTextChanged(CharSequence s, int start, int before,
                                                                        int count) {
                                              }

                                              @Override
                                              public void beforeTextChanged(CharSequence s, int start, int count,
                                                                            int after) {
                                              }

                                              @Override
                                              public void afterTextChanged(Editable s) {


                                                  int pos = i;
                                                  if (((s.toString().trim() == "") || (s.toString() == null) || (s
                                                          .toString().length() == 0) || (s.toString().trim() == " "))) {

                                                  } else {

                                                      if (Integer.parseInt(s.toString()) > 0 && Integer.parseInt(s.toString()) <= 10) {
                                                          float subtotal = (Float.parseFloat(edQnty.getText().toString().trim())
                                                                  * Float.parseFloat(edRate.getText().toString().trim()));
                                                          edAmt.setText(Math.round(subtotal) + "");//₹
                   /* amt = amt + subtotal;
                    txtTotal.setText("" + amt);*/
                                                          float dis_amt = (subtotal * Float.parseFloat(s.toString()) / 100);

                                                          edAmt.setText(Math.round(subtotal - dis_amt) + "");//₹
                                                          // txtTotal.setText("" + (amt - (subtotal - dis_amt)));
                                                      } else {
                                                          Toast.makeText(parent, "Discount range should be 0 to 10 ",
                                                                  Toast.LENGTH_LONG).show();
                                                      }
                                                  }
                                              }
                                          }
        );

        btn_ok.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          if (tcf.getItems_AgainstCustomer(custName, custMob, p_id) == 0) {
                                              txtTotal.setText("" + amt);
                                              if (edQnty.getText().toString().trim().length() > 0
                                                      && !(edQnty.getText().toString().trim().equalsIgnoreCase("0"))
                                                      && myAutoComplete.getText().toString().length() > 0) {
                                                  Toast.makeText(ItemListCB.this, "Item added", Toast.LENGTH_LONG).show();

                                                  tcf.addCartItems(custMob, custName, edQnty.getText().toString().trim(), "",
                                                          "", edRate.getText().toString().trim(), myAutoComplete.getText().toString(), edAmt.getText().toString(),
                                                          p_id, "", "", "", "", "",
                                                          edDiscount.getText().toString().trim()/*,spinnerunit.getSelectedItem().toString(),unit*/);

                                                  containerLayout_one.removeAllViews();
                                                  for (int i = 0; i < tcf.getCartItems_AgainstCustomer(custName, custMob) + 1;
                                                       i++) {
                                                      addView(i);
                                                  }

                                                  amt = 0;
                                                  if (myCartBeanArrayList.size() > 0) {
                                                      for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                                                          amt = amt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(j).getAmount()));

                                                      }
                                                  }
                                                  txtTotal.setText("" + Math.round(amt));

                                              } else {
                                                  Toast.makeText(ItemListCB.this, "Enter valid details", Toast.LENGTH_LONG).show();
                                              }

                                          } else {
                                              Toast.makeText(ItemListCB.this, "Item already added", Toast.LENGTH_LONG).show();
                                          }
                                      }
                                  }
        );

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = i;
                Float q = Float.valueOf(edQnty.getText().toString());
                if (pos <= myCartBeanArrayList.size()) {
                    if (!(myCartBeanArrayList.get(pos).getQnty().equals("0"))
                            && !(myCartBeanArrayList.get(pos).getQnty().equals(" "))) {
                        Toast.makeText(ItemListCB.this, "Item deleted", Toast.LENGTH_LONG).show();

                        //  SQLiteDatabase db = databaseHandler.getWritableDatabase();
                        sql_db.execSQL("DELETE FROM " + dbhandler.TABLE_CART_ITEM_CB +
                                " WHERE Cust_Name='" + custName +
                                "' and Product_name='" + myCartBeanArrayList.get(pos).getProduct_name() + "'");
                        sql_db.close();
                        containerLayout_one.removeAllViews();
                        for (int i = 0; i < tcf.getCartItems_AgainstCustomer(custName, custMob) + 1; i++) {
                            addView(i);
                        }
                    } else {
                        Toast.makeText(ItemListCB.this, "Enter Item details", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ItemListCB.this, "Enter Item details", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
            WarehouseMasterId = sharedpreferences.getString("WareHouseMasterId","");
            WarehouseDescription = sharedpreferences.getString("WarehouseDescription","");
            LocationMasterId = sharedpreferences.getString("LocationMasterId","");
            LocationDesc = sharedpreferences.getString("LocationDesc","");
            OrderTypeId = sharedpreferences.getString("OrderTypeId","");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // count = 0;

        if (mService.isBTopen() == false) {
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent,
                    AnyMartData.REQUEST_ENABLE_BT);
        }
    }

    private void connectDevice() {
        // TODO
        String address = getBluetoothAddress(parent);
        if (address != null) {
            con_dev = mService.getDevByMac(address);
            mService.connect(con_dev);
            Log.e("Auto connected", "state : " + mService.getState());
        } else {
            scanBluetooth();
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg1) {
            switch (msg1.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg1.arg1) {
                        case BluetoothService.STATE_CONNECTED: // ÒÑÁ¬½Ó
                            Toast.makeText(parent, "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            deviceConnected = true;
                            break;
                        case BluetoothService.STATE_CONNECTING: // ÕýÔÚÁ¬½Ó
                            Log.d("À¶ÑÀµ÷ÊÔ", "ÕýÔÚÁ¬½Ó.....");
                            break;
                        case BluetoothService.STATE_LISTEN: // ¼àÌýÁ¬½ÓµÄµ½À´
                        case BluetoothService.STATE_NONE:
                            Log.d("À¶ÑÀµ÷ÊÔ", "µÈ´ýÁ¬½Ó.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST: // À¶ÑÀÒÑ¶Ï¿ªÁ¬½Ó
                    Toast.makeText(parent, "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT: // ÎÞ·¨Á¬½ÓÉè±¸
                    Toast.makeText(parent, "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    deviceConnected = false;
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            BluetoothClass.pairPrinter(getApplicationContext(), ItemListCB.this);
            //isConnected = true;

        }else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode ==  RESULT_OK) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS).split("\n")[1];
            BluetoothClass.pairedPrinterAddress(getApplicationContext(), ItemListCB.this,address);
        }

        /*switch (requestCode) {
            case AnyMartData.REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(parent, "Bluetooth open successful", Toast.LENGTH_LONG).show();
                } else {
                    // finish();
                }
                break;
            case AnyMartData.REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(
                            DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    clearTable(parent, "Bluetooth_Address");
                    tcf.AddBluetooth(address);
                    con_dev = mService.getDevByMac(address);
                    mService.connect(con_dev);
                    Log.e("bluetooth state", "state : " + mService.getState());
                }
                break;
        }*/
    }

    public static void clearTable(Context parent, String tablename) {
        //   DatabaseHandler db = new DatabaseHandler(parent);
        //   SQLiteDatabase sql = db.getWritableDatabase();
        sql_db.delete(tablename, null, null);

        /*sql.close();
        db.close();*/
    }

    public static String getBluetoothAddress(Context parent) {
        //  DatabaseHandler db1 = new DatabaseHandler(parent);
        //  SQLiteDatabase sql = db1.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery("Select * from Bluetooth_Address", null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String str = cursor.getString(0);
            /*cursor.close();
            sql.close();
            db1.close();*/
            return str;

        } else {

            /*cursor.close();
            sql.close();
            db1.close();*/
            return null;
        }
    }

    public void getItmsListDataForBilling() {
        cbillList.clear();
        final_subtotal = 0.0F;
        final_discountedTotal = 0.0F;
        final_taxinRupsTotal = 0.0F;
        float _youSave = 0.0f, _totMRP = 0.0f, _rndOffDiff = 0.0f;

        String igstType = "", sgstType = "", cgstType = "",ugstType = "",vatType = "",
                sgstVal = "0", cgstVal = "0", igstVal = "0", ugstVal = "0", vatVal = "0";
        float cgst = 0.0F;
        String CGST = "0", SGST = "0", IGST = "0";

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

        if(cbillList.size() > 0){
            titlecgst.setText("CGST "+cgstVal+" %:");
            titlesgst.setText("SGST "+sgstVal+" %:");
        }

        String query = "Select * from " + dbhandler.TABLE_ADD_ITMDTLS_FORBILL + " WHERE isbilluploaded='N'";
        Cursor c = sql_db.rawQuery(query, null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            do {
                try {
                    CounterbillingBean cbean = new CounterbillingBean();
                    cbean.setItemCode(c.getString(c.getColumnIndex("itmcode")));
                    cbean.setItemDesc(c.getString(c.getColumnIndex("itmdesc")));
                    cbean.setQty(c.getString(c.getColumnIndex("qnty")));
                    cbean.setRate(Float.parseFloat(c.getString(c.getColumnIndex("rate"))));
                    cbean.setMRP(Float.parseFloat(c.getString(c.getColumnIndex("mrp"))));
                    cbean.setLineamt(Float.parseFloat(c.getString(c.getColumnIndex("lineamt"))));
                    cbean.setDiscount(Float.parseFloat(c.getString(c.getColumnIndex("discount"))));
                    cbean.setTaxclass(c.getString(c.getColumnIndex("taxclass")));
                    cbean.setTax_inRups(c.getString(c.getColumnIndex("taxamtinrups")));
                    cbean.setDiscinrupees(Boolean.parseBoolean(c.getString(c.getColumnIndex("isdiscinrupees"))));
                    cbean.setTotAmt_incltax_lineamt(Float.parseFloat(c.getString(c.getColumnIndex("total_incl_taxanddisc"))));
                    cbean.setDiscamt(Float.parseFloat(c.getString(c.getColumnIndex("discamt"))));
                    cbean.setDicountedTotal(Float.parseFloat(c.getString(c.getColumnIndex("totwithdisc"))));

                    cbillList.add(cbean);

                    float sbtot = Float.parseFloat(c.getString(c.getColumnIndex("total_incl_taxanddisc")));
                    final_subtotal = final_subtotal + sbtot;

                    float discountedTotal = Float.parseFloat(c.getString(c.getColumnIndex("totwithdisc")));
                    final_discountedTotal = final_discountedTotal + discountedTotal;

                    float taxinrupsTotal = Float.parseFloat(c.getString(c.getColumnIndex("taxamtinrups")));
                    final_taxinRupsTotal = final_taxinRupsTotal + taxinrupsTotal;

                  //  _totMRP = _totMRP + Float.parseFloat(c.getString(c.getColumnIndex("mrp")));
                    _totMRP = _totMRP +
                            (Float.parseFloat(c.getString(c.getColumnIndex("mrp"))) * Float.parseFloat(c.getString(c.getColumnIndex("qnty"))));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());

            cBillAdapter = new AdapterCBillingList(this, cbillList);
            listdtls.setAdapter(cBillAdapter);
            billNoClass.setCbillList(cbillList);
            setListViewHeightBasedOnChildren(listdtls,1);

            String billingObj = new Gson().toJson(billNoClass);
            AppCommon.getInstance(this).setBillingObject(billingObj);

            float _cgst = 0.0F, _CGST = 0.0F, _SGST = 0.0F;

            _cgst = final_taxinRupsTotal;
            _CGST = Float.parseFloat(String.format("%.02f", _cgst / 2));
            _SGST = Float.parseFloat(String.format("%.02f", _cgst / 2));

            _total = final_discountedTotal+_CGST+_SGST;
            txttotal.setText(String.format("%.2f",_total));

            txtbaseamount.setText(String.format("%.02f", final_discountedTotal));
            txtcgstamt.setText(String.format("%.02f", _CGST));
            txtsgstamt.setText(String.format("%.02f", _SGST));
        } else {
            if(!cbillList.isEmpty()){
                cBillAdapter.notifyDataSetChanged();
            }

            final_subtotal = 0.00F;
            txtsubtotal.setText("0.00");
            Totalincltax.setText("0.00");
            txtNetAmt.setText("0.00");
            edttxtTotalDiscount.setText("0.00");
            txtpaybleamt.setText("0.00");
            txtbalamt.setText("0.00");
            txtcgstamt.setText("0.00");
            txtsgstamt.setText("0.00");
        }

        txtsubtotal.setText(String.format("%.02f", final_subtotal));  //get total from listview
        Totalincltax.setText(txtsubtotal.getText().toString().trim());
        txtbalamt.setText(Totalincltax.getText().toString().trim());
        txtpaybleamt.setText(txtNetAmt.getText().toString().trim());
        //txtNetAmt.setText(Totalincltax.getText().toString().trim());
        txtNetAmt.setText(roundOff(Float.parseFloat(Totalincltax.getText().toString())));
        //txtpaybleamt.setText(txtNetAmt.getText().toString().trim());
        txtpaybleamt.setText(roundOff(Float.parseFloat(txtNetAmt.getText().toString())));

        _rndOffDiff = Float.parseFloat(txtNetAmt.getText().toString()) - _total;
        if(String.format("%.2f",_rndOffDiff).equalsIgnoreCase("-0.00")){
            txtroundoff.setText("0.00");
        }else {
            txtroundoff.setText(String.format("%.2f",_rndOffDiff));
        }
        //txtroundoff.setText(roundOff(Float.parseFloat(Totalincltax.getText().toString())));

        _youSave = _totMRP - Float.parseFloat(txtpaybleamt.getText().toString());
        txtyousave.setText(String.format("%.2f",_youSave));

        if(cbillList.size() > 0){
            titlecgst.setText("CGST "+cgstVal+" %:");
            titlesgst.setText("SGST "+sgstVal+" %:");
        }

    }

    private void setData(ArrayList<CounterbillingBean> cbillList) {
        final_subtotal = 0.0F;
        final_discountedTotal = 0.0F;
        final_taxinRupsTotal = 0.0F;
        float _totMRP = 0.0f, _youSave = 0.0f, _rndOffDiff = 0.0f;

        for (CounterbillingBean counterbillingBean : cbillList) {
            float sbtot = counterbillingBean.getTotAmt_incltax_lineamt();
            final_subtotal = final_subtotal + sbtot;

            float discountedTotal = counterbillingBean.getDicountedTotal();
            final_discountedTotal = final_discountedTotal + discountedTotal;

            float taxinrupsTotal = Float.parseFloat(counterbillingBean.getTax_inRups());
            final_taxinRupsTotal = final_taxinRupsTotal + taxinrupsTotal;

            _totMRP = _totMRP + (counterbillingBean.getMRP() * Float.parseFloat(counterbillingBean.getQty()));

            custName = counterbillingBean.getCustName();
            custMob = counterbillingBean.getMobileNo();
            edtcustname.setText(custName);
            edtcustMob.setText(custMob);
            edtcustgstn.setText(counterbillingBean.getCustGSTN());
            edtgstn.setText(counterbillingBean.getCmpnyGSTN());
        }

        float cgst = 0.0F, CGST = 0.0F, SGST = 0.0F;
        cgst = final_taxinRupsTotal;
        CGST = Float.parseFloat(String.format("%.02f", cgst / 2));
        SGST = Float.parseFloat(String.format("%.02f", cgst / 2));

        txtbaseamount.setText(String.format("%.02f", final_discountedTotal));
        txtcgstamt.setText(String.format("%.02f", CGST));
        txtsgstamt.setText(String.format("%.02f", SGST));

        CGST_TOTAL = CGST;
        SGST_TOTAL = SGST;

        _total = final_discountedTotal+CGST+SGST;
        txttotal.setText(String.format("%.2f",_total));

        txtsubtotal.setText(String.format("%.02f", final_subtotal));  //get total from listview
        Totalincltax.setText(txtsubtotal.getText().toString().trim());
        txtbalamt.setText(Totalincltax.getText().toString().trim());
        //txtNetAmt.setText(Totalincltax.getText().toString().trim());
        txtNetAmt.setText(roundOff(Float.parseFloat(Totalincltax.getText().toString())));
        //txtpaybleamt.setText(txtNetAmt.getText().toString().trim());
        txtpaybleamt.setText(roundOff(Float.parseFloat(txtNetAmt.getText().toString())));

        _rndOffDiff = Float.parseFloat(txtNetAmt.getText().toString()) - _total;
        if(String.format("%.2f",_rndOffDiff).equalsIgnoreCase("-0.00")){
            txtroundoff.setText("0.00");
        }else {
            txtroundoff.setText(String.format("%.2f",_rndOffDiff));
        }
        //txtroundoff.setText(roundOff(Float.parseFloat(Totalincltax.getText().toString())));

        _youSave = _totMRP - Float.parseFloat(txtpaybleamt.getText().toString());
        txtyousave.setText(String.format("%.2f",_youSave));

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

    public void insertDataInTempTable(){

        for(int position=0; position< cbillList.size(); position++){
            String itemcode = cbillList.get(position).getItemCode();
            String itemdesc = cbillList.get(position).getItemDesc();
            String qty = cbillList.get(position).getQty();
            String mrp = String.valueOf(cbillList.get(position).getMRP());
            String rate = String.valueOf(cbillList.get(position).getRate());
            String lineamt = String.valueOf(cbillList.get(position).getLineamt());
            float discount = cbillList.get(position).getDiscount();
            String taxclass = cbillList.get(position).getTaxclass();
            String taxamtinrps = cbillList.get(position).getTax_inRups();
            String totinctax = String.valueOf(cbillList.get(position).getTotAmt_incltax_lineamt());
            boolean discinrups = cbillList.get(position).isDiscinrupees();
            String discamt = String.valueOf(cbillList.get(position).getDiscamt());

            Float totline_disc = Float.parseFloat(lineamt) - Float.parseFloat(discamt);

            tcf.additmforbilling(itemcode, itemdesc, qty, String.valueOf(mrp),rate,String.valueOf(lineamt),String.valueOf(discount),
                    String.valueOf(taxclass), String.valueOf(taxamtinrps),String.valueOf(totinctax),"",
                    String.valueOf(totline_disc),"N",String.valueOf(discinrups), String.valueOf(discamt));  //taxType IGST SGST CGST
        }

    }

    public void getCompanyDetailsData(){

        String query = "Select Cid, Cname, Address from "+ dbhandler.TABLE_COMPANY_DETAILS;
        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() != 0){
            c.moveToFirst();
            do{
                Cid = c.getString(c.getColumnIndex("Cid"));
                Cname = c.getString(c.getColumnIndex("Cname"));
                factoryAddress = c.getString(c.getColumnIndex("Address"));

                if(Cname.contains("Meeshna")){
                    regAddress = "B-13, Shri Ganesh Appt., Gujrat Colony, Kothrud, Pune-411038";
                }else {

                }

            }while (c.moveToNext());

        }else {

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

    class DownloadCompanyDetailsJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mprogress.setVisibility(View.VISIBLE);
            Toast.makeText(parent,"Downloading company details", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getCompanyDetails + "?CompanyName="+EnvMasterId+"&PlantMasterId="+PlantMasterId;

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
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
            mprogress.setVisibility(View.GONE);

            try{
                if(jResults != null){
                    tcf.clearTable(parent, DatabaseHandlers.TABLE_COMPANY_DETAILS);

                    String Cid = "", Cname = "", Address = "";

                    for(int i=0; i < jResults.length(); i++){
                        try{
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            Cid = jsonObject.getString("Cid");
                            Cname = jsonObject.getString("Cname");
                            Address = jsonObject.getString("Address");

                            tcf.insertCompanyDetails(Cid, Cname, Address);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }else {
                }

                getCompanyDetailsData();

            }catch (Exception e){

            }
        }
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

        getItmsListDataForBilling();
    }

    public void splitAddressInLines(String name){
        addressLines.clear();

        float diff = 0.0F, cnt = 0.0F, left = 0.0F, right = 0.0F, space = 0.0F;
        //name = "7, Second floor, Krishna Ganga Apartment, Parijat Corner, Gulmohar Road, Savedi,";
        //String name = "Vritti Solutions Limited";
        float length = name.length();
        float tab = 4;
        left = length/2;
        right = length - left;

        System.out.println("length = "+length);

        if(length <= 32){
            diff = 32 - length;
            cnt = length/tab;
            left = diff/2;
            right = diff - left;

            System.out.println("diff =32-length = "+diff);
            String format = "%" + left + "c%-" + right + "c";
            System.out.println(format);
            System.out.println("*"+"\t"+name+"\t"+"*");

        }else if(length > 32){
            String[] arr;
            diff = length;
            diff = diff - 24;
            System.out.println("diff = length - 24 = "+diff);
            diff = diff - 24;
            System.out.println("diff = diff - 24; = "+diff);
            diff = diff - 24;
            System.out.println("diff = diff - 24; = "+diff);

            System.out.println("*"+"\t"+name+"\t"+"*");

            char[] data = name.toCharArray();
            System.out.println(data.length);
            String printline = "", print = "";

            int linecnt = data.length/24;
            System.out.println(linecnt);
            int reminder = data.length - (linecnt*24);
            System.out.println(reminder);
            int start=0, end =24;
            name.substring(start, end);

            int a = linecnt + 1;

            for(int j =0; j<a; j++){

                print = name.substring(start, end);

                if(length >= 24){

                    if(end < data.length){
                        start = start + 24;
                        if(end +24 <= data.length){
                            end = end +24;
                        }else{
                            end = end +reminder;
                        }

                    }else{
                        start = start + reminder;
                        end = end + reminder;
                    }

                    length = length - 24;
                }else if(length < 23){
                    start = start + reminder;
                    end = end + reminder;
                }

                System.out.println("*\t"+print+"\t*");
                addressLines.add(print);
            }
        }
    }

    public void createJSON(){
        jMain = getDataJson();
        finalData = jMain.toString().replaceAll("\\\\", "");
        finalData = finalData.replaceAll("^\"+ \"+$","");
        String a = "\"[";
        finalData = finalData.replace(a,"[");
        String b = "]\"";
        finalData = finalData.replace(b,"]");

        //call API to post json then finish screen
        if (isnet()) {
            new StartSession(ItemListCB.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostCounterBill().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    public JSONObject getDataJson(){
        JSONObject billJson = null;

        SoScheduleId = UUID.randomUUID().toString();

        //shipment array
        JSONArray jsonArray_shipment = new JSONArray();
        try{
            //put jsonarr.put() inside for loop
            for(int i=0; i<cbillList.size();i++){
                jsonArray_shipment.put(getShipmentArr(i));
            }
            ShipmentArr = jsonArray_shipment.toString().replaceAll("\\\\", "");
        }catch (Exception e){
            e.printStackTrace();
        }

        //main1 array
        JSONArray jsonArray_main1 = new JSONArray();
        try{
            //single array obj
            jsonArray_main1.put(getMainArr1());
            MainArr1 = jsonArray_main1.toString().replaceAll("\\\\", "");
        }catch (Exception e){
            e.printStackTrace();
        }

        removedarr = String.valueOf(new JSONArray());
        Chargedt = String.valueOf(new JSONArray());
        BoxData = String.valueOf(new JSONArray());

        //chalan array
        JSONArray jsonArray_chalan = new JSONArray();
        try{
            //single array obj
            jsonArray_chalan.put(getChalanArray());
            ChalanArray = jsonArray_chalan.toString().replaceAll("\\\\", "");
        }catch (Exception e){
            e.printStackTrace();
        }

        billJson = new JSONObject();
        try{
            billJson.put("ShipmentArr",ShipmentArr);
            billJson.put("MainArr1",MainArr1);
            billJson.put("removedarr",removedarr);
            billJson.put("Chargedt",Chargedt);
            billJson.put("BoxData",BoxData);
            billJson.put("ChalanArray",ChalanArray);
        }catch (Exception e){
            e.printStackTrace();
            billJson = null;
        }

        return billJson;
    }

    public JSONObject getShipmentArr(int i){
        JSONObject shipJson = null;

        ItemMasterId = getItemMasterid(cbillList.get(i).getItemCode());
        ItemCode = cbillList.get(i).getItemCode();
        ItemDesc = cbillList.get(i).getItemDesc();
        SalesQty = cbillList.get(i).getQty();
        TotalQty = cbillList.get(i).getQty();
        SalesRate = String.format("%.2f",cbillList.get(i).getMRP());
        TaxClassDesc = cbillList.get(i).getTaxclass();
        getTaxDtls(TaxClassDesc);
        FLineAmt= String.valueOf(cbillList.get(i).getDicountedTotal());
        LineAmt = String.valueOf(cbillList.get(i).getDicountedTotal());
        FLineTaxes = String.valueOf(cbillList.get(i).getDicountedTotal());
        FLineTotal = String.valueOf(cbillList.get(i).getDicountedTotal());
        LineTaxes = String.valueOf(cbillList.get(i).getDicountedTotal());
        LineTotal = String.valueOf(cbillList.get(i).getDicountedTotal());
        Rate = String.format("%.02f",cbillList.get(i).getRate());
        DiscPC = String.valueOf(cbillList.get(i).getDiscount());
        DiscAmntFC = String.valueOf(cbillList.get(i).getDiscamt());
        DiscAmt = String.valueOf(cbillList.get(i).getDiscamt());
        Qty=cbillList.get(i).getQty();
        TotalAmnt= txtpaybleamt.getText().toString().trim();
        TotalTaxAmnt=String.format("%.02f", final_taxinRupsTotal);;
        TotalGrossAmnt = TotalAmnt;
        CommittedDt = getCommittedDate();

        JSONObject jObj_shipment = new JSONObject();
        try{
            jObj_shipment.put("SoScheduleId",SoScheduleId);
            jObj_shipment.put("OrderTypeId",OrderTypeId);
            jObj_shipment.put("CommittedDt",CommittedDt);   //18-11-2020 12:00:00 AM
            jObj_shipment.put("ItemMasterId",ItemMasterId); //9389
            jObj_shipment.put("ItemCode",ItemCode);
            jObj_shipment.put("ItemDesc",ItemDesc);
            jObj_shipment.put("BatchNo",BatchNo);
            jObj_shipment.put("LocationMasterId",LocationMasterId); //483
            jObj_shipment.put("LocationDesc",LocationDesc); //Jaipur
            jObj_shipment.put("WarehouseMasterId",WarehouseMasterId);   //25D05C73-B283-4233-A5D6-2E07C6552C62
            jObj_shipment.put("WarehouseDescription",WarehouseDescription); //Vritti Solutions Ltd Pune
            jObj_shipment.put("SONo",SONo); //1
            jObj_shipment.put("SalesQty",SalesQty); //1
            jObj_shipment.put("SalesRate",SalesRate);   //100
            jObj_shipment.put("TaxClassMasterId",TaxClassMasterId); //1070
            jObj_shipment.put("TaxClassCode",TaxClassCode); //TXC0069
            jObj_shipment.put("TaxClassDesc",TaxClassDesc); //SGST 2.5% + CGST 2.5% INPUT
            jObj_shipment.put("FLineAmt",FLineAmt);     //90.48
            jObj_shipment.put("LineAmt",LineAmt);       //90.48
            jObj_shipment.put("FLineTaxes",FLineTaxes); //90.48
            jObj_shipment.put("FLineTotal",FLineTotal); //90.48
            jObj_shipment.put("LineTaxes",LineTaxes);   //90.48
            jObj_shipment.put("LineTotal",LineTotal);   //90.48
            jObj_shipment.put("DiscPC",DiscPC);     //5
            jObj_shipment.put("DiscAmntFC",DiscAmntFC);     //4.76
            jObj_shipment.put("DiscAmt",DiscAmt);       //4.76
            jObj_shipment.put("Qty",Qty);       //1
            jObj_shipment.put("Rate",Rate);     //90.48
            jObj_shipment.put("CustOrderPONo",CustOrderPONo);
            jObj_shipment.put("SOHeaderId",SOHeaderId);
            jObj_shipment.put("ChargeAmntFC",ChargeAmntFC);
            jObj_shipment.put("ChargeAmnt",ChargeAmnt);
            jObj_shipment.put("BasicTaxAmntFC",BasicTaxAmntFC);     //0
            jObj_shipment.put("ChargeTaxAmntFC",ChargeTaxAmntFC);
            jObj_shipment.put("ChargeTaxAmnt",ChargeTaxAmnt);
            jObj_shipment.put("TotalAmnt",TotalAmnt);       //95
            jObj_shipment.put("TotalTaxAmnt",TotalTaxAmnt);     //4.52
            jObj_shipment.put("TotalTaxAmntFC",TotalTaxAmntFC);
            jObj_shipment.put("TotalGrossAmnt",TotalGrossAmnt);     //90.48
            jObj_shipment.put("TotalGrossAmntFC",TotalGrossAmntFC);
            jObj_shipment.put("ItmClsDesc",ItmClsDesc);
            jObj_shipment.put("MarksNo",MarksNo);
            jObj_shipment.put("ContDimensn",ContDimensn);
            jObj_shipment.put("ContGrossWt",ContGrossWt);
            jObj_shipment.put("ContNetWt",ContNetWt);
            jObj_shipment.put("NoKindPkgs",NoKindPkgs);
            jObj_shipment.put("BoxNo",BoxNo);
            jObj_shipment.put("SeqNo",SeqNo);
            jObj_shipment.put("LicenseNo",LicenseNo);
            jObj_shipment.put("Remark",Remark);
            jObj_shipment.put("ExemptMaterialAndNetWt",ExemptMaterialAndNetWt);
            jObj_shipment.put("QtyPerCont",QtyPerCont);
            jObj_shipment.put("NoofCont",NoofCont);
            jObj_shipment.put("SalesDtlId",SalesDtlId);
            jObj_shipment.put("ItemClass",ItemClass);
            jObj_shipment.put("TechDesc",TechDesc);
            jObj_shipment.put("Transporter",Transporter);
            jObj_shipment.put("NOOfBoxes",NOOfBoxes);
            jObj_shipment.put("QtyPerBox",QtyPerBox);
            jObj_shipment.put("TotalQty",TotalQty);

            shipJson = jObj_shipment;

        }catch (Exception e){
            e.printStackTrace();
        }

        return shipJson;
    }

    public JSONObject getMainArr1(){
        JSONObject mainJson = null;

        SalesHeaderId = UUID.randomUUID().toString();
        CustomerId = UUID.randomUUID().toString();
        ShipToMasterId = UUID.randomUUID().toString();
        BillToId = ShipToMasterId;
        FLineTaxes =  txtbaseamount.getText().toString().trim();
        GrossAMT = FLineTaxes;
        DiscountAMNTFC = GrossAMT;
        ChargeTaxAmount = DiscountAMNTFC;
        DiscPC = DiscPC;
        DiscAMT = txtyousave.getText().toString().trim();
        ConsigneeName = custName;
        LineCount =  String.valueOf(cbillList.size());

        ShipmentDt = getShipmntDate();
        RefDt = ShipmentDt;

        JSONObject jObj_mainarr = new JSONObject();
        try{
            jObj_mainarr.put("SalesHeaderId",SalesHeaderId);    //91A17A81-EEA8-4FB9-8B14-31F708D7B354
            jObj_mainarr.put("OrderTypeId",OrderTypeId);        //1056
            jObj_mainarr.put("ShipmentDt",ShipmentDt);      //2020-11-18 12:00:00 AM
            jObj_mainarr.put("CustomerId",CustomerId);      //C1AFB85B-ED39-43D6-B3EC-81B8C7E5D130
            jObj_mainarr.put("DispatchNo",DispatchNo);
            jObj_mainarr.put("DeliveryMode",DeliveryMode);
            jObj_mainarr.put("FreightPayMode",FreightPayMode);
            jObj_mainarr.put("ConsigneeName",ConsigneeName);        //Test Chetana
            jObj_mainarr.put("ShipToMasterId",ShipToMasterId);      //9DF61775-5EC1-499E-BBC0-85B16067DB99
            jObj_mainarr.put("BillToId",BillToId);               //9DF61775-5EC1-499E-BBC0-85B16067DB99
            jObj_mainarr.put("Dele_Transporter",Dele_Transporter);
            jObj_mainarr.put("dispatchMode",dispatchMode);
            jObj_mainarr.put("DiscPC",DiscPC);          //5
            jObj_mainarr.put("FLineTaxes",FLineTaxes);      //90.48
            jObj_mainarr.put("BasicAMNT",BasicAMNT);
            jObj_mainarr.put("TaxAMT",TaxAMT);
            jObj_mainarr.put("GrossAMT",GrossAMT);      //90
            jObj_mainarr.put("DiscAMT",DiscAMT);        //5
            jObj_mainarr.put("ChargeAmnt",ChargeAmnt);
            jObj_mainarr.put("DiscountAMNTFC",DiscountAMNTFC);      //90.48
            jObj_mainarr.put("ChargeTaxAmount",ChargeTaxAmount);    //90.48
            jObj_mainarr.put("LineCount",LineCount);        //1
            jObj_mainarr.put("NetWt",NetWt);
            jObj_mainarr.put("GrossWt",GrossWt);
            jObj_mainarr.put("Shipper",Shipper);
            jObj_mainarr.put("ApproverId",ApproverId);
            jObj_mainarr.put("remark",remark);
            jObj_mainarr.put("SOHeaderid",SOHeaderid);
            jObj_mainarr.put("RoundOff",RoundOff);
            jObj_mainarr.put("RefDt",RefDt);        //2020-11-18 12:00:00 AM
            jObj_mainarr.put("Mobile",custMob);        //2020-11-18 12:00:00 AM
        }catch (Exception e){
            e.printStackTrace();
        }

        mainJson = jObj_mainarr;

        return mainJson;
    }

    public JSONObject getChalanArray(){
        JSONObject chalanJson = null;

        JSONObject jObj_chalan = new JSONObject();
        try{
            jObj_chalan.put("SoScheduleid",SoScheduleId);
            jObj_chalan.put("ChalanNo",ChalanNo);
            jObj_chalan.put("ItemPlantId",ItemPlantId);
            jObj_chalan.put("IssueQty",IssueQty);
            jObj_chalan.put("GrndtlId",GrndtlId);
            jObj_chalan.put("StockId",StockId);
        }catch (Exception e){
            e.printStackTrace();
        }

        chalanJson = jObj_chalan;

        return chalanJson;
    }

    public void getTaxDtls(String taxClsName){
        String q = "Select TaxClassMasterId,TaxClassCode from "+dbhandler.TABLE_TAXCLASS+" WHERE TaxClassDesc='"+taxClsName+"'";
        Cursor c = sql_db.rawQuery(q,null);
        if(c.getCount()>0){
            c.moveToFirst();
            TaxClassMasterId = c.getString(c.getColumnIndex("TaxClassMasterId"));
            TaxClassCode = c.getString(c.getColumnIndex("TaxClassCode"));
        }

    }

    public String getItemMasterid(String itemcode){
        String q = "Select ItemPlantId from "+dbhandler.TABLE_ADD_ITEMS_COUNTERBILL+" WHERE ItemCode='"+itemcode+"'";
        Cursor c = sql_db.rawQuery(q,null);
        if(c.getCount()>0){
            c.moveToFirst();
            ItemPlantId = c.getString(c.getColumnIndex("ItemPlantId"));
        }

        return ItemPlantId;

    }

    class PostCounterBill extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;
        String so_no = "", ItemCode = "", ItemDesc = "", AddedDt = "", scheduledate = "", ScheduleQty = "", ShipmentQty = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            mprogress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_postCounterBill;

                res = ut.OpenPostConnection(url,finalData,parent);


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
            mprogress.setVisibility(View.GONE);

            try{
                String invoiceNo = "", status = "",otp="";


                if(response.contains("InvoiceNo")){
                    try{
                        String res = response.replace("\"[","[");
                        res = res.replace("]\"","]");
                        JSONArray jArr = new JSONArray(res);

                        status = jArr.getJSONObject(0).getString("Status");
                        invoiceNo = jArr.getJSONObject(0).getString("InvoiceNo");
                        otp = jArr.getJSONObject(0).getString("OTP");

                        if (AppCode.equalsIgnoreCase("SM")){
                            getotp(invoiceNo,otp);
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(parent,"Bill saved successfully",Toast.LENGTH_SHORT).show();

                }else if(response.contains("Not able to process shipment due to lot") || response.contains("error")){

                    Toast.makeText(parent,"Sorry, Not able to process shipment due to lot.",Toast.LENGTH_SHORT).show();

                }else {

                    Toast.makeText(parent,"Sorry, server error! failed to save bill.",Toast.LENGTH_SHORT).show();

                }

                if(isTakePrint){
                    print_CGST_SGST(cbillList,invoiceNo);
                }else {
                    cbillList.clear();
                    tcf.clearTable(parent, dbhandler.TABLE_ADD_ITMDTLS_FORBILL);
                    AppCommon.getInstance(parent).setBillingObject("");
                   // finish();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void getotp(String invoice, final String otp) {

        Invoicenumber=invoice;

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemListCB.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.billing_otp_lay, null);
        dialogBuilder.setView(dialogView);

        // set the custom dialog components - text, image and button
        final EditText textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
        final EditText edt_amount = (EditText) dialogView.findViewById(R.id.edt_paid_amount);
        Button button = (Button) dialogView.findViewById(R.id.txt_submit);

        // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
        // if button is clicked, close the custom dialog
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String entrotp = textotp.getText().toString().trim();
                paid_amount=edt_amount.getText().toString();


                if (paid_amount.equalsIgnoreCase("")||paid_amount.equalsIgnoreCase("null")||paid_amount==null){
                    Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG).show();
                }else {
                    if (!(entrotp.equals(""))) {
                        if (entrotp.equalsIgnoreCase(otp)) {

                            if (isnet()) {

                                new StartSession(parent, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {

                                        new PostPaidAmount().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid OTP!!! try again", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });


                /*
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
*/


    }

    // verifying if storage permission is given or not
    public static void verifystoragepermissions(Activity activity) {

        int permissions = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If storage permission is not given then request for External Storage Permission
        if (permissions != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionstorage, REQUEST_EXTERNAL_STORAGe);
        }
    }

    protected File screenshot(View view, String filename) {
        Date date = new Date();

        // Here we are initialising the format of our image name
        CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", date);
        try {
            // Initialising the directory of storage

            /*String dirpath = Environment.getExternalStorageDirectory() + "";
            File file = new File(dirpath);
            if (!file.exists()) {
                boolean mkdir = file.mkdir();
            }
*/


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Ekatm/Generated_Doc");


            if (!file.exists())
                file.mkdirs();
            File file1 = new File(file, "Image-"+ new Random().nextInt() + ".jpg");
            if (file1.exists())
                file1.delete();

            FileOutputStream out = new FileOutputStream(file1);


            // File name
            //String path = path1 + "/" + filename + "-" + format + ".jpeg";

            Bitmap bitmap = null;

            /* to take whole listview screenshot - old screenshot only visible content*/
            try{
                view.setDrawingCacheEnabled(true);
                bitmap = Bitmap.createBitmap(view.getDrawingCache());
                view.setDrawingCacheEnabled(false);
            }catch (Exception e){
                e.printStackTrace();
              // Toast.makeText(ItemListCB.this, "Unable to capture screenshot.", Toast.LENGTH_SHORT).show();
            }

            out.flush();
            out.close();
            String path = file1.getAbsolutePath();

            /*as per screensize image created and bitmap loaded*/
            try{
                bitmap = loadBitmapFromView(llscroll, llscroll.getWidth(), llscroll.getHeight()); // yala error hota linearlayout not initialize
                Toast.makeText(ItemListCB.this, "You just Captured a Screenshot," +
                        " Open Gallery/ File Storage to view your captured Screenshot", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(ItemListCB.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            /**********************************************/

            File imageurl = new File(path);
            FileOutputStream outputStream = new FileOutputStream(imageurl);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageurl;

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            Toast.makeText(ItemListCB.this, e1.getMessage().toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(ItemListCB.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        /*bitmap created on canvas*/
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    public void setListViewHeightBasedOnChildren(ListView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x+5);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    /*private String createPdf(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
        }
        PdfDocument.PageInfo pageInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        }
        PdfDocument.Page page = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            page = document.startPage(pageInfo);
        }

        Canvas canvas = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            canvas = page.getCanvas();
        }

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setTextSize(15);
        paint.setTextScaleX(50);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.finishPage(page);
        }

       *//* Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_YYYY");
        String addedDt = sdf.format(c.getTime());

        txtnw.setText(Network +" - "+  StationName + " - " + Supporter + " - " + addedDt);*//*

        // write the document content
        final String fileName = "counterbill.pdf";
        String targetPdf = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Meeshna_"+fileName;
        String _fileName = fileName;
        File filePath;
        filePath = new File(targetPdf);
        try {

            if (!filePath.exists()) {
                filePath.getParentFile().mkdirs();
                filePath.createNewFile();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //FileOutputStream fileOutputStream = new FileOutputStream(filePath,true);
                document.writeTo(new FileOutputStream(filePath));
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            document.close();
        }
        Toast.makeText(this, "PDF of Scroll is created!!!", Toast.LENGTH_SHORT).show();

        return _fileName;

        // openGeneratedPDF();
    }*/

    private boolean hasManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                return true;
            } else {
                if (Environment.isExternalStorageLegacy()) {
                    return true;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:com.vritti.ekatm"));
                    startActivityForResult(intent, REQUEST_EXTERNAL_STORAGe); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (Environment.isExternalStorageLegacy()) {
                return true;
            } else {
                try {
                    Intent intent = new Intent();
                    intent.setAction(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:com.vritti.ekatm"));
                    startActivityForResult(intent, REQUEST_EXTERNAL_STORAGe); //result code is just an int
                    return false;
                } catch (Exception e) {
                    return true; //if anything needs adjusting it would be this
                }
            }
        }
        return true; // assumed storage permissions granted
    }

    class PostPaidAmount extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_SavePaidAmount + "?InvoiceNo="+Invoicenumber+"&RecdAmt="+paid_amount;

            try {
                res = ut.OpenConnection(url, getApplicationContext());
              //  res = res.replaceAll("\\\\", "");
                //res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }


        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res.contains("false")||res.contains("False")) {
                b.dismiss();
              Toast.makeText(ItemListCB.this,"Bill submitted successfully",Toast.LENGTH_LONG).show();
            }else {
                b.dismiss();
                Toast.makeText(ItemListCB.this,"Bill not submitted successfully",Toast.LENGTH_LONG).show();

            }
        }
    }
}
