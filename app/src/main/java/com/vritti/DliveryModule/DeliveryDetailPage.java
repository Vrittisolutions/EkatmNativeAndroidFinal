package com.vritti.DliveryModule;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vritti.chat.activity.AddGroupActivity;
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
import com.vritti.vwb.ImageWithLocation.EnoSampleSubmitClass;
import com.vritti.vwb.ImageWithLocation.FileExtantionArray;
import com.vritti.vwb.ImageWithLocation.ImageWithLoactionActivity;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityMain;

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

import static com.vritti.vwb.vworkbench.ActivityMain.AtendanceSheredPreferance;

public class DeliveryDetailPage extends Activity implements TextWatcher {

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
    String soNo = "PRM/18-19/0094";
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

    String paymentType = "", amuntPrice = "";
    public static FirebaseJobDispatcher dispatcherNew;
    public static Job myJobNew = null;
    private GoogleApiClient googleApiClient = null;

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
            soNo = getIntent().getStringExtra("activityName");
            ActivityId = getIntent().getStringExtra("activityId");
            isApproval = getIntent().getStringExtra("isApproval");
            ActivityName = getIntent().getStringExtra("activityName");
        }
        String sonoArray[] = soNo.split(",");
        soNo = sonoArray[0];
        JSONObject jsonObject = isInLocal();
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        cliackBtn(pickupBtn, resheduleBtn);
        if (jsonObject == null) {
            getDetailsApiCall();
            resheduleTxt.setVisibility(View.GONE);
            ll.setVisibility(View.VISIBLE);
            resheduleLayout.setVisibility(View.GONE);
            pickUpLayout.setVisibility(View.GONE);
            paymentTypeLayout.setVisibility(View.GONE);
            expectedTimeLayout.setVisibility(View.GONE);

        } else {
            if (!currentStatus.equals("")) {
                if (currentStatus.equals("Reschedule")) {
                    currentStatus = "Pickup";
                    resheduleTxt.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                    waereHouseLayout.setVisibility(View.GONE);
                    expectedTimeLayout.setVisibility(View.VISIBLE);
                } else {
                    if (currentStatus.equals("")) {
                        resheduleTxt.setVisibility(View.GONE);
                        ll.setVisibility(View.VISIBLE);
                        waereHouseLayout.setVisibility(View.VISIBLE);
                        expectedTimeLayout.setVisibility(View.GONE);
                    } else {
                        resheduleTxt.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.GONE);
                    }
                }
                setStatus(currentStatus, true);
                pickUpLayout.setVisibility(View.VISIBLE);
                paymentTypeLayout.setVisibility(View.VISIBLE);
                waereHouseLayout.setVisibility(View.GONE);
                expectedTimeLayout.setVisibility(View.VISIBLE);
            } else {
                resheduleTxt.setVisibility(View.GONE);
                ll.setVisibility(View.VISIBLE);
                waereHouseLayout.setVisibility(View.GONE);
                expectedTimeLayout.setVisibility(View.VISIBLE);
                resheduleLayout.setVisibility(View.GONE);
                pickUpLayout.setVisibility(View.GONE);
                paymentTypeLayout.setVisibility(View.GONE);
                expectedTimeLayout.setVisibility(View.GONE);
            }
            setData(jsonObject, false);
        }
        editTextOne.addTextChangedListener(this);
        editTextTwo.addTextChangedListener(this);
        editTextThree.addTextChangedListener(this);
        editTextFour.addTextChangedListener(this);
        editTextFive.addTextChangedListener(this);
        editTextSix.addTextChangedListener(this);
    }

    private JSONObject isInLocal() {
        JSONObject jsonObject = null;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_DELIVERY_BOY + " where InvoiceNo='" + soNo + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                jsonObject = new JSONObject();
                try {
                    jsonObject.put("ConsigneeName", c.getString(c.getColumnIndex("ConsigneeName")))
                    ;
                    jsonObject.put("InvoiceNo", c.getString(c.getColumnIndex("InvoiceNo")));
                    jsonObject.put("Address", c.getString(c.getColumnIndex("Address")));
                    jsonObject.put("Mobile", c.getString(c.getColumnIndex("Mobile")));
                    jsonObject.put("TotNetAmnt", c.getString(c.getColumnIndex("TotNetAmnt")));
                    jsonObject.put("Latitude", c.getString(c.getColumnIndex("Latitude")));
                    jsonObject.put("Longitude", c.getString(c.getColumnIndex("Longitude")));
                    jsonObject.put("Status", c.getString(c.getColumnIndex("Status")));
                    jsonObject.put("OrderDt", c.getString(c.getColumnIndex("OrderDt")));
                    jsonObject.put("WarehouseDescription", c.getString(c.getColumnIndex("WarehouseDescription")));
                    jsonObject.put("PrefDelFrmTime", c.getString(c.getColumnIndex("PrefDelFrmTime")));
                    jsonObject.put("PrefDelToTime", c.getString(c.getColumnIndex("PrefDelToTime")));
                    if (c.getString(c.getColumnIndex("Latitude")) != null && c.getString(c.getColumnIndex("Longitude")) != null) {
                        try {
                            wLat = Double.parseDouble(c.getString(c.getColumnIndex("Latitude")));
                            wLng = Double.parseDouble(c.getString(c.getColumnIndex("Longitude")));
                            if (wLng == 0.0d && wLng == 0.0d) {
                                getLocationFromAddress(c.getString(c.getColumnIndex("Address")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    currentStatus = c.getString(c.getColumnIndex("Status"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //val = c.getString(c.getColumnIndex(columnname));

            } while (c.moveToNext());
        }
        sql.close();

        return jsonObject;
    }

    private void getDetailsApiCall() {
        if (isnet()) {
            new StartSession(DeliveryDetailPage.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    //new GetDeliveryDetailsApi.execute();
                    new GetDeliveryDetailsApi().execute(soNo);
                }

                @Override
                public void callfailMethod(String msg) {
                    //Log.
                    progressDialog.dismiss();
                }
            });
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


    private void setStatus(String status, boolean isUpdate) {
        /* */
        if (isUpdate) {
            SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Status", status);


            // updating row
            sqLiteDatabase.update(db.TABLE_DELIVERY_BOY, values, "InvoiceNo" + " = ?",
                    new String[]{String.valueOf(soNo)});

            switch (status) {

                case "Reschedule":

                    setStatus(lastStatus, true);
                    Toast.makeText(context, "Activity is Reschedule for :" + reDate, Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    waereHouseLayout.setVisibility(View.GONE);
                    expectedTimeLayout.setVisibility(View.VISIBLE);
                    break;

                case "Complete":
                    Toast.makeText(context, "Activity is Completed!..", Toast.LENGTH_SHORT).show();
                    SQLiteDatabase sql1 = db.getWritableDatabase();
                    sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    sql1.close();
                    onBackPressed();
                    break;
                case "Pickup":
                    Toast.makeText(context, "Product pickup successfully!", Toast.LENGTH_SHORT).show();
                    resheduleTxt.setVisibility(View.VISIBLE);
                    ll.setVisibility(View.GONE);
                    waereHouseLayout.setVisibility(View.GONE);
                    resheduleLayout.setVisibility(View.VISIBLE);
                    pickUpLayout.setVisibility(View.VISIBLE);
                    paymentTypeLayout.setVisibility(View.VISIBLE);
                    expectedTimeLayout.setVisibility(View.VISIBLE);
                    break;

            }
            sqLiteDatabase.close();
        } else {


            switch (status) {
                case "Pickup":

                    ChangeActivityStatusJSONObj("WIP");
                    break;


                case "Reschedule":
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    // final String[] date = new String[1];
                    DatePickerDialog datePickerDialog = new DatePickerDialog(DeliveryDetailPage.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker datePicker, final int year,
                                                      final int monthOfYear, final int dayOfMonth) {

                                    datePicker.setMinDate(c.getTimeInMillis());
                                    reDate = String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth)) + "-" + year;
                                    Log.i("Date::", reDate);

                                    final Calendar c = Calendar.getInstance();
                                    final int mHour = c.get(Calendar.HOUR_OF_DAY);
                                    ;
                                    int mMinute = c.get(Calendar.MINUTE);


                                    Toast.makeText(DeliveryDetailPage.this, "Please select start time", Toast.LENGTH_SHORT).show();

                                    // Launch Time Picker Dialog
                                    TimePickerDialog timePickerDialog = new TimePickerDialog(DeliveryDetailPage.this,
                                            new TimePickerDialog.OnTimeSetListener() {

                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                      int minute) {

                                                    Log.i("Time::", String.valueOf(hourOfDay + ":" + minute));
                                                    Toast.makeText(DeliveryDetailPage.this, "Please select end time", Toast.LENGTH_SHORT).show();

                                                    // end time picker
                                                    final int eHour = hourOfDay;
                                                    final int eMinute = minute;
                                                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(DeliveryDetailPage.this,
                                                            new TimePickerDialog.OnTimeSetListener() {

                                                                @Override
                                                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                                                      int minute) {
                                                                    Calendar startDayInstance = Calendar.getInstance();
                                                                    Calendar endDayInstance = Calendar.getInstance();
                                                                    startDayInstance.set(Calendar.HOUR_OF_DAY, eHour);
                                                                    startDayInstance.set(Calendar.MINUTE, eMinute);
                                                                    endDayInstance.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                                                    endDayInstance.set(Calendar.MINUTE, minute);

                                                                    if (startDayInstance.getTimeInMillis() <= endDayInstance.getTimeInMillis()) {


                                                                        Log.i("Time::", String.valueOf(hourOfDay + ":" + minute));

                                                                        // reason dialog

                                                                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DeliveryDetailPage.this);
                                                                        LayoutInflater inflater = getLayoutInflater();
                                                                        final View dialogView = inflater.inflate(R.layout.vwb_dialog_reschedule, null);
                                                                        dialogBuilder.setView(dialogView);
                                                                        Button btn_rescheduleCancel = (Button) dialogView.findViewById(R.id.btn_rescheduleCancel);
                                                                        final EditText edt_remark = (EditText) dialogView.findViewById(R.id.edt_remark);
                                                                        final Button btn_rescheduleOk = (Button) dialogView.findViewById(R.id.btn_rescheduleOk);
                                                                        final Button btn_reschedule = (Button) dialogView.findViewById(R.id.btn_reschedule);
                                                                        final AlertDialog b = dialogBuilder.create();
                                                                        if (!reDate.equals("")) {

                                                                            btn_reschedule.setText(String.format("%02d", (dayOfMonth)) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year);
                                                                            b.show();
                                                                        }
                                                                        btn_rescheduleCancel.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                b.dismiss();
                                                                            }
                                                                        });
                                                                        btn_rescheduleOk.setOnClickListener(new View.OnClickListener() {
                                                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                                if (!edt_remark.getText().toString().trim().equals("")) {
                                                                                    AppCommon.getInstance(context).onHideKeyBoard(DeliveryDetailPage.this);
                                                                                    getRescheduleJSONObj(edt_remark.getText().toString(), reDate);


                                                                                    b.dismiss();
                                                                                } else {
                                                                                    Toast.makeText(context, "Please enter the remark", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }
                                                                        });
                                                                    } else {
                                                                        // timePickerDialog1.show();
                                                                        Toast.makeText(DeliveryDetailPage.this, "Please tyr again and enter valid time.", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }, eHour, eMinute, true);
                                                    timePickerDialog1.setTitle("start time");
                                                    timePickerDialog1.show();
                                                }
                                            }, mHour, mMinute, true);
                                    timePickerDialog.setTitle("start time");
                                    timePickerDialog.show();


                                }
                            }, year, month, day);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // only for gingerbread and newer versions
                        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    }
                    datePickerDialog.setTitle("Select Date");

                    datePickerDialog.show();

                    // ChangeActivityStatusJSONObj("WIP");
                    break;

            }
        }

    }

    private void getRescheduleJSONObj(String remark, final String date) {
        JSONObject RescheduleObj = new JSONObject();
        try {
            RescheduleObj.put("ActivityId", ActivityId);
            RescheduleObj.put("EndDate", date);
            RescheduleObj.put("ResRemark", remark);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = RescheduleObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        final String op = "Success";
        final String url = CompanyURL + WebUrlClass.api_Reschedule + "?ActivityId=" + ActivityId + "&EndDate=" + date + "&ResRemark=" + remark;

        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        // CreateOfflineModeChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        new StartSession(getApplicationContext(), new CallbackInterface() {
            @Override
            public void callMethod() {
                new SendTaskGet().execute(url, date);
                // new SendTask().execute(url, FinalObj, op);
            }

            @Override
            public void callfailMethod(String msg) {
                progressDialog.dismiss();
                Toast.makeText(context, "please try again something went wrong,,", Toast.LENGTH_SHORT).show();
                Log.i("checkStatus::", msg);
            }
        });

    }

    @OnClick(R.id.pickupBtn)
    void setPickupBtn() {


        if (currentStatus.equals("Pickup") || currentStatus.equals("")) {
            setStatus("Pickup", false);
            //  cliackBtn(pickupBtn, resheduleBtn);
            currentStatus = "Pickup";

        } else
            Toast.makeText(context, "it's already pick the order", Toast.LENGTH_SHORT).show();

    }


    @OnClick(R.id.rbCash)
    void setCashBtn() {

        if (!rbCash.isActivated()) {
            rbCash.setChecked(true);
            rbOnline.setChecked(false);
            rbPaid.setChecked(false);
            paymentType = "cash";
        }
    }

    @OnClick(R.id.rbOnline)
    void setOnlineBtn() {
        if (!rbOnline.isActivated()) {
            rbCash.setChecked(false);
            rbOnline.setChecked(true);
            rbPaid.setChecked(false);
            paymentType = "cash";
        }
    }

    @OnClick(R.id.rbPaid)
    void setPaidBtn() {
        if (!rbOnline.isActivated()) {
            rbCash.setChecked(false);
            rbOnline.setChecked(false);
            rbPaid.setChecked(true);
            paymentType = "paid";
        }
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
        //  cliackBtn(resheduleBtn, pickupBtn);
        lastStatus = currentStatus;
        currentStatus = "Reschedule";
        setStatus("Reschedule", false);
    }

    @OnClick({R.id.arraiveBtn, R.id.arraiveBtn_next})
    void setArrivedBtn() {
        currentStatus = "Arrived";
        setStatus("Arrived", false);

        if (rbPaid.isChecked() || rbCash.isChecked() || rbOnline.isChecked()) {

            if (rbCash.isChecked()) {
                cashAmmount.setVisibility(View.VISIBLE);
                String priceStr = price.getText().toString().trim().replace(getResources().getString(R.string.rs), " ");
                editTexCash.setText(priceStr.trim());
                editTexCash.setSelection(editTexCash.getText().length());

            } else if (rbOnline.isChecked()) {
                try {
                    if (ContextCompat.checkSelfPermission(DeliveryDetailPage.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                        }
                    } else {

                        captureImage();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String serverPriceStr = price.getText().toString().trim().replace(getResources().getString(R.string.rs), " ");
                amuntPrice = serverPriceStr;
                if (phoneNum != null && !(phoneNum.equals(""))) {
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    new StartSession(DeliveryDetailPage.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new DownloadAuthenticate().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(context, "Sorry customer number is Invalid No.", Toast.LENGTH_SHORT).show();
                }
            }
        } else
            Toast.makeText(context, "Please select payment type", Toast.LENGTH_SHORT).show();


    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public static File getOutputMediaFile(int type) {
        File mediaStorageDir;
        // External sdcard location
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), SetAppName.IMAGE_DIRECTORY_NAME);


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(SetAppName.IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + SetAppName.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + timeStamp + ".jpg");

            Log.d("test", "mediaFile" + mediaFile);


        }
        return mediaFile;
    }

    @OnClick(R.id.cashBtn_new)
    void setCash() {
        String serverPriceStr = price.getText().toString().trim().replace(getResources().getString(R.string.rs), " ");
        String getPriceStr = editTexCash.getText().toString().trim();
        float enterPrice = Float.parseFloat(getPriceStr);
        float serverPrice = Float.parseFloat(serverPriceStr);
        enterPrice = serverPrice - enterPrice;
        if (getPriceStr.isEmpty()) {
            Toast.makeText(context, "Please enter the collected amount", Toast.LENGTH_SHORT).show();
        } else if (enterPrice > 0.0 || enterPrice == 0.0) {
            cashAmmount.setVisibility(View.GONE);
            amuntPrice = getPriceStr;
            if (phoneNum != null && !(phoneNum.equals(""))) {
                progressDialog.setTitle("Please wait...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                new StartSession(DeliveryDetailPage.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadAuthenticate().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        progressDialog.dismiss();
                    }
                });

            } else {
                Toast.makeText(context, "Sorry customer number is Invalid No.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Sorry you can't collect more than mention amount", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.resendOtp)
    void setResend() {
        currentStatus = "Arrived";
        setStatus("Arrived", false);
        otpLayout_new.setVisibility(View.GONE);

        if (phoneNum != null && !(phoneNum.equals(""))) {
            progressDialog.setTitle("Please wait...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
            new StartSession(DeliveryDetailPage.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadAuthenticate().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(context, "Sorry customer number is Invalid No.", Toast.LENGTH_SHORT).show();
        }


        // ChangeActivityStatusJSONObj("WIP");
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
            case 100: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Permission", "Granted");
                    try {
                        captureImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Permission", "Denied");
                }
                break;
            }
        }
    }

    @OnClick({R.id.otpBtn, R.id.otpBtn_new})
    void getOtp() {
        String s1 = editTextOne.getText().toString().trim();
        String s2 = editTextTwo.getText().toString().trim();
        String s3 = editTextThree.getText().toString().trim();
        String s4 = editTextFour.getText().toString().trim();
        String s5 = editTextFive.getText().toString().trim();
        String s6 = editTextSix.getText().toString().trim();
        String enterOtp = "";
        if (s1.isEmpty() || s2.isEmpty() || s3.isEmpty() || s4.isEmpty() || s5.isEmpty() || s6.isEmpty()) {
            Toast.makeText(context, "Please enter valid Otp", Toast.LENGTH_SHORT).show();
        } else {
            enterOtp = s1 + s2 + s3 + s4 + s5 + s6;
        }
        if (enterOtp.equals("")) {
            Toast.makeText(context, "Please enter otp", Toast.LENGTH_SHORT).show();
        } else if (enterOtp.equals(Otp)) {
            currentStatus = "Complete";
            ChangeActivityStatusJSONObj("Complete");

        } else {
            Toast.makeText(context, "Please enter valid Otp", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.otpLayout_new)
    void setOtpLayout() {
        if (otpLayout_new.getVisibility() == View.VISIBLE) {
            otpLayout_new.setVisibility(View.GONE);
        } else {
            otpLayout_new.setVisibility(View.VISIBLE);

        }
    }


    private void ChangeActivityStatusJSONObj(String status) {

        ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

           /* string ActivityId = JsonData.ActivityId;
            string StatusCode = JsonData.StatusCode;
            string IsApproval = JsonData.IsApproval;
            string Amt = JsonData.Amt;*/
            ChangeActivityStatus.put("ActivityId", ActivityId);
            if (status.equalsIgnoreCase("Cancelled")) {
                StatusFlag = WebUrlClass.FlagCancel;
                ChangeActivityStatus.put("StatusCode", "15");
                remark = "Cancel the activity " + ActivityName;

            } else if (status.equalsIgnoreCase("WIP")) {
                StatusFlag = WebUrlClass.FlagWIP;
                ChangeActivityStatus.put("StatusCode", "14");
                remark = "Change status of activity " + ActivityName + " to WIP ";

            } else if (status.equalsIgnoreCase("Pause")) {
                StatusFlag = WebUrlClass.FlagPause;
                ChangeActivityStatus.put("StatusCode", "25");
                remark = "Change status of activity " + ActivityName + " to Pause ";
            } else if (status.equalsIgnoreCase("Complete")) {
                StatusFlag = WebUrlClass.FlagComplete;
                ChangeActivityStatus.put("StatusCode", "12");
                ChangeActivityStatus.put("IsApproval", isApproval);
                ChangeActivityStatus.put("Amt", "");


                remark = "Complete the activity " + ActivityName;

            }
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String url = CompanyURL + WebUrlClass.api_change_activity_status;
        final String op = "Success";
        progressDialog.setTitle("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        // CreateOfflineModeChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        new StartSession(getApplicationContext(), new CallbackInterface() {
            @Override
            public void callMethod() {
                new SendTask().execute(url, FinalObj, op);
            }

            @Override
            public void callfailMethod(String msg) {
                progressDialog.dismiss();
                Toast.makeText(context, "please try again something went wrong,,", Toast.LENGTH_SHORT).show();
                Log.i("checkStatus::", msg);
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() == 1) {
            if (editTextOne.length() == 1) {
                editTextTwo.requestFocus();
            }

            if (editTextTwo.length() == 1) {
                editTextThree.requestFocus();
            }
            if (editTextThree.length() == 1) {
                editTextFour.requestFocus();
            }
            if (editTextFour.length() == 1) {
                editTextFive.requestFocus();
            }
            if (editTextFive.length() == 1) {
                editTextSix.requestFocus();
            }

        } else if (editable.length() == 0) {
            if (editTextSix.length() == 0) {
                editTextFive.requestFocus();
            }
            if (editTextFive.length() == 0) {
                editTextFour.requestFocus();
            }
            if (editTextFour.length() == 0) {
                editTextThree.requestFocus();
            }
            if (editTextThree.length() == 0) {
                editTextTwo.requestFocus();
            }
            if (editTextTwo.length() == 0) {
                editTextOne.requestFocus();
            }
        }
    }


    public class SendTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenPostConnection(url, params[1], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\", "");

                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                Log.i("checkStatus::", e.getMessage());
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            //  data.add(params[3]);
            // data.add(params[4]);
            //  data.add(params[5]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            // String stdres = result.get(1);
            //String recid = result.get(2);
            //String filepath = result.get(3);
            //String filename = result.get(4);
            SQLiteDatabase sql = db.getWritableDatabase();
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                progressDialog.dismiss();
                Log.i("response::", res);
                if (res.equals("Success")) {
                    setStatus(currentStatus, true);
                }


            } else {
                progressDialog.dismiss();
                Toast.makeText(context, "please try again something went wrong,,", Toast.LENGTH_SHORT).show();
                Log.i("response::", res);
            }

        }
    }


    public class SendTaskGet extends AsyncTask<String, Void, String[]> {
        Object res;
        String response, date = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String[] doInBackground(String... params) {

            try {
                String url = params[0];
                date = params[1];
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            //ArrayList<String> data = new ArrayList<String>();
            String[] data = {response};
            //data[0] = response;
            // data.add(response);
            return data;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();

            String res = result[0];//.get(0);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                Log.i("Status::", res);
                if (res.equals("Success")) {
                    setStatus(currentStatus, true);
                    SimpleDateFormat spf = new SimpleDateFormat("mm-dd-yyyy");
                    Date newDate = null;
                    try {
                        newDate = spf.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    spf = new SimpleDateFormat("dd-mm-yyyy");
                    date = spf.format(newDate);
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("OrderDt", date);


                    // updating row
                    sqLiteDatabase.update(db.TABLE_DELIVERY_BOY, values, "InvoiceNo" + " = ?",
                            new String[]{String.valueOf(soNo)});
                }
            } else {
                Log.i("Status::", res);
                //  Toast.makeText(getApplicationContext(),"Record not send",Toast.LENGTH_LONG).show();

            }


        }
    }

    private void CreateOfflineModeChageStatus(final String url, final String parameter,
                                              final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagCancel)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});

            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagWIP)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "14");
                values.put("Status", "WIP");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagPause)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "25");
                values.put("Status", "PAUSED");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});

            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagComplete)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql1.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});

            }
            sql1.close();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

    @Override
    public void onBackPressed() {

        if (cashAmmount.getVisibility() == View.VISIBLE) {
            cashAmmount.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    class GetDeliveryDetailsApi extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_devery_details + "?InvoiceNo=" + soNo;
            try {
                res = ut.OpenConnection(url, DeliveryDetailPage.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {

                e.printStackTrace();
                response = "Error";
                progressDialog.dismiss();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                response = response.substring(1, response.length() - 1);
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject != null)
                    setData(jsonObject, true);

            } catch (Exception e) {
                progressDialog.dismiss();
                Toast.makeText(context, "No record found !..", Toast.LENGTH_SHORT).show();
                onBackPressed();
                e.printStackTrace();
            }
        }
    }

    private void setData(JSONObject jsonObject, boolean setInsert) {
        progressDialog.dismiss();
        customerName.setText(jsonObject.optString("ConsigneeName"));
        InvoiveNumber.setText(jsonObject.optString("InvoiceNo"));
        addressName.setText(jsonObject.optString("Address"));
        phoneNumber.setText(jsonObject.optString("Mobile"));
        if (jsonObject.optString("WarehouseDescription").equals("null") || jsonObject.optString("WarehouseDescription").equals(""))
            wareHouseAddressTxt.setText(jsonObject.optString("N/A"));
        else
            wareHouseAddressTxt.setText(jsonObject.optString("WarehouseDescription"));
        time.setText(jsonObject.optString("PrefDelFrmTime") + " To " + jsonObject.optString("PrefDelToTime"));
        phoneNumber.setText(jsonObject.optString("Mobile"));
        price.setText(getString(R.string.rs) + " " + jsonObject.optString("TotNetAmnt"));
        phoneNum = phoneNumber.getText().toString().trim();
        String orderDate = jsonObject.optString("OrderDt");
        String startDate = "";
        if (jsonObject.optString("Latitude") != null && jsonObject.optString("Longitude") != null) {
            try {
                wLat = Double.parseDouble(jsonObject.optString("Latitude"));
                wLng = Double.parseDouble(jsonObject.optString("Longitude"));
                if (wLng == 0.0d && wLng == 0.0d) {
                    getLocationFromAddress(jsonObject.optString("Address"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (orderDate.contains("Date")) {
            String StarDresult = orderDate.substring(orderDate.indexOf("(") + 1, orderDate.lastIndexOf(")"));
            long Stime = Long.parseLong(StarDresult);
            Date StartDate = new Date(Stime);
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            startDate = sdf.format(StartDate);
        } else {
            startDate = orderDate;
        }
        date.setText(startDate);


        //getLocationFromAddress("Plot No.17/18, S. No. 89/90, Ramchandra Mane Rd, Anant Kurpa Society, Lokmanya Colony, Kothrud, Pune, Maharashtra 411038, India");
        getLocationFromAddress(addressName.getText().toString().trim());

        if (setInsert) {
            SQLiteDatabase sql = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("ConsigneeName", jsonObject.optString("ConsigneeName")); // Contact Name
            values.put("InvoiceNo", jsonObject.optString("InvoiceNo"));
            values.put("Address", jsonObject.optString("Address"));
            values.put("Mobile", jsonObject.optString("Mobile"));
            values.put("TotNetAmnt", jsonObject.optString("TotNetAmnt"));
           /* values.put("Latitiude", String.valueOf(lat));
            values.put("Logitude", String.valueOf(lng));*/
            values.put("Latitude", jsonObject.optString("Latitude"));
            values.put("Longitude", jsonObject.optString("Longitude"));
            values.put("Status", currentStatus);
            values.put("OrderDt", startDate);
            values.put("WarehouseDescription", jsonObject.optString("WarehouseDescription"));

            values.put("PrefDelFrmTime", jsonObject.optString("PrefDelFrmTime"));
            values.put("PrefDelToTime", jsonObject.optString("PrefDelToTime"));
            long a = sql.insert(db.TABLE_DELIVERY_BOY, null, values);
            Log.e("ContACTtABLE", "" + a);
             sql.close();
        }
        calculateExpectedTime();
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

    class DownloadAuthenticate extends AsyncTask<String, Void, String> {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String AppName = "";
            AppName = SetAppName.AppNameFCM;

            String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_Get_DelieveryBoy_OTP + "?MobNo=" + phoneNum + "&Amt=" + amuntPrice.trim() + "&OrderType=" + paymentType + "&OrderNo=" + soNo;
            //UserLoginId=300207&AppName
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                Log.i("sms:", res);
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";

                // Toast.makeText(context, "Please try again something went wrong ", Toast.LENGTH_SHORT).show();
            }
            return res;
        }


        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res != null) {
                if (res.contains("#Success")) {
                    progressDialog.dismiss();
                    String data[] = res.split("#");
                    Otp = data[0];

                    editTextOne.getText().clear();
                    editTextTwo.getText().clear();
                    editTextThree.getText().clear();
                    editTextFour.getText().clear();
                    otpLayout_new.setVisibility(View.VISIBLE);

                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("Status", "Arrived");


                    // updating row
                    sqLiteDatabase.update(db.TABLE_DELIVERY_BOY, values, "InvoiceNo" + " = ?",
                            new String[]{String.valueOf(soNo)});
                    sqLiteDatabase.close();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Please try again something went wrong ", Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(context, "Please try again something went wrong ", Toast.LENGTH_SHORT).show();
            }
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
        Geocoder geocoder = new Geocoder(DeliveryDetailPage.this);
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
        ProgressDialog dialog = new ProgressDialog(DeliveryDetailPage.this);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                bitmap = null;
                try {
                    // bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));

                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    FileOutputStream out = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 75, out);
                    String url = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "attachment", null);

                    fileUri = Uri.parse(url);
                    File f = new File(String.valueOf(fileUri));

                    String remark = "Send " + f.getName() + " by Delivery boy";

                    //  CreateOfflineSaveAttachment(path[1], f.getName(), WebUrlClass.ATTACHMENTFlAG, remark, ActivityId);
                    CreateOfflineSaveAttachment(String.valueOf(fileUri), f.getName(), 3, remark, ActivityId);
                   setJobShedulder();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(context, "Cancel action", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    }

    private void CreateOfflineSaveAttachment(String imageUri, String name, int attachmentFlAG, String remark, String activityId) {
        long a = cf.addofflinedata(imageUri, name, attachmentFlAG, remark, activityId);
        if (a != -1) {
            String serverPriceStr = price.getText().toString().trim().replace(getResources().getString(R.string.rs), " ");
            amuntPrice = serverPriceStr;
            //setJobShedulder();
            if (phoneNum != null && !(phoneNum.equals(""))) {
                if ((progressDialog != null) && !progressDialog.isShowing()) {
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);
                    new StartSession(DeliveryDetailPage.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                            new DownloadAuthenticate().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            progressDialog.dismiss();
                        }
                    });
                }

            } else {
                Toast.makeText(context, "Sorry customer number is Invalid No.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

        }
    }

    private void setJobShedulder() {
        if (myJobNew == null) {
            dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(DeliveryDetailPage.this));
            callJobDispacher();
        } else {
            if (!AppCommon.getInstance(this).isServiceIsStart()) {
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(DeliveryDetailPage.this));
                callJobDispacher();
            } else {
                dispatcherNew.cancelAll();
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(DeliveryDetailPage.this));
                myJobNew = null;
                callJobDispacher();
            }
        }
    }

    private void callJobDispacher() {
        myJobNew = dispatcherNew.newJobBuilder()
                // the JobService that will be called
                .setService(EnoJobService.class)
                // uniquely identifies the job
                .setTag("Eno")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)

                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 180))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(

                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_IDLE


                )
                .build();

        dispatcherNew.mustSchedule(myJobNew);
        AppCommon.getInstance(this).setServiceStarted(true);
    }


}




