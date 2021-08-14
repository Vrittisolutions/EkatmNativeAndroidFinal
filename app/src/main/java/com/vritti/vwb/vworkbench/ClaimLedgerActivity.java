package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.ClaimLedgerAdapter;
import com.vritti.crm.bean.Ledger;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.vritti.vwb.vworkbench.AttendanceDisplayActivity.formateDateFromstring;

public class ClaimLedgerActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    private String settingKey, dabasename;
    public String IsChatApplicable;
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    Toolbar toolbar;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    String UserType;
    RecyclerView callhistory_listview;
    ProgressBar center_progress;
    ArrayList<Ledger> ledgerArrayList;
    ClaimLedgerAdapter claimLedgerAdapter;
    TextView txt_firm_title;
    String callid = "", firmname = "", calltype = "", SourceId = "", FormId = "", callstatu = "", table = "", ProspectId = "";
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    TextView txt_fromdate,txt_todate,txt_submit;
    ImageView img_from_date,img_to_date;
    private String FromDate,Todate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_list_lay);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        sql = db.getWritableDatabase();

        InitView();



        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Ledger");

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.add(Calendar.DATE, -26);
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

        if (ut.isNet(ClaimLedgerActivity.this)) {
            center_progress.setVisibility(View.VISIBLE);
            new StartSession(ClaimLedgerActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadLedgerData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_from_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ClaimLedgerActivity.this,
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(ClaimLedgerActivity.this,
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
                    new StartSession(ClaimLedgerActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadLedgerData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                }


            }
        });



    }

    private void InitView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        callhistory_listview = findViewById(R.id.report_list);
        center_progress = findViewById(R.id.progressbar_1);
        txt_firm_title = findViewById(R.id.txt_firm_title);
        ledgerArrayList = new ArrayList<>();
        txt_fromdate=findViewById(R.id.txt_fromdate);
        txt_todate=findViewById(R.id.txt_todate);
        txt_submit=findViewById(R.id.txt_submit);
        img_from_date=findViewById(R.id.img_from_date);
        img_to_date=findViewById(R.id.img_to_date);
    }

    class DownloadLedgerData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            center_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            ledgerArrayList.clear();
            try {
                String url =CompanyURL+WebUrlClass.fetchClaimLedger+"?startDate="+FromDate+"&endDate="+Todate;

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    JSONArray jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jcallhistory = jResults.getJSONObject(i);

                        Ledger ledger = new Ledger();
                        ledger.setCredit(jcallhistory.getString("Credit"));
                        ledger.setDebit(jcallhistory.getString("Debit"));
                        ledger.setEffectiveDate(jcallhistory.getString("EffectiveDate"));
                        ledger.setTransNarrative(jcallhistory.getString("TransNarrative"));
                        ledger.setTransNo(jcallhistory.getString("TransNo"));
                        ledgerArrayList.add(ledger);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            center_progress.setVisibility(View.GONE);
            if (response.equals("[]")) {
                Toast.makeText(ClaimLedgerActivity.this, "Record not found", Toast.LENGTH_SHORT).show();


            } else {
                if (response != null) {
                    claimLedgerAdapter = new ClaimLedgerAdapter(ClaimLedgerActivity.this, ledgerArrayList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    callhistory_listview.setLayoutManager(mLayoutManager);
                    callhistory_listview.setItemAnimator(new DefaultItemAnimator());
                    callhistory_listview.setAdapter(claimLedgerAdapter);

                }

            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

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

}
