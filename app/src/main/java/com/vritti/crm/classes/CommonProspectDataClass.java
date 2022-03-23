package com.vritti.crm.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.vritti.crm.adapter.CountryAdapter;
import com.vritti.crm.adapter.StateAdapter;
import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.State;
import com.vritti.crm.vcrm7.CountryListActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommonProspectDataClass {
    Context parent;
    String loadDataType = "";

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    SQLiteDatabase sql;

    ArrayList<Country> mList = new ArrayList<>();
    ArrayList<State> lstState = new ArrayList<>();

    public static final int COUNTRY = 2;
    public static final int STATE = 3;
    public static final int CITY = 4;
    public static final int TERRITORY = 5;
    public static final int BUSINESSSEGMENT = 5;

    String Stateid="",countryId="";
    private String finalCountryId;

    public CommonProspectDataClass(Context context/*, String loadData*/){
        this.parent = context;
       // this.loadDataType = loadData;

        ut = new Utility();
        cf = new CommonFunctionCrm(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
    }

    public ArrayList<Country> getCountryData(){
        if (cf.getCountrycount() > 0) {
            getCountry();
        } else {
            if (ut.isNet(parent)) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCountryListJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        return mList;

    }

    public ArrayList<State> getStateData(String countryId){
        finalCountryId = countryId;
        if (cf.getStatecount() > 0) {
            getState(countryId);
        } else {
            if (ut.isNet(parent)) {

                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadStatelistJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        return lstState;
    }

    public void getCityData(){

    }

    public void getTerritoryData(){

    }

    private ArrayList<Country> getCountry() {
        mList.clear();
        String query = "SELECT *" +
                " FROM " + db.TABLE_COUNTRY;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                mList.add(new Country(cur.getString(cur.getColumnIndex("PKCountryId")),
                        cur.getString(cur.getColumnIndex("CountryName"))));


            } while (cur.moveToNext());

        }
        return mList;
        /*cAdapter = new CountryAdapter(parent,mList);
        country_list.setAdapter(cAdapter);*/

    }

    class DownloadCountryListJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* if(center_progressbar.getVisibility() == View.VISIBLE){
                dismissProgressDialog();
            }else{*/
           // showProgressDialog();
            //  }

        }

        @Override
        protected String doInBackground(String... params) {

            /*String url = CompanyURL + WebUrlClass.api_get_countrylist;*/
            String url = CompanyURL + WebUrlClass.api_getCountry;

            try {
                res = ut.OpenConnection(url);

                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_COUNTRY, null, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_COUNTRY, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_COUNTRY, null, values);
                    Log.e("country", "" + a);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
       /*     if(center_progressbar.getVisibility() == View.VISIBLE){

            }else {*/
          //  dismissProgressDialog();
            // }
            if (response.contains("PKCountryId")) {

                getCountry();
            } else {

            }

        }

    }

    private ArrayList<State> getState(String countryId) {
        lstState.clear();
        String query = "SELECT distinct PKStateId,StateDesc" + " FROM " + db.TABLE_STATE ;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                State state = new State();
                state.setStateDesc(cur.getString(cur.getColumnIndex("StateDesc")));
                state.setPKStateId(cur.getString(cur.getColumnIndex("PKStateId")));

                lstState.add(state);

            } while (cur.moveToNext());
        }

        return lstState;

    }

    class DownloadStatelistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
            //progressHUD1 = ProgressHUD.show(context, " ", false, false, null);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_get_statelistdata + "?Id=" + finalCountryId;
            // String url = CompanyURL + WebUrlClass.api_get_Statelist;
            try {
                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);

                //sql.delete(db.TABLE_STATE, null, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_STATE, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        if (j == 0)
                            Stateid = columnValue;

                    }

                    long a = sql.insert(db.TABLE_STATE, null, values);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //dismissProgressDialog();
            if (response.equals("") || response.equals("[]")) {

            } else {
                getState(finalCountryId);
            }
        }

    }

}
