package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.DliveryModule.DeliveryDetailPage;
import com.vritti.crm.adapter.CityAdapter;
import com.vritti.crm.adapter.CountryAdapter;
import com.vritti.crm.adapter.ListDataAdapter;
import com.vritti.crm.adapter.StateAdapter;
import com.vritti.crm.adapter.TerritoryAdapter;
import com.vritti.crm.bean.City;
import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.ListData;
import com.vritti.crm.bean.State;
import com.vritti.crm.bean.Territory;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonProspectDataClass;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryListActivity extends AppCompatActivity {
 
    Context parent;
    ListView country_list;
    ProgressBar progressbar;
    EditText edtsearch;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    SQLiteDatabase sql;

    ArrayList<ListData> tempList = new ArrayList<>();
    ListDataAdapter listAdapter;
    ArrayList<ListData>listDataArrayList;


    String loadData="",countryId="",Stateid="";

    public static final int COUNTRY = 2;
    public static final int STATE = 3;
    public static final int CITY = 4;
    public static final int TERRITORY = 5;
    public static final int BUSINESSSEGMENT = 5;

    CommonProspectDataClass commClass;

    /*R and D data added by Kirti*/
    String TABLE_NAME = "";
    String ColId = "";
    String ColDisp ="";
    String WhereClause_params = "";
    String WhereClause_val = "";
    String APIName = "";
    String APIParams = "";

    ImageView img_add,img_refresh,img_back,img_appotunity_update;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        init();

        AppCommon.getInstance(CountryListActivity.this).onHideKeyBoard(CountryListActivity.this);
        
        //getcountry data
        //getLoadDataValidations();

        R_and_D_Method_getLocatlData();

        setlistener();
    }
    
    public void init(){
        parent = CountryListActivity.this;


        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("");
        setSupportActionBar(toolbar_action);
        
        country_list = findViewById(R.id.country_list);
        progressbar =  findViewById(R.id.progressbar);

        progressbar.setVisibility(View.GONE);
        edtsearch =  findViewById(R.id.edtsearch);

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

        listDataArrayList = new ArrayList<ListData>();
        tempList = new ArrayList<ListData>();

        Intent intent = getIntent();
        TABLE_NAME = intent.getStringExtra("Table_Name");
        ColId = intent.getStringExtra("Id");
        ColDisp = intent.getStringExtra("DispName");
        WhereClause_params = intent.getStringExtra("WHClauseParameter");
       // WhereClause_val = intent.getStringExtra("WHClauseParamVal");
       // APIParams = intent.getStringExtra("APIParameters");
        APIName = intent.getStringExtra("APIName");

        commClass = new CommonProspectDataClass(parent);


        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        if (getIntent().hasExtra("out")){
            if (getIntent().getStringExtra("out").equals("follow")){
                txt_title.setText("Followup purpose");
            }else if (getIntent().getStringExtra("out").equals("reason")){
                txt_title.setText("Reason");
            }else if (getIntent().getStringExtra("out").equals("contact")){
                txt_title.setText("Contact list");
            }else if (getIntent().getStringExtra("out").equals("cam")){
                txt_title.setText("Campaign");
            }
            else if (getIntent().getStringExtra("out").equals("order")){
                txt_title.setText("Order Type");
            }
            else if (getIntent().getStringExtra("out").equals("1")){
                txt_title.setText("Outcome list");
            }
            else if (getIntent().getStringExtra("out").equals("expense")){
                txt_title.setText("Employee name");
            }
            else {
                txt_title.setText("List");
            }

        }else {

            txt_title.setText("List");
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    
    public void setlistener(){

        country_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("ID", listDataArrayList.get(position).getId());
                intent.putExtra("Name", listDataArrayList.get(position).getName());
                setResult(COUNTRY, intent);
                finish();

                /*}else if(TABLE_NAME.equals(db.TABLE_STATE)){
                    intent.putExtra("FkStateId",lstState.get(position).getPKStateId());
                    intent.putExtra("StateName",lstState.get(position).getStateDesc());
                    setResult(STATE,intent);
                }else if(TABLE_NAME.equals(db.TABLE_CITY_ENTITY)){
                    intent.putExtra("PKCityID", lstCity.get(position).getPKCityID());
                    intent.putExtra("CityName", lstCity.get(position).getCityName());
                    setResult(CITY, intent);
                }else if(TABLE_NAME.equals(db.TABLE_Teritory)){
                    intent.putExtra("PKTerritoryId", lstTerrority.get(position).getPKTerritoryId());
                    intent.putExtra("TerritoryName", lstTerrority.get(position).getTerritoryName());
                    setResult(TERRITORY, intent);
                }*/
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    String search="";
                    search = edtsearch.getText().toString();

                    listAdapter.filter(edtsearch.getText().toString());

                    /*if(TABLE_NAME.equals(db.TABLE_COUNTRY)){
                        listAdapter.filter(edtsearch.getText().toString());
                    }else if(TABLE_NAME.equals(db.TABLE_STATE)){
                        listAdapter.filter(edtsearch.getText().toString());
                    }else if(TABLE_NAME.equals(db.TABLE_CITY_ENTITY)){
                        listAdapter.filter(edtsearch.getText().toString());
                    }else if(TABLE_NAME.equals(db.TABLE_Teritory)){
                        listAdapter.filter(edtsearch.getText().toString());
                    }*/
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        
    }

    private void showProgressDialog() {

        progressbar.setVisibility(View.VISIBLE);

    }

    private void dismissProgressDialog() {

        progressbar.setVisibility(View.GONE);

    }

    public void updateList(ArrayList<ListData> arraylist) {
        tempList.clear();
        tempList.addAll(arraylist);
    }

    public void R_and_D_Method_getLocatlData(){
        listDataArrayList.clear();

        String id = "";
        String dispVal = "";

        String qry = "Select "+ColId+" as Id,"+ColDisp+" as Dispname from "+TABLE_NAME+" "+WhereClause_params;
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                id = c.getString(0);
                dispVal = c.getString(1);

                listDataArrayList.add(new ListData(dispVal,id));

               /* if(TABLE_NAME.equals(db.TABLE_COUNTRY)){
                    listDataArrayList.add(new ListData(id,dispVal));
                }else if(TABLE_NAME.equals(db.TABLE_STATE)){
                    listAdapter.add(new State(id,dispVal));
                }else if(TABLE_NAME.equals(db.TABLE_CITY_ENTITY)){
                    City city = new City();
                    city.setCityName(dispVal);
                    city.setPKCityID(id);
                    listAdapter.add(city);
                }else if(TABLE_NAME.equals(db.TABLE_Teritory)){
                    Territory territory = new Territory();
                    territory.setPKTerritoryId(id);
                    territory.setTerritoryName(dispVal);
                    listAdapter.add(territory);
                }*/

            }while (c.moveToNext());

            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);

            /*if(TABLE_NAME.equals(db.TABLE_COUNTRY)){
                listAdapter = new CountryAdapter(parent,mList);
                country_list.setAdapter(listAdapter);
            }else if(TABLE_NAME.equals(db.TABLE_STATE)){
                listAdapter = new StateAdapter(parent,lstState);
                country_list.setAdapter(listAdapter);
            }else if(TABLE_NAME.equals(db.TABLE_CITY_ENTITY)){
                listAdapter = new CityAdapter(parent,lstCity);
                country_list.setAdapter(listAdapter);
            }else if(TABLE_NAME.equals(db.TABLE_Teritory)){
                listAdapter = new TerritoryAdapter(parent,lstTerrority);
                country_list.setAdapter(tAdapter);
            }*/

        }else {
            //call api method
            R_and_D_API_Method();
        }
    }

    public void R_and_D_API_Method(){
        if (ut.isNet(parent)) {
            new StartSession(parent, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadDataJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    class DownloadDataJSON extends AsyncTask<String, Void, String> {
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

            String url = APIName;

            try {

                    res = ut.OpenConnection(url);
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

               /* res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);*/
                JSONArray jResults = new JSONArray(response);
                ContentValues values = new ContentValues();
                sql.delete(TABLE_NAME, null, null);
                Cursor c = sql.rawQuery("SELECT * FROM " + TABLE_NAME, null);
                int count = c.getCount();
                String columnName, columnValue;

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                        /*if (j == 0)
                           // Stateid = columnValue;*/
                    }

                    long a = sql.insert(TABLE_NAME, null, values);

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
               R_and_D_Method_getLocatlData();
            }
        }

    }

}