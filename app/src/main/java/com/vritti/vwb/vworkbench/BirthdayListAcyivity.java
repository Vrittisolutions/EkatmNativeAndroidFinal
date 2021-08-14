package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ListObject;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.Adapter.BirthdayMainAdapter;
import com.vritti.vwb.Beans.BirthdayBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 10/13/16.
 */
public class BirthdayListAcyivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    RecyclerView ls_birthday;
    SQLiteDatabase sql;
    ArrayList<BirthdayBean> lsBirthdayList;
    BirthdayMainAdapter BirthdayAdapter;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_birthday_main);
        InitView();
        UpdateBirthdayList();

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        /*img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/
        txt_title.setText("Birthdays");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
        lsBirthdayList = new ArrayList<BirthdayBean>();
        ls_birthday =  findViewById(R.id.ls_birthday);
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
                String imagepath=cur.getString(cur.getColumnIndex("ImagePath"));
                bean.setImagePath(CompanyURL+imagepath);
                bean.setEmail(cur.getString(cur.getColumnIndex("Email")));
                bean.setUserMasterID(cur.getString(cur.getColumnIndex("UserMasterID")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                lsBirthdayList.add(bean);
            } while (cur.moveToNext());
        }
        /*LinkedHashMap<String, Set<BirthdayBean>> groupedHashMap = new LinkedHashMap<>();
        Set<BirthdayBean> list = null;
        for(BirthdayBean birthdayBean : lsBirthdayList){

            String hashMapKey = "";
            if(birthdayBean.getDtDay().contains("today")) {
                hashMapKey = "Today";

            }else {
                hashMapKey = birthdayBean.getDtDay();
            }
                if (groupedHashMap.containsKey(hashMapKey)) {
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    groupedHashMap.get(hashMapKey).add(birthdayBean);
                } else {
                    // The key is not there in the HashMap; create a new key-value pair
                    list = new LinkedHashSet<>();
                    list.add(birthdayBean);
                    groupedHashMap.put(hashMapKey, list);
                }

        }

        for(int i = 0 ; i < lsBirthdayList.size() ; i++ ){
            if(lsBirthdayList.get(i).getDtDay().contains("Today")){
                lsBirthdayList.get(i).setType("Today");
            }else {
                lsBirthdayList.get(i).setType(lsBirthdayList.get(i).getDtDay());
            }
        }*/
        BirthdayAdapter = new BirthdayMainAdapter(BirthdayListAcyivity.this, lsBirthdayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        ls_birthday.setLayoutManager(mLayoutManager);
        ls_birthday.setAdapter(BirthdayAdapter);

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);


    }
}
