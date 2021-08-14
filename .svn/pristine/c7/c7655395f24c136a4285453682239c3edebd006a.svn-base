package com.vritti.MilkModule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.vritti.DliveryModule.DeliveryDetailPage;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.GPSTracker;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MilkRunLocationListActivity extends Activity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", ActivityId, currentStatus;
    static String settingKey = "";
    @BindView(R.id.toolbar1)
    Toolbar toolBar;
    @BindView(R.id.changeStatus)
    Button changeStatus;
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    MilkRunListAdapter milkRunListAdapter;
    ArrayList<MilkDetailObject> milkDetailObjectArrayList;
    ProgressDialog dialog;
    String activityName;
    int lastSelectedPos = -1;
    String statusValue,FinalObj , StatusFlag;
    String pendinValue = "10";
    String startValue = "20";
    String arrivedValue = "30";
    String loadingValue = "40";
    String completeValue = "70";
    String cancelValue = "90";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.milk_run_location_list);
        ButterKnife.bind(this);

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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        milkDetailObjectArrayList = new ArrayList<>();
        milkRunListAdapter = new MilkRunListAdapter(this, milkDetailObjectArrayList);
        recycleview.setAdapter(milkRunListAdapter);
        if (getIntent() != null) {
            //soNo = getIntent().getStringExtra("activityName");
            ActivityId = getIntent().getStringExtra("activityId");
            activityName = getIntent().getStringExtra("activityName");

        }
        toolBar.setTitle(activityName);

        milkDetailObjectArrayList.addAll(isInLocal());
        if (milkDetailObjectArrayList.size() == 0) {
            callApi();

        } else {
            milkRunListAdapter.notifyDataSetChanged();

        }
        if(milkDetailObjectArrayList.size()!= 0) {
            if (milkDetailObjectArrayList.get(0).getTripHeaderStatus().equals("10")&& milkDetailObjectArrayList.get(0).getStatus().equals(WebUrlClass.statusPending)) {
                changeStatus.setVisibility(View.VISIBLE);
                changeStatus.setText("Start");
            } else if (milkDetailObjectArrayList.get(0).getTripHeaderStatus().equals("70")||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size() - 1).getStatus().equals(WebUrlClass.statusComplete)||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size() - 1).getStatus().equals(WebUrlClass.statusCancel)) {
               if(milkDetailObjectArrayList.get(milkDetailObjectArrayList.size()-1).getTripHeaderStatus().equals(startValue)||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size()-1).getTripHeaderStatus().equals(pendinValue)) {
                   changeStatus.setVisibility(View.VISIBLE);
                   changeStatus.setText("Arrival On Gate");
               }else {
                   changeStatus.setVisibility(View.VISIBLE);
                   changeStatus.setText("Complete");
               }

            } else {
                changeStatus.setVisibility(View.GONE);
            }
        }


    }

    private void callApi() {
        if (isnet()) {
            dialog = new ProgressDialog(MilkRunLocationListActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetMilkdeliveryLocationList().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    dialog.dismiss();
                    ut.displayToast(MilkRunLocationListActivity.this, msg);
                    // hideProgresHud();
                }
            });
        } else {
            ut.displayToast(MilkRunLocationListActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }
    }

    public void selectUser(int adapterPosition) {
       /* if(milkDetailObjectArrayList.get(adapterPosition).getStatus().equals(WebUrlClass.statusComplete)){
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
        }else */
        lastSelectedPos = adapterPosition;

        if (milkDetailObjectArrayList.get(lastSelectedPos).getStatus().equals(WebUrlClass.statusPending)) {
            if (lastSelectedPos != 0) {
                if (milkDetailObjectArrayList.get(lastSelectedPos - 1).getStatus().equals(WebUrlClass.statusComplete)||milkDetailObjectArrayList.get(lastSelectedPos - 1).getStatus().equals(WebUrlClass.statusCancel)) {
                    startActivityForResult(new Intent(this, MilkDeliveryDetailPage.class).putExtra("activityId", ActivityId).putExtra("objectData", new Gson().toJson(milkDetailObjectArrayList.get(adapterPosition)))
                            , 998);
                } else {
                    Toast.makeText(context, "Please complete the previous task", Toast.LENGTH_SHORT).show();
                }
            } else {
                startActivityForResult(new Intent(this, MilkDeliveryDetailPage.class).putExtra("activityId", ActivityId)
                                .putExtra("objectData", new Gson().toJson(milkDetailObjectArrayList.get(adapterPosition)))

                        , 998);

            }
        } else {
            startActivityForResult(new Intent(this, MilkDeliveryDetailPage.class)
                    .putExtra("activityId", ActivityId)
                    .putExtra("objectData", new Gson().toJson(milkDetailObjectArrayList.get(adapterPosition)))
                    , 998);

        }
    }

    class GetMilkdeliveryLocationList extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_milk_get_trip_details + "?ActivityId=" + ActivityId;
            /*http://a207.ekatm.com/api/MilkRunApi/getTripDetail?ActivityId=190ED223-18EB-4CB8-9527-8ACD40CAC467*/
            try {
                res = ut.OpenConnection(url, MilkRunLocationListActivity.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                Log.i("response :: ", response.toString());
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* String msg = "";
                // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                sql.delete(db.TABLE_BIRTHDAY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BIRTHDAY, null);
                int count = c.getCount();
                String columnName, columnValue;*/
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    MilkDetailObject bean = new MilkDetailObject();
                    bean.setConsigneeName(jorder.getString("ConsigneeName"));
                    bean.setContactNo(jorder.getString("ContactNo"));
                    bean.setAddress(jorder.getString("Address"));
                    bean.setLatitude(jorder.getString("Latitude"));
                    bean.setLongitude(jorder.getString("Longitude"));
                    bean.setShipToMasterId(jorder.getString("ShipToMasterId"));
                    bean.setShipToMasterId1(jorder.getString("ShipToMasterId1"));
                    bean.setDriverContact(jorder.getString("DriverContact"));
                    bean.setSequneceNumber(jorder.getString("SequneceNumber"));
                    bean.setTripdetailid(jorder.getString("tripdetailid"));
                    bean.setUserName(jorder.getString("UserName"));
                    bean.setVehicleno(jorder.getString("Vehicleno"));
                    bean.setTripHeaderId(jorder.getString("TripHeaderId"));
                    bean.setTripDetailStatus(jorder.getString("TripDetailStatus"));
                    bean.setTripHeaderStatus(jorder.getString("TripHeaderStatus"));
                    String statusValue = jorder.getString("TripDetailStatus");
                    // Status Value
                    String pendinValue = "10";
                    String startValue = "20";
                    String arrivedValue = "30";
                    String loadingValue = "40";
                    String completeValue = "70";
                    String cancelValue = "90";

                    if (statusValue.equals(pendinValue)) {
                        bean.setStatus(WebUrlClass.statusPending);
                    } else if (statusValue.equals(startValue)) {
                        bean.setStatus(WebUrlClass.statusStart);
                    } else if (statusValue.equals(arrivedValue)) {
                        bean.setStatus(WebUrlClass.statusArrived);
                    } else if (statusValue.equals(loadingValue)) {
                        bean.setStatus(WebUrlClass.statusLoading);
                    } else if (statusValue.equals(completeValue)) {
                        bean.setStatus(WebUrlClass.statusComplete);
                    } else if (statusValue.equals(cancelValue)) {
                        bean.setStatus(WebUrlClass.statusCancel);
                    }else {
                        bean.setStatus(WebUrlClass.statusPending);
                    }

                    bean.setActivityId(ActivityId);

                    Log.i("triObject :: ", new Gson().toJson(bean));
                    milkDetailObjectArrayList.add(bean);

                    SQLiteDatabase sql = db.getWritableDatabase();
                    values.put("ConsigneeName", bean.getConsigneeName()); // Contact Name
                    values.put("ContactNo", bean.getContactNo()); // Contact Name
                    values.put("Address", bean.getAddress()); // Contact Name
                    values.put("Latitude", bean.getLatitude()); // Contact Name
                    values.put("Longitude", bean.getLongitude()); // Contact Name
                    values.put("ShipToMasterId", bean.getShipToMasterId()); // Contact Name
                    values.put("ShipToMasterId1", bean.getShipToMasterId1()); // Contact Name
                    values.put("DriverContact", bean.getDriverContact()); // Contact Name
                    values.put("SequneceNumber", bean.getSequneceNumber()); // Contact Name
                    values.put("tripdetailid", bean.getTripdetailid()); // Contact Name
                    values.put("UserName", bean.getUserName()); // Contact Name
                    values.put("Vehicleno", bean.getVehicleno()); // Contact Name
                    values.put("TripHeaderId", bean.getTripHeaderId()); // Contact Name
                    values.put("Status", bean.getStatus()); // Contact Name
                    values.put("ActivityId", bean.getActivityId()); // Contact Name
                    values.put("TripDetailStatus", bean.getTripDetailStatus()); // Contact Name
                    values.put("TripHeaderStatus", bean.getTripHeaderStatus()); // Contact Name

                    long a = sql.insert(db.TABLE_DELIVERY_MILK_RUN, null, values);
                    Log.e("ContACTtABLE", "" + a);
                    //2nd argument is String containing nullColumnHack
                    sql.close();

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {

                } else {
                    //    ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                ///   ut.displayToast(ActivityMain.this, "Server error...");
            }
            dialog.dismiss();
            if(milkDetailObjectArrayList.size()!= 0) {
                if (milkDetailObjectArrayList.get(0).getTripHeaderStatus().equals("10")&& milkDetailObjectArrayList.get(0).getStatus().equals(WebUrlClass.statusPending)) {
                    changeStatus.setVisibility(View.VISIBLE);
                    changeStatus.setText("Start");
                } else if (milkDetailObjectArrayList.get(0).getTripHeaderStatus().equals("70")||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size() - 1).getStatus().equals(WebUrlClass.statusComplete)||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size() - 1).getStatus().equals(WebUrlClass.statusCancel)) {
                    if(milkDetailObjectArrayList.get(milkDetailObjectArrayList.size()-1).getTripHeaderStatus().equals(startValue)) {
                        changeStatus.setVisibility(View.VISIBLE);
                        changeStatus.setText("Arrival On Gate");
                    }else {
                        changeStatus.setVisibility(View.VISIBLE);
                        changeStatus.setText("Complete");
                    }

                } else {
                    changeStatus.setVisibility(View.GONE);
                }
            }
            milkRunListAdapter.notifyDataSetChanged();

        }

    }

    private boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private ArrayList<MilkDetailObject> isInLocal() {
        MilkDetailObject bean = null;
        ArrayList<MilkDetailObject> milkDetailObjectArrayList = new ArrayList<>();
        ;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_DELIVERY_MILK_RUN + " where ActivityId='" + ActivityId + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                bean = new MilkDetailObject();

                try {
                    bean.setConsigneeName(c.getString(c.getColumnIndex("ConsigneeName")));
                    bean.setContactNo(c.getString(c.getColumnIndex("ContactNo")));
                    bean.setAddress(c.getString(c.getColumnIndex("Address")));
                    bean.setLatitude(c.getString(c.getColumnIndex("Latitude")));
                    bean.setLongitude(c.getString(c.getColumnIndex("Longitude")));
                    bean.setShipToMasterId(c.getString(c.getColumnIndex("ShipToMasterId")));
                    bean.setShipToMasterId1(c.getString(c.getColumnIndex("ShipToMasterId1")));
                    bean.setDriverContact(c.getString(c.getColumnIndex("DriverContact")));
                    bean.setSequneceNumber(c.getString(c.getColumnIndex("SequneceNumber")));
                    bean.setTripdetailid(c.getString(c.getColumnIndex("tripdetailid")));
                    bean.setUserName(c.getString(c.getColumnIndex("UserName")));
                    bean.setVehicleno(c.getString(c.getColumnIndex("Vehicleno")));
                    bean.setTripHeaderId(c.getString(c.getColumnIndex("TripHeaderId")));
                    bean.setActivityId(c.getString(c.getColumnIndex("ActivityId")));
                    bean.setStatus(c.getString(c.getColumnIndex("Status")));
                    bean.setTripDetailStatus(c.getString(c.getColumnIndex("TripDetailStatus")));
                    bean.setTripHeaderStatus(c.getString(c.getColumnIndex("TripHeaderStatus")));

                    milkDetailObjectArrayList.add(bean);

                    currentStatus = c.getString(c.getColumnIndex("Status"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //val = c.getString(c.getColumnIndex(columnname));

            } while (c.moveToNext());
        }
        sql.close();

        return milkDetailObjectArrayList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 998 & resultCode == 997) {
            MilkDetailObject milkDetailObject = new Gson().fromJson(data.getStringExtra("updateStatus"), MilkDetailObject.class);
            milkDetailObjectArrayList.set(lastSelectedPos, milkDetailObject);
            milkRunListAdapter.notifyDataSetChanged();
            if(milkDetailObjectArrayList.get(milkDetailObjectArrayList.size()-1).getStatus().equals(WebUrlClass.statusComplete)||milkDetailObjectArrayList.get(milkDetailObjectArrayList.size()-1).getStatus().equals(WebUrlClass.statusCancel)){
                changeStatus.setVisibility(View.VISIBLE);
                changeStatus.setText("Arrival On Gate");
            }

        }
    }

    @OnClick(R.id.changeStatus)
    void clcikComplete(){
        if(changeStatus.getText().toString().equalsIgnoreCase("start")) {
            sendSMSToVender();
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            callChangeStatusApi(WebUrlClass.statusStart);
        }else if(changeStatus.getText().toString().equalsIgnoreCase("Arrival On Gate")){
            // complete trip
            callChangeStatusApi(WebUrlClass.statusArrived);
        }else {
            callChangeStatusApi(WebUrlClass.statusComplete);
        }
    }

    private void callChangeStatusApi(String status) {
          /* String pendinValue = "10";
    String startValue = "20";
    String arrivedValue = "30";
    String loadingValue = "40";
    String completeValue = "70";
    String cancelValue = "90";*/
        if(status.equalsIgnoreCase(WebUrlClass.statusStart)){
            statusValue = "20";
        }else if(status.equalsIgnoreCase(WebUrlClass.statusArrived)) {
            statusValue = "30";
        }else{
            statusValue = "70";
        }

     //  statusValue = status;
            if (isnet()) {
                dialog = new ProgressDialog(MilkRunLocationListActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new UpdateStatusForMilk().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dialog.dismiss();
                        ut.displayToast(MilkRunLocationListActivity.this, msg);
                        // hideProgresHud();
                    }
                });
            } else {
                ut.displayToast(MilkRunLocationListActivity.this, "No Internet connection");
                //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
            }

    }

    private void sendSMSToVender() {
        /*qm.vritti.co.in:420/VrittiQM.asmx/CallWebService?m=mobile number &u=ae1001&p=vritti123&s=Test OTP*/

            try {
                dialog = new ProgressDialog(MilkRunLocationListActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                double latitude, longitude, nextLat = 0.0d, nextLong = 0.0d;
                String lastETA = "";
                Date currentTime = Calendar.getInstance().getTime();
                for (int j = 0; j < milkDetailObjectArrayList.size(); j++) {


                    if (j == 0) {
                        GPSTracker gpsTracker = new GPSTracker(this);
                        latitude = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                    } else {
                        latitude = Double.parseDouble(milkDetailObjectArrayList.get(j - 1).getLatitude());
                        longitude = Double.parseDouble(milkDetailObjectArrayList.get(j - 1).getLongitude());
                    }
                    nextLat = Double.parseDouble(milkDetailObjectArrayList.get(j).getLatitude());
                    nextLong = Double.parseDouble(milkDetailObjectArrayList.get(j).getLongitude());
                  /*  if(j== 0){

                    }
                    if (j != numberStringArray.size() - 1) {
                        nextLat = Double.parseDouble(numberStringArray.get(j).getLatitude());
                        nextLong = Double.parseDouble(numberStringArray.get(j + 1).getLongitude());
                    }*/
                    String timeValue = "";
                    if (latitude != 0.0d && longitude != 0.0d && nextLat != 0.0d && nextLong != 0.0d) {
                        timeValue = findDistance(String.valueOf(latitude), String.valueOf(longitude), String.valueOf(nextLat), String.valueOf(nextLong));
                        Log.i("etaTime ::",timeValue);
                        if (!timeValue.equals("")) {
                            if (timeValue.contains("hours") && timeValue.contains("mins")) {
                                String splitTime[] = timeValue.split(" ");

                                String hr = splitTime[0];
                                String min = splitTime[2];

                                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                Calendar cal = Calendar.getInstance();
                                if (lastETA.equals("")) {
                                    cal.setTime(currentTime);
                                } else {
                                    Date d = null;
                                    try {
                                        d = df.parse(lastETA);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(d);
                                }
                                cal.add(Calendar.HOUR, Integer.parseInt(hr));
                                cal.add(Calendar.MINUTE, Integer.parseInt(min));
                                lastETA = df.format(cal.getTime());
                            } else if (timeValue.contains("hour") && timeValue.contains("mins")) {
                                String splitTime[] = timeValue.split(" ");

                                String hr = splitTime[0];
                                String min = splitTime[2];

                                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                Calendar cal = Calendar.getInstance();
                                if (lastETA.equals("")) {
                                    cal.setTime(currentTime);
                                } else {
                                    Date d = null;
                                    try {
                                        d = df.parse(lastETA);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(d);
                                }
                                cal.add(Calendar.HOUR, Integer.parseInt(hr));
                                cal.add(Calendar.MINUTE, Integer.parseInt(min));
                                lastETA = df.format(cal.getTime());
                            } else if (timeValue.contains("mins")) {
                                String splitTime[] = timeValue.split(" ");

                                String min = splitTime[0];

                                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                Calendar cal = Calendar.getInstance();
                                if (lastETA.equals("")) {
                                    cal.setTime(currentTime);
                                } else {
                                    Date d = null;
                                    try {
                                        d = df.parse(lastETA);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(d);
                                }
                                cal.add(Calendar.MINUTE, Integer.parseInt(min));
                                lastETA = df.format(cal.getTime());

                            } else if (timeValue.contains("min")) {
                                String splitTime[] = timeValue.split(" ");
                                String hr = splitTime[0];
                                String min = splitTime[2];
                                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                                Calendar cal = Calendar.getInstance();
                                if (lastETA.equals("")) {
                                    cal.setTime(currentTime);
                                } else {
                                    Date d = null;
                                    try {
                                        d = df.parse(lastETA);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    cal.setTime(d);
                                }
                                cal.add(Calendar.HOUR, Integer.parseInt(hr));
                                cal.add(Calendar.MINUTE, Integer.parseInt(min));
                                lastETA = df.format(cal.getTime());
                            }
                        }
                    }
                    String phoneNumber = milkDetailObjectArrayList.get(j).getContactNo();
                   /* SendSMS_Vendor fetchUrl = new SendSMS_Vendor();
                    //fetchUrl.execute(pickupLocationAddress, dropLocationAddress);
                    fetchUrl.execute(phoneNumber, lastETA , milkDetailObjectArrayList.get(j).getConsigneeName());*/
                    String url ="http://qm.vritti.co.in:420/VrittiQM.asmx/CallWebService?m=" + phoneNumber + "&u=ae1001&p=vritti123&s=" +" "+milkDetailObjectArrayList.get(j).getConsigneeName()+"\n Vehicle No. : "+milkDetailObjectArrayList.get(j).getVehicleno()+"\n Reach by: "+ lastETA+"\n Driver Name : "+milkDetailObjectArrayList.get(j).getUserName()+"\n Mobile No: "+milkDetailObjectArrayList.get(j).getDriverContact();
                    RequestQueue queue = Volley.newRequestQueue(context);
                    // String url ="http://www.google.com";


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // Display the first 500 characters of the response string.

                                    Log.i("response::" , response);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("response::" , String.valueOf(error));
                        }
                    });


                    queue.add(stringRequest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    private class SendSMS_Vendor extends AsyncTask<String, Void, String[]> {
        String response;

        @Override
        protected String[] doInBackground(String... strings) {
            String number = strings[0];
            String ETA = strings[1];
            String name = strings[2];

            String url ="http://qm.vritti.co.in:420/VrittiQM.asmx/CallWebService?m=" + "7087741181" + "&u=ae1001&p=vritti123&s=" +"Hello "+name+"Your expected time for delivery is :"+ ETA;
            response = ut.OpenConnection(url, MilkRunLocationListActivity.this);
            Log.i("result", response);
            return new String[0];


        }
    }

    public String findDistance(String slat, String slng, String dlat, String dlng) {

        String response = "";

        try {
            // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address="+txtaddressSource.getText().toString().trim()+"&key="+getResources().getString(R.string.google_map_api));
            // response = getLatLongByURL("https://maps.google.com/maps/api/geocode/json?address=" + currentAddres + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
            response = getLatLongByURL("https://maps.googleapis.com/maps/api/directions/json?origin=" + slat + "," + slng + "&destination=" + dlat + "," + dlng + "&key=" + "AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk");
            //https://maps.googleapis.com/maps/api/directions/json?origin=18.5204,%2073.8567&destination=15.2993%C2%B0,74.1240&key=AIzaSyD3ONS8gu5RY-Db5shmfI1Fc4NyygBGHSk
            Log.d("response", "" + response);

            String result[] = new String[]{response};
            JSONObject jsonObject = new JSONObject(result[0]);
            response = ((JSONArray) jsonObject.get("routes")).getJSONObject(0)
                    .getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
        } catch (Exception e) {
        }
        return response;

    }

    public String getLatLongByURL(String requestURL) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);
            Log.i("LocationUrl ::" , requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private class UpdateStatusForMilk extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        PostDataObject postDataObject;
        //String statusValue;





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int pos = 0;
            if(statusValue.equals("20")){
                pos = 0;
            }else {
                pos = milkDetailObjectArrayList.size()-1;
            }
            MilkDetailObject milkDetailObject = milkDetailObjectArrayList.get(pos);
            if(pos==0)
                milkDetailObject.setTripDetailStatus(statusValue);
            GPSTracker gpsTracker = new GPSTracker();
            gpsTracker.getLatitude();
            gpsTracker.getLongitude();
            postDataObject = new PostDataObject();



            postDataObject.setObjTripDetail(new ObjTripDetail(UserMasterId, milkDetailObject.getTripdetailid(),
                    milkDetailObject.getTripHeaderId(),
                    milkDetailObject.getSequneceNumber(), milkDetailObject.getAddress(),
                    ActivityId,
                    milkDetailObject.getShipToMasterId(),
                    String.valueOf(gpsTracker.getLatitude()),
                    String.valueOf(gpsTracker.getLongitude()),
                    "", "",
                    "",
                    milkDetailObject.getTripDetailStatus(),
                    statusValue,
                    milkDetailObject.getUserName()

            ));
        }

        @Override
        protected String doInBackground(Integer... integers) {
            String url = CompanyURL + WebUrlClass.changeTripDetailStatus;
            try {
                res = ut.OpenPostConnection(url, new Gson().toJson(postDataObject), getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();


            if (res.equals("true")) {
                int pos = 0;
                String statusVal;
                if(statusValue.equals("20")){
                    pos = 0;
                    statusVal = WebUrlClass.statusStart;
                }else if(statusValue.equals(arrivedValue)) {
                    pos = milkDetailObjectArrayList.size()-1;
                    statusVal = WebUrlClass.statusArrived;
                }else {
                    pos = milkDetailObjectArrayList.size()-1;
                    statusVal = WebUrlClass.statusComplete;
                }
                MilkDetailObject milkDetailObject = milkDetailObjectArrayList.get(pos);

                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("TripHeaderStatus", statusValue);


                // updating row
                sqLiteDatabase.update(db.TABLE_DELIVERY_MILK_RUN, values, "TripHeaderId"+ " = ? ",
                        new String[]{String.valueOf(milkDetailObject.getTripHeaderId())});
                sqLiteDatabase.close();
              /*else {
                   callCompleteApi(WebUrlClass.WIP);
               }*/
                //onBackPressed();

                if(!statusVal.equals(WebUrlClass.statusArrived)) {
                    changeStatus.setVisibility(View.GONE);
                    callCompleteApi(statusVal);
                }
                else {
                    changeStatus.setVisibility(View.VISIBLE);
                    changeStatus.setText("Complete");
                }

            } else {
                Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void callCompleteApi(String status) {
       JSONObject ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {


            ChangeActivityStatus.put("ActivityId", ActivityId);
             if (status.equalsIgnoreCase(WebUrlClass.statusStart)||status.equalsIgnoreCase(WebUrlClass.statusArrived)) {
                 StatusFlag = WebUrlClass.FlagWIP;
                 ChangeActivityStatus.put("StatusCode", "14");
                 remark = "Change status of activity " + activityName + " to WIP ";

             } else if (status.equalsIgnoreCase(WebUrlClass.statusComplete)) {
                StatusFlag = WebUrlClass.FlagComplete;
                ChangeActivityStatus.put("StatusCode", "12");



                remark = "Complete the activity " + activityName;

            }
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        final String url = CompanyURL + WebUrlClass.api_change_activity_status;
        final String op = "Success";
        dialog.setTitle("Please wait...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show(); // Display Progress Dialog
        dialog.setCancelable(false);
        // CreateOfflineModeChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        new StartSession(getApplicationContext(), new CallbackInterface() {
            @Override
            public void callMethod() {
                new SendTask().execute(url, FinalObj, op);
            }

            @Override
            public void callfailMethod(String msg) {
                dialog.dismiss();
                Toast.makeText(context, "please try again something went wrong,,", Toast.LENGTH_SHORT).show();
                Log.i("checkStatus::", msg);
            }
        });
    }
    public class SendTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenPostConnection(url, params[1], getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                //response = response.replaceAll("\\\\", "");

                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                dialog.dismiss();
                Log.i("checkStatus::", e.getMessage());
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            //  data.add(params[3]);
            // data.add(params[4]);
            //  data.add(params[5]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            String res = result.get(0);
            // String stdres = result.get(1);
            //String recid = result.get(2);
            //String filepath = result.get(3);
            //String filename = result.get(4);

            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if(dialog.isShowing())
                dialog.dismiss();
                Log.i("response::", res);
                if (res.equals("Success")) {
                    if(statusValue.equals("20")){
                        changeStatus.setVisibility(View.GONE);
                    }else {
                        Toast.makeText(context, "Activity is Completed!..", Toast.LENGTH_SHORT).show();
                        SQLiteDatabase sql1 = db.getWritableDatabase();
                        sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                        sql1.close();
                        onBackPressed();
                    }
                }


            } else {
                if(dialog.isShowing())
                dialog.dismiss();
                Toast.makeText(context, "please try again something went wrong,,", Toast.LENGTH_SHORT).show();
                Log.i("response::", res);
            }

        }
    }

}
