package com.vritti.vwb.vworkbench;

/**
 * Created by Admin-1 on 12/25/2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.classes.CommonFunction;

public class ActivityIndentApprovalNew extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "",
            CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ProgressBar mprogress;
    SharedPreferences userpreferences;
    String ActivityId, SourceId;
    SQLiteDatabase sql;
    WebView web_pdf;
    EditText edApproveRemar;
    String Remark = "";
    Button btnApprove, btnDisApprove;
    Button btnChangeApprover, btnCancel;
    Spinner sp_pdf;
    String ActivityName, AssignedById;
    JSONObject leaveApprovalObj;
    String FinalLeaveobj;
    String DescriptionApprovalType,
            URL,
            DocApprMthdId,
            DocValue,
            DocApprHdrId,
            DocApprDtlId,
            DocType,
            DocSourceId,
            FilePath, clsMthdFORHtmlBody, PrimaryKeyColumn;

    String UserMstId,
            AppvLvl,
            StatusID,
            ApprDt,
            IsDeleted,
            UserLevel,
            CreationLevel,
            AddedBy,
            AddedDt,
            ModifiedBy,
            ModifiedDt;
    int nextAppr = 0;
    String UserMasterIDnextAppr = "";


    String txtLevel,
            txtUserName,
            txtRemark,
            txtApproverType,
            txtDate,
            txtDocApprDtlId,
            txtStatusId, leave_date, Assigned_By;

    Boolean LeaveApprovalFlag = false, ClaimApprovalFlag = false;
    WebView webView;
    TextView mtext;
    LinearLayout mLincontainer, len_location;
    TextView mLevel, txt_gpsdetails, txt_gps_date,txt_nofound;
    SearchableSpinner sp_appr;
    Button btn_ok, btn_cancel;
    ScrollView scroll_location;
    private String Publish_date;
    private String onDutydate="",onDutyEnddate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_activity_approval);

        InitView();
        SetListners();
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
        if (getIntent().hasExtra("ActivityId")) {
            Intent intent = getIntent();
            ActivityId = intent.getStringExtra("ActivityId");//ActivityNAme
            SourceId = intent.getStringExtra("SourceId");
            ActivityName = intent.getStringExtra("ActivityNAme");
            AssignedById = intent.getStringExtra("AssignedById");
            Assigned_By = intent.getStringExtra("Assigned_By");
            mtext.setText(ActivityName);
        }


        if (isnet()) {
            showProgress();
            new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetMainDtl().execute();

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(getApplicationContext(), msg);
                    dismissProgress();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void SetListners() {
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remark = edApproveRemar.getText().toString();
                showProgress();
                if (isnet()) {
                    new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadnextLevelApprvr().execute();
                            //new DownloadApprovaDoc().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }
        });
        btnDisApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Remark = edApproveRemar.getText().toString();
                if (!Remark.equalsIgnoreCase("")) {
                   /* showProgress();
                    new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadDisApprovaDoc().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getApplicationContext(), msg);
                            dismissProgress();
                        }
                    });*/
                    try {
                        String remark = "Disapprove an activity  " + ActivityName;
                        String url = CompanyURL + WebUrlClass.api_disapproveDoc + "?ActivityId="
                                + URLEncoder.encode(ActivityId, "UTF-8") + "&IssuedTo="
                                + URLEncoder.encode(UserMasterId, "UTF-8") + "&SourceId="
                                + URLEncoder.encode(SourceId, "UTF-8") + "&Remark="
                                + URLEncoder.encode(Remark, "UTF-8");
                        String op = "";
                        CreateOfflineIntend(url, null, WebUrlClass.GETFlAG, remark, op);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    ut.displayToast(getApplicationContext(), "Enter Remark");

                }

            }
        });
        btnChangeApprover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DescriptionApprovalType.equalsIgnoreCase("Indent Approval")) {

                } else if (DescriptionApprovalType.equalsIgnoreCase("LeaveApproval")) {

                } else if (DescriptionApprovalType.equalsIgnoreCase("ClaimApproval")) {

                    ut.displayToast(getApplicationContext(), "Change Approver is Not Allowed");
                    /*Intent intent = new Intent(ActivityIndentApprovalNew.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/
                    onBackPressed();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent intent = new Intent(ActivityIndentApprovalNew.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                onBackPressed();
            }
        });
    }

    private void LeaveApprovalJSON() {
        leaveApprovalObj = new JSONObject();
        try {
            leaveApprovalObj.put("MLId", DocSourceId);
            leaveApprovalObj.put("ApprRemark", Remark);
            FinalLeaveobj = leaveApprovalObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (LeaveApprovalFlag) {
            String FinalObj = FinalLeaveobj.toString();
            String remark = "Approve an activity  " + ActivityName;
            String url = CompanyURL + WebUrlClass.api_leave_approval;
            String op = "Success";
            CreateOfflineIntend(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

        } else {
            String FinalObj = FinalLeaveobj.toString();
            String remark = "Disapprove an activity  " + ActivityName;
            String url = CompanyURL + WebUrlClass.api_leave_reject;
            String op = "Success";
            CreateOfflineIntend(url, FinalObj, WebUrlClass.POSTFLAG, remark, op);

        }
       /* if (LeaveApprovalFlag) {
            new DownloadLeaveApproval().execute();
        } else {
            new DownloadLeaveDisapproval().execute();
        }*/

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        mLincontainer = (LinearLayout) findViewById(R.id.lindis);
        len_location = (LinearLayout) findViewById(R.id.len_location);
        scroll_location = (ScrollView) findViewById(R.id.scroll_location);
        mtext = (TextView) findViewById(R.id.asd);
        txt_gpsdetails = (TextView) findViewById(R.id.txt_gpsdetails);
        txt_gps_date = (TextView) findViewById(R.id.txt_gps_date);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        web_pdf = (WebView) findViewById(R.id.web_pdf);
        sp_pdf = (Spinner) findViewById(R.id.sp_pdf);
        webView = (WebView) findViewById(R.id.web_pdf);
        edApproveRemar = (EditText) findViewById(R.id.edApproveRemark);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        btnDisApprove = (Button) findViewById(R.id.btnDisApprove);
        btnChangeApprover = (Button) findViewById(R.id.btnChangeApprover);
        btnCancel = (Button) findViewById(R.id.btncancel);
        txt_nofound=(TextView) findViewById(R.id.txt_nofound);
    }


    class DownloadGetMainDtl extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_approval_GetMainDtl + "?SourceId=" + URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.replaceAll("\\\\", "");
                    res = res.substring(1, res.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(res);
                    String msg = "";
                    JSONObject jorder = jResults.getJSONObject(0);

                    DocSourceId = jorder.getString("SourceId");
                    DocType = jorder.getString("SourceType");
                    URL = jorder.getString("URL");
                    FilePath = jorder.getString("FilePath");
                    DescriptionApprovalType = jorder.getString("Description");
                    DocApprMthdId = jorder.getString("DocApprMthdId");
                    DocValue = jorder.getString("DocValue");
                    DocApprHdrId = jorder.getString("DocApprHdrId");
                    DocApprDtlId = jorder.getString("DocApprDtlId");
                    clsMthdFORHtmlBody = jorder.getString("clsMthdFORHtmlBody");
                    PrimaryKeyColumn = jorder.getString("PrimaryKeyColumn");

                /*"clsMthdFORHtmlBody":"ExtraWorkApprovalAPIController|getCompOffApprovalHTML",
                        "PrimaryKeyColumn":" PKEmployeeleaveId"*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            new DownloadGetMainDtl2().execute();
            new DownloadGetDivDl().execute();
            if (!URL.equalsIgnoreCase("null") && !URL.equalsIgnoreCase("")) {
                if (!clsMthdFORHtmlBody.equalsIgnoreCase("null")) {
                    if (clsMthdFORHtmlBody.contains("Areas")) {
                        String[] data1 = clsMthdFORHtmlBody.split("\\.");
                        String htmlbody = data1[4];
                        String[] data = htmlbody.split(Pattern.quote("|"));
                        String ApiName = data[0];
                        ApiName = ApiName.substring(0, ApiName.length() - 10);
                        String MethodName = data[1];
                        PrimaryKeyColumn = PrimaryKeyColumn.trim();
                        String Url = "/api/" + ApiName + "/" + MethodName + "?" + PrimaryKeyColumn + "=";
                        Url = Url.trim();
                        new DowmloadAppoveHTML().execute(Url);
                    } else {
                        if (clsMthdFORHtmlBody.equals("")||clsMthdFORHtmlBody.equalsIgnoreCase(" ")){
                            txt_nofound.setVisibility(View.VISIBLE);
                            web_pdf.setVisibility(View.GONE);
                        }else {
                            try {
                                String data[] = clsMthdFORHtmlBody.split(Pattern.quote("|"));

                                String ApiName = data[0];
                                ApiName = ApiName.substring(0, ApiName.length() - 10);
                                String MethodName = data[1];
                                PrimaryKeyColumn = PrimaryKeyColumn.trim();
                                String Url = "/api/" + ApiName + "/" + MethodName + "?" + PrimaryKeyColumn + "=";
                                //Url = Url.trim();

                                new DowmloadAppoveHTML().execute(Url);

                            }catch (Exception e){
                                e.printStackTrace();

                            }
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No HTML present", Toast.LENGTH_LONG).show();
                }


            } else {
                if (FilePath != null) {
                    new DownloadGetDtl().execute();
                }
            }


        }

    }

    class DownloadGetMainDtl2 extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = CompanyURL + WebUrlClass.api_approval_GetMainDtl2 + "?SourceId=" + URLEncoder.encode(SourceId, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.replaceAll("\\\\", "");
                    res = res.substring(1, res.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(res);
                    String msg = "";
                    JSONObject jorder = jResults.getJSONObject(0);
                    DocApprDtlId = jorder.getString("DocApprDtlId");
                    DocApprHdrId = jorder.getString("DocApprHdrId");
                    UserMstId = jorder.getString("UserMstId");
                    AppvLvl = jorder.getString("AppvLvl");
                    StatusID = jorder.getString("StatusID");
                    ApprDt = jorder.getString("ApprDt");
                    int data = Integer.parseInt(AppvLvl);
                    nextAppr = data + 1;
                    Remark = jorder.getString("Remark");
                    IsDeleted = jorder.getString("IsDeleted");
                    UserLevel = jorder.getString("UserLevel");
                    CreationLevel = jorder.getString("CreationLevel");
                    AddedBy = jorder.getString("AddedBy");
                    AddedDt = jorder.getString("AddedDt");
                    ModifiedBy = jorder.getString("ModifiedBy");
                    ModifiedDt = jorder.getString("ModifiedDt");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);


        }

    }

    class DownloadGetDivDl extends AsyncTask<String, Void, String> {
        String res;
        JSONObject jorder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
            //OLD API- 20-9-21
              //  String url = CompanyURL + WebUrlClass.api_approval_Getdiv + "?DocApprHdrId=" + URLEncoder.encode(DocApprHdrId, "UTF-8") + "&SourceId=" + URLEncoder.encode(DocSourceId, "UTF-8");

                // Changes given by shubham -20-9-21
                String url = CompanyURL + WebUrlClass.api_approval_Getdiv + "?DocApprHdrId=" + URLEncoder.encode(DocApprHdrId, "UTF-8") + "&SourceId=" + URLEncoder.encode(DocApprDtlId, "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.replaceAll("\\\\", "");
                    res = res.substring(1, res.length() - 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";

            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            if (integer.contains("Level")) {
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(integer);
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        txtLevel = jorder.getString("Level");
                        txtUserName = jorder.getString("UserName");
                        txtRemark = jorder.getString("Remark");
                        txtApproverType = jorder.getString("ApproverType");
                        txtDate = jorder.getString("Date");
                        txtDocApprDtlId = jorder.getString("DocApprDtlId");
                        txtStatusId = jorder.getString("StatusId");
                        onDutydate = jorder.getString("StartDate");
                        onDutyEnddate = jorder.getString("EndDate");
                        TextView mTxtlevel, mTxtname, mTxtreqtype, mTxtremark, mTxtreqdate;
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.vwb_itemlevel, mLincontainer, false);
                        LinearLayout mlinitem = (LinearLayout) view.findViewById(R.id.lin);
                        mTxtlevel = (TextView) view.findViewById(R.id.txt_level);
                        mTxtname = (TextView) view.findViewById(R.id.txt_name);
                        mTxtreqtype = (TextView) view.findViewById(R.id.reuestertype);
                        mTxtremark = (TextView) view.findViewById(R.id.Remark);
                        mTxtreqdate = (TextView) view.findViewById(R.id.reqdate);
                        mTxtlevel.setText(txtLevel);
                        mTxtname.setText(txtUserName);
                        mTxtremark.setText(txtRemark);
                        String date = getDateAdded(txtDate);
                        mTxtreqdate.setText(date);
                        leave_date = getLeaveDate(txtDate);
                        mTxtreqtype.setText(txtApproverType);
                        mLincontainer.addView(mlinitem);


                        if (ActivityName.contains("OnDuty")) {
                            if (txtLevel.equals("1")) {

                                onDutydate = formateDateFromstring("dd MMM yyyy", "yyyy-MM-dd", onDutydate);
                                onDutyEnddate = formateDateFromstring("dd MMM yyyy", "yyyy-MM-dd", onDutyEnddate);
                            }


                            if (isnet()) {
                                new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadOnDutyLocationJSON().execute();
                                        //new DownloadApprovaDoc().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        ut.displayToast(getApplicationContext(), msg);
                                        dismissProgress();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                            }


                        } else if (ActivityName.contains("Claim")) {


                            if (isnet()) {
                                new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadClaimLocationJSON().execute();
                                        //new DownloadApprovaDoc().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {
                                        ut.displayToast(getApplicationContext(), msg);
                                        dismissProgress();
                                    }
                                });
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

    }

    private String getDateAdded(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }


    private String getLeaveDate(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }

    class DownloadGetDtl extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            try {
                JSONArray jResults = new JSONArray(res);
                if (jResults.length() > 1) {
                    ls_pdf = new ArrayList<String>();
                    sp_pdf.setVisibility(View.VISIBLE);
                    for (int i = 0; i < jResults.length(); i++) {
                        ls_pdf.add(jResults.getJSONObject(i).getString("FileName"));
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ActivityIndentApprovalNew.this, android.R.layout.simple_spinner_item, ls_pdf);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp_pdf.setAdapter(dataAdapter);
                    }
                    sp_pdf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String FileName = sp_pdf.getSelectedItem().toString();
                            String extension = FileName.substring(FileName.indexOf(".") + 1);
                            if (extension.equalsIgnoreCase("docx")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("pdf")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("xlsx")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("png")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("jpg")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("htm")) {
                                showPDF(FileName);
                            } else if (extension.equalsIgnoreCase("mp3")) {
                                {
                                    playFile(FileName);
                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } else {
                    sp_pdf.setVisibility(View.GONE);
                    JSONObject jobj = jResults.getJSONObject(0);
                    showPDF(jobj.getString("FileName"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {


                url = CompanyURL + WebUrlClass.api_getDtl + "?SourceId=" + URLEncoder.encode(SourceId, "UTF-8");


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
            res = res.replaceAll("\\\\", "");
            res = res.substring(1, res.length() - 1);

            return "";
        }
    }

    class DownloadnextLevelApprvr extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String res;
            try {
                String url = CompanyURL + WebUrlClass.api_getnxtAppr + "?DocApprMthdId=" + URLEncoder.encode(DocApprMthdId, "UTF-8") +
                        "&DocValue=" + URLEncoder.encode(DocValue, "UTF-8") + "&nextApprlvl=" + URLEncoder.encode(nextAppr
                        + "", "UTF-8") + "&SourceId=" + URLEncoder.encode(SourceId
                        + "", "UTF-8") + "&Remark=" + URLEncoder.encode(Remark
                        + "", "UTF-8");
                res = ut.OpenConnection(url, getApplicationContext());
                if (res != null) {
                    res = res.replaceAll("\\\\", "");
                    res = res.substring(1, res.length() - 1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                res = "Error";
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (integer.contains("UserName")) {
                ContentValues values = new ContentValues();
                JSONArray jResults = null;
                try {
                    jResults = new JSONArray(integer);
                    String msg = "";
                    sql.delete(db.TABLE_NEXTAPPR, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_NEXTAPPR, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {
                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);
                        }
                        long a = sql.insert(db.TABLE_NEXTAPPR, null, values);
                        Log.e("", "" + a);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ChangeApproverDialog();
            } else if ((integer.equalsIgnoreCase("[]") || (integer.equalsIgnoreCase("")))) {


                // new DownloadApprovaDoc().execute();
                Remark = edApproveRemar.getText().toString();
                try {
                    String remark = "Approve an activity  " + ActivityName;
                    String url = CompanyURL + WebUrlClass.api_ApprivalDoc + "?ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8")
                            + "&IssuedTo=" + URLEncoder.encode(UserMasterId, "UTF-8") + "&SourceId="
                            + URLEncoder.encode(SourceId, "UTF-8") + "&Remark=" + URLEncoder.encode(Remark, "UTF-8");
                    String op = "";
                    CreateOfflineIntend(url, null, WebUrlClass.GETFlAG, remark, op);
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});

                    //   startActivity(new Intent(ActivityIndentApprovalNew.this,ActivityMain.class));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            } else {
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }

        }
    }

    class SendToNextAppr extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
            Remark = edApproveRemar.getText().toString();

        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_sendNextAppr + "?DocApprHdrId=" + URLEncoder.encode(DocApprHdrId, "UTF-8") +
                        "&ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8") +
                        "&IssuedTo=" + URLEncoder.encode(UserMasterId + "", "UTF-8") +
                        "&SourceId=" + URLEncoder.encode(DocSourceId + "", "UTF-8") +
                        "&UserMasterId=" + URLEncoder.encode(UserMasterIDnextAppr + "", "UTF-8") +
                        "&Remark=" + URLEncoder.encode(Remark + "", "UTF-8");

                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                res = "Error";
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
        }
    }

    private void ChangeApproverDialog() {
        final String[] UserName = new String[1];
        final Dialog dialog1 = new Dialog(ActivityIndentApprovalNew.this);
        dialog1.setContentView(R.layout.vwb_dialog_next_approver);
        mLevel = dialog1.findViewById(R.id.nextappr);
        sp_appr = (SearchableSpinner) dialog1.findViewById(R.id.sp_nxtAppr);
        btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);
        btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

        getAppr();
        dialog1.setCancelable(false);
        dialog1.show();
        mLevel.setText(nextAppr + "");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* new StartSession(ActivityIndentApprovalNew.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {

                        new SendToNextAppr().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getApplicationContext(), msg);
                        dismissProgress();
                    }
                });*/


                Remark = edApproveRemar.getText().toString();
                try {
                    String remarkname = ActivityName + " has send to " + UserName[0] + "  for approval";
                    String url = CompanyURL + WebUrlClass.api_sendNextAppr + "?DocApprHdrId=" + URLEncoder.encode(DocApprHdrId, "UTF-8") +
                            "&ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8") +
                            "&IssuedTo=" + URLEncoder.encode(UserMasterId + "", "UTF-8") +
                            "&SourceId=" + URLEncoder.encode(SourceId + "", "UTF-8") +
                            "&UserMasterId=" + URLEncoder.encode(UserMasterIDnextAppr + "", "UTF-8") +
                            "&Remark=" + URLEncoder.encode(Remark + "", "UTF-8");


                    String op = "";
                    CreateOfflineIntend(url, null, WebUrlClass.GETFlAG, remarkname, op);
                    sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                dialog1.dismiss();
            }
        });

        sp_appr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserName[0] = parent.getItemAtPosition(position).toString();
                String query = "SELECT * FROM " + db.TABLE_NEXTAPPR + " WHERE UserName='" + UserName[0] + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    UserMasterIDnextAppr = cur.getString(cur.getColumnIndex("UserMasterId"));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void getAppr() {
        ArrayList<String> Workspace_list = new ArrayList<>();
        String query = "SELECT * FROM " + db.TABLE_NEXTAPPR;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Workspace_list.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityIndentApprovalNew.this, android.R.layout.simple_spinner_item, Workspace_list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_appr.setAdapter(adapter);

        }
    }

    class DowmloadAppoveHTML extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgress();
            if (!res.equalsIgnoreCase("") || !res.equalsIgnoreCase(null)) {
                web_pdf.getSettings().setJavaScriptEnabled(true);
                web_pdf.loadDataWithBaseURL(null, res, "text/html", "utf-8", null);
            } else {
                Toast.makeText(getApplicationContext(), "HTML is not generated", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
               String url = CompanyURL + params[0] + URLEncoder.encode(DocSourceId, "UTF-8");

               // String url = "http://c207.ekatm.co.in/approval_UI/Approval.html?A,2544f2fa-e915-4834-8281-af10039cb90d";

                res = ut.OpenConnection(url, getApplicationContext());
                res = res.substring(1, res.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }



    class DownloadApprovaDoc extends AsyncTask<Integer, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (res.equalsIgnoreCase("")) {
                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});

                Toast.makeText(ActivityIndentApprovalNew.this, "Document approved", Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(ActivityIndentApprovalNew.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                onBackPressed();
            }
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_ApprivalDoc + "?ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8")
                        + "&IssuedTo=" + URLEncoder.encode(UserMasterId, "UTF-8") + "&SourceId="
                        + URLEncoder.encode(SourceId, "UTF-8") + "&Remark=" + URLEncoder.encode(Remark, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
            res = res.substring(1, res.length() - 1);
            return "";
        }
    }

    class DownloadDisApprovaDoc extends AsyncTask<Integer, Void, Integer> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            dismissProgress();
            if (res.equalsIgnoreCase("")) {
                sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
                sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
                Toast.makeText(ActivityIndentApprovalNew.this, "Intent disapproved", Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(ActivityIndentApprovalNew.this, com.vritti.vwb.vworkbench.ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                onBackPressed();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_disapproveDoc + "?ActivityId=" + URLEncoder.encode(ActivityId, "UTF-8") +
                        "&IssuedTo=" + URLEncoder.encode(UserMasterId, "UTF-8") + "&SourceId="
                        + URLEncoder.encode(SourceId, "UTF-8") + "&Remark=" + URLEncoder.encode(Remark, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
            res = res.substring(1, res.length() - 1);
            return null;
        }
    }

    private void showPDF(String url) {
        try {


            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showProgress();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dismissProgress();
                }
            });
           /* webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setInitialScale(0);
            webView.getSettings().setBuiltInZoomControls(true);*/
            webView.getSettings().setJavaScriptEnabled(true);

            if (url.contains(".htm")) {
                url = CompanyURL + "/attachments/" + url;
                webView.loadUrl(url);
            } else {
                url = CompanyURL + "/attachments/" + url;
                url = "https://docs.google.com/viewer?url=" + url + "&embedded=true";
              //  url = "https://docs.google.com/gview?url=" + url + "&embedded=true";
                webView.loadUrl(url);
            }


            /*url = CompanyURL + "/attachments/" + url;
            url = "https://docs.google.com/viewer?url=" + url;
            webView.loadUrl(url);*/

        } catch (ActivityNotFoundException e) {

        }
    }

    private void playFile(String url) {
        //webView.getSettings().setPluginsEnabled(true);

// Added in API level 8
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        url = CompanyURL + "/media/" + url;
        MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(url));
        mediaPlayer.start();
    }

    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mprogress.setVisibility(View.GONE);

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    private void CreateOfflineIntend(final String url, final String parameter,
                                     final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            sql.delete(db.TABLE_ACTIVITYMASTER, "ActivityId=?", new String[]{ActivityId});
            sql.delete(db.TABLE_ACTIVITYMASTER_PAGING, "ActivityId=?", new String[]{ActivityId});
            Cursor c = sql.rawQuery("select * from " + db.TABLE_ACTIVITYMASTER, null);
            int a1 = c.getCount();

            Toast.makeText(getApplicationContext(), "Record Saved Successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            startService(intent1);

           onBackPressed();
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    class DownloadOnDutyLocationJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetGPSDetailForOnDuty + "?UserMasterId=" + AssignedById + "&StartDate=" + URLEncoder.encode(onDutydate,"UTF-8") + "&EndDate=" + URLEncoder.encode(onDutyEnddate,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            try {
                res = ut.OpenConnection(url, ActivityIndentApprovalNew.this);
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                JSONArray jResults = null;
                try {
                    len_location.removeAllViews();
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        scroll_location.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        String locationname = jsonObject.getString("LocationName");
                        String Time_from = jsonObject.getString("TimeFrom");
                        String TimetTo = jsonObject.getString("TimeTo");

                        TextView txt_time_from, txt_time_to, txt_location_name;
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.vwb_claim_location_lay, len_location, false);
                        LinearLayout mlinitem = (LinearLayout) view.findViewById(R.id.len_onduty);
                        txt_time_from = (TextView) view.findViewById(R.id.txt_time_from);
                        txt_time_to = (TextView) view.findViewById(R.id.txt_time_to);

                        txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);
                        txt_gpsdetails.setText("GPS details of " + Assigned_By);
                        txt_time_from.setText(Time_from);
                        txt_time_to.setText(TimetTo);
                        txt_location_name.setText(locationname);
                        len_location.addView(mlinitem);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }


    }

    class DownloadClaimLocationJSON extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebUrlClass.api_GetRefreshChatRoom + "?ApplicationCode="+WebUrlClass.AppNameFCM;

            String url = CompanyURL + WebUrlClass.api_GetGPSLocationForUser + "?ClaimHeaderId=" + DocSourceId;

            try {
                res = ut.OpenConnection(url, ActivityIndentApprovalNew.this);
                response = res.toString();
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            // progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")) {


            } else {
                JSONArray jResults = null;
                try {
                    len_location.removeAllViews();
                    jResults = new JSONArray(response);
                    for (int i = 0; i < jResults.length(); i++) {
                        scroll_location.setVisibility(View.VISIBLE);
                        txt_gps_date.setVisibility(View.VISIBLE);

                        JSONObject jsonObject = jResults.getJSONObject(i);
                        String locationname = jsonObject.getString("LocationName");
                        String Time_from = jsonObject.getString("TimeFrom");
                        String TimetTo = jsonObject.getString("TimeTo");
                        String GPSDate = jsonObject.getString("GPSDate");

                        TextView txt_time_from, txt_time_to, txt_location_name, txt_gps_date;
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View view = layoutInflater.inflate(R.layout.vwb_claim_location_lay, len_location, false);
                        LinearLayout mlinitem = (LinearLayout) view.findViewById(R.id.len_onduty);
                        txt_time_from = (TextView) view.findViewById(R.id.txt_time_from);
                        txt_time_to = (TextView) view.findViewById(R.id.txt_time_to);
                        txt_gps_date = (TextView) view.findViewById(R.id.txt_gps_date);
                        txt_gps_date.setVisibility(View.VISIBLE);

                        txt_location_name = (TextView) view.findViewById(R.id.txt_location_name);


                        txt_gpsdetails.setText("GPS details of " + Assigned_By);
                        txt_time_from.setText(Time_from);
                        txt_time_to.setText(TimetTo);
                        txt_location_name.setText(locationname);

                        Date initDate = null;
                        try {
                            initDate = new SimpleDateFormat("yyyy-MM-dd").parse(GPSDate);
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM");
                            Publish_date = formatter.format(initDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        txt_gps_date.setText(Publish_date);
                        len_location.addView(mlinitem);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }


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
}
