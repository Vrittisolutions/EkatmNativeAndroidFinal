package com.vritti.ekatm.services;




import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.IBinder;

import android.util.Log;
import android.widget.Toast;

import java.net.URLEncoder;

public class SendTimeSheet extends Service {
    String url, mobno, activityid, projectid, curdate, fromtime, totime, desc,
            iscomplete, PromiseDate, finaloutcome, ReasonTransfer,
            transfertoid;
    String isc = "";
    AsyncTask uploadtimesheet;
    String  MobileNo;
    SharedPreferences  userpreferences;
    String resp = "";
    private final String NAMESPACE = "vWorkbench";
    private String URL = "/vwb/webservice/VWBservice.asmx?wsdl";
    private final String SOAP_ACTION = "vWorkbench/UpdateTimesheet";
    private final String METHOD_NAME = "UpdateTimesheet";
    SQLiteDatabase sql;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="",UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        ut = new Utility();
        Context context = this;
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
        curdate = intent.getStringExtra("curDate");
        fromtime = intent.getStringExtra("fromTime");
        totime = intent.getStringExtra("totime");
        desc = intent.getStringExtra("desc");
        activityid = intent.getStringExtra("actid");
        projectid = intent.getStringExtra("pid");

        isc = intent.getStringExtra("isc");
        PromiseDate = intent.getStringExtra("PromiseDate");
        finaloutcome = intent.getStringExtra("finaloutcome");
        ReasonTransfer = intent.getStringExtra("ReasonTransfer");
        transfertoid = intent.getStringExtra("transfertoid");
        if (isnet()) {
            //uploadtimesheet = new UploadTS().execute();

            new StartSession(getApplicationContext(), new CallbackInterface() {
                @Override
                public void callMethod() {
                    Boolean aBoolean = false;
                    String SaveChecked1 = aBoolean.toString();
                    new UploadgetTimesheetDataJSON().execute(curdate, SaveChecked1, fromtime, activityid, totime,"Timesheet filled through biometric attendance from mobile ");
                }

                @Override
                public void callfailMethod(String msg) {
                  //  ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "No internet connection Found.Please turn on data usage on Device..",
                    Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
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
    class UploadgetTimesheetDataJSON extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            String a = integer;
            Log.e(" string ..", a);

            if (a.contains("You can not fill time sheet from less than IN time")) {
                ut.displayToast(getApplicationContext(), "You can not fill time sheet from less than IN time");
            } else if (a.contains("You can not fill time sheet for more than")) {
                ut.displayToast(getApplicationContext(), "You can not fill time sheet for more than current Time");

            } else if (a.contains("From time should not be grater than To time")||a.equalsIgnoreCase("30")) {
               ut.displayToast(getApplicationContext(), "From time should not be grater than To time");

            } else if (a.contains("You can not fill time sheet greater than current time")||a.equalsIgnoreCase("40")) {
                ut.displayToast(getApplicationContext(), "You can not fill time sheet greater than current time");
            } else if (a.contains("You have already filled the time slot")||a.equalsIgnoreCase("10")) {
                ut.displayToast(getApplicationContext(), "You have already filled the time slot");

            } else if (a.equalsIgnoreCase("1")) {
                ut.displayToast(getApplicationContext(), "Record Added successfully");

                String activityname = getname();
                String s = "TimeSheet Record added successfully -  Detail : "
                        + activityname
                        + " Applied between - "
                        + fromtime
                        + " To - " + totime ;

                stopSelf();
            } else {
                ut.displayToast(getApplicationContext(), a);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getInsertTimesheet + "?forDate=" +  URLEncoder.encode(params[0],"UTF-8") + "&SaveChecked="
                        + URLEncoder.encode(params[1], "UTF-8") + "&fromTime=" + URLEncoder.encode(params[2],"UTF-8") + "&ActivityId=" + URLEncoder.encode(params[3], "UTF-8") +
                        "&toTime=" + URLEncoder.encode(params[4],"UTF-8") + "&workDesc=" + URLEncoder.encode(params[5], "UTF-8")+"&Timehrs=0&ActivityTypeId=";

                res = ut.OpenConnection(url,getApplicationContext());
                Log.e("response data", res + "");
                response = res.toString();
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }
   /* public class UploadTS extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            try {


                String urlnew = CompanyURL + URL;
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                PropertyInfo propInfo = new PropertyInfo();

                propInfo.type = PropertyInfo.STRING_CLASS;
                // adding parameters

                request.addProperty("mobile", mobno);
                request.addProperty("date", curdate);
                request.addProperty("FromTime", fromtime);
                request.addProperty("Totime", totime);
                request.addProperty("Desc", desc);
                request.addProperty("ActivityId", activityid);
                request.addProperty("ProjectId", projectid);
                request.addProperty("Iscompleted", isc);
                request.addProperty("PromiseDate", PromiseDate);
                request.addProperty("finaloutcome", finaloutcome);
                request.addProperty("transfertoid", transfertoid);
                request.addProperty("ReasonTransfer", ReasonTransfer);
                request.addProperty("Enddt", totime);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);

                envelope.dotNet = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        urlnew);

                androidHttpTransport.call(SOAP_ACTION, envelope);

                SoapPrimitive res = (SoapPrimitive) envelope.getResponse();

                System.out.println("---------------   soap response --- "
                        + res.toString());
                resp = res.toString();
            } catch (Exception e) {
                resp = "Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            if (resp.equalsIgnoreCase("Error")) {
                Toast.makeText(getApplicationContext(),
                        "Server down.Please try  after some time.",
                        Toast.LENGTH_LONG).show();

            } else if (resp
                    .equalsIgnoreCase("You have already filled the time slot")) {
                *//*
                 * Utilities.showCustomMessageDialog(
				 * "You have already filled the time slot", "Alert!!",
				 * getApplicationContext());
				 *//*
                Toast.makeText(getApplicationContext(),
                        "You have already filled the time slot",
                        Toast.LENGTH_LONG).show();

            } else if (resp
                    .equalsIgnoreCase("You cannot fill timesheet greater than current time")) {
                *//*
                 * Utilities.showCustomMessageDialog(
				 * "You cannot fill timesheet greater than current time",
				 * "Alert!!", getApplicationContext());
				 *//*
                Toast.makeText(getApplicationContext(),
                        "You cannot fill timesheet greater than current time",
                        Toast.LENGTH_LONG).show();

            } else if (resp.contains("Invalid Access You can not fill time sheet for more than")) {
				*//*
				 * Utilities.showCustomMessageDialog( ""+resp, "Alert!!",
				 * getApplicationContext());
				 *//*
                Toast.makeText(getApplicationContext(), resp, Toast.LENGTH_LONG)
                        .show();

            } else if (resp.equalsIgnoreCase("Update successfully")) {
                String activityname = getname();
                Toast.makeText(getApplicationContext(), "Update successfully",
                        Toast.LENGTH_LONG).show();

                String s = "Time Sheet entry added successfully vWork Detail - "
                        + activityname
                        + " Applied between - "
                        + fromtime
                        + " To - " + totime + " Description - " + desc;
               *//* LogFile lf = new LogFile();
                lf.writeFile(s);
*//*
                stopSelf();
            }

        }

    }*/

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public String getname() {
        // TODO Auto-generated method stub

        String ActivityName = "";
        try {
            String[] params = new String[1];
            params[0] = activityid;
            Cursor c2 = sql
                    .rawQuery(
                            "SELECT * FROM ActivityAllocationMaster where ActivityId=?",
                            params);

            String PId = "";
            c2.moveToFirst();
            ActivityName = c2.getString(c2.getColumnIndex("ActivityName"));
            PId = c2.getString(c2.getColumnIndex("ProjectId"));
            c2.moveToLast();

            c2.close();
           // db.close();
            //sql.close();

        } catch (Exception e) {

        }
        return ActivityName;

    }

}
