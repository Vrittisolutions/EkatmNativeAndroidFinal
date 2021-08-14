package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.ekatm.services.SendTimeSheet;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EnoSamplingScreen3 extends AppCompatActivity {
    static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;
    ProgressBar mProgrss;
    TextView txtHeader;
    EditText txtTotalQuant, txtSampleUnit, txtSaleUnit;
    Button btnAttachment, btnSave, btnSaveReport;
    static String Screen;
    GPSTracker gps;
    public static double latitude = 0.0, longitude = 0.0;
    static String LocationName = "";
    String result = "";
    public static SharedPreferences AtendanceSheredPreferance;
    static ImageView img1;
    LinearLayout lin_quna, lin_img;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    static File mediaFile;
    String Imagefilename, path;

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonimage = new JSONObject();
    private Uri fileUri;
    static Boolean _flagAttachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eno_sampling_screen3);
        _flagAttachment = false;
        init();
        context = EnoSamplingScreen3.this;
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
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);

        AtendanceSheredPreferance = getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES, Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {

            }
        } else {
        }

        setListner();

        Intent i = getIntent();
        Screen = i.getStringExtra(WebUrlClass.INTENT_ENO_SCREEN);
        if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY1)) {
            txtHeader.setText("Eateries 1");
            btnAttachment.setVisibility(View.VISIBLE);
            btnSaveReport.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);

        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY2)) {
            btnAttachment.setVisibility(View.VISIBLE);

            txtHeader.setText("Eateries 2");
            btnSaveReport.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);


        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_MEDICAL)) {
            txtHeader.setText("Medical Store");
            btnAttachment.setVisibility(View.VISIBLE);

            btnSaveReport.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);


        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_HAATBAZAR)) {
            txtHeader.setText("Haat BAZAR");
            btnAttachment.setVisibility(View.VISIBLE);

            btnSaveReport.setVisibility(View.GONE);
            btnSave.setVisibility(View.VISIBLE);

        }

    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mProgrss = (ProgressBar) findViewById(R.id.toolbar_progress_logging);
        txtHeader = (TextView) findViewById(R.id.txt_header);

        txtTotalQuant = (EditText) findViewById(R.id.txttotalQuant);
        txtSampleUnit = (EditText) findViewById(R.id.txtsampleunit);
        txtSaleUnit = (EditText) findViewById(R.id.txtsaleunit);
        img1 = (ImageView) findViewById(R.id.imageView1);
        lin_img = (LinearLayout) findViewById(R.id.lay_img);
        lin_quna = (LinearLayout) findViewById(R.id.lay_quan);
        btnAttachment = (Button) findViewById(R.id.btn_attachment);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSaveReport = (Button) findViewById(R.id.btn_finalsave);

    }

    public void setListner() {
        btnAttachment.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    _flagAttachment = false;

                    fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValide()) {

                    if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY1)) {
                        EnoSamplingScreen1.enoFinalList.get(0).setTotalQuantityET1(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSampleunitET1(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSaleUnitET1(txtTotalQuant.getText().toString());
                        _flagAttachment = false;

                        Toast.makeText(EnoSamplingScreen3.this, " Record Saved", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(EnoSamplingScreen3.this, EnoSamplingScreen2.class);
                        i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_EATERY2);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY2)) {
                        EnoSamplingScreen1.enoFinalList.get(0).setTotalQuantityET2(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSampleunitET2(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSaleUnitET2(txtTotalQuant.getText().toString());
                        Toast.makeText(EnoSamplingScreen3.this, " Record Saved", Toast.LENGTH_LONG).show();
                        _flagAttachment = false;

                        Intent i = new Intent(EnoSamplingScreen3.this, EnoSamplingScreen2.class);
                        i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_MEDICAL);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_MEDICAL)) {
                        EnoSamplingScreen1.enoFinalList.get(0).setTotalQuantityMS(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSampleunitMS(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSaleUnitMS(txtTotalQuant.getText().toString());
                        Toast.makeText(EnoSamplingScreen3.this, " Record Saved", Toast.LENGTH_LONG).show();
                        _flagAttachment = false;

                        Intent i = new Intent(EnoSamplingScreen3.this, EnoSamplingScreen2.class);
                        i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_HAATBAZAR);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_HAATBAZAR)) {
                        EnoSamplingScreen1.enoFinalList.get(0).setTotalQuantityHB(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSampleunitHB(txtTotalQuant.getText().toString());
                        EnoSamplingScreen1.enoFinalList.get(0).setSaleUnitHB(txtTotalQuant.getText().toString());
                        _flagAttachment = false;

                        getCurrentLocationNew();
                        Toast.makeText(EnoSamplingScreen3.this, " Record Saved. Please click  Save Record to submit report ", Toast.LENGTH_LONG).show();
                        btnSaveReport.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.GONE);

                    }
                }
            }
        });

        btnSaveReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateJson();
               /* if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {


                            //new DownloadAuthenticate().execute();

                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {
                    Toast.makeText(EnoSamplingScreen3.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }*/


            }
        });

    }

    private boolean isValide() {
        if (_flagAttachment.equals(false)) {
            Toast.makeText(EnoSamplingScreen3.this, "Upload selfie", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtTotalQuant.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen3.this, "Enter Total Quantity", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtSampleUnit.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen3.this, "Enter Sample Unit", Toast.LENGTH_LONG).show();
            return false;
        } else if ((txtSaleUnit.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen3.this, "Enter sale Unit", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }

        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            //    Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                File f = new File(fileUri.getPath().toString());
                path = f.toString();
                Imagefilename = f.getName();
                lin_img.setVisibility(View.VISIBLE);
                img1.setImageBitmap(photo);
                SimpleDateFormat dff = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String date = dff.format(new Date());
                String remark = "Send " + Imagefilename + " to server having upload time" + date;
                String url = "";


                CreateOfflineSaveAttachment(path, Imagefilename, WebUrlClass.ATTACHMENTFlAG, remark, LoggingTimeActivity.ActivityId);
               /* if (isnet()) {
                    new PostUploadImageMethod().execute();

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }*/

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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


        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "Audio_" + timeStamp + ".mp3");
        } else if (type == MEDIA_TYPE_VIDEO_CAPTURE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VIDEO_" + timeStamp + ".mp4");
        } else {
            return null;
        }
        return mediaFile;
    }

    private void showProgress() {
        mProgrss.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mProgrss.setVisibility(View.GONE);
    }

    private void CreateOfflineSaveAttachment(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            _flagAttachment = true;
            Toast.makeText(getApplicationContext(), "Attachment Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

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
            String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_GetOTPServer + "?MobNo=" + MobileNo + "&UserLoginId=" + LoginId + "&AppName=" + AppName;
            //UserLoginId=300207&AppName
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }


        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res.contains("#Success")) {
                String data[] = res.split("#");
                final String OPT = data[0];

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EnoSamplingScreen3.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.vwb_otp_lay, null);
                dialogBuilder.setView(dialogView);

                // set the custom dialog components - text, image and button
                final EditText textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
                Button button = (Button) dialogView.findViewById(R.id.txt_submit);
                Button txt_resend_otp = (Button) dialogView.findViewById(R.id.txt_resend_otp);
                // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
                dialogBuilder.setCancelable(false);
                final AlertDialog b = dialogBuilder.create();
                b.show();
                // if button is clicked, close the custom dialog
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String entrotp = textotp.getText().toString().trim();
                        if (!(entrotp.equals(""))) {
                            if (entrotp.equalsIgnoreCase(OPT)) {
                                b.dismiss();
                                //CreateJson();
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid OTP!!! try again", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter OTP", Toast.LENGTH_LONG).show();
                        }
                    }
                });
/*
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.dismiss();
                    }
                });
*/

                txt_resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {

                                    // new DownloadAuthenticate().execute();

                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        } else {
                            Toast.makeText(EnoSamplingScreen3.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                        }


                    }
                });

            } else if (res.contains("User Not Found")) {

                Toast.makeText(getApplicationContext(), "Please Enter Register Mobile Number", Toast.LENGTH_LONG).show();
            } else if (res.contains("UserId and Password not found in ERPModuleSetUp")) {

                Toast.makeText(getApplicationContext(), "OTP service is not registered ", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "temporarily unavailable service!!! Please try after some time..", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void getCurrentLocationNew() {


        gps = new GPSTracker(EnoSamplingScreen3.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(),
                        Locale.getDefault());

                List<Address> addressList = geocoder.getFromLocation(latitude,
                        longitude, 1);
                //  System.out.println(latitude + " lat " + longitude + " long");
                if (addressList != null && addressList.size() > 0) {
                    Address address1 = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address1.getMaxAddressLineIndex(); i++) {
                        if (i == 0) {
                            sb.append(address1.getAddressLine(0));
                        } else {
                            sb.append("," + address1.getAddressLine(i));
                        }

                    }
                  /*  String data = address.getAdminArea();
                    YourCityName = address.getLocality();
                    result = sb.toString();
                    txtMyLocation.setText(result);
                    //  btnSE.setVisibility(View.VISIBLE);
                    imggps.setVisibility(View.VISIBLE);*/

                    String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addressList.get(0).getLocality();
                    String state = addressList.get(0).getAdminArea();
                    String country = addressList.get(0).getCountryName();
                    String postalCode = addressList.get(0).getPostalCode();
                    // String knownName = addressList.get(0).getFeatureName();
                    LocationName = address + " , " + city + " , " + state + "," + country + " . " + postalCode;

                }
            } catch (IOException e) {
                result = "Location not Found";
                Log.e("test", "Unable connect to Geocoder", e);
                if (isnet()) {

                    try {
                        final String[] loc = new String[1];
                        final Thread t = new Thread() {

                            public void run() {
                                try {
                                    String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + latitude + "," + longitude + "&sensor=true";
                                    Object res = ut.OpenConnection(url, getApplicationContext());
                                    if (res == null) {

                                    } else {
                                        String response = res.toString();
                                        JSONObject jsonObj = null;
                                        jsonObj = new JSONObject(response);
                                        String Status1 = jsonObj.getString("status");
                                        if (Status1.equalsIgnoreCase("OK")) {
                                            JSONArray Results = jsonObj.getJSONArray("results");
                                            int cnt = Results.length();
                                            if (cnt > 1) {
                                                JSONObject zero2 = Results.getJSONObject(1);
                                                result = zero2.getString("formatted_address");

                                            } else {
                                                JSONObject zero2 = Results.getJSONObject(0);
                                                result = zero2.getString("formatted_address");

                                            }

                                        } else {

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
                    LocationName = result;

                } else {
                    //  ut.displayToast(getApplicationContext(), "No Internet Connection");
                }

            }

        } else {

            gps.showSettingsAlert();
        }

    }


    void sendTimesheet(String desc) {


        Date now = new Date(); // java.util.Date, NOT java.sql.Date
        // or
        // java.sql.Timestamp!

        String format2 = new SimpleDateFormat("dd-MMM-yy ")
                .format(now);

        if (LoggingTimeActivity.IsDelayedActivityAllowed.equalsIgnoreCase("")) {
            String PromiseDate = "1";
            String finaloutcome = "1";
            String ReasonTransfer = "1";
            String UserMasterId = "1";

            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            Calendar c = Calendar.getInstance();
            System.out.println(dateFormat.format(c.getTime()));
            String currentTime = dateFormat.format(c.getTime());

           /* Intent theintent = new Intent(EnoSamplingScreen3.this,
                    SendTimeSheet.class);
            String isComp = String.valueOf("true");
            theintent.putExtra("mob", MobileNo);
            theintent.putExtra("url", CompanyURL);
            theintent.putExtra("curDate", format2);
            theintent.putExtra("fromTime", LoggingTimeActivity.Starttime);
            theintent.putExtra("totime", currentTime);
            theintent.putExtra("desc", desc);
            theintent.putExtra("actid", LoggingTimeActivity.ActivityId);
            theintent.putExtra("pid", LoggingTimeActivity.ProjectId);
            theintent.putExtra("isc", isComp);
            theintent.putExtra("PromiseDate", PromiseDate);
            theintent.putExtra("finaloutcome", finaloutcome);
            theintent.putExtra("ReasonTransfer", ReasonTransfer);
            theintent.putExtra("transfertoid", UserMasterId);
            startService(theintent);*/


            String remark = "Add Timesheet of  " + LoggingTimeActivity.ActivityName + " From time " + LoggingTimeActivity.Starttime + " to " + currentTime;
            String url = null;
            Boolean aBoolean = true;
            String SaveChecked1 = aBoolean.toString();
            String a = format2;
            String a1 = SaveChecked1;
            String a2 = LoggingTimeActivity.Starttime;
            String a3 = LoggingTimeActivity.ActivityId;
            String a4 = currentTime;
            String a5 = desc;

            try {
                url = CompanyURL + WebUrlClass.api_getInsertTimesheet + "?forDate=" + URLEncoder.encode(format2, "UTF-8") + "&SaveChecked="
                        + URLEncoder.encode(SaveChecked1, "UTF-8") + "&fromTime=" + URLEncoder.encode(LoggingTimeActivity.Starttime, "UTF-8") + "&ActivityId=" + URLEncoder.encode(LoggingTimeActivity.ActivityId, "UTF-8") +
                        "&toTime=" + URLEncoder.encode(currentTime, "UTF-8") + "&workDesc=" + URLEncoder.encode(desc, "UTF-8") + "&Timehrs=" + 0 + "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String op = "1";

            CreateOfflineModeTimesheet(url, null, WebUrlClass.GETFlAG, remark, LoggingTimeActivity.ActivityId);


        } else {
            if (LoggingTimeActivity.compare_date(LoggingTimeActivity.FormatEndDt)) {

                String PromiseDate = "1";
                String finaloutcome = "1";
                String ReasonTransfer = "1";
                String UserMasterId = "1";

                DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                Calendar c = Calendar.getInstance();
                System.out.println(dateFormat.format(c.getTime()));
                String currentTime = dateFormat.format(c.getTime());

                Intent theintent = new Intent(
                        EnoSamplingScreen3.this,
                        SendTimeSheet.class);
                String isComp = String.valueOf("true");
                theintent.putExtra("mob", MobileNo);
                theintent.putExtra("url", CompanyURL);
                theintent.putExtra("curDate", format2);
                theintent.putExtra("fromTime", LoggingTimeActivity.Starttime);
                theintent.putExtra("totime", currentTime);
                theintent.putExtra("desc", desc);
                theintent.putExtra("actid", LoggingTimeActivity.ActivityId);
                theintent.putExtra("pid", LoggingTimeActivity.ProjectId);
                theintent.putExtra("isc", isComp);
                theintent.putExtra("PromiseDate", PromiseDate);
                theintent.putExtra("finaloutcome", finaloutcome);
                theintent
                        .putExtra("ReasonTransfer", ReasonTransfer);
                theintent.putExtra("transfertoid", UserMasterId);
                startService(theintent);


                String remark = "Add Timesheet of  " + LoggingTimeActivity.ActivityName + " From time " + LoggingTimeActivity.Starttime + " to " + currentTime;
                String url = null;
                Boolean aBoolean = true;
                String SaveChecked1 = aBoolean.toString();
                try {
                    url = CompanyURL + WebUrlClass.api_getInsertTimesheet + "?forDate=" + URLEncoder.encode(format2, "UTF-8") + "&SaveChecked="
                            + URLEncoder.encode(SaveChecked1, "UTF-8") + "&fromTime=" + URLEncoder.encode(LoggingTimeActivity.Starttime, "UTF-8") + "&ActivityId=" + URLEncoder.encode(LoggingTimeActivity.ActivityId, "UTF-8") +
                            "&toTime=" + URLEncoder.encode(currentTime, "UTF-8") + "&workDesc=" + URLEncoder.encode(desc, "UTF-8") + "&Timehrs=" + 0 + "";
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String op = "1";

                CreateOfflineModeTimesheet(url, null, WebUrlClass.GETFlAG, remark, op);

               /* SharedPreferences.Editor editor = AtendanceSheredPreferance
                        .edit();
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                editor.commit();*/


            } else {

                SharedPreferences.Editor editor = AtendanceSheredPreferance
                        .edit();
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
                editor.commit();
                LoggingTimeActivity.showCustomMessageDialog(
                        "You can not fill this activity as activity is overdue.",
                        "Alert!!", EnoSamplingScreen3.this);
            }
        }
    }

    private void CreateJson() {
        EnoSamplingScreen1.enoFinalList.get(0).setUsermasterid(UserMasterId);
        EnoSamplingScreen1.enoFinalList.get(0).setLatitude_start(LoggingTimeActivity.latitude + "");
        EnoSamplingScreen1.enoFinalList.get(0).setLongitude_start(LoggingTimeActivity.longitude + "");
        EnoSamplingScreen1.enoFinalList.get(0).setAdress_start(LoggingTimeActivity.LocationName);
        EnoSamplingScreen1.enoFinalList.get(0).setVillagecode(LoggingTimeActivity.client_GeoLocation);
        EnoSamplingScreen1.enoFinalList.get(0).setIsVerified(LoggingTimeActivity.str_attaindenceVerification);


        EnoSamplingScreen1.enoFinalList.get(0).setLatitude_end(latitude + "");
        EnoSamplingScreen1.enoFinalList.get(0).setLongitude_end(longitude + "");
        EnoSamplingScreen1.enoFinalList.get(0).setAdress_end(LocationName);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ActivityId", EnoSamplingScreen1.enoFinalList.get(0).getActivityId());
            jsonObject.put("Usermasterid", EnoSamplingScreen1.enoFinalList.get(0).getUsermasterid());
            jsonObject.put("villagecode", EnoSamplingScreen1.enoFinalList.get(0).getVillagecode());
            jsonObject.put("Latitude_start", EnoSamplingScreen1.enoFinalList.get(0).getLatitude_start());
            jsonObject.put("longitude_start", EnoSamplingScreen1.enoFinalList.get(0).getLongitude_start());
            jsonObject.put("adress_start", EnoSamplingScreen1.enoFinalList.get(0).getAdress_start());
            jsonObject.put("territory_RSR", EnoSamplingScreen1.enoFinalList.get(0).getTerritory_RSR());
            jsonObject.put("territory_RSR_Mob", EnoSamplingScreen1.enoFinalList.get(0).getTerritory_RSR_Mob());
            jsonObject.put("village_RSD", EnoSamplingScreen1.enoFinalList.get(0).getVillage_RSD());
            jsonObject.put("village_RSD_mobile", EnoSamplingScreen1.enoFinalList.get(0).getVillage_RSD_mobile());
            jsonObject.put("TotalQuantityET1", EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityET1());
            jsonObject.put("sampleunitET1", EnoSamplingScreen1.enoFinalList.get(0).getSampleunitET1());
            jsonObject.put("saleUnitET1", EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitET1());
            jsonObject.put("TotalQuantityET2", EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityET2());
            jsonObject.put("sampleunitET2", EnoSamplingScreen1.enoFinalList.get(0).getSampleunitET2());
            jsonObject.put("saleUnitET2", EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitET2());
            jsonObject.put("TotalQuantityMS", EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityMS());
            jsonObject.put("sampleunitMS", EnoSamplingScreen1.enoFinalList.get(0).getSampleunitMS());
            jsonObject.put("saleUnitMS", EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitMS());
            jsonObject.put("TotalQuantityHB", EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityHB());
            jsonObject.put("sampleunitHB", EnoSamplingScreen1.enoFinalList.get(0).getSampleunitHB());
            jsonObject.put("saleUnitHB", EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitHB());
            jsonObject.put("Latitude_end", EnoSamplingScreen1.enoFinalList.get(0).getLatitude_end());
            jsonObject.put("longitude_end", EnoSamplingScreen1.enoFinalList.get(0).getLatitude_end());
            jsonObject.put("adress_end", EnoSamplingScreen1.enoFinalList.get(0).getAdress_end());
            jsonObject.put("isVerified", EnoSamplingScreen1.enoFinalList.get(0).getIsVerified());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsondata = jsonObject.toString();

        String data = EnoSamplingScreen1.enoFinalList.get(0).getActivityId() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getUsermasterid() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getVillagecode() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getLatitude_start() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getLongitude_start() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getAdress_start() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTerritory_RSR() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTerritory_RSR_Mob() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getVillage_RSD() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getVillage_RSD_mobile() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityET1() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSampleunitET1() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitET1() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityET2() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSampleunitET2() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitET2() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityMS() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSampleunitMS() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitMS() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getTotalQuantityHB() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSampleunitHB() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getSaleUnitHB() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getLatitude_end() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getLatitude_end() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getAdress_end() + "@##@" +
                EnoSamplingScreen1.enoFinalList.get(0).getIsVerified();

        sendTimesheet(data);
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


    private void CreateOfflineModeTimesheet(final String url, final String parameter,
                                            final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            EnoSamplingScreen1.enoFinalList.clear();
            startService(intent1);
            SQLiteDatabase sql = db.getWritableDatabase();

            Cursor c2 = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
            c2.getCount();
            Log.e("ActivityCountc1 :", "" + c2.getCount());

            String ActivityID = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            Log.e("Activity :", "" + ActivityID);

            String Ad = LoggingTimeActivity.ActivityId;
            sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityID});
            sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityID});

            Cursor c1 = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
            c1.getCount();
            Log.e("ActivityCountc2 :", "" + c1.getCount());

/*
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            String ActivityID1 = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            Log.e("ActivityID1 :", "" + ActivityID1);

            SharedPreferences.Editor editor = AtendanceSheredPreferance
                    .edit();
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
            editor.commit();

            String ActivityID2 = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            Log.e("ActivityID2 :", "" + ActivityID2);


            Intent i = new Intent(EnoSamplingScreen3.this, com.vritti.vwb.vworkbench.ActivityMain.class);
            // i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(getApplicationContext(), "Data not saved", Toast.LENGTH_LONG).show();
        }

    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, String> {

        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        protected String doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(path);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_FileUpload;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", path);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + path + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {

                    EnoSamplingScreen3.this.runOnUiThread(new Runnable() {
                        public void run() {
                            showProgress();
                            Toast.makeText(EnoSamplingScreen3.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            //attachment_name.setText(Imagefilename);

                            // jsonArray.put(Imagefilename);
                            try {
                                jsonimage.put("File", Imagefilename);
                                jsonArray.put(jsonimage);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    });


                } else {

                    if (serverResponseMessage.contains("Error")) {
                        EnoSamplingScreen3.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(EnoSamplingScreen3.this, "Server Error", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;

        }

        protected void onPostExecute(String feed) {
            String Vendordata = "";
            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                JSONArray Idjsonarray = new JSONArray();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", LoggingTimeActivity.ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isnet()) {
                    new SaveAttachment().execute(Vendordata);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }

        }
    }

    class SaveAttachment extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();

            if (response != null) {
                if (response.equalsIgnoreCase("true")) {
                    _flagAttachment = true;

                    Toast.makeText(EnoSamplingScreen3.this, "Attachment save successfully", Toast.LENGTH_SHORT).show();
                    //  onBackPressed();
                } else {
                    Toast.makeText(EnoSamplingScreen3.this, "Please try again", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(EnoSamplingScreen3.this, "Please try again", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0],EnoSamplingScreen3.this);
                response = res.toString();
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
