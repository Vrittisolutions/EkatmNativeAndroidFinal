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
import com.vritti.crmlib.adapter.MeetingAdapter;
import com.vritti.crmlib.bean.MeetingBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

public class MeetingActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView lst_meeting;
    SQLiteDatabase sql;
    ArrayList<MeetingBean> meetingBeanArrayList;

    // BirthdayMainAdapter BirthdayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_meeting);
        InitView();
        UpdateBirthdayList();
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_action);

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
        meetingBeanArrayList = new ArrayList<MeetingBean>();
        lst_meeting = (ListView) findViewById(R.id.lst_meeting);
    }

    private void UpdateBirthdayList() {
        String query = "SELECT MOMId ,MOMDate ,MOMTitle ,MeetTime ,MeetVenue FROM "
                + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MeetingBean meetingBean = new MeetingBean();
                meetingBean.setMOMId(cur.getString(cur.getColumnIndex("MOMId")));
                meetingBean.setMeetTime(cur.getString(cur.getColumnIndex("MeetTime")));
                meetingBean.setMeetVenue(cur.getString(cur.getColumnIndex("MeetVenue")));
                meetingBean.setMOMDate(cur.getString(cur.getColumnIndex("MOMDate")));
                meetingBean.setMOMTitle(cur.getString(cur.getColumnIndex("MOMTitle")));
                meetingBeanArrayList.add(meetingBean);

            } while (cur.moveToNext());
        } else {
            MeetingBean meetingBean = new MeetingBean();
            meetingBean.setMOMId("");
            meetingBean.setMeetTime("");
            meetingBean.setMeetVenue("");
            meetingBean.setMOMDate("");
            meetingBean.setMOMTitle("No meeting schedule");
            meetingBeanArrayList.add(meetingBean);
        }
        MeetingAdapter meetingAdapter = new MeetingAdapter
                (MeetingActivity.this, meetingBeanArrayList);
        lst_meeting.setAdapter(meetingAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MeetingActivity.this.finish();
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
