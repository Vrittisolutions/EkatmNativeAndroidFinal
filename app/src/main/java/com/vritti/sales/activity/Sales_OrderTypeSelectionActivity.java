package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.sales.PlaceClasses.PlaceArrayAdapter;
import com.vritti.sales.PlaceClasses.PlacePredictions;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class Sales_OrderTypeSelectionActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    AutoCompleteTextView edt_custtype, edt_addr;
    Spinner edt_ordtype;
    Button btnbookord, btncntrsales, btnsavecust, btncancelcust;
    ImageView btnnewcust, btnprospsetting,btnrefresh;
    LinearLayout lay_addnewcust, lay_btns,lay_ordtype,lay_custtype;
    ListView list_ordertypes;
    TextView txtaddr;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    List<String> lstOrdertype = new ArrayList<String>();
    ArrayList<Customer> customerArrayList;
    ArrayList<Customer> OrderTypeArrayList;
    String usertype = "";
    String username = "";
    String CustVendorMasterId;

    List<String> lstReference = new ArrayList<>();
    SearchableSpinner sReference;

    //////////////////////New latlong
    PlacePredictions placePredictions = new PlacePredictions();
    Bundle extras;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = null;
    PlaceArrayAdapter mPlaceArrayAdapter;
    private int GOOGLE_API_CLIENT_ID = 2258;
    String locationStr = "", latn = "", lngn = "";
    AutocompleteFilter typeFilter;
    String northLat = "", southLatValue = "", northLng = "", southLngValue = "", currentAddres;
    GPSTracker gps;
    private GoogleMap mMap;
    String shippingAddress;
    boolean IsShipInvRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_sales__order_type_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if (getOrderTypecount() > 0) {
            displayOrderType();
         //  GetCustomerData();
        } else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        GetOrderTypeData();
                 //       GetCustomerData();
                    }
                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        setListener();
    }

    public void init(){
        parent = Sales_OrderTypeSelectionActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Sales Order");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);

        //edt_custtype = (AutoCompleteTextView)findViewById(R.id.edt_custtype);
        edt_ordtype = (Spinner)findViewById(R.id.edt_ordtype);
        btnnewcust = (ImageView)findViewById(R.id.btnnewcust);
        btnnewcust.setVisibility(View.GONE);
        btnbookord = (Button)findViewById(R.id.btnbookord);
        btncntrsales = (Button)findViewById(R.id.btncntrsales);
        btnsavecust = (Button)findViewById(R.id.btnsavecust);
        btncancelcust = (Button)findViewById(R.id.btncancelcust);
        btnprospsetting = (ImageView)findViewById(R.id.btnprospsetting);
        btnprospsetting.setVisibility(View.GONE);
        btnrefresh = (ImageView)findViewById(R.id.btnrefresh);
        btnrefresh.setVisibility(View.VISIBLE);

        edt_addr = (AutoCompleteTextView)findViewById(R.id.edt_addr);

        lay_addnewcust = (LinearLayout)findViewById(R.id.lay_addnewcust);
        lay_btns = (LinearLayout)findViewById(R.id.lay_btns);
        lay_ordtype = (LinearLayout)findViewById(R.id.lay_ordtype);
        lay_custtype = (LinearLayout)findViewById(R.id.lay_custtype);
        sReference = (SearchableSpinner)findViewById(R.id.edt_custtype);
        list_ordertypes = (ListView)findViewById(R.id.list_ordertypes);
        txtaddr = (TextView) findViewById(R.id.txtaddr);

        ut = new Utility();
        cf = new Tbuds_commonFunctions(Sales_OrderTypeSelectionActivity.this);
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
        OrderTypeArrayList = new ArrayList<>();

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

    }

    public void setListener(){
        btnnewcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  lay_addnewcust.setVisibility(View.GONE);
                btnnewcust.setVisibility(View.GONE);
                lay_btns.setVisibility(View.GONE);
                lay_ordtype.setVisibility(View.GONE);
                lay_custtype.setVisibility(View.GONE);

                convertLatLngintoAddress();
                DataLongOperationAsynchTask fetchUrl = new DataLongOperationAsynchTask();
                fetchUrl.execute();*/
                Intent intent = new Intent(Sales_OrderTypeSelectionActivity.this, AddNewCustProspect.class);
                startActivity(intent);
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    new StartSession(parent, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            GetOrderTypeData();
                            //GetCustomerData();
                        }
                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }
        });

        btnprospsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sales_OrderTypeSelectionActivity.this,
                        ProspectSettingsActivity_tbuds.class);
                startActivity(intent);
            }
        });

        btnsavecust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_addnewcust.setVisibility(View.GONE);
                lay_btns.setVisibility(View.VISIBLE);
                lay_ordtype.setVisibility(View.VISIBLE);
                lay_custtype.setVisibility(View.VISIBLE);
                btnnewcust.setVisibility(View.VISIBLE);
                Toast.makeText(parent,"Customer saved successfully",Toast.LENGTH_SHORT).show();
            }
        });

        btncancelcust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_addnewcust.setVisibility(View.GONE);
                lay_btns.setVisibility(View.VISIBLE);
                lay_ordtype.setVisibility(View.VISIBLE);
                lay_custtype.setVisibility(View.VISIBLE);
                btnnewcust.setVisibility(View.VISIBLE);
            }
        });

        btnbookord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Mobileno", AnyMartData.MOBILE);
                editor.putString("usertype", usertype);
                editor.putString("username", username);
                editor.putString("CustVendorMasterId", AnyMartData.USER_ID);
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.commit();

                Intent intent = new Intent(Sales_OrderTypeSelectionActivity.this, MainActivity.class);
              //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("intentFrom","SalesOrderTypeSelection");
                startActivity(intent);
                finish();
            }
        });

        sReference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String CustId = customerArrayList.get(position).getClient_id();
                String nname =  customerArrayList.get(position).getCustomer_name();
                AnyMartData.USER_ID = customerArrayList.get(position).getClient_id();
                username = customerArrayList.get(position).getCustomer_name();
                shippingAddress = customerArrayList.get(position).getShipToAddress();
                AnyMartData.ADDRESS = shippingAddress;
                txtaddr.setText(AnyMartData.ADDRESS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edt_ordtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AnyMartData.Order_Type = edt_ordtype.getSelectedItem().toString();

                String query = "SELECT OrderTypeMasterId FROM "+ db.TABLE_OrderType+
                        " WHERE Description="+edt_ordtype.getSelectedItem().toString().trim();

                Cursor cotype = sql.rawQuery(query,null);
                if(cotype.getCount()>0){
                    cotype.moveToFirst();
                    AnyMartData.OrderTypeMasterId = cotype.getString(cotype.getColumnIndex("OrderTypeMasterId"));

                }else {

                }

                //AnyMartData.OrderTypeMasterId = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        list_ordertypes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AnyMartData.Order_Type = OrderTypeArrayList.get(position).getOrderType().trim();
                AnyMartData.OrderTypeMasterId = OrderTypeArrayList.get(position).getOrderTypeMasterId();
                IsShipInvRequired = OrderTypeArrayList.get(position).isShipInvRequired();

               /* String query = "SELECT OrderTypeMasterId FROM "+ db.TABLE_OrderType+
                        " WHERE Description="+OrderTypeArrayList.get(position).getOrderType();
                Cursor cotype = sql.rawQuery(q
                wuery,null);
                if(cotype.getCount()>0){
                    cotype.moveToFirst();
                    AnyMartData.OrderTypeMasterId = cotype.getString(cotype.getColumnIndex("OrderTypeMasterId"));
                }else {

                }*/

               if(AnyMartData.Order_Type.equals("Wholesale")){
     /****************************************Portal New sales order form****************************************************************/
                   Intent intent = new Intent(Sales_OrderTypeSelectionActivity.this, NewSalesOrderBooking.class);
                   intent.putExtra("OrderType",AnyMartData.Order_Type);
                   intent.putExtra("OrderTypeMasterId",AnyMartData.OrderTypeMasterId);
                   intent.putExtra("IsShipInvRequired",String.valueOf(IsShipInvRequired));
                   intent.putExtra("Mode","AddNew");
                   startActivity(intent);
               }else{
      /****************************************AnyDukaan sales order form****************************************************************/
                   Intent intent = new Intent(Sales_OrderTypeSelectionActivity.this, CustomersListSelection_Activity.class);
                   intent.putExtra("OrderType",AnyMartData.Order_Type);
                   startActivity(intent);

               }
            }
        });
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
        String countQuery = "SELECT  Description, OrderTypeMasterId, IsShipInvRequired FROM "+ db.TABLE_OrderType;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String Description = cursor.getString(cursor.getColumnIndex("Description"));
                String OrderTypeMasterId = cursor.getString(cursor.getColumnIndex("OrderTypeMasterId"));
                String _IsShipInvRequired = cursor.getString(cursor.getColumnIndex("IsShipInvRequired"));

                if(_IsShipInvRequired.equalsIgnoreCase("N")){
                    IsShipInvRequired = false;
                }else if(_IsShipInvRequired.equalsIgnoreCase("Y")){
                    IsShipInvRequired = true;
                }

                if(!Description.equalsIgnoreCase("")){
                    lstOrdertype.add(Description);
                }

                Customer c_ordertype = new Customer();
                c_ordertype.setOrderType(Description);
                c_ordertype.setOrderTypeMasterId(OrderTypeMasterId);
                c_ordertype.setShipInvRequired(IsShipInvRequired);
                OrderTypeArrayList.add(c_ordertype);

            } while (cursor.moveToNext());
        }

       MySpinnerAdapter adapter = new MySpinnerAdapter(parent, R.layout.crm_custom_spinner_txt, lstOrdertype);
       // edt_ordtype.setAdapter(adapter);
        list_ordertypes.setAdapter(adapter);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(gps, "GooglePlaceAPI connection failed with error code: "+connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
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
            showProgress();
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

                    sql.delete(db.TABLE_OrderType, null,null);
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
            hideProgress();
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
            List<Address> addressList = geocoder.getFromLocation(curentLat, curentLng, 5);
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

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(Sales_OrderTypeSelectionActivity.this);

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
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
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
        edt_addr.setThreshold(1);
        edt_addr.setOnItemClickListener(mAutocompleteClickListener);
        edt_addr.setAdapter(mPlaceArrayAdapter);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            //  Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            // Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            //checkClickOk();
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
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
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.addMarker(new MarkerOptions().position(sydney).title(place.getAddress().toString()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            //   mMap.addMarker(new MarkerOptions().position(sydney));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //getLatLongFromAddress(address);
        }
    };

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
                            String Mobile = jsonObject.getString("Mobile");
                            String Email = jsonObject.getString("Email");
                            String Address = jsonObject.getString("Address");

                            Customer cust = new Customer();
                            cust.setCustomer_name(CustVendorName);
                            cust.setClient_id(CustVendorMasterId);
                            cust.setShipToAddress(Address);
                            cust.setShipToEmail(Email);
                            cust.setShipToMobile(Mobile);

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

            MySpinnerAdapter strlst = new MySpinnerAdapter(Sales_OrderTypeSelectionActivity.this,
                    R.layout.crm_custom_spinner_txt, lstReference);
            sReference.setAdapter(strlst);
            //  customDept.notifyDataSetChanged();
            sReference.setSelection(0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(this, Sales_HomeSActivity.class));
        finish();
    }

}
