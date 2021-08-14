package com.vritti.vwblib.vworkbench;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;



import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Adapter.BirthdayMainAdapter;
import com.vritti.vwblib.Beans.BirthdayBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;

/**
 * Created by 300151 on 10/13/16.
 */
public class BirthdayListAcyivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView ls_birthday;
    SQLiteDatabase sql;
    ArrayList<BirthdayBean> lsBirthdayList;
    BirthdayMainAdapter BirthdayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_birthday_main);
        InitView();
        UpdateBirthdayList();

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);

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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        sql=db.getWritableDatabase();
        lsBirthdayList = new ArrayList<BirthdayBean>();
        ls_birthday = (ListView) findViewById(R.id.ls_birthday);
    }

    private void UpdateBirthdayList() {
        String query = "SELECT * FROM " + db.TABLE_BIRTHDAY;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                BirthdayBean bean = new BirthdayBean();
                bean.setDOB(cur.getString(cur.getColumnIndex("DOB")));
                bean.setDOJ(cur.getString(cur.getColumnIndex("DOJ")));
                bean.setDtDay(cur.getString(cur.getColumnIndex("DtDay")));
                bean.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                bean.setTitle(cur.getString(cur.getColumnIndex("Title")));
                bean.setUserLoginId(cur.getString(cur.getColumnIndex("UserLoginId")));
                bean.setImagePath(cur.getString(cur.getColumnIndex("ImagePath")));
                bean.setEmail(cur.getString(cur.getColumnIndex("Email")));
                bean.setUserMasterID(cur.getString(cur.getColumnIndex("UserMasterID")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                lsBirthdayList.add(bean);
            }while (cur.moveToNext());
        }
        BirthdayAdapter = new BirthdayMainAdapter(BirthdayListAcyivity.this, lsBirthdayList);
        ls_birthday.setAdapter(BirthdayAdapter);
    }


}
