package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.MeetingAdapter;
import com.vritti.vwb.Beans.MeetingBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

public class MeetingActivity extends AppCompatActivity {
    ListView lst_meeting;
    SQLiteDatabase sql;
    ArrayList<MeetingBean> meetingBeanArrayList;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    // BirthdayMainAdapter BirthdayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_meeting);
        InitView();

        //UpdateMeetingList();

        if(ut.isNet(MeetingActivity.this)){
            new StartSession(MeetingActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadMeetingJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        meetingBeanArrayList = new ArrayList<MeetingBean>();
        lst_meeting = (ListView) findViewById(R.id.lst_meeting);
    }

    private void UpdateMeetingList() {
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
          /*  MeetingBean meetingBean = new MeetingBean();
            meetingBean.setMOMId("");
            meetingBean.setMeetTime("");
            meetingBean.setMeetVenue("");
            meetingBean.setMOMDate("");
            meetingBean.setMOMTitle("No meeting schedule");
            meetingBeanArrayList.add(meetingBean);*/
/*
          if(ut.isNet(MeetingActivity.this)){
              new StartSession(MeetingActivity.this, new CallbackInterface() {
                  @Override
                  public void callMethod() {
                      new DownloadMeetingJSON().execute();
                  }

                  @Override
                  public void callfailMethod(String msg) {

                  }
              });
          }*/
        }
        if(meetingBeanArrayList.size() != 0) {
            MeetingAdapter meetingAdapter = new MeetingAdapter(MeetingActivity.this, meetingBeanArrayList);
            lst_meeting.setAdapter(meetingAdapter);
        }else{
            Toast.makeText(MeetingActivity.this, "No Meeting Schedule", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MeetingActivity.this.finish();
    }

    class DownloadMeetingJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Meetings;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MEETING, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MEETING, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MEETING, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (response != null) {
                UpdateMeetingList();
            }else{

            }


        }

    }
}
