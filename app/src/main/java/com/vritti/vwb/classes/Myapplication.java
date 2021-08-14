package com.vritti.vwb.classes;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.bumptech.glide.request.target.ViewTarget;
import com.vritti.ekatm.R;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Admin-1 on 6/23/2017.
 */

public class Myapplication extends Application {
    SharedPreferences userpreferences;

    String EnvMasterId, PlantMasterId, LoginId, Password, UserMasterId, CompanyURL;


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);

    }
    @Override
    public void onCreate() {
        super.onCreate();
      //  Realm.init(this); //initialize other plugins


        Intent intent = new Intent();
        intent.setAction("com.hoanganhtuan95ptit.JobSchedulerReceiver");
        sendBroadcast(intent);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        ViewTarget.setTagId(R.id.glide_tag);
       /* int badgeCount = 1;
        ShortcutBadger.with(getApplicationContext()).count(badgeCount);
*/
        /*AutoErrorReporter.get(this)
                .setEmailAddresses("vrittiisolutions@gmail.com")
                .setEmailSubject("vwb Craint badgeCount = 1;sh Report")
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

}