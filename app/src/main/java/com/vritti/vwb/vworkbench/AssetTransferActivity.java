package com.vritti.vwb.vworkbench;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.UserListAdapter;
import com.vritti.vwb.Beans.UserList;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by sharvari on 17-Oct-18.
 */

public class AssetTransferActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;

    Spinner spinner_username;
    TextView txt_asset_no, txt_asset_type, txt_invoice_no, txt_date_purchase, txt_serial_no, txt_model_no;
    Button btn_transfer, btncancel;
    ProgressBar mprogress;
    ArrayList<UserList> userListArrayList;
    UserListAdapter userListAdapter;
    SharedPreferences userpreferences;
    String Usermaster,AssetId, ScratchWorkspaceId, ScratchWorkspaceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_assettransfer_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(context, WebUrlClass.GET_MOBILE_KEY, settingKey);
        sql = db.getWritableDatabase();


        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        spinner_username = (Spinner) findViewById(R.id.spinner_username);
        txt_asset_no = (TextView) findViewById(R.id.txt_asset_no);
        txt_asset_type = (TextView) findViewById(R.id.txt_asset_type);
        txt_invoice_no = (TextView) findViewById(R.id.txt_invoice_no);
        txt_date_purchase = (TextView) findViewById(R.id.txt_date_purchase);
        txt_serial_no = (TextView) findViewById(R.id.txt_serial_no);
        txt_model_no = (TextView) findViewById(R.id.txt_model_no);
        btn_transfer = (Button) findViewById(R.id.btn_transfer);
        btncancel = (Button) findViewById(R.id.btncancel);

        Intent intent=getIntent();
        AssetId=intent.getStringExtra("AssetId");


        userListArrayList = new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AssetTransferActivity.this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("User", "");
        Type type = new TypeToken<List<UserList>>() {}.getType();
        userListArrayList = gson.fromJson(json, type);


        if (userListArrayList == null) {
            if (isnet()) {
                showProgress();
                new StartSession(AssetTransferActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadUserlistData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            if (userListArrayList.size() > 0) {
                userListAdapter = new UserListAdapter(AssetTransferActivity.this, userListArrayList);
                spinner_username.setAdapter(userListAdapter);
            }

        }
        if (isnet()) {
            showProgress();
            new StartSession(AssetTransferActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadAssetData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        }

        btn_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isnet()) {
                    showProgress();
                    new StartSession(AssetTransferActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new CheckTransferAsset().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                }
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        spinner_username.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Usermaster=userListArrayList.get(position).getUserMasterId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);

    }

    private void dismissProgress() {

        mprogress.setVisibility(View.GONE);


    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    class DownloadUserlistData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetFillUser;

            try {
                res = ut.OpenConnection(url, AssetTransferActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);

                    userListArrayList = new ArrayList<>();
                    userListArrayList.clear();
                    JSONArray jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {
                        UserList userList = new UserList();
                        JSONObject jorder = jResults.getJSONObject(i);

                        userList.setUserMasterId(jorder.getString("UserMasterId"));
                        userList.setUserName(jorder.getString("UserName"));
                        userListArrayList.add(userList);


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

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                Toast.makeText(AssetTransferActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AssetTransferActivity.this);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();

                String json = gson.toJson(userListArrayList);
                editor.putString("User", json);
                editor.commit();
                userListAdapter = new UserListAdapter(AssetTransferActivity.this, userListArrayList);
                spinner_username.setAdapter(userListAdapter);


            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.refresh1:

                if (isnet()) {
                    new StartSession(AssetTransferActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadUserlistData().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }


                return true;
            default:
                return false;
        }

    }

    class DownloadAssetData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetAssets + "?headerid="+AssetId;

            try {
                res = ut.OpenConnection(url, AssetTransferActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
                Toast.makeText(AssetTransferActivity.this, "Data not found", Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);


                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);

                        String PKAssetId=jorder.getString("PKAssetId");
                        String AssetNo=jorder.getString("AssetNo");
                        txt_asset_no.setText(AssetNo);
                        String AssetTypeId=jorder.getString("AssetTypeId");
                        String DateOfPurchase=jorder.getString("DateOfPurchase");
                        DateOfPurchase = DateOfPurchase.substring(DateOfPurchase.indexOf("(") + 1, DateOfPurchase.lastIndexOf(")"));
                        long timestamp = Long.parseLong(DateOfPurchase);
                        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date(timestamp);
                        DateOfPurchase = targetFormat.format(date);
                        txt_date_purchase.setText(DateOfPurchase);
                        String SerialNo=jorder.getString("SerialNo");
                        txt_serial_no.setText(SerialNo);
                        String ModelNo=jorder.getString("ModelNo");
                        txt_model_no.setText(ModelNo);
                        String InvoiceNo=jorder.getString("InvoiceNo");
                        txt_invoice_no.setText(InvoiceNo);
                        String UserMasterId=jorder.getString("UserMasterId");
                        String AssetTypeDesc=jorder.getString("AssetTypeDesc");
                        txt_asset_type.setText(AssetTypeDesc);



                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

    }

    class CheckTransferAsset extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_CheckTransferAsset + "?AssetId="+AssetId;

            try {
                res = ut.OpenConnection(url, AssetTransferActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {

                if (isnet()) {
                    showProgress();
                    new StartSession(AssetTransferActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetCreateDocument().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                }



            } else {

                dismissProgress();
                Toast.makeText(AssetTransferActivity.this, "Same asset can't be transfer again", Toast.LENGTH_SHORT).show();

            }
        }

    }
    class GetCreateDocument extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_GetCreateDocument + "?AssetId="+AssetId+"&UserMasterId="+Usermaster;

            try {
                res = ut.OpenConnection(url, AssetTransferActivity.this);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");


                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            // progressDialog.dismiss();
            dismissProgress();
            if (response.contains("[]")) {
                dismissProgress();
            } else {

                Toast.makeText(AssetTransferActivity.this, "Asset transfer successfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        }

    }
}
