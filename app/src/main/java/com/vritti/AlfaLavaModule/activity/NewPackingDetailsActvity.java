package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.vritti.AlfaLavaModule.adapter.Adp_ViewPager;
import com.vritti.AlfaLavaModule.fragment.FragmentCartonList;
import com.vritti.AlfaLavaModule.fragment.FragmentScanPackingPacket;
import com.vritti.ekatm.R;

public class NewPackingDetailsActvity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Context pContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_packing_tablayout);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Packing Details");
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        pContext = getApplicationContext();

        Intent intent = getIntent();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("Page scrolled", " page " + position);

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

    public ViewPager getViewPager() {
        return viewPager = (ViewPager) findViewById(R.id.viewpager1);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adp_ViewPager adapter = new Adp_ViewPager(getSupportFragmentManager(), this);
        adapter.addFragment(new FragmentScanPackingPacket(), "Scan Packet");
        adapter.addFragment(new FragmentCartonList(), "Carton List");
        viewPager.setAdapter(adapter);

    }
}
