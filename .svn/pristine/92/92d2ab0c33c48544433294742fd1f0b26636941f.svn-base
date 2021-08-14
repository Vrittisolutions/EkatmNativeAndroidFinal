package com.vritti.crmlib.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vritti.crmlib.services.SendGPSNotification;



public class SendGPSNotificationBroadcastReceiver extends BroadcastReceiver {
    Boolean isInternetPresent;
    Context parent;
    private int itime;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        itime = 15;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            long aTime = 1000 * 60 * itime;
            Intent igpsalarm = new Intent(context, SendGPSNotification.class);
                PendingIntent piHeartBeatService = PendingIntent.getService(
                        context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(piHeartBeatService);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), aTime, piHeartBeatService);

        }
    }
}
