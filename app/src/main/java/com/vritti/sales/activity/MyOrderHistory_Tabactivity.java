package com.vritti.sales.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.fragments.ApprovedOrdersFragment;
import com.vritti.sales.fragments.CancelledOrdersFragment;
import com.vritti.sales.fragments.DeliveredOrdersFragment;
import com.vritti.sales.fragments.FullPackedOrdersFragment;
import com.vritti.sales.fragments.OpenOrdersFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharvari on 2/25/2016.
 */
public class MyOrderHistory_Tabactivity extends AppCompatActivity {
    static ListView listview_my_orders_history;
    private static Context parent;
    static OrderHistoryBean bean;
    static String res = "";
    static ArrayList<OrderHistoryBean> arrayList;
    private static DatabaseHandlers databaseHelper;
    String Address,ItemMasterId, Qty, Rate;
    static ProgressHUD progress;
    SharedPreferences sharedpreferences;
    private String image_URL;
    static SQLiteDatabase sql;
    String Reason = "Cancel this order";
    String appCallFrom="";

    Tbuds_commonFunctions tcf;
    Utility ut;
    static DatabaseHandlers db;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "",  usertype = "", username = "";
    String IsChatApplicable, IsGPSLocation;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    String Position;
    private int[] tabIcons = {
            R.drawable.plcord,
            R.drawable.check,
            R.drawable.pckging,
            R.drawable.delscootr,
            R.drawable.cancelord
    };

    @SuppressLint({"ResourceAsColor", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myordhistory_tabactivity);

        parent = MyOrderHistory_Tabactivity.this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.myordhistory)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initViews();

        tcf.clearTable(parent, AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        for(int n = 0; n < tabLayout.getTabCount(); n++){

            View tab = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(n);
            if(tab != null && tab.getBackground() instanceof RippleDrawable){
                RippleDrawable rippleDrawable = (RippleDrawable)tab.getBackground();
                if (rippleDrawable != null) {
                    rippleDrawable.setColor(ColorStateList.valueOf(getResources().getColor(R.color.btncolorlight)));
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

        Intent intent = getIntent();
        appCallFrom = intent.getStringExtra("appName");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            adapter.addFragment(new OpenOrdersFragment(appCallFrom),getResources().getString(R.string.openords));
            adapter.addFragment(new ApprovedOrdersFragment(appCallFrom),getResources().getString(R.string.apprords));
            //adapter.addFragment(new PackagedOrdersFragment(),getResources().getString(R.string.pckgdords));
            adapter.addFragment(new FullPackedOrdersFragment(appCallFrom),getResources().getString(R.string.fulpackord));
            adapter.addFragment(new DeliveredOrdersFragment(appCallFrom),getResources().getString(R.string.delvords));
            adapter.addFragment(new CancelledOrdersFragment(appCallFrom),getResources().getString(R.string.canords));
            viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
    }

}


