package com.vritti.crmlib.vcrm7;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;


/**
 * Created by 300151 on 10/10/2016.
 */
public class CompanyURLActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    EditText ed_company_url;
    Button btn_submit;
    SharedPreferences userpreferences;
    String  version, Secure;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    CheckBox checkbox_secure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_company_url);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        InitView();

        setListner();
    }

    public void InitView() {
        ed_company_url = (EditText) findViewById(R.id.ed_company_url);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        checkbox_secure = (CheckBox) findViewById(R.id.checkbox_secure);
        Intent intent = getIntent();
        version = intent.getStringExtra("version");
    }

    public void setListner() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(intent);*/

                if (ed_company_url.getText().toString().trim().equalsIgnoreCase("")
                        || ed_company_url.getText().toString().trim().equalsIgnoreCase(null)) {
                    Toast.makeText(CompanyURLActivity.this, "Enter url", Toast.LENGTH_LONG).show();
                } else {
                    if (isGooglePlayServicesAvailable()) {

                        int PERMISSION_ALL = 1;
                        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS
                                , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
                        if (!hasPermissions(CompanyURLActivity.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(CompanyURLActivity.this, PERMISSIONS, PERMISSION_ALL);
                        } else {
                            if (checkbox_secure.isChecked()) {
                                CompanyURL = "https://" + ed_company_url.getText().toString().trim();
                                CompanyURL = CompanyURL.replaceAll(" ", "");
                                SharedPreferences.Editor editor = userpreferences.edit();
                                editor.putString("CompanyURL", CompanyURL);
                                editor.commit();
                                // Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class);

                                Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class).putExtra("version", version);
                                startActivity(intent);
                                CompanyURLActivity.this.finish();
                            } else {
                                CompanyURL = "http://" + ed_company_url.getText().toString().trim();
                                CompanyURL = CompanyURL.replaceAll(" ", "");
                                SharedPreferences.Editor editor = userpreferences.edit();
                                editor.putString("CompanyURL", CompanyURL);
                                editor.commit();
                                // Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class);

                                Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class).putExtra("version", version);
                                startActivity(intent);
                                CompanyURLActivity.this.finish();
                            }
                        }

                    }
                }
            }
        });
        ed_company_url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (ed_company_url.getText().toString().trim().equalsIgnoreCase("")
                            || ed_company_url.getText().toString().trim().equalsIgnoreCase(null)) {
                        Toast.makeText(CompanyURLActivity.this, "Enter url", Toast.LENGTH_LONG).show();
                    } else {

                        if (isGooglePlayServicesAvailable()) {
                            int PERMISSION_ALL = 1;
                            String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.INTERNET, Manifest.permission.RECEIVE_SMS
                                    , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS, Manifest.permission.CALL_PHONE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS};
                            if (!hasPermissions(CompanyURLActivity.this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(CompanyURLActivity.this, PERMISSIONS, PERMISSION_ALL);
                            } else {
                                InputMethodManager inputManager = (InputMethodManager)
                                        getSystemService(Context.INPUT_METHOD_SERVICE);

                                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                                if (checkbox_secure.isChecked()) {
                                    CompanyURL = "https://" + ed_company_url.getText().toString().trim();
                                    CompanyURL = CompanyURL.replaceAll(" ", "");
                                    SharedPreferences.Editor editor = userpreferences.edit();
                                    editor.putString("CompanyURL", CompanyURL);
                                    editor.commit();
                                    // Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class);

                                    Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class).putExtra("version", version);
                                    startActivity(intent);
                                    CompanyURLActivity.this.finish();
                                } else {
                                    CompanyURL = "http://" + ed_company_url.getText().toString().trim();
                                    CompanyURL = CompanyURL.replaceAll(" ", "");
                                    SharedPreferences.Editor editor = userpreferences.edit();
                                    editor.putString("CompanyURL", CompanyURL);
                                    editor.commit();
                                    Intent intent = new Intent(CompanyURLActivity.this, ActivityLogIn.class).putExtra("version", version);
                                    startActivity(intent);
                                    CompanyURLActivity.this.finish();
                                }
                            }

                        }
                    }


                }
                return false;
            }
        });

        checkbox_secure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Secure = checkbox_secure.getText().toString();

            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(CompanyURLActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
