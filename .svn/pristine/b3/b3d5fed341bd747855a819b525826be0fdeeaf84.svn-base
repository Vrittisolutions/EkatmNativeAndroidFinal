package com.vritti.vwblib.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

/**
 * Created by 300151 on 2/9/2017.
 */
public class WorkspacewiseActDetailActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    private ProgressBar mProgress;
    TextView ed_Title;
    ListView lsactivity_list;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    public static String Mode, ProjectId, ProjectNAme;
    commonObjectProperties commonObj;
    String FinalObj;
    ActivityListMainAdapter activityListadapter;
    ArrayList<ActivityBean> lsActivityList;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_workspacewise_activity);
        InitView();

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

        sql = db.getWritableDatabase();
        if (getIntent().hasExtra("Mode")) {
            Mode = getIntent().getStringExtra("Mode");
            ProjectId = getIntent().getStringExtra("ProjectId");
            ProjectNAme = getIntent().getStringExtra("ProjectName");//"
             if (Mode.equalsIgnoreCase("C")) {
                 ed_Title.setText(ProjectNAme +" : Critical Activities");
            } else if (Mode.equalsIgnoreCase("Comp")) {
                 ed_Title.setText(ProjectNAme +" : Completed Activities");

            } else if (Mode.equalsIgnoreCase("A")) {
                 ed_Title.setText(ProjectNAme +" : Assigned Activities");

             } else if (Mode.equalsIgnoreCase("Appr")) {//O
                 ed_Title.setText(ProjectNAme +" : Unapproved Activities");
            }else if (Mode.equalsIgnoreCase("O")) {//O
                 ed_Title.setText(ProjectNAme +" : Overdue Activities");
             }

            CreateCommonJsonObj();
        } else if (getIntent().hasExtra("ProjectId")) {
            ProjectId = getIntent().getStringExtra("ProjectId");
            ProjectNAme = getIntent().getStringExtra("ProjectName");
            ed_Title.setText(ProjectNAme +" : Assigned Activities");

            createWrokspaceActivityJSON(ProjectId);
        }
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        ed_Title = (TextView) findViewById(R.id.titlee);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        lsactivity_list = (ListView) findViewById(R.id.lsactivity_list);
        lsActivityList = new ArrayList<ActivityBean>();
    }

    private void CreateCommonJsonObj() {
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;


        try {
            jsonObj = jsoncommonObj.getJSONObject("ProjId");
            if (ProjectId != null) {
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "mul");
                jsonObj.put("value1", "('" + ProjectId + "')");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("ParentActId");
            jsonObj.put("IsSet", true);
            jsonObj = jsoncommonObj.getJSONObject("Status");

            if (Mode.equalsIgnoreCase("A")) {
                //toolbar.setTitle("Assigned Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");




            } else if (Mode.equalsIgnoreCase("O")) {
               // toolbar.setTitle("Overdue Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");

            } else if (Mode.equalsIgnoreCase("Appr")) {
              //  toolbar.setTitle("Unapproved Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15')");




            } else if (Mode.equalsIgnoreCase("Comp")) {
               // toolbar.setTitle("Complete Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','13')");


            } else if (Mode.equalsIgnoreCase("C")) {
               // toolbar.setTitle("Critical Activities");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<>");
                jsonObj.put("value1", "('15','12')");



            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            jsonObj = jsoncommonObj.getJSONObject("ToDt");

            if (Mode.equalsIgnoreCase("O")) {
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
                String date = sff.format(new Date());
                jsonObj.put("value1", date);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            jsonObj = jsoncommonObj.getJSONObject("PriorityIndex");
            if (Mode == "C") {
                jsonObj.put("IsSet", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            jsonObj = jsoncommonObj.getJSONObject("Await");
            if (Mode.equalsIgnoreCase("Appr")) {
                jsonObj.put("IsSet", true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

       /* try {
            jsonObj = jsoncommonObj.getJSONObject("issuedTo");
            if (UserMasterId != null) {
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", UserMasterId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        showProgressDialog();

        new StartSession(WorkspacewiseActDetailActivity.this, new CallbackInterface() {
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


    private void showProgressDialog() {


        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    private void createWrokspaceActivityJSON(String ProjectId) {
        commonObj = new commonObjectProperties();
        JSONObject jsoncommonObj = commonObj.WorkDataObj();
        JSONObject jsonObj;

        try {
            jsonObj = jsoncommonObj.getJSONObject("ProjId");
            if (ProjectId != null) {
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "mul");
                jsonObj.put("value1", "('" + ProjectId + "')");
            }
            jsonObj = jsoncommonObj.getJSONObject("ParentActId");
            jsonObj.put("IsSet", true);

            jsonObj = jsoncommonObj.getJSONObject("Status");
            jsonObj.put("IsSet", true);
            jsonObj.put("Operator", "<>");
            jsonObj.put("value1", "('15','12')");

            FinalObj = jsoncommonObj.toString();
            FinalObj = FinalObj.replaceAll("\\\\", "");
            showProgressDialog();

            new StartSession(WorkspacewiseActDetailActivity.this, new CallbackInterface() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DownloadCommanDataURLJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_PostloadWorkData;
            try {
                res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                // response = response.replaceAll("\\\\\"", "");


                response = response.substring(1, response.length() - 1);

                JSONArray jResults = new JSONArray(response);

                lsActivityList.clear();

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    ActivityBean bean = new ActivityBean();
                    bean.setIssuedToName(jorder.getString("IssuedToName"));
                    bean.setAssigned_By(jorder.getString("Assigned_By"));
                    bean.setActivityCode(jorder.getString("ActivityCode"));
                    bean.setConsigneeName(jorder.getString("ConsigneeName"));
                    bean.setContMob(jorder.getString("ContMob"));
                    bean.setSourceId(jorder.getString("SourceId"));
                    bean.setActivityName(jorder.getString("ActivityName"));
                    bean.setFormatEndDt(jorder.getString("FormatEndDt"));
                    bean.setFormatStDt(jorder.getString("FormatStDt"));
                    bean.setHoursRequired(jorder.getString("HoursRequired"));
                    bean.setPriorityIndex(jorder.getString("PriorityIndex"));
                    bean.setProjectName(jorder.getString("ProjectName"));
                    bean.setTotalHoursBooked(jorder.getString("TotalHoursBooked"));//PAllowUsrTimeSlotHrs
                    bean.setPAllowUsrTimeSlotHrs(jorder.getString("PAllowUsrTimeSlotHrs"));
                    bean.setEndDate(jorder.getString("EndDate"));
                    bean.setStartDate(jorder.getString("StartDate"));
                    bean.setSourceType(jorder.getString("SourceType"));
                    bean.setStatus(jorder.getString("Status"));
                    bean.setActivityId(jorder.getString("ActivityId"));
                    bean.setProjectID(jorder.getString("ProjectId"));

                    bean.setIsChargable(jorder.getString("IsChargable"));
                    bean.setAssignedById(jorder.getString("AssignedById"));
                    bean.setSubActCount(jorder.getString("SubActCount"));
                    bean.setSubActStaus(jorder.getString("SubActStaus"));
                    bean.setExpectedCompleteDate(jorder.getString("ExpectedCompleteDate"));
                    bean.setExpectedComplete_Date(jorder.getString("ExpectedComplete_Date"));
                    bean.setModifiedBy(jorder.getString("ModifiedBy"));
                    bean.setModified_By(jorder.getString("Modified_By"));
                    bean.setStartDt(jorder.getString("StartDt"));
                    bean.setEndDt(jorder.getString("EndDt"));
                    bean.setIsActivityMandatory(jorder.getString("IsActivityMandatory"));
                    bean.setIsDelayedActivityAllowed(jorder.getString("IsDelayedActivityAllowed"));
                    bean.setCd(jorder.getString("Cd"));
                    bean.setUnitId(jorder.getString("UnitId"));
                    bean.setPKModuleMastId(jorder.getString("PKModuleMastId"));
                    bean.setPriorityName(jorder.getString("PriorityName"));
                    bean.setColour(jorder.getString("Colour"));
                    bean.setAddedDt(jorder.getString("AddedDt"));
                    bean.setUserMasterId(jorder.getString("UserMasterId"));
                    bean.setModifiedDt(jorder.getString("ModifiedDt"));
                    bean.setAssignedById1(jorder.getString("AssignedById1"));
                    bean.setIsDeleted(jorder.getString("IsDeleted"));
                    bean.setIsApproved(jorder.getString("IsApproved"));
                    bean.setIsChargable1(jorder.getString("IsChargable1"));
                    bean.setActivityTypeId(jorder.getString("ActivityTypeId"));
                    bean.setIsApproval(jorder.getString("IsApproval"));
                    bean.setAttachmentName(jorder.getString("AttachmentName"));
                    bean.setAttachmentContent(jorder.getString("AttachmentContent"));
                    bean.setModifiedDt1(jorder.getString("ModifiedDt1"));
                    bean.setUnitName(jorder.getString("UnitName"));
                    bean.setUnitDesc(jorder.getString("UnitDesc"));
                    bean.setModuleName(jorder.getString("ModuleName"));
                    bean.setActivityName1(jorder.getString("ActivityName1"));
                    bean.setRemarks(jorder.getString("Remarks"));
                    bean.setProjectCode(jorder.getString("ProjectCode"));
                    bean.setUserName(jorder.getString("UserName"));
                    bean.setExpectedComplete_Date1(jorder.getString("ExpectedComplete_Date1"));
                    bean.setDeptDesc(jorder.getString("DeptDesc"));
                    bean.setDeptMasterId(jorder.getString("DeptMasterId"));
                    bean.setCompletionIntimate(jorder.getString("CompletionIntimate"));
                    bean.setModifiedBy1(jorder.getString("ModifiedBy1"));
                    bean.setReassignedBy(jorder.getString("ReassignedBy"));
                    bean.setReassignedDt(jorder.getString("ReassignedDt"));
                    bean.setActualCompletionDate(jorder.getString("ActualCompletionDate"));
                    bean.setWarrantyCode(jorder.getString("WarrantyCode"));
                    bean.setTicketCategory(jorder.getString("TicketCategory"));
                    bean.setIsEndTime(jorder.getString("IsEndTime"));
                    bean.setIsCompActPresent(jorder.getString("IsCompActPresent"));
                    bean.setCompletionActId(jorder.getString("CompletionActId"));
                    bean.setTktCustReportedBy(jorder.getString("TktCustReportedBy"));
                    bean.setTktCustApprovedBy(jorder.getString("TktCustApprovedBy"));
                    bean.setIsSubActivity(jorder.getString("IsSubActivity"));
                    bean.setParentActId(jorder.getString("ParentActId"));
                    bean.setActivityTypeName(jorder.getString("ActivityTypeName"));
                    bean.setCompActName(jorder.getString("CompActName"));
                    lsActivityList.add(bean);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response != null) {
                activityListadapter = new ActivityListMainAdapter(WorkspacewiseActDetailActivity.this, lsActivityList);
                lsactivity_list.setAdapter(activityListadapter);
            }
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
/*
if (ProjectId != "") {
        obj.ProjId.IsSet = true;
        obj.ProjId.Operator = 'mul';
        obj.ProjId.value1 = "('" + ProjectId + "')";
        }

        if (Mode == "A") {
        obj.Status.IsSet = true;
        obj.Status.Operator = '<>';
        obj.Status.value1 = "('15','12')";
        } else if (Mode == "O") {
        obj.Status.IsSet = true;
        obj.Status.Operator = '<>';
        obj.Status.value1 = "('15','12')";
        obj.ToDt.IsSet = true;
        obj.ToDt.Operator = '<';
        obj.ToDt.value1 = moment().format("YYYY-MM-DD");
        } else if (Mode == "Appr") {
        obj.Status.IsSet = true;
        obj.Status.Operator = '<>';
        obj.Status.value1 = "('15')";

        obj.Await.IsSet = true;
        } else if (Mode == "Comp") {
        obj.Status.IsSet = true;
        obj.Status.Operator = '<>';
        obj.Status.value1 = "('15','13')";
        } else if (Mode == "C") {
        obj.Status.IsSet = true;
        obj.Status.Operator = '<>';
        obj.Status.value1 = "('15','12')";
        obj.PriorityIndex.IsSet = true;
        }
        if (Usermstid != "") {
        obj.issuedTo.IsSet = true;
        obj.issuedTo.value1 = Usermstid;
        }*/
