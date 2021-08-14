package com.vritti.vwblib.reciver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Services.BackgroundService;
import com.vritti.vwblib.classes.CommonFunction;

/**
 * Created by Admin-1 on 6/30/2017.
 */

public class BroadcastBackgroundData extends BroadcastReceiver {
    String CompanyURL, EnvMasterId = "", LoginId ="", Password = "", PlantMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    private int itime;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            ut = new Utility();
            String settingKey = ut.getSharedPreference_SettingKey(context);
            String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(context, dabasename);
            SQLiteDatabase sql = db.getWritableDatabase();
            Cursor c = sql.rawQuery("Select * from " + db.TABLE_SETTING, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    itime = c.getInt(c.getColumnIndex("SettingValue"));
                } while (c.moveToNext());
            }
            long aTime = 1000 * 60 * itime;
            Intent igpsalarm = new Intent(context, BackgroundService.class);
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