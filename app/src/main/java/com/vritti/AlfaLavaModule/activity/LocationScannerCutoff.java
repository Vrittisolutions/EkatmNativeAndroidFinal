package com.vritti.AlfaLavaModule.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.pixplicity.htmlcompat.HtmlCompat;
import com.vritti.AlfaLavaModule.PI.LocationScanner;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.expensemanagement.AddExpenseActivity;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationScannerCutoff extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;

    @BindView(R.id.locationId)
    EditText locationId;

    PutAwayDetail putAwayDetails;
    ArrayList<PutAwayDetail> putAwayDetail;
    String locationCode = "";
    private TextToSpeech t1;
    ImageView img_barcode;
    EditText edt_lot_no;
    TextView txt_date;
    Button btn_scan;
    String LOT="",FIFODate="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutover_location_scan);
        ButterKnife.bind(this);
        pContext = LocationScannerCutoff.this;
        getSupportActionBar().setTitle("GRN Put-away");



        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        img_barcode=findViewById(R.id.img_barcode);
        txt_date=findViewById(R.id.txt_date);
        edt_lot_no=findViewById(R.id.edt_lot_no);
        btn_scan=findViewById(R.id.btn_next);

       // sql.delete(db.TABLE_GRN_PACKET, null, null);

        long date1 = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(date1);
        txt_date.setText(dateString);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        t1.speak("Scan Location", TextToSpeech.QUEUE_FLUSH, null);



        locationId.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
             if ((event.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (locationId != null) {
                                locationId.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay


                    locationCode = locationId.getText().toString().trim();
                    locationId.setText(locationCode);

               /*    startActivity(new Intent(LocationScannerCutoff.this, PacketScanDataCutoff.class)
                         .putExtra("locationcode" , locationCode).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));*/


                    return true;
                }
                return false;
            }
        });
        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOT=edt_lot_no.getText().toString();
                String d=txt_date.getText().toString();
                FIFODate=formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd",d );
                if (!FIFODate.equals("")&&!LOT.equals("")) {
                    IntentIntegrator integrator = new IntentIntegrator(LocationScannerCutoff.this);
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                    integrator.setPrompt("Scan");
                    integrator.setCameraId(0);
                    integrator.setBeepEnabled(false);
                    integrator.setBarcodeImageEnabled(false);
                    integrator.initiateScan();
                }else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        Toast toast = Toast.makeText(LocationScannerCutoff.this, "Please enter LOT", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(LocationScannerCutoff.this, R.raw.alert);
                        mp.start();
                    }else {
                        Toast toast = Toast.makeText(LocationScannerCutoff.this, Html.fromHtml("<font color='#EF4F4F' ><b>"+"Please enter LOT"+"</b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });

        txt_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LocationScannerCutoff.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                FIFODate = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                txt_date.setText(FIFODate);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOT=edt_lot_no.getText().toString();
                String d=txt_date.getText().toString();
                FIFODate=formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd",d );
                locationCode=locationId.getText().toString();

                if (!FIFODate.equals("")&&!LOT.equals("")&&!locationCode.equals("")) {

                    startActivity(new Intent(LocationScannerCutoff.this, PacketScanDataCutoff.class)
                            .putExtra("locationcode", locationCode)
                            .putExtra("lot", LOT)
                            .putExtra("fifo", FIFODate).
                                    setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                        Toast toast = Toast.makeText(LocationScannerCutoff.this, "Please fill all fields", Toast.LENGTH_LONG);
                        View toastView = toast.getView();
                        TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                        toastMessage.setTextSize(18);
                        toastMessage.setTextColor(Color.RED);
                        toastMessage.setGravity(Gravity.CENTER);
                        toastMessage.setCompoundDrawablePadding(5);
                        toastView.setBackgroundColor(Color.TRANSPARENT);
                        toast.show();
                        final MediaPlayer mp = MediaPlayer.create(LocationScannerCutoff.this, R.raw.alert);
                        mp.start();
                    }
                    else {
                        Toast toast = Toast.makeText(LocationScannerCutoff.this, Html.fromHtml("<font color='#EF4F4F' ><b>"+"Please fill all fields"+"</b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });


    }




    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationId.setText("");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");


                locationCode = result.getContents().toString();
                locationId.setText(locationCode);
                    LOT=edt_lot_no.getText().toString();
                    String d=txt_date.getText().toString();
                    FIFODate=formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd",d );

                if (!FIFODate.equals("")&&!LOT.equals("")&&!locationCode.equals("")) {
                        startActivity(new Intent(LocationScannerCutoff.this, PacketScanDataCutoff.class)
                                .putExtra("locationcode", locationCode)
                                .putExtra("lot", LOT)
                                .putExtra("fifo", FIFODate).
                                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {

                        locationId.setText(locationCode);
                    Toast toast = Toast.makeText(LocationScannerCutoff.this, "Please fill all fields", Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.WHITE);
                    toast.show();
                    final MediaPlayer mp = MediaPlayer.create(LocationScannerCutoff.this, R.raw.alert);
                    mp.start();
                }
                    else {
                        Toast toast = Toast.makeText(LocationScannerCutoff.this, Html.fromHtml("<font color='#EF4F4F' ><b>" + "Please fill all fields" + "</b></font>"), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
            }




            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment

        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }
}
