package com.vritti.vwb.vworkbench;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.MyTeamActivityAdapter;
import com.vritti.vwb.Beans.MyTeamDeptBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

public class MyTeamActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SharedPreferences userpreferences;
    SharedPreferences timesheetpreferences;
    private Toolbar toolbar;
    ListView teamlist;
    private ArrayList<MyTeamDeptBean> Myteam;
    MyTeamActivityAdapter myTeamActivityAdapter;
    String DeptID;
    ProgressBar mProgress;
    ImageView location;
    TextView txt_wip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_my_team);

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
        initObj();
        initView();
        setListner();

        getData();


       /* if (getMyTeamCount() > 0) {

            updateTeam();
        } else if (ut.checkNet(MyTeamActivity.this)) {
            getData();
        } else {
            ut.displayToast(getApplicationContext(), "No Internet connection");
            //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
        }
*/



    }

    private void initObj() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);

        setSupportActionBar(toolbar);

        Myteam = new ArrayList<MyTeamDeptBean>();

    }

    private void initView() {
        teamlist = (ListView) findViewById(R.id.list_team);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        location = (ImageView) findViewById(R.id.location);
        txt_wip = (TextView) findViewById(R.id.txt_wip);

        location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent2 = new Intent(MyTeamActivity.this, GPSTeamList.class);
                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent2);

                }
            });
        txt_wip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(MyTeamActivity.this, MyTeamMemberWIPActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);

            }
        });

    }

    private void setListner() {
    }

    private void getData() {
        showProgressDialog();
        new StartSession(MyTeamActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                DownloadMyTeamData myTeamData = new DownloadMyTeamData();
                myTeamData.execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
                hideProgressDialog();
            }
        });


    }

    public int getMyTeamCount() {
        SQLiteDatabase sql = db.getWritableDatabase();
        int count = 0;
        Cursor cursor = null;
        try {
            String countQuery = "SELECT  * FROM " + db.TABLE_MYTEAM_DEPT;
            cursor = sql.rawQuery(countQuery, null);
            if (cursor != null && !cursor.isClosed()) {
                count = cursor.getCount();
                cursor.close();
            }
        } catch (Exception e) {
            cursor.close();
        }
        sql.close();
        return count;
    }

    private void updateTeam() {
        Myteam.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String que = "SELECT * FROM " + db.TABLE_MYTEAM_DEPT;
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
                bean.setToday(cur.getString(cur.getColumnIndex("Today")));
                String Locationname = cur.getString(cur.getColumnIndex("LocationName"));
                if (Locationname.equals("")) {
                    bean.setLocationName("GPS Location not found");
                } else{
                    bean.setLocationName(cur.getString(cur.getColumnIndex("LocationName")));
                }
                Myteam.add(bean);
            } while (cur.moveToNext());

            myTeamActivityAdapter = new MyTeamActivityAdapter(MyTeamActivity.this, Myteam);
            teamlist.setAdapter(myTeamActivityAdapter);
        }
        sql.close();
    }


    private void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgressDialog() {
        mProgress.setVisibility(View.GONE);

    }


    private class DownloadMyTeamData extends AsyncTask<String, Void, String> {
        Object res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String response;

            try {
                //  String url = CompanyURL + WebUrlClass.api_getDepartments+"?deptId="+ URLEncoder.encode(params[0],"UTF-8");//URLEncoder.encode(PlantMasterId, "UTF-8")
                //String url = CompanyURL + WebUrlClass.api_getDepartments + "?deptId=";//URLEncoder.encode(PlantMasterId, "UTF-8")
                String url = CompanyURL + WebUrlClass.api_getDepartments + "?deptId=&IsCompleteRight=0&RightforDept=0";

                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
              /*  response = response.substring(1, response.length() - 1);
                response = "["+response+"]";*/
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            hideProgressDialog();
            if (res.contains("UserMasterId")) {
                ContentValues values = new ContentValues();
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(res);
                    SQLiteDatabase sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_MYTEAM_DEPT, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYTEAM_DEPT, null);
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

                        long a = sql.insert(db.TABLE_MYTEAM_DEPT, null, values);
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

    private class DownloadDeptID extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getDeptID;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                /*  response = "["+response+"]";*/
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (!(res.contains(WebUrlClass.Errormsg))) {
                DeptID = res;

            } else {
                hideProgressDialog();
                ut.displayToast(getApplicationContext(), "Can not Identify Department...");
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

            if (id == R.id.refresh1) {
                if (ut.isNet(MyTeamActivity.this)) {
                    getData();
                } else {
                    ut.displayToast(getApplicationContext(), "No Internet connection");
                    //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
                }
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent i = new Intent(getApplicationContext(),ActivityMain.class);
       // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);*/
        finish();

    }
}
