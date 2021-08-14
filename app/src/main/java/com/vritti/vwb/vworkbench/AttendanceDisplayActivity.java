package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.expensemanagement.ExpenseData;
import com.vritti.expensemanagement.HistoryActivity;
import com.vritti.expensemanagement.HistoryAdapter;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AttendanceAdapter;
import com.vritti.vwb.Beans.Attendance;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AttendanceDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",Attachment="",LinkId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    static Context context;
    SQLiteDatabase sql;
    AttendanceAdapter attendanceAdapter;
    ArrayList<Attendance> attendanceArrayList;
    TextView txt_fromdate,txt_todate,txt_submit;
    ImageView img_from_date,img_to_date;
    private String FromDate,Todate;
    String UserMasterid="";
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    ProgressBar progressbar_1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_list_lay);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        context=AttendanceDisplayActivity.this;
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
        sql = db.getWritableDatabase();

        recyclerView=findViewById(R.id.report_list);
        attendanceArrayList=new ArrayList<>();
        txt_fromdate=findViewById(R.id.txt_fromdate);
        txt_todate=findViewById(R.id.txt_todate);
        txt_submit=findViewById(R.id.txt_submit);
        img_from_date=findViewById(R.id.img_from_date);
        img_to_date=findViewById(R.id.img_to_date);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        progressbar_1=findViewById(R.id.progressbar_1);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.DATE, -30);
        Date firstDateOfCurrentMonth = aCalendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dayFirst = sdf.format(firstDateOfCurrentMonth);
        System.out.println(dayFirst);

        txt_fromdate.setText(dayFirst);
        FromDate =formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", dayFirst);

        //txt_fromdate.setText("01-02-2021");

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf1.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        txt_todate.setText(dateString);
        Todate =formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", dateString);

        if (getIntent().hasExtra("UserMasterId")){
            UserMasterid=getIntent().getStringExtra("UserMasterId");
            UserMasterId=UserMasterid;
            txt_title.setText(getIntent().getStringExtra("UserName"));

        }else {
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            txt_title.setText("Attendance");
        }


        if (isnet()) {
            progressbar_1.setVisibility(View.VISIBLE);
            new StartSession(AttendanceDisplayActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadAttendanceList().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

        img_from_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceDisplayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                FromDate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;
                                txt_fromdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });

        img_to_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AttendanceDisplayActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                                Todate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;
                                txt_todate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String from=txt_fromdate.getText().toString();
                String to=txt_todate.getText().toString();
                FromDate =formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", from);
                Todate =formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", to);

                if (isnet()) {
                    new StartSession(AttendanceDisplayActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadAttendanceList().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                }


            }
        });



    }
    class DownloadAttendanceList extends AsyncTask<String, Void, String> {

        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getRecordsforAttendance + "?UserMasterId="+UserMasterId+"&fromdate="+FromDate+"&todate="+Todate;;





            try {
                res = ut.OpenConnection(url,AttendanceDisplayActivity.this);
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressbar_1.setVisibility(View.GONE);
            if (response.equalsIgnoreCase("[]")){


            }else {
                if (response != null) {
                    JSONArray jResults = new JSONArray();
                    try {
                        attendanceArrayList.clear();
                        jResults = new JSONArray(response);
                        ContentValues values = new ContentValues();

                        if (jResults.length() > 0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jsonObject = jResults.getJSONObject(i);
                                Attendance attendance = new Attendance();
                                attendance.setDate(jsonObject.getString("Date"));
                                attendance.setBioInTime(jsonObject.getString("BioInTime"));
                                attendance.setBioDiff(jsonObject.getString("BioDiff"));
                                attendance.setBioOutTime(jsonObject.getString("BioOutTime"));
                                attendance.setWorkHours(jsonObject.getString("WorkHours"));
                                attendance.setEndTime(jsonObject.getString("EndTime"));
                                attendance.setMainDiff(jsonObject.getString("MainDiff"));
                                attendance.setLeaveType(jsonObject.getString("LeaveType"));
                                attendance.setAttendanceCode(jsonObject.getString("AttendanceCode"));
                                attendance.setRemarks(jsonObject.getString("Remarks"));
                                attendance.setCalls(jsonObject.getString("Calls"));
                                attendance.setVisits(jsonObject.getString("Visits"));
                                attendance.setMails(jsonObject.getString("Mails"));
                                attendanceArrayList.add(attendance);



                            }
                            attendanceAdapter=new AttendanceAdapter(AttendanceDisplayActivity.this,attendanceArrayList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setAdapter(attendanceAdapter);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

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
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}
