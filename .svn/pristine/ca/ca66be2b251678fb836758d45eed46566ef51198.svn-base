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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.vritti.AlfaLavaModule.adapter.Adp_Putaways;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Interface.CallBack;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class ActivityGRNPutAway extends AppCompatActivity {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String ScanGRN = null;
    public Dialog myDialog;
    private static TextView D_txt_grn;
    private static TextView D_txt_grn_wait;
    private static RecyclerView recycler;
    private static Adp_Putaways adapter;
    private List<PutAwaysBean> list;

    private static Context pContext;
    private static View view;
    private static DowmloadAssignedPutaway downloadputlist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText s_search;
    private List<AlfaLocation> locationList;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_putaway);
        getSupportActionBar().setTitle("GRN Putaway");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(ActivityGRNPutAway.this);
        String settingKey = ut.getSharedPreference_SettingKey(ActivityGRNPutAway.this);
        String dabasename = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ActivityGRNPutAway.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(ActivityGRNPutAway.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
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
                ProgressHUD.show(ActivityGRNPutAway.this, "Fetching Pending Putaway...", true, false);
                new StartSession(ActivityGRNPutAway.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        downloadputlist = new DowmloadAssignedPutaway();
                        downloadputlist.execute(UserMasterId);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        downloadputlist = new DowmloadAssignedPutaway();
                        downloadputlist.execute(UserMasterId);
                    }


                });

            } else {
                Toast.makeText(ActivityGRNPutAway.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }



            /*s_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                            ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                                    && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                        Toast.makeText(ActivityGRNPutAway.this,
                                s_search.getText().toString() + "Enter Pressed", Toast.LENGTH_LONG).show();
                        String data = s_search.getText().toString();
                        try {
                            filter(data);
                        } catch (Exception e) {
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            });
*/

            s_search.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_UP &&
                            keyCode == KeyEvent.KEYCODE_ENTER)
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
                        try {
                            filter(data);
                        } catch (Exception e) {
                        }
                        return true;
                    } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = s_search.getText().toString();
                        filter(data);
                        return true;
                    }
                    return false;
                }
            });

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
                        Log.i("data::" , data);

                        try {
                            filter(data);
                        } catch (Exception e) {
                        }
                        return true;
                    } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = s_search.getText().toString();
                        filter(data);
                        return true;
                    }
                    return false;
                }
            });


/*
            s_search.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if (s.length() != 0) {
                        s_search.setText("");
                    }else {
                        String data=s.toString();
                        filter(data);
                    }
                }
            });
*/




           /* s_search.setOnKeyListener(new View.OnKeyListener() {
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
                    }else if(event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = s_search.getText().toString();
                        filter(data);
                        return true;
                    }
                    return false;
                }
            });*/

           /* s_search.setOnKeyListener(new View.OnKeyListener() {
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
                    }else if(event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                        String data = s_search.getText().toString();
                        filter(data);
                        return true;
                    }
                    return false;
                }
            });
*/

           /* s_search.setOnKeyListener(new View.OnKeyListener() {
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
            });*/
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
        List<PutAwaysBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGRN_Number().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update(dummyList);
            }

        }
        if (dummyList.size() == 0) {
            dummyList.clear();
            Toast.makeText(ActivityGRNPutAway.this,"No data found !!",Toast.LENGTH_SHORT).show();
        }
        s_search.setText("");
    }

    private Boolean Check_Db() {

        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_USER + "'", null);
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
                    ProgressHUD.show(ActivityGRNPutAway.this, "Fetching Pending Putaway...", true, false);
                    new StartSession(ActivityGRNPutAway.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            downloadputlist = new DowmloadAssignedPutaway();
                            downloadputlist.execute(UserMasterId);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            downloadputlist = new DowmloadAssignedPutaway();
                            downloadputlist.execute(UserMasterId);
                        }


                    });

                } else {
                    Toast.makeText(ActivityGRNPutAway.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Update_PutAways();
        // Toast.makeText(pContext, "on resume", Toast.LENGTH_SHORT).show();
    }



    private void Update_PutAways() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_PUTAWAY_USER + "'", null);
            if (c.getCount() == 0) {
                //  addgrn_D(ScanGRN);
            } else {
                c.moveToFirst();
                do {
                    PutAwaysBean bean = new PutAwaysBean();
                    bean.setGRN_Number(c.getString(c.getColumnIndex("GRNNo")));
                    bean.setGRN_Header(c.getString(c.getColumnIndex("GRNHeaderId")));
                    bean.setIsPacket(c.getString(c.getColumnIndex("IsPacketapl")));
                    list.add(bean);
                } while (c.moveToNext());
                adapter = new Adp_Putaways(list);
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
                Toast toast = Toast.makeText(ActivityGRNPutAway.this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                ScanGRN = contents;
                //addgrn_D(ScanGRN);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
                Toast toast = Toast.makeText(ActivityGRNPutAway.this, "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();

            }
        }
    }

    private void MySnackbar(String display, View view) {
        Snackbar.make(view.findViewById(R.id.putaway), display, Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText( LogInScreen.this, "Snackbar Action", Toast.LENGTH_LONG).show();
            }
        }).show();

    }


    private class DowmloadAssignedPutaway extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String strRes = null;
            UserMAster = params[0];
            String url = CompanyURL + WebUrlClass.api_getGRNForUser + "?UserMasterId=" + params[0];
            try {
                res = ut.OpenConnection(url,ActivityGRNPutAway.this);
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
            cf.DeleteAllRecord(db.TABLE_PUTAWAY_USER);
            if (s.contains("GRNHeaderId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    Log.d("test", "jResults :=" + jResults);
                    cf.DeleteAllRecord(db.TABLE_PUTAWAY_USER);
                    Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_PUTAWAY_USER, null);
                    Log.e("Table values----", "" + cur.getCount());
                    ContentValues Container = new ContentValues();
                    String columnName, columnValue;
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jstring = jResults.getJSONObject(index);
                        for (int j = 0; j < cur.getColumnCount(); j++) {
                            columnName = cur.getColumnName(j);
                            columnValue = jstring.getString(columnName);
                            Container.put(columnName, columnValue);
                            Log.e("Count Location ...",
                                    " count i: " + index + "  j:" + j);
                        }
                        long a = sql.insert(db.TABLE_PUTAWAY_USER, null, Container);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Update_PutAways();
            } else if (s.contains("[]")) {
                Toast.makeText(ActivityGRNPutAway.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ActivityGRNPutAway.this, "Server Time Out...", Toast.LENGTH_LONG).show();
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
