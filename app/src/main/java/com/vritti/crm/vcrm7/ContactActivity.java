package com.vritti.crm.vcrm7;

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
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.ShowContact;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.expensemanagement.AddExpenseActivity_Next;
import com.vritti.expensemanagement.AddExpenseActivity_V1;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import static com.vritti.crm.vcrm7.BusinessProspectusActivity.COUNTRY;

/**
 * Created by sharvari on 29-Sep-17.
 */

public class ContactActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    EditText edt_contact_name, edt_designation, edt_emailid, edt_mobile, edt_offfice;
    AutoCompleteTextView spinner_department;
    SwitchCompat checkbox_primary_contact;
    TextView txt_save, txt_reset;
    String Call_Contactname = "", Call_Designation = "", Call_Department = "", Call_Mobile = "",
            Call_Email = "", Call_Office = "", Addcalljson = "", Call_Callid = "", Call_CallType = "", Call_ProspectId = "", Deletecalljson = "", Contact_id = "", Call_primary = "", Editcalljson = "";
    boolean Check_Value = false;
    ArrayList<ShowContact> showContactArrayList = new ArrayList<>();
    String show_contactname, show_designation, show_department, show_email, show_mobile, show_primary_contact;
    View showcontactconvertView;
    TextView txt_show_contactname, txt_show_contact_designation, txt_show_contact_department, txt_show_contact_mobileno, txt_show_contact_email, txt_show_contact_primary;
    Spinner txt_action;
    EditText edit_contactname, edit_designation, edit_mobile, edit_email;
    TextView txt_edit_save, txt_update_msg, txt_update;
    Spinner spinner_edit_primary, spinner_edit_department;
    String ActionPerformitem, UserType;
    ArrayAdapter<String> ActionPerform;
    String[] ArrarlistActionPerform = {"Edit", "Delete"};
    int position;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    LinearLayout len_show_calldetails, len_action, len_contact_edit, len_contact;
    private int position1, pos;
    LinearLayout len;
    ImageView img_add, img_refresh, img_back;
    TextView txt_title;
    ArrayAdapter<CharSequence> SubcategoryAdapter;
    private String Mode = "";
    private String Firmname="",Actiondatetime="",Remark="",call="",
            Status="",Call="",SourceId="",Milestone="",Mobile="",
            EstimateValue="",CallLogType="",Start="",EndTime="",Duration="",RowNo="",NextAction="";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_contact_lay);


        init();

        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        Call_ProspectId = intent.getStringExtra("call_prospect");


    }

    private void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString(WebUrlClass.USERINFO_USER_TYPE, null);
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
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        txt_title = findViewById(R.id.txt_title);
        img_add = findViewById(R.id.img_add);
        img_back = findViewById(R.id.img_back);
        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));
        progressDialog = new ProgressDialog(ContactActivity.this);


        edt_contact_name = (EditText) findViewById(R.id.edt_contact_name);
        edt_designation = (EditText) findViewById(R.id.edt_designation);
        spinner_department = (AutoCompleteTextView) findViewById(R.id.spinner_department);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_offfice = (EditText) findViewById(R.id.edt_offfice);
        checkbox_primary_contact = (SwitchCompat) findViewById(R.id.checkbox_primary_contact);
        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_reset = (TextView) findViewById(R.id.txt_reset);
        len_show_calldetails = (LinearLayout) findViewById(R.id.len_show_calldetails);
        len_contact = (LinearLayout) findViewById(R.id.len_contact);


        if (getIntent().hasExtra("callmob")){
            edt_mobile.setText(getIntent().getStringExtra("callmob"));
            Firmname=getIntent().getStringExtra("firm");
            Actiondatetime=getIntent().getStringExtra("date");
            Remark=getIntent().getStringExtra("remark");
            call=getIntent().getStringExtra("call");
            Call_Callid = getIntent().getStringExtra("callid");
            Call_CallType = getIntent().getStringExtra("call_type");
            Call_ProspectId = getIntent().getStringExtra("call_prospect");
            Status=getIntent().getStringExtra("status");
            Call=getIntent().getStringExtra("call_type_1");
            SourceId=getIntent().getStringExtra("SourceId");
            Milestone=getIntent().getStringExtra("mile");
            Mobile=getIntent().getStringExtra("mobile");
            EstimateValue=getIntent().getStringExtra("evalue");
            CallLogType=getIntent().getStringExtra("Callfromcalllogs");
            Start=getIntent().getStringExtra("starttime");
            EndTime=getIntent().getStringExtra("endtime");
            Duration=getIntent().getStringExtra("duration");
            RowNo=getIntent().getStringExtra("rowNo");
            NextAction=getIntent().getStringExtra("action");
            Log.e("NEXTACTION --> "," --> "+NextAction);
        }

        /*SubcategoryAdapter = ArrayAdapter.createFromResource(ContactActivity.this, R.array.department, android.R.layout.simple_spinner_item);
        SubcategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_department.setAdapter(SubcategoryAdapter);
*/

        spinner_department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,
                        CommonListActivity.class);
                intent.putExtra("option", "dept");
                startActivityForResult(intent, COUNTRY);
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().hasExtra("Mode")) {
            Mode = getIntent().getStringExtra("Mode");
            if (Mode.equals("E")) {
                txt_title.setText("Edit Contact");
                txt_save.setText("Update");


                edt_contact_name.setText(getIntent().getStringExtra("name"));
                edt_designation.setText(getIntent().getStringExtra("designation"));
                edt_mobile.setText(getIntent().getStringExtra("contact"));
                edt_emailid.setText(getIntent().getStringExtra("email"));
                spinner_department.setText(getIntent().getStringExtra("dept"));
            } else {
                txt_title.setText("Add Contact");

            }
        } else {
            txt_title.setText("Add Contact");

        }

        txt_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_contact_name.setText("");
                edt_designation.setText("");
                edt_mobile.setText("");
                edt_emailid.setText("");
                edt_offfice.setText("");
                checkbox_primary_contact.setChecked(false);


            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (v.getTag() instanceof Integer) {
                    pos = (Integer) v.getTag();
                }

                Call_Contactname = edt_contact_name.getText().toString();
                Call_Designation = edt_designation.getText().toString();
                Call_Mobile = edt_mobile.getText().toString();
                Call_Email = edt_emailid.getText().toString();
                Call_Office = edt_offfice.getText().toString();
                CallData();

                if (Mode.equals("E")) {
                    if (Call_Contactname.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter contact name", Toast.LENGTH_LONG).show();
                    } else if (Call_Designation.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter designation", Toast.LENGTH_LONG).show();
                    } else if (Call_Mobile.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                    } else if (Call_Department.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please select department", Toast.LENGTH_LONG).show();
                    } else {

                        JSONObject jsoncontactadd = new JSONObject();

                        try {
                            jsoncontactadd.put("CallId", Call_Callid);
                            jsoncontactadd.put("CallType", Call_CallType);
                            jsoncontactadd.put("ProspectId", Call_ProspectId);
                            jsoncontactadd.put("ContactName", Call_Contactname);
                            jsoncontactadd.put("Designation", Call_Designation);
                            jsoncontactadd.put("DeptRoleName", Call_Department);
                            jsoncontactadd.put("EmailId", Call_Email);
                            jsoncontactadd.put("TeleNo", Call_Office);
                            jsoncontactadd.put("MobileNo", Call_Mobile);
                            jsoncontactadd.put("IsPrimaryContact", Check_Value);

                            Addcalljson = jsoncontactadd.toString();

                            System.out.println("Contact list : " + jsoncontactadd.toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Addcalljson = Addcalljson.toString();
                        Addcalljson = Addcalljson.replaceAll("\\\\", "");

                        if (isnet()) {
                            progressDialog.setMessage("Please wait,sending data...");
                            if (!isFinishing()) {
                                progressDialog.show();
                            }
                            new StartSession(ContactActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new POSTEditSaveContact().execute(Addcalljson);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });
                        }
                    }
                } else {


                    if (Call_Contactname.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter contact name", Toast.LENGTH_LONG).show();
                    } else if (Call_Designation.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter designation", Toast.LENGTH_LONG).show();
                    } else if (Call_Mobile.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please enter mobile number", Toast.LENGTH_LONG).show();
                    } else if (Call_Department.equals("")) {
                        Toast.makeText(ContactActivity.this, "Please select department", Toast.LENGTH_LONG).show();
                    } else {

                        JSONObject jsoncontactadd = new JSONObject();

                        try {
                            jsoncontactadd.put("CallId", Call_Callid);
                            jsoncontactadd.put("CallType", Call_CallType);
                            jsoncontactadd.put("ProspectId", Call_ProspectId);
                            jsoncontactadd.put("ContactName", Call_Contactname);
                            jsoncontactadd.put("Designation", Call_Designation);
                            jsoncontactadd.put("DeptRoleName", Call_Department);
                            jsoncontactadd.put("EmailId", Call_Email);
                            jsoncontactadd.put("TeleNo", Call_Office);
                            jsoncontactadd.put("MobileNo", Call_Mobile);
                            jsoncontactadd.put("IsPrimaryContact", Check_Value);

                            Addcalljson = jsoncontactadd.toString();

                            System.out.println("Contact list : " + jsoncontactadd.toString());


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Addcalljson = Addcalljson.toString();
                        Addcalljson = Addcalljson.replaceAll("\\\\", "");

                        if (isnet()) {
                            progressDialog.setMessage("Please wait,sending data...");
                            if (!isFinishing()) {
                                progressDialog.show();
                            }
                            new StartSession(ContactActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new PostAddContactJSON().execute(Addcalljson);
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

        checkbox_primary_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkbox_primary_contact.isChecked()) {
                    Check_Value = true;
                } else {
                    Check_Value = false;
                }
            }
        });


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

    class POSTdeleteContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTdeleteContact;
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], ContactActivity.this);

                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);

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


            Toast.makeText(ContactActivity.this, "Contact deleted successfully", Toast.LENGTH_LONG).show();
        }

    }

    class POSTEditSaveContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveEditContact;
            System.out.println("BusinessAPIURL-1 :" + Editcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], ContactActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);
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


            Toast.makeText(ContactActivity.this, "Contact updated successfully", Toast.LENGTH_LONG).show();
            onBackPressed();
            len_contact.setVisibility(View.VISIBLE);
        }

    }

    private void CallData() {
        String query = "SELECT ProspectId "
                + " FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            //   tv_callrating_cnt.setText(cur.getString(cur.getColumnIndex("ThisWeekCollection")));
            Call_ProspectId = cur.getString(cur.getColumnIndex("ProspectId"));


           /* if (isnet()) {
                new StartSession(CallListActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetAddShowcallDetailsJSON().execute(Call_ProspectId);
                    }
                });
            }
*/

        }

    }

    class PostAddContactJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTAddContact;
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0], ContactActivity.this);
                System.out.println("BusinessAPI-2 :" + res);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    System.out.println("BusinessAPI-1 :" + response);
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
            try {
                if (!integer.equals("")) {

                    if (getIntent().hasExtra("callmob")) {
                        if (getIntent().getStringExtra("callmob").equalsIgnoreCase("")) {
                            onBackPressed();
                        } else {
                            Intent intent = new Intent(ContactActivity.this, OpportunityUpdateActivity_New.class);
                            intent.putExtra("callid", Call_Callid);
                            intent.putExtra("firmname", Firmname);
                            intent.putExtra("firm", Firmname);
                            intent.putExtra("calltype", getIntent().getStringExtra("call_type"));
                            intent.putExtra("table", "Call");
                            intent.putExtra("date", Actiondatetime);
                            intent.putExtra("remark", Remark);
                            intent.putExtra("call", call);
                            intent.putExtra("status", Status);
                            intent.putExtra("call_type_1", Call);
                            intent.putExtra("SourceId", SourceId);
                            intent.putExtra("mile", Milestone);
                            intent.putExtra("mobile", Mobile);
                            intent.putExtra("evalue", EstimateValue);
                            intent.putExtra("call_type", Call_CallType);
                            intent.putExtra("action", NextAction);
                            intent.putExtra("ProspectId", Call_ProspectId);
                            intent.putExtra("type", "Callfromcalllogs");
                            intent.putExtra("starttime", Start);
                            intent.putExtra("endtime", EndTime);
                            intent.putExtra("duration", Duration);
                            intent.putExtra("rowNo", RowNo);

                            if (getIntent().hasExtra("callmob")) {
                                intent.putExtra("callmob", getIntent().getStringExtra("callmob"));
                            }

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                        }
                    } else {

                        onBackPressed();
                    }
                } else {
                    if (getIntent().hasExtra("callmob")) {
                        if (getIntent().getStringExtra("callmob").equalsIgnoreCase("")) {
                            onBackPressed();
                        } else {
                            Intent intent = new Intent(ContactActivity.this, OpportunityUpdateActivity_New.class);
                            intent.putExtra("callid", Call_Callid);
                            intent.putExtra("firmname", Firmname);
                            intent.putExtra("firm", Firmname);
                            intent.putExtra("calltype", getIntent().getStringExtra("call_type"));
                            intent.putExtra("table", "Call");
                            intent.putExtra("date", Actiondatetime);
                            intent.putExtra("remark", Remark);
                            intent.putExtra("call", call);
                            intent.putExtra("status", Status);
                            intent.putExtra("call_type_1", Call);
                            intent.putExtra("SourceId", SourceId);
                            intent.putExtra("mile", Milestone);
                            intent.putExtra("mobile", Mobile);
                            intent.putExtra("evalue", EstimateValue);
                            intent.putExtra("call_type", Call_CallType);
                            intent.putExtra("action", NextAction);
                            intent.putExtra("ProspectId", Call_ProspectId);
                            intent.putExtra("type", "Callfromcalllogs");
                            intent.putExtra("starttime", Start);
                            intent.putExtra("endtime", EndTime);
                            intent.putExtra("duration", Duration);
                            intent.putExtra("rowNo", RowNo);
                            if (getIntent().hasExtra("callmob")) {
                                intent.putExtra("callmob", getIntent().getStringExtra("callmob"));
                            }
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

                        }
                    } else {

                        onBackPressed();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ContactActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == COUNTRY && resultCode == COUNTRY) {
                Call_Department = data.getStringExtra("Name");
                spinner_department.setText(Call_Department);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

