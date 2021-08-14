package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

public class ReimbursementActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    AutoCompleteTextView edt_catdesc;
    EditText edt_travel, edt_stay, edt_food, edt_others;
    Button btnadd, btn_cancel;
    ProgressBar progressBar;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Tbuds_commonFunctions tcf;
    Utility ut;
    SQLiteDatabase sql;

    ArrayList<ConfigDropDownData> categorylist;
    ArrayList<String> tempcategorylist;

    public static final int SALES_REIMBURSEMENT_FILLED = 7;
    String catID = "", catDesc = "";
    JSONObject jobj_remb;
    JSONArray jArray;
    String finalOBJ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimbursement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if(tcf.getCatCnt() > 0){
            getCategoryData();
        }else {
            if (isnet()) {
                new StartSession(parent, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCategoryJSON().execute();
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
        parent = ReimbursementActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Reimbursement");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.toolbar_progress_Assgnwork);
        btnadd = findViewById(R.id.btnadd);
        btn_cancel = findViewById(R.id.btn_cancel);
        edt_catdesc = findViewById(R.id.edt_catdesc);
        edt_travel = findViewById(R.id.edt_travel);
        edt_stay = findViewById(R.id.edt_stay);
        edt_food = findViewById(R.id.edt_food);
        edt_others = findViewById(R.id.edt_others);

        ut = new Utility();
        cf = new CommonFunction(ReimbursementActivity.this);
        tcf = new Tbuds_commonFunctions(ReimbursementActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ReimbursementActivity.this);
        String dabasename = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ReimbursementActivity.this, dabasename);
        CompanyURL = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ReimbursementActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        categorylist = new ArrayList<ConfigDropDownData>();
        tempcategorylist = new ArrayList<String>();

    }

    public void setListeners(){

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate()){
                    jArray = createJSON();
                    finalOBJ = jArray.toString();

                    Intent intent = new Intent(ReimbursementActivity.this, NewSalesOrderBooking.class);
                    intent.putExtra("jRembArray",finalOBJ);
                    setResult(SALES_REIMBURSEMENT_FILLED,intent);
                    finish();
                }
            }
        });

        edt_catdesc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_catdesc.showDropDown();
                return false;
            }
        });

        edt_catdesc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catDesc = categorylist.get(position).getCategoryDesc();
                catID = categorylist.get(position).getPKUserCategoryId();
            }
        });
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    class DownloadCategoryJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getCategoryReimbursement;

                res = ut.OpenConnection(url);
                if (res != null) {
                 /*   String a = "\\";
                    String b = "\"";
                    String c = a+b;
                    response = res.toString().replaceAll(c,"");*/

                    response = res.toString().replaceAll("\\r\\n","");
                    response = response.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);
                    jResults = new JSONArray(response);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);
            hideProgress();
            try{
                if(jResults != null){

                    sql.delete(db.TABLE_CATEGORY_REIMB,null,null);

                    for(int i=0; i<jResults.length();i++){
                        try {
                            JSONObject jsonObject = jResults.getJSONObject(i);
                            String PKUserCategoryId = jsonObject.getString("PKUserCategoryId");
                            String CategoryDesc = jsonObject.getString("CategoryDesc");

                            ConfigDropDownData dropdown = new ConfigDropDownData();
                            dropdown.setPKUserCategoryId(PKUserCategoryId);
                            dropdown.setCategoryDesc(CategoryDesc);

                            tcf.insertCategory(PKUserCategoryId,CategoryDesc);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    getCategoryData();

                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    public void getCategoryData(){
        if(categorylist.size() > 0){
            categorylist.clear();
            tempcategorylist.clear();
        }

        String payTerms = "Select * from "+db.TABLE_CATEGORY_REIMB;
        Cursor c = sql.rawQuery(payTerms,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                ConfigDropDownData dropdown = new ConfigDropDownData();
                dropdown.setPKUserCategoryId(c.getString(c.getColumnIndex("PKUserCategoryId")));
                dropdown.setCategoryDesc(c.getString(c.getColumnIndex("CategoryDesc")));

                tempcategorylist.add(c.getString(c.getColumnIndex("CategoryDesc")));
                categorylist.add(dropdown);

            }while (c.moveToNext());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_spinner_item,tempcategorylist);
            edt_catdesc.setAdapter(adapter);

        }else {

        }
    }

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        String soReimbID = uuid.toString();

        try {
            jArray = new JSONArray();
            jobj_remb = new JSONObject();

            jobj_remb.put("SrNo","1");
            jobj_remb.put("SOReimbursementId",soReimbID);  //guid
            jobj_remb.put("CategoryDesc",catDesc);
            jobj_remb.put("Travel",edt_travel.getText().toString());
            jobj_remb.put("Stay",edt_stay.getText().toString());
            jobj_remb.put("Food",edt_food.getText().toString());
            jobj_remb.put("Others",edt_others.getText().toString());
            jobj_remb.put("CategoryId",catID);
            jobj_remb.put("Action","");
            jobj_remb.put("IsClient","");

            jArray.put(jobj_remb);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }

    /*"ReimbursementDetails": [
    {
      "SrNo": "1",
      "SOReimbursementId": "0dce2310-90a4-4305-a945-7f4abcf45e7f",
      "CategoryDesc": "test",
      "Travel": "1.00",
      "Stay": "2.00",
      "Food": "3.00",
      "Others": "4.0000",
      "CategoryId": "931F16F3-FF9B-4E72-B50E-17CB534F7BA9",
      "Action": "<div style=\"margin: 0 8px 0 0;position:relative;\"> <button data-toggle=\"dropdown\" class=\"btn btn-default\">Actions<b class=\"caret\"></b></button><ul role=\"menu\" class=\"dropdown-menu animated fadeInDown\" style=\"left:auto;\"><li onclick=\"funReimbursementPopupEdit(1)\"><a href=\"#\">Edit</a></li><li onclick=\"funReimbursementPopupDelete(1)\"><a href=\"#\">Delete</a></li></ul></div>"
    }
  ],
*/

    public boolean validate() {
        boolean val = false;

        if (edt_catdesc.getText().toString().equalsIgnoreCase("") || edt_catdesc.getText().toString().equalsIgnoreCase(null)) {
            Toast.makeText(ReimbursementActivity.this, "Please select category description", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else{
            val = true;
            return val;
        }

    }

}
