package com.vritti.ekatm.services;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class CallReceiverIntentService extends JobIntentService {

    // Service unique ID
    static final int SERVICE_JOB_ID = 50;
    Context context;

    // Enqueuing work in to this service.
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, CallReceiverIntentService.class, SERVICE_JOB_ID, work);
    }

    @Override
    protected void onHandleWork( Intent intent) {
        onHandleIntent(intent);
    }

    private void onHandleIntent(Intent intent) {
        //Handling of notification goes here
            intent.putExtra("flag", "direct");
            CallReceiverIntentService.this.startService(intent);



    }
}