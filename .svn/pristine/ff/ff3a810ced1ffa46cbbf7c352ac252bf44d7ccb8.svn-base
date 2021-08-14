package com.vritti.crmlib.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;

import com.vritti.crmlib.services.PaidLocationFusedLocationTracker1;
import com.vritti.databaselib.other.WebUrlClass;

import java.util.ArrayList;
import java.util.List;




/**
 * Created by Admin-1 on 3/29/2017.
 */

public class AlarmManagerBroadcastReceiverGPS extends BroadcastReceiver {
    final public static String ONE_TIME = "onetime";
    String EnvMasterId, PlantMasterId, LoginId, Password, UserMasterId, CompanyURL, MobileNo, IsCrmUser;
    private int itime;
    SharedPreferences userpreferences;


    @Override
    public void onReceive(Context context, Intent intent) {
        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        EnvMasterId = userpreferences.getString("EnvMasterId", null);
        PlantMasterId = userpreferences.getString("PlantMasterId", null);
        LoginId = userpreferences.getString("LoginId","");
        Password = userpreferences.getString("Password", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        itime = 15;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            long aTime = 1000 * 60 * itime;
            if (!(LoginId.equalsIgnoreCase(""))) {
                if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

                    int i;
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
                        }
            }
        } else {
           /* long aTime = 1000 * 60 * itime;
            Intent igpsalarm = new Intent(context, GPSTracker.class);
            PendingIntent piHeartBeatService = PendingIntent.getService(
                    context, 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(piHeartBeatService);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), aTime, piHeartBeatService);*/

          ///  Toast toast = Toast.makeText(context,"",Toast.LENGTH_SHORT)
        }
    }
}
