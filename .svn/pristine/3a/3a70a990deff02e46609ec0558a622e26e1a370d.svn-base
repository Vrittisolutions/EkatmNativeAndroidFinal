package com.vritti.ekatm.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.ekatm.services.SendGPSNotification;

public class SendGPSNotificationBroadcastReceiver extends BroadcastReceiver {
    Boolean isInternetPresent;
    Context parent;
    private int itime;
    DatabaseHandlers db;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        db = new DatabaseHandlers(context);
        itime = 15;
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            if(getCount()) {
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
