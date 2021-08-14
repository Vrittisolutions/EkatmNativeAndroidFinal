package com.vritti.sales.OrderBookingNew;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CategoryAdapter;
import com.vritti.sales.OrderBookingNew.Adapter.OrdSummaryAdapter;
import com.vritti.sales.activity.GetAddressActivity;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.PlaceOrderBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.vritti.sales.beans.Tbuds_commonFunctions.updateTime;

public class DeliveryDetail_Multimerchant extends AppCompatActivity {
    private Context parent;

    java.util.ArrayList<GetAddressActivity.Address> addressArrayList;
    GetAddressActivity.Address addressbean;
    String homeaddress = "No address found", officeaddress = "No address found", currentaddress = "No address found",
            otheraddress = "No address found";
    private String currentTime;
    DatePickerDialog datePickerDialog;

    EditText btndate, btntime, btntime_to;
    RadioGroup radgrpdelmode;
    RadioButton raddoorstep,radtakeaway;
    TextView textview_total_amount_pay,txtdelchrg,txtsubtot,txtoffer,txttot_prod,editTextAddress;
    Button btn_place_order;
    TextView txtdelcharges;

    SharedPreferences sharedpreferences;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    private ProgressDialog progressDialog;
    public String restoredMobile, restoredusername, cvid, usertype;
    private java.util.ArrayList<MyCartBean> ArrayList;
    private java.util.ArrayList<MyCartBean> merchList;

    static int year, month, day;
    public static String date = null;
    public static String time = null;
    String expectedDateTime;
    int cart_count = 0;

    Toolbar toolbar;
    String image_URL;
    String res = "";
    String prfDel_timefrm = "", prfDel_timeto = "", delAddress_latitude = "", delAddress_longitude = "";
    String deliverymodeStatus = "",User_Address, User_Vehicle;
    public static String today, todaysDate;
    String payableAmount, amtTosend = "",callFrom="";
    private String SubcatID, PurDigit;

    JSONObject JsonMain;
    PlaceOrderBean placeOrderBean;
    List<PlaceOrderBean> placeOrderBeanList  = new ArrayList<PlaceOrderBean>();
    String dabasename;

    public static String xml1, xml2;
    JSONObject jsonMain;
    String DateToStr;
    Dialog dialog;

    public static int Address = 10;
    public static int Vehicle_no = 11;

    java.util.ArrayList<MyCartBean> jsonList;
    java.util.ArrayList<MyCartBean> payment_jsonList;
    public int paymnt_callIndex = 0, paymnt_callendIndex = 0;
    public int callIndex = 0, callendIndex = 0;
    ListView listmerchants;
    //GridView listmerchants;
    OrdSummaryAdapter ordAdapter;
    String exp_delMinutes="";
    Button btnchangeaddr;

    String paySTATUS="", sohdrIdToPay="",merchIdTopay="",PaymentMode="";
    JSONObject jsonPay;
    private String FinalJson;
    private String FinalJsonPayment;
    AlertDialog.Builder dialogBuilder;

    java.util.ArrayList<OrderHistoryBean> paymentDoneList;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    String cat_name, cat_id,CatImgPath = "";
    int SubCatCount = 0;
    String selectedCatid="",selectedCatName="";
    CategoryAdapter cadt;
    LinearLayout showdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail__multimerchant);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        Bundle b = getIntent().getExtras();
        try{
            payableAmount = b.getString("PayableAmount");
            amtTosend = b.getString("PayableAmount");
            callFrom = b.getString("callFrom");
        }catch (Exception e){
            e.printStackTrace();
        }

        if(callFrom.equalsIgnoreCase("CheckoutScreen")){
            payableAmount = sharedpreferences.getString("payableAmount","0");
            amtTosend = sharedpreferences.getString("payableAmount","0");
        }

        ArrayList = CheckoutActivity_Multimerchant.ArrayList;
        jsonList = new ArrayList<MyCartBean>();
        payment_jsonList = new ArrayList<MyCartBean>();

        getOrdsumlistData();

        setPayableAmount(payableAmount);

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy"); //Feb 23 2016 12:16PM
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd yyyy");
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

      //  getDataFromDatabase();
        editTextAddress.setText(AnyMartData.ADDRESS);

        setListeners();
    }

    public void init(){
        parent = DeliveryDetail_Multimerchant.this;

        final ActionBar ab = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.deldtls)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        showdialog =  findViewById(R.id.showdialog);
        textview_total_amount_pay = (TextView) findViewById(R.id.textview_total_amount_pay);
        btntime =  findViewById(R.id.btntime);
        btntime_to = findViewById(R.id.btntime_to);
        btndate = findViewById(R.id.btndate);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        editTextAddress = findViewById(R.id.editTextAddress);
        radgrpdelmode =  findViewById(R.id.radgrpdelmode);
        raddoorstep =  findViewById(R.id.raddoorstep);
        radtakeaway =  findViewById(R.id.radtakeaway);
        txttot_prod =  findViewById(R.id.txttot_prod);
        txtoffer =  findViewById(R.id.txtoffer);
        txtsubtot =  findViewById(R.id.txtsubtot);
        txtdelchrg =  findViewById(R.id.txtdelchrg);
        listmerchants =  findViewById(R.id.listmerchants);
        txtdelcharges =  findViewById(R.id.txtdelcharges);
        btnchangeaddr =  findViewById(R.id.btnchangeaddr);

        radtakeaway.setChecked(true);
        deliverymodeStatus = "Take Away";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(DeliveryDetail_Multimerchant.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId",null);
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");

        addressArrayList = new ArrayList<GetAddressActivity.Address>();
        progressDialog = new ProgressDialog(parent);
        ArrayList = new ArrayList<MyCartBean>();
        merchList = new ArrayList<MyCartBean>();
        paymentDoneList = new ArrayList<OrderHistoryBean>();

        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat datef = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timef = new SimpleDateFormat("HH:mm aa");

        Date cal = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(cal);
        c.add(Calendar.DATE,7);
        cal = c.getTime();

        System.out.println(cal);
        System.out.println("caldt: " +sdf.format(cal));
        System.out.println("cdate: " +datef.format(cal));
        System.out.println("ctime: " +timef.format(cal));

        date = datef.format(cal);

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        time = updateTime(hour, minute);
        System.out.println("time: " + time);

        btndate.setText(date);
        btntime.setText("9:00 AM");
        btntime_to.setText("6:00 PM");

        prfDel_timefrm = btntime.getText().toString();
        prfDel_timeto = btntime_to.getText().toString();

        String edtaddress = editTextAddress.getText().toString();
        User_Address = editTextAddress.getText().toString();

        if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
            if (AnyMartData.URL.contains("petro")) {
                editTextAddress.setHint("Delivery Vehicle ");
            }else if (AnyMartData.URL.contains("bakery")) {
                editTextAddress.setHint("Delivery Address ");
            }
        }

        AnyMartData.ADDRESS = editTextAddress.getText().toString();

        restoredMobile = sharedpreferences.getString("Mobileno", "");
        cvid = sharedpreferences.getString("userid", "");
        cvid = sharedpreferences.getString("CustVendorMasterId", "");
        usertype = sharedpreferences.getString("usertype", "");
        restoredusername = sharedpreferences.getString("username", "");
        image_URL = sharedpreferences.getString("logopath",null);
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");

        editTextAddress.setText(AnyMartData.ADDRESS);
       // editTextAddress.setSelection(editTextAddress.getText().toString().length());
        homeaddress = AnyMartData.ADDRESS;

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setListeners(){

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(DeliveryDetail_Multimerchant.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;

                                if (compare_date(date) == true) {
                                    btndate.setText(date);
                                    calculateExprDelTime();
                                } else {
                                    btndate.setText(date);
                                    Toast.makeText(DeliveryDetail_Multimerchant.this,
                                            ""+getResources().getString(R.string.you_can_not_sel_day_early),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle(""+getResources().getString(R.string.selectdate));
                datePickerDialog.show();
            }
        });

        btntime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                //  btntime.setText(hour+":"+ minute + " ");

                mTimePicker = new TimePickerDialog(DeliveryDetail_Multimerchant.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = tcf.updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(DeliveryDetail_Multimerchant.this,
                                            ""+getResources().getString(R.string.plz_sel_datefirst), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();

                                        calculateExprDelTime();
                                    } else {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();
                                        Toast.makeText(DeliveryDetail_Multimerchant.this,
                                                ""+getResources().getString(R.string.you_cant_sel_time_early), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, hour, minute, true);// Yes 24 hour time
                mTimePicker.setTitle(""+getResources().getString(R.string.selecttime));
                mTimePicker.show();
            }
        });

        btntime_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                btntime_to.setText(hour+":"+ minute + " ");

                mTimePicker = new TimePickerDialog(DeliveryDetail_Multimerchant.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = tcf.updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(DeliveryDetail_Multimerchant.this,
                                            ""+getResources().getString(R.string.plz_sel_datefirst), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;

                                        calculateExprDelTime();

                                    } else {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;
                                        Toast.makeText(DeliveryDetail_Multimerchant.this,
                                                ""+getResources().getString(R.string.you_cant_sel_time_early), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, hour, minute, true);// Yes 24 hour time
                mTimePicker.setTitle(""+getResources().getString(R.string.selecttime));
                mTimePicker.show();
            }
        });

        btnchangeaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliveryDetail_Multimerchant.this, GetAddressActivity.class);
                startActivityForResult(intent, Address);

            }
        });

        raddoorstep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    raddoorstep.setChecked(true);
                    radtakeaway.setChecked(false);
                    deliverymodeStatus="Door Step";

                    txtdelcharges.setVisibility(View.VISIBLE);

                    calculateExprDelTime();

                    //getOrdsumlistData();

                }else {
                }
            }
        });

        radtakeaway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radtakeaway.setChecked(true);
                    raddoorstep.setChecked(false);
                    deliverymodeStatus="Take Away";

                    txtdelcharges.setVisibility(View.GONE);

                    getOrdsumlistData();

                }else {
                }
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(parent);
                dialog.setContentView(R.layout.dialog_message);
                TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
                Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
                Button btnno = (Button) dialog.findViewById(R.id.btn_no);
                EditText edtreason =  dialog.findViewById(R.id.edtreason);
                edtreason.setVisibility(View.GONE);
                txtMsg.setText(getResources().getString(R.string.dialogcnfrmord));
                dialog.show();

                btnyes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        showdialog.setVisibility(View.VISIBLE);
                        // TODO Auto-generated method stub
                        if (!(editTextAddress.getText().toString().trim().equalsIgnoreCase("")
                                || editTextAddress.getText().toString().trim() == null || date == null || time == null)) {
                            // Utilities.clearTable(parent, DatabaseHandlers.TABLE_CART_ITEM_new);
                            tcf.clearTable(parent, DatabaseHandlers.TABLE_PLACE_ORDER);

                            expectedDateTime = tcf.convertDateDDMMYYYYToYYYYMMDD(date)
                                    + " " + tcf.convertTime12HrsTo24Hrs(time) + ":00.000";

                            getMerchData();

                        } else {
                            if (editTextAddress.getText().toString().trim().equalsIgnoreCase("")
                                    || editTextAddress.getText().toString().trim() == null) {
                                Toast.makeText(parent, ""+getResources().getString(R.string.plz_fill_address), Toast.LENGTH_LONG).show();
                            }
                            if (date == null) {
                                Toast.makeText(parent, ""+getResources().getString(R.string.plz_fill_deldate), Toast.LENGTH_LONG).show();
                            }
                            if (time == null) {
                                Toast.makeText(parent, ""+getResources().getString(R.string.plz_fill_deltime), Toast.LENGTH_LONG).show();
                            }
                        }

                        dialog.dismiss();
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
    }

    private void getDataFromDatabase() {
        addressArrayList.clear();

        Cursor cursor = sql_db.rawQuery("Select distinct PermanentAddress,OfficeAddress" +
                        ",GpsLocationAddress,CurrentAddress from "
                        + DatabaseHandlers.TABLE_MY_ADDRESS +
                        " where Mobile =" + AnyMartData.MOBILE
                , null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                addressbean = new GetAddressActivity.Address();
                String permanentAddress = cursor.getString(cursor
                        .getColumnIndex("PermanentAddress"));
                String officeaddress1 = cursor.getString(cursor
                        .getColumnIndex("OfficeAddress"));
                String gpslocationaddress = cursor.getString(cursor
                        .getColumnIndex("GpsLocationAddress"));
                String currentaddress1 = cursor.getString(cursor
                        .getColumnIndex("CurrentAddress"));

                if (!permanentAddress.equals("N")) {
                    homeaddress = permanentAddress;
                    addressbean.setP_address(homeaddress);
                    addressbean.setType("Home");
                } else if (!officeaddress1.equals("N")) {
                    officeaddress = officeaddress1;
                    addressbean.setType("Office");
                    addressbean.setP_address(officeaddress);
                } else if (!gpslocationaddress.equals("N")) {
                    otheraddress = gpslocationaddress;
                    addressbean.setType("Other");
                    addressbean.setP_address(otheraddress);
                } else if (!currentaddress1.equals("N")) {
                    currentaddress = currentaddress1;
                    addressbean.setType("Current");
                    addressbean.setP_address(currentaddress);
                }
                addressArrayList.add(addressbean);

            } while (cursor.moveToNext());
        }
    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).before(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public static boolean compare_datetime(String fromdate, int selectedHour, int selectedMinute) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

        todaysDate = dfDate.format(new Date());
        try {
            if ((dfDate.parse(todaysDate).before(dfDate.parse(fromdate)))) {
                b = true;
            } else if ((dfDate.parse(todaysDate).equals(dfDate.parse(fromdate)))) {
                time = updateTime(selectedHour, selectedMinute);
                b = true;
            } else {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                time = updateTime(hour+2, minute);
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public void getMerchData(){
        if(merchList.size() > 0){
            merchList.clear();
        }

        String qry = "Select distinct MerchantId,MerchantName from "+DatabaseHandlers.TABLE_CART_ITEM_new;
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                MyCartBean bean = new MyCartBean();
                bean.setMerchantId(c.getString(c.getColumnIndex("MerchantId")));
                bean.setMerchantName(c.getString(c.getColumnIndex("MerchantName")));
                merchList.add(bean);

                try{
                    //add to mymerchant
                    int ordCnt = getMerchCount(c.getString(c.getColumnIndex("MerchantId")));

                    /*if(ordCnt == 0){
                        ordCnt = ordCnt + 1;
                        tcf.insertMerchData(c.getString(c.getColumnIndex("MerchantId")),
                                c.getString(c.getColumnIndex("MerchantName")), String.valueOf(ordCnt),"","");
                    }else {
                        ordCnt = ordCnt + 1;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("OrdCounts",ordCnt);
                        long q = sql_db.update(DatabaseHandlers.TABLE_MY_MERCHANTS, contentValues,
                                "MerchantId=?",
                                new String[]{c.getString(c.getColumnIndex("MerchantId"))});
                    }*/

                    Log.e("OrdCnt : ", String.valueOf(ordCnt));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }while (c.moveToNext());
        }

        //generate object according to merchnat count

        generateObject(merchList.size());
    }

    public float getTotKg(String merchId){
        String totContent = "";
        float totContPerMerch = (float) 0.0;
        float content = (float) 0.0;
        float qty =0;
        String qry = "Select Content, UOMCode,qnty from "+DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE MerchantId='"+merchId+"'";
        Cursor cqry = sql_db.rawQuery(qry,null);
        if(cqry.getCount() > 0){
            cqry.moveToFirst();
            do{
                String uom = cqry.getString(cqry.getColumnIndex("UOMCode"));
                content = Float.parseFloat(cqry.getString(cqry.getColumnIndex("Content")));
                qty = Float.parseFloat(cqry.getString(cqry.getColumnIndex("qnty")));

                if(uom.contains("Gm") || uom.contains("gm") || uom.contains("Ml") || uom.contains("ml")){
                    content = (content*qty)/1000;
                }else {
                    content=(content*qty);
                }

                totContPerMerch = totContPerMerch + content;

            }while (cqry.moveToNext());
        }

        return totContPerMerch;
    }

    public void generateObject(int merchCount){
        merchCount=1;

        for(int p = 0; p < merchCount; p++){
            //generate obj
            String merchantId = merchList.get(p).getMerchantId();

            JSONArray jsonArray = new JSONArray();

            float payamt = 0.0f;
            float delchrg = 0.0f, amt = 0.0f;

            String q1 = "Select * from "+DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE MerchantId='"+merchantId+"'";
            /*SUM(Amount) as NetAMT,*/
            Cursor cursor = sql_db.rawQuery(q1,null);
            if(cursor.getCount()>0){
                cursor.moveToFirst();
                do{
                    if(deliverymodeStatus.equalsIgnoreCase("Door Step")){
                        delchrg = Float.parseFloat(cursor.getString(cursor.getColumnIndex("AppliedDelCharges")));
                        amt = Float.parseFloat(cursor.getString(cursor.getColumnIndex("Amount")));
                        payamt = payamt + amt;
                    }else {
                        payamt = payamt + Float.parseFloat(cursor.getString(cursor.getColumnIndex("Amount")));
                    }

                    JSONObject jsonObject1 = new JSONObject();
                    try {
                        jsonObject1.put("ItemMasterId", cursor.getString(cursor.getColumnIndex("Product_id")));
                        jsonObject1.put("ItemName", cursor.getString(cursor.getColumnIndex("Product_name")));
                        jsonObject1.put("Rate", cursor.getString(cursor.getColumnIndex("price")));
                        jsonObject1.put("Qty", cursor.getString(cursor.getColumnIndex("qnty")));
                        jsonObject1.put("Amount", cursor.getString(cursor.getColumnIndex("Amount")));
                        jsonObject1.put("MerchantId", AnyMartData.MerchantID);
                        jsonObject1.put("MerchantName", AnyMartData.MerchantName);
                        jsonObject1.put("SODetailId", "");
                        jsonObject1.put("SOScheduleId", "");

                        jsonArray.put(jsonObject1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }while (cursor.moveToNext());

                payamt = payamt + delchrg;

                payableAmount = String.format("%.2f",payamt);

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("CustomerMasterId", cvid);
                    jsonObject.put("Mobile", restoredMobile);
                    jsonObject.put("NetAmt", payableAmount);        //net amt merchwise
                    jsonObject.put("SOHeaderId", "");
                    jsonObject.put("sono", "");
                    jsonObject.put("prefDeliveryFromTime",prfDel_timefrm);
                    jsonObject.put("prefDeliveryToTime",prfDel_timeto);
                    jsonObject.put("delvAddressLatitude",AnyMartData.LATITUDE);
                    jsonObject.put("delvAddressLongitude",AnyMartData.LONGITUDE);
                    jsonObject.put("WarehouseMasterId","");
                    jsonObject.put("OrderTypeMasterId","DefData1");
                    jsonObject.put("Scheduledate",expectedDateTime);
                    jsonObject.put("AppEnvMasterId",EnvMasterId);
                    jsonObject.put("UserLoginId",LoginId);
                    jsonObject.put("UserPwd",Password);
                    jsonObject.put("PlantId",PlantMasterId);
                    jsonObject.put("DeliveryMode",deliverymodeStatus);
                    jsonObject.put("MerchantId", merchantId);
                    jsonObject.put("ShiptoId", AnyMartData.SHIPTOMASTERID);
                    jsonObject.put("AddedBy", UserName);
                    //add SEId field

                    if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
                        jsonObject.put("Delivery_Address", User_Address);
                        jsonObject.put("Delivery_Address", editTextAddress.getText().toString());
                    } else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
                        jsonObject.put("Delivery_Address", User_Vehicle);
                    }

                    xml1 = jsonObject.toString();
                    xml2 = jsonArray.toString();

                    jsonMain = new JSONObject();
                    jsonMain.put("HeaderData",jsonObject);
                    jsonMain.put("ItemData",jsonArray);
                    AnyMartData.JMain = jsonMain;

                    MyCartBean jBean = new MyCartBean();
                    jBean.setJsonData(jsonMain.toString());
                    jBean.setMerchantId(merchantId);
                    jsonList.add(jBean);

                    tcf.addPlaceOrder(usertype, expectedDateTime, xml1, xml2, DateToStr,"No");

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
            //place order by deleting pid
        }

        callendIndex = jsonList.size();
        final String jsonData = jsonList.get(callIndex).getJsonData();
        final String merId = jsonList.get(callIndex).getMerchantId();

        if (NetworkUtils.isNetworkAvailable(parent)) {
            /*new StartSession_tbuds(DeliveryDetail_Multimerchant.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PlaceOrder().execute(expectedDateTime,jsonData, merId);
                }

                @Override
                public void callfailMethod(String msg) {

                }

            });*/
            new PlaceOrder().execute(expectedDateTime,jsonData, merId);
        } else {
          //  Toast.makeText(parent, ""+parent.getResources().getString(R.string.poor_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    public void calculateExprDelTime(){
        String frmtime = btndate.getText().toString().trim()+" "+btntime.getText().toString();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date date = null;
        try {
            date = (Date)formatter.parse(frmtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " +date.getTime());

        String totime = btndate.getText().toString().trim()+" "+btntime_to.getText().toString();
        DateFormat formatterto = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date dateto = null;

        try {
            dateto = (Date)formatterto.parse(totime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " +dateto.getTime());

        double timfrm = date.getTime();
        double timto = dateto.getTime();
        double diff = timto - timfrm;
        double minutes = Math.floor(diff / 60) % 60;    //minutes & 1 minute = 60,000 milliseconds
        minutes = (diff/60000);
        double hours = Math.floor(diff / 3600) % 24;    //hours & 1hr = 36,00,000 milliseconds
        hours = (diff/3600000);
        exp_delMinutes = String.valueOf(minutes);

        //if doorstep then call ordsumand apply expr del charges
        if(deliverymodeStatus.equalsIgnoreCase("Door Step")){
            getOrdsumlistData();
        }
    }

    public void openDelDtlsDialogBox(float subtot,float custdist,float custtotkg,float freedelamt,float exprdelchrge, float freetotdist,
                                     float mindelkg_,float mindelkm_){
        dialog = new Dialog(parent);
        dialog.setContentView(R.layout.deliverychargedetails);
        TextView freedelaboveamt = findViewById(R.id.freedelaboveamt);
        TextView freedelmaxdist = findViewById(R.id.freedelmaxdist);
        TextView mindelkg = findViewById(R.id.mindelkg);
        TextView mindelkm = findViewById(R.id.mindelkm);
        TextView exprminutes = findViewById(R.id.exprminutes);
        TextView exprschrge = findViewById(R.id.exprschrge);
        TextView appl_del_charge = findViewById(R.id.appl_del_charge);
        Button btnok = findViewById(R.id.btnok);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        if(custtotkg >= mindelkg_ && custdist >= mindelkm_){
            //delivery charges will get applied
            if(subtot >= freedelamt && custdist <= freetotdist){
                //free del charges applied
                Toast.makeText(parent,"Free delivery service is applicable", Toast.LENGTH_SHORT).show();
            }else {
                //express del charges
                //txtdelchrge.setText(formatter.format(exprschrge));
                Toast.makeText(parent,"Express delivery service is applicable", Toast.LENGTH_SHORT).show();
            }
        }else {
            // no delivery will be provided
            Toast.makeText(parent,"Sorry! We are unable to provide delivery service in your area", Toast.LENGTH_SHORT).show();
        }

        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialog.dismiss();
            }
        });
    }

    public int getMerchCount(String merchId){
        int ocnt = 1;
        /*String qry = "Select OrdCounts from "+DatabaseHandlers.TABLE_MY_MERCHANTS+ " WHERE MerchantId='"+merchId+"'";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            ocnt = c.getCount();
        }else {
            c.getCount();
        }*/

        return ocnt;
    }

    class PlaceOrder extends AsyncTask<String, Void, String> {
        String responseString = "";
        String response_generateOrder = "";
        String res ="";
        String ID = "";
        String merId = "";
        String jsonData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* try{
                progress = ProgressHUD.show(parent,""+parent.getResources().getString(R.string.order_getting_placed),
                        false,true, null);
                progress.setCanceledOnTouchOutside(true);
            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        @Override
        protected String doInBackground(String... params) {

            String dt=params[0];
            String body=params[1];
            merId = params[2];
            jsonData = params[1];

            // ID = params[2];
            String url_generateOrder = "";

            if(jsonData.toString() != null){
                /*new url POST type*/
                url_generateOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_GENERATE_ORDER;
                //"&Arr="+  URLEncoder.encode(JsonMain.toString(),"UTF-8");

            }else {
                //No data found
            }

            try {

                res = String.valueOf(Utility.OpenPostConnection(url_generateOrder,
                        jsonData.toString().replaceAll("^\"|\"$",""),DeliveryDetail_Multimerchant.this));
                res = res.replaceAll("\\\\", "");

                sohdrIdToPay = res.toString().replaceAll("^\"|\"$", "");

                responseString = res.toString().replaceAll("^\"|\"$", "")+ "," + params[2];
                response_generateOrder = responseString.replaceAll("\\\\","");
                System.out.println("resp ="+response_generateOrder);
                Log.e("Response", response_generateOrder);

            } catch (Exception e) {
                response_generateOrder = "error";
                e.printStackTrace();
            }
            return response_generateOrder;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           if (response_generateOrder.equalsIgnoreCase("error") ||
                    response_generateOrder.equalsIgnoreCase("null") ||
                   response_generateOrder.equalsIgnoreCase(null) ||
                   response_generateOrder.contains("null")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DeliveryDetail_Multimerchant.this,
                                ""+getResources().getString(R.string.sorry_ord_not_place), Toast.LENGTH_LONG).show();
                    }
                });
                placeOrderBeanList.clear();
            }else if(response_generateOrder.contains("ExceptionMessage")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DeliveryDetail_Multimerchant.this,
                                ""+getResources().getString(R.string.sorry_ord_not_place), Toast.LENGTH_LONG).show();
                    }
                });

                placeOrderBeanList.clear();

                String id[] = result.split(",");

                try{
                   // sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+ID+"'", null);
                    sql_db.delete(DatabaseHandlers.TABLE_CART_ITEM_new, "MerchantId='"+merId+"'", null);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {

               showdialog.setVisibility(View.GONE);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DeliveryDetail_Multimerchant.this, ""+getResources().getString(R.string.ord_place_success), Toast.LENGTH_LONG).show();
                    }
                });
                placeOrderBeanList.clear();

                String id[] = result.split(",");

                /*try{
                    //sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+id[1]+"'", null);
                    sql_db.delete(DatabaseHandlers.TABLE_CART_ITEM_new, "MerchantId='"+merId+"'", null);
                    //new HistoryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch (Exception e){
                    e.printStackTrace();
                }*/

               tcf.clearTable(parent, DatabaseHandlers.TABLE_CART_ITEM_new);
               tcf.clearTable(parent, DatabaseHandlers.TABLE_PLACE_ORDER);


               Bundle bplaceorder = new Bundle();
               Intent intent = new Intent(DeliveryDetail_Multimerchant.this, ThankyouActivity_Multimerchant.class);

               if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")){
                   intent.putExtra("User_Address", User_Address);
                   intent.putExtra("User_Address", editTextAddress.getText().toString());
               } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
                   intent.putExtra("User_Vehicle", User_Vehicle);
               }

               bplaceorder.putString("PayableAmount", String.valueOf(amtTosend));
               bplaceorder.putString("date", String.valueOf(date));
               bplaceorder.putString("time", String.valueOf(prfDel_timefrm));
               bplaceorder.putString("time_to", String.valueOf(prfDel_timeto));
               bplaceorder.putString("delmode",deliverymodeStatus);

               intent.putExtras(bplaceorder);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               finish();

            }
        }
    }

    public void getOrdsumlistData(){
        merchList.clear();

        String qry = "Select distinct MerchantId,MerchantName from "+DatabaseHandlers.TABLE_CART_ITEM_new;
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                MyCartBean bean = new MyCartBean();
                bean.setMerchantId(c.getString(c.getColumnIndex("MerchantId")));
                bean.setMerchantName(c.getString(c.getColumnIndex("MerchantName")));

                String mId = c.getString(c.getColumnIndex("MerchantId"));
                String merchnameForDelivery = c.getString(c.getColumnIndex("MerchantName"));

                //gettotKg
                float toContent = 0, subtotamt = 0, appldelchrge = 0,total = 0;
                toContent=getTotKg(mId);
                bean.setTotContentPerMerch(String.valueOf(toContent));

                String q2 = "Select SUM(Amount) as NetAMT, AppliedDelCharges from "+DatabaseHandlers.TABLE_CART_ITEM_new
                        +" WHERE MerchantId='"+mId+"'";
                Cursor cq2 = sql_db.rawQuery(q2,null);
                if(cq2.getCount()>0){
                    cq2.moveToFirst();

                    subtotamt = Float.valueOf(cq2.getString(cq2.getColumnIndex("NetAMT")));
                    appldelchrge = Float.valueOf(cq2.getString(cq2.getColumnIndex("AppliedDelCharges")));
                    bean.setAmount(Float.valueOf(cq2.getString(cq2.getColumnIndex("NetAMT"))));
                    bean.setAppliedDelCharges(cq2.getString(cq2.getColumnIndex("AppliedDelCharges")));
                }

                try{

                    String del = "Select FreeAboveAmt,FreeDelyMaxDist,MinDelyKg,MinDelyKm,ExprDelyWithinMin,ExpressDelyChg,Distance,IsDelivery,UPI from "+
                            DatabaseHandlers.TABLE_CART_ITEM_new+" WHERE MerchantId='"+mId+"'";
                    Cursor cdel = sql_db.rawQuery(del,null);
                    if(cdel.getCount()>0){
                        cdel.moveToFirst();
                    //    do {
                        bean.setIsMerchDelivery(cdel.getString(cdel.getColumnIndex("IsDelivery")));
                        bean.setUPIMerch(cdel.getString(cdel.getColumnIndex("UPI")));
                            if(cdel.getString(cdel.getColumnIndex("FreeAboveAmt")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("FreeAboveAmt")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("FreeAboveAmt")).equalsIgnoreCase("null")){
                                bean.setFreeAboveAmt("0");
                            }else {
                                bean.setFreeAboveAmt(cdel.getString(cdel.getColumnIndex("FreeAboveAmt")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")).equalsIgnoreCase("null")){
                                bean.setFreeDelyMaxDist("0");
                            }else {
                                bean.setFreeDelyMaxDist(cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("MinDelyKg")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("MinDelyKg")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("MinDelyKg")).equalsIgnoreCase("null")){
                                bean.setMinDelyKg("0");
                            }else {
                                bean.setMinDelyKg(cdel.getString(cdel.getColumnIndex("MinDelyKg")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("MinDelyKm")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("MinDelyKm")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("MinDelyKm")).equalsIgnoreCase("null")){
                                bean.setMinDelyKm("0");
                            }else {
                                bean.setMinDelyKm(cdel.getString(cdel.getColumnIndex("MinDelyKm")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")).equalsIgnoreCase("null")){
                                bean.setExprDelyWithinMin("0");
                            }else {
                                bean.setExprDelyWithinMin(cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("ExpressDelyChg")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("ExpressDelyChg")).equalsIgnoreCase(null) ||
                                    cdel.getString(cdel.getColumnIndex("ExpressDelyChg")).equalsIgnoreCase("null")){
                                bean.setExpressDelyChg("0");
                            }else {
                                bean.setExpressDelyChg(cdel.getString(cdel.getColumnIndex("ExpressDelyChg")));
                            }

                            if(cdel.getString(cdel.getColumnIndex("Distance")).equalsIgnoreCase("") ||
                                    cdel.getString(cdel.getColumnIndex("Distance")).equalsIgnoreCase(null)||
                                    cdel.getString(cdel.getColumnIndex("Distance")).equalsIgnoreCase("null")){
                                bean.setDistance("0");
                            }else {
                                bean.setDistance(cdel.getString(cdel.getColumnIndex("Distance")));
                            }

                        if(cdel.getString(cdel.getColumnIndex("IsDelivery")).equalsIgnoreCase("Y")){
                            if(deliverymodeStatus.equalsIgnoreCase("Door Step")){

                                if(merchnameForDelivery.equalsIgnoreCase("Madam Home")){
                                    //only for madam home

                                    //above 1000 free delivery and below 1000 fix 15 Rs.
                                    float appliedDelCharges = 0;
                                    float subtot = subtotamt;
                                    float custdist = Float.parseFloat(cdel.getString(cdel.getColumnIndex("Distance")));
                                    float custtotkg = toContent;
                                    float freedelamt = Float.parseFloat(cdel.getString(cdel.getColumnIndex("FreeAboveAmt")));
                                    float exprdelchrge = Float.parseFloat(cdel.getString(cdel.getColumnIndex("ExpressDelyChg")));
                                    float freetotdist =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")));
                                    float mindelkg =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("MinDelyKg")));
                                    float mindelkm =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("MinDelyKm")));
                                    float exprdelmin =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")));

                                    if(subtot >= freedelamt && custdist <= freetotdist){
                                        //free delivery
                                        bean.setAppliedDelCharges("0");
                                        appliedDelCharges = 0;

                                    }else if(subtot < freedelamt && custdist <= freetotdist){
                                        //delivery charge fix 15 rs.
                                      //  float diffkm = custdist - freetotdist;
                                        float delchrge = 15;
                                        delchrge = math(delchrge);

                                        bean.setAppliedDelCharges(String.format("%.2f",delchrge));
                                        appliedDelCharges = delchrge;

                                    }else if(custdist > freetotdist){
                                        //no delivery service providing
                                        bean.setAppliedDelCharges("0");
                                        appliedDelCharges = 0;
                                    }else {
                                        bean.setAppliedDelCharges("0");
                                        appliedDelCharges = 0;
                                    }

                                    //update cart column
                                    ContentValues values = new ContentValues();
                                    values.put("AppliedDelCharges",appliedDelCharges);

                                    long b = sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new, values, "MerchantId=?", new String[]{mId});

                                }else {
                                    //regular old logic
                                    try{
                                        float appliedDelCharges = 0;
                                        float subtot = subtotamt;
                                        float custdist = Float.parseFloat(cdel.getString(cdel.getColumnIndex("Distance")));
                                        float custtotkg = toContent;
                                        float freedelamt = Float.parseFloat(cdel.getString(cdel.getColumnIndex("FreeAboveAmt")));
                                        float exprdelchrge = Float.parseFloat(cdel.getString(cdel.getColumnIndex("ExpressDelyChg")));
                                        float freetotdist =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("FreeDelyMaxDist")));
                                        float mindelkg =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("MinDelyKg")));
                                        float mindelkm =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("MinDelyKm")));
                                        float exprdelmin =  Float.parseFloat(cdel.getString(cdel.getColumnIndex("ExprDelyWithinMin")));

                                        //delivery charges will get applied
                                        if(subtot >= freedelamt && custdist <= freetotdist){
                                            //free del charges applied
                                            //Toast.makeText(parent,"Free delivery service is applicable",Toast.LENGTH_SHORT).show();
                                            bean.setAppliedDelCharges("0");
                                            appliedDelCharges = 0;

                                        } else if(Float.parseFloat(exp_delMinutes) <= exprdelmin){
                                            //express del charges
                                       /* if(((Float.parseFloat(exp_delMinutes) == 0.0) ||
                                                (Float.parseFloat(exp_delMinutes) == 0))){

                                        }else {*/
                                            bean.setAppliedDelCharges(String.format("%.2f",exprdelchrge));
                                            appliedDelCharges = exprdelchrge;
                                            // }

                                        }else if((subtot > freedelamt && custdist > freetotdist)){
                                            //del charges applied as per km
                                            float diffkm = custdist - freetotdist;
                                            float delchrge = diffkm * mindelkm;
                                            delchrge = math(delchrge);

                                            bean.setAppliedDelCharges(String.format("%.2f",delchrge));
                                            appliedDelCharges = delchrge;
                                        }/*else if((subtot <= freedelamt && custdist >= freetotdist)){
                                        //del charges applied as per km
                                        float diffkm = custdist - freetotdist;
                                        float delchrge = diffkm * mindelkm;
                                        delchrge = math(delchrge);

                                        bean.setAppliedDelCharges(String.format("%.2f",delchrge));
                                        appliedDelCharges = delchrge;
                                    }*/else if((subtot < freedelamt /*&& custdist <= freetotdist)*/)){
                                            //del charges applied as per km
                                            float diffkm = custdist;
                                            float delchrge = diffkm * mindelkm;
                                            delchrge = math(delchrge);

                                            bean.setAppliedDelCharges(String.format("%.2f",delchrge));
                                            appliedDelCharges = delchrge;
                                        }else {
                                            //del charges applied as per km
                                            float diffkm = 0;
                                            if(custdist > freetotdist){
                                                diffkm = custdist - freetotdist;
                                            }else {
                                                diffkm = freetotdist - custdist;
                                            }

                                            float delchrge = diffkm * mindelkm;
                                            delchrge = math(delchrge);

                                            bean.setAppliedDelCharges(String.format("%.2f",delchrge));
                                            appliedDelCharges = delchrge;
                                        }

                                        //update cart column
                                        ContentValues values = new ContentValues();
                                        values.put("AppliedDelCharges",appliedDelCharges);

                                        long b = sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new, values, "MerchantId=?", new String[]{mId});

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }

                            }else {
                                bean.setAppliedDelCharges("0");
                                //update cart column
                                ContentValues values = new ContentValues();
                                values.put("AppliedDelCharges","0");

                                long b = sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new, values, "MerchantId=?", new String[]{mId});
                            }
                        }else {
                            bean.setAppliedDelCharges("0");
                            //update cart column
                            ContentValues values = new ContentValues();
                            values.put("AppliedDelCharges","0");

                            long b = sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new, values, "MerchantId=?", new String[]{mId});
                        }

                        bean.setExpDelMinByCust(exp_delMinutes);
                    //    }while (cdel.moveToNext());
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

                merchList.add(bean);

            }while (c.moveToNext());

        }

        ordAdapter = new OrdSummaryAdapter(this,merchList);
        listmerchants.setAdapter(ordAdapter);

        updatePayableAmount(merchList);

        setListViewHeightBasedOnChildren(listmerchants,1);
    }

    public void updateDelChargesToAmt(java.util.ArrayList<MyCartBean> list){

        for(int i=0; i< list.size();i++){
            ContentValues values = new ContentValues();
            values.put("AppliedDelCharges",list.get(i).getAppliedDelCharges());

            String merchid = list.get(i).getMerchantId();

           long b = sql_db.update(DatabaseHandlers.TABLE_CART_ITEM_new, values, "MerchantId=?", new String[]{merchid});
        }

    }

    public void setListViewHeightBasedOnChildren(ListView listView, int columns) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    public static int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    public void setPayableAmount(String payableAmount){
        amtTosend = payableAmount;
        double amount = Double.parseDouble(payableAmount);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount_pay.setText(formatted+" ₹");
        txttot_prod.setText(formatted+" ₹");
        txtsubtot.setText(formatted+" ₹");
    }

    public void updatePayableAmount(java.util.ArrayList<MyCartBean> list){
        float delcharge = 0, amount = 0, paybleamt = 0;
        for(int i=0; i<list.size(); i++){
            delcharge = Float.parseFloat(list.get(i).getAppliedDelCharges());
            amount = list.get(i).getAmount();

            if(deliverymodeStatus.equalsIgnoreCase("Door Step")){
                paybleamt = paybleamt + (amount + delcharge);
            }else {
                paybleamt = paybleamt + amount;
            }
        }

        payableAmount = String.valueOf(paybleamt);
        amtTosend = payableAmount;
        double amount2 = paybleamt;
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount2);
        textview_total_amount_pay.setText(formatted+" ₹");
        txttot_prod.setText(formatted+" ₹");
        txtsubtot.setText(formatted+" ₹");
    }

    /*public void payToMerchant(String amountToPay, String UPI, String MerchName, String merchid){

        merchIdTopay = merchid;

        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.payment_options);
        myDialog.setCancelable(true);

        final LinearLayout lay_cod = myDialog.findViewById(R.id.lay_cod);
        final LinearLayout txt_payment = myDialog.findViewById(R.id.txt_payment); //itemname

        lay_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paySTATUS="42";
                PaymentMode = "COD";

                OrderHistoryBean bean = new OrderHistoryBean();
                bean.setTransactionId("COD");
                paymentDoneList.add(bean);

                insertIntoPaymentTable(merchIdTopay,"COD");

                myDialog.dismiss();
            }
        });

        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentMode = "UPI";
                paySTATUS="42";

                String transactionNote="Product purchase";
                String currencyUnit="INR";

                int randomPIN = (int)(Math.random()*9000)+1000;
                String val = ""+randomPIN;

                Uri uri = null;
                try {
                    uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+amountToPay+"&cu="+currencyUnit+"&tr="+val);
                    *//*uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+"1"+"&cu="+currencyUnit+"&tr="+val);*//*
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.d("Order", "onClick: uri: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent,1);

                myDialog.dismiss();
            }
        });

        myDialog.show();

    }*/

  /*  public void openPopup(String status, String paySTATUS){
        dialogBuilder = new AlertDialog.Builder(DeliveryDetail_Multimerchant.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.paymen_status_dialog, null);
        dialogBuilder.setView(dialogView);dialogBuilder.setCancelable(true);

        // set the custom dialog components - text, image and button
        final LinearLayout psuccess =  dialogView.findViewById(R.id.psuccess);
        psuccess.setVisibility(View.VISIBLE);
        final LinearLayout pfail =  dialogView.findViewById(R.id.pfail);
        pfail.setVisibility(View.GONE);
        final ImageView gifimgsuccess =  dialogView.findViewById(R.id.gifimgsuccess);
        final Button btntryagain =  dialogView.findViewById(R.id.btntryagain);

       *//* GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifimgsuccess);
        Glide.with(this).load(R.raw.paymentsuccessfulgif).into(imageViewTarget);*//*

        *//*Glide.with(this)
                .load(R.raw.paymentsuccessfulgif)
                .into(gifimgsuccess);*//*

        if(status.equalsIgnoreCase("SUCCESS")){
            psuccess.setVisibility(View.VISIBLE);
            pfail.setVisibility(View.GONE);
        }else {
            psuccess.setVisibility(View.GONE);
            pfail.setVisibility(View.VISIBLE);
        }

        btntryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String transactionNote="Product purchase";
                String currencyUnit="INR";

                int randomPIN = (int)(Math.random()*9000)+1000;
                String val = ""+randomPIN;

                Uri uri = null;
                *//*try {
                    uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchantName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+TotalAmt+"&cu="+currencyUnit+"&tr="+val);
                    *//**//*uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchantName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+"1"+"&cu="+currencyUnit+"&tr="+val);*//**//*
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*//*

                Log.d("Order", "onClick: uri: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent,1);

            }
        });

        dialogBuilder.setCancelable(true);
        final AlertDialog b = dialogBuilder.create();
        b.show();
    }*/

    /*public void createPaymentJSON(){
        payment_jsonList.clear();

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //Feb 23 2016 12:16PM
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        String _txnDate = format.format(curDate);
        System.out.println(_txnDate);

        paySTATUS = "42";

        String qry1 = "Select * from "+DatabaseHandlers.TABLE_Payment;
        Cursor cq = sql_db.rawQuery(qry1,null);
        if(cq.getCount()>0){
            cq.moveToFirst();
            do {
                jsonPay = new JSONObject();
                try {
                    jsonPay.put("TransactionId",cq.getString(cq.getColumnIndex("TxnId")));
                    jsonPay.put("PaymentStatus",paySTATUS);
                    jsonPay.put("TransactionDate",_txnDate);
                    jsonPay.put("SOHeaderId",cq.getString(cq.getColumnIndex("SoHdrId")));
                    jsonPay.put("MerchantId",cq.getString(cq.getColumnIndex("MerchId")));
                    jsonPay.put("CustomerMasterId",AnyMartData.USER_ID);
                    jsonPay.put("PaymentMode",PaymentMode);

                    MyCartBean jBean = new MyCartBean();
                    jBean.setJsonData(jsonPay.toString());
                    jBean.setMerchantId(cq.getString(cq.getColumnIndex("MerchId")));
                    payment_jsonList.add(jBean);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }while (cq.moveToNext());

        }

        paymnt_callendIndex = payment_jsonList.size();
        String jsonData = payment_jsonList.get(paymnt_callIndex).getJsonData();
        String merId = payment_jsonList.get(paymnt_callIndex).getMerchantId();

        FinalJsonPayment = jsonData;

       *//* if (NetworkUtils.isNetworkAvailable(DeliveryDetail_Multimerchant.this)) {
            new StartSession(DeliveryDetail_Multimerchant.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostPaymentStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }

            });
        }*//*

    }*/

    /*class PostPaymentStatus extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;
        Object res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String url_authentication = AnyMartData.MAIN_URL + AnyMartData.api_postPaySTatus;

            //url_authentication = url_authentication.replaceAll(" ","%20");

            String auth = url_authentication;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                res = Utility.OpenPostConnection(url_authentication,
                        FinalJsonPayment.toString().replaceAll("^\"|\"$", ""),
                        DeliveryDetail_Multimerchant.this);
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                res = responseString;

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (responseString.contains("true")) {
                //data send to server
                paymnt_callIndex++;

                if(paymnt_callIndex < paymnt_callendIndex){
                    String jsonData = payment_jsonList.get(paymnt_callIndex).getJsonData();
                    String merId = payment_jsonList.get(paymnt_callIndex).getMerchantId();
                    FinalJsonPayment = jsonData;

                    if (NetworkUtils.isNetworkAvailable(DeliveryDetail_Multimerchant.this)) {
                        new StartSession_tbuds(DeliveryDetail_Multimerchant.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostPaymentStatus().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }

                        });
                    }

                }else if(paymnt_callIndex == paymnt_callendIndex){

                    try{
                     //   progress.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Bundle bplaceorder = new Bundle();
                    Intent intent = new Intent(DeliveryDetail_Multimerchant.this, ThankyouActivity_Multimerchant.class);

                    if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")){
                        intent.putExtra("User_Address", User_Address);
                        intent.putExtra("User_Address", editTextAddress.getText().toString());
                    } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
                        intent.putExtra("User_Vehicle", User_Vehicle);
                    }

                    bplaceorder.putString("PayableAmount", String.valueOf(amtTosend));
                    bplaceorder.putString("date", String.valueOf(date));
                    bplaceorder.putString("time", String.valueOf(prfDel_timefrm));
                    bplaceorder.putString("time_to", String.valueOf(prfDel_timeto));
                    bplaceorder.putString("delmode",deliverymodeStatus);

                    intent.putExtras(bplaceorder);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
           //     Toast.makeText(parent,""+getResources().getString(R.string.paysubsuss),Toast.LENGTH_SHORT).show();

            } else if (responseString.contains("false")) {
                //data not updated to server
            //    Toast.makeText(parent,""+getResources().getString(R.string.paysubfail),Toast.LENGTH_SHORT).show();

            }else {

            }
        }
    }*/

    public void insertIntoPaymentTable(String merchIdTopay, String TxnId){
        //databaseHelper.insertPaynowData(merchIdTopay,TxnId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Address) {
            editTextAddress.setText(data.getStringExtra("Address"));
            User_Address = data.getStringExtra("Address");

        }
        if (resultCode == Vehicle_no) {
            editTextAddress.setText(data.getStringExtra("Vehicle"));
            User_Vehicle = data.getStringExtra("Vehicle");
        }

        try{
            if(data!=null) {
                Log.d("TAG", "onActivityResult: data: " + data.getStringExtra("response"));
                String res = data.getStringExtra("response");
                String TxnId = data.getStringExtra("txnId");
                String pStatus = data.getStringExtra("Status");
                String search = "SUCCESS";

                try{
                    if (pStatus.equalsIgnoreCase(search)) {
                        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
                        //call API to send transactionid and status
                        paySTATUS="42";

                        OrderHistoryBean bean = new OrderHistoryBean();
                        bean.setTransactionId(TxnId);
                        paymentDoneList.add(bean);

                        insertIntoPaymentTable(merchIdTopay,TxnId);

                        //createPaymentJSON(TxnId,paySTATUS);

                      //  openPopup("SUCCESS","42");

                    } else {
                        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        paySTATUS="41";

                      //  openPopup("FAILURE","41");
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

       /* //test
       String TxnId = "1test";
        paySTATUS="41";
        OrderHistoryBean bean = new OrderHistoryBean();
        bean.setTransactionId(TxnId);
        paymentDoneList.add(bean);

        insertIntoPaymentTable(merchIdTopay,TxnId);*/

    }
}
