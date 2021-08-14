package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 11/16/2016.
 */
public class AddTimesheetActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    TextView tv_activity_disc;
    Button btn_transfer_activity, btn_date, btn_fromTime, btn_toTime, btn_save, btn_close;
    EditText ed_timesheet_desc;
    CheckBox chk_make_complete;
    String TimesheetDate, TimesheetFromTime, TimesheetToTime;
    JSONObject jsonObj;
    String ActivityId, ActivityName;
    String JsonString;
    String ValidBackDate, IsTimeslotBooked;
    private Boolean SaveChecked;
    SharedPreferences userpreferences;
    String UsermasterID;
    SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat tff = new SimpleDateFormat("hh:mm aa");
    ProgressBar mprogress;
    LinearLayout mLinFromTo, mLinBooked;
    EditText mEditbooked;
    Boolean Flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_timesheet_entry);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        IsTimeslotBooked = userpreferences.getString(WebUrlClass.USERINFO_TIMESHEET_ISTIMESlOT, "");
        Intent i = getIntent();
        if (i.hasExtra("ActivityId")) {
            ActivityName = i.getStringExtra("ActivityName");
            ActivityId = i.getStringExtra("ActivityId");
        }

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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        ValidBackDate = ut.getValue(context, WebUrlClass.GET_BACKDATE_TIMESHEET_KEY, settingKey);
        InitView();
        setListner();
        if (IsTimeslotBooked.equalsIgnoreCase("Y")) {
            Flag = true;
            mLinBooked.setVisibility(View.VISIBLE);
            mLinFromTo.setVisibility(View.GONE);
        } else {
            IsTimeslotBooked = i.getStringExtra("PAllowUsrTimeSlotHrs");
            if (IsTimeslotBooked.equalsIgnoreCase("Y")) {
                Flag = true;
                mLinBooked.setVisibility(View.VISIBLE);
                mLinFromTo.setVisibility(View.GONE);
            } else {
                Flag = false;
                mLinBooked.setVisibility(View.GONE);
                mLinFromTo.setVisibility(View.VISIBLE);
            }


        }
    }


    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        tv_activity_disc = (TextView) findViewById(R.id.tv_activity_disc);
        tv_activity_disc.setText(ActivityName);
        chk_make_complete = (CheckBox) findViewById(R.id.chk_make_complete);
        btn_transfer_activity = (Button) findViewById(R.id.btn_transfer_activity);
        btn_date = (Button) findViewById(R.id.btn_date);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_timesheet);
        btn_fromTime = (Button) findViewById(R.id.btn_fromTime);
        btn_toTime = (Button) findViewById(R.id.btn_toTime);
        ed_timesheet_desc = (EditText) findViewById(R.id.ed_timesheet_desc);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_close = (Button) findViewById(R.id.btn_close);
        mEditbooked = (EditText) findViewById(R.id.btn_booked);
        mLinFromTo = (LinearLayout) findViewById(R.id.linFromto);
        mLinBooked = (LinearLayout) findViewById(R.id.timeslothr);
        String date = dff.format(new Date());
        //String ftime = tff.format(new Date());
        btn_date.setText(date);
        TimesheetDate = btn_date.getText().toString();
        dounloadtimesheettime();
    }

    private void setListner() {
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chk_make_complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String desc = ed_timesheet_desc.getText().toString();
                if (!Flag) {
                    if (btn_fromTime.getText().toString().equalsIgnoreCase("From Time")) {
                        ut.displayToast(getApplicationContext(), "Please Select From Time");
                    } else if (btn_toTime.getText().toString().equalsIgnoreCase("To Time")) {
                        ut.displayToast(getApplicationContext(), "Please Select To Time");
                    } else {
                        if (!(desc.equalsIgnoreCase(""))) {
                            //  showProgress();
                            insertTimesheet(ActivityId);
                        } else {
                            ut.displayToast(getApplicationContext(), "Please Enter Description");
                        }
                    }
                } else {
                    if (mEditbooked.getText().toString().equalsIgnoreCase("")) {
                        ut.displayToast(getApplicationContext(), "Please Enter Timesheet Hours");
                    } else if (mEditbooked.getText().toString().equalsIgnoreCase("0")) {
                        ut.displayToast(getApplicationContext(), "Enter Valid Hours");
                    } else {
                        if (!(desc.equalsIgnoreCase(""))) {
                            //  showProgress();
                            insertTimesheet(ActivityId);
                        } else {
                            ut.displayToast(getApplicationContext(), "Please Enter Description");
                        }
                    }
                }


            }
        });
        btn_transfer_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                int a = 0;
                try {
                    a = Integer.parseInt(ValidBackDate);

                } catch (Exception e) {
                    e.printStackTrace();
                    a = 0;
                }
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                final int finalA = a;
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTimesheetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());
                                Calendar cal = Calendar.getInstance();
                                datePicker.setMaxDate(cal.getTimeInMillis());
                                String date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/"
                                        + year;
                                btn_date.setText(date);
                                TimesheetDate = date;

                                dounloadtimesheettime();

                            }
                        }, year, month, day);
                c.add(Calendar.DATE, -a);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                }
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        btn_fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTimesheetActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override

                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {

                                SimpleDateFormat dff = new SimpleDateFormat("hh:mm aa");
                                mcurrentTime.set(mcurrentTime.HOUR, selectedHour);
                                mcurrentTime.set(mcurrentTime.MINUTE, selectedMinute);

                                String date = dff.format(mcurrentTime.getTime());
                                String time = updateTime(selectedHour, selectedMinute);
                                btn_fromTime.setText("");
                                btn_fromTime.setText(time);
                            }
                        }, hour, minute, false);
                mTimePicker.setTitle("Select Time");

                mTimePicker.show();

            }
        });
        btn_toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddTimesheetActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override

                            public void onTimeSet(TimePicker timePicker,
                                                  int selectedHour, int selectedMinute) {
                                String time = updateTime(selectedHour, selectedMinute);
                                //  SimpleDateFormat dff = new SimpleDateFormat("hh:mm a");
                                mcurrentTime.set(mcurrentTime.HOUR, selectedHour);
                                mcurrentTime.set(mcurrentTime.MINUTE, selectedMinute);
                                //  String date = dff.format(mcurrentTime.getTime());
                                btn_toTime.setText(time);
                            }
                        }, hour, minute, false);// Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private void dounloadtimesheettime() {
        showProgress();
        new StartSession(AddTimesheetActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadTimesheetTme().execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
                dismissProgress();
            }
        });
    }

    private String checktime() {
        if (btn_fromTime.getText().toString().equalsIgnoreCase("From Time")) {
            return "Select From Time";
        } else if (btn_fromTime.getText().toString().equalsIgnoreCase("To Time")) {
            return "Select To Time";
        }
        return "";
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "", hour = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        if (hours < 10) {
            hour = "0" + hours;
        } else {
            hour = String.valueOf(hours);
        }
        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hour).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    private void insertTimesheet(final String ActivityId) {
        jsonObj = new JSONObject();
        try {
            jsonObj.put("ActivityId", ActivityId);
            jsonObj.put("workDesc", ed_timesheet_desc.getText().toString());
            jsonObj.put("forDate", btn_date.getText().toString());
            jsonObj.put("fromTime", btn_fromTime.getText().toString());
            jsonObj.put("toTime", btn_toTime.getText().toString());
            jsonObj.put("SaveChecked", chk_make_complete.isChecked());
            JsonString = jsonObj.toString().trim();
            JsonString = JsonString.replaceAll("\\\\", "");
            JsonString = JsonString.replaceAll("\" ", "\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String fordate = btn_date.getText().toString();
        String input = getDate(fordate);
        SaveChecked = chk_make_complete.isChecked();
        String SaveChecked1 = SaveChecked.toString();
        String fromTime, toTime, Bookedhrs;
        if (Flag) {
            fromTime = "";
            toTime = "";
            Bookedhrs = mEditbooked.getText().toString().trim();
        } else {
            fromTime = btn_fromTime.getText().toString();
            toTime = btn_toTime.getText().toString();
            Bookedhrs = "0";
        }

        String workDesc = ed_timesheet_desc.getText().toString();
        String remark = "Add Timesheet of  " + ActivityName + " From time " + fromTime + " to " + toTime;
        String url = null;
        try {
            url = CompanyURL + WebUrlClass.api_getInsertTimesheet + "?forDate=" + URLEncoder.encode(input, "UTF-8") + "&SaveChecked="
                    + URLEncoder.encode(SaveChecked1, "UTF-8") + "&fromTime=" + URLEncoder.encode(fromTime, "UTF-8") + "&ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8") +
                    "&toTime=" + URLEncoder.encode(toTime, "UTF-8") + "&workDesc=" + URLEncoder.encode(workDesc, "UTF-8") + "&Timehrs=" + Bookedhrs + "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String op = "1";

        CreateOfflineModeTimesheet(url, null, WebUrlClass.GETFlAG, remark, op);

        if (SaveChecked) {
            SQLiteDatabase sql = db.getWritableDatabase();
            sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
            sql.delete(db.TABLE_CHAT_CHATROOM_GROUP_LIST, "ChatSourceId=?", new String[]{ActivityId});

            Cursor c = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);

            int a1 = c.getCount();
            Intent i = new Intent(AddTimesheetActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else {
            Intent i = new Intent(AddTimesheetActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }



     /*   new StartSession(AddTimesheetActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                String fordate = btn_date.getText().toString();
                String input = getDate(fordate);
                SaveChecked = chk_make_complete.isChecked();
                String SaveChecked1 = SaveChecked.toString();
                String fromTime, toTime , Bookedhrs ;
                if (Flag) {
                     fromTime ="";
                     toTime ="";
                     Bookedhrs = mEditbooked.getText().toString().trim();
                } else {
                     fromTime = btn_fromTime.getText().toString();
                     toTime = btn_toTime.getText().toString();
                     Bookedhrs = "0";
                }

                String workDesc = ed_timesheet_desc.getText().toString();

                new UploadgetTimesheetDataJSON().execute(input, SaveChecked1, fromTime, ActivityId, toTime, workDesc, Bookedhrs);
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);
                dismissProgress();
            }
        });*/
    }

    private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

   /* private String changetoyy_mm_dd(String input) {
        String output = null;
        try {
            SimpleDateFormat dateFormatinput = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dateFormatoutput = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormatinput.parse(input);
            output = dateFormatoutput.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            output = null;
        }
        return output;
    }*/

  /*  class UploadTimesheetDataJSON extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        DatabaseHandler db = new DatabaseHandler(AddTimesheetActivity.this);
        SQLiteDatabase sql = db.getWritableDatabase();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostInsertTimesheet;
            try {
                res = ut.OpenPostConnection(url, JsonString);
                Log.e("response data",res+"");

              *//*  ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(response);
                String msg = "";
                sql.delete(db.TABLE_POST_INSERT_TIMESHEET, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_POST_INSERT_TIMESHEET, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_POST_INSERT_TIMESHEET, null, values);
                }
*//*
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }*/

    private void CreateOfflineModeTimesheet(final String url, final String parameter,
                                            final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);

            startService(intent1);

        } else {
            Toast.makeText(getApplicationContext(), "Data not saved", Toast.LENGTH_LONG).show();
        }

    }

    class UploadgetTimesheetDataJSON extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        SQLiteDatabase sql = db.getWritableDatabase();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            String a = integer;
            Log.e(" string ..", a);
            if (a.contains("You can not fill time sheet from less than IN time")) {
                ut.displayToast(AddTimesheetActivity.this, "You can not fill time sheet less than IN time");
            } else if (a.contains("You can not fill time sheet for more than")) {
                ut.displayToast(AddTimesheetActivity.this, "You can not fill time sheet for more than current Time");
            } else if (a.contains("From time should not be grater than To time")) {
                ut.displayToast(AddTimesheetActivity.this, "From time should not be grater than To time");
            } else if (a.contains("You can not fill time sheet greater than current time")) {
                ut.displayToast(AddTimesheetActivity.this, "You can not fill time sheet greater than current time");
            } else if (a.contains("You have already filled the time slot")) {
                ut.displayToast(AddTimesheetActivity.this, "You have already filled the time slot");
            } else if (a.equalsIgnoreCase("1")) {
                ut.displayToast(AddTimesheetActivity.this, "Record Added successfully");
                if (SaveChecked) {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                    Cursor c = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
                    int a1 = c.getCount();
                    Intent i = new Intent(AddTimesheetActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(AddTimesheetActivity.this, ActivityMain.class);
                    startActivity(i);
                    finish();
                }

            } else {
                ut.displayToast(AddTimesheetActivity.this, "Could not Connect to server");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getInsertTimesheet + "?forDate=" + URLEncoder.encode(params[0], "UTF-8") + "&SaveChecked="
                        + URLEncoder.encode(params[1], "UTF-8") + "&fromTime=" + URLEncoder.encode(params[2], "UTF-8") + "&ActivityId=" + URLEncoder.encode(params[3], "UTF-8") +
                        "&toTime=" + URLEncoder.encode(params[4], "UTF-8") + "&workDesc=" + URLEncoder.encode(params[5], "UTF-8") + "&Timehrs=" + params[6] + "";
                res = ut.OpenConnection(url, getApplicationContext());
                Log.e("response data", res + "");
                response = res.toString();
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }

    class GetTimesheetDataSession extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        SQLiteDatabase sql = db.getWritableDatabase();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (integer.contains("ActivityId")) {

            }
            String a = integer;
            Log.e(" string ..", a);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetTimeSheet + "?UserMstrId=" + UsermasterID;

                res = ut.OpenConnection(url, getApplicationContext());

                Log.e("response data", res + "");
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }

    class DownloadTimesheetTme extends AsyncTask<String, Void, String> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (ValidBackDate.equalsIgnoreCase("")) {
                new DownloadValidBackDateDataJSON().execute();
            }
            dismissProgress();

            String TimesheetFinal = "09:00 AM";
            if (!(TimesheetFromTime.equalsIgnoreCase("NoValue"))) {
                if (TimesheetFromTime.contains("AM")) {
                    String TimesheetTime = TimesheetFromTime.substring(1, 6);
                    TimesheetTime = TimesheetTime.trim();
                    TimesheetFinal = TimesheetTime + " AM";
                } else if (TimesheetFromTime.contains("PM")) {
                    String TimesheetTime = TimesheetFromTime.substring(1, 6);
                    TimesheetTime = TimesheetTime.trim();
                    TimesheetFinal = TimesheetTime + " PM";
                }

                btn_fromTime.setText(TimesheetFinal);

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa", Locale.US);
                Date currentDateandTime = null;
                try {
                    currentDateandTime = sdf1.parse(TimesheetFinal);
                    TimesheetFromTime = sdf1.format(currentDateandTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  Date date = sdf.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDateandTime);
                calendar.add(Calendar.HOUR, 1);
                TimesheetToTime = calendar.getTime() + "";
                try {
                    TimesheetToTime = sdf1.format(calendar.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TimesheetToTime.contains(".")) {
                    TimesheetToTime.replaceAll(".", "");
                } else {

                }

                btn_toTime.setText(TimesheetToTime);
            }


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                TimesheetDate = getDate(TimesheetDate);
                url = CompanyURL + WebUrlClass.api_timesheetime + "?date=" + URLEncoder.encode(TimesheetDate, "UTF-8");
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");
                TimesheetFromTime = res.substring(res.indexOf(" "));
                if (TimesheetFromTime.contains("AM")) {

                } else if (TimesheetFromTime.contains("PM")) {

                } else {
                    TimesheetFromTime = "NoValue";
                }


            } catch (Exception e) {
                TimesheetFromTime = "NoValue";
            }
            return TimesheetFromTime;
        }
    }

    public static boolean validatePastDate(Context mContext, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day < currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (month > currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (year > currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean validateFutureDate(Context mContext, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day < currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_LONG).show();
            return false;
        } else if (month < currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_LONG).show();
            return false;
        } else if (year < currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    class DownloadValidBackDateDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_valid_backdate_entry;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ValidBackDate = response;
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


    }

    private String getDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = null;
        String ads = "";
        try {
            date1 = dateFormat.parse(date);
            ads = dateFormat1.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return ads;
    }
}