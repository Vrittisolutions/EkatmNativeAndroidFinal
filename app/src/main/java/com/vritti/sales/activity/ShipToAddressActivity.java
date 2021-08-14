package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.SegmentSelection;
import com.vritti.sales.adapters.ShipToListAdapter;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShipToAddressActivity extends FragmentActivity implements OnMapReadyCallback {

    private Context parent;
    ListView list_addresses;
    Toolbar toolbar;
    TextView txthdr;
    ProgressBar mprogress;
    ImageView imgadd, img_marker, img_add_address;
    Button btn_showmap, btn_add_address, btn_place;
    EditText edtsearch_address;

    private GoogleMap mMap;
    Double lng = 0.0d, lat = 0.0d;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    ArrayList<Customer> lstReference = new ArrayList<>();
    ShipToListAdapter shipAdapter;

    String usertype = "";
    String username = "";

    String CustomerID, final_shipto_address = "", intentFrom="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_ship_to_address);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(isnet()){
            new StartSession(parent, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadShipToDataJSON().execute();
                }
                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
        /*//get multiple shipto
        if(cf.getShipTocount() == 0){
            new DownloadShipToDataJSON().execute();
        }else {
            if(intentFrom.equalsIgnoreCase("CustomerSelectionScreen") || intentFrom.equalsIgnoreCase("AddNewAddress")){
                getDataFromDatabase();
            }else if(intentFrom.equalsIgnoreCase("LocateOnMap")) {
                new DownloadShipToDataJSON().execute();
            }
        }*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(ShipToAddressActivity.this);

        setListeners();
    }

    public void init(){
        parent = ShipToAddressActivity.this;

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        list_addresses = (ListView)findViewById(R.id.list_addresses);
        imgadd = (ImageView)findViewById(R.id.imgadd);
        img_add_address = (ImageView)findViewById(R.id.img_add_address);
        img_marker = (ImageView)findViewById(R.id.img_marker);
        edtsearch_address = (EditText)findViewById(R.id.edtsearch_address);
        btn_showmap = (Button) findViewById(R.id.btn_showmap);
        btn_add_address = (Button) findViewById(R.id.btn_add);
        btn_place = (Button)findViewById(R.id.btn_place);
        txthdr = (TextView)findViewById(R.id.txthdr);

        ut = new Utility();
        cf = new Tbuds_commonFunctions(ShipToAddressActivity.this);
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

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.AppCode = sharedpreferences.getString("AppCode","");
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        Intent intent = getIntent();
        CustomerID = intent.getStringExtra("CustVendorMasterId");
        username = intent.getStringExtra("username");

        txthdr.setText("Ship to Address -  "+ username);

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

    public void setListeners(){

        img_add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShipToAddressActivity.this, AddNewAddressActivity.class);
                intent.putExtra("CustVendorMasterId",CustomerID);
                intent.putExtra("username",username);
                startActivity(intent);
                finish();

            }
        });

        btn_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnyMartData.ADDRESS = edtsearch_address.getText().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Mobileno", AnyMartData.MOBILE);
                editor.putString("usertype", usertype);
                editor.putString("username", username);
                editor.putString("CustVendorMasterId", AnyMartData.USER_ID);
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.commit();

                Intent intent = new Intent(ShipToAddressActivity.this, MainActivity.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("intentFrom","SalesOrderTypeSelection");
                startActivity(intent);
                finish();
            }
        });

        btn_showmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtsearch_address.getText().toString().trim().equalsIgnoreCase("") ||
                        edtsearch_address.getText().toString().trim().equalsIgnoreCase(null)) {
                    Toast.makeText(parent, "No address to show", Toast.LENGTH_LONG).show();

                } else {
                    img_marker.setVisibility(View.VISIBLE);
                    onMapReady(mMap);
                }

            }
        });

        list_addresses.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                edtsearch_address.setText(lstReference.get(position).getShipToAddress());
                return true;
            }
        });

        list_addresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AnyMartData.ADDRESS = lstReference.get(position).getShipToAddress();
                AnyMartData.LATITUDE = lstReference.get(position).getLatitude();
                AnyMartData.LONGITUDE = lstReference.get(position).getLongitude();
                AnyMartData.SHIPTOMASTERID = lstReference.get(position).getShipTomasterId();

                getLatLng(AnyMartData.ADDRESS);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("Mobileno", AnyMartData.MOBILE);
                editor.putString("usertype", usertype);
                editor.putString("username", username);
                editor.putString("CustVendorMasterId", AnyMartData.USER_ID);
                editor.putString("CompanyURL", AnyMartData.MAIN_URL);
                editor.putString("ShipToId", AnyMartData.SHIPTOMASTERID);
                editor.putString("Address",  AnyMartData.ADDRESS);
                editor.putString("Latitude",  AnyMartData.LATITUDE);
                editor.putString("Longitude",  AnyMartData.LONGITUDE);
                editor.commit();

                if(AnyMartData.AppCode.equalsIgnoreCase("SM")){
                    /*new order booking call*/
                    //call segment selection
                    Intent intent = new Intent(ShipToAddressActivity.this, SegmentSelection.class);
                    intent.putExtra("callFrom","ShiptoAddress");
                    startActivity(intent);
                    finish();
                }else {
                    /*old call*/
                    Intent intent = new Intent(ShipToAddressActivity.this, MainActivity.class);
                    //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("intentFrom","SalesOrderTypeSelection");
                    startActivity(intent);
                    finish();
                }

            }
        });
    }

    public void getDataFromDatabase(){
        lstReference.clear();

        String query = "Select Address  from  "+db.TABLE_SHIPTO_DETAILS; // + " WHERE CustVendorMasterId='"+CustomerID+"'";
        Cursor c = sql.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                String Address = c.getString(c.getColumnIndex("Address"));

                Customer cust = new Customer();
                cust.setShipToAddress(Address);

                lstReference.add(cust);

            }while (c.moveToNext());

            shipAdapter = new ShipToListAdapter(ShipToAddressActivity.this, lstReference);
            list_addresses.setAdapter(shipAdapter);

        }else {

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        final String searchAddress = edtsearch_address.getText().toString();

        try {
            final String[] loc = new String[1];
            final Thread t = new Thread() {

                @SuppressLint("MissingPermission")
                public void run() {
                    try {
                        String url = "https://maps.google.com/maps/api/geocode/json?address=" + searchAddress + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
                        // String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + lat + "," + lang + "&sensor=true";
                        Object res = Utility.OpenConnection_ekatm(url, getApplicationContext());
                        if (res == null) {

                        } else {
                            String response = res.toString();
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(response);

                            // LatLng Source = getLatLng(jsonObj);

                            lng = new Double(0);
                            lat = new Double(0);

                            try {

                                lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lng");

                                lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lat");

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            if(lng != 0.0d || lat != 0.0d){
                                AnyMartData.LATITUDE = String.valueOf(lat);
                                AnyMartData.LONGITUDE = String.valueOf(lng);
                            }else {

                            }

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            t.start();

            t.join();

           /* LatLng address_ = new LatLng(lat, lng);
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
          //  mMap.addMarker(new MarkerOptions().position(address_).title(searchAddress));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(address_));
           // mMap.addMarker(new MarkerOptions().position(address_));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));*/

           /* mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    Log.i("lat:" + cameraPosition.target.latitude, "Long:" + cameraPosition.target.longitude);
                    convertLatLngintoAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                }
            });
            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    lat = mMap.getCameraPosition().target.latitude;
                    lng = mMap.getCameraPosition().target.longitude;
                    convertLatLngintoAddress(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                }
            });*/

        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    private void convertLatLngintoAddress(double lat, double lng) {
        {
            Geocoder geocoder = new Geocoder(this);
            try {

                List<Address> addressList = geocoder.getFromLocation(lat, lng, 5);
                if (addressList.size() != 0) {
                    String area = addressList.get(0).getAdminArea();
                    String cName = addressList.get(0).getCountryName();
                    String pCode = addressList.get(0).getPostalCode();
                    String cityName = addressList.get(0).getLocality();
                    String address = cityName + "," + area + "," + cName + "," + pCode;
                    edtsearch_address.setText(addressList.get(0).getAddressLine(0));
                    Log.i("Address :: ", address);

                    final_shipto_address = edtsearch_address.getText().toString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {

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

    public void getLatLng(String address){
        final String searchAddress = address;

        try {
            final String[] loc = new String[1];
            final Thread t = new Thread() {

                @SuppressLint("MissingPermission")
                public void run() {
                    try {
                        String url = "https://maps.google.com/maps/api/geocode/json?address=" + searchAddress + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
                        // String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + lat + "," + lang + "&sensor=true";
                        Object res = Utility.OpenConnection_ekatm(url, getApplicationContext());
                        if (res == null) {

                        } else {
                            String response = res.toString();
                            JSONObject jsonObj = null;
                            jsonObj = new JSONObject(response);

                            // LatLng Source = getLatLng(jsonObj);

                            lng = new Double(0);
                            lat = new Double(0);

                            try {

                                lng = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lng");

                                lat = ((JSONArray)jsonObj.get("results")).getJSONObject(0)
                                        .getJSONObject("geometry").getJSONObject("location")
                                        .getDouble("lat");

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            if(lng != 0.0d || lat != 0.0d){
                                AnyMartData.LATITUDE = String.valueOf(lat);
                                AnyMartData.LONGITUDE = String.valueOf(lng);
                            }else {

                            }

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            };
            t.start();

            t.join();

        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    class DownloadShipToDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        JSONArray jResults = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getMultipleShipToData + "?CustId="+CustomerID;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("^\"|\"+$","");
                    //response = response.substring(1, response.length() - 1);
                    jResults = new JSONArray(response);
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
            if (jResults != null) {
                //parse json
                parseShipToJson(jResults);

            }else {
                Toast.makeText(parent,"No data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parseShipToJson(JSONArray jResults){

        cf.clearTable(parent,db.TABLE_SHIPTO_DETAILS);
        lstReference.clear();

        String RowID="", Action="", ConsigneeName="", ContactPerson="", Address="", City = "",Phone="", Fax="", Mobile="", Country="",
                State="", ShipToMasterId = "",Latitude="", Longitude="", Distance="", RouteMasterId="", CityName="", CountryName,
                 StateName="", GeoLocation="", GSTCode="", GSTState="", GSTStateName="", Rating = "",TANNo="", TANNoName="", PAN="",
                EmailId="", isBlocked="", TAN_GSTIN_Number="";

        for(int i = 0; i<= jResults.length(); i++){
            try {
                JSONObject jObj_shipto = jResults.getJSONObject(i);

                RowID = jObj_shipto.getString("RowID");
                Action = jObj_shipto.getString("Action");
                ConsigneeName = jObj_shipto.getString("ConsigneeName");
                ContactPerson = jObj_shipto.getString("ContactPerson");
                Address = jObj_shipto.getString("Address");
                City = jObj_shipto.getString("City");
                Phone = jObj_shipto.getString("Phone");
                Fax = jObj_shipto.getString("Fax");
                Mobile = jObj_shipto.getString("Mobile");
                Country = jObj_shipto.getString("Country");
                State = jObj_shipto.getString("State");
                ShipToMasterId = jObj_shipto.getString("ShipToMasterId");
                Latitude = jObj_shipto.getString("Latitude");
                Longitude = jObj_shipto.getString("Longitude");
                Distance = jObj_shipto.getString("Distance");
                RouteMasterId = jObj_shipto.getString("RouteMasterId");
                CityName = jObj_shipto.getString("CityName");
                CityName = jObj_shipto.getString("CityName");
                CountryName = jObj_shipto.getString("CountryName");
                StateName = jObj_shipto.getString("StateName");
                GeoLocation = jObj_shipto.getString("GeoLocation");
                GSTCode = jObj_shipto.getString("GSTCode");
                GSTState = jObj_shipto.getString("GSTState");
                GSTStateName = jObj_shipto.getString("GSTStateName");
                Rating = jObj_shipto.getString("Rating");
                TANNo = jObj_shipto.getString("TANNo");
                TANNoName = jObj_shipto.getString("TANNoName");
                PAN = jObj_shipto.getString("PAN");
                EmailId = jObj_shipto.getString("EmailId");
                isBlocked = jObj_shipto.getString("isBlocked");
                TAN_GSTIN_Number = jObj_shipto.getString("TAN_GSTIN_Number");

                String City_state_pin_Country;
                if(CityName.equalsIgnoreCase("") || CityName.equalsIgnoreCase(null)){
                     City_state_pin_Country = City + " " + StateName + "" + CountryName;
                }else {
                     City_state_pin_Country = CityName + " " + StateName + "" + CountryName;
                }
                //String City_state_pin_Country = City + " " + StateName + "" + CountryName;

                Customer cust = new Customer();
                cust.setShipToAddress(Address);
                cust.setLatitude(Latitude);
                cust.setLongitude(Longitude);
                cust.setCity_state_pin_Country( City_state_pin_Country);
                cust.setShipTomasterId(ShipToMasterId);

                lstReference.add(cust);

                cf.insertShipTo(RowID, Action, ConsigneeName, ContactPerson, Address, City, Phone, Fax, Mobile, Country, State, ShipToMasterId,
                         Latitude, Longitude, Distance, RouteMasterId, CityName, CountryName, StateName, GeoLocation, GSTCode, GSTState,
                         GSTStateName, Rating,TANNo, TANNoName, PAN, EmailId, isBlocked, TAN_GSTIN_Number);

            } catch (JSONException e) {
                e.printStackTrace();
                //Toast.makeText(parent,"Data not inserted",Toast.LENGTH_SHORT).show();
            }

            shipAdapter = new ShipToListAdapter(ShipToAddressActivity.this, lstReference);
            list_addresses.setAdapter(shipAdapter);

            //getDataFromDatabase();

        }

    }

}
