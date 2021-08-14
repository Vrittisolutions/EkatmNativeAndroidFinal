package com.vritti.sales.activity;

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
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.sales.PlaceClasses.PlaceArrayAdapter;
import com.vritti.sales.PlaceClasses.PlacePredictions;
import com.vritti.sales.adapters.CheckoutListAdapter;
import com.vritti.sales.beans.Address;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.NotificationBean;
import com.vritti.sales.beans.PlaceOrderBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HttpsURLConnection;
//import org.json.XML;

/**
 * Created by Chetana
 */

public class CheckoutActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private Context parent;
    private ProgressDialog progressDialog;
    private String json;
    private Button button_checkout;
    private Button btn_place_order;
    private TextView textview_cart, textview_total_amount_pay;
    TextView textview_total_amount;
    private EditText btndate, btntime, btntime_to;
  //  EditText btntime_to;
    AutoCompleteTextView editTextAddress;
    MyCartBean myCartBean;
    private ArrayList<MyCartBean> ArrayList;
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
    NotificationBean notificationBean;
    ArrayList<NotificationBean> notificationBeanArrayList;
    float flag = 0;
    //TextView tv;
    static Button notifCount;
    static int mNotifCount = 0;
    public static String today, todaysDate;
    ProgressHUD progress;
    ListView checkoutlist;
    String payableAmount;
    String DateToStr;
    private String SubcatID, PurDigit;
    ArrayList<Address> addressArrayList;
    Address addressbean;
    String homeaddress = "No address found", officeaddress = "No address found",
            currentaddress = "No address found",
            otheraddress = "No address found";
    private String currentTime;
    DatePickerDialog datePickerDialog;

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    String res = "";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    JSONObject JsonMain;
    PlaceOrderBean placeOrderBean;
    List<PlaceOrderBean> placeOrderBeanList  = new ArrayList<PlaceOrderBean>();
    String dabasename;

    //////////////////////New latlong
    PlacePredictions placePredictions = new PlacePredictions();
    Bundle extras;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = null;
    PlaceArrayAdapter mPlaceArrayAdapter;
    private int GOOGLE_API_CLIENT_ID = 2258;
    String locationStr = "", latn = "", lngn = "";
    AutocompleteFilter typeFilter;
    GPSTracker gps;
    String northLat = "", southLatValue = "", northLng = "", southLngValue = "", currentAddres;
    String prfDel_timefrm = "", prfDel_timeto = "", delAddress_latitude = "", delAddress_longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_checkout);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Checkout");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        delAddress_latitude = AnyMartData.LATITUDE;
        delAddress_longitude = AnyMartData.LONGITUDE;

        Picasso.with(parent)
                .load(image_URL)//Your image link url
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        //BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                        BitmapDrawable d = new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(bitmap, 110, 50, true));
                     //   ab.setIcon(d);
                     //   ab.setDisplayShowHomeEnabled(true);
                        //ab.setDisplayHomeAsUpEnabled(true);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }
                });

        cart_count = ArrayList.size();
        textview_cart.setText("cart (" + cart_count + ")");
        checkoutlist.setAdapter(new CheckoutListAdapter(parent,ArrayList));

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

       // showlist_new();
        /*           Google API address             */

        convertLatLngintoAddress();
        DataLongOperationAsynchTask fetchUrl = new DataLongOperationAsynchTask();
        fetchUrl.execute();

        getDataFromDatabase();
        homeaddress = AnyMartData.ADDRESS;
        editTextAddress.setText(homeaddress);

        setListeners();

    }

    @SuppressLint("WrongViewCast")
    private void initialize() {
        parent = CheckoutActivity.this;

        addressArrayList = new ArrayList<Address>();
        progressDialog = new ProgressDialog(parent);
        ArrayList = new ArrayList<MyCartBean>();
        linearlayout_checkout_container = (LinearLayout) findViewById(R.id.linearlayout_checkout_container);

        textview_cart = (TextView) findViewById(R.id.textview_cart);
        checkoutlist = (ListView)findViewById(R.id.listcheckout);
        textview_total_amount_pay = (TextView) findViewById(R.id.textview_total_amount_pay);
        btntime = findViewById(R.id.btntime);
        btntime_to = findViewById(R.id.btntime_to);
        btndate = findViewById(R.id.btndate);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(CheckoutActivity.this);
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
        mprogress=findViewById(R.id.toolbar_progress_App_bar);

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
        time = tcf.updateTime(hour, minute);
        System.out.println("time: " + time);

        btndate.setText(date);
        btntime.setText("9:00 AM");
        btntime_to.setText("6:00 PM");

        prfDel_timefrm = btntime.getText().toString();
        prfDel_timeto = btntime_to.getText().toString();

        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        editTextAddress = (AutoCompleteTextView) findViewById(R.id.editTextAddress);

        String edtaddress = editTextAddress.getText().toString();
        User_Address = editTextAddress.getText().toString();

        if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
            if (AnyMartData.URL.contains("petro")) {
                editTextAddress.setHint("Delivery Vehicle ");
            }else if (AnyMartData.URL.contains("bakery")) {
                editTextAddress.setHint("Delivery Address ");
            }
        }
        notificationBeanArrayList = new ArrayList<NotificationBean>();
       // AnyMartData.ADDRESS = editTextAddress.getText().toString();
        editTextAddress.setText(AnyMartData.ADDRESS);

        sb = new StringBuilder();
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);

       //restoredMobile = sharedpreferences.getString("Mobileno", null);
        restoredMobile = sharedpreferences.getString("MobileCust", null);
       //cvid = sharedpreferences.getString("userid", null);
        usertype = sharedpreferences.getString("usertype", null);
        restoredusername = sharedpreferences.getString("username", null);

        image_URL = sharedpreferences.getString("logopath",null);
        cvid = AnyMartData.USER_ID;

        gps = new GPSTracker(CheckoutActivity.this);

        linearlayout_checkout_container.setVisibility(View.INVISIBLE);

        Bundle b = getIntent().getExtras();
        String jsonData = b.getString("OrderedItems");
        SubcatID = b.getString("SubCategoryId");
        payableAmount = b.getString(String.valueOf("PayableAmount"));
        PurDigit = b.getString("PurDigit");

        if (tcf.getCartItems() > 0) {
            linearlayout_checkout_container.setVisibility(View.VISIBLE);
            ArrayList = CartActivity.myCartBeanArrayList;
        }
    }

    public void setListeners(){

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(CheckoutActivity.this,
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
                                    Toast.makeText(CheckoutActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
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

                mTimePicker = new TimePickerDialog(CheckoutActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = tcf.updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(CheckoutActivity.this,
                                            "Please select date first!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();
                                    } else {
                                        btntime.setText(time);
                                        prfDel_timefrm = btntime.getText().toString().trim();
                                        Toast.makeText(CheckoutActivity.this,
                                                "You cannot select a time earlier than current time!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, hour, minute, true);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
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

                mTimePicker = new TimePickerDialog(CheckoutActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                time = tcf.updateTime(selectedHour, selectedMinute);

                                if (date == null) {
                                    Toast.makeText(CheckoutActivity.this,
                                            "Please select date first!", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (compare_datetime(date + " " + time, selectedHour, selectedMinute) == true) {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;
                                    } else {
                                        btntime_to.setText(time);
                                        prfDel_timeto = time;
                                        Toast.makeText(CheckoutActivity.this,
                                                "You cannot select a time earlier than current time!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }, hour, minute, true);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        editTextAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {

                   /* if(databaseHelper.getAddress(restoredMobile) == 1){
                        editTextAddress.setText(AnyMartData.ADDRESS);

                    }else{*/
                    // editTextAddress.setText(homeaddress);
                    //                 Intent intent = new Intent(CheckoutActivity.this, GetAddressActivity.class);
                    //                 startActivityForResult(intent, Address);
                    // }

                }else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")){

                    //        Intent intent = new Intent(CheckoutActivity.this, GetVehicleNoActivity.class);
                    //        startActivityForResult(intent, Vehicle_no);
                }
            }
        });

        btn_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(editTextAddress.getText().toString().trim().equalsIgnoreCase("")
                        || editTextAddress.getText().toString().trim() == null
                        || date == null
                        || time == null)) {
                    tcf.clearTable(parent, databaseHelper.TABLE_CART_ITEM);

                    tcf.clearTable(parent, databaseHelper.TABLE_PLACE_ORDER);

                   delAddress_latitude =  AnyMartData.LATITUDE ;
                   delAddress_longitude = AnyMartData.LONGITUDE;

                    expectedDateTime = tcf.convertDateDDMMYYYYToYYYYMMDD(date)
                            + " " + tcf.convertTime12HrsTo24Hrs(time) + ":00.000";

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CustomerMasterId", cvid);
                        jsonObject.put("Mobile", restoredMobile);
                        jsonObject.put("NetAmt", payableAmount);
                        jsonObject.put("SOHeaderId", "");
                        jsonObject.put("sono", "");
                        jsonObject.put("prefDeliveryFromTime",prfDel_timefrm);
                        jsonObject.put("prefDeliveryToTime",prfDel_timeto);
                        jsonObject.put("delvAddressLatitude",delAddress_latitude);
                        jsonObject.put("delvAddressLongitude",delAddress_longitude);
                        jsonObject.put("WarehouseMasterId","");
                        jsonObject.put("OrderTypeMasterId",AnyMartData.OrderTypeMasterId);
                        if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
                            jsonObject.put("Delivery_Address", User_Address);
                            jsonObject.put("Delivery_Address", editTextAddress.getText().toString());
                        } else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
                            jsonObject.put("Delivery_Address", User_Vehicle);
                        }
                        jsonObject.put("Scheduledate",expectedDateTime);
                        jsonObject.put("AppEnvMasterId",EnvMasterId);
                        jsonObject.put("UserLoginId",LoginId);
                        jsonObject.put("UserPwd",Password);
                        jsonObject.put("PlantId",PlantMasterId);
                        jsonObject.put("DeliveryMode","Take Away");

                        try{
                            //jsonObject.put("MerchantId", "fee4b450-174f-4bdc-84d2-ad6eca7d37fc");
                            jsonObject.put("MerchantId", LoginId);
                            jsonObject.put("ShiptoId", AnyMartData.SHIPTOMASTERID);
                            jsonObject.put("ResReqId", "");
                            jsonObject.put("AddedBy", UserName);
                        }catch (Exception e){
                            e.printStackTrace();
                           // jsonObject.put("MerchantId", "fee4b450-174f-4bdc-84d2-ad6eca7d37fc");
                            jsonObject.put("MerchantId", LoginId);
                            jsonObject.put("ShiptoId", AnyMartData.SHIPTOMASTERID);
                            jsonObject.put("ResReqId", "");
                            jsonObject.put("AddedBy", UserName);
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
                            jsonObject1.put("MerchantId", LoginId);
                            jsonObject1.put("MerchantName", UserName);
                            jsonObject1.put("SODetailId", "");
                            jsonObject1.put("SOScheduleId", "");

                            jsonArray.put(jsonObject1);

                            if (tcf.getMerchantsAgainstItems_chkrecords(ArrayList.get(i).getMerchantId(),
                                    ArrayList.get(i).getProduct_name()) > 0) {
                                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                                ContentValues cv = new ContentValues();

                                cv.put("MerchantName", "TEST CHETANA MERCHANT");
                                cv.put("qnty", ArrayList.get(i).getQnty());
                                cv.put("offers", ArrayList.get(i).getOffers());
                                cv.put("price", ArrayList.get(i).getPrice());
                                cv.put("Freeitemqty", ArrayList.get(i).getFree_item_qnty_trade());
                                cv.put("Freeitemname", ArrayList.get(i).getFree_item_name_trade());
                                cv.put("validfrom", ArrayList.get(i).getValidfrom_trade());
                                cv.put("validto", ArrayList.get(i).getValidto_trade());
                                sql_db.update(databaseHelper.TABLE_MERCHANT_AGAINST_ITEM, cv, "MerchantId=? and Product_name=?",
                                        new String[]{ArrayList.get(i).getMerchantId(), ArrayList.get(i).getProduct_name()});

                            } else {
                                tcf.addMerchantsAgainstItems(ArrayList.get(i).getMerchantId(),
                                        ArrayList.get(i).getMerchantName(),
                                        String.valueOf(ArrayList.get(i).getQnty()),
                                        ArrayList.get(i).getMinqnty(),
                                        ArrayList.get(i).getOffers(),
                                        ArrayList.get(i).getPrice(),
                                        ArrayList.get(i).getProduct_name(),
                                        "",
                                        String.valueOf(ArrayList.get(i).getFree_item_qnty_trade()),
                                        ArrayList.get(i).getFree_item_name_trade(),
                                        ArrayList.get(i).getValidfrom_trade(),
                                        ArrayList.get(i).getValidto_trade());
                            }
                        }

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
                    if (NetworkUtils.isNetworkAvailable(CheckoutActivity.this)) {
                     //   startService(new Intent(CheckoutActivity.this, PlaceOrderService.class));
                        //Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_LONG).show();

                        CallplaceOrder();

                    } else {
                        Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                    }

                } else {

                    if (editTextAddress.getText().toString().trim().equalsIgnoreCase("")
                            || editTextAddress.getText().toString().trim() == null) {
                        Toast.makeText(parent, "Please fill address", Toast.LENGTH_LONG).show();
                    }
                    if (date == null) {
                        Toast.makeText(parent, "Please fill date of delivery", Toast.LENGTH_LONG).show();
                    }
                    if (time == null) {
                        Toast.makeText(parent, "Please fill time of delivery", Toast.LENGTH_LONG).show();
                    }
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

        if (tcf.getMerchantCount() > 0) {
            //   for (int i = 0; i < databaseHelper.getMerchantCount(); i++) {//
            cntAmt = 0;
            int i = 0;
            String Murchant_name = "";
       //     DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
       //     SQLiteDatabase db = db1.getWritableDatabase();
            Cursor c1 = sql_db.rawQuery("Select distinct MerchantName from "
                    + databaseHelper.TABLE_CART_ITEM, null);

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

    private void addView_new(int i, String M_name_1) {

        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_checkout_row,
                null);
        LinearLayout itemcheckoutfreelayout = (LinearLayout) baseView.findViewById(R.id.itemcheckoutfreelayout);
        TextView textview_merchant_name = (TextView) baseView
                .findViewById(R.id.textview_merchant_name);

        textview_total_amount = (TextView) baseView
                .findViewById(R.id.textview_total_amount);

        linearlayout_checkout_list = (LinearLayout) baseView.findViewById(R.id.linearlayout_checkout_row_container);

        TextView textview_checkout_free_name = (TextView) baseView
                .findViewById(R.id.textview_checkout_free_name);


        textview_merchant_name.setText(ArrayList.get(i).getMerchantName());

        M_name = ArrayList.get(i).getMerchantName();

        float cntAmt1 = 0;


        if (M_name_1.equalsIgnoreCase(M_name)) {
            if (!(i == 0)) {
                if (M_name_1.equalsIgnoreCase(ArrayList.get(i - 1).getMerchantName())) {
                    for (int k = 0; k < i; k++) {
                        if (M_name_1.equalsIgnoreCase(ArrayList.get(i - 1).getMerchantName())) {
                            flag = 0;
                        }
                    }
                } else {

                    String discount_vol = String.valueOf(ArrayList.get(i).getMinvalue());
                    if (ArrayList.size() > 0) {
                        discount_vol = String.valueOf(ArrayList.get(i).getMinvalue());
                        for (int j = 0; j < ArrayList.size(); j++) {
                            cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(ArrayList.get(j).getAmount()));

                        }
                    }

                    String splits[] = ArrayList.get(i).getDiscount().split(" ");
                    String disamt = splits[0];//10
                    String distype = splits[1];

                    if (distype.equalsIgnoreCase("₹")) {

                        if (Integer.parseInt(discount_vol) <= cntAmt1) {


                            cntAmt1 = cntAmt1 - Integer.parseInt(disamt);

                            for (int j = 0; j < ArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }

                        } else {

                        }
                    } else if (distype.equalsIgnoreCase("%")) {
                        if (Integer.parseInt(discount_vol) <= cntAmt1) {
                            float dis_amt = (Float.parseFloat(String.valueOf(ArrayList.get(i).getAmount())) * Float.parseFloat(disamt)
                                    / 100);

                            cntAmt1 = cntAmt1 - dis_amt;

                            for (int j = 0; j < ArrayList.size(); j++) {

                                addView_list(j, M_name_1);
                            }

                        } else {

                        }
                    }

                    Merchant_name = M_name_1;
                    ShowTextValue(M_name_1);

                    if (!(ArrayList.get(i).getFreeitemqnty() == 0)) {
                        itemcheckoutfreelayout.setVisibility(View.VISIBLE);
                        textview_checkout_free_name.setText("Get " + ArrayList.get(i).getFreeitemqnty() + " "
                                + ArrayList.get(i).getFreeitemname()
                                + " free with above purchase");
                    }
                }
            } else {
                String discount_vol = String.valueOf(ArrayList.get(i).getMinvalue());
                if (ArrayList.size() > 0) {
                    discount_vol = String.valueOf(ArrayList.get(i).getMinvalue());
                    for (int j = 0; j < ArrayList.size(); j++) {
                        cntAmt1 = cntAmt1 + Float.parseFloat(String.valueOf(ArrayList.get(j).getAmount()));

                    }
                }

                String splits[] = ArrayList.get(i).getDiscount().split(" ");
                String disamt = splits[0];//10
                String distype = splits[1];

                if (distype.equalsIgnoreCase("₹")) {

                    if (Integer.parseInt(discount_vol) <= cntAmt1) {


                        cntAmt1 = cntAmt1 - Float.parseFloat(disamt);

                        for (int j = 0; j < ArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    } else {

                    }
                } else if (distype.equalsIgnoreCase("%")) {
                    if (Integer.parseInt(discount_vol) <= cntAmt1) {
                        float dis_amt = (Float.parseFloat(String.valueOf(ArrayList.get(i).getAmount())) * Float.parseFloat(disamt)
                                / 100);

                        cntAmt1 = cntAmt1 - dis_amt;

                        for (int j = 0; j < ArrayList.size(); j++) {

                            addView_list(j, M_name_1);
                        }

                    } else {

                    }
                }


                Merchant_name = M_name_1;
                ShowTextValue(M_name_1);
                if (!(ArrayList.get(i).getFreeitemqnty() == 0)) {
                    itemcheckoutfreelayout.setVisibility(View.VISIBLE);
                    textview_checkout_free_name.setText("Get " + ArrayList.get(i).getFreeitemqnty() + " "
                            + ArrayList.get(i).getFreeitemname()
                            + " free with above purchase");
                }
            }

        } else {
            flag = 0;
        }

        if (flag > 5) {
            linearlayout_checkout_container.addView(baseView);
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

    private void addView_list(int j, String mname) {
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_checkout_item_list,
                null);
        ImageView imageview_product_co = (ImageView) baseView
                .findViewById(R.id.imageview_product_co);
        TextView textview_checkout_product_name = (TextView) baseView
                .findViewById(R.id.textview_checkout_product_name);
        TextView textview_checkout_offer_amount = (TextView) baseView
                .findViewById(R.id.textview_checkout_offer_amount);
        TextView textview_checkout_product_amount = (TextView) baseView
                .findViewById(R.id.textview_checkout_product_amount);
        final EditText edittext_checkout_product_qty = (EditText) baseView
                .findViewById(R.id.edittext_checkout_product_qty);
        TextView textview_item_free_name = (TextView) baseView
                .findViewById(R.id.textview_item_free_name);
        LinearLayout itemcheckoutfreelayout1 = (LinearLayout) baseView.
                findViewById(R.id.itemcheckoutfreelayout1);

        if (mname.equalsIgnoreCase(ArrayList.get(j).getMerchantName())) {
            Picasso.with(parent)
                    .load(ArrayList.get(j).getItemImgPath())
                 //  .placeholder(R.drawable.default_img).error(R.drawable.error)      // optional
                    .resize(50, 50)                        // optional
                    .into(imageview_product_co);
            textview_checkout_product_name.setText(ArrayList.get(j).getProduct_name());
            textview_checkout_offer_amount.setText(ArrayList.get(j).getOffers());
            edittext_checkout_product_qty.setTag(linearlayout_checkout_list.getChildCount());
            edittext_checkout_product_qty.setText(String.valueOf(ArrayList.get(j)
                    .getQnty()));
            textview_checkout_product_amount.setText(ArrayList.get(j).getAmount() + "");// ₹
            String discount_vol = String.valueOf(ArrayList.get(j).getMinvalue());
            Float c = ArrayList.get(j).getAmount();
            cntAmt = cntAmt + Float.parseFloat(String.valueOf(ArrayList.get(j).getAmount()));
            int getfreeitem = 0;
            if (Integer.parseInt(ArrayList.get(j).getFree_item_qnty_trade()) > 0) {
                itemcheckoutfreelayout1.setVisibility(View.VISIBLE);
                getfreeitem = Integer.parseInt(edittext_checkout_product_qty.getText().toString().trim())
                        * Integer.parseInt(ArrayList.get(j).getFree_item_qnty_trade())
                        / Integer.parseInt(ArrayList.get(j).getMinqnty());

               /* getfreeitem = (Integer.parseInt(edittext_cart_product_qty.getText().toString().trim())
                        * Integer.parseInt(myCartBeanArrayList.get(pos).getFree_item_qnty_trade())) /
                        Integer.parseInt(myCartBeanArrayList.get(pos).getMinqnty());*/

                textview_item_free_name.setText("Get " + getfreeitem + " " +
                        ArrayList.get(j).getFree_item_name_trade()
                        + " free with " + edittext_checkout_product_qty.getText().toString().trim() +
                        " " + ArrayList.get(j).getProduct_name());
            }
            linearlayout_checkout_list.addView(baseView);
        } else {
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
        //Intent i = new Intent(parent, com.vritti.orderbilling.customer.CartActivity.class);
        int i =0;

        JSONArray jsonArray1 = new JSONArray();
        if(ArrayList.size()>0){
            for ( i = 0; i < ArrayList.size(); i++) {

                String ItemName = ArrayList.get(i).getProduct_name();
                String ItemId = ArrayList.get(i).getProduct_id();
                float qty = ArrayList.get(i).getQnty();
                float ToatlAmount = ArrayList.get(i).getAmount();
                float Rate = Float.parseFloat(ArrayList.get(i).getPrice());
                String Merchant_Name = ArrayList.get(i).getMerchantName();
                String Merchant_id = ArrayList.get(i).getMerchantId();
                String perDigit = ArrayList.get(i).getPerdigit();

                JSONObject jsonObject = new JSONObject();

                try {

                    jsonObject.put("ItemName",ItemName);
                    jsonObject.put("ItemId",ItemId);
                    jsonObject.put("Qty",qty);
                    jsonObject.put("TotalAmount",ToatlAmount);
                    jsonObject.put("itemmrp",Rate);
                    jsonObject.put("custVendorname",Merchant_Name);
                    jsonObject.put("CustVendorMasterId",Merchant_id);
                    jsonObject.putOpt("PurDigit", PurDigit);
                    //price
                    jsonArray1.put(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Bundle b = new Bundle();
            Intent intent = new Intent(CheckoutActivity.this, CartActivity.class);
            b.putString("OrderedItems", jsonArray1.toString());
            b.putString("SubCategoryId",SubcatID);
            b.putString("PayableAmount",payableAmount);
            b.putString("PurDigit",PurDigit);
            intent.putExtras(b);
            startActivity(intent);
     //       overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
            finish();
        }

      /*
        Intent i = new Intent(parent, com.vritti.orderbilling.customer.MainActivity.class);
        startActivity(i);
        finish();*/

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
                time = tcf.updateTime(selectedHour, selectedMinute);
                b = true;
            } else {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                time = tcf.updateTime(hour+2, minute);
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    private void getDataFromDatabase() {
        addressArrayList.clear();

     //   DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
     //   SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery("Select distinct PermanentAddress,OfficeAddress" +
                        ",GpsLocationAddress,CurrentAddress from "
                        + databaseHelper.TABLE_MY_ADDRESS +
                        " where Mobile =" + AnyMartData.MOBILE
                , null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                addressbean = new Address();
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
         //   db.close();
         //   db1.close();
        }
    }

    public void CallThankYouActivity(Context context){

        Bundle bplaceorder = new Bundle();
        Intent intent = new Intent(context, ThankyouActivity.class);
        if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")){
            intent.putExtra("User_Address", User_Address);
            intent.putExtra("User_Address", editTextAddress.getText().toString());
        } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
            intent.putExtra("User_Vehicle", User_Vehicle);
        }
        //intent.putExtra("paybaleAmt", paybaleAmt);
        bplaceorder.putString("PayableAmount",String.valueOf(payableAmount));
        bplaceorder.putString("date",String.valueOf(date));
        bplaceorder.putString("time",String.valueOf(prfDel_timefrm));
        bplaceorder.putString("time_to",String.valueOf(prfDel_timeto));
        bplaceorder.putString("PurDigit",PurDigit);

        // intent.putExtra("PayableAmount", paybaleAmt);
        //intent.putExtra("date", date);//Utilities.convertDateDDMMYYYYToYYYYMMDD(date)
        // intent.putExtra("time", time);

        intent.putExtras(bplaceorder);
      // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // finish();
    }

    private void convertLatLngintoAddress() {
        GPSTracker gpsTracker = new GPSTracker(this);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // latString = String.valueOf(lat);
        // lngString = String.valueOf(lng);

        double curentLat = gpsTracker.getLatitude();
        double curentLng = gpsTracker.getLongitude();
                /*SharedHelper.putKey(context, "current_lat", current_lat);
            SharedHelper.putKey(context, "current_lng", current_lng);*/
        try {
            List<android.location.Address> addressList = geocoder.getFromLocation(curentLat, curentLng, 5);
            if (addressList.size() != 0) {
                String area = addressList.get(0).getAdminArea();
                String cName = addressList.get(0).getCountryName();
                String cityName = addressList.get(0).getLocality();
                currentAddres = cityName + "," + area + "," + cName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(gps, "GooglePlaceAPI connection failed with error code: "+connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(CheckoutActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String response;
            try {
                // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address="+txtaddressSource.getText().toString().trim()+"&key="+getResources().getString(R.string.google_map_api));
                response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address=" + currentAddres + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
                Log.d("response", "" + response);

                return new String[]{response};
            } catch (Exception e) {
                getLocation();
                return new String[]{"error"};

            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");
                // north  long
                double nothLng = 0.0d;
                nothLng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast")
                        .getDouble("lng");

                if (nothLng != 0.0d)
                    northLng = String.valueOf(nothLng);
                double nothLat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast")
                        .getDouble("lat");

                if (nothLat != 0.0d)
                    northLat = String.valueOf(nothLat);
                // south  long
                double southLng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest")
                        .getDouble("lng");

                if (southLng != 0.0d)
                    southLngValue = String.valueOf(southLng);

                double southLat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest")
                        .getDouble("lat");

                if (southLat != 0.0d)
                    southLatValue = String.valueOf(southLat);
                getLocation();
                ///////////////////////////////////

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);

                delAddress_latitude = String.valueOf(lat);
                delAddress_longitude = String.valueOf(lng);

            } catch (JSONException e) {
                e.printStackTrace();
                getLocation();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void getLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID,this)
                .addConnectionCallbacks(this)
                .build();

      /*  mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, typeFilter);*/

        double minLat = 0.0d, minLong = 0.0d, maxLat = 0.0d, maxLong = 0.0d;
        if (!(northLat.equals("") || northLat.equals("0.0d")))
            maxLat = Double.parseDouble(northLat);

        if (!(northLng.equals("") || northLng.equals("0.0d")))
            maxLong = Double.parseDouble(northLng);

        if (!(southLatValue.equals("") || southLatValue.equals("0.0d")))
            minLat = Double.parseDouble(southLatValue);

        if (!(southLngValue.equals("") || southLngValue.equals("0.0d")))
            minLong = Double.parseDouble(southLngValue);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong)), typeFilter);

        editTextAddress.setThreshold(1);
        editTextAddress.setOnItemClickListener(mAutocompleteClickListener);
        editTextAddress.setAdapter(mPlaceArrayAdapter);

    }

    private class GetLatLngByAddressAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(CheckoutActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String[] doInBackground(String... params) {
            String response;

            try {
                // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address="+txtaddressSource.getText().toString().trim()+"&key="+getResources().getString(R.string.google_map_api));
                response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address=" + editTextAddress.getText().toString().trim() + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
                Log.d("response", "" + response);

                return new String[]{response};
            } catch (Exception e) {
                getLocation();
                return new String[]{"error"};

            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");
                // north  long
                double nothLng = 0.0d;
                nothLng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast")
                        .getDouble("lng");

                if (nothLng != 0.0d)
                    northLng = String.valueOf(nothLng);
                double nothLat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("northeast")
                        .getDouble("lat");

                if (nothLat != 0.0d)
                    northLat = String.valueOf(nothLat);
                // south  long
                double southLng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest")
                        .getDouble("lng");

                if (southLng != 0.0d)
                    southLngValue = String.valueOf(southLng);

                double southLat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("bounds").getJSONObject("southwest")
                        .getDouble("lat");

                if (southLat != 0.0d)
                    southLatValue = String.valueOf(southLat);
                getLocation();
                ///////////////////////////////////

                double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);

                delAddress_latitude = String.valueOf(lat);
                delAddress_longitude = String.valueOf(lng);

            } catch (JSONException e) {
                e.printStackTrace();
                getLocation();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //  Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            // Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            //checkClickOk();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                //    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                places.getStatus().toString();
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            locationStr = String.valueOf(place.getAddress());
            placePredictions.strSourceLatitude = String.valueOf(place.getLatLng().latitude);
            placePredictions.strSourceLongitude = String.valueOf(place.getLatLng().longitude);
            placePredictions.strSourceLatLng = String.valueOf(place.getLatLng());

            LatLng sydney = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
     //       mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     //       mMap.addMarker(new MarkerOptions().position(sydney).title(place.getAddress().toString()));
     //       mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            //   mMap.addMarker(new MarkerOptions().position(sydney));
     //       mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //getLatLongFromAddress(address);
        }
    };

    private void CallplaceOrder() {

        placeOrderBeanList.clear();

        DatabaseHandlers db1_new = new DatabaseHandlers(CheckoutActivity.this,dabasename);
        SQLiteDatabase db_new = db1_new.getWritableDatabase();

        JsonMain = AnyMartData.JMain;

        Cursor c1 = db_new.rawQuery("Select * from " + DatabaseHandlers.TABLE_PLACE_ORDER + " where isUploaded='No'", null);

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
            db_new.close();
            c1.close();
        }

        if (NetworkUtils.isNetworkAvailable(CheckoutActivity.this)) {

            new StartSession_tbuds(CheckoutActivity.this, new CallbackInterface() {

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
                public void callfailMethod(String s) {
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

            if(JsonMain.toString() != null){

                /*new url POST type*/
                url_generateOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_GENERATE_ORDER;
                //"&Arr="+  URLEncoder.encode(JsonMain.toString(),"UTF-8");

            }else {
                //No data found
            }

            try {
                res = String.valueOf(Utility.OpenPostConnection(url_generateOrder,
                        JsonMain.toString().replaceAll("^\"|\"$",""),parent));
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

           if(response_generateOrder.equalsIgnoreCase("error") ||
                   response_generateOrder.equalsIgnoreCase("null") ||
                   response_generateOrder.equalsIgnoreCase(null) ||
                   response_generateOrder.contains("null")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity.this, "Sorry! Order not Placed due to network error", Toast.LENGTH_LONG).show();
                    }
                });
                placeOrderBeanList.clear();
            }else if(response_generateOrder.contains("ExceptionMessage")){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity.this, "Sorry! Order not Placed due to server error", Toast.LENGTH_LONG).show();
                    }
                });

                placeOrderBeanList.clear();

                String id[] = result.split(",");

                try{
                    sql_db.delete(DatabaseHandlers.TABLE_PLACE_ORDER, "Pid='"+ID+"'",
                            null);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CheckoutActivity.this, "Order Placed Successfully", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(CheckoutActivity.this, ThankyouActivity.class);

                if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")){
                    intent.putExtra("User_Address", User_Address);
                    intent.putExtra("User_Address", editTextAddress.getText().toString());
                } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")){
                    intent.putExtra("User_Vehicle", User_Vehicle);
                }
                //intent.putExtra("paybaleAmt", paybaleAmt);
                bplaceorder.putString("PayableAmount",String.valueOf(payableAmount));
                bplaceorder.putString("date",String.valueOf(date));
                bplaceorder.putString("time",String.valueOf(prfDel_timefrm));
                bplaceorder.putString("time_to",String.valueOf(prfDel_timeto));
                bplaceorder.putString("PurDigit",PurDigit);
                intent.putExtras(bplaceorder);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        }
    }
}