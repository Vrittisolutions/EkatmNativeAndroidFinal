package com.vritti.ekatm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Date;

public class PhoneCallReceiver1 extends PhoneStateListener {

    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;

    public void onCallStateChanged(Context context, int state, String phoneNumber) {
        if (lastState == state) {
//No change, debounce extras
            return;
        }

        // System.out.println("Number inside onCallStateChange : "  + phoneNumber);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();

                // Toast.makeText(context, "Incoming Call Ringing " + phoneNumber, Toast.LENGTH_SHORT).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    //    Toast.makeText(context, "Outgoing Call Started " + phoneNumber, Toast.LENGTH_SHORT).show();
                }
                break;

            case TelephonyManager.CALL_STATE_IDLE:
//Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
//Ring but no pickup-  a miss
                   // Toast.makeText(context, "Ringing but no pickup" + phoneNumber + " Call time " + callStartTime + " Date " + new Date(), Toast.LENGTH_SHORT).show();
                } else if (isIncoming) {
                    CallReceiver2 callreceiver2 = new CallReceiver2();
                    callreceiver2.onIncomingCallEnded(context, phoneNumber, callStartTime, new Date());
                    // Toast.makeText(context, "Incoming " + phoneNumber + " Call time " + callStartTime  , Toast.LENGTH_SHORT).show();
                } else {
                    CallReceiver2 callreceiver2 = new CallReceiver2();
                    callreceiver2.onOutgoingCallEnded(context, phoneNumber, callStartTime, new Date());
                    //    Toast.makeText(context, "outgoing " + phoneNumber + " Call time " + callStartTime +" Date " + new Date() , Toast.LENGTH_SHORT).show();

                }

                break;
        }
        lastState = state;
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context context, String number, Date start) {}
    protected void onOutgoingCallStarted(Context context, String number, Date start) {}
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {}
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {}
    protected void onMissedCall(Context context, String number, Date start) {}

}