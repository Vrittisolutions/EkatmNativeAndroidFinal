package com.vritti.vwblib.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.vritti.vwblib.Services.PaidLocationFusedLocationTracker1;

/**
 * Created by Admin-1 on 3/29/2017.
 */

public class AlarmManagerBroadcastReceiverGPS extends BroadcastReceiver {
    private int itime;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmMan", " fired ");

        itime = 15;

        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent service = new Intent(context,PaidLocationFusedLocationTracker1.class);
            context.startService(service);
        }else {

        }

       /* if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            long aTime = 1000 * 60 * itime;
            if (!(LoginId.equalsIgnoreCase(""))) {
                Intent igpsalarm = new Intent(context, FusedLocationTracker.class);
                PendingIntent piHeartBeatService = PendingIntent.getService(
                        context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);

                alarmManager.cancel(piHeartBeatService);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), aTime, piHeartBeatService);
            }

        } else {
            long aTime = 1000 * 60 * itime;

            Intent igpsalarm = new Intent(context, FusedLocationTracker.class);
            PendingIntent piHeartBeatService = PendingIntent.getService(
                    context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(piHeartBeatService);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), aTime, piHeartBeatService);
        }*/
    }
}
















































      /*  if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Log.e("AlarmMan"," Boot Completed ");

        }
        boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, ActivityLogIn.igpsalarm, PendingIntent.FLAG_NO_CREATE) != null);
        Toast.makeText(context,"Status"+alarmRunning,Toast.LENGTH_LONG).show();
        Log.e("status","Status"+alarmRunning);

        if (!alarmRunning) {
            ActivityLogIn.regservicenonGPS();

            Log.e("task","executed after boot");
        } else {
          //  Toast.makeText(context,"run",Toast.LENGTH_LONG).show();
            Log.e("task","run");

            Intent myIntent = new Intent(context, FusedLocationTracker.class);
            context.startService(myIntent);
        }

               *//* Intent igpsalarm = new Intent(context, FusedLocationTracker.class);
                PendingIntent piHeartBeatService = PendingIntent.getService(
                        context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context
                        .getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(piHeartBeatService);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis(), aTime, piHeartBeatService);*//*



           *//* long aTime = 1000 * 60 * itime;
            Intent igpsalarm = new Intent(context, GPSTracker.class);
            PendingIntent piHeartBeatService = PendingIntent.getService(
                    context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(piHeartBeatService);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), aTime, piHeartBeatService);*//*

        ///  Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT)


    }
}*/