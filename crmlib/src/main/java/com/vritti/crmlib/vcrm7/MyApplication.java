package com.vritti.crmlib.vcrm7;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.support.multidex.MultiDex;



/**
 * Created by sharvari on 09-Oct-17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

      /*  StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        ViewTarget.setTagId(R.id.glide_tag);

       initializeSSLContext(getApplicationContext());*/
       /* AutoErrorReporter.get(this)
                .setEmailAddresses("vrittiisolutions@gmail.com")
                .setEmailSubject("Crash Report CRM")
                .start();*/

       /* DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY).cacheInMemory()

                .cacheOnDisc().build();

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .threadPoolSize(5).memoryCache(new WeakMemoryCache()).build();

        ImageLoader.getInstance().init(configuration);*/


    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
  /*  public static void initializeSSLContext(Context mContext){
        try {
            SSLContext.getInstance("TLSv1.2");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            ProviderInstaller.installIfNeeded(mContext.getApplicationContext());
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }*/
  /*  private Activity mActivity=null;
    public Activity getmActivity(){
        return  mActivity;
    }
    public void setmActivity(Activity mActivity){
        this.mActivity=mActivity;
    }*/


}
