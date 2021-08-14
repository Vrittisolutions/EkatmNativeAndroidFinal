package com.vritti.crmlib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_crm);

        mBtn = (Button) findViewById(R.id.datacrm);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.vritti.databaselib.other.MainActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "CRM", Toast.LENGTH_LONG).show();

            }

        });


    }
}
