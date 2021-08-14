package com.vritti.sales.OrderBookingNew;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
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
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.CategoryAdapter;
import com.vritti.sales.OrderBookingNew.Adapter.CheckoutList_MultimerchantAdapter;
import com.vritti.sales.activity.GetAddressActivity;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.PlaceOrderBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.vritti.sales.beans.Tbuds_commonFunctions.updateTime;

public class CheckoutActivity_Multimerchant extends AppCompatActivity {
    private Context parent;
    private ProgressDialog progressDialog;
    private DatabaseHandlers databaseHelper;
    private String json;
    private Button btn_delivery;
    private Button btn_place_order;
    ImageView imgzoomview;
    LinearLayout imgzoomview_lay;
    TextView txtbname, txtiname;
    private TextView textview_cart, textview_total_amount_pay;
    TextView textview_total_amount;
    private EditText btndate, btntime, btntime_to;
    EditText editTextAddress;
    RadioGroup radgrpdelmode;
    RadioButton raddoorstep,radtakeaway;
    MyCartBean myCartBean;
    public static java.util.ArrayList<MyCartBean> ArrayList;
    LinearLayout linearlayout_checkout_container;
    LinearLayout linearlayout_checkout_list;
    float paybaleAmt = 0;
    String M_name = "", Merchant_name = "";
    public static String date = null;
    public static String time = null;
    String expectedDateTime;
    int cart_count = 0;
    static int year, month, day;
    JSONObject jsonMain;
    public static String xml1, xml2;
    public static int Address = 10;
    public static int Vehicle_no = 11;
    String User_Address, User_Vehicle;
    public String restoredMobile, restoredusername, cvid, usertype;
    private StringBuilder sb;
    SharedPreferences sharedpreferences;
    float cntAmt = 0;
    final static String GROUP_KEY_EMAILS = "group_order_billing";
    public static int NOTIFICATION_ID = 2;
    private NotificationManager mNotificationManager;
    int numMessages;
    NotificationCompat.Builder builder;
    float flag = 0;
    //TextView tv;
    static Button notifCount;
    static int mNotifCount = 0;
    public static String today, todaysDate;
    ListView checkoutlist;
    String payableAmount;
    String DateToStr;
    private String SubcatID, PurDigit, callFrom;
    String homeaddress = "No address found", officeaddress = "No address found",
            currentaddress = "No address found",
            otheraddress = "No address found";
    private String currentTime;
    DatePickerDialog datePickerDialog;

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    String res = "";
    String prfDel_timefrm = "", prfDel_timeto = "", delAddress_latitude = "", delAddress_longitude = "";
    String deliverymodeStatus = "";
    private float amt = 0;

    JSONObject JsonMain;
    PlaceOrderBean placeOrderBean;
    List<PlaceOrderBean> placeOrderBeanList  = new ArrayList<PlaceOrderBean>();
    String dabasename;
    SQLiteDatabase sql_db;

    RelativeLayout layyellow;
    int cartItmCnt=0;

    Utility ut;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        int count = 0;
        count = tcf.getCartItems1();
        cartItmCnt = count;
        textview_cart.setText(String.valueOf(count));

        linearlayout_checkout_container.setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();
        String jsonData = b.getString("OrderedItems");
        payableAmount = b.getString(String.valueOf("PayableAmount"));
        PurDigit = b.getString("PurDigit");
        callFrom = b.getString("callFrom");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("payableAmount", payableAmount);
        editor.commit();

        if (tcf.getCartItems1() > 0) {
            linearlayout_checkout_container.setVisibility(View.VISIBLE);
            if(callFrom.equalsIgnoreCase("CartActivity")){
                ArrayList = CartActivity_MultiMerchant.myCartBeanArrayList;
            }else if(callFrom.equalsIgnoreCase("WishList")){
                ArrayList = WishlistActivity.myCartBeanArrayList;
            }

            Collections.sort(ArrayList, new Comparator<MyCartBean>() {
                @Override
                public int compare(MyCartBean lhs, MyCartBean rhs) {
                    return rhs.getProduct_name().compareTo(lhs.getProduct_name());
                }
            });

            calculate_total();
        }

        cart_count = ArrayList.size();
        textview_cart.setText(String.valueOf(cart_count));
        checkoutlist.setAdapter(new CheckoutList_MultimerchantAdapter(parent,ArrayList));

        double amount = Double.parseDouble(payableAmount);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount_pay.setText(formatted+" ₹");

        //Current date/place order date logic getPlaceOrderDate
        //get_date();

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy"); //Feb 23 2016 12:16PM
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd yyyy");
        DateToStr = format.format(curDate);
        System.out.println(DateToStr);

        setListeners();

    }

    @SuppressLint("WrongViewCast")
    private void initialize() {
        parent = CheckoutActivity_Multimerchant.this;

        final ActionBar ab = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.chekout)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        linearlayout_checkout_container = (LinearLayout) findViewById(R.id.linearlayout_checkout_container);
        layyellow =  findViewById(R.id.layyellow);
        textview_cart = (TextView) findViewById(R.id.textview_cart);
        checkoutlist = (ListView)findViewById(R.id.listcheckout);
        textview_total_amount_pay = (TextView) findViewById(R.id.textview_total_amount_pay);
        btntime =  findViewById(R.id.btntime);
        btntime_to = findViewById(R.id.btntime_to);
        btndate = findViewById(R.id.btndate);
        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        radgrpdelmode =  findViewById(R.id.radgrpdelmode);
        raddoorstep =  findViewById(R.id.raddoorstep);
        radtakeaway =  findViewById(R.id.radtakeaway);
        btn_delivery =  findViewById(R.id.btn_delivery);
        imgzoomview =  findViewById(R.id.imgzoomview);
        imgzoomview_lay =  findViewById(R.id.imgzoomview_lay);
        txtbname =  findViewById(R.id.txtbname);
        txtiname =  findViewById(R.id.txtiname);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CheckoutActivity_Multimerchant.this);
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
        AnyMartData.LANGUAGE = sharedpreferences.getString("Language","");
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.CatImgPath = sharedpreferences.getString("CatImgPath","");
        AnyMartData.SubCatImgPath = sharedpreferences.getString("SubCatImgPath","");
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");

        progressDialog = new ProgressDialog(parent);
        ArrayList = new ArrayList<MyCartBean>();

        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        DateFormat datef = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat timef = new SimpleDateFormat("HH:mm aa");

        Date cal = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(cal);
        c.add(Calendar.DATE, 7);
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

       // AnyMartData.ADDRESS = editTextAddress.getText().toString();

    }

    public void setListeners(){

        layyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartItmCnt > 0){
                    Intent intent = new Intent(CheckoutActivity_Multimerchant.this, WishlistActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(CheckoutActivity_Multimerchant.this,getResources().getString(R.string.no_items_in_cart),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgzoomview_lay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                imgzoomview_lay.setVisibility(View.GONE);
                return false;
            }
        });

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CheckoutActivity_Multimerchant.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    btndate.setText(date);
                                } else {
                                    btndate.setText(date);
                                    Toast.makeText(CheckoutActivity_Multimerchant.this,
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

                mTimePicker = new TimePickerDialog(CheckoutActivity_Multimerchant.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(CheckoutActivity_Multimerchant.this,
                                            ""+getResources().getString(R.string.plz_sel_datefirst), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();
                                    } else {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();
                                        Toast.makeText(CheckoutActivity_Multimerchant.this,
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

                mTimePicker = new TimePickerDialog(CheckoutActivity_Multimerchant.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(CheckoutActivity_Multimerchant.this,
                                            ""+getResources().getString(R.string.plz_sel_datefirst), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;
                                    } else {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;
                                        Toast.makeText(CheckoutActivity_Multimerchant.this,
                                                ""+getResources().getString(R.string.you_cant_sel_time_early), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, hour, minute, true);// Yes 24 hour time
                mTimePicker.setTitle(""+getResources().getString(R.string.selecttime));
                mTimePicker.show();

            }
        });

        editTextAddress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
                   /* if(databaseHelper.getAddress(restoredMobile) == 1){
                        editTextAddress.setText(AnyMartData.ADDRESS);
                    }else{*/
                    // editTextAddress.setText(homeaddress);
                    Intent intent = new Intent(CheckoutActivity_Multimerchant.this, GetAddressActivity.class);
                    startActivityForResult(intent, Address);
                    // }

                }else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")){

                }
                return false;
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(editTextAddress.getText().toString().trim().equalsIgnoreCase("")
                        || editTextAddress.getText().toString().trim() == null
                        || date == null
                        || time == null)) {
                    tcf.clearTable(parent, DatabaseHandlers.TABLE_CART_ITEM);
                    tcf.clearTable(parent, DatabaseHandlers.TABLE_PLACE_ORDER);

                    xml1 = "<Header>";
                    xml1 += "<CustomerMasterId>" + cvid + "</CustomerMasterId>";
                    xml1 += "<Mobile>" + restoredMobile + "</Mobile>";
                    xml1 += "<NetAmt>" + paybaleAmt + "</NetAmt>";
                    if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
                        xml1 += "<Delivery_Address>" + User_Address + "</Delivery_Address>";
                    } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
                        xml1 += "<Delivery_Address>" + User_Vehicle + "</Delivery_Address>";
                    }
                    xml1 += "</Header>";

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CustomerMasterId", cvid);
                        jsonObject.put("Mobile", restoredMobile);
                        jsonObject.put("NetAmt", payableAmount);
                        jsonObject.put("SOHeaderId", "");
                        jsonObject.put("sono", "");
                        jsonObject.put("prefDeliveryFromTime",prfDel_timefrm);
                        jsonObject.put("prefDeliveryToTime",prfDel_timeto);
                        jsonObject.put("delvAddressLatitude","");
                        jsonObject.put("delvAddressLongitude","");
                        jsonObject.put("WarehouseMasterId","");
                        jsonObject.put("OrderTypeMasterId","DefData1"); //AnyMartData.OrderTypeMasterId
                        if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
                            jsonObject.put("Delivery_Address", User_Address);
                            jsonObject.put("Delivery_Address", editTextAddress.getText().toString());
                        } else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
                            jsonObject.put("Delivery_Address", User_Vehicle);
                        }

                        xml1 = jsonObject.toString();
                        //sb.setLength(0);
                        JSONArray jsonArray = new JSONArray();
                        //sb.append("<Detail>");
                        for (int i = 0; i < ArrayList.size(); i++) {

                            JSONObject jsonObject1 = new JSONObject();

                            jsonObject1.put("ItemMasterId", ArrayList.get(i).getProduct_id());
                            jsonObject1.put("ItemName", ArrayList.get(i).getProduct_name());
                            jsonObject1.put("Rate", ArrayList.get(i).getPrice());
                            jsonObject1.put("Qty", ArrayList.get(i).getQnty());
                            jsonObject1.put("Amount", ArrayList.get(i).getAmount());
                            jsonObject1.put("MerchantId", ArrayList.get(i).getMerchantId());
                            jsonObject1.put("MerchantName", ArrayList.get(i).getMerchantName());
                            jsonObject1.put("SODetailId", "");
                            jsonObject1.put("SOScheduleId", "");

                                        sb.append("<Table>");
                                        sb.append("<ItemMasterId>" + ArrayList.get(i).getProduct_id() + "</ItemMasterId>");
                                        sb.append("<ItemName>" + ArrayList.get(i).getProduct_name() + "</ItemName>");
                                        sb.append("<Rate>" + ArrayList.get(i).getPrice()
                                                + "</Rate>");
                                        sb.append("<Qty>" + ArrayList.get(i).getQnty()
                                                + "</Qty>");
                                        sb.append("<Amount>" + ArrayList.get(i).getAmount()
                                                + "</Amount>");
                                        sb.append("<MerchantId>" + ArrayList.get(i).getMerchantId()
                                                + "</MerchantId>");
                                        sb.append("<MerchantName>" + ArrayList.get(i).getMerchantName()
                                                + "</MerchantName>");
                                        sb.append("</Table>");

                            jsonArray.put(jsonObject1);

                            if (tcf.getMerchantsAgainstItems_chkrecords(ArrayList.get(i).getMerchantId(),
                                    ArrayList.get(i).getProduct_name()) > 0) {
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                                ContentValues cv = new ContentValues();
                                cv.put("MerchantName", ArrayList.get(i).getMerchantName());
                                cv.put("qnty", ArrayList.get(i).getQnty());
                                cv.put("offers", ArrayList.get(i).getOffers());
                                cv.put("price", ArrayList.get(i).getPrice());
                                cv.put("Freeitemqty", ArrayList.get(i).getFree_item_qnty_trade());
                                cv.put("Freeitemname", ArrayList.get(i).getFree_item_name_trade());
                                cv.put("validfrom", ArrayList.get(i).getValidfrom_trade());
                                cv.put("validto", ArrayList.get(i).getValidto_trade());
                                db.update(DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM, cv, "MerchantId=? and Product_name=?",
                                        new String[]{ArrayList.get(i).getMerchantId(), ArrayList.get(i).getProduct_name()});

                            } else {
                                tcf.addMerchantsAgainstItems(ArrayList.get(i).getMerchantId(),
                                        ArrayList.get(i).getMerchantName(),
                                        String.valueOf(ArrayList.get(i).getQnty()),
                                        ArrayList.get(i).getMinqnty(),
                                        ArrayList.get(i).getOffers(),
                                        ArrayList.get(i).getPrice(),
                                        ArrayList.get(i).getProduct_name(), "",
                                        String.valueOf(ArrayList.get(i).getFree_item_qnty_trade()),
                                        ArrayList.get(i).getFree_item_name_trade(),
                                        ArrayList.get(i).getValidfrom_trade(),
                                        ArrayList.get(i).getValidto_trade());
                            }
                        }

                        expectedDateTime = tcf.convertDateDDMMYYYYToYYYYMMDD(date)
                                + " " + tcf.convertTime12HrsTo24Hrs(time) + ":00.000";

                        // sb.append("</Detail>");
                        xml2 = jsonArray.toString();

                        jsonMain = new JSONObject();
                        jsonMain.put("HeaderData",jsonObject);
                        jsonMain.put("ItemData",jsonArray);
                        AnyMartData.JMain = jsonMain;

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    //add current date column as place order date
                    tcf.addPlaceOrder(usertype, expectedDateTime, xml1, xml2, DateToStr,"No");
                    if (NetworkUtils.isNetworkAvailable(parent)) {
                        // startService(new Intent(CheckoutActivity.this, PlaceOrderService.class));
                        // Toast.makeText(parent, "Order Placed Successfully", Toast.LENGTH_LONG).show();

                        CallplaceOrder();

                    } else {
                        Toast.makeText(parent, ""+parent.getResources().getString(R.string.servererror), Toast.LENGTH_LONG).show();
                    }

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
            }
        });

        raddoorstep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    raddoorstep.setChecked(true);
                    deliverymodeStatus=raddoorstep.getText().toString();

                }else {
                }
            }
        });

        radtakeaway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radtakeaway.setChecked(true);
                    deliverymodeStatus=radtakeaway.getText().toString();

                }else {
                }
            }
        });

        btn_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                JSONArray jsonArray1 = new JSONArray();
                if (ArrayList.size() > 0) {

                    Bundle b = new Bundle();
                    Intent intent = new Intent(CheckoutActivity_Multimerchant.this, DeliveryDetail_Multimerchant.class);
                    b.putString("PayableAmount", String.valueOf(amt));
                    b.putString("callFrom","MainCall");
                    intent.putExtras(b);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {
                    Toast.makeText(parent, ""+getResources().getString(R.string.no_prod_to_checkout), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////
    public String get_date(String d) {
        String toDate;
        try {

            Date date1 = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy hh:mm a");//Feb 23 2016 12:16PM

            SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd yyyy");
            String date = d;
            try {
                date1 = toFormat.parse(date);
            } catch (Exception e) {
                Log.e("Date", "" + e);
            }
            toDate = toFormat.format(date1);
        } catch (Exception e) {
            toDate = "";
        }
        return toDate;
    }

    private void showlist_new() {

        if (tcf.getMerchantCount1() > 0) {
            //   for (int i = 0; i < databaseHelper.getMerchantCount111(); i++) {//
            cntAmt = 0;
            int i = 0;
            String Murchant_name = "";
            Cursor c1 = sql_db.rawQuery("Select distinct MerchantName from "
                    + DatabaseHandlers.TABLE_CART_ITEM, null);

            if (c1.getCount() > 0) {
                c1.moveToFirst();
                do {
                    cntAmt = 0;
                    Murchant_name = c1.getString(c1.getColumnIndex("MerchantName"));
                    for (int k = 0; k < ArrayList.size(); k++) {
                      //  addView_new(k, Murchant_name);
                    }

                } while (c1.moveToNext());
            } else {

            }
        }
    }

    private void ShowTextValue(String M) {
        cntAmt = 0;
        paybaleAmt = 0;
        String discount = "0";
        flag = 0;
        String disamt = "";
        String distype = "";

        for (int k = 0; k < ArrayList.size(); k++) {
            if (M.equalsIgnoreCase(ArrayList.get(k).getMerchantName())) {
                Float c1 = ArrayList.get(k).getAmount();
                cntAmt = cntAmt + Float.parseFloat(String.valueOf(ArrayList.get(k).getAmount()));
                String splits[] = ArrayList.get(k).getDiscount().split(" ");
                disamt = splits[0];//10
                distype = splits[1];
            }
        }
        float amt = 0;
        for (int k = 0; k < ArrayList.size(); k++) {
            amt = amt + Float.parseFloat(String.valueOf(ArrayList.get(k).getAmount()));
        }

        if (distype.equalsIgnoreCase("₹")) {
            if (Integer.parseInt(discount) <= cntAmt) {
                cntAmt = cntAmt - Float.parseFloat(disamt);
                //textview_discount.setText(disamt + " " + distype);
            }
        } else if (distype.equalsIgnoreCase("%")) {
            if (Integer.parseInt(discount) <= cntAmt) {
                float dis_amt = ((cntAmt) * Float.parseFloat(disamt)
                        / 100);
                cntAmt = cntAmt - dis_amt;
                //  textview_discount.setText(disamt + " " + distype);
            }
        }
        if (cntAmt >= 200) {
        } else {
            cntAmt = cntAmt + 0;
            amt = amt + 0;
        }
        flag = cntAmt;
        paybaleAmt = (paybaleAmt + amt) - Float.parseFloat(disamt);
        textview_total_amount.setText(" ");

        double amount = Double.parseDouble(String.valueOf(cntAmt));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount.setText("Total " + formatted + " ₹");

        double amount1 = Double.parseDouble(String.valueOf(paybaleAmt));
       // DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted1 = formatter.format(amount1);
        textview_total_amount_pay.setText(formatted + " ₹");
    }

    public void expandImage(String imgPath, String itmname, String brandname, String content, String UOMCOde, String PackQtyOf){
        imgzoomview_lay.setVisibility(View.VISIBLE);

        if(PackQtyOf.equalsIgnoreCase("0") || PackQtyOf.equalsIgnoreCase("1") ||
                PackQtyOf.equalsIgnoreCase("0.0") || PackQtyOf.equalsIgnoreCase("1.0")){
            if(brandname.equalsIgnoreCase("") || brandname.equalsIgnoreCase("") ||
                    brandname.equalsIgnoreCase("null")){
                txtiname.setText(/*brandname+""+*/itmname+", "+content+" "+UOMCOde);
            }else {
                txtiname.setText(/*brandname+" "+*/itmname+", "+content+" "+UOMCOde);
            }
        }else {
            if(brandname.equalsIgnoreCase("") || brandname.equalsIgnoreCase("") ||
                    brandname.equalsIgnoreCase("null")){
                txtiname.setText(/*brandname+""+*/itmname+", "+getResources().getString(R.string.combo1)+" "+content+" "+UOMCOde+" x "+PackQtyOf);
            }else {
                txtiname.setText(/*brandname+" "+*/itmname+", "+getResources().getString(R.string.combo1)+" "+content+" "+UOMCOde+" x "+PackQtyOf);
            }
        }

        txtbname.setText(brandname);

        if(!imgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(imgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.SubCatImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.SubCatImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.CatImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.CatImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!AnyMartData.SpecImgPath.equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(AnyMartData.SpecImgPath)
                        //.resize(50,55).into(holder.imgitm);
                        .into(imgzoomview);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    public void calculate_total(){
        amt =0;
        for (int k = 0; k < ArrayList.size(); k++) {
            amt = amt + ArrayList.get(k).getAmount();
        }

        float payableAmount = amt;
        double amount = Double.parseDouble(String.valueOf(payableAmount));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        textview_total_amount_pay.setText(formatted+" ₹");
    }

    private void CallplaceOrder() {

        placeOrderBeanList.clear();

        JsonMain = AnyMartData.JMain;
        Cursor c1 = sql_db.rawQuery("Select * from " + DatabaseHandlers.TABLE_PLACE_ORDER + " where isUploaded='No'", null);

        /* Cursor c1 = sql_db.rawQuery("Select * from " + DatabaseHandlers.TABLE_PLACE_ORDER  + " where isUploaded='No'", null);*/
        placeOrderBeanList.clear();

        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                placeOrderBean = new PlaceOrderBean();
                placeOrderBean.setPid(c1.getInt(c1.getColumnIndex("Pid")));
                placeOrderBean.setUsertype(c1.getString(c1.getColumnIndex("C_V_type")));
                placeOrderBean.setExpectedDateTime(c1.getString(c1.getColumnIndex("schedule_date_time")));
                placeOrderBean.setXml1(c1.getString(c1.getColumnIndex("xml1")));
                placeOrderBean.setXml2(c1.getString(c1.getColumnIndex("xml2")));
                placeOrderBeanList.add(placeOrderBean);

            } while (c1.moveToNext());
        } else {
        }

        if (NetworkUtils.isNetworkAvailable(CheckoutActivity_Multimerchant.this)) {

            new StartSession_tbuds(CheckoutActivity_Multimerchant.this, new CallbackInterface() {

                @Override
                public void callMethod() {

                    if ((AnyMartData.SESSION_ID != null)
                            && (AnyMartData.HANDLE != null)) {
                        if (placeOrderBeanList.size() > 0) {
                            JSONObject jsonMain ;
                            for (int i = 0; i < placeOrderBeanList.size(); i++) {
                                String xml1 = placeOrderBeanList.get(i).getXml1();
                                String xml2 = placeOrderBeanList.get(i).getXml2();
                                String datetime = placeOrderBeanList.get(i).getExpectedDateTime();
                                String id = String.valueOf(placeOrderBeanList.get(i).getPid());

                                jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("HeaderData", xml1);
                                    jsonMain.put("ItemData",xml2);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                //  new PlaceOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,datetime, jsonMain.toString(), id);
                                new PlaceOrder().execute(datetime, jsonMain.toString(), id);
                                Log.e("In - ","place order execution failed");
                            }
                        }

                    } else {

                        //  new GetSessionId().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        Log.e("log - ","session execution failed");
                    }

                }

                @Override
                public void callfailMethod(String msg) {
                    
                }
            });
        }
        //if network off then condition
    }

    class PlaceOrder extends AsyncTask<String, Void, String> {

        String responseString = "";
        String response_generateOrder = "";
        String res ="";
        String ID = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String dt=params[0];
            String body=params[1];
            // ID = params[2];
            String url_generateOrder = "";

            try {
                if(JsonMain.toString() != null){

                    url_generateOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_GENERATE_ORDER +
                            "?handler="+ AnyMartData.HANDLE +
                            "&sessionid="+ AnyMartData.SESSION_ID +
                            "&Scheduledate="+ URLEncoder.encode(params[0],"UTF-8") +
                            // "&Arr="+  JsonMain.toString().replaceAll("^\"|\"$", "");
                            "&Arr="+  URLEncoder.encode(JsonMain.toString(),"UTF-8");
                }else {
                    //No data found
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                res = Utility.OpenconnectionOrferbilling(url_generateOrder, parent);
                //  int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");

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

            if(response_generateOrder.equalsIgnoreCase("Session Expired")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(CheckoutActivity_Multimerchant.this, ""+getResources().getString(R.string.session_expired), Toast.LENGTH_LONG).show();
                    }
                });

                placeOrderBeanList.clear();
            } else if (response_generateOrder.equalsIgnoreCase("error")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity_Multimerchant.this, ""+getResources().getString(R.string.sorry_ord_not_place), Toast.LENGTH_LONG).show();
                    }
                });
                placeOrderBeanList.clear();
            }else if(response_generateOrder.contains("ExceptionMessage")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity_Multimerchant.this, ""+getResources().getString(R.string.sorry_ord_not_place), Toast.LENGTH_LONG).show();
                    }
                });

                placeOrderBeanList.clear();

                String id[] = result.split(",");

                try{
                    sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+ID+"'", null);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity_Multimerchant.this, ""+getResources().getString(R.string.ord_place_success), Toast.LENGTH_LONG).show();
                    }
                });
                placeOrderBeanList.clear();

                String id[] = result.split(",");

                try{
                    sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+id[1]+"'",
                            null);
                    //new HistoryOrder().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }catch (Exception e){
                    e.printStackTrace();
                }

                Bundle bplaceorder = new Bundle();
                Intent intent = new Intent(CheckoutActivity_Multimerchant.this, ThankyouActivity_Multimerchant.class);

                if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")){
                    intent.putExtra("User_Address", User_Address);
                    intent.putExtra("User_Address", editTextAddress.getText().toString());
                } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
                    intent.putExtra("User_Vehicle", User_Vehicle);
                }
                //intent.putExtra("paybaleAmt", paybaleAmt);
                bplaceorder.putString("PayableAmount", String.valueOf(payableAmount));
                bplaceorder.putString("date", String.valueOf(date));
                bplaceorder.putString("time", String.valueOf(prfDel_timefrm));
                bplaceorder.putString("time_to", String.valueOf(prfDel_timeto));
                bplaceorder.putString("PurDigit",PurDigit);
                intent.putExtras(bplaceorder);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try{
            cartItmCnt = tcf.getCartItems1();
            textview_cart.setText(String.valueOf(cartItmCnt));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}