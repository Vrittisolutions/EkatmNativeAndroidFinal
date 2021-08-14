package com.vritti.inventory.physicalInventory.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.vritti.ekatm.R;

public class SelectLableSize extends AppCompatActivity {
    private Context parent;
    RadioGroup radgrp;
    AppCompatRadioButton label2_2mm, label3_2mm;
    Button btn_yes, btn2_2, btn3_2;

    private SharedPreferences sharedPrefs;
    String radStatus = "";
    String labelSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lable_size);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        setListeners();
    }

    public void init(){
        parent =SelectLableSize.this;

        radgrp = findViewById(R.id.radgrp);
        btn_yes = findViewById(R.id.btn_yes);
        btn2_2 = findViewById(R.id.btn2_2);
        btn3_2 = findViewById(R.id.btn3_2);
        label2_2mm = findViewById(R.id.label2_2mm);
        label3_2mm = findViewById(R.id.label3_2mm);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(SelectLableSize.this);
        labelSize = sharedPrefs.getString("labelSize", "3x2mm");

        if(labelSize.equalsIgnoreCase("") || labelSize.equalsIgnoreCase(null) ){
            radStatus = "2x2mm";
        }else if(labelSize.equalsIgnoreCase("2x2mm")){
            radStatus = "2x2mm";
            label2_2mm.setChecked(true);
        }else if(labelSize.equalsIgnoreCase("3x2mm")){
            radStatus = "3x2mm";
            label3_2mm.setChecked(true);
        }


    }

    public void setListeners(){

         label2_2mm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    label2_2mm.setChecked(true);
                    //  radStatus[0] = label2_2mm.getText().toString();
                    radStatus = "2x2mm";

                }else {

                }
            }
        });

        label3_2mm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    label3_2mm.setChecked(true);
                    //  radStatus[0] = label3_2mm.getText().toString();
                    radStatus = "3x2mm";

                }else {

                }
            }
        });

        btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStatus = "2x2mm";
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(parent);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("labelSize",radStatus);
                editor.commit();

                finish();

            }
        });

        btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStatus = "3x2mm";
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(parent);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("labelSize",radStatus);
                editor.commit();

                finish();
                /*sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                labelSize = sharedPrefs.getString("labelSize", radStatus);*/

            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //proceed further

              /*  if(label2_2mm.isChecked()){
                    radStatus = "2x2mm";
                }else if(label3_2mm.isChecked()){
                    radStatus = "3x2mm";
                }*/
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(parent);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("labelSize",radStatus);
                editor.commit();
                finish();

            }
        });
    }
}