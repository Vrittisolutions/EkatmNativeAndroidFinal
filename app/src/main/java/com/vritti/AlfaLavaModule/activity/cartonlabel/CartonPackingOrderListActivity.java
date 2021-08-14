package com.vritti.AlfaLavaModule.activity.cartonlabel;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Locale;

public class CartonPackingOrderListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_CartonPackingOrder adapter;
    private ArrayList<PicklistNO> picklistNOList;
    private static RecyclerView recycler;
    TextView edt_scanPacket;
    ImageView img_search;
    ProgressBar progress;
    LinearLayout len_search;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);
        getSupportActionBar().setTitle("Packing Order Select");


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(CartonPackingOrderListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(CartonPackingOrderListActivity.this);
        String dabasename = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(CartonPackingOrderListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(CartonPackingOrderListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        picklistNOList = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        edt_scanPacket = (TextView) findViewById(R.id.edt_scanPacket);
        img_search = (ImageView) findViewById(R.id.img_search);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CartonPackingOrderListActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_CartonPackingOrder(picklistNOList);
        recycler.setAdapter(adapter);

        edt_scanPacket.setHint("Search pack order");
        img_search.setVisibility(View.GONE);



        /*edt_scanPacket.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_ENTER)
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


                    String data = edt_scanPacket.getText().toString().trim();

                    try {
                                     String  DONumber =data;
                                     startActivity(new Intent(CartonPackingOrderListActivity.this,
                                     CartonLabelHeaderListActivity.class).
                                     putExtra("dono", DONumber));
                                     edt_scanPacket.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }






                    return true;
                }
                return false;
            }
        });*/

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edt_scanPacket.getText().toString().trim();

                        edt_scanPacket.setText("");
                        startActivity(new Intent(CartonPackingOrderListActivity.this,
                        CartonLabelHeaderListActivity.class).
                                putExtra("dono", data));
            }
        });

        edt_scanPacket.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String text = edt_scanPacket.getText().toString().toLowerCase(Locale.getDefault());
                filter(text);
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

    }

    public void setdonumber(String DONumber,String Pack_OrdHdrId ) {

        startActivity(new Intent(CartonPackingOrderListActivity.this, CartonLabelHeaderListActivity.class)
        .putExtra("CODE",DONumber).putExtra("ID",Pack_OrdHdrId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


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

    private class GetPicklistNO extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getShipmentNo;
            try {
                res = ut.OpenConnection(url, CartonPackingOrderListActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                // response = response.substring(1, response.length() - 1);
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

            picklistNOList.clear();
            if (s.contains("Pack_OrdHdrId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    for (int i=0;i<jResults.length();i++){
                        PicklistNO picklistNO=new PicklistNO();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        picklistNO.setPick_listHdrId(jsonObject.getString("Pack_OrdHdrId"));
                        picklistNO.setPicklistNo(jsonObject.getString("InvoiceNo"));
                        picklistNOList.add(picklistNO);
                    }

                    adapter.update(picklistNOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {
                Toast.makeText(CartonPackingOrderListActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(CartonPackingOrderListActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
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
                new StartSession(CartonPackingOrderListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        GetPicklistNO getPicklistNO = new GetPicklistNO();
                        getPicklistNO.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(CartonPackingOrderListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<PicklistNO> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (PicklistNO s : picklistNOList) {
            //if the existing elements contains the search input
            if (s.getPicklistNo().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.update(filterdNames);
    }

}