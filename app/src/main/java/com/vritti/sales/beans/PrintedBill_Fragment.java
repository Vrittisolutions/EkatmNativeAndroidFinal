package com.vritti.sales.beans;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.CounterBilling.PrintedBillDetailsActivity;
import com.vritti.sales.activity.ProductList_TabActivity;
import com.vritti.sales.adapters.BillListAdapter;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class PrintedBill_Fragment extends Fragment {
    private static Context parent;
    SharedPreferences sharedpreferences;

    ListView list_printed_bills;
    ArrayList<CounterbillingBean> printedBillList;
    BillListAdapter billAdapter;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @SuppressLint("ValidFragment")
    public PrintedBill_Fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.tbuds_printed_bill);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parent = getActivity();

        View view = inflater.inflate(R.layout.tbuds_printed_bill, container, false);

        sharedpreferences = parent.getSharedPreferences(WebUrlClass.MyPREFERENCES,Context.MODE_PRIVATE);

        list_printed_bills = (ListView)view.findViewById(R.id.list_printed_bills);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
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

        /*SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =(SearchView)view.findViewById(R.id.searchview);
        searchView.setVisibility(View.VISIBLE);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));*/

        ProductList_TabActivity.myJSONArray = new ArrayList<Merchants_against_items>();

        printedBillList = new ArrayList<CounterbillingBean>();

        //get printed bills records from database
        getPrintedBillsList();

        setListener();

        return view;
    }

    private void setListener() {
        list_printed_bills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String billNo = printedBillList.get(position).getBillNo();
                String billDate = printedBillList.get(position).getDateTime();
                Intent intent = new Intent(getActivity(), PrintedBillDetailsActivity.class);
                intent.putExtra("IntentFrom", "PrintedBills");
                intent.putExtra("BillNo",billNo);
                intent.putExtra("BillDate",billDate);
                startActivity(intent);
            }
        });

    }

    public void getPrintedBillsList(){

        String billNo = "", dateTime = "", billPaybleAmount = "";

        String query = "SELECT DISTINCT BillPrintNo from  "+ databaseHelper.TABLE_BILL_CB + " WHERE isPrinted='Yes'";
        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                billNo = c.getString(c.getColumnIndex("BillPrintNo"));

                String qry = "SELECT Date,PayableAmt from  "+ databaseHelper.TABLE_BILL_CB +
                        " WHERE BillPrintNo="+billNo+" AND isPrinted='Yes' LIMIT 1";
                Cursor c1 = sql_db.rawQuery(qry,null);
                if(c1.getCount() > 0){
                    c1.moveToLast();
                    dateTime = c1.getString(c1.getColumnIndex("Date"));
                    billPaybleAmount = c1.getString(c1.getColumnIndex("PayableAmt"));

                    CounterbillingBean counterbillingBean = new CounterbillingBean();
                    counterbillingBean.setBillNo(billNo);
                    counterbillingBean.setDateTime(dateTime);
                    counterbillingBean.setBillPaybleAmount(billPaybleAmount);

                    printedBillList.add(counterbillingBean);
                }

            }while (c.moveToNext());

            billAdapter = new BillListAdapter(parent, printedBillList);
            list_printed_bills.setAdapter(billAdapter);
            billAdapter.notifyDataSetChanged();

        }else {

        }
    }

}
