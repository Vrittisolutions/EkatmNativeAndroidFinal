package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Chetana
 */
public class GetAddressActivity extends AppCompatActivity {
    private Context parent;
    String homeaddress = "No address found", officeaddress = "No address found",
            currentaddress = "No address found",
            otheraddress = "No address found";

    LinearLayout layout_address_container;
   // ProgressHUD progress;
    private DatabaseHandlers databaseHelper;
    Address addressbean;
    ArrayList<Address> addressArrayList;
    public String restoredMobile, usertype, userid;
    SharedPreferences sharedpreferences;
    Toolbar toolbar;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_getaddress);

        parent = GetAddressActivity.this;

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("My Cart");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initViews();

        if (tcf.getAddress(AnyMartData.MOBILE) > 0) {
            getDataFromDatabase();
        } else {
            getDataFromServer();
        }
    }

    private void initViews() {
        addressArrayList = new ArrayList<Address>();

        layout_address_container = (LinearLayout) findViewById(R.id.layout_address_container);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(GetAddressActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);

        //sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES, Context.MODE_PRIVATE);
       // restoredMobile = sharedpreferences.getString("Mobileno", null);
       // usertype = sharedpreferences.getString("usertype", null);
        userid = /*sharedpreferences.getString("userid", null);*/UserMasterId;
    }

    private void getDataFromDatabase() {
        addressArrayList.clear();

        Cursor cursor = sql_db.rawQuery("Select distinct PermanentAddress,OfficeAddress" +
                ",GpsLocationAddress,CurrentAddress from "
                + databaseHelper.TABLE_MY_ADDRESS +
                " where Mobile =" + AnyMartData.MOBILE
                , null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                addressbean = new Address();
                String permanentAddress = cursor.getString(cursor
                        .getColumnIndex("PermanentAddress"));
                String officeaddress1 = cursor.getString(cursor
                        .getColumnIndex("OfficeAddress"));
                String gpslocationaddress = cursor.getString(cursor
                        .getColumnIndex("GpsLocationAddress"));
                String currentaddress1 = cursor.getString(cursor
                        .getColumnIndex("CurrentAddress"));

                if (!permanentAddress.equals("N")) {
                    homeaddress = permanentAddress;
                    addressbean.setP_address(homeaddress);
                    addressbean.setType("Home");
                } else if (!officeaddress1.equals("N")) {
                    officeaddress = officeaddress1;
                    addressbean.setType("Office");
                    addressbean.setP_address(officeaddress);
                } else if (!gpslocationaddress.equals("N")) {
                    otheraddress = gpslocationaddress;
                    addressbean.setType("Other");
                    addressbean.setP_address(otheraddress);
                } else if (!currentaddress1.equals("N")) {
                    currentaddress = currentaddress1;
                    addressbean.setType("Current");
                    addressbean.setP_address(currentaddress);
                }
                addressArrayList.add(addressbean);

            } while (cursor.moveToNext());
          //  db.close();
          //  db1.close();

        }
        layout_address_container.removeAllViews();
        showlist_new();
    }

    private void showlist_new() {
        for (int i = 0; i < addressArrayList.size(); i++) {
            addview(i);
        }
    }

    private void addview(int i) {
        // int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_get_address,
                null);

        final TextView txt_loc_add_address = (TextView) baseView.findViewById(R.id.txt_loc_add_address);

        final LinearLayout selecttextlayout = (LinearLayout)baseView.findViewById(R.id.selecttextlayout);

        final  LinearLayout selectbtn = (LinearLayout)baseView.findViewById(R.id.selectbtn);
        selectbtn.setVisibility(View.GONE);

       // Button type = (Button) baseView.findViewById(R.id.type);
        //type.setVisibility(View.GONE);
        Button selectAddress = (Button) baseView.findViewById(R.id.selectAddress);

        txt_loc_add_address.setText(addressArrayList.get(i).getP_address());

        //type.setText(addressArrayList.get(i).getType());
        selectAddress.setTag(layout_address_container.getChildCount());

        selecttextlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(txt_loc_add_address.getText().toString().equalsIgnoreCase("")) &&
                        !(txt_loc_add_address.getText().toString() == null)
                        && !(txt_loc_add_address.getText().toString().equalsIgnoreCase("No address found"))) {

                    Intent intent = new Intent();
                    intent.putExtra("Address", txt_loc_add_address.getText().toString());
             //       overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    setResult(CheckoutActivity.Address, intent);
                    finish();
                } else {
                    Toast.makeText(parent, "Please Select address",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

        selectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(txt_loc_add_address.getText().toString().equalsIgnoreCase("")) &&
                        !(txt_loc_add_address.getText().toString() == null)
                        && !(txt_loc_add_address.getText().toString().equalsIgnoreCase("No address found"))) {

                    Intent intent = new Intent();
                    intent.putExtra("Address", txt_loc_add_address.getText().toString());
               //     overridePendingTransition(R.anim.enter_right_to_left, R.anim.exit_left_to_right);
                    setResult(CheckoutActivity.Address, intent);
                    finish();
                } else {
                    Toast.makeText(parent, "Please Select address",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        layout_address_container.addView(baseView);
    }

    private void getDataFromServer() {
        if (NetworkUtils.isNetworkAvailable(parent)) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetAddress().execute();
            } else {
                new StartSession_tbuds(GetAddressActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetAddress().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
            Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

    class GetAddress extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        String resp_address = "";
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
           //progress = ProgressHUD.show(getApplicationContext(),"Loading...", false, true, null);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_getAddress = AnyMartData.MAIN_URL + AnyMartData.METHOD_GET_ALL_ADDRESS +
                    "?mobileno="+ AnyMartData.MOBILE +
                    "&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID ;

            String url1 = url_getAddress;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_getAddress, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
               /* URL urlGETAddress = new URL(url_getAddress);
                URL address = urlGETAddress;
                urlConnection = urlGETAddress.openConnection();
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.setRequestProperty("Accept","JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"utf-8"),8);
                StringBuffer stringBuff_getAddress = new StringBuffer();
                String line;
                StringBuilder str_addUser = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_getAddress = stringBuff_getAddress.append(line);
                }
*/
                responseString = res.toString().replaceAll("^\"|\"$","")+
                        ","+params[0]+","+params[1];

                resp_address = responseString.replaceAll("\\\\","");
                System.out.println("resp ="+resp_address);
            } catch (Exception e) {
                resp_address = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            //progress.dismiss();
            if (resp_address.equalsIgnoreCase("Session Expired")) {
                if (NetworkUtils.isNetworkAvailable(parent)) {
                    new StartSession_tbuds(parent, new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new GetAddress().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }

            } else if (!resp_address.equalsIgnoreCase("error")) {

                parseJson(resp_address);
            } else {
                Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    protected void parseJson(String json) {
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString(
                        "PermanentAddress").equalsIgnoreCase("N")
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("PermanentAddress").equalsIgnoreCase(""))
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("PermanentAddress").equalsIgnoreCase("0"))) {
                    homeaddress = "No address found";

                             } else {

                    homeaddress = jsonArray.getJSONObject(i).getString(
                            "PermanentAddress");
                    if (tcf.getAddress_home("Home") > 0) {
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put("UserId", userid);
                        cv.put("PermanentAddress", homeaddress);

                        sql_db.update(databaseHelper.TABLE_MY_ADDRESS,
                                cv, "Mobile=? and  type=?",
                                new String[]{restoredMobile, "Home"});
                    } else {
                        tcf.addAddress(AnyMartData.USER_ID,
                                AnyMartData.MOBILE,
                                homeaddress
                                , "N", "N", "N", "N", "N", "Home");
                    }
                }

                if (jsonArray.getJSONObject(i).getString(
                        "OfficeAddress").equalsIgnoreCase("N")
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("OfficeAddress").equalsIgnoreCase(""))
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("OfficeAddress").equalsIgnoreCase("0"))) {
                    officeaddress = "No address found";


                } else {

                    officeaddress = jsonArray.getJSONObject(i).getString(
                            "OfficeAddress");
                    if (tcf.getAddress_office("Office") > 0) {

                        ContentValues cv = new ContentValues();
                        cv.put("UserId", userid);
                        cv.put("OfficeAddress",officeaddress);
                        cv.put("type", "Office");
                        sql_db.update(databaseHelper.TABLE_MY_ADDRESS,
                                cv, "Mobile=? and  type=?",
                                new String[]{restoredMobile, "Office"});
                    } else {
                        tcf.addAddress(AnyMartData.USER_ID,
                                AnyMartData.MOBILE,
                                "N", "N", "N", "N", "N", officeaddress, "Office");
                    }
                }
                if (jsonArray.getJSONObject(i).getString(
                        "CurrentAddress").equalsIgnoreCase("N")
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("CurrentAddress").equalsIgnoreCase(""))
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("CurrentAddress").equalsIgnoreCase("0"))) {
                    currentaddress = "No address found";

                } else {

                    currentaddress = jsonArray.getJSONObject(i).getString(
                            "CurrentAddress");
                    if (tcf.getAddress_current("Current") > 0) {

                        ContentValues cv = new ContentValues();
                        cv.put("UserId", userid);
                        cv.put("CurrentAddress",currentaddress);
                        cv.put("type", "Current");

                        sql_db.update(databaseHelper.TABLE_MY_ADDRESS,
                                cv, "Mobile=? and type=?",
                                new String[]{restoredMobile, "Current"});
                    } else {
                        tcf.addAddress(AnyMartData.USER_ID,
                                AnyMartData.MOBILE,
                                "N", "N", "N", currentaddress, "N", "N", "Current");
                    }

                }
                if (jsonArray.getJSONObject(i).getString(
                        "GpsLocationAddress").equalsIgnoreCase("N")
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("GpsLocationAddress").equalsIgnoreCase(""))
                        ||
                        (jsonArray.getJSONObject(i)
                                .getString("GpsLocationAddress").equalsIgnoreCase("0"))) {
                    otheraddress = "No address found";

                } else {

                    otheraddress = jsonArray.getJSONObject(i).getString(
                            "GpsLocationAddress");
                    if (tcf.getAddress_other("Other") > 0) {
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put("UserId", userid);
                        cv.put("GpsLocationAddress", otheraddress);
                        cv.put("type", "Other");

                        sql_db.update(databaseHelper.TABLE_MY_ADDRESS,
                                cv, "Mobile=? and type=?",
                                new String[]{restoredMobile, "Other"});
                    } else {
                        tcf.addAddress(AnyMartData.USER_ID,
                                AnyMartData.MOBILE,
                                "N", otheraddress, "N", "N", "N", "N", "Other");
                    }

                }

                getDataFromDatabase();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.refresh_cart:

                getDataFromServer();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

    public static class Address {
        String P_address;
        String type;

        public String getP_address() {
            return P_address;
        }

        public void setP_address(String p_address) {
            P_address = p_address;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}