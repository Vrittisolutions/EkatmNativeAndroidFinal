package com.vritti.AlfaLavaModule.activity.packing_qc;

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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.adapter.Adp_CartonListData;
import com.vritti.AlfaLavaModule.bean.CartonData;
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

public class QCCartonHeaderListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private static Adp_QCCartonListData adapter;
    private ArrayList<CartonData> cartonDataArrayList;
    private static RecyclerView recycler;
    TextView edt_scanPacket;
    ImageView img_search;
    ProgressBar progress;
    private String CartonHeaderId="",PackOrderHeaderId="";
    LinearLayout len_search;
    private AlertDialog b;
    private String finaljson;
    Handler mHandler = new Handler();
    private String PackOrderNo="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alfa_dolist_lay);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(QCCartonHeaderListActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(QCCartonHeaderListActivity.this);
        String dabasename = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(QCCartonHeaderListActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(QCCartonHeaderListActivity.this, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        cartonDataArrayList = new ArrayList<>();
        progress=findViewById(R.id.progress);
        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        edt_scanPacket = (TextView) findViewById(R.id.edt_scanPacket);
        edt_scanPacket.setVisibility(View.VISIBLE);
        img_search = (ImageView) findViewById(R.id.img_search);
        len_search = (LinearLayout) findViewById(R.id.len_search);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QCCartonHeaderListActivity.this);
        recycler.setLayoutManager(layoutManager);
        adapter=new Adp_QCCartonListData(cartonDataArrayList);
        recycler.setAdapter(adapter);

        len_search.setVisibility(View.GONE);
        PackOrderNo=getIntent().getStringExtra("CODE");
        PackOrderHeaderId=getIntent().getStringExtra("ID");
        getSupportActionBar().setTitle(PackOrderNo);






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


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    return true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edt_scanPacket.getText().toString().trim();


            }
        });
    }

    public void setdonumber(String DONumber,String Pack_OrdHdrId ) {

                        startActivity(new Intent(QCCartonHeaderListActivity.this, QCPackingCartonDetailActivity.class)
                        .putExtra("dono",DONumber).
                        putExtra("headerid",Pack_OrdHdrId).
                        putExtra("PackHdrid",PackOrderHeaderId).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));


//       Packetdeletedialog(DONumber,Pack_OrdHdrId);

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

    private class GetCartonList extends AsyncTask<String, String, String> {
        Object res;
        String response;
        String UserMAster = null;

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_getCartonHdr +"?Pack_OrdHdrId="+PackOrderHeaderId;
            try {
                res = ut.OpenConnection(url, QCCartonHeaderListActivity.this);
                response = res.toString();
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

            cartonDataArrayList.clear();
            if (s.contains("CartonHeaderId")) {
                try {
                    JSONArray jResults = new JSONArray(s);
                    for (int i=0;i<jResults.length();i++){
                        CartonData cartonData=new CartonData();
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        cartonData.setCartonHeaderId(jsonObject.getString("CartonHeaderId"));
                        cartonData.setCartonCode(jsonObject.getString("CartonCode"));
                        cartonDataArrayList.add(cartonData);
                    }

                    adapter.update(cartonDataArrayList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (s.contains("[]")) {


                Toast toast = Toast.makeText(QCCartonHeaderListActivity.this, "Carton not found in " +PackOrderNo, Toast.LENGTH_LONG);
                View toastView = toast.getView();
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setTextSize(18);
                toastMessage.setTextColor(Color.RED);
                toastMessage.setGravity(Gravity.CENTER);
                toastView.setBackgroundColor(Color.WHITE);
                toast.show();
                progress.setVisibility(View.GONE);

                final MediaPlayer mp = MediaPlayer.create(QCCartonHeaderListActivity.this, R.raw.alert);
                mp.start();


            } else {
                Toast.makeText(QCCartonHeaderListActivity.this, "Server Time Out...", Toast.LENGTH_LONG).show();
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
                new StartSession(QCCartonHeaderListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        GetCartonList getCartonList = new GetCartonList();
                        getCartonList.execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            Toast.makeText(QCCartonHeaderListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void Packetdeletedialog(final String DONumber, final String pack_OrdHdrId) {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(QCCartonHeaderListActivity.this);
        LayoutInflater inflater = QCCartonHeaderListActivity.this.getLayoutInflater();
        final View myView = inflater.inflate(R.layout.unpacking_option, null);
        dialogBuilder.setView(myView);

        Button btn_full=myView.findViewById(R.id.btn_full);
        Button btn_partial=myView.findViewById(R.id.btn_partial);


        btn_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                startActivity(new Intent(QCCartonHeaderListActivity.this, QCPackingCartonDetailActivity.class)
                                .putExtra("dono",DONumber).
                                putExtra("headerid",pack_OrdHdrId).
                                putExtra("PackHdrid",PackOrderHeaderId).
                                putExtra("Flag","F").
                                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));




                b.dismiss();




            }
        });
        btn_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QCCartonHeaderListActivity.this, QCPackingCartonDetailActivity.class)
                        .putExtra("dono",DONumber).
                                putExtra("headerid",pack_OrdHdrId).
                                putExtra("PackHdrid",PackOrderHeaderId).
                                putExtra("Flag","P").
                                setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                b.dismiss();
            }
        });






        b = dialogBuilder.create();
        b.show();
    }


}