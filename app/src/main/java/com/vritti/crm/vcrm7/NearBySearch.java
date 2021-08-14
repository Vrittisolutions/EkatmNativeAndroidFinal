package com.vritti.crm.vcrm7;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vritti.chat.activity.SharefunctionActivity;
import com.vritti.crm.adapter.NearBySearchAdapter;
import com.vritti.crm.bean.NearBySearchData;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.AppConstants;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.classes.GpsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NearBySearch extends AppCompatActivity {
    public static final String MyPREFERENCES = "LoginPrefs";
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 101;
    SharedPreferences sharedpreferences;
    RecyclerView recyclerView;
    ArrayList<NearBySearchData> nearBySearchDataArrayList;
    private NearBySearchAdapter nearBySearchAdapter;
    Button create;
    private String[] user;
    private String lat = "", lng = "", firmname = "", add = "", CompanyURL = "";

    Utility ut;
    DatabaseHandlers db;
    SQLiteDatabase sdb;
    CommonFunction cf;
    Context context;
    private SQLiteDatabase sql;
    ImageView img_location;
    double mLatitude=0,mLongitude=0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    ;
    private StringBuilder stringBuilder;

    private boolean isContinue = false;
    private boolean isGPS = false;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    Spinner spinner_business,spinner_radius;
    String  Business_type="",Radius="";
    ProgressBar toolbar_progress_Activity_main;
    TextView txt_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_search_lay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Business Type");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);

        sql = db.getWritableDatabase();


        nearBySearchDataArrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.assitant_response);
        spinner_business = findViewById(R.id.spinner_business);
        spinner_radius = findViewById(R.id.spinner_radius);
        txt_submit = findViewById(R.id.txt_submit);
        img_location = findViewById(R.id.img_location);
        create = findViewById(R.id.create);
        toolbar_progress_Activity_main = findViewById(R.id.toolbar_progress_Activity_main);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NearBySearch.this);
        recyclerView.setLayoutManager(mLayoutManager);



             getLocation();








        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NearBySearch.this, LocationSelectActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearBySearchDataArrayList = nearBySearchAdapter.getArrayList();
                    if (nearBySearchDataArrayList.size() > 0) {
                        if (nearBySearchDataArrayList.size() > 0) {
                            user = new String[nearBySearchDataArrayList.size()];
                            for (int i = 0; i < nearBySearchDataArrayList.size(); i++) {
                                if (nearBySearchDataArrayList.get(i).isSelected()) {
                                    lat = nearBySearchDataArrayList.get(i).getLat();
                                    lng = nearBySearchDataArrayList.get(i).getLat();
                                    firmname = nearBySearchDataArrayList.get(i).getPlace_name();
                                    add = nearBySearchDataArrayList.get(i).getVicinity();

                                }
                                CreateIndiProspectNew(add, String.valueOf(lat), String.valueOf(lng), "", firmname);

                            }
                        }
                    }else {
                        Toast.makeText(NearBySearch.this,"Please select locaton name",Toast.LENGTH_SHORT).show();
                }
            }
        });


        spinner_business.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Business_type=parent.getSelectedItem().toString();

                /*StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                sb.append("&radius=3000");
                //   sb.append("&types=" + "grocery_or_supermarket");
                sb.append("&types=" + Business_type);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyDecNu2xdHvGTMgAhaJinP90TCapteUGGM");

                Log.d("Map", "api: " + sb.toString());
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sb.toString());*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_radius.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String radius = parent.getSelectedItem().toString();
                if (radius.equals("3")) {
                    Radius = "3000";
                } else if (radius.equals("5")) {
                    Radius = "5000";
                } else if (radius.equals("10")) {
                    Radius = "10000";
                } else if (radius.equals("20")) {
                    Radius = "20000";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                sb.append("&radius="+Radius);
                //   sb.append("&types=" + "grocery_or_supermarket");
                sb.append("&types=" + Business_type);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyDecNu2xdHvGTMgAhaJinP90TCapteUGGM");

                Log.d("Map", "api: " + sb.toString());
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sb.toString());

            }
        });
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {

        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  toolbar_progress_Activity_main.setVisibility(View.VISIBLE);
        }

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // toolbar_progress_Activity_main.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {


                jObject = new JSONObject(jsonData[0]);
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
 //           toolbar_progress_Activity_main.setVisibility(View.GONE);
            nearBySearchDataArrayList.clear();
            try{
            for (int i = 0; i < list.size(); i++) {

                // Creating a marker

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                String lat = hmPlace.get("lat");
                // Getting longitude of the place
                String lng = hmPlace.get("lng");
                String name = hmPlace.get("place_name");
                String vicinity = hmPlace.get("vicinity");
                String icon = hmPlace.get("icon");
                String rating = hmPlace.get("rating");

                NearBySearchData nearBySearchData = new NearBySearchData();
                nearBySearchData.setIcon(icon);
                nearBySearchData.setLat(lat);
                nearBySearchData.setLng(lng);
                nearBySearchData.setPlace_name(name);
                nearBySearchData.setVicinity(vicinity);
                nearBySearchData.setType_of_shop(Business_type);
                nearBySearchData.setRating(rating);
                nearBySearchDataArrayList.add(nearBySearchData);
                nearBySearchAdapter = new NearBySearchAdapter(NearBySearch.this, nearBySearchDataArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                        NearBySearch.this,
                        LinearLayoutManager.VERTICAL,
                        false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(nearBySearchAdapter);
            }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class PlaceJSONParser {

        /**
         * Receives a JSONObject and returns a list
         */
        public List<HashMap<String, String>> parse(JSONObject jObject) {

            JSONArray jPlaces = null;
            try {
                /** Retrieves all the elements in the 'places' array */
                jPlaces = jObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /** Invoking getPlaces with the array of json object
             * where each json object represent a place
             */
            return getPlaces(jPlaces);
        }

        private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
            int placesCount = jPlaces.length();
            List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> place = null;

            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */
                    place = getPlace((JSONObject) jPlaces.get(i));
                    placesList.add(place);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return placesList;
        }

        /**
         * Parsing the Place JSON object
         */
        private HashMap<String, String> getPlace(JSONObject jPlace) {

            HashMap<String, String> place = new HashMap<String, String>();
            String placeName = "-NA-";
            String vicinity = "-NA-";
            String latitude = "";
            String longitude = "";
            String reference = "";
            String icon = "";
            String rating = "";
            nearBySearchDataArrayList.clear();

            try {
                // Extracting Place name, if available
                if (!jPlace.isNull("name")) {
                    placeName = jPlace.getString("name");
                }

                // Extracting Place Vicinity, if available
                if (!jPlace.isNull("vicinity")) {
                    vicinity = jPlace.getString("vicinity");
                }

                if (!jPlace.isNull("icon")) {
                    icon = jPlace.getString("icon");
                }
                if (!jPlace.isNull("rating")) {
                    rating = jPlace.getString("rating");
                }

                latitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat");
                longitude = jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng");
                reference = jPlace.getString("reference");

                place.put("place_name", placeName);
                place.put("vicinity", vicinity);
                place.put("lat", latitude);
                place.put("lng", longitude);
                place.put("reference", reference);
                place.put("rating", rating);
                place.put("icon", icon);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return place;
        }
    }

    private void CreateIndiProspectNew(String add, String lat, String lng, String pin, String firmname) {

        JSONObject jsoncontact = new JSONObject();
        String contact = "", product = "";
        try {
            jsoncontact.put("ContactName", "");
            jsoncontact.put("Designation", "");
            jsoncontact.put("EmailId", "");
            jsoncontact.put("Mobile", "");
            jsoncontact.put("Telephone", "");
            jsoncontact.put("DateofBirth", "");
            jsoncontact.put("ContactPersonDept", "");
            jsoncontact.put("Fax", "");
            jsoncontact.put("AnniversaryDate", "");
            jsoncontact.put("Gender", "");
            jsoncontact.put("MaritalStatus", "");
            jsoncontact.put("SpouseName", "");
            jsoncontact.put("WhatsAppNo", "");

            contact = jsoncontact.toString();

            System.out.println("Contact list : " + jsoncontact.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }


        JSONObject jsonProduct = new JSONObject();
        try {
            jsonProduct.put("FKProductId", "");
            product = jsonProduct.toString();
        } catch (Exception e) {
        }

        //susmaster = new String[5];
        JSONObject jsonBusinessprospect = new JSONObject();

        try {

            jsonBusinessprospect.put("PKSuspectId", null);
            jsonBusinessprospect.put("FirmName", firmname);
            jsonBusinessprospect.put("Address", add);
            jsonBusinessprospect.put("FirmAlias", "");
            jsonBusinessprospect.put("FKCityId", "");
            jsonBusinessprospect.put("FKTerritoryId", "");
            jsonBusinessprospect.put("FKBusiSegmentId", "");
            jsonBusinessprospect.put("CompanyURL", "");
            jsonBusinessprospect.put("FKEnqSourceId", "");
            jsonBusinessprospect.put("Fax", "");
            jsonBusinessprospect.put("Notes", "");
            jsonBusinessprospect.put("Remark", "");
            jsonBusinessprospect.put("Department", "");
            jsonBusinessprospect.put("BusinessDetails", "");
            jsonBusinessprospect.put("CurrencyMasterId", "");
            jsonBusinessprospect.put("CurrencyDesc", "");
            jsonBusinessprospect.put("Turnover", "");
            jsonBusinessprospect.put("NoOfEmployees", "");
            jsonBusinessprospect.put("NoOfOffices", "");
            jsonBusinessprospect.put("LeadGivenBYId", "");
            jsonBusinessprospect.put("FKConsigneeId", "");
            jsonBusinessprospect.put("FKCustomerId", "");
            jsonBusinessprospect.put("EntityType", "");
            jsonBusinessprospect.put("PBT", "");
            jsonBusinessprospect.put("Rating", "");
            jsonBusinessprospect.put("Network", "");
            jsonBusinessprospect.put("Borrowings", "");
            jsonBusinessprospect.put("FKStateId", "");
            jsonBusinessprospect.put("GSTState", "");
            jsonBusinessprospect.put("GSTCode", "");
            jsonBusinessprospect.put("TANNo", "");
            jsonBusinessprospect.put("TANNoName", "");
            jsonBusinessprospect.put("FKCountryId", "");
            jsonBusinessprospect.put("ProspectType", "2");
            jsonBusinessprospect.put("Qualification: ", "");
            jsonBusinessprospect.put("Experience: ", "");


            jsonBusinessprospect.put("val1", lat);
            jsonBusinessprospect.put("val2", lng);
            jsonBusinessprospect.put("val3", pin);
            jsonBusinessprospect.put("val4", "");
            jsonBusinessprospect.put("val5", "");
            jsonBusinessprospect.put("val6", "");
            jsonBusinessprospect.put("val7", "");
            jsonBusinessprospect.put("val8", "");
            jsonBusinessprospect.put("val9", "");
            jsonBusinessprospect.put("val10", "");
            jsonBusinessprospect.put("sex", "");
            jsonBusinessprospect.put("District", "");
            jsonBusinessprospect.put("Village", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonData = new JSONObject();

        try {

            JSONArray ob = new JSONArray();
            JSONObject j = new JSONObject(jsonBusinessprospect.toString());
            System.out.println("ArrayBusiness : " + jsonBusinessprospect.toString());
            ob.put(j);

            jsonData.put("SuspMaster", ob);

            JSONArray obj1 = new JSONArray();
            JSONObject a = null;

            a = new JSONObject(contact);
            obj1.put(a);

            jsonData.put("SuspContactDetails", obj1);

            JSONArray obj = new JSONArray();
            JSONObject a2 = null;

            a2 = new JSONObject(product);
            obj.put(a2);

            jsonData.put("SuspProdDetails", obj);
            jsonData.put("EnquiryRegistryId", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // FinalArray[0]
        String finaljson = jsonData.toString();
        finaljson = finaljson.replaceAll("\\\\", "");


        final String finalJson = finaljson;

        String remark = "Prospect created";
        String url = CompanyURL + WebUrlClass.api_Post_Prospect;

        String op = "";


        CreateOfflineAssignActivity(url, finalJson, WebUrlClass.POSTFLAG, remark, op);

            /*new StartSession(SharefunctionActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostProspectUpdate_savenstartJSON().execute(finalJson);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });*/
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
    private void CreateOfflineAssignActivity(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();

        }

    }
    private void initializeLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000); // 10 seconds
        locationRequest.setFastestInterval(5 * 1000); // 5 seconds

        new GpsUtils(this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPS = isGPSEnable;   // turn on GPS
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        mLatitude = location.getLatitude();
                        mLongitude = location.getLongitude();
                        if (!isContinue) {
                        } else {
                            stringBuilder.append(mLatitude);
                            stringBuilder.append("-");
                            stringBuilder.append(mLongitude);
                            stringBuilder.append("\n\n");
                            //  txtContinueLocation.setText(stringBuilder.toString());
                        }
                        if (!isContinue && mFusedLocationClient != null) {
                            mFusedLocationClient.removeLocationUpdates(locationCallback);
                        }
                    }
                }
            }
        };

        if (!isGPS) {
            Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        isContinue = false;
        getLocation();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                NearBySearch.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                NearBySearch.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                mLatitude = lat;
                mLongitude = longi;
                StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                sb.append("location=" + mLatitude + "," + mLongitude);
                sb.append("&radius=3000");
                //   sb.append("&types=" + "grocery_or_supermarket");
                sb.append("&types=" + Business_type);
                sb.append("&sensor=true");
                sb.append("&key=AIzaSyDecNu2xdHvGTMgAhaJinP90TCapteUGGM");

                Log.d("Map", "api: " + sb.toString());
                PlacesTask placesTask = new PlacesTask();
                placesTask.execute(sb.toString());
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                    // get String data from Intent
                   mLatitude = data.getDoubleExtra("lat",0);
                   mLongitude = data.getDoubleExtra("lng",0);
                    StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                    sb.append("location=" + mLatitude + "," + mLongitude);
                    sb.append("&radius="+Radius);
                    //   sb.append("&types=" + "grocery_or_supermarket");
                    sb.append("&types=" + Business_type);
                    sb.append("&sensor=true");
                    sb.append("&key=AIzaSyDecNu2xdHvGTMgAhaJinP90TCapteUGGM");

                    Log.d("Map", "api: " + sb.toString());
                    PlacesTask placesTask = new PlacesTask();
                    placesTask.execute(sb.toString());
                }
            }
        }


    }



