package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.vwb.classes.CommonFunction;

import static com.vritti.vwb.vworkbench.ClaimDetailsFragment.adapter;


/**
 * Created by 300151 on 11/18/2016.
 */
public class ClaimSummaryFragment extends Fragment {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Button buttonApplyClaim;
    TextView textview_task, textview_approver, textview_purpose, textview_remark, textview_costcenter;
    LinearLayout detailsListView;
    private View rootView;
    public static TextView tv_claim_edit, tv_claim_cancel,tv_distance;

    public static TextView edit, cancel, tv_Cdate, tv_Camount, fromPlace, ToPlace, tv_mode, tv_travelling, tv_lodging, tv_food, tv_Local, tv_Ph, tv_Maintenanace;
    JSONObject ClaimJSONObj;
    String  ClaimJSON;
    SharedPreferences userpreferences;
    String TaskId = "", ApporverId = "", costcenterId = "";
    String DocApproveInfo;
    JSONArray ClaimDeleteRecarray;
    JSONArray jarray;
    SQLiteDatabase sql;
    JSONObject claimDtlsobjTotalobj, claimDtlsobj;
    public static JSONArray claimDtlArray = new JSONArray();
    public static JSONArray claimDtls = new JSONArray();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vwb_fragment_claim_summary, container,
                false);
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

        sql = db.getWritableDatabase();

        initialize(rootView);
        SetListner();
        return rootView;
    }

    public void addView_new(int i) {
        String[] claimAction = {"Edit", "Cancel"};
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_claim_details_item,
                null);
        edit = (TextView) baseView.findViewById(R.id.edit);
        edit.setVisibility(View.GONE);
        cancel = (TextView) baseView.findViewById(R.id.cancel);
        cancel.setVisibility(View.GONE);
        tv_claim_cancel = (TextView) baseView.findViewById(R.id.tv_claim_cancel);
        tv_distance = (TextView) baseView.findViewById(R.id.tv_distance);
        tv_claim_edit = (TextView) baseView.findViewById(R.id.tv_claim_edit);
        tv_claim_cancel.setVisibility(View.GONE);
        tv_claim_edit.setVisibility(View.GONE);
        tv_Camount = (TextView) baseView.findViewById(R.id.tv_Total);
        tv_travelling = (TextView) baseView.findViewById(R.id.tv_travelling);
        tv_Cdate = (TextView) baseView.findViewById(R.id.tv_clim_date);
        fromPlace = (TextView) baseView.findViewById(R.id.fromPlace);
        ToPlace = (TextView) baseView.findViewById(R.id.ToPlace);
        tv_food = (TextView) baseView.findViewById(R.id.tv_food);
        tv_Local = (TextView) baseView.findViewById(R.id.tv_Local);
        tv_lodging = (TextView) baseView.findViewById(R.id.tv_lodging);
        tv_Maintenanace = (TextView) baseView.findViewById(R.id.tv_Maintenanace);
        tv_mode = (TextView) baseView.findViewById(R.id.tv_mode);
        tv_Ph = (TextView) baseView.findViewById(R.id.tv_Ph);
        tv_Cdate.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getClaimDate());
        tv_Camount.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getAmount());
        fromPlace.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getFromPlace());
        ToPlace.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getToPlace());
        tv_food.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_food());
        tv_Local.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Local());
        tv_lodging.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_lodging());
        tv_Maintenanace.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Maintenanace());
        tv_mode.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_mode());
        tv_Ph.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Ph());
        tv_travelling.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_travelling());
        tv_distance.setText(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getDistance());

        detailsListView.addView(baseView);
    }

    private void initialize(View rootView) {


        buttonApplyClaim = (Button) rootView
                .findViewById(R.id.button_claim_summary_apply_claim);//progressbarc

        textview_task = (TextView) rootView
                .findViewById(R.id.textview_claim_summary_task);
        textview_approver = (TextView) rootView
                .findViewById(R.id.textview_claim_summary_approver);
        textview_costcenter = (TextView) rootView
                .findViewById(R.id.textview_claim_summary_costcenter);
        textview_purpose = (TextView) rootView
                .findViewById(R.id.textview_claim_summary_purpose);
        textview_remark = (TextView) rootView
                .findViewById(R.id.textview_claim_summary_remarks);

        detailsListView = (LinearLayout) rootView
                .findViewById(R.id.listview_claims_summary_details);

        setValues();
        detailsListView.removeAllViews();
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            addView_new(i);
        }


    }

    private void SetListner() {
        buttonApplyClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size() > 0 && adapter != null) {

                        String remark = "Apply claim for  " + textview_purpose.getText().toString() + " From " + fromPlace.getText().toString() + " to " + ToPlace.getText().toString();
                        JSONObject jobj = getJobj();
                        final String sentdata = jobj.toString().replace("\\\\", "");
                        String url = CompanyURL + WebUrlClass.api_upload_Cliam;
                        String op = "Success";
                        CreateOfflineModeReschedule(url, sentdata, WebUrlClass.POSTFLAG, remark, op);

                } else {
                    Toast.makeText(getContext(), "Fill claim detail first", Toast.LENGTH_LONG).show();

                }


                   /* new StartSession(getActivity(), new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            JSONObject jobj = getJobj();
                            final String sentdata = jobj.toString().replace("\\\\", "");
                            new UploadClaimJSONData().execute(sentdata);
                            // new DownloadGetDocApproveInfo().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ut.displayToast(getContext(), msg);
                            ClaimMainActivity.mprogress.setVisibility(View.GONE);

                        }
                    });*/
            }
        });
    }

    class DownloadGetDocApproveInfo extends AsyncTask<String, String, String> {
        String res;

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (!(DocApproveInfo.equalsIgnoreCase(""))) {
                JSONObject jobj = getJobj();
                final String sentdata = jobj.toString().replace("\\\\", "");
                new UploadClaimJSONData().execute(sentdata);
            } else {
                com.vritti.vwb.vworkbench.ClaimMainActivity.mprogress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to Download DocApprover ID", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_GetDocAppInfoMyCalim + "?Source=" + URLEncoder.encode("Claim", "UTF-8");
                res = ut.OpenConnection(url,getContext());
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                JSONArray jResults = new JSONArray(res);
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jobject = jResults.getJSONObject(i);
                    DocApproveInfo = jobject.getString("DocApprMthdId");
                }
            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

    class UploadClaimJSONData extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            ClaimMainActivity.mprogress.setVisibility(View.GONE);
            if (integer.equalsIgnoreCase("Success")) {
                Toast.makeText(getActivity(), "Claim Applied Succesfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), com.vritti.vwb.vworkbench.ActivityMain.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getContext(), "Claim Application Failed", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + WebUrlClass.api_upload_Cliam;
            try {
                res = ut.OpenPostConnection(url, params[0],getContext());
                int b = res.toString().getBytes().length;
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = "Error";

            }
            return response;
        }
    }


    public int gettotTotal() {
        int totTravel = 0;



        totTravel = gettotTravel() + gettotFood() + gettotLocal() + gettotPhone() + gettotRM()+getLogging();

        return totTravel;
    }

    public int gettotTravel() {
        int totTravel = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totTravel = (int) (totTravel + Double.parseDouble(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_travelling()));
        }
        return totTravel;
    }

    public int gettotLocal() {
        int totTravel = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totTravel = totTravel + Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Local());
        }
        return totTravel;
    }

    public int gettotPhone() {
        int totTravel = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totTravel = totTravel + Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Ph());
        }
        return totTravel;
    }

    public int gettotFood() {
        int totTravel = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totTravel = totTravel + Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_food());
        }
        return totTravel;
    }

    public int gettotRM() {
        int totTravel = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totTravel = totTravel + Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Maintenanace());
        }
        return totTravel;
    }

    public int getLogging() {
        int totlodg = 0;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            totlodg = totlodg + Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_lodging());
        }
        return totlodg;
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
        int detailTotal;
        for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
            detailTotal = 0;
            ClaimDtlmain = new JSONObject();
            try {

                ClaimDtlmain.put("Date", com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getClaimDate());
                ClaimDtlmain.put("FrmPlace", com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getFromPlace());
                ClaimDtlmain.put("ToPlace", com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getToPlace());
                String que = "SELECT Cd FROM " + db.TABLE_MODE_OF_JOURNY + " WHERE Desc_r='" + com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_mode() + "'";
                Cursor cur = sql.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ClaimDtlmain.put("JMode", cur.getString(cur.getColumnIndex("Cd")));
                }
                ClaimDtlmain.put("ModeName", com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_mode());
                ClaimDtlmain.put("Travel", Double.parseDouble(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_travelling()));
                ClaimDtlmain.put("Local", Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Local()));
                ClaimDtlmain.put("Lodging", Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_lodging()));
                ClaimDtlmain.put("Phone", Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Ph()));
                ClaimDtlmain.put("Food", Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_food()));
                ClaimDtlmain.put("Distance", com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getDistance());
                ClaimDtlmain.put("RM", Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Maintenanace()));
                ClaimDtlmain.put("Exp6", 0);
                ClaimDtlmain.put("Exp7", 0);
                ClaimDtlmain.put("Exp8", 0);
                ClaimDtlmain.put("Exp9", 0);
                ClaimDtlmain.put("lblhdn", 0);
                ClaimDtlmain.put("lblDtlId", "");
                 detailTotal = Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_travelling()) +
                        Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Local()) +
                        Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Ph()) +
                        Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_food()) +
                        Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_lodging()+
                                Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getTv_Maintenanace()));

                int tot = Integer.parseInt(com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.get(i).getAmount());
                ClaimDtlmain.put("Total", tot);
                ClaimDtl.put(ClaimDtlmain);

            } catch (Exception ex) {

            }
        }

        try {
            main = new JSONObject();
            main.put("Task", TaskId);// "CostCentre": "Ren/236"
            main.put("Approver", ApporverId);
            main.put("ApproverName", URLEncoder.encode(textview_approver.getText().toString(), "UTF-8"));
            main.put("CostCentre", costcenterId);
            String PofTravel=textview_purpose.getText().toString();
            main.put("POfTravel",PofTravel );
            String Remark=textview_remark.getText().toString();
            main.put("Remark",Remark);
            main.put("ClaimDtlTotal", ClaimDtlTotal);
            main.put("ClaimDtl", ClaimDtl);
            main.put("FinalMode", "A");
            main.put("ClaimHeaderId", "");
            main.put("ClaimDeleteRec", DocMthdId);
            main.put("CallFlag", "");
            main.put("DocMthdId", "");//DocApproveInfo
            main.put("RejectMode", "N");

        } catch (Exception e) {
            main = null;
        }
        return main;
    }


    private void setValues() {
        if (!ClaimHeaderFragment.string_sp_task.equalsIgnoreCase("")) {
            textview_task.setText(ClaimHeaderFragment.string_sp_task);
            String que = "SELECT ActivityId FROM " + db.TABLE_ACTIVITYMASTER_PAGING + " WHERE ActivityName='" + com.vritti.vwb.vworkbench.ClaimHeaderFragment.sp_task.getSelectedItem().toString() + "'";
            Cursor cur = sql.rawQuery(que, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                TaskId = cur.getString(cur.getColumnIndex("ActivityId"));
            }
        }
        if (!ClaimHeaderFragment.string_sp_approver.equalsIgnoreCase("")) {
            textview_approver.setText(ClaimHeaderFragment.string_sp_approver);
            String que = "SELECT UserMasterId FROM " + db.TABLE_CLAIM_APPROVER + " WHERE UserName='" + com.vritti.vwb.vworkbench.ClaimHeaderFragment.sp_approver.getSelectedItem().toString() + "'";
            Cursor cur1 = sql.rawQuery(que, null);
            if (cur1.getCount() > 0) {
                cur1.moveToFirst();
                ApporverId = cur1.getString(cur1.getColumnIndex("UserMasterId"));
            }
        }

        if (!ClaimHeaderFragment.string_Sp_costCenter.equalsIgnoreCase("")) {
            textview_costcenter.setText(ClaimHeaderFragment.string_Sp_costCenter);
            String que = "SELECT CostCtrMasterId FROM " + db.TABLE_COST_CENTER + " WHERE CostCtrDesc='" + com.vritti.vwb.vworkbench.ClaimHeaderFragment.Sp_costCenter.getSelectedItem().toString() + "'";
            Cursor cur2 = sql.rawQuery(que, null);
            if (cur2.getCount() > 0) {
                cur2.moveToFirst();
                costcenterId = cur2.getString(cur2.getColumnIndex("CostCtrMasterId"));
            }
        }
        String purpose = com.vritti.vwb.vworkbench.ClaimHeaderFragment.ed_travel_purpose.getText().toString();
        if (!purpose.equalsIgnoreCase("")) {
            textview_purpose.setText(purpose);
        }
        textview_remark.setText(ClaimHeaderFragment.ed_remark.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (detailsListView != null) {
            setValues();
            detailsListView.removeAllViews();
            for (int i = 0; i < com.vritti.vwb.vworkbench.ClaimDetailActivity.lsCalimDetails.size(); i++) {
                addView_new(i);
            }
        }
    }

    private void CreateOfflineModeReschedule(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getContext(), "Record Saved Sucessfully", Toast.LENGTH_LONG).show();
            ClaimDetailActivity.lsCalimDetails.clear();
            Intent intent1 = new Intent(getContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY,
                    WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);
            getContext().startService(intent1);
            Intent intent = new Intent(getContext(), ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

           /* if (detailsListView != null) {
                detailsListView.removeAllViews();
            }*/

        } else {
            Toast.makeText(getContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }
}
