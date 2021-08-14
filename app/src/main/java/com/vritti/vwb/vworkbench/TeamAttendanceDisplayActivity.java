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
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AttendanceAdapter;
import com.vritti.vwb.Adapter.TeamAttendanceAdapter;
import com.vritti.vwb.Beans.Attendance;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TeamAttendanceDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",Attachment="",LinkId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    static Context context;
    SQLiteDatabase sql;
    TeamAttendanceAdapter attendanceAdapter;
    ArrayList<Attendance> attendanceArrayList;
    TextView txt_fromdate,txt_todate,txt_submit;
    ImageView img_from_date,img_to_date;
    private String FromDate,Todate;
    String UserMasterid="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_list_lay);

        context= TeamAttendanceDisplayActivity.this;
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


        Calendar aCalendar = Calendar.getInstance();

        aCalendar.set(Calendar.HOUR_OF_DAY, 0);
        aCalendar.set(Calendar.MINUTE, 0);
        aCalendar.set(Calendar.SECOND, 0);
        aCalendar.set(Calendar.MONTH, 1);

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
        cal.add(Calendar.DATE, -1);
        txt_todate.setText(dateString);
        Todate =formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", dateString);

        if (getIntent().hasExtra("UserMasterId")){
            UserMasterid=getIntent().getStringExtra("UserMasterId");
            UserMasterId=UserMasterid;
            getSupportActionBar().setTitle(getIntent().getStringExtra("UserName"));

        }else {
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            getSupportActionBar().setTitle("Attendance");
        }


        if (isnet()) {
            new StartSession(TeamAttendanceDisplayActivity.this, new CallbackInterface() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TeamAttendanceDisplayActivity.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(TeamAttendanceDisplayActivity.this,
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
                    new StartSession(TeamAttendanceDisplayActivity.this, new CallbackInterface() {
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(TeamAttendanceDisplayActivity.this);
                progressDialog.setMessage("Loading. Please wait...");
                progressDialog.setIndeterminate(false);
                //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.setCancelable(true);

            }
            progressDialog.show();


        }
        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebAPIUrl.api_GetRefreshChatRoom + "?ApplicationCode="+WebAPIUrl.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_getRecordsforAttendance + "?UserMasterId="+UserMasterId+"&fromdate="+FromDate+"&todate="+Todate;;

         //  String url ="https://vritti.ekatm.co.in/api/WorkWithUserAPI/getRecordsforAttendance?UserMasterId=206&fromdate=2021-02-01&todate=2021-02-28";




            try {
                res = ut.OpenConnection(url, TeamAttendanceDisplayActivity.this);
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
            progressDialog.dismiss();
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
                                attendance.setPKAttendanceId(jsonObject.getString("PKAttendanceId"));
                                attendanceArrayList.add(attendance);



                            }
                            attendanceAdapter=new TeamAttendanceAdapter(TeamAttendanceDisplayActivity.this,attendanceArrayList);
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

}
