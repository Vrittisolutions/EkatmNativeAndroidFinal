package com.vritti.inventory.physicalInventory.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.qs.helper.printer.PrinterClass;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.UOMBean;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CounterAuditorSelectActvity extends AppCompatActivity {
    private Context parent;
    AppCompatRadioButton radio_online,radio_ofline,radio_counter,radio_auditor,radio_immediate,radio_batch;
    String Onstatus="",Costatus="",Bostatus="";
    Button btn_next;
    ImageView menuoption;

    private SharedPreferences sharedPrefs;
    String radStatus = "";
    String labelSize = "";
    String isCounterAuditor = "";

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.option_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        Intent intent=getIntent();
        int position=intent.getIntExtra("position",0);

        if(BluetoothConnectivityActivity.pl!=null&&(position==0||position==1)&& BluetoothConnectivityActivity.pl.getState() != PrinterClass.STATE_CONNECTED)
        {
            intent=new Intent();
            intent.setClass(CounterAuditorSelectActvity.this, Devicelist_NewPrinterSetting.class);
            startActivityForResult(intent, 0);
        }

        //showalert();

        //downloadUOMMaster
        callUOMAPI();

        setListeners();

    }

    private void init() {
        parent = CounterAuditorSelectActvity.this;

        /*radio_online=findViewById(R.id.radio_online);
        radio_ofline=findViewById(R.id.radio_ofline);*/
        radio_counter=findViewById(R.id.radio_counter);
        radio_auditor=findViewById(R.id.radio_auditor);
       /* radio_immediate=findViewById(R.id.radio_immediate);
        radio_batch=findViewById(R.id.radio_batch);*/
        btn_next=findViewById(R.id.btn_next);
        menuoption= findViewById(R.id.menuoption);

        ut = new Utility();
        cf = new CommonFunction(CounterAuditorSelectActvity.this);
        String settingKey = ut.getSharedPreference_SettingKey(CounterAuditorSelectActvity.this);
        String dabasename = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(CounterAuditorSelectActvity.this, dabasename);
        CompanyURL = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(CounterAuditorSelectActvity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
        isCounterAuditor = sharedPrefs.getString("isCounterAuditor", "");

        Costatus = "Counter";

        if(radio_counter.isChecked()){
           Costatus = radio_counter.getText().toString();
        }else {
            Costatus = radio_auditor.getText().toString();
        }

    }

    public void setListeners(){

        radio_counter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radio_counter.setChecked(true);
                    Costatus=radio_counter.getText().toString();

                }else {

                }
            }
        });
        radio_auditor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    radio_auditor.setChecked(true);
                    Costatus=radio_auditor.getText().toString();

                }else {

                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(parent,BatchSelectionActivity.class);
                startActivity(intent);*/


                Toast.makeText(parent,"You have opted for "+Costatus,Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("isCounterAuditor",Costatus);
                editor.commit();

                startActivity(new Intent(parent, BatchSelectionActivity.class)
                        .putExtra("Costatus",Costatus)
                        // .putExtra("status",Onstatus)
                        // .putExtra("bathchprint",Bostatus)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

               /* if (Costatus.equals("Counter")){

                    startActivity(new Intent(parent,PIEntryPrintingActivity.class)
                            .putExtra("CounterAuditorStatus",Costatus)
                           // .putExtra("status",Onstatus)
                           // .putExtra("bathchprint",Bostatus)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }else if (Costatus.equals("Auditor")) {
                    startActivity(new Intent(parent,AudiScreenActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }*/

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.itemsync){
            Intent intent = new Intent(this, ItemMasterSyncActivity.class);
            startActivity(intent);

        }/*else if(id == R.id.audit){
            Intent intent = new Intent(this, AudiScreenActivity.class);
            startActivity(intent);

        }*//*else if(id == R.id.edit_delete){
            Intent intent = new Intent(this, EditDeleteScreenActivity.class);
            startActivity(intent);

        }*/else if(id == R.id.reprint){
            Intent intent = new Intent(this, ReprintScreenActivity.class);
            startActivity(intent);
        }else if(id == R.id.labelsize){
            //show dialogue alert
            Intent intent = new Intent(this, SelectLableSize.class);
            startActivity(intent);
            //showalert();
        }else if(id == R.id.refresh){

            if (isnet()) {
                new StartSession(CounterAuditorSelectActvity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                    }
                    @Override
                    public void callfailMethod(String msg) {
                       // ut.displayToast(getApplicationContext(), msg);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
            return super.onOptionsItemSelected(item);

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void showalert() {
        // TODO Auto-generated method stub

        final Dialog myDialog = new Dialog(CounterAuditorSelectActvity.this);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.label_selection);
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        String msg = "Select label size";

        /*final TextView quest = myDialog.findViewById(R.id.textMsg);
        quest.setText(Html.fromHtml(msg)); */     //details message here

        Button btnyes, btn2_2, btn3_2;
      //  final AppCompatRadioButton label2_2mm, label3_2mm;
        btnyes = myDialog.findViewById(R.id.btn_yes);
        btn2_2 = myDialog.findViewById(R.id.btn2_2);
        btn3_2 = myDialog.findViewById(R.id.btn3_2);
       /* label2_2mm = findViewById(R.id.label2_2mm);
        label3_2mm = findViewById(R.id.label3_2mm);*/

       /* label2_2mm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    label2_2mm.setChecked(true);
                    //  radStatus[0] = label2_2mm.getText().toString();

                }else {

                }
            }
        });

        label3_2mm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    label3_2mm.setChecked(true);
                    //  radStatus[0] = label3_2mm.getText().toString();

                }else {

                }
            }
        });*/

        btnyes.setText("Ok");

        btn2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStatus = "2x2mm";
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("labelSize",radStatus);
                editor.commit();

                myDialog.dismiss();
            }
        });

        btn3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radStatus = "3x2mm";
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("labelSize",radStatus);
                editor.commit();
                /*sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                labelSize = sharedPrefs.getString("labelSize", radStatus);*/

                myDialog.dismiss();
            }
        });

        /*btnyes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //proceed further

                *//*if(label2_2mm.isChecked()){
                    radStatus = "2x2mm";
                }else if(label3_2mm.isChecked()){
                    radStatus = "3x2mm";
                }*//*

                sharedPrefs = PreferenceManager.getDefaultSharedPreferences(CounterAuditorSelectActvity.this);
                labelSize = sharedPrefs.getString("labelSize", radStatus);

                myDialog.dismiss();
            }
        });*/

       /* btnno.setText("NO");
        btnno.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                //cancel submition and cleart edittext
                myDialog.dismiss();
            }
        });*/

        myDialog.show();
    }

    public void callUOMAPI(){
        if (isnet()) {
            new StartSession(CounterAuditorSelectActvity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadUOM().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                @Override
                public void callfailMethod(String msg) {
                   // ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pi, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class DownloadUOM extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetBatchList;
            String url = CompanyURL + WebUrlClass.api_UOM;

            try {
                res = ut.OpenConnection(url, parent);
                if (res != null) {
                    response = res.toString();
                    response = response.toString().replaceAll("\\\\","");
                    response = response.substring(1, response.length() - 1);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (response.contains("[]")) {
                Toast.makeText(parent, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                parseUOMJson(response);
            }
        }
    }

    public void parseUOMJson(String response){

        sql.delete(db.TABLE_UOM, null, null);

        JSONArray jResults = null;
        try {
            jResults = new JSONArray(response);

            ArrayList<UOMBean> beanUOMlist = new ArrayList<>();

            for (int i = 0; i < jResults.length(); i++) {
                JSONObject jorder = jResults.getJSONObject(i);

                UOMBean beanUOM = new UOMBean();

                beanUOM.setUOMMasterId(jorder.getString("UOMMasterId"));
                beanUOM.setUOMCode(jorder.getString("UOMCode"));

                cf.insertUOM(jorder.getString("UOMMasterId"), jorder.getString("UOMCode"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}