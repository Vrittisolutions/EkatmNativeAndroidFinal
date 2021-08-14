package com.vritti.inventory.physicalInventory.activity;

import android.app.ProgressDialog;
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
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.adapter.PartNameAdapter;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class PartNameActivity extends AppCompatActivity {
    EditText edtsearchcode;
    ListView listpartname;
    RadioGroup rdgrpsearch;
    AppCompatRadioButton rdbtnsearch_startswith, rdbtnsearch_random;

    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<PartCodeName> ItemCodelist;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount;
    String searchStatus = "";

    private SharedPreferences sharedPrefs;
    Gson gson;
    private String json;
    Type type;

    double CONV_factor = 1;
    String partName = "", partCode = "", itemPlantID = "", PurUnit = "", uomval = "",StockUnit = "", WareHouseMasterId = "",
            LocationMasterId = "", WarehouseCode = "",LocationCode = "";
    public static final int REQ_PARTNAME = 8;

    PartNameAdapter padapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_name);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if (cf.getGetItemcount()>0){
            displayProduct();
        }else {
            if (isnet()) {
                new StartSession(PartNameActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        //new DownloadGetItemlistJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        setListeners();

    }

    public void init(){
        edtsearchcode = findViewById(R.id.edtsearchcode);
        listpartname = findViewById(R.id.listpartname);
        rdgrpsearch = findViewById(R.id.rdgrpsearch);
        rdbtnsearch_startswith = findViewById(R.id.rdbtnsearch_1);
        rdbtnsearch_random = findViewById(R.id.rdbtnsearch_2);

        ut = new Utility();
        cf = new CommonFunction(PartNameActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(PartNameActivity.this);
        String dabasename = ut.getValue(PartNameActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PartNameActivity.this, dabasename);
        CompanyURL = ut.getValue(PartNameActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PartNameActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PartNameActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PartNameActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(PartNameActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PartNameActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PartNameActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        ItemCodelist = new ArrayList<PartCodeName>();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartNameActivity.this);
        searchStatus = sharedPrefs.getString("searchStatus", "");
    }

    public void setListeners(){

        rdbtnsearch_startswith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rdbtnsearch_startswith.setChecked(true);
                    searchStatus = rdbtnsearch_startswith.getText().toString();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartNameActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("searchStatus","StartWith");
                    editor.commit();

                    /*rdgrpsearch.setEnabled(false);
                    rdbtnsearch_random.setEnabled(false);
                    rdbtnsearch_startswith.setEnabled(false);
                    rdgrpsearch.setAlpha(Float.parseFloat("0.5"));*/

                }else {
                }
            }
        });

        rdbtnsearch_random.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rdbtnsearch_random.setChecked(true);
                    searchStatus = rdbtnsearch_random.getText().toString();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartNameActivity.this);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putString("searchStatus","Float");
                    editor.commit();


                }else {
                }
            }
        });

        edtsearchcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               /* padapter.filter((edtsearchcode)
                        .getText().toString().trim()
                        .toLowerCase(Locale.getDefault()));*/

                if(searchStatus.equalsIgnoreCase("StartWith")){
                    //set search startwith
                    padapter.filter_startwith((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                }else if(searchStatus.equalsIgnoreCase("Float")){
                    //set search float
                    padapter.filter((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                }else {
                    searchStatus = "Float";
                    padapter.filter((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                }

            }
        });


        listpartname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                partName = ItemCodelist.get(position).getPartName();
                GetItemDesc(partName);
            }
        });

    }

    private void displayProduct() {

        ItemCodelist.clear();
       // ItemDesclist.clear();

        String query = "SELECT distinct ItemCode,ItemMasterId,ItemDesc,ItemPlantId" +
                " FROM " + db.TABLE_GetItemList;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                //ItemCodelist.add(cur.getString(cur.getColumnIndex("ItemCode")));
                //ItemDesclist.add(cur.getString(cur.getColumnIndex("ItemDesc")));

                PartCodeName pcode = new PartCodeName();
                pcode.setPartCode(cur.getString(cur.getColumnIndex("ItemCode")));
                pcode.setPartName(cur.getString(cur.getColumnIndex("ItemDesc")));
                pcode.setItemPlantID(cur.getString(cur.getColumnIndex("ItemPlantId")));

                ItemCodelist.add(pcode);

                //Productionitems.add(cur.getString(cur.getColumnIndex("ItemCode")));

            } while (cur.moveToNext());

        }

        padapter = new PartNameAdapter(this, ItemCodelist);
        listpartname.setAdapter(padapter);

        /*ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(PIEntryPrintingActivity.this,
                R.layout.crm_custom_spinner_txt,ItemDesclist);
        edt_description.setAdapter(stringArrayAdapter);//SF0006*/

        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
    }

    private void GetItemDesc(String itemdesc) {
        //String query = "SELECT * FROM "  + db.TABLE_GetItemList + " WHERE  ItemDesc like '%" + itemdesc + "%'";
        String query = "SELECT * FROM "  + db.TABLE_GetItemList + " WHERE  ItemDesc='"+itemdesc+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                //edt_weight.setText("");
                //edt_qty.setText("");

                partCode = cur.getString(cur.getColumnIndex("ItemCode"));
                itemPlantID = cur.getString(cur.getColumnIndex("ItemPlantId"));
                PurUnit = cur.getString(cur.getColumnIndex("PurUnit"));
                StockUnit = cur.getString(cur.getColumnIndex("StockUnit"));
                WareHouseMasterId = cur.getString(cur.getColumnIndex("WareHouseMasterId"));
                LocationMasterId = cur.getString(cur.getColumnIndex("LocationMasterId"));
                WarehouseCode = cur.getString(cur.getColumnIndex("WarehouseCode"));
                LocationCode = cur.getString(cur.getColumnIndex("LocationCode"));

                uomval = getUOMVAl(PurUnit);
              //  edt_itemcode.setText(itemdesc1);
                try{
                    CONV_factor = Double.parseDouble(cur.getString(cur.getColumnIndex("ConvFactor")));

                    /*if(CONV_factor == 1){
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        *//*addressLayout.setAlpha((float) 0.3);
                        edt_weight.setEnabled(false);
                        edt_weight.setFocusable(false);
                        edt_weight.setClickable(false);*//*
                    }else if(CONV_factor > 1) {
                        edt_qty.setEnabled(false);
                        addressLayout.setVisibility(View.VISIBLE);
                       *//* addressLayout.setAlpha(1);
                        edt_weight.setEnabled(true);
                        edt_weight.setFocusable(true);
                        edt_weight.setClickable(true);*//*
                    }*/

                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                intent.putExtra("PartCode",partCode);
                intent.putExtra("PartName", partName);
                intent.putExtra("ItemPlantId",itemPlantID);
                intent.putExtra("ConvFactor", String.valueOf(CONV_factor));
                intent.putExtra("uomval", uomval);
                intent.putExtra("WareHouseMasterId", WareHouseMasterId);
                intent.putExtra("WarehouseCode", WarehouseCode);
                intent.putExtra("LocationMasterID", LocationMasterId);
                intent.putExtra("LocationCode", LocationCode);
                setResult(REQ_PARTNAME, intent);
                finish();

            } while (cur.moveToNext());


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

    class DownloadGetItemlistJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(PartNameActivity.this);
                    progressDialog.setMessage("Loading Please wait...");
                    progressDialog.setIndeterminate(false);
                    //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(false);

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_ItemListAndroid;

            try {
                res = ut.OpenConnection(url, PartNameActivity.this);
                if (res!=null) {
                    response = res.toString();

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_GetItemList, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_GetItemList, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_GetItemList, null, values);

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
            progressDialog.dismiss();
            // dismissProgressDialog();
            if (response.contains("")) {

            }else {
                displayProduct();
            }
        }
    }

    public String getUOMVAl(String pur_Unit){
        String qry = "SELECT UOMCode from "+db.TABLE_UOM+" WHERE UOMMasterId='"+pur_Unit+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            uomval = c.getString(c.getColumnIndex("UOMCode"));
        }else {

        }

        return uomval;
    }

}
