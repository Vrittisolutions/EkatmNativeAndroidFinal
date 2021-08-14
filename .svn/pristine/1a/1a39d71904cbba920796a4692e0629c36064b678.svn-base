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
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.Beans.ActivityTrailDetailBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

public class ActivityTrailDetails extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    public static SharedPreferences userpreferences;
    Toolbar toolbar;
    public static ProgressBar mprogress;
    String ActivityId;
    ArrayList<ActivityTrailDetailBean> lsRecordList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_trail_details);
        initView();
        setListner();
        Intent i = getIntent();
        ActivityId = i.getStringExtra("ActivityId");


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

       /* mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);*/
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_trail);



    }

    private void setListner() {

    }

    private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);
    }

    private void DismissProgress() {
        mprogress.setVisibility(View.GONE);
    }

    private void updateRecordList() {
        lsRecordList.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ACTIVITY_TRAIL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityTrailDetailBean bean = new ActivityTrailDetailBean();
                bean.setToUserName(cur.getString(cur.getColumnIndex("ToUserName")));
                bean.setByUserName(cur.getString(cur.getColumnIndex("ByUserName")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setDesc_r(cur.getString(cur.getColumnIndex("Desc_r")));
                bean.setActivityTrailId(cur.getString(cur.getColumnIndex("ActivityTrailId")));
                bean.setActivityId(cur.getString(cur.getColumnIndex("ActivityId")));
                bean.setAssigned_Count(cur.getString(cur.getColumnIndex("Assigned_Count")));
                bean.setForUserMasterId(cur.getString(cur.getColumnIndex("ForUserMasterId")));
                bean.setAction(cur.getString(cur.getColumnIndex("Action")));
                bean.setNarration(cur.getString(cur.getColumnIndex("Narration")));
                bean.setStatus(cur.getString(cur.getColumnIndex("Status")));
                bean.setPrecedingActivityTrailId(cur.getString(cur.getColumnIndex("PrecedingActivityTrailId")));
                bean.setAddedDt1(cur.getString(cur.getColumnIndex("AddedDt1")));
                bean.setAddedBy(cur.getString(cur.getColumnIndex("AddedBy")));
                bean.setPreActivityStatus(cur.getString(cur.getColumnIndex("PreActivityStatus")));
                bean.setNxtActivityStatus(cur.getString(cur.getColumnIndex("NxtActivityStatus")));
                bean.setSourceType(cur.getString(cur.getColumnIndex("SourceType")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                bean.setUserLoginId(cur.getString(cur.getColumnIndex("UserLoginId")));
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setTitle(cur.getString(cur.getColumnIndex("Title")));
                bean.setUserPassword(cur.getString(cur.getColumnIndex("UserPassword")));
                bean.setHintQuestion(cur.getString(cur.getColumnIndex("HintQuestion")));
                bean.setAnswer(cur.getString(cur.getColumnIndex("Answer")));
                bean.setDeptMasterId(cur.getString(cur.getColumnIndex("DeptMasterId")));
                bean.setEmail(cur.getString(cur.getColumnIndex("Email")));
                bean.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                bean.setExtNo(cur.getString(cur.getColumnIndex("ExtNo")));
                bean.setLocationId(cur.getString(cur.getColumnIndex("LocationId")));
                bean.setReportingTo(cur.getString(cur.getColumnIndex("ReportingTo")));
                bean.setPhone(cur.getString(cur.getColumnIndex("Phone")));
                bean.setDOB(cur.getString(cur.getColumnIndex("DOB")));
                bean.setDOJ(cur.getString(cur.getColumnIndex("DOJ")));
                bean.setIsActive(cur.getString(cur.getColumnIndex("IsActive")));
                bean.setIsReportingUsingExcel(cur.getString(cur.getColumnIndex("IsReportingUsingExcel")));
                bean.setDesignationId(cur.getString(cur.getColumnIndex("DesignationId")));
                bean.setEmpID(cur.getString(cur.getColumnIndex("EmpID")));
                bean.setFirstName(cur.getString(cur.getColumnIndex("FirstName")));
                bean.setLastName(cur.getString(cur.getColumnIndex("LastName")));
                bean.setISUProfileExpire(cur.getString(cur.getColumnIndex("ISUProfileExpire")));
                bean.setUProfileExpDate(cur.getString(cur.getColumnIndex("UProfileExpDate")));
                bean.setPlantMasterId(cur.getString(cur.getColumnIndex("PlantMasterId")));
                bean.setActvDactvDt(cur.getString(cur.getColumnIndex("ActvDactvDt")));
                bean.setCRMCode(cur.getString(cur.getColumnIndex("CRMCode")));
                bean.setCRMCategory(cur.getString(cur.getColumnIndex("CRMCategory")));
                bean.setCRMNoofDays(cur.getString(cur.getColumnIndex("CRMNoofDays")));
                bean.setPswModifiedDt(cur.getString(cur.getColumnIndex("PswModifiedDt")));
                bean.setLastLoginDt(cur.getString(cur.getColumnIndex("LastLoginDt")));
                bean.setIsDisabled(cur.getString(cur.getColumnIndex("IsDisabled")));
                bean.setFailedLoginAttempt(cur.getString(cur.getColumnIndex("FailedLoginAttempt")));
                bean.setCreationLevel(cur.getString(cur.getColumnIndex("CreationLevel")));
                bean.setUserLevel(cur.getString(cur.getColumnIndex("UserLevel")));
                bean.setIsDeleted(cur.getString(cur.getColumnIndex("IsDeleted")));
                bean.setAddedBy1(cur.getString(cur.getColumnIndex("AddedBy1")));
                bean.setAddedDt2(cur.getString(cur.getColumnIndex("AddedDt2")));
                bean.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                bean.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                bean.setIsCompleteRight(cur.getString(cur.getColumnIndex("IsCompleteRight")));
                bean.setAllowEditingDays(cur.getString(cur.getColumnIndex("AllowEditingDays")));
                bean.setMiddleName(cur.getString(cur.getColumnIndex("MiddleName")));
                bean.setChangePass1stLogin(cur.getString(cur.getColumnIndex("ChangePass1stLogin")));
                bean.setIsClientContact(cur.getString(cur.getColumnIndex("IsClientContact")));
                bean.setActive(cur.getString(cur.getColumnIndex("Active")));
                bean.setGender(cur.getString(cur.getColumnIndex("Gender")));
                bean.setIsSysUser(cur.getString(cur.getColumnIndex("IsSysUser")));
                bean.setMobileActive(cur.getString(cur.getColumnIndex("MobileActive")));
                bean.setRegularWKOff(cur.getString(cur.getColumnIndex("RegularWKOff")));
                bean.setAlternetWkOff(cur.getString(cur.getColumnIndex("AlternetWkOff")));
                bean.setAlternetWkOffVal(cur.getString(cur.getColumnIndex("AlternetWkOffVal")));
                bean.setEffectLeaveDate(cur.getString(cur.getColumnIndex("EffectLeaveDate")));
                bean.setIsBuyer(cur.getString(cur.getColumnIndex("IsBuyer")));
                bean.setSeptDate(cur.getString(cur.getColumnIndex("SeptDate")));
                bean.setInTime(cur.getString(cur.getColumnIndex("InTime")));
                bean.setOutTime(cur.getString(cur.getColumnIndex("OutTime")));
                bean.setInOutFlg(cur.getString(cur.getColumnIndex("InOutFlg")));
                bean.setFkShiftMasterId(cur.getString(cur.getColumnIndex("FkShiftMasterId")));
                bean.setIsAttendanceRecordMandatory(cur.getString(cur.getColumnIndex("IsAttendanceRecordMandatory")));
                bean.setConsiderWeeklyOff(cur.getString(cur.getColumnIndex("ConsiderWeeklyOff")));
                bean.setIsIntDeptEmp(cur.getString(cur.getColumnIndex("IsIntDeptEmp")));
                bean.setMembershipNo(cur.getString(cur.getColumnIndex("MembershipNo")));
                bean.setFKUserCategoryId(cur.getString(cur.getColumnIndex("FKUserCategoryId")));
                bean.setPreState(cur.getString(cur.getColumnIndex("PerState")));
                bean.setPerCountry(cur.getString(cur.getColumnIndex("PerCountry")));
                bean.setPreCountry(cur.getString(cur.getColumnIndex("PreCountry")));
                bean.setPreCode(cur.getString(cur.getColumnIndex("PreCode")));
                bean.setPerCode(cur.getString(cur.getColumnIndex("PerCode")));
                bean.setPerAddr(cur.getString(cur.getColumnIndex("PerAddr")));
                bean.setPreAddr(cur.getString(cur.getColumnIndex("PreAddr")));
                bean.setPerCity(cur.getString(cur.getColumnIndex("PerCity")));
                bean.setPreCity(cur.getString(cur.getColumnIndex("PreCity")));
                bean.setAllowUsrTimeSlotHrs(cur.getString(cur.getColumnIndex("AllowUsrTimeSlotHrs")));
                bean.setSameAsPresent(cur.getString(cur.getColumnIndex("SameAsPresent")));
                bean.setUseShift(cur.getString(cur.getColumnIndex("UseShift")));
                bean.setPreState(cur.getString(cur.getColumnIndex("PreState")));
                bean.setIsHRExecutive(cur.getString(cur.getColumnIndex("IsHRExecutive")));
                bean.setEntityContactInfoId(cur.getString(cur.getColumnIndex("EntityContactInfoId")));
                bean.setBQM_BanquetClientId(cur.getString(cur.getColumnIndex("BQM_BanquetClientId")));
                bean.setContPerName(cur.getString(cur.getColumnIndex("ContPerName")));
                bean.setDesignation(cur.getString(cur.getColumnIndex("Designation")));
                bean.setEmailId(cur.getString(cur.getColumnIndex("EmailId")));
                bean.setContactNo(cur.getString(cur.getColumnIndex("ContactNo")));
                bean.setInfluentialLevel(cur.getString(cur.getColumnIndex("InfluentialLevel")));
                bean.setIsDefault(cur.getString(cur.getColumnIndex("IsDefault")));
                bean.setIsDeleted1(cur.getString(cur.getColumnIndex("IsDeleted1")));
                bean.setContactType(cur.getString(cur.getColumnIndex("ContactType")));
                bean.setIsVWBLoginAllowed(cur.getString(cur.getColumnIndex("IsVWBLoginAllowed")));
                bean.setIsTimeAllow(cur.getString(cur.getColumnIndex("IsTimeAllow")));
                bean.setLoginId(cur.getString(cur.getColumnIndex("LoginId")));
                bean.setPassword(cur.getString(cur.getColumnIndex("Password")));
                bean.setIsApprovalAllowed(cur.getString(cur.getColumnIndex("IsApprovalAllowed")));
                bean.setEmailVerificationKey(cur.getString(cur.getColumnIndex("EmailVerificationKey")));
                bean.setIsActAllocAllowed(cur.getString(cur.getColumnIndex("IsActAllocAllowed")));
                bean.setUserMasterId1(cur.getString(cur.getColumnIndex("UserMasterId1")));
                bean.setUserLoginId1(cur.getString(cur.getColumnIndex("UserLoginId1")));
                bean.setUserName1(cur.getString(cur.getColumnIndex("UserName1")));
                bean.setTitle1(cur.getString(cur.getColumnIndex("Title1")));
                bean.setUserPassword1(cur.getString(cur.getColumnIndex("UserPassword1")));
                bean.setHintQuestion1(cur.getString(cur.getColumnIndex("HintQuestion1")));
                bean.setAnswer1(cur.getString(cur.getColumnIndex("Answer1")));
                bean.setDeptMasterId1(cur.getString(cur.getColumnIndex("DeptMasterId1")));
                bean.setEmail1(cur.getString(cur.getColumnIndex("Email1")));
                bean.setMobile1(cur.getString(cur.getColumnIndex("Mobile1")));
                bean.setExtNo1(cur.getString(cur.getColumnIndex("ExtNo1")));
                bean.setLocationId1(cur.getString(cur.getColumnIndex("LocationId1")));
                bean.setReportingTo1(cur.getString(cur.getColumnIndex("ReportingTo1")));
                bean.setPhone1(cur.getString(cur.getColumnIndex("Phone1")));
                bean.setDOB1(cur.getString(cur.getColumnIndex("DOB1")));
                bean.setDOJ1(cur.getString(cur.getColumnIndex("DOJ1")));
                bean.setIsActive1(cur.getString(cur.getColumnIndex("IsActive1")));
                bean.setIsReportingUsingExcel1(cur.getString(cur.getColumnIndex("IsReportingUsingExcel1")));
                bean.setDesignationId1(cur.getString(cur.getColumnIndex("DesignationId1")));
                bean.setEmpID1(cur.getString(cur.getColumnIndex("EmpID1")));
                bean.setFirstName1(cur.getString(cur.getColumnIndex("FirstName1")));
                bean.setLastName1(cur.getString(cur.getColumnIndex("LastName1")));
                bean.setISUProfileExpire1(cur.getString(cur.getColumnIndex("ISUProfileExpire1")));
                bean.setUProfileExpDate1(cur.getString(cur.getColumnIndex("UProfileExpDate1")));
                bean.setPlantMasterId1(cur.getString(cur.getColumnIndex("PlantMasterId1")));
                bean.setActvDactvDt1(cur.getString(cur.getColumnIndex("ActvDactvDt1")));
                bean.setCRMCode1(cur.getString(cur.getColumnIndex("CRMCode1")));
                bean.setCRMCategory1(cur.getString(cur.getColumnIndex("CRMCategory1")));
                bean.setCRMNoofDays1(cur.getString(cur.getColumnIndex("CRMNoofDays1")));
                bean.setPswModifiedDt1(cur.getString(cur.getColumnIndex("PswModifiedDt1")));
                bean.setLastLoginDt1(cur.getString(cur.getColumnIndex("LastLoginDt1")));
                bean.setIsDisabled1(cur.getString(cur.getColumnIndex("IsDisabled1")));
                bean.setFailedLoginAttempt1(cur.getString(cur.getColumnIndex("FailedLoginAttempt1")));
                bean.setCreationLevel1(cur.getString(cur.getColumnIndex("CreationLevel1")));
                bean.setUserLevel1(cur.getString(cur.getColumnIndex("UserLevel1")));
                bean.setIsDeleted2(cur.getString(cur.getColumnIndex("IsDeleted2")));
                bean.setAddedBy2(cur.getString(cur.getColumnIndex("AddedBy2")));
                bean.setAddedDt3(cur.getString(cur.getColumnIndex("AddedDt3")));
                bean.setModifiedBy1(cur.getString(cur.getColumnIndex("ModifiedBy1")));
                bean.setModifiedDt1(cur.getString(cur.getColumnIndex("ModifiedDt1")));
                bean.setIsCompleteRight1(cur.getString(cur.getColumnIndex("IsCompleteRight1")));
                bean.setAllowEditingDays1(cur.getString(cur.getColumnIndex("AllowEditingDays1")));
                bean.setMiddleName1(cur.getString(cur.getColumnIndex("MiddleName1")));
                bean.setChangePass1stLogin1(cur.getString(cur.getColumnIndex("ChangePass1stLogin1")));
                bean.setIsClientContact1(cur.getString(cur.getColumnIndex("IsClientContact1")));
                bean.setActive1(cur.getString(cur.getColumnIndex("Active1")));
                bean.setGender1(cur.getString(cur.getColumnIndex("Gender1")));
                bean.setIsSysUser1(cur.getString(cur.getColumnIndex("IsSysUser1")));
                bean.setMobileActive1(cur.getString(cur.getColumnIndex("MobileActive1")));
                bean.setSeptDate1(cur.getString(cur.getColumnIndex("SeptDate1")));
                bean.setType1(cur.getString(cur.getColumnIndex("Type1")));
                bean.setCd(cur.getString(cur.getColumnIndex("Cd")));
                bean.setDesc_r1(cur.getString(cur.getColumnIndex("Desc_r1")));
                bean.setLongDesc(cur.getString(cur.getColumnIndex("LongDesc")));
                bean.setFixed(cur.getString(cur.getColumnIndex("Fixed")));
                bean.setIsDeleted3(cur.getString(cur.getColumnIndex("IsDeleted3")));
                lsRecordList.add(bean);
            } while (cur.moveToNext());
        }
       // mAdapter = new (getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    class DownloadGetLeaveRecords extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getActivityTrailDetails+"?activityId="+ActivityId;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                SQLiteDatabase sql = db.getWritableDatabase();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_ACTIVITY_TRAIL, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ACTIVITY_TRAIL, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_ACTIVITY_TRAIL, null, values);
                    String s = a + "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            // DismissProgress();
            if (integer.contains("Narration")) {
                //    updateRecordList();
            } else {
                Toast.makeText(getApplicationContext(), "Could not connect to the server", Toast.LENGTH_LONG).show();
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
        switch (id) {

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
