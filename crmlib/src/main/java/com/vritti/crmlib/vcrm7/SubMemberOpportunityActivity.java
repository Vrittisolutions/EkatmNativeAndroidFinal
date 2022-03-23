package com.vritti.crmlib.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.PartialCallList;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.CommonObjectProperties;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

public class SubMemberOpportunityActivity extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    LinearLayout lsCall_list, realcolors, laycall_type;
    String Obj, Usermasterid, Username, type;
    TextView txtopportunitytype;

    String UserType;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    ArrayList<PartialCallList> partialCallLists;
    TextView
            txtfirmname, tvcall,
            txtcityname, txtactiondatetime;
    CommonObjectProperties commonObj;
    SimpleDateFormat dfDate;
    String FinalObj;
    ProgressBar progressbar;
    ImageView img_search_firm;
    EditText edt_search_firm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_team_member_opportunity);

        context = SubMemberOpportunityActivity.this;

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

        init();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);

        UserType = userpreferences.getString("UserType", null);
        Intent intent = getIntent();
        Obj = intent.getStringExtra("Obj");
        Usermasterid = intent.getStringExtra("UserMasterId");
        Username = intent.getStringExtra("Username");
        type = intent.getStringExtra("Type");
        if (type.equalsIgnoreCase("A")) {
            txtopportunitytype.setText("Assigned Opportunity of " + Username);
        } else if (type.equalsIgnoreCase("O")) {
            txtopportunitytype.setText("Overdue Opportunity of " + Username);

        } else if (type.equalsIgnoreCase("T")) {
            txtopportunitytype.setText("Today's Opportunity of " + Username);

        } else if (type.equalsIgnoreCase("C")) {
            txtopportunitytype.setText("Collection Opportunity of " + Username);
        } else if (type.equalsIgnoreCase("H")) {
            txtopportunitytype.setText("Hot Opportunity of " + Username);
        }

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanDataURLJSON().execute(Obj);
                }

                @Override
                public void callfailMethod(String msg) {

                }

            });
        }

    }


    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("CRM");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lsCall_list = (LinearLayout) findViewById(R.id.lsCall_list);
        txtopportunitytype = (TextView) findViewById(R.id.txtopportunitytype);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        sql = db.getWritableDatabase();
        partialCallLists = new ArrayList<PartialCallList>();
        edt_search_firm = (EditText) findViewById(R.id.edt_search_firm);
        img_search_firm = (ImageView) findViewById(R.id.img_search_firm);


        img_search_firm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Firmname = edt_search_firm.getText().toString();
                FilterOppUpdatList(Firmname);


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

    class DownloadCommanDataURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[0],SubMemberOpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    //  response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);

                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL_PARTIAL_TEAM, null,
                            null);
                    sql.delete(db.TABLE_CRM_CALL_TEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("isPartial")) {

                                values.put(columnName, "P");
                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else if (columnName.equalsIgnoreCase("ContactName")) {
                                if (jorder.getString("ContactName").equalsIgnoreCase("null")) {
                                    values.put(columnName, "");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_PARTIAL_TEAM, null, values);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();
            if (response.contains("CallId")) {
                UpdatList();

            } else if (response.contains("Error")) {

            }
        }

    }

    private void UpdatList() {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CityName,TerritoryName,CallId,ProductName," +
                "NextActionDateTime,AssignedBy,Mobile,isPartial,CallType,CallStatus,ContactName" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + "";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }

    private void addView_CallList(int i) {

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_calllist_partial,
                null);


        realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
        if (i % 2 == 1) {
            realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        txtfirmname = (TextView) convertView
                .findViewById(R.id.firmname);
        txtcityname = (TextView) convertView.findViewById(R.id.city);
       /* txtcityterritoryname = (TextView) convertView
                .findViewById(R.id.cityterritory);*/

        txtactiondatetime = (TextView) convertView
                .findViewById(R.id.actiondatetime);
        tvcall = (TextView) convertView
                .findViewById(R.id.tvcall);
        laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);

//laycall_type

        if (partialCallLists.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
            } else if (partialCallLists.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
            }


        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("2")) {
            laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallLists.get(i).getCallType().equalsIgnoreCase("3")) {
            laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }

        txtfirmname.setText(partialCallLists.get(i).getFirmname());
        txtcityname.setText(partialCallLists.get(i)
                .getCityname() + "-" + partialCallLists.get(i)
                .getProductname());

        txtactiondatetime.setText(partialCallLists.get(i)
                .getActiondatetime());
        String status = partialCallLists.get(i).getContactName();

        if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("")) {
            tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
        } else {
            if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {
                tvcall.setText("(" + partialCallLists.get(i).getMobileno() + ")");
            } else {
                tvcall.setText("(" + partialCallLists.get(i).getContactName() + " - "
                        + partialCallLists.get(i).getMobileno() + ")");
            }

        }


        tvcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partialCallLists.get(position).getMobileno().equalsIgnoreCase("No Contact Available")) {

                } else {
                    try {
                        String mobile = partialCallLists.get(position).getMobileno();
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + mobile));
                        startActivity(callIntent);

                    } catch (SecurityException e) {

                    }
                }
            }
        });

        laycall_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cf.getCallListTeamcount(partialCallLists.get(position).getCallId()) > 0) {

                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_TEAM, cv, "CallId=?",
                            new String[]{partialCallLists.get(position).getCallId()});

                    Intent intent = new Intent(context, TeamsCallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("Usermasterid", Usermasterid);
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());

                    startActivity(intent);
                    //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else {

                    getCallListData_Full(partialCallLists.get(position).getCallId(), position);
                   /* Intent intent = new Intent(context, CallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(position).getCallId());
                    intent.putExtra("firmname", partialCallLists.get(position).getFirmname());
                    intent.putExtra("calltype", partialCallLists.get(position).getCallType());
                    intent.putExtra("callstatus", partialCallLists.get(position).getCallStatus());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
*/
                }

            }
        });


        lsCall_list.addView(convertView);
    }


    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
            Date datestop = sdf.parse(datedb);


            return_value = datedb;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("crm_dialog_action", "crm_dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(datedb);
            return_value = "Today" + tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(datedb);
            return_value = "Yesterday" + tm[0];
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(datedb);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {
            String k = datedb.substring(0, datedb.length() - 15);
            String tm[] = splitfrom(return_value);
            return k + tm[0];
        }

    }

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    private String getObj(String a, String uid, String cid) {
        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", uid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("CallId");

            jsonObj.put("IsSet", true);
            jsonObj.put("value1", cid);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (a.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("O")) {

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("T")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("C")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 2);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("H")) {

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Hot");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        return FinalObj;
    }

    public void getCallListData_Full(String cid, final int position) {
        final String obj = getObj(type, Usermasterid, cid);

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadCommanData_fullURLJSON().execute(String.valueOf(position), obj);
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

    }

    class DownloadCommanData_fullURLJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");
        Date DOBDate = null;
        int pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_Get_Call;

            try {
                res = ut.OpenPostConnection(url, params[1],SubMemberOpportunityActivity.this);
                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    pos = Integer.parseInt(params[0]);
                    ContentValues values = new ContentValues();

                    JSONArray jResults = new JSONArray(response);
                    String msg = "";
                    sql.delete(db.TABLE_CRM_CALL_TEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_TEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            String jsonDOB = jorder.getString("NextActionDateTime");
                            String jsonDt = jorder.getString("ExpectedCloserDate");
                            if (columnName.equalsIgnoreCase("NextActionDateTime")) {
                                jsonDOB = jsonDOB.substring(jsonDOB.indexOf("(") + 1, jsonDOB.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDOB);
                                DOBDate = new Date(DOB_date);
                                jsonDOB = sdf.format(DOBDate);
                                values.put(columnName, jsonDOB);

                            } else if (columnName.equalsIgnoreCase("Mobile")) {
                                if (jorder.getString("Mobile").equalsIgnoreCase("null")) {
                                    values.put(columnName, "No Contact Available");
                                } else {
                                    columnValue = jorder.getString(columnName);
                                    values.put(columnName, columnValue);
                                }

                            }
                            if (columnName.equalsIgnoreCase("ExpectedCloserDate")) {
                                jsonDt = jsonDt.substring(jsonDt.indexOf("(") + 1, jsonDt.lastIndexOf(")"));
                                long DOB_date = Long.parseLong(jsonDt);
                                DOBDate = new Date(DOB_date);
                                jsonDt = sdf.format(DOBDate);
                                values.put(columnName, jsonDt);

                            } else {
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);
                            }


                        }

                        long a = sql.insert(db.TABLE_CRM_CALL_TEAM, null, values);
                        Log.d("crm_dialog_action", "count " + a);
                    }
                    Cursor c1 = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL_TEAM, null);
                    int count1 = c1.getCount();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgressDialog();


            if (integer.equalsIgnoreCase("[]")) {


            } else {
                if (cf.getCallListTeamcount(partialCallLists.get(pos).getCallId()) > 0) {
                    ContentValues cv = new ContentValues();

                    cv.put("isPartial", "C");

                    sql.update(db.TABLE_CRM_CALL_PARTIAL_TEAM, cv, "CallId=?",
                            new String[]{partialCallLists.get(pos).getCallId()});


                    Intent intent = new Intent(context, TeamsCallListActionActivity.class);
                    intent.putExtra("callid", partialCallLists.get(pos).getCallId());
                    intent.putExtra("calltype", partialCallLists.get(pos).getCallType());
                    intent.putExtra("firmname", partialCallLists.get(pos).getFirmname());
                    intent.putExtra("Usermasterid", Usermasterid);


                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                }
            }

        }

    }

    private void showProgressDialog() {


        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.refresh) {
            if (isnet()) {
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadCommanDataURLJSON().execute(Obj);
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }
                });

            }
        }
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SubMemberOpportunityActivity.this.finish();
    }

    private void FilterOppUpdatList(String Firmname) {
        partialCallLists.clear();
        String query = "SELECT  FirmName,CityName,CallId," +
                "NextActionDateTime,Mobile,CallType,CallStatus,ContactName" +
                " FROM " + db.TABLE_CRM_CALL_PARTIAL_TEAM + " WHERE  FirmName like '%" + Firmname + "%'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                String dt = calculatediff(cur.getString(cur.getColumnIndex("NextActionDateTime")));
                PartialCallList partialCallList = new PartialCallList();
                partialCallList.setActiondatetime(dt);
                //partialCallList.setAssignedby(cur.getString(cur.getColumnIndex("AssignedBy")));
                partialCallList.setCallId(cur.getString(cur.getColumnIndex("CallId")));
                partialCallList.setCityname(cur.getString(cur.getColumnIndex("CityName")));
                // partialCallList.setCityterritoryname(cur.getString(cur.getColumnIndex("TerritoryName")));
                partialCallList.setFirmname(cur.getString(cur.getColumnIndex("FirmName")));
                partialCallList.setMobileno(cur.getString(cur.getColumnIndex("Mobile")));
                // partialCallList.setProductname(cur.getString(cur.getColumnIndex("ProductName")));
                // partialCallList.setIsPartial(cur.getString(cur.getColumnIndex("isPartial")));
                partialCallList.setCallStatus(cur.getString(cur.getColumnIndex("CallStatus")));
                partialCallList.setCallType(cur.getString(cur.getColumnIndex("CallType")));
                partialCallList.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                partialCallLists.add(partialCallList);

            } while (cur.moveToNext());
           /* callListPartialAdapter = new CallListPartialAdapter(CallListActivity.this, partialCallLists);
            lsCall_list.setAdapter(callListPartialAdapter);*/
            lsCall_list.removeAllViews();
            if (partialCallLists.size() > 0) {
                for (int i = 0; i < partialCallLists.size(); i++) {
                    addView_CallList(i);
                }
            }
        }

    }
}