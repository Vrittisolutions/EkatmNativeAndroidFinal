package com.vritti.vwblib.vworkbench;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
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
import com.vritti.vwblib.Adapter.AutocompleteReassignAdapter;
import com.vritti.vwblib.R;
import com.vritti.vwblib.classes.CommonFunction;
import com.vritti.vwblib.classes.CustomAutoCompleteTextChangedListener;

/**
 * Created by 300151 on 11/17/2016.
 */
public class ReassignActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    TextView tv_activity_name, tv_workspace_id, tv_main_group, tv_sub_group, tv_assigned_to;
    CustomAutoCompleteView reassign_to_autocomplete;
    EditText ed_reason, ed_expected_hrs;
    Button btn_fromDate, btn_toDate, btn_save, btn_cancel, btn_expectedcompletion;
    String ActivityId, ActivityName, Assigned_To, ProjectId;
    SharedPreferences userpreferences;
    private ProgressBar mProgress;
    public CustomAutoCompleteView myAutoComplete;
    SQLiteDatabase sql;
    static int year, month, day;
    public static ArrayAdapter<String> autocompleteAdapter;
    JSONObject jobj;
    String FinalObj;
    SimpleDateFormat sdf;
    Spinner sp_activity_type, sp_hr, sp_priority;
    String priority, natureofwork, selectedhr;
    int flag_fromTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_acitivity_reassign_activity);

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

        if (getIntent().hasExtra("ActivityId")) {
            Intent i = getIntent();
            ActivityName = i.getStringExtra("ActivityName");
            ActivityId = i.getStringExtra("ActivityId");
            Assigned_To = i.getStringExtra("Assigned_To");//ProjectId
            ProjectId = i.getStringExtra("ProjectId");
            flag_fromTeam = i.getIntExtra("Flag_fromteam", 0);
        }

        sql = db.getWritableDatabase();
        initView();
        getActivityInfo(ActivityId);
        SetListner();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle("  vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        String IssueTomastId;
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.reassign_to_autocomplete);
        tv_activity_name = (TextView) findViewById(R.id.tv_activity_name);
        tv_activity_name.setText(ActivityName);
        tv_workspace_id = (TextView) findViewById(R.id.tv_workspace_id);
        tv_main_group = (TextView) findViewById(R.id.tv_main_group);
        tv_sub_group = (TextView) findViewById(R.id.tv_sub_group);
        tv_assigned_to = (TextView) findViewById(R.id.tv_assiged_to);
        reassign_to_autocomplete = (CustomAutoCompleteView) findViewById(R.id.reassign_to_autocomplete);
        ed_reason = (EditText) findViewById(R.id.ed_reason);
        ed_expected_hrs = (EditText) findViewById(R.id.ed_expected_hrs);
        btn_fromDate = (Button) findViewById(R.id.btn_fromDate);
        btn_toDate = (Button) findViewById(R.id.btn_toDate);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_expectedcompletion = (Button) findViewById(R.id.btn_expected_completion);
        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(ReassignActivity.this));

        sp_activity_type = (Spinner) findViewById(R.id.sp_spinner_activity_type);
        sp_priority = (Spinner) findViewById(R.id.sp_spinner_priority);
        sp_hr = (Spinner) findViewById(R.id.sp_spinner_hr);

        if (CheckWorkNature()) {
            setSpinnerActivityType();
        } else {
            new StartSession(ReassignActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadNAtureOfWork().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);

                }
            });

        }


        setSpinnerhr();
        setSpinnerPriority();

        //   edQnty.setText("" + 0);
        // edAmt.setText("" + 0);
        String myObject = null;
        String[] ObjectItemData = new String[1];
        myObject = new String();
        ObjectItemData[0] = myObject;

        autocompleteAdapter = new AutocompleteReassignAdapter(ReassignActivity.this, R.layout.vwb_list_view_row_item, ObjectItemData);
        myAutoComplete.setAdapter(autocompleteAdapter);

      /*  int textLength = edQnty.getText().length();
        edQnty.setSelection(textLength, textLength);*/

        int textLength1 = myAutoComplete.getText().length();
        myAutoComplete.setSelection(textLength1, textLength1);
        myAutoComplete.setFocusable(true);


    }

    private boolean CheckWorkNature() {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK, null);
        if (c.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    private void SetListner() {
        btn_fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReassignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_fromDate.setText(date);


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
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sa = ed_expected_hrs.getText().toString();
                if (myAutoComplete.getText().toString().equalsIgnoreCase("")) {

                    Toast.makeText(ReassignActivity.this, "Please Enter user name", Toast.LENGTH_LONG).show();
                } else if (ed_reason.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ReassignActivity.this, "Please Enter reason", Toast.LENGTH_LONG).show();

                } else if (ed_expected_hrs.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(ReassignActivity.this, "Please Enter hours", Toast.LENGTH_LONG).show();
                } else {
                    ReassignJSONObj();
                }
            }
        });
        btn_toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReassignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //       datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_toDate.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();


            }
        });
        btn_expectedcompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ReassignActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_expectedcompletion.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();


            }
        });

        sp_activity_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                natureofwork = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_hr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedhr = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setSpinnerActivityType() {
        List<String> categories = getAllActivityType();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sp_activity_type.setAdapter(dataAdapter);
    }

    private List<String> getAllActivityType() {
        List<String> data = new ArrayList<String>();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * From " + db.TABLE_NATURE_Of_WORK, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("ActivityTypeName")));
            } while (c.moveToNext());
        }

        return data;
    }

    private void setSpinnerhr() {
        List<String> categories = new ArrayList<String>();
        categories.add("Hours");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_hr.setAdapter(dataAdapter);
    }

    private void setSpinnerPriority() {
        List<String> categories = new ArrayList<String>();
        categories.add("Normal");
        categories.add("Important");
        categories.add("Critical");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        sp_priority.setAdapter(dataAdapter);
    }

    private void getProjectId() {

    }

    private void ReassignJSONObj() {
        String IssueTomastId = "";

        String que = "SELECT UnitId,UnitDesc,PriorityIndex,ActivityTypeId,ActivityCode,IsCompActPresent,CompletionActId,ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityId='" + ActivityId + "'";
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            jobj = new JSONObject();
            try {
                jobj.put("ActivityName", ActivityName);
                jobj.put("UnitId", cur.getString(cur.getColumnIndex("UnitId")));
                String typeID = getActivitypeID(natureofwork);
                jobj.put("ActivityTypeId", typeID);
                jobj.put("PriorityId", priority);
                jobj.put("StartDate", btn_fromDate.getText().toString());
                jobj.put("EndDate", btn_toDate.getText().toString());
                jobj.put("ActualStartDate", btn_fromDate.getText().toString());
                jobj.put("ActualEndDate", btn_toDate.getText().toString());
                jobj.put("DueDate", btn_toDate.getText().toString());
                jobj.put("ExpectedComplete_Date", btn_expectedcompletion.getText().toString());
                jobj.put("HoursRequired", ed_expected_hrs.getText().toString());
                String d = myAutoComplete.getText().toString();
                //  que = "SELECT * FROM " + db.TABLE_ALL_MEMBERS + " WHERE UserName LIKE '%" + myAutoComplete.getText().toString().substring(0, myAutoComplete.getText().toString().indexOf("(")) + "%'";
                que = "SELECT * FROM " + db.TABLE_ALL_MEMBERS + " WHERE UserName = '" + myAutoComplete.getText().toString() + "'";
                Cursor cur1 = sql.rawQuery(que, null);
                int a = cur1.getCount();
                if (cur1.getCount() > 0) {
                    cur1.moveToFirst();
                    IssueTomastId = cur1.getString(cur1.getColumnIndex("UserMasterId"));
                }
                jobj.put("IssuedTo", IssueTomastId);
                jobj.put("ActivityCode", cur.getString(cur.getColumnIndex("ActivityCode")));
                jobj.put("Reason", ed_reason.getText().toString());
                jobj.put("ddlDaysHours", " ");
                jobj.put("ActivityId", cur.getString(cur.getColumnIndex("ActivityId")));
                jobj.put("NewAssignee", "");
                jobj.put("hdnfldTransferFlag", "");
                jobj.put("IsCompActPresent", cur.getString(cur.getColumnIndex("IsCompActPresent")));
                jobj.put("CompletionActId", cur.getString(cur.getColumnIndex("CompletionActId")));
                jobj.put("hdnfldUnitIdLocal", " ");
                jobj.put("ActRassExpctdEndDt", btn_toDate.getText().toString());
                jobj.put("expdt", btn_toDate.getText().toString());
                FinalObj = jobj.toString();
                FinalObj = FinalObj.replaceAll("\\\\", "");

                new StartSession(ReassignActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new UploadReassignDataURLJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String getActivitypeID(String data) {
        SQLiteDatabase sql = db.getWritableDatabase();
        String ID = "1";
        Cursor c = sql.rawQuery("Select * From '" + db.TABLE_NATURE_Of_WORK + "' Where ActivityTypeName ='" + data + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                ID = c.getString(c.getColumnIndex("ActivityTypeId"));
            } while (c.moveToNext());
        }
        return ID;
    }

    private void showProgressDialog() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);
    }

    class UploadReassignDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
        }


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_reassignActivity;
            try {
                res = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);
            String Activityid = "";
            if (resp.contains("success")) {
                try {
                    Activityid = jobj.getString("ActivityId");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (flag_fromTeam == 0) {
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{Activityid});
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{Activityid});
                    Toast.makeText(ReassignActivity.this, "Activity Reassign successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ReassignActivity.this, ActivityMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    sql.delete(db.TABLE_ACTIVITYMASTER_TEAM, "ActivityId=?",
                            new String[]{Activityid});
                    Toast.makeText(ReassignActivity.this, "Activity Reassign successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ReassignActivity.this, ActivityMain.class);
                    startActivity(intent);
                    finish();

                }
            } else {
                Toast.makeText(ReassignActivity.this, "Activity Reassign Unsuccessful", Toast.LENGTH_LONG).show();
            }
        }

    }


    private class DownloadNAtureOfWork extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }


        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_NatureOfWork + "?projectId=" + URLEncoder.encode(ProjectId, "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            String Activityid = "";
            dismissProgressDialog();
            if (res.contains("ActivityTypeName")) {
                try {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_NATURE_Of_WORK, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NATURE_Of_WORK, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_NATURE_Of_WORK, null, values);
                        Log.e("", "" + a);
                    }
                } catch (Exception e) {

                }
                setSpinnerActivityType();
            } else {
                ut.displayToast(ReassignActivity.this, "Server Error...");
            }
        }

    }

    public void getActivityInfo(String ActivityId) {
        SimpleDateFormat sff = new SimpleDateFormat("dd MMM yyyy");
        Date FDate, TDate;
        String fd = "", td = "";
        String que = "SELECT ProjectId,ProjectName,UnitId,UnitDesc,PKModuleMastId,ModuleName,StartDate,EndDate FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityId='" + ActivityId + "'";
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                tv_workspace_id.setText(cur.getString(cur.getColumnIndex("ProjectName")));
                tv_main_group.setText(cur.getString(cur.getColumnIndex("ModuleName")));
                tv_sub_group.setText(cur.getString(cur.getColumnIndex("UnitDesc")));
                tv_assigned_to.setText(Assigned_To);

                try {
                    FDate = sff.parse(cur.getString(cur.getColumnIndex("StartDate")));
                   // fd = sdf.format(FDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                btn_fromDate.setText(sdf.format(new Date()));

                try {
                    TDate = sff.parse(cur.getString(cur.getColumnIndex("EndDate")));
                    //td = sdf.format(TDate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                btn_toDate.setText(sdf.format(new Date()));
                btn_expectedcompletion.setText(sdf.format(new Date()));
            } while (cur.moveToNext());
        }
    }
}
