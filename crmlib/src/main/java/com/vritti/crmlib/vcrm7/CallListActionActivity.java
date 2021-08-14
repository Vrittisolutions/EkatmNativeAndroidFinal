package com.vritti.crmlib.vcrm7;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.CallHistoryAdapter;
import com.vritti.crmlib.classes.CallHistory;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

/**
 * Created by sharvari on 03-Jan-17.
 */

public class CallListActionActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    LinearLayout textEdit, textRequest, textEditProspect, layAmt,
            textAddQuotation, textOpportunityRating,
            textOpportunityUpdate, textfilldatasheet;
    String callid, firmname, calltype, SourceId, FormId, callstatus, ExpectedValue,
            Amount, ExpectedCloserDate, table, tablename = "", ProspectId;
    TextView txtcall, txtHome, txtTravelPlan, txtExecutivePerformance,
            txtProspectMaster, txtEntityMaster, txtCityMaster, txtBusinessSegMaster,
            txtTeritoryMaster, txtReportlist, textAmt, txtEnquiryform, txtEnquiryFormSetting, txt_rating, txt_update;
    Toolbar toolbar_action;
    MenuItem menuItem;
    SQLiteDatabase sql;
    Dialog dialog;
    ListView callhistory_listview;
    SharedPreferences userpreferences;
    ArrayList<CallHistory> callHistoryArrayList;
    CallHistoryAdapter callHistoryAdapter;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_calllist_action);
        init();

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);
        callHistoryArrayList = new ArrayList<>();
        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
        firmname = intent.getStringExtra("firmname");
        calltype = intent.getStringExtra("calltype");
        table = intent.getStringExtra("table");
        ProspectId = intent.getStringExtra("ProspectId");
        // callstatus= intent.getStringExtra("callstatus");
        txtcall.setText(firmname);
        if (table.equalsIgnoreCase("Call")) {
            tablename = db.TABLE_CRM_CALL;
        } else if (table.equalsIgnoreCase("Opportunity")) {
            tablename = db.TABLE_CRM_CALL_OPP;
        }

        if (calltype.equalsIgnoreCase("3")) {
            textfilldatasheet.setVisibility(View.VISIBLE);
        } else {
            textfilldatasheet.setVisibility(View.GONE);
        }
        if (calltype.equalsIgnoreCase("2")) {
            textfilldatasheet.setVisibility(View.VISIBLE);
        } else {
            textfilldatasheet.setVisibility(View.GONE);
        }
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

        sql = db.getWritableDatabase();
        getData();

        if (cf.getCallhistorycount() > 0) {
            UpdatList();
        } else {
            if (isnet()) {
                new StartSession(CallListActionActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCallHistoryData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });
            }
        }


        layAmt.setVisibility(View.GONE);
        if (calltype.equalsIgnoreCase("1")) {

            if (Float.parseFloat(ExpectedValue) > 0) {
                layAmt.setVisibility(View.VISIBLE);
                String k = ExpectedCloserDate.substring(0, ExpectedCloserDate.length() - 15);
                String[] v1 = {k};
                textAmt.setText("EV-" + ExpectedValue + " by " + v1[0]);
            } else {
                layAmt.setVisibility(View.GONE);
            }

        } else if (calltype.equalsIgnoreCase("2")) {
            if (Amount == null) {

            } else {
                if (Float.parseFloat(Amount) > 0) {
                    layAmt.setVisibility(View.VISIBLE);
                    //  textAmt.setText("CV-" + Amount);
                    textAmt.setText(Amount);

                } else {
                    layAmt.setVisibility(View.GONE);
                }

            }

        } else {
            layAmt.setVisibility(View.GONE);
        }

        setListener();
    }

    public void init() {
        txtcall = (TextView) findViewById(R.id.txtcall);
        textOpportunityRating = (LinearLayout) findViewById(R.id.textOpportunityRating);
        textOpportunityUpdate = (LinearLayout) findViewById(R.id.textOpportunityUpdate);
        toolbar_action = (Toolbar) findViewById(R.id.toolbar_action);
        toolbar_action.setTitle("CRM");
        toolbar_action.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar_action);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layAmt = (LinearLayout) findViewById(R.id.layAmt);
        textAmt = (TextView) findViewById(R.id.textAmt);
        textfilldatasheet = (LinearLayout) findViewById(R.id.textfilldatasheet);
        callhistory_listview = (ListView) findViewById(R.id.callhistory_listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);


    }

    private void getData() {

        String query = "SELECT SouceId,CallStatus,ExpectedValue,ExpectedCloserDate FROM "
                + tablename + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            SourceId = cur.getString(cur.getColumnIndex("SouceId"));
            callstatus = cur.getString(cur.getColumnIndex("CallStatus"));
            ExpectedValue = cur.getString(cur.getColumnIndex("ExpectedValue"));
            ExpectedCloserDate = cur.getString(cur.getColumnIndex("ExpectedCloserDate"));
            // Amount = cur.getString(cur.getColumnIndex("Amount"));
        } else {

        }


    }

    public void setListener() {
        textOpportunityRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        textOpportunityUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallListActionActivity.this,
                        OpportunityUpdateActivity.class);
                intent.putExtra("callid", callid);
                intent.putExtra("calltype", calltype);
                intent.putExtra("firmname", firmname);
                intent.putExtra("table", table);
                intent.putExtra("ProspectId", ProspectId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        textfilldatasheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CallListActionActivity.this, DatasheetMainActivity.class);
                intent.putExtra("callid", callid);
                intent.putExtra("calltype", calltype);
                intent.putExtra("firmname", firmname);
                intent.putExtra("SourceId", SourceId);

                startActivity(intent);
                CallListActionActivity.this.finish();
            }
        });


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.calllist, menu);
        return true;
    }*/

   /* @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.action_menu);

        return super.onPrepareOptionsMenu(menu);
    }*/

    /* @Override
     public boolean onOptionsItemSelected(MenuItem item) {

         int id = item.getItemId();

         //noinspection SimplifiableIfStatement
         if (id == R.id.action_menu) {
             dialog = new Dialog(CallListActionActivity.this);
             Window window = dialog.getWindow();
             dialog.requestWindowFeature(window.FEATURE_NO_TITLE);
             dialog.setContentView(R.layout.lay_opportunity_dialog);

             WindowManager.LayoutParams wlp = window.getAttributes();
             wlp.gravity = Gravity.TOP | Gravity.RIGHT;

             wlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
             wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
             window.setAttributes(wlp);
             txtHome = (TextView) dialog.findViewById(R.id.txtHome);
             txtTravelPlan = (TextView) dialog.findViewById(R.id.txtTravelPlan);
             txt_rating = (TextView) dialog.findViewById(R.id.txt_rating);
             txt_update = (TextView) dialog.findViewById(R.id.txt_update);
             // txtExecutivePerformance = (TextView) dialog.findViewById(R.id.txtExecutivePerformance);
             txtProspectMaster = (TextView) dialog.findViewById(R.id.txtProspectMaster);
            *//* txtEntityMaster = (TextView) dialog.findViewById(R.id.txtEntityMaster);
            txtCityMaster = (TextView) dialog.findViewById(R.id.txtCityMaster);
            txtBusinessSegMaster = (TextView) dialog.findViewById(R.id.txtBusinessSegMaster);
            txtTeritoryMaster = (TextView) dialog.findViewById(R.id.txtTeritoryMaster);
            txtReportlist = (TextView) dialog.findViewById(R.id.txtReportlist);*//*
            txtEnquiryform = (TextView) dialog.findViewById(R.id.txtEnquiryform);
            txtEnquiryFormSetting = (TextView) dialog.findViewById(R.id.txtEnquiryFormSetting);

            LinearLayout len_update= (LinearLayout) dialog.findViewById(R.id.len_rating);


            if (calltype.equalsIgnoreCase("1")) {// || calltype.equalsIgnoreCase("2")
                len_update.setVisibility(View.VISIBLE);
            } else {
                len_update.setVisibility(View.GONE);
            }

            txtHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, CallListActivity.class);
                    startActivity(intent);

                //    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
            txtTravelPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, TravelPlanShowFormActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtProspectMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, ProspectEntryActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });

            txtEnquiryform.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, EnquiryFormActivity.class);
                    startActivity(intent);
                  //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
            txtEnquiryFormSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, PromotionalFormSettingActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });
           *//* txt_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, CallRatingActivity.class);
                    intent.putExtra("callid", callid);
                    intent.putExtra("callstatus", callstatus);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            });*//*
     *//* txt_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActionActivity.this, OpportunityUpdateActivity.class);
                    intent.putExtra("callid", callid);
                    intent.putExtra("calltype", calltype);
                    intent.putExtra("firmname", firmname);
                    intent.putExtra("table", table);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });*//*
            dialog.show();
            return true;
        }
        if (id==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CallListActionActivity.this.finish();
    }


    class DownloadCallHistoryData extends AsyncTask<String, Void, String> {
        Object res;
        String response = "error";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM hh:mm");
        Date DOJDate = null, DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            callHistoryArrayList.clear();
            try {
                String url = CompanyURL + WebUrlClass.api_GetCallHistory + "?CallId=" +
                        URLEncoder.encode(callid, "UTF-8");

                System.out.println("URLCALLHISTORY :" + url);
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    // Cursor deleteCur = sql.rawQuery("DELETE FROM " + db.TABLE_BIRTHDAY, null);
                    sql.delete(db.TABLE_CALLHISTORY, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALLHISTORY, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jcallhistory = jResults.getJSONObject(i);

                        CallHistory callHistory = new CallHistory();

                        callHistory.setCallHistoryId(jcallhistory.getString("CallHistoryId"));
                        callHistory.setCallId(jcallhistory.getString("CallId"));
                        callHistory.setCurrentCallOwner(jcallhistory.getString("CurrentCallOwner"));
                        callHistory.setActionType(jcallhistory.getString("ActionType"));

                        String contact = jcallhistory.getString("Contact");

                        contact = contact.replace("rr0", "");
                        String Contact = contact;
                        System.out.println("Whoom :" + Contact);
                        callHistory.setContact(Contact);
                        callHistory.setPurpose(jcallhistory.getString("Purpose"));
                        callHistory.setNextAction(jcallhistory.getString("NextAction"));
                        // callHistory.setNextActionDateTime(jcallhistory.getString("NextActionDateTime"));
                        String jsonDOJ = jcallhistory.getString("NextActionDateTime");

                        System.out.println("Datetimecall :" + jsonDOJ);
                        jsonDOJ = jsonDOJ.substring(jsonDOJ.indexOf("(") + 1, jsonDOJ.lastIndexOf(")"));
                        long DOJ_date = Long.parseLong(jsonDOJ);
                        DOJDate = new Date(DOJ_date);
                        jsonDOJ = sdf1.format(DOJDate);
                        System.out.println("Datetimecall_1 :" + jsonDOJ);
                        callHistory.setNextActionDateTime(jsonDOJ);
                        String jsonDOB = jcallhistory.getString("ModifiedDt");
                        jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                        long DOB_date = Long.parseLong(jsonDOB);
                        DOBDate = new Date(DOB_date);
                        jsonDOB = sdf.format(DOBDate);
                        callHistory.setModifiedDt(jsonDOB);
                        callHistory.setOutcome(jcallhistory.getString("Outcome"));
                        callHistory.setUserName(jcallhistory.getString("UserName"));
                        callHistory.setOutcomeCode(jcallhistory.getString("OutcomeCode"));
                        callHistory.setLatestRemark(jcallhistory.getString("LatestRemark"));
                        cf.AddCallHistory(callHistory);
                        callHistoryArrayList.add(callHistory);

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

            if (response.equals("[]")) {
                Toast.makeText(CallListActionActivity.this, "Call history not found", Toast.LENGTH_SHORT).show();
                dismissProgressDialog();

            } else {
                if (response != null) {
                   /*callHistoryAdapter = new CallHistoryAdapter(CallListActionActivity.this, callHistoryArrayList);
                   callhistory_listview.setAdapter(callHistoryAdapter);
                   callhistory_listview.setVisibility(View.VISIBLE);*/

                    UpdatList();
                    dismissProgressDialog();
                }

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


    private void showProgressDialog() {

        progressbar.setVisibility(View.VISIBLE);
        // progressHUD = ProgressHUD.show(context, "", false, false, null);
    }

    private void dismissProgressDialog() {

        progressbar.setVisibility(View.GONE);

    }


    private void UpdatList() {
        callHistoryArrayList.clear();
        String query = "SELECT * FROM " + db.TABLE_CALLHISTORY + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CallHistory callHistory = new CallHistory();
                callHistory.setCallHistoryId(cur.getString(cur.getColumnIndex("CallHistoryId")));
                callHistory.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                callHistory.setCurrentCallOwner(cur.getString(cur.getColumnIndex("CurrentCallOwner")));
                callHistory.setActionType(cur.getString(cur.getColumnIndex("ActionType")));
                callHistory.setContact(cur.getString(cur.getColumnIndex("Contact")));
                callHistory.setPurpose(cur.getString(cur.getColumnIndex("Purpose")));
                callHistory.setNextAction(cur.getString(cur.getColumnIndex("NextAction")));
                callHistory.setNextActionDateTime(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                callHistory.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                callHistory.setOutcome(cur.getString(cur.getColumnIndex("Outcome")));
                callHistory.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                callHistory.setOutcomeCode(cur.getString(cur.getColumnIndex("OutcomeCode")));
                callHistory.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                callHistoryArrayList.add(callHistory);
            } while (cur.moveToNext());

            callHistoryAdapter = new CallHistoryAdapter(CallListActionActivity.this, callHistoryArrayList);

            if (callHistoryAdapter.getCount() != 0) {
                callhistory_listview.setAdapter(callHistoryAdapter);
                callhistory_listview.setVisibility(View.VISIBLE);


            } else {
                Toast.makeText(CallListActionActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        if (menuItem.getItemId() == R.id.refresh) {


            if (isnet()) {
                new StartSession(this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCallHistoryData().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG).show();
            }
            return true;
        } else {
            return (super.onOptionsItemSelected(menuItem));
        }
    }
}

