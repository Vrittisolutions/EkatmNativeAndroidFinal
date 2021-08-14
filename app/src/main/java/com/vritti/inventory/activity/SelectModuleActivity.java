package com.vritti.inventory.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.vritti.ekatm.R;

/**
 * Created by sharvari on 13-May-19.
 */

public class SelectModuleActivity extends AppCompatActivity {


    private Toolbar topToolBar;
    ImageView img_indent,img_vendor,img_material,img_project;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_select_module_lay);
        topToolBar = (Toolbar) findViewById(R.id.toolbar);
        topToolBar.setTitle(R.string.app_name_toolbar_Ekatm);
        topToolBar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        topToolBar.setTitleTextColor(Color.WHITE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        img_indent=findViewById(R.id.img_indent);
        img_vendor=findViewById(R.id.img_vendor);
        img_material=findViewById(R.id.img_material);
        img_project=findViewById(R.id.img_project);



        img_indent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SelectModuleActivity.this,VendorRegistrationForm.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

        img_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        img_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}