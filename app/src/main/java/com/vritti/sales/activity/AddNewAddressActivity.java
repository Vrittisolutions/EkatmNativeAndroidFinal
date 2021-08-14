package com.vritti.sales.activity;

import android.app.ProgressDialog;
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
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.sales.PlaceClasses.PlaceArrayAdapter;
import com.vritti.sales.PlaceClasses.PlacePredictions;
import com.vritti.sales.beans.CityBean;
import com.vritti.sales.beans.StateBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static com.vritti.databaselib.data.Factory.TbudsFactory.CREATE_TABLE_CITIES;
import static com.vritti.databaselib.data.Factory.TbudsFactory.CREATE_TABLE_STATES;

public class AddNewAddressActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private Context parent;
    Toolbar toolbar;
    ProgressBar mprogress;
    AutoCompleteTextView edt_mainaddress, edt_subaddress, edt_city, edt_state, edt_pincode,
            edt_name, edt_mobile, edt_email, edt_business, edt_gstn, edt_tan;
    CheckBox chk_gstn;
    TextView txtline_mainaddr, txtline_subaddr, txtline_state_city_pincode;
    LinearLayout layout_txt;
    Button btn_confirm, btnclear;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions cf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;

    String usertype = "";
    String username = "";

    String CustomerID, final_shipto_address = "";

    ArrayList<StateBean> listStates;
    ArrayList<CityBean> listCities;

    private String Statename = "", City_name = "", spin_val1_state = "", spin_val2_city = "",
            Selected_stateID = "", Selected_cityID = "",postalCode = "";
    public JSONArray jrresult;

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
    String delAddress_latitude = "", delAddress_longitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_add_new_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(cf.getStatecount() > 0 ){
            getStateFromDatabase();

        }else {
            if (isnet()) {
                new StartSession(AddNewAddressActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetStates().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if(cf.getCitycount() > 0){
              getCityFromDatabase();
        }else {
            if (isnet()) {
                new StartSession(AddNewAddressActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetCities().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        /* *//*          Google API address   *//*
        convertLatLngintoAddress();
        DataLongOperationAsynchTask fetchUrl = new DataLongOperationAsynchTask();
        fetchUrl.execute();*/

        setListeners();
    }

    public void init(){
        parent = AddNewAddressActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
      //  toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Add New Address");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        layout_txt = (LinearLayout)findViewById(R.id.layout_txt);
        layout_txt.setVisibility(View.VISIBLE);

        edt_mainaddress = (AutoCompleteTextView) findViewById(R.id.edt_mainaddress);
        edt_subaddress = (AutoCompleteTextView) findViewById(R.id.edt_subaddress);
        edt_city = (AutoCompleteTextView) findViewById(R.id.edt_city);
        edt_state = (AutoCompleteTextView) findViewById(R.id.edt_state);
        edt_pincode = (AutoCompleteTextView) findViewById(R.id.edt_pincode);
        edt_name = (AutoCompleteTextView) findViewById(R.id.edt_name);
        edt_mobile = (AutoCompleteTextView) findViewById(R.id.edt_mobile);
        edt_email = (AutoCompleteTextView) findViewById(R.id.edt_email);
        edt_business = (AutoCompleteTextView) findViewById(R.id.edt_business);
        edt_gstn = (AutoCompleteTextView) findViewById(R.id.edt_gstn);
        edt_tan = (AutoCompleteTextView) findViewById(R.id.edt_tan);
        chk_gstn = (CheckBox) findViewById(R.id.chk_gstn);
        txtline_mainaddr = (TextView)findViewById(R.id.txtline_mainaddr);
        txtline_subaddr = (TextView)findViewById(R.id.txtline_subaddr);
        txtline_state_city_pincode = (TextView)findViewById(R.id.txtline_state_city_pincode);
        btn_confirm = (Button)findViewById(R.id.btn_confirm);
        btnclear = (Button)findViewById(R.id.btnclear);

        ut = new Utility();
        cf = new Tbuds_commonFunctions(AddNewAddressActivity.this);
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
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        Intent intent = getIntent();
        CustomerID = intent.getStringExtra("CustVendorMasterId");
        username = intent.getStringExtra("username");

         listStates = new ArrayList<>();
         listCities = new ArrayList<>();

    }

    public void setListeners(){

        edt_mainaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s == null || s.equals("")){
                    //Do not set text
                    txtline_mainaddr.setText("");
                }else {
                    txtline_mainaddr.setText(s.toString().trim());
                }

            }
        });

        edt_subaddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s == null || s.equals("")){
                    //Do not set text
                    txtline_subaddr.setText("");
                }else {
                    txtline_subaddr.setText(s.toString().trim());
                }

            }
        });

       /* edt_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s == null || s.equals("")){
                    //Do not set text
                    txtline_state_city_pincode.setText(spin_val2_city );
                }else {
                    spin_val1_state = s.toString();
                    txtline_state_city_pincode.setText(spin_val2_city + " "+ spin_val1_state );

                }
            }
        });*/

        edt_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                edt_city.setText("");

                Object item = adapterView.getItemAtPosition(position);
                String statename = edt_state.getText().toString().trim();
                try{
                    Selected_stateID = getPosition_Statefromspin(listStates, statename);
                }catch (Exception e){
                    e.printStackTrace();
                }

                spin_val1_state = statename;
                txtline_state_city_pincode.setText(spin_val2_city + " "+ spin_val1_state );

                new GetCities().execute();

                //getCityFromDatabase();
            }
        });

        edt_city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Object item = adapterView.getItemAtPosition(position);
                spin_val2_city = edt_city.getText().toString();
                try {
                    Selected_cityID = getPositionofCityfromspin(jrresult, spin_val2_city);
                    Log.e("CityID", Selected_cityID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(item.toString());
                txtline_state_city_pincode.setText("");
                txtline_state_city_pincode.setText(spin_val2_city + ", " + spin_val1_state);
            }
        });

        /*edt_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s == null || s.equals("")){
                    //Do not set text
                    txtline_state_city_pincode.setText(spin_val1_state);
                }else {
                    spin_val2_city = s.toString();
                    txtline_state_city_pincode.setText(s.toString() + " "+spin_val1_state );
                }
            }
        });*/

        edt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s == null || s.equals("")){
                    //Do not set text
                    txtline_state_city_pincode.setText(spin_val2_city + " "+spin_val1_state);
                }else {
                    postalCode = s.toString();
                    txtline_state_city_pincode.setText(spin_val2_city + " "+ spin_val1_state + " "+ s.toString());
                }
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_mainaddress.setText("");
                placePredictions.strSourceLatitude = "";
                placePredictions.strSourceLongitude = "";
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String final_address_main = txtline_mainaddr.getText().toString().trim();
               String final_subaddress = txtline_subaddr.getText().toString().trim();
               String City = edt_city.getText().toString().trim();
               String State = edt_state.getText().toString().trim();
               String pincode = edt_pincode.getText().toString().trim();
               String CompleteAddress = txtline_mainaddr.getText().toString().trim() + " " +
                       txtline_state_city_pincode.getText().toString().trim();

               AnyMartData.ADDRESS = CompleteAddress;

               getLatlngFromAddress();

               AnyMartData.LATITUDE = delAddress_latitude;      // placePredictions.strSourceLatitude;
               AnyMartData.LONGITUDE = delAddress_longitude;    //placePredictions.strSourceLongitude;

               Intent intent = new Intent(parent, LocateOnMapActivity.class);
               intent.putExtra("address_line_main",final_address_main);
               intent.putExtra("address_line_two",final_subaddress);
               intent.putExtra("city_state_area",City + " "+ State + " "+ pincode);
               intent.putExtra("CompleteAddress", CompleteAddress);
               intent.putExtra("latitude", AnyMartData.LATITUDE);
               intent.putExtra("longitude",AnyMartData.LONGITUDE);
               intent.putExtra("ConsigneeName", edt_name.getText().toString().trim());
               intent.putExtra("City", edt_city.getText().toString().trim());
               intent.putExtra("Country", "India");
               intent.putExtra("State", edt_state.getText().toString().trim());
               intent.putExtra("Mobile", edt_mobile.getText().toString().trim());
               intent.putExtra("ShipToMasterId", "");
               intent.putExtra("EmailId", edt_email.getText().toString().trim());
               intent.putExtra("TANNo", edt_tan.getText().toString().trim());
               intent.putExtra("TAN_GSTIN_Number", edt_gstn.getText().toString().trim());
               intent.putExtra("CustVendorMasterId",CustomerID);
               intent.putExtra("username",username);
               startActivity(intent);
               placePredictions.strSourceLatitude = "";
               placePredictions.strSourceLongitude = "";
               finish();

            }
        });
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
        Toast.makeText(gps, "GooglePlaceAPI connection failed with error code: "+connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    private void convertLatLngintoAddress() {
        GPSTracker gpsTracker = new GPSTracker(this);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        // latString = String.valueOf(lat);
        // lngString = String.valueOf(lng);

        double curentLat = gpsTracker.getLatitude();
        double curentLng = gpsTracker.getLongitude();

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
        ProgressDialog dialog = new ProgressDialog(AddNewAddressActivity.this);

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

        edt_mainaddress.setThreshold(1);
        edt_mainaddress.setOnItemClickListener(mAutocompleteClickListener);
        edt_mainaddress.setAdapter(mPlaceArrayAdapter);

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

            layout_txt.setVisibility(View.VISIBLE);
            txtline_mainaddr.setText(edt_mainaddress.getText().toString().trim());
        }
    };


    //GetStates
    private class GetStates extends AsyncTask<Void, Void, JSONArray> {
        String responseString = "", res = "";
        String respState = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(parent,
                    "Loading states..", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {

            String url_States = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_STATESLIST;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);

                res = Utility.OpenconnectionOrferbilling(url_States, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                respState = responseString.toString().replaceAll("\\\\", "");
                String rs = respState;
                Log.e("Response", respState);

                sql.execSQL("DROP TABLE IF EXISTS " + db.TABLE_STATES_SALES);
                sql.execSQL(CREATE_TABLE_STATES);
                jrresult = new JSONArray(respState);

                listStates.clear();

                for (int i = 0; i < jrresult.length(); ++i) {
                    JSONObject jsonObject = jrresult.getJSONObject(i);
                    String stateId = jsonObject.getString("PKStateId");
                    String statename = jsonObject.getString("StateDesc");

                   /* if(!statename.equalsIgnoreCase("") && statename !=null){
                        listStates.add(statename);
                    }*/

                    cf.addStates(stateId, statename);
                }

            } catch (Exception e) {
                respState = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            super.onPostExecute(result);

            if(!respState.equalsIgnoreCase("error")){
                getStateFromDatabase();
            }else {

            }

               /* //StateSpinner.setPrompt("Select State");
                edt_state.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        edt_state.showDropDown();
                        return false;
                    }
                });*/
            }
    }

    public void getStateFromDatabase() {
        listStates.clear();

        String query = "SELECT distinct state_id,state_name" +" FROM " + db.TABLE_STATES_SALES;

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                listStates.add(new StateBean(cur.getString(cur.getColumnIndex("state_id")),
                        cur.getString(cur.getColumnIndex("state_name"))
                ));

            } while (cur.moveToNext());

            ArrayAdapter<StateBean> stateadapter = new ArrayAdapter<StateBean>(parent,
                    android.R.layout.simple_spinner_dropdown_item, listStates);

            edt_state.setAdapter(stateadapter);
            edt_state.setThreshold(1);

            if(listStates.size() == 1){
                edt_state.setEnabled(false);
                edt_state.setSelection(0);
                edt_state.setText(listStates.get(0).getStateDesc());
                Selected_stateID = listStates.get(0).getPKStateId();
            }else if(listStates.size() > 1) {
                edt_state.setEnabled(true);
            }

        } else {

        }
    }

        //GetCities
    private class GetCities extends AsyncTask<Void, Void, JSONArray> {
            String responseString = "", res = "";
            String respCity = "";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(parent,
                        "Loading cities..", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected JSONArray doInBackground(Void... voids) {
                String url_Cities = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_CITYLIST +
                        "?Stateid=" + Selected_stateID;

                URLConnection urlConnection = null;
                BufferedReader bufferedReader = null;

                try {

                    res = Utility.OpenconnectionOrferbilling(url_Cities, parent);
                    int a = res.getBytes().length;
                    res = res.replaceAll("\\\\", "");
                    responseString = res.toString().replaceAll("^\"|\"$", "");

                    respCity = responseString.toString().replaceAll("\\\\", "");
                    String rs = respCity;
                    Log.e("Response", respCity);

                    sql.execSQL("DROP TABLE IF EXISTS " + db.TABLE_CITY_SALES);
                    sql.execSQL(CREATE_TABLE_CITIES);
                    JSONArray jrresult = new JSONArray(respCity);
                    listCities.clear();

                    for (int i = 0; i < jrresult.length(); ++i) {
                        JSONObject jsonObject = jrresult.getJSONObject(i);
                        String CityId = jsonObject.getString("PKCityID");
                        String CityName = jsonObject.getString("CityName");

                       // listCities.add(CityName);
                        cf.addCities(CityId, CityName, Selected_stateID);
                    }

                } catch (Exception e) {
                    respCity = "No Data";
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(JSONArray result) {
                super.onPostExecute(result);

                if (respCity.contains("No Data")) {
                    edt_city.setAdapter(null);
                } else {

                    getCityFromDatabase();

                }
            }
        }

    public void getCityFromDatabase() {
        listCities.clear();

        String query1 = "Select * from " + db.TABLE_CITY_SALES + " WHERE stateId='"+ Selected_stateID + "'";
        Cursor c1 = sql.rawQuery(query1, null);
        if (c1.getCount() > 0) {
            c1.moveToFirst();
            do {
                String city = c1.getString(c1.getColumnIndex("city_name"));

                //listCities.add(city);

                listCities.add(new CityBean(c1.getString(c1.getColumnIndex("city_id")),
                        c1.getString(c1.getColumnIndex("city_name"))));

            } while (c1.moveToNext());

            ArrayAdapter<CityBean> stateAdapter = new ArrayAdapter<CityBean>(parent,
                    android.R.layout.simple_spinner_dropdown_item, listCities);

            edt_city.setThreshold(1);
            edt_city.setAdapter(stateAdapter);

            if(listCities.size() == 1){
                edt_city.setEnabled(false);
                edt_city.setSelection(0);
                edt_city.setText(listCities.get(0).getCityName());
            }else if(listCities.size() > 1) {
                edt_city.setEnabled(true);
            }else if (listCities.size() == 0){
                edt_city.setEnabled(true);
            }

        } else {

        }

       /* edt_city.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                edt_city.showDropDown();
                return false;
            }
        });*/

    }

    private String getPosition_Statefromspin(ArrayList<StateBean> lst_State, String State) throws JSONException {
            String StateId = null;
            for (StateBean stateBean : lst_State) {
                if (stateBean.getStateDesc().equalsIgnoreCase(State)) {
                    StateId = stateBean.getPKStateId();
                }
            }
            return StateId; //it wasn't found at all
        }

    private String getPositionofStatefromspin(JSONArray jsonArray, String State) throws JSONException {
            String StateId = null;
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                if (jsonObject.getString("StateDesc").equals(State)) {
                    StateId = jsonObject.getString("PKStateId");
                    Log.e("StateID", StateId);
                    //this is the index of the JSONObject you want
                }
            }
            return StateId; //it wasn't found at all
        }

    private String getPositionofCityfromspin(JSONArray jsonArray, String City) throws JSONException {
            String CityId = null;
            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);
                if (jsonObject.getString("CityName").equals(City)) {
                    CityId = jsonObject.getString("PKCityID");
                    Log.e("CityID", CityId);
                    //this is the index of the JSONObject you want
                }
            }
            return CityId; //it wasn't found at all
        }

    public void getLatlngFromAddress() {
            Geocoder geocoder = new Geocoder(parent);
            try {
                List<Address> addressesList = geocoder.getFromLocationName(edt_mainaddress.getText().toString().trim(), 5);
                double lat = addressesList.get(0).getLatitude();
                double lng = addressesList.get(0).getLongitude();

                delAddress_latitude = String.valueOf(lat);
                delAddress_longitude = String.valueOf(lng);

            } catch (IOException e) {
                e.printStackTrace();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(parent,ShipToAddressActivity.class);
        intent.putExtra("CustVendorMasterId", CustomerID);
        intent.putExtra("username",username);
        intent.putExtra("intentFrom","AddNewAddressActivity");
        startActivity(intent);
        finish();
    }
}
