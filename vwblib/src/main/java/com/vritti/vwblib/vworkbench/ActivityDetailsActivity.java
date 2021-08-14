package com.vritti.vwblib.vworkbench;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Beans.ActivityBean;
import com.vritti.vwblib.Beans.Attachment;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.SendOfflineData;
import com.vritti.vwblib.chat.AttachmentsActivity;
import com.vritti.vwblib.chat.MultipleGroupActivity;
import com.vritti.vwblib.classes.CommonFunction;

/**
 * Created by 300151 on 11/15/2016.
 */
public class ActivityDetailsActivity extends AppCompatActivity {
   // String[] options;
    ListView ls_activity_option;
    String    SourceType, ProjectName, Status, Assigned_By, ActivityCode, Assigned_To;
    static String ActivityName,ActivityId,SourceId;
    TextView txtstatus, txtprojectgroup, txtassignedby, tv_activity_name;
    Spinner sp_project, sp_module, sp_unit;
    Button btn_save, btn_cancel;
    int year, month, day;
    ArrayList<String> Workspace_list, Subgroup_List, MainGroup_List;
    public static String prjMstId, moduleId, UnitId;
    JSONObject ChangeWorkspaceObj, ChangeActivityStatus, RescheduleObj;
    String FinalObj, ProjectId, PAllowUsrTimeSlotHrs;
    SharedPreferences userpreferences;
    Spinner sp_change_status;
    LinearLayout mLinCharged;
    EditText mEditChaged;
    Button btn_reschedule, btn_rescheduleOk, btn_rescheduleCancel;
    TextView txt_activityName;
    int flag_fromTeam;
    String Mode = "";
    static String StatusFlag = "";
    String IsChargable,
            AssignedById,
            SubActCount,
            SubActStaus,
            ExpectedCompleteDate,
            ExpectedComplete_Date,
            ModifiedBy,
            Modified_By,
            StartDt,
            EndDt,
            IsActivityMandatory,
            IsDelayedActivityAllowed,
            Cd,
            PKModuleMastId,
            PriorityName,
            Colour,
            AddedDt,
            ModifiedDt,
            AssignedById1,
            IsDeleted,
            IsApproved,
            IsChargable1,
            ActivityTypeId,
            IsApproval,
            AttachmentName,
            AttachmentContent,
            ModifiedDt1,
            UnitName,
            UnitDesc,
            ModuleName,
            ActivityName1,
            Remarks,
            ProjectCode,
            ExpectedComplete_Date1,
            DeptDesc,
            DeptMasterId,
            CompletionIntimate,
            ModifiedBy1,
            ReassignedBy,
            ReassignedDt,
            ActualCompletionDate,
            WarrantyCode,
            TicketCategory,
            IsEndTime,
            IsCompActPresent,
            CompletionActId,
            TktCustReportedBy,
            TktCustApprovedBy,
            IsSubActivity,
            ParentActId,
            ActivityTypeName,
            CompActName, ConMob;
     String IsChatApplicable;
    ArrayList<Attachment> attachmentArrayList=new ArrayList<>();
    String Attachment_count="",Assign,unapprove="";
    public static ProgressBar mprogress;
    EditText edt_remark;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_activity_details);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        IsChatApplicable=userpreferences.getString("chatapplicable","");
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


        Intent intent = getIntent();
        Assign=intent.getStringExtra("checkassign");
        unapprove=intent.getStringExtra("unapprove");

        Bundle bundle = intent.getExtras();
        ls_activity_option = (ListView) findViewById(R.id.ls_activity_option);
        attachmentArrayList=new ArrayList<>();

        if (bundle != null) {
            ActivityBean selectedObj = (ActivityBean) bundle.getSerializable("actbean");
            /*ActivityName = i.getStringExtra("ActivityName");
            ActivityId = i.getStringExtra("ActivityId");
            SourceType = i.getStringExtra("SourceType");
            Assigned_By = i.getStringExtra("Assigned_By");
            ProjectName = i.getStringExtra("ProjectName");
            Status = i.getStringExtra("Status");
            SourceId = i.getStringExtra("SourceId");//Assigned_To
            Assigned_To = i.getStringExtra("Assigned_To");
            ProjectId = i.getStringExtra("ProjectId");//PAllowUsrTimeSlotHrs
            PAllowUsrTimeSlotHrs = i.getStringExtra("PAllowUsrTimeSlotHrs");//*/


            ActivityName = selectedObj.getActivityName();
            ActivityId = selectedObj.getActivityId();
            SourceType = selectedObj.getSourceType();
            Assigned_By = selectedObj.getAssigned_By();
            ProjectName = selectedObj.getProjectName();
            Status = selectedObj.getStatus();
            SourceId = selectedObj.getSourceId();
            Assigned_To = selectedObj.getIssuedToName();
            ProjectId = selectedObj.getProjectID();
            PAllowUsrTimeSlotHrs = selectedObj.getPAllowUsrTimeSlotHrs();

            IsChargable = selectedObj.getIsChargable();
            AssignedById = selectedObj.getAssignedById();
            //SubActCount = selectedObj.getSubActCount();
            //SubActStaus = selectedObj.getSubActStaus();
            ExpectedCompleteDate = selectedObj.getExpectedCompleteDate();
            ExpectedComplete_Date = selectedObj.getExpectedComplete_Date();
            ModifiedBy = selectedObj.getModifiedBy();
            Modified_By = selectedObj.getModified_By();
            StartDt = selectedObj.getStartDt();
            EndDt = selectedObj.getEndDt();
            IsActivityMandatory = selectedObj.getIsActivityMandatory();
            IsDelayedActivityAllowed = selectedObj.getIsDelayedActivityAllowed();
            Cd = selectedObj.getCd();
            PKModuleMastId = selectedObj.getPKModuleMastId();
            PriorityName = selectedObj.getPriorityName();
            Colour = selectedObj.getColour();
            AddedDt = selectedObj.getAddedDt();
            ModifiedDt = selectedObj.getModifiedDt();
            AssignedById1 = selectedObj.getAssignedById1();
            IsDeleted = selectedObj.getIsDeleted();
            IsApproved = selectedObj.getIsApproved();
            IsChargable1 = selectedObj.getIsChargable1();
            ActivityTypeId = selectedObj.getActivityTypeId();
            IsApproval = selectedObj.getIsApproval();
            AttachmentName = selectedObj.getAttachmentName();
            AttachmentContent = selectedObj.getAttachmentContent();
            ModifiedDt1 = selectedObj.getModifiedDt1();
            UnitName = selectedObj.getUnitName();
            UnitDesc = selectedObj.getUnitDesc();
            ModuleName = selectedObj.getModuleName();
            ActivityName1 = selectedObj.getActivityName1();
            Remarks = selectedObj.getRemarks();
            ProjectCode = selectedObj.getProjectCode();
            ExpectedComplete_Date1 = selectedObj.getExpectedComplete_Date1();
            DeptDesc = selectedObj.getDeptDesc();
            DeptMasterId = selectedObj.getDeptMasterId();
            CompletionIntimate = selectedObj.getCompletionIntimate();
            ModifiedBy1 = selectedObj.getModifiedBy1();
            ReassignedBy = selectedObj.getReassignedBy();
            ReassignedDt = selectedObj.getReassignedDt();
            ActualCompletionDate = selectedObj.getActualCompletionDate();
            WarrantyCode = selectedObj.getWarrantyCode();
            TicketCategory = selectedObj.getTicketCategory();
            IsEndTime = selectedObj.getIsEndTime();
            IsCompActPresent = selectedObj.getIsCompActPresent();
            CompletionActId = selectedObj.getCompletionActId();
            TktCustReportedBy = selectedObj.getTktCustReportedBy();
            TktCustApprovedBy = selectedObj.getTktCustApprovedBy();
            IsSubActivity = selectedObj.getIsSubActivity();
            ParentActId = selectedObj.getParentActId();
            ActivityTypeName = selectedObj.getActivityTypeName();
            ConMob = selectedObj.getContMob();
            CompActName = selectedObj.getCompActName();

            flag_fromTeam = intent.getIntExtra("Flag_fromteam", 0);
            if (intent.hasExtra("Mode")) {
                Mode = intent.getStringExtra("Mode");
            }

        }
       /* if (getIntent().hasExtra("SourceId")) {
            Intent i = getIntent();
  }*/

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);



        if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUploadAttachment().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        if (SourceType==null){

        }else {

        if (SourceType.equalsIgnoreCase("DocAppr") || SourceType.equalsIgnoreCase("PreBid") || SourceType.equalsIgnoreCase("Indent") || SourceType.equalsIgnoreCase("PO") || SourceType.equalsIgnoreCase("SO") || SourceType.equalsIgnoreCase("Ship") || SourceType.equalsIgnoreCase("CLAIM") || SourceType.equalsIgnoreCase("Leave") || SourceType.equalsIgnoreCase("ExtraWork") || SourceType.equalsIgnoreCase("LeaveApply") || SourceType.equalsIgnoreCase("LeaveCancel") || SourceType.equalsIgnoreCase("RRF")) {
            // options = new String[]{"Chat", "open","Attachments"};
            attachmentArrayList.add(new Attachment("Chat", ""));
            attachmentArrayList.add(new Attachment("Open", ""));
            // attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

        } else {
            if (SourceType.equalsIgnoreCase("Datasheet")) {

                attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                attachmentArrayList.add(new Attachment("Change status", ""));
                // options = new String[]{"Fill datasheet", "Change status"};
            } else if (SourceType.equalsIgnoreCase("Support")) {
                attachmentArrayList.add(new Attachment("Chat", ""));
                attachmentArrayList.add(new Attachment("View ticket", ""));
                attachmentArrayList.add(new Attachment("Subordinate task", ""));
                attachmentArrayList.add(new Attachment("Activity trail", ""));
                // attachmentArrayList.add(new Attachment("Timesheet log", ""));

                // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
            } else if (unapprove.equalsIgnoreCase("true")) {
                attachmentArrayList.add(new Attachment("Approve", ""));
                attachmentArrayList.add(new Attachment("Disapprove", ""));
                attachmentArrayList.add(new Attachment("Activity trail", ""));

                // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
            } else {
                if (Mode.equalsIgnoreCase("")) {
                    if (flag_fromTeam == 1) {
                        attachmentArrayList.add(new Attachment("Chat", ""));
                        attachmentArrayList.add(new Attachment("Change status", ""));
                        attachmentArrayList.add(new Attachment("Reschedule", ""));
                        attachmentArrayList.add(new Attachment("Reassign", ""));
                        attachmentArrayList.add(new Attachment("Subordinate task", ""));
                        attachmentArrayList.add(new Attachment("Change assignment", ""));
                        attachmentArrayList.add(new Attachment("Activity trail", ""));
                        //    attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                        // options = new String[]{"Chat", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                    } else {
                        attachmentArrayList.add(new Attachment("Chat", ""));
                        attachmentArrayList.add(new Attachment("Timesheet", ""));
                        attachmentArrayList.add(new Attachment("Timesheet log", ""));
                        attachmentArrayList.add(new Attachment("Change status", ""));
                        attachmentArrayList.add(new Attachment("Reschedule", ""));
                        attachmentArrayList.add(new Attachment("Reassign", ""));
                        attachmentArrayList.add(new Attachment("Subordinate task", ""));
                        attachmentArrayList.add(new Attachment("Change assignment", ""));
                        attachmentArrayList.add(new Attachment("Activity trail", ""));
                        //    attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                        // options = new String[]{"Chat", "Timesheet", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                    }
                } else {

                    attachmentArrayList.add(new Attachment("Chat", ""));
                    attachmentArrayList.add(new Attachment("Approve", ""));
                    attachmentArrayList.add(new Attachment("Disapprove", ""));
                    attachmentArrayList.add(new Attachment("Activity trail", ""));
                    //  attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                    //  options = new String[]{"Chat", "Approve", "Disapprove", "Activity trail","Attachments"};
                }
            }
        }
        }
        MySpinnerAdapter customoption = new MySpinnerAdapter(ActivityDetailsActivity.this,
                R.layout.vwb_custom_spinner_txt, attachmentArrayList);
        ls_activity_option.setAdapter(customoption);

        InitView();
        setListner();
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle("  vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        txtstatus = (TextView) findViewById(R.id.txtstatus);
        txtprojectgroup = (TextView) findViewById(R.id.txtprojectgroup);
        txtassignedby = (TextView) findViewById(R.id.txtassignedby);
        txt_activityName = (TextView) findViewById(R.id.txt_activityName);
        txtassignedby.setText(Assigned_By);
        txtprojectgroup.setText(ProjectName);
        txtstatus.setText(Status);
        txt_activityName.setText(ActivityName);
     //   ls_activity_option.setAdapter(new ActivityOptionAdapter(this, options));




        Workspace_list = new ArrayList<String>();
        Subgroup_List = new ArrayList<String>();
        MainGroup_List = new ArrayList<String>();
    }

    private void setListner() {
        ls_activity_option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String option = attachmentArrayList.get(position).getOption();
                if (option.equalsIgnoreCase("Timesheet")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, AddTimesheetActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("PAllowUsrTimeSlotHrs", PAllowUsrTimeSlotHrs);
                    startActivity(intent);
                } else if (option.equalsIgnoreCase("Reassign")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, ReassignActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);//Assigned_To
                    intent.putExtra("Assigned_To", Assigned_To);
                    intent.putExtra("ProjectId", ProjectId);
                    intent.putExtra("FlagFromTeam", flag_fromTeam);
                    startActivity(intent);

                } else if (option.equalsIgnoreCase("fill Datasheet")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, DatasheetMainActivity.class);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("SourceId", SourceId);
                    startActivity(intent);
                    finish();

                } else if (option.equalsIgnoreCase("Change status")) {
                    CreateChangeStatusdialog();
                } else if (option.equalsIgnoreCase("Change assignment")) {
                    CreateChangeWorkspaceDialog();
                } else if (option.equalsIgnoreCase("Reschedule")) {
                    CreateRescheduledialog();
                } else if (option.equalsIgnoreCase("Subordinate task")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, AssignActivity.class);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("IsSubordinate", "Y");
                    startActivity(intent);
                    finish();
                } else if (option.equalsIgnoreCase("Open")) {
                    Intent intent = new Intent(ActivityDetailsActivity.this, ActivityIndentApprovalNew.class);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityNAme", ActivityName);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("AssignedById",AssignedById);
                    intent.putExtra("Assigned_By",Assigned_By);
                    startActivity(intent);
                    finish();
                } else if (option.equalsIgnoreCase("View ticket")) {
                    String company_name = "http://www.appnet.co.in";
                    String company_name1 = "http://d207.ekatm.com";
                    String company_name2 = "http://www.dconnect.in";
                    //www.appnet.co.in
                    if (CompanyURL.equals(company_name) || CompanyURL.equals(company_name1)|| CompanyURL.equals(company_name2)) {
                        Intent intent = new Intent(ActivityDetailsActivity.this, TicketUpdateDEPLActivity.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("ProjectID", ProjectId);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(ActivityDetailsActivity.this, ViewTicketMain.class);
                        intent.putExtra("ActivityId", ActivityId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("ProjectID", ProjectId);
                        intent.putExtra("Contact", ConMob);
                        startActivity(intent);
                        finish();
                    }
                } else if (option.equalsIgnoreCase("Approve")) {
                    if (unapprove.equalsIgnoreCase("true")){
                        CreateApprrovejson();
                    }else {
                        CreateApprrovedialog();
                    }
                } else if (option.equalsIgnoreCase("Disapprove")) {
                    if (unapprove.equalsIgnoreCase("true")){
                        CreateDisapprrovejson();
                    }else {
                        ChangedisApproveJson();
                    }
                } else if (option.equalsIgnoreCase("Activity trail")) {
                    startActivity(new Intent(ActivityDetailsActivity.this, ActivityTrailActivity.class).putExtra("ActId",ActivityId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


                } else if (option.equalsIgnoreCase("Chat")) {
                    if (IsChatApplicable.equalsIgnoreCase("true")) {
                        Intent intent = new Intent(ActivityDetailsActivity.this, MultipleGroupActivity.class);
                        intent.putExtra("callid", ActivityId); // Source Id
                        intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + SourceType);// Source Type
                        intent.putExtra("firm", ActivityName);
                        intent.putExtra("projmasterId", ProjectId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Chat module is not installed", Toast.LENGTH_SHORT).show();
                    }
                } else if (option.equalsIgnoreCase("Attachments")) {

                       startActivity(new Intent(ActivityDetailsActivity.this, AttachmentsActivity.class).putExtra("SourceId",SourceId));
                }
                else if (option.equalsIgnoreCase("Timesheet log")) {
                    startActivity(new Intent(ActivityDetailsActivity.this, TimeSheetLogActivity.class).putExtra("ActId",ActivityId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

            }
        });


    }

    public void CreateRescheduledialog() {
        Date Edate;
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.vwb_dialog_reschedule, null);
        dialogBuilder.setView(dialogView);
        /*final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_dialog_reschedule);
        dialog.setTitle("Select New End Date");*/
        btn_reschedule = (Button) dialogView.findViewById(R.id.btn_reschedule);
        btn_rescheduleCancel = (Button) dialogView.findViewById(R.id.btn_rescheduleCancel);
        btn_rescheduleOk = (Button) dialogView.findViewById(R.id.btn_rescheduleOk);
        edt_remark=(EditText) dialogView.findViewById(R.id.edt_remark);

        String que = "SELECT EndDate FROM " + db.TABLE_ACTIVITYMASTER + " WHERE ActivityId='" + ActivityId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            String EndDresults = cur.getString(cur.getColumnIndex("EndDate"));
            EndDresults = EndDresults.substring(EndDresults.indexOf("(") + 1, EndDresults.lastIndexOf(")"));
            long Etime = Long.parseLong(EndDresults);
            Edate = new Date(Etime);
            String FEndDate = sdf.format(Edate);
            btn_reschedule.setText(FEndDate);
        }
        btn_reschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth)) + "-" + year;
                                btn_reschedule.setText(date);


                            }
                        }, year, month, day);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.show();
        btn_rescheduleCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        btn_rescheduleOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRescheduleJSONObj();
                b.dismiss();
            }
        });
    }

    private void getRescheduleJSONObj() {
        RescheduleObj = new JSONObject();
        try {
            RescheduleObj.put("ActivityId", ActivityId);
            RescheduleObj.put("EndDate", btn_reschedule.getText().toString());
            RescheduleObj.put("ResRemark",edt_remark.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = RescheduleObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");

        String remark = "Reschedule activity  " + ActivityName + " to " + btn_reschedule.getText().toString();
        String url = CompanyURL + WebUrlClass.api_Reschedule + "?ActivityId=" + ActivityId + "&EndDate=" + btn_reschedule.getText().toString() +"&ResRemark=" + edt_remark.getText().toString();

       /* JSONObject jobj = new JSONObject();
        try {
            jobj.put("output1","Success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String op = jobj.toString();*/
        String op = "Success";
        CreateOfflineModeReschedule(url, null, WebUrlClass.GETFlAG, remark, op);
        onBackPressed();
       /* new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new UpdaterescheduleActivity().execute(ActivityId,btn_reschedule.getText().toString());
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
            }
        });*/
    }

    public void CreateChangeStatusdialog() {
        final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_change_activity_status);//ed_billable_client ln_billable_client
        sp_change_status = (Spinner) dialog.findViewById(R.id.sp_change_status);
        mLinCharged = (LinearLayout) dialog.findViewById(R.id.ln_billable_client);
        mEditChaged = (EditText) dialog.findViewById(R.id.ed_billable_client);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        String[] change_status = {"WIP", "Complete", "Pause", "Cancelled"};
        dialog.setCancelable(true);
        dialog.setTitle("Change Status");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, change_status);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_change_status.setAdapter(dataAdapter);
        dialog.show();
        sp_change_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sp_status = parent.getItemAtPosition(position).toString();
                if (sp_status.equalsIgnoreCase("Complete")) {
                    if (IsChargable.equalsIgnoreCase("true")
                            && IsApproval.equalsIgnoreCase("true")) {
                        mLinCharged.setVisibility(View.VISIBLE);
                    } else {
                        mLinCharged.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeActivityStatusJSONObj();
                dialog.dismiss();

            }
        });

    }


    public void CreateApprrovedialog() {
        final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_approve_periodic_billing);//ed_billable_client ln_billable_client
        mEditChaged = (EditText) dialog.findViewById(R.id.ed_billable_client);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_save);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialog.setCancelable(true);
        dialog.setTitle("Approve Status");
        dialog.show();
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mEditChaged.getText().toString().equalsIgnoreCase(""))) {
                    String amt = mEditChaged.getText().toString();
                    ChangeApproveJson(amt);

                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Amount", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    private void getProject() {
        Workspace_list.clear();
        String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("ProjectName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_project.setAdapter(adapter);
            sp_project.setSelection(Workspace_list.indexOf(ProjectName));
        }
        sql.close();
    }

    private void getMainGroupList() {
        MainGroup_List.clear();
        String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ProjectId='" + prjMstId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MainGroup_List.add(cur.getString(cur.getColumnIndex("ModuleName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, MainGroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_module.setAdapter(adapter);
        }
        sql.close();
    }

    private void getSubGroupList() {
        Subgroup_List.clear();
        String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE PKModuleMastId='" + moduleId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();

        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Subgroup_List.add(cur.getString(cur.getColumnIndex("UnitDesc")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityDetailsActivity.this, android.R.layout.simple_spinner_item, Subgroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_unit.setAdapter(adapter);
        }
        sql.close();
    }

    private void CreateChangeWorkspaceDialog() {
        final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
        dialog.setContentView(R.layout.vwb_dialog_change_workspace);
        tv_activity_name = (TextView) dialog.findViewById(R.id.tv_activity_name);
        sp_project = (Spinner) dialog.findViewById(R.id.sp_project);
        sp_module = (Spinner) dialog.findViewById(R.id.sp_module);
        sp_unit = (Spinner) dialog.findViewById(R.id.sp_unit);
        btn_save = (Button) dialog.findViewById(R.id.btn_save);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialog.setCancelable(true);
        dialog.setTitle("Change assignment");
        SetListnerChangeWorkspace();
        getProject();
        dialog.show();
        tv_activity_name.setText(ActivityName);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeWorkspaceJSONObj();
                dialog.dismiss();
            }
        });

    }

    private void CreateOfflineModeChageStatus(final String url, final String parameter,
                                              final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagCancel)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagWIP)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "14");
                values.put("Status", "WIP");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagPause)) {
                ContentValues values = new ContentValues();
                values.put("Cd", "25");
                values.put("Status", "PAUSED");
                sql1.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
            } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagComplete)) {
                sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            }
            sql1.close();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

    private void CreateOfflineModeChangeWorkspace(final String url, final String parameter,
                                                  final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void CreateOfflineModeDisapprove(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record saved for Disapprove", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void CreateOfflineModeReschedule(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler cf = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql = db.getWritableDatabase();
            String data = btn_reschedule.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date date = null;
            try {
                date = sdf.parse(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long startDate = date.getTime();
            String fiDate = "/Date(" + startDate + ")/";
            ContentValues values = new ContentValues();
            values.put("EndDate", fiDate);
            sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
            sql.update(db.TABLE_ACTIVITYMASTER_PAGING, values, "ActivityId=?", new String[]{ActivityId});
            Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }


    /*  private void SaveOfflineMode() {
          Button bt_save, bt_cancel;
          final Dialog dialog = new Dialog(ActivityDetailsActivity.this);
          dialog.setContentView(R.layout.vwb_dialog_custom_offline_check);
          bt_save = (Button) dialog.findViewById(R.id.btn_save);
          bt_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
          dialog.setCancelable(false);
          dialog.show();
          bt_cancel.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  dialog.dismiss();
              }
          });
          bt_save.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_LONG).show();
                  dialog.dismiss();
              }
          });

      }*/
    private void SetListnerChangeWorkspace() {

        sp_project.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ProjectName = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST + " WHERE ProjectName='" + ProjectName + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    prjMstId = cur.getString(cur.getColumnIndex("ProjectId"));
                }
                getMainGroupList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_module.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ModuleName = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ModuleName='" + ModuleName + "'";
                SQLiteDatabase sql = db.getWritableDatabase();
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    moduleId = cur.getString(cur.getColumnIndex("PKModuleMastId"));
                }
                getSubGroupList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitdesc = parent.getItemAtPosition(position).toString();


                String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE UnitDesc='" + unitdesc + "'";
                SQLiteDatabase sql = db.getWritableDatabase();

                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UnitId = cur.getString(cur.getColumnIndex("UnitId"));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void ChangeActivityStatusJSONObj() {

        ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

           /* string ActivityId = JsonData.ActivityId;
            string StatusCode = JsonData.StatusCode;
            string IsApproval = JsonData.IsApproval;
            string Amt = JsonData.Amt;*/
            ChangeActivityStatus.put("ActivityId", ActivityId);
            if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Cancelled")) {
                StatusFlag = WebUrlClass.FlagCancel;
                ChangeActivityStatus.put("StatusCode", "15");
                remark = "Cancel the activity " + ActivityName;

            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("WIP")) {
                StatusFlag = WebUrlClass.FlagWIP;
                ChangeActivityStatus.put("StatusCode", "14");
                remark = "Change status of activity " + ActivityName + " to WIP ";

            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Pause")) {
                StatusFlag = WebUrlClass.FlagPause;
                ChangeActivityStatus.put("StatusCode", "25");
                remark = "Change status of activity " + ActivityName + " to Pause ";
            } else if (sp_change_status.getSelectedItem().toString().equalsIgnoreCase("Complete")) {
                StatusFlag = WebUrlClass.FlagComplete;
                if (mLinCharged.getVisibility() == View.VISIBLE) {
                    String d = mEditChaged.getText().toString();
                    ChangeActivityStatus.put("StatusCode", "12");
                    ChangeActivityStatus.put("IsApproval", IsApproval);
                    ChangeActivityStatus.put("Amt", d);
                } else {
                    ChangeActivityStatus.put("StatusCode", "12");
                    ChangeActivityStatus.put("IsApproval", IsApproval);
                    ChangeActivityStatus.put("Amt", "");
                }

                remark = "Complete the activity " + ActivityName;

            }
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpdateChangeActivityStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {*/
        String url = CompanyURL + WebUrlClass.api_change_activity_status;
        String op = "Success";
        CreateOfflineModeChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);


    }

    private void ChangeApproveJson(String amt) {

        JSONObject ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

            ChangeActivityStatus.put("ActivityId", ActivityId);
            ChangeActivityStatus.put("StatusCode", amt);

            remark = "Approve the activity " + ActivityName + " with billable amount " + amt;

            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = CompanyURL + WebUrlClass.api_Approve_periodic;
        String op = "";
        CreateOfflineModeChangeWorkspace(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

    }

    private void ChangedisApproveJson() {

        JSONObject ChangeActivityStatus = new JSONObject();
        String remark = "";
        try {

            ChangeActivityStatus.put("ActivityId", ActivityId);
            remark = "DisApprove the activity " + ActivityName;
            FinalObj = ChangeActivityStatus.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = CompanyURL + WebUrlClass.api_Disapprove_periodic;
        String op = "";
        CreateOfflineModeDisapprove(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        finish();
    }

    private void ChangeWorkspaceJSONObj() {
        String que = "SELECT ActivityCode FROM " + db.TABLE_ACTIVITYMASTER + " WHERE ActivityId='" + ActivityId + "'";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            ActivityCode = cur.getString(cur.getColumnIndex("ActivityCode"));
        }
        ChangeWorkspaceObj = new JSONObject();
        try {
            ChangeWorkspaceObj.put("ActivityId", ActivityId);
            ChangeWorkspaceObj.put("ActivityNm", ActivityName);
            ChangeWorkspaceObj.put("ActivityCode", ActivityCode);
            ChangeWorkspaceObj.put("ProjectId", prjMstId);
            ChangeWorkspaceObj.put("UnitId", UnitId);
            ChangeWorkspaceObj.put("ModuleId", moduleId);
            ChangeWorkspaceObj.put("ActivityType", " ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        FinalObj = ChangeWorkspaceObj.toString();
        String remark = "Change assignment of activity  " + ActivityName + " to " + ProjectName;
        String url = CompanyURL + WebUrlClass.api_change_workspace;
        String op = "";
        CreateOfflineModeChangeWorkspace(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        onBackPressed();

       /* new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new UpdateChangeWorkspace().execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);

            }
        });*/
    }

    /* public class UpdateChangeWorkspace extends AsyncTask<Integer, Void, Integer> {
         Object res;
         String responce;

         @Override
         protected void onPreExecute() {
             super.onPreExecute();
         }

         @Override
         protected void onPostExecute(Integer integer) {
             super.onPostExecute(integer);
             if (responce.toString().equalsIgnoreCase("")) {
                 Toast.makeText(ActivityDetailsActivity.this, "Workspace changed successfully", Toast.LENGTH_LONG).show();
             }
         }

         @Override
         protected Integer doInBackground(Integer... params) {
             String url = CompanyURL + WebUrlClass.api_change_workspace;
             res = ut.OpenPostConnection(url, FinalObj);
             responce = res.toString().substring(1, res.toString().length() - 1);

             return null;
         }
     }
 */
    public class UpdaterescheduleActivity extends AsyncTask<String, Void, Integer> {
        Object res;
        String responce;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (responce.toString().equalsIgnoreCase("")) {
                Toast.makeText(ActivityDetailsActivity.this, "Activity Rescheduled successfully", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Reschedule + "?ActivityId=" + params[0] + "&EndDate=" + params[1];
            res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
            responce = res.toString().substring(1, res.toString().length() - 1);
            return 0;
        }
    }

    public class UpdateChangeActivityStatus extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String responce;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (responce.toString().equalsIgnoreCase("Success")) {
                SQLiteDatabase sql = db.getWritableDatabase();
                Toast.makeText(ActivityDetailsActivity.this, "Activity status changed successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ActivityDetailsActivity.this, ActivityMain.class);
                startActivity(intent);
                finish();
                if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagCancel)) {
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagWIP)) {
                    ContentValues values = new ContentValues();
                    values.put("Cd", "14");
                    values.put("Status", "WIP");
                    sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagPause)) {
                    ContentValues values = new ContentValues();
                    values.put("Cd", "25");
                    values.put("Status", "PAUSED");
                    sql.update(db.TABLE_ACTIVITYMASTER, values, "ActivityId=?", new String[]{ActivityId});
                } else if (StatusFlag.equalsIgnoreCase(WebUrlClass.FlagComplete)) {
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_change_activity_status;
            res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
            responce = res.toString().substring(1, res.toString().length() - 1);

            return null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    private  class MySpinnerAdapter extends ArrayAdapter<Attachment> {
        // Initialise custom font, for example:
        ArrayList<Attachment> attachmentArrayList = new ArrayList<>();


        public MySpinnerAdapter(Context context, int textViewResourceId, ArrayList<Attachment> attachmentArrayList) {
            super(context, textViewResourceId, attachmentArrayList);
            this.attachmentArrayList=attachmentArrayList;
            attachmentArrayList = attachmentArrayList;
        }
        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vwb_attachnmet_count, null);
            TextView textView = (TextView) v.findViewById(R.id.txt);
            TextView text_cartcount = (TextView) v.findViewById(R.id.text_cartcount);
            textView.setText(attachmentArrayList.get(position).getOption());

            if (attachmentArrayList.get(position).getOption().equalsIgnoreCase("Attachments")) {
                text_cartcount.setVisibility(View.VISIBLE);
                text_cartcount.setText(attachmentArrayList.get(position).getCount());
            }
            return v;

        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }

    class DownloadUploadAttachment extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            try {

               /* if (res.contains("error")||res.equalsIgnoreCase("[]")) {


                } else {*/
                    try {
                        JSONArray jResults = new JSONArray(res);
                        if (jResults.length() > 0) {
                            Attachment_count = String.valueOf(jResults.length());
                            if (SourceType.equalsIgnoreCase("DocAppr") || SourceType.equalsIgnoreCase("PreBid") || SourceType.equalsIgnoreCase("Indent") || SourceType.equalsIgnoreCase("PO") || SourceType.equalsIgnoreCase("SO") || SourceType.equalsIgnoreCase("Ship") || SourceType.equalsIgnoreCase("CLAIM") || SourceType.equalsIgnoreCase("Leave") || SourceType.equalsIgnoreCase("ExtraWork") || SourceType.equalsIgnoreCase("LeaveApply") || SourceType.equalsIgnoreCase("LeaveCancel") || SourceType.equalsIgnoreCase("RRF")) {
                                // options = new String[]{"Chat", "open","Attachments"};
                                /*attachmentArrayList.add(new Attachment("Chat", ""));
                                attachmentArrayList.add(new Attachment("Open", ""));*/
                                attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                            } else {
                                if (SourceType.equalsIgnoreCase("Datasheet")) {

                                    //attachmentArrayList.add(new Attachment("Fill datasheet", ""));
                                   // attachmentArrayList.add(new Attachment("Change status", ""));
                                    // options = new String[]{"Fill datasheet", "Change status"};
                                } else if (SourceType.equalsIgnoreCase("Support")) {
                                  //  attachmentArrayList.add(new Attachment("Chat", ""));
                                  //  attachmentArrayList.add(new Attachment("View ticket", ""));
                                   // attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                   // attachmentArrayList.add(new Attachment("Activity trail", ""));
                                 //   attachmentArrayList.add(new Attachment("Timesheet log", ""));

                                    // attachmentArrayList.add(new Attachment("View ticket", ""));

                                    // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                                }
                                else if (unapprove.equalsIgnoreCase("true")) {
                                   // attachmentArrayList.add(new Attachment("Approve", ""));
                                   // attachmentArrayList.add(new Attachment("Disapprove", ""));
                                   // attachmentArrayList.add(new Attachment("Activity trail", ""));

                                    // options = new String[]{"Chat", "View ticket", "Subordinate work", "Activity trail"};
                                }else {
                                    if (Mode.equalsIgnoreCase("")) {
                                        if (flag_fromTeam == 1) {
/*                                            attachmentArrayList.add(new Attachment("Chat", ""));
                                            attachmentArrayList.add(new Attachment("Change status", ""));
                                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                                            attachmentArrayList.add(new Attachment("Reassign", ""));
                                            attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                            attachmentArrayList.add(new Attachment("Change workspace", ""));
                                            attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                            attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                                            // options = new String[]{"Chat", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                                        } else {
                                           /* attachmentArrayList.add(new Attachment("Chat", ""));
                                            *//*if (Assign.equalsIgnoreCase("true")){
                                                attachmentArrayList.add(new Attachment("Timesheet log", ""));
                                            }else {
                                            }*//*
                                            attachmentArrayList.add(new Attachment("Timesheet", ""));
                                            attachmentArrayList.add(new Attachment("Timesheet log", ""));
                                            attachmentArrayList.add(new Attachment("Change status", ""));
                                            attachmentArrayList.add(new Attachment("Reschedule", ""));
                                            attachmentArrayList.add(new Attachment("Reassign", ""));
                                            attachmentArrayList.add(new Attachment("Subordinate work", ""));
                                            attachmentArrayList.add(new Attachment("Change workspace", ""));
                                            attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                            attachmentArrayList.add(new Attachment("Attachments", Attachment_count));

                                            // options = new String[]{"Chat", "Timesheet", "Change status", "Reschedule", "Reassign", "Subordinate work", "Change workspace", "Activity trail","Attachments"};
                                        }
                                    } else {

                                       /* attachmentArrayList.add(new Attachment("Chat", ""));
                                        attachmentArrayList.add(new Attachment("Approve", ""));
                                        attachmentArrayList.add(new Attachment("Disapprove", ""));
                                        attachmentArrayList.add(new Attachment("Activity trail", ""));*/
                                        attachmentArrayList.add(new Attachment("Attachments", Attachment_count));


                                        //  options = new String[]{"Chat", "Approve", "Disapprove", "Activity trail","Attachments"};
                                    }
                                }
                            }
                        }
                        MySpinnerAdapter customoption = new MySpinnerAdapter(ActivityDetailsActivity.this,
                                R.layout.vwb_custom_spinner_txt, attachmentArrayList);
                        ls_activity_option.setAdapter(customoption);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
                @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUploadedAttachment + "?activityId=" + URLEncoder.encode(SourceId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
                    if (res!=null) {
                        res = res.replaceAll("\\\\", "");
                        res = res.toString();

                        // res = res.substring(1, res.length() - 1);
                    }
                        return "";

        }
    }

    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);


    }

    private void CreateApprrovejson() {

        JSONObject jsonapprove = new JSONObject();
        String remark = "";
        try {

            jsonapprove.put("ActivityId", ActivityId);
            jsonapprove.put("StatusCode", "0");

             remark = "Complete activity " + ActivityName;


            FinalObj = jsonapprove.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = CompanyURL + WebUrlClass.api_PostApproveStatus;
        String op = "";
        CreateOfflineApproveChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
        //onBackPressed();



    }
    private void CreateDisapprrovejson() {

        JSONObject jsondisapprove = new JSONObject();
        String remark = "";
        try {


            jsondisapprove.put("ActivityId", ActivityId);
            remark = "Complete the activity " + ActivityName;
            FinalObj = jsondisapprove.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*if (isnet()) {
            new StartSession(ActivityDetailsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpdateChangeActivityStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {*/
        String url = CompanyURL + WebUrlClass.api_PostActivityDisapprove;
        String op = "";
        CreateOfflineApproveChageStatus(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);
      //  onBackPressed();

    }

    private void CreateOfflineApproveChageStatus(final String url, final String parameter,
                                              final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            sql1.close();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }
}
