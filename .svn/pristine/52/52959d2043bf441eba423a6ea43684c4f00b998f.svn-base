package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.NestedSubteamAdapter;
import com.vritti.vwb.Beans.MyTeamDeptBean;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Nested_SubTeam extends AppCompatActivity {

    String PlantMasterId = "", CompanyURL = "", EnvMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SharedPreferences userpreferences;
    SharedPreferences timesheetpreferences;
    private Toolbar toolbar;
    ListView teamlist;
    private ArrayList<MyTeamDeptBean> Mynestedsubteam;
    NestedSubteamAdapter mynestedsubteamActivityAdapter;
    String DeptID;
    ProgressBar mProgress;
    String User_Id,User_Name;
    SQLiteDatabase sql;
    ImageView location;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_suteam_nested);
        context = getApplicationContext();

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);

        initObj();
        initView();
        // setListner();
        getData();


        Intent intent = getIntent();
        User_Id = intent.getStringExtra("UserMasterID");
        User_Name = intent.getStringExtra("UserName");



        String settingKey = ut.getSharedPreference_SettingKey(context);
        String databasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY,settingKey);
        db =new DatabaseHandlers(context,databasename);
        CompanyURL = ut.getValue(context,WebUrlClass.GET_COMPANY_URL_KEY,settingKey);//a207
        EnvMasterId = ut.getValue(context,WebUrlClass.GET_EnvMasterID_KEY,settingKey);//vritti
        PlantMasterId = ut.getValue(context,WebUrlClass.GET_PlantID_KEY,settingKey);//1

    }

    private void initObj() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);

        setSupportActionBar(toolbar);

        Mynestedsubteam = new ArrayList<MyTeamDeptBean>();

    }
    private void initView() {
        teamlist = (ListView) findViewById(R.id.list_team);
        // mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

        location = (ImageView) findViewById(R.id.location);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Nested_SubTeam.this, GPSTeamList.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);

            }
        });
    }
    private void getData() {
        //  showProgressDialog();
        new StartSession(Nested_SubTeam.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                Downloadmysubteam mySubTeamData = new Downloadmysubteam();
                mySubTeamData.execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
                hideProgressDialog();
            }
        });


    }
    private void hideProgressDialog() {
        mProgress.setVisibility(View.GONE);

    }

    private  class Downloadmysubteam extends AsyncTask<String, Void, String> {
        Object res;
        Cursor c;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response;
            String url;


            try {

                url = CompanyURL + WebUrlClass.api_vwb_getSubTeamMembers+ "?UserMasterId="+User_Id;
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }



            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);


            if (res.contains("UserMasterId")) {
                ContentValues values = new ContentValues();
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(res);
                    SQLiteDatabase sql = db.getWritableDatabase();

                    sql.delete(db.TABLE_MYSUBTEAM_DEPT, null,
                            null);
                    try {
                        c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYSUBTEAM_DEPT, null);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                    }
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);

                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                            columnValue = jorder.getString(columnName);
                            if (columnValue == null) {
                                columnValue = "";
                            }
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_MYSUBTEAM_DEPT, null, values);
                        Log.e("Cnt ", "" + a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateTeam();
            } else if (res.contains(WebUrlClass.Errormsg)) {
                ut.displayToast(getApplicationContext(), "Could not connect...");

            } else {
                ut.displayToast(getApplicationContext(), "No Data Present...");
            }

        }
    }

    private void updateTeam() {
        Mynestedsubteam.clear();
        sql = db.getWritableDatabase();
        String que = "SELECT * FROM " + db.TABLE_MYSUBTEAM_DEPT;
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MyTeamDeptBean bean = new MyTeamDeptBean();
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                //  bean.setMobileActive(cur.getString(cur.getColumnIndex("MobileActive")));
                bean.setTotalAssigned(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setMobileUser(cur.getString(cur.getColumnIndex("MobileUser")));
                bean.setReport(cur.getString(cur.getColumnIndex("Report")));
                bean.setTotalOverdueActivities(cur.getString(cur.getColumnIndex("TotalOverdueActivities")));
                bean.setAwaitingActivities(cur.getString(cur.getColumnIndex("AwaitingActivities")));
                bean.setCritical(cur.getString(cur.getColumnIndex("Critical")));
                Mynestedsubteam.add(bean);
            } while (cur.moveToNext());
            try {
                mynestedsubteamActivityAdapter = new NestedSubteamAdapter(Nested_SubTeam.this, Mynestedsubteam);
                teamlist.setAdapter(mynestedsubteamActivityAdapter);
            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
        }
        //sql.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
