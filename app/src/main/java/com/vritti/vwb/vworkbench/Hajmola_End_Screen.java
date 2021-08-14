package com.vritti.vwb.vworkbench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.vritti.ekatm.R;

public class Hajmola_End_Screen extends AppCompatActivity {

    ImageView msgSahara;
    Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hajmola__end__screen);

        msgSahara = findViewById(R.id.msgSahara);


        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(3000);

                    startActivity(new Intent(Hajmola_End_Screen.this, WelcomeScreenActivity.class));
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        thread.start();
    }




}

