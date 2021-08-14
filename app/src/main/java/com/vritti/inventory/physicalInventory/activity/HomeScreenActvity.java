package com.vritti.inventory.physicalInventory.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.vritti.ekatm.R;

public class HomeScreenActvity extends AppCompatActivity {
    private Context parent;
    AppCompatRadioButton radio_online,radio_ofline,radio_counter,radio_auditor,radio_immediate,radio_batch;
    String Onstatus="",Costatus="",Bostatus="";
    Button btn_next;
    ImageView menuoption;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        setListeners();

    }

    private void init() {
        parent = HomeScreenActvity.this;

        radio_online=findViewById(R.id.radio_online);
        radio_ofline=findViewById(R.id.radio_ofline);
        radio_counter=findViewById(R.id.radio_counter);
        radio_auditor=findViewById(R.id.radio_auditor);
        radio_immediate=findViewById(R.id.radio_immediate);
        radio_batch=findViewById(R.id.radio_batch);
        btn_next=findViewById(R.id.btn_next);
        menuoption= findViewById(R.id.menuoption);

        Costatus = radio_counter.getText().toString();

        Intent intent=getIntent();
        Costatus = intent.getStringExtra("Costatus");
      /*  Status=intent.getStringExtra("status");
        BatchPrint=intent.getStringExtra("bathchprint");*/
    }

    public void setListeners(){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(parent, BatchSelectionActivity.class);
                startActivity(intent);

               /* if (Costatus.equals("Counter")){

                    startActivity(new Intent(HomeScreenActvity.this,PIEntryPrintingActivity.class)
                            .putExtra("status",Onstatus)
                            .putExtra("bathchprint",Bostatus)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }else if (Costatus.equals("Auditor")) {
                    startActivity(new Intent(HomeScreenActvity.this,AudiScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }*/

            }
        });

        radio_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_online.setChecked(true);
                    Onstatus=radio_online.getText().toString();

                }else {
                }
            }
        });

        radio_ofline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_ofline.setChecked(true);
                    Onstatus=radio_ofline.getText().toString();

                }else {
                }
            }
        });

        radio_counter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radio_counter.setChecked(true);
                    Costatus=radio_counter.getText().toString();

                }else {

                }
            }
        });
        radio_auditor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radio_auditor.setChecked(true);
                    Costatus=radio_auditor.getText().toString();

                }else {

                }
            }
        });

        radio_immediate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_immediate.setChecked(true);
                    Bostatus=radio_immediate.getText().toString();

                }else {
                }
            }
        });
        radio_batch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    radio_batch.setChecked(true);
                    Bostatus=radio_batch.getText().toString();
                }else {
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.itemsync){
            Intent intent = new Intent(this, ItemMasterSyncActivity.class);
            startActivity(intent);

        }/*else if(id == R.id.audit){
            Intent intent = new Intent(this, AudiScreenActivity.class);
            startActivity(intent);

        }*//*else if(id == R.id.edit_delete){
            Intent intent = new Intent(this, EditDeleteScreenActivity.class);
            startActivity(intent);

        }*/else if(id == R.id.reprint){
            Intent intent = new Intent(this, ReprintScreenActivity.class);
            startActivity(intent);

        }
            return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pi, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
