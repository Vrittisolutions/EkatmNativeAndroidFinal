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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ActivityListMainAdapter;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.classes.commonObjectProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyTeamMemberWIPActivity extends AppCompatActivity {
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
    private JSONArray jResults;


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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);


        initObj();
        initView();
        setListner();
        getData();
        /*  if (getActivityMasterCount() > 0) {
            updateList();
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
                    Intent intent = new Intent(MyTeamMemberWIPActivity.this,ActivityDetailsActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("actbean",bean);
                    intent.putExtra("Flag_fromteam", 1);
                    intent.putExtra("unapprove","");
                    intent.putExtras(bundle);
                    startActivity(intent);

                    startActivity(intent);
                } else {
                    ActivityBean bean  = lsActivityList.get(position);
                    Intent intent = new Intent(MyTeamMemberWIPActivity.this, ActivityDetailsActivity.class);
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
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);

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
        teamMemberName.setText(UserName + "-Worksheet");


    }

    private void setListner() {


    }

    private void getData() {
        if (ut.isNet(MyTeamMemberWIPActivity.this)) {
            showProgressDialog();
            new StartSession(MyTeamMemberWIPActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanDataURLJSON().execute();

                  //  getActicityData();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            ut.displayToast(MyTeamMemberWIPActivity.this, "No Internet Connection");
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
            activityListadapter = new ActivityListMainAdapter(MyTeamMemberWIPActivity.this, lsActivityList);
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
            String url = CompanyURL + WebUrlClass.api_PostloadWorkDataWIP;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                lsActivityList.clear();
                jResults = new JSONArray(response);


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
                    try {
                        for (int i = 0; i < jResults.length(); i++) {

                            JSONObject jsonObject = jResults.getJSONObject(i);

                            ActivityBean bean = new ActivityBean();
                            bean.setIssuedToName(jsonObject.getString("IssuedToName"));
                            bean.setAssigned_By(jsonObject.getString("Assigned_By"));
                            bean.setActivityCode(jsonObject.getString("ActivityCode"));
                            bean.setConsigneeName(jsonObject.getString("ConsigneeName"));
                            bean.setContMob(jsonObject.getString("ContMob"));
                            bean.setSourceId(jsonObject.getString("SourceId"));
                            bean.setActivityName(jsonObject.getString("ActivityName"));
                            bean.setFormatEndDt(jsonObject.getString("FormatEndDt"));
                            bean.setFormatStDt(jsonObject.getString("FormatStDt"));
                            bean.setHoursRequired(jsonObject.getString("HoursRequired"));
                            bean.setPriorityIndex(jsonObject.getString("PriorityIndex"));
                            bean.setProjectName(jsonObject.getString("ProjectName"));
                            bean.setTotalHoursBooked(jsonObject.getString("TotalHoursBooked"));//PAllowUsrTimeSlotHrs
                            bean.setPAllowUsrTimeSlotHrs(jsonObject.getString("PAllowUsrTimeSlotHrs"));
                            bean.setEndDate(jsonObject.getString("EndDate"));
                            bean.setStartDate(jsonObject.getString("StartDate"));
                            bean.setSourceType(jsonObject.getString("SourceType"));
                            bean.setStatus(jsonObject.getString("Status"));
                            bean.setActivityId(jsonObject.getString("ActivityId"));
                            bean.setProjectID(jsonObject.getString("ProjectId"));

                            bean.setIsChargable(jsonObject.getString("IsChargable"));
                            bean.setAssignedById(jsonObject.getString("AssignedById"));
                            bean.setSubActCount(jsonObject.getString("SubActCount"));
                            bean.setSubActStaus(jsonObject.getString("SubActStaus"));
                            bean.setExpectedCompleteDate(jsonObject.getString("ExpectedCompleteDate"));
                            bean.setExpectedComplete_Date(jsonObject.getString("ExpectedComplete_Date"));
                            bean.setModifiedBy(jsonObject.getString("ModifiedBy"));
                            bean.setModified_By(jsonObject.getString("Modified_By"));
                          //  bean.setStartDt(jsonObject.getString("StartDt"));
                            //bean.setEndDt(jsonObject.getString("EndDt"));
                            //bean.setIsActivityMandatory(jsonObject.getString("IsActivityMandatory"));
                            bean.setIsDelayedActivityAllowed(jsonObject.getString("IsDelayedActivityAllowed"));
                            bean.setCd(jsonObject.getString("Cd"));
                            bean.setUnitId(jsonObject.getString("UnitId"));
                            bean.setPKModuleMastId(jsonObject.getString("PKModuleMastId"));
                            bean.setPriorityName(jsonObject.getString("PriorityName"));
                            bean.setColour(jsonObject.getString("Colour"));
                            bean.setAddedDt(jsonObject.getString("AddedDt"));
                            bean.setUserMasterId(jsonObject.getString("UserMasterId"));
                            bean.setModifiedDt(jsonObject.getString("ModifiedDt"));
                            bean.setAssignedById1(jsonObject.getString("AssignedById1"));
                            bean.setIsDeleted(jsonObject.getString("IsDeleted"));
                            bean.setIsApproved(jsonObject.getString("IsApproved"));
                            bean.setIsChargable1(jsonObject.getString("IsChargable1"));
                            bean.setActivityTypeId(jsonObject.getString("ActivityTypeId"));
                            bean.setIsApproval(jsonObject.getString("IsApproval"));
                            bean.setAttachmentName(jsonObject.getString("AttachmentName"));
                            bean.setAttachmentContent(jsonObject.getString("AttachmentContent"));
                            bean.setModifiedDt1(jsonObject.getString("ModifiedDt1"));
                            bean.setUnitName(jsonObject.getString("UnitName"));
                            bean.setUnitDesc(jsonObject.getString("UnitDesc"));
                            bean.setModuleName(jsonObject.getString("ModuleName"));
                            bean.setActivityName1(jsonObject.getString("ActivityName1"));
                            bean.setRemarks(jsonObject.getString("Remarks"));
                            bean.setProjectCode(jsonObject.getString("ProjectCode"));
                            bean.setUserName(jsonObject.getString("UserName"));
                            //bean.setExpectedComplete_Date1(jsonObject.getString("ExpectedComplete_Date1"));
                            bean.setDeptDesc(jsonObject.getString("DeptDesc"));
                            bean.setDeptMasterId(jsonObject.getString("DeptMasterId"));
                            bean.setCompletionIntimate(jsonObject.getString("CompletionIntimate"));
                            bean.setModifiedBy1(jsonObject.getString("ModifiedBy1"));
                            bean.setReassignedBy(jsonObject.getString("ReassignedBy"));
                            bean.setReassignedDt(jsonObject.getString("ReassignedDt"));
                            bean.setActualCompletionDate(jsonObject.getString("ActualCompletionDate"));
                            bean.setWarrantyCode(jsonObject.getString("WarrantyCode"));
                            bean.setTicketCategory(jsonObject.getString("TicketCategory"));
                            bean.setIsEndTime(jsonObject.getString("IsEndTime"));
                            bean.setIsCompActPresent(jsonObject.getString("IsCompActPresent"));
                            bean.setCompletionActId(jsonObject.getString("CompletionActId"));
                            bean.setTktCustReportedBy(jsonObject.getString("TktCustReportedBy"));
                            bean.setTktCustApprovedBy(jsonObject.getString("TktCustApprovedBy"));
                            bean.setIsSubActivity(jsonObject.getString("IsSubActivity"));
                            bean.setParentActId(jsonObject.getString("ParentActId"));
                            bean.setActivityTypeName(jsonObject.getString("ActivityTypeName"));
                            //bean.setCompActName(jsonObject.getString("CompActName"));
                            lsActivityList.add(bean);

                        }
                        activityListadapter = new ActivityListMainAdapter(MyTeamMemberWIPActivity.this, lsActivityList);
                        lsactivity_list.setAdapter(activityListadapter);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                  //  updateList();

                } else {
                    //ut.displayToast(getApplicationContext(), "Server error...");
                }
            } else {
                //ut.displayToast(getApplicationContext(), "Server error...");
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

                    if (ut.isNet(MyTeamMemberWIPActivity.this)) {

                        new StartSession(MyTeamMemberWIPActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadCommanDataURLJSON().execute();

                                //  getActicityData();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(getApplicationContext(), msg);
                            }
                        });
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
