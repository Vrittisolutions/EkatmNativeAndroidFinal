package com.vritti.vwb.vworkbench;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

/**
 * Created by 300151 on 11/18/2016.
 */
public class ClaimHeaderFragment extends Fragment {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;


    private View rootView;
    SQLiteDatabase sql;
    public static ArrayList<String> lsTaskActivityList, lsClaimApproverList, lsCRMCallList, CostCenterlist;
    public static Spinner sp_task, sp_approver, Sp_climAgainst, Sp_costCenter;
    public static String string_sp_task="", string_sp_approver="", string_Sp_climAgainst="", string_Sp_costCenter="";
    String  ActivityId;
    String IsCrmUser;
    public static EditText ed_remark, ed_travel_purpose;
    String CostCenter = "";
    String[] claim_against = {"Work", "Sales call"};
    LinearLayout lay_claim_against,lay_cost_center;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.vwb_fragment_claim_header, container,
                false);
        mContext = getActivity().getApplicationContext();


        context = getContext();
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsCrmUser = ut.getValue(context, WebUrlClass.GET_ISCRMUSER_KEY, settingKey);

        InitView(rootView);
        if (IsCrmUser.equalsIgnoreCase("true")) {
            lay_claim_against.setVisibility(View.GONE);
            UpdateTaskList();
        } else {
            lay_claim_against.setVisibility(View.VISIBLE);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, claim_against);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Sp_climAgainst.setAdapter(dataAdapter);
        }
        SetListner();
        if (!(IsCostCenter())) {
            if (ut.isNet(getContext())) {
                showprogress();
                new StartSession(getActivity(), new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new GetCostcenter().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(getContext(), msg);
                        hidprogress();
                    }
                });
            } else {
                ut.displayToast(getContext(), "No Internet Connection");
            }
        } else {
            UpdateCostCenter();
        }


        return rootView;
    }

    private void InitView(View rootView) {
        sql = db.getWritableDatabase();
        lsTaskActivityList = new ArrayList<String>();
        lsClaimApproverList = new ArrayList<String>();
        lsCRMCallList = new ArrayList<String>();
        CostCenterlist = new ArrayList<String>();
        sp_task = (Spinner) rootView.findViewById(R.id.sp_task);
        lay_claim_against = (LinearLayout) rootView.findViewById(R.id.lay_claim_against);
        lay_cost_center = (LinearLayout) rootView.findViewById(R.id.lay_costcenter);

        Sp_climAgainst = (Spinner) rootView.findViewById(R.id.Sp_climAgainst);
        sp_approver = (Spinner) rootView.findViewById(R.id.sp_approver);
        ed_travel_purpose = (EditText) rootView.findViewById(R.id.ed_travel_purpose);
        ed_remark = (EditText) rootView.findViewById(R.id.ed_remark);
        Sp_costCenter = (Spinner) rootView.findViewById(R.id.sp_costcenter);//progressbarc
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
                        if (ut.isNet(getContext())) {


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
                            ut.displayToast(getContext(), "No Internet Connetion");
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
                String query = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING  + " WHERE ActivityName LIKE '" + ActivityName + "'";
                Cursor cur = sql.rawQuery(query, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    do {
                        ActivityId = cur.getString(cur.getColumnIndex("ActivityId"));
                    } while (cur.moveToNext());
                }
                if (cf.check_claim_approver() > 0) {
                    UpdateApproverList();
                } else {
                    showprogress();
                    new StartSession(getActivity(), new CallbackInterface() {

                        @Override
                        public void callMethod() {
                            new GetClaimApprover().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getContext(), msg);
                            hidprogress();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Sp_costCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                string_sp_approver= parent.getItemAtPosition(position).toString();
                CostCenter = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void UpdateTaskList() {
        lsTaskActivityList.clear();
        String query = "SELECT ActivityName,ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsTaskActivityList.add(cur.getString(cur.getColumnIndex("ActivityName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lsTaskActivityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_task.setAdapter(dataAdapter);
    }

    public void UpdateCRMCall() {
        lsCRMCallList.clear();
        String query = "SELECT FirmName,CallId FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsCRMCallList.add(cur.getString(cur.getColumnIndex("FirmName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lsCRMCallList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_task.setAdapter(dataAdapter);
    }

    public Boolean CRMCall() {
        String query = "SELECT FirmName,CallId FROM " + db.TABLE_CRM_CALL;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*private void showProgressDialog() {
        progressHUD = ProgressHUD.show(getActivity(), "", false, false, null);
    }

    private void dismissProgressDialog() {
        if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }*/

    public void UpdateApproverList() {
        lsClaimApproverList.clear();
        String query = "SELECT UserName FROM " + db.TABLE_CLAIM_APPROVER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                lsClaimApproverList.add(cur.getString(cur.getColumnIndex("UserName")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, lsClaimApproverList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_approver.setAdapter(dataAdapter);

    }

    public Boolean IsCostCenter() {
        CostCenterlist.clear();
        String query = "SELECT * FROM " + db.TABLE_COST_CENTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public void UpdateCostCenter() {
        CostCenterlist.clear();
        String query = "SELECT CostCtrDesc FROM " + db.TABLE_COST_CENTER;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                CostCenterlist.add(cur.getString(cur.getColumnIndex("CostCtrDesc")));
            } while (cur.moveToNext());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, CostCenterlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Sp_costCenter.setAdapter(dataAdapter);
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
            if (!res.equalsIgnoreCase("")) {
                hidprogress();
                UpdateApproverList();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            url = CompanyURL + WebUrlClass.api_claim_approver + "?ActivityId=" + ActivityId + "&UserMstrId=" + UserMasterId;
            try {
                res = ut.OpenConnection(url,getContext());
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
                lay_cost_center.setVisibility(View.VISIBLE);
                UpdateCostCenter();
                }else {
                lay_cost_center.setVisibility(View.GONE);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            url = CompanyURL + WebUrlClass.api_claim_cost_center;
            try {
                res = ut.OpenConnection(url,getContext());
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

    @Override
    public void onResume() {
        super.onResume();
        if (ed_remark != null && ed_travel_purpose != null && !ed_remark.getText().toString().equalsIgnoreCase("") && !ed_travel_purpose.getText().toString().equalsIgnoreCase("")) {
            ed_remark.setText(ed_remark.getText().toString());
            ed_travel_purpose.setText(ed_travel_purpose.getText().toString());
        }


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
                Toast.makeText(getContext(), "Fail to download call list ", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                url = CompanyURL + WebUrlClass.api_Get_Call + "?UserMstrId=" + URLEncoder.encode(UserMasterId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                ut.displayToast(getContext(), "Unsupported Encoding Exception occurred");
            }

            try {
                res = ut.OpenConnection(url,getContext());
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
        com.vritti.vwb.vworkbench.ClaimMainActivity.mprogress.setVisibility(View.VISIBLE);

    }

    void hidprogress() {
        ClaimMainActivity.mprogress.setVisibility(View.GONE);

    }


}