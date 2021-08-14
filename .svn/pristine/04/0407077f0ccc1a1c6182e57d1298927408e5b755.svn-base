package com.vritti.vwblib.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.ActivityListMainAdapter;
import com.vritti.vwblib.Beans.ActivityBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.commonObjectProperties;

public class MyTeamMemberActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    private ProgressBar mProgress;
    SharedPreferences userpreferences;
    SharedPreferences timesheetpreferences;
    private Toolbar toolbar;
    String Username;
    ListView teamdatalist;
    TextView teamMemberName;
    public static Boolean Activity_AssignByMe = false;
    commonObjectProperties commonObj;
    String FinalObj;
    private ListView lsactivity_list;
    private ActivityListMainAdapter activityListadapter;
    private ArrayList<ActivityBean> lsActivityList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_my_team_member);
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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);


        initObj();
        initView();
        setListner();
        getData();
        /*  if (getActivityMasterCount() > 0) {
            UpdatList();
        } else {
            getData();
        }*/
        lsactivity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ActivityName, ActivityId, SourceId = "";
                ActivityName = lsActivityList.get(position).getActivityName();
                String SourceType = lsActivityList.get(position).getSourceType();
                if (SourceType.equalsIgnoreCase("Datasheet")) {
                    ActivityBean bean  = lsActivityList.get(position);
                    Intent intent = new Intent(MyTeamMemberActivity.this, ActivityDetailsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("actbean",bean);
                    intent.putExtra("Flag_fromteam", 1);
                    intent.putExtra("unapprove","");
                    intent.putExtras(bundle);
                    startActivity(intent);

                    startActivity(intent);
                } else {
                    ActivityBean bean  = lsActivityList.get(position);
                    Intent intent = new Intent(MyTeamMemberActivity.this, ActivityDetailsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("actbean",bean);
                    intent.putExtra("Flag_fromteam", 1);
                    intent.putExtra("unapprove","");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }


            }
        });
    }


    private void initObj() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        lsActivityList = new ArrayList<ActivityBean>();

    }

    private void initView() {

        teamMemberName = (TextView) findViewById(R.id.teammembername);
        lsactivity_list = (ListView) findViewById(R.id.list_team_data);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_MT);
        Intent intent = getIntent();
        UserMasterId = intent.getStringExtra("UsermasterID");
        Username = intent.getStringExtra("Username");
        teamMemberName.setText(Username + "-Worksheet");


    }

    private void setListner() {


    }

    private void getData() {
        if (ut.isNet(MyTeamMemberActivity.this)) {

            new StartSession(MyTeamMemberActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    getActicityData();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            ut.displayToast(MyTeamMemberActivity.this, "No Internet Connection");
        }
    }

    public void getActicityData() {
        showProgressDialog();
        Activity_AssignByMe = false;
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;

        try {

            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("Operator", "<>");
            jsonObj.put("value1", "('15','12')");
            // jsonObj.put("value1", "('13','14','25')");


            jsonObj = jsoncommonObj.getJSONObject("ParentActId");
            jsonObj.put("IsSet", true);


        } catch (Exception e) {
            e.printStackTrace();
        }

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        new DownloadCommanDataURLJSON().execute();
    }

    public int getActivityMasterCount() {
        SQLiteDatabase sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_ACTIVITYMASTER_TEAM + " WHERE UserMasterId='" + UserMasterId + "'";
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    private void UpdatList() {
        lsActivityList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITYMASTER_TEAM;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ActivityBean bean = new ActivityBean();
                bean.setIssuedToName(cur.getString(cur.getColumnIndex("IssuedToName")));
                bean.setAssigned_By(cur.getString(cur.getColumnIndex("Assigned_By")));
                bean.setActivityCode(cur.getString(cur.getColumnIndex("ActivityCode")));
                bean.setConsigneeName(cur.getString(cur.getColumnIndex("ConsigneeName")));
                bean.setContMob(cur.getString(cur.getColumnIndex("ContMob")));
                bean.setSourceId(cur.getString(cur.getColumnIndex("SourceId")));
                bean.setActivityName(cur.getString(cur.getColumnIndex("ActivityName")));
                bean.setFormatEndDt(cur.getString(cur.getColumnIndex("FormatEndDt")));
                bean.setFormatStDt(cur.getString(cur.getColumnIndex("FormatStDt")));
                bean.setHoursRequired(cur.getString(cur.getColumnIndex("HoursRequired")));
                bean.setPriorityIndex(cur.getString(cur.getColumnIndex("PriorityIndex")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                bean.setTotalHoursBooked(cur.getString(cur.getColumnIndex("TotalHoursBooked")));//PAllowUsrTimeSlotHrs
                bean.setPAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("PAllowUsrTimeSlotHrs")));
                bean.setEndDate(cur.getString(cur.getColumnIndex("EndDate")));
                bean.setStartDate(cur.getString(cur.getColumnIndex("StartDate")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setProjectID(cur.getString(cur.getColumnIndex("ProjectId")));

                bean.setIsChargable(cur.getString(cur.getColumnIndex("IsChargable")));
                bean.setAssignedById(cur.getString(cur.getColumnIndex("AssignedById")));
                bean.setSubActCount(cur.getString(cur.getColumnIndex("SubActCount")));
                bean.setSubActStaus(cur.getString(cur.getColumnIndex("SubActStaus")));
                bean.setExpectedCompleteDate(cur.getString(cur.getColumnIndex("ExpectedCompleteDate")));
                bean.setExpectedComplete_Date(cur.getString(cur.getColumnIndex("ExpectedComplete_Date")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModified_By(cur.getString(cur.getColumnIndex("Modified_By")));
                bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
                bean.setIsDelayedActivityAllowed(cur.getString(cur.getColumnIndex("IsDelayedActivityAllowed")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setUnitId(cur.getString(cur.getColumnIndex("UnitId")));
                bean.setPKModuleMastId(cur.getString(cur.getColumnIndex("PKModuleMastId")));
                bean.setPriorityName(cur.getString(cur.getColumnIndex("PriorityName")));
                bean.setColour(cur.getString(cur.getColumnIndex("Colour")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setAssignedById1(cur.getString(cur.getColumnIndex("AssignedById1")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setIsApproved(cur.getString(cur.getColumnIndex("IsApproved")));
                bean.setIsChargable1(cur.getString(cur.getColumnIndex("IsChargable1")));
                bean.setActivityTypeId(cur.getString(cur.getColumnIndex("ActivityTypeId")));
                bean.setIsApproval(cur.getString(cur.getColumnIndex("IsApproval")));
                bean.setAttachmentName(cur.getString(cur.getColumnIndex("AttachmentName")));
                bean.setAttachmentContent(cur.getString(cur.getColumnIndex("AttachmentContent")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setUnitName(cur.getString(cur.getColumnIndex("UnitName")));
                bean.setUnitDesc(cur.getString(cur.getColumnIndex("UnitDesc")));
                bean.setModuleName(cur.getString(cur.getColumnIndex("ModuleName")));
                bean.setActivityName1(cur.getString(cur.getColumnIndex("ActivityName1")));
                bean.setRemarks(cur.getString(cur.getColumnIndex("Remarks")));
                bean.setProjectCode(cur.getString(cur.getColumnIndex("ProjectCode")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setCompletionIntimate(cur.getString(cur.getColumnIndex("CompletionIntimate")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setReassignedBy(cur.getString(cur.getColumnIndex("ReassignedBy")));
                bean.setReassignedDt(cur.getString(cur.getColumnIndex("ReassignedDt")));
                bean.setActualCompletionDate(cur.getString(cur.getColumnIndex("ActualCompletionDate")));
                bean.setWarrantyCode(cur.getString(cur.getColumnIndex("WarrantyCode")));
                bean.setTicketCategory(cur.getString(cur.getColumnIndex("TicketCategory")));
                bean.setIsEndTime(cur.getString(cur.getColumnIndex("IsEndTime")));
                bean.setIsCompActPresent(cur.getString(cur.getColumnIndex("IsCompActPresent")));
                bean.setCompletionActId(cur.getString(cur.getColumnIndex("CompletionActId")));
                bean.setTktCustReportedBy(cur.getString(cur.getColumnIndex("TktCustReportedBy")));
                bean.setTktCustApprovedBy(cur.getString(cur.getColumnIndex("TktCustApprovedBy")));
                bean.setIsSubActivity(cur.getString(cur.getColumnIndex("IsSubActivity")));
                bean.setParentActId(cur.getString(cur.getColumnIndex("ParentActId")));
                bean.setActivityTypeName(cur.getString(cur.getColumnIndex("ActivityTypeName")));
                bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                lsActivityList.add(bean);
            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberActivity.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }


    }

    private void showProgressDialog() {


        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {


        mProgress.setVisibility(View.GONE);
    }


    class DownloadCommanDataURLJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_PostloadWorkData;
            try {
                res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                String msg = "";
                SQLiteDatabase sql = db.getWritableDatabase();
                sql.delete(db.TABLE_ACTIVITYMASTER_TEAM, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITYMASTER_TEAM, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_ACTIVITYMASTER_TEAM, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            dismissProgressDialog();

            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                    UpdatList();

                } else {
                    ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                ut.displayToast(getApplicationContext(), "Server error...");
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

            if(id== R.id.refresh1) {

                    if (ut.isNet(MyTeamMemberActivity.this)) {
                        getData();
                    } else {
                        ut.displayToast(getApplicationContext(), "No Internet connection");
                        //  Toast.makeText(getApplicationContext(), , Toast.LENGTH_LONG).show();
                    }
                    return true;

            }else {
                return super.onOptionsItemSelected(item);
        }


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),ActivityMain.class);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }
}
