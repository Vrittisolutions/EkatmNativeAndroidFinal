package com.vritti.crm.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pixplicity.htmlcompat.HtmlCompat;
import com.vritti.crm.bean.CRMDayEndReportDetailsBean;
import com.vritti.crm.vcrm7.CRMDayEndReport;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import java.util.ArrayList;

public class CRMDayEndReportAdapter extends RecyclerView.Adapter<CRMDayEndReportAdapter.ReportHolder> {

    Context context;
    ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList;
    String prospectName = "-", person = "-", followupType = "-", outCome = "-", reason = "-", time = "-", duration = "-",
            callPurposeDesc = "-", historyNotes = "-", details1 = "";
    int initiatedBy;
    StringBuilder fullString1 = new StringBuilder();

    public CRMDayEndReportAdapter(Context context, ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList) {

        this.context = context;
        this.crmDayEndReportDetailsBeanArrayList = crmDayEndReportDetailsBeanArrayList;
    }


    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crm_day_end_report_row_activity, parent, false);


        return new ReportHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {

        fullString1 = new StringBuilder();
        prospectName = crmDayEndReportDetailsBeanArrayList.get(position).getFirmname().trim();
        person = crmDayEndReportDetailsBeanArrayList.get(position).getContactName().trim();
        followupType = crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().trim();
        outCome = crmDayEndReportDetailsBeanArrayList.get(position).getOutcome().trim();
        reason = crmDayEndReportDetailsBeanArrayList.get(position).getReasonDescription().trim();
        time = crmDayEndReportDetailsBeanArrayList.get(position).getSchTime().trim();
        try{
            String[] split1;
            String[] splitTime = new String[0];
            if(time.contains("am") || time.contains("pm")||time.contains("AM") || time.contains("PM")){
                split1 = time.split(" ");
                splitTime = split1[0].split(":");
            }else{
                splitTime = time.split(":");
            }

            // int h  = Integer.parseInt(splitTime[0]);
            if (!time.equals("")) {
                time = updateTime(Integer.parseInt(splitTime[0].trim()), Integer.parseInt(splitTime[1].trim())).trim();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        duration = crmDayEndReportDetailsBeanArrayList.get(position).getTotalHoursSpent().trim();
        callPurposeDesc = crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().trim();
        historyNotes = crmDayEndReportDetailsBeanArrayList.get(position).getHistorynotes().trim();
        initiatedBy = crmDayEndReportDetailsBeanArrayList.get(position).getInitiatedBy();


        if(crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().equalsIgnoreCase("")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }else if(crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().equalsIgnoreCase("Telephone")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }else if(crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().equalsIgnoreCase("Email")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.email_icon_white));
        }else if(crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc().equalsIgnoreCase("visit")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.visit));
        }else{
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }

        if(time.equals("")){
            holder.txt_time.setVisibility(View.VISIBLE);
            holder.txt_time.setText("09:00 AM");
        }else{
            holder.txt_time.setVisibility(View.VISIBLE);
            holder.txt_time.setText(time);
        }

        if(duration.equals("")){
            holder.txt_duration.setVisibility(View.INVISIBLE);
        }else{
            holder.txt_duration.setVisibility(View.VISIBLE);
            holder.txt_duration.setText(duration);
        }

        holder.txt_title.setText(prospectName);
        if(person.equals("") || person.equals("null")){
            holder.txt_person.setVisibility(View.GONE);
            holder.img_initiatedBy.setVisibility(View.GONE);
            holder.ln_contactperson.setVisibility(View.GONE);
        }else{
            holder.txt_person.setVisibility(View.VISIBLE);
            holder.img_initiatedBy.setVisibility(View.VISIBLE);
            holder.ln_contactperson.setVisibility(View.VISIBLE);
            holder.txt_person.setText(person);
        }

        if(initiatedBy == 1){
            holder.img_initiatedBy.setImageResource(R.drawable.crm_forward_arrow);
        }else if(initiatedBy == 2){
            holder.img_initiatedBy.setImageResource(R.drawable.crm_back_arrow);
        }else{
            holder.img_initiatedBy.setVisibility(View.GONE);
        }



        if (prospectName.equals(null) || prospectName.equals("null") || prospectName.equals("")) {
            prospectName = "-";
        }
        if (person.equals(null) || person.equals("null") || person.equals("")) {
            person = "-";
        }
        if (followupType.equals(null) || followupType.equals("null") || followupType.equals("")) {
            followupType = "";
        }
        if (outCome.equals(null) || outCome.equals("null") || outCome.equals("")) {
            outCome = "-";
        }
        if (reason.equals(null) || reason.equals("null") || reason.equals("")) {
            reason = "-";
        }
        if (time.equals(null) || time.equals("null") || time.equals("")) {
            time = "-";
        }
        if (duration.equals(null) || duration.equals("null") || duration.equals("")) {
            duration = "-";
        }

        String company = "\n" + "      Vritti Solutions Ltd      " + "\n\n";


        Spanned prospect = HtmlCompat.fromHtml(context, "<b>" + prospectName + "</b>", 0);

/*SARHAAN PETROLEUMTelephone by at 9:00 AM for duration of 03:30 with Riaz Mulani .Outcome is Call Again because of
'history notes'  and reason is -
Client busy*/
        String reasonappend = "";

        if (outCome.equalsIgnoreCase("Call Again") ||
                outCome.equalsIgnoreCase("Call Close without Order") ||
                outCome.equalsIgnoreCase("Order Received") ||
                outCome.equalsIgnoreCase("Reschedule") ||
                outCome.equalsIgnoreCase("Order Lost") ||
                outCome.equalsIgnoreCase("Demo Reschedule") ||
                outCome.equalsIgnoreCase("Transfer To BOE") ||
                outCome.equalsIgnoreCase("Customer will Call")) {
            reasonappend = " because of " + reason;
        } else {
            reasonappend = ".";
        }


        if (reason.equals("")) {
            details1 = "Outcome was " + outCome + reasonappend + "\n";
        } else {
            details1 = "Outcome was " + outCome + reasonappend + "\n";

        }
        holder.txt_reason.setText(details1);

     /*   StyleSpan boldStyle = new StyleSpan(Typeface.BOLD);
        SpannableStringBuilder sb1 = new SpannableStringBuilder(details1);
        int start = details1.indexOf(details1);
        int end = start + details1.length();
        sb1.setSpan(boldStyle, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/


      /*  try{
            String followupType = crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc();
            String firmName = crmDayEndReportDetailsBeanArrayList.get(position).getFirmname();
            String contactName = crmDayEndReportDetailsBeanArrayList.get(position).getContactName();
            String outCome = crmDayEndReportDetailsBeanArrayList.get(position).getOutcome();
            String reason = crmDayEndReportDetailsBeanArrayList.get(position).getReasonDescription();
            String historyNotes = crmDayEndReportDetailsBeanArrayList.get(position).getHistorynotes();

            if(!(followupType == null || followupType.equals("null"))){
                holder.txt_followupType.setText(crmDayEndReportDetailsBeanArrayList.get(position).getCallPurposeDesc());
            }else{
                holder.txt_followupType.setText("");
            }

            if(!(firmName == null || firmName.equals("null"))){
                holder.txt_title.setText(firmName);
            }else{
                holder.txt_title.setText("");
            }

            if(!(contactName == null || contactName.equals("null"))){
                holder.txt_person.setText(contactName);
            }else{
                holder.txt_person.setText("");
            }
            if(!(outCome == null || outCome.equals("null"))){
                holder.txt_outcome.setText(outCome);
            }else{
                holder.txt_outcome.setText("");
            }

            if(!(reason == null || reason.equals("null"))){
                holder.txt_reason.setText(reason);
            }else{
                holder.txt_reason.setText("");
            }

            if(!(historyNotes == null || historyNotes.equals("null"))){
                holder.txt_historyNotes.setText(historyNotes);
            }else{
                holder.txt_historyNotes.setText("");
            }




           *//* holder.txt_title.setText(crmDayEndReportDetailsBeanArrayList.get(position).getFirmname());
            holder.txt_person.setText(crmDayEndReportDetailsBeanArrayList.get(position).getContactName());
            holder.txt_outcome.setText(crmDayEndReportDetailsBeanArrayList.get(position).getOutcome());
            holder.txt_reason.setText(crmDayEndReportDetailsBeanArrayList.get(position).getReasonDescription());*//*

            String time = crmDayEndReportDetailsBeanArrayList.get(position).getSchTime();
            String duration = crmDayEndReportDetailsBeanArrayList.get(position).getTotalHoursSpent();
//"09:00
            String[] splitTime = time.split(":");
            int hours = Integer.parseInt(splitTime[0].trim());
            int min = Integer.parseInt(splitTime[1].trim());


            time = updateTime(hours,min);
            if(duration.equals("0") || duration.equals("00 : ")){
                duration = "00:00";
            }
            String concat = time +" / "+duration;
            if(concat != null) {
                holder.txt_duration.setText(concat);
            }else{
                holder.txt_duration.setText("00:00 / 00:00");
            }





        }catch (Exception e){
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return crmDayEndReportDetailsBeanArrayList.size();
    }

    public class ReportHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_followupType, txt_person, txt_outcome, txt_reason, txt_duration, txt_historyNotes,txt_time;
        ImageView img_followuptype,img_initiatedBy;
        LinearLayout ln_contactperson;

        public ReportHolder(View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            // txt_followupType = itemView.findViewById(R.id.txt_followupType);
            txt_person = itemView.findViewById(R.id.txt_person);
            txt_outcome = itemView.findViewById(R.id.txt_outcome);
            txt_reason = itemView.findViewById(R.id.txt_reason);
            txt_duration = itemView.findViewById(R.id.txt_duration);
            //txt_historyNotes = itemView.findViewById(R.id.txt_historyNote);
            img_followuptype = itemView.findViewById(R.id.img_followuptype);
            txt_time = itemView.findViewById(R.id.txt_time);
            img_initiatedBy = itemView.findViewById(R.id.img_initiatedBy);
            ln_contactperson = itemView.findViewById(R.id.ln_contactperson);
        }
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            if (hours > 12) {
                hours = hours - 12;
                timeSet = "AM";
            } else if (hours == 12) {
                timeSet = "AM";
            } else {
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
