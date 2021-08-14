package com.vritti.vwblib.vworkbench;

import android.app.DatePickerDialog;
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

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
import com.vritti.vwblib.Adapter.AutoCompleteAssignAdapter;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.SendOfflineData;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.CustomAssignAutoCompleteTextChangedListener;


/**
 * Created by 300151 on 10/19/2016.
 */
public class AssignActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    SharedPreferences userpreferences;
    SharedPreferences workspaceferences;

    Toolbar toolbar;
    String  ScratchWorkspaceId, ScratchWorkspaceName;
    RadioGroup radio_groupPriority;
    RadioButton RaioNormal, RadioCritical, Radioimportant;
    EditText ed_activity_desc, ed_hours, ed_bill_amount;
    public CustomAutoCompleteView sp_issueto;
    Spinner sp_responsible;
    Spinner sp_workspace, sp_group, sp_subgroup, sp_nature_work,spinner_all_group;
    CheckBox chk_mail,
            chk_completion,
            chk_ChkApproval;
    Button btn_fromdate, btn_endon, btn_save, btn_cancel;
    TextView tv_Subactivity;
       EditText     tv_Subactivity_desc;
    ProgressBar mprogress;
    String[] ls_Responsible = {"Any One","Workspace Participants","Group"};
    static ArrayList<String> Workspace_list;
    static ArrayList<String> ActivityGroup_list;
    ArrayList<String> Subgroup_List, MainGroup_List;
    ArrayList<String> ChkUser_list, UnChkUser_list;
    public static String ProjectId, moduleId, prjMstId, UnitId = " ", IssueTomastId,GroupId;
    static int year, month, day;
    JSONObject ActivityJsonObj;
    String PriorityId = "1";
    String ProjectName = "Scratch";
    String SubActivityId, SunActivityName,ActivityGroup;
    public Boolean IsSubAct = false;
    public AutoCompleteAssignAdapter autocompleteAdapter;
    public static Boolean IsProjectMember = false;
    String NatureOfWork, TypeID;
    LinearLayout mbillableamt;
    private String UserGroupName;
    SQLiteDatabase sql;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_assign_activitiy);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        workspaceferences = getSharedPreferences("WORKSPACE",
                Context.MODE_PRIVATE);
        ScratchWorkspaceName = userpreferences.getString("ScratchWorkspaceName", null);
        ScratchWorkspaceId = userpreferences.getString("ScratchWorkspaceId", null);
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

        InitView();
        setListner();
        if (getIntent().hasExtra("IsSubordinate")) {
            IsSubAct = true;
            Intent i = getIntent();
            SubActivityId = i.getStringExtra("ActivityId");
            SunActivityName = i.getStringExtra("ActivityName");
            ed_activity_desc.setText(SunActivityName);
            tv_Subactivity.setVisibility(View.VISIBLE);
            tv_Subactivity_desc.setVisibility(View.VISIBLE);
            tv_Subactivity_desc.setText(SunActivityName);
            tv_Subactivity_desc.setSelection(tv_Subactivity_desc.length());

            ed_activity_desc.setFocusable(false);
            ed_activity_desc.setClickable(false);
        }
        if (cf.getWorkspaceList() > 0) {
            UpadateWorkspaceList();
        } else {
            refresh();
        }

        if (cf.getActivityGroupList()>0){
            UpdateActivityGroupList();
        }else {
            GroupList();
        }

        if (cf.getAllMember()>0){

        }else {

            AllMemberdatalist();

        }
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        Workspace_list = new ArrayList<String>();
        ActivityGroup_list=new ArrayList<>();
        Subgroup_List = new ArrayList<String>();
        MainGroup_List = new ArrayList<String>();
        ChkUser_list = new ArrayList<String>();
        UnChkUser_list = new ArrayList<String>();
        radio_groupPriority = (RadioGroup) findViewById(R.id.radio_groupPriority);
        RaioNormal = (RadioButton) findViewById(R.id.radionorml);
        Radioimportant = (RadioButton) findViewById(R.id.radioimportant);
        RadioCritical = (RadioButton) findViewById(R.id.radiocritical);
        ed_activity_desc = (EditText) findViewById(R.id.ed_activity_desc);
        sp_responsible = (Spinner) findViewById(R.id.sp_responsible);
        spinner_all_group=findViewById(R.id.spinner_all_group);
        sp_workspace = (Spinner) findViewById(R.id.sp_workspace);
        sp_group = (Spinner) findViewById(R.id.sp_group);
        sp_subgroup = (Spinner) findViewById(R.id.sp_subgroup);
        sp_nature_work = (Spinner) findViewById(R.id.spinner_natureofwork);
        sp_issueto = (CustomAutoCompleteView) findViewById(R.id.sp_issueto);
        btn_fromdate = (Button) findViewById(R.id.btn_start_date);
        btn_endon = (Button) findViewById(R.id.btn_end_date);
        ed_hours = (EditText) findViewById(R.id.ed_hours);
        ed_bill_amount = (EditText) findViewById(R.id.ed_billable_client);
        mbillableamt = (LinearLayout) findViewById(R.id.ln_billable_client);
        tv_Subactivity = (TextView) findViewById(R.id.tv_Subactivity);
        tv_Subactivity_desc = (EditText) findViewById(R.id.ed_sub_activity_desc);
        chk_mail = (CheckBox) findViewById(R.id.mail);
        chk_mail.setChecked(true);
        chk_completion = (CheckBox) findViewById(R.id.chk_openend);
        chk_ChkApproval = (CheckBox) findViewById(R.id.chk_plan);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);//ed_billable_client

        // SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
        btn_fromdate.setText(dff.format(new Date()));
        btn_endon.setText(dff.format(new Date()));
        ed_hours.setText("4");
        ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, ls_Responsible);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_responsible.setAdapter(adapter);

        sp_issueto.addTextChangedListener(new CustomAssignAutoCompleteTextChangedListener(AssignActivity.this));
        String myObject = null;
        String[] ObjectItemData = new String[1];
        myObject = new String();
        ObjectItemData[0] = myObject;

        autocompleteAdapter = new AutoCompleteAssignAdapter(AssignActivity.this, R.layout.vwb_list_view_row_item, ObjectItemData);
        sp_issueto.setAdapter(autocompleteAdapter);

        int textLength1 = sp_issueto.getText().length();
        sp_issueto.setSelection(textLength1, textLength1);
        sp_issueto.setFocusable(true);


    }

    private void setListner() {
        radio_groupPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = group.findViewById(checkedId);
                int index = group.indexOfChild(radioButton);
                int a = index;
                if (a == 1) {
                    PriorityId = "1";
                } else if (a == 2) {
                    PriorityId = "2";
                } else if (a == 0) {
                    PriorityId = "3";
                }
            }
        });
        btn_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AssignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                                btn_fromdate.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_endon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(AssignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //   datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                                btn_endon.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        sp_responsible.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ActivityGroup= sp_responsible.getSelectedItem().toString();
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Group")) {
                    sp_issueto.setVisibility(View.GONE);
                    spinner_all_group.setVisibility(View.VISIBLE);

                    //IsProjectMember = true;
                    //  UpadateWorkspaceList();
                }
                if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Workspace Participants")) {
                    sp_issueto.setVisibility(View.VISIBLE);
                    spinner_all_group.setVisibility(View.GONE);
                    // UpadateWorkspaceList();
                    IsProjectMember = true;

                } else if (parent.getItemAtPosition(position).toString().equalsIgnoreCase("Any One")) {
                    sp_issueto.setVisibility(View.VISIBLE);
                    spinner_all_group.setVisibility(View.GONE);
                    //   UpadateWorkspaceList();
                    IsProjectMember = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_workspace.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ProjectName = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = workspaceferences.edit();
                editor.putString("worksp", ProjectName);
                editor.commit();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST + " WHERE ProjectName='" + ProjectName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ProjectId = cur.getString(cur.getColumnIndex("ProjectId"));
                    prjMstId = ProjectId;
                    UpadateMainGroupList();
                    if (!isProjectmemberpresent(prjMstId)) {
                        new StartSession(getApplicationContext(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadGetChkUserlistDataJSON().execute(prjMstId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }
                    if (!isnatureworkpresent(prjMstId)) {
                        new StartSession(getApplicationContext(), new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadNAtureOfWork().execute(prjMstId);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    } else {
                        setSpinnerNatureOfWork(prjMstId);
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "No Workspace Assign or Refresh Data", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ModuleName = parent.getItemAtPosition(position).toString();
                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ModuleName='" + ModuleName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    moduleId = cur.getString(cur.getColumnIndex("PKModuleMastId"));
                    UpadateSubGroupList();
                }


                //new DownloadSubGroupDataJSON().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_subgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String unitdesc = parent.getItemAtPosition(position).toString();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE UnitDesc='" + unitdesc + "'";
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

        spinner_all_group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserGroupName = spinner_all_group.getSelectedItem().toString();

                SQLiteDatabase sql = db.getWritableDatabase();
                String query = "SELECT * FROM " + db.TABLE_ActivityGetGroupList + " WHERE UserGroupName='" + UserGroupName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    GroupId = cur.getString(cur.getColumnIndex("PKUserGroupId"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mbillableamt.getVisibility() == View.VISIBLE) {
                    String d = ed_bill_amount.getText().toString();
                    if (!(d.equalsIgnoreCase(""))) {
                        CreateActivityJson();
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Billable Amount", Toast.LENGTH_LONG).show();
                    }
                } else {
                    CreateActivityJson();
                }

            }
        });
        sp_nature_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                NatureOfWork = parent.getItemAtPosition(position).toString();
                TypeID = GetActivityTypeId(NatureOfWork);
                String IntemateCompletion = isIntementCompletion(TypeID);
                String Chargavle = isChargeble(TypeID);
                String ApprovalRequired = isApprovalRequired(TypeID);
                if (Chargavle.equalsIgnoreCase("true")
                        && ApprovalRequired.equalsIgnoreCase("true")) {
                    chk_mail.setChecked(true);
                    chk_ChkApproval.setChecked(true);
                    mbillableamt.setVisibility(View.VISIBLE);
                } else {
                    mbillableamt.setVisibility(View.GONE);
                    chk_ChkApproval.setChecked(false);
                }
                if (IntemateCompletion.equalsIgnoreCase("true")) {
                    chk_completion.setChecked(true);
                } else {
                    chk_completion.setChecked(false);
                }
               /* if (ApprovalRequired.equalsIgnoreCase("true")) {
                    chk_ChkApproval.setChecked(true);
                } else {
                    chk_ChkApproval.setChecked(false);
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* sp_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String Priority = parent.getItemAtPosition(position).toString();
                if (Priority.equalsIgnoreCase("Important")) {
                    PriorityId = "2";
                } else if (Priority.equalsIgnoreCase("Critical")) {
                    PriorityId = "1";
                } else if (Priority.equalsIgnoreCase("Normal")) {
                    PriorityId = "3";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private String GetActivityTypeId(String natureOfWork) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeName='" + natureOfWork + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("ActivityTypeId"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isIntementCompletion(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IntimateCompletion"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isApprovalRequired(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IsApproval"));
            } while (c.moveToNext());
        }

        return data;
    }

    private String isChargeble(String TypeID) {
        String data = "false";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK + " Where ActivityTypeId='" + TypeID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data = c.getString(c.getColumnIndex("IsChargable"));
            } while (c.moveToNext());
        }

        return data;
    }


    private String getAllMembers(String name) {
        UnChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ALL_MEMBERS + " WHERE UserName='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        String returndata = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                returndata = cur.getString(cur.getColumnIndex("UserMasterId"));
            } while (cur.moveToNext());
        }

        return returndata;
    }

    private String getProjectMembers(String ProjectId, String name) {
        ChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();

        String query = "SELECT * FROM " + db.TABLE_PROJECT_MEMBERS + " WHERE prjMstId='" + ProjectId + "' AND UserName='" + name + "'";
        Cursor cur = sql.rawQuery(query, null);
        String data = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("UserMasterId"));
            } while (cur.moveToNext());
        }

        return data;

    }

    private String getStatusforBilling(String Id) {
        ChkUser_list.clear();
        SQLiteDatabase sql = db.getWritableDatabase();

        String query = "SELECT * FROM " + db.TABLE_ISBILLABLE_AMOUNT + " WHERE NAtureofworkID='" + Id + "'";
        Cursor cur = sql.rawQuery(query, null);
        String data = "";
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("IsChargable"));
            } while (cur.moveToNext());
        }

        return data;

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void CreateActivityJson() {
        // showProgress();

        String issuedtoname = sp_issueto.getText().toString();

        if (IsProjectMember) {
            IssueTomastId = getProjectMembers(prjMstId, issuedtoname);
        } else {
            IssueTomastId = getAllMembers(issuedtoname);
        }

        if (ActivityGroup.equalsIgnoreCase("Group")){
            ActivityJsonObj = new JSONObject();
            try {

                ActivityJsonObj.put("UnitId", UnitId);
                ActivityJsonObj.put("ActivityTypeId", TypeID);
                ActivityJsonObj.put("CompletionActId", " ");
                ActivityJsonObj.put("IsCompActPresent", " ");
                ActivityJsonObj.put("PriorityId", PriorityId);
                String StartDate = getyyyymmdd(btn_fromdate.getText().toString());
                String EndADte = getyyyymmdd(btn_endon.getText().toString());
                ActivityJsonObj.put("StartDate", StartDate);
                ActivityJsonObj.put("EndDate", EndADte);
                ActivityJsonObj.put("ActualStartDate", StartDate);
                ActivityJsonObj.put("ActualEndDate", EndADte);
                ActivityJsonObj.put("DueDate", EndADte);
                ActivityJsonObj.put("ExpectedComplete_Date", EndADte);
                ActivityJsonObj.put("HoursRequired", ed_hours.getText().toString());
                JSONArray jsonArray=new JSONArray();
                jsonArray.put(GroupId);
                ActivityJsonObj.put("objarr",jsonArray);
               // ActivityJsonObj.put("IssuedTo", GroupId);
                if (ActivityGroup.equalsIgnoreCase("Group")){
                    ActivityJsonObj.put("UsrGrp", "Y");
                }else {
                    ActivityJsonObj.put("UsrGrp", "N");
                }

                if (IsSubAct) {
                    ActivityJsonObj.put("IsSubActivity", "Y");
                    ActivityJsonObj.put("ParentActId", SubActivityId);
                    ActivityJsonObj.put("ActivityName", tv_Subactivity_desc.getText().toString());
                } else {
                    ActivityJsonObj.put("IsSubActivity", "N");
                    ActivityJsonObj.put("ParentActId", " ");
                    ActivityJsonObj.put("ActivityName", ed_activity_desc.getText().toString());
                }

                ActivityJsonObj.put("SourceId", " ");
                ActivityJsonObj.put("SourceType", " ");
                ActivityJsonObj.put("MOMId", " ");
                ActivityJsonObj.put("IsUnplanned", "Y");
                String s;
                if (chk_completion.isChecked()) {
                    s = "Y";
                } else {
                    s = "N";
                }
                ActivityJsonObj.put("CompletionIntimate", s);
                String app;
                if (chk_ChkApproval.isChecked()) {
                    app = "1";
                } else {
                    app = "0";
                }
                ActivityJsonObj.put("IsApproval", app);//
                int ac = 0;
                if (mbillableamt.getVisibility() == View.VISIBLE) {
                    String d = ed_bill_amount.getText().toString();
                    ac = Integer.parseInt(d);
                }
                ActivityJsonObj.put("BillableAmt", ac);
                String a;
                if (chk_mail.isChecked()) {
                    a = "Y";
                } else {
                    a = "N";

                }
                ActivityJsonObj.put("chkIntimteByEmail", a);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String FinalObj = ActivityJsonObj.toString();
            String remark = "Assign activity  " + ed_activity_desc.getText().toString() + " to " +UserGroupName ;
            String url = CompanyURL + WebUrlClass.api_PostInsertAct;

            String op = "Success";
            CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

             /* new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new AssignActivityDataJson().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });*/

        }else {
            if (!(IssueTomastId.equalsIgnoreCase(""))) {
                ActivityJsonObj = new JSONObject();
                try {

                    ActivityJsonObj.put("UnitId", UnitId);
                    ActivityJsonObj.put("ActivityTypeId", TypeID);
                    ActivityJsonObj.put("CompletionActId", " ");
                    ActivityJsonObj.put("IsCompActPresent", " ");
                    ActivityJsonObj.put("PriorityId", PriorityId);
                    String StartDate = getyyyymmdd(btn_fromdate.getText().toString());
                    String EndADte = getyyyymmdd(btn_endon.getText().toString());
                    ActivityJsonObj.put("StartDate", StartDate);
                    ActivityJsonObj.put("EndDate", EndADte);
                    ActivityJsonObj.put("ActualStartDate", StartDate);
                    ActivityJsonObj.put("ActualEndDate", EndADte);
                    ActivityJsonObj.put("DueDate", EndADte);
                    ActivityJsonObj.put("ExpectedComplete_Date", EndADte);
                    ActivityJsonObj.put("HoursRequired", ed_hours.getText().toString());

                    JSONArray jsonArray=new JSONArray();
                    jsonArray.put(IssueTomastId);
                    ActivityJsonObj.put("objarr",jsonArray);
                    //ActivityJsonObj.put("IssuedTo", IssueTomastId);
                    if (ActivityGroup.equalsIgnoreCase("Group")) {
                        ActivityJsonObj.put("UsrGrp", "Y");
                    } else {
                        ActivityJsonObj.put("UsrGrp", "N");
                    }

                    if (IsSubAct) {
                        ActivityJsonObj.put("IsSubActivity", "Y");
                        ActivityJsonObj.put("ParentActId", SubActivityId);
                        ActivityJsonObj.put("ActivityName", tv_Subactivity_desc.getText().toString());
                    } else {
                        ActivityJsonObj.put("IsSubActivity", "N");
                        ActivityJsonObj.put("ParentActId", " ");
                        ActivityJsonObj.put("ActivityName", ed_activity_desc.getText().toString());
                    }

                    ActivityJsonObj.put("SourceId", " ");
                    ActivityJsonObj.put("SourceType", " ");
                    ActivityJsonObj.put("MOMId", " ");
                    ActivityJsonObj.put("IsUnplanned", "Y");
                    String s;
                    if (chk_completion.isChecked()) {
                        s = "Y";
                    } else {
                        s = "N";
                    }
                    ActivityJsonObj.put("CompletionIntimate", s);
                    String app;
                    if (chk_ChkApproval.isChecked()) {
                        app = "1";
                    } else {
                        app = "0";
                    }
                    ActivityJsonObj.put("IsApproval", app);//
                    int ac = 0;
                    if (mbillableamt.getVisibility() == View.VISIBLE) {
                        String d = ed_bill_amount.getText().toString();
                        ac = Integer.parseInt(d);
                    }
                    ActivityJsonObj.put("BillableAmt", ac);
                    String a;
                    if (chk_mail.isChecked()) {
                        a = "Y";
                    } else {
                        a = "N";

                    }
                    ActivityJsonObj.put("chkIntimteByEmail", a);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String FinalObj = ActivityJsonObj.toString();
                String remark = "Assign activity  " + ed_activity_desc.getText().toString() + " to " + issuedtoname;
                String url = CompanyURL + WebUrlClass.api_PostInsertAct;

                String op = "Success";
                CreateOfflineAssignActivity(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

            /*new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new AssignActivityDataJson().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });*/


            } else {
                Toast.makeText(getApplicationContext(), "Enter Valid User", Toast.LENGTH_LONG).show();
            }
        }

    }

    class AssignActivityDataJson extends AsyncTask<Integer, Void, String> {
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String val) {
            super.onPostExecute(val);
            dismissProgress();
            if (val.contains("Success")) {//Success
                Toast.makeText(AssignActivity.this, "Activity assign Successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AssignActivity.this, ActivityMain.class);
                startActivity(intent);
            } else if (val.equalsIgnoreCase(WebUrlClass.Errormsg)) {
                Toast.makeText(AssignActivity.this, "Can not Assign Activity ", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AssignActivity.this, "Server Error...", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            Object res;
            try {
                String objFinalObj = ActivityJsonObj.toString().replaceAll("\\\\", "");
                String url = CompanyURL + WebUrlClass.api_PostInsertAct;
                res = ut.OpenPostConnection(url, objFinalObj,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                //   response = response.replaceAll("\"", "");
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }

    private void UpadateMainGroupList() {
        MainGroup_List.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_MAINGROUP_LIST + " WHERE ProjectId='" + prjMstId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MainGroup_List.add(cur.getString(cur.getColumnIndex("ModuleName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, MainGroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_group.setAdapter(adapter);

        } else {
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadMainGroupDataJSON().execute(prjMstId);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });

        }

    }

    private void UpadateSubGroupList() {
        Subgroup_List.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_SUBGROUP_LIST + " WHERE PKModuleMastId='" + moduleId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Subgroup_List.add(cur.getString(cur.getColumnIndex("UnitDesc")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, Subgroup_List);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_subgroup.setAdapter(adapter);

        } else {
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadSubGroupDataJSON().execute(moduleId);
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });

        }

    }

    private void UpadateWorkspaceList() {
        Workspace_list.clear();
        String query = "SELECT * FROM " + db.TABLE_WORKSPACE_LIST;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("ProjectName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_workspace.setAdapter(adapter);
            String data = workspaceferences.getString("worksp", "");
            if (!(data.equalsIgnoreCase(""))) {
                int a = Workspace_list.indexOf(data);
                sp_workspace.setSelection(Workspace_list.indexOf(data));
            }
        }


    }
    private void UpdateActivityGroupList() {
        String query = "SELECT * FROM " + db.TABLE_ActivityGetGroupList;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ActivityGroup_list.add(cur.getString(cur.getColumnIndex("UserGroupName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(AssignActivity.this, android.R.layout.simple_spinner_item, ActivityGroup_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_all_group.setAdapter(adapter);

        }


    }

    class DownloadWorkspaceDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Workspace_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_WORKSPACE_LIST, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_WORKSPACE_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    ProjectId = jorder.getString("ProjectId");
                    if (CheckifRecordPresent(db.TABLE_WORKSPACE_LIST, "ProjectId", ProjectId)) {//ProjectId
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            //   ProjectId = jorder.getString("ProjectId");
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_WORKSPACE_LIST, null, values);
                        Log.e("Added Record Count", "" + a);
                    }

                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                UpadateWorkspaceList();
            }
        }

    }
    class DownloadGroupListDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_ActivityGroup_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ActivityGetGroupList, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            //   ProjectId = jorder.getString("ProjectId");
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_ActivityGetGroupList, null, values);
                        Log.e("Added Record Count", "" + a);


                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                UpdateActivityGroupList();
            }
        }

    }

    class Downloadisbillable extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_ISbillable_amt + "?ActivityTypeId='" + params[0] + "'";
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ISBILLABLE_AMOUNT, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        //   ProjectId = jorder.getString("ProjectId");
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("NAtureofworkID")) {
                            columnValue = params[0];
                            values.put(columnName, columnValue);
                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                    }
                    long a = sql.insert(db.TABLE_ISBILLABLE_AMOUNT, null, values);
                    Log.e("Added Record Count", "" + a);


                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                String data = getStatusforBilling(TypeID);
                if (data.equalsIgnoreCase("true")) {
                    mbillableamt.setVisibility(View.VISIBLE);
                } else {
                    mbillableamt.setVisibility(View.GONE);
                }
            }
        }

    }

    class DownloadMainGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_MainGroup_list + "?projectId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MAINGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    String ModuleId = jorder.getString("PKModuleMastId");
                    if (CheckifRecordPresent(db.TABLE_MAINGROUP_LIST, "PKModuleMastId", ModuleId)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            //  moduleId = jorder.getString("PKModuleMastId");
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("ProjectId")) {
                                columnValue = params[0];
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_MAINGROUP_LIST, null, values);
                        Log.e("Added Value", "" + a);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {

                UpadateMainGroupList();
            }
        }

    }

    class DownloadSubGroupDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_SubGroup_list + "?moduleId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SUBGROUP_LIST, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    String UnID = jorder.getString("UnitId");
                    if (CheckifRecordPresent(db.TABLE_SUBGROUP_LIST, "UnitId", UnID)) {
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("PKModuleMastId")) {
                                columnValue = params[0];
                            } else {
                                columnValue = jorder.getString(columnName);
                            }
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_SUBGROUP_LIST, null, values);
                        Log.e("DAtaInsert :", " " + a);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {
                UpadateSubGroupList();
            }
        }

    }

    class DownloadGetChkUserlistDataJSON extends AsyncTask<String, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetChkUser_list + "?prjMstId=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
               /* sql.delete(db.TABLE_PROJECT_MEMBERS, null,
                        null);*/
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROJECT_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("prjMstId")) {
                            columnValue = params[0];
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_PROJECT_MEMBERS, null, values);
                    String data = a + "";


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response != null) {

            }
        }

    }

    private class DownloadNAtureOfWork extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_NatureOfWork + "?projectId=" + params[0];
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(params[0]);
            data.add(response);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> res) {
            super.onPostExecute(res);
            dismissProgress();
            String prjmasterID = res.get(0);
            String response = res.get(1);
            String Activityid = "";
            if (response.contains("ActivityTypeName")) {
                try {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    /*sql.delete(db.TABLE_NATURE_Of_WORK, null,
                            null);*/
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NATURE_Of_WORK, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {//projectId
                            columnName = c.getColumnName(j);
                            if (columnName.equalsIgnoreCase("ProjectId")) {
                                columnValue = prjmasterID;
                                values.put(columnName, columnValue);
                            } else {
                                columnValue = jorder.getString(columnName);
                                if (columnValue.equalsIgnoreCase("null")) {
                                    columnValue = "false";
                                }
                                values.put(columnName, columnValue);
                            }
                        }
                        long a = sql.insert(db.TABLE_NATURE_Of_WORK, null, values);
                        Log.e("", "" + a);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setSpinnerNatureOfWork(prjmasterID);
            } else {
                ut.displayToast(AssignActivity.this, "Could not connect to server");
            }
        }

    }

    private List<String> getAllActivityType(String projectmstrId) {
        List<String> data = new ArrayList<String>();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From '" + db.TABLE_NATURE_Of_WORK + "' where ProjectId='" + projectmstrId + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("ActivityTypeName")));
            } while (c.moveToNext());
        }

        return data;
    }

    private void setSpinnerNatureOfWork(String projectmsterId) {
        List<String> categories = getAllActivityType(projectmsterId);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_nature_work.setAdapter(dataAdapter);
    }

    private String getyyyymmdd(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        String ghe = "";
        try {
            date = dateFormat1.parse(data);
            ghe = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return ghe;
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    private void refresh() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadWorkspaceDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void GroupList() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGroupListDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void AllMemberdatalist() {
        if (isnet()) {
            showProgress();
            new StartSession(AssignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUserlistData ().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }



    private boolean isProjectmemberpresent(String projectmasterID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_PROJECT_MEMBERS + " Where prjMstId='" + projectmasterID + "'", null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPresentToLocal(String natureworkID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_ISBILLABLE_AMOUNT + " Where NAtureofworkID='" + natureworkID + "'", null);
        if (c.getCount() > 0) {
            c.close();
            return true;
        } else {
            c.close();
            return false;
        }
    }

    private boolean isnatureworkpresent(String projectId) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From '" + db.TABLE_NATURE_Of_WORK + "' where ProjectId='" + projectId + "'", null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean CheckifRecordPresent(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table , null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
            if(id == R.id.refresh1) {

                refresh();
                GroupList();
                if (isnet()) {
                    new StartSession(AssignActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadUserlistData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                return true;
            }else {
                return false;
        }

    }

    private void CreateOfflineAssignActivity(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();

        }

    }


    class DownloadUserlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetUnChkUser_list;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_ALL_MEMBERS, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ALL_MEMBERS, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_ALL_MEMBERS, null, values);
                    String data = a + "";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
   dismissProgress();
        }
    }


   /* public int getNatureofWorkCountList(String projectId) {

       SQLiteDatabase sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_NATURE_Of_WORK+" WHERE ";
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public int getWorkspaceList() {
        db = new DatabaseHandler(parent);
        sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_WORKSPACE_LIST;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }*/
}

