package com.vritti.vwb.vworkbench;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.vcrm7.BusinessProspectusActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.ApproverData;
import com.vritti.vwb.Beans.AuthUserBeans;
import com.vritti.vwb.Beans.DealerCode;
import com.vritti.vwb.Beans.DealerName;
import com.vritti.vwb.Beans.UserList;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CreditSanctionActivity extends AppCompatActivity {

    ScrollView scroll_view;
    ArrayList<String> common = new ArrayList<String>();
    ArrayList<String> transport = new ArrayList<String>();

    AutoCompleteTextView edt_trans_type,edt_security_cheque,edt_mou,edt_agreement,edt_approver,edt_dealername,edt_dealercode;
    EditText edt_commitment,edt_dealer_territory,edt_nc_holding,edt_account_remark,edt_order_qty,
            edt_approval_amt,edt_last_appr_date,edt_current_ledger_amt,edt_reason_approval;
    ImageView img_comm_calender;
    private String CommitmentDate;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",

    UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    public  Context context;
    private SQLiteDatabase sql;
    ArrayList<ApproverData> lsClaimApproverList;
    ArrayList<DealerName> dealerNameArrayList;
    ArrayList<DealerCode> dealerCodeArrayList;
    ProgressBar mProgress;
    private String docHdrId="";
    private String Dealername="",DealerCode="",Territory="",TranportType="",Acc_Remark="",Security="",MOU="",Agreement=""
            ,NC_Holding="",OrderQty="",CreditAmt="",Approver="",LastAppDate="",Current_Amt="",ReasonApproval="";
    private String DealerId="";
    private String CustomerId="";
    Button btn_save;
    String uuidInString = "";
    private UUID uuid;
    private JSONObject jsonObject1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_approval_request);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lsClaimApproverList = new ArrayList<>();
        dealerNameArrayList = new ArrayList<>();
        dealerCodeArrayList = new ArrayList<>();

        context =CreditSanctionActivity.this;
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

       /* ((ScrollView) findViewById(R.id.scroll_view)).post(new Runnable() {
            public void run() {
                ((ScrollView) findViewById(R.id.scroll_view)).fullScroll(View.FOCUS_UP);
            }
        });*/

        edt_trans_type=findViewById(R.id.edt_trans_type);
        edt_security_cheque=findViewById(R.id.edt_security_cheque);
        edt_mou=findViewById(R.id.edt_mou);
        edt_agreement=findViewById(R.id.edt_agreement);
        edt_approver=findViewById(R.id.edt_approver);
        edt_commitment=findViewById(R.id.edt_commitment);
        img_comm_calender=findViewById(R.id.img_comm_calender);
        mProgress=findViewById(R.id.progressbar_business);
        edt_dealername=findViewById(R.id.edt_dealername);
        edt_dealercode=findViewById(R.id.edt_dealercode);
        edt_dealer_territory=findViewById(R.id.edt_dealer_territory);
        edt_nc_holding=findViewById(R.id.edt_nc_holding);
        edt_account_remark=findViewById(R.id.edt_account_remark);
        edt_order_qty=findViewById(R.id.edt_order_qty);
        edt_approval_amt=findViewById(R.id.edt_approval_amt);
        edt_last_appr_date=findViewById(R.id.edt_last_appr_date);
        edt_current_ledger_amt=findViewById(R.id.edt_current_ledger_amt);
        edt_reason_approval=findViewById(R.id.edt_reason_approval);
        btn_save=findViewById(R.id.button_s);

        transport.add("FOR");
        transport.add("OWN");
        ArrayAdapter<String> transportadapter = new ArrayAdapter(CreditSanctionActivity.this, android.R.layout.simple_list_item_1, transport);
        transportadapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        edt_trans_type.setAdapter(transportadapter);

        common.add( "Yes" );
        common.add( "No" );
        ArrayAdapter<String> adapter = new ArrayAdapter(CreditSanctionActivity.this, android.R.layout.simple_list_item_1, common);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        edt_security_cheque.setAdapter(adapter);
        edt_mou.setAdapter(adapter);
        edt_agreement.setAdapter(adapter);

        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date1);
        edt_commitment.setText(dateString);


        edt_trans_type.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        edt_trans_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TranportType=edt_trans_type.getText().toString();
            }
        });
        edt_security_cheque.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        edt_security_cheque.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Security=edt_security_cheque.getText().toString();

            }
        });

        edt_agreement.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        edt_agreement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agreement=edt_agreement.getText().toString();

            }
        });

        edt_mou.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        edt_mou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MOU=edt_mou.getText().toString();
            }
        });
        edt_approver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });
        edt_approver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Approver=lsClaimApproverList.get(position).getUserMasterId();
            }
        });

        img_comm_calender.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CreditSanctionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + year;
                                CommitmentDate = year + "/"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "/" + dayOfMonth;
                                edt_commitment.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        edt_dealername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>4) {
                    Dealername = s.toString();

                    if (isnet()) {
                        showprogress();
                        new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                            @Override
                            public void callMethod() {

                                new DownloadDealerName().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(CreditSanctionActivity.this, msg);
                                hidprogress();
                            }
                        });
                    } else {
                        Toast.makeText(CreditSanctionActivity.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_dealercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s.length() > 4) {
                        DealerCode = s.toString();
                        if (isnet()) {
                            showprogress();
                            new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                                @Override
                                public void callMethod() {

                                    new DownloadDealerCode().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(CreditSanctionActivity.this, msg);
                                    hidprogress();
                                }
                            });
                        } else {
                            Toast.makeText(CreditSanctionActivity.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                        }
                    }


            }
                @Override
                public void afterTextChanged (Editable s){

                }

        });

        edt_dealername.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String  dealername=dealerNameArrayList.get(position).getUserName();
                String  code=dealerNameArrayList.get(position).getUserCode();
                DealerId=dealerNameArrayList.get(position).getUserMasterId();
                edt_dealercode.setText(code);

                if (isnet()) {
                    showprogress();
                    new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                        @Override
                        public void callMethod() {

                            new GetLasApprNCHold().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(CreditSanctionActivity.this, msg);
                            hidprogress();
                        }
                    });
                } else {
                    Toast.makeText(CreditSanctionActivity.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edt_dealercode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  dealername=dealerCodeArrayList.get(position).getUserName();
                String  code=dealerCodeArrayList.get(position).getUserCode();
                DealerId=dealerCodeArrayList.get(position).getUserMasterId();
                edt_dealername.setText(dealername);

                if (isnet()) {
                    showprogress();
                    new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                        @Override
                        public void callMethod() {

                            new GetLasApprNCHold().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(CreditSanctionActivity.this, msg);
                            hidprogress();
                        }
                    });
                } else {
                    Toast.makeText(CreditSanctionActivity.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(CreditSanctionActivity.this, "Click", Toast.LENGTH_SHORT).show();

                Acc_Remark = edt_account_remark.getText().toString();
                OrderQty = edt_order_qty.getText().toString();
                ReasonApproval = edt_reason_approval.getText().toString();
                String date = edt_commitment.getText().toString();
                CommitmentDate = formateDateFromstring("dd/MM/yyyy", "yyyy/MM/dd", date);
                CreditAmt = edt_approval_amt.getText().toString();

                if (Dealername.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Dealer Name", Toast.LENGTH_SHORT).show();
                } else if (DealerCode.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Dealer Code", Toast.LENGTH_SHORT).show();
                } else if (TranportType.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Transport Type", Toast.LENGTH_SHORT).show();
                } else if (Acc_Remark.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Accounts Remark", Toast.LENGTH_SHORT).show();
                } else if (Security.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Select Security Cheque", Toast.LENGTH_SHORT).show();
                } else if (MOU.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Select MOU", Toast.LENGTH_SHORT).show();
                } else if (Agreement.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Select Agreement Status", Toast.LENGTH_SHORT).show();
                } else if (OrderQty.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Order Qty", Toast.LENGTH_SHORT).show();
                } else if (CreditAmt.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Credit Approval amt", Toast.LENGTH_SHORT).show();
                } else if (ReasonApproval.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Enter Reason", Toast.LENGTH_SHORT).show();
                } else if (Approver.equals("")) {
                    Toast.makeText(CreditSanctionActivity.this, "Please Select Approver", Toast.LENGTH_SHORT).show();
                } else {

                    final JSONObject jsonObject = new JSONObject();

                    try {

                        jsonObject.put("Transport", TranportType);
                        jsonObject.put("ActRemark", Acc_Remark);
                        jsonObject.put("Security", Security);
                        jsonObject.put("Customerid", DealerId);
                        jsonObject.put("MOU", MOU);
                        jsonObject.put("Agreement", Agreement);
                        jsonObject.put("Commitdate", CommitmentDate);
                        jsonObject.put("Qty", OrderQty);
                        jsonObject.put("CreditAmt", CreditAmt);
                        jsonObject.put("Reason", ReasonApproval);
                        jsonObject.put("Approver", Approver);
                        jsonObject.put("DocMthdId", docHdrId);
                        //   jsonObject.put("PKCreditId",uuidInString);

                        jsonObject1 = new JSONObject();
                        jsonObject1.put("Data", jsonObject);

                        Log.d("JSON", jsonObject1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (isnet()) {
                        showprogress();
                        new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                            @Override
                            public void callMethod() {

                                new UpLoadData().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(CreditSanctionActivity.this, msg);
                                hidprogress();
                            }
                        });
                    } else {
                        Toast.makeText(CreditSanctionActivity.this, "Internet connection not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (isnet()) {
            showprogress();
            new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                @Override
                public void callMethod() {

                    new GetDochdrId().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(CreditSanctionActivity.this, msg);
                    hidprogress();
                }
            });
        }else {
            Toast.makeText(CreditSanctionActivity.this,"Internet connection not available",Toast.LENGTH_SHORT).show();
        }


    }
    class GetDochdrId extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected Integer doInBackground(Integer... params) {

            url =  CompanyURL + WebUrlClass.api_getCreditDocAppInfo;

             try {
                res = ut.OpenConnection(url, CreditSanctionActivity.this);
                res = res.replaceAll("\\\\", "");
                //res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }


        protected void onPostExecute(Integer integer) {

            hidprogress();
            super.onPostExecute(integer);
            if (!res.equalsIgnoreCase("")) {


                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        docHdrId = jsonObject.getString("DocApprMthdId");
                        //stringArrayList.add("DocApprMthdId2");

                    }

                    if(ut.isNet(context)) {
                      showprogress();
                        new StartSession(CreditSanctionActivity.this, new CallbackInterface() {

                            @Override
                            public void callMethod() {

                                new GetClaimApprover().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(CreditSanctionActivity.this, msg);
                                hidprogress();
                            }
                        });
                    }

                    /*ArrayList<String> stringArrayList = new ArrayList<>();
                    stringArrayList.add(DocApprMthdId2);*/
                    //  stringArrayList.add(DocApprMthdId2);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // docHdrId = stringArrayList.get(0);
            }else{

            }
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
/*
    private void UpdateApproverList() {
        lsClaimApproverList.clear();
        String query = "SELECT * FROM " + db.TABLE_CLAIM_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                ApproverData authUserBeans = new ApproverData();
                authUserBeans.setUserName(cur.getString(cur.getColumnIndex("UserName")));//UserLoginId
                authUserBeans.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                authUserBeans.setApprLvl(cur.getString(cur.getColumnIndex("ApprLvl")));
                lsClaimApproverList.add(authUserBeans);
            } while (cur.moveToNext());
        }
        ArrayAdapter<ApproverData> dataAdapter = new ArrayAdapter<ApproverData>(CreditSanctionActivity.this, android.R.layout.simple_spinner_item, lsClaimApproverList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edt_approver.setAdapter(dataAdapter);
    }
*/
    void showprogress() {
        mProgress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        mProgress.setVisibility(View.GONE);

    }

    class GetClaimApprover extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (!res.equalsIgnoreCase("")) {

                ContentValues values = new ContentValues();
                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int i=0;i<jResults.length();i++){
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        ApproverData authUserBeans = new ApproverData();
                        authUserBeans.setUserName(jsonObject.getString("UserName"));//UserLoginId
                        authUserBeans.setUserMasterId(jsonObject.getString("UserMasterId"));
                        authUserBeans.setApprLvl(jsonObject.getString("ApprLvl"));
                        lsClaimApproverList.add(authUserBeans);
                    }
                    ArrayAdapter<ApproverData> dataAdapter = new ArrayAdapter<ApproverData>(CreditSanctionActivity.this, android.R.layout.simple_spinner_item, lsClaimApproverList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edt_approver.setAdapter(dataAdapter);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }else{
                if(ut.isNet(context)){
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"No Approver Data Found",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_claim_approver +"?DocMthdId=" + docHdrId;

            Log.i("url::",url);

            // url = http://b207.ekatm.com/api/MyClaimAPI/getApproverList?DocMthdId=69
            try {
                res = ut.OpenConnection(url, CreditSanctionActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);



            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    class DownloadDealerName extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (!res.equalsIgnoreCase("")) {
               try {

                   dealerNameArrayList.clear();
                   JSONArray jResults = new JSONArray(res);
                   for (int i=0;i<jResults.length();i++){
                       DealerName dealerName=new DealerName();
                       String name=jResults.getString(i);
                       String[] namesList = name.split(",");
                       String custname = namesList [0];
                       dealerName.setUserName(custname);
                       String custid = namesList [1];
                       dealerName.setUserMasterId(custid);
                       String condition = namesList [2];
                       dealerName.setCondition(condition);
                       String code = namesList [3];
                       dealerName.setUserCode(code);
                       dealerNameArrayList.add(dealerName);
                   }



                   ArrayAdapter<DealerName> dataAdapter = new ArrayAdapter<DealerName>(CreditSanctionActivity.this, android.R.layout.simple_spinner_item, dealerNameArrayList);
                   dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                   edt_dealername.setAdapter(dataAdapter);

               }catch (Exception e){
                   e.printStackTrace();
               }

            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_FillCustomerByName +"?SearchText=" + Dealername;
            try {
                res = ut.OpenConnection(url, CreditSanctionActivity.this);
                res=res.toString();
                //res = res.replaceAll("\\[]", "");
             //   res = res.substring(1, res.length() - 1);




            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    class DownloadDealerCode extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (!res.equalsIgnoreCase("")) {
                try {

                    dealerCodeArrayList.clear();
                    JSONArray jResults = new JSONArray(res);
                    for (int i=0;i<jResults.length();i++){
                        DealerCode dealerCode=new DealerCode();
                        String name=jResults.getString(i);
                        String[] namesList = name.split(",");
                        String custname = namesList [0];
                        dealerCode.setUserName(custname);
                        String custid = namesList [1];
                        dealerCode.setUserMasterId(custid);
                        String condition = namesList [2];
                        dealerCode.setCondition(condition);
                        String code = namesList [3];
                        dealerCode.setUserCode(code);
                        dealerCodeArrayList.add(dealerCode);
                    }



                    ArrayAdapter<DealerCode> dataAdapter = new ArrayAdapter<DealerCode>(CreditSanctionActivity.this, android.R.layout.simple_spinner_item, dealerCodeArrayList);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    edt_dealercode.setAdapter(dataAdapter);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_FillCustomerByCode +"?SearchText=" + DealerCode;
            try {
                res = ut.OpenConnection(url, CreditSanctionActivity.this);
                res=res.toString();
                //res = res.replaceAll("\\[]", "");
                //   res = res.substring(1, res.length() - 1);




            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    class GetLasApprNCHold extends AsyncTask<Integer, Void, Integer> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (!res.equalsIgnoreCase("")) {

                try {

                    JSONObject jsonObject=new JSONObject(res);

                    JSONArray CNjResults = jsonObject.getJSONArray("CN");
                    if (CNjResults.length()>0){

                    }
                    JSONArray CGNCjResults = jsonObject.getJSONArray("CGNC");
                    if (CGNCjResults.length()>0){
                        for (int i=0;i<CGNCjResults.length();i++) {
                            JSONObject TerrJson = CGNCjResults.getJSONObject(i);
                            int NCHolding =TerrJson.getInt("NCHolding");
                                edt_nc_holding.setText(String.valueOf(NCHolding));
                        }
                    }
                    JSONArray TerrResults = jsonObject.getJSONArray("Terr");
                    if (TerrResults.length()>0){
                        for (int i=0;i<TerrResults.length();i++) {
                            JSONObject TerrJson = TerrResults.getJSONObject(i);
                            String TerritoryName =TerrJson.getString("TerritoryName");
                                    edt_dealer_territory.setText(TerritoryName);
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            }else{
                if(ut.isNet(context)){
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"No Approver Data Found",Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            // url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_GetLasApprNCHold +"?CustomerId=" + DealerId;

            Log.i("url::",url);

            // url = http://b207.ekatm.com/api/MyClaimAPI/getApproverList?DocMthdId=69
            try {
                res = ut.OpenConnection(url, CreditSanctionActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);



            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    private class UpLoadData extends AsyncTask<String, String, String> {

        Object res;
        String response, data;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String url = CompanyURL + WebUrlClass.api_Post;
            try {
                res = ut.OpenPostConnection(url, jsonObject1.toString(), CreditSanctionActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
                hidprogress();
                if (s.contains("Success")) {
                    Toast.makeText(CreditSanctionActivity.this,"Record added successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CreditSanctionActivity.this,ActivityMain.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();



            }
        }
    }
    private void getuuid() {
        uuid = UUID.randomUUID();
        uuidInString = uuid.toString();
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
