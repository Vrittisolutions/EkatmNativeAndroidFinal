package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.AdapterPacketEnquiryDetail;
import com.vritti.AlfaLavaModule.adapter.Adp_PacketDisplay;
import com.vritti.AlfaLavaModule.bean.Packet;
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
import java.util.Locale;


public class PacketNoEnquiryActivity extends AppCompatActivity {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private  RecyclerView recycler;
    private AdapterPacketEnquiryDetail adapter;
    private ArrayList<Packet> list;
    ArrayList<Packet> templist = new ArrayList<>();

    private static Context pContext;
    private static View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText s_search;
    SQLiteDatabase sql;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    private String HearderId="";
    private String Flag="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_putaway);


        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(PacketNoEnquiryActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(PacketNoEnquiryActivity.this);
        String dabasename = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PacketNoEnquiryActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(PacketNoEnquiryActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        getSupportActionBar().setTitle("Packet Details");
        s_search =  findViewById(R.id.s_search);
        list=new ArrayList<>();
        adapter= new AdapterPacketEnquiryDetail(list);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PacketNoEnquiryActivity.this);
        recycler.setLayoutManager(layoutManager);
        s_search.setHint("Search packet");

        HearderId=getIntent().getStringExtra("header");
        recycler.setVisibility(View.GONE);

        if (isnet()) {
            ProgressHUD.show(PacketNoEnquiryActivity.this, "Fetching data...", true, false);

            new StartSession(PacketNoEnquiryActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    DowmloadAssignedPutaway dowmloaditem = new DowmloadAssignedPutaway();
                    dowmloaditem.execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(PacketNoEnquiryActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        s_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                String text = s_search.getText().toString().toLowerCase(Locale.getDefault());
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

    }

    private void getloaddata() {

        String searchQuery = "SELECT  * FROM " + db.TABLE_GRN_PACKET;
        Cursor cursor = sql.rawQuery(searchQuery, null);
        int count = cursor.getCount();
        if (count > 0) {
            cursor.moveToFirst();
            do {
                recycler.setVisibility(View.VISIBLE);
                Packet bean = new Packet();
                bean.setPacketNo(cursor.getString(cursor.getColumnIndex("PacketNo")));
                list.add(bean);
            } while (cursor.moveToNext());
            adapter.update(list);

        }
    }

    private void GRNfilter(String text) {
        //new array list that will hold the filtered data
        ArrayList<Packet> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Packet s : list) {
            //if the existing elements contains the search input
            if (s.getPacketNo().toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
        adapter.update(filterdNames);
    }

    public boolean isnet () {
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
        String url="";

        @Override
        protected String doInBackground(String... params) {
                url = CompanyURL + WebUrlClass.GetPacketEnquiryData + "?PacketNo=" + HearderId;

            try {
                res = Utility.OpenConnection(url, PacketNoEnquiryActivity.this);
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
            ProgressHUD.Destroy();
            list.clear();
            if (s.contains("PacketNo")) {
                try {
                    JSONArray jResults = new JSONArray(s);

                    for (int i=0;i<jResults.length();i++){
                        recycler.setVisibility(View.VISIBLE);
                        Packet grnpost=new Packet();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        String PacketNo=jsonObject.getString("PacketNo");
                        grnpost.setPacketNo(PacketNo);
                        String BalQty=jsonObject.getString("BalQty");
                        grnpost.setBalQty(BalQty);
                        String MovedQty=jsonObject.getString("MovedQty");
                        grnpost.setMovedQty(MovedQty);
                        String ItemCode=jsonObject.getString("ItemCode");
                        grnpost.setItemCode(ItemCode);
                        String ItemDesc=jsonObject.getString("ItemDesc");
                        grnpost.setItemDesc(ItemDesc);
                        String LocationCode=jsonObject.getString("LocationCode");
                        grnpost.setLocationCode(LocationCode);
                        String LocationType=jsonObject.getString("LocationType");
                        grnpost.setLocationType(LocationType);

                        list.add(grnpost);

                    }



                    adapter.update(list);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (s.contains("[]")) {
                Toast.makeText(PacketNoEnquiryActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(PacketNoEnquiryActivity.this, s, Toast.LENGTH_LONG).show();
            }

            ProgressHUD.Destroy();
        }
    }

}
