package com.vritti.sales.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.ItemListAdapter_customer;
import com.vritti.sales.beans.AddProductsToCart;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.NonOrdered_Fragment;
import com.vritti.sales.beans.OrderedItems_Fragment;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.vwb.Adapter.ProductListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductList_TabActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    String Position;

    String product_name, product_id, product_img;
    TextView textview_product_name, textview_cart_count;
    private static Context parent;
    private ProgressDialog progressDialog;
    private ArrayList<Merchants_against_items> arrayList;
    private ArrayList<Merchants_against_items> arrayList_two;
    public static ArrayList<AllCatSubcatItems> arrayList_items;
    public static ArrayList<Merchants_against_items> myJSONArray;
    private LinearLayout containerLayout_one, containerLayout_two, linearlayout_cart_bottom_layout;
    private LinearLayout layoutOne, layoutTwo;
    ImageView imageview_product_logo_details;
    public static Merchants_against_items bean;
    public Merchants_against_items bean_addnew;
    public static AllCatSubcatItems bean_item;
    private String json;
    ScrollView scrollview_cart_list1, scrollview_cart_list;
    Button button_my_cart_proceed;
    AddProductsToCart addProductsToCart;
    public static ArrayList<AddProductsToCart> addProductsToCartArrayList;
    public static String today;
    ProgressHUD progress;
    String edit_qty;

    public static String[] myItemId;
    ListView ordered_list, non_ordered_list;
    static ProductListAdapter padapter;
    static ItemListAdapter_customer iAdapter;
    Button btn_confirm, btn_cancel, btn_addtoOrderedList;
    static String SubcatID,PurDigit;
    private String[] newuser;
    public static JSONArray jsonArray = null;
    ImageView img_delete_row;
    String ItemId,ItemName;
    float ToatlAmount;
    int qty ;
    int index =0;
    static TextView total_toPay;
    static float amt = 0;
    private String[] user;

    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;
    SearchView searchView;
    EditText edit_List;
    TextView non_orderedList;
    LinearLayout ordl1, ordl2;
    public String jsonData;
    String res = "";

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_product_list__tab);

        parent = ProductList_TabActivity.this;

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Product Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        Bundle b = getIntent().getExtras();

        jsonData = b.getString("user");
        SubcatID = b.getString("SubCategoryId");
        PurDigit = b.getString("PurDigit");

        if (b!=null) {
            Position = b.getString("Position");
        }

        init();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        if(Position!=null){
            viewPager.setCurrentItem(Integer.parseInt(Position));
        }

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        Picasso.with(parent)
                .load(image_URL)//Your image link url
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        //BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                        BitmapDrawable d = new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(bitmap, 110, 50, true));
                     //   ab.setIcon(d);
                     //   ab.setDisplayShowHomeEnabled(true);
                        //ab.setDisplayHomeAsUpEnabled(true);

                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }
                });

        try {
            jsonArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        myItemId = null;
        myItemId = new String[ProductList_TabActivity.jsonArray.length()];

        for (int i = 0; i< ProductList_TabActivity.jsonArray.length(); i++) {

            JSONObject jOBJ = new JSONObject();

            try {

                myItemId[i] = ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemId");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String myString = savedInstanceState.getString("user");
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OrderedItems_Fragment(jsonData,SubcatID,PurDigit), "Ordered Items");
        adapter.addFragment(new NonOrdered_Fragment(jsonData,SubcatID,PurDigit ), "Non-Ordered Items");
        viewPager.setAdapter(adapter);
    }

    private void init() {
        progressDialog = new ProgressDialog(parent);
        // textview_product_name = (TextView) findViewById(R.id.textview_product_name_details);
        arrayList = new ArrayList<Merchants_against_items>();
        arrayList_two = new ArrayList<Merchants_against_items>();
        arrayList_items = new ArrayList<AllCatSubcatItems>();

        myJSONArray = new ArrayList<Merchants_against_items>();
        addProductsToCartArrayList = new ArrayList<AddProductsToCart>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ProductList_TabActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent1 = new Intent(parent, com.vritti.orderbilling.customer.MainActivity.class);
        startActivity(intent1);*/
        //overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
        finish();
    }
}
