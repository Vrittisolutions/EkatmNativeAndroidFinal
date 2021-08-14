package com.vritti.inventory.physicalInventory.activity;

import android.app.ProgressDialog;
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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import com.vritti.inventory.physicalInventory.adapter.LocationListAdapter;
import com.vritti.inventory.physicalInventory.bean.BluetoothClass;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudiScreenActivity extends AppCompatActivity {
    private Context parent;
    AutoCompleteTextView edttagno;
    ImageView imgserch, img_barcode;
    LinearLayout addressLayout;
    TextView edt_location, t4,txtuom;
    ImageButton imgedtpartno, imgedtpartdesc, imgedtloc, imgedtarea, imgedtweight,imgedtqty, imgedtverifyby,imgedtcountedby;
    View sheetview;
    Button btncancel, btnsave;
    LinearLayout dtlslay;
    RelativeLayout laytag;
    ScrollView scrollview;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="", UserMasterId="",UserName = "",
            MobileNo = "",Indentamount;
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    EditText edt_qty, edt_weight, edt_countedby, edt_area, edt_verifyby, edt_tag_desc;
  //  Spinner spinner_location;
    TextView edt_itemcode, edt_description;
    ArrayList<String> ItemCodelist;
    ArrayList<String> ItemDesclist;

    public static final int REQUEST_ENABLE_BT = 4;
    public static final int REQUEST_CONNECT_DEVICE = 6;
    public static final int REQ_PARTCODE = 7;
    public static final int REQ_PARTNAME = 8;
    public static final int REQ_LOCATION = 9;

    double CONV_factor = 1;
    String UOMVAL = "";
    String PIHdrId = "", PIDtlId = "", AuditedItemPlantId = "", AuditedItemCode = "", AuditedItemDesc = "", AuditedLocationCode = "",
            AuditedLocationMasterId = "", AuditedWeight = "", AuditedActualQty = "", AuditedVerifyBy = "", TAGNO = "",
            AuditedCountedBy = "", AuditedWareHouseMasterId = "", AuditedWarehouseCode = "", TAGDescription = "";

    String OLDItemPlantId = "", OLDItemCode = "", OLDItemDesc = "", OLDLocationCode = "",OLDLocationMasterId = "",
            OLDWeight = "", OLDActualQty = "", OLDVerifyBy = "", OLDWareHouseMasterId = "", OLDWarehouseCode = "",OLDCountedBy = "";

    String dateStr = "", AuditDATE = "";
    JSONObject jMain = null;

    ArrayList<LocationList>locationListArrayList;
    LocationListAdapter locationListAdapter;

    private SharedPreferences sharedPrefs;
    Gson gson;
    private String json;
    Type type;
    /*int  TagCurrentNo,TagEndNo,TagStartNo;
    int billNO = 0;*/

    String BatchHdID="",LocationID="",LocationCode="";
    String batchHDR = "", batchCODE = "",WareHouseMasterId = "", warehousecode = "", CounterPersonID = "", AuditorPersonID = "";
    int TagStartNo = 0, TagEndNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audi_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();

        setListeners();

        //by default addtextchange is called so write here by validating
        if(edt_weight.isEnabled()){
            //add textchange of edtweight
            edt_weight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String edt = s.toString();

                    if( edt.equals("")){

                    }else {

                        if(CONV_factor == 1 || CONV_factor == 1.0){
                            edt_qty.setText(String.format("%.2f",CONV_factor));
                        }else if(CONV_factor > 1 || CONV_factor > 1.0) {
                            double wt = 0, qty = 0;
                            if(edt_weight.getText().toString().trim() == ""){
                                wt = 0;
                            }else {
                                wt = Double.parseDouble(edt_weight.getText().toString().trim());
                            }

                            qty = wt * CONV_factor;
                            edt_qty.setText(String.format("%.2f",qty));
                        }
                    }
                }
            });
        }
    }

    private void init() {
        parent = AudiScreenActivity.this;

        edttagno = findViewById(R.id.edttagno);
        imgserch = findViewById(R.id.imgserch);
        img_barcode = findViewById(R.id.img_barcode);
        dtlslay = findViewById(R.id.dtlslay);
        laytag = findViewById(R.id.laytag);
        laytag.setVisibility(View.VISIBLE);

        btnsave = findViewById(R.id.btnsave);
        btncancel = findViewById(R.id.btncancel);

        imgedtpartno = findViewById(R.id.imgedtpartno);
        imgedtpartdesc = findViewById(R.id.imgedtpartdesc);
        imgedtloc = findViewById(R.id.imgedtpartloc);
        imgedtarea = findViewById(R.id.imgedtarea);
        imgedtweight = findViewById(R.id.imgedtweight);
        imgedtqty = findViewById(R.id.imgedtqty);
        imgedtverifyby = findViewById(R.id.imgedtverifyby);
        imgedtcountedby = findViewById(R.id.imgedtcountedby);

        scrollview = findViewById(R.id.scrollview);
        scrollview.setVisibility(View.GONE);

        addressLayout = findViewById(R.id.addressLayout);
        edt_itemcode=findViewById(R.id.edt_itemcode);
        edt_itemcode.setEnabled(false);
        edt_description=findViewById(R.id.edt_description);
        edt_description.setEnabled(false);
        edt_qty=findViewById(R.id.edt_qty);
        edt_qty.setEnabled(false);
        edt_weight = findViewById(R.id.txt_weight);
        edt_weight.setEnabled(false);
        edt_location = findViewById(R.id.edt_location);
        edt_location.setEnabled(false);
        edt_countedby = findViewById(R.id.edt_countedby);
        edt_countedby.setEnabled(false);
        edt_area = findViewById(R.id.edt_area);
        edt_area.setEnabled(false);
        edt_verifyby = findViewById(R.id.edt_verifyby);
        edt_tag_desc = findViewById(R.id.edt_tag_desc);
        edt_verifyby.setEnabled(false);
        t4 = findViewById(R.id.t4);
        t4.setVisibility(View.GONE);
        txtuom = findViewById(R.id.txtuom);

      //  spinner_location = findViewById(R.id.spinner_location);

        ut = new Utility();
        cf = new CommonFunction(AudiScreenActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(AudiScreenActivity.this);
        String dabasename = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(AudiScreenActivity.this, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(AudiScreenActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        ItemCodelist = new ArrayList<String>();
        ItemDesclist = new ArrayList<String>();

        Date today = new Date();
        SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-dd");
        AuditDATE = curFormater.format(today);
        Log.e("AuditDATE : ",AuditDATE);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AudiScreenActivity.this);
        gson = new Gson();
        json = sharedPrefs.getString("location", "");
        type = new TypeToken<List<LocationList>>() {}.getType();
        locationListArrayList = gson.fromJson(json, type);

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(AudiScreenActivity.this);
        batchCODE = sharedPrefs.getString("selectedBatchCode", "");
        BatchHdID = sharedPrefs.getString("selectedBatchHDRID", "");
        WareHouseMasterId = sharedPrefs.getString("WareHouseMasterId", "");
        warehousecode = sharedPrefs.getString("warehousecode", "");
        CounterPersonID = sharedPrefs.getString("CounterPersonID","");
        AuditorPersonID = sharedPrefs.getString("AuditorPersonID","");
        TagStartNo = sharedPrefs.getInt("startNo",0);
        TagEndNo = sharedPrefs.getInt("endNo",0);

        if (locationListArrayList == null) {
           // downloadlocationdata();
        }else {
            if (locationListArrayList.size() > 0) {
                locationListAdapter = new LocationListAdapter(AudiScreenActivity.this, locationListArrayList);
                //spinner_location.setAdapter(locationListAdapter);
            }
        }
    }

    private void setListeners() {

        img_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(AudiScreenActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        edt_itemcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudiScreenActivity.this, PartCodeActivity.class);
                startActivityForResult(intent,REQ_PARTCODE);
            }
        });

        edt_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudiScreenActivity.this, PartNameActivity.class);
                startActivityForResult(intent,REQ_PARTNAME);
            }
        });

        edt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudiScreenActivity.this, LocationPIActivity.class);
                startActivityForResult(intent,REQ_LOCATION);
            }
        });

        imgserch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dismissKeyboard(AudiScreenActivity.this);

                laytag.setEnabled(true);
                laytag.setVisibility(View.VISIBLE);

                TAGNO = edttagno.getText().toString().trim();
                AuditedVerifyBy = UserName;

                if(TAGNO.equalsIgnoreCase("") || TAGNO.equalsIgnoreCase(null)){
                    Toast.makeText(parent,"Please enter tag number",Toast.LENGTH_SHORT).show();
                }else {
                    //call API to get Tag details
                    if (isnet()) {
                        new StartSession(AudiScreenActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {

                                new DownloadTagDetails().execute(TAGNO);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }else {
                        Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
                    }
                }/*else {
                    Toast.makeText(parent,"You are not authorised to audit this tag",Toast.LENGTH_SHORT).show();
                }*//*if(Integer.parseInt(TAGNO) > TagStartNo && Integer.parseInt(TAGNO) < TagEndNo)*/


                /*// slide-up animation
                Animation slideUp = AnimationUtils.loadAnimation(parent, R.anim.slide_up);
                if (scrollview.getVisibility() == View.GONE) {
                    scrollview.setVisibility(View.VISIBLE);
                    scrollview.startAnimation(slideUp);
                    scrollview.fullScroll(ScrollView.FOCUS_UP);
                }*/
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createJSON();

                Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);

                if (scrollview.getVisibility() == View.VISIBLE) {
                    scrollview.setVisibility(View.GONE);
                    scrollview.startAnimation(slideDown);
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }

                edttagno.setText("");
                edt_itemcode.setText("");
                edt_description.setText("");
                edt_location.setText("");
                edt_area.setText("");
                edt_weight.setText("");
                edt_qty.setText("");
                edt_countedby.setText("");
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laytag.setVisibility(View.VISIBLE);
                laytag.setEnabled(true);

                Animation slideDown = AnimationUtils.loadAnimation(parent, R.anim.slide_down);

                if (scrollview.getVisibility() == View.VISIBLE) {
                    scrollview.setVisibility(View.GONE);
                    scrollview.startAnimation(slideDown);
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }

                edttagno.setText("");
                edt_itemcode.setText("");
                edt_description.setText("");
                edt_location.setText("");
                edt_area.setText("");
                edt_weight.setText("");
                edt_qty.setText("");
                edt_countedby.setText("");

                finish();
            }
        });

        imgedtpartno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_itemcode.setEnabled(true);
            }
        });

        imgedtpartdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_description.setEnabled(true);
            }
        });

        imgedtloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_location.setEnabled(true);
            }
        });

        imgedtarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_area.setEnabled(true);
            }
        });

        imgedtweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_weight.setEnabled(true);
            }
        });

        imgedtqty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_qty.setEnabled(true);
            }
        });

        imgedtcountedby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_countedby.setEnabled(true);
                edt_countedby.setFocusable(true);
            }
        });

        imgedtverifyby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_verifyby.setEnabled(true);
                edt_verifyby.setFocusable(true);
            }
        });

       /* spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AuditedLocationMasterId = locationListArrayList.get(position).getLocationMasterId();
                AuditedLocationCode = locationListArrayList.get(position).getLocationCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
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

    private void GetItemCode(String itemcode) {
        //String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode like '%" + itemcode + "%'";
        String query = "SELECT * FROM " + db.TABLE_GetItemList + " WHERE  ItemCode='"+itemcode+"'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                edt_weight.setText("");
                edt_qty.setText("");

                String itemcode1=cur.getString(cur.getColumnIndex("ItemDesc"));

                edt_description.setText(itemcode1);
                try{
                    String cnv = cur.getString(cur.getColumnIndex("ConvFactor"));
                    if(cnv.equalsIgnoreCase("null")){
                        CONV_factor = 0;
                    }else {
                        CONV_factor = Double.parseDouble(cnv);
                    }

                    if(CONV_factor == 1){
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        t4.setVisibility(View.VISIBLE);

                    }else if(CONV_factor > 1) {
                        edt_qty.setEnabled(false);
                        addressLayout.setVisibility(View.VISIBLE);
                        t4.setVisibility(View.GONE);

                    }else {
                        edt_qty.setEnabled(true);
                        edt_qty.setClickable(true);
                        addressLayout.setVisibility(View.GONE);
                        t4.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            } while (cur.moveToNext());
        }
    }

    class DownloadTagDetails extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                   /* progressDialog = new ProgressDialog(AudiScreenActivity.this);
                    progressDialog.setMessage("Searching for Tag details Please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);*/

                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {

            TAGNO = params[0];
            String url = CompanyURL + WebUrlClass.api_TagDetails+"?TagNo="+TAGNO;  //RQry,from,to

            try {
                res = ut.OpenConnection(url, AudiScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
           // progressDialog.dismiss();
            getTagDetails(response);

        }
    }

    private void getTagDetails(String response) {
        if(response.equalsIgnoreCase("[]")){
            Toast.makeText(parent,"Sorry, No records found",Toast.LENGTH_SHORT).show();
        }else if(response.equalsIgnoreCase("Already audited")){
            Toast.makeText(parent,"Sorry, Tag Already audited",Toast.LENGTH_SHORT).show();
        }else if(response.equalsIgnoreCase("No Record Found")){
            Toast.makeText(parent,"Sorry, No records found",Toast.LENGTH_SHORT).show();
        } else if (!response.equalsIgnoreCase(null)) {

            JSONArray jResults = null;
            try {
                jResults = new JSONArray(response);

                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);

                    PIHdrId = jorder.getString("PIHdrId");
                    PIDtlId = jorder.getString("PIDtlId");
                    TAGNO = edttagno.getText().toString().trim();
                    AuditedItemCode = jorder.getString("ItemCode");
                    AuditedItemDesc = jorder.getString("ItemDesc");
                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    AuditedLocationCode = jorder.getString("LocationCode");     //todisplay
                    AuditedLocationMasterId = jorder.getString("LocationMasterId");
                    AuditedWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    AuditedWarehouseCode = jorder.getString("WarehouseCode");  //todisplay
                    AuditedWeight = jorder.getString("weight");
                    AuditedActualQty = jorder.getString("ActualQty");
                    AuditedItemPlantId = OLDItemPlantId;
                    AuditedCountedBy = jorder.getString("CountedBy");
                    TAGDescription = jorder.getString("TAGDescription");
                    // AuditedVerifyBy = jorder.getString("ActualQty");

                    OLDItemCode = jorder.getString("ItemCode");
                    OLDItemDesc = jorder.getString("ItemDesc");
                    OLDItemPlantId = jorder.getString("ItemPlantId");
                    OLDLocationCode = jorder.getString("LocationCode");     //todisplay
                    OLDLocationMasterId = jorder.getString("LocationMasterId");
                    OLDWareHouseMasterId = jorder.getString("WareHouseMasterId");
                    OLDWarehouseCode = jorder.getString("WarehouseCode");  //todisplay
                    OLDWeight = jorder.getString("weight");
                    OLDActualQty = jorder.getString("ActualQty");
                    OLDVerifyBy = UserName;
                    OLDCountedBy = "";  //countedbyname

                    try{
                        UOMVAL = getUOMVAL(OLDItemCode);
                        txtuom.setText(UOMVAL);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    CONV_factor = getConvFactor(OLDItemCode);
                    Log.e("CF -",String.valueOf(CONV_factor));
                }

               /* if(BatchHdID.equalsIgnoreCase(PIHdrId)){
                    //authorised to audit tag
                }else {

                }*/

                edt_itemcode.setText(AuditedItemCode);
                edt_description.setText(AuditedItemDesc);
                edt_location.setText(AuditedLocationCode);
                edt_area.setText(AuditedWarehouseCode);
                edt_qty.setText(AuditedActualQty);
                edt_countedby.setText(AuditedVerifyBy);
                edt_verifyby.setText(UserName);
                edt_countedby.setText(AuditedCountedBy);     //set countedpersons name
                edt_tag_desc.setText(TAGDescription);

                if(CONV_factor == 1 || CONV_factor == 1.0){
                    addressLayout.setVisibility(View.GONE);
                    edt_weight.setText(AuditedWeight);
                    t4.setVisibility(View.VISIBLE);
                }else {
                    addressLayout.setVisibility(View.VISIBLE);
                    edt_weight.setText(AuditedWeight);
                    t4.setVisibility(View.GONE);
                }

                  if(AuditedWeight.equalsIgnoreCase("0") || AuditedWeight.equalsIgnoreCase("0.0") ||
                        AuditedWeight.equalsIgnoreCase("0.00")){
                    addressLayout.setVisibility(View.GONE);
                      t4.setVisibility(View.VISIBLE);
                }else {
                    addressLayout.setVisibility(View.VISIBLE);
                    edt_weight.setText(AuditedWeight);
                      t4.setVisibility(View.GONE);
                }

                // slide-up animation
                Animation slideUp = AnimationUtils.loadAnimation(parent, R.anim.slide_up);
                if (scrollview.getVisibility() == View.GONE) {
                    scrollview.setVisibility(View.VISIBLE);
                    scrollview.startAnimation(slideUp);
                    scrollview.fullScroll(ScrollView.FOCUS_UP);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(response.equalsIgnoreCase("error")){
            Toast.makeText(parent,"Server error",Toast.LENGTH_SHORT).show();
        }
    }

    public String locationMasterId(){
       // AuditedLocationMasterId = locationListArrayList.get(spinner_location.getSelectedItemPosition()).getLocationMasterId();
        return AuditedLocationMasterId;
    }

    public String getUOMVAL(String OLDItemCode){
        String Pur_Unit = "", uomval = "";
        String qry = "SELECT PurUnit FROM "+db.TABLE_GetItemList+ " WHERE ItemCode='"+OLDItemCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            Pur_Unit = c.getString(c.getColumnIndex("PurUnit"));

            String query = "SELECT UOMCode from "+db.TABLE_UOM+" WHERE UOMMasterId='"+Pur_Unit+"'";
            Cursor c_ = sql.rawQuery(query,null);
            if(c_.getCount() > 0){
                c_.moveToFirst();
                uomval = c_.getString(c_.getColumnIndex("UOMCode"));
            }else {

            }
        }
        return uomval;
    }

    public void createJSON(){

        PIDtlId = PIDtlId;
        PIHdrId = PIHdrId;
        TAGNO = edttagno.getText().toString().trim();
        AuditedItemCode = edt_itemcode.getText().toString().trim();
        AuditedItemDesc = edt_description.getText().toString().trim();
        AuditedItemPlantId = AuditedItemPlantId;   //getnew items itemplantid
        AuditedLocationCode = edt_location.getText().toString().trim();
       // AuditedLocationCode = spinner_location.getSelectedItem().toString().trim();
       // AuditedLocationMasterId = locationMasterId();  //audited locn masterid    //if spinner
        AuditedLocationMasterId = AuditedLocationMasterId;
        AuditedWarehouseCode = edt_area.getText().toString().trim();
        AuditedWeight = edt_weight.getText().toString().trim();
        AuditedActualQty = edt_qty.getText().toString().trim();
        AuditedVerifyBy = edt_verifyby.getText().toString().trim();
        AuditedCountedBy = edt_countedby.getText().toString().trim();
        TAGDescription = TAGDescription;

        /*{"PIDtlId":"DFDB7816-C210-43B8-8FD4-650344D845ED",
        "PIHdrId":"2B54E440-C844-45BA-B9B1-A0594C94484D",
        "ItemPlantId":"07cf072e-47c6-4ea8-9b78-4b11827d0c67",   //selected items ItemplantId
        "Location":"1",     //locationcode
        "Weight":"45",
        "ActualQty":"450.0",
        "AddedBy":"Ravindra Bhapkar",
        "Printed":"",
        "TagNo":00001,
        "Mode":"E",
        "AuditedDt":"2018-06-01"}

        {
  "PIDtlId": "347t53874567834657365093",
  "PIHdrId": "46537465079364879604360",
  "TagNo": "56346503475984375",
  "ItemPlantId": "73598746436436036",
  "Location": "LOC1",
  "Weight": "45.00",
  "ActualQty": "45.00",
  "AddedBy": "Ravindra Bhapkar",
  "Printed": "",
  "Mode": "E",
  "AuditedDt": "2019-12-07",
  "AuditedBy": "Ravindra Bhapkar",
  "AuditedLocation": "LOC2",
  "AuditedWeight": "30.00",
  "AuditedActualQty": "30:00",
  "AuditedItemPlantId": "7460219-019308244758"
}
*/

        JSONObject jAudit = new JSONObject();

        try{
            jAudit.put("PIDtlId",PIDtlId);
            jAudit.put("PIHdrId",PIHdrId);
            jAudit.put("ItemPlantId",OLDItemPlantId);
            jAudit.put("Location",OLDLocationMasterId);        //display LocationCode, Send LocationMasterId
            jAudit.put("Weight",OLDWeight);
            jAudit.put("ActualQty",OLDActualQty);
            jAudit.put("AddedBy",OLDVerifyBy);
            jAudit.put("Printed","");
            jAudit.put("TagNo",TAGNO);
            jAudit.put("Mode","E");
            jAudit.put("AuditedDt",AuditDATE);
            jAudit.put("AuditedItemPlantId",AuditedItemPlantId);
            jAudit.put("AuditedLocation",AuditedLocationMasterId);
            jAudit.put("AuditedWeight",AuditedWeight);
            jAudit.put("AuditedActualQty",AuditedActualQty);
            jAudit.put("AuditedBy",AuditedVerifyBy);
            jAudit.put("CountedBy",AuditedCountedBy);
            jAudit.put("TAGDescription",TAGDescription);
            //TAGDescription
            //CountedBy

            //Edit
           /* jAudit.put("ItemCode",ItemCode);
            jAudit.put("ItemDesc",ItemDesc);
            jAudit.put("LocationMasterId",LocationMasterId);
            jAudit.put("WareHouseMasterId",WareHouseMasterId);*/

        }catch (Exception e){
            e.printStackTrace();
        }

        jMain = jAudit;

        if (isnet()) {
            new StartSession(AudiScreenActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                   //submit audit details to server
                    new PostAuditData().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }else {
            Toast.makeText(parent,"No internet connection available",Toast.LENGTH_SHORT).show();
        }
    }

    class PostAuditData extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(AudiScreenActivity.this);
                    progressDialog.setMessage("Submitting data please wait...");
                    progressDialog.setIndeterminate(false);
                    progressDialog.setCancelable(false);
                }
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String FinalObj = jMain.toString();

            String url = CompanyURL + WebUrlClass.api_PostPIdetail;

            try {
                res = ut.OpenPostConnection(url,FinalObj, AudiScreenActivity.this);
                if (res!=null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);

                   /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                   */
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
             progressDialog.dismiss();
             respMessage(response);
        }
    }

    public double getConvFactor(String ItemCode){
        //String qry = " SELECT ConvFactor FROM "+db.TABLE_GetItemList+ " WHERE  ItemCode like '%" + ItemCode + "%'";
        String qry = " SELECT ConvFactor FROM "+db.TABLE_GetItemList+ " WHERE  ItemCode='"+ItemCode+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            CONV_factor = c.getInt(c.getColumnIndex("ConvFactor"));
        }else {

        }

        return CONV_factor;
    }

    private void respMessage(String response) {
        JSONObject jobj = null;
        String Result = "";

            if(response != null){
            try {
                jobj = new JSONObject(response);
                Result = jobj.getString("Result");

                    //Result = response.trim();

                    if(Result.equalsIgnoreCase("true")){
                        Toast.makeText(parent,"Tag audited successfully",Toast.LENGTH_SHORT).show();
                    }else if(Result.equalsIgnoreCase("false")) {
                        Toast.makeText(parent,"Tag not audited",Toast.LENGTH_SHORT).show();
                    }else{

                    }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(parent,"Server Error",Toast.LENGTH_SHORT).show();
        }

    }

    public boolean validate(){
        boolean val = false;

        if(edt_itemcode.getText().toString().equalsIgnoreCase("")
                && edt_description.getText().toString().equalsIgnoreCase("")
                && edt_location.getText().toString().equalsIgnoreCase("")
                && edt_area.getText().toString().equalsIgnoreCase("")
                && edt_weight.getText().toString().equalsIgnoreCase("")
                && edt_qty.getText().toString().equalsIgnoreCase("")
                && edt_countedby.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(AudiScreenActivity.this,"Please fill all details",Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if(CONV_factor > 1){
            if(edt_weight.getText().toString().equalsIgnoreCase("") ||
                    edt_weight.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(AudiScreenActivity.this,"Please enter weight",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(AudiScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(AudiScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }else if(CONV_factor == 1){
            if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(AudiScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(AudiScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }else {
            if(edt_qty.getText().toString().equalsIgnoreCase("") ||
                    edt_qty.getText().toString().equalsIgnoreCase(null)){
                Toast.makeText(AudiScreenActivity.this,"Please enter quantity",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            }else if(edt_itemcode.getText().toString().equalsIgnoreCase(edt_location.getText().toString())){
                Toast.makeText(AudiScreenActivity.this,"Part code and location should not be same.",Toast.LENGTH_SHORT).show();
                val = false;
                return val;
            } else {
                val = true;
                return val;
            }
        }
    }

   /* public void dismissKeyboard(Activity parent) {
        InputMethodManager imm = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != parent.getCurrentFocus())
            imm.hideSoftInputFromWindow(parent.getCurrentFocus().getApplicationWindowToken(), 0);

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //scan barcode
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.e("Scan*******", "Cancelled scan");
            } else {
                Log.e("Scan", "Scanned");
                 edttagno.setText(result.getContents());
                 TAGNO = result.getContents();
                 //GetItemCode(result.getContents());
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            BluetoothClass.pairPrinter(getApplicationContext(), AudiScreenActivity.this);
        }/*else if (requestCode == REQUEST_CONNECT_DEVICE && resultCode == RESULT_OK) {
            String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            BluetoothClass.pairedPrinterAddress(getApplicationContext(),AudiScreenActivity.this,address);
        }else if (requestCode == Constants_wifi.REQUEST_CODE_PRINTER && resultCode == Constants_wifi.RESULT_CODE_PRINTER) {
            mPrinterConfiguration = Util_Wifi_print.getWifiConfiguration(AudiScreenActivity.this, Constants_wifi.CONTROLLER_PRINTER);
            doPrint();
        }*/else if(requestCode == REQ_PARTCODE && resultCode == REQ_PARTCODE){
            edt_itemcode.setText(data.getStringExtra("PartCode"));
            edt_description.setText(data.getStringExtra("PartName"));
            AuditedItemPlantId = data.getStringExtra("ItemPlantId");
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            //Toast.makeText(this, "getting back frm ocr",Toast.LENGTH_SHORT).show();
            LocationID = data.getStringExtra("LocationMasterID");
            LocationCode = data.getStringExtra("LocationCode");
            AuditedLocationCode = LocationCode;
            AuditedLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
            UOMVAL = data.getStringExtra("uomval");
            txtuom.setText(UOMVAL);

            if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                addressLayout.setAlpha((float) 0.3);
                edt_weight.setEnabled(false);
                edt_weight.setFocusable(false);
                edt_weight.setClickable(false);
                t4.setVisibility(View.VISIBLE);

            }else if(CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
                edt_qty.setEnabled(false);
                addressLayout.setVisibility(View.VISIBLE);
                addressLayout.setAlpha(1);
                edt_weight.setEnabled(true);
                edt_weight.setFocusable(true);
                edt_weight.setClickable(true);
                t4.setVisibility(View.GONE);
            }else {
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                t4.setVisibility(View.VISIBLE);
            }

        }else if(requestCode == REQ_PARTNAME && resultCode == REQ_PARTNAME){
            edt_itemcode.setText(data.getStringExtra("PartCode"));
            edt_description.setText(data.getStringExtra("PartName"));
            AuditedItemPlantId = data.getStringExtra("ItemPlantId");
            CONV_factor = Double.parseDouble(data.getStringExtra("ConvFactor"));
            LocationID = data.getStringExtra("LocationMasterID");
            LocationCode = data.getStringExtra("LocationCode");
            AuditedLocationCode = LocationCode;
            AuditedLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
            UOMVAL = data.getStringExtra("uomval");
            txtuom.setText(UOMVAL);

            if(CONV_factor == 1 || CONV_factor == 1.0 || CONV_factor == 1.00 || CONV_factor == 1.000 || CONV_factor == 1.0000){
                edt_qty.setEnabled(true);
                edt_qty.setClickable(true);
                addressLayout.setVisibility(View.GONE);
                edt_weight.setEnabled(false);
                edt_weight.setFocusable(false);
                edt_weight.setClickable(false);
                t4.setVisibility(View.VISIBLE);
            }else if(CONV_factor > 1 || CONV_factor > 1.0 || CONV_factor > 1.00 || CONV_factor > 1.000 || CONV_factor > 1.0000) {
                edt_qty.setEnabled(false);
                addressLayout.setVisibility(View.VISIBLE);
                edt_weight.setEnabled(true);
                edt_weight.setFocusable(true);
                edt_weight.setClickable(true);
                t4.setVisibility(View.GONE);
            }
        }else if(requestCode == REQ_LOCATION && resultCode == REQ_LOCATION){
            LocationCode = data.getStringExtra("LocationCode");
            LocationID = data.getStringExtra("LocationMasterID");
            AuditedLocationCode = LocationCode;
            AuditedLocationMasterId = LocationID;
            edt_location.setText(LocationCode);
        }
    }
}