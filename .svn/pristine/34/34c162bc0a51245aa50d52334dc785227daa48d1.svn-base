package com.vritti.crmlib.vcrm7;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ReasonBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

/**
 * Created by sharvari on 03-Jan-17.
 */

public class CallRatingActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    Toolbar toolbar_action;
    public static EditText editTextExpecteddate, Expectedvalue;
    static int year, month, day;
    public static String date = null;
    public static String today, todaysDate;

    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    ArrayList<ReasonBean> reasonBeanArrayList;
    List reason;
    Spinner spinner_reason, spinner_callstatus;
    public static String PKReasonID, callid, jsonparams, callstatus, CurrentDate;
    public static String callStatus;
    Button buttonSave, buttonClose;
    public static String ReasonDescription;
    ProgressHUD progressHUD;
    SimpleDateFormat dfDate;
    LinearLayout layreason;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_call_rating);


        dfDate = new SimpleDateFormat("dd/MM/yyy");
        CurrentDate = dfDate.format(new Date());
        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
        callstatus = intent.getStringExtra("callstatus");
        //Amount for cv
        init();
        if (callstatus.equalsIgnoreCase("Hot")) {
            ((Spinner) findViewById(R.id.spinner_callstatus)).setSelection(1);
        } else if (callstatus.equalsIgnoreCase("Warm")) {
            ((Spinner) findViewById(R.id.spinner_callstatus)).setSelection(2);
        } else if (callstatus.equalsIgnoreCase("Cold")) {
            ((Spinner) findViewById(R.id.spinner_callstatus)).setSelection(3);
        }
        editTextExpecteddate.setText(CurrentDate);
        setListener();
        if (cf.getReasoncount() > 0) {
            UpdatList();
        } else {
            if (isnet()) {
                new StartSession(CallRatingActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetReason().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

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

    private void init() {
        toolbar_action = (Toolbar) findViewById(R.id.toolbar_action);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextExpecteddate = (EditText) findViewById(R.id.editTextExpecteddate);
        Expectedvalue = (EditText) findViewById(R.id.Expectedvalue);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
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
        reasonBeanArrayList = new ArrayList<ReasonBean>();
        spinner_reason = (Spinner) findViewById(R.id.spinner_reason);
        spinner_callstatus = (Spinner) findViewById(R.id.spinner_callstatus);
        ((Spinner) findViewById(R.id.spinner_reason)).setSelection(0);
        ((Spinner) findViewById(R.id.spinner_callstatus)).setSelection(0);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonClose = (Button) findViewById(R.id.buttonClose);
        layreason = (LinearLayout) findViewById(R.id.layreason);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    private void setListener() {

        editTextExpecteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(CallRatingActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                if (compare_date(date) == true) {
                                    editTextExpecteddate.setText(date);
                                } else {
                                    editTextExpecteddate.setText(date);
                                    Toast.makeText(CallRatingActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();


            }
        });

        spinner_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ReasonDescription = spinner_reason.getSelectedItem().toString();

                if (ReasonDescription.equalsIgnoreCase("Select")) {

                } else {
                    String que = "SELECT PKReasonID FROM " + db.TABLE_REASON + " WHERE ReasonDescription='" + ReasonDescription + "'";
                    Cursor cur = sql.rawQuery(que, null);
                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        PKReasonID = cur.getString(cur.getColumnIndex("PKReasonID"));
                    }

                    SharedPreferences.Editor editor = userpreferences.edit();
                    editor.putString("PKReasonID", PKReasonID);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_callstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callStatus = spinner_callstatus.getSelectedItem().toString();

               /* if (callstatus.equalsIgnoreCase(callStatus)) {
                    layreason.setVisibility(View.GONE);
                } else if (callStatus.equalsIgnoreCase("Select")) {
                    layreason.setVisibility(View.GONE);
                } else {
                    layreason.setVisibility(View.VISIBLE);
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callStatus.equalsIgnoreCase("Cold")) {
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CallId", callid);
                        jsonObject.put("CallStatus", callStatus);
                        jsonObject.put("CallStatusChangeId", PKReasonID);
                        jsonObject.put("ExpectedValue", Expectedvalue.getText().toString());
                        jsonObject.put("ExpectedClosureDate", editTextExpecteddate.getText().toString());
                        jsonparams = jsonObject.toString();
                        jsonparams = jsonparams.replaceAll("\\\\", "");


                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    if (isnet()) {
                        new StartSession(CallRatingActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostCallRating().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                } else {
                    if (validate(CallRatingActivity.this) == true) {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("CallId", callid);
                            jsonObject.put("CallStatus", callStatus);
                            jsonObject.put("CallStatusChangeId", PKReasonID);
                            jsonObject.put("ExpectedValue", Expectedvalue.getText().toString());
                            jsonObject.put("ExpectedClosureDate", editTextExpecteddate.getText().toString());
                            jsonparams = jsonObject.toString();
                            jsonparams = jsonparams.replaceAll("\\\\", "");


                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                        if (isnet()) {
                            new StartSession(CallRatingActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostCallRating().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }
                }
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallRatingActivity.this.finish();
            }
        });


    }

    public static boolean validate(Context context) {
        if (callStatus.equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Select Call Status", Toast.LENGTH_LONG).show();
            return false;
        } else if (ReasonDescription.equalsIgnoreCase("Select")) {
            Toast.makeText(context, "Select Reason", Toast.LENGTH_LONG).show();
            return false;
        } else if (Expectedvalue.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Select Expected value", Toast.LENGTH_LONG).show();
            return false;
        } else if (editTextExpecteddate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(context, "Select Expected closure date", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).before(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    class GetReason extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ActivityLogIn.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();*/
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Get_Reason
                        + "?ReasonCode=" + URLEncoder.encode("RD", "UTF-8");

                res = ut.OpenConnection(url);
                res = res.toString().replaceAll("\\\\", "");
                res = res.replaceAll("\\\\\\\\/", "");
                res = res.substring(1, res.length() - 1);

                ContentValues values = new ContentValues();
                reason = new ArrayList();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_REASON, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_REASON, null);
                int count = c.getCount();
                String columnName, columnValue;
                reason.add("Select");
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (columnName.equalsIgnoreCase("ReasonDescription")) {
                            reason.add(jorder.getString("ReasonDescription"));
                        }


                    }
                    long a = sql.insert(db.TABLE_REASON, null, values);
                    Log.d("crm_dialog_action", "count " + a);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("PKReasonID")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CallRatingActivity.this, android.R.layout.simple_spinner_item, reason);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_reason.setAdapter(dataAdapter);
            }
        }

    }


    private void UpdatList() {
        reason = new ArrayList();
        String query = "SELECT distinct  ReasonDescription" +
                " FROM " + db.TABLE_REASON;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            reason.add("Select");

            cur.moveToFirst();
            do {
                reason.add(cur.getString(cur.getColumnIndex("ReasonDescription")));
                //   String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));


            } while (cur.moveToNext());

        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CallRatingActivity.this, android.R.layout.simple_spinner_item, reason);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_reason.setAdapter(dataAdapter);
    }

    private void showProgressDialog() {

        progressbar.setVisibility(View.VISIBLE);
        // progressHUD = ProgressHUD.show(CallRatingActivity.this, "", false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    class PostCallRating extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Save_Call_Rating;

            try {
                res = ut.OpenPostConnection(url, jsonparams);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.equalsIgnoreCase("")) {
                Intent intent = new Intent(CallRatingActivity.this, CallListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
