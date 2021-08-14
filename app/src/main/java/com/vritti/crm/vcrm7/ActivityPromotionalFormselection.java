package com.vritti.crm.vcrm7;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;


public class ActivityPromotionalFormselection extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    ListView ls_birthday;

    SQLiteDatabase sql;
    ProgressBar mProgress;
    SearchableSpinner mSpinner;
    SharedPreferences userpreferences;

    TextView mTxtdate,mTxtmydate;
    Button mBtnProceed,mBtnmyproceed;
    private String PromoeterLogInId = "", PromoterName = "";

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_promotional_formselection);
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
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        InitView();
        if (CheckList()) {
            UpdatePromoterList();
        } else {
            Refresh();
        }
    }

    private void Refresh() {
        if (isnet()) {
            ShowProgress();
            new StartSession(ActivityPromotionalFormselection.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetPromoterList().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    HideProgres();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }
            });
        }
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // toolbar.setLogo(R.drawable.vwb_logo);
        //toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

    //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        /*img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
*/
        txt_title.setText("Promotor List");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        mSpinner = (SearchableSpinner) findViewById(R.id.spinner);
        mSpinner.setTitle("Select Promoter");
        mTxtdate = (TextView) findViewById(R.id.txtselectdate);
        mBtnProceed = (Button) findViewById(R.id.btnproceed);
        mTxtmydate = (TextView) findViewById(R.id.txtmyselectdate);
        mBtnmyproceed = (Button) findViewById(R.id.btnmyproceed);


        mTxtdate.setOnClickListener(new View.OnClickListener() {
            int year, month, day;
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityPromotionalFormselection.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // datePicker.setMinDate(c.getTimeInMillis());
                                //   Calendar cal = Calendar.getInstance();
                                //   datePicker.setMaxDate(cal.getTimeInMillis());
                                String date = String.format("%02d", (year)) + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
                                        + dayOfMonth;
                                mTxtdate.setText(date);

                            }
                        }, year, month, day);
                // c.add(Calendar.DATE, -a);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    // datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }


        });

        mTxtmydate.setOnClickListener(new View.OnClickListener() {
            int year, month, day;
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityPromotionalFormselection.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                // datePicker.setMinDate(c.getTimeInMillis());
                                //   Calendar cal = Calendar.getInstance();
                                //   datePicker.setMaxDate(cal.getTimeInMillis());
                                String date = String.format("%02d", (year)) + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
                                        + dayOfMonth;
                                mTxtmydate.setText(date);

                            }
                        }, year, month, day);
                // c.add(Calendar.DATE, -a);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    // datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                }
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }


        });
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PromoterName = (String) mSpinner.getSelectedItem();
                String query = "SELECT *" +
                        " FROM " + db.TABLE_PROMOTER +
                        " WHERE UserName='" + PromoterName + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        PromoeterLogInId = cur.getString(cur.getColumnIndex("UsermasterId"));

                    } while (cur.moveToNext());

                } else {
                    PromoeterLogInId = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBtnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate()) {
                    String date = mTxtdate.getText().toString().trim();
                    Intent intent = new Intent(getApplicationContext(), ActivityPromotionalFormRecord.class);
                    intent.putExtra("PromoterLiginId", PromoeterLogInId);
                    intent.putExtra("Date", date);
                    startActivity(intent);

                }

            }
        });

        mBtnmyproceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validate1()) {
                    String date = mTxtmydate.getText().toString().trim();
                    Intent intent = new Intent(getApplicationContext(), ActivityPromotionalFormRecord.class);
                    intent.putExtra("PromoterLiginId", UserMasterId);
                    intent.putExtra("Date", date);
                    startActivity(intent);

                }

            }
        });
    }

    private Boolean Validate() {
        if (PromoterName.equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Select Promoter", Toast.LENGTH_LONG).show();
            return false;
        } else if ((mTxtdate.getText().toString()).equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }
    private Boolean Validate1() {
        if ((mTxtmydate.getText().toString()).equalsIgnoreCase("")) {
            Toast.makeText(getApplicationContext(), "Please select date", Toast.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }

    private Boolean CheckList() {
        String query = "SELECT * FROM " + db.TABLE_PROMOTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;

        } else {
            return false;
        }

    }

    private void UpdatePromoterList() {
        ArrayList<String> mData = new ArrayList<String>();
        mData.clear();

        String query = "SELECT * FROM " + db.TABLE_PROMOTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                mData.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> customDept = new ArrayAdapter<String>(ActivityPromotionalFormselection.this,
                R.layout.crm_custom_spinner_txt, mData);
        mSpinner.setAdapter(customDept);//SF0006_ADATSOFT
    }


    class GetPromoterList extends AsyncTask<String, Void, String> {
        String response;
        Object res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            HideProgres();
            if (integer.contains("UsermasterId")) {

            }else if(integer.contains("error"))  {
                Toast.makeText(getApplicationContext(),"Error occured while fetching promoter list",Toast.LENGTH_LONG).show();
            }
            UpdatePromoterList();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetPromoterList2;
                res = ut.OpenConnection(url, getApplicationContext());

                Log.e("response data", res + "");
                response = res.toString().replaceAll("\\\\", "");
                //  response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_PROMOTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PROMOTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);

                        if(columnName.equalsIgnoreCase("UserLoginId")){
                            columnValue = jorder.getString("ReportingTo");

                        } else if(columnName.equalsIgnoreCase("UsermasterId")){
                            columnValue = jorder.getString("UserMasterId");

                        }else {
                            columnValue = jorder.getString(columnName);

                        }
                        values.put(columnName, columnValue);

                    }
                    long a = sql.insert(db.TABLE_PROMOTER, null, values);
                    Log.e("promoter Table", "" + a);

                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityPromotionalFormselection.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        int id = menuItem.getItemId();
        if (id == R.id.refresh) {
            Refresh();
        }
        return (super.onOptionsItemSelected(menuItem));
    }*/

    public void ShowProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    public void HideProgres() {
        mProgress.setVisibility(View.GONE);

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
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
