package com.vritti.crmlib.vcrm7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.TeamMemberAdapter;
import com.vritti.crmlib.bean.TeamMemberbean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

public class TeamMemberActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    ListView ls_team;
    SQLiteDatabase sql;
    ArrayList<TeamMemberbean> teamMemberbeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_team_member);
        InitView();
        UpdateTeamMemberList();
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);

        toolbar.setTitle("Team");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        teamMemberbeanArrayList = new ArrayList<TeamMemberbean>();
        ls_team = (ListView) findViewById(R.id.ls_team);
    }

    private void UpdateTeamMemberList() {
        String query = "SELECT UserMasterId, UserLoginId, UserName,Assigned,Overdue," +
                "Today,Hot,Collection,Tomorrow,Report FROM "
                + db.TABLE_Team_Member;
        Cursor cur = sql.rawQuery(query, null);
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
            teamMemberbean.setUserMasterId("");
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
