package com.vritti.sales.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.ProspectSettingAdapter;
import com.vritti.sales.beans.ProspectSetting;
import com.vritti.sales.beans.Tbuds_commonFunctions;

import java.util.ArrayList;

public class ProspectSettingsActivity_tbuds extends AppCompatActivity {
        private Context parent;
        ListView list_components;
        //TextView txt_details;
        ArrayList<ProspectSetting> Propectlist;
        ArrayList<ProspectSetting> temp_proslistvisible;
        ArrayList<ProspectSetting> temp_proslistmndt;
        ArrayList<String> Plistdummy;
        ProspectSettingAdapter prospectSettingAdapter;
        ProspectSetting prospectSetting;
        String pname="";
        Toolbar toolbar;
        Button btnsave, btncancel;
        String[] prospect;
        private ArrayList<ProspectSetting> arrayList;
        String KEY_INDIVIDUAL = "Individual Prospect";  //3
        String KEY_SMALL_BUSINESS = "Small Business";   //2
        String KEY_ENTERPRISE = "Enterprise Prospect";  //1
        String HdrID_INDIVIDUAL = "3";  //3
        String HdrID_SMALL_BUSINESS = "2";   //2
        String HdrID_ENTERPRISE = "1";  //1

        static Tbuds_commonFunctions tcf;
        Utility ut;
        static DatabaseHandlers dbhandler;
        Tbuds_commonFunctions cf;
        static String settingKey = "";
        String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
        String IsChatApplicable, IsGPSLocation;
        public static SQLiteDatabase sql_db;
        SharedPreferences sharedpreferences;
        ProgressBar progressBar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.tbuds_activity_prospect_settings);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Indiviual Prospect Setting");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
            }

            initview();

            if(tcf.getCount_prospectSetting() == 0){
                insertInDatabase(prospect.length);
            }else {
                getDataFromDatabase();
            }

            setListener();
        }

        private void initview() {
            parent = ProspectSettingsActivity_tbuds.this;

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
            toolbar.setTitle(R.string.app_name_toolbar_sales);
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);

            list_components = (ListView)findViewById(R.id.list_components);
            // txt_details = findViewById(R.id.txt_details);

            btnsave = (Button)findViewById(R.id.btnsave);
            btncancel = (Button)findViewById(R.id.btncancel);

            ut = new Utility();
            tcf = new Tbuds_commonFunctions(ProspectSettingsActivity_tbuds.this);
            cf = new Tbuds_commonFunctions(ProspectSettingsActivity_tbuds.this);
            String settingKey = ut.getSharedPreference_SettingKey(parent);
            String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            dbhandler = new DatabaseHandlers(parent, dabasename);
            sql_db = dbhandler.getWritableDatabase();
            CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
            PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
            LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
            Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
            UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
            MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);

            Propectlist = new ArrayList<ProspectSetting>();
            temp_proslistvisible = new ArrayList<ProspectSetting>();
            temp_proslistmndt = new ArrayList<ProspectSetting>();
            Plistdummy = new ArrayList<String>();
            arrayList = new ArrayList<ProspectSetting>();
            prospectSetting = new ProspectSetting();

            prospect = new String[]{"Address","Birth Date","Chill test perform","City","Country","District","Email ID","Name",
                    "Gender",/*"Last Name",*/"Marital Status",/*"Middle Name",*/"Mobile","Notes","Phone No","Postal Code",
                    "Reference","Sample Given","Sex","Sold Amount","Source","State","teeth sensitivity","Territory","Test Type","Val10",
                    "Val6","Val7","Val8","Val9","Village"};
        }

        public void setListener(){

            btnsave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //add checked data in table / update from table
                    temp_proslistvisible = prospectSettingAdapter.getAllVisibleChekedData();
                    temp_proslistmndt = prospectSettingAdapter.getAllmandatoryChekedData();

                    //update visibility and mandatory fields
                    updatefromdatabase(temp_proslistvisible,temp_proslistmndt);
                }
            });

            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    public void updatefromdatabase(ArrayList<ProspectSetting> vislist, ArrayList<ProspectSetting> mndtlist){

            if(vislist.size() == 0){
                sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsVisible = 'false'");
                //Toast.makeText(parent,"Visibility record updated successfully",Toast.LENGTH_SHORT).show();
            }else {
                String pid_VISIBLE = "(";

                for(int j=0; j<vislist.size();j++){
                    if(j!=0){
                        pid_VISIBLE = pid_VISIBLE+",";
                    }
                    pid_VISIBLE = pid_VISIBLE + "'" + vislist.get(j).getPKFieldID() +"'";
                }
                pid_VISIBLE = pid_VISIBLE + ")";

                sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsVisible = 'false'");
                sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsVisible = 'true' WHERE PKFieldID IN " + pid_VISIBLE );
               // Toast.makeText(parent,"Visibility record updated successfully",Toast.LENGTH_SHORT).show();
            }

        if(mndtlist.size() == 0){
            sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsMandatory = 'false'");
           // Toast.makeText(parent,"Mandatory record updated successfully",Toast.LENGTH_SHORT).show();
        }else {
            String pid_MNDTRY = "(";
            for(int j=0; j<mndtlist.size();j++){
                if(j!=0){
                    pid_MNDTRY = pid_MNDTRY+",";
                }
                pid_MNDTRY = pid_MNDTRY + "'" + mndtlist.get(j).getPKFieldID() +"'";
            }
            pid_MNDTRY = pid_MNDTRY + ")";

            sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsMandatory = 'false'");
            sql_db.execSQL("UPDATE " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES + " SET IsMandatory = 'true' WHERE PKFieldID IN " + pid_MNDTRY );
           // Toast.makeText(parent,"Mandatory record updated successfully",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(parent,"Prospect settings saved successfully",Toast.LENGTH_SHORT).show();

        finish();
    }

    public void insertInDatabase(int length){
        for(int i = 0; i<length; i++){
            pname = prospect[i];
            Plistdummy.add(pname);

            prospectSetting = new ProspectSetting();
            prospectSetting.setPKUserId(UserMasterId);
            prospectSetting.setFKProspectHdrID(HdrID_INDIVIDUAL);    //Individual=3,
            prospectSetting.setProspectField(pname);
            prospectSetting.setIsvisiblecheck(true);
            prospectSetting.setIsmandatorycheck(false);
            prospectSetting.setCaption(pname);
            prospectSetting.setSection(KEY_INDIVIDUAL);
            prospectSetting.setFieldType("");
            Propectlist.add(prospectSetting);

            tcf.addProspSettings(prospectSetting);
        }

        getDataFromDatabase();
    }

    public void getDataFromDatabase(){

        Propectlist.clear();

        boolean is_visible, is_mandatory;
        String PKFieldID = "";

        String query = " SELECT * FROM " + dbhandler.TABLE_PROSPECT_VALIDATIONS_SALES ;

        Cursor c = sql_db.rawQuery(query,null);
        if(c.getCount() > 0){
            c.moveToFirst();

            do{
                is_visible = Boolean.parseBoolean(c.getString(c.getColumnIndex("IsVisible")));
                is_mandatory = Boolean.parseBoolean(c.getString(c.getColumnIndex("IsMandatory")));
                UserMasterId = c.getString(c.getColumnIndex("PKUserId"));
                HdrID_INDIVIDUAL = c.getString(c.getColumnIndex("FKProspectHdrID"));
                pname = c.getString(c.getColumnIndex("ProspectField"));
                KEY_INDIVIDUAL = c.getString(c.getColumnIndex("Section"));
                PKFieldID = c.getString(c.getColumnIndex("PKFieldID"));

                prospectSetting = new ProspectSetting();
                prospectSetting.setPKUserId(UserMasterId);
                prospectSetting.setFKProspectHdrID(HdrID_INDIVIDUAL);    //Individual=3,
                prospectSetting.setProspectField(pname);
                prospectSetting.setIsvisiblecheck(is_visible);
                prospectSetting.setIsmandatorycheck(is_mandatory);
                prospectSetting.setCaption(pname);
                prospectSetting.setSection(KEY_INDIVIDUAL);
                prospectSetting.setFieldType("");
                prospectSetting.setPKFieldID(PKFieldID);
                Propectlist.add(prospectSetting);

            }while(c.moveToNext());

            //retrieve from database and then set visibility
            prospectSettingAdapter = new ProspectSettingAdapter(this, Propectlist);
            list_components.setAdapter(prospectSettingAdapter);

        }else {

        }
    }
}