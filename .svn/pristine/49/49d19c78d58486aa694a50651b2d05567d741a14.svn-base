package com.vritti.ekatm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.vritti.ekatm.activity.ActivityLogIn;



public class IncomingSmsReceiver extends BroadcastReceiver {

	final SmsManager sms = SmsManager.getDefault();
//	@Override
	public void onReceive(Context context, Intent intent) {
		final Bundle bundle = intent.getExtras();
		try {
		    if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");
				for (int i = 0; i < pdusObj.length; i++) {
					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();
					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();
					Log.i("SmsReceiver", "senderNum: " + senderNum
							+ "; OTP : " + message);
					if (message.contains("OTP") && senderNum.contains("VRITTI")) {
						ActivityLogIn.textotp.setText(message.replaceAll("[^0-9]", ""));
						ActivityLogIn.textotp.setSelection(ActivityLogIn.textotp.getText().length());
					}
				}
			}
		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);
		}
	}
}
