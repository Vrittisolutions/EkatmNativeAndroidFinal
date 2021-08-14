package com.vritti.vwblib.chat;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sharvari on 21-Nov-17.
 */

public class MessageRecieveBackground extends Service {


    public static final long INTERVAL =60*2;
    private Handler mHandler = new Handler();
    private Timer mtimer = null;


    @Override
    public void onCreate() {
        super.onCreate();
        if(mtimer != null){
            mtimer.cancel();
        }else
            mtimer = new Timer();
        mtimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, INTERVAL);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    Intent service = new Intent(getApplicationContext(),ChattingDataSendBackground.class);
                    getApplicationContext().startService(service);
                }
            });
        }
    }
}
