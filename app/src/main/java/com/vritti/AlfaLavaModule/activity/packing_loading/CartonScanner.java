package com.vritti.AlfaLavaModule.activity.packing_loading;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.PutAwayScanDetails;
import com.vritti.AlfaLavaModule.activity.ReceiptPackagingDOListActivity;
import com.vritti.AlfaLavaModule.activity.pick_riversal.PickPacketScanDetails;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartonScanner extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;

    @BindView(R.id.locationId)
    EditText locationId;

    String locationCode = "";
    private TextToSpeech t1;
    String  Location_Transfer="";
    private Handler mHandler;
    ProgressBar progress;
    private String Pack_OrdHdrId="",PackOrderNo="";
    Button btn_finish;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wms_carton_scan);
        ButterKnife.bind(this);
        pContext = CartonScanner.this;



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
        progress=findViewById(R.id.progress);
        btn_finish=findViewById(R.id.btn_finish);

        Pack_OrdHdrId=getIntent().getStringExtra("ID");
        PackOrderNo=getIntent().getStringExtra("CODE");
        getSupportActionBar().setTitle(PackOrderNo);


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
                     Log.i("id", locationId.getText().toString());

                     if (locationCode != null && !(locationCode.equals(""))) {


                         if (isnet()) {
                             progress.setVisibility(View.VISIBLE);
                             new StartSession(pContext, new CallbackInterface() {
                                 @Override
                                 public void callMethod() {
                                     DownloadCartonValidation downloadCartonValidation = new DownloadCartonValidation();
                                     downloadCartonValidation.execute();
                                 }

                                 @Override
                                 public void callfailMethod(String msg) {

                                 }


                             });

                         } else {
                             Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                         }




                 }

                    return true;
                }
                return false;
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    progress.setVisibility(View.VISIBLE);
                    new StartSession(CartonScanner.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostShipment().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }


                    });

                } else {
                    Toast.makeText(CartonScanner.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    class DownloadCartonValidation extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.CartonScaning + "?CartonCode=" + locationCode+"" +
                    "&Pack_OrdHdrId="+Pack_OrdHdrId;

            try {

                res = ut.OpenConnection(url, pContext);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);


            } catch (final Exception e) {
                response = "Error";

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CartonScanner.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                        mp.start();
                    }
                });
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            progress.setVisibility(View.GONE);
            locationId.setText("");

            if (integer.contains("Success")) {

                Toast toast = Toast.makeText(CartonScanner.this, "Data send successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();

                //onBackPressed();

            }
            else if (integer.contains("Failed")) {
                try {
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(CartonScanner.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }  else if (integer.contains("False")||integer.contains("false")) {
                try {
                    JSONObject jsonObject = new JSONObject(integer);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(CartonScanner.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
    public class PostShipment extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_AWBShipmentCreation + "?Pack_OrdHdrId=" + Pack_OrdHdrId +"&AWBNo="+ URLEncoder.encode(PackOrderNo,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url, CartonScanner.this);
                response = res.toString().replaceAll("\\\\", "");
                // response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);//GRNHeaderId
            progress.setVisibility(View.GONE);
            String s = resp;
           /* String s = String.valueOf(res);
            if (s.contains("false")) {
                Toast.makeText(DOPackingScanDetails.this, "Please check stock", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(DOPackingScanDetails.this, "Server Error....", Toast.LENGTH_LONG).show();
            }
            else if (s.contains("true")) {
                Toast.makeText(DOPackingScanDetails.this, "Record saved Successfully", Toast.LENGTH_LONG).show();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove(WebUrlClass.MyPREFERENCES_CODE);
                editor.remove("OrdNo");
                editor.remove("OrdNo");
                editor.commit();
                cf.DeleteAllRecord(db.TABLE_SECONDARY_BOX);
                secondaryBoxArrayList.clear();
                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")||EnvMasterId.equals("jaltest")||EnvMasterId.equals("jallocal")) {
//                      startActivity(new Intent(DOPackingScanDetails.this, DOPackingScanDetails.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                      finish();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    DOPackingScanDetails.this.recreate();


                }else {
                    startActivity(new Intent(DOPackingScanDetails.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                }
            }*/

            if (s.contains("Success")) {


                Toast toast = Toast.makeText(CartonScanner.this, "Shipment created Successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();


                final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.ok);
                mp.start();

                startActivity(new Intent(CartonScanner.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
            else if (s.contains("Failed")) {
                try {
                    s = s.substring(1, s.length() - 1);
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");
                    Toast toast = Toast.makeText(CartonScanner.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                    mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (s.contains("False"))
            {
                s = s.substring(1, s.length() - 1);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String status = jsonObject.getString("ERROR");

                    Toast toast = Toast.makeText(CartonScanner.this, status, Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(18);
                    toastMessage.setTextColor(Color.RED);
                    toastMessage.setGravity(Gravity.CENTER);
                    toastMessage.setCompoundDrawablePadding(5);
                    toastView.setBackgroundColor(Color.TRANSPARENT);
                    toast.show();

                    final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                    mp.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast toast = Toast.makeText(CartonScanner.this, s, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();
                final MediaPlayer mp = MediaPlayer.create(CartonScanner.this, R.raw.alert);
                mp.start();

            }
        }
    }

}
