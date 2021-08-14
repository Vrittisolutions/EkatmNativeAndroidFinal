package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.vritti.AlfaLavaModule.adapter.Adp_ViewPager;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysPacketComplete;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysPacketDetail;
import com.vritti.ekatm.R;


import java.util.ArrayList;

public class PutAwayPacketDetails extends AppCompatActivity {

    private static Context pContext;
    private ArrayList<PutAwaysBean> list;
    public static String GRN_ID_Packet, GRN_Header_Packet;
    private boolean add = false;


    private String UserId;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_put_away_packet_details);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.app_logo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initObj();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        Intent intent = getIntent();
        GRN_ID_Packet = intent.getStringExtra("GRN_No");//GRN_header
        GRN_Header_Packet = intent.getStringExtra("GRN_header");
        setListner();
    }
    private void initObj(){
        pContext = PutAwayPacketDetails.this;
        list = new ArrayList<PutAwaysBean>();

    }
    private void setListner(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Page scrolled", " page " + position);
            /*  if(position == 0){
                  new FragmentPutAwaysDetail();
              }else {
                  new  FragmentPutAwaysComplete();
              }*/
            }

            @Override
            public void onPageSelected(int position) {
                //    actionBar.setSelectedNavigationItem(position);
                Fragment fragment = ((Adp_ViewPager) viewPager.getAdapter()).getFragment(position);
                if (position == 1 && fragment != null) {
                    fragment.onResume();
                } else if (position == 0 && fragment != null) {
                    fragment.onResume();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setupViewPager(ViewPager viewPager) {
        Adp_ViewPager adapter = new Adp_ViewPager(getSupportFragmentManager(), this);
        adapter.addFragment(new FragmentPutAwaysPacketDetail(), "Detail");
        adapter.addFragment(new FragmentPutAwaysPacketComplete(), "Completed");
        viewPager.setAdapter(adapter);

    }
}
