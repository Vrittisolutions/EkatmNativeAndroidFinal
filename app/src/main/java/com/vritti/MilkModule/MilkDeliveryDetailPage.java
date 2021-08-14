package com.vritti.MilkModule;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.EnoJobService;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MilkDeliveryDetailPage extends Activity {

    @BindView(R.id.pickupBtn)
    Button pickupBtn;

    @BindView(R.id.resheduleBtn)
    Button resheduleBtn;

    @BindView(R.id.otpLayout)
    LinearLayout otpLayout;

    /*@BindView(R.id.buttonLayout)
    LinearLayout buttonLayout;*/

    @BindView(R.id.phoneNumber)
    TextView phoneNumber;

    @BindView(R.id.toolbar1)
    Toolbar toolBar;

    @BindView(R.id.etOtp)
    EditText etOtp;
    @BindView(R.id.customerName)
    TextView customerName;
    @BindView(R.id.InvoiveNumber)
    TextView InvoiveNumber;

    @BindView(R.id.addressName)
    TextView addressName;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.date)
    TextView date;
   /* @BindView(R.id.spinnerStatus)
    Spinner spinnerStatus;*/

    @BindView(R.id.spinnerLayout)
    RelativeLayout spinnerLayout;
    @BindView(R.id.changeStatusBtn)
    Button changeStatusBtn;
    @BindView(R.id.route)
    ImageView route;

    String phoneNum;
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Bitmap bitmap;
    private static File mediaFile;

    JSONObject ChangeActivityStatus;
    String ActivityId, StatusFlag, ActivityName, FinalObj, CompanyURL;
    CommonFunction cf;
    String PlantMasterId = "", LoginId = "", Password = "", EnvMasterId = "", UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    Context context;
    ProgressDialog progressDialog;
    String Otp = null;
    double lat = 0.0d, lng = 0.0d;
    double wLat = 0.0d, wLng = 0.0d;
    List<String> statusList;
    String currentStatus = "";
    ArrayAdapter<String> statusAdapter;
    String isApproval;
    String reDate = "", lastStatus = "";


    @BindView(R.id.editTextone)
    EditText editTextOne;

    @BindView(R.id.editTexttwo)
    EditText editTextTwo;

    @BindView(R.id.editTextthree)
    EditText editTextThree;

    @BindView(R.id.editTextfour)
    EditText editTextFour;
    @BindView(R.id.editTextFive)
    EditText editTextFive;
    @BindView(R.id.editTextSix)
    EditText editTextSix;

    @BindView(R.id.resheduleTxt)
    TextView resheduleTxt;
    @BindView(R.id.wareHouseAddressTxt)
    TextView wareHouseAddressTxt;
    @BindView(R.id.time)
    TextView time;

    @BindView(R.id.otpLayout_new)
    RelativeLayout otpLayout_new;

    @BindView(R.id.ll)
    LinearLayout ll;

    @BindView(R.id.waereHouseLayout)
    RelativeLayout waereHouseLayout;

    @BindView(R.id.resheduleLayout)
    RelativeLayout resheduleLayout;

    @BindView(R.id.pickUpLayout)
    CardView pickUpLayout;
    @BindView(R.id.expectedTime)
    TextView expectedTime;

    @BindView(R.id.expectedTimeLayout)
    RelativeLayout expectedTimeLayout;

    @BindView(R.id.cashAmmount)
    RelativeLayout cashAmmount;

    @BindView(R.id.paymentTypeLayout)
    LinearLayout paymentTypeLayout;

    @BindView(R.id.editTexCash)
    EditText editTexCash;

    @BindView(R.id.rbCash)
    RadioButton rbCash;

    @BindView(R.id.rbOnline)
    RadioButton rbOnline;

    @BindView(R.id.rbPaid)
    RadioButton rbPaid;
    @BindView(R.id.invoiceLayout)
    RelativeLayout invoiceLayout;
    @BindView(R.id.ammountlayout)
    RelativeLayout ammountlayout;
    @BindView(R.id.datelayout)
    RelativeLayout datelayout;
    @BindView(R.id.timelayout)
    RelativeLayout timelayout;
    @BindView(R.id.arraiveBtn_next)
    Button arraiveBtn_next;
    @BindView(R.id.arraiveBtn)
    RelativeLayout arraiveBtn;

    String paymentType = "", amuntPrice = "";
    public static FirebaseJobDispatcher dispatcherNew;
    public static Job myJobNew = null;
    private GoogleApiClient googleApiClient = null;
    MilkDetailObject milkDetailObject;

    // Status Value
    String pendinValue = "10";
    String startValue = "20";
    String arrivedValue = "30";
    String loadingValue = "40";
    String completeValue = "70";
    String cancelValue = "90";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.delivery_detail_page);
        //setContentView(R.layout.delivery_detail_page_new);
        setContentView(R.layout.delivery_detail_page_new_again);
        ButterKnife.bind(this);
        route.setVisibility(View.VISIBLE);
        toolBar.setTitle(R.string.deliveryDetails);
        context = getApplicationContext();
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
        progressDialog = new ProgressDialog(this);
        if (getIntent() != null) {

            ActivityId = getIntent().getStringExtra("Act");
            milkDetailObject = new Gson().fromJson(getIntent().getStringExtra("objectData"), MilkDetailObject.class);
        }
        invoiceLayout.setVisibility(View.GONE);
        waereHouseLayout.setVisibility(View.GONE);
        ammountlayout.setVisibility(View.GONE);
        datelayout.setVisibility(View.GONE);
        timelayout.setVisibility(View.GONE);
        paymentTypeLayout.setVisibility(View.GONE);
        resheduleTxt.setVisibility(View.GONE);

        setButtonText();

        cliackBtn(pickupBtn, resheduleBtn);
        setData();

    }

    private void setButtonText() {
        if (milkDetailObject.getStatus().equals(WebUrlClass.statusPending)) {
            resheduleBtn.setVisibility(View.GONE);
            resheduleTxt.setVisibility(View.GONE);
            arraiveBtn_next.setText("Start Trip");
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusStart)) {

            arraiveBtn_next.setText("Arrived");
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusArrived)) {

            arraiveBtn_next.setText("Loading");
            resheduleTxt.setText("Loading Cancel");
            resheduleTxt.setVisibility(View.VISIBLE);
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusLoading)) {

            arraiveBtn_next.setText("Loading Complete");
            resheduleTxt.setVisibility(View.GONE);
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusComplete) ||milkDetailObject.getStatus().equals(WebUrlClass.statusCancel)) {
            resheduleBtn.setVisibility(View.GONE);
            resheduleTxt.setVisibility(View.GONE);
            arraiveBtn_next.setVisibility(View.GONE);
            arraiveBtn.setVisibility(View.GONE);
            route.setVisibility(View.GONE);
            expectedTimeLayout.setVisibility(View.GONE);
            pickUpLayout.setVisibility(View.GONE);
        }
    }

    private void setData() {
        if (milkDetailObject != null) {
            customerName.setText(milkDetailObject.getConsigneeName());
            addressName.setText(milkDetailObject.getAddress());
            phoneNumber.setText(milkDetailObject.getContactNo());
            try {
                wLat = Double.parseDouble(milkDetailObject.getLatitude());
                wLng = Double.parseDouble(milkDetailObject.getLongitude());
                calculateExpectedTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
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


    @OnClick(R.id.pickupBtn)
    void setPickupBtn() {
        if (currentStatus.equals("Pickup") || currentStatus.equals("")) {
            currentStatus = "Pickup";
        } else
            Toast.makeText(context, "it's already pick the order", Toast.LENGTH_SHORT).show();

    }


    private void cliackBtn(Button selected, Button unselected) {
        GradientDrawable gd = (GradientDrawable) selected.getBackground().getCurrent();
        gd.setColor(Color.parseColor("#069E22"));
        selected.setTextColor(Color.parseColor("#FFFFFF"));

        GradientDrawable unSelected_gd = (GradientDrawable) unselected.getBackground().getCurrent();
        unSelected_gd.setColor(Color.parseColor("#EFF1F3"));
        unselected.setTextColor(Color.parseColor("#70899B"));


    }

    @OnClick({R.id.resheduleBtn, R.id.resheduleTxt})
    void setResheduleBtn() {
        milkDetailObject.setStatus(WebUrlClass.statusCancel);
        callUpdateStatusApi();
    }

    @OnClick({R.id.arraiveBtn, R.id.arraiveBtn_next})
    void setArrivedBtn() {

        callUpdateStatusApi();


    }

    private void callUpdateStatusApi() {
        if (isnet()) {
            progressDialog = new ProgressDialog(MilkDeliveryDetailPage.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpdateStatusForMilk().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    progressDialog.dismiss();
                    ut.displayToast(MilkDeliveryDetailPage.this, msg);
                    // hideProgresHud();
                }
            });
        } else {
            ut.displayToast(MilkDeliveryDetailPage.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }
    }


    @OnClick(R.id.route)
    void setRootBtn() {


        if (wLat != 0.0d && wLng != 0.0d) {
            String uriMap = String.format(Locale.ENGLISH, "geo:%f,%f", wLat, wLng);  // use for faocus to location by google map
            String nameAdd = "?q=" + Uri.encode(String.valueOf(wLat) + " ," + String.valueOf(wLng) + addressName.getText().toString().trim());  // use for add marker on location
            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + wLat + "," + wLng);
            Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } else {
            Toast.makeText(context, "Address can't open on map ", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick({R.id.phoneNumber, R.id.phoneNumber1})
    void setPhoneNumber() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber.getText().toString().trim()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    987);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 987: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setPhoneNumber();
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }

        }
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("updateStatus", new Gson().toJson(milkDetailObject));
        setResult(997, intent);
        finish();
        /*if (cashAmmount.getVisibility() == View.VISIBLE) {
            cashAmmount.setVisibility(View.GONE);

        } else {
            super.onBackPressed();
        }*/
    }


    private void getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        //  GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                //return null;
            }
            Address location = address.get(0);
            wLat = location.getLatitude();
            wLng = location.getLongitude();

           /* p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));*/

            // return new Lo(location.getLatitude() , location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void calculateExpectedTime() {
        GPSTracker gpsTracker = new GPSTracker(this);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        if (latitude != 0.0d && longitude != 0.0d && wLat != 0.0d && wLng != 0.0d) {
            String pickupLocationAddress = convertLatLngintoAddress(latitude, longitude);
            String dropLocationAddress = convertLatLngintoAddress(wLat, wLng);
            DataLongOperationAsynchTask fetchUrl = new DataLongOperationAsynchTask();
            //fetchUrl.execute(pickupLocationAddress, dropLocationAddress);
            fetchUrl.execute(String.valueOf(latitude), String.valueOf(longitude), String.valueOf(wLat), String.valueOf(wLng));
        }
        // https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=YOUR_API_KEY


    }

    private String convertLatLngintoAddress(double lat, double lng) {
        String area = "";
        Geocoder geocoder = new Geocoder(MilkDeliveryDetailPage.this);
        try {

            List<Address> addressList = geocoder.getFromLocation(lat, lng, 5);
            if (addressList.size() != 0) {
                area = addressList.get(0).getAddressLine(0);
                   /* String cName = addressList.get(0).getCountryName();
                    String pCode = addressList.get(0).getPostalCode();
                    String cityName = addressList.get(0).getLocality();*/
                // return area;
                // address = cityName + "," + area + "," + cName + "," + pCode;
                // etLocation.setText(address);
                //  Log.i("Address :: ", address);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return area;

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

    private class DataLongOperationAsynchTask extends AsyncTask<String, Void, String[]> {
        ProgressDialog dialog = new ProgressDialog(MilkDeliveryDetailPage.this);

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
           /* String picAddress = params[0];
            String dropAddress = params[1];*/
            String slat = params[0];
            String slng = params[1];
            String dlat = params[2];
            String dlng = params[3];
            try {
                // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address="+txtaddressSource.getText().toString().trim()+"&key="+getResources().getString(R.string.google_map_api));
                // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address=" + currentAddres + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
                response = getLatLongByURL("https://maps.googleapis.com/maps/api/directions/json?origin=" + slat + "," + slng + "&destination=" + dlat + "," + dlng + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
                //https://maps.googleapis.com/maps/api/directions/json?origin=18.5204,%2073.8567&destination=15.2993%C2%B0,74.1240&key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk
                Log.d("response", "" + response);

                return new String[]{response};
            } catch (Exception e) {
                return new String[]{"error"};

            }
        }

        @Override
        protected void onPostExecute(String... result) {
            try {
                JSONObject jsonObject = new JSONObject(result[0]);

                String lng = ((JSONArray) jsonObject.get("routes")).getJSONObject(0)
                        .getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");

                expectedTime.setText(lng);


            } catch (Exception e) {
                e.printStackTrace();
                // getLocation();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }


    private class UpdateStatusForMilk extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        PostDataObject postDataObject;
        String statusValue;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (milkDetailObject.getStatus().equals(WebUrlClass.statusPending)) {
                //  arraiveBtn_next.setText("Arrived");
                currentStatus = WebUrlClass.statusStart;
                /// milkDetailObject.setStatus(WebUrlClass.statusStart);
                statusValue = startValue;
            } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusStart)) {
                //  arraiveBtn_next.setText("Loading");
                currentStatus = WebUrlClass.statusArrived;
                //   milkDetailObject.setStatus(WebUrlClass.statusArrived);
                statusValue = arrivedValue;
            } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusArrived)) {
                //  arraiveBtn_next.setText("Complete Trip");
                currentStatus = WebUrlClass.statusLoading;
                // milkDetailObject.setStatus(WebUrlClass.statusLoading);
                statusValue = loadingValue;
            } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusLoading)) {
                //  arraiveBtn_next.setText("Complete Trip");
                currentStatus = WebUrlClass.statusComplete;
                //  milkDetailObject.setStatus(WebUrlClass.statusComplete);
                statusValue = completeValue;
            } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusCancel)) {
                currentStatus = WebUrlClass.statusCancel;
                //  milkDetailObject.setStatus(WebUrlClass.statusComplete);
                statusValue = cancelValue;
            }


            postDataObject = new PostDataObject();
            postDataObject.setObjTripDetail(new ObjTripDetail(UserMasterId, milkDetailObject.getTripdetailid(),
                    milkDetailObject.getTripHeaderId(),
                    milkDetailObject.getSequneceNumber(),
                    milkDetailObject.getAddress(),
                    ActivityId,
                    milkDetailObject.getShipToMasterId(),
                    milkDetailObject.getLatitude(),
                    milkDetailObject.getLongitude(),
                    "", "",
                    expectedTime.getText().toString(),
                    statusValue,
                    pendinValue,
                    milkDetailObject.getUserName()
            ));
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String url = CompanyURL + WebUrlClass.changeTripDetailStatus;
            try {
                res = ut.OpenPostConnection(url, new Gson().toJson(postDataObject), getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            if (res.equals("true")) {
                if (milkDetailObject.getStatus().equals(WebUrlClass.statusPending)) {

                    milkDetailObject.setStatus(WebUrlClass.statusStart);
                    milkDetailObject.setTripDetailStatus(startValue);
                    AppCommon.getInstance(context).setSMS_Servive(true, milkDetailObject.getContactNo()
                            , milkDetailObject.getLatitude(), milkDetailObject.getLongitude()
                            , milkDetailObject.getConsigneeName());

                    SendSMS_Vendor();

                } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusStart)) {
                    milkDetailObject.setTripDetailStatus(arrivedValue);
                    milkDetailObject.setStatus(WebUrlClass.statusArrived);
                    AppCommon.getInstance(context).setSMS_Servive(false, milkDetailObject.getContactNo()
                            , milkDetailObject.getLatitude(), milkDetailObject.getLongitude()
                            , milkDetailObject.getConsigneeName());

                } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusArrived)) {
                    milkDetailObject.setTripDetailStatus(loadingValue);
                    milkDetailObject.setStatus(WebUrlClass.statusLoading);
                    resheduleTxt.setText("Loading Cancel");
                    resheduleTxt.setVisibility(View.GONE);
                } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusLoading)) {
                    milkDetailObject.setTripDetailStatus(completeValue);
                    milkDetailObject.setStatus(WebUrlClass.statusComplete);
                } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusComplete)) {
                    milkDetailObject.setTripDetailStatus(cancelValue);
                    milkDetailObject.setStatus(WebUrlClass.statusComplete);
                    arraiveBtn_next.setVisibility(View.GONE);
                    arraiveBtn.setVisibility(View.GONE);
                    route.setVisibility(View.GONE);
                    expectedTimeLayout.setVisibility(View.GONE);
                    pickUpLayout.setVisibility(View.GONE);
                }else if(milkDetailObject.getStatus().equals(WebUrlClass.statusCancel) ){
                    milkDetailObject.setTripDetailStatus(cancelValue);
                    milkDetailObject.setStatus(WebUrlClass.statusCancel);
                    arraiveBtn_next.setVisibility(View.GONE);
                    arraiveBtn.setVisibility(View.GONE);
                    route.setVisibility(View.GONE);
                    expectedTimeLayout.setVisibility(View.GONE);
                    pickUpLayout.setVisibility(View.GONE);
                }

                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("Status", currentStatus);
                values.put("TripDetailStatus", statusValue);


                // updating row
                sqLiteDatabase.update(db.TABLE_DELIVERY_MILK_RUN, values, "tripdetailid" + " = ?",
                        new String[]{String.valueOf(milkDetailObject.tripdetailid)});
                onBackPressed();
                setButtonText();
            } else {
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
            }
        }

        private void SendSMS_Vendor() {
            String phoneNumber = milkDetailObject.getContactNo();
                   /* SendSMS_Vendor fetchUrl = new SendSMS_Vendor();
                    //fetchUrl.execute(pickupLocationAddress, dropLocationAddress);
                    fetchUrl.execute(phoneNumber, lastETA , milkDetailObjectArrayList.get(j).getConsigneeName());*/

            String url ="http://qm.vritti.co.in:420/VrittiQM.asmx/CallWebService?m=" + phoneNumber + "&u=ae1001&p=vritti123&s=" +" "+milkDetailObject.getConsigneeName()+" \n Vehicle No. : "+milkDetailObject.getVehicleno()+"\n Reach by: "+ expectedTime.getText().toString().trim()+" \n Driver Name : "+milkDetailObject.getUserName()+"\n Mobile No: "+milkDetailObject.getDriverContact();

            RequestQueue queue = Volley.newRequestQueue(context);
            // String url ="http://www.google.com";


            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            Log.i("response::" , response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i("response::" , String.valueOf(error));
                }
            });


            queue.add(stringRequest);
        }
    }


}




