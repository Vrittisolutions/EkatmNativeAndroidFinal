package com.vritti.AlfaLavaModule.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.utility.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

public class CartonListActivity extends AppCompatActivity {

    Button btn_create,btn_create_ship;
    String PackOrderNo="",Pack_OrdHdrId="";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    private Context pContext;
    RecyclerView my_recycler_carton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carton_list_lay);

        pContext = CartonListActivity.this;
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(pContext);
        String settingKey = ut.getSharedPreference_SettingKey(pContext);
        String dabasename = ut.getValue(pContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(pContext, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(pContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(pContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(pContext, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(pContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(pContext, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(pContext, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(pContext, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(pContext, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        btn_create=findViewById(R.id.btn_create);
        btn_create_ship=findViewById(R.id.btn_create_ship);

        PackOrderNo=getIntent().getStringExtra("dono");
        Pack_OrdHdrId=getIntent().getStringExtra("header");

        getSupportActionBar().setTitle(PackOrderNo+"\n"+"Carton List");

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartonListActivity.this, BoxmasterActivity.class);
                intent.putExtra("packorder", Pack_OrdHdrId);
                intent.putExtra("dono", PackOrderNo);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);


            }
        });


        btn_create_ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isnet()) {
                    ProgressHUD.show(CartonListActivity.this, "Sending data...", true, false);
                    new StartSession(CartonListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new PostShipment().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                        }


                    });

                } else {
                    Toast.makeText(CartonListActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    public class PostShipment extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_AWBShipmentCreation + "?Pack_OrdHdrId=" + Pack_OrdHdrId +"&AWBNo="+PackOrderNo;
            try {
                res = ut.OpenConnection(url, CartonListActivity.this);
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
        protected void onPostExecute(String resp) {
            super.onPostExecute(resp);//GRNHeaderId
            ProgressHUD.Destroy();

            String s = String.valueOf(res);
            if (s.contains("false")) {
                Toast.makeText(CartonListActivity.this, "Please check stock", Toast.LENGTH_LONG).show();
            } else if (s.equals("Error")) {
                Toast.makeText(CartonListActivity.this, "Server Error....", Toast.LENGTH_LONG).show();
            } else if (s.contains("true")) {
                Toast.makeText(CartonListActivity.this, "Record updated Successfully", Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = userpreferences.edit();
                editor.remove(WebUrlClass.MyPREFERENCES_HEADER);
                editor.remove("OrdNo");
                editor.commit();
                if (EnvMasterId.equals("jal")||EnvMasterId.equals("jaluat")) {
                    startActivity(new Intent(CartonListActivity.this, AlfaHomePage.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }else {
                    startActivity(new Intent(CartonListActivity.this, ReceiptPackagingDOListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
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

}
