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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ActivityListMainAdapter;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.classes.commonObjectProperties;

public class MyTeamMemberTimesheetActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    private ProgressBar mProgress;
    SharedPreferences userpreferences;
    SharedPreferences timesheetpreferences;
    private Toolbar toolbar;
    String Mode;
    TextView teamMemberName;
    private ListView lsactivity_list;
    String FinalObj;
    commonObjectProperties commonObj;
    private ArrayList<ActivityBean> lsActivityList;
    ActivityListMainAdapter activityListadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_my_team_member_timesheet);
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
        initViews();
        setListner();
        CreateCommonJsonObj();

        /*if (getIntent().hasExtra("Mode")) {
            if (Mode.equalsIgnoreCase("A")) {
                if (getAssgncnt() > 0) {
                    teamMemberName.append(" -Assigned Activities");
                    UpdatListAssigned();
                } else {
                    CreateCommonJsonObj();
                }
            } else if (Mode.equalsIgnoreCase("C")) {
                if (getcriticalcnt() > 0) {
                    teamMemberName.append(" -Critical Activities");
                    UpdatListCritical();
                } else {
                    CreateCommonJsonObj();
                }

            } else if (Mode.equalsIgnoreCase("Appr")) {
                if (getunapprovedcnt() > 0) {
                    teamMemberName.append(" -Unapproved Activities");
                    UpdatListUnapproved();
                } else {
                    CreateCommonJsonObj();
                }

            } else if (Mode.equalsIgnoreCase("O")) {
                if (getAssgncnt() > 0) {
                    teamMemberName.append(" -Overdue Activities");
                    UpdatListOverdue();
                } else {
                    CreateCommonJsonObj();
                }
            }
        }*/

        lsactivity_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ActivityName, ActivityId, SourceId = "";
                ActivityName = lsActivityList.get(position).getActivityName();
                String SourceType = lsActivityList.get(position).getSourceType();
                if (SourceType.equalsIgnoreCase("Datasheet")) {
                    ActivityBean bean = lsActivityList.get(position);
                    Intent intent = new Intent(MyTeamMemberTimesheetActivity.this, ActivityDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("actbean", bean);
                    intent.putExtra("Flag_fromteam", 1);
                    intent.putExtra("unapprove", "");
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    ActivityBean bean = lsActivityList.get(position);
                    Intent intent = new Intent(MyTeamMemberTimesheetActivity.this, ActivityDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("actbean", bean);
                    intent.putExtra("Flag_fromteam", 1);
                    intent.putExtra("unapprove", "");
                    intent.putExtras(bundle);
                    if (Mode.equalsIgnoreCase("Appr")) {
                        intent.putExtra("Mode", Mode);
                    }
                    startActivity(intent);
                }

                finish();


            }
        });
    }

    private void initObj() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        lsActivityList = new ArrayList<ActivityBean>();

        Intent intent = getIntent();
        Mode = intent.getStringExtra("Mode");
        UserMasterId = intent.getStringExtra("UserMasterId");
        UserName = intent.getStringExtra("UserName");
    }

    private void initViews() {
        teamMemberName = (TextView) findViewById(R.id.headertext);
        lsactivity_list = (ListView) findViewById(R.id.ls_team_timesheet);
        teamMemberName.setText(UserName);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_team);

    }

    private void setListner() {


    }

    private void CreateCommonJsonObj() {
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;


        try {
            jsonObj = jsoncommonObj.getJSONObject("Status");
            if (Mode.equalsIgnoreCase("A")) {
                teamMemberName.setText(UserName + " -Assigned Activities");// teamMemberName.setText(UserName);
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");

                // jsonObj.put("value1", "('13','14','25')");

                /*jsonObj = jsoncommonObj.getJSONObject("ParentActId");
                jsonObj.put("IsSet", true);
*/

            } else if (Mode.equalsIgnoreCase("C")) {
                teamMemberName.setText(UserName + " -Critical Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");
                jsonObj = jsoncommonObj.getJSONObject("PriorityIndex");
                jsonObj.put("IsSet", true);

                /*  obj.Status.IsSet = true;
                obj.Status.Operator = '<>';
                obj.Status.value1 = "('15','12')";
                obj.PriorityIndex.IsSet = true;*/

            } else if (Mode.equalsIgnoreCase("Appr")) {
                teamMemberName.setText(UserName + " -Unapproved Activities");

                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "=");
                jsonObj.put("value1", "('15')");
                Calendar calendar = Calendar.getInstance();

                Date date = calendar.getTime();
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");//2017/10/21
                String dateTo = format.format(date);
                calendar.add(Calendar.MONTH, -1);
                Date date1 = calendar.getTime();
                String datefrom = format.format(date1);


               /* jsonObj = jsoncommonObj.getJSONObject("ToDt");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dateTo);
*/
                jsonObj = jsoncommonObj.getJSONObject("FromDt");
                jsonObj.put("IsSet", true);

               /*jsonObj.put("Operator", "bet");
               jsonObj.put("value1", datefrom);
               */

                jsonObj = jsoncommonObj.getJSONObject("Await");
                jsonObj.put("IsSet", true);


               /*jsonObj = jsoncommonObj.getJSONObject("SourceType");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "Support");
               */

            } else if (Mode.equalsIgnoreCase("O")) {
                teamMemberName.setText(UserName + " -Overdue Activities");

                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");
                jsonObj = jsoncommonObj.getJSONObject("ToDt");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
                String date = sff.format(new Date());
                jsonObj.put("value1", date);


              /*  jsonObj = jsoncommonObj.getJSONObject("ParentActId");
                jsonObj.put("IsSet", true);
*/

              /*  obj.Status.IsSet = true;
                obj.Status.Operator = '<>';
                obj.Status.value1 = "('15','12')";
                obj.ToDt.IsSet = true;
                obj.ToDt.Operator = '<';
                obj.ToDt.value1 = moment().format("YYYY-MM-DD");*/


            } else if (Mode.equalsIgnoreCase("T")) {
                teamMemberName.setText(UserName + " -Today Activities");

                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "=");
                jsonObj.put("value1", "('13')");
                jsonObj = jsoncommonObj.getJSONObject("ToDt");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "=");
                SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
                String date = sff.format(new Date());
                jsonObj.put("value1", date);



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            if (UserMasterId != null) {
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        showProgressDialog();
        new StartSession(MyTeamMemberTimesheetActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadCommanDataURLJSON().execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
                dismissProgressDialog();

            }
        });
    }

    private void UpdatListAssigned() {
        lsActivityList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM '" + db.TABLE_ACTIVITYMASTER_TEAM + "' WHERE UserMasterId ='" + UserMasterId + "'";
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
         //       bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
           //     bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
              //  bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
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
             //   bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
//                bean.setDeptDesc(cur.getString(cur.getColumnIndex("DeptDesc")));
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
  //              bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                lsActivityList.add(bean);

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberTimesheetActivity.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void UpdatListOverdue() {
        lsActivityList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM '" + db.TABLE_ACTIVITYMASTER_TEAM + "' WHERE UserMasterId ='" + UserMasterId + "'";
        Cursor cur = sql.rawQuery(query, null);
        Date EndDate = null, Todaydate = null;
        String Enddate, todaydate;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                String jsonEDate = cur.getString(cur.getColumnIndex("EndDate"));
                jsonEDate = jsonEDate.substring(jsonEDate.indexOf("(") + 1, jsonEDate.lastIndexOf(")"));
                long Etime = Long.parseLong(jsonEDate);
                EndDate = new Date(Etime);
                Todaydate = new Date();
                todaydate = sdf.format(Todaydate);
                Enddate = sdf.format(EndDate);
                try {
                    EndDate = sdf.parse(Enddate);
                    Todaydate = sdf.parse(todaydate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (EndDate.before(Todaydate) && !EndDate.equals(Todaydate)) {

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
                   // bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                   // bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
//                    bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
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
               //     bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                 //   bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                    lsActivityList.add(bean);
                }

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberTimesheetActivity.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }

    }

    private void UpdatListCritical() {
        lsActivityList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();

        String query = "SELECT * FROM '" + db.TABLE_ACTIVITYMASTER_TEAM + "' WHERE PriorityIndex='1' AND UserMasterId ='" + UserMasterId + "'";
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
                //bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
                //bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
             //   bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
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
         //       bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
           //     bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));
                lsActivityList.add(bean);

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberTimesheetActivity.this, lsActivityList);
            lsactivity_list.setAdapter(activityListadapter);

        }
    }

    private void UpdatListUnapproved() {
        lsActivityList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM '" + db.TABLE_ACTIVITYMASTER_TEAM + "' WHERE UserMasterId ='" + UserMasterId + "' AND IsApproval ='true'";
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
             //   bean.setStartDt(cur.getString(cur.getColumnIndex("StartDt")));
               // bean.setEndDt(cur.getString(cur.getColumnIndex("EndDt")));
                //bean.setIsActivityMandatory(cur.getString(cur.getColumnIndex("IsActivityMandatory")));
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
                //bean.setExpectedComplete_Date1(cur.getString(cur.getColumnIndex("ExpectedComplete_Date1")));
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
                //bean.setCompActName(cur.getString(cur.getColumnIndex("CompActName")));

                lsActivityList.add(bean);

            } while (cur.moveToNext());
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberTimesheetActivity.this, lsActivityList);
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
            showProgressDialog();

        }


        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_PostloadWorkData;
            try {
                res = ut.OpenPostConnection(url, FinalObj, getApplicationContext());
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
                    Log.e("table cnt :", " " + a);
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
                    if (Mode.equalsIgnoreCase("A")) {
                        UpdatListAssigned();
                    } else if (Mode.equalsIgnoreCase("C")) {
                        UpdatListCritical();
                    } else if (Mode.equalsIgnoreCase("Appr")) {
                        UpdatListUnapproved();
                    } else if (Mode.equalsIgnoreCase("O")) {
                        UpdatListOverdue();
                    } else if (Mode.equalsIgnoreCase("T")) {
                        UpdatListAssigned();
                    }

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


        if (id == R.id.refresh1) {
            CreateCommonJsonObj();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
