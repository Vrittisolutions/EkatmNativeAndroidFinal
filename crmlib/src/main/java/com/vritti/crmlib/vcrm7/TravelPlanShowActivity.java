package com.vritti.crmlib.vcrm7;

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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.TravelPlan;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class TravelPlanShowActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    TextView textViewcity, textViewdate, textViewnotes, txtname;
    TextView spinneraction;
    String fromdt, todt, Userid, User_name;
    LinearLayout lay_travelplan, lay1;
    // Button btnshow;
    ProgressHUD progressHUD;
    public static String date = null, CurrentDate;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    ArrayList<TravelPlan> travelPlanArrayList;
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd  hh:mm a");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
    Date DOBDate = null;
    static int year, month, day;
    Date result;
    String deleteresponse;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_travel_plan_show);
        init();

        context = TravelPlanShowActivity.this;

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

        Intent intent = getIntent();
        deleteresponse = intent.getStringExtra("response");

        System.out.println("DeleteResponse :" + deleteresponse);
        if (intent.hasExtra("message")) {

            Userid = intent.getStringExtra("userid");
            fromdt = intent.getStringExtra("from");
            todt = intent.getStringExtra("to");

           /* if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadTravelPlan().execute("", "", UserMasterId);
                    }
                });

            }*/
            txtname.setText("Travel plan of " + UserName);
        } else {

            Userid = intent.getStringExtra("userid");
            fromdt = intent.getStringExtra("from");
            todt = intent.getStringExtra("to");
            User_name = intent.getStringExtra("username");
            txtname.setText("Travel plan of " + User_name);
        }

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadTravelPlan().execute(fromdt, todt, Userid);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_action);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.mipmap.ic_crm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        travelPlanArrayList = new ArrayList<TravelPlan>();
        lay_travelplan = (LinearLayout) findViewById(R.id.lay_travelplan);
        txtname = (TextView) findViewById(R.id.txtname);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
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

        progressbar.setVisibility(View.VISIBLE);
        // progressHUD = ProgressHUD.show(context, "", false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            //progressHUD.dismiss();
            progressbar.setVisibility(View.GONE);
        }
    }

    class DownloadTravelPlan extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showProgressDialog();

        }


        @Override
        protected String doInBackground(String... params) {
            //FromDate: '01/01/2015', ToDate: '31/05/2015'


            try {
                String url = CompanyURL + WebUrlClass.api_TRAVEL_PLAN
                        + "?UserId=" + URLEncoder.encode(params[2], "UTF-8") +
                        "&FromDate=" + URLEncoder.encode(params[0], "UTF-8")
                        + "&ToDate=" + URLEncoder.encode(params[1], "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);


                sql.delete(db.TABLE_Travelplan, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Travelplan, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);

                        String jsonDOB = jorder.getString("Date");

                        if (columnName.equalsIgnoreCase("Date")) {
                            jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(")
                                    + 1, jsonDOB.lastIndexOf(")"));
                            long DOB_date = Long.parseLong(jsonDOB);
                            DOBDate = new Date(DOB_date);
                            jsonDOB = sdf1.format(DOBDate);

                            String k = jsonDOB.substring(0, jsonDOB.length() - 9);
                            values.put(columnName, k);


                        } else if (columnName.equalsIgnoreCase("Notes")) {
                            columnValue = jorder.getString(columnName);
                            columnValue = URLDecoder.decode(columnValue, "UTF-8");
                            values.put(columnName, columnValue);
                        } else {
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }


                    }

                    long a = sql.insert(db.TABLE_Travelplan, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgressDialog();
            if (s.contains("error")) {
                Toast.makeText(TravelPlanShowActivity.this, "Someting went wrong", Toast.LENGTH_SHORT).show();

            } else {
                displayTravelplan();
            }
        }

    }

    private void displayTravelplan() {
        try {
            travelPlanArrayList.clear();
            lay_travelplan.removeAllViews();
            Date d1;
            String query = "SELECT  PKTravelPlanId,Date,Notes,CityName" +
                    " FROM " + db.TABLE_Travelplan + " ORDER BY Date desc";

            Cursor cur = sql.rawQuery(query, null);

            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    TravelPlan travelPlan = new TravelPlan();
                    travelPlan.setPKTravelPlanId(cur.getString(cur.getColumnIndex("PKTravelPlanId")));
                    d1 = sdf2.parse(cur.getString(cur.getColumnIndex("Date")));


                    travelPlan.setDate(sdf.format(d1));
                    travelPlan.setNotes(cur.getString(cur.getColumnIndex("Notes")));
                    travelPlan.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                    travelPlanArrayList.add(travelPlan);
                } while (cur.moveToNext());
            }

            if (travelPlanArrayList.size() > 0) {
                for (int i = 0; i < travelPlanArrayList.size(); i++) {
                    addView_Travellist(i);
                }
            } else {
                TravelPlan travelPlan = new TravelPlan();
                travelPlan.setPKTravelPlanId("");
                travelPlan.setDate("");
                travelPlan.setNotes("No data available");
                travelPlan.setCityName("");
                travelPlanArrayList.add(travelPlan);
                for (int i = 0; i < travelPlanArrayList.size(); i++) {
                    addView_Travellist(i);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void addView_Travellist(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int pos = i;
        final View convertView = layoutInflater.inflate(R.layout.crm_custom_travel_plan_show,
                null);
        textViewcity = (TextView) convertView.findViewById(R.id.textViewcity);
        textViewdate = (TextView) convertView.findViewById(R.id.textViewdate);
        textViewnotes = (TextView) convertView.findViewById(R.id.textViewnotes);
        spinneraction = (TextView) convertView.findViewById(R.id.spinneraction);
        lay1 = (LinearLayout) convertView.findViewById(R.id.lay1);
        //  spinneraction.setSelection(0);No data available

        if (travelPlanArrayList.get(i).getNotes().equalsIgnoreCase("No data available")) {
            textViewnotes.setText(travelPlanArrayList.get(i).getNotes());
          /*  textViewcity.setVisibility(View.GONE);
            textViewdate.setVisibility(View.GONE);*/
            spinneraction.setVisibility(View.GONE);
            lay1.setVisibility(View.GONE);
        } else {
            textViewcity.setText(travelPlanArrayList.get(i).getCityName());

            String dt = travelPlanArrayList.get(i).getDate();
            System.out.println("DateBefore  :" + dt);


            String date_after = formateDateFromstring("MM/dd/yyyy", "dd MMM yyyy", dt);

           /* if (dt != null) {
                dt = dt.substring(dt.indexOf("(") + 1, dt.lastIndexOf(")"));
                long DOB_date = Long.parseLong(dt);
                DOBDate = new Date(DOB_date);
                dt = sdf.format(DOBDate);
                String k = dt.substring(0, dt.length() - 9);

            } else {
                textViewdate.setText("");
            }*/
            textViewdate.setText(date_after);
            textViewnotes.setText(travelPlanArrayList.get(i).getNotes());
            spinneraction.setTag(lay_travelplan.getChildCount());
            spinneraction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TravelPlanDeleteActivity.class);
                    intent.putExtra("PKTravelPlanId", travelPlanArrayList.get(pos).getPKTravelPlanId());
                    intent.putExtra("Date", travelPlanArrayList.get(pos).getDate());
                    intent.putExtra("Notes", travelPlanArrayList.get(pos).getNotes());
                    intent.putExtra("City", travelPlanArrayList.get(pos).getCityName());
                    intent.putExtra("UserId", Userid);
                    startActivity(intent);
                    TravelPlanShowActivity.this.finish();
                }
            });
        }

      /*  spinneraction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = spinneraction.getSelectedItem().toString();
                int a = spinneraction.getSelectedItemPosition();





                if (selected_item.equalsIgnoreCase("Delete")) {
                    Intent intent = new Intent(context, TravelPlanDeleteActivity.class);
                    intent.putExtra("PKTravelPlanId", travelPlanArrayList.get(pos).getPKTravelPlanId());
                    intent.putExtra("Date", travelPlanArrayList.get(pos).getDate());
                    intent.putExtra("Notes", travelPlanArrayList.get(pos).getNotes());
                    intent.putExtra("City", travelPlanArrayList.get(pos).getCityName());
                    startActivity(intent);
                    TravelPlanShowActivity.this.finish();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/
        lay_travelplan.addView(convertView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(context, TravelPlanShowFormActivity.class);
        startActivity(intent);*/
        //  overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        TravelPlanShowActivity.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismissProgressDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
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

