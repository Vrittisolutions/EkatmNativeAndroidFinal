package com.vritti.SaharaModule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ActivityMain;

public class SplashActivity extends Activity {

    ImageView msgSahara;
    Animation animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.sahara_activity_splash);

        msgSahara = findViewById(R.id.msgSahara);

        // load the animation
        animation = AnimationUtils.loadAnimation(this,R.anim.zoomin);

        msgSahara.startAnimation(animation);

        msgSahara.setVisibility(View.VISIBLE);
        msgSahara.startAnimation(animation);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(1200);

                    startActivity(new Intent(SplashActivity.this, ActivityMain.class));
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
    }


}
