package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ActivityMain;

public class ReceiptActivity extends AppCompatActivity {
    Context parent;

    LinearLayout lay_bill_to_bill,lay_collection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        setListeners();
    }

    public void init(){
        parent = ReceiptActivity.this;

        lay_bill_to_bill = findViewById(R.id.lay_bill_to_bill);
        lay_collection = findViewById(R.id.lay_collection);

    }

    public void setListeners(){

        lay_bill_to_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, ActivityMain.class);
                intent.putExtra("callFrom","SalesModule");
                startActivity(intent);
            }
        });

        lay_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent, CollectionReceiptActivity.class);
                startActivity(intent);
            }
        });

    }
}
