package com.vritti.crm.vcrm7;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.TeamMemberAdapter;
import com.vritti.crm.bean.TeamMemberbean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.Progresdialog;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.vworkbench.GPSTeamList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class TeamMemberActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    ListView ls_team;
    ImageView location;
    SQLiteDatabase sql;
    ArrayList<TeamMemberbean> teamMemberbeanArrayList;
    ProgressBar googleProgressBar;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_team_member);
        InitView();
        googleProgressBar.setVisibility(View.VISIBLE);
        new DownloadTeamJSON().execute();

       // UpdateTeamMemberList();
    }

    private void showProgressDialog() {


        googleProgressBar.setVisibility(View.VISIBLE);

    }

    private void dismissProgressDialog() {
        if (googleProgressBar != null && googleProgressBar.isShown()) {
            googleProgressBar.setVisibility(View.GONE);
        }
    }

    class DownloadTeamJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
      //  showProgressDialog();




        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_TeamMembers +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                 res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_Team_Member, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Team_Member, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Team_Member, null, values);
                    Log.e("", "" + a);

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
            googleProgressBar.setVisibility(View.GONE);
            if (response != null) {

            }else{
                Toast.makeText(TeamMemberActivity.this,"Unable to fetch team data",Toast.LENGTH_SHORT).show();
            }

          UpdateTeamMemberList();

        }

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        googleProgressBar = (ProgressBar) findViewById(R.id.google_progress);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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
        teamMemberbeanArrayList = new ArrayList<TeamMemberbean>();
        ls_team = (ListView) findViewById(R.id.ls_team);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.file_location));

        txt_title.setText("Team");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        location = (ImageView) findViewById(R.id.location);

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(TeamMemberActivity.this, GPSTeamList.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });


    }

    private void UpdateTeamMemberList() {
        String query = "SELECT UserMasterId, UserLoginId, UserName,Assigned,Overdue," +
                "Today,Hot,Collection,Tomorrow,Report,CallReview,Warm,ISMobileApp,LocationName FROM "
                + db.TABLE_Team_Member;
        Cursor cur = sql.rawQuery(query, null);
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                TeamMemberbean teamMemberbean = new TeamMemberbean();
                teamMemberbean.setAssigned(cur.getString(cur.getColumnIndex("Assigned")));
                teamMemberbean.setCollection(cur.getString(cur.getColumnIndex("Collection")));
                teamMemberbean.setHot(cur.getString(cur.getColumnIndex("Hot")));
                teamMemberbean.setOverdue(cur.getString(cur.getColumnIndex("Overdue")));
                teamMemberbean.setToday(cur.getString(cur.getColumnIndex("Today")));
                teamMemberbean.setTomorrow(cur.getString(cur.getColumnIndex("Tomorrow")));
                teamMemberbean.setUserLoginId(cur.getString(cur.getColumnIndex("UserLoginId")));
                teamMemberbean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                teamMemberbean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                teamMemberbean.setReport(cur.getString(cur.getColumnIndex("Report")));
                teamMemberbean.setCallReview(cur.getString(cur.getColumnIndex("CallReview")));
                teamMemberbean.setWarm(cur.getString(cur.getColumnIndex("Warm")));
                teamMemberbean.setLocationName(cur.getString(cur.getColumnIndex("LocationName")));
                teamMemberbean.setISMobileApp(cur.getString(cur.getColumnIndex("ISMobileApp")));
                teamMemberbeanArrayList.add(teamMemberbean);

            } while (cur.moveToNext());
        } else {
            TeamMemberbean teamMemberbean = new TeamMemberbean();
            teamMemberbean.setAssigned("");
            teamMemberbean.setCollection("");
            teamMemberbean.setHot("");
            teamMemberbean.setOverdue("");
            teamMemberbean.setToday("");
            teamMemberbean.setTomorrow("");
            teamMemberbean.setUserLoginId("");
            teamMemberbean.setCallReview("");
            teamMemberbean.setWarm("");
            teamMemberbean.setUserMasterId("");
            teamMemberbean.setLocationName("");
            teamMemberbean.setISMobileApp("");
            teamMemberbean.setUserName("No members");
            teamMemberbeanArrayList.add(teamMemberbean);
        }
        TeamMemberAdapter teamMemberAdapter = new TeamMemberAdapter
                (TeamMemberActivity.this, teamMemberbeanArrayList);
        ls_team.setAdapter(teamMemberAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TeamMemberActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}