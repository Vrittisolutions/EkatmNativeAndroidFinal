package com.vritti.crm.vcrm7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.vritti.crm.adapter.CallListInfoAdapter;
import com.vritti.crm.bean.CallListdataInfo;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharvari on 05-Jun-17.
 */

public class CallsActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    public  static Context context;


    ListView listview_calls;
    SQLiteDatabase sql;
    CallListInfoAdapter callsListAdapter;
    ArrayList<CallListdataInfo> callListdataInfoList;
    Date DOJDate = null;
    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_calls_listview_lay);
        getSupportActionBar().setLogo(R.mipmap.ic_toolbar_logo_crm);
        getSupportActionBar().setTitle(R.string.app_name_toolbar_CRM);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = CallsActivity.this;
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        listview_calls = (ListView) findViewById(R.id.listview_calls);
        callListdataInfoList = new ArrayList<>();
        if (cf.getCallInfocount() > 0) {
            UpdatList();
        }


    }

    private void UpdatList() {
        callListdataInfoList.clear();
        String query = "SELECT * FROM " + db.TABLE_CALLLISTDATA;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CallListdataInfo callListdataInfo = new CallListdataInfo();
                callListdataInfo.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                callListdataInfo.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                callListdataInfo.setProductname(cur.getString(cur.getColumnIndex("Product")));
                callListdataInfo.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                callListdataInfo.setOutcome(cur.getString(cur.getColumnIndex("Outcome")));
                callListdataInfo.setCallId(cur.getString(cur.getColumnIndex("CallId")));

                String jsonDOJ = (cur.getString(cur.getColumnIndex("NextActionDateTime")));
                jsonDOJ = jsonDOJ.substring(jsonDOJ.indexOf("(") + 1, jsonDOJ.lastIndexOf(")"));
                long DOJ_date = Long.parseLong(jsonDOJ);
                DOJDate = new Date(DOJ_date);
                jsonDOJ = sdf1.format(DOJDate);
                callListdataInfo.setNextActionDateTime(jsonDOJ);

                callListdataInfo.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                callListdataInfo.setUsername(cur.getString(cur.getColumnIndex("UserName")));
                callListdataInfoList.add(callListdataInfo);
            } while (cur.moveToNext());

            callsListAdapter = new CallListInfoAdapter(CallsActivity.this, callListdataInfoList);
            listview_calls.setAdapter(callsListAdapter);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CallsActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}
