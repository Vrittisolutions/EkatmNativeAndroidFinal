package com.vritti.ekatm.services;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.vritti.crm.vcrm7.OpportunityActivity;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.LoggingTimeActivity;

public class ForegroundService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    String module="";
    String Title="",Notification_Title="",Opportunity="";
    private PendingIntent pendingIntent;

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        String ID = intent.getStringExtra("id");
        String flag = intent.getStringExtra("f");
        String WorkTime = intent.getStringExtra("time");
        module = intent.getStringExtra("module");
        Opportunity = intent.getStringExtra("Opportunity");


        if (module.equalsIgnoreCase("CRM")){
            Title="You started working on this opportunity "+ input +" at :"+ WorkTime;
            Notification_Title="CRM Notification";
        }else {
            Title="You started working on this activity "+ input +" at :"+ WorkTime;
            Notification_Title="Vwb Notification";
        }

        createNotificationChannel();
        if (Opportunity.equalsIgnoreCase("Activity")){
            Intent notificationIntent = new Intent(this, ActivityMain.class);
            notificationIntent.putExtra("Opportunity",Opportunity);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
        }else {
            Intent notificationIntent = new Intent(this, OpportunityActivity.class);
            notificationIntent.putExtra("Opportunity",Opportunity);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);
        }


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(Notification_Title)
                .setContentText(Title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Title))
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_logo))
                .setSmallIcon(R.mipmap.dummy)
                .setContentIntent(pendingIntent)
                .setSubText("Click to complete")
                .build();
        startForeground(1, notification);
        //do heavy work on a background thread
        //stopSelf();
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "LoginTime",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}