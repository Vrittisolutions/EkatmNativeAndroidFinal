package com.vritti.crm.vcrm7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.EnquiryBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONException;
import org.json.JSONObject;

public class ActvityEnquiryDetails extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    private String name, registeryID;
    SQLiteDatabase sql;
    ProgressBar mprogress;
    TextView txtcustname, txtcontname, txtcontactNumber, txtemail, txtenqdetail;
    EditText edtactiontakn, edtresonforcancellation;
    LinearLayout ln_actiontakn, ln_resonforcancellation;
    Button btn_convert_prospect, btn_cancel;
    SharedPreferences userpreferences;
    private static String CustomerName, ContactName, ContactNumber, Email, EnaquiryDetail;
    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_actvity_enquiry_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        EnquiryBean data = (EnquiryBean) getIntent().getSerializableExtra("EnquiryDetailExtra");
        CustomerName = data.getCustomerName();
        ContactName = data.getContactName();
        ContactNumber = data.getContactNumber();
        registeryID = data.getEnquiryRegistryId();
        Email = data.getEmail();
        EnaquiryDetail = data.getEnquiryDetails();
        Log.e("", "" + CustomerName);
        init();
        setListner();
    }

    private void init() {
        mprogress = (ProgressBar) findViewById(R.id.progressbar_enquiry);
        txtcustname = (TextView) findViewById(R.id.txtcustname);
        txtcustname.setText(CustomerName);

        txtcontname = (TextView) findViewById(R.id.txtcontname);
        txtcontname.setText(ContactName);

        txtcontactNumber = (TextView) findViewById(R.id.txtcontnumber);
        txtcontactNumber.setText(ContactNumber);

        txtemail = (TextView) findViewById(R.id.txtemail);
        txtemail.setText(Email);

        txtenqdetail = (TextView) findViewById(R.id.edtenqdetail);
        txtenqdetail.setText(EnaquiryDetail);

        ln_actiontakn = (LinearLayout) findViewById(R.id.lnactiontakn);
        ln_actiontakn.setVisibility(View.GONE);
        edtactiontakn = (EditText) findViewById(R.id.edtactntakn);
        ln_resonforcancellation = (LinearLayout) findViewById(R.id.ln_reson);
        ln_resonforcancellation.setVisibility(View.GONE);
        edtresonforcancellation = (EditText) findViewById(R.id.edtresonforcancellation);

        btn_convert_prospect = (Button) findViewById(R.id.btn_save_prostect);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        imm.hideSoftInputFromWindow(edtactiontakn.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
        imm.hideSoftInputFromWindow(edtresonforcancellation.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void setListner() {
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ln_actiontakn.getVisibility() == View.GONE)
                        && (ln_resonforcancellation.getVisibility() == View.GONE)) {
                    ln_actiontakn.setVisibility(View.VISIBLE);
                    ln_resonforcancellation.setVisibility(View.VISIBLE);
                    imm.hideSoftInputFromWindow(edtactiontakn.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
                    imm.hideSoftInputFromWindow(edtresonforcancellation.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
                    Toast.makeText(getApplicationContext(), "Enter reson for cancellation", Toast.LENGTH_LONG).show();
                } else {
                    if (isnet()) {
                        String actakn = edtactiontakn.getText().toString();
                        String resoncancellation = edtresonforcancellation.getText().toString();
                        JSONObject obj = new JSONObject();
                        try {
                            obj.put("EnquiryRegistryId", registeryID);
                            obj.put("ActionTaken", actakn);
                            obj.put("ReasonForCancellation", resoncancellation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String jobj = obj.toString().replaceAll("\\\\", "");
                        new StartSession(ActvityEnquiryDetails.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                showProgress();
                                new postCancelEnquiry().execute(jobj);
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                                hideProgress();
                            }
                        });

                    }

                }

            }
        });
        btn_convert_prospect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ActvityEnquiryDetails.this, ProspectEnterpriseActivity.class);
                intent.putExtra("custname", CustomerName);
                intent.putExtra("contactname", ContactName);
                intent.putExtra("contactnumber", ContactNumber);
                intent.putExtra("registryID", registeryID);
                intent.putExtra("email", Email);
                intent.putExtra("enquirydetail", EnaquiryDetail);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void showProgress() {
        mprogress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mprogress.setVisibility(View.GONE);

    }


    class postCancelEnquiry extends AsyncTask<String, Void, String> {
        String res = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Cancel_Enuiry;
            try {

                Object response = ut.OpenPostConnection(url, params[0], getApplicationContext());
                res = response.toString();
                res = res.replaceAll("\\\\", "");
//                res = res.substring(1, res.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hideProgress();

            if (res.equalsIgnoreCase("")) {
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_ENQUIRY, null);
                int a = c.getCount();
                Log.e("a:", "" + a);
                sql.delete(db.TABLE_ENQUIRY, "EnquiryRegistryId=?", new String[]{registeryID});
                Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_ENQUIRY, null);
                int b = c1.getCount();
                Log.e("b:", "" + b);
                Intent intent = new Intent(ActvityEnquiryDetails.this, ActivityEnquiryList.class);
                startActivity(intent);
                finish();

                Toast.makeText(getApplicationContext(), "Enquiry cancelled successfully", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getApplicationContext(), "Can not delete enquiry data", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ActvityEnquiryDetails.this, ActivityEnquiryList.class);
        startActivity(intent);
        finish();
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

}
