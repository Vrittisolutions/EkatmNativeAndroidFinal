package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Customer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TicketActivityServiceReportCustomerList extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SQLiteDatabase sql;
    private Toolbar toolbar;

    SharedPreferences userpreferences;
    ProgressBar progressBar;
    ListView listservicerpt;
    EditText edt_search_cust;
    Button img_add_cust;
    ArrayList<Customer> customerArrayList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_service_report_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_support);
        toolbar.setTitle(R.string.app_name_toolbar_service);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        customerArrayList=new ArrayList<>();

        progressBar = (ProgressBar) findViewById(R.id.progressbar_custmer);
        listservicerpt = (ListView) findViewById(R.id.listview_cust);
        edt_search_cust = (EditText) findViewById(R.id.searchcust);
        img_add_cust = (Button) findViewById(R.id.img_add_cust);



        edt_search_cust.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3)  {
                    final String pass = s.toString();


                    if (isnet()) {
                        new StartSession(TicketActivityServiceReportCustomerList.this, new CallbackInterface() {
                            @Override
                            public void callMethod() {
                                new DownloaDownloadCustomerJSON().execute(pass);
                            }

                            @Override
                            public void callfailMethod(String msg) {

                            }
                        });

                    } else {

                    }

                } else {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        listservicerpt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String custname = customerArrayList.get(position).getCustomer_name();
                String ShiftMasterid = customerArrayList.get(position).getShiftKeyMasterId();
                Intent i = new Intent(getApplicationContext(), TicketActivityServiceReport.class);
                i.putExtra("CustName", custname);
                i.putExtra("CustVenderMasterId", ShiftMasterid);
                startActivity(i);
            }
        });
    }



    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TicketActivityServiceReportCustomerList.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return true;
    }
    class DownloaDownloadCustomerJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error", name, id;
        String a[], b[];
        List<String> suggestions;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            suggestions = new ArrayList<String>();
            try {
                String url = CompanyURL + WebUrlClass.api_GetAutoCompleteReported +
                        "?txtval=" + URLEncoder.encode(params[0], "UTF-8");

                res = ut.OpenConnection(url,getApplicationContext());
                if (res != null) {
                    response = res.toString();
                    response = response.substring(1, response.length() - 1);
                    response = response.substring(1, response.length() - 1);
                    a = response.split("\"");
                    customerArrayList.clear();
                    for (int i = 0; i < a.length; i++) {
                        if (a[i].equalsIgnoreCase(",")) {

                        } else {
                            name=a[i];
                            //b = a[i].split(",");
                            // name = b[0];
                            String[] arrayString = name.split("@");

                            String custname = arrayString[0];
                            String shiftmasterid = arrayString[1];
                            Customer customer = new Customer();
                            customer.setCustomer_name(custname);
                            customer.setShiftKeyMasterId(shiftmasterid);
                            customerArrayList.add(customer);
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //  dismissProgressDialog();
                                MySpinnerAdapter customAdcity = new MySpinnerAdapter(TicketActivityServiceReportCustomerList.this,
                                        R.layout.vwb_custom_spinnerticket_txt, customerArrayList);
                                listservicerpt.setAdapter(customAdcity);
                                customAdcity.notifyDataSetChanged();
                            }
                        });
                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

                 hideProgress();        }

    }
    private  class MySpinnerAdapter extends ArrayAdapter<Customer> {
        // Initialise custom font, for example:
        ArrayList<Customer> customerArrayList = new ArrayList<>();


        public MySpinnerAdapter(Context context, int textViewResourceId, ArrayList<Customer> customerArrayList) {
            super(context, textViewResourceId, customerArrayList);
            this.customerArrayList=customerArrayList;
        }
        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.vwb_spinner_text, null);
            TextView textView = (TextView) v.findViewById(R.id.txt);
            textView.setText(customerArrayList.get(position).getCustomer_name());


            return v;

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


}
