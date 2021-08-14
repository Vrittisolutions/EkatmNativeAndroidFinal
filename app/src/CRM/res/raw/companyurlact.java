package com.vworkbench;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Other.CommonClass;
import com.Services.FusedLocationTracker;
import com.classes.WebUrlClass;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

import vworkbench7.vritti.com.vworkbench7.R;

import static com.google.ads.AdRequest.LOGTAG;

/**
 * Created by 300151 on 10/10/2016.
 */
public class CompanyURLActivity extends AppCompatActivity {
    final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    EditText ed_company_url;
    Button btn_submit;
    SharedPreferences userpreferences;
    String CompanyURL;
    private ProgressDialog progressDialog;
    private DownloadGetEnvJSON getEnvJSON;
    private CommonClass cc;
    ProgressBar mprogress;
    String App_version,App_Version_No;
    CheckBox mSecure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_url);
        //  regservicenonGPS();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);

        cc = new CommonClass();

        InitView();
        setListner();
        Intent intent=getIntent();
        App_version=intent.getStringExtra("version");
        App_Version_No=intent.getStringExtra("app_version");
    }


    public void InitView() {
        ed_company_url = (EditText) findViewById(R.id.ed_company_url);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        mprogress = (ProgressBar) findViewById(R.id.progresslog1);
        mSecure = (CheckBox) findViewById(R.id.checkBoxsecure);
    }

    public void setListner() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cc.checkNet(getApplicationContext())) {
                   if (isGooglePlayServicesAvailable()) {
                        int PERMISSION_ALL = 1;
                        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS
                                , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS};
                        if (!hasPermissions(CompanyURLActivity.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(CompanyURLActivity.this, PERMISSIONS, PERMISSION_ALL);
                        } else {
                            String txt = ed_company_url.getText().toString();
                            String extn ;
                            Boolean flaBoolean = mSecure.isChecked();
                            if(mSecure.isChecked()){
                                extn = "https://";
                            }else {
                                extn = "http://";
                            }

                            CompanyURL = extn + ed_company_url.getText().toString().toLowerCase().trim();
                            if (!txt.equalsIgnoreCase("") && txt != null) {
                                mprogress.setVisibility(View.VISIBLE);
                                getEnvJSON = new DownloadGetEnvJSON();
                                getEnvJSON.execute(CompanyURL);
                            } else {
                                cc.displayToast(getApplicationContext(), "Enter URL");
                            }
                        }
                    }


                } else {
                    cc.displayToast(getApplicationContext(), "No Internet Connection");
                }

            }
        });



    }


    private class DownloadGetEnvJSON extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0] + WebUrlClass.api_getEnv;
            //String url ="http://qm.vritti.co.in:420/VrittiQM.asmx/MissCallSMS?u=AE1001&p=vritti123&m=8600669097";
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.Errormsg;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            // progressDialog.dismiss();
            //  dismissProgressDialog();
            // progressDialog.dismiss();
            mprogress.setVisibility(View.INVISIBLE);
            if (res.contains("AppEnvMasterId")) {
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString("CompanyURL", CompanyURL);
                editor.commit();
                Intent intent = new Intent(CompanyURLActivity.this, MainActivity.class);
                intent.putExtra("version",App_version);
                intent.putExtra("app_version",App_Version_No);
                startActivity(intent);
                finish();
            } else {
                cc.displayToast(getApplicationContext(), "Enter Valid URL");
            }
        }
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);


        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
                GooglePlayServicesUtil.getErrorDialog(status, this,
                        ConnectionResult.SUCCESS).show();
                Toast.makeText(getApplicationContext(), "Google play services are not available please update it first before Login", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported for Location services", Toast.LENGTH_LONG)
                        .show();
                // finish();
            }

            return false;
        }
    }
    private boolean checkGooglePlayServicesAvailable() {
        final int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            return true;
        }

        Log.e(LOGTAG, "Google Play Services not available: " + GooglePlayServicesUtil.getErrorString(status));

        if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            final Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(status, this, 1);
            if (errorDialog != null) {
                errorDialog.show();
            }
        }

        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                break;

            default:
                break;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS, Manifest.permission.CAMERA};


}


