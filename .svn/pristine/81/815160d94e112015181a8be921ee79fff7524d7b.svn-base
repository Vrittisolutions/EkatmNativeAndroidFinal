package com.vritti.crmlib.vcrm7;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.ProductDetailsAdapter;
import com.vritti.crmlib.bean.ProductBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class CreateOpportunityActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    public  static Context context;

    SimpleDateFormat dfDate = null;
    String CurrentDate = "", Firmname, Alias, SuspectID,  finaljson;
    Button btnAddProd, buttonSave, buttonCancel;
    EditText editTextFirmname, editTextAlias, editTextScheduledate, editTextNotes, editqnty;
    Spinner spinner_toWhom, spinner_Scheduledtime,
            spinner_Ordertype, spinner_campaign, SpinnerProd;
    private static String string_assignto = "";
    SearchableSpinner spinner_Assigntocall;
    SharedPreferences userpreferences;
    ProgressHUD progressHUD;
    SQLiteDatabase sql;
    String[] Product;
    String[] FinalArray;
    List<String> lstSE = new ArrayList<String>();
    List<String> lstBOE = new ArrayList<String>();
    List<String> lstCompaign = new ArrayList<String>();
    List<String> lstOrdertype = new ArrayList<String>();
    List<String> lstFollowuptime = new ArrayList<String>();
    List<ProductBean> productBeanList = new ArrayList<ProductBean>();
    ProductBean productBean;
    String seid = "", boeid = "", CurrentCallOwner = "", ProductId = "", OrderTypeMasterId = "";
    ProgressBar progressbar;


    //Product Activity

    LinearLayout laytxt;
    RecyclerView rv;
    LinearLayoutManager llm;
    ProductDetailsAdapter adapter;
    RecyclerView.ItemDecoration itemDecoration;
    List<String> lstProduct = new ArrayList<String>();
    Spinner EdtProd;
    EditText editqnty1;
    Button buttonSave1, buttonCancel1;
    LinearLayout lst, linear_product_data, linear_list_product;
    ArrayList<String> Productionitems = new ArrayList<String>();
    String Productid, product_name, pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_create_opportunity);
        context = CreateOpportunityActivity.this;
        init();

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        UserMasterId = userpreferences.getString("UserMasterId", null);
        UserName = userpreferences.getString("UserName", null);
        Intent intent = getIntent();
      /*  Firmname = intent.getStringExtra("Firmname");
        Alias = intent.getStringExtra("Alias");*/
        SuspectID = intent.getStringExtra("SuspectID");
        editTextFirmname.setText(Firmname);
        editTextAlias.setText(Alias);

        dfDate = new SimpleDateFormat("dd/MM/yyy");
        Date d = new Date();
        CurrentDate = dfDate.format(d);
        editTextScheduledate.setText(CurrentDate);

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadProductDetailJSON().execute();
                    new DownloadSuspectDetailsJSON().execute();


                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
        }


        if (cf.getOrderTypecount() > 0) {
            displayOrderType();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadOrderTypeJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


        if (cf.getCampaigncount() > 0) {
            displayCampaign();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadCampaignJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }

        if (cf.getFollowUpTimecount() > 0) {
            displayFollowup();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadFollowupTimeJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


        if (cf.getProuctcount() > 0) {
            displayProduct();
        } else {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadProductJSON().execute();

                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }
        setListener();
    }

    private void displayProductDetails() {

        productBeanList.clear();
        String countQuery = "SELECT ItemMasterId, ItemDesc,Qnty FROM "
                + db.TABLE_Product_Details;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                productBean.setItemMasterId(cursor.getString(cursor.getColumnIndex("ItemMasterId")));
                if (cursor.getString(cursor.getColumnIndex("Qnty")).equalsIgnoreCase("")) {
                    productBean.setQnty("1");
                } else {
                    productBean.setQnty(cursor.getString(cursor.getColumnIndex("Qnty")));
                }

                productBeanList.add(productBean);

            } while (cursor.moveToNext());

            ProductId = productBeanList.get(0).getItemMasterId();
        }


        if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("SE")) {
            String countQ = "SELECT  EkatmUserMasterId,UserName FROM "
                    + db.TABLE_SE + " WHERE UserName='" + string_assignto + "'";
            Cursor c = sql.rawQuery(countQ, null);

            if (c.getCount() > 0) {
                c.moveToFirst();

                seid = c.getString(c.getColumnIndex("EkatmUserMasterId"));
                CurrentCallOwner = seid;
            }
        } else if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("BOE")) {
            String kdjL = (String) spinner_Assigntocall.getSelectedItem();
            String countQ = "SELECT  EkatmUserMasterId,UserName FROM "
                    + db.TABLE_BOE + " WHERE UserName='" + string_assignto + "'";
            Cursor c = sql.rawQuery(countQ, null);

            if (c.getCount() > 0) {
                c.moveToFirst();

                boeid = c.getString(c.getColumnIndex("EkatmUserMasterId"));
                CurrentCallOwner = boeid;

            }
        }


        String countQry = "SELECT  OrderTypeMasterId,Description FROM "
                + db.TABLE_OrderType + " WHERE Description='"
                + (String) spinner_Ordertype.getSelectedItem() + "'";
        Cursor cursor1 = sql.rawQuery(countQry, null);
        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();

            OrderTypeMasterId = cursor1.getString(cursor1.getColumnIndex("OrderTypeMasterId"));

        }


    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        editTextFirmname = (EditText) findViewById(R.id.editTextFirmname);
        editTextAlias = (EditText) findViewById(R.id.editTextAlias);
        editTextScheduledate = (EditText) findViewById(R.id.editTextScheduledate);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        spinner_toWhom = (Spinner) findViewById(R.id.spinner_toWhom);
        spinner_Assigntocall = (SearchableSpinner) findViewById(R.id.spinner_Assigntocall);
        spinner_Scheduledtime = (Spinner) findViewById(R.id.spinner_Scheduledtime);
        spinner_Ordertype = (Spinner) findViewById(R.id.spinner_Ordertype);
        spinner_campaign = (Spinner) findViewById(R.id.spinner_campaign);
        btnAddProd = (Button) findViewById(R.id.btnAddProd);
        progressbar = (ProgressBar) findViewById(R.id.toolbar_progress_set);
        //spinner_Assigntocall.setHint("Enter name ");


        //Product Activity List
        linear_product_data = (LinearLayout) findViewById(R.id.linear_product_data);
        editqnty = (EditText) findViewById(R.id.editqnty);
        EdtProd = (Spinner) findViewById(R.id.EdtProd);
        //rv = (RecyclerView) findViewById(R.id.recyclerproduct);
        // llm = new LinearLayoutManager(getApplicationContext());
        //  rv.setLayoutManager(llm);
        laytxt = (LinearLayout) findViewById(R.id.laytxt);
        laytxt.setVisibility(View.GONE);
        //  rv.setVisibility(View.GONE);
        buttonSave1 = (Button) findViewById(R.id.buttonSave1);
        buttonCancel1 = (Button) findViewById(R.id.buttonCancel1);
        linear_list_product = (LinearLayout) findViewById(R.id.linear_list_product);


        EdtProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "SELECT distinct ItemMasterId,ItemDesc" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemDesc='" + EdtProd.getSelectedItem().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Productid = cur.getString(cur.getColumnIndex("ItemMasterId"));
                        product_name = cur.getString(cur.getColumnIndex("ItemDesc"));

                    } while (cur.moveToNext());

                } else {
                    Productid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buttonSave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cf.getProuctdetailcount(product_name) > 0) {
                    ContentValues values = new ContentValues();
                    values.put("Qnty", editqnty.getText().toString().trim());
                    long a = sql.update(db.TABLE_Product_Details, values,
                            "ItemDesc=?", new String[]{product_name});
                } else {
                    ContentValues values = new ContentValues();
                    values.put("ItemDesc", product_name);
                    values.put("Qnty", editqnty.getText().toString().trim());
                    values.put("ItemMasterId", getpid(Productid));
                    long a = sql.insert(db.TABLE_Product_Details, null, values);
                }

                editqnty.setText("");
                displayProductDetails1();

            }
        });
        buttonCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void setListener() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate1()) {
                    displayProductDetails();
                    Product = new String[productBeanList.size()];
                    JSONObject jprod = new JSONObject();
                    try {

                        for (int i = 0; i < productBeanList.size(); i++) {
                            jprod.put("FKProductId", productBeanList.get(i).getItemMasterId());
                            jprod.put("ProductQty", productBeanList.get(i).getQnty());

                            Product[i] = jprod.toString();
                        }
                    } catch (Exception e) {

                    }

                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("ProspectId", SuspectID);
                        jobj.put("SEId", seid);
                        jobj.put("BOEId", boeid);
                        jobj.put("LatestRemark", editTextNotes.getText().toString().trim());
                        jobj.put("CurrentCallOwner", CurrentCallOwner);
                        jobj.put("ProductId", ProductId);
                        jobj.put("OrderTypeMasterId", OrderTypeMasterId);


                    } catch (JSONException e) {

                    }

                    //     FinalArray[0] = jobj.toString();

                    JSONObject jobjdata = new JSONObject();
                    JSONArray ob = new JSONArray();
                    try {
                        JSONObject a = new JSONObject(jobj.toString());
                        ob.put(a);
                        jobjdata.put("AllCall", ob);
                    } catch (JSONException e) {

                    }

                    JSONArray obprod = new JSONArray();
                    try {
                        for (int i = 0; i < Product.length; i++) {
                            JSONObject a = new JSONObject(Product[i]);
                            obprod.put(a);
                        }
                        jobjdata.put("AllProduct", obprod);
                    } catch (JSONException e) {

                    }


                    try {
                        jobjdata.put("ScheduleDate", editTextScheduledate.getText().toString().trim());
                        jobjdata.put("ScheduleTime", spinner_Scheduledtime.getSelectedItem().toString().trim());

                    } catch (JSONException e) {

                    }


                    finaljson = jobjdata.toString();
                    finaljson = finaljson.replaceAll("\\\\", "");
                    //finaljson = finaljson.replaceAll(" ", "");
                    if (isnet()) {
                        new StartSession(context, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new PostProspectUpdateJSON().execute(finaljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOpportunityActivity.this.finish();
            }
        });
        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_product_data.setVisibility(View.VISIBLE);
                /*Intent intent = new Intent(context, ProductActivity.class);
                startActivity(intent);
*/
            }
        });


        editTextScheduledate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog date = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                editTextScheduledate.setText(dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                date.setTitle("Select date");
                date.show();
            }
        });

        spinner_toWhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) spinner_toWhom.getSelectedItem();
                if (selection.equalsIgnoreCase("SE")) {
                    if (cf.getSEcount() > 0) {
                        displaySE();
                    } else {
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadSEJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                } else if (selection.equalsIgnoreCase("BOE")) {

                    if (cf.getBOEcount() > 0) {
                        displayBOE();
                    } else {
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadBOEJSON().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_Assigntocall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_assignto = (String) spinner_Assigntocall.getSelectedItem();
                if (string_assignto.equalsIgnoreCase("") ||
                        string_assignto.equalsIgnoreCase(" ")
                        || string_assignto.equalsIgnoreCase(null)) {
                    if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("SE")) {
                        Toast.makeText(getApplicationContext(), "No SE Present", Toast.LENGTH_LONG).show();
                    } else if (((String) spinner_toWhom.getSelectedItem()).equalsIgnoreCase("BOE")) {
                        Toast.makeText(getApplicationContext(), "No BOE Present", Toast.LENGTH_LONG).show();

                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void displaySE() {

        lstSE.clear();
        String countQuery = "SELECT  UserName FROM "
                + db.TABLE_SE;
        Cursor cursor = sql.rawQuery(countQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstSE.add(cursor.getString(cursor.getColumnIndex("UserName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstSE);
        spinner_Assigntocall
                .setAdapter(adapter);

    }

    private void displayBOE() {

        lstBOE.clear();
        String countQuery = "SELECT  UserName FROM "
                + db.TABLE_BOE;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstBOE.add(cursor.getString(cursor.getColumnIndex("UserName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstBOE);
        spinner_Assigntocall.setAdapter(adapter);
    }


    private void displayFollowup() {

        lstFollowuptime.clear();
        String countQuery = "SELECT  FollowUpTime FROM "
                + db.TABLE_FollowUpTime;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstFollowuptime.add(cursor.getString(cursor.getColumnIndex("FollowUpTime")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstFollowuptime);
        spinner_Scheduledtime
                .setAdapter(adapter);
        spinner_Scheduledtime.setSelection(0);
    }

    private void displayCampaign() {

        lstCompaign.clear();
        String countQuery = "SELECT  CampaignName FROM "
                + db.TABLE_Campaign;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstCompaign.add(cursor.getString(cursor.getColumnIndex("CampaignName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstCompaign);
        spinner_campaign
                .setAdapter(adapter);
    }

    private void displayOrderType() {

        lstOrdertype.clear();
        String countQuery = "SELECT  Description FROM "
                + db.TABLE_OrderType;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstOrdertype.add(cursor.getString(cursor.getColumnIndex("Description")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstOrdertype);
        spinner_Ordertype
                .setAdapter(adapter);
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }

    private void showProgressDialog() {


        //progressHUD = ProgressHUD.show(context, "", false, false, null);
        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            // progressHUD.dismiss();
            progressbar.setVisibility(View.GONE);
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

    class DownloadSEJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_SE;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");

                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_SE, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SE, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_SE, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //    dismissProgressDialog();
            if (response.contains("UserLoginId")) {
                // displaySE();
            }
            displaySE();
        }

    }

    class DownloadBOEJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Boe;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_BOE, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_BOE, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_BOE, null, values);

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("UserLoginId")) {
                // displayBOE();
            }
            displayBOE();
        }

    }

    class DownloadFollowupTimeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Followuptime;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_FollowUpTime, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_FollowUpTime, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_FollowUpTime, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //  dismissProgressDialog();
            if (response.contains("")) {

            }
            displayFollowup();

        }

    }

    class DownloadCampaignJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Compaign;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_Campaign, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Campaign, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_Campaign, null, values);

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {

            }
            displayCampaign();

        }

    }

    class DownloadOrderTypeJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    //   response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);

                    sql.delete(db.TABLE_OrderType, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_OrderType, null);
                    int count = c.getCount();
                    String columnName, columnValue;

                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);


                        }

                        long a = sql.insert(db.TABLE_OrderType, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {

            }
            displayOrderType();

        }

    }


    class DownloadSuspectDetailsJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;
        String FirmName, FirmAlias;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getSuspectDetails
                        + "?PKSuspectId=" + URLEncoder.encode(SuspectID, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                    JSONArray jResults = new JSONArray(response);


                    for (int i = 0; i < jResults.length(); i++) {

                        JSONObject jorder = jResults.getJSONObject(i);

                        FirmName = jorder.getString("FirmName");
                        FirmAlias = jorder.getString("FirmAlias");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextFirmname.setText(FirmName);
                                editTextAlias.setText(FirmAlias);
                            }
                        });
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {

            }

        }

    }

    class DownloadProductDetailJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getProductDetail
                        + "?FKSuspectId=" + URLEncoder.encode(SuspectID, "UTF-8");

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Product_Details, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                            if (columnName.equalsIgnoreCase("Qnty")) {
                                columnValue = "1";
                            } else {
                                columnValue = jorder.getString(columnName);
                            }

                            values.put(columnName, columnValue);
                        }

                        long a = sql.insert(db.TABLE_Product_Details, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("")) {
                displayProductDetails1();
            }


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.refresh) {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOrderTypeJSON().execute();
                        new DownloadCampaignJSON().execute();
                        new DownloadSEJSON().execute();
                        new DownloadBOEJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });


            }
        }

        return super.onOptionsItemSelected(item);
    }


    class PostProspectUpdateJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CreateOpportunityActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_post_save_call;
            try {
                res = ut.OpenPostConnection(url, params[0]);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (integer.contains("error")) {
                Toast.makeText(context, "Opportunity not created", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Opportunity created successfully", Toast.LENGTH_LONG).show();
            }


            onBackPressed();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CreateOpportunityActivity.this, CallListActivity.class);
        startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        CreateOpportunityActivity.this.finish();
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((EdtProd.getSelectedItem().toString().equalsIgnoreCase("") ||
                EdtProd.getSelectedItem().toString().equalsIgnoreCase(" ") ||
                EdtProd.getSelectedItem().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter product name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editqnty.getText().toString().equalsIgnoreCase("") ||
                editqnty.getText().toString().equalsIgnoreCase(" ") ||
                editqnty.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter quantity", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void displayProductDetails1() {

        productBeanList.clear();
        String countQuery = "SELECT  ItemDesc,Qnty FROM "
                + db.TABLE_Product_Details;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                if (cursor.getString(cursor.getColumnIndex("Qnty")).equalsIgnoreCase("")) {
                    productBean.setQnty("1");
                } else {
                    productBean.setQnty(cursor.getString(cursor.getColumnIndex("Qnty")));
                }

                productBeanList.add(productBean);

            } while (cursor.moveToNext());
        }
        laytxt.setVisibility(View.VISIBLE);


        linear_list_product.removeAllViews();
        if (productBeanList.size() > 0) {
            for (int i = 0; i < productBeanList.size(); i++) {
                addViewList(i);
            }
        }

    }


    private void addViewList(int i) {
        TextView txtProduct;
        EditText edtqty;
        Button btn_delete;
        LayoutInflater layoutInflater = (LayoutInflater) CreateOpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_product_recycler,
                null);
        txtProduct = (TextView) convertView.findViewById(R.id.txtProduct);
        edtqty = (EditText) convertView.findViewById(R.id.edtqty);
        btn_delete = (Button) convertView.findViewById(R.id.btn_delete);


        txtProduct.setText(productBeanList.get(position).getItemDesc());
        edtqty.setText(productBeanList.get(position).getQnty());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name = productBeanList.get(position).getItemDesc();
                long a = sql.delete(db.TABLE_Product_Details,
                        "ItemDesc=?", new String[]{name});
                displayProductDetails1();

            }
        });


        linear_list_product.addView(convertView);
    }

    class DownloadProductJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Product;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Product, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_Product, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();

            if (response.contains("")) {
                displayProduct();

            }
        }

    }

    private void displayProduct() {


        Productionitems.clear();
        String query = "SELECT distinct ItemMasterId,ItemDesc" +
                " FROM " + db.TABLE_Product;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Productionitems.add(cur.getString(cur.getColumnIndex("ItemDesc")));

            } while (cur.moveToNext());

        }
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(CreateOpportunityActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        EdtProd.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);

        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
    }

    private String getpid(String Pname) {
        String countQ = "SELECT  ItemMasterId FROM "
                + db.TABLE_Product + " WHERE ItemDesc='" + Pname + "'";
        Cursor c = sql.rawQuery(countQ, null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            pid = c.getString(c.getColumnIndex("ItemMasterId"));

        }
        return pid;
    }

    public boolean validate1() {

        if (((String) spinner_Assigntocall.getSelectedItem()) == null) {
            Toast.makeText(getApplicationContext(), "Select Assign call to", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
