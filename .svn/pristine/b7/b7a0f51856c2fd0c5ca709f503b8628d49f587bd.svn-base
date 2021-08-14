package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.Beans.ClaimNotification;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by sharvari on 31-May-18.
 */

public class ClaimNotificationActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView claim_notification_listview;
    ArrayList<ClaimNotification>claimNotificationArrayList;

    public  SQLiteDatabase sql;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_claimnotification_listvview_lay);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_toolbar_logo_vwb);
        getSupportActionBar().setTitle(R.string.app_name_toolbar_Vwb);

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
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
        claim_notification_listview=findViewById(R.id.claim_notification_listview);
        claimNotificationArrayList=new ArrayList<>() ;


        claimNotificationArrayList.clear();
        claimNotificationArrayList=new ArrayList<>() ;

        String query = "SELECT * FROM " + db.TABLE_CLAIM_NOTIFICATION;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ClaimNotification claimNotification= new ClaimNotification();
                String  Data = cur.getString(cur.getColumnIndex("Data"));
                claimNotification.setMessage(Data);
                claimNotificationArrayList.add(claimNotification);


            } while (cur.moveToNext());

            MySpinnerAdapter customoption = new MySpinnerAdapter(ClaimNotificationActivity.this,
                    R.layout.vwb_custom_spinner_txt, claimNotificationArrayList);
            claim_notification_listview.setAdapter(customoption);

        }


    }
    private  class MySpinnerAdapter extends ArrayAdapter<ClaimNotification> {
        ArrayList<ClaimNotification> claimNotificationArrayList = new ArrayList<>();


        public MySpinnerAdapter(Context context, int textViewResourceId, ArrayList<ClaimNotification> claimNotificationArrayList) {
            super(context, textViewResourceId, claimNotificationArrayList);
            this.claimNotificationArrayList=claimNotificationArrayList;
            claimNotificationArrayList = claimNotificationArrayList;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vwb_claim_notification_item_lay, null);
            TextView textView = (TextView) v.findViewById(R.id.txt_message);
            textView.setText(claimNotificationArrayList.get(position).getMessage());

            return v;

        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }

}
