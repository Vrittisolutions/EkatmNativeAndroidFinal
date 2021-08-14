package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.CommonClass.AppCommon;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Hajmola_MainActivity extends AppCompatActivity {

    TextInputLayout txtlay_Name, txtlay_Mobno, txtlay_Gender, txtlay_Age;
    Button btn_forward;
    EditText edt_Name, edt_Mobno, edt_Age;
    SearchableSpinner spinner_sex;
    LinearLayout ln_details;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    AppCompatRadioButton radioBtn_Male, radioBtn_Female, radioBtn_Others;
    String selectedItem = "";

    static String Imagefilename = "", path = "";

    private String contact, product;

    Utility ut;
    @BindView(R.id.ln_imgMain)
    LinearLayout ln_imgMain;

    @BindView(R.id.ln_img1)
    LinearLayout ln_img1;

    @BindView(R.id.ln_img2)
    LinearLayout ln_img2;

    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;
    SharedPreferences Prospectpreference;
    private static String ProspectTypeID;
    static Boolean _flagAttachment;
    private String finaljson;

    String gender = "Male";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hajmola__main);
        ButterKnife.bind(this);
        AppCommon.getInstance(Hajmola_MainActivity.this).onHideKeyBoard(Hajmola_MainActivity.this);



        _flagAttachment = false;

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        Prospectpreference = getSharedPreferences(WebUrlClass.Sharedpreference_Prospect, Context.MODE_PRIVATE);
        ProspectTypeID = Prospectpreference.getString(WebUrlClass.Key_indivisual, "");


        initView();
        setListener();


    }

    @OnClick({R.id.img_h1})
    void imliClick() {
        selectHajmolaItem(0);
    }


    @OnClick({R.id.img_h2})
    void pudinaClick() {
        selectHajmolaItem(1);
    }

    @OnClick({R.id.img_h3})
    void anarClick() {
        selectHajmolaItem(2);
    }

    @OnClick({R.id.img_h4})
    void regularClick() {
        selectHajmolaItem(3);
    }

    @OnClick({R.id.img_h5})
    void chatkolaClick() {
        selectHajmolaItem(4);
    }

    @OnClick({R.id.img_h6})
    void hingClick() {
        selectHajmolaItem(5);
    }

    private void selectHajmolaItem(int i) {
        //0=imli,1=pudina,2=anar,3=regular,4=chatkola,5=hing

        int imgClickId = i;
        switch (i) {
            case 0:
                selectedItem = "h1";
                break;
            case 1:
                selectedItem = "h2";
                break;
            case 2:
                selectedItem = "h3";
                break;
            case 3:
                selectedItem = "h4";
                break;
            case 4:
                selectedItem = "h5";
                break;
            case 5:
                selectedItem = "h6";
                break;

            default:
                selectedItem = "";

        }

        if (Validate()) {

            getData();
        }else{
            Toast.makeText(getApplicationContext(), "Please Enter Valid Data", Toast.LENGTH_LONG).show();
        }

    }

    private void setListener() {

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ln_imgMain.setVisibility(View.VISIBLE);

            }
        });

        radioBtn_Male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String clickgender = radioBtn_Male.getText().toString();

                if (radioBtn_Male.isChecked()) {
                    gender = "Male";
                } else {
                }
            }
        });

        radioBtn_Female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String clickgender = radioBtn_Female.getText().toString();

                if (radioBtn_Female.isChecked()) {
                    gender = "Female";
                } else {
                }
            }
        });

        radioBtn_Others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String clickgender = radioBtn_Others.getText().toString();

                if (radioBtn_Others.isChecked()) {
                    gender = "Others";
                } else {
                }
            }
        });


    }

    public void getData() {

        String name = "", mobNo = "", age = "";

        name = edt_Name.getText().toString();
        mobNo = edt_Mobno.getText().toString();
        age = edt_Age.getText().toString();


               /* getId();
             //  getId();*/
        JSONObject jsoncontact = new JSONObject();
        try {
            jsoncontact.put("ContactName", name);
            jsoncontact.put("Designation", "");
            jsoncontact.put("EmailId", "");
            jsoncontact.put("Mobile", mobNo);
            jsoncontact.put("Telephone", "");
            jsoncontact.put("DateofBirth", "");
            jsoncontact.put("ContactPersonDept", "");
            jsoncontact.put("Fax", "");
            jsoncontact.put("AnniversaryDate", "");
            jsoncontact.put("Gender", gender);
            jsoncontact.put("MaritalStatus", "");
            jsoncontact.put("SpouseName", "");
            jsoncontact.put("WhatsAppNo", mobNo);

            contact = jsoncontact.toString();


        } catch (Exception e) {
            e.printStackTrace();
        }

                   /* Product details-

                            Product[k] = { FKProductId: v.FKProductId };*/
        JSONObject jsonProduct = new JSONObject();
        try {

            jsonProduct.put("FKProductId", "");
            product = jsonProduct.toString();


        } catch (Exception e) {

        }

        //susmaster = new String[5];
        JSONObject jsonBusinessprospect = new JSONObject();

        try {

            jsonBusinessprospect.put("PKSuspectId", null);
            jsonBusinessprospect.put("FirmName", name);
            jsonBusinessprospect.put("Address", "");
            jsonBusinessprospect.put("FirmAlias", "");
            jsonBusinessprospect.put("FKCityId", "");
            jsonBusinessprospect.put("FKTerritoryId", "");
            jsonBusinessprospect.put("FKBusiSegmentId", "");
            jsonBusinessprospect.put("CompanyURL", CompanyURL);
            jsonBusinessprospect.put("FKEnqSourceId", "");
            jsonBusinessprospect.put("Fax", "");
            jsonBusinessprospect.put("Notes", "");
            jsonBusinessprospect.put("Remark", "");
            jsonBusinessprospect.put("Department", "");
            jsonBusinessprospect.put("BusinessDetails", "");
            jsonBusinessprospect.put("CurrencyMasterId", "");
            jsonBusinessprospect.put("CurrencyDesc", "");
            jsonBusinessprospect.put("Turnover", "");
            jsonBusinessprospect.put("NoOfEmployees", "");
            jsonBusinessprospect.put("NoOfOffices", "");
            jsonBusinessprospect.put("LeadGivenBYId", "");
            jsonBusinessprospect.put("FKConsigneeId", "");
            jsonBusinessprospect.put("FKCustomerId", "");
            jsonBusinessprospect.put("EntityType", "");
            jsonBusinessprospect.put("PBT", "");
            jsonBusinessprospect.put("Rating", "");
            jsonBusinessprospect.put("Network", "");
            jsonBusinessprospect.put("Borrowings", "");
            jsonBusinessprospect.put("FKStateId", "");
            jsonBusinessprospect.put("FKCountryId", "");
            jsonBusinessprospect.put("GSTState", "");
            jsonBusinessprospect.put("GSTCode", "");
            jsonBusinessprospect.put("TANNo", "");
            jsonBusinessprospect.put("TANNoName", "");
            jsonBusinessprospect.put("ProspectType", ProspectTypeID);//"Individual"
            jsonBusinessprospect.put("Qualification: ", "");
            jsonBusinessprospect.put("Experience: ", "");


        } catch (JSONException e) {

        }

        JSONObject jsonData = new JSONObject();

        try {

            JSONArray ob = new JSONArray();
            //  for (int i = 0; i < contact.length; i++) {


            JSONArray obj1 = new JSONArray();
            JSONObject a = null;
            String sex = "", district = "", village = "",
                    val1 = "", val2 = "", val3 = "", val4 = "",
                    val5 = "", val6 = "", val7 = "", val8 = "", val9 = "",
                    val10 = "";
            val8 = age;


            jsonBusinessprospect.put("val1", val1);
            jsonBusinessprospect.put("val2", val2);
            jsonBusinessprospect.put("val3", val3);
            jsonBusinessprospect.put("val4", val4);
            jsonBusinessprospect.put("val5", val5);
            jsonBusinessprospect.put("val6", val6);
            jsonBusinessprospect.put("val7", val7);
            jsonBusinessprospect.put("val8", val8);
            jsonBusinessprospect.put("val9", selectedItem);
            jsonBusinessprospect.put("val10", val10);

            jsonBusinessprospect.put("sex", "");
            jsonBusinessprospect.put("District", district);
            jsonBusinessprospect.put("Village", village);

            JSONObject j = new JSONObject(jsonBusinessprospect.toString());
            System.out.println("ArrayBusiness : " + jsonBusinessprospect.toString());
            ob.put(j);
            //    }

            jsonData.put("SuspMaster", ob);

            a = new JSONObject(contact);
            obj1.put(a);

            jsonData.put("SuspContactDetails", obj1);

            JSONArray obj = new JSONArray();
            JSONObject a2 = null;

            a2 = new JSONObject(product);
            obj.put(a2);

            jsonData.put("SuspProdDetails", obj);
            jsonData.put("EnquiryRegistryId", "");

        } catch (JSONException e) {

        }

        // FinalArray[0]
        finaljson = jsonData.toString();
        finaljson = finaljson.replaceAll("\\\\", "");

        String fName = edt_Name.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy HH:mm");
        String date = sdf.format(new Date());
        String remark1 = "Promotional form Added for firm " + fName + " on" + date;
        String url = CompanyURL + WebUrlClass.api_Post_Prospect;

        String op = "";
        CreateOfflineIntent(url, finaljson, WebUrlClass.POSTFLAG, remark1, op);
        //isClicked = false;


    }

    public boolean Validate() {

        String mobNo = edt_Mobno.getText().toString();
        String age = edt_Age.getText().toString();
        int num=Integer.parseInt(age);

        if(edt_Name.getText().toString().equalsIgnoreCase("") ||
                edt_Name.getText().toString().equalsIgnoreCase(" ") ||
                edt_Name.getText().toString().equalsIgnoreCase(null)){
            Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_LONG).show();
            return false;
        } else if(edt_Mobno.getText().toString().equalsIgnoreCase("") ||
                edt_Mobno.getText().toString().equalsIgnoreCase(" ") ||
                edt_Mobno.getText().toString().equalsIgnoreCase(null) ||
                edt_Mobno.getText().toString().length() != 10 ){
            Toast.makeText(getApplicationContext(), "Enter valid mobile number", Toast.LENGTH_LONG).show();
            return false;
        } else if(edt_Age.getText().toString().equalsIgnoreCase("") ||
                edt_Age.getText().toString().equalsIgnoreCase(" ") ||
                edt_Age.getText().toString().equalsIgnoreCase(null)||
                   num>50){
            Toast.makeText(getApplicationContext(), "Age should not be greater than 50", Toast.LENGTH_LONG).show();
            return false;
        }else if(gender.equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "Click on any of the given gender", Toast.LENGTH_LONG).show();
            return false;

        } else {
            return true;
        }

    }

    private void CreateOfflineIntent(final String url, final String parameter,
                                     final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {

            Toast.makeText(Hajmola_MainActivity.this, "Thank You for your Vote", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(Hajmola_MainActivity.this,
                    SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);
            finish();

            startActivity(new Intent(Hajmola_MainActivity.this, Hajmola_End_Screen.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private void initView() {

        //ln_imgMain = findViewById(R.id.ln_imgMain);
        ln_details = findViewById(R.id.ln_details);

        txtlay_Name = findViewById(R.id.txtlay_name);
        txtlay_Mobno = findViewById(R.id.txtlay_mobno);
        //txtlay_Gender = findViewById(R.id.txtlay_gender);
        txtlay_Age = findViewById(R.id.txtlay_age);
        btn_forward = findViewById(R.id.btn_forward);
        edt_Name = findViewById(R.id.edt_name);
        edt_Mobno = findViewById(R.id.edt_mobno);
        radioBtn_Male = findViewById(R.id.radiobtn_male);
        radioBtn_Female = findViewById(R.id.radiobtn_female);
        radioBtn_Others = findViewById(R.id.radiobtn_others);
        edt_Age = findViewById(R.id.edt_age);


    }
}
