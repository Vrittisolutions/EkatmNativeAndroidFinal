package com.vritti.ekatm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.ekatm.services.DownloadJobService;
import com.vritti.ekatm.services.PaidLocationFusedLocationTracker1;
import com.vritti.vwb.CommonClass.AppCommon;

/**
 * Created by Admin-1 on 3/29/2017.
 */

public class AlarmManagerBroadcastReceiverGPS extends BroadcastReceiver {
    private int itime;
    DatabaseHandlers db;
    public static FirebaseJobDispatcher dispatcher ;
    public static Job myJob = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AlarmMan", " fired ");
        db = new DatabaseHandlers(context);
        itime = 15;

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())||"android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
            if (getCount()) {

               /* int i;
                ArrayList<String> packagenameArrayList = new ArrayList<String>();
                List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
                for (i = 0; i < packageInfoList.size(); i++) {
                    PackageInfo packageInfo = packageInfoList.get(i);
                    String packagename = packageInfo.packageName;
                    packagenameArrayList.add(packagename);
                }
                if (packagenameArrayList.contains("vworkbench7.vritti.com.vworkbench7")) {

                } else {
                    Intent service = new Intent(context, PaidLocationFusedLocationTracker1.class);
                    context.startService(service);                    }
            }*/
              //  setJobShedulder(context);
             /*   Intent serviceIntent = new Intent(context, PaidLocationFusedLocationTracker1.class);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                    //ContextCompat.startForegroundService(context, serviceIntent);
                } else {
                    context.startService(serviceIntent);
                }*/
            }

        } else {

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

    private void setJobShedulder(Context context) {
        if(myJob == null) {
            dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
            callJobDispacher(context);
        }
        else{
            if(!AppCommon.getInstance(context).isServiceIsStart()){
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
                callJobDispacher(context);
            }else {
                dispatcher.cancelAll();
                dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
                myJob = null;
                callJobDispacher(context);
            }
        }

    }

    private void callJobDispacher(Context context) {
        myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(DownloadJobService.class)
                // uniquely identifies the job
                .setTag("test")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)

                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(180, 240))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(

                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_IDLE


                )
                .build();

        dispatcher.mustSchedule(myJob);
        AppCommon.getInstance(context).setServiceStarted(true);
    }

    private boolean getCount() {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOGIN_SETTING, null);
        int cnt = c.getCount();
        if (cnt > 0) {
            return true;
        } else {
            return false;
        }
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
