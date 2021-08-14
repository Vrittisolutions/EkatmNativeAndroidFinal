package com.vritti.crmlib.vcrm7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.NotificationAdapter;
import com.vritti.crmlib.bean.NotificationBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

public class NotificationActivity extends AppCompatActivity {
    ListView ls_notification;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;
    ArrayList<NotificationBean> notificationBeanArrayList;

    // BirthdayMainAdapter BirthdayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_notification);

        InitView();
        UpdateBirthdayList();
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        sql = db.getWritableDatabase();
        notificationBeanArrayList = new ArrayList<NotificationBean>();
        ls_notification = (ListView) findViewById(R.id.ls_notification);
    }

    private void UpdateBirthdayList() {
        String query = "SELECT PKNotifDtlsId," +
                "            NotifText, NotifTitle," +
                "            NotificationTypeId, TypeName, UserName, AddedDt FROM "
                + db.TABLE_NOTIFICATION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                NotificationBean notificationBean = new NotificationBean();
                notificationBean.setNotificationTypeId(cur.getString(cur.getColumnIndex("NotificationTypeId")));
                notificationBean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                notificationBean.setNotifText(cur.getString(cur.getColumnIndex("NotifText")));
                notificationBean.setNotifTitle(cur.getString(cur.getColumnIndex("NotifTitle")));
                notificationBean.setPKNotifDtlsId(cur.getString(cur.getColumnIndex("PKNotifDtlsId")));
                notificationBean.setTypeName(cur.getString(cur.getColumnIndex("TypeName")));
                notificationBean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                notificationBeanArrayList.add(notificationBean);

            } while (cur.moveToNext());
        } else {
           /* NotificationBean notificationBean = new NotificationBean();
            notificationBean.setNotificationTypeId("");
            notificationBean.setAddedDt("");
            notificationBean.setNotifText("");
            notificationBean.setNotifTitle("");
            notificationBean.setPKNotifDtlsId("");
            notificationBean.setTypeName("");
            notificationBean.setUserName("");
            notificationBeanArrayList.add(notificationBean);*/
        }
        NotificationAdapter notificationAdapter = new NotificationAdapter
                (NotificationActivity.this, notificationBeanArrayList);
        ls_notification.setAdapter(notificationAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NotificationActivity.this.finish();
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
