package com.vritti.vwblib.receiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by sharvari on 22-Aug-18.
 */

public class JobSchedulerReceiver extends WakefulBroadcastReceiver {

    int interval=5*1000;
    private PowerManager.WakeLock screenWakeLock;

    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent) {



        //  MyFirebaseMessagingService.start(context);
        Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        Intent serviceIntent = new Intent(context, MyFirebaseMessagingService.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 11, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, alarmIntent);
        }
        startWakefulService(context, serviceIntent);

   }
}