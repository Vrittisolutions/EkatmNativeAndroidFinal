package com.vritti.crmlib.vcrm7;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AutocompleteCustomArrayAdapter;
import com.vritti.crmlib.bean.CityBean;
import com.vritti.crmlib.classes.CustomAutoCompleteTextChangedListener;
import com.vritti.crmlib.classes.CustomAutoCompleteView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class TravelPlanAddActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    private EditText editTextDate, editTextNotes;

    private Button buttonSave_travelplan, buttonClose_travelplan;
    public static CustomAutoCompleteView myAutoComplete;
    public static Context context;
    public static String  date = null, CurrentDate;
    String CityId, CityName, finaljson;
    static int year, month, day;
    SimpleDateFormat dfDate;
    ProgressHUD progressHUD;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    List city;
    public static ArrayAdapter<CityBean> myAdapter;

    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_travel_plan);
        context = TravelPlanAddActivity.this;
        init();


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

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        //UserType = userpreferences.getString("UserType", null);
        dfDate = new SimpleDateFormat("dd/MM/yyy");
        CurrentDate = dfDate.format(new Date());
        editTextDate.setText(CurrentDate);

        if (cf.getCitycount() > 0) {
            getCity();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCityJSON().execute();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);
        editTextDate = (EditText) findViewById(R.id.editTextDate);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        buttonSave_travelplan = (Button) findViewById(R.id.buttonSave_travelplan);
        buttonClose_travelplan = (Button) findViewById(R.id.buttonClose_travelplan);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((editTextDate.getText().toString().equalsIgnoreCase("") ||
                editTextDate.getText().toString().equalsIgnoreCase(" ") ||
                editTextDate.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Select date", Toast.LENGTH_LONG).show();
            return false;
        } else if ((myAutoComplete.getText().toString().equalsIgnoreCase("") ||
                myAutoComplete.getText().toString().equalsIgnoreCase(" ") ||
                myAutoComplete.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter city", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editTextNotes.getText().toString().equalsIgnoreCase("") ||
                editTextNotes.getText().toString().equalsIgnoreCase(" ") ||
                editTextNotes.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter notes", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setListener() {
        buttonClose_travelplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        buttonSave_travelplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (validate()) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("Mode", "A");
                        jsonObject.put("CityId ", CityId);
                        jsonObject.put("CityName", CityName);
                        String notes = editTextNotes.getText().toString();
                        jsonObject.put("Notes", URLEncoder.encode(notes, "UTF-8"));//
                        jsonObject.put("TravelDate", editTextDate.getText().toString());
                        jsonObject.put("PKTravelPlanId", "");
                        finaljson = jsonObject.toString();
                        finaljson = finaljson.replaceAll("\\\\", "");
                        finaljson = finaljson.replaceAll(" ", "");


                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostTravelPlanJSON().execute(finaljson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }


                } catch (JSONException e) {

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                //    datePicker.setMinDate(c.getTimeInMillis());
                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                editTextDate.setText(date);
                               /* if (compare_date(date) == true) {
                                    editTextDate.setText(date);
                                } else {
                                    editTextReceiveddate.setText(date);
                                    Toast.makeText(OpportunityUpdateActivity.this,
                                            "You cannot select a day earlier than today!",
                                            Toast.LENGTH_SHORT).show();
                                }*/


                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();

            }
        });

        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {


                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                myAutoComplete.setText(tv.getText().toString());
                String que = "SELECT *  FROM " + db.TABLE_CITY +
                        " WHERE CityName='" + myAutoComplete.getText().toString() + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    CityId = cur.getString(cur.getColumnIndex("PKCityID"));
                    CityName = cur.getString(cur.getColumnIndex("CityName"));
                }


            }

        });
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

    private void showProgressDialog() {


        // progressHUD = ProgressHUD.show(context, "", false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }

    private void getCity() {

        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this));
        CityBean myObject = null;
        CityBean[] ObjectItemData = new CityBean[1];
        myObject = new CityBean();
        ObjectItemData[0] = myObject;
        myAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.crm_list_view_row_item, ObjectItemData);
        myAutoComplete.setAdapter(myAdapter);
        int textLength1 = myAutoComplete.getText().length();
        myAutoComplete.setSelection(textLength1, textLength1);
        myAutoComplete.setFocusable(true);


    }

    class DownloadCityJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_city
                        + "?PKCityID=" + URLEncoder.encode("", "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                sql.delete(db.TABLE_CITY, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CITY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        String jsonAddeddt = jorder.getString("AddedDt");
                        String jsonModifiedDt = jorder.getString("ModifiedDt");
                        if (columnName.equalsIgnoreCase("AddedDt")) {
                            jsonAddeddt = jsonAddeddt.substring(jsonAddeddt.indexOf("(") + 1, jsonAddeddt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonAddeddt);
                            DOBDate = new Date(DOB_date);
                            jsonAddeddt = sdf.format(DOBDate);
                            values.put(columnName, jsonAddeddt);

                        } else if (columnName.equalsIgnoreCase("ModifiedDt")) {
                            jsonModifiedDt = jsonModifiedDt.substring(jsonModifiedDt.indexOf("(")
                                    + 1, jsonModifiedDt.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonModifiedDt);
                            DOBDate = new Date(DOB_date);
                            jsonModifiedDt = sdf.format(DOBDate);
                            values.put(columnName, jsonModifiedDt);

                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_CITY, null, values);

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
            if (response.contains("")) {

                //   getInitiatedby();
                getCity();
            }

        }

    }

    class PostTravelPlanJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_post_travel_plan;
            try {
                res = ut.OpenPostConnection(url, params[0]);
                response = res.toString().replaceAll("\\\\", "");
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
            dismissProgressDialog();

            if (response != null) {
                Toast.makeText(context, "Travel plan added succcessfully on " + response, Toast.LENGTH_LONG).show();
            }
            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(context, CallListActivity.class);
        startActivity(intent);*/
        // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        TravelPlanAddActivity.this.finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}