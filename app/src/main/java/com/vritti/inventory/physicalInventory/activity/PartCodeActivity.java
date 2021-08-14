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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.adapter.PartCodeNameAdapter;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class PartCodeActivity extends AppCompatActivity {
    EditText edtsearchcode;
    ListView listpartcode;
    ImageView img_barcode;
    Button btnocr;
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
    String partName = "", partCode = "", itemPlantID = "", PurUnit = "", uomval = "", StockUnit = "", WareHouseMasterId = "",
            LocationMasterId = "", WarehouseCode = "",LocationCode = "";
    public static final int REQ_PARTCODE = 7;
    private static final int RC_OCR_CAPTURE = 9003;

    PartCodeNameAdapter padapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_code);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        if (cf.getGetItemcount()>0){
            displayProduct();
        }else {
            if (isnet()) {
                new StartSession(PartCodeActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                       // new DownloadGetItemlistJSON().execute();
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
        listpartcode = findViewById(R.id.listpartcode);
       // listpartcode.setVisibility(View.GONE);
        img_barcode = findViewById(R.id.img_barcode);
        btnocr = findViewById(R.id.btnocr);
        rdgrpsearch = findViewById(R.id.rdgrpsearch);
        rdbtnsearch_startswith = findViewById(R.id.rdbtnsearch_1);
        rdbtnsearch_random = findViewById(R.id.rdbtnsearch_2);

        ut = new Utility();
        cf = new CommonFunction(PartCodeActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(PartCodeActivity.this);
        String dabasename = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(PartCodeActivity.this, dabasename);
        CompanyURL = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(PartCodeActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(PartCodeActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        ItemCodelist = new ArrayList<PartCodeName>();

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartCodeActivity.this);
        searchStatus = sharedPrefs.getString("searchStatus", "");

    }

    public void setListeners(){
        rdbtnsearch_startswith.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    rdbtnsearch_startswith.setChecked(true);
                    searchStatus = rdbtnsearch_startswith.getText().toString();

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartCodeActivity.this);
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

                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(PartCodeActivity.this);
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
                /*padapter.filter((edtsearchcode)
                        .getText().toString().trim()
                        .toLowerCase(Locale.getDefault()));*/
                if(searchStatus.equalsIgnoreCase("StartWith")){
                    //set search startwith
                    padapter.filter_startwith((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                 //   listpartcode.setVisibility(View.VISIBLE);
                }else if(searchStatus.equalsIgnoreCase("Float")){
                    //set search float
                    padapter.filter((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                 //   listpartcode.setVisibility(View.VISIBLE);
                }else {
                    searchStatus = "Float";
                    padapter.filter((edtsearchcode)
                            .getText().toString().trim()
                            .toLowerCase(Locale.getDefault()));
                 //   listpartcode.setVisibility(View.GONE);
                }

            }
        });

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(PartCodeActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        listpartcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                partCode = ItemCodelist.get(position).getPartCode();
                //itemPlantID = ItemCodelist.get(position).getItemPlantID();
                GetItemCode(partCode);
            }
        });

        btnocr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(PartCodeActivity.this, OcrCaptureActivity.class);
               // intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus.isChecked());
               // intent.putExtra(OcrCaptureActivity.UseFlash, useFlash.isChecked());
                startActivityForResult(intent, RC_OCR_CAPTURE);*/
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

        padapter = new PartCodeNameAdapter(this, ItemCodelist);
        listpartcode.setAdapter(padapter);

        /*ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(PIEntryPrintingActivity.this,
                R.layout.crm_custom_spinner_txt,ItemDesclist);
        edt_description.setAdapter(stringArrayAdapter);//SF0006*/

        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
    }

    private void GetItemCode(String itemcode) {
        //String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode like '%" + itemcode + "%'";
        String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode='"+itemcode+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                partName = cur.getString(cur.getColumnIndex("ItemDesc"));
                itemPlantID = cur.getString(cur.getColumnIndex("ItemPlantId"));
                PurUnit = cur.getString(cur.getColumnIndex("PurUnit"));
                StockUnit = cur.getString(cur.getColumnIndex("StockUnit"));
                WareHouseMasterId = cur.getString(cur.getColumnIndex("WareHouseMasterId"));
                LocationMasterId = cur.getString(cur.getColumnIndex("LocationMasterId"));
                WarehouseCode = cur.getString(cur.getColumnIndex("WarehouseCode"));
                LocationCode = cur.getString(cur.getColumnIndex("LocationCode"));
              //  Toast.makeText(this, "read text - "+partName,Toast.LENGTH_SHORT).show();

                uomval = getUOMVAl(PurUnit);

                try{
                    String cnv = cur.getString(cur.getColumnIndex("ConvFactor"));
                    if(cnv.equalsIgnoreCase("null")){
                        CONV_factor = 0;
                    }else {
                        CONV_factor = Double.parseDouble(cnv);
                    }

                    /*if(CONV_factor == 1){
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        addressLayout.setAlpha((float) 0.3);
                        edt_weight.setEnabled(false);
                        edt_weight.setFocusable(false);
                        edt_weight.setClickable(false);
                    }else if(CONV_factor > 1) {
                        edt_qty.setEnabled(false);
                        addressLayout.setVisibility(View.VISIBLE);
                        addressLayout.setAlpha(1);
                        edt_weight.setEnabled(true);
                        edt_weight.setFocusable(true);
                        edt_weight.setClickable(true);
                    }else {
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                    }*/

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
                    setResult(REQ_PARTCODE, intent);
                   // Toast.makeText(this, "getting back- "+partName,Toast.LENGTH_SHORT).show();
                    finish();

                }catch (Exception e){
                    e.printStackTrace();
                }

            } while (cur.moveToNext());
        }else{
            Toast.makeText(this, "Scanned code not found", Toast.LENGTH_SHORT).show();
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
                    progressDialog = new ProgressDialog(PartCodeActivity.this);
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
                res = ut.OpenConnection(url, PartCodeActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");

            } else {
                Log.e("Scan", "Scanned");

               // edt_itemcode.setText(result.getContents());
                partCode = result.getContents();
                GetItemCode(partCode);
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = "";
                    partCode = "";
                   // text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    //statusMessage.setText(R.string.ocr_success);
                   // textValue.setText(text);
                   // Log.d(TAG, "Text read: " + text);
                    /*HSP0004*/

                    partCode = text;

                    if(partCode.contains("HSP")){

                        if(String.valueOf(partCode.charAt(3)).equals("O")){
                            partCode = partCode.replace("O","0");
                        }else {
                        }

                    }else {

                    }

                    GetItemCode(partCode);

                  //  Toast.makeText(this, "read text1 - "+partCode,Toast.LENGTH_SHORT).show();
                } else {
                    //statusMessage.setText(R.string.ocr_failure);
                  //  Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
                /*statusMessage.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));*/
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
