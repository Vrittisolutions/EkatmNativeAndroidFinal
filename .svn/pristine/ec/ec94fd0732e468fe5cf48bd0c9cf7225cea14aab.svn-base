package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by 300151 on 11/30/2016.
 */
public class DatasheetMainActivity extends AppCompatActivity {
    String SourceId, FormId, callid, calltype, firmname;
    SharedPreferences userpreferences;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InitView();
        if (getIntent().hasExtra("SourceId")) {
            Intent i = getIntent();

            SourceId = i.getStringExtra("SourceId");
            callid = i.getStringExtra("callid");
            calltype = i.getStringExtra("calltype");
            firmname = i.getStringExtra("firmname");

        }
        if (isnet()) {
            new StartSession(DatasheetMainActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetformData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    private void InitView() {
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        sql = db.getWritableDatabase();
    }

    class DownloadGetformData extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_get_formData +
                        "?sourceId=" + URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url);
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONObject jorder = new JSONObject(res);
                ContentValues values = new ContentValues();
                sql.delete(db.TABLE_FORM_DATA, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_FORM_DATA, null);
                int count = c.getCount();
                String columnName, columnValue;


                for (int j = 0; j < c.getColumnCount(); j++) {
                    columnName = c.getColumnName(j);
                    if (columnName.equalsIgnoreCase("FKCssFormsId")) {
                        FormId = jorder.getString(columnName);

                    }
                    columnValue = jorder.getString(columnName);
                    values.put(columnName, columnValue);
                }
                long a = sql.insert(db.TABLE_FORM_DATA, null, values);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("FKCssFormsId")) {
                new StartSession(DatasheetMainActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        //new DownloadDatasheetGetData().execute();
                        new DownloadDatasheetMode().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }

        }
    }

    class DownloadDatasheetMode extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_DatasheetMode + "?sourceId=" +
                        URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url);
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                //    JSONObject jorder = new JSONObject(res);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (res.contains("A")) {
                Intent intent = new Intent(DatasheetMainActivity.this, AddDatasheetActivityMain.class);
                intent.putExtra("callid", callid);
                intent.putExtra("calltype", calltype);
                intent.putExtra("firmname", firmname);
                intent.putExtra("SourceId", SourceId);
                intent.putExtra("FormId", FormId);
                startActivity(intent);
                finish();
            }
         /*   if (res.contains("E")) {
                Intent intent = new Intent(DatasheetMainActivity.this, EditDatasheetActivityMain.class);
                intent.putExtra("callid", callid);
                intent.putExtra("calltype", calltype);
                intent.putExtra("firmname", firmname);
                intent.putExtra("SourceId", SourceId);
                intent.putExtra("FormId", FormId);
                startActivity(intent);
                finish();
            }*/

        }
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
