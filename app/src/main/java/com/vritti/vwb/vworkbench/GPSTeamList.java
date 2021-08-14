package com.vritti.vwb.vworkbench;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.GPSReportingToAdapter;
import com.vritti.vwb.Beans.MyTeamBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.ActivityGPSLocalData;
import com.vritti.vwb.vworkbench.GPSMyOwnLocation;
import com.vritti.vwb.vworkbench.GoogleMapTeamActivity;


public class GPSTeamList extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView reportingtolist;

    String sop = "no";
    String mobno, link;
    private Context parent;
    private View rootView;
    SharedPreferences userpreferences;
    TextView txt_no_record;
    ArrayList<MyTeamBean> lsMyteam;
    DownloadTeamDataJSON teamDataJSON;
    ImageView owngps, view_map;
    ProgressBar mProgress;
    private boolean asc=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_gps_location_reportinglist);
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
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        initialize();
        setListner();

    }

    public void setListner() {
        reportingtolist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg1, View view,
                                    int position, long id) {

                String teamuserName = lsMyteam.get(position).getUserName();
                String teamusermasterID = lsMyteam.get(position).getUserMasterId();
                Intent myIntent = new Intent(GPSTeamList.this, GPSMyOwnLocation.class);
                myIntent.putExtra("usermaterId", teamusermasterID);//usernamegps
                myIntent.putExtra("usernamegps", teamuserName);
                startActivity(myIntent);
            }
        });
        owngps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(UserMasterId == null)) {
                    Intent myIntent = new Intent(GPSTeamList.this, GPSMyOwnLocation.class);
                    myIntent.putExtra("usermaterId", UserMasterId);//usernamegps
                    myIntent.putExtra("usernamegps", UserName);
                    startActivity(myIntent);
                } else {
                    ut.displayToast(getApplicationContext(), "Cann't Identify User");
                }
            }
        });


        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(GPSTeamList.this,GoogleMapTeamActivity.class);
                intent.putExtra("bundle",lsMyteam);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    private void initialize() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_teamlist);
        reportingtolist = (ListView) findViewById(R.id.gpsreporting);
        owngps = (ImageView) findViewById(R.id.viewowndps);
        view_map = (ImageView) findViewById(R.id.view_map);
        txt_no_record = (TextView) findViewById(R.id.txt_no_record);
        lsMyteam = new ArrayList<MyTeamBean>();

        refreshData();

//        if (cf.check_gps_reportingto() > 0) {
//            updatelist();
//        } else {
//            refreshData();
//        }
    }

    public void refreshData() {
        if (isInternetAvailable(GPSTeamList.this)) {
            showProgressDialog();
            new StartSession(GPSTeamList.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    teamDataJSON = new DownloadTeamDataJSON();
                    teamDataJSON.execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                }
            });
        } else {
            ut.displayToast(getApplicationContext(), "No Internet Connection");
        }
    }

  /*  private void updatelist() {
        lsMyteam.clear();
        String que = "SELECT * FROM " + db.TABLE_MYTEAM;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            txt_no_record.setVisibility(View.GONE);
            cur.moveToFirst();
            do {
                MyTeamBean bean = new MyTeamBean();
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setTotalOverdueActivities(cur.getString(cur.getColumnIndex("TotalOverdueActivities")));
                bean.setTotalCount(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                lsMyteam.add(bean);
            } while (cur.moveToNext());
            reportingtolist.setAdapter(new GPSReportingToAdapter(GPSTeamList.this,
                    lsMyteam));
        } else {
            txt_no_record.setVisibility(View.VISIBLE);
        }
        sql.close();
    }*/


    private void updatelist() {
        lsMyteam.clear();
        String que = "SELECT * FROM " + db.TABLE_MYTEAM;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cur = sql.rawQuery(que, null);
        if (cur.getCount() > 0) {
            txt_no_record.setVisibility(View.GONE);
            cur.moveToFirst();
            do {
                MyTeamBean bean = new MyTeamBean();
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setTotalOverdueActivities(cur.getString(cur.getColumnIndex("TotalOverdueActivities")));
                bean.setTotalCount(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                String lat = cur.getString(cur.getColumnIndex("Latitude"));
                if (lat.equals("")) {
                    bean.setLatitude("0");

                } else {
                    bean.setLatitude(lat);
                }
                String lng = cur.getString(cur.getColumnIndex("Longitude"));
                if (lng.equals("")) {
                    bean.setLongitude("0");

                } else {
                    bean.setLongitude(lng);
                }
                String Locationname = cur.getString(cur.getColumnIndex("LocationName"));
                if (Locationname.equals("")) {
                    bean.setLocationName("GPS Location not found");
                } else{
                    bean.setLocationName(cur.getString(cur.getColumnIndex("LocationName")));
                 }
                String Imagepath=cur.getString(cur.getColumnIndex("ImagePath"));
                bean.setImagePath(CompanyURL+Imagepath);
                bean.setGender(cur.getString(cur.getColumnIndex("Gender")));
                bean.setDayDiff(cur.getString(cur.getColumnIndex("DayDiff")));
                lsMyteam.add(bean);
            } while (cur.moveToNext());
            sortData(asc);
            reportingtolist.setAdapter(new GPSReportingToAdapter(GPSTeamList.this,
                    lsMyteam));
        } else {
            txt_no_record.setVisibility(View.VISIBLE);
            view_map.setVisibility(View.GONE);

        }
        sql.close();
    }
    private void showProgressDialog() {


        mProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        mProgress.setVisibility(View.GONE);

    }

    class DownloadTeamDataJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override



        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_MyTeam;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                SQLiteDatabase sql = db.getWritableDatabase();
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MYTEAM, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYTEAM, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_MYTEAM, null, values);
                    Log.e("team data", "ssss..." + a);
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
                updatelist();
            }
        }

    }

    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.manu_gps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.refresh1) {
            refreshData();
            return true;
        } else if (id == R.id.locdata) {

            Intent i = new Intent(getApplicationContext(), ActivityGPSLocalData.class);
            startActivity(i);
            return true;
           /* case R.id.StartLocation://locdata
                ActivityLogIn.regservicenonGPS(getApplicationContext());
                return true;*/
        } else {
            return false;
        }

    }
    private void sortData(boolean asc)
    {
        //SORT ARRAY ASCENDING AND DESCENDING
        if (asc)
        {

            Collections.sort(lsMyteam, new Comparator<MyTeamBean>() {
                @Override
                public int compare(MyTeamBean o1, MyTeamBean o2) {
                    return o1.getUserName().compareTo(o2.getUserName());
                }
            });


        }
        else
        {

        }



    }


}