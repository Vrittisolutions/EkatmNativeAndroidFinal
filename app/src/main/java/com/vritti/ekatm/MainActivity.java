package com.vritti.ekatm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button mBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_main);
        mBtn = findViewById(R.id.data);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),
                        com.vritti.sessionlib.MainActivity.class);
                startActivity(i);

            }
        });

        if (Constants.type == Constants.Type.CRM) {


        } else if (Constants.type == Constants.Type.Vwb) {


        } else if (Constants.type == Constants.Type.PM) {


        }


    }
}
