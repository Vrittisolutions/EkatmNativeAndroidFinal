package com.vritti.crmlib.vcrm7;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ShowContact;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

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
    Spinner spinner_department;
    AppCompatCheckBox checkbox_primary_contact;
    TextView txt_save, txt_reset;
    String Call_Contactname, Call_Designation, Call_Department, Call_Mobile, Call_Email, Call_Office, Addcalljson, Call_Callid, Call_CallType, Call_ProspectId, Deletecalljson, Contact_id, Call_primary, Editcalljson;
    boolean Check_Value;
    ArrayList<ShowContact> showContactArrayList = new ArrayList<>();
    String show_contactname, show_designation, show_department, show_email, show_mobile, show_primary_contact;
    View showcontactconvertView;
    TextView txt_show_contactname, txt_show_contact_designation, txt_show_contact_department, txt_show_contact_mobileno, txt_show_contact_email, txt_show_contact_primary;
    Spinner txt_action;
    EditText edit_contactname, edit_designation, edit_mobile, edit_email;
    TextView txt_edit_save, txt_update_msg, txt_update;
    Spinner spinner_edit_primary, spinner_edit_department;
    String ActionPerformitem,  UserType;
    ArrayAdapter<String> ActionPerform;
    String[] ArrarlistActionPerform = {"Edit", "Delete"};
    int position;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    LinearLayout len_show_calldetails, len_action, len_contact_edit, len_contact;
    private int position1, pos;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_contact_lay);


        init();

        Intent intent = getIntent();
        Call_ProspectId = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        Call_ProspectId = intent.getStringExtra("call_prospect");


        if (isnet()) {
            new StartSession(ContactActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new GetAddShowcallDetailsJSON().execute(Call_ProspectId);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }
    }

    private void init() {


        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);
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

        getSupportActionBar().setTitle("Add Contact");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edt_contact_name = (EditText) findViewById(R.id.edt_contact_name);
        edt_designation = (EditText) findViewById(R.id.edt_designation);
        spinner_department = (Spinner) findViewById(R.id.spinner_department);
        edt_emailid = (EditText) findViewById(R.id.edt_emailid);
        edt_mobile = (EditText) findViewById(R.id.edt_mobile);
        edt_offfice = (EditText) findViewById(R.id.edt_offfice);
        checkbox_primary_contact = (AppCompatCheckBox) findViewById(R.id.checkbox_primary_contact);
        txt_save = (TextView) findViewById(R.id.txt_save);
        txt_reset = (TextView) findViewById(R.id.txt_reset);
        len_show_calldetails = (LinearLayout) findViewById(R.id.len_show_calldetails);
        len_contact = (LinearLayout) findViewById(R.id.len_contact);


        spinner_department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Call_Department = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        txt_save.setOnClickListener(new View.OnClickListener() {
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
        });
        checkbox_primary_contact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (checkbox_primary_contact.isChecked()) {
                    Check_Value = Boolean.parseBoolean("1");
                } else {
                    Check_Value = Boolean.parseBoolean("0");
                }
            }
        });


    }


    class GetAddShowcallDetailsJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_getSuspectContactDetails + "?ProspectId=" + URLEncoder.encode(params[0], "utf-8");

                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    System.out.println("Response Call:" + response.toString());
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";


            }
            return response;
        }

        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (integer.equals("[]")) {

                Toast.makeText(ContactActivity.this, "Data not Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {

                try {
                    JSONArray jResults = null;
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_SHOW_CONTACT, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SHOW_CONTACT, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_SHOW_CONTACT, null, values);

                        Log.e("log data", "" + a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Updatgetshowcontactlist();


            }
        }

    }

    private void Updatgetshowcontactlist() {
        showContactArrayList.clear();

        String query = "SELECT  PKSuspContactDtlsID,ContactName,Designation,ContactPersonDept,Mobile," +
                "EmailId,IsPrimaryContact" +
                " FROM " + db.TABLE_SHOW_CONTACT + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ShowContact showContact = new ShowContact();

                showContact.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                showContact.setPKSuspContactDtlsID(cur.getString(cur.getColumnIndex("PKSuspContactDtlsID")));
                showContact.setContactPersonDept(cur.getString(cur.getColumnIndex("ContactPersonDept")));
                showContact.setDesignation(cur.getString(cur.getColumnIndex("Designation")));
                showContact.setEmailId(cur.getString(cur.getColumnIndex("EmailId")));
                showContact.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                showContact.setIsPrimaryContact(cur.getString(cur.getColumnIndex("IsPrimaryContact")));


                showContactArrayList.add(showContact);

            } while (cur.moveToNext());
            len_show_calldetails.removeAllViews();
            if (showContactArrayList.size() > 0) {
                for (int i = 0; i < showContactArrayList.size(); i++) {
                    addView_showcontact(i);


                }
            }
        }


    }

    private void addView_showcontact(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) ContactActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        position1 = i;


        showcontactconvertView = layoutInflater.inflate(R.layout.crm_lay_show_contact,
                null);

        txt_show_contactname = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contactname);
        txt_show_contact_designation = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_designation);
        txt_show_contact_department = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_department);
        txt_show_contact_mobileno = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_mobileno);
        txt_show_contact_email = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_email);
        txt_show_contact_primary = (TextView) showcontactconvertView.findViewById(R.id.txt_show_contact_primary);
        txt_action = (Spinner) showcontactconvertView.findViewById(R.id.txt_action);
        len_action = (LinearLayout) showcontactconvertView.findViewById(R.id.len_action);
        len_contact_edit = (LinearLayout) showcontactconvertView.findViewById(R.id.len_contact_edit);
        edit_contactname = (EditText) showcontactconvertView.findViewById(R.id.edit_contactname);
        edit_designation = (EditText) showcontactconvertView.findViewById(R.id.edit_designation);
        edit_email = (EditText) showcontactconvertView.findViewById(R.id.edit_email);
        edit_mobile = (EditText) showcontactconvertView.findViewById(R.id.edit_mobile);
        txt_edit_save = (TextView) showcontactconvertView.findViewById(R.id.txt_edit_save);
        spinner_edit_department = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_department);
        spinner_edit_primary = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_primary);

        edit_mobile = (EditText) showcontactconvertView.findViewById(R.id.edit_mobile);

        show_contactname = showContactArrayList.get(position1).getContactName();
        txt_show_contactname.setText(show_contactname);
        show_designation = showContactArrayList.get(position1).getDesignation();
        txt_show_contact_designation.setText(show_designation);
        show_department = showContactArrayList.get(position1).getContactPersonDept();
        txt_show_contact_department.setText(show_department);
        show_mobile = showContactArrayList.get(position1).getMobile();
        txt_show_contact_mobileno.setText(show_mobile);
        show_email = showContactArrayList.get(position1).getEmailId();
        txt_show_contact_email.setText(show_email);
        show_primary_contact = showContactArrayList.get(position1).getIsPrimaryContact();
        txt_show_contact_primary.setText(show_primary_contact);

      /*  for (int position=0; position<partialCallLists.size(); position++) {
            showcontactconvertView.setTag(position);
        }
*/

        for (int position = 0; position < showContactArrayList.size(); position++) {
            showcontactconvertView.setTag(position);
        }


        txt_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (view.getTag() instanceof Integer) {
                    position1 = (Integer) view.getTag();
                }

                ActionPerformitem = txt_action.getSelectedItem().toString();

                if (ActionPerformitem.equals("Delete")) {


                    //  Call_Callid = partialCallLists.get(pos).getCallId();
                    System.out.println("Call_id :" + Call_Callid);
                    Contact_id = showContactArrayList.get(position1).getPKSuspContactDtlsID();
                    System.out.println("Call_id-1 :" + Contact_id);

                    JSONObject jsoncontactdelete = new JSONObject();

                    try {

                        jsoncontactdelete.put("CallId", Call_Callid);
                        jsoncontactdelete.put("PKSuspContactDtlsID", Contact_id);
                        Deletecalljson = jsoncontactdelete.toString();

                        System.out.println("Contact list : " + jsoncontactdelete.toString());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Deletecalljson = Deletecalljson.toString();
                    Deletecalljson = Deletecalljson.replaceAll("\\\\", "");

                    if (isnet()) {
                        new StartSession(ContactActivity.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new POSTdeleteContact().execute(Deletecalljson);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });
                    }

                }
                if (ActionPerformitem.equals("Edit")) {
                    len_contact_edit.setVisibility(View.VISIBLE);
                    Call_Contactname = txt_show_contactname.getText().toString();
                    edit_contactname.setText(Call_Contactname);
                    Call_Designation = txt_show_contact_designation.getText().toString();
                    edit_designation.setText(Call_Designation);
                    Call_Email = txt_show_contact_email.getText().toString();
                    edit_email.setText(Call_Email);
                    Call_Mobile = txt_show_contact_mobileno.getText().toString();
                    edit_mobile.setText(Call_Mobile);
                    Call_Department = spinner_edit_department.getSelectedItem().toString();
                    Call_primary = spinner_edit_primary.getSelectedItem().toString();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });


        txt_edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                System.out.println("Call_id :" + Call_Callid);
                Contact_id = showContactArrayList.get(position1).getPKSuspContactDtlsID();
                System.out.println("Call_id-1 :" + Contact_id);
                Call_Contactname = edit_contactname.getText().toString();
                Call_Designation = edit_designation.getText().toString();
                Call_Email = edit_email.getText().toString();
                Call_Mobile = edit_mobile.getText().toString();
                Call_Department = spinner_edit_department.getSelectedItem().toString();
                Call_primary = spinner_edit_primary.getSelectedItem().toString();
                if (Call_primary.equals("Yes")) {
                    Call_primary = "1";
                } else {
                    Call_primary = "0";

                }

                JSONObject jsoncontactedit = new JSONObject();

                try {

                    jsoncontactedit.put("CallId", Call_Callid);
                    jsoncontactedit.put("PKSuspContactDtlId", Contact_id);
                    jsoncontactedit.put("CallType", Call_CallType);
                    jsoncontactedit.put("ProspectId", Call_ProspectId);
                    jsoncontactedit.put("ContactName", Call_Contactname);
                    jsoncontactedit.put("Designation", Call_Designation);
                    jsoncontactedit.put("DeptRoleName", Call_Department);
                    jsoncontactedit.put("EmailId", Call_Email);
                    //  jsoncontactedit.put("TeleNo","0");
                    jsoncontactedit.put("MobileNo", Call_Mobile);
                    jsoncontactedit.put("IsPrimaryContact", Call_primary);

                    Editcalljson = jsoncontactedit.toString();

                    System.out.println("Contact list : " + jsoncontactedit.toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }
                Editcalljson = Editcalljson.toString();
                Editcalljson = Editcalljson.replaceAll("\\\\", "");

                if (isnet()) {
                    new StartSession(ContactActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new POSTEditSaveContact().execute(Editcalljson);
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }
                    });
                }


            }
        });
        len_show_calldetails.addView(showcontactconvertView);
        int position = len_show_calldetails.indexOfChild(showcontactconvertView);
        len_show_calldetails.setTag(position);


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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactActivity.this);
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
                res = ut.OpenPostConnection(url, params[0]);

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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTSaveEditContact;
            System.out.println("BusinessAPIURL-1 :" + Editcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
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


            Toast.makeText(ContactActivity.this, "Contact update successfully", Toast.LENGTH_LONG).show();
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_POSTAddContact;
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0]);
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


            Toast.makeText(ContactActivity.this, "Contact added successfully", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ContactActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

}

