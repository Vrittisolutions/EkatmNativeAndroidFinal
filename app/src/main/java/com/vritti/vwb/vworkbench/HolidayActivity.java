package com.vritti.vwb.vworkbench;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.HolidayAdapter;
import com.vritti.vwb.Beans.Holiday;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sharvari on 02-Apr-19.
 */

public class HolidayActivity extends AppCompatActivity {

      ListView list_holiday;
    HolidayAdapter holidayAdapter;
    ArrayList<Holiday> holidayArrayList;

    SharedPreferences userpreferences;
    String CompanyURL;
    ProgressBar mprogress;
    SharedPreferences sharedPrefs;
    Gson gson;
    String json;
    Type type;
    private boolean ascending = true;
    Utility ut;;
    DatabaseHandlers db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holiday_lay_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(HolidayActivity.this);
        String dabasename = ut.getValue(HolidayActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(HolidayActivity.this, dabasename);

        CompanyURL = ut.getValue(HolidayActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);






        list_holiday= findViewById(R.id.list_holiday);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);
        holidayArrayList=new ArrayList<>();
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HolidayActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("holiday", "");
        type = new TypeToken<List<Holiday>>() {
        }.getType();
        holidayArrayList = gson.fromJson(json, type);

        if (holidayArrayList == null) {
            if (ut.isNet(HolidayActivity.this)) {
                showProgress();
                new StartSession(HolidayActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadHolidayData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (holidayArrayList.size() > 0) {
                holidayAdapter = new HolidayAdapter(HolidayActivity.this, holidayArrayList);
                //list_contractor.setAdapter(new ArrayAdapter(ContractorListActivity.this,android.R.layout.simple_list_item_1,contractorArrayList));
                list_holiday.setAdapter(holidayAdapter);
            }

        }

    }
    class DownloadHolidayData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute
                    ();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_getHolidaylistforandroid;

            try {
                res = ut.OpenConnection(url,HolidayActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    holidayArrayList=new ArrayList<>();
                    holidayArrayList.clear();

                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        Holiday holiday = new Holiday();
                        JSONObject jorder = jResults.getJSONObject(i);

                        holiday.setDayName(jorder.getString("DayName"));
                        holiday.setHolidayReason(jorder.getString("HolidayReason"));
                        holiday.setShortHolidayDate(jorder.getString("ShortHolidayDate"));
                        holidayArrayList.add(holiday);
                    }
                    //Collections.sort(contractorArrayList);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                Toast.makeText(HolidayActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(HolidayActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(holidayArrayList);
                editor.putString("holiday", json);
                editor.commit();
                holidayAdapter=new HolidayAdapter(HolidayActivity.this,holidayArrayList);
                list_holiday.setAdapter(holidayAdapter);



            }


        }
    }


    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        }
        switch (item.getItemId()) {
            case R.id.refresh:

                if (ut.isNet(HolidayActivity.this)) {
                    showProgress();
                    new StartSession(HolidayActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadHolidayData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
