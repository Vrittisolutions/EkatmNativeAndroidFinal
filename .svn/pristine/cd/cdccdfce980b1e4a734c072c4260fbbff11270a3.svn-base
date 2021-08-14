package com.vritti.vwblib.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.Adapter.DatasheetQueListAdapter;
import com.vritti.vwblib.Beans.Datasheet;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.SendOfflineData;
import com.vritti.vwblib.classes.CommonFunction;

/**
 * Created by 300151 on 12/5/2016.
 */
public class AddDatasheetActivityMain extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    SQLiteDatabase sql;
    String  FormId, FKQuesId, SourceId, ActivityId, ActivityName;
    SharedPreferences userpreferences;
    Button btn_save, btn_return;
    ListView lv_queList;
    JSONArray DatasheetAnsDataFinal;
    JSONObject DatasheetAnsData, DatasheetFinalobj;
    String FinalObj;
    public static ArrayList<Datasheet> datasheetlists = new ArrayList<Datasheet>();
    private String remark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_add_datasheet_main);
        InitView();
        SetListner();

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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();

        if (getIntent().hasExtra("FormId")) {
            Intent i = getIntent();
            FormId = i.getStringExtra("FormId");
            SourceId = i.getStringExtra("SourceId");
            ActivityId = i.getStringExtra("ActivityId");
            ActivityName = i.getStringExtra("ActivityName");
        }
        if (cf.check_DatasheetQueList(FormId) > 0) {
            UpdateDatasheet();
        } else {
            new StartSession(AddDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadDatasheetGetData().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(),msg);
                }
            });
        }
    }

    private void getDatasheetJSONObj() {
        try {
            for (int i = 0; i < datasheetlists.size(); i++) {
                DatasheetAnsData = new JSONObject();

                DatasheetAnsData.put("QuesText", datasheetlists.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");
                DatasheetAnsData.put("ResponseByCustomer", datasheetlists.get(i).getAnswer().toString());
                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", datasheetlists.get(i).getPKCssFormsQuesID());
                if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (datasheetlists.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", datasheetlists.get(i).getFKQuesId().toString());
                DatasheetAnsData.put("PKCssDtlsID", datasheetlists.get(i).getDetailid().toString());
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "A");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            DatasheetFinalobj.put("ActivityId", ActivityId);
            DatasheetFinalobj.put("Module", "VWB");
            DatasheetFinalobj.put("CallId", " ");
            DatasheetFinalobj.put("FormDesc", ActivityName);
            FinalObj = DatasheetFinalobj.toString();
            FinalObj = FinalObj.replaceAll("\\\\", "");

            String status="";
            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            String op =status;
            remark="Datasheet save successfully for " + ActivityName;
            CreateOfflinedatasheetfill(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

           /* new StartSession(AddDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UploadDatasheet().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(),msg);
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetListner() {
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatasheetJSONObj();
            }
        });
        lv_queList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aa = datasheetlists.get(position).getQuesText();
                String que = "SELECT FormId,FKQuesId FROM " + db.TABLE_DATASHEET_DATA + " WHERE QuesText LIKE '" + aa + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        FormId = cur.getString(cur.getColumnIndex("FormId"));
                        FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                    } while (cur.moveToNext());
                    Intent intent = new Intent(AddDatasheetActivityMain.this, DatasheetAddDetailActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("FormId", FormId);
                    intent.putExtra("FKQuesId", FKQuesId);
                    startActivity(intent);
                    //  finish();
                } else {
                    Toast.makeText(AddDatasheetActivityMain.this, "Please refresh data", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle("  Add datasheet");
        toolbar.setTitleTextColor(Color.WHITE);
        lv_queList = (ListView) findViewById(R.id.lv_queList);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_return = (Button) findViewById(R.id.btn_return);
        datasheetlists = new ArrayList<Datasheet>();

        DatasheetAnsDataFinal = new JSONArray();
        DatasheetFinalobj = new JSONObject();
    }

    private void UpdateDatasheet() {
        datasheetlists.clear();
        datasheetlists = getdata();
        DatasheetQueListAdapter dataAdapter = new DatasheetQueListAdapter(AddDatasheetActivityMain.this, datasheetlists);
        lv_queList.setAdapter(dataAdapter);
    }

    private ArrayList<Datasheet> getdata() {
        ArrayList<Datasheet> results = new ArrayList<Datasheet>();

        Datasheet datasheet;

        Cursor c = sql
                .rawQuery(
                        "SELECT * FROM " + db.TABLE_DATASHEET_DATA + " WHERE FormId='" + FormId + "'", null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                datasheet = new Datasheet();

                datasheet
                        .setFKQuesId(c.getString(c.getColumnIndex("FKQuesId")));
                datasheet.setSequenceNo(Integer.parseInt(c.getString(c
                        .getColumnIndex("SequenceNo"))));
                datasheet
                        .setQuesText(c.getString(c.getColumnIndex("QuesText")));
                datasheet
                        .setValueMax(c.getString(c.getColumnIndex("ValueMax")));
                datasheet.setSelectionValue(c.getString(c
                        .getColumnIndex("SelectionValue")));
                datasheet.setWeightage(c.getString(c
                        .getColumnIndex("Weightage")));
                datasheet.setPKCssFormsQuesID(c.getString(c
                        .getColumnIndex("PKCssFormsQuesID")));
                datasheet.setIsResponseMandatory(c.getString(c
                        .getColumnIndex("IsResponseMandatory")));
                datasheet.setSelectionText(c.getString(c
                        .getColumnIndex("SelectionText")));
                datasheet
                        .setValueMin(c.getString(c.getColumnIndex("ValueMin")));
                datasheet.setSelectionType(c.getString(c
                        .getColumnIndex("SelectionType")));
                datasheet.setMaxValueText(c.getString(c
                        .getColumnIndex("MaxValueText")));
                datasheet.setControlWidth(c.getString(c
                        .getColumnIndex("ControlWidth")));
                datasheet.setMaxNoOfResponses(c.getString(c
                        .getColumnIndex("MaxNoOfResponses")));
                datasheet.setResponseType(c.getString(c
                        .getColumnIndex("ResponseType")));
                datasheet.setNotes(c.getString(c.getColumnIndex("Notes")));
                datasheet.setFKPrimaryQuesId(c.getString(c
                        .getColumnIndex("FKPrimaryQuesId")));
                datasheet.setFKSecondaryQuesId(c.getString(c
                        .getColumnIndex("FKSecondaryQuesId")));
                datasheet.setDisableQuesStr(c.getString(c
                        .getColumnIndex("DisableQuesStr")));
                datasheet.setGroupID(c.getString(c.getColumnIndex("GroupID")));
                datasheet.setGroupName(c.getString(c
                        .getColumnIndex("GroupName")));
                datasheet
                        .setQuesCode(c.getString(c.getColumnIndex("QuesCode")));
                datasheet.setMaxExpectedResponse(c.getString(c
                        .getColumnIndex("MaxExpectedResponse")));
                datasheet.setResponseValue(c.getString(c
                        .getColumnIndex("ResponseValue")));
                datasheet.setPKCssFormsQuesID(c.getString(c.getColumnIndex("PKCssFormsQuesID")));
                results.add(datasheet);

            } while (c.moveToNext());

        } else {


        }
        return results;

    }

    class UploadDatasheet extends AsyncTask<Integer, Void, Integer> {
        Object respond;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("")) {

                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                        new String[]{ActivityId});
                Toast.makeText(AddDatasheetActivityMain.this, "Datasheet Uploaded successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();
                dismissProgressDialog();

            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            try {
                FinalObj = DatasheetFinalobj.toString();
                FinalObj = FinalObj.replaceAll("\\\\", "");
                respond = ut.OpenPostConnection(url, FinalObj,getApplicationContext());
                res = respond.toString();
                res = res.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void showProgressDialog() {


    }

    private void dismissProgressDialog() {

    }

    class DownloadDatasheetGetData extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
            String url = CompanyURL + WebUrlClass.api_Datasheet_GetData + "?FormId=" + URLEncoder.encode(FormId, "UTF-8");
                res = ut.OpenConnection(url,getApplicationContext());
                res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);
                ContentValues values = new ContentValues();
                sql.delete(db.TABLE_DATASHEET_DATA, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_DATASHEET_DATA, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("FormId")) {
                            columnValue = FormId;
                        } else {
                            columnValue = jorder.getString(columnName);
                        }


                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_DATASHEET_DATA, null, values);
                    Log.e("",""+a);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            UpdateDatasheet();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddDatasheetActivityMain.this, ActivityMain.class);
        startActivity(intent);
        finish();

    }

    private void CreateOfflinedatasheetfill(final String url, final String parameter,
                                                 final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            SQLiteDatabase sql1 = db.getWritableDatabase();
           /* sql1.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?",
                    new String[]{ActivityId});*/
            Toast.makeText(AddDatasheetActivityMain.this, "Datasheet save successfully", Toast.LENGTH_LONG).show();
          //  dismissProgressDialog();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            startService(intent1);
            onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved ", Toast.LENGTH_LONG).show();


        }

    }

}
