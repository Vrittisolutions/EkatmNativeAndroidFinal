package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.vritti.vwb.Beans.Username;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by sharvari on 28-Mar-18.
 */

public class EmployeeAdvanceRequestActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    SimpleDateFormat dateFormatdate,dateFormatdate_1;
    EditText edt_date, ed_travel_purpose, edt_amount;
    Spinner sp_approver, spinner_employee;
    ImageView img_date;
    String date;


    SQLiteDatabase sql;
    String DocApprMthdId2;
    SharedPreferences userpreferences;
    public static ProgressBar mprogress;
    ArrayList<String> lsClaimApproverList;
    ArrayList<Username> usernameArrayList;
    ArrayList<String> usernameArraylist1;
    TextView txt_send_request, txt_out_amount, txt_out_date, txt_last_amount, txt_last_date, txt_cancel;
    String Amount = "", Purpose = "", ApproverId, EmployeeId, FinalObject, OutstandingAmt, OutstandingSince, LastClaimAmt, LastClaimDt;
    private JSONObject jsonObject;
    LinearLayout len_amount;
    String Source="AdvancePay",docHdrId="";
    String Advance="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_emloyee_advance_req);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();

        lsClaimApproverList = new ArrayList<String>();
        usernameArrayList = new ArrayList<>();
        usernameArraylist1 = new ArrayList<>();
        init();


        if (cf.check_emprequest_claim_approver() > 0) {
            UpdateApproverList();
        } else {
            if (ut.isNet(getApplicationContext())) {

                new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new GetDochdrId().execute();
     }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {

                Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }

        if (ut.isNet(getApplicationContext())) {

            new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetUsername().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } else {

            Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        dateFormatdate = new SimpleDateFormat("dd/MM/yyyy");
        edt_date = (EditText) findViewById(R.id.edt_date);
        ed_travel_purpose = (EditText) findViewById(R.id.ed_travel_purpose);
        edt_amount = (EditText) findViewById(R.id.edt_amount);
        img_date = (ImageView) findViewById(R.id.img_date);
        sp_approver = (Spinner) findViewById(R.id.sp_approver);
        spinner_employee = (Spinner) findViewById(R.id.spinner_employee);
        txt_send_request = (TextView) findViewById(R.id.txt_send_request);
        txt_cancel = (TextView) findViewById(R.id.txt_cancel);
        txt_out_date = (TextView) findViewById(R.id.txt_out_date);
        txt_out_amount = (TextView) findViewById(R.id.txt_out_amount);
        txt_last_amount = (TextView) findViewById(R.id.txt_last_amount);
        txt_last_date = (TextView) findViewById(R.id.txt_last_date);
        len_amount = (LinearLayout) findViewById(R.id.len_amount);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        edt_date.setText(dateFormatdate.format(new Date()));


        dateFormatdate_1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Advance=dateFormatdate_1.format(new Date());




        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        img_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeAdvanceRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                               // date = year + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", (dayOfMonth));
                                date = String.format("%02d", (dayOfMonth)) + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + year;
                                edt_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        edt_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EmployeeAdvanceRequestActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = year + "/" + String.format("%02d", (monthOfYear + 1)) + "/" + String.format("%02d", (dayOfMonth));
                                edt_date.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        sp_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String query = "SELECT distinct UserMasterId,UserName" +
                        " FROM " + db.TABLE_EMPLOYEE_REQUEST_APPROVER +
                        " WHERE UserName='" + sp_approver.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);
                // lstReference.add("Select");
                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {


                        ApproverId = cur.getString(cur.getColumnIndex("UserMasterId"));


                    } while (cur.moveToNext());

                } else {
                    ApproverId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_employee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (usernameArrayList.size() > 0) {
                    EmployeeId = usernameArrayList.get(position).getUserMasterId();
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        txt_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amount = edt_amount.getText().toString();
                date = edt_date.getText().toString();

                Advance=formateDateFromstring("dd/MM/yyyy", "yyyy-MM-dd hh:mm:ss", date);
                Purpose = ed_travel_purpose.getText().toString();

                if(Amount.equals("0")){
                    Toast.makeText(EmployeeAdvanceRequestActivity.this, "Requested Amount should be greater then 0", Toast.LENGTH_SHORT).show();

                }else if(Purpose.equalsIgnoreCase("") || Purpose.equalsIgnoreCase(" ")
            || Purpose.equalsIgnoreCase(null) || Purpose.equalsIgnoreCase("null")){
                    Toast.makeText(EmployeeAdvanceRequestActivity.this, "Please Fill Purpose of Advance", Toast.LENGTH_SHORT).show();
                }else{
                    jsonObject = new JSONObject();
                    Purpose = ed_travel_purpose.getText().toString();
                    date = date.toString().replace("\\\\", "");
                    date = date.toString().replace("\\\\//", "");

                    try {
                        jsonObject.put("EmployeeId", EmployeeId);
                        jsonObject.put("EmpDate", Advance+".00");
                        jsonObject.put("EmpAdvAmt", Amount);
                        jsonObject.put("Reason", Purpose);
                        jsonObject.put("ApproverId", ApproverId);
                        jsonObject.put("DocMthdId", DocApprMthdId2);
                        jsonObject.put("Mode", "A");
                        jsonObject.put("AdvancePaymentHdrId", "");
                        jsonObject.put("PaymentNo", "");
                        FinalObject = jsonObject.toString().replaceAll("\\\\", "");
                        FinalObject = FinalObject.replaceAll("\\\\\\\\/", "");

                        if (ut.isNet(EmployeeAdvanceRequestActivity.this)) {
                            new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostProspectUpdateJSON().execute(FinalObject);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }


                            });

                        }

                       /* String remark = "Apply advance request for  "+ Purpose;
                        String url = CompanyURL + WebUrlClass.api_PostInsertAdvanePayment;
                        String op = "Success";

                        CreateOfflineModeReschedule(url, FinalObject, WebUrlClass.POSTFLAG, remark, op);
*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    private void CreateOfflineModeReschedule(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(EmployeeAdvanceRequestActivity.this, "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            ClaimDetailActivity.lsCalimDetails.clear();
            Intent intent1 = new Intent(EmployeeAdvanceRequestActivity.this, SendOfflineData.class);
            startService(intent1);
            Intent intent = new Intent(EmployeeAdvanceRequestActivity.this, com.vritti.vwb.vworkbench.ActivityMain.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

           /* if (detailsListView != null) {
                detailsListView.removeAllViews();
            }*/

        } else {
            Toast.makeText(EmployeeAdvanceRequestActivity.this, "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }


    class GetDochdrId extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected Integer doInBackground(Integer... params) {
            //url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_advancepay_dochdrId +"?Source=" + Source;

            // url = http://b207.ekatm.com/api/MyClaimAPI/getApproverList?DocMthdId=69
            try {
                res = ut.OpenConnection(url, EmployeeAdvanceRequestActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            String DocApprMthdId2="";
            super.onPostExecute(integer);
            if (!res.equalsIgnoreCase("")) {


                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        docHdrId = jsonObject.getString("DocApprMthdId");
                        //stringArrayList.add("DocApprMthdId2");

                    }

                    if (ut.isNet(getApplicationContext())) {

                        new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new GetClaimApprover().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }


                    /*ArrayList<String> stringArrayList = new ArrayList<>();
                    stringArrayList.add(DocApprMthdId2);*/
                    //  stringArrayList.add(DocApprMthdId2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // docHdrId = stringArrayList.get(0);
            }else{

            }
        }
    }

    class GetClaimApprover extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Integer doInBackground(Integer... params) {

            url = CompanyURL + WebUrlClass.api_GetApprover+"?DocMthdId=" + docHdrId;

            Log.i("Url::",url);
            try {
                res = ut.OpenConnection(url, EmployeeAdvanceRequestActivity.this);
                res = res.toString().replaceAll("\\\\", "");
                res = res.replaceAll("\\\\\\\\/", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_EMPLOYEE_REQUEST_APPROVER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_EMPLOYEE_REQUEST_APPROVER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_EMPLOYEE_REQUEST_APPROVER, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (!res.equalsIgnoreCase("")) {
                hidprogress();
                UpdateApproverList();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EmployeeAdvanceRequestActivity.this.finish();
    }

    void showprogress() {
        mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mprogress.setVisibility(View.GONE);

    }

    public void UpdateApproverList() {
        lsClaimApproverList.clear();
        String query = "SELECT UserName FROM " + db.TABLE_EMPLOYEE_REQUEST_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsClaimApproverList.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EmployeeAdvanceRequestActivity.this, android.R.layout.simple_spinner_item, lsClaimApproverList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_approver.setAdapter(dataAdapter);

    }

    class GetUsername extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetUserName;
                res = ut.OpenConnection(url, EmployeeAdvanceRequestActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();

            usernameArrayList = new ArrayList<>();
            usernameArraylist1 = new ArrayList<>();
            try {
                JSONArray jResults = null;
                jResults = new JSONArray(response);
                for (int i = 0; i < jResults.length(); i++) {
                    if (jResults.length() > 0) {
                        Username username = new Username();
                        JSONObject jSection = jResults.getJSONObject(i);
                        String UserMasterId = jSection.getString("UserMasterId");
                        username.setUserMasterId(UserMasterId);
                        username.setReportingTo(jSection.getString("ReportingTo"));
                        username.setUserName(jSection.getString("UserName"));
                        usernameArrayList.add(username);
                        String UserName = jSection.getString("UserName");
                        usernameArraylist1.add(UserName);


                    }
                    spinner_employee.setAdapter(new ArrayAdapter<String>(EmployeeAdvanceRequestActivity.this,
                            R.layout.vwb_custom_spinner_txt,
                            usernameArraylist1));


                }
                if (ut.isNet(getApplicationContext())) {

                    new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetDocAppInfo().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                } else {

                    Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class GetDocAppInfo extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getDocAppInfo + "?Source=AdvancePay";
                res = ut.OpenConnection(url, EmployeeAdvanceRequestActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();

            try {
                JSONArray jResults = null;
                jResults = new JSONArray(integer);
                for (int i = 0; i < jResults.length(); i++) {
                    if (jResults.length() > 0) {
                        JSONObject jSection = jResults.getJSONObject(i);
                        DocApprMthdId2 = jSection.getString("DocApprMthdId");


                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (ut.isNet(getApplicationContext())) {

                new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetOutstandingAmt().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {

                Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }

    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_PostInsertAdvanePayment;
            try {
                res = ut.OpenPostConnection(url, params[0], EmployeeAdvanceRequestActivity.this);
                response = res.toString();
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (res != null) {
                Toast.makeText(EmployeeAdvanceRequestActivity.this, "Advance Vouchar Saved & send for Approval Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EmployeeAdvanceRequestActivity.this, ActivityMain.class));
                finish();
            } else {
                Toast.makeText(EmployeeAdvanceRequestActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

            }


        }

    }

    class GetOutstandingAmt extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showprogress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetOutstandingAmt + "?SelectedValue=" + UserMasterId;
                res = ut.OpenConnection(url, EmployeeAdvanceRequestActivity.this);
                if (res != null) {
                    response = res.toString();
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();

            try {
                JSONArray jResults = null;
                jResults = new JSONArray(integer);
                for (int i = 0; i < jResults.length(); i++) {
                    if (jResults.length() > 0) {
                        JSONObject jSection = jResults.getJSONObject(i);
                        OutstandingAmt = jSection.getString("OutstandingAmt");
                        String OutstandingAmt1 = OutstandingAmt.split("\\.", 2)[0];

                        txt_out_amount.setText(OutstandingAmt1 + "." + "00");
                        OutstandingSince = jSection.getString("OutstandingSince");
                        txt_out_date.setText(OutstandingSince);
                        LastClaimAmt = jSection.getString("LastClaimAmt");
                        txt_last_amount.setText(LastClaimAmt);
                        LastClaimDt = jSection.getString("LastClaimDt");
                        txt_last_date.setText(LastClaimDt);
                        len_amount.setVisibility(View.VISIBLE);

                        //  DocApprMthdId2 = jSection.getString("DocApprMthdId");


                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

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

        if (id == R.id.refresh1) {

            if (ut.isNet(getApplicationContext())) {

                new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetUsername().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {

                Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            if (ut.isNet(getApplicationContext())) {

                new StartSession(EmployeeAdvanceRequestActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetDochdrId().execute();
                        // new GetClaimApprover().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }else{
                Toast.makeText(EmployeeAdvanceRequestActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
            return true;
        } else if (id == android.R.id.home) {

            onBackPressed();
            return true;
        } else {
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
