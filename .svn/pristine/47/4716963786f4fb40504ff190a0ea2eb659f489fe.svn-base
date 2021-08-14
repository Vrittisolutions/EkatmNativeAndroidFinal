package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.GPSRecordsAdapter;
import com.vritti.vwb.Beans.GPSMyLocationBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

public class GPSMyOwnLocation extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ProgressBar mProgress;
    SharedPreferences userpreferences;
    String UserNamegps;
    private String mindATE;
    TextView mtxtsetname, mtxt_no_record;
    Button reload;
    ListView mList;
    ArrayList<GPSMyLocationBean> GPSRecods;
    DownloadGpsData_First gpsDataJSON;
    DownloadGpsData_LoadMore data_loadMore;
    private SimpleDateFormat dateFormat;
    private Calendar cal_L, cal_H;
    String Date_L;
    String Date_H;
    private Date date;
    private String mobno, LocationName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_gpsmy_own_location);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = new Date();
        dateSetting();
        initObj();
        initView();
        setListner();

        if (isInternetAvailable(getApplicationContext())) {
            refreshData();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        if(isInternetAvailable(getApplicationContext())){
            getRowFromDatabase();
        }
       /* if (getCount() > 0) {

            updateListfirst();

        } else {
            if (isInternetAvailable(getApplicationContext())) {
                refreshData();
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    private void initObj() {

        GPSRecods = new ArrayList<GPSMyLocationBean>();
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_teamgps);
        Intent intent = getIntent();
        UserMasterId = intent.getStringExtra("usermaterId");
        UserNamegps = intent.getStringExtra("usernamegps");
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mtxtsetname = (TextView) findViewById(R.id.txtname);
        mtxt_no_record = (TextView) findViewById(R.id.txt_no_record);
        mList = (ListView) findViewById(R.id.gpsmemberlist);
        reload = (Button) findViewById(R.id.reloadmore);
    }

    private void setListner() {
        if (UserNamegps != null) {
            mtxtsetname.setText(UserNamegps);
        }

    }

    private void dateSetting() {
        // TODO Auto-generated method stub

        cal_L = Calendar.getInstance();
        Date today = cal_L.getTime();
        cal_H = Calendar.getInstance();
        Date toadayH = cal_H.getTime();
        cal_H.add(Calendar.DATE, 1);
        Date nextday = cal_H.getTime();

        Date_L = dateFormat.format(today);
        Date_H = dateFormat.format(nextday);

    }

    public void reLoadMore(View v) {

        String s = mindATE;// 05-17-2016 10:22:04
        if (!(s == null || s.equalsIgnoreCase(" "))) {
            if (isInternetAvailable(GPSMyOwnLocation.this)) {
                new StartSession(GPSMyOwnLocation.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        data_loadMore = new DownloadGpsData_LoadMore();
                        data_loadMore.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }
        }
    }

    private int getCount() {
        int count = 0;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "'", null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void refreshData() {
        if (isInternetAvailable(GPSMyOwnLocation.this)) {
            showProgressDialog();
            new StartSession(GPSMyOwnLocation.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    dismissProgressDialog();
                    gpsDataJSON = new DownloadGpsData_First();
                    gpsDataJSON.execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgressDialog();
                }
            });
        } else {
            ut.displayToast(getApplicationContext(), "No Internet Connection");
        }
    }


    private void updateListfirst() {
        GPSRecods.clear();
        ArrayList<String> ls = new ArrayList<String>();
        SQLiteDatabase sql = db.getWritableDatabase();
        // String que = "SELECT * FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "' ORDER BY AddedDT DESC";

        String que = "SELECT * FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "'";
        String que1 = "SELECT MIN(AddedDT) FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "'";
        Cursor cur = sql.rawQuery(que, null);
        Cursor c1 = sql.rawQuery(que1, null);


        if (c1.getCount() == 0) {
            c1.close();
        } else {
            c1.moveToFirst();
            do {

                ls.add(c1.getString(0));

            } while (c1.moveToNext());
            c1.close();
            mindATE = ls.get(0);
        }
        if (cur.getCount() > 0) {
            mtxt_no_record.setVisibility(View.GONE);
            cur.moveToFirst();
            do {
                GPSMyLocationBean bean = new GPSMyLocationBean();
                bean.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                bean.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                bean.setGPSID(cur.getString(cur.getColumnIndex("GPSID")));
                bean.setFKUserMasterId(cur.getString(cur.getColumnIndex("FKUserMasterId")));
                String s = cur.getString(cur.getColumnIndex("AddedDt"));
                s = getDate(s);
                bean.setAddedDT(s);
                bean.setLocationName(cur.getString(cur.getColumnIndex("locationName")));
                bean.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                GPSRecods.add(bean);

            } while (cur.moveToNext());
            mList.setAdapter(new GPSRecordsAdapter(GPSMyOwnLocation.this,
                    GPSRecods));
        } else {
            mtxt_no_record.setVisibility(View.VISIBLE);
        }
        sql.close();
    }

    private void updateListloadmore() {
        GPSRecods.clear();
        ArrayList<String> ls = new ArrayList<String>();
        SQLiteDatabase sql = db.getWritableDatabase();
        // String que = "SELECT * FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "' ORDER BY AddedDT DESC";
        String que = "SELECT * FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "'";
        String que1 = "SELECT MIN(AddedDT) FROM " + db.TABLE_GET_GPSRECORDS + " WHERE FKUserMasterId='" + UserMasterId + "'";
        Cursor cur = sql.rawQuery(que, null);
        Cursor c1 = sql.rawQuery(que1, null);
        if (c1.getCount() == 0) {

            c1.close();
        } else {
            c1.moveToFirst();
            do {
                ls.add(c1.getString(0));

            } while (c1.moveToNext());
            c1.close();
            mindATE = ls.get(0);
        }
        if (cur.getCount() > 0) {
            mtxt_no_record.setVisibility(View.GONE);
            cur.moveToFirst();
            do {
                GPSMyLocationBean bean = new GPSMyLocationBean();
                bean.setLatitude(cur.getString(cur.getColumnIndex("latitude")));
                bean.setLongitude(cur.getString(cur.getColumnIndex("longitude")));
                bean.setGPSID(cur.getString(cur.getColumnIndex("GPSID")));
                bean.setFKUserMasterId(cur.getString(cur.getColumnIndex("FKUserMasterId")));
                String s = cur.getString(cur.getColumnIndex("AddedDt"));
                s = getDate(s);
                bean.setAddedDT(s);
                bean.setLocationName(cur.getString(cur.getColumnIndex("locationName")));
                bean.setMobileNo(cur.getString(cur.getColumnIndex("MobileNo")));
                GPSRecods.add(bean);
            } while (cur.moveToNext());

            int currentPosition = mList.getFirstVisiblePosition();
            mList.setAdapter(new GPSRecordsAdapter(GPSMyOwnLocation.this,
                    GPSRecods));
            mList.setSelectionFromTop(currentPosition + 1, 0);

        } else {
            mtxt_no_record.setVisibility(View.VISIBLE);
        }
        sql.close();
    }

    private String getDate(String s) {
        String data = "";
        try {
            SimpleDateFormat input = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            Date date = input.parse(s);
            data = output.format(date);
        } catch (Exception e) {
            data = "0";
        }
        return data;
    }

    private void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    class DownloadGpsData_First extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String date = "2017-03-21";
            String date1 = "2017-04-22";
            try {
                String url = CompanyURL + WebUrlClass.api_getGpsLocation + "?UserLoginId=" + URLEncoder.encode(UserMasterId, "UTF-8") + "&LDate=" + URLEncoder.encode(Date_L, "UTF-8") + "&Hdate=" + URLEncoder.encode(Date_H, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (response.contains("GPSID")) {
                try {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_GET_GPSRECORDS, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GET_GPSRECORDS, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnName.equalsIgnoreCase("locationName")) {
                                if (columnValue.equalsIgnoreCase("Location Not Found")
                                        || columnValue.equalsIgnoreCase("null")) {
                                    Double lat = jorder.getDouble("latitude");
                                    Double Long = jorder.getDouble("longitude");
                                    columnValue = GetCurrentLocation(lat, Long);
                                    values.put(columnName, columnValue);

                                } else {
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                values.put(columnName, columnValue);
                            }

                        }
                        long a = sql.insert(db.TABLE_GET_GPSRECORDS, null, values);
                        Log.e("team data", "ssss..." + a);
                    }
                    dismissProgressDialog();
                    updateListfirst();
                    reload.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (response.contains("No record found")) {
                //  reload.setVisibility(View.GONE);
                dismissProgressDialog();
                new Download_Date().execute();

            } else {
                dismissProgressDialog();
                reload.setVisibility(View.GONE);
                ut.displayToast(getApplicationContext(), "Server Error");
            }

        }

    }

    class DownloadGpsData_LoadMore extends AsyncTask<String, String, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
            String s = mindATE;// 05-17-2016 10:22:04
            SimpleDateFormat nextdt = new SimpleDateFormat(
                    "MM-dd-yyyy hh:mm:ss");
            Date min;
            try {
                min = nextdt.parse(s);
                SimpleDateFormat fom = new SimpleDateFormat("yyyy-MM-dd");
                Date_H = fom.format(min);

                Date_L = fom.format(min.getTime() - MILLIS_IN_DAY);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                //  String url = CompanyURL + WebUrlClass.api_getGpsLocation + "?UserLoginId=" + UserMasterId + "&LDate=" + Date_L + "&Hdate=" + Date_H;
                String url = CompanyURL + WebUrlClass.api_getGpsLocation + "?UserLoginId=" + URLEncoder.encode(UserMasterId, "UTF-8") + "&LDate=" + URLEncoder.encode(Date_L, "UTF-8") + "&Hdate=" + URLEncoder.encode(Date_H, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (response.contains("GPSID")) {
                try {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GET_GPSRECORDS, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnName.equalsIgnoreCase("locationName")) {
                                if (columnValue.equalsIgnoreCase("Location Not Found")
                                        || columnValue.equalsIgnoreCase("null")) {
                                    Double lat = jorder.getDouble("latitude");
                                    Double Long = jorder.getDouble("longitude");
                                    columnValue = GetCurrentLocation(lat, Long);
                                    values.put(columnName, columnValue);

                                } else {
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                values.put(columnName, columnValue);
                            }
                        }
                        long a = sql.insert(db.TABLE_GET_GPSRECORDS, null, values);
                        Log.e("team data", "ssss..." + a);
                    }
                    reload.setVisibility(View.VISIBLE);
                    updateListloadmore();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                dismissProgressDialog();
            } else if (response.contains("No record found")) {
                reload.setVisibility(View.GONE);
                dismissProgressDialog();
                ut.displayToast(getApplicationContext(), "No record to display");
            } else {
                dismissProgressDialog();
                reload.setVisibility(View.GONE);
                ut.displayToast(getApplicationContext(), "Server Error");
            }

        }

    }


    public class Download_Date extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = "";
            try {
                url = CompanyURL + WebUrlClass.api_getDategps + "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dismissProgressDialog();
            if (result.contains("AddedDt")) {
                String LatestDate = "";
                String ID;
                Date ss = null;
                try {

                    JSONArray jResults = new JSONArray(result);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        LatestDate = jorder.getString("AddedDt");
                        ID = jorder.getString("FKUserMasterId");
                    }
                    LatestDate = LatestDate.substring(0, LatestDate.length() - 4);
                    SimpleDateFormat date = new SimpleDateFormat(
                            "dd MMM yyyy HH:mm:ss");// 7/6/2016 2:50:31 PM//07 Apr 2017 18:18:13:760

                    ss = date.parse(LatestDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Date_L = dateFormat.format(ss);// 2016-07-06

                Calendar c;
                c = Calendar.getInstance();
                c.setTime(ss);
                c.add(Calendar.DATE, 1);
                Date nextday = c.getTime();
                Date_H = dateFormat.format(nextday);
                new DownloadGpsData_First().execute();

            } else {
                reload.setVisibility(View.GONE);
                ut.displayToast(getApplicationContext(), "No record to display");
            }
        }

    }


    public class Download_Adress extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = "";
            try {
                url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + params[0] + "," + params[1] + "&sensor=true";
                res = ut.OpenConnection(url, getApplicationContext());
                String response = res.toString();
                Log.e("adress ", response);
                JSONObject jsonObj = new JSONObject(response);
                String Status = jsonObj.getString("status");
                if (Status.equalsIgnoreCase("OK")) {
                    JSONArray Results = jsonObj.getJSONArray("results");
                    JSONObject zero = Results.getJSONObject(0);
                    JSONArray address_components = zero.getJSONArray("address_components");

                    for (int i = 0; i < address_components.length(); i++) {
                        JSONObject zero2 = address_components.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);


                    }

                    // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dismissProgressDialog();
            if (result.contains("AddedDt")) {
                String LatestDate = "";
                String ID;
                Date ss = null;
                try {

                    JSONArray jResults = new JSONArray(result);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        LatestDate = jorder.getString("AddedDt");
                        ID = jorder.getString("FKUserMasterId");
                    }
                    LatestDate = LatestDate.substring(0, LatestDate.length() - 4);
                    SimpleDateFormat date = new SimpleDateFormat(
                            "dd MMM yyyy HH:mm:ss");// 7/6/2016 2:50:31 PM//07 Apr 2017 18:18:13:760

                    ss = date.parse(LatestDate);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Date_L = dateFormat.format(ss);// 2016-07-06

                Calendar c;
                c = Calendar.getInstance();
                c.setTime(ss);
                c.add(Calendar.DATE, 1);
                Date nextday = c.getTime();
                Date_H = dateFormat.format(nextday);
                new DownloadGpsData_First().execute();

            } else {
                reload.setVisibility(View.GONE);
                ut.displayToast(getApplicationContext(), "No record to display");
            }
        }

    }


    private String GetCurrentLocation(final double lat, final double lang) {
        String LocationName = "Latitude : " + lat + "Longitude : " + lang;
        try {
            Geocoder geocoder = new Geocoder(GPSMyOwnLocation.this,
                    Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(lat, lang, 1);

            int a = addressList.get(0).getMaxAddressLineIndex();
            if (a > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    if (i == 0) {
                        sb.append(address.getAddressLine(0));
                    } else {
                        sb.append(" , " + address.getAddressLine(i));
                    }
                }
                LocationName = sb.toString();
            } else {
                String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postalCode = addressList.get(0).getPostalCode();
                LocationName = address + " , " + city + " , " + state + " , " + country;
            }


           /* String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addressList.get(0).getLocality();
            String state = addressList.get(0).getAdminArea();
            //  String country = addressList.get(0).getCountryName();
            //  String postalCode = addressList.get(0).getPostalCode();
            // String knownName = addressList.get(0).getFeatureName();
            LocationName = address + " , " + city + " , " + state;*/


        } catch (Exception e) {
            e.printStackTrace();

            final double latvalue = lat;
            final double longvalue = lang;

            try {
                final String[] loc = new String[1];
                final Thread t = new Thread() {

                    public void run() {
                        try {
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk&latlng=" + latvalue + "," + longvalue + "&sensor=true";
                            Object res = ut.OpenConnection(url, getApplicationContext());
                            if (res==null) {
                                loc[0] = "Latitude : " + lat + " Longitude : " + lang;
                            }
                            else
                            {
                                String response = res.toString();
                                JSONObject jsonObj = null;
                                jsonObj = new JSONObject(response);
                                String Status1 = jsonObj.getString("status");
                                if (Status1.equalsIgnoreCase("OK")) {
                                    JSONArray Results = jsonObj.getJSONArray("results");
                                    int cnt = Results.length();
                                    if (cnt > 1) {
                                        JSONObject zero2 = Results.getJSONObject(1);
                                        loc[0] = zero2.getString("formatted_address");

                                    } else {
                                        JSONObject zero2 = Results.getJSONObject(0);
                                        loc[0] = zero2.getString("formatted_address");
                                    }

                           /* for (int i = 0; i < address_components.length(); i++) {
                                JSONObject zero2 = address_components.getJSONObject(i);
                                String long_name = zero2.getString("long_name");
                                JSONArray mtypes = zero2.getJSONArray("types");
                                String Type = mtypes.getString(0);
                            }*/
                                } else {
                                    loc[0] = "Latitude : " + lat + " Longitude : " + lang;
                                }
                            }
                            } catch(JSONException e1){
                                e1.printStackTrace();
                                loc[0] = "Latitude : " + lat + " Longitude : " + lang;
                            }
                        }

                    }

                    ;
                t.start();

                t.join();

                    LocationName =loc[0];
                } catch(Exception e1){
                    e1.printStackTrace();
                    LocationName = "Latitude : " + lat + " Longitude : " + lang;
                }

            }
        return LocationName;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            if(id== R.id.refresh1){


                if (ut.isNet(getApplicationContext())) {
                    dateSetting();
                    refreshData();
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }
    private void getRowFromDatabase() {
        String GPSID;

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_ADD_GPSRECORDS + " where isUploaded=?",
                new String[]{"No"});
        int a = cursor.getCount();
        if (cursor.getCount() == 0) {
            sql.close();
            System.out.println("======= c= 0  fetchall ");
        } else {
            cursor.moveToFirst();
            GPSID = cursor.getString(cursor.getColumnIndex("GPSID"));
            JSONObject Object = new JSONObject();
            try {
                Object.put("Lat", cursor.getString(cursor.getColumnIndex("latitude")));
                Object.put("Long", cursor.getString(cursor.getColumnIndex("longitude")));
                Object.put("locationname", cursor.getString(cursor.getColumnIndex("locationName")));
                Object.put("GpsDate", cursor.getString(cursor.getColumnIndex("GpsAddedDt")));
                Object.put("usermasterId", cursor.getString(cursor.getColumnIndex("UserMasterID")));
                Object.put("AppName", SetAppName.AppNameFCM);

                String MobileNo = cursor.getString(cursor.getColumnIndex("MobileNo"));
                String latitude = cursor.getString(cursor.getColumnIndex("latitude"));
                String longitude = cursor.getString(cursor.getColumnIndex("longitude"));
                String locationName = cursor.getString(cursor.getColumnIndex("locationName"));
                String GpsAddedDt = cursor.getString(cursor.getColumnIndex("GpsAddedDt"));
                String UsermaterID = cursor.getString(cursor.getColumnIndex("UserMasterID"));
                // isUploaded = cursor.getString(cursor.getColumnIndex("Mobileno"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            sql.close();
            String s = Object.toString();
            s = s.replace("\\\\", "");
            if (isInternetAvailable(getApplicationContext())) {
                final String finalS = s;
                final String Id = GPSID;
                new StartSession(getApplicationContext(), new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GPSTask().execute(finalS, Id);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        //  ut.displayToast(getApplicationContext(), msg);
                        // stopSelf();
                    }
                });

            } else {


            }

        }

    }

    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public class GPSTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_postGpsLocation;
                res = ut.OpenPostConnection(url, params[0], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[1]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            String gpsid = result.get(1);
            SQLiteDatabase sql = db.getWritableDatabase();
            if (res.contains("Success")) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", "Yes");
                sql.update(db.TABLE_ADD_GPSRECORDS, contentValues, "GPSID=?",
                        new String[]{gpsid});
                sql.delete(db.TABLE_ADD_GPSRECORDS, "isUploaded=?",
                        new String[]{"Yes"});
                sql.close();
                getRowFromDatabase();
            } else {
                sql.close();
                getRowFromDatabase();
            }

        }
    }


}
