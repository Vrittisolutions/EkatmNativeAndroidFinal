package com.vritti.AlfaLavaModule.activity.grn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.unpacking.CartonHeaderListActivity;
import com.vritti.AlfaLavaModule.adapter.Adp_UnPackingOrder;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GRNPendingActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    TextView edt_scanPacket;
    ImageView img_search;
    ProgressBar progress;
    private static RecyclerView recycler;
    private static Adp_CreateGRN adapter;
    private List<GRNPOST> list;
    ArrayList<GRNPOST> templist = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);
        getSupportActionBar().setTitle("Pending GRN");


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(GRNPendingActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(GRNPendingActivity.this);
        String dabasename = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(GRNPendingActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(GRNPendingActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        list = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        edt_scanPacket = (TextView) findViewById(R.id.edt_scanPacket);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GRNPendingActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter= new Adp_CreateGRN(list);
        recycler.setAdapter(adapter);

       edt_scanPacket.setHint("Search grn no");



        edt_scanPacket.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String text = edt_scanPacket.getText().toString().toLowerCase(Locale.getDefault());
                GRNfilter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        edt_scanPacket.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER)
                        || keyCode == KeyEvent.KEYCODE_TAB) {
                    // handleInputScan();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (edt_scanPacket != null) {
                                edt_scanPacket.requestFocus();
                            }
                        }
                    }, 10); // Remove this Delay Handler IF requestFocus(); works just fine without delay
                    String data = edt_scanPacket.getText().toString();
                    filter(data);

                    return true;
                }else if(event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                    String data = edt_scanPacket.getText().toString();
                    filter(data);
                    return true;
                }
                return false;
            }
        });

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


    private class DowmloadAssignedPutaway extends AsyncTask<String, String, String> {
        Object res;
        String response;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.GetPendingGRN;
            try {
                res = ut.OpenConnection(url, GRNPendingActivity.this);
                response = res.toString();
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
            progress.setVisibility(View.GONE);
            list.clear();
            if (s.contains("GRNHeaderId")) {
                try {
                    JSONArray jResults = new JSONArray(s);

                    for (int i=0;i<jResults.length();i++){
                        GRNPOST grnpost=new GRNPOST();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        String GRNNo=jsonObject.getString("GRNNo");
                        grnpost.setGRNNo(GRNNo);
                        String GRNHeaderId=jsonObject.getString("GRNHeaderId");
                        grnpost.setGRNHeaderId(GRNHeaderId);
                        String PartyDCNo=jsonObject.getString("PartyDCNo");
                        grnpost.setPartyDCNo(PartyDCNo);
                        String SupplierId=jsonObject.getString("SupplierId");
                        grnpost.setSupplierId(SupplierId);
                        list.add(grnpost);

                    }


                    adapter.update(list);
                    templist.addAll(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (s.contains("[]")) {
                Toast.makeText(GRNPendingActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(GRNPendingActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            progress.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isnet()) {

            try {
                progress.setVisibility(View.VISIBLE);
                new StartSession(GRNPendingActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        DowmloadAssignedPutaway dowmloadAssignedPutaway = new DowmloadAssignedPutaway();
                        dowmloadAssignedPutaway.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(GRNPendingActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void GRNfilter(String text) {
        //new array list that will hold the filtered data
        ArrayList<GRNPOST> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (GRNPOST s : list) {
            //if the existing elements contains the search input
            if (s.getGRNNo().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.update(filterdNames);
    }
    private void filter(String data) {
        List<GRNPOST> dummyList = new ArrayList<>();

        for(int i = 0 ; i< list.size() ; i ++){
            if(list.get(i).getGRNNo().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update(dummyList);
            }

        }
        if(dummyList.size() == 0) {
            dummyList.clear();

        }
        edt_scanPacket.setText("");
    }

}