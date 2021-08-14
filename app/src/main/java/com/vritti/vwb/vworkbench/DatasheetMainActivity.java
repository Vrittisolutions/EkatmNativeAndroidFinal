package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by 300151 on 11/30/2016.
 */
public class DatasheetMainActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Designation = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    String SourceId, FormId, ActivityId, ActivityName = "", Datasheet = "", AllowDatasheetEntryOn = "", assignNew = "", call_type = "", projmasterId = "", AssignBy = "", AssignById = "";
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Designation = ut.getValue(context, WebUrlClass.GET_Designation, settingKey);


        InitView();
        if (getIntent().hasExtra("SourceId")) {
            Intent i = getIntent();
            if (EnvMasterId.equalsIgnoreCase("pragati")
                    || EnvMasterId.equalsIgnoreCase("Pragati") || EnvMasterId.equalsIgnoreCase("b207")) {
                SourceId = i.getStringExtra("PkCssHeaderID");
            } else {
                SourceId = i.getStringExtra("SourceId");
            }

            FormId = i.getStringExtra("FormId");
            ActivityId = i.getStringExtra("ActivityId");
            ActivityName = i.getStringExtra("ActivityName");
            call_type = i.getStringExtra("call_type");
            projmasterId = i.getStringExtra("projmasterId");
            AssignBy = i.getStringExtra("AssignBy");
            AssignById = i.getStringExtra("AssignById");
            // Datasheet = i.getStringExtra("Datasheet");
        }


        new StartSession(DatasheetMainActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadGetformData().execute();
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(getApplicationContext(), msg);

            }
        });
    }


    private void InitView() {
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

                String url = CompanyURL + WebUrlClass.api_get_formData + "?sourceId=" + URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
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
                    if (columnName.equalsIgnoreCase("AllowDatasheetEntryOn")) {
                        AllowDatasheetEntryOn = jorder.getString(columnName);

                    }
                    if (columnName.equalsIgnoreCase("AssignNew")) {
                        assignNew = jorder.getString(columnName);
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
            if (res != null) {
                if (res.contains("FKCssFormsId")) {
                    new StartSession(DatasheetMainActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            //new DownloadDatasheetGetData().execute();
                            new DownloadDatasheetMode().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);

                        }
                    });
                }
            } else {
                Toast.makeText(context, "Server error please try after sometime", Toast.LENGTH_SHORT).show();
                onBackPressed();
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
                //source id is headerid

                res = ut.OpenConnection(url, getApplicationContext());
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
            if (assignNew.equalsIgnoreCase("yes")) {
                res = "A";
                if (res.contains("A")) {
                    Intent intent = new Intent(DatasheetMainActivity.this, AddDatasheetActivityMain.class);
                    intent.putExtra("FormId", FormId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("Mode", "A");
                    intent.putExtra("AllowDatasheetEntryOn", AllowDatasheetEntryOn);
                    intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + "Activity");// Source Type
                    intent.putExtra("projmasterId", projmasterId);
                    intent.putExtra("AssignBy", AssignBy);
                    intent.putExtra("AssignById", AssignById);
                    intent.putExtra("AssignNew", assignNew);
                    startActivity(intent);
                    finish();
                }
            } else {
                if (res.contains("A")) {
                    Intent intent = new Intent(DatasheetMainActivity.this, AddDatasheetActivityMain.class);
                    intent.putExtra("FormId", FormId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("Mode", "A");
                    intent.putExtra("AllowDatasheetEntryOn", AllowDatasheetEntryOn);
                    intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + "Activity");// Source Type
                    intent.putExtra("projmasterId", projmasterId);
                    intent.putExtra("AssignBy", AssignBy);
                    intent.putExtra("AssignById", AssignById);
                    intent.putExtra("AssignNew", assignNew);
                    startActivity(intent);
                    finish();
                }
                if (res.contains("E")) {

                    Intent intent = new Intent(DatasheetMainActivity.this, EditDatasheetActivityMain.class);
                    intent.putExtra("FormId", FormId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("ActivityId", ActivityId);
                    intent.putExtra("ActivityName", ActivityName);
                    intent.putExtra("AllowDatasheetEntryOn", AllowDatasheetEntryOn);
                    intent.putExtra("call_type", WebUrlClass.AppNameChat + "_" + "Activity");// Source Type
                    intent.putExtra("projmasterId", projmasterId);
                    intent.putExtra("AssignBy", AssignBy);
                    intent.putExtra("AssignById", AssignById);
                    intent.putExtra("Mode", "E");
                    startActivity(intent);
                    finish();
                }

            }
        }
    }


}
