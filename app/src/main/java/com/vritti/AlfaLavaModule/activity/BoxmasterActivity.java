package com.vritti.AlfaLavaModule.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Box;
import com.vritti.AlfaLavaModule.adapter.Adapter_PrinterName;
import com.vritti.AlfaLavaModule.adapter.Adp_Box;
import com.vritti.AlfaLavaModule.adapter.Adp_Putaways;
import com.vritti.AlfaLavaModule.bean.AlfaLocation;
import com.vritti.AlfaLavaModule.bean.BoxBean;
import com.vritti.AlfaLavaModule.bean.PrinterName;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class BoxmasterActivity extends AppCompatActivity {


    public Dialog myDialog;
    private static TextView D_txt_grn;
    private static TextView D_txt_grn_wait;
    private static RecyclerView recycler;
    private static Adp_Box adapter;
    private List<BoxBean> list;

    private static Context pContext;
    private static View view;
    private static DowmloadBox downloadputlist;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText s_search;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    String ItemMasterId, Pack_OrdHdrId, SoScheduleId, PackOrderNo, PacketNo = "";
    private String BoxTypeMasterId;
    int qty = 0, Packed = 0;
    private String Cartan_code="";
    private String CartanHeaderId;

    ArrayList<PrinterName>printerNameArrayList;
    private String printerName;
    private Adapter_PrinterName adapterPrinterName;
    private AlertDialog b;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_activity_putaway);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Carton/Box");

        printerNameArrayList=new ArrayList<>();


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(BoxmasterActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(BoxmasterActivity.this);
        String dabasename = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(BoxmasterActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(BoxmasterActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        list = new ArrayList<>();

        Cartan_code=userpreferences.getString(WebUrlClass.MyPREFERENCES_CODE,"");
        CartanHeaderId=userpreferences.getString(WebUrlClass.MyPREFERENCES_HEADER,"");


        init();


    }

    private void init() {


        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        s_search = findViewById(R.id.s_search);
        s_search.setVisibility(View.GONE);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(pContext);
        recycler.setLayoutManager(layoutManager);


        Intent intent = getIntent();
        //ItemMasterId= intent.getStringExtra("itemid");
        Pack_OrdHdrId = intent.getStringExtra("packorder");
        //SoScheduleId=intent.getStringExtra("soScheduleId");
        //qty=intent.getIntExtra("qty",0);
        //PacketNo=intent.getStringExtra("packetno");
        PackOrderNo=intent.getStringExtra("dono");
        // Packed=intent.getIntExtra("Packed",0);

        if (Check_Db()) {
            Update_BOX();
        } else {
            if (isnet()) {
                ProgressHUD.show(BoxmasterActivity.this, "Fetching data...", true, false);
                new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        downloadputlist = new DowmloadBox();
                        downloadputlist.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        downloadputlist = new DowmloadBox();
                        downloadputlist.execute();
                    }


                });

            } else {
                Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
        List<BoxBean> dummyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBoxCode().equals(data)) {
                dummyList.add(list.get(i));
                adapter.update(dummyList);
            }

        }
        if (dummyList.size() == 0) {
            dummyList.clear();
            Toast.makeText(BoxmasterActivity.this, "No data found !!", Toast.LENGTH_SHORT).show();
        }
        s_search.setText("");
    }

    private Boolean Check_Db() {

        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_BOX + "'", null);
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
                    ProgressHUD.show(BoxmasterActivity.this, "Fetching data...", true, false);
                    new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            downloadputlist = new DowmloadBox();
                            downloadputlist.execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            downloadputlist = new DowmloadBox();
                            downloadputlist.execute();
                        }


                    });

                } else {
                    Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Update_BOX();
        // Toast.makeText(pContext, "on resume", Toast.LENGTH_SHORT).show();
    }


    private void Update_BOX() {
        list.clear();
        int count = 0;
        try {
            Cursor c = sql.rawQuery("SELECT * FROM '" + db.TABLE_BOX + "'", null);
            if (c.getCount() == 0) {
                //  addgrn_D(ScanGRN);
            } else {
                c.moveToFirst();
                do {
                    BoxBean bean = new BoxBean();
                    bean.setBoxTypeMasterId(c.getString(c.getColumnIndex("BoxTypeMasterId")));
                    bean.setBoxCode(c.getString(c.getColumnIndex("BoxCode")));
                    bean.setBoxName(c.getString(c.getColumnIndex("BoxName")));
                    list.add(bean);
                } while (c.moveToNext());
                adapter = new Adp_Box(list);
                recycler.setAdapter(adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectbox(String boxTypeMasterId) {

        BoxTypeMasterId = boxTypeMasterId;

        final JSONObject jsonObject = new JSONObject();

        // SoScheduleId - 790F73BF-E734-4184-A376-FC406F9965F9
        try {
            jsonObject.put("RefType", "Pack");
            jsonObject.put("RefId", Pack_OrdHdrId);
            jsonObject.put("BoxTypeMasterId", boxTypeMasterId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (isnet()) {
            ProgressHUD.show(BoxmasterActivity.this, "Sending  data...", true, false);
            new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UpLoadData().execute(jsonObject.toString());
                    ;
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        } else {
            Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }



  /*      startActivity(new Intent(BoxmasterActivity.this,PackagingActivity.class).
                //putExtra("itemid",ItemMasterId).
                putExtra("packorder", Pack_OrdHdrId).
                //putExtra("soScheduleId",SoScheduleId).
                putExtra("boxTypeMasterId",BoxTypeMasterId)
               // putExtra("qty",qty).
                //putExtra("packetno",PacketNo).
                //putExtra("dono", PackOrderNo).
                //putExtra("Packed", Packed)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
*/


    }


    private class DowmloadBox extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getAllData;
            try {
                res = ut.OpenConnection(url, BoxmasterActivity.this);
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
            ProgressHUD.Destroy();

            list.clear();
            if (s.contains("BoxTypeMasterId")) {
                try {
                    Log.e("save ps : ", "res : " + s);
                    JSONArray jResults = new JSONArray(s);
                    Log.d("test", "jResults :=" + jResults);
                    cf.DeleteAllRecord(db.TABLE_BOX);
                    Cursor cur = sql.rawQuery("SELECT *   FROM " + db.TABLE_BOX, null);
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
                        long a = sql.insert(db.TABLE_BOX, null, Container);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Update_BOX();
            } else if (s.contains("[]")) {
                Toast.makeText(BoxmasterActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(BoxmasterActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
            }

            ProgressHUD.Destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProgressHUD.Destroy();
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

            String url = CompanyURL + WebUrlClass.api_POSTInsertPutPacketInCartonHdr;
            try {
                res = ut.OpenPostConnection(url, params[0], BoxmasterActivity.this);
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
            if (s.contains(",")) {

                String[] namesList = s.split(",");
                String Cartan_ID = namesList[0].replace("\"", "");
                Cartan_code = namesList[1];

                SharedPreferences.Editor editor = userpreferences.edit();
                editor.putString(WebUrlClass.MyPREFERENCES_HEADER, Cartan_ID);
                editor.putString(WebUrlClass.MyPREFERENCES_CODE, Cartan_code);
                editor.commit();

                final MediaPlayer mp = MediaPlayer.create(BoxmasterActivity.this, R.raw.ok);
                mp.start();

                Toast toast = Toast.makeText(BoxmasterActivity.this, "Carton created successfully", Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastMessage.setCompoundDrawablePadding(5);
                toastView.setBackgroundColor(Color.TRANSPARENT);
                toast.show();



                editor = userpreferences.edit();
                editor.putString("OrdNo", PackOrderNo);
                editor.commit();

                if (Constants.type == Constants.Type.Alfa) {
                    if (isnet()) {
                        new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloadPrinterData().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }


                        });

                    } else {
                        Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    finish();
                }

               /* startActivity(new Intent(BoxmasterActivity.this,DOPackingScanDetails.class)
                        .putExtra("dono",PackOrderNo)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
*/
            }
        }
    }

    private class UpLoadSplitData extends AsyncTask<String, String, String> {
        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_Get_Carton_Packet_For_QR+"?CartonNo="+Cartan_code+"&PackOrderNo="+PackOrderNo+"&PrinterName="+ URLEncoder.encode(printerName,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                res = ut.OpenConnection(url, BoxmasterActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase("ok")) {
                Toast.makeText(BoxmasterActivity.this, "Label Printed Successfully", Toast.LENGTH_SHORT).show();
                finish();
                //b.dismiss();
            } else {
                //    Toast.makeText(ItemWisePickListDetailActivity.this, "Packet split Successfully", Toast.LENGTH_SHORT).show();

                Toast toast = Toast.makeText(BoxmasterActivity.this, "Label Printed Successfully", Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.GREEN);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();
                finish();


                //   onResume();
                // b.dismiss();
            }
        }
    }
    class DownloadPrinterData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();

        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetPrinterName;

            try {
                res = ut.OpenConnection(url, BoxmasterActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    printerNameArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        PrinterName userList = new PrinterName();
                        JSONObject jorder = jResults.getJSONObject(i);
                        userList.setPrinterName(jorder.getString("PrinterName"));
                        printerNameArrayList.add(userList);


                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);



            if (response.contains("[]")) {

                Toast.makeText(BoxmasterActivity.this, "Printer not found", Toast.LENGTH_SHORT).show();
            } if (response.equals("")) {
                printerName="";
                //   Toast.makeText(ItemWisePickListDetailActivity.this, "Printer not found", Toast.LENGTH_SHORT).show();

                if (isnet()) {
                    new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UpLoadSplitData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            new UpLoadSplitData().execute();

                        }


                    });

                } else {
                    Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BoxmasterActivity.this);
                LayoutInflater inflater = BoxmasterActivity.this.getLayoutInflater();
                final View myView = inflater.inflate(R.layout.printername_lay, null);
                dialogBuilder.setView(myView);
                Spinner printername = (Spinner) myView .findViewById(R.id.spinner_printer);

                adapterPrinterName = new Adapter_PrinterName(BoxmasterActivity.this, printerNameArrayList);
                printername.setAdapter(adapterPrinterName);
                printername.setSelection(0,false);


                printername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        printerName=printerNameArrayList.get(position).getPrinterName();


                        if (isnet()) {
                            b.dismiss();
                            new StartSession(BoxmasterActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new UpLoadSplitData().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    new UpLoadSplitData().execute();

                                }


                            });

                        } else {
                            Toast.makeText(BoxmasterActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                b = dialogBuilder.create();
                b.show();

            }


        }
    }


}

