package com.vritti.inventory.physicalInventory.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.adapter.LocationListAdapter;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class LocationPIActivity extends AppCompatActivity {
    EditText edtlocation;
    ListView listlocationname;
    Button btnaddlocation;
    ImageView refresh;
    ProgressDialog progressDialog_1;

    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<PartCodeName> ItemCodelist;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount;

    String locationCode = "", locationMasterID = "";
    ArrayList<LocationList>locationListArrayList;
    LocationListAdapter locationListAdapter;

    private SharedPreferences sharedPrefs;
    Gson gson;
    private String json;
    Type type;

    public static final int REQ_LOCATION = 9;
    String finalObj = "",WareHouseMasterId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationpi);

        init();

        if (locationListArrayList == null) {
            callLocationListAPI();
        }else {
            if (locationListArrayList.size() > 0) {

                locationListAdapter = new LocationListAdapter(LocationPIActivity.this, locationListArrayList);
                listlocationname.setAdapter(locationListAdapter);

                //if locationlist size is less than location table size then call API
               /* if(locationListArrayList.size() < checkLocationCount()){
                    callLocationListAPI();
                }else {
                    locationListAdapter = new LocationListAdapter(LocationPIActivity.this, locationListArrayList);
                    // spinner_location.setAdapter(locationListAdapter);
                    listlocationname.setAdapter(locationListAdapter);
                }*/
            }
        }

        setListeners();

    }

    private int checkLocationCount() {
        String qry = "Select LocationCode from "+db.TABLE_PI_GENERATION;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            return c.getCount();
        }else {

        }
        return c.getCount();
    }

    public void init(){
        edtlocation = findViewById(R.id.edtlocation);
        listlocationname = findViewById(R.id.listlocationname);
        btnaddlocation= findViewById(R.id.btnaddlocation);
        progressDialog_1=new ProgressDialog(LocationPIActivity.this);
        refresh = findViewById(R.id.refresh);

        ut = new Utility();
        cf = new CommonFunction(LocationPIActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(LocationPIActivity.this);
        String dabasename = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(LocationPIActivity.this, dabasename);
        CompanyURL = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(LocationPIActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(LocationPIActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();


        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LocationPIActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("location", "");
        type = new TypeToken<List<LocationList>>() {}.getType();
        locationListArrayList = gson.fromJson(json, type);
        WareHouseMasterId = sharedPrefs.getString("WareHouseMasterId", "");
    }

    public void setListeners(){

        edtlocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try{
                    locationListAdapter.filter((edtlocation)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        listlocationname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                locationCode = locationListArrayList.get(position).getLocationCode();
                locationMasterID = locationListArrayList.get(position).getLocationMasterId();

                Intent intent = new Intent();
                intent.putExtra("LocationCode",locationCode);
                intent.putExtra("LocationMasterID", locationMasterID);
                setResult(REQ_LOCATION, intent);
                finish();
            }
        });

        btnaddlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call API of add location
                //add new location in the table and server also
                //and pass intent back to PIEntry screen
                if(edtlocation.getText().toString().trim().equalsIgnoreCase("") ||
                        edtlocation.getText().toString().trim().equalsIgnoreCase(null)){
                    Toast.makeText(LocationPIActivity.this,"Please enter location", Toast.LENGTH_SHORT).show();
                }else {
                    createLocationJSON();
                }

            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //locationListArrayList.clear();
                callLocationListAPI();
            }
        });

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

    private void callLocationListAPI() {
        try{
        if (isnet()) {

            try {
                if (progressDialog_1 == null) {
                    // progressDialog_1.setMessage("Sending data please wait...");
                    progressDialog_1.setMessage("Loading locations please wait...");
                    progressDialog_1.setIndeterminate(false);
                    progressDialog_1.setCancelable(false);
                    progressDialog_1.setCanceledOnTouchOutside(false);
                }
                progressDialog_1.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Loading locations please wait...",Toast.LENGTH_LONG).show();
                }
            });

            new StartSession(LocationPIActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadGetLocationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class DownloadGetLocationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Loading locations please wait...",Toast.LENGTH_LONG).show();
                    }
                });
               /* if (progressDialog_1 == null) {
                    progressDialog_1 = new ProgressDialog(LocationPIActivity.this);
                    progressDialog_1.setMessage("Loading locations please wait...");
                    progressDialog_1.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog_1.setCancelable(false);

                }
                progressDialog_1.show();*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetLocation+"?WareHouseMasterId=1";
            //String url = CompanyURL + WebUrlClass.api_GetLocation+"?WareHouseMasterId=dc7019b8-ce80-4446-98f9-8a81b15888e8";

            //String url = CompanyURL + WebUrlClass.api_GetLocation+"?WareHouseMasterId="+WareHouseMasterId;

            try {
                res = ut.OpenConnection(url, LocationPIActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    locationListArrayList=new ArrayList<>();
                    locationListArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        LocationList locationList = new LocationList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        locationList.setLocationMasterId(jorder.getString("LocationMasterId"));
                        locationList.setLocationCode(jorder.getString("LocationCode"));
                        locationListArrayList.add(locationList);

                        cf.insertLocationPI(jorder.getString("LocationMasterId"),
                                locationCode, jorder.getString("LocationDesc"), WareHouseMasterId,UserMasterId);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                //progressDialog_1.dismiss();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            progressDialog_1.dismiss();

            if (response.contains("[]")) {
                Toast.makeText(LocationPIActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {

                try{
                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(LocationPIActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    Gson gson = new Gson();

                    String json = gson.toJson(locationListArrayList);
                    editor.putString("location", json);
                    editor.commit();

                    locationListAdapter = new LocationListAdapter(LocationPIActivity.this, locationListArrayList);
                    //spinner_location.setAdapter(locationListAdapter);
                    listlocationname.setAdapter(locationListAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void createLocationJSON(){
        String location = edtlocation.getText().toString().trim();

        UUID locGuid = UUID.randomUUID();
        String uuidInString = locGuid.toString();

        JSONObject jobjLoc = new JSONObject();
        try {
            jobjLoc.put("LocationMasterId", uuidInString);
            jobjLoc.put("LocationCode", location);
            jobjLoc.put("LocationDesc", location);
            //jobjLoc.put("WarehouseMasterId", WareHouseMasterId);
            jobjLoc.put("WarehouseMasterId", "1");
            jobjLoc.put("AddedBy", UserMasterId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        locationCode = location;    //same as location code, desc
        locationMasterID = uuidInString;

        finalObj = jobjLoc.toString();

        cf.insertLocationPI(uuidInString, locationCode, locationCode, WareHouseMasterId,UserMasterId);

        edtlocation.setText("");
        callAddLocationAPI();

    }

    private void callAddLocationAPI() {
        if (isnet()) {

            new StartSession(LocationPIActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new PostLocationData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                   // progressDialog_1.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    class PostLocationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(LocationPIActivity.this);
                    progressDialog.setMessage("Submitting data please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_AddLocationPI;

            try {
                res = ut.OpenPostConnection(url,finalObj, LocationPIActivity.this);
                if (res!=null) {
                    response = res.toString();
                    //response = response.substring(1, response.length() - 1);

                   /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                   */
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            respMessage(response);
        }
    }

    private void respMessage(String response) {

       //callLocationListAPI();

        if(response.equalsIgnoreCase("true")){
            Toast.makeText(LocationPIActivity.this,"Location added successfully", Toast.LENGTH_SHORT).show();

            callLocationListAPI();

            /*Intent intent = new Intent();
            intent.putExtra("LocationCode",locationCode);
            intent.putExtra("LocationMasterID", locationMasterID);
            setResult(REQ_LOCATION, intent);
            finish();*/

        }else {
            Toast.makeText(LocationPIActivity.this,"Unable to add location", Toast.LENGTH_SHORT).show();
        }
    }

}