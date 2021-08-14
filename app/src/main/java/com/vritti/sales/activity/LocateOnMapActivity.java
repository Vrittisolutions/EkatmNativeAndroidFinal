package com.vritti.sales.activity;

import android.annotation.SuppressLint;
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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.mp4parser.srt.SrtParser;
import com.vritti.crm.classes.CommonFunctionCrm;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class LocateOnMapActivity extends FragmentActivity implements
        GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {
    private Context parent;

    AutoCompleteTextView edt_mainaddress;
    TextView txtline_mainaddr, txtline_subaddr, txtline_state_city_pincode;
    Button btnsave, btnclear, btnshow;

    private GoogleMap mMap;
    Double lng, lat;

    String main_address, subaddress, latitude = "", longitude = "", city_state_area, finalAddress, searchAddress = "";
    String _ConsigneeName = "",_City ="", _Country ="", _State ="", _Mobile ="", _ShipToMasterId ="", _EmailId ="",
            _TANNo ="",_TAN_GSTIN_Number = "", CustomerId = "", username = "";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static DatabaseHandlers dbhandler;
    static Tbuds_commonFunctions cf;
    CommonFunctionCrm crmf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String usertype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql_db;
    SharedPreferences sharedpreferences;

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

    String InsCountry1 = "", InsSeg1 = "", LendCountry1 = "", LendSeg1 = "", CallId = "", CustVendorCode = "", RegistrationFormNo = "",
            CustVendorName = "", ContactTitle = "", ContactName = "", Address = "", ShortName = "", City = "", Phone = "", Mobile = "",
            Email = "", Pin = "", State = "", Country = "", District = "", Taluka = "", Website = "", PriceListId = "", vendorCode = "",
            ENRect = "", ENInv = "", ENPndPO = "",EnterpriseType = "", CustVendor = "", IsWLForCRMRef = "", Active = "", IsActive = "",
            CountryId = "", ENGRN = "", ENPymt = "", CreditLimit = "", CreditDays = "", Currency = "", PaymentTerms = "", DeliveryTerms = "",
            TaxClass = "", SlCatId = "",ResellerName = "",CreditTerms = "", PayeeName = "", BankName = "", Branch = "", BankAddress = "",
            AccountNo = "", AccountType = "", IFSCode = "", RemittanceInstruction = "", EvaluationDt = "", ValidFrom = "",ValidTo = "",
            SystemUserId = "", IsApproved = "", IsContractReqd = "",CurrencyMasterID = "", EntityGroupMasterId = "", EntityClass = "",
            CustVendorType = "", Typeofservices = "", ServClId = "", PANNO = "", GSTNO = "", TAN_GSTIN_Number = "", TANNO = "",CSTNo = "",
            MSTNo = "", ECCNo = "", ServiceTaxNo = "", ExDivi = "", ExRange = "", CAT = "", AccountId = "", EsicNo = "", AadharNo = "",
            PFNo = "", CIN = "", EntityRestDate = "", ClientDetails = "", ShipToDetails = "", SalesFamily = "", LenderDetails = "",
            InsuranceDetails = "", BankPayeeName = "", ExpertiseDetails = "",Latitude = "", Longitude = "",VendorMasterID = "",
            TenorYear = "", IndIndemnity = "";

    String CustVendorMasterId = "", Fax="", CommPer="",CommFrom="",CommTo = "", InspectionBody="",CreationLevel="", UserLevel = "",
            IsDeleted = "", AddedBy = "", AddedDt = "", ModifiedBy = "", ModifiedDt = "", OMS = "", IsForeign = "", CommRate = "", IsSalesEngr = "",
            CopyofCustVendorName = "", ReslName = "", FKBusiSegmentId = "",CRMCode = "", GroupId = "", ExtnlSysRef1 = "", ExtnlSysRef2 = "",
            ExtnlSysRef3 = "";

    JSONObject jMain = null;
    String finalData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_locate_on_map);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        /*    Google API address    */
        convertLatLngintoAddress();
        new GetLocationOnMapAsynchTask().execute();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(LocateOnMapActivity.this);

        if (isnet()) {
            new StartSession(LocateOnMapActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //get data from database
                    new DownloadCustomerDataForUpdateJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        setListeners();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        searchAddress = finalAddress;

        try {
            final String[] loc = new String[1] ;
            final Thread t = new Thread() {

                @SuppressLint("MissingPermission")
                public void run() {

                    if(latitude.equals("") || longitude.equals("")){
                        Geocoder geocoder = new Geocoder(parent);
                        try {
                            List<Address> addressesList = geocoder.getFromLocationName(searchAddress.trim(), 5);
                            lat = addressesList.get(0).getLatitude();
                            lng = addressesList.get(0).getLongitude();

                            AnyMartData.LATITUDE = String.valueOf(lat);
                            AnyMartData.LONGITUDE = String.valueOf(lng);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                   /* try {
                        String url = "https://maps.google.com/maps/api/geocode/json?address=" + searchAddress.trim() + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk";
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

                                Log.e("LatitudeAndLong  : ","longitude"+lng.toString()+":"+"latitude:"+lat.toString());

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                Log.e("Exception : ",e.getMessage());
                                Toast.makeText(parent,"No address found", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        Log.e("Exception : ",e1.getMessage());
                    }*/

                    }else {
                        lat = Double.valueOf(latitude);
                        lng = Double.valueOf(longitude);
                        AnyMartData.LATITUDE = String.valueOf(lat);
                        AnyMartData.LONGITUDE = String.valueOf(lng);
                    }
                }
            };
            t.start();

            t.join();

            if(lat != 0.0 && lng != 0.0){

                LatLng address_latlng = new LatLng(lat, lng);
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // mMap.addMarker(new MarkerOptions().position(address_latlng).title(main_address));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(address_latlng));
                // mMap.addMarker(new MarkerOptions().position(address_latlng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        Log.i("lat:" + cameraPosition.target.latitude, "Long:" + cameraPosition.target.longitude);
                        Toast.makeText(parent, "Latitude :  "+cameraPosition.target.latitude +
                                " Longitude : "+cameraPosition.target.longitude, Toast.LENGTH_LONG).show();

                        AnyMartData.LATITUDE = String.valueOf(cameraPosition.target.latitude);
                        AnyMartData.LONGITUDE = String.valueOf(cameraPosition.target.longitude);
                       // convertLatLngintoAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                    }
                });

                mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                    @Override
                    public void onCameraIdle() {
                        lat = mMap.getCameraPosition().target.latitude;
                        lng = mMap.getCameraPosition().target.longitude;
                        Toast.makeText(parent, "Latitude :  "+lat +
                                " Longitude : "+lng, Toast.LENGTH_LONG).show();

                        AnyMartData.LATITUDE = String.valueOf(lat);
                        AnyMartData.LONGITUDE = String.valueOf(lng);
                       // convertLatLngintoAddress(mMap.getCameraPosition().target.latitude, mMap.getCameraPosition().target.longitude);
                    }
                });

            }else {
                searchAddress = city_state_area;
                onMapReady(mMap);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
            Log.i("mapError::" , e1.getMessage());
        }

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    public void init(){
        parent = LocateOnMapActivity.this;

        edt_mainaddress = (AutoCompleteTextView) findViewById(R.id.edt_mainaddress);
        txtline_mainaddr = (TextView)findViewById(R.id.txtline_mainaddr);
        txtline_subaddr = (TextView)findViewById(R.id.txtline_subaddr);
        txtline_state_city_pincode= (TextView)findViewById(R.id.txtline_state_city_pincode);
        btnsave = (Button)findViewById(R.id.btnsaveaddress);
        btnclear = (Button)findViewById(R.id.btnclear);
        btnshow = (Button)findViewById(R.id.btnshow);

        Intent intent = getIntent();
        main_address = intent.getStringExtra("address_line_main");
        subaddress = intent.getStringExtra("address_line_two");
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");
        city_state_area = intent.getStringExtra("city_state_area");
        finalAddress = intent.getStringExtra("CompleteAddress");
        _ConsigneeName = intent.getStringExtra("ConsigneeName");
        _City = intent.getStringExtra("City");
        _Country = intent.getStringExtra("Country");
        _State = intent.getStringExtra("State");
        _Mobile = intent.getStringExtra("Mobile");
        _ShipToMasterId = intent.getStringExtra("ShipToMasterId");
        _EmailId = intent.getStringExtra("EmailId");
        _TANNo = intent.getStringExtra("TANNo");
        _TAN_GSTIN_Number = intent.getStringExtra("TAN_GSTIN_Number");
        CustomerId = intent.getStringExtra("CustVendorMasterId");
        username = intent.getStringExtra("username");

        txtline_mainaddr.setText(main_address);
        //txtline_subaddr.setText(subaddress);
        txtline_state_city_pincode.setText(city_state_area);

        edt_mainaddress.setText(finalAddress);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(LocateOnMapActivity.this);
        cf = new Tbuds_commonFunctions(LocateOnMapActivity.this);
        crmf = new CommonFunctionCrm(LocateOnMapActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(parent, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo/*"7057411246"*/;  //customer's mobile number.
        usertype = "C";

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

    }

    public void setListeners(){

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_mainaddress.setText("");
            }
        });

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                latitude = ""; longitude = "";
                finalAddress = edt_mainaddress.getText().toString().trim();
                onMapReady(mMap);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnyMartData.ADDRESS = finalAddress;

                jMain = getDataToSaveEntityPassAPI();
                finalData = jMain.toString().replaceAll("\\\\", "");
                finalData = finalData.replaceAll("^\"+ \"+$","");
                String a = "\"[";
                finalData = finalData.replace(a,"[");
                String b = "]\"";
                finalData = finalData.replace(b,"]");

                new PostUpdateEntityAPI().execute();

                Intent intent = new Intent(parent, ShipToAddressActivity.class);
                intent.putExtra("CustVendorMasterId", CustomerId);
                intent.putExtra("username",username);
                intent.putExtra("intentFrom","LocateOnMap");
                startActivity(intent);
                finish();

            }
        });

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

    private class GetLocationOnMapAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(LocateOnMapActivity.this);

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
                response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address="
                        + currentAddres + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
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
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID,LocateOnMapActivity.this)
                .addConnectionCallbacks(this)
                .build();

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
        }
    };

    class DownloadCustomerDataForUpdateJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        JSONObject jResults = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getRecordCustomer + "?CustId="+CustomerId;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("^\"|\"+$","");
                    response = response.substring(1, response.length() - 1);
                    jResults = new JSONObject(response);

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
                parseCustomerJson(jResults);

            }else {
                Toast.makeText(parent,"No data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void parseCustomerJson(JSONObject jsonObject){

            try {

                CustVendorMasterId = jsonObject.getString("CustVendorMasterId");
                CustVendor = jsonObject.getString("CustVendor");
                CustVendorCode = jsonObject.getString("CustVendorCode");
                CustVendorName = jsonObject.getString("CustVendorName");
                ShortName = jsonObject.getString("ShortName");
                CustVendorType = jsonObject.getString("CustVendorType");
                ContactName = jsonObject.getString("ContactName");
                ContactTitle = jsonObject.getString("ContactTitle");
                Address = jsonObject.getString("Address");
                PaymentTerms = jsonObject.getString("PaymentTerms");
                DeliveryTerms = jsonObject.getString("DeliveryTerms");
                City = jsonObject.getString("City");
                CurrencyMasterID = jsonObject.getString("CurrencyMasterID");
                Country = jsonObject.getString("Country");
                Pin = jsonObject.getString("Pin");
                Phone = jsonObject.getString("Phone");
                Fax = jsonObject.getString("Fax");
                Mobile = jsonObject.getString("Mobile");
                Email = jsonObject.getString("Email");
                CAT = jsonObject.getString("CAT");
                CreditLimit = jsonObject.getString("CreditLimit");
                CreditDays = jsonObject.getString("CreditDays");
                CreditTerms = jsonObject.getString("CreditTerms");
                CommPer = jsonObject.getString("CommPer");
                Currency = jsonObject.getString("Currency");
                BankName = jsonObject.getString("BankName");
                BankAddress = jsonObject.getString("BankAddress");
                CSTNo = jsonObject.getString("CSTNo");
                MSTNo = jsonObject.getString("MSTNo");
                ECCNo = jsonObject.getString("ECCNo");
                ExDivi = jsonObject.getString("ExDivi");
                ExRange = jsonObject.getString("ExRange");
                CountryId = jsonObject.getString("CountryId");
                Active = jsonObject.getString("Active");
                InspectionBody = jsonObject.getString("InspectionBody");
                RemittanceInstruction = jsonObject.getString("RemittanceInstruction");
                State = jsonObject.getString("State");
                PANNO = jsonObject.getString("PANNO");
                ServiceTaxNo = jsonObject.getString("ServiceTaxNo");
                EnterpriseType = jsonObject.getString("EnterpriseType");
                CreationLevel = jsonObject.getString("CreationLevel");
                UserLevel = jsonObject.getString("UserLevel");
                IsDeleted = jsonObject.getString("IsDeleted");
                AddedBy = jsonObject.getString("AddedBy");
                ModifiedBy = jsonObject.getString("ModifiedBy");
                AccountId = jsonObject.getString("AccountId");
                PayeeName = jsonObject.getString("PayeeName");
                OMS = jsonObject.getString("OMS");
                EntityGroupMasterId = jsonObject.getString("EntityGroupMasterId");
                Typeofservices = jsonObject.getString("Typeofservices");
                IsForeign = jsonObject.getString("IsForeign");
                CRMCode = jsonObject.getString("CRMCode");
                EntityClass = jsonObject.getString("EntityClass");
                SystemUserId = jsonObject.getString("SystemUserId");
                CommRate = jsonObject.getString("CommRate");
                ExtnlSysRef1 = jsonObject.getString("ExtnlSysRef1");
                ExtnlSysRef2 = jsonObject.getString("ExtnlSysRef2");
                ExtnlSysRef3 = jsonObject.getString("ExtnlSysRef3");
                IFSCode = jsonObject.getString("IFSCode");
                Branch = jsonObject.getString("Branch");
                AccountType = jsonObject.getString("AccountType");
                AccountNo = jsonObject.getString("AccountNo");
                RegistrationFormNo = jsonObject.getString("RegistrationFormNo");
                IsSalesEngr = jsonObject.getString("IsSalesEngr");
                TaxClass = jsonObject.getString("TaxClass");
                ServClId = jsonObject.getString("ServClId");
                SlCatId = jsonObject.getString("SlCatId");
                ENPndPO = jsonObject.getString("ENPndPO");
                ENGRN = jsonObject.getString("ENGRN");
                ENPymt = jsonObject.getString("ENPymt");
                ENInv = jsonObject.getString("ENInv");
                ENRect = jsonObject.getString("ENRect");
                TANNO = jsonObject.getString("TANNO");
                PriceListId = jsonObject.getString("PriceListId");
                IsApproved = jsonObject.getString("IsApproved");
                ResellerName = jsonObject.getString("ResellerName");
                IsActive = jsonObject.getString("IsActive");
                IsWLForCRMRef = jsonObject.getString("IsWLForCRMRef");
                FKBusiSegmentId = jsonObject.getString("FKBusiSegmentId");
                Latitude = jsonObject.getString("Latitude");
                Longitude = jsonObject.getString("Longitude");
                Taluka = jsonObject.getString("Taluka");
                District = jsonObject.getString("District");
                Website = jsonObject.getString("Website");
                CopyofCustVendorName = jsonObject.getString("CopyofCustVendorName");
                IsContractReqd = jsonObject.getString("IsContractReqd");
                vendorCode = jsonObject.getString("vendorCode");
                AadharNo = jsonObject.getString("AadharNo");
                EsicNo = jsonObject.getString("EsicNo");
                PFNo = jsonObject.getString("PFNo");
                CIN = jsonObject.getString("CIN");
                TenorYear = jsonObject.getString("TenorYear");
                IndIndemnity = jsonObject.getString("IndIndemnity");
                GroupId = jsonObject.getString("GroupId");
                ReslName = jsonObject.getString("ReslName");
                ModifiedDt = jsonObject.getString("ModifiedDt");

                if(ModifiedDt.equalsIgnoreCase("null")){
                    ModifiedDt = "";
                }else {
                    ModifiedDt = getDate(Long.parseLong(Replace(jsonObject.getString("ModifiedDt"))),"yyyy-MM-dd");
                }

                CommFrom = getDate(Long.parseLong(Replace(jsonObject.getString("CommFrom"))),"yyyy-MM-dd");
                CommTo = getDate(Long.parseLong(Replace(jsonObject.getString("CommTo"))),"yyyy-MM-dd");
                AddedDt = getDate(Long.parseLong(Replace(jsonObject.getString("AddedDt"))),"yyyy-MM-dd");
                EvaluationDt = getDate(Long.parseLong(Replace(jsonObject.getString("EvaluationDt"))),"yyyy-MM-dd");
                ValidFrom = getDate(Long.parseLong(Replace(jsonObject.getString("ValidFrom"))),"yyyy-MM-dd");
                ValidTo = getDate(Long.parseLong(Replace(jsonObject.getString("ValidTo"))),"yyyy-MM-dd");
                EntityRestDate = getDate(Long.parseLong(Replace(jsonObject.getString("EntityRestDate"))),"yyyy-MM-dd");

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    public String Replace(String date){
        String Date = "";
        date = date.replace("/Date(","").trim();
        Date = date.replace(")/","").trim();
        return Date;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        try {
            // Create a DateFormatter object for displaying date in specified format.
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            return formatter.format(calendar.getTime());
        }catch (NullPointerException ne){
            ne.printStackTrace();
            return "";
        }

    }

    public JSONObject getContactDetailsData(){

        JSONObject ContactDetails_JSON = null;

        String ContPerName = "", Designation = "", DIN = "", EmailId = "", InfluentialLevel = "", IsDefault = "",
                EntityContactInfoId = "",ContactNo = "", IsVWBLoginAllowed = "", CD_LoginId = "", CD_Password = "";

        JSONObject jObj_contactDetails = new JSONObject();
        try {
            jObj_contactDetails.put("ContPerName",ContPerName);
            jObj_contactDetails.put("Designation",Designation);
            jObj_contactDetails.put("DIN",DIN);
            jObj_contactDetails.put("EmailId",Email);
            jObj_contactDetails.put("InfluentialLevel",InfluentialLevel);
            jObj_contactDetails.put("IsDefault",IsDefault);
            jObj_contactDetails.put("EntityContactInfoId",EntityContactInfoId);
            jObj_contactDetails.put("ContactNo",Mobile);
            jObj_contactDetails.put("IsVWBLoginAllowed",IsVWBLoginAllowed);
            jObj_contactDetails.put("LoginId",Email);
            jObj_contactDetails.put("Password",CD_Password);

            ContactDetails_JSON = jObj_contactDetails;

        } catch (JSONException e) {
            e.printStackTrace();
            ContactDetails_JSON = null;
        }

        return ContactDetails_JSON;

    }

    public JSONObject getShipToDetailsData(){

        JSONObject shiptoDetails_JSON = null;
        String ConsigneeName = "",ContactPerson = "",Address = "", City = "", Country = "",State = "", CountryName = "", StateName = "",
                Phone = "",Mobile = "", Fax = "",ShipToMasterId = "",Distance = "",EmailId = "",RouteMasterId= "",CityName= "",GSTState= "",
                GSTCode = "",TAN_GSTIN_Number = "",TANNo = "", TANNoName = "",PAN = "",LOCAAdhar = "",LOCPFNo = "", LOCESIC = "",
                GeoLocation = "",isBlocked= "",Rating = "",Latitude = "",Longitude = "";

        ConsigneeName = _ConsigneeName;
        ContactPerson = _ConsigneeName;
        Address = AnyMartData.ADDRESS;
        City = _City;
        Country = "1";
        State = _State;
        CountryName = _Country;
        StateName = _State;
        Mobile = _Mobile;
        ShipToMasterId = _ShipToMasterId;
        EmailId = _EmailId;
        CityName = _City;
        Latitude = AnyMartData.LATITUDE;
        Longitude = AnyMartData.LONGITUDE;
        TANNo = _TANNo;
        TAN_GSTIN_Number = _TAN_GSTIN_Number;

        JSONObject jObj_shipto = new JSONObject();

        try {
            jObj_shipto.put("ConsigneeName",ConsigneeName);
            jObj_shipto.put("ContactPerson",ContactPerson);
            jObj_shipto.put("Address",finalAddress);
            jObj_shipto.put("City",City);
            jObj_shipto.put("Country",Country);
            jObj_shipto.put("State",State);
            jObj_shipto.put("CountryName",CountryName);
            jObj_shipto.put("StateName",StateName);
            jObj_shipto.put("Phone",Phone);
            jObj_shipto.put("Mobile",Mobile);
            jObj_shipto.put("Fax",Fax);
            jObj_shipto.put("ShipToMasterId",ShipToMasterId);
            jObj_shipto.put("Distance",Distance);
            jObj_shipto.put("EmailId",EmailId);
            jObj_shipto.put("RouteMasterId",RouteMasterId);
            jObj_shipto.put("CityName",CityName);
            jObj_shipto.put("StateName",StateName);
            jObj_shipto.put("CountryName",CountryName);
            jObj_shipto.put("GSTState",GSTState);
            jObj_shipto.put("GSTCode",GSTCode);
            jObj_shipto.put("TAN_GSTIN_Number",TAN_GSTIN_Number);
            jObj_shipto.put("TANNo",TANNo);
            jObj_shipto.put("TANNoName",TANNoName);
            jObj_shipto.put("PAN",PAN);
            jObj_shipto.put("LOCAAdhar",LOCAAdhar);
            jObj_shipto.put("LOCPFNo",LOCPFNo);
            jObj_shipto.put("LOCESIC",LOCESIC);
            jObj_shipto.put("GeoLocation",GeoLocation);
            jObj_shipto.put("isBlocked",isBlocked);
            jObj_shipto.put("Rating",Rating);
            jObj_shipto.put("Latitude",AnyMartData.LATITUDE);
            jObj_shipto.put("Longitude",AnyMartData.LONGITUDE);

            shiptoDetails_JSON = jObj_shipto;

        } catch (JSONException e) {
            e.printStackTrace();
            shiptoDetails_JSON = null;
        }

        return shiptoDetails_JSON;
    }

    public JSONObject getDataToSaveEntityPassAPI(){

        JSONObject entity_JSON = null;

        ClientDetails = String.valueOf(new JSONArray());

        JSONArray jsonArray_shipto = new JSONArray();
        try {
            jsonArray_shipto.put(getShipToDetailsData());
            ShipToDetails = jsonArray_shipto.toString().replaceAll("\\\\", "");
            //ShipToDetails = ShipToDetails.replaceAll("^\"|\"+$","");
            //ShipToDetails = ShipToDetails.substring(1, ContactDetails.length()-1);
        }catch (Exception e){
            e.printStackTrace();
        }

        LenderDetails = String.valueOf(new JSONArray());
        InsuranceDetails = String.valueOf(new JSONArray());
        BankPayeeName = String.valueOf(new JSONArray());
        ExpertiseDetails = String.valueOf(new JSONArray());

        JSONObject jObj_entity = new JSONObject();

        try {
            jObj_entity.put("InsCountry1",InsCountry1);
            jObj_entity.put("InsSeg1",InsSeg1);
            jObj_entity.put("LendCountry1",LendCountry1);
            jObj_entity.put("LendSeg1",LendSeg1);
            jObj_entity.put("CallId",CallId);
            jObj_entity.put("CustVendorCode",CustVendorCode);
            jObj_entity.put("RegistrationFormNo",RegistrationFormNo);
            jObj_entity.put("CustVendorName",CustVendorName);
            jObj_entity.put("CustVendorMasterId",CustVendorMasterId);
            jObj_entity.put("ContactTitle",ContactTitle);
            jObj_entity.put("ContactName",ContactName);
            jObj_entity.put("Address",Address);
            jObj_entity.put("ShortName",ShortName);
            jObj_entity.put("City",City);
            jObj_entity.put("Phone",Phone);
            jObj_entity.put("Mobile",Mobile);
            jObj_entity.put("Email",Email);
            jObj_entity.put("Pin",Pin);
            jObj_entity.put("State",State);
            jObj_entity.put("Country",Country);
            jObj_entity.put("District",District);
            jObj_entity.put("Taluka",Taluka);
            jObj_entity.put("Website",Website);
            jObj_entity.put("PriceListId",PriceListId);
            jObj_entity.put("vendorCode",vendorCode);

            jObj_entity.put("ENRect","Y");
            jObj_entity.put("ENInv","Y");
            jObj_entity.put("ENPndPO",ENPndPO);
            jObj_entity.put("EnterpriseType",EnterpriseType);
            jObj_entity.put("CustVendor","C");       //C

            jObj_entity.put("IsWLForCRMRef","Y");     //Y
            jObj_entity.put("Active","true");
            jObj_entity.put("IsActive",IsActive);
            jObj_entity.put("CountryId",CountryId);
            jObj_entity.put("ENGRN",ENGRN);
            jObj_entity.put("ENPymt",ENPymt);
            jObj_entity.put("CreditLimit",CreditLimit);
            jObj_entity.put("CreditDays",CreditDays);
            jObj_entity.put("Currency",Currency);       //pass currency id
            jObj_entity.put("PaymentTerms",PaymentTerms);
            jObj_entity.put("DeliveryTerms",DeliveryTerms);

            jObj_entity.put("TaxClass",TaxClass);
            jObj_entity.put("SlCatId",SlCatId);
            jObj_entity.put("ResellerName",ResellerName);
            jObj_entity.put("CreditTerms",CreditTerms);
            jObj_entity.put("PayeeName",PayeeName);
            jObj_entity.put("BankName",BankName);
            jObj_entity.put("Branch",Branch);
            jObj_entity.put("BankAddress",BankAddress);
            jObj_entity.put("AccountNo",AccountNo);
            jObj_entity.put("AccountType",AccountType);
            jObj_entity.put("IFSCode",IFSCode);
            jObj_entity.put("RemittanceInstruction",RemittanceInstruction);

            jObj_entity.put("EvaluationDt",EvaluationDt);
            jObj_entity.put("ValidFrom",ValidFrom);
            jObj_entity.put("ValidTo",ValidTo);

            jObj_entity.put("SystemUserId",SystemUserId);
            jObj_entity.put("IsApproved",IsApproved);
            jObj_entity.put("IsContractReqd",IsContractReqd);
            jObj_entity.put("Currency",Currency);
            jObj_entity.put("PaymentTerms",PaymentTerms);
            jObj_entity.put("DeliveryTerms",DeliveryTerms);
            jObj_entity.put("CurrencyMasterID",CurrencyMasterID);   //pass currencymasterId

            jObj_entity.put("EntityGroupMasterId",EntityGroupMasterId); //Customer = 1, Supplier = 2, subcontractor = 3, only finance = 4
            jObj_entity.put("EntityClass",EntityClass);         //Customer = 1, Supplier = 2, subcontractor = 3, only finance = 4
            jObj_entity.put("CustVendorType",CustVendorType);       //pass EntitytypeMasterId

            jObj_entity.put("Typeofservices",Typeofservices);
            jObj_entity.put("ServClId",ServClId);
            jObj_entity.put("PANNO",PANNO);
            jObj_entity.put("GSTNO",GSTNO);
            jObj_entity.put("TAN_GSTIN_Number",TAN_GSTIN_Number);
            jObj_entity.put("TANNO",TANNO);
            jObj_entity.put("CSTNo",CSTNo);
            jObj_entity.put("MSTNo",MSTNo);
            jObj_entity.put("ECCNo",ECCNo);
            jObj_entity.put("ServiceTaxNo",ServiceTaxNo);
            jObj_entity.put("ExDivi",ExDivi);
            jObj_entity.put("ExRange",ExRange);
            jObj_entity.put("CAT",CAT);
            jObj_entity.put("AccountId",AccountId);

            jObj_entity.put("EsicNo",EsicNo);
            jObj_entity.put("AadharNo",AadharNo);
            jObj_entity.put("PFNo",PFNo);
            jObj_entity.put("CIN",CIN);
            jObj_entity.put("ValidFrom",ValidFrom);
            jObj_entity.put("ValidTo",ValidTo);
            jObj_entity.put("EvaluationDt",EvaluationDt);
            jObj_entity.put("EntityRestDate",EntityRestDate);
            jObj_entity.put("ClientDetails",ClientDetails);
            jObj_entity.put("ShipToDetails",ShipToDetails);
            jObj_entity.put("SalesFamily",SalesFamily);
            jObj_entity.put("LenderDetails",LenderDetails);
            jObj_entity.put("InsuranceDetails",InsuranceDetails);
            jObj_entity.put("BankPayeeName",BankPayeeName);
            jObj_entity.put("ExpertiseDetails",ExpertiseDetails);
            jObj_entity.put("Latitude",AnyMartData.LATITUDE);
            jObj_entity.put("Longitude",AnyMartData.LONGITUDE);
            jObj_entity.put("VendorMasterID",VendorMasterID);

            Log.e("latlng","lat :"+AnyMartData.LATITUDE + ", lng :"+AnyMartData.LONGITUDE);

            // for blends
            jObj_entity.put("TenorYear",TenorYear);
            jObj_entity.put("IndIndemnity","N");

            entity_JSON = jObj_entity;

        } catch (JSONException e) {
            e.printStackTrace();
            entity_JSON = null;
        }
        return entity_JSON;
    }

    class PostUpdateEntityAPI extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mprpgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_updateNewEntityPost;
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
            //mprogress.setVisibility(View.GONE);

            try{
                if(!response.equalsIgnoreCase(null)){
                    Toast.makeText(parent,"Address saved successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }else if(response.equalsIgnoreCase("Error")){
                    Toast.makeText(parent,"Sorry, server error! failed to save address.",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public String getCurrentDate(){

        String evalDate = "", validFromdate = "", validToDate = "";

        Calendar cal_valfrom = Calendar.getInstance();
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat(" , hh:mm a");    //Thu Jul 18 17:33:55 GMT+05:30 2019
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

        Date d1= null;
        String validFrom = String.valueOf(cal_valfrom.getTime());
        try {
            d1 = sdf.parse(validFrom);
            validFrom = Format.format(d1);
            System.out.println(validFrom);

        } catch (ParseException e) {
            e.printStackTrace();
            validFrom = "";
        }

        Calendar cal_valto = Calendar.getInstance();
        cal_valto.add(Calendar.MONTH, 1);
        String dt_valto = String.valueOf(cal_valto.getTime());
        try {
            d1 = sdf.parse(dt_valto);
            dt_valto = Format.format(d1);
            System.out.println(dt_valto);

        } catch (ParseException e) {
            e.printStackTrace();
            dt_valto = "";
        }

        Calendar cal_evaldt = Calendar.getInstance();
        cal_evaldt.add(Calendar.YEAR, 1);
        String dt_evaldt = String.valueOf(cal_evaldt.getTime());
        try {
            d1 = sdf.parse(dt_evaldt);
            dt_evaldt = Format.format(d1);
            System.out.println(dt_evaldt);

        } catch (ParseException e) {
            e.printStackTrace();
            dt_evaldt = "";
        }

        String eval_from_to = dt_evaldt + "," + validFrom + "," + dt_valto;

        return eval_from_to;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(parent,ShipToAddressActivity.class);
        intent.putExtra("CustVendorMasterId", CustomerId);
        intent.putExtra("username",username);
        intent.putExtra("intentFrom","LocateOnMap");
        startActivity(intent);
        finish();
    }

}