package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.crm.bean.Schedule;
import com.vritti.crm.bean.VisitPlan;
import com.vritti.crm.vcrm7.FollowupActivity;
import com.vritti.ekatm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by 300151 on 10/13/16.
 */
public class VisitPlanAdapter extends RecyclerView.Adapter<VisitPlanAdapter.callHolder> {
    ArrayList<VisitPlan> callHistoryArrayList;
    LayoutInflater mInflater;
    Context context;

    public VisitPlanAdapter(Context context1, ArrayList<VisitPlan> callHistoryArrayList) {
        this.callHistoryArrayList = callHistoryArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }


    @NonNull
    @Override
    public callHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crm_visit_plan, parent, false);


        return new callHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitPlanAdapter.callHolder holder, @SuppressLint("RecyclerView") final int pos) {


        holder.txt_firmname.setText(callHistoryArrayList.get(pos).getFirmName());
        holder.txt_comment.setText(callHistoryArrayList.get(pos).getFollowupComment());
        holder.txt_followup_date.setText(callHistoryArrayList.get(pos).getCityName());

      /*  SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        String followdate=callHistoryArrayList.get(pos).getFollowupDate();
        followdate = followdate.substring(followdate.indexOf("(") + 1, followdate.lastIndexOf(")"));
        long timestamp = Long.parseLong(followdate);
        Date date = new Date(timestamp);
        String followupDate = sdf.format(date);
        holder.txt_followup_date.setText(followupDate);

        String time=callHistoryArrayList.get(pos).getNextActionDateTime();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy hh:mm");
        time = time.substring(time.indexOf("(") + 1, time.lastIndexOf(")"));
        long timestamp1 = Long.parseLong(time);
        Date date1 = new Date(timestamp1);
        String Actiondatetime = sdf1.format(date1);
        holder.actiondatetime.setText(Actiondatetime);*/


    }


    @Override
    public int getItemCount() {
        return callHistoryArrayList.size();
    }

    public class callHolder extends RecyclerView.ViewHolder {
        TextView txt_firmname,txt_followup_date,actiondatetime,txt_comment;
        CardView cardView;
        ImageView img_followuptype;

        public callHolder(View convertView) {
            super(convertView);
            cardView = convertView.findViewById(R.id.cardview);
            txt_firmname = convertView.findViewById(R.id.txt_firmname);
            txt_followup_date = convertView.findViewById(R.id.txt_followup_date);
            actiondatetime = convertView.findViewById(R.id.actiondatetime);
            txt_comment = convertView.findViewById(R.id.txt_comment);
        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

}
