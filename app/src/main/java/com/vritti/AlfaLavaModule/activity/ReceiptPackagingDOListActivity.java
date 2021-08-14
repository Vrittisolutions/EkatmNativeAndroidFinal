package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_DONumber;
import com.vritti.AlfaLavaModule.adapter.Adp_PickOrderNo;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
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

import java.util.ArrayList;

public class ReceiptPackagingDOListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_PickOrderNo adapter;
    private ArrayList<PicklistNO> picklistNOList;
    private static RecyclerView recycler;
    TextView edt_scanPacket;
    ImageView img_search;
    ProgressBar progress;



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
        cf = new CommonFunction(ReceiptPackagingDOListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ReceiptPackagingDOListActivity.this);
        String dabasename = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ReceiptPackagingDOListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(ReceiptPackagingDOListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        picklistNOList = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        edt_scanPacket = (TextView) findViewById(R.id.edt_scanPacket);
        img_search = (ImageView) findViewById(R.id.img_search);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReceiptPackagingDOListActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_PickOrderNo(picklistNOList);
        recycler.setAdapter(adapter);


        sql.delete(db.TABLE_GRN_PACKET, null, null);



       /* if (isnet()) {
            try {
                ProgressHUD.show(ReceiptPackagingDOListActivity.this, "Fetching data...", true, false);
                new StartSession(ReceiptPackagingDOListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        UploadDoDump uploadDoDump = new UploadDoDump();
                        uploadDoDump.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ReceiptPackagingDOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

*/
        edt_scanPacket.setOnKeyListener(new View.OnKeyListener() {
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
                        if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")){
                            if (data.length()==13){
                                DONumber = data;
                            }else if (data.length()==11) {
                                DONumber = data;
                            }
                            else {
                                data = data.substring(data.length() - 12);
                                DONumber = data;
                            }
                            edt_scanPacket.setText("");
                            startActivity(new Intent(ReceiptPackagingDOListActivity.this,
                                    DOPackingScanDetails.class).
                                    putExtra("dono", DONumber));
                        }else {
                            edt_scanPacket.setText("");
                            startActivity(new Intent(ReceiptPackagingDOListActivity.this,
                                    DOPackingScanDetails.class).
                                    putExtra("dono", DONumber));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    // callApi id pass
                    //      public string GetScanLocation(string LocationCode)


                  /*  if(PackOrderNo != null && !(PackOrderNo.equals(""))) {


                        if (isnet()) {
                            ProgressHUD.show(pContext, "Fetching data...", true, false);
                            new StartSession(pContext, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                    downloadPutAwayDetails.execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    downloadPutAwayDetails = new DownloadPacketedOrdNoDetails();
                                    downloadPutAwayDetails.execute();
                                }


                            });

                        } else {
                            Toast.makeText(pContext, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
*/

                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edt_scanPacket.getText().toString().trim();

                try {
                    String  DONumber =data;
                    if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")){
                        if (data.length()==13){
                            DONumber = data;
                        }else if (data.length()==11) {
                            DONumber = data;
                        }
                        else {
                            data = data.substring(data.length() - 12);
                            DONumber = data;
                        }
                        edt_scanPacket.setText("");
                        startActivity(new Intent(ReceiptPackagingDOListActivity.this,
                                DOPackingScanDetails.class).
                                putExtra("dono", DONumber));
                    }else {
                        edt_scanPacket.setText("");
                        startActivity(new Intent(ReceiptPackagingDOListActivity.this,
                                DOPackingScanDetails.class).
                                putExtra("dono", DONumber));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setdonumber(String DONumber,String Pack_OrdHdrId ) {
        String ord=userpreferences.getString("OrdNo","");

        if (ord.equals("")){
            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("OrdNo", DONumber);
            editor.commit();

            startActivity(new Intent(ReceiptPackagingDOListActivity.this, DOPackingScanDetails.class).
                    putExtra("dono", DONumber)
                    .putExtra("header", Pack_OrdHdrId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }else if (ord.equals(DONumber)) {

            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("OrdNo", DONumber);
            editor.commit();

            startActivity(new Intent(ReceiptPackagingDOListActivity.this, DOPackingScanDetails.class).
                    putExtra("dono", DONumber)
                    .putExtra("header", Pack_OrdHdrId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }else {
            /*Toast toast = Toast.makeText(ReceiptPackagingDOListActivity.this, "You have selected different pack order number", Toast.LENGTH_LONG);
            View toastView = toast.getView();
            TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
            toastMessage.setTextSize(18);
            toastMessage.setTextColor(Color.GREEN);
            toastMessage.setGravity(Gravity.CENTER);
            toastMessage.setCompoundDrawablePadding(5);
            toastView.setBackgroundColor(Color.TRANSPARENT);
            toast.show();
            final MediaPlayer mp = MediaPlayer.create(ReceiptPackagingDOListActivity.this, R.raw.alert);
            mp.start();*/

            SharedPreferences.Editor editor = userpreferences.edit();
            editor.putString("OrdNo", DONumber);
            editor.remove(WebUrlClass.MyPREFERENCES_CODE);
            editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
            editor.commit();
            startActivity(new Intent(ReceiptPackagingDOListActivity.this, DOPackingScanDetails.class).
                    putExtra("dono", DONumber)
                    .putExtra("header", Pack_OrdHdrId)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

       /* startActivity(new Intent(ReceiptPackagingDOListActivity.this,CartonListActivity.class).
                putExtra("dono",DONumber)
                .putExtra("header",Pack_OrdHdrId)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

*/


    }
    private class UploadDoDump extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_OrderPacking;
            try {
                res = ut.OpenConnection(url, ReceiptPackagingDOListActivity.this);
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
            try {
                if (s.equalsIgnoreCase("true")) ;
                if (isnet()) {
                    try {
                        ProgressHUD.show(ReceiptPackagingDOListActivity.this, "Fetching data...", true, false);
                        new StartSession(ReceiptPackagingDOListActivity.this, new CallbackInterface() {
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
                    Toast.makeText(ReceiptPackagingDOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
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

    private class GetPicklistNO extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetPackOrdNO;
            try {
                res = ut.OpenConnection(url, ReceiptPackagingDOListActivity.this);
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
                        if (Constants.type == Constants.Type.Alfa){
                        picklistNO.setPick_listHdrId(jsonObject.getString("Pack_OrdHdrId"));
                        picklistNO.setPicklistNo(jsonObject.getString("Pack_OrdNo"));
                        }else if (EnvMasterId.equalsIgnoreCase("jal")||EnvMasterId.equalsIgnoreCase("jaluat")){
                            picklistNO.setPick_listHdrId(jsonObject.getString("Pack_OrdHdrId"));
                            picklistNO.setPicklistNo(jsonObject.getString("AWBNO"));
                        }else {
                            picklistNO.setPick_listHdrId(jsonObject.getString("Pack_OrdHdrId"));
                            if (jsonObject.getString("AWBNO").equalsIgnoreCase("")){
                                picklistNO.setPicklistNo(jsonObject.getString("Pack_OrdNo"));
                            }else {
                                picklistNO.setPicklistNo(jsonObject.getString("AWBNO"));
                            }

                        }
                        picklistNOList.add(picklistNO);
                    }

                    adapter.update(picklistNOList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {
                Toast.makeText(ReceiptPackagingDOListActivity.this, "No records Present", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(ReceiptPackagingDOListActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
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
                new StartSession(ReceiptPackagingDOListActivity.this, new CallbackInterface() {
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
            Toast.makeText(ReceiptPackagingDOListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
}