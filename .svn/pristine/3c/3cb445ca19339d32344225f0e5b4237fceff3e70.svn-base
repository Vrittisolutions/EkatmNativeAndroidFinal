package com.vritti.AlfaLavaModule.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_MRS;
import com.vritti.AlfaLavaModule.adapter.Adp_Putaways;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.MRSBean;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MRSHeaderActivity extends AppCompatActivity {


    private static String ScanGRN = null;
    private static RecyclerView recycler;
    private static Adp_MRS adapter;
    private List<MRSBean> list;

    private static Context pContext;
    private static DowmloadMRS downloadputlist;
    // TODO: Rename and change types of parameters
    EditText s_search;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_mrsheader);
        getSupportActionBar().setTitle("MO Flowcard List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(MRSHeaderActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(MRSHeaderActivity.this);
        String dabasename = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(MRSHeaderActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(MRSHeaderActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        list = new ArrayList<>();

        init();


    }

    private void init() {


        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        s_search = findViewById(R.id.s_search);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);
        if (Check_Db()) {
            Update_PutAways();
        } else {
            if (isnet()) {
                ProgressHUD.show(MRSHeaderActivity.this, "Fetching MRS...", true, false);
                new StartSession(MRSHeaderActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        downloadputlist = new DowmloadMRS();
                        downloadputlist.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        downloadputlist = new DowmloadMRS();
                        downloadputlist.execute();
                    }


                });

            } else {
                Toast.makeText(MRSHeaderActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            s_search.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                            || keyCode == KeyEvent.KEYCODE_TAB) {
                        // handleInputScan();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (s_search != null) {
                                    s_search.requestFocus();
                                }
                            }
                        }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                        String data = s_search.getText().toString();
                        filter(data);

                        return true;
                    } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = s_search.getText().toString();
                        filter(data);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public boolean isnet() {
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void filter(String data) {
        List<MRSBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getMRSNO().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update(dummyList);
            }

        }
        if (dummyList.size() == 0) {
            dummyList.clear();
            Toast.makeText(MRSHeaderActivity.this,"No data found !!",Toast.LENGTH_SHORT).show();
        }
        s_search.setText("");
    }

    private Boolean Check_Db() {

        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_MRS + "'", null);
            if (c.getCount() > 0) {

                return true;
            } else {

                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_refresh:
                if (isnet()) {
                    ProgressHUD.show(MRSHeaderActivity.this, "Fetching MRS...", true, false);
                    new StartSession(MRSHeaderActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            downloadputlist = new DowmloadMRS();
                            downloadputlist.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            downloadputlist = new DowmloadMRS();
                            downloadputlist.execute();
                        }


                    });

                } else {
                    Toast.makeText(MRSHeaderActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ProgressHUD progressHUD=new ProgressHUD(MRSHeaderActivity.this);
        Update_PutAways();
        // Toast.makeText(pContext, "on resume", Toast.LENGTH_SHORT).show();
    }



    private void Update_PutAways() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_MRS + "'", null);

            if (c.getCount() == 0) {
                //  addgrn_D(ScanGRN);
            } else {
                c.moveToFirst();
                do {
                    MRSBean bean = new MRSBean();
                    bean.setMRSHeaderId(c.getString(c.getColumnIndex("MRSHeaderId")));
                    bean.setMRSNO(c.getString(c.getColumnIndex("MRSNO")));
                    bean.setMRSDate(c.getString(c.getColumnIndex("MRSDate")));
                    bean.setMONo(c.getString(c.getColumnIndex("MONo")));
                    list.add(bean);
                } while (c.moveToNext());
                adapter = new Adp_MRS(list);
                recycler.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(MRSHeaderActivity.this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                ScanGRN = contents;
                //addgrn_D(ScanGRN);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(MRSHeaderActivity.this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }


    private class DowmloadMRS extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetMRSHeader_ForAndr;
            try {
                res = ut.OpenConnection(url, MRSHeaderActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                response = "Error";
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);//GRNHeaderId
            ProgressHUD.Destroy();

            list.clear();

            if (s.contains("MRSHeaderId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    Log.d("test", "jResults :=" + jResults);
                    cf.DeleteAllRecord(db. TABLE_MRS);
                    Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_MRS, null);
                    Log.e("Table values----", "" + cur.getCount());
                    ContentValues Container = new ContentValues();
                    String columnName, columnValue;
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jstring = jResults.getJSONObject(index);
                        for (int j = 0; j < cur.getColumnCount(); j++) {
                            columnName = cur.getColumnName(j);
                            columnValue = jstring.getString(columnName);
                            Container.put(columnName, columnValue);

                        }
                        long a = sql.insert(db.TABLE_MRS, null, Container);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Update_PutAways();
            } else if (s.contains("[]")) {
                cf.DeleteAllRecord(db. TABLE_MRS);
                Toast.makeText(MRSHeaderActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MRSHeaderActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            ProgressHUD.Destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

}
