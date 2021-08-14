package com.vritti.AlfaLavaModule.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GRNScanner extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private  Context pContext;

    @BindView(R.id.locationId)
    EditText locationId;

    private static DownloadPutAwayDetails downloadPutAwayDetails;
    PutAwayDetail putAwayDetails;
    ArrayList<PutAwayDetail> putAwayDetail;
    String locationCode = "";
    private TextToSpeech t1;
    String  Location_Transfer="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_grn_scanner);
        ButterKnife.bind(this);
        pContext = GRNScanner.this;
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

        sql.delete(db.TABLE_GRN_PACKET, null, null);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                }
            }
        });


        t1.speak("Scan Location", TextToSpeech.QUEUE_FLUSH, null);

       Location_Transfer=getIntent().getStringExtra("loc_transcode");


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
                 if (Constants.type == Constants.Type.Alfa) {

                     try {

                         locationCode = new JSONObject(locationCode.replace("Info:", "")).getString("Location");
                         locationId.setText("");
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }

                     Log.i("id", locationId.getText().toString());
                     // callApi id pass
                     //      public string GetScanLocation(string LocationCode)
                     if (locationCode != null && !(locationCode.equals(""))) {


                         if (isnet()) {
                             ProgressHUD.show(pContext, "Fetching location details ...", true, false);
                             new StartSession(pContext, new CallbackInterface() {
                                 @Override
                                 public void callMethod() {
                                     downloadPutAwayDetails = new DownloadPutAwayDetails();
                                     downloadPutAwayDetails.execute();
                                 }

                                 @Override
                                 public void callfailMethod(String msg) {
                                     downloadPutAwayDetails = new DownloadPutAwayDetails();
                                     downloadPutAwayDetails.execute();
                                 }


                             });

                         } else {
                             Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                         }


                     }
                     ;
                 }else {
                     if (locationCode != null && !(locationCode.equals(""))) {


                         if (isnet()) {
                             ProgressHUD.show(pContext, "Fetching location details ...", true, false);
                             new StartSession(pContext, new CallbackInterface() {
                                 @Override
                                 public void callMethod() {
                                     downloadPutAwayDetails = new DownloadPutAwayDetails();
                                     downloadPutAwayDetails.execute();
                                 }

                                 @Override
                                 public void callfailMethod(String msg) {
                                     downloadPutAwayDetails = new DownloadPutAwayDetails();
                                     downloadPutAwayDetails.execute();
                                 }


                             });

                         } else {
                             Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                         }


                     }

                 }

                    return true;
                }
                return false;
            }
        });
    }



    public class DownloadPutAwayDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;




        @Override
        protected String doInBackground(String... params) {
            String strRes = null;
           // locationCode = "PV-H2-100";
            // String url = pUt.getSharedPreference_URL(pContext) + WebUrlClass.api_GetUserWithCount;
            //String url = pUt.getSharedPreference_URL(pContext) + WebUrlClass.api_GetUserWithCount;

            String url = CompanyURL+ WebUrlClass.GetPutAwayDetails+"?LocationCode="+locationCode;

            // String url = CompanyURL+ WebUrlClass.GetPutAwayDetails+"?LocationCode=B038";

            /*http://vistartest.ekatm.com/api/GRNSuggPutAwayApi/GetItem?id=A64D40D8-EA46-4968-948F-C93487AC0CE6*/

            try {
               res = ut.OpenConnection(url,pContext);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
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
        protected void onPostExecute(String res) {
            super.onPostExecute(res);//GRNHeaderId

            String s = res;

            if (response.equals("Error")){
                ProgressHUD.Destroy();
            }
            if(s.equals("[]")){
                ProgressHUD.Destroy();
                Toast.makeText(pContext, "Record not present", Toast.LENGTH_LONG).show();
                t1.speak("Record not present", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
            }else if(s.equals("Error")){
                ProgressHUD.Destroy();
                Toast.makeText(pContext, "Technical error....", Toast.LENGTH_LONG).show();
                t1.speak("Technical error....", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
            }
            else if(s.contains("Loction not found in data")){
                ProgressHUD.Destroy();
                Toast.makeText(pContext, "Location not found", Toast.LENGTH_LONG).show();
                t1.speak("Location not found", TextToSpeech.QUEUE_FLUSH, null);
                if (t1 != null) {
                    t1.stop();
                    t1.shutdown();
                }
            }else {
                try {
                    putAwayDetail = new ArrayList<>();

                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    Log.d("test", "jResults :=" + jResults);
                    for(int i=0;i<jResults.length();i++){

                        putAwayDetails = new PutAwayDetail();
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        putAwayDetails.setDestLocationMasterId(jsonObject.getString("LocationMasterId"));
                        putAwayDetails.setLocationCode(jsonObject.getString("LocationCode"));
                        putAwayDetails.setDestWareHouseMasterId(jsonObject.getString("WarehouseId"));
                        putAwayDetails.setLocationType(jsonObject.getString("LocationType"));

                    }
                    putAwayDetail.add(putAwayDetails);
/*[{"LocationMasterId":"0036344d-6f13-4869-a0c9-adedae1baefe",
                            "LocationCode":"PV-H2-100","WarehouseId":"1",
                            "LocationType":"Receiving Buffer"}*/
                    //Intent intent = new Intent();
                    startActivity(new Intent(GRNScanner.this, PutAwayScanDetails.class)
                            .putExtra("loc_transcode",Location_Transfer)
                    .putExtra("putArayObject" , new Gson().toJson(putAwayDetails)
                    ));


                    ProgressHUD.Destroy();

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
}
