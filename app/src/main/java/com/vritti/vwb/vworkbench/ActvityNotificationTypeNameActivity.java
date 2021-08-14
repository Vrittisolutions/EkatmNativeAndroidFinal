package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.activity.ActivityModuleSelection;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.BusinessNotificationTypeAdapter;
import com.vritti.vwb.Beans.BusinessInformation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sharvari on 15-Mar-19.
 */

public class ActvityNotificationTypeNameActivity extends AppCompatActivity  {

    private ListView recyclerView;
    private ArrayList<BusinessInformation> arrayList;
    BusinessNotificationTypeAdapter notificationTypeAdapter;
    ProgressBar progressBar;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    private SharedPreferences userpreferences;
    private String response,url;
    LinearLayout ln_worklist;
    TextView txt_title;
    private String Designation="";
    ImageView img_activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_type_lay);


        ut = new Utility();
        cf = new CommonFunctionCrm(ActvityNotificationTypeNameActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ActvityNotificationTypeNameActivity.this);
        String dabasename = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ActvityNotificationTypeNameActivity.this, dabasename);
        CompanyURL = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        Designation = ut.getValue(ActvityNotificationTypeNameActivity.this, WebUrlClass.GET_Designation, settingKey);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        ln_worklist = findViewById(R.id.ln_worklist);
        txt_title = findViewById(R.id.txt_title);
        img_activity = findViewById(R.id.img_activity);

        if(Designation.equalsIgnoreCase("School")){
            txt_title.setText(UserName+"\n"+"UDISE Number :"+LoginId);
        }


        if(Designation.equalsIgnoreCase("School") && Constants.type == Constants.Type.Sahara){
            ln_worklist.setVisibility(View.VISIBLE);
            ln_worklist.setBackground(getResources().getDrawable(R.drawable.sahara_gradient));
            txt_title.setTextColor(Color.parseColor("#FFFFFF"));
        }

         arrayList=new ArrayList<>();

        recyclerView= (ListView) findViewById(R.id.recyclerView);
        notificationTypeAdapter = new BusinessNotificationTypeAdapter(ActvityNotificationTypeNameActivity.this, arrayList);
        recyclerView.setAdapter(notificationTypeAdapter);


                if (isnet()) {
                    showProgress();
                    new StartSession(ActvityNotificationTypeNameActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadNotificationTypeJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String TypeName=arrayList.get(position).getTypeCode();
                startActivity(new Intent(ActvityNotificationTypeNameActivity.this,ActvityNotificationTypeActivity.class)
                        .putExtra("TypeCode",TypeName));
            }
        });

        img_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(ActvityNotificationTypeNameActivity.this,ActivityMain.class));
                finish();*/
              onBackPressed();

            }
        });

    }



    class DownloadNotificationTypeJSON extends AsyncTask<String, Void, String> {

        Object res;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = CompanyURL + WebUrlClass.api_GetAllNotices;
                res = ut.OpenConnection(url, ActvityNotificationTypeNameActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


            if (response.equalsIgnoreCase("error")||integer.equals("[]")) {
                Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            }

                try {
                    JSONArray jResults = new JSONArray(response);

                    arrayList.clear();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {

                        JSONObject jorder = jResults.getJSONObject(i);

                        BusinessInformation cust = new BusinessInformation();
                        cust.setPKNotificationId(jorder.getString("PKNotificationId"));
                        cust.setTypeName(jorder.getString("TypeName"));
                        cust.setTypeCode(jorder.getString("TypeCode"));
                        cust.setNotificationType(jorder.getString("NotificationType"));
                        cust.setTotalNotification(jorder.getString("TotalNotification"));
                        cust.setNotRead(jorder.getString("NotRead"));
                        cust.setReadNotification(jorder.getString("ReadNotification"));


                        arrayList.add(cust);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                notificationTypeAdapter.notifyDataSetChanged();

                //updateCustList(CusID, flagloc);

            hideProgress();
        }


    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ActvityNotificationTypeNameActivity.this.finish();

    }


}
