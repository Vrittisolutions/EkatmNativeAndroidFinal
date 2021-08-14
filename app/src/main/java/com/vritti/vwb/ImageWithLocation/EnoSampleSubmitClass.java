package com.vritti.vwb.ImageWithLocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.EnoJobService;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityMain;
import com.vritti.vwb.vworkbench.LoggingTimeActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.vritti.vwb.vworkbench.ActivityMain.AtendanceSheredPreferance;

public class EnoSampleSubmitClass extends Activity {

    @BindView(R.id.txttotalQuant)
    EditText txttotalQuant;

    @BindView(R.id.txtsampleunit)
    EditText txtsampleunit;

    @BindView(R.id.txtsaleunit)
    EditText txtsaleunit;
    ArrayList<SamplePojoClass> myList;
    FinalObjectForENO finalObjectForENO;
    SampleActivityObject sampleActivityObject;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;

    public static FirebaseJobDispatcher dispatcherNew ;
    public static Job myJobNew = null;
    private GoogleApiClient googleApiClient=null;
String ActivityId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eno_sample_submit);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            myList = (ArrayList<SamplePojoClass>) getIntent().getSerializableExtra("sampleList");
            sampleActivityObject = new Gson().fromJson(getIntent().getStringExtra("finalObj"), SampleActivityObject.class);
            ActivityId = getIntent().getStringExtra("activityId");
        }
        dbInti();
    }

    private void dbInti() {
        context = EnoSampleSubmitClass.this;
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
    }


    @OnClick(R.id.btn_finalsave)
    void finalSubmit() {
        finalObjectForENO = new FinalObjectForENO();
        String sampleUnitStr = txtsampleunit.getText().toString().trim();
        String saleUnit = txtsaleunit.getText().toString().trim();
        if (sampleUnitStr.isEmpty()) {
            Toast.makeText(this, "Please enter sample unit!", Toast.LENGTH_SHORT).show();
        } else if (saleUnit.isEmpty()) {
            Toast.makeText(this, "Please enter sale unit!", Toast.LENGTH_SHORT).show();
        } else {
            sampleActivityObject.setSampleUnit(sampleUnitStr);
            sampleActivityObject.setSaleUnit(saleUnit);
            finalObjectForENO.setSampleArrayList(myList);
            Date now = new Date(); // java.util.Date, NOT java.sql.Date
            String format2 = new SimpleDateFormat("dd-MMM-yy ").format(now);
            sampleActivityObject.setEndDate(format2);
            finalObjectForENO.setActivityObject(sampleActivityObject);
            ArrayList<FileExtantionArray> fileExtantionArrays = new ArrayList<>();
            try{
                SQLiteDatabase sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_DATA_OFFLINE, "output=?",
                            new String[]{ActivityId});


            }catch (Exception e){
                e.printStackTrace();
            }
            for (SamplePojoClass samplePojoClass : finalObjectForENO.getSampleArrayList()) {
                String path[] = samplePojoClass.getImageUri().split("file:///");
                File f = new File(samplePojoClass.getImageUri());

                String mimeType = path[1].substring(path[1].lastIndexOf("."));
                fileExtantionArrays.add(new FileExtantionArray(finalObjectForENO.getActivityObject().getActivityId(), finalObjectForENO.getActivityObject().getActivityId(), f.getName(), f.getName()));
                String remark = "Send " + f.getName() + " to server having upload time" + samplePojoClass.getDate();
                //  CreateOfflineSaveAttachment(path[1], f.getName(), WebUrlClass.ATTACHMENTFlAG, remark, ActivityId);
                 CreateOfflineSaveAttachment(path[1], f.getName(), 3, remark, ActivityId);

            }

            finalObjectForENO.setStoreUploadedFile(fileExtantionArrays);
            ListObjectForEno listObjectForEno = new Gson().fromJson(AppCommon.getInstance(this).getEnoSampelList(), ListObjectForEno.class);

            if (listObjectForEno.getFinalObjectForENOArrayList() != null)
                listObjectForEno.getFinalObjectForENOArrayList().add(finalObjectForENO);
            else {
                ArrayList<FinalObjectForENO> finalObjectForENOS = new ArrayList();
                listObjectForEno.setFinalObjectForENOArrayList(finalObjectForENOS);
                listObjectForEno.getFinalObjectForENOArrayList().add(finalObjectForENO);
            }
                AppCommon.getInstance(this).setFilnalListSampl(new Gson().toJson(listObjectForEno));
        }

    }

    private void CreateOfflineSaveAttachment(String imageUri, String name, int attachmentFlAG, String remark, String activityId) {
        long a = cf.addofflinedata(imageUri, name, attachmentFlAG, remark, activityId);
        if (a != -1) {
            // _flagAttachment = true;

          /*  Toast.makeText(getApplicationContext(), "Attachment Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);*/
            setJobShedulder();
            SQLiteDatabase sql = db.getWritableDatabase();

            Cursor c2 = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
            c2.getCount();
            Log.e("ActivityCountc1 :", "" + c2.getCount());

          //  String ActivityID = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
           // Log.e("Activity :", "" + ActivityID);

            String Ad = ActivityId;
            sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
            sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});
            SharedPreferences.Editor editor = AtendanceSheredPreferance
                    .edit();
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
            editor.putString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
            editor.commit();
            Cursor c1 = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
            c1.getCount();
            Log.e("ActivityCountc2 :", "" + c1.getCount());

            startActivity(new Intent(this , ActivityMain.class));
            finishAffinity();
        } else {
            Toast.makeText(getApplicationContext(), "Attachment not Saved", Toast.LENGTH_LONG).show();

        }
    }

    private void setJobShedulder() {
        if(myJobNew == null) {
            dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(EnoSampleSubmitClass.this));
            callJobDispacher();
        }
        else{
            if(!AppCommon.getInstance(this).isServiceIsStart()){
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(EnoSampleSubmitClass.this));
                callJobDispacher();
            }else {
                dispatcherNew.cancelAll();
                dispatcherNew = new FirebaseJobDispatcher(new GooglePlayDriver(EnoSampleSubmitClass.this));
                myJobNew = null;
                callJobDispacher();
            }
        }
    }

    private void callJobDispacher() {
        myJobNew = dispatcherNew.newJobBuilder()
                // the JobService that will be called
                .setService(EnoJobService.class)
                // uniquely identifies the job
                .setTag("Eno")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)

                // start between 0 and 60 seconds from now
                .setTrigger(Trigger.executionWindow(0, 180))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(

                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK,
                        // only run when the device is charging
                        Constraint.DEVICE_IDLE


                )
                .build();

        dispatcherNew.mustSchedule(myJobNew);
        AppCommon.getInstance(this).setServiceStarted(true);
    }
}
