package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;

public class LocationOptionsSelection extends AppCompatActivity {

        String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
        Utility ut;
        DatabaseHandlers db;
        CommonFunction cf;
        SharedPreferences userpreferences;
        SQLiteDatabase sql;
        private Context pContext;
        ArrayList<DataModel> dataModels;
        ListView listView;
        private static CustomAdapter adapter;


        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.location_transfer_list);
            pContext = LocationOptionsSelection.this;
            getSupportActionBar().setTitle("Location Transfer");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
            ut = new Utility();
            cf = new CommonFunction(pContext);
            String settingKey = ut.getSharedPreference_SettingKey(pContext);
            String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(pContext, dabasename);
            sql = db.getWritableDatabase();
            CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
            PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
            LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
            Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
            UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
            IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);



            listView=findViewById(R.id.list);

            dataModels= new ArrayList<>();

            dataModels.add(new DataModel("Good to Good location", "GG"));
            dataModels.add(new DataModel("Good to Reject location", "GR"));
            dataModels.add(new DataModel("Reject to Good location", "RG"));
            dataModels.add(new DataModel("Reject to Reject location", "RR"));
            dataModels.add(new DataModel("Packing buffer to Good location", "PG"));
            dataModels.add(new DataModel("Packing buffer to Reject location", "PR"));

            adapter= new CustomAdapter(dataModels,getApplicationContext());

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    DataModel dataModel= dataModels.get(position);

                    startActivity(new Intent(LocationOptionsSelection.this, GRNScanner.class).
                            putExtra("loc_transcode",dataModel.getLocationcode()).
                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));




                }
            });
        }



    public class DataModel {

        String name;
        String locationcode;

        public DataModel(String name, String locationcode) {
            this.name=name;
            this.locationcode=locationcode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocationcode() {
            return locationcode;
        }

        public void setLocationcode(String locationcode) {
            this.locationcode = locationcode;
        }
    }

    public class CustomAdapter extends ArrayAdapter<DataModel>{

        private ArrayList<DataModel> dataSet;
        Context mContext;

        // View lookup cache
        private class ViewHolder {
            TextView txt;
            }

        public CustomAdapter(ArrayList<DataModel> data, Context context) {
            super(context, R.layout.crm_spinner_text, data);
            this.dataSet = data;
            this.mContext=context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            DataModel dataModel = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.crm_spinner_text, parent, false);
                viewHolder.txt = (TextView) convertView.findViewById(R.id.txt);
                viewHolder.txt.setBackgroundColor(Color.WHITE);
                viewHolder.txt.setTextSize(18);
                viewHolder.txt.setPadding(10,10,10,10);


                result=convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

            viewHolder.txt.setText(dataModel.getName());

            return convertView;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}
