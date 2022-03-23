package com.vritti.sales.CounterBilling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.Interface.AddItemReport;
import com.vritti.sales.adapters.ItemlistAdapter;
import com.vritti.sales.beans.AdditemBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sharvari on 7/29/2016.
 */
public class ReportActivity extends AppCompatActivity implements AddItemReport {

    Button buttonItem, buttonFdate, buttonTdate;
    private Context parent;
    private ListView listView;
    ItemlistAdapter itemlistAdapter;
    private ArrayList<AdditemBean> arrayList;
    public static ArrayList<AdditemBean> additemBeanslist;
    Button button_add, buttonDailySale, buttonTotalSale, buttonCancel;
    AdditemBean additemBean;
    String itemname = null;
    String itemid = "";
    static int year, month, day;
    String From_date = null, To_date = null;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_sale);

        /*getSupportActionBar().setTitle("Reports");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        Listeners();

    }

    private void init() {
        buttonItem = (Button) findViewById(R.id.buttonItem);
        buttonFdate = (Button) findViewById(R.id.buttonFdate);
        buttonTdate = (Button) findViewById(R.id.buttonTdate);
        arrayList = new ArrayList<AdditemBean>();
        additemBeanslist = new ArrayList<AdditemBean>();
        buttonDailySale = (Button) findViewById(R.id.buttonDailySale);
        buttonTotalSale = (Button) findViewById(R.id.buttonTotalSale);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ReportActivity.this);
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

    private void Listeners() {
        buttonItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                additemBeanslist.clear();

                final Dialog dialog = new Dialog(ReportActivity.this);
                dialog.setContentView(R.layout.tbuds_activity_add_items_report);
                dialog.setTitle("Item List");
                listView = (ListView) dialog.findViewById(R.id.listView_additemlist);
                button_add = (Button) dialog.findViewById(R.id.button_addItem);
                getDataFromDatabase();
                dialog.show();

                button_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (additemBeanslist.size() > 0) {
                            for (int i = 0; i < additemBeanslist.size(); i++) {

                                if (i == 0) {
                                    itemname = additemBeanslist.get(0).getItemname();
                                    itemid = additemBeanslist.get(0).getItemmasterid();
                                } else {
                                    itemname = itemname + " , " + additemBeanslist.get(i).getItemname();
                                    itemid = itemid + " , " + additemBeanslist.get(0).getItemmasterid();
                                }
                            }
                        }
                        if (itemname == null) {
                            buttonItem.setText("Select Item");
                        } else {
                            buttonItem.setText(itemname);
                        }
                        dialog.dismiss();
                    }
                });

               // buttonItem.setText(itemname);
            }
        });

        buttonFdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                buttonFdate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                From_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                      /*  fromdate = year + "-" + String.format("%02d", (monthOfYear + 1))
                                + "-" + dayOfMonth + " 00:00:01.000";*/

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });
        buttonTdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                //  c.add(Calendar.DAY_OF_MONTH, 30);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                buttonTdate.setText(dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year);

                                To_date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                //  toDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" +
                                // dayOfMonth + " 00:00:01.000";

                            }
                        }, year, month, day);

                datePickerDialog.show();
            }
        });


        buttonDailySale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (itemname != null && From_date != null && To_date != null) {
                    Intent intent = new Intent(ReportActivity.this, DailySaleActivity.class);
                    intent.putExtra("Items", itemid);
                    intent.putExtra("FromDate", From_date);
                    intent.putExtra("ToDate", From_date);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportActivity.this,
                            "Enter Item and select dates", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonTotalSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemname != null && From_date != null && To_date != null) {
                    Intent intent = new Intent(ReportActivity.this, TotalSaleActivity.class);
                    intent.putExtra("Items", itemid);
                    intent.putExtra("FromDate", From_date);
                    intent.putExtra("ToDate", To_date);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportActivity.this,
                            "Enter Item and select dates", Toast.LENGTH_LONG).show();
                }
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportActivity.this.finish();
            }
        });

    }


    public void getDataFromDatabase() {
      /*  sql += "SELECT * FROM " + TABLE_MARCHANT_ITEM_RUNI;
        sql += " WHERE ItemName LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY ItemMasterId DESC";
        ItemMasterId
        ItemName*/
        // TODO Auto-generated method stub
        arrayList.clear();
    //    DatabaseHandler db1 = new DatabaseHandler(ReportActivity.this);
    //    SQLiteDatabase db = db1.getWritableDatabase();
      /*  + "(Itemid TEXT, Itemname TEXT, NMrpV TEXT, " +
                "QtyV TEXT,UnitV TEXT,IsUploaded TEXT)";*/
        try {
            Cursor c = sql_db.rawQuery("Select distinct Itemname,Itemid from "
                    + dbhandler.TABLE_ITEM_MRP_Runi, null);
            AdditemBean additemBean1 = new AdditemBean();
            additemBean1.setItemname("All Items");
            additemBean1.setItemmasterid("All Items");
            arrayList.add(additemBean1);


            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    additemBean = new AdditemBean();
                    additemBean.setItemmasterid(c.getString(c.getColumnIndex("Itemid")));
                    // additemBean.setItemMrp(c.getString(c.getColumnIndex("NMrpV")));
                    additemBean.setItemname(c.getString(c.getColumnIndex("Itemname")));
                    arrayList.add(additemBean);

                } while (c.moveToNext());
            }
        } catch (Exception e) {

        } finally {
            // db.close();
        }
        itemlistAdapter = new ItemlistAdapter(ReportActivity.this, arrayList, this);
        listView.setAdapter(itemlistAdapter);
    }


    @Override
    public void addItemsReports(String itemmasterid, String itemname, String itemmrp,
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
        if (!isProductAdded) {
            AdditemBean bean = new AdditemBean();
            bean.setItemmasterid(itemmasterid);
            bean.setItemMrp(itemmrp);
            bean.setItemname(itemname);
            additemBeanslist.add(bean);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}