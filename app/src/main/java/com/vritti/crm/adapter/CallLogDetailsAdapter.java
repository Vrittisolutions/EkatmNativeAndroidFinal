package com.vritti.crm.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vritti.crm.bean.CallLogsDetails;
import com.vritti.crm.vcrm7.CRM_CallLogList;
import com.vritti.crm.vcrm7.CallUpdateActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CallLogDetailsAdapter extends RecyclerView.Adapter<CallLogDetailsAdapter.ActivityHolder> {

    Context context;
    ArrayList<CallLogsDetails> callLogsDetailsArrayList;
    DatabaseHandlers db;
    SQLiteDatabase sql;

    public CallLogDetailsAdapter(CRM_CallLogList crm_callLogList, ArrayList<CallLogsDetails> list_calllogs) {
        this.context = crm_callLogList;
        this.callLogsDetailsArrayList = list_calllogs;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calllog_list, parent, false);


        return new ActivityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
        int hours = 0, minutes = 0, seconds = 0;
        String number = callLogsDetailsArrayList.get(position).getNumber();
        String contactPersonName = callLogsDetailsArrayList.get(position).getContactPersonName();
        String startTime = callLogsDetailsArrayList.get(position).getStartTime();
        String endtime = callLogsDetailsArrayList.get(position).getEndTime();
        String Duration = callLogsDetailsArrayList.get(position).getDuration();
        String customerName = callLogsDetailsArrayList.get(position).getCustomerName();
        String d[] = Duration.split(":");
        if (d.length == 3) {
            hours = Integer.parseInt(d[0]);
            minutes = Integer.parseInt(d[1]);
            seconds = Integer.parseInt(d[2]);
        }
        String CallType = callLogsDetailsArrayList.get(position).getCallType();
        String userName = callLogsDetailsArrayList.get(position).getUserName();
        /*  2020-06-05 20:59:38.393  */

        String startTime1 = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "HH:mm", startTime);
        String starttime[] = startTime1.split(":");
        startTime1= updateTime(Integer.parseInt(starttime[0]),Integer.parseInt(starttime[1]));

        String Date = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "dd-MMM", startTime);
        String EndTime1 = formateDateFromstring("yyyy-MM-dd HH:mm:ss.SSS", "HH:mm", endtime);

        if (contactPersonName != null) {
            if (contactPersonName.equals("")) {
                if (callLogsDetailsArrayList.get(position).getUsername()==null||callLogsDetailsArrayList.get(position).getUsername().equalsIgnoreCase("null")){
                    holder.txt_number.setText(number);
                }else {
                    holder.txt_mobile.setVisibility(View.VISIBLE);
                    holder.txt_number.setText(callLogsDetailsArrayList.get(position).getUsername());
                    holder.txt_mobile.setText(number);
                }
            } else {
                holder.txt_number.setText(contactPersonName);
            }

        } else {

        }

        String dur="";

        if(hours == 0){
           dur = minutes+"m "+seconds+"s";
        }else if(hours == 0 && minutes == 0){
            dur = seconds+"s";
        }else{
            dur = hours+"hr "+ minutes+"m "+seconds+"s";
        }
        holder.txt_durationdetails.setText(Date+" at "+startTime1+" for "+dur);

        if(customerName != null){
            holder.txt_customerName.setVisibility(View.VISIBLE);
            holder.txt_customerName.setText(customerName);
        }else{
            holder.txt_customerName.setVisibility(View.GONE);
        }
        if(CallType.equalsIgnoreCase("Incoming")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.colection));
            holder.txt_calltypename.setText("Not Linked");
           holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));

        } if(CallType.equalsIgnoreCase("Outgoing")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.notlinked));
            holder.txt_calltypename.setText("Not Linked");
           holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));

        }else if(CallType.equalsIgnoreCase("Spam")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.spam));
            holder.txt_calltypename.setText("Spam");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));
        }else if(CallType.equalsIgnoreCase("Colleague")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.colleagus));
            holder.txt_calltypename.setText("Colleague");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));
        }else if(CallType.equalsIgnoreCase("Personal")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.friends));
            holder.txt_calltypename.setText("Personal");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));
        }else if(CallType.equalsIgnoreCase("Collection")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.friends));
            holder.txt_calltypename.setText("Collection");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));
        }else if(CallType.equalsIgnoreCase("Opportunity")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.opportunity));
            holder.txt_calltypename.setText("Opportunity");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));
        }else if(CallType.equalsIgnoreCase("Opportunity/Collection") || CallType.equalsIgnoreCase("Opportunity/Collection")){
            holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.notlinked));
            holder.txt_calltypename.setText("Opp/Coll");
            holder.txt_calltypename.setTextColor(context.getResources().getColor(R.color.black));

        }
        holder.txt_call_state.setText(callLogsDetailsArrayList.get(position).getCallType());
        String firm=callLogsDetailsArrayList.get(position).getFirmname();
        if (firm==null||firm.equalsIgnoreCase("null")){
            holder.txt_firmname.setVisibility(View.GONE);
        }else {
            holder.txt_firmname.setVisibility(View.VISIBLE);
            holder.txt_firmname.setText(firm);
        }








      /*  holder.txt_starttime.setText("Start Time   " + startTime1);
        holder.txt_endtime.setText("End Time   " + EndTime1);
        holder.txt_date.setText("Date : " + Date);
        holder.txt_duration.setText("Duration   " + Duration);
        holder.txt_Calltype.setText("CallType : " + CallType);*/
        //    holder.txt_name.setText(userName);

        /*if (position % 2 == 1) {
            holder.ln_main.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            holder.ln_main.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }*/

    }

    @Override
    public int getItemCount() {
        return callLogsDetailsArrayList.size();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_number)
        TextView txt_number;
        @BindView(R.id.txt_customerName)
        TextView txt_customerName;
        @BindView(R.id.txt_durationdetails)
        TextView txt_durationdetails;
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.txt_calltypename)
        TextView txt_calltypename;
        @BindView(R.id.card_view)
        CardView card_view;
        @BindView(R.id.ln_main)
        LinearLayout ln_main;
        @BindView(R.id.txt_call_state)
        TextView txt_call_state;
        @BindView(R.id.txt_firmname)
        TextView txt_firmname;
        @BindView(R.id.txt_mobile)
        TextView txt_mobile;





      /*  @BindView(R.id.txt_starttime)
        TextView txt_starttime;
        @BindView(R.id.txt_endtime)
        TextView txt_endtime;
        @BindView(R.id.txt_duration)
        TextView txt_duration;
        @BindView(R.id.txt_Calltype)
        TextView txt_Calltype;
        @BindView(R.id.txt_date)
        TextView txt_date;
        *//*   @BindView(R.id.txt_name)
           TextView txt_name;*//*
        @BindView(R.id.card_view)
        CardView card_view;*/


        public ActivityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        void rowClick() {
            ((CRM_CallLogList) context).rowClick(getAdapterPosition());
           /* String datasheetList = new Gson().toJson(new CallLogsDetails(callLogsDetailsArrayList));
            Intent intent = new Intent(context, CallUpdateActivity.class);
            intent.putExtra("list",datasheetList);
            intent.putExtra("position",getAdapterPosition());*/
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

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if(hours > 12){
                hours = hours - 12;
                timeSet = "AM";
            }else if(hours == 12) {
                timeSet ="AM";
            }else{
                timeSet = "PM";
            }

        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

}
