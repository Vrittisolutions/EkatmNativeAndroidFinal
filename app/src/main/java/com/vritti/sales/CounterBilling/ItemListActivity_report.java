package com.vritti.sales.CounterBilling;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.Interface.AddItemReport;
import com.vritti.sales.adapters.ItemlistAdapter;
import com.vritti.sales.beans.AdditemBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;

import java.util.ArrayList;


/**
 * Created by sharvari on 7/29/2016.
 */
public class ItemListActivity_report extends AppCompatActivity implements AddItemReport {
        private Context parent;
        private ListView listView;
        ItemlistAdapter itemlistAdapter;
        private ArrayList<AdditemBean> arrayList;
        public static ArrayList<AdditemBean> additemBeanslist;
        Button button_add;
        AdditemBean additemBean;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_add_merchant);

       /* getSupportActionBar().setTitle("Add Frequent Marchant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

            toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle("Daily Transaction Report");
         //   setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
            }

       init();

        getDataFromDatabase();
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < additemBeanslist.size(); i++) {


                }
                ItemListActivity_report.this.finish();
            }
        });

    }

    public void init(){
        parent = ItemListActivity_report.this;

        listView = (ListView) findViewById(R.id.listView_addMerchantlist);
        button_add = (Button) findViewById(R.id.button_add);

        arrayList = new ArrayList<AdditemBean>();
        additemBeanslist = new ArrayList<AdditemBean>();

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ItemListActivity_report.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);


    }

        public void getDataFromDatabase() {

            // TODO Auto-generated method stub

         //  DatabaseHandler db1 = new DatabaseHandler(parent);
         //   SQLiteDatabase db = db1.getWritableDatabase();

            try {
                Cursor c = sql_db.rawQuery("Select distinct ItemMRP, itemname,itemmasterid from "
                        + dbhandler.TABLE_ITEM_MRP, null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    do {
                        additemBean=new AdditemBean();
                        additemBean.setItemmasterid(c.getString(c.getColumnIndex("itemname")));
                        additemBean.setItemMrp(c.getString(c.getColumnIndex("ItemMRP")));
                        additemBean.setItemname(c.getString(c.getColumnIndex("itemmasterid")));
                        arrayList.add(additemBean);

                    } while (c.moveToNext());
                }
            } catch (Exception e) {

            } finally {
                // db.close();
            }
            itemlistAdapter=new ItemlistAdapter(ItemListActivity_report.this, arrayList,
                    this);
            listView.setAdapter(itemlistAdapter);
        }

        @Override
        public void addItemsReports(String itemmasterid, String itemname,String itemmrp,
                                     boolean status) {
            Boolean isProductAdded = false;
            if (additemBeanslist.size() > 0) {
                for (int i = 0; i < additemBeanslist.size(); i++) {
                    if (additemBeanslist.get(i).getItemname()
                            .equalsIgnoreCase(itemname)) {
                        isProductAdded = true;
                    }

                }
            }
            if(isProductAdded) {
                additemBean=new AdditemBean();
                additemBean.setItemmasterid(itemmasterid);
                additemBean.setItemMrp(itemmrp);
                additemBean.setItemname(itemname);
                additemBeanslist.add(additemBean);

            }
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();

                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
