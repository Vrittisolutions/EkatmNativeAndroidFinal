package com.vritti.vwb.Adapter;

/**
 * Created by sharvari on 18-Sep-19.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.crm.bean.OrderType;
import com.vritti.crm.vcrm7.CreateOpportunityActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.Attendance;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.TeamAttendanceDisplayActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jerry on 12/19/2017.
 */

public class TeamAttendanceAdapter extends RecyclerView.Adapter<TeamAttendanceAdapter.AssistantViewHolder> {

    private ArrayList<Attendance> attendanceArrayList;
    Context context;
    List<String> ListAttendis = new ArrayList<String>();
    List<OrderType> listAttendis = new ArrayList<>();
    AutoCompleteTextView txt_present;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",

    UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    public TeamAttendanceAdapter(Context context, ArrayList<Attendance> attendanceArrayList) {
        this.context=context;
        this.attendanceArrayList = attendanceArrayList;

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
    }

    @Override
    public void onBindViewHolder(final AssistantViewHolder holder, final int position) {
        final Attendance attendance = attendanceArrayList.get(position);
        holder.actiondatetime.setText(attendance.getDate());
        holder.txt_bio_in.setText(attendance.getBioInTime());
        holder.txt_bio_out.setText(attendance.getBioOutTime());
        holder.txt_bio_diff.setText(attendance.getBioDiff());
        if (attendance.getWorkHours().equals("null")){
            holder.txt_timesheet_in.setText("-");
        }else {
            holder.txt_timesheet_in.setText(attendance.getWorkHours());
        }
        if (attendance.getEndTime().equals("null")){
            holder.txt_timesheet_out.setText("-");
        }else {
            holder.txt_timesheet_out.setText(attendance.getEndTime());
        }
        if (attendance.getMainDiff().equals("null")){
            holder.txt_timesheet_diff.setText("-");
        }else {
            holder.txt_timesheet_diff.setText(attendance.getMainDiff().replace("-",""));
        }

        if (attendance.getLeaveType().equals("null")){
            holder.txt_leavetype.setText("-");
        }else {
            holder.txt_leavetype.setText(attendance.getLeaveType());
        }
        String atten=attendance.getAttendanceCode();
        if (atten.equals("P")) {
            txt_present.setText("Present");
        }if (atten.equals("EarlyExit")) {
            txt_present.setText("Early Go");
        }if (atten.equals("NP")) {
           txt_present.setText("Without Pay");
        }if (atten.equals("L")) {
           txt_present.setText("Leave");
        }if (atten.equals("LateEntry")) {
            txt_present.setText("Late Mark");
        }if (atten.equals("HalfDay")) {
            txt_present.setText("Half Day");
        }if (atten.equals("Holiday")) {
            txt_present.setText("Holiday");
        }
        if (atten.equals("LateEarly")) {
           txt_present.setText("Late and Early");
        }
        if (atten.equals("NPHalf")) {
            txt_present.setText("Without Pay Half Day");
        }
        if (atten.equals("AUHalf")) {
            txt_present.setText("Unauth Half Absentee");
        }
        if (atten.equals("AU")) {
            txt_present.setText("Unauthorized Absentee");
        }
        if (atten.equals("NHalf")) {
            txt_present.setText("Half Leave +Unauth Half");
        }
        if (atten.equals("NotOnRoll")) {
            txt_present.setText("Not On Roll");
        }
        if (atten.equals("PHalf")) {
            txt_present.setText("Half Day + Leave");
        }
        if (atten.equals("WeeklyOff")) {
            txt_present.setText("Weekly Off");
        }
        if (atten.equals("SaturdayOff")) {
            txt_present.setText("Saturday Off");
        }

        holder.txt_remark.setText(attendance.getRemarks());
        holder.txt_calls.setText(attendance.getCalls());
        holder.txt_visit.setText(attendance.getVisits());
        holder.txt_mail.setText(attendance.getMails());

      /*  holder.check_approve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }else {

                }



            }
        });
*/
/*
        txt_present.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((AutoCompleteTextView)view).showDropDown();
                return false;
            }
        });

        txt_present.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });*/



    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.team_attendance_item_lay, parent, false);
        return new AssistantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(attendanceArrayList==null)
        {
            attendanceArrayList = new ArrayList<Attendance>();
        }
        return attendanceArrayList.size();
    }
    public class AssistantViewHolder extends RecyclerView.ViewHolder {


        TextView actiondatetime,txt_bio_in,txt_bio_out,txt_bio_diff,txt_timesheet_in,
                txt_timesheet_out,txt_timesheet_diff,txt_leavetype,txt_remark;
        TextView txt_visit,txt_mail,txt_calls;
        LinearLayout len_source;
        ImageView img_edit,img_delete;
        AppCompatCheckBox check_approve;

        public AssistantViewHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {
                actiondatetime = (TextView) itemView.findViewById(R.id.actiondatetime);
                txt_bio_in = (TextView) itemView.findViewById(R.id.txt_bio_in);
                txt_bio_out = (TextView) itemView.findViewById(R.id.txt_bio_out);
                txt_bio_diff = (TextView) itemView.findViewById(R.id.txt_bio_diff);
                txt_timesheet_in = (TextView) itemView.findViewById(R.id.txt_timesheet_in);
                txt_timesheet_out = (TextView) itemView.findViewById(R.id.txt_timesheet_out);
                txt_timesheet_diff = (TextView) itemView.findViewById(R.id.txt_timesheet_diff);
                txt_leavetype = (TextView) itemView.findViewById(R.id.txt_leavetype);
                txt_present = (AutoCompleteTextView) itemView.findViewById(R.id.txt_present);
                txt_remark = (TextView) itemView.findViewById(R.id.txt_remark);
                txt_visit = (TextView) itemView.findViewById(R.id.txt_visit);
                txt_mail = (TextView) itemView.findViewById(R.id.txt_mail);
                txt_calls = (TextView) itemView.findViewById(R.id.txt_calls);
                check_approve = (AppCompatCheckBox) itemView.findViewById(R.id.check_approve);


               /* new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new DownloadAttendanceJSON().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {

                    }


                });*/


            }
        }
    }

    class DownloadAttendanceJSON extends AsyncTask<String , Void, String > {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected String  doInBackground(String ... params) {
            String url = CompanyURL + WebUrlClass.api_getFillAttendis;
            try {
                res = ut.OpenConnection(url);
                if (res != null) {
                    response = res.toString();
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.substring(1, response.length() - 1);

                    }


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String  integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();
            if (response.contains("PKAttendanceCodeMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(response);
                    for(int i=0;i<jResults.length();i++){
                        JSONObject jsonObject=jResults.getJSONObject(i);
                        String  PKAttendanceCodeMasterId=jsonObject.getString("PKAttendanceCodeMasterId");
                        String AttendanceCodeValue=jsonObject.getString("AttendanceCodeValue");
                        String AttendanceCodeDesc=jsonObject.getString("AttendanceCodeDesc");
                        listAttendis.add(new OrderType(PKAttendanceCodeMasterId,AttendanceCodeValue,AttendanceCodeDesc));
                        ListAttendis.add(AttendanceCodeDesc);
                    }

                   CreateOpportunityActivity.MySpinnerAdapter adapter = new CreateOpportunityActivity.MySpinnerAdapter(context,
                            R.layout.crm_custom_spinner_txt, ListAttendis);
                    txt_present.setAdapter(adapter);




                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }




}