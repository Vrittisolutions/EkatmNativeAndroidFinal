package com.vritti.vwb.vworkbench;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.gson.Gson;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.other.FileUtilities;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.ClaimDetailsRecycleAdapter;
import com.vritti.vwb.Beans.AuthUserBeans;
import com.vritti.vwb.Beans.ClaimDetailsBean;
import com.vritti.vwb.Beans.ClaimSummayBean;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClaimNewActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 9876;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", claimHeaderId = "";
    String TaskId, ApporverId, costcenterId;
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    SQLiteDatabase sql;
    public ArrayList<String> lsTaskActivityList, lsCRMCallList, CostCenterlist;
    ArrayList<AuthUserBeans> lsClaimApproverList;
    // ArrayList<CostCenterBeans> CostCenterlist ;
    @BindView(R.id.sp_task)
    Spinner sp_task;
    @BindView(R.id.sp_approver)
    Spinner sp_approver;
    @BindView(R.id.Sp_climAgainst)
    Spinner Sp_climAgainst;
    @BindView(R.id.sp_costcenter)
    Spinner Sp_costCenter;

    public String string_sp_task = "", string_sp_approver = "", string_Sp_climAgainst = "", string_Sp_costCenter = "";
    String ActivityId;
    String IsCrmUser;

    @BindView(R.id.ed_remark)
    EditText ed_remark;

    @BindView(R.id.ed_travel_purpose)
    EditText ed_travel_purpose;

    @BindView(R.id.lay_claim_details)
    RecyclerView lay_claim_details;
    @BindView(R.id.btnSave)
    Button btnSave;


    String CostCenter = "";
    String[] claim_against = {"Work", "Sales call"};

    @BindView(R.id.lay_claim_against)
    LinearLayout lay_claim_against;
    @BindView(R.id.lay_costcenter)
    LinearLayout lay_costcenter;
    //lay_cost_center;
    Context mContext = ClaimNewActivity.this;
    // ClaimDetailsFrangentAdapter adapter;
    ClaimDetailsRecycleAdapter adapter;
    List<ClaimDetailsBean> claimDetailsBeanList = new ArrayList<ClaimDetailsBean>();

    @BindView(R.id.toolbar1)
    Toolbar toolbar;

    boolean isEdit = false;

    /// File attachment
    String filePath, fileBase64Code, fileName;

    @BindView(R.id.toolbar_progress_App_bar)
    ProgressBar mProgress;


    ClaimSummayBean summayBean;
    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> taskAdapter;
    ArrayAdapter<String> costCenterBeansArrayAdapter;
    String activityName = "", claimDetailId = "";
    String Source="Claim";
    String docHdrId = "";
    ArrayList<String> stringArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.vwb_add_claim);
        ButterKnife.bind(this);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);

        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
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
        IsCrmUser = ut.getValue(context, WebUrlClass.GET_ISCRMUSER_KEY, settingKey);
        sql = db.getWritableDatabase();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        lay_claim_details.setLayoutManager(layoutManager);
        adapter = new ClaimDetailsRecycleAdapter(ClaimNewActivity.this, claimDetailsBeanList);
        btnSave.setVisibility(View.GONE);
        lay_claim_details.setAdapter(adapter);
        if (getIntent() != null) {
            summayBean = new Gson().fromJson(getIntent().getStringExtra("editObject"), ClaimSummayBean.class);
            if (summayBean != null)
                callEditApi(summayBean.getClaimHeaderId());
        }

        lsTaskActivityList = new ArrayList<String>();
        lsClaimApproverList = new ArrayList<>();
        lsCRMCallList = new ArrayList<String>();
        CostCenterlist = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        if (IsCrmUser.equalsIgnoreCase("true")) {
            lay_claim_against.setVisibility(View.GONE);
            UpdateTaskList();
        } else {
            lay_claim_against.setVisibility(View.VISIBLE);
            dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, claim_against);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Sp_climAgainst.setAdapter(dataAdapter);
        }
        SetListner();
        if (!(IsCostCenter())) {
            if (ut.isNet(this)) {
                showprogress();
                new StartSession(ClaimNewActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetCostcenter().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(ClaimNewActivity.this, msg);
                        hidprogress();
                    }
                });
            } else {
                ut.displayToast(ClaimNewActivity.this, "No Internet Connection");
            }
        } else {
            UpdateCostCenter();
        }

        if (cf.check_claim_approver() > 0) {
            UpdateApproverList();
        } else {
            showprogress();
            new StartSession(ClaimNewActivity.this, new CallbackInterface() {

                @Override
                public void callMethod() {

                    new GetDochdrId().execute();
                    // new GetClaimApprover().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(ClaimNewActivity.this, msg);
                    hidprogress();
                }
            });
        }

    }

    private boolean IsCostCenter() {
        CostCenterlist.clear();
        String query = "SELECT * FROM " + db.TABLE_COST_CENTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void callEditApi(final String claimHeaderId) {
        this.claimHeaderId = claimHeaderId;
        new StartSession(ClaimNewActivity.this, new CallbackInterface() {
            @Override
            public void callMethod() {
                new GetAllClaimData().execute(claimHeaderId);
            }

            @Override
            public void callfailMethod(String msg) {
                ut.displayToast(ClaimNewActivity.this, msg);
            }
        });

    }


    @OnClick(R.id.btn_add_claim)
    void addClaim() {
        if(ed_travel_purpose.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context,"Please fill purpose of travel details",Toast.LENGTH_SHORT).show();
            ed_travel_purpose.setFocusable(true);
        }else if(ed_remark.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(context,"Please fill remark",Toast.LENGTH_SHORT).show();
            ed_travel_purpose.setFocusable(true);
        }else {
            Intent intent = new Intent(this, com.vritti.vwb.vworkbench.ClaimDetailActivity.class);
            startActivityForResult(intent, 11);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 11 && resultCode == 12) {
                int pos = data.getIntExtra("Position", -1);
                ClaimDetailsBean bean = new Gson().fromJson(data.getStringExtra("object"), ClaimDetailsBean.class);
                if (pos == -1) {
                    claimDetailsBeanList.add(bean);

                } else {
                    claimDetailsBeanList.set(pos, bean);

                }
                setTotalData();
                if (claimDetailsBeanList.size() != 0) {
                    btnSave.setVisibility(View.VISIBLE);
                } else
                    btnSave.setVisibility(View.GONE);

                // adapter = new ClaimDetailsFrangentAdapter(ClaimNewActivity.this, claimDetailsBeanList);
                //lay_claim_details.setAdapter(adapter);

            }
            if (requestCode == MY_PERMISSIONS_REQUEST_READ_MEDIA) {
                if (data != null) {
                    Uri url = null;
                    url = data.getData();
              /*  if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // only for gingerbread and newer versions
                    int permissionCheck = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
                    } else {
                    }
                } else {
                }*/
                    filePath = FileUtilities.getPath(this, url);
                    fileName = filePath.substring((filePath.lastIndexOf('/') + 1),
                            filePath.length());
               /* text = new StringBuilder();
                File file = new File(filePath);*/
                /* FileReader fr = new FileReader(file);
                    UploadedImage = new BufferedReader(fr);
                    String line;
                    while ((line = UploadedImage.readLine()) != null) {
                        text.append(line);
                    }
                    UploadedImage.close();*/
                    new StartSession(this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new UploadAttach().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(ClaimNewActivity.this, msg);

                        }
                    });


                } else {
                    Toast.makeText(ClaimNewActivity.this, "Nothing Selected.", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }
    }

    private void setTotalData() {
        boolean isTotal = false;
        int pos = -1;
        float totoal =0.0f , distance = 0.0f , travelling = 0.0f ,local =0.0f , ph =0.0f, food =0.0f, loading = 0.0f , maintenance = 0.0f;
        for(int i = 0 ; i < claimDetailsBeanList.size() ; i++) {
            ClaimDetailsBean claimDetailsBean1 = claimDetailsBeanList.get(i);

            if (claimDetailsBean1.getClaimDate().equals("All Total")) {
                isTotal = true;
                pos = i;
               // claimDetailsBeanList.remove(claimDetailsBean1);
            } else {
                try {
                    totoal = Float.parseFloat(claimDetailsBean1.getAmount()) + totoal;
                    travelling = Float.parseFloat(claimDetailsBean1.getTv_travelling()) + travelling;
                    local = Float.parseFloat(claimDetailsBean1.getTv_Local()) + local;
                    ph = Float.parseFloat(claimDetailsBean1.getTv_Ph()) + ph;
                    food = Float.parseFloat(claimDetailsBean1.getTv_food()) + food;
                    loading = Float.parseFloat(claimDetailsBean1.getTv_lodging()) + loading;
                    maintenance = Float.parseFloat(claimDetailsBean1.getTv_Maintenanace()) + maintenance;
                    distance = Float.parseFloat(claimDetailsBean1.getDistance()) + distance;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ClaimDetailsBean caBean = new ClaimDetailsBean();
        caBean.setAmount(String.valueOf(totoal));
        caBean.setDistance(String.valueOf(distance));
        caBean.setTv_travelling(String.valueOf(travelling));
        caBean.setTv_Local(String.valueOf(local));
        caBean.setTv_Ph(String.valueOf(ph));
        caBean.setTv_food(String.valueOf(food));
        caBean.setTv_lodging(String.valueOf(loading));
        caBean.setTv_Maintenanace(String.valueOf(maintenance));
        caBean.setClaimDate("All Total");
        if(isTotal){
            claimDetailsBeanList.remove(pos);
        }
        claimDetailsBeanList.add(caBean);
        adapter.notifyDataSetChanged();
    }

    private void SetListner() {
        Sp_climAgainst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_Sp_climAgainst = parent.getItemAtPosition(position).toString();
                String Action = parent.getItemAtPosition(position).toString();
                if (Action.equalsIgnoreCase("Work")) {
                    UpdateTaskList();
                } else if (Action.equalsIgnoreCase("Sales call")) {
                    if (CRMCall()) {
                        UpdateCRMCall();
                    } else {
                        if (ut.isNet(context)) {


                            new StartSession(mContext, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new GetSalesCall().execute();
                                }

                                @Override
                                public void callfailMethod(String msg) {
                                    ut.displayToast(mContext, msg);
                                }
                            });
                        } else {
                            ut.displayToast(ClaimNewActivity.this, "No Internet Connetion");
                        }
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_approver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_sp_approver = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_task.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_sp_task = parent.getItemAtPosition(position).toString();
                String ActivityName = parent.getItemAtPosition(position).toString();
                String query = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName LIKE '" + ActivityName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        ActivityId = cur.getString(cur.getColumnIndex("ActivityId"));
                    } while (cur.moveToNext());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Sp_costCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_sp_approver = parent.getItemAtPosition(position).toString();
                CostCenter = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void UpdateCostCenter() {
        CostCenterlist.clear();
        String query = "SELECT * FROM " + db.TABLE_COST_CENTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                // CostCenterlist.add(new CostCenterBeans(cur.getString(cur.getColumnIndex("CostCtrDesc")) , cur.getString(cur.getColumnIndex("CostCtrMasterId"))));
                CostCenterlist.add(cur.getString(cur.getColumnIndex("CostCtrDesc")));
            } while (cur.moveToNext());
        }
        costCenterBeansArrayAdapter = new ArrayAdapter<>(ClaimNewActivity.this, android.R.layout.simple_spinner_item, CostCenterlist);
        costCenterBeansArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_costCenter.setAdapter(costCenterBeansArrayAdapter);
    }


    private void UpdateCRMCall() {
        lsCRMCallList.clear();
        String query = "SELECT FirmName,CallId FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsCRMCallList.add(cur.getString(cur.getColumnIndex("FirmName")));
            } while (cur.moveToNext());
        }
        taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsCRMCallList);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_task.setAdapter(taskAdapter);
        if (isEdit) {
            if (!activityName.equals("")) {
                String query1 = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName LIKE '" + activityName + "'";
                Cursor cur1 = sql.rawQuery(query1, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        ActivityId = cur1.getString(cur.getColumnIndex("ActivityId"));
                    } while (cur.moveToNext());
                }
            }
        }
    }

    private boolean CRMCall() {
        String query = "SELECT FirmName,CallId FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void UpdateTaskList() {
        lsTaskActivityList.clear();
        String query = "SELECT ActivityName,ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsTaskActivityList.add(cur.getString(cur.getColumnIndex("ActivityName")));
            } while (cur.moveToNext());
        }
        taskAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lsTaskActivityList);
        taskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_task.setAdapter(taskAdapter);
        if (isEdit) {
            if (!activityName.equals("")) {
                String query1 = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName LIKE '" + activityName + "'";
                Cursor cur1 = sql.rawQuery(query1, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        ActivityId = cur1.getString(cur.getColumnIndex("ActivityId"));
                    } while (cur.moveToNext());
                }
            }
        }
    }

    public void deleteClaim(final int adapterPosition) {
        new AlertDialog.Builder(context)
                .setTitle("Delete claim")
                .setMessage("Are you sure you want to delete this claim?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        claimDetailsBeanList.remove(adapterPosition);

                        adapter.notifyItemRemoved(adapterPosition);
                        if (claimDetailsBeanList.size() != 0) {
                            setTotalData();
                            btnSave.setVisibility(View.VISIBLE);
                        } else
                            setTotalData();
                            btnSave.setVisibility(View.GONE);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void editClaim(int adapterPosition) {
        Intent intent = new Intent(context, ClaimDetailActivity.class);
        intent.putExtra("Action", "Edit");
        intent.putExtra("Position", adapterPosition);
        intent.putExtra("object", new Gson().toJson(claimDetailsBeanList.get(adapterPosition)));
        startActivityForResult(intent, 11);

    }


    class GetSalesCall extends AsyncTask<Integer, Void, Integer> {
        String res, url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialog();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (res.contains("CallId")) {
                UpdateCRMCall();
            } else {
                Toast.makeText(ClaimNewActivity.this, "Fail to download call list ", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                url = CompanyURL + WebUrlClass.api_Get_Call + "?UserMstrId=" + URLEncoder.encode(UserMasterId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ut.displayToast(ClaimNewActivity.this, "Unsupported Encoding Exception occurred");
            }

            try {
                res = ut.OpenConnection(url, ClaimNewActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_CRM_CALL, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CRM_CALL, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    int a = jorder.length();
                    int a1 = c.getColumnCount();
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        if (columnName.contains("_")) {
                            columnValue = jorder.getString(columnName.replaceAll("_", " "));
                        } else {
                            columnValue = jorder.getString(columnName);
                        }
                        values.put(columnName, columnValue);

                    }

                    long a2 = sql.insert(db.TABLE_CRM_CALL, null, values);
                    String jhjs = a2 + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

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

                UpdateApproverList();
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
                res = ut.OpenConnection(url, ClaimNewActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();

                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_CLAIM_APPROVER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CLAIM_APPROVER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_CLAIM_APPROVER, null, values);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
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
            //url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;

            url =  CompanyURL + WebUrlClass.api_claim_dochdrId +"?Source=" + Source;

            // url = http://b207.ekatm.com/api/MyClaimAPI/getApproverList?DocMthdId=69
            try {
                res = ut.OpenConnection(url, ClaimNewActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }


        protected void onPostExecute(Integer integer) {
            String DocApprMthdId2="";
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

                        new StartSession(ClaimNewActivity.this, new CallbackInterface() {

                            @Override
                            public void callMethod() {

                                new GetClaimApprover().execute();
                            }

                            @Override
                            public void callfailMethod(String msg) {
                                ut.displayToast(ClaimNewActivity.this, msg);
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

    private void UpdateApproverList() {
        lsClaimApproverList.clear();
        String query = "SELECT * FROM " + db.TABLE_CLAIM_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                AuthUserBeans authUserBeans = new AuthUserBeans();
                authUserBeans.setUserName(cur.getString(cur.getColumnIndex("UserName")));//UserLoginId
                authUserBeans.setUserLoginId(cur.getString(cur.getColumnIndex("UserLoginId")));
                authUserBeans.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                lsClaimApproverList.add(authUserBeans);
            } while (cur.moveToNext());
        }
        ArrayAdapter<AuthUserBeans> dataAdapter = new ArrayAdapter<AuthUserBeans>(ClaimNewActivity.this, android.R.layout.simple_spinner_item, lsClaimApproverList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_approver.setAdapter(dataAdapter);
    }

    @OnClick(R.id.imAttachment)
    void addAttachment() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_MEDIA);
        } else {
            showFileChooser();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        // intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent = Intent.createChooser(intent, "Choose a file");
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showFileChooser();
                }
                break;

            default:
                break;
        }
    }

    class UploadAttach extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String upLoadServerUri1 = CompanyURL + WebUrlClass.api_UploadAttechment;
            String upLoadServerUri = WebUrlClass.api_UploadAttechment1;

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(filePath);

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", filePath);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + filePath + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @OnClick(R.id.btnSave)
    void submitClaim() {
        if (ed_travel_purpose.getText().toString().trim().equals("")) {
            ed_travel_purpose.setFocusable(true);
            Toast.makeText(this, "Please fill the purpose of travel", Toast.LENGTH_LONG).show();
        }else if(string_sp_approver.equalsIgnoreCase("")){
            sp_approver.setFocusable(true);
            Toast.makeText(context, "Please fill the remark ", Toast.LENGTH_SHORT).show();
        } else {
            if (claimDetailsBeanList.size() != 0) {

                if (!string_sp_task.equalsIgnoreCase("")) {
                    //textview_task.setText(ClaimHeaderFragment.string_sp_task);
                    String que = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName='" + sp_task.getSelectedItem().toString() + "'";
                    Cursor cur = sql.rawQuery(que, null);
                    if (cur.getCount() > 0) {
                        cur.moveToFirst();
                        TaskId = cur.getString(cur.getColumnIndex("ActivityId"));
                    }
                }
                if (!string_sp_approver.equalsIgnoreCase("")) {
                    //textview_approver.setText(ClaimHeaderFragment.string_sp_approver);
                    String que = "SELECT UserMasterId FROM " + db.TABLE_CLAIM_APPROVER + " WHERE UserName='" + sp_approver.getSelectedItem().toString() + "'";
                    Cursor cur1 = sql.rawQuery(que, null);
                    if (cur1.getCount() > 0) {
                        cur1.moveToFirst();
                        ApporverId = cur1.getString(cur1.getColumnIndex("UserMasterId"));
                    }
                }

                if (!ClaimHeaderFragment.string_Sp_costCenter.equalsIgnoreCase("")) {
                    // textview_costcenter.setText(ClaimHeaderFragment.string_Sp_costCenter);
                    String que = "SELECT CostCtrMasterId FROM " + db.TABLE_COST_CENTER + " WHERE CostCtrDesc='" + Sp_costCenter.getSelectedItem().toString() + "'";
                    Cursor cur2 = sql.rawQuery(que, null);
                    if (cur2.getCount() > 0) {
                        cur2.moveToFirst();
                        costcenterId = cur2.getString(cur2.getColumnIndex("CostCtrMasterId"));
                    }
                }

       /* if (!purpose.equalsIgnoreCase("")) {
            textview_purpose.setText(purpose);
        }
        textview_remark.setText(ClaimHeaderFragment.ed_remark.getText().toString());*/
                if (claimDetailsBeanList.size() > 0 && adapter != null) {

                    String remark = "Apply claim for  " + ed_travel_purpose.getText().toString() + " From " + claimDetailsBeanList.get(0).getFromPlace() + " to " + claimDetailsBeanList.get(claimDetailsBeanList.size() - 1).getToPlace();
                    claimDetailsBeanList.remove(claimDetailsBeanList.size()-1); // remove last object which is add all values of total
                    JSONObject jobj = getJobj();
                    final String sentdata = jobj.toString().replace("\\\\", "");
                    String url = CompanyURL + WebUrlClass.api_upload_Cliam;
                    String op = "Success";
                    CreateOfflineModeReschedule(url, sentdata, WebUrlClass.POSTFLAG, remark, op);

                } else {
                    Toast.makeText(this, "Fill claim detail first", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(this, "Fill claim detail first", Toast.LENGTH_LONG).show();
            }
        }


    }

    private void CreateOfflineModeReschedule(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(this, "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            ClaimDetailActivity.lsCalimDetails.clear();
            Intent intent1 = new Intent(this, SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            this.startService(intent1);
            Intent intent = new Intent(this, ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

           /* if (detailsListView != null) {
                detailsListView.removeAllViews();
            }*/

        } else {
            Toast.makeText(this, "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }

    private JSONObject getJobj() {
        JSONArray ClaimDtlTotal = null;
        JSONArray ClaimDtl = null;
        JSONObject main = null;
        JSONObject ClaimDtlTotalmain = null;
        JSONObject ClaimDtlmain = null;
        JSONArray DocMthdId = new JSONArray();
        ClaimDtlTotal = new JSONArray();
        try {
            // 1st object
            ClaimDtlTotalmain = new JSONObject();
            ClaimDtlTotalmain.put("totTravel", gettotTravel());
            ClaimDtlTotalmain.put("totLocal", gettotLocal());
            ClaimDtlTotalmain.put("totLodging", getLogging());
            ClaimDtlTotalmain.put("totPhone", gettotPhone());
            ClaimDtlTotalmain.put("totFood", gettotFood());
            ClaimDtlTotalmain.put("totRM", gettotRM());
            ClaimDtlTotalmain.put("totExp6", 0);
            ClaimDtlTotalmain.put("totExp7", 0);
            ClaimDtlTotalmain.put("totExp8", 0);
            ClaimDtlTotalmain.put("totExp9", 0);
            ClaimDtlTotalmain.put("totTotal", gettotTotal());
            ClaimDtlTotal.put(ClaimDtlTotalmain);

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            ClaimDtlTotal = null;
        }
        ClaimDtl = new JSONArray();
        Float detailTotal;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            detailTotal = 0.0f;
            ClaimDtlmain = new JSONObject();
            try {

                ClaimDtlmain.put("Date", getDateFormate(claimDetailsBeanList.get(i).getClaimDate()));
                ClaimDtlmain.put("FrmPlace", claimDetailsBeanList.get(i).getFromPlace());
                ClaimDtlmain.put("ToPlace", claimDetailsBeanList.get(i).getToPlace());
                String que = "SELECT Cd FROM " + db.TABLE_MODE_OF_JOURNY + " WHERE Desc_r='" + claimDetailsBeanList.get(i).getTv_mode() + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ClaimDtlmain.put("JMode", cur.getString(cur.getColumnIndex("Cd")));
                }
                ClaimDtlmain.put("ModeName", claimDetailsBeanList.get(i).getTv_mode());
                ClaimDtlmain.put("Travel", Double.parseDouble(claimDetailsBeanList.get(i).getTv_travelling()));
                ClaimDtlmain.put("Local", Float.parseFloat(claimDetailsBeanList.get(i).getTv_Local()));
                ClaimDtlmain.put("Lodging", Float.parseFloat(claimDetailsBeanList.get(i).getTv_lodging()));
                ClaimDtlmain.put("Phone", Float.parseFloat(claimDetailsBeanList.get(i).getTv_Ph()));
                ClaimDtlmain.put("Food", Float.parseFloat(claimDetailsBeanList.get(i).getTv_food()));
                ClaimDtlmain.put("Distance", claimDetailsBeanList.get(i).getDistance());
                ClaimDtlmain.put("RM", Float.parseFloat(claimDetailsBeanList.get(i).getTv_Maintenanace()));
                ClaimDtlmain.put("Exp6", 0);
                ClaimDtlmain.put("Exp7", 0);
                ClaimDtlmain.put("Exp8", 0);
                ClaimDtlmain.put("Exp9", 0);
                ClaimDtlmain.put("lblhdn", 0);
                ClaimDtlmain.put("lblDtlId", claimDetailId);
              /*  detailTotal = Float.parseFloat(claimDetailsBeanList.get(i).getTv_travelling()) +
                        Float.parseFloat(claimDetailsBeanList.get(i).getTv_Local()) +
                        Float.parseFloat(claimDetailsBeanList.get(i).getTv_Ph()) +
                        Float.parseFloat(claimDetailsBeanList.get(i).getTv_food()) +
                        Float.parseFloat(claimDetailsBeanList.get(i).getTv_lodging() +
                                Float.parseFloat(claimDetailsBeanList.get(i).getTv_Maintenanace()));*/

                Float tot = Float.parseFloat(claimDetailsBeanList.get(i).getAmount());
                ClaimDtlmain.put("Total", tot);
                ClaimDtl.put(ClaimDtlmain);

            } catch (Exception ex) {

            }
        }

        try {
            main = new JSONObject();
            main.put("Task", TaskId);// "CostCentre": "Ren/236"
            main.put("Approver", ApporverId);
            main.put("ApproverName", URLEncoder.encode(sp_approver.getSelectedItem().toString(), "UTF-8"));
            main.put("CostCentre", costcenterId);
            String PofTravel = ed_travel_purpose.getText().toString();
            main.put("POfTravel", PofTravel);
            String Remark = ed_remark.getText().toString();
            main.put("Remark", Remark);
            main.put("ClaimDtlTotal", ClaimDtlTotal);
            main.put("ClaimDtl", ClaimDtl);

            if (isEdit)
                main.put("FinalMode", "E");
            else
                main.put("FinalMode", "A");
            main.put("ClaimHeaderId", claimHeaderId);
            main.put("ClaimDeleteRec", DocMthdId);
            main.put("CallFlag", "");
            main.put("DocMthdId", "");//DocApproveInfo
            main.put("RejectMode", "N");
            main.put("FlagExp",false);

        } catch (Exception e) {
            main = null;
        }
        return main;
    }

    private String getDateFormate(String claimDate) {
        String formateDate = null;
        if (isEdit) {

            SimpleDateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(claimDate);
                System.out.println("Old Format :   " + originalFormat.format(date));
                System.out.println("New Format :   " + targetFormat.format(date));
                formateDate = String.valueOf(targetFormat.format(date));
                //return String.valueOf(targetFormat.format(date));
            } catch (ParseException ex) {
                // Handle Exception.
            }
        } else {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd MMM yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(claimDate);
                System.out.println("Old Format :   " + originalFormat.format(date));
                System.out.println("New Format :   " + targetFormat.format(date));
                formateDate = String.valueOf(targetFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return formateDate;
    }

    public float gettotTotal() {
        float totTravel = 0;


        totTravel = gettotTravel() + gettotFood() + gettotLocal() + gettotPhone() + gettotRM() + getLogging();

        return totTravel;
    }

    public float gettotTravel() {
        float totTravel = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totTravel = (float) (totTravel + Float.parseFloat(claimDetailsBeanList.get(i).getTv_travelling()));
        }
        return totTravel;
    }

    public float gettotLocal() {
        float totTravel = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totTravel = totTravel + Float.parseFloat(claimDetailsBeanList.get(i).getTv_Local());
        }
        return totTravel;
    }

    public float gettotPhone() {
        float totTravel = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totTravel = totTravel + Float.parseFloat(claimDetailsBeanList.get(i).getTv_Ph());
        }
        return totTravel;
    }

    public float gettotFood() {
        float totTravel = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totTravel = totTravel + Float.parseFloat(claimDetailsBeanList.get(i).getTv_food());
        }
        return totTravel;
    }

    public float gettotRM() {
        float totTravel = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totTravel = totTravel + Float.parseFloat(claimDetailsBeanList.get(i).getTv_Maintenanace());
        }
        return totTravel;
    }

    public float getLogging() {
        float totlodg = 0;
        for (int i = 0; i < claimDetailsBeanList.size(); i++) {
            totlodg = totlodg + Float.parseFloat(claimDetailsBeanList.get(i).getTv_lodging());
        }
        return totlodg;
    }

    class GetAllClaimData extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress();


        }

        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebAPIUrl.api_GetRefreshChatRoom + "?ApplicationCode="+WebAPIUrl.AppNameFCM;
            String claimId = params[0];
            String url = CompanyURL + WebUrlClass.get_claim_details + "?ClaimHeaderId=" + claimId;

            try {
                res = ut.OpenConnection(url, ClaimNewActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                JSONArray jResults = new JSONArray(response);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (response.equalsIgnoreCase("[]")) {
                //txt_chatroom_add.setVisibility(View.VISIBLE);

            } else {
                if (response != null) {
                    Log.i("response ::", response.toString());
                    JSONArray jResults = null;
                    try {
                        jResults = new JSONArray(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = null;
                        try {
                            jorder = jResults.getJSONObject(i);
                            if (i == jResults.length() - 1)
                                setData(jorder, 1);
                            else
                                setData(jorder, 0);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } else {

                }
            }
        }

    }


    class GetCostcenter extends AsyncTask<String, String, String> {
        String url, res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hidprogress();
            if (integer.contains("CostCtrMasterId")) {
                lay_costcenter.setVisibility(View.VISIBLE);
                UpdateCostCenter();
            } else {
                lay_costcenter.setVisibility(View.GONE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            url = CompanyURL + WebUrlClass.api_claim_cost_center;
            try {
                res = ut.OpenConnection(url, ClaimNewActivity.this);
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(res);
                String msg = "";
                sql.delete(db.TABLE_COST_CENTER, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_COST_CENTER, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_COST_CENTER, null, values);
                    String S = a + "";
                }

            } catch (Exception e) {
                e.printStackTrace();
                res = "error";
            }
            return res;
        }
    }

    private void setData(JSONObject jorder, int isLatValue) {

        //sp_task.setSelection(pos);

        //sp_approver.setSelection(pos1);
        ed_travel_purpose.setText(jorder.optString("Purpose"));
        ed_remark.setText(jorder.optString("Remark"));
        ClaimDetailsBean claimDetailsBean = new ClaimDetailsBean();
        claimDetailsBean.setAmount(jorder.optString("Total"));
        claimDetailsBean.setDistance(jorder.optString("Distance"));
        claimDetailsBean.setTv_mode(jorder.optString("ModeName"));
        claimDetailsBean.setFromPlace(jorder.optString("FromLocation"));
        claimDetailsBean.setToPlace(jorder.optString("ToLocation"));
        claimDetailsBean.setTv_travelling(jorder.optString("Exp1"));
        claimDetailsBean.setTv_Local(jorder.optString("Exp4"));
        claimDetailsBean.setTv_Ph(jorder.optString("Exp5"));
        claimDetailsBean.setTv_food(jorder.optString("Exp3"));
        claimDetailsBean.setTv_lodging(jorder.optString("Exp2"));
        claimDetailsBean.setTv_Maintenanace(jorder.optString("Exp10"));
        String jsonSDate = (jorder.optString("Date"));
        String StarDresult = jsonSDate.substring(jsonSDate.indexOf("(") + 1, jsonSDate.lastIndexOf(")"));
        long Stime = Long.parseLong(StarDresult);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        StarDresult = sdf.format(new Date(Stime));
        isEdit = true;
        claimDetailsBean.setClaimDate(StarDresult);
        claimDetailsBeanList.add(claimDetailsBean);
        if(isLatValue == 1){
            float totoal =0.0f , distance = 0.0f , travelling = 0.0f ,local =0.0f , ph =0.0f, food =0.0f, loading = 0.0f , maintenance = 0.0f;
            for(ClaimDetailsBean claimDetailsBean1 : claimDetailsBeanList){
                totoal = Float.parseFloat(claimDetailsBean1.getAmount())+totoal;
                distance = Float.parseFloat(claimDetailsBean1.getDistance())+distance;
                travelling = Float.parseFloat(claimDetailsBean1.getTv_travelling())+travelling;
                local = Float.parseFloat(claimDetailsBean1.getTv_Local())+local;
                ph = Float.parseFloat(claimDetailsBean1.getTv_Ph())+ph;
                food = Float.parseFloat(claimDetailsBean1.getTv_food())+food;
                loading = Float.parseFloat(claimDetailsBean1.getTv_lodging())+loading;
                maintenance = Float.parseFloat(claimDetailsBean1.getTv_Maintenanace())+maintenance;


            }
            ClaimDetailsBean caBean = new ClaimDetailsBean();
            caBean.setAmount(String.valueOf(totoal));
            caBean.setDistance(String.valueOf(distance));
            caBean.setTv_travelling(String.valueOf(travelling));
            caBean.setTv_Local(String.valueOf(local));
            caBean.setTv_Ph(String.valueOf(ph));
            caBean.setTv_food(String.valueOf(food));
            caBean.setTv_lodging(String.valueOf(loading));
            caBean.setTv_Maintenanace(String.valueOf(maintenance));
            caBean.setClaimDate("All Total");
            claimDetailsBeanList.add(caBean);
        }
        adapter.notifyDataSetChanged();
        claimDetailId = jorder.optString("ClaimDetailId");
        btnSave.setVisibility(View.VISIBLE);
        btnSave.setText("Update");
        activityName = jorder.optString("ActivityName");
        ActivityId = jorder.optString("ActivityId");
        TaskId = ActivityId;
        String query = "SELECT ActivityName FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityId LIKE '" + ActivityId + "'";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                int listPos = lsTaskActivityList.indexOf(cur.getString(cur.getColumnIndex("ActivityName")));
                taskAdapter.notifyDataSetChanged();
                sp_task.setSelection(listPos);
            } while (cur.moveToNext());
        } else {
            lsTaskActivityList.add(activityName);
            int listPos = lsTaskActivityList.indexOf(activityName);
            taskAdapter.notifyDataSetChanged();
            sp_task.setSelection(listPos);
        }



        sp_task.setEnabled(false);
        sp_task.setClickable(false);
        costcenterId = jorder.optString("CostCtrMasterId");
        String costDes = jorder.optString("CostCtrDesc");

        int costPos = CostCenterlist.indexOf(costDes);
        if (costPos != -1) {
            Sp_costCenter.setSelection(costPos);
        } else {
            CostCenterlist.add(activityName);
            int listPos = CostCenterlist.indexOf(costDes);
            costCenterBeansArrayAdapter.notifyDataSetChanged();
            Sp_costCenter.setSelection(listPos);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        if (id == R.id.refresh1) {

            if (!(IsCostCenter())) {
                if (ut.isNet(this)) {
                    showprogress();
                    new StartSession(ClaimNewActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetCostcenter().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(ClaimNewActivity.this, msg);
                            hidprogress();
                        }
                    });
                } else {
                    ut.displayToast(ClaimNewActivity.this, "No Internet Connection");
                }
            }


            if(ut.isNet(context)){
                showprogress();
                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetDochdrId().execute();
                        hidprogress();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        hidprogress();

                    }
                });
            }


            return true;
        } else if (id == android.R.id.home) {

            onBackPressed();
            return true;
        } else {

            return false;
        }
    }



}