package com.vritti.ekatm.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.splunk.mint.Mint;
import com.vritti.AlfaLavaModule.PI.PacketScanDetails;
import com.vritti.AlfaLavaModule.activity.AlfaHomePage;
import com.vritti.SaharaModule.SplashActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.Interface.CallBack;
import com.vritti.ekatm.R;
import com.vritti.ekatm.bean.CountryModel;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.ekatm.other.ValidateUser;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.WelcomeScreenActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import me.leolin.shortcutbadger.ShortcutBadger;


public class ActivityLogIn extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // DownloadAuthenticate();

    public static final String APP_URL_SAHARA = "http://e207.ekatm.com";
    //public static final String APP_URL_SAHARA = "http://education.talukahaveli.in";
    public static final String APP_URL_HAJMOLA = "http://hajmola.ekatm.com";

    String CompanyURL = "";

    // public static final String APP_URL_SAHARA = "http://education.talukahaveli.in";

    String PlantMasterId = "", LoginId = "", Password = "", EnvMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    public static Context context;
    Button btnLogin;
    Boolean IsSessionActivate, IsValidUser;
    Spinner edEnv, edPlant/*,spinnerCountry*/;
    String IsCrmUser;
    private ProgressDialog progressDialog;
    EditText edLoginId, edPassword, edmob/*,selectCountry*/;
    public static EditText textotp;
    public static Intent igpsalarm;
    String App_version;
    private Button BtnOk;
    private LinearLayout lin_compcode, lin_login;
    private CheckBox mCbShowPwd;
    CheckBox mSecure;
    EditText mEturl;
    private static int REQUEST_CODE = 12;
    private static final int PERMISSION_REQUEST_CODE = 1;
    int PERMISSION_ALL = 1;

    SharedPreferences sharedpreferences;
    public static SharedPreferences userpreferences;

    private RelativeLayout relProgress;
    CommonFunction cf;
    private boolean isFirstImage = true;
    private volatile static int numThread = 1;
    private volatile static int threadAllowedToRun = 1;
    private int myThreadID;
    private volatile static Object myLock = new Object();
    private volatile static boolean hasNotSlept = true;
    public volatile static boolean fixForMatch = false;
    GoogleApiClient googleApiClient = null;

//    private String APP_URL_Alfa = "http://alfatest.ekatm.co.in";


    private String APP_URL_Alfa = "http://10.128.72.105";

    private FirebaseAuth mAuth;
    private String mVerificationId;

    ImageView img_back;
    TextView txt_title;
    String[] PERMISSIONS;
    private AlertDialog dialog;
    private int cnt;
    EditText input_company;

    static String settingKey = "";
    private SQLiteDatabase sql;
    private List<CountryModel> countryNamesListModels;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        setToolbar();
        InitView();
        setListner();

        // App_version=intent.getStringExtra("version");
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        db = new DatabaseHandlers(context);
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        ShortcutBadger.with(getApplicationContext()).remove();
        SharedPreferences.Editor editor = userpreferences.edit();
        editor.remove(WebUrlClass.USERINFO_SHORTCUTADGER_COUNT);
        editor.commit();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (ut.isNet(getApplicationContext())) {
            versionCheck();
        }
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
*/


        if (googleApiClient == null) {


            if (ut.isNet(getApplicationContext())) {
                if(Constants.type == Constants.Type.Alfa){

                }else {
                    //getpermissiondialog();
                    EnableGPSAutoMatically();
                }
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }


            Intent i = getIntent();
            if (!(i.hasExtra(WebUrlClass.INTENT_LOGIN_SCREEN_BACKFLAG))) {
               /* if (getLogINCount() && !ut.IsChangePassword(context)) {
                    Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                    startActivity(intent);
                    finish();
                }*/ if (getLogINCount() ) {

                    context = ActivityLogIn.this;
                    ut = new Utility();
                    settingKey = ut.getSharedPreference_SettingKey(context);
                    String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
                    db = new DatabaseHandlers(context, dabasename);
                    sql = db.getWritableDatabase();
                    CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
                    EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);

                    if(Constants.type == Constants.Type.Sahara) {
                        startActivity(new Intent(ActivityLogIn.this, SplashActivity.class));
                        finish();

                    }
                    else{
                        Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                        startActivity(intent);
                        finish();
                    }
                    if(Constants.type == Constants.Type.Alfa)
                    {

                        Intent intent = new Intent(ActivityLogIn.this, AlfaHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        if (EnvMasterId.equalsIgnoreCase("dabur")||EnvMasterId.equalsIgnoreCase("sharthtest")){
                            Intent intent = new Intent(ActivityLogIn.this, WelcomeScreenActivity.class);
                            startActivity(intent);
                            finish();

                        }else {
                            Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }

            }


        } else {
            Intent i = getIntent();
            if (!(i.hasExtra(WebUrlClass.INTENT_LOGIN_SCREEN_BACKFLAG))) {
                if (getLogINCount()) {
                    Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                    startActivity(intent);
                    finish();
                }
            }
        }


        if(Constants.type == Constants.Type.Alfa){
            lin_login.setVisibility(View.VISIBLE);
            lin_compcode.setVisibility(View.GONE);
            methodEnvirnment();

            /*if (ut.isNet(getApplicationContext())) {
                if (isGooglePlayServicesAvailable()) {
                    if (googleApiClient == null) {
                        EnableGPSAutoMatically();
                    } else {
                        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE};
                               *//* Manifest.permission.RECEIVE_SMS
                                        , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,*//*
                        if (!hasPermissions(ActivityLogIn.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(ActivityLogIn.this, PERMISSIONS, PERMISSION_ALL);
                        } else {
                            BtnOk.setEnabled(false);

                        }

                    }
                }


            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
       */
        }


        if(Constants.type == Constants.Type.Sahara) {
            lin_login.setVisibility(View.VISIBLE);
            lin_compcode.setVisibility(View.GONE);
            if (ut.isNet(getApplicationContext())) {
                if (isGooglePlayServicesAvailable()) {
                    if (googleApiClient == null) {
                        EnableGPSAutoMatically();
                    } else {
                        String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,
                                Manifest.permission.READ_CONTACTS,
                        };
                               /* Manifest.permission.RECEIVE_SMS
                                        , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,*/
                        if (!hasPermissions(ActivityLogIn.this, PERMISSIONS)) {
                            ActivityCompat.requestPermissions(ActivityLogIn.this, PERMISSIONS, PERMISSION_ALL);
                        } else {
                            BtnOk.setEnabled(false);
                            methodEnvirnment();
                        }
                       /* String[] PERMISSIONS = {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE};
                               *//* Manifest.permission.RECEIVE_SMS
                                        , Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS,*//*
                            if (!hasPermissions(ActivityLogIn.this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(ActivityLogIn.this, PERMISSIONS, PERMISSION_ALL);
                            } else {
                                BtnOk.setEnabled(false);
                                methodEnvirnment();
                            }*/

                    }
                }


            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
        }

        //    sendVerificationCode("+918600669097");
    }

    private void getpermissiondialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = ActivityLogIn.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.location_permission_dialog, null);
        builder.setView(myView);

        TextView btn_accept=myView.findViewById(R.id.btn_accept);
        TextView btn_deny=myView.findViewById(R.id.btn_deny);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog = builder.create();
        dialog.show();

    }

    private void setListner() {
        AppCommon.getInstance(ActivityLogIn.this).onHideKeyBoard(ActivityLogIn.this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()) {
                    LoginId = edLoginId.getText().toString();
                    Password = edPassword.getText().toString();
                    MobileNo = edmob.getText().toString();

                    final String strUserID = LoginId;
                    final String strpsw = Password;
                    if (ut.isNet(getApplicationContext())) {
                        btnLogin.setEnabled(false);
                        ut.showProgress(relProgress);
                        new ValidateUser(EnvMasterId, LoginId, strpsw, PlantMasterId, getApplicationContext(), new CallBack() {
                            @Override
                            public void onCall() {
                                Register register = new Register();
                                register.execute("", EnvMasterId, strUserID, strpsw, MobileNo, PlantMasterId);

                            }

                            @Override
                            public void failCall(String msg) {
                                MySnackbar(msg);
                                btnLogin.setEnabled(true);

                                ut.hideProgress(relProgress);
                            }
                        });
                    } else {
                        MySnackbar("No Internet Connection");
                    }
                }
            }
        });

        mCbShowPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
        BtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCommon.getInstance(ActivityLogIn.this).onHideKeyBoard(ActivityLogIn.this);

                /*if(Constants.type == Constants.Type.Vwb) {

                    lin_login.setVisibility(View.VISIBLE);
                    lin_compcode.setVisibility(View.GONE);
                    methodEnvirnment();

                }else {
*/
                if (ut.isNet(getApplicationContext())) {

                    if (isGooglePlayServicesAvailable()) {
                        if (googleApiClient == null) {
                            EnableGPSAutoMatically();
                        } else {
                            if (checkEditTextUrl()) {
                  /*              if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                    String[] PERMISSIONS = {
                                            Manifest.permission.ACCESS_COARSE_LOCATION,
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.INTERNET,
                                            Manifest.permission.READ_CONTACTS,
                                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                                     };
                                    if (!hasPermissions(ActivityLogIn.this, PERMISSIONS)) {
                                        try {
                                            ActivityCompat.requestPermissions(ActivityLogIn.this,PERMISSIONS, PERMISSION_ALL);
                                        }catch (Exception e){
                                            e.printStackTrace();
                                            Log.d("Excep :",e.getMessage());
                                        }
                                    } else {
                                        BtnOk.setEnabled(false);
                                        methodEnvirnment();
                                    }
                                } else {*/

                                if(Constants.type == Constants.Type.PM){
                                    PERMISSIONS =
                                            new String[]{
                                            
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                                    Manifest.permission.INTERNET,
                                            };
                                }else {
                                     PERMISSIONS =
                                             new String[]{
                                                     Manifest.permission.ACCESS_COARSE_LOCATION,
                                                     Manifest.permission.ACCESS_FINE_LOCATION,
                                                     Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                     Manifest.permission.INTERNET,
                                                     Manifest.permission.READ_CONTACTS,
                                             };
                                }
                                    if (!hasPermissions(ActivityLogIn.this, PERMISSIONS)) {
                                        ActivityCompat.requestPermissions(ActivityLogIn.this, PERMISSIONS, PERMISSION_ALL);
                                    } else {
                                        BtnOk.setEnabled(false);
                                        methodEnvirnment();
                                    }
                                }
                            }


                    }


                } else {
                    ut.displayToast(getApplicationContext(), "No Internet Connection");
                }
            }
        });

        edPlant.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String PlantName = edPlant.getSelectedItem().toString();
                PlantMasterId = getPlantID(PlantName);
                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_PlantID_KEY, PlantMasterId);
                editor.putString(WebUrlClass.MyPREFERENCES_PlantName_KEY, PlantName);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String countryName = spinnerCountry.getSelectedItem().toString();
                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.SELECTED_COUNTRY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.SELECTED_COUNTRY_NAME, countryName);
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        edEnv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                EnvMasterId = edEnv.getSelectedItem().toString();
                if (!(EnvMasterId.equalsIgnoreCase(getResources().getString(R.string.instruction_Spinner_Change_Company)))) {
                    SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(WebUrlClass.MyPREFERENCES_EnvMasterID_KEY, EnvMasterId);
                    editor.commit();
                    if (Constants.type == Constants.Type.PM) {
                        GetEnvpm envpm = new GetEnvpm();
                        envpm.execute(EnvMasterId);
                    }
                    DownloadPlantsJSON plants = new DownloadPlantsJSON();
                    plants.execute(EnvMasterId);
                } else {
                    mEturl.setText("");
                    ut.hideProgress(relProgress);
                    lin_login.setVisibility(View.GONE);
                    lin_compcode.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void methodEnvirnment() {

        String extn;
        if (Constants.type == Constants.Type.CRM) {


        }else if (Constants.type == Constants.Type.Sahara) {

            CompanyURL = APP_URL_SAHARA;
            GetEnv getEnv = new GetEnv();
            getEnv.execute(CompanyURL);

        } else if (Constants.type == Constants.Type.Vwb) {

            Boolean flaBoolean = mSecure.isChecked();

            if (mSecure.isChecked()) {
                extn = "https://";
            } else {
                if (input_company.getText().toString().toLowerCase().trim().contains("vritti") || input_company.getText().toString().toLowerCase().trim().contains("mmpl") || input_company.getText().toString().toLowerCase().trim().contains("vst"))
                    extn = "https://";
                else
                    extn = "http://";
            }

            if (input_company.getText().toString().equalsIgnoreCase("") || input_company.getText().toString().equalsIgnoreCase("null")
                    || input_company.getText().toString()==null){

                Toast.makeText(ActivityLogIn.this,"Enter company name",Toast.LENGTH_SHORT).show();
            }
            else if (mEturl.getText().toString().equalsIgnoreCase("")||
                    mEturl.getText().toString().equalsIgnoreCase("null")||
                    mEturl.getText().toString()==null){
                Toast.makeText(ActivityLogIn.this,"Enter company code",Toast.LENGTH_SHORT).show();
            }
            else {


                String FinalURL = input_company.getText().toString().toLowerCase().trim() + mEturl.getText().toString().toLowerCase().trim();
                //  CompanyURL = extn + mEturl.getText().toString().toLowerCase().trim();
                CompanyURL = extn + FinalURL;
                GetEnv getEnv = new GetEnv();
                getEnv.execute(CompanyURL);
            }

           /* CompanyURL = extn + mEturl.getText().toString().toLowerCase().trim();
            GetEnv getEnv = new GetEnv();
            getEnv.execute(CompanyURL);
*/
        } else if (Constants.type == Constants.Type.PM) {
             mEturl.setHint("Enter Company Code");
            String compcode = mEturl.getText().toString().toLowerCase().trim();
            compcode = compcode.replaceAll("\\s", "");
            String strcomp = compcode.trim();//

            CheckEnv checkEnv = new CheckEnv();
            checkEnv.execute(strcomp);

        } else if (Constants.type == Constants.Type.Delivery) {

            Boolean flaBoolean = mSecure.isChecked();
            if (mSecure.isChecked()) {
                extn = "https://";
            } else {
                if (mEturl.getText().toString().toLowerCase().trim().contains("vritti") || mEturl.getText().toString().toLowerCase().trim().contains("mmpl") || mEturl.getText().toString().toLowerCase().trim().contains("vst"))
                    extn = "https://";
                else
                    extn = "http://";
            }

            CompanyURL = extn + mEturl.getText().toString().toLowerCase().trim();
            GetEnv getEnv = new GetEnv();
            getEnv.execute(CompanyURL);

        }else if (Constants.type == Constants.Type.Alfa) {

            CompanyURL = APP_URL_Alfa;
            GetEnv getEnv = new GetEnv();
            getEnv.execute(CompanyURL);

        }else {

            Boolean flaBoolean = mSecure.isChecked();
            if (mSecure.isChecked()) {
                extn = "https://";
            } else {
                if (mEturl.getText().toString().toLowerCase().trim().contains("vritti")||mEturl.getText().toString().toLowerCase().trim().contains("mmpl")||mEturl.getText().toString().toLowerCase().trim().contains("vst"))
                    extn = "https://";
                else
                    extn = "http://";
            }

            if (input_company.getText().toString().equalsIgnoreCase("") || input_company.getText().toString().equalsIgnoreCase("null")
                    || input_company.getText().toString()==null){

                Toast.makeText(ActivityLogIn.this,"Enter company name",Toast.LENGTH_SHORT).show();
            }
            else if (mEturl.getText().toString().equalsIgnoreCase("")||
                        mEturl.getText().toString().equalsIgnoreCase("null")||
                        mEturl.getText().toString()==null){
                Toast.makeText(ActivityLogIn.this,"Enter company code",Toast.LENGTH_SHORT).show();
            }
            else {


                String FinalURL = input_company.getText().toString().toLowerCase().trim() + mEturl.getText().toString().toLowerCase().trim();
                //  CompanyURL = extn + mEturl.getText().toString().toLowerCase().trim();
                CompanyURL = extn + FinalURL;
                GetEnv getEnv = new GetEnv();
                getEnv.execute(CompanyURL);
            }
        }
    }

    private void InitView() {
        countryNamesListModels=new ArrayList<>();
        edEnv = (Spinner) findViewById(R.id.edEnv);
        edPlant = (Spinner) findViewById(R.id.edPlant);
        //spinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        //selectCountry = findViewById(R.id.selectCountry);
        ((Spinner) findViewById(R.id.edEnv)).setSelection(0);
        ((Spinner) findViewById(R.id.edPlant)).setSelection(0);
        edLoginId = (EditText) findViewById(R.id.edLoginId);
        edPassword = (EditText) findViewById(R.id.edPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        edmob = (EditText) findViewById(R.id.edmob);
        lin_compcode = (LinearLayout) findViewById(R.id.lin_compcode);
        lin_login = (LinearLayout) findViewById(R.id.lin_login);
        BtnOk = (Button) findViewById(R.id.BtnOK);
        mCbShowPwd = (CheckBox) findViewById(R.id.checkBox);
        mSecure = (CheckBox) findViewById(R.id.checkBoxsecure);
        mEturl = (EditText) findViewById(R.id.input_Url);
        input_company = (EditText) findViewById(R.id.input_company);
        if (Constants.type == Constants.Type.Vwb) {

            mSecure.setVisibility(View.VISIBLE);
            mCbShowPwd.setChecked(true);
        } else if (Constants.type == Constants.Type.PM) {
            input_company.setVisibility(View.GONE);
                mEturl.setHint("Enter Company Code");
            mSecure.setVisibility(View.GONE);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input_company, InputMethodManager.SHOW_IMPLICIT);
        input_company.requestFocus();
        relProgress = (RelativeLayout) findViewById(R.id.rel_progress);


        CountryModel countryModel  = new CountryModel();
        countryModel.setCountryId("1");
        countryModel.setCountryName("India");
        countryNamesListModels.add(countryModel);
        CountryModel countryModel1  = new CountryModel();
        countryModel1.setCountryId("2");
        countryModel1.setCountryName("Nepal");
        countryNamesListModels.add(countryModel1);

        //PM - Latest Release 22 march 2022
        /*selectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogIn.this,CountryListViewActivity.class);
                intent.putExtra("CountryList", (Serializable) countryNamesListModels);
                startActivityForResult(intent,900);
            }
        });*/

       /* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, countryNamesList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(dataAdapter);*/


    }



    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);

        txt_title.setText(R.string.app_name_toolbar_Ekatm);

        if(Constants.type == Constants.Type.PM)
            img_back.setImageDrawable(getResources().getDrawable(R.drawable.simplify));
        else
        img_back.setImageDrawable(getResources().getDrawable(R.drawable.app_logo_1));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        setSupportActionBar(toolbar);
        Mint.initAndStartSession(this.getApplication(), "4cf63235");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private boolean getLogINCount() {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOGIN_SETTING, null);
        int cnt = c.getCount();
        if (cnt > 0) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("NewApi")


    private Boolean checkEditText() {
        if (EnvMasterId.equalsIgnoreCase("Environment ID")) {
            ut.displayToast(getApplicationContext(), "Please Select Environment ID");
            return false;
        } else if (PlantMasterId.equalsIgnoreCase("Plant ID")) {
            ut.displayToast(getApplicationContext(), "Please Select Plant ID");
            return false;
        } else if (!(edLoginId.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter User ID");
            return false;
        } else if (!(edPassword.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter Password");
            return false;
        } else if (!(edmob.getText().toString().length() > 0)) {
            ut.displayToast(getApplicationContext(), "Please Enter Register Mobile No.");
            return false;
        } else if (!(edmob.getText().toString().length() > 9)) {
            ut.displayToast(getApplicationContext(), "Please Enter valid Mobile No.");
            return false;
        }

        return true;
    }

    private void UpdateSpinner(String companyName) {
        List<String> list = new ArrayList<String>();
        list.add(companyName);
        list.add(getResources().getString(R.string.instruction_Spinner_Change_Company));
        // list = GetEnvData();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        edEnv.setAdapter(dataAdapter);
    }

    public class Register extends AsyncTask<String, String, List<String>> {
        @Override
        protected List<String> doInBackground(String... params) {
            Object res;
            String response;
            String a = ut.getSharedPreference_URL(context);
            String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_GetSessions + "?AppEnvMasterId="
                    + params[1] + "&UserLoginId=" + params[2] +
                    "&UserPwd=" + params[3] + "&PlantId=" + params[5] + "";
            try {
                url = url.replaceAll(" ", "%20");
                res = Utility.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            List<String> result = new ArrayList<String>();
            result.add(response);
            result.add(params[1]);
            result.add(params[2]);
            result.add(params[3]);
            result.add(params[4]);
            result.add(params[5]);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            String s2 = s.get(0);
            String envId = s.get(1);
            String logInId = s.get(2);
            String userPsw = s.get(3);
            String mobile = s.get(4);
            String PlantId = s.get(5);

            if (s2.equalsIgnoreCase(WebUrlClass.setError)) {
                btnLogin.setEnabled(true);
                MySnackbar("Server not responding...Please try after Sometime...");
                ut.hideProgress(relProgress);
            } else if (s2.contains("true")) {
                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_LOGIN_KEY, logInId);
                editor.putString(WebUrlClass.MyPREFERENCES_PSW_KEY, userPsw);
                editor.putString(WebUrlClass.MyPREFERENCES_MOBILE_KEY, mobile);
                editor.commit();
                //new DownloadAuthenticate().execute();
                new DownloadUserMasterIdFromServer().execute();

            } else {
                btnLogin.setEnabled(true);
                ut.hideProgress(relProgress);
                MySnackbar("Failed to Start Session");
            }
        }
    }

    private JSONObject getJobject() {
        JSONObject data = null;
        try {
            FirebaseInstanceId.getInstance().getToken();
            data = new JSONObject();
            String Token = sharedpreferences.getString(WebUrlClass.MyPREFERENCES_FIREBASE_TOKEN_KEY, null);
            data.put("App_Name", SetAppName.AppNameFCM);
            data.put("UserMasterId", ut.getSharedPreference_UserMasterID(getApplicationContext()));
            data.put("DeviceId", Token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    class UploadFCMDetail extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = ut.getSharedPreference_URL(context) + WebUrlClass.FCMurl;
            try {
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            new DownloadIfCRMUserJson().execute();

            if (integer.equalsIgnoreCase("Fail")) {
                Log.d("FCM registration Status", "Fail");
            } else if (integer.equalsIgnoreCase("Error")) {
                ut.displayToast(getApplicationContext(), "FCM Registration Failed");
                Log.d("FCM registration Status", "Error");

            } else if (integer.equalsIgnoreCase("Success")) {

                Log.d("FCM registration Status", "Success");
                // ut.displayToast(getApplicationContext(), "FCM Registration Failed");
            }
        }
    }

    class DownloadIfCRMUserJson extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = null;
            try {
                url = ut.getSharedPreference_URL(context) + WebUrlClass.api_getIfCRMUser + "?UserMstrId=" + URLEncoder.encode
                        (ut.getSharedPreference_UserMasterID(getApplicationContext()), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ut.displayToast(getApplicationContext(), "Unsupported Encoding Exception occurred");
            }

            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                res = res.replaceAll(" ", "");
                IsCrmUser = res;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            ut.hideProgress(relProgress);

            if (getLogINCountgps()) {
                // regservicenonGPS(getApplicationContext());
            }
            if (/*EnvMasterId.contains("c207")||*/EnvMasterId.contains("hyva")) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_IS_CRMUSER_KEY, IsCrmUser);
                editor.commit();

                cnt = insertLoginData();
                editor.putInt(WebUrlClass.MyPREFERENCES_SETTING_POSITION_KEY, cnt - 1);
                editor.commit();
                btnLogin.setEnabled(true);
                MySnackbar("Login Successful...");

                if (ut.isNet(ActivityLogIn.this)) {

                    new StartSession(ActivityLogIn.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDeviceAuthentication().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(ActivityLogIn.this, msg);
                        }
                    });
                } else {
                    ut.displayToast(ActivityLogIn.this, "No Internet connection");
                }
            }else {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_IS_CRMUSER_KEY, IsCrmUser);
                editor.commit();

                cnt = insertLoginData();
                editor.putInt(WebUrlClass.MyPREFERENCES_SETTING_POSITION_KEY, cnt - 1);
                editor.commit();
                btnLogin.setEnabled(true);
                MySnackbar("Login Successful...");

                if (EnvMasterId.equalsIgnoreCase("dabur")||EnvMasterId.equalsIgnoreCase("sharthtest")){
                    Intent intent = new Intent(ActivityLogIn.this, WelcomeScreenActivity.class);
                    startActivity(intent);
                    finish();

                }else if (Constants.type == Constants.Type.Sahara) {
                    Intent intent = new Intent(ActivityLogIn.this, ActivityMain.class);
                    //  intent.putExtra("cnt", cnt);
                    startActivity(intent);
                    finish();

                } else if (CompanyURL.equals(APP_URL_HAJMOLA)) {
                    Intent intent = new Intent(ActivityLogIn.this, WelcomeScreenActivity.class);
                    // intent.putExtra("cnt", cnt);
                    startActivity(intent);
                    finish();

                } else {
                    if (Constants.type == Constants.Type.Alfa) {
                        Intent intent = new Intent(ActivityLogIn.this, AlfaHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("cnt", cnt);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                        intent.putExtra("cnt", cnt);
                        startActivity(intent);
                        finish();
                    }
                }

            }


        }
    }


    class DownloadUserMasterIdFromServer extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_GetUserMasterIdAndroid;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                res = res.substring(1, res.length() - 1);
                // UserMasterId = res;

            } catch (Exception e) {
                res = WebUrlClass.setError;
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            String s = integer;
            if (s.contains("UserMasterI")) {//UserMasterId
                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                String UsermasterID = "", UserName = "", LogINID = "",Designation="";
                if (s.contains("UserMasterId##")) {
                    String data[] = res.split("##");
                    String us1 = data[0];
                    UsermasterID = data[1];
                } else {
                    try {
                        JSONArray jResults = new JSONArray(s);
                        for (int index = 0; index < jResults.length(); index++) {
                            JSONObject jorder = jResults.getJSONObject(index);
                            UsermasterID = jorder.getString("UserMasterID");
                            UserName = jorder.getString("UserName");
                            LogINID = jorder.getString("LoginID");
                            Designation = jorder.getString("Designation");


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_USERMASTER_ID_KEY, UsermasterID);
                editor.putString(WebUrlClass.MyPREFERENCES_USERNAME_KEY, UserName);
                editor.putString(WebUrlClass.MyPREFERENCES_Designation_KEY, Designation);
                editor.commit();

                if (!getLogINCount()) {
                    JSONObject Jobj = getJobject();
                    String jobject = Jobj.toString().replaceAll("\\\\", "");
                    new UploadFCMDetail().execute(jobject);

                    //  regservicenonGPS(getApplicationContext());

                } else {
                    new DownloadIfCRMUserJson().execute();
                }

            } else {
                btnLogin.setEnabled(true);
                ut.hideProgress(relProgress);
                Toast.makeText(getApplicationContext(), "Can not find out user", Toast.LENGTH_LONG).show();
            }


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

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityLogIn.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.vwb_otp_lay, null);
                dialogBuilder.setView(dialogView);

                // set the custom dialog components - text, image and button
                textotp = (EditText) dialogView.findViewById(R.id.edt_otp);
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
                                //Toast.makeText(getApplicationContext(), "OTP s", Toast.LENGTH_LONG).show();
                                new DownloadUserMasterIdFromServer().execute();

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

                        MobileNo = edmob.getText().toString();
                        LoginId = edLoginId.getText().toString();

                        new DownloadAuthenticate().execute();

                    }
                });

            } else if (res.contains("User Not Found")) {
                btnLogin.setEnabled(true);
                ut.hideProgress(relProgress);
                Toast.makeText(getApplicationContext(), "Please Enter Register Mobile Number", Toast.LENGTH_LONG).show();
            } else if (res.contains("UserId and Password not found in ERPModuleSetUp")) {
                btnLogin.setEnabled(true);
                ut.hideProgress(relProgress);
                Toast.makeText(getApplicationContext(), "OTP service is not registered ", Toast.LENGTH_LONG).show();
            } else {
                btnLogin.setEnabled(true);
                ut.hideProgress(relProgress);
                Toast.makeText(getApplicationContext(), "temporarily unavailable service!!! Please try after some time..", Toast.LENGTH_LONG).show();
            }

        }
    }

    class DownloadPlantsJSON extends AsyncTask<String, String, String> {
        String res;
        List plantsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = ut.getSharedPreference_URL(context) + WebUrlClass.api_getPlants + "?AppEnvMasterId=" +
                        URLEncoder.encode(EnvMasterId, "UTF-8") + "&PlantId=";
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                //  res = res.replaceAll("\\\"", "");
                //  res = res.replaceAll("", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);
                SQLiteDatabase sql = db.getWritableDatabase();
                sql.delete(db.TABLE_PLANTMASTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PLANTMASTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                ContentValues values = new ContentValues();
                plantsName = new ArrayList();
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (columnName.equalsIgnoreCase("PlantMasterId")) {

                        } else {
                            plantsName.add(jorder.getString("PlantName"));
                        }
                    }
                    long a = sql.insert(db.TABLE_PLANTMASTER, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            ut.hideProgress(relProgress);
            if (integer.contains("PlantMasterId")) {
                Collections.sort(plantsName);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, plantsName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edPlant.setAdapter(dataAdapter);
            } else {

                ut.displayToast(getApplicationContext(), "Couldn't Connect to the Server");
            }
        }

    }

    class GetEnv extends AsyncTask<String, Void, List<String>> {
        String res;
        List<String> EnvName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ut.showProgress(relProgress);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            String url = params[0] + WebUrlClass.api_getEnv;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();

                res = e.getMessage();
            }

            List<String> result = new ArrayList<>();
            result.add(res);
            result.add(params[0]);
            return result;

        }

        @Override
        protected void onPostExecute(List<String> str) {
            super.onPostExecute(str);

            BtnOk.setEnabled(true);
            String s1 = str.get(1);
            String s2 = str.get(0);
            ut.hideProgress(relProgress);

            //  Toast.makeText(ActivityLogIn.this,res +"--"+CompanyURL,Toast.LENGTH_SHORT).show();

            if (res.contains("AppEnvMasterId")) {


                lin_compcode.setVisibility(View.GONE);
                lin_login.setVisibility(View.VISIBLE);
                /// URL Sharedpreference

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_URL_KEY, s1);

                try {
                    JSONArray jResults = new JSONArray(res);

                    EnvName = new ArrayList<String>();
                    for (int index = 0; index < jResults.length(); index++) {

                        JSONObject jorder = jResults.getJSONObject(index);
                        EnvName.add(jorder.getString("AppEnvMasterId"));
                        String isChatApplicable = jorder.getString("IsChatApplicable");
                        String isGPSLocation = jorder.getString("IsGPSLocation");
                        String AppCode = jorder.getString("AppCode");
                        editor.putString(WebUrlClass.MyPREFERENCES_IS_CHAT_APPLICABLE_KEY, isChatApplicable);
                        editor.putString(WebUrlClass.MyPREFERENCES_IS_GPS_LOCATION_KEY, isGPSLocation);
                        editor.putString(WebUrlClass.MyPREFERENCES_IS_APPCODE, AppCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
                EnvName.add(getResources().getString(R.string.instruction_Spinner_Change_Company));
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityLogIn.this, android.R.layout.simple_spinner_item, EnvName);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                edEnv.setAdapter(dataAdapter);
            } else {
               // mEturl.setText("");
                lin_compcode.setVisibility(View.VISIBLE);
                lin_login.setVisibility(View.GONE);
                //ut.displayToast(getApplicationContext(), "Enter valid Url");
                MySnackbar("Enter valid URL");
            }
        }
    }

    class GetEnvpm extends AsyncTask<String, Void, List<String>> {
        String res;
        List<String> EnvName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ut.showProgress(relProgress);
        }

        @Override
        protected List<String> doInBackground(String... params) {
            String url = ut.getSharedPreference_URL(getApplicationContext()) + WebUrlClass.api_getEnvPM + "?AppEnvMasterId=" + params[0];
            url = url.replaceAll(" ", "%20");
            try {

                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.setError;
            }
            List<String> result = new ArrayList<>();
            result.add(res);
            result.add(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> str) {
            super.onPostExecute(str);


            String s1 = str.get(1);
            String s2 = str.get(0);

            if (res.contains("AppEnvMasterId")) {
                lin_compcode.setVisibility(View.GONE);
                lin_login.setVisibility(View.VISIBLE);
                /// URL Sharedpreference


                try {
                    JSONArray jResults = new JSONArray(res);
                    EnvName = new ArrayList<String>();
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        EnvName.add(jorder.getString("AppEnvMasterId"));
                        String isChatApplicable = jorder.getString("IsChatApplicable");
                        String isGPSLocation = jorder.getString("IsGPSLocation");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(WebUrlClass.MyPREFERENCES_IS_CHAT_APPLICABLE_KEY, isChatApplicable);
                        editor.putString(WebUrlClass.MyPREFERENCES_IS_GPS_LOCATION_KEY, isGPSLocation);
                        editor.commit();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class CheckEnv extends AsyncTask<String, String, List<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ut.showProgress(relProgress);
        }

        @Override
        protected List<String> doInBackground(String... params) {

            SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.SELECTED_COUNTRY, MODE_PRIVATE);
            String countryName = sharedpreferences.getString(WebUrlClass.SELECTED_COUNTRY_NAME,"");

            String url;


            if(countryName.equalsIgnoreCase("India")){
              url = WebUrlClass.APP_URL_PM + WebUrlClass.api_checkEnv + "?AppEnvMasterId=" + params[0];
            }else{
                url = WebUrlClass.APP_URL_PM_NEPAL + WebUrlClass.api_checkEnv + "?AppEnvMasterId=" + params[0];
            }

            try {
                res = Utility.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            List<String> result = new ArrayList<>();
            result.add(response);
            result.add(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<String> s) {
            super.onPostExecute(s);
            ut.hideProgress(relProgress);
            BtnOk.setEnabled(true);
            String s1 = s.get(1);
            String s2 = s.get(0);
            if (s2.contains("True")) {

                lin_compcode.setVisibility(View.GONE);
                lin_login.setVisibility(View.VISIBLE);
                /// URL Sharedpreference

                SharedPreferences sharedpreferencesN = getSharedPreferences(WebUrlClass.SELECTED_COUNTRY, MODE_PRIVATE);
                String countryName = sharedpreferencesN.getString(WebUrlClass.SELECTED_COUNTRY_NAME,"");

                SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if(countryName.equalsIgnoreCase("India")){
                    editor.putString(WebUrlClass.MyPREFERENCES_URL_KEY, WebUrlClass.APP_URL_PM);
                }else{
                    editor.putString(WebUrlClass.MyPREFERENCES_URL_KEY, WebUrlClass.APP_URL_PM_NEPAL);
                }

                editor.apply();

                UpdateSpinner(s1);
            } else {
                mEturl.setText("");
                lin_compcode.setVisibility(View.VISIBLE);
                lin_login.setVisibility(View.GONE);
                MySnackbar(getResources().getString(R.string.error_code_company_valid));
            }
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, ActivityLogIn.this, 0).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private Boolean checkEditTextUrl() {
        if (!(mEturl.getText().toString().length() > 0)) {
            MySnackbar(getResources().getString(R.string.error_code_company));
            return false;
        }
        return true;
    }

    private void MySnackbar(String display) {
        Snackbar.make(findViewById(R.id.asd), display, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText( CompanyURLActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
            }
        }).show();
    }

    private boolean getLogINCountgps() {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOGIN_SETTING, null);
        int cnt = c.getCount();
        if (cnt == 1) {
            return true;
        } else {
            return false;
        }
    }

    private int insertLoginData() {
        String imei_number = "";
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            imei_number = telephonyManager.getDeviceId();
            imei_number = telephonyManager.getDeviceId();

        } catch (SecurityException e) {
            imei_number = "0";
        }
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ContentValues contentValues = new ContentValues();
        String uniqueID = UUID.randomUUID().toString();
        contentValues.put("LogInKey", uniqueID);
        contentValues.put("CompanyURL", ut.getSharedPreference_URL(getApplicationContext()));
        contentValues.put("EnvId", ut.getSharedPreference_EnvMasterID(getApplicationContext()));
        contentValues.put("PlantID", ut.getSharedPreference_PlantID(getApplicationContext()));
        contentValues.put("PlantName", ut.getSharedPreference_plantName(getApplicationContext()));
        contentValues.put("UserLogInId", ut.getSharedPreference_UserloginID(getApplicationContext()));
        contentValues.put("UserMasterId", ut.getSharedPreference_UserMasterID(getApplicationContext()));
        contentValues.put("Password", ut.getSharedPreference_Psw(getApplicationContext()));
        contentValues.put("Mobile", ut.getSharedPreference_Mobile(getApplicationContext()));
        contentValues.put("UserName", ut.getSharedPreference_getUsername(getApplicationContext()));
        contentValues.put("IsCRMuser", ut.getSharedPreference_isCRMUser(getApplicationContext()));
        contentValues.put("IsChatApplicable", ut.getSharedPreference_isChatApplicable(getApplicationContext()));
        contentValues.put("IsGpsLocation", ut.getSharedPreference_isGPSLocation(getApplicationContext()));
        contentValues.put("BackDateTimesheet", "");
        contentValues.put("AndroidId", android_id);
        contentValues.put("IMEINumber", imei_number);
        contentValues.put("FCMToken", ut.getSharedPreference_getFCMToken(getApplicationContext()));
        contentValues.put("Designation", ut.getSharedPreference_Designation(getApplicationContext()));
        contentValues.put("Material", "");

        final int min = 1000;
        final int max = 9999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        contentValues.put("DatabaseName", random + ".dbo");
        SQLiteDatabase sql = db.getWritableDatabase();
        long e = sql.insert(db.TABLE_LOGIN_SETTING, null, contentValues);
        Log.e("", "Setting table value : " + e);
        int i = (int) e;
        SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(WebUrlClass.MyPREFERENCES_SETTING_KEY, uniqueID);
        editor.commit();
        return i;

    }

    private String getPlantID(String plantName) {
        String data = "";
        SQLiteDatabase Sql = db.getWritableDatabase();
        Cursor c = Sql.rawQuery("Select * from " + db.TABLE_PLANTMASTER + " where PlantName='" + plantName + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("PlantMasterId"));
            } while (c.moveToNext());
        }
        c.close();
        // Sql.close();

        return data;
    }

    public void regservicenonGPS(Context mcontext) {

       /* Intent serviceIntent = new Intent(mcontext, PaidLocationFusedLocationTracker1.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            ContextCompat.startForegroundService(this, serviceIntent);
        } else {
            mcontext.startService(serviceIntent);
        }
*/
         /* int itime;
         itime = 15;
         long aTime = 1000 * 60 * itime;
         Intent igpsalarm = new Intent(mcontext, FusedLocationTracker.class);
         PendingIntent piHeartBeatService = PendingIntent.getService(
                 mcontext, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
         AlarmManager alarmManager = (AlarmManager) mcontext
                 .getSystemService(Context.ALARM_SERVICE);
         alarmManager.cancel(piHeartBeatService);
         alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                 System.currentTimeMillis(), aTime, piHeartBeatService);*/


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("Failed","Permission grant");
                    return false;
                }else {
                    Log.d("Failed","Permission failed");
                }
            }
        }
        return true;
    }

    private void EnableGPSAutoMatically() {
        // GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) ActivityLogIn.this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) ActivityLogIn.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            /*Intent i = getIntent();
                            if (!(i.hasExtra(WebUrlClass.INTENT_LOGIN_SCREEN_BACKFLAG))) {
                                if (getLogINCount()) {
                                    Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);

                                    startActivity(intent);
                                    finish();
                                }
                            }*/
                            //toast("Success");
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //  toast("GPS is not on");
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.

                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(ActivityLogIn.this, REQUEST_CODE);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //  toast("Setting change not allowed");
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (requestCode == PERMISSION_ALL) {
            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[5] == PackageManager.PERMISSION_GRANTED) {
                    BtnOk.setEnabled(false);
                    methodEnvirnment();
                }
            }else {*/

            if(Constants.type == Constants.Type.PM){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED )
                       /* grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED)*/ {
                    BtnOk.setEnabled(false);
                    methodEnvirnment();
                }
                }else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[4] == PackageManager.PERMISSION_GRANTED) {
                    BtnOk.setEnabled(false);
                    methodEnvirnment();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //PM - Release 21 - March - 2022
       /* if (requestCode == 900 && resultCode == Activity.RESULT_OK){
            String result=data.getStringExtra("CountrySelected");
            Log.e("CountrySelected",result);
            selectCountry.setText(result);
            SharedPreferences sharedpreferences = getSharedPreferences(WebUrlClass.SELECTED_COUNTRY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(WebUrlClass.SELECTED_COUNTRY_NAME, result);
            editor.apply();

        }*/

        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                System.out.println("Resultdata" + result);
                // splash();

                Intent i = getIntent();
                if (!(i.hasExtra(WebUrlClass.INTENT_LOGIN_SCREEN_BACKFLAG))) {
                    if (getLogINCount()) {
                        Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

                if(Constants.type == Constants.Type.PM){

                }else {
                   // ActivityLogIn.this.finish();
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(Constants.type == Constants.Type.Alfa) {
            lin_login.setVisibility(View.VISIBLE);
            lin_compcode.setVisibility(View.GONE);
            methodEnvirnment();
        }
    }

    private void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            //  splash();
        } catch (Exception ex) {
        }

    }


    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }

    private void versionCheck() {
        Document doc = null;
        try {
            WebUrlClass.APP_CURRENT_VERSION = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            if (Constants.type == Constants.Type.Vwb) {
                doc = Jsoup.connect("https://play.google.com/store/apps/details?id="//com.stavigilmonitoring
                        + "vworkbench7.vritti.com.vworkbench7").get();

            } else if (Constants.type == Constants.Type.PM) {
                doc = Jsoup.connect("https://play.google.com/store/apps/details?id="//com.stavigilmonitoring
                        + "practice.vritti.com").get();
            }
            String AllStr = doc.text();
            String parts[] = AllStr.split("Current Version");
            String newparts[] = parts[1].split("Requires Android");
            //String divleftpart[] = ne
            WebUrlClass.APP_NEW_VERSION = newparts[0].trim();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (WebUrlClass.APP_NEW_VERSION != "" && !WebUrlClass.APP_NEW_VERSION.isEmpty()) {
            if (value(WebUrlClass.APP_CURRENT_VERSION) < value(WebUrlClass.APP_NEW_VERSION) || value(WebUrlClass.APP_CURRENT_VERSION) > value(WebUrlClass.APP_NEW_VERSION)) {
                final String Update_Message = "A new version " + WebUrlClass.APP_NEW_VERSION + " of application is now available on playstore";
                new Thread() {
                    public void run() {
                        ActivityLogIn.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(ActivityLogIn.this, Update_Message, Toast.LENGTH_LONG);
                               // TextView v = (TextView) toast.getView().findViewById(R.id.message);
                              //  if (v != null) v.setGravity(Gravity.CENTER);
                                toast.show();
                            }
                        });
                    }
                }.start();
                // Toast.makeText(SpalshActivity.this,",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /*private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                //verifying the code
                //verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ActivityLogIn.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };*/

    class GetDeviceAuthentication extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            ut.showProgress(relProgress);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = ut.getSharedPreference_URL(getApplicationContext()) + WebUrlClass.api_GetDeviceAuthentication + "?AppCode="+SetAppName.AppNameFCM;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.toString();


            } catch (Exception e) {
                e.printStackTrace();
                res = WebUrlClass.setError;
            }
            return res;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);


            if (res.contains("Device_Id")) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityLogIn.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.vwb_already_login, null);
                dialogBuilder.setView(dialogView);

                Button button = (Button) dialogView.findViewById(R.id.txt_submit);
                Button txt_resend_otp = (Button) dialogView.findViewById(R.id.txt_resend_otp);
                // TextView txt_resend_otp=dialogView.findViewById(R.id.txt_resend_otp);
                dialogBuilder.setCancelable(false);
                final AlertDialog b = dialogBuilder.create();
                b.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Constants.type == Constants.Type.Sahara) {
                            Intent intent = new Intent(ActivityLogIn.this, ActivityMain.class);
                            //  intent.putExtra("cnt", cnt);
                            startActivity(intent);
                            finish();

                        } else if (CompanyURL.equals(APP_URL_HAJMOLA)) {
                            Intent intent = new Intent(ActivityLogIn.this, WelcomeScreenActivity.class);
                            // intent.putExtra("cnt", cnt);
                            startActivity(intent);
                            finish();

                        } else {
                            if (Constants.type == Constants.Type.Alfa) {
                                Intent intent = new Intent(ActivityLogIn.this, AlfaHomePage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("cnt", cnt);
                                startActivity(intent);
                                finish();
                            } else {
                                Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                                intent.putExtra("cnt", cnt);
                                startActivity(intent);
                                finish();
                            }
                        }

                        b.dismiss();
                    }
                });

                txt_resend_otp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                      b.dismiss();
                      finish();

                    }
                });

            }else {
                if (Constants.type == Constants.Type.Sahara) {
                    Intent intent = new Intent(ActivityLogIn.this, ActivityMain.class);
                    //  intent.putExtra("cnt", cnt);
                    startActivity(intent);
                    finish();

                } else if (CompanyURL.equals(APP_URL_HAJMOLA)) {
                    Intent intent = new Intent(ActivityLogIn.this, WelcomeScreenActivity.class);
                    // intent.putExtra("cnt", cnt);
                    startActivity(intent);
                    finish();

                } else {
                    if (Constants.type == Constants.Type.Alfa) {
                        Intent intent = new Intent(ActivityLogIn.this, AlfaHomePage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("cnt", cnt);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ActivityLogIn.this, ActivityModuleSelection.class);
                        intent.putExtra("cnt", cnt);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
    }

}
