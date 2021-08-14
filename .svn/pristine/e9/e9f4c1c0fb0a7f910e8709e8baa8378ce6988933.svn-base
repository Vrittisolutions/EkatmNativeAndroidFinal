package com.vritti.vwb.vworkbench;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.SetAppName;
import com.vritti.vwb.vworkbench.ClaimAttachmentFragment;
import com.vritti.vwb.vworkbench.ClaimDetailsFragment;
import com.vritti.vwb.vworkbench.ClaimHeaderFragment;
import com.vritti.vwb.vworkbench.ClaimSummaryFragment;

/**
 * Created by 300151 on 11/18/2016.
 */
public class ClaimMainActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    TabLayout tabLayout;
    String Position;
    public static ProgressBar mprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_main_claim);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        InitView();
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Position = extra.getString("Position");
        }
        setupViewPager(viewPager);
        if (Position != null) {
            viewPager.setCurrentItem(Integer.parseInt(Position));
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (ClaimHeaderFragment.ed_travel_purpose.getText().toString().equalsIgnoreCase("")) {
                     Toast.makeText(ClaimMainActivity.this, "Fill Purpose of travel", Toast.LENGTH_LONG).show();
                    viewPager.setCurrentItem(0);
                } else if (!(ClaimHeaderFragment.lsClaimApproverList.size() > 0)) {
                    Toast.makeText(ClaimMainActivity.this, "Select Approver", Toast.LENGTH_LONG).show();
                    viewPager.setCurrentItem(0);
                } /*else if (!(ClaimHeaderFragment.CostCenterlist.size() > 0)) {
                    viewPager.setCurrentItem(0);
                }*/
            }

            @Override
            public void onPageSelected(int newPosition) {
                Fragment fragment = ((ViewPagerAdapter) viewPager.getAdapter()).getFragment(newPosition);
                if (newPosition == 0 && fragment != null) {
                    fragment.onResume();
                } else if (newPosition == 1 && fragment != null) {
                    fragment.onResume();
                } else if (newPosition == 2 && fragment != null) {
                    fragment.onResume();
                } else if (newPosition == 3 && fragment != null) {
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ClaimHeaderFragment(), "Header");
        adapter.addFragment(new ClaimDetailsFragment(), "Details");
        adapter.addFragment(new ClaimAttachmentFragment(), "Attachment");
        adapter.addFragment(new ClaimSummaryFragment(), "Summary");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;
        private Map<Integer, String> mFragmenttag;
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            fragmentManager = manager;
            mFragmenttag = new HashMap<Integer, String>();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmenttag.put(position, tag);
            }
            return obj;
        }

        public Fragment getFragment(int position) {
            String tag = mFragmenttag.get(position);
            if (tag == null)
                return null;

            return fragmentManager.findFragmentByTag(tag);
        }
    }

    private void InitView() {

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
    }


}
