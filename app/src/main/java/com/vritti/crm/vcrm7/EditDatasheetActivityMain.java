package com.vritti.crm.vcrm7;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vritti.crm.adapter.EditDatasheetAdapter;
import com.vritti.crm.bean.EditDatasheet;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 300151 on 12/12/2016.
 */
public class EditDatasheetActivityMain extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    String FormId, SourceId, ActivityId, FKQuesId;

    public static List<EditDatasheet> editDatasheetslist;
    ListView lstquestions_editdatasheet;
    SQLiteDatabase sql;
    SharedPreferences userpreferences;
    EditDatasheet editDatasheet;
    EditDatasheetAdapter editDatasheetAdapter;
    Button btnSave, btnReturn;
    JSONObject DatasheetAnsData, DatasheetFinalobj;
    JSONArray DatasheetAnsDataFinal;
    String FinalObj, callid, calltype, firmname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_editdatasheet);
        InitView();
        SetListner();

        if (getIntent().hasExtra("FormId")) {
            Intent i = getIntent();
            FormId = i.getStringExtra("FormId");
            SourceId = i.getStringExtra("SourceId");
            callid = i.getStringExtra("callid");
            calltype = i.getStringExtra("calltype");
            firmname = i.getStringExtra("firmname");
        }
        Toast.makeText(EditDatasheetActivityMain.this, "In Edit Datasheet", Toast.LENGTH_LONG).show();
        new DownloadDatasheetGetData().execute();

    }

    private void SetListner() {
        lstquestions_editdatasheet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String aa = editDatasheetslist.get(position).getQuesText();
                String que = "SELECT FormId,FKQuesId FROM " + db.TABLE_EDIT_DATASHEET + " WHERE QuesText LIKE '" + aa + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        FormId = cur.getString(cur.getColumnIndex("FormId"));
                        FKQuesId = cur.getString(cur.getColumnIndex("FKQuesId"));
                    } while (cur.moveToNext());
                    Intent intent = new Intent(EditDatasheetActivityMain.this, EditDatasheetDetailActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("FormId", FormId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("FKQuesId", FKQuesId);
                    startActivity(intent);
                    //finish();
                } else {
                    Toast.makeText(EditDatasheetActivityMain.this, "Please refresh data", Toast.LENGTH_LONG).show();
                }

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatasheetJSONObj();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(EditDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();*/
            }
        });
    }

    private void getDatasheetJSONObj() {
        try {
            for (int i = 0; i < editDatasheetslist.size(); i++) {
                DatasheetAnsData = new JSONObject();

                DatasheetAnsData.put("QuesText", editDatasheetslist.get(i).getQuesText().toString());
                DatasheetAnsData.put("Remarks", " ");
                DatasheetAnsData.put("ResponseByCustomer", editDatasheetslist.get(i).getAnswer().toString());
                DatasheetAnsData.put("FKCssHeaderId", SourceId);
                DatasheetAnsData.put("PKCssFormsQuesID", editDatasheetslist.get(i).getPKCssFormsQuesID());
                if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Text")) {
                    DatasheetAnsData.put("Flag", "T");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Selection")) {
                    DatasheetAnsData.put("Flag", "R");
                } else if (editDatasheetslist.get(i).getResponseType().equalsIgnoreCase("Numeric")) {
                    DatasheetAnsData.put("Flag", "T");
                } else {

                }
                DatasheetAnsData.put("FKQuesId", editDatasheetslist.get(i).getFKQuesId().toString());
                DatasheetAnsData.put("PKCssDtlsID", editDatasheetslist.get(i).getDetailid().toString());
                DatasheetAnsDataFinal.put(DatasheetAnsData);


            }
            DatasheetFinalobj.put("FormId", FormId);
            DatasheetFinalobj.put("Mode", "E");
            DatasheetFinalobj.put("DatasheetAnsDataFinal", DatasheetAnsDataFinal);
            DatasheetFinalobj.put("ActivityId", "");
            DatasheetFinalobj.put("CallId", callid);
            DatasheetFinalobj.put("Module", "CRM");
            DatasheetFinalobj.put("FormDesc", firmname);
            new StartSession(EditDatasheetActivityMain.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new UploadDatasheet().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UploadDatasheet extends AsyncTask<Integer, Void, Integer> {
        Object respond;
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("")) {
                Toast.makeText(EditDatasheetActivityMain.this, "Datasheet Uploaded successfully", Toast.LENGTH_LONG).show();
              /*  Intent intent = new Intent(EditDatasheetActivityMain.this, ActivityMain.class);
                startActivity(intent);
                finish();
*/
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            String url = CompanyURL + WebUrlClass.api_save_datasheet;
            try {
                FinalObj = DatasheetFinalobj.toString();
                FinalObj = FinalObj.replaceAll("\\\\", "");
                respond = ut.OpenPostConnection(url, FinalObj,EditDatasheetActivityMain.this);
                res = respond.toString();
                res = res.replaceAll("\"", "");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);

        toolbar.setTitleTextColor(Color.WHITE);
        lstquestions_editdatasheet = (ListView) findViewById(R.id.lstquestions_editdatasheet);
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
        btnReturn = (Button) findViewById(R.id.btnReturn);
        btnSave = (Button) findViewById(R.id.btnSave);
        DatasheetAnsDataFinal = new JSONArray();
        DatasheetFinalobj = new JSONObject();
    }

    private void updatelist() {

        editDatasheetslist = getdata();

        editDatasheetAdapter = new EditDatasheetAdapter(
                EditDatasheetActivityMain.this, editDatasheetslist);
        lstquestions_editdatasheet.setAdapter(editDatasheetAdapter);

    }

    class DownloadDatasheetGetData extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            String url = CompanyURL + WebUrlClass.api_edit_datasheet + "?FormId=" +
                    FormId + "&HeaderId=" + SourceId;


            try {
                res = ut.OpenConnection(url);
               /* res = res.toString().replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);*/
                JSONArray jResults = new JSONArray(res);
                ContentValues values = new ContentValues();
                sql.delete(db.TABLE_EDIT_DATASHEET, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_EDIT_DATASHEET, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int index = 0; index < jResults.length(); index++) {
                    JSONObject jorder = jResults.getJSONObject(index);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        if (columnName.equalsIgnoreCase("SourceId")) {
                            columnValue = SourceId;
                        } else if (columnName.equalsIgnoreCase("FormId")) {
                            columnValue = FormId;
                        } else {
                            columnValue = jorder.getString(columnName);
                        }


                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_EDIT_DATASHEET, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            updatelist();

        }
    }


    private ArrayList<EditDatasheet> getdata() {
        ArrayList<EditDatasheet> results = new ArrayList<EditDatasheet>();


        Cursor c = sql
                .rawQuery(
                        "SELECT distinct SequenceNo, ExpectedResponse, QuesText ,"
                                + "FKQuesId, PKCssFormsQuesID, Weightage, IsResponseMandatory,"
                                + "SelectionText, SelectionValue, ValueMin, ValueMax, MaxValueText,"
                                + "ResponseType, ResponseValue, SelectionType, ControlWidth,"
                                + "MaxNoOfResponses, MaxExpectedResponse, pkcssdtlsid, FKCSSheaderid,"
                                + "	responsebycustomer, selectionvalue1,SourceId from " + db.TABLE_EDIT_DATASHEET + " where SourceId ='"
                                + SourceId + "'  ORDER BY SequenceNo asc",
                        null);

        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                editDatasheet = new EditDatasheet();
                editDatasheet.setSequenceNo(Integer.parseInt(c.getString(c
                        .getColumnIndex("SequenceNo"))));
                editDatasheet.setExpectedResponse(c.getString(c
                        .getColumnIndex("ExpectedResponse")));
                editDatasheet.setQuesText(c.getString(c
                        .getColumnIndex("QuesText")));
                editDatasheet.setFKQuesId(c.getString(c
                        .getColumnIndex("FKQuesId")));
                editDatasheet.setPKCssFormsQuesID(c.getString(c
                        .getColumnIndex("PKCssFormsQuesID")));
                editDatasheet.setWeightage(c.getString(c
                        .getColumnIndex("Weightage")));
                editDatasheet.setIsResponseMandatory(c.getString(c
                        .getColumnIndex("IsResponseMandatory")));
                editDatasheet.setSelectionText(c.getString(c
                        .getColumnIndex("SelectionText")));
                editDatasheet.setSelectionValue(c.getString(c
                        .getColumnIndex("SelectionValue")));
                editDatasheet.setValueMin(c.getString(c
                        .getColumnIndex("ValueMin")));
                editDatasheet.setValueMax(c.getString(c
                        .getColumnIndex("ValueMax")));
                editDatasheet.setMaxValueText(c.getString(c
                        .getColumnIndex("MaxValueText")));
                editDatasheet.setResponseType(c.getString(c
                        .getColumnIndex("ResponseType")));
                editDatasheet.setResponseValue(c.getString(c
                        .getColumnIndex("ResponseValue")));
                editDatasheet.setSelectionType(c.getString(c
                        .getColumnIndex("SelectionType")));
                editDatasheet.setControlWidth(c.getString(c
                        .getColumnIndex("ControlWidth")));
                editDatasheet.setMaxNoOfResponses(c.getString(c
                        .getColumnIndex("MaxNoOfResponses")));
                editDatasheet.setMaxExpectedResponse(c.getString(c
                        .getColumnIndex("MaxExpectedResponse")));
                editDatasheet.setPkcssdtlsid(c.getString(c
                        .getColumnIndex("PKCssDtlsID")));
                editDatasheet.setFKCSSheaderid(c.getString(c
                        .getColumnIndex("FKCssHeaderId")));
                editDatasheet.setResponsebycustomer(c.getString(c
                        .getColumnIndex("ResponseByCustomer")));
                editDatasheet.setAnswer(c.getString(c
                        .getColumnIndex("ResponseByCustomer")));
                editDatasheet.setSelectionvalue1(c.getString(c
                        .getColumnIndex("SelectionValue1")));
                editDatasheet.setActivityid(c.getString(c
                        .getColumnIndex("SourceId")));

                results.add(editDatasheet);

            } while (c.moveToNext());

        } else {

        }
        return results;

    }

}
