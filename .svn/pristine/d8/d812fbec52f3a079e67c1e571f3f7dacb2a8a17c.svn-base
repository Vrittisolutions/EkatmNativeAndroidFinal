package com.vritti.ekatm.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.services.CallReceiverIntentService;
import com.vritti.ekatm.services.SendOfflineData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static android.widget.Toast.LENGTH_SHORT;

public class CallReceiver2 extends PhoneCallReceiver1 {
    String UserType;
    SharedPreferences userpreferences;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;


    public CallReceiver2(){

    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {

        if (number.contains(" ")) {
            number = number.replaceAll(" ", "");
        }
        number = number.trim();
        if (number.length() >= 10) {
            number = number.substring(number.length() - 10, number.length());
        }
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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

        sql = db.getWritableDatabase();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String Start = sdf.format(start);
        String End = sdf.format(end);
        String Start1 = sdf1.format(start);
        String End1 = sdf1.format(end);

        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);
        Log.e("IncomingCallEnded", " " + number + "" + start);
        Long time_difference = end.getTime() - start.getTime();
        int hours1 = (int) (time_difference / (1000 * 60 * 60));
        int mins1 = (int) (time_difference % (1000 * 60 * 60));
        int s = (int) (time_difference % 60);
        int minutes = (int) ((time_difference / (1000 * 60)) % 60);
        int hours = (int) ((time_difference / (1000 * 60 * 60)) % 24);
        String duration = hours + ":" + minutes + ":" + s;
        //  if (time_difference >= 5 * 60 * 1000) {//5*60*1000
      //  SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
     //   String date = sdf2.format(new Date());
       // String remark1 = "Incomingcall Record has been added of Number " + number + " on " + date;
        /*String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + number +
                "&StartTime=" + Start1 + "&EndTime=" + End1 + "&Duration=" + duration + "&MobileCallType=Incoming";
        url = url.replace(" ", "%20");
        String op = "true";
        CreateOfflineIntend(url, "", WebUrlClass.GETFlAG, remark1, op, context);*/
        String callid, firmname, callstatus, mobile, calltype = "", contactname="", ProspectId="",callType="";
        Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();
        count1 = count1 +1;
        Log.e("Call Log Count", "" + count1);

        Cursor c12 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG + " WHERE  MobileNo ='" + number + "'", null);
        int count12 = c12.getCount();

        if(c1.getCount() > 0){
            ArrayList<CallLogsDetails> callLogsDetailsArrayList1 = new ArrayList<>();
            c1.moveToFirst();
            do {
                CallLogsDetails callLogsDetails = new CallLogsDetails();

                callLogsDetails.setUserMasterId(c1.getString(c1.getColumnIndex("UserMasterId")));
                callLogsDetails.setUserName(c1.getString(c1.getColumnIndex("UserMasterName")));
                callLogsDetails.setNumber(c1.getString(c1.getColumnIndex("MobileNo")));
                callLogsDetails.setStartTime(c1.getString(c1.getColumnIndex("StartTime")));
                callLogsDetails.setEndTime(c1.getString(c1.getColumnIndex("EndTime")));
                callLogsDetails.setDuration(c1.getString(c1.getColumnIndex("Duration")));
                callLogsDetails.setCallType(c1.getString(c1.getColumnIndex("MobileCallType")));
                callLogsDetails.setRowNo(c1.getString(c1.getColumnIndex("RowNo")));
                callLogsDetails.setContactPersonName(c1.getString(c1.getColumnIndex("ContactPersonName")));
                callLogsDetails.setCustomerName(c1.getString(c1.getColumnIndex("CustomerName")));
                callLogsDetailsArrayList1.add(callLogsDetails);
            } while (c1.moveToNext());
            if(callLogsDetailsArrayList1.size() != 0){
                for(int j=0 ; j<callLogsDetailsArrayList1.size() ; j++) {
                    if (number.equals(callLogsDetailsArrayList1.get(j).getNumber())){
                        callType= callLogsDetailsArrayList1.get(j).getCallType();
                        contactname = callLogsDetailsArrayList1.get(j).getContactPersonName();
                       // callid = callLogsDetailsArrayList1.get(j).getc
                        break;
                    }
                }
            }
        }


        if(count12 > 0) {

            if (c12.getCount() > 0) {
                c12.moveToFirst();
                do {
                   // callid = c12.getString(c12.getColumnIndex("CallId"));
                    calltype = c12.getString(c12.getColumnIndex("MobileCallType"));
                    contactname = c12.getString(c12.getColumnIndex("ContactPersonName"));

                } while (c12.moveToNext());
            }
            if(callType != "") {
                db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, callType, count1,contactname);

            }else if(calltype != ""){
                if(calltype.equalsIgnoreCase("opportunity") || calltype.equalsIgnoreCase("collection") ||
                        calltype.equalsIgnoreCase("opportunity/collection")){
                    db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration,
                            "Incoming",count1,"");
                }else{
                    if(contactname != ""){
                        db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, calltype,count1,contactname);
                    }else{
                        db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, calltype,count1,"");
                    }
                }


            } else{
                calltype="Incoming";
                db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, calltype,count1,"");
                //Create table callLog
            }

        }else{
            calltype="Incoming";
            db.addCallLogsDetails(UserMasterId, UserName, number, Start1, End1, duration, calltype,count1,"");
            //Create table callLog
        }
        if(callType != ""){
            if(callType.equalsIgnoreCase("spam") ||
                    callType.equalsIgnoreCase("colleague") ||
                    callType.equalsIgnoreCase("Personal")){
                final String uniqueID = UUID.randomUUID().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserMasterId", UserMasterId);
                    jsonObject.put("MobileNo", number);
                    jsonObject.put("StartTime",Start1);
                    jsonObject.put("EndTime",End1);
                    jsonObject.put("Duration",duration);
                    jsonObject.put("MobileCallType", calltype);
                    if (callType.equals("Colleague")) {
                        jsonObject.put("CallingType", "3");
                    } else if (callType.equals("Spam")) {
                        jsonObject.put("CallingType", "4");
                    } else if (callType.equals("Personal")) {
                        jsonObject.put("CallingType", "5");
                    }

                    if(callType != "") {
                        jsonObject.put("CallType", callType);
                    }else{
                        jsonObject.put("CallType", calltype);
                    }
                    //if(calltype.e(colleague,personal,spam)-> callid ="" //

                    jsonObject.put("CallId", "");
                    jsonObject.put("NewId", uniqueID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String  FinalJSOnObject = jsonObject.toString();

                // SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date1CAll = sdf1.format(new Date());
                String remark2 = "Incoming all Record has been added of Number " + number + " on " + date1CAll;
                String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                String op = "true";
                CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark2, op, context);
            }
        }
        else if(calltype != ""){
            if(calltype.equalsIgnoreCase("spam") ||
                    calltype.equalsIgnoreCase("colleague") ||
                    calltype.equalsIgnoreCase("Personal")){
                final String uniqueID = UUID.randomUUID().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserMasterId", UserMasterId);
                    jsonObject.put("MobileNo", number);
                    jsonObject.put("StartTime",Start1);
                    jsonObject.put("EndTime",End1);
                    jsonObject.put("Duration",duration);
                    jsonObject.put("MobileCallType", calltype);
                    jsonObject.put("CallingType", "1");
                    if(callType != "") {
                        jsonObject.put("CallType", callType);
                    }else{
                        jsonObject.put("CallType", calltype);
                    }
                    //if(calltype.e(colleague,personal,spam)-> callid ="" //

                    jsonObject.put("CallId", "");
                    jsonObject.put("NewId", uniqueID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String  FinalJSOnObject = jsonObject.toString();

                // SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date1CAll = sdf1.format(new Date());
                String remark2 = "Outgoingall Record has been added of Number " + number + " on " + date1CAll;
                String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                String op = "true";
                CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark2, op, context);
            }
        }



       // Toast.makeText(context,"Incomimg call called", LENGTH_SHORT).show();


    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        Log.e("OutgoingCallEnded", " " + number + "" + start);


        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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
        sql = db.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String Start = sdf.format(start);
        String End = sdf.format(end);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String Start2 = sdf2.format(start);
        String End2 = sdf2.format(end);
        int n = number.length() - 10;
        Log.e("text", "" + n);

        if (number.contains(" ")) {
            number = number.replaceAll(" ", "");
        }
        number = number.trim();
        if (number.length() >= 10) {
            number = number.substring(number.length() - 10, number.length());
        }

        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);
        Log.e("IncomingCallEnded", " " + number + "" + start);
        Long time_difference = end.getTime() - start.getTime();
        int hours1 = (int) (time_difference / (1000 * 60 * 60));
        int mins = (int) (time_difference % (1000 * 60 * 60));
        int s = (int) (time_difference % 60);
        int minutes = (int) ((time_difference / (1000 * 60)) % 60);
        int hours = (int) ((time_difference / (1000 * 60 * 60)) % 24);


        String duration = hours + ":" + minutes + ":" + s;


        //  if (time_difference >= 5 * 60 * 1000) {//5*60*1000
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = sdf1.format(new Date());
        String remark1 = "Incomingcall Record has been added of Number " + number + " on " + date;

      /*  String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + number +
                "&StartTime=" + Start2 + "&EndTime=" + End2 + "&Duration=" + duration + "&MobileCallType=Outgoing";

        //Personal call //Call id  - kutalya call la attach ahe //category - Opportunity,personal,spam,crm -
        url = url.replace(" ", "%20");

        String op = "true";
        CreateOfflineIntend(url, "", WebUrlClass.GETFlAG, remark1, op, context);*/


        Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count1 = c1.getCount();
        count1 = count1 + 1;
        Log.e("Call Log Count", "" + count1);

        Cursor c12 = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG + " WHERE  MobileNo ='" + number + "'", null);
        int count12 = c12.getCount();
        String callid, firmname, callstatus, mobile, calltype = "", contactname="", ProspectId,callType="";
        if(c1.getCount() > 0){
            ArrayList<CallLogsDetails> callLogsDetailsArrayList1 = new ArrayList<>();
            c1.moveToFirst();
            do {
                CallLogsDetails callLogsDetails = new CallLogsDetails();

                callLogsDetails.setUserMasterId(c1.getString(c1.getColumnIndex("UserMasterId")));
                callLogsDetails.setUserName(c1.getString(c1.getColumnIndex("UserMasterName")));
                callLogsDetails.setNumber(c1.getString(c1.getColumnIndex("MobileNo")));
                callLogsDetails.setStartTime(c1.getString(c1.getColumnIndex("StartTime")));
                callLogsDetails.setEndTime(c1.getString(c1.getColumnIndex("EndTime")));
                callLogsDetails.setDuration(c1.getString(c1.getColumnIndex("Duration")));
                callLogsDetails.setCallType(c1.getString(c1.getColumnIndex("MobileCallType")));
                callLogsDetails.setRowNo(c1.getString(c1.getColumnIndex("RowNo")));
                callLogsDetails.setContactPersonName(c1.getString(c1.getColumnIndex("ContactPersonName")));
                callLogsDetails.setCustomerName(c1.getString(c1.getColumnIndex("CustomerName")));
                callLogsDetailsArrayList1.add(callLogsDetails);
            } while (c1.moveToNext());
            if(callLogsDetailsArrayList1.size() != 0){
                for(int j=0 ; j<callLogsDetailsArrayList1.size() ; j++) {
                    if (number.equals(callLogsDetailsArrayList1.get(j).getNumber())){
                        callType= callLogsDetailsArrayList1.get(j).getCallType();
                        contactname = callLogsDetailsArrayList1.get(j).getContactPersonName();
                        break;
                    }
                }
            }
        }



        if(count12 > 0) {

            if (c12.getCount() > 0) {
                c12.moveToFirst();

                //  callid = cur.getString(cur.getColumnIndex("CallId"));
                // firmname = cur.getString(cur.getColumnIndex("FirmName"));
                calltype = c12.getString(c12.getColumnIndex("MobileCallType"));
                contactname = c12.getString(c12.getColumnIndex("ContactPersonName"));

                // mobile = cur.getString(cur.getColumnIndex("Mobile"));
                // callstatus = cur.getString(cur.getColumnIndex("CallStatus"));

                //calltype = cur.getString(cur.getColumnIndex("CallType"));
                // contactname = cur.getString(cur.getColumnIndex("ContactName"));
                // ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));

            } while (c12.moveToNext());

            if(callType != "") {
                db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, callType, count1,contactname);


            } else if(calltype != ""){
                if(calltype.equalsIgnoreCase("opportunity") || calltype.equalsIgnoreCase("collection") ||
                        calltype.equalsIgnoreCase("opportunity/collection")){
                    calltype="Outgoing";
                    db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, "Outgoing",count1,"");
                }else {
                    if (contactname != "") {
                        db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, calltype, count1,contactname);
                    } else {
                        db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, calltype, count1,"");
                    }
                }

            } else{
                calltype="Outgoing";
                db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, "Outgoing",count1,"");
            }

        }else{
            calltype="Outgoing";
            db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, "Outgoing",count1,"");
        }
        if(callType != ""){
            if(callType.equalsIgnoreCase("spam") ||
                    callType.equalsIgnoreCase("colleague") ||
                    callType.equalsIgnoreCase("Personal")){
                final String uniqueID = UUID.randomUUID().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserMasterId", UserMasterId);
                    jsonObject.put("MobileNo", number);
                    jsonObject.put("StartTime",Start2);
                    jsonObject.put("EndTime",End2);
                    jsonObject.put("Duration",duration);
                    jsonObject.put("MobileCallType", calltype);
                    if (callType.equals("Colleague")) {
                        jsonObject.put("CallingType", "3");
                    } else if (callType.equals("Spam")) {
                        jsonObject.put("CallingType", "4");
                    } else if (callType.equals("Personal")) {
                        jsonObject.put("CallingType", "5");
                    }

                    if(callType != "") {
                        jsonObject.put("CallType", callType);
                    }else{
                        jsonObject.put("CallType", calltype);
                    }
                    //if(calltype.e(colleague,personal,spam)-> callid ="" //

                    jsonObject.put("CallId", "");
                    jsonObject.put("NewId", uniqueID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String  FinalJSOnObject = jsonObject.toString();

                // SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date1CAll = sdf1.format(new Date());
                String remark2 = "Outgoingall Record has been added of Number " + number + " on " + date;
                String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                String op = "true";
                CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark1, op, context);
            }
        }
        else if(calltype != ""){
            if(calltype.equalsIgnoreCase("spam") ||
                    calltype.equalsIgnoreCase("colleague") ||
                    calltype.equalsIgnoreCase("Personal")){
                final String uniqueID = UUID.randomUUID().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("UserMasterId", UserMasterId);
                    jsonObject.put("MobileNo", number);
                    jsonObject.put("StartTime",Start2);
                    jsonObject.put("EndTime",End2);
                    jsonObject.put("Duration",duration);
                    jsonObject.put("MobileCallType", calltype);
                    jsonObject.put("CallingType", "1");
                    if(callType != "") {
                        jsonObject.put("CallType", callType);
                    }else{
                        jsonObject.put("CallType", calltype);
                    }
                    //if(calltype.e(colleague,personal,spam)-> callid ="" //

                    jsonObject.put("CallId", "");
                    jsonObject.put("NewId", uniqueID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String  FinalJSOnObject = jsonObject.toString();

                // SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String date1CAll = sdf1.format(new Date());
                String remark2 = "Outgoingall Record has been added of Number " + number + " on " + date;
                String url = CompanyURL + WebUrlClass.api_save_Mocall_Record;
                String op = "true";
                CreateOfflineIntend(url, FinalJSOnObject, WebUrlClass.POSTFLAG, remark2, op, context);
            }
        }



        //Toast.makeText(context,"outgoing call called", LENGTH_SHORT).show();

        // db.addCallLogsDetails(UserMasterId, UserName, number, Start2, End2, duration, "Outgoing",count1);
        //Create table callLog

      /*  Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALL_LOG, null);
        int count = c.getCount();
        Log.e("Call Log Count", "" + count);


        String query = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur.getString(cur.getColumnIndex("CallId"));
            firmname = cur.getString(cur.getColumnIndex("FirmName"));

            mobile = cur.getString(cur.getColumnIndex("Mobile"));
            callstatus = cur.getString(cur.getColumnIndex("CallStatus"));

            calltype = cur.getString(cur.getColumnIndex("CallType"));
            contactname = cur.getString(cur.getColumnIndex("ContactName"));
            ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));

        }
        String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  Mobile ='" + number + "'";
        Cursor cur1 = sql.rawQuery(query1, null);
        int cnt = cur1.getCount();
        Log.e("Table Crm opportunity", "" + cnt);

        String query2 = "SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + " WHERE  Mobile ='" + number + "'";
        Cursor cur2 = sql.rawQuery(query2, null);
        int cnt2 = cur2.getCount();
        Log.e("Table Crm opportunity", "" + cnt2);

        //

        if (cur1.getCount() > 0) {
            cur1.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur1.getString(cur1.getColumnIndex("CallId"));
            firmname = cur1.getString(cur1.getColumnIndex("FirmName"));
            mobile = cur1.getString(cur1.getColumnIndex("Mobile"));
            callstatus = cur1.getString(cur1.getColumnIndex("CallStatus"));

            calltype = cur1.getString(cur1.getColumnIndex("CallType"));
            contactname = cur1.getString(cur1.getColumnIndex("ContactName"));
            ProspectId = cur1.getString(cur1.getColumnIndex("ProspectId"));

            Intent intent = new Intent(context,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Me");
            intent.putExtra("Callnature", "OUT");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else if (cur2.getCount() > 0) {
            cur2.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur2.getString(cur2.getColumnIndex("CallId"));
            firmname = cur2.getString(cur2.getColumnIndex("FirmName"));
            mobile = cur2.getString(cur2.getColumnIndex("Mobile"));
            callstatus = cur2.getString(cur2.getColumnIndex("CallStatus"));

            calltype = cur2.getString(cur2.getColumnIndex("CallType"));
            contactname = cur2.getString(cur2.getColumnIndex("ContactName"));
            ProspectId = cur2.getString(cur2.getColumnIndex("ProspectId"));

            Intent intent = new Intent(context,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Me");
            intent.putExtra("Callnature", "OUT");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);
        }*/
    }



    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op, Context context) {

        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            //Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
           /* Intent intent1 = new Intent(context,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");

            CallReceiverIntentService.enqueueWork(context, intent1);*/
            /*Intent intent1 = new Intent(context,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            CallReceiverIntentService.enqueueWork(context, intent1);*/
            //CallReceiverIntentService.enqueueWork(context, intent1);
            Intent intent1 = new Intent(context,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            context.startService(intent1);

        } else {
            // Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }
}
