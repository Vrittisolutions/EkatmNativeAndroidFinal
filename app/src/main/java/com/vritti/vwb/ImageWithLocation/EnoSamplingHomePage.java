package com.vritti.vwb.ImageWithLocation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.LoggingTimeActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EnoSamplingHomePage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;


    @BindView(R.id.sampleSpinner)
    Spinner sampleSpinner;

    @BindView(R.id.locationNameET)
    EditText locationNameET;
    @BindView(R.id.toolbar_progress_logging)
    ProgressBar progressBar;

    SampleAdapter sampleAdapter;
    String selectedType = "", locationName = "";

    ArrayList<SamplePojoClass> samplePojoClassArrayList;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    String ActivityId;
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;
    ArrayList<LocationTypePojoClass> locationTypePojoClassArrayList;
    // ProgressBar toolbar_progress_logging;
    ArrayAdapter<LocationTypePojoClass> aa;
    SampleActivityObject sampleActivityObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sampling_dummy_ui);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        dbInti();
        sampleSpinner.setOnItemSelectedListener(this);

        if(getIntent() != null)
        {
            ActivityId = getIntent().getStringExtra("activityId");
        }
        //Creating the ArrayAdapter instance having the country list

        aa = new ArrayAdapter<LocationTypePojoClass>(this, android.R.layout.simple_spinner_item, locationTypePojoClassArrayList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sampleSpinner.setAdapter(aa);


        initAdapter();
        callLocationTypeApi();
    }

    private void callLocationTypeApi() {
        if (isnet()) {
            progressBar.setVisibility(View.VISIBLE);
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetLocationTypeClass().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(EnoSamplingHomePage.this, msg);
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            ut.displayToast(EnoSamplingHomePage.this, "No Internet connection");
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
        }
        return false;
    }

    private void dbInti() {
        context = EnoSamplingHomePage.this;
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        locationTypePojoClassArrayList = new ArrayList<>();
        sampleActivityObject = new SampleActivityObject();
    }

    private void initAdapter() {
        try {
            samplePojoClassArrayList = new ArrayList<>();
            String listStr = AppCommon.getInstance(this).getEnoSampelList();
            if (listStr != null) {
                Sample_List_Object sample_list_object = new Gson().fromJson(AppCommon.getInstance(this).getEnoSampelList(), Sample_List_Object.class);
                samplePojoClassArrayList = sample_list_object.getSamplePojoClassArrayList();
                ArrayList<SamplePojoClass> tempSampleClassArray = new ArrayList<>();
                if (samplePojoClassArrayList.size() != 0) {
                    for (SamplePojoClass tempSampleClassObj : samplePojoClassArrayList) {
                        if (tempSampleClassObj.getActivityId() != null && ActivityId != null && tempSampleClassObj.getActivityId().equals(ActivityId)) {
                            tempSampleClassArray.add(tempSampleClassObj);
                        }
                    }
                }
                samplePojoClassArrayList.clear();
                samplePojoClassArrayList = tempSampleClassArray;
                // tempSampleClassArray.clear();

            }
            sampleAdapter = new SampleAdapter(this, samplePojoClassArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recycleView.setLayoutManager(linearLayoutManager);
            recycleView.setAdapter(sampleAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @OnClick(R.id.addImageBtn)
    void addImgButton() {
        locationName = locationNameET.getText().toString().trim();
        if (selectedType.isEmpty())
            Toast.makeText(this, "Please Select the Location Type", Toast.LENGTH_SHORT).show();
        else if (locationName.isEmpty())
            Toast.makeText(this, "Please Enter the Location Name", Toast.LENGTH_SHORT).show();
        else {
            SamplePojoClass samplePojoClass = new SamplePojoClass();
            samplePojoClass.setLocationName(locationName);
            samplePojoClass.setLocationType(selectedType);
            String objStr = new Gson().toJson(samplePojoClass);

            Intent intent = new Intent(this, ImageWithLoactionActivity.class)
                    .putExtra("objectClass", objStr)
                    .putExtra("type", true)
                    .putExtra("locationname", locationName)
                    .putExtra("type", selectedType)
                    .putExtra("activityId", ActivityId);

            startActivityForResult(intent, 665);

            /*Intent intent = new Intent(this, ImageWithLoactionActivity.class)
                    .putExtra("objectClass", objStr)
                    .putExtra("type", true)
                    .putExtra("activityId", ActivityId);
            startActivity(intent);*/
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedType = locationTypePojoClassArrayList.get(position).getLocationType();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            locationNameET.setText("");
            sampleSpinner.setSelection(0);
            if (requestCode == 665 && resultCode == 998) {
                SamplePojoClass samplePojoClass = new Gson().fromJson(data.getStringExtra("objClass"), SamplePojoClass.class);
                int pos = data.getIntExtra("pos", -1);
                if (pos == -1) {
                    samplePojoClassArrayList.add(samplePojoClass);
                } else {
                    samplePojoClassArrayList.set(pos, samplePojoClass);
                }
                String listObj = new Gson().toJson(new Sample_List_Object(samplePojoClassArrayList));
                AppCommon.getInstance(this).setEnoSampelList(listObj);
                sampleAdapter.notifyDataSetChanged();

            }
        }
    }

    public void updateImage(int adapterPosition) {
        String objStr = new Gson().toJson(samplePojoClassArrayList.get(adapterPosition));
        Intent intent = new Intent(this, ImageWithLoactionActivity.class)
                .putExtra("objectClass", objStr)
                .putExtra("type", false)
                .putExtra("pos", adapterPosition);
        startActivityForResult(intent, 665);
    }

    @OnClick(R.id.nextBtn)
    void nextBtn() {
        if (samplePojoClassArrayList.size() != 0) {
            sampleActivityObject.setActivityId(ActivityId);
            sampleActivityObject.setUsermasterId(UserMasterId);
            sampleActivityObject.setLatitude_start(String.valueOf(LoggingTimeActivity.latitude));
            sampleActivityObject.setLongitude_start(String.valueOf(LoggingTimeActivity.longitude));
            sampleActivityObject.setAdress_start(String.valueOf(LoggingTimeActivity.LocationName));
            sampleActivityObject.setVillageCode(String.valueOf(LoggingTimeActivity.client_GeoLocation));
           // sampleActivityObject.setSaleUnit(String.valueOf(LoggingTimeActivity.));
            //sampleActivityObject.setSampleUnit(String.valueOf(LoggingTimeActivity.LocationName));
            //sampleActivityObject.setEndDate(String.valueOf(LoggingTimeActivity.LocationName));
            Intent intent = new Intent(this, EnoSampleSubmitClass.class);
            intent.putExtra("sampleList", samplePojoClassArrayList);
            intent.putExtra("finalObj", new Gson().toJson(sampleActivityObject));
            intent.putExtra("activityId", ActivityId);

            startActivity(intent);
        } else {
            Toast.makeText(this, "Sorry!! Please add sample first..", Toast.LENGTH_SHORT).show();
        }

    }

    private class GetLocationTypeClass extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = CompanyURL + WebUrlClass.api_getLocationTypeENO;
            try {
                res = ut.OpenConnection(url, EnoSamplingHomePage.this);
                // res = res.toString().replaceAll("\\\\\\\\\\\"", "");
                //res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("u0026", "&");
                // res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (res != null) {
                try {
                    JSONArray jsonArray = new JSONArray(res);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String locId = jsonObject.getString("locationTypeID");
                        String locationType = jsonObject.getString("LocationType");
                        locationTypePojoClassArrayList.add(new LocationTypePojoClass(locId, locationType));
                        aa.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAdapter();
    }
}