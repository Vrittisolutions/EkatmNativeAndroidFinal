package com.vritti.crmlib.vcrm7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by sharvari on 08-Feb-17.
 */

public class TravelPlanDeleteActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;

    EditText edtDate, edtcity, edtNotes;
    Button buttonDelete_travelplan, buttonClose_travelplan;
    String Travelid, dt, notes, city;
    public static Context context;
    ProgressHUD progressHUD;
    public static String  date = null, CurrentDate;
    SharedPreferences userpreferences;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy  hh:mm a");
    Date DOBDate = null;
    String UserID = "";
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_delete_travel_plan);
        context = TravelPlanDeleteActivity.this;
        Intent intent = getIntent();
        Travelid = intent.getStringExtra("PKTravelPlanId");
        dt = intent.getStringExtra("Date");
        System.out.println("Datecall :"+dt);
        notes = intent.getStringExtra("Notes");
        city = intent.getStringExtra("City");//UserId
        UserID = intent.getStringExtra("UserId");

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        init();
        setListener();
    }

    private void init() {
        edtDate = (EditText) findViewById(R.id.edtDate);
        edtcity = (EditText) findViewById(R.id.edtcity);
        edtNotes = (EditText) findViewById(R.id.edtNotes);
        buttonDelete_travelplan = (Button) findViewById(R.id.buttonDelete_travelplan);
        buttonClose_travelplan = (Button) findViewById(R.id.buttonClose_travelplan);
        progressbar= (ProgressBar) findViewById(R.id.progressbar);
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
        else {
            Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void setListener() {
        if (dt != null) {
           // dt = dt.substring(dt.indexOf("(") + 1, dt.lastIndexOf(")"));

            System.out.println("DateDelete :"+dt);
           // long DOB_date = Long.parseLong(dt);
           // DOBDate = new Date(DOB_date);
          //  String dt1 = sdf.format(dt);

Date date = null;
            try {
                SimpleDateFormat  format = new SimpleDateFormat("MM/dd/yyyy");
                date = format.parse(dt);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            String newFormat = sdf.format(date);
            System.out.println(".....Date..."+newFormat);

            edtDate.setText(newFormat);
        } else {
            edtDate.setText("");
        }

        edtcity.setText(city);
        edtNotes.setText(notes);
        buttonDelete_travelplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadTravelPlan().execute("", "");
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });

                }
            }
        });
        buttonClose_travelplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showProgressDialog() {


       // progressHUD = ProgressHUD.show(context, "", false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    class DownloadTravelPlan extends AsyncTask<String, Void, String> {
        Object res;
        String response,finaljson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected String doInBackground(String... params) {
            //FromDate: '01/01/2015', ToDate: '31/05/2015'
            try {
                JSONObject object = new JSONObject();
                object.put("PKTravelPlanId", Travelid);
                finaljson = object.toString();
                finaljson = finaljson.replaceAll("\\\\", "");
                finaljson = finaljson.replaceAll(" ", "");
                String url = CompanyURL + WebUrlClass.api_TRAVEL_PLAN_Delete;
                try {
                    res = ut.OpenPostConnection(url, finaljson);
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            Toast.makeText(TravelPlanDeleteActivity.this,"Travel plan deleted successfully",Toast.LENGTH_SHORT).show();
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, TravelPlanShowActivity.class);
        intent.putExtra("message", "msg");
        intent.putExtra("from", "");
        intent.putExtra("to", "");//
        intent.putExtra("userid", UserID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

       // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
