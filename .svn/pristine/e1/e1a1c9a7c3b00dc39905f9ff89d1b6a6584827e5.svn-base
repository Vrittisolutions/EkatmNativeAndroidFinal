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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.SendOfflineData;
import com.vritti.vwblib.classes.CommonFunction;

/**
 * Created by 300151 on 11/28/2016.
 */
public class ApplyLeaveMainActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    SharedPreferences userpreferences, LeaveMobPrefrence;
    SQLiteDatabase sql;
    Spinner sp_leave_reportingto;
    Button btn_from_date, btn_to_date, btn_apply_leave_save, btn_apply_leave_cancel, btn_reset;
    CheckBox chk_first_half, chk_second_half, chek_to_first_half;
    EditText ed_leave_description;
    ArrayList<String> lsLeaveCode, lsLeaveReportingTo;
    JSONObject LeaveObj;
    String LeaveType;
    int year, month, day;
    TextView leavetype, EmpName;
    ProgressBar mProgress;
    LinearLayout mLintodate;
    String FromDate, ToDate;
    TextView Mobile;
    String response;
    SimpleDateFormat sdf;
    Boolean flag;
    String MLID;
    public static final String USERMOB = "EmpMobile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_apply_leave);
        LeaveMobPrefrence = getSharedPreferences(ApplyLeaveMainActivity.USERMOB,
                Context.MODE_PRIVATE);
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

        sdf = new SimpleDateFormat("dd/MM/yyyy");


        InitView();
        Setlistner();
        Intent intent = getIntent();
        if (intent.hasExtra("LeaveCode")) {
            flag = false;
            LeaveType = intent.getStringExtra("LeaveCode");
            leavetype.setText(LeaveType);

        } else if (intent.hasExtra("response")) {//mLId
            flag = true;
            MLID = intent.getStringExtra("mLId");
            response = intent.getStringExtra("response");
            Log.e("jkdlajdfklafjklfjk", response);
            JSONArray jResults = null;
            Date sDate, eDate = null;
            String StartDt, EndDt, Reason, ApprovedBy, LeaveType, Contact, HalfLeaveOption, HalfLeaveOptionTo, UserName, UserMasterId = "";
            try {
                jResults = new JSONArray(response);
                // for (int i = 0; i < jResults.length(); i++) {
                JSONObject jorder = jResults.getJSONObject(0);
                StartDt = jorder.getString("StartDt");
                StartDt = StartDt.substring(StartDt.indexOf("(") + 1, StartDt.lastIndexOf(")"));
                long Etime = Long.parseLong(StartDt);
                sDate = new Date(Etime);
                String sdate = sdf.format(sDate);
                btn_from_date.setText(sdate);
                EndDt = jorder.getString("EndDt");
                EndDt = EndDt.substring(EndDt.indexOf("(") + 1, EndDt.lastIndexOf(")"));
                long Dtime = Long.parseLong(EndDt);
                eDate = new Date(Dtime);
                String edate = sdf.format(eDate);
                btn_to_date.setText(edate);
                Reason = jorder.getString("Reason");
                ed_leave_description.setText(Reason);
                ApprovedBy = jorder.getString("ApprovedBy");
                LeaveType = jorder.getString("LeaveType");
                leavetype.setText(LeaveType);
                Contact = jorder.getString("Contact");
                Mobile.setText(Contact);
                HalfLeaveOption = jorder.getString("HalfLeaveOption");
                HalfLeaveOptionTo = jorder.getString("HalfLeaveOptionTo");
                UserName = jorder.getString("UserName");
                UserMasterId = jorder.getString("UserMasterId");
                //  }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        EmpName.setText(UserName);

        if (cf.check_LeaveMode() > 0) {
            String MOb = LeaveMobPrefrence.getString("ActivityId", "");
            if (MOb.equalsIgnoreCase("")) {
                new StartSession(ApplyLeaveMainActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new DownloadmobNO().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });
            }
            Mobile.setText(MOb.trim());
            UpdateLeaveReportingTo();
        } else {
            showProgress();
            new StartSession(ApplyLeaveMainActivity.this, new CallbackInterface() {

                @Override
                public void callMethod() {
                    String MOb = LeaveMobPrefrence.getString("ActivityId", "");
                    if (MOb.equalsIgnoreCase("")) {
                        new DownloadmobNO().execute();
                    }
                    Mobile.setText(MOb.trim());
                    new DownloadLeavereportingTo().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        }
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle("  vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_leave);
        lsLeaveCode = new ArrayList<String>();
        lsLeaveReportingTo = new ArrayList<String>();
        sp_leave_reportingto = (Spinner) findViewById(R.id.sp_leave_reportingto);
        btn_to_date = (Button) findViewById(R.id.btn_to_date);
        btn_from_date = (Button) findViewById(R.id.btn_from_date);
        chek_to_first_half = (CheckBox) findViewById(R.id.chek_to_first_half);
        chk_first_half = (CheckBox) findViewById(R.id.chk_first_half);
        chk_second_half = (CheckBox) findViewById(R.id.chk_second_half);
        ed_leave_description = (EditText) findViewById(R.id.ed_leave_description);
        btn_apply_leave_save = (Button) findViewById(R.id.btn_apply_leave_save);
        btn_apply_leave_cancel = (Button) findViewById(R.id.btn_apply_leave_cancel);
        btn_reset = (Button) findViewById(R.id.btn_rst);
        EmpName = (TextView) findViewById(R.id.EmployeeName);
        leavetype = (TextView) findViewById(R.id.LeaveType);
        mLintodate = (LinearLayout) findViewById(R.id.linearlayout_apply_leave_to_date);
        Mobile = (TextView) findViewById(R.id.mobileno);
        Date c = Calendar.getInstance().getTime();
        String Fromdate = sdf.format(c);
        btn_from_date.setText(Fromdate);
        String Todate = sdf.format(c);
        btn_to_date.setText(Todate);




    }

    private void Setlistner() {
        btn_apply_leave_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate();
            }
        });
        btn_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeaveMainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_from_date.setTextColor(Color.BLACK);
                                btn_from_date.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();


            }
        });
        btn_apply_leave_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyLeaveMainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //      datePicker.setMinDate(c.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_to_date.setTextColor(Color.BLACK);
                                btn_to_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmpName.setText(UserName);
                UpdateLeaveReportingTo();
                mLintodate.setVisibility(View.VISIBLE);
                chek_to_first_half.setChecked(false);
                chk_first_half.setChecked(false);
                chk_second_half.setChecked(false);
                btn_from_date.setText(getNextDayDate());
                btn_to_date.setText(getNextDayDate());
                ed_leave_description.setText("");
                ed_leave_description.setHint("Reason");

            }
        });
        chk_first_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLintodate.setVisibility(View.GONE);
                chk_second_half.setChecked(false);
                chek_to_first_half.setChecked(false);
            }
        });
        chk_second_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLintodate.setVisibility(View.VISIBLE);

            }
        });
        chek_to_first_half.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLintodate.setVisibility(View.VISIBLE);

            }
        });

    }

    class DownloadGetAppDocInfo extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetDocAppInfo + "?Source=" + URLEncoder.encode("LeaveApply", "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_APP_DOC_INFO, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_APP_DOC_INFO, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_APP_DOC_INFO, null, values);
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
            if (res.contains("LeaveCode")) {

            }


        }

    }

    class ApplyLeaveUpload extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (response.contains("You have already applied leave for selected date !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You have already applied leave for selected date !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("You have less balance than applied !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You have less balance than applied !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Select proper date as you applying for second half !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Select proper date as you applying for second half !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("You can apply second half for same date !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You can apply second half for same date !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("selected date is Regular OFF !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "selected date is Regular OFF !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Start Date should be less than End Date !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Start Date should be less than End Date !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Selected date is holiday !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Selected date is holiday !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Selected date is your Weekly Off !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Selected date is your Weekly Off !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("You can not take more than 2 CL at a stretch !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You can not take more than 2 CL at a stretch !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("You can not take more than 6 SL at a stretch !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You can not take more than 6 SL at a stretch !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("You can't take CL/SL adjacent to PL !")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "You can't take CL/SL adjacent to PL !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Leave Applied Successfully!")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Leave Applied Successfully !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Leave Updated Successfully!")) {//"Leave Updated Successfully!"
                Toast.makeText(ApplyLeaveMainActivity.this, "Leave Updated successfully !", Toast.LENGTH_LONG).show();
                finish();
            } else if (response.contains("Select proper date as you applying for second half !")) {//"Leave Updated Successfully!"
                Toast.makeText(ApplyLeaveMainActivity.this, "Select proper date as you applying for second half !", Toast.LENGTH_LONG).show();
                finish();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_apply_leave;
            try {
                res = ut.OpenPostConnection(url, LeaveObj.toString().replaceAll("\\\\", ""),getApplicationContext());
                response = res.toString();
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }

            return "";
        }
    }

    class DownloadLeavereportingTo extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_ReportingTo;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_LEAVE_REPORTING_TO, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LEAVE_REPORTING_TO, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_LEAVE_REPORTING_TO, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (res.contains("UserLoginId")) {
                UpdateLeaveReportingTo();
                new DownloadGetAppDocInfo().execute();
            } else {
                dismissProgress();
            }


        }

    }

    class DownloadmobNO extends AsyncTask<String, Void, String> {
        String res;
        String columnName, columnValue;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Leave_Getmob;
            try {
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = null;
                    try {
                        jorder = jResults.getJSONObject(i);
                        columnValue = jorder.getString("Mobile");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                columnValue = "";
            }
            return columnValue;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (!res.equalsIgnoreCase("")) {
                SharedPreferences.Editor editor = LeaveMobPrefrence.edit();
                editor.putString("EmpMob", integer.trim());
                editor.commit();
                Mobile.setText(integer.trim());

            }


        }

    }


    public void Validate() {
        if (mLintodate.getVisibility() == View.GONE) {
            FromDate = btn_from_date.getText().toString();
            ToDate = btn_from_date.getText().toString();
            if (btn_from_date.getText().toString().equalsIgnoreCase("From Date")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Select From Date", Toast.LENGTH_LONG).show();
            } else if (ed_leave_description.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Enter Reason", Toast.LENGTH_LONG).show();
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date Start = null;
                Date End = null;
                try {
                    Start = formatter.parse(FromDate);
                    End = formatter.parse(ToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int a = End.compareTo(Start);
                if (End.compareTo(Start) >= 0) {
                    showProgress();
                    getLeaveObj();
                } else {
                    Toast.makeText(ApplyLeaveMainActivity.this, "Start Date should be less than End Date", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            FromDate = btn_from_date.getText().toString();
            ToDate = btn_to_date.getText().toString();
            if (btn_from_date.getText().toString().equalsIgnoreCase("From Date")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Select From Date", Toast.LENGTH_LONG).show();

            } else if (btn_to_date.getText().toString().equalsIgnoreCase("To Date")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Select To Date", Toast.LENGTH_LONG).show();
            } else if (ed_leave_description.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(ApplyLeaveMainActivity.this, "Enter Reason", Toast.LENGTH_LONG).show();
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date Start = null;
                Date End = null;
                try {
                    Start = formatter.parse(FromDate);
                    End = formatter.parse(ToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int a = End.compareTo(Start);
                if (End.compareTo(Start) >= 0) {
                    showProgress();
                    getLeaveObj();
                } else {
                    Toast.makeText(ApplyLeaveMainActivity.this, "Start Date should be less than End Date", Toast.LENGTH_LONG).show();
                }
            }
        }


    }

    private void getLeaveObj() {
        LeaveObj = new JSONObject();
        try {
            if (flag) {
                LeaveObj.put("MLId", MLID);
            } else {
                LeaveObj.put("MLId", "");
            }
            LeaveObj.put("LeaveType", URLEncoder.encode(leavetype.getText().toString(), "UTF-8"));
            LeaveObj.put("FromDate", FromDate);
            LeaveObj.put("ToDate", ToDate);
            LeaveObj.put("Reason", ed_leave_description.getText().toString());
            LeaveObj.put("Contact", Mobile.getText().toString().trim());
            String que = "SELECT UserLoginId FROM " + db.TABLE_LEAVE_REPORTING_TO + " WHERE UserName='" + sp_leave_reportingto.getSelectedItem().toString() + "'";
            Cursor cur = sql.rawQuery(que, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                LeaveObj.put("ReportingTo", cur.getString(cur.getColumnIndex("UserLoginId")));
            }
            LeaveObj.put("FromFH", chk_first_half.isChecked());
            LeaveObj.put("FromSH", chk_second_half.isChecked());
            LeaveObj.put("ToFH", chek_to_first_half.isChecked());
            LeaveObj.put("ExtraWorkingDate", "");
            LeaveObj.put("UserMasterId", URLEncoder.encode(UserMasterId, "UTF-8"));
            que = "SELECT LeaveApproval FROM " + db.TABLE_APP_DOC_INFO;
            Cursor cur1 = sql.rawQuery(que, null);
            if (cur1.getCount() > 0) {
                cur1.moveToFirst();
                LeaveObj.put("DocMthdId", URLEncoder.encode(cur1.getString(cur1.getColumnIndex("LeaveApproval")), "UTF-8"));

            }

            String FinalObj = LeaveObj.toString();
            String remark = "Apply " + LeaveType + " From " + FromDate + " to " + ToDate + " ";
            String url = CompanyURL + WebUrlClass.api_apply_leave;
            String op;
            if (flag) {
                op = "Leave Updated Successfully!";
            } else {
                op = "Leave Applied Successfully!";
            }

            CreateOfflineApplyLeave(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

           /* if (isnet()) {
                new StartSession(ApplyLeaveMainActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new ApplyLeaveUpload().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });
            } else {
                ut.displayToast(getApplicationContext(), "No Internet Connection");
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNextDayDate() {
        Date date1 = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date1);
    }

    private void UpdateLeaveReportingTo() {
        lsLeaveReportingTo.clear();
        String que = "SELECT UserName FROM " + db.TABLE_LEAVE_REPORTING_TO;
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsLeaveReportingTo.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ApplyLeaveMainActivity.this, android.R.layout.simple_spinner_item, lsLeaveReportingTo);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_leave_reportingto.setAdapter(dataAdapter);


    }

    private void CreateOfflineApplyLeave(final String url, final String parameter,
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

    private void showProgress() {

        mProgress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mProgress.setVisibility(View.GONE);


    }
}
