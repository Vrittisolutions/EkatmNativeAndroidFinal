package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.crm.adapter.ListDataAdapter;
import com.vritti.crm.bean.ListData;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonProspectDataClass;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.expensemanagement.AddExpenseActivity_Next;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommonListActivity extends AppCompatActivity {
 
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
    ArrayAdapter<CharSequence> SubcategoryAdapter;
    String Option="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_list);
        init();

        AppCommon.getInstance(CommonListActivity.this).onHideKeyBoard(CommonListActivity.this);
        setlistener();

    }
    
    public void init(){
        parent = CommonListActivity.this;


        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("");
        setSupportActionBar(toolbar_action);
        
        country_list = findViewById(R.id.country_list);
        progressbar =  findViewById(R.id.progressbar);

        progressbar.setVisibility(View.GONE);
        edtsearch =  findViewById(R.id.edtsearch);

        listDataArrayList=new ArrayList<>();
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



        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        txt_title.setText("List");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listDataArrayList.clear();
        Option=getIntent().getStringExtra("option");

        if (Option.equals("payment")){
            txt_title.setText("Mode of Payment");
            listDataArrayList.add(new ListData("Cash"));
            listDataArrayList.add(new ListData("Credit Card"));
            listDataArrayList.add(new ListData("Debit Card"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        } else if (Option.equals("category")){
            txt_title.setText("Type of Expense");
            listDataArrayList.add(new ListData("Travelling"));
            listDataArrayList.add(new ListData("Lodging"));
            listDataArrayList.add(new ListData("Food"));
            listDataArrayList.add(new ListData("Laundry"));
            listDataArrayList.add(new ListData("Local"));
            listDataArrayList.add(new ListData("Entertainment"));
            listDataArrayList.add(new ListData("Phone"));
            listDataArrayList.add(new ListData("Tips"));
            listDataArrayList.add(new ListData("Maintenance"));
            listDataArrayList.add(new ListData("Other"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        }else if (Option.equals("vehicle")){
            txt_title.setText("Mode of Travel");
            listDataArrayList.add(new ListData("2 Wheeler"));
            listDataArrayList.add(new ListData("4 Wheeler"));
            listDataArrayList.add(new ListData("Bus"));
            listDataArrayList.add(new ListData("Taxi"));
            listDataArrayList.add(new ListData("Airplane"));
            listDataArrayList.add(new ListData("Train"));
            listDataArrayList.add(new ListData("Auto"));
            listDataArrayList.add(new ListData("Travels"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        }else if (Option.equals("update")){
            listDataArrayList.add(new ListData("Colleague"));
            listDataArrayList.add(new ListData("Spam"));
            listDataArrayList.add(new ListData("Personal"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        }else if (Option.equals("exptype")){
            txt_title.setText("Expense Type");
            listDataArrayList.add(new ListData("Official"));
            listDataArrayList.add(new ListData("Personal"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        }else if (Option.equals("dept")){
            txt_title.setText("Department");
            listDataArrayList.add(new ListData("User"));
            listDataArrayList.add(new ListData("Technical Evalution"));
            listDataArrayList.add(new ListData("Purchase-Commercial"));
            listDataArrayList.add(new ListData("Decision Maker"));
            listAdapter = new ListDataAdapter(parent,listDataArrayList);
            country_list.setAdapter(listAdapter);
        }






    }
    
    public void setlistener(){

        country_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
              //  intent.putExtra("ID", listDataArrayList.get(position).getId());

                intent.putExtra("Name",listDataArrayList.get(position).getName());
                setResult(COUNTRY, intent);
                finish();

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
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        
    }


}