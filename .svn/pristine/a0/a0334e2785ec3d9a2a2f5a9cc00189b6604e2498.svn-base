package com.vritti.crmlib.receiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.services.SendOfflineData;
import com.vritti.crmlib.vcrm7.OpportunityUpdateActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;



public class CallReceiver extends PhonecallReceiver {
    String  UserType;
    SharedPreferences userpreferences;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;

    public CallReceiver() {


    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {

        Log.e("IncomingCallStarted", " " + number + "" + start);
    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        Log.e("OutgoingCallStarted", " " + number + "" + start);

    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {

        if (number.contains(" ")) {
            number = number.replaceAll(" ", "");
        }
        number = number.trim();
        if (number.length() >= 10) {
            number = number.substring(number.length() - 10, number.length());
        }
        this.context = ctx;
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

        sql = db.getWritableDatabase();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String Start = sdf.format(start);
        String End = sdf.format(end);
        String Start1 = sdf1.format(start);
        String End1 = sdf1.format(end);

        userpreferences = ctx.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        UserType = userpreferences.getString("UserType", null);
        Log.e("IncomingCallEnded", " " + number + "" + start);
        Long time_difference = end.getTime() - start.getTime();
        int hours1 = (int) (time_difference / (1000 * 60 * 60));
        int mins1 = (int) (time_difference % (1000 * 60 * 60));
        int s = (int) (time_difference % 60);
        int minutes = (int) ((time_difference / (1000 * 60)) % 60);
        int hours = (int) ((time_difference / (1000 * 60 * 60)) % 24);
        String duration = hours + ":" + minutes + ":" + s;
        if (time_difference >= 5 * 60 * 1000) {//5*60*1000
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = sdf2.format(new Date());
            String remark1 = "Incomingcall Record has been added of Number " + number + " on " + date;
            String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + number +
                    "&StartTime=" + Start1 + "&EndTime=" + End1 + "&Duration=" + duration + "&MobileCallType=Incoming";
            url = url.replace(" ", "%20");
            String op = "true";
            CreateOfflineIntend(url, "", WebUrlClass.GETFlAG, remark1, op, ctx);

        }
        String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  Mobile ='" + number + "'";
        Cursor cur1 = sql.rawQuery(query1, null);
        int cnt1 = cur1.getCount();
        Log.e("Table Partial Cnt", "" + cnt1);
        String query = "SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + " WHERE  Mobile ='" + number + "'";
        Cursor cur = sql.rawQuery(query, null);
        int cnt = cur.getCount();
        Log.e("Table Crm opportunity", "" + cnt);

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

            Intent intent = new Intent(ctx,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Customer");
            intent.putExtra("Callnature", "IN");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ctx.startActivity(intent);
        } else if (cur.getCount() > 0) {
            cur.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur.getString(cur.getColumnIndex("CallId"));
            firmname = cur.getString(cur.getColumnIndex("FirmName"));
            mobile = cur.getString(cur.getColumnIndex("Mobile"));
            callstatus = cur.getString(cur.getColumnIndex("CallStatus"));

            calltype = cur.getString(cur.getColumnIndex("CallType"));
            contactname = cur.getString(cur.getColumnIndex("ContactName"));
            ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));

            Intent intent = new Intent(ctx,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Customer");
            intent.putExtra("Callnature", "IN");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ctx.startActivity(intent);
        }
    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        Log.e("OutgoingCallEnded", " " + number + "" + start);

        this.context = ctx;
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

        userpreferences = ctx.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        UserType = userpreferences.getString("UserType", null);
        Log.e("IncomingCallEnded", " " + number + "" + start);
        Long time_difference = end.getTime() - start.getTime();
        int hours1 = (int) (time_difference / (1000 * 60 * 60));
        int mins = (int) (time_difference % (1000 * 60 * 60));
        int s = (int) (time_difference % 60);
        int minutes = (int) ((time_difference / (1000 * 60)) % 60);
        int hours = (int) ((time_difference / (1000 * 60 * 60)) % 24);


        String duration = hours + ":" + minutes + ":" + s;
        if (time_difference >= 5 * 60 * 1000) {//5*60*1000
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String date = sdf1.format(new Date());
            String remark1 = "Incomingcall Record has been added of Number " + number + " on " + date;
            String url = CompanyURL + WebUrlClass.api_save_Mocall_Record + "?UserMasterId=" + UserMasterId + "&MobileNo=" + number +
                    "&StartTime=" + Start2 + "&EndTime=" + End2 + "&Duration=" + duration + "&MobileCallType=Outgoing";
            url = url.replace(" ", "%20");

            String op = "true";
            CreateOfflineIntend(url, "", WebUrlClass.GETFlAG, remark1, op, ctx);

        }


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
        String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  Mobile ='" + number+"'";
        Cursor cur1 = sql.rawQuery(query1, null);
        int cnt = cur1.getCount();
        Log.e("Table Crm opportunity", "" + cnt);

        String query2 = "SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + " WHERE  Mobile ='" + number+"'";
        Cursor cur2 = sql.rawQuery(query2, null);
        int cnt2 = cur2.getCount();
        Log.e("Table Crm opportunity", "" + cnt2);

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

            Intent intent = new Intent(ctx,
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
            ctx.startActivity(intent);
        } else if (cur2.getCount() > 0) {
            cur2.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur2.getString(cur2.getColumnIndex("CallId"));
            firmname = cur2.getString(cur2.getColumnIndex("FirmName"));
            mobile = cur2.getString(cur2.getColumnIndex("Mobile"));
            callstatus = cur2.getString(cur2.getColumnIndex("CallStatus"));

            calltype = cur2.getString(cur2.getColumnIndex("CallType"));
            contactname = cur2.getString(cur2.getColumnIndex("ContactName"));
            ProspectId = cur2.getString(cur2.getColumnIndex("ProspectId"));

            Intent intent = new Intent(ctx,
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

            ctx.startActivity(intent);
        }
    }

    @Override
    protected void onMissedCall(Context ctx, String number, Date start) {

        Log.e("IncomingCallStarted", " " + number + "" + start);
        Log.e("OutgoingCallEnded", " " + number + "" + start);

        this.context = ctx;
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

        sql = db.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String Start = sdf.format(start);
        String End = sdf.format(start);
        int n = number.length() - 10;
        Log.e("text", "" + n);
        if (number.contains(" ")) {
            number = number.replaceAll(" ", "");
        }
        number = number.trim();
        if (number.length() >= 10) {
            number = number.substring(number.length() - 10, number.length());
        }

        //  number = phoeNumberWithOutCountryCode(number);
        // number = number.substring(3, number.length());
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
        String query1 = "SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL + " WHERE  Mobile='" + number + "'";
        Cursor cur1 = sql.rawQuery(query1, null);
        int cnt1 = cur1.getCount();
        Log.e("Table Crm partial", "" + cnt1);
        String query2 = "SELECT * FROM " + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER + " WHERE  Mobile='" + number + "'";
        Cursor cur2 = sql.rawQuery(query2, null);
        int cnt2 = cur2.getCount();
        Log.e("Table Crm opportunity", "" + cnt2);
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

            Intent intent = new Intent(ctx,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Me");
            intent.putExtra("Callnature", "MISSED");

            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ctx.startActivity(intent);
        } else if (cur2.getCount() > 0) {
            cur2.moveToFirst();
            String callid, firmname, callstatus, mobile, calltype, contactname, ProspectId;
            callid = cur2.getString(cur2.getColumnIndex("CallId"));
            firmname = cur2.getString(cur2.getColumnIndex("FirmName"));
            mobile = cur2.getString(cur2.getColumnIndex("Mobile"));
            callstatus = cur2.getString(cur2.getColumnIndex("CallStatus"));

            calltype = cur2.getString(cur2.getColumnIndex("CallType"));
            contactname = cur2.getString(cur2.getColumnIndex("ContactName"));
            ProspectId = cur2.getString(cur2.getColumnIndex("ProspectId"));

            Intent intent = new Intent(ctx,
                    OpportunityUpdateActivity.class);
            intent.putExtra("nature", "Telephone");
            intent.putExtra("starttime", Start);
            intent.putExtra("endtime", End);
            intent.putExtra("initiatedby", "Me");
            intent.putExtra("Callnature", "MISSED");
            intent.putExtra("callid", callid);
            intent.putExtra("calltype", calltype);
            intent.putExtra("firmname", firmname);
            intent.putExtra("table", "Call");
            intent.putExtra("ProspectId", ProspectId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }

    }

    public String phoeNumberWithOutCountryCode(String phoneNumberWithCountryCode) {
        if (phoneNumberWithCountryCode.startsWith("+")) {
            Pattern complie = Pattern.compile(" ");
            String[] phonenUmber = complie.split(phoneNumberWithCountryCode);
            Log.e("number is", phonenUmber[1]);
            phoneNumberWithCountryCode = phonenUmber[1];
        }
        return phoneNumberWithCountryCode;
    }

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op, Context ctx) {

        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            //Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(ctx,
                    SendOfflineData.class);
            intent1.putExtra("flag", "direct");
            ctx.startService(intent1);

        } else {
            // Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }
}

