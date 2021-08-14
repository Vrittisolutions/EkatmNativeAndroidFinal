package com.vritti.vwb.vworkbench;

import android.Manifest;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.vwb.Beans.BeanEnoSampling;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EnoSamplingScreen1 extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;
    ProgressBar mProgrss;
    TextView txtHeader;
    public static ArrayList<BeanEnoSampling> enoFinalList = new ArrayList<BeanEnoSampling>();

    Button btnVillagename, btnNext;
    ImageView img_show;
    TextView txtAttachment;
    EditText txtTerritoryRSR, txtMoileRSR, txtVillageRSD, txtMobileRSD;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    static File mediaFile;
    String Imagefilename, path;

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonimage = new JSONObject();
    private Uri fileUri;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 5;
    public static final int MEDIA_TYPE_VIDEO_CAPTURE = 22;
    static Boolean _flagAttachment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eno_sampling_screen1);
        _flagAttachment = false;

        init();

        context = EnoSamplingScreen1.this;
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
        setListner();


    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mProgrss = (ProgressBar) findViewById(R.id.toolbar_progress_logging);
        txtHeader = (TextView) findViewById(R.id.txt_header);
        btnVillagename = (Button) findViewById(R.id.btn_Village_name);
        btnNext = (Button) findViewById(R.id.btn_next);

        txtTerritoryRSR = (EditText) findViewById(R.id.txtRSR);
        txtMoileRSR = (EditText) findViewById(R.id.txtRSRMobile);
        txtVillageRSD = (EditText) findViewById(R.id.txtRSD);
        txtMobileRSD = (EditText) findViewById(R.id.txtRSDMobile);
        txtAttachment = (TextView) findViewById(R.id.scr1attachment);
        img_show = (ImageView) findViewById(R.id.imageView1);

        BeanEnoSampling bean = new BeanEnoSampling();
        bean.setActivityId(LoggingTimeActivity.ActivityId);
        bean.setUsermasterid("");
        bean.setIsVerified("");
        bean.setAdress_start("");
        bean.setAdress_end("");
        bean.setLongitude_end("");
        bean.setLatitude_end("");
        bean.setLongitude_start("");
        bean.setLatitude_start("");
        bean.setSaleUnitHB("");
        bean.setSampleunitHB("");
        bean.setTotalQuantityHB("");
        bean.setSaleUnitMS("");
        bean.setSampleunitMS("");
        bean.setTotalQuantityMS("");
        bean.setSaleUnitET1("");
        bean.setSampleunitET1("");
        bean.setTotalQuantityET1("");
        bean.setSaleUnitET2("");
        bean.setSampleunitET2("");
        bean.setTotalQuantityET2("");
        bean.setVillage_RSD("");
        bean.setTerritory_RSR_Mob("");
        bean.setVillage_RSD_mobile("");
        bean.setTerritory_RSR("");
        bean.setVillagecode("");
        enoFinalList.add(bean);

        Intent i = new Intent(EnoSamplingScreen1.this, EnoSamplingScreen2.class);
        i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_EATERY1);
        startActivity(i);
        finish();
    }

    public void setListner() {
        txtAttachment.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValide()) {
                    _flagAttachment = false;

                    enoFinalList.get(0).setTerritory_RSR(txtTerritoryRSR.getText().toString());
                    enoFinalList.get(0).setTerritory_RSR_Mob(txtMoileRSR.getText().toString());
                    enoFinalList.get(0).setVillage_RSD(txtVillageRSD.getText().toString());
                    enoFinalList.get(0).setVillage_RSD_mobile(txtMobileRSD.getText().toString());
                    Intent i = new Intent(EnoSamplingScreen1.this, EnoSamplingScreen2.class);
                    i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_EATERY1);
                    startActivity(i);
                }

            }
        });


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
            //  Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(fileUri));
                File f = new File(fileUri.getPath().toString());
                path = f.toString();
                Imagefilename = f.getName();
                img_show.setVisibility(View.VISIBLE);
                img_show.setImageBitmap(photo);
                if (isnet()) {
                    new PostUploadImageMethod().execute();

                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }

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

    private boolean isValide() {
        if (_flagAttachment.equals(false)) {
            Toast.makeText(EnoSamplingScreen1.this, "Upload selfie", Toast.LENGTH_LONG).show();
            return false;
        }else  if ((txtTerritoryRSR.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen1.this, "Enter Territory RSR", Toast.LENGTH_LONG).show();
            return false;
        }else if ((txtMoileRSR.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen1.this, "Enter RSR mobile number", Toast.LENGTH_LONG).show();
            return false;
        }else if ((txtVillageRSD.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen1.this, "Enter Village RSD", Toast.LENGTH_LONG).show();
            return false;
        }else if ((txtMobileRSD.getText().length() < 1)) {
            Toast.makeText(EnoSamplingScreen1.this, "Enter RSD mobile number", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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

                    EnoSamplingScreen1.this.runOnUiThread(new Runnable() {
                        public void run() {
                            showProgress();
                            Toast.makeText(EnoSamplingScreen1.this, "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
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
                        EnoSamplingScreen1.this.runOnUiThread(new Runnable() {
                            public void run() {
                                dismissProgress();
                                Toast.makeText(EnoSamplingScreen1.this, "Server Error", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EnoSamplingScreen1.this, "Attachment save successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EnoSamplingScreen1.this, "Please try again", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(EnoSamplingScreen1.this, "Please try again", Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0],EnoSamplingScreen1.this);
                response = res.toString();
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }
    }

    private void showProgress() {
        mProgrss.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mProgrss.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
      //  super.onBackPressed();
    }
}
