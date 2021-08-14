package com.vritti.vwb.Adapter;

/**
 * Created by sharvari on 18-Sep-19.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.expensemanagement.EditExpensesActivity;
import com.vritti.expensemanagement.ExpenseData;
import com.vritti.expensemanagement.HistoryActivity;
import com.vritti.vwb.Beans.Attendance;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Jerry on 12/19/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AssistantViewHolder> {

    private ArrayList<Attendance> attendanceArrayList;
    Context context;
    double currentTotal=0;
    double currentExpense;

    public AttendanceAdapter(Context context, ArrayList<Attendance> attendanceArrayList) {
        this.context=context;
        this.attendanceArrayList = attendanceArrayList;
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
            holder.txt_present.setText("Present");
        }if (atten.equals("EarlyExit")) {
            holder.txt_present.setText("Early Go");
        }if (atten.equals("NP")) {
            holder.txt_present.setText("Without Pay");
        }if (atten.equals("L")) {
            holder.txt_present.setText("Leave");
        }if (atten.equals("LateEntry")) {
            holder.txt_present.setText("Late Mark");
        }if (atten.equals("HalfDay")) {
            holder.txt_present.setText("Half Day");
        }if (atten.equals("Holiday")) {
            holder.txt_present.setText("Holiday");
        }
        if (atten.equals("LateEarly")) {
            holder.txt_present.setText("Late and Early");
        }
        if (atten.equals("NPHalf")) {
            holder.txt_present.setText("Without Pay Half Day");
        }
        if (atten.equals("AUHalf")) {
            holder.txt_present.setText("Unauth Half Absentee");
        }
        if (atten.equals("AU")) {
            holder.txt_present.setText("Unauthorized Absentee");
        }
        if (atten.equals("NHalf")) {
            holder.txt_present.setText("Half Leave +Unauth Half");
        }
        if (atten.equals("NotOnRoll")) {
            holder.txt_present.setText("Not On Roll");
        }
        if (atten.equals("PHalf")) {
            holder.txt_present.setText("Half Day + Leave");
        }
        if (atten.equals("WeeklyOff")) {
            holder.txt_present.setText("Weekly Off");
        }
        if (atten.equals("SaturdayOff")) {
            holder.txt_present.setText("Saturday Off");
        }

        holder.txt_remark.setText(attendance.getRemarks());
        holder.txt_calls.setText(attendance.getCalls());
        holder.txt_visit.setText(attendance.getVisits());
        holder.txt_mail.setText(attendance.getMails());






    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.attendance_item_lay, parent, false);
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
                txt_timesheet_out,txt_timesheet_diff,txt_leavetype,txt_present,txt_remark;
        TextView txt_visit,txt_mail,txt_calls;
        LinearLayout len_source;
        ImageView img_edit,img_delete;

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
                txt_present = (TextView) itemView.findViewById(R.id.txt_present);
                txt_remark = (TextView) itemView.findViewById(R.id.txt_remark);
                txt_visit = (TextView) itemView.findViewById(R.id.txt_visit);
                txt_mail = (TextView) itemView.findViewById(R.id.txt_mail);
                txt_calls = (TextView) itemView.findViewById(R.id.txt_calls);



            }
        }
    }



}