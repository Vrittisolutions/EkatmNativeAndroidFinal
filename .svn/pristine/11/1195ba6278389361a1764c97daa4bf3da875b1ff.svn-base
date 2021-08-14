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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class TravelPlanShowFormActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;

    AutoCompleteTextView AutocompExecutive;
    EditText editTextfrom, editTextto;
    CheckBox chkdate, chkExecutive;
    Button btnshow, btncancel;
    public static Context context;
    ProgressHUD progressHUD;
    public static String date = null, CurrentDate;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
    Date DOBDate = null;
    static int year, month, day;
    Date result;
    boolean chkdt = false, chkexe = true;
    List category;
    String AssignToUserId = "";
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_travel_plan_show_form);
        init();

        context = TravelPlanShowFormActivity.this;

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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();


        if (cf.getExecutivecount() > 0) {
            getCategory();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }

        setListener();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_action);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_crm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AutocompExecutive = (AutoCompleteTextView) findViewById(R.id.AutocompExecutive);
        editTextfrom = (EditText) findViewById(R.id.editTextfrom);
        editTextto = (EditText) findViewById(R.id.editTextto);
        btnshow = (Button) findViewById(R.id.btnshow);
        btncancel = (Button) findViewById(R.id.btncancel);
        chkdate = (CheckBox) findViewById(R.id.chkdate);
        chkExecutive = (CheckBox) findViewById(R.id.chkdate);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if (chkdt == true) {
            if ((editTextfrom.getText().toString().equalsIgnoreCase("") ||
                    editTextfrom.getText().toString().equalsIgnoreCase(" ") ||
                    editTextfrom.getText().toString().equalsIgnoreCase(null))) {

                //  Toast.makeText(context, "Select from date", Toast.LENGTH_LONG).show();
                return false;
            } else if ((editTextto.getText().toString().equalsIgnoreCase("") ||
                    editTextto.getText().toString().equalsIgnoreCase(" ") ||
                    editTextto.getText().toString().equalsIgnoreCase(null))) {

                //   Toast.makeText(context, "Select to date", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    private void setListener() {
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        chkExecutive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chkexe = true;

                } else {
                    chkexe = false;
                }
            }
        });
        chkdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    chkdt = true;
                } else {
                    chkdt = false;
                }
            }
        });

        btnshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (validate() == true && chkexe == true) {

                    Intent intent = new Intent(context, TravelPlanShowActivity.class);
                    if (validate() == true) {
                        intent.putExtra("from", editTextfrom.getText().toString());
                        intent.putExtra("to", editTextto.getText().toString());
                    } else {
                        intent.putExtra("from", "");
                        intent.putExtra("to", "");
                    }
                    if (chkexe == true) {

                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Executive + " WHERE " +
                                " UserName='" + AutocompExecutive.getText().toString() + "'";
                        Cursor cur = sql.rawQuery(query, null);

                        if (cur.getCount() > 0) {

                            cur.moveToFirst();

                            AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                            intent.putExtra("userid", AssignToUserId);
                            intent.putExtra("username", AutocompExecutive.getText().toString());
                        } else {
                            intent.putExtra("userid", UserMasterId);
                            intent.putExtra("username", UserName);
                        }

                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                } else {

                    Intent intent = new Intent(context, TravelPlanShowActivity.class);
                    String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                            " FROM " + db.TABLE_Executive + " WHERE " +
                            " UserName='" + AutocompExecutive.getText().toString() + "'";
                    Cursor cur = sql.rawQuery(query, null);

                    if (cur.getCount() > 0) {

                        cur.moveToFirst();

                        AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                        intent.putExtra("userid", AssignToUserId);
                        intent.putExtra("username", AutocompExecutive.getText().toString());
                    } else {
                        intent.putExtra("userid", UserMasterId);
                        intent.putExtra("username", UserName);
                    }
                    intent.putExtra("from", "");
                    intent.putExtra("to", "");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


            }
        });


        editTextfrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(c.getTimeInMillis() - 10000);
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextfrom.setText(date);


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });
        editTextto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                date = "";
                result = c.getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //  datePicker.setMinDate(result.getTime());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                //  editTextto.setText(date);
                                if (editTextfrom.getText().toString().trim().length() > 0) {
                                    if (compare_date(editTextfrom.getText().toString(), date) == true) {
                                        editTextto.setText(date);
                                        btnshow.setVisibility(View.VISIBLE);
                                    } else {
                                        btnshow.setVisibility(View.GONE);
                                        //  editTextto.setText(date);
                                        Toast.makeText(TravelPlanShowFormActivity.this,
                                                "You cannot select a day earlier than from date!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(TravelPlanShowFormActivity.this,
                                            "Select from date ",
                                            Toast.LENGTH_LONG).show();
                                }


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });

        AutocompExecutive.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {


                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    if (validate() == true && chkexe == true) {

                        Intent intent = new Intent(context, TravelPlanShowActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if (validate() == true) {
                            intent.putExtra("from", editTextfrom.getText().toString());
                            intent.putExtra("to", editTextto.getText().toString());
                        } else {
                            intent.putExtra("from", "");
                            intent.putExtra("to", "");
                        }
                        if (chkexe == true) {

                            String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                    " FROM " + db.TABLE_Executive + " WHERE " +
                                    " UserName='" + AutocompExecutive.getText().toString() + "'";
                            Cursor cur = sql.rawQuery(query, null);

                            if (cur.getCount() > 0) {

                                cur.moveToFirst();

                                AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                                intent.putExtra("userid", AssignToUserId);
                                intent.putExtra("username", AutocompExecutive.getText().toString());
                            } else {
                                intent.putExtra("userid", UserMasterId);
                                intent.putExtra("username", UserName);
                            }

                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        //TravelPlanShowFormActivity.this.finish();
                    } else {

                        Intent intent = new Intent(context, TravelPlanShowActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                                " FROM " + db.TABLE_Executive + " WHERE " +
                                " UserName='" + AutocompExecutive.getText().toString() + "'";
                        Cursor cur = sql.rawQuery(query, null);

                        if (cur.getCount() > 0) {

                            cur.moveToFirst();

                            AssignToUserId = cur.getString(cur.getColumnIndex("UserMasterId"));
                            intent.putExtra("userid", AssignToUserId);
                            intent.putExtra("username", AutocompExecutive.getText().toString());
                        } else {
                            intent.putExtra("userid", UserMasterId);
                            intent.putExtra("username", UserName);
                        }
                        intent.putExtra("from", "");
                        intent.putExtra("to", "");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // TravelPlanShowFormActivity.this.finish();
                    }

                }
                return false;
            }
        });


    }                                                   //todate

    public static boolean compare_date(String fromdate, String dt) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");
        Date d1, d2;
        String s1;

        // str = outputFormat.format(date);
        //   String today;
        //  today = dfDate.format(new Date());
        try {
            d1 = dfDate.parse(fromdate);
            d2 = dfDate.parse(dt);
            if ((dfDate.parse(dt).after(dfDate.parse(fromdate)) ||
                    dfDate.parse(dt).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                // date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
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

    private void showProgressDialog(String txt) {


        // progressHUD = ProgressHUD.show(context, "" + txt, false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        /*if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }*/
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }


    }

    class DownloadCategoryJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            // progressHUD7 = ProgressHUD.show(ReassignCallsActivity.this, " ", false, false, null);
            showProgressDialog("");
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Category;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_Executive, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Executive, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_Executive, null, values);

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

            if (response != null) {

            }

            getCategory();
        }


    }

    private void getCategory() {

        category = new ArrayList();
        String query = "SELECT distinct UserMasterId,UserName,CRMCategory" +
                " FROM " + db.TABLE_Executive + " WHERE CRMCategory='1' OR CRMCategory='2'";
        Cursor cur = sql.rawQuery(query, null);
        category.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {

                category.add(cur.getString(cur.getColumnIndex("UserName")));

            } while (cur.moveToNext());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (context, android.R.layout.simple_spinner_item, category);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutocompExecutive.setAdapter(dataAdapter);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(context, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        TravelPlanShowFormActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(context, TravelPlanAddActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}
