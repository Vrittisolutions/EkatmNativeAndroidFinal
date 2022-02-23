package com.vritti.vwb.vworkbench;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.devs.acr.AutoErrorReporter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import com.vritti.ekatm.MainActivity;
import com.vritti.ekatm.R;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.ekatm.receiver.ConnectivityReceiver;
import com.vritti.ekatm.services.DownloadJobService;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by Admin-1 on 6/23/2017.
 */

public class Myapplication extends Application {
    SharedPreferences userpreferences;
    public static FirebaseJobDispatcher dispatcher ;


    String EnvMasterId, PlantMasterId, LoginId, Password, UserMasterId, CompanyURL;

    private static Myapplication mInstance;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
        mInstance = this;

    }

    public static synchronized Myapplication getInstance() {
        return mInstance;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        //  Realm.init(this); //initialize other plugins


       /* Intent intent = new Intent();
        intent.setAction("com.hoanganhtuan95ptit.JobSchedulerReceiver");
        sendBroadcast(intent);*/
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        ViewTarget.setTagId(R.id.glide_tag);
        int badgeCount = 1;
        ShortcutBadger.with(getApplicationContext()).count(badgeCount);


      /*  AutoErrorReporter.get(this)
                .setEmailAddresses("vrittiisolutions@gmail.com")
                .setEmailSubject("vwb crash report")
                .start();
*/




    }
    private Activity mActivity=null;
    public Activity getmActivity(){
        return  mActivity;
    }
    public void setmActivity(Activity mActivity){
        this.mActivity=mActivity;
    }
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

}