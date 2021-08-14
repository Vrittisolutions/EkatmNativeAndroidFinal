package com.vritti.AlfaLavaModule.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.AdapteMRSDetail;
import com.vritti.AlfaLavaModule.adapter.AdapterPutawayPacketDetail;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.MRSDetailBean;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysPacketDetail;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MRSDetailActivity extends AppCompatActivity {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static RecyclerView recycler;
    private static AdapteMRSDetail adapter;
    private static Paint p = new Paint();
    private static ArrayList<MRSDetailBean> list;
    static View Creatview;
    private String S_GRN, S_HDR;
    private Dowmloaditem dowmloaditem;
    MenuItem chk;
    ArrayList<String> barcodecontent;
    static boolean DoNOTShowPopup = false;
    MenuItem menuItem;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject J_obj;
    String GrnId = "";
    EditText s_search;
    private EditText editText;
    private AlertDialog dialog;
    private List<AlfaLocation> locationList;
    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback;
    private ItemTouchHelper itemTouchHelper;
    RelativeLayout bgTransParent;
    TextView grnText;
    TextView poNumberText;
    TextView poDate;
    TextView currentDate;
    Button cancelBtn, cfmBtn;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private String ItemCode,PacketNo;
    private double ReqQty;
    private String HeatNo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_mrsheader);
        getSupportActionBar().setTitle("MO Flowcard Picklist");

        userpreferences = this.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(MRSDetailActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(MRSDetailActivity.this);
        String dabasename = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(MRSDetailActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(MRSDetailActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);




        S_HDR=getIntent().getStringExtra("MRSHeaderId");

        initView();


        if (Check_Db()) {
            detailPacket();
        } else {


            if (isnet()) {
                ProgressHUD.show(MRSDetailActivity.this, "Fetching MRS Detail ...", true, false);
                new StartSession(MRSDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR);
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        dowmloaditem = new Dowmloaditem();
                        dowmloaditem.execute(S_HDR);
                    }


                });

            } else {
                Toast.makeText(MRSDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

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
                    Log.i("data::", data);
                    String[] separated = data.split("Info:");
                    String json = separated[1];


                    try {

                        JSONObject jsonObject = new JSONObject(json);

                        ItemCode = jsonObject.getString("Itemcode");
                        PacketNo = jsonObject.getString("PacketNo");



                        filter(ItemCode,PacketNo);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    // convertToGson(data);

                    return true;
                } else if (event.getAction() == EditorInfo.IME_ACTION_SEARCH) {
                   /* String data = s_search.getText().toString();
                    filter(data);*/
                    return true;
                }
                return false;
            }
        });

    }

    private void initView() {

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        s_search = findViewById(R.id.s_search);
        s_search.setHint("Scan Heat No");
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MRSDetailActivity.this);
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<MRSDetailBean>();
        barcodecontent = new ArrayList<String>();
    }


    public void detailPacket() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_MRS_DETAIL, null);
            if (c.getCount() == 0) {

            } else {
                c.moveToFirst();
                do {
                    MRSDetailBean bean = new MRSDetailBean();
                    bean.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setMRSDetailId(c.getString(c.getColumnIndex("MRSDetailId")));
                    bean.setMRSHeaderId(c.getString(c.getColumnIndex("MRSHeaderId")));
                    bean.setItemMasterId(c.getString(c.getColumnIndex("ItemMasterId")));
                    bean.setReqQty(c.getString(c.getColumnIndex("ReqQty")));
                    bean.setIssuedQty(c.getString(c.getColumnIndex("IssuedQty")));
                    bean.setUOMCode(c.getString(c.getColumnIndex("UOMCode")));
                    bean.setMODetailId(c.getString(c.getColumnIndex("MODetailId")));
                    bean.setSuggLotNo(c.getString(c.getColumnIndex("HeatNo")));
                    bean.setMONo(c.getString(c.getColumnIndex("MONo")));
                    bean.setFlag(c.getString(c.getColumnIndex("Flag")));
                    list.add(bean);
                } while (c.moveToNext());

            }

            adapter = new AdapteMRSDetail(list,MRSDetailActivity.this);
            recycler.setAdapter(adapter);
            adapter.update(list);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private Boolean Check_Db() {
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_MRS_DETAIL + "' WHERE MRSHeaderId='" + S_HDR + "'", null);
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


    @Override
    public void onResume() {
        super.onResume();

        detailPacket();

    }

    private class UpLoadData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetPacketSplitUpdate+"?PacketNo="+PacketNo+"&RemovedQty="+ReqQty;
            try {
                res = ut.OpenConnection(url,MRSDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();
            if (s.equalsIgnoreCase("ok")) {
                onResume();
                Toast.makeText(MRSDetailActivity.this, "Data Send Successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MRSDetailActivity.this, "Data Not Send", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class Dowmloaditem extends AsyncTask<String, String, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetMRSDtl_ForAndr + "?MRSHeaderid=" + S_HDR;
            try {

                res = ut.OpenConnection(url, MRSDetailActivity.this);
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
            super.onPostExecute(s);
            ProgressHUD.Destroy();

            if (!(s.equalsIgnoreCase("[]"))) {
                if (s.contains("MRSHeaderId")) {
                    try {
                        JSONArray jResults = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            jResults = new JSONArray(s);
                        }
                        Log.d("test", "jResults :=" + jResults);

                        cf.DeleteAllRecord(db.TABLE_MRS_DETAIL);
                        Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_MRS_DETAIL, null);
                        Log.e("Table values----", "" + cur.getCount());
                        ContentValues Container = new ContentValues();
                        String columnName, columnValue;
                        for (int index = 0; index < jResults.length(); index++) {
                            JSONObject jstring = jResults.getJSONObject(index);
                            for (int j = 0; j < cur.getColumnCount(); j++) {
                                columnName = cur.getColumnName(j);
                                if (columnName.equalsIgnoreCase("Flag")) {
                                    columnValue = WebUrlClass.DoneFlag_Default;
                                } else {
                                    columnValue = jstring.getString(columnName);
                                }
                                Container.put(columnName, columnValue);
                                Log.e("Count ...",
                                        " count i: " + index + "  j:" + j);
                            }
                            long a = sql.insert(db.TABLE_MRS_DETAIL, null, Container);
                        }


                    } catch (Exception e) {
                    }
                    detailPacket();
                } else {
                    Toast.makeText(MRSDetailActivity.this, "Invalid MONo", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(MRSDetailActivity.this, "No data found", Toast.LENGTH_SHORT).show();
            }
            ProgressHUD.Destroy();
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void filter(String data,String packet) {
        List<MRSDetailBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItemCode().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update(dummyList);
            }

        }

        if (dummyList.size() == 0) {
            dummyList.clear();
            Toast.makeText(MRSDetailActivity.this, "HeatNo not found  in ZHT100 data!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MRSDetailActivity.this, "HeatNo found !!", Toast.LENGTH_SHORT).show();
            PacketNo=packet;
            ItemCode=data;
            ContentValues values = new ContentValues();
            String aa = WebUrlClass.DoneFlag_Complete;
            values.put("Flag", aa);
            sql.update(db.TABLE_MRS_DETAIL, values, "ItemCode=?", new String[]{String.valueOf(ItemCode)});


            try {
                Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_MRS_DETAIL + "' WHERE ItemCode='" + ItemCode + "'", null);
                if (c.getCount() == 0) {

                } else {
                    c.moveToFirst();
                    do {

                        ReqQty= Double.parseDouble(c.getString(c.getColumnIndex("ReqQty")));

                    } while (c.moveToNext());

                }
            }catch (Exception e){
                e.printStackTrace();
            }


            if (isnet()) {
                ProgressHUD.show(MRSDetailActivity.this, "Sending Detail ...", true, false);
                new StartSession(MRSDetailActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new UpLoadData();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        new UpLoadData();
                    }


                });

            } else {
                Toast.makeText(MRSDetailActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }





            s_search.setText("");
        }



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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();


    }

    private class UpLoadHeatNoData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_CheckQtyForLot+"?HeatNo="+HeatNo+"&ItemCode="+ItemCode;
            try {
                res = ut.OpenConnection(url,MRSDetailActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ProgressHUD.Destroy();
            if (s.contains("0.0000")) {
                Toast.makeText(MRSDetailActivity.this, "No quantity available for this Heat No", Toast.LENGTH_LONG).show();

            }else if (s.equalsIgnoreCase("") || s.equalsIgnoreCase("null")) {
                Toast.makeText(MRSDetailActivity.this, "Please select right packet", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(MRSDetailActivity.this, "Remaining quantity is "+s, Toast.LENGTH_LONG).show();
            }
        }
    }


}

