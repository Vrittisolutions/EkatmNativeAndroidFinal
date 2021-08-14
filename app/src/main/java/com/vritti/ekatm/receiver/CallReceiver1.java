package com.vritti.ekatm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;

public class CallReceiver1 extends BroadcastReceiver {

    String UserType;
    SharedPreferences userpreferences;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;


    @Override
    public void onReceive(Context context, Intent intent) {
//Log.w("intent " , intent.getAction().toString());
        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneCallReceiver1 customPhoneListener = new PhoneCallReceiver1();

        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = intent.getExtras();
        String phone_number = bundle.getString("incoming_number");

        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
// String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        int state = 0;
        if(stateStr != null){
            if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                state = TelephonyManager.CALL_STATE_IDLE;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            }
            else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            if (phone_number == null || "".equals(phone_number)) {
                return;
            }
        }

        customPhoneListener.onCallStateChanged(context, state, phone_number);
        //Toast.makeText(context, "Phone Number " + phone_number , Toast.LENGTH_SHORT).show();

    }




}

