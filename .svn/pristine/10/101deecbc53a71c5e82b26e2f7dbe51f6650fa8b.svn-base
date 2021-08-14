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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AssetTransferAdapter;
import com.vritti.vwb.Beans.AssetTransfer;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by sharvari on 17-Oct-18.
 */

public class AssetTransferListActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;

    ListView list_asset;


    AssetTransfer assetTransfer;
    ArrayList<AssetTransfer>assetTransferArrayList=new ArrayList<>();
    AssetTransferAdapter assetTransferAdapter;
    SharedPreferences userpreferences;
    ProgressBar toolbar_progress_App_bar;
    TextView txt_record;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_asset_transfer_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        list_asset=(ListView) findViewById(R.id.list_asset);
        txt_record=(TextView) findViewById(R.id.txt_record);
        toolbar_progress_App_bar= (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        assetTransferArrayList=new ArrayList<>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
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



        if (isnet()) {
            new StartSession(AssetTransferListActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetAssetsListforAndroid().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }


            });

        }

        list_asset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String AssetId=assetTransferArrayList.get(position).getPKAssetId();
                startActivity(new Intent(AssetTransferListActivity.this, AssetTransferActivity.class).putExtra("AssetId",AssetId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });

    }

    class DownloadGetAssetsListforAndroid extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();


        }
        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebAPIUrl.api_GetRefreshChatRoom + "?ApplicationCode="+WebAPIUrl.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetAssetsListforAndroid + "?usermasterid="+UserMasterId;

            try {
                res = ut.OpenConnection(url,AssetTransferListActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
              //  response = response.substring(1, response.length() - 1);
                JSONArray jResults = new JSONArray(response);
                assetTransferArrayList.clear();
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    assetTransfer = new AssetTransfer();
                    assetTransfer.setAssetNo(jorder.getString("AssetNo"));
                    assetTransfer.setAssetTypeDesc(jorder.getString("AssetTypeDesc"));
                    assetTransfer.setInvoiceNo(jorder.getString("InvoiceNo"));
                    assetTransfer.setModelNo(jorder.getString("ModelNo"));
                    assetTransfer.setDealerName(jorder.getString("DealerName"));
                    assetTransfer.setDateOfPurchase(jorder.getString("DateOfPurchase"));
                    assetTransfer.setWarrantyDate(jorder.getString("WarrantyDate"));
                    assetTransfer.setSerialNo(jorder.getString("SerialNo"));
                    assetTransfer.setPKAssetId(jorder.getString("PKAssetId"));

                    assetTransferArrayList.add(assetTransfer);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.equalsIgnoreCase("[]")){
                //txt_chatroom_add.setVisibility(View.VISIBLE);
                txt_record.setVisibility(View.VISIBLE);
            }else {
                if (response != null) {
                    assetTransferAdapter=new AssetTransferAdapter(AssetTransferListActivity.this,assetTransferArrayList);
                    if(assetTransferAdapter.getCount()!=0){
                        list_asset.setAdapter(assetTransferAdapter);
                        list_asset.setVisibility(View.VISIBLE);

                    }else{

                    }
                }
            }

        }

    }

    void showprogress() {
        toolbar_progress_App_bar.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        toolbar_progress_App_bar.setVisibility(View.GONE);

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}



