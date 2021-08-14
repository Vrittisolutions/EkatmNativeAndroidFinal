package com.vritti.sales.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SalesPaymentActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    Button btnadd, btn_cancel;
    EditText edt_instr_num, edt_amt,edt_bankname,edt_branchname,edt_remark,edt_date;
    ImageView img_date;

    static int year, month, day;
    DatePickerDialog datePickerDialog;
    public static String date = null;
    public static String today, todaysDate;

    public static final int SALES_PAYMENT_FILLED = 6;
    JSONObject jobj_paymnt;
    JSONArray jArray;
    String finalOBJ = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }
        
        init();
        
        setListeners();
    }
    
    public void init(){
        parent = SalesPaymentActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Payment");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        btnadd = findViewById(R.id.btnadd);
        btn_cancel = findViewById(R.id.btn_cancel);
        edt_instr_num = findViewById(R.id.edt_instr_num);
        edt_amt = findViewById(R.id.edt_amt);
        edt_bankname = findViewById(R.id.edt_bankname);
        edt_branchname = findViewById(R.id.edt_branchname);
        edt_remark = findViewById(R.id.edt_remark);
        edt_date = findViewById(R.id.edt_date);
        img_date = findViewById(R.id.img_date);

        ut = new Utility();
        cf = new CommonFunction(SalesPaymentActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(SalesPaymentActivity.this);
        String dabasename = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(SalesPaymentActivity.this, dabasename);
        CompanyURL = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(SalesPaymentActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        
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

                    Intent intent = new Intent(SalesPaymentActivity.this, NewSalesOrderBooking.class);
                    intent.putExtra("jPaymentArray",finalOBJ);
                    setResult(SALES_PAYMENT_FILLED,intent);
                    finish();
                }
            }
        });

        img_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(parent,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                datePicker.setMinDate(c.getTimeInMillis());

                                date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;/*

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    // only for gingerbread and newer versions
                                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);
                                    //   datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                                }*/

                                if (compare_date(date) == true) {
                                    edt_date.setText(date);
                                } else {
                                    edt_date.setText(date);
                                    Toast.makeText(parent,
                                            "You cannot select a day earlier than today!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
    }

    public static boolean compare_date(String fromdate) {
        boolean b = false;
        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        today = dfDate.format(new Date());
        try {
            if ((dfDate.parse(today).before(dfDate.parse(fromdate)) ||
                    dfDate.parse(today).equals(dfDate.parse(fromdate)))) {
                b = true;
            } else {
                date = today;
                b = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }

    public JSONArray createJSON(){

        UUID uuid = UUID.randomUUID();
        String paymntID = uuid.toString();

        try {
            jArray = new JSONArray();
            jobj_paymnt = new JSONObject();

            jobj_paymnt.put("SrNo","1");
            jobj_paymnt.put("PaymentId",paymntID);  //guid
            jobj_paymnt.put("ChequeNo",edt_instr_num.getText().toString().trim());
            jobj_paymnt.put("ChequeDate",edt_date.getText().toString());
            jobj_paymnt.put("Amount",edt_amt.getText().toString());
            jobj_paymnt.put("BankName",edt_bankname.getText().toString());
            jobj_paymnt.put("Branch",edt_branchname.getText().toString());
            jobj_paymnt.put("Remark",edt_remark.getText().toString());
            jobj_paymnt.put("Action","");

            jArray.put(jobj_paymnt);

        }catch (Exception e){
            e.printStackTrace();
        }

        return jArray;
    }
    
    /*  "PaymentsDetails": [
    {
      "SrNo": "1",
      "PaymentId": "3de21f3b-34ca-42e9-a059-b9e382c32d3a",
      "ChequeNo": "5454",
      "ChequeDate": "17/01/2020",
      "Amount": "5000.00",
      "BankName": "abc",
      "Branch": "xyz",
      "Remark": "pqr",
      "Action": "<div style=\"margin: 0 8px 0 0;position:relative;\"> <button data-toggle=\"dropdown\" 
      class=\"btn btn-default\">Actions<b class=\"caret\"></b></button><ul role=\"menu\" 
      class=\"dropdown-menu animated fadeInDown\" style=\"left:auto;\"><li onclick=\"funPaymentPopupEdit(1)\">
      <a href=\"#\">Edit</a></li><li onclick=\"funPaymentPopupDelete(1)\"><a href=\"#\">Delete</a></li></ul></div>"
    }
  ]
*/

    public boolean validate() {
        boolean val = false;

        if (edt_instr_num.getText().toString().equalsIgnoreCase("")
                && edt_date.getText().toString().equalsIgnoreCase("")
                && edt_amt.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(SalesPaymentActivity.this, "Please fill all details", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if (edt_instr_num.getText().toString().equalsIgnoreCase("") ||
                edt_instr_num.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesPaymentActivity.this, "Enter instrument number", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if (edt_date.getText().toString().equalsIgnoreCase("") ||
                edt_date.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesPaymentActivity.this, "Select date", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else if (edt_amt.getText().toString().equalsIgnoreCase("") ||
                edt_amt.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(SalesPaymentActivity.this, "Enter amount", Toast.LENGTH_SHORT).show();
            val = false;
            return val;
        }else{
            val = true;
            return val;
        }

    }

}
