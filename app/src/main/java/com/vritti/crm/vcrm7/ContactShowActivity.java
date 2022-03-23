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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.vworkbench.ActivityMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import static com.vritti.crm.vcrm7.CountryListActivity.COUNTRY;

/**
 * Created by sharvari on 29-Sep-17.
 */

public class ContactShowActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    TextView spinner_department;
    AppCompatCheckBox checkbox_primary_contact;
    Button txt_save, txt_reset;
    String Call_Contactname, Call_Designation, Call_Department, Call_Mobile, Call_Email, Call_Office, Addcalljson, Call_Callid, Call_CallType, Call_ProspectId, Deletecalljson, Contact_id, Call_primary, Editcalljson;
    boolean Check_Value;
    ArrayList<ShowContact> showContactArrayList = new ArrayList<>();
    String show_contactname="", show_designation="", show_department="", show_email="", show_mobile="", show_primary_contact="";
    View showcontactconvertView;
    TextView txt_show_contactname, txt_show_contact_designation, txt_show_contact_department, txt_show_contact_mobileno, txt_show_contact_email, txt_show_contact_primary;
    Spinner txt_action;
    EditText edit_contactname, edit_designation, edit_mobile, edit_email;
    ImageView edit, delete,add;
    Spinner spinner_edit_primary, spinner_edit_department;
    String ActionPerformitem,  UserType;
    ArrayAdapter<String> ActionPerform;
    int position;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    LinearLayout len_show_calldetails, len_action, len_contact_edit, len_contact;
    private int position1, pos;
    LinearLayout len;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title,txt_notfound;
    private String Contact="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_contact_lay_v1);


        init();

        Intent intent = getIntent();
        Call_Callid = intent.getStringExtra("callid");
        Call_CallType = intent.getStringExtra("call_type");
        Call_ProspectId = intent.getStringExtra("call_prospect");
        if (getIntent().hasExtra("callmob")) {
            Contact = getIntent().getStringExtra("callmob");
        }

        if (isnet()) {
            new StartSession(ContactShowActivity.this, new CallbackInterface() {
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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        len_show_calldetails=findViewById(R.id.len_show_calldetails);
        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);
        txt_notfound=findViewById(R.id.txt_notfound);

        img_add.setVisibility(View.VISIBLE);
        txt_title.setText("Contact List");
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactShowActivity.this, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("Mode", "A");
                if (getIntent().hasExtra("callmob")) {
                    intent.putExtra("callmob", Contact);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
            progressDialog = new ProgressDialog(ContactShowActivity.this);
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
                String url="";
                if (Call_CallType.equals("2")) {
                    url= CompanyURL + WebUrlClass.api_getCustContactDetails + "?ProspectId=" + URLEncoder.encode(params[0], "utf-8");
                }else {
                    url= CompanyURL + WebUrlClass.api_getSuspectContactDetails + "?ProspectId=" + URLEncoder.encode(params[0], "utf-8");

                }
                res = ut.OpenConnection(url, ContactShowActivity.this);
                response = res.toString();
                response = response.substring(1, response.length() - 1);

                response="{\"Activity\":\""+response+"\n" +"\"}";
                /*res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    System.out.println("Response Call:" + response.toString());
                }*/

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";


            }
            return response;
        }

        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (integer.equals("[]")) {

                Toast.makeText(ContactShowActivity.this, "Data not Available", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {

                try {
                    JSONArray jResults = null;
                    JSONObject  obj = new JSONObject(response);

                    String Msgcontent=obj.getString("Activity");
                    jResults = new JSONArray(Msgcontent);

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

        String query = "SELECT PKSuspContactDtlsID,ContactName,Designation,Mobile," +
                "EmailId,IsPrimaryContact,FKSuspectId" +
                " FROM " + db.TABLE_SHOW_CONTACT + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                ShowContact showContact = new ShowContact();

                showContact.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                showContact.setPKSuspContactDtlsID(cur.getString(cur.getColumnIndex("PKSuspContactDtlsID")));
                showContact.setDesignation(cur.getString(cur.getColumnIndex("Designation")));
                showContact.setEmailId(cur.getString(cur.getColumnIndex("EmailId")));
                showContact.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                showContact.setFKSuspectId(cur.getString(cur.getColumnIndex("FKSuspectId")));
                showContact.setIsPrimaryContact(cur.getString(cur.getColumnIndex("IsPrimaryContact")));


                showContactArrayList.add(showContact);

            } while (cur.moveToNext());
            len_show_calldetails.removeAllViews();
            if (showContactArrayList.size() > 0) {
                for (int i = 0; i < showContactArrayList.size(); i++) {
                    addView_showcontact(i);


                }
            }
            txt_notfound.setVisibility(View.GONE);

        }else {
            txt_notfound.setVisibility(View.VISIBLE);
        }


    }

    private void addView_showcontact(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) ContactShowActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        position1 = i;


        showcontactconvertView = layoutInflater.inflate(R.layout.crm_lay_show_contact_v1, null);

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
        edit = (ImageView) showcontactconvertView.findViewById(R.id.edit);
        delete = (ImageView) showcontactconvertView.findViewById(R.id.delete);
        add = (ImageView) showcontactconvertView.findViewById(R.id.add);
        spinner_edit_department = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_department);
        spinner_edit_primary = (Spinner) showcontactconvertView.findViewById(R.id.spinner_edit_primary);
        len = (LinearLayout) showcontactconvertView.findViewById(R.id.len);

        len.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("ID", showContactArrayList.get(position1).getPKSuspContactDtlsID());
                intent.putExtra("Name", showContactArrayList.get(position1).getContactName());
                setResult(COUNTRY, intent);
                finish();

            }
        });

        edit_mobile = (EditText) showcontactconvertView.findViewById(R.id.edit_mobile);
        show_contactname = showContactArrayList.get(position1).getContactName();
        if (!show_contactname.equals("")) {
            len.setVisibility(View.VISIBLE);
            txt_show_contactname.setText(show_contactname);
            show_designation = showContactArrayList.get(position1).getDesignation();
            txt_show_contact_designation.setText(show_designation);
            show_department = showContactArrayList.get(position1).getContactPersonDept();
            if (show_department==null||show_department.equals("null")||show_department.equals("")){
                txt_show_contact_designation.setVisibility(View.GONE);
            }else {
                txt_show_contact_department.setText(show_department);
            }

            show_mobile = showContactArrayList.get(position1).getMobile();
            txt_show_contact_mobileno.setText(show_mobile);
            show_email = showContactArrayList.get(position1).getEmailId();
            txt_show_contact_email.setText(show_email);
            if (showContactArrayList.get(position1).getIsPrimaryContact().equalsIgnoreCase("Yes")){
                txt_show_contactname.setTextColor(getResources().getColor(R.color.orange));
            }else {
            }
            txt_show_contact_primary.setText(show_primary_contact);
        }

      /*  for (int position=0; position<partialCallLists.size(); position++) {
            showcontactconvertView.setTag(position);
        }
*/

        for (int position = 0; position < showContactArrayList.size(); position++) {
            showcontactconvertView.setTag(position);
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    new StartSession(ContactShowActivity.this, new CallbackInterface() {
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
        });

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


       /* add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });*/

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ContactActivity.class);
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("name",showContactArrayList.get(position1).getContactName());
                intent.putExtra("designation",showContactArrayList.get(position1).getDesignation());
                intent.putExtra("dept",showContactArrayList.get(position1).getContactPersonDept());
                intent.putExtra("contact",showContactArrayList.get(position1).getMobile());
                intent.putExtra("email",showContactArrayList.get(position1).getEmailId());
                intent.putExtra("Mode","E");
               /* if (Call_primary.equals("Yes")) {
                    Call_primary = "1";
                } else {
                    Call_primary = "0";

                }*/
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);



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
        String url="";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactShowActivity.this);
            progressDialog.setMessage("Please wait data sending...");
            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (Call_CallType.equals("2")){
                url = CompanyURL + WebUrlClass.api_POSTdeleteCustContact;
            }else {
                url = CompanyURL + WebUrlClass.api_POSTdeleteContact;
            }
            System.out.println("BusinessAPIURL-1 :" + Addcalljson);

            try {
                res = ut.OpenPostConnection(url, params[0],ContactShowActivity.this);

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


            Toast.makeText(ContactShowActivity.this, "Contact deleted successfully", Toast.LENGTH_LONG).show();
            onBackPressed();
        }

    }

    class POSTEditSaveContact extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ContactShowActivity.this);
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
                res = ut.OpenPostConnection(url, params[0],ContactShowActivity.this);
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


            Toast.makeText(ContactShowActivity.this, "Contact update successfully", Toast.LENGTH_LONG).show();
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
            progressDialog = new ProgressDialog(ContactShowActivity.this);
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
                res = ut.OpenPostConnection(url, params[0],ContactShowActivity.this);
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


            Toast.makeText(ContactShowActivity.this, "Contact added successfully", Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ContactShowActivity.this.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
