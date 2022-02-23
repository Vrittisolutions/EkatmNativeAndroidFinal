package com.vritti.crm.vcrm7;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.adapter.ProductDetailsAdapter;
import com.vritti.crm.bean.OrderType;
import com.vritti.crm.bean.ProductBean;
import com.vritti.crm.bean.WhomBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.expensemanagement.AddExpenseActivity;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

public class CreateOpportunityActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", ReviewDate = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    public Context context;

    SimpleDateFormat dfDate = null;
    String CurrentDate = "", Firmname="", Alias="", SuspectID="", finaljson, callId = "", Mode = "A";
    Button  buttonSave, buttonCancel;
    EditText editTextFirmname, editTextAlias,editTextNotes;
    TextView editTextScheduledate;
    AutoCompleteTextView editqnty;
    LinearLayout ln_dttime;
    // Spinner spinner_toWhom, spinner_Scheduledtime,
    //       spinner_Ordertype, spinner_campaign, SpinnerProd;
    private String string_assignto = "";
    //  SearchableSpinner spinner_Assigntocall;
    SharedPreferences userpreferences;
    ProgressHUD progressHUD;
    SQLiteDatabase sql;
    String[] Product;
    String[] FinalArray;
    List<WhomBean> lstSE = new ArrayList<>();
    List<String> lstSEString = new ArrayList<>();

    List<String> lstBOEString = new ArrayList<String>();
    List<OrderType> lstBOE = new ArrayList<>();
    List<String> lstCampaignString = new ArrayList<String>();
    List<OrderType> lstCampaign = new ArrayList<>();
    List<OrderType> lstOrdertype = new ArrayList<>();
    List<String> lstOrderTypeString = new ArrayList<String>();
    List<String> lstFollowuptime = new ArrayList<String>();
    List<ProductBean> productBeanList = new ArrayList<ProductBean>();
    ProductBean productBean;
    String boeid = "", CurrentCallOwner = "", ProductId = "", OrderTypeMasterId = "";
    int seid;


    //Product Activity

    LinearLayout laytxt;
    RecyclerView rv;
    LinearLayoutManager llm;
    ProductDetailsAdapter adapter;
    RecyclerView.ItemDecoration itemDecoration;
    List<String> lstProduct = new ArrayList<String>();
    AutoCompleteTextView EdtProd, spinner_Prod_Code;
    EditText editqnty1;
    Button buttonSave1, buttonCancel1;
    LinearLayout lst, linear_product_data, linear_list_product;
    ArrayList<String> Productionitems = new ArrayList<>();
    ArrayList<ProductBean> Productionitemscombine = new ArrayList<>();
    ArrayList<String> productCodeItems = new ArrayList<>();
    String Productid, product_name, pid, product_Code = "";
    private String ordertypeid = "", campaignId = "";
    ImageView img_birth_calender;
    private String selection;
    LinearLayout len_tutor;

    Spinner spinner_tutor, spinner_From, spinner_To;
    EditText edt_duration, edt_week, edt_budget;
    TextView txt_title;
    String tutor = "", from = "", to = "", duration = "", week = "", budget = "";
    private String settingKey;
    private String dabasename;
    String FirmName = "", FirmAlias = "", toWhom = "", assignOppId = "", notes = "";
    ProgressBar progressBar;

    AutoCompleteTextView spinner_toWhom, spinner_Assigntocall, spinner_Ordertype, spinner_campaign;
    TextView spinner_Scheduledtime;
    private String format="";
    ImageView img_add,img_refresh,img_back;
    View view;
    private boolean isOrderType=false;
    private boolean isCampaigh=false;
    ImageView btnAddProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_create_opportunity_v1);
        context = CreateOpportunityActivity.this;
        init();

        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        if (EnvMasterId.equalsIgnoreCase("learningpotato")) {
            len_tutor.setVisibility(View.VISIBLE);
        }
        boeid = UserMasterId;
        CurrentCallOwner = boeid;


        Intent intent = getIntent();
        Firmname = intent.getStringExtra("Firmname");
        Alias = intent.getStringExtra("Alias");
        SuspectID = intent.getStringExtra("SuspectID");
        editTextFirmname.setText(Firmname);
        editTextAlias.setText(Alias);
        spinner_Assigntocall.setText(UserName);

        if (getIntent().hasExtra("CallId")) {
            callId = getIntent().getStringExtra("CallId");
            Mode = "E";
            view.setVisibility(View.GONE);
            ln_dttime.setVisibility(View.GONE);
            txt_title.setText("Update Opportunity");
        }

        dfDate = new SimpleDateFormat("dd/MM/yyy");
        Date d = new Date();
        CurrentDate = dfDate.format(d);
        editTextScheduledate.setText(CurrentDate);

        if (cf.getBOEcount() > 0) {
            displayBOE();
        } else {
            if (ut.isNet(CreateOpportunityActivity.this)) {
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


//order type
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

//campaign
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


        //schdule time
        if (cf.getFollowUpTimecount() > 0) {
            //displayFollowup();
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

        //SE

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


        /*************************************************/


        if (Mode.equals("E")) {
            if (isnet()) {
                progressBar.setVisibility(View.VISIBLE);
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadEditProductDetailJSON().execute();
                        new DownloadOpportunityDetails().execute();
                        ///api/CRMCallAssignmentAPI/getOpportunityDetail?CallId=a8debd89-47b5-4a84-a8a2-45ed941c212f
                        //new DownloadSuspectDetailsJSON().execute();


                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        } else {
            if (isnet()) {
                progressBar.setVisibility(View.VISIBLE);
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new DownloadProductDetailJSON().execute();
                        // new DownloadOpportunityDetails().execute();
                        ///api/CRMCallAssignmentAPI/getOpportunityDetail?CallId=a8debd89-47b5-4a84-a8a2-45ed941c212f
                        new DownloadSuspectDetailsJSON().execute();


                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
        }
     /*   if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadProductDetailJSON().execute();
                    new DownloadOpportunityDetails().execute();
                    ///api/CRMCallAssignmentAPI/getOpportunityDetail?CallId=a8debd89-47b5-4a84-a8a2-45ed941c212f
                    new DownloadSuspectDetailsJSON().execute();


                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
        }
*/

        setListener();
    }

    private void displayProductDetails() {

        productBeanList.clear();
        String countQuery = "SELECT ItemDesc,FkProductId,Quantity FROM "
                + db.TABLE_Product_Details_New;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                productBean.setProductId(cursor.getString(cursor.getColumnIndex("FkProductId")));
                //productBean.setItemMasterId(cursor.getString(cursor.getColumnIndex("ItemMasterId")));
                if (cursor.getString(cursor.getColumnIndex("Quantity")).equalsIgnoreCase("")) {
                    productBean.setQnty("1");
                } else {
                    productBean.setQnty(cursor.getString(cursor.getColumnIndex("Quantity")));


                }

                productBeanList.add(productBean);

            } while (cursor.moveToNext());

            ProductId = productBeanList.get(0).getItemMasterId();
        }

/*
        if (selection.equals("SE")) {

            String query = "SELECT distinct UserName,vWBUsermasterId " +
                    " FROM " + db.TABLE_SE +
                    " WHERE UserName='" + string_assignto + "'";
            Cursor cur = sql.rawQuery(query, null);

            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    seid = cur.getString(cur.getColumnIndex("vWBUsermasterId"));
                    CurrentCallOwner = seid;

                } while (cur.moveToNext());

            }

        } else {
            if (selection.equals("BOE")) {
                String query = "SELECT distinct UserName,vWBUsermasterId " +
                        " FROM " + db.TABLE_BOE +
                        " WHERE UserName='" + string_assignto + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        boeid = cur.getString(cur.getColumnIndex("vWBUsermasterId"));
                        CurrentCallOwner = boeid;

                    } while (cur.moveToNext());

                }
            }
        }*/


        String countQry = "SELECT  OrderTypeMasterId,Description FROM "
                + db.TABLE_OrderType + " WHERE Description='"
                + (String) spinner_Ordertype.getText().toString() + "'";
        Cursor cursor1 = sql.rawQuery(countQry, null);
        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();

            OrderTypeMasterId = cursor1.getString(cursor1.getColumnIndex("OrderTypeMasterId"));

        }


    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        // toolbar.setLogo(R.drawable.crm_logo_1);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        view=findViewById(R.id.view);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));


        txt_title.setText("Create Opportunity");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        editTextFirmname = (EditText) findViewById(R.id.editTextFirmname);
        editTextAlias = (EditText) findViewById(R.id.editTextAlias);
        editTextScheduledate = (TextView) findViewById(R.id.editTextScheduledate);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        spinner_toWhom = findViewById(R.id.spinner_toWhom);
        ln_dttime = findViewById(R.id.ln_dttime);
        spinner_Assigntocall = findViewById(R.id.spinner_Assigntocall);
        spinner_Scheduledtime = findViewById(R.id.spinner_Scheduledtime);
        spinner_Ordertype = findViewById(R.id.spinner_Ordertype);
        spinner_campaign = findViewById(R.id.spinner_campaign);
        btnAddProd = (ImageView) findViewById(R.id.btnAddProd);
        len_tutor = (LinearLayout) findViewById(R.id.len_tutor);
        spinner_To = (Spinner) findViewById(R.id.spinner_To);
        spinner_From = (Spinner) findViewById(R.id.spinner_From);
        spinner_tutor = (Spinner) findViewById(R.id.spinner_tutor);
        edt_budget = (EditText) findViewById(R.id.edt_budget);
        edt_duration = (EditText) findViewById(R.id.edt_duration);
        edt_week = (EditText) findViewById(R.id.edt_week);
        txt_title =  findViewById(R.id.txt_title);
        img_birth_calender = findViewById(R.id.img_birth_calender);
        progressBar = findViewById(R.id.progressBar_1);


        //spinner_Assigntocall.setHint("Enter name ");
        WhomBean whomBean = new WhomBean();
        whomBean.setSeId("1.0");
        whomBean.setSeName("SE");
        lstSE.add(whomBean);
        WhomBean whomBean1 = new WhomBean();
        whomBean1.setSeId("2.0");
        whomBean1.setSeName("BOE");
        lstSE.add(whomBean1);


        ArrayAdapter<WhomBean> orderTypeArrayAdapter = new ArrayAdapter<WhomBean>(CreateOpportunityActivity.this,
                android.R.layout.simple_list_item_1,lstSE);
        orderTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_toWhom.setAdapter(orderTypeArrayAdapter);
        spinner_toWhom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        //Product Activity List
        linear_product_data = (LinearLayout) findViewById(R.id.linear_product_data);
        editqnty = findViewById(R.id.editqnty);
        EdtProd = findViewById(R.id.EdtProd);
        spinner_Prod_Code = findViewById(R.id.spinner_Prod_Code);
        //rv = (RecyclerView) findViewById(R.id.recyclerproduct);
        // llm = new LinearLayoutManager(getApplicationContext());
        //  rv.setLayoutManager(llm);
        laytxt = (LinearLayout) findViewById(R.id.laytxt);
        laytxt.setVisibility(View.GONE);
        //  rv.setVisibility(View.GONE);
        buttonSave1 = (Button) findViewById(R.id.buttonSave1);
        buttonCancel1 = (Button) findViewById(R.id.buttonCancel1);
        linear_list_product = (LinearLayout) findViewById(R.id.linear_list_product);
        long date1 = System.currentTimeMillis();
        SimpleDateFormat sdf1 = new SimpleDateFormat("h:mm");
        String timeString = sdf1.format(date1);
        spinner_Scheduledtime.setText(timeString);



        spinner_Scheduledtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(CreateOpportunityActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                if (hourOfDay == 0) {

                                    hourOfDay += 12;

                                    format = "AM";
                                } else if (hourOfDay == 12) {

                                    format = "PM";

                                } else if (hourOfDay > 12) {

                                    hourOfDay -= 12;

                                    format = "PM";

                                } else {

                                    format = "AM";
                                }


                               // spinner_Scheduledtime.setText(hourOfDay + ":" + minute + format);

                                spinner_Scheduledtime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                mTimePicker.show();

            }
        });




        EdtProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String query = "SELECT distinct ItemCode,ItemMasterId,ItemDesc" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemDesc='" + EdtProd.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Productid = cur.getString(cur.getColumnIndex("ItemMasterId"));
                        product_name = cur.getString(cur.getColumnIndex("ItemDesc"));
                        product_Code = cur.getString(cur.getColumnIndex("ItemCode"));

                    } while (cur.moveToNext());

                    spinner_Prod_Code.setText(product_Code);


                   /* int pos = -1;
                    try {
                        for (int j = 0; j < Productionitemscombine.size(); j++) {
                            if (Productionitemscombine.get(j).getItemDesc().equalsIgnoreCase(product_name)) {
                                pos = j;
                                break;
                            }
                        }
                        if (pos != -1) {
                            spinner_Prod_Code.setSelection(pos);
                        } else {
                            spinner_Prod_Code.setSelection(0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

                } else {
                    Productid = "";
                    // spinner_Prod_Code.setPrompt("Select Code");
                }


            }
        });

        spinner_Prod_Code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String query = "SELECT distinct ItemCode,ItemMasterId,ItemDesc" +
                        " FROM " + db.TABLE_Product +
                        " WHERE ItemCode='" + spinner_Prod_Code.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Productid = cur.getString(cur.getColumnIndex("ItemMasterId"));
                        product_name = cur.getString(cur.getColumnIndex("ItemDesc"));
                        product_Code = cur.getString(cur.getColumnIndex("ItemCode"));

                    } while (cur.moveToNext());
                    EdtProd.setText(product_name);

                   /* try {
                        int pos = -1;

                        for (int j = 0; j < Productionitemscombine.size(); j++) {
                            if (Productionitemscombine.get(j).getItemDesc().equals(product_name)) {
                                pos = j;
                                break;
                            }
                        }

                        if (pos != -1) {
                            EdtProd.setSelection(pos);
                        } else {
                            EdtProd.setSelection(0);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }*/

                } else {
                    Productid = "";
                }


            }
        });


        buttonSave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {

                    if (cf.getProuctdetailcount(product_name) > 0) {
                        ContentValues values = new ContentValues();
                        values.put("Quantity", editqnty.getText().toString().trim());
                        // long a = sql.update(db.TABLE_Product_Details, values, "ItemDesc=?", new String[]{product_name});
                        long a = sql.update(db.TABLE_Product_Details_New, values, "ItemDesc=?", new String[]{product_name});
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("ItemDesc", product_name);
                        values.put("Quantity", editqnty.getText().toString().trim());
                        values.put("FkProductId", Productid);
                        // long a = sql.insert(db.TABLE_Product_Details, null, values);"(FkProductId TEXT,ItemDesc TEXT ,ItemDesc TEXT)";
                        long a = sql.insert(db.TABLE_Product_Details_New, null, values);
                    }
                }


                EdtProd.setSelection(0);
                spinner_Prod_Code.setSelection(0);
                EdtProd.setText("");
                spinner_Prod_Code.setText("");
                editqnty.setText("");
                linear_product_data.setVisibility(View.GONE);
                displayProductDetails1();

            }
        });
        buttonCancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linear_product_data.getVisibility() == View.VISIBLE) {
                    linear_product_data.setVisibility(View.GONE);
                }
            }
        });


        spinner_Ordertype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOpportunityActivity.this,
                        /*CityListActivity.class*/CountryListActivity.class);
                isOrderType = true;
                isCampaigh = false;


                String url = CompanyURL + WebUrlClass.api_Get_Ordertype;
                intent.putExtra("Table_Name", db.TABLE_OrderType);
                intent.putExtra("Id", "OrderTypeMasterId");
                intent.putExtra("DispName", "Description");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });


        spinner_campaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateOpportunityActivity.this,
                        /*CityListActivity.class*/CountryListActivity.class);
                isOrderType = false;
                isCampaigh = true;

                String url = CompanyURL + WebUrlClass.api_Get_Compaign;
                intent.putExtra("Table_Name", db.TABLE_Campaign);
                intent.putExtra("Id", "PKCampaignId");
                intent.putExtra("DispName", "CampaignName");
                intent.putExtra("WHClauseParameter", "");
                intent.putExtra("APIName", url);
                startActivityForResult(intent, COUNTRY);
            }
        });


        /*spinner_Ordertype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ordertypeid = "";
                String odertp = (String) spinner_Ordertype.getText().toString();

                String query = "SELECT distinct OrderTypeMasterId,Description " +
                        " FROM " + db.TABLE_OrderType +
                        " WHERE Description='" + odertp + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        ordertypeid = cur.getString(cur.getColumnIndex("OrderTypeMasterId"));

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetReviewDaysJSON().execute(ordertypeid);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }


                    } while (cur.moveToNext());

                }

            }
        });
*/

        spinner_tutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tutor = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                from = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                to = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setListener() {
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Mode.equals("E"))
                {
                    Calendar mcurrentDate = Calendar.getInstance();
                    int mYear = mcurrentDate.get(Calendar.YEAR);
                    int mMonth = mcurrentDate.get(Calendar.MONTH);
                    int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                    String date = String.format("%02d", (mDay)) + "/" + String.format("%02d", (mMonth + 1)) + "/"
                            + mYear;
                    /*03/09/2021*/
                    long date1 = System.currentTimeMillis();

                    SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm aa");
                    String timeString = sdf1.format(date1);
                    
                    
                    if (validate1()) {
                        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);

                        duration = edt_duration.getText().toString();
                        week = edt_week.getText().toString();
                        budget = edt_budget.getText().toString();


                        displayProductDetails();
                        Product = new String[productBeanList.size()];
                        JSONObject jprod = new JSONObject();
                        try {

                            for (int i = 0; i < productBeanList.size(); i++) {
                                jprod.put("FKProductId", productBeanList.get(i).getProductId());
                                jprod.put("Quantity", productBeanList.get(i).getQnty());

                                Product[i] = jprod.toString();
                            }
                        } catch (Exception e) {

                        }
                        JSONObject jobj = new JSONObject();
                        try {
                            if (EnvMasterId.equalsIgnoreCase("learningpotato")) {
                                jobj.put("ProspectId", SuspectID);
                                jobj.put("SEId", seid);
                                jobj.put("BOEId", boeid);
                                jobj.put("LatestRemark", editTextNotes.getText().toString().trim());
                                jobj.put("CurrentCallOwner", boeid);
                                jobj.put("ProductId", ProductId);
                                jobj.put("OrderTypeMasterId", OrderTypeMasterId);
                                jobj.put("CallNo", "");
                                jobj.put("CallReviewDate", ReviewDate);
                                jobj.put("Campaign", "");
                                jobj.put("DurationOfClass", duration);
                                jobj.put("NoOfDaysInWeek", week);
                                jobj.put("PrefferedTime", from);
                                jobj.put("PrefferedTimeTo", to);
                                jobj.put("PrefferedTutor", tutor);
                                jobj.put("IdeaAboutPayment", "");

                            }
                            else {

                                jobj.put("OrderType", ordertypeid);
                                jobj.put("Campaign", campaignId);
                                /*"Campaign": g*/
                                jobj.put("LatestRemark", editTextNotes.getText().toString().trim());
                                jobj.put("FixedRemark", "");
                                jobj.put("CallId", callId);
                                jobj.put("AddCorrigendum", "");
                                jobj.put("TenderNo", "");
                                jobj.put("CRMCategory", seid);
                                jobj.put("CurrentCallOwner", boeid);

                          /*  jobj.put("ProspectId", SuspectID);
                            jobj.put("SEId", seid);
                            jobj.put("BOEId", boeid);

                            jobj.put("CurrentCallOwner", boeid);

                            jobj.put("ProductId", ProductId);
                            jobj.put("OrderTypeMasterId", OrderTypeMasterId);
                            jobj.put("CallNo", "");
                            jobj.put("CallReviewDate", ReviewDate);*/


                            }
                        } catch (JSONException e) {

                        }


                        JSONObject jobtender = new JSONObject();
                        try {
                            jobtender.put("OfferType", "B");
                            jobtender.put("QuotNum", "");
                            jobtender.put("TenderNo", "");
                            jobtender.put("Tenderdt", date);
                            jobtender.put("TenderFee", "0");
                            jobtender.put("ModeofPayment", "");
                            jobtender.put("PrebidTime", timeString);
                            jobtender.put("SubmissionDt", date);
                            jobtender.put("SubmissionTime", timeString);
                            jobtender.put("SecurityDeposite", "0");
                            jobtender.put("EMDAmount", "0");
                            jobtender.put("PurchaseDt", date);
                            jobtender.put("PurchaseTime", timeString);
                            jobtender.put("RevisedDueDate", date);
                            jobtender.put("DateofIssue", date);
                            jobtender.put("CorrigendumNotes", "");
                            jobtender.put("TechNotes", "");
                            jobtender.put("FinNotes", "");
                            jobtender.put("CorrigendumNo", "");
                            jobtender.put("SDModeofPayment", "");
                            jobtender.put("EMDModeofPayment", "");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray ob1 = new JSONArray();
                        try {
                            JSONObject a = new JSONObject(jobtender.toString());
                            ob1.put(a);
                            jobj.put("objTender", ob1);
                        } catch (JSONException e) {

                        }

                        JSONArray obprod = new JSONArray();
                        try {
                            for (int i = 0; i < Product.length; i++) {
                                JSONObject a = new JSONObject(Product[i]);
                                obprod.put(a);
                            }
                            jobj.put("AllProduct", obprod);
                        } catch (JSONException e) {

                        }


                        finaljson = jobj.toString();
                        finaljson = finaljson.replaceAll("\\\\", "");
                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                      new PostEditProspectUpdateJSON().execute(finaljson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }

                }
                else {

                    if (validate1()) {
                        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);

                        duration = edt_duration.getText().toString();
                        week = edt_week.getText().toString();
                        budget = edt_budget.getText().toString();


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
                            if (EnvMasterId.equalsIgnoreCase("learningpotato")) {
                                jobj.put("ProspectId", SuspectID);
                                jobj.put("SEId", seid);
                                jobj.put("BOEId", boeid);
                                jobj.put("LatestRemark", editTextNotes.getText().toString().trim());
                                jobj.put("CurrentCallOwner", CurrentCallOwner);
                                jobj.put("ProductId", ProductId);
                                jobj.put("OrderTypeMasterId", OrderTypeMasterId);
                                jobj.put("CallNo", "");
                                jobj.put("CallReviewDate", ReviewDate);
                                jobj.put("Campaign", "");
                                jobj.put("DurationOfClass", duration);
                                jobj.put("NoOfDaysInWeek", week);
                                jobj.put("PrefferedTime", from);
                                jobj.put("PrefferedTimeTo", to);
                                jobj.put("PrefferedTutor", tutor);
                                jobj.put("IdeaAboutPayment", "");

                            }
                            else {


                                jobj.put("ProspectId", SuspectID);
                                jobj.put("SEId", seid);
                                jobj.put("BOEId", boeid);
                                jobj.put("LatestRemark", editTextNotes.getText().toString().trim());
                                jobj.put("CurrentCallOwner", CurrentCallOwner);
                                jobj.put("ProductId", ProductId);
                                jobj.put("OrderTypeMasterId", OrderTypeMasterId);
                                jobj.put("CallNo", "");
                                jobj.put("CallReviewDate", ReviewDate);


                            }
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


                        JSONObject jobtender = new JSONObject();
                        try {
                            jobtender.put("TenderNo", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray ob1 = new JSONArray();
                        try {
                            JSONObject a = new JSONObject(jobtender.toString());
                            ob1.put(a);
                            jobjdata.put("objTender", ob1);
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
                            jobjdata.put("ScheduleTime", spinner_Scheduledtime.getText().toString().trim());

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

                    //finaljson = finaljson.replaceAll(" ", "");

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
                startActivity(intent);*/
            }
        });

       /* spinner_campaign.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String campaign = spinner_campaign.getText().toString();
                campaignId=lstCampaign.get(position).getOrderTypeMasterId();

                Log.d("Campen",campaignId);


               *//* if (lstCampaign.size() != 0) {
                    int pos = -1;
                    for (int i = 0; i < lstCampaign.size(); i++) {
                        if (campaign.equals(lstCampaign.get(i).getDescription())) {
                            campaignId = lstCampaign.get(i).getOrderTypeMasterId();
                            Log.d("Campen",campaignId);
                            pos = i;

                            break;
                        }
                    }

                    if (pos != -1) {
                        Log.d("CampenB",campaignId);

                    } else {
                        campaignId = "";
                    }
                }*//*
            }
        });*/

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

        spinner_toWhom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selection = spinner_toWhom.getText().toString();

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
                }
            }
        });






        spinner_Assigntocall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                string_assignto = (String) spinner_Assigntocall.getText().toString();
                CurrentCallOwner = lstBOE.get(position).getvWBUsermasterId();
                boeid = CurrentCallOwner;
                if (lstBOE.size() != 0) {
                    int pos = -1;
                    for (int i = 0; i < lstBOE.size(); i++) {
                        if (lstBOE.get(i).getUserName().equals(string_assignto)) {
                            pos = i;
                            break;
                        }
                    }
                    if (pos != -1) {
                        spinner_Assigntocall.setText(lstBOE.get(pos).getUserName());
                        CurrentCallOwner = lstBOE.get(pos).getvWBUsermasterId();
                        boeid = CurrentCallOwner;
                    } else {
                        CurrentCallOwner = "";
                        boeid = "";
                        /*spinner_Assigntocall.setText(lstBOE.get(0).getUserName());
                        /*spinner_Assigntocall.setText(lstBOE.get(0).getUserName());
                        CurrentCallOwner = lstBOE.get(0).getvWBUsermasterId();
                        boeid = CurrentCallOwner;*/
                    }
                }

                if (string_assignto.equalsIgnoreCase("") ||
                        string_assignto.equalsIgnoreCase(" ")
                        || string_assignto.equalsIgnoreCase(null)) {
                    if (((String) spinner_toWhom.getText().toString()).equalsIgnoreCase("SE")) {
                        Toast.makeText(getApplicationContext(), "No SE Present", Toast.LENGTH_LONG).show();
                    } else if (((String) spinner_toWhom.getText().toString()).equalsIgnoreCase("BOE")) {
                        Toast.makeText(getApplicationContext(), "No BOE Present", Toast.LENGTH_LONG).show();

                    }

                }
            }
        });


    }


    private void displaySE() {

        //lstSE.clear();
        lstSEString.clear();
        String countQuery = "SELECT  UserName,CRMCategory FROM "
                + db.TABLE_SE;
        Cursor cursor = sql.rawQuery(countQuery, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstSEString.add(cursor.getString(cursor.getColumnIndex("CRMCategory")));
            } while (cursor.moveToNext());
        }
/*
        if (lstSE.size() != 0) {
            if (lstSE.get(0).equals("SE")) {
                spinner_toWhom.setSelection(0);
            } else if (lstSE.get(0).equals("BOE")) {
                spinner_toWhom.setSelection(1);
            } else {
                spinner_toWhom.setSelection(0);
            }
        }*/


     /*   MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstSE);
        spinner_toWhom.setAdapter(adapter);*/

    }

    private void displayBOE() {

        lstBOE.clear();
        lstBOEString.clear();
        String countQuery = "SELECT  vWBUsermasterId,UserLoginId,UserName FROM "
                + db.TABLE_BOE;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstBOE.add(new OrderType(cursor.getString(cursor.getColumnIndex("UserLoginId")),
                        cursor.getString(cursor.getColumnIndex("vWBUsermasterId")),
                        cursor.getString(cursor.getColumnIndex("UserName"))));
                lstBOEString.add(cursor.getString(cursor.getColumnIndex("UserName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstBOEString);
        spinner_Assigntocall.setAdapter(adapter);
       /* int pos = -1;
        for (int i = 0; i < lstBOE.size(); i++) {
            if (UserName.equals(lstBOE.get(i))) {
                pos = i;
            }
        }
        if (pos != -1) {
            spinner_Assigntocall.setSelection(pos);
        } else {
            spinner_Assigntocall.setSelection(0);
        }*/
    }


/*
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
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner_Scheduledtime.setAdapter(adapter);

    }
*/


    private void displayCampaign() {

        lstCampaignString.clear();
        lstCampaign.clear();
        String countQuery = "SELECT PKCampaignId,CampaignName FROM "
                + db.TABLE_Campaign;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstCampaign.add(new OrderType(cursor.getString(cursor.getColumnIndex("CampaignName")),
                        cursor.getString(cursor.getColumnIndex("PKCampaignId")), "", ""));
                lstCampaignString.add(cursor.getString(cursor.getColumnIndex("CampaignName")));
            } while (cursor.moveToNext());
        }

        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstCampaignString);
        spinner_campaign.setAdapter(adapter);
    }

    private void displayOrderType() {

        lstOrdertype.clear();
        lstOrderTypeString.clear();
        String countQuery = "SELECT  OrderTypeMasterId,Description FROM "
                + db.TABLE_OrderType;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                lstOrdertype.add(new OrderType(cursor.getString(cursor.getColumnIndex("Description")),
                        cursor.getString(cursor.getColumnIndex("OrderTypeMasterId"))));
                lstOrderTypeString.add(cursor.getString(cursor.getColumnIndex("Description")));
            } while (cursor.moveToNext());
        }


        MySpinnerAdapter adapter = new MySpinnerAdapter(context,
                R.layout.crm_custom_spinner_txt, lstOrderTypeString);
        spinner_Ordertype.setAdapter(adapter);


    }

    public static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        public MySpinnerAdapter(Context context, int resource,
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
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressBar != null && progressBar.isShown()) {
            // progressHUD.dismiss();
            progressBar.setVisibility(View.GONE);
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
           // displayFollowup();

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
                    response = res.toString();
                    /*response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");*/
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

            progressBar.setVisibility(View.GONE);

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
                /*    sql.delete(db.TABLE_Product_Details, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details, null);   */

                    sql.delete(db.TABLE_Product_Details_New, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details_New, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                            if (columnName.equalsIgnoreCase("Quantity")) {
                                columnValue = "1";
                            } else {
                                columnValue = jorder.getString(columnName);
                            }

                            values.put(columnName, columnValue);
                        }

                        //   long a = sql.insert(db.TABLE_Product_Details, null, values);
                        long a = sql.insert(db.TABLE_Product_Details_New, null, values);

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
            progressBar.setVisibility(View.GONE);
            if (response.contains("")) {

            }
            displayProductDetails1();

        }

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.refresh) {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadOrderTypeJSON().execute();
                        new DownloadCampaignJSON().execute();
                        new DownloadSEJSON().execute();
                        new DownloadBOEJSON().execute();
                        // new DownloadOrderTypeJSON().execute();
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
                res = ut.OpenPostConnection(url, params[0], CreateOpportunityActivity.this);
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


            Intent intent = new Intent(CreateOpportunityActivity.this, CRMHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    class PostEditProspectUpdateJSON extends AsyncTask<String, Void, String> {
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
            String url = CompanyURL + WebUrlClass.api_PostUpdate;
            try {
                res = ut.OpenPostConnection(url, params[0], CreateOpportunityActivity.this);
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
                Toast.makeText(context, "Opportunity Updated successfully", Toast.LENGTH_LONG).show();
            }


            Intent intent = new Intent(CreateOpportunityActivity.this, CRMHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CreateOpportunityActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((EdtProd.getText().toString().equalsIgnoreCase("") ||
                EdtProd.getText().toString().equalsIgnoreCase(" ") ||
                EdtProd.getText().toString().equalsIgnoreCase(null))) {

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
        //    String countQuery = "SELECT  ItemDesc,Qnty FROM " + db.TABLE_Product_Details;
        String countQuery = "SELECT  ItemDesc,FkProductId,Quantity FROM " + db.TABLE_Product_Details_New;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                productBean.setProductId(cursor.getString(cursor.getColumnIndex("FkProductId")));
                productBean.setQnty(cursor.getString(cursor.getColumnIndex("Quantity")));
                productBeanList.add(productBean);

            } while (cursor.moveToNext());
        }

        if (Mode.equals("E")){
            laytxt.setVisibility(View.VISIBLE);
        }


        linear_list_product.removeAllViews();
        if (productBeanList.size() > 0) {
            laytxt.setVisibility(View.VISIBLE);
            for (int i = 0; i < productBeanList.size(); i++) {
                addViewList(i);
            }
        }

    }


    private void addViewList(int i) {
        TextView txtProduct;
        TextView edtqty;
        //EditText edtqty;
        ImageView btn_delete;
        LayoutInflater layoutInflater = (LayoutInflater) CreateOpportunityActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_product_recycler,
                null);
        txtProduct = (TextView) convertView.findViewById(R.id.txtProduct);
        //edtqty = (EditText) convertView.findViewById(R.id.edtqty);
        edtqty = convertView.findViewById(R.id.edtqty);
        btn_delete = convertView.findViewById(R.id.btn_delete);


        txtProduct.setText(productBeanList.get(position).getItemDesc());
        edtqty.setText(productBeanList.get(position).getQnty());


        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name = productBeanList.get(position).getItemDesc();
                String productId = productBeanList.get(position).getProductId();
                long a = sql.delete(db.TABLE_Product_Details_New,
                        "FkProductId=?", new String[]{productId});
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
            String url = CompanyURL + WebUrlClass.api_getListProduct+"?OrderTypeMasterId=";
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString()/*.replaceAll("\\\\", "")*/;
                    // response = response.replaceAll("\\\\\\\\/", "");
                    //response = response.substring(1, response.length() - 1);
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
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();

            if (response.equals("error")) {


            } else {
                displayProduct();
            }
        }

    }

    private void displayProduct() {


        Productionitems.clear();
        productCodeItems.clear();
        String query = "SELECT distinct ItemCode,ItemMasterId,ItemDesc" +
                " FROM " + db.TABLE_Product;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Productionitemscombine.add(new ProductBean(cur.getString(cur.getColumnIndex("ItemCode")),
                        cur.getString(cur.getColumnIndex("ItemDesc")),
                        cur.getString(cur.getColumnIndex("ItemMasterId"))));

                productCodeItems.add(cur.getString(cur.getColumnIndex("ItemDesc")));
                Productionitems.add(cur.getString(cur.getColumnIndex("ItemCode")));

                //Productionitems.add(cur.getString(cur.getColumnIndex("ItemCode")));

            } while (cur.moveToNext());

        }
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> customDept = new ArrayAdapter<>(CreateOpportunityActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        spinner_Prod_Code.setAdapter(customDept);//SF0006_ADATSOFT

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(CreateOpportunityActivity.this,
                R.layout.crm_custom_spinner_txt, productCodeItems);
        EdtProd.setAdapter(stringArrayAdapter);//SF0006

        //Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);

        // Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
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

        if (((String) spinner_Assigntocall.getText().toString()) == null) {
            Toast.makeText(getApplicationContext(), "Select assign call to", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public class GetReviewDaysJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_GetReviewDays + "?OrderType=" + ordertypeid;
            try {
                res = ut.OpenConnection(url);
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
            //   dismissProgressDialog();
            if (response.equals("")) {
                ReviewDate = "12/30/9999 00:00:00.000";
            } else {
                ReviewDate = response;
            }


        }

    }


    private class DownloadOpportunityDetails extends AsyncTask<String, Void, String> {
        String res = "", response = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getOpportunityDetail
                        + "?CallId=" + URLEncoder.encode(callId, "UTF-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (response != null) {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(response);

                    for (int i = 0; i < jResults.length(); i++) {

                        JSONObject jorder = jResults.getJSONObject(i);

                        FirmName = jorder.getString("FirmName");
                        FirmAlias = jorder.getString("FirmAlias");
                        seid = jorder.getInt("CRMCategory");
                        notes = jorder.getString("LatestRemark");
                        editTextNotes.setText(notes);
                        editTextFirmname.setText(FirmName);
                        editTextAlias.setText(FirmAlias);

                      /*  if(toWhom.equals("1.0")){

                            spinner_toWhom.setSelection(lstSE.get(0));
                        }else if(toWhom.equals("2")){
                            spinner_toWhom.setSelection(1);
                        }else{
                            spinner_toWhom.setSelection(0);
                        }*/
                       /* if (lstSE.size() != 0) {
                            int pos = -1;
                            for (int j = 0; j < lstSE.size(); j++) {
                                if (lstSE.get(j).getSeId().equals(toWhom)){
                                    pos = j;
                                    break;
                                }
                            }
                            if(pos != -1){
                           spinner_toWhom.setSelection(pos);
                            }else{
                                spinner_toWhom.setSelection(0);
                            }
                        }*/


                        boeid = jorder.getString("CurrentCallOwner");
                        if (lstBOE.size() != 0) {
                            int boePos = -1;
                            for (int j = 0; j < lstBOE.size(); j++) {
                                if (lstBOE.get(j).getvWBUsermasterId().equals(boeid)) {
                                    boePos = j;
                                    break;
                                }
                            }
                            if (boePos != -1) {
                                spinner_Assigntocall.setText(lstBOE.get(boePos).getUserName());
                            } else {
                                spinner_Assigntocall.setText(lstBOE.get(0).getUserName());
                            }
                        }

                        ordertypeid = jorder.getString("OrderTypeMasterId");
                        if (lstOrdertype.size() != 0) {
                            int orderPos = -1;
                            for (int j = 0; j < lstOrdertype.size(); j++) {
                                if (lstOrdertype.get(j).getOrderTypeMasterId().equals(ordertypeid)) {
                                    orderPos = j;
                                    break;
                                }
                            }
                            if (orderPos != -1) {
                                spinner_Ordertype.setText(lstOrdertype.get(orderPos).getDescription());
                            } else {
                                spinner_Ordertype.setText(lstOrdertype.get(0).getDescription());
                            }
                        }

                        seid = jorder.getInt("CRMCategory");
                        if (seid == 1) {
                            spinner_toWhom.setText(lstSE.get(0).getSeName());
                        } else if (seid == 2) {
                            spinner_toWhom.setText(lstSE.get(1).getSeName());
                        }

                        campaignId = jorder.getString("Campaign");
                        if (lstCampaign.size() != 0) {
                            int pos = -1;
                            for (int j = 0; j < lstCampaign.size(); j++) {
                                if (campaignId.equals(lstCampaign.get(j).getOrderTypeMasterId())) {
                                    pos = j;
                                    break;
                                }
                            }

                            if (pos != -1) {
                                spinner_campaign.setText(lstCampaign.get(pos).getDescription());
                            } else {
                                spinner_campaign.setText("");
                            }
                        }




                      /*  runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editTextFirmname.setText(FirmName);
                                editTextAlias.setText(FirmAlias);
                            }
                        });*/
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class DownloadEditProductDetailJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getProductDetailEdit
                        + "?CallId=" + URLEncoder.encode(callId, "UTF-8");

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    // response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                /*    sql.delete(db.TABLE_Product_Details, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details, null);   */

                    sql.delete(db.TABLE_Product_Details_New, null, null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product_Details_New, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);

                         /*   if (columnName.equalsIgnoreCase("Quantity")) {
                                columnValue = "1";
                            } else {*/
                                columnValue = jorder.getString(columnName);
                          //  }

                            values.put(columnName, columnValue);
                        }

                        //   long a = sql.insert(db.TABLE_Product_Details, null, values);
                        long a = sql.insert(db.TABLE_Product_Details_New, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            if (response.contains("")) {

            }
            displayProductDetails1();

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUNTRY && resultCode == COUNTRY) {
            if (isOrderType == true) {
                String Description = data.getStringExtra("Name");
                ordertypeid = data.getStringExtra("ID");

                spinner_Ordertype.setText(Description);

                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetReviewDaysJSON().execute(ordertypeid);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }
            }else if (isCampaigh == true) {
                String Campaign = data.getStringExtra("Name");
                campaignId = data.getStringExtra("ID");
                spinner_campaign.setText(Campaign);

            }

        }
    }


}
