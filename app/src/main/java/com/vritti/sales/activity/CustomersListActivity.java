package com.vritti.sales.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.vritti.ekatm.R;

public class CustomersListActivity extends AppCompatActivity {
    private Context parent;

    ListView listcustmrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_customers_list);

        init();

        setListeners();
    }

    public void init(){
        parent = CustomersListActivity.this;

        listcustmrs = (ListView)findViewById(R.id.listcustmrs);

    }

    public void setListeners(){

    }
}
