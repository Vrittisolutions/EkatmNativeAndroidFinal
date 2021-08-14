package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.chat.activity.HistoryConversationActivity;
import com.vritti.crm.classes.CallHistory;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by 300151 on 10/13/16.
 */
public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.callHolder> {
    ArrayList<CallHistory> callHistoryArrayList;
    LayoutInflater mInflater;
    Context context;

    public CallHistoryAdapter(Context context1, ArrayList<CallHistory> callHistoryArrayList) {
        this.callHistoryArrayList = callHistoryArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }


    @NonNull
    @Override
    public callHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crm_updated_call_history_lay, parent, false);


        return new callHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CallHistoryAdapter.callHolder holder, @SuppressLint("RecyclerView") final int pos) {
        try {
            String call_dateTime = callHistoryArrayList.get(pos).getModifiedDt().trim();
            String[] callSplit = call_dateTime.split(" ");


            holder.txt_next_action.setText(callSplit[0] + " " + callSplit[1] + " " + callSplit[2]);
            holder.txt_next_time.setText(callSplit[3]);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        String username = "",toWhom="",purpose="",outcome="",nextActionDateTime="",nextActionRemark="",nextActionPlan="",actionType="";
        username= callHistoryArrayList.get(pos).getUserName();
        toWhom = callHistoryArrayList.get(pos).getContact();
        purpose = callHistoryArrayList.get(pos).getPurpose();
        outcome = callHistoryArrayList.get(pos).getOutcome();
        nextActionDateTime =  callHistoryArrayList.get(pos).getNextActionDateTime();
        nextActionRemark =  callHistoryArrayList.get(pos).getLatestRemark();
        actionType =  callHistoryArrayList.get(pos).getActionType();

        if(callHistoryArrayList.get(pos).getNextAction().equalsIgnoreCase("")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }else if(callHistoryArrayList.get(pos).getNextAction().equalsIgnoreCase("Telephone")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }else if(callHistoryArrayList.get(pos).getNextAction().equalsIgnoreCase("Email")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.email_icon_white));
        }else if(callHistoryArrayList.get(pos).getNextAction().equalsIgnoreCase("visit")){
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.visit));
        }else{
            holder.img_followuptype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }
        StringBuilder appendline1 = new StringBuilder();

        if(purpose.equals("") || purpose.equals("-")){
            purpose=".";
        }else{
            purpose = "for "+purpose+".";
        }
        appendline1.append(username+" "+purpose);
        holder.txt_username.setText(appendline1);

        StringBuilder appendline2 = new StringBuilder();
        if(outcome.equals("")){

        }else{
            if(outcome.equals("reassigned") || outcome.equals("transfer")){
                if(nextActionRemark.equals("")) {

                    outcome = "Outcome was " + outcome + " reassigned to " + toWhom+".";
                }else{
                    outcome = "Outcome was " + outcome + " reassigned to " + toWhom +" because "+nextActionRemark+".";
                }
            }else{
                if(nextActionRemark.equals("")) {
                    outcome = "Outcome was "+outcome+".";
                }else{
                    outcome = "Outcome was "+outcome +" because "+nextActionRemark+".";
                }
            }

        }
        holder.txt_outcome.setText(outcome);
        holder.txt_remark.setText(nextActionRemark);
        if(nextActionDateTime.equals("")){
            if(actionType.equals("")){
                nextActionPlan = "Next Action required is on "+nextActionDateTime+".";
            }else {
                nextActionPlan = "Next Action required is " + actionType+".";
            }
        }else{
            if(actionType.equals("")){
                nextActionPlan = "Next Action required is on "+nextActionDateTime+".";
            }else {
                nextActionPlan = "Next Action required is "+actionType+" on "+nextActionDateTime+".";
            }

        }

        holder.txt_nextActionplan.setText(nextActionPlan);





/*

        holder.txt_username.setText(*/
        /*"Followup by : " +*//*
 callHistoryArrayList.get(pos).getUserName());
        //holder.txt_follow_type.setText("Followup Type : " + callHistoryArrayList.get(pos).getActionType());
        holder.txt_with_whom.setText("With Whom : " + callHistoryArrayList.get(pos).getContact());
        holder.txt_purpose.setText("Purpose : " + callHistoryArrayList.get(pos).getPurpose());
        holder.txt_outcome.setText("Outcome : " + callHistoryArrayList.get(pos).getOutcome());
        String nextactiondate = callHistoryArrayList.get(pos).getNextActionDateTime();
        String[] nextactiondt = new String[1];

        if (!nextactiondate.equals("")) {
            nextactiondt = nextactiondate.split(" ");
        }

        holder.txt_next_action.setText(nextactiondt[0] + " " + nextactiondt[1]);
        holder.txt_next_time.setText(nextactiondt[2]);


        // holder.txt_nextdatetime.setText(callHistoryArrayList.get(pos).getNextActionDateTime());

*/

        /*if (pos % 2 == 1) {
            holder.cardView.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            holder.cardView.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }*/
        holder.txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Call_id = callHistoryArrayList.get(pos).getCallId();
                Intent intent = new Intent(context, HistoryConversationActivity.class);
                intent.putExtra("callid", Call_id);
                context.startActivity(intent);
            }
        });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return callHistoryArrayList.size();
    }

    public class callHolder extends RecyclerView.ViewHolder {
        TextView txt_call_datetime, txt_username, txt_full_message, txt_chat,txt_nextActionplan,
                txt_follow_type/*txt_nextdatetime*/, txt_next_action, txt_outcome, txt_purpose, txt_with_whom, txt_next_time,txt_remark;
        CardView cardView;
        ImageView img_followuptype;

        public callHolder(View convertView) {
            super(convertView);
            // txt_call_datetime = (TextView) convertView.findViewById(R.id.txt_call_datetime);
            txt_username = (TextView) convertView.findViewById(R.id.txt_username);
            //txt_follow_type= (TextView) convertView.findViewById(R.id.txt_follow_type);
            //  txt_nextdatetime= (TextView) convertView.findViewById(R.id.txt_nextdatetime);
            txt_next_action = (TextView) convertView.findViewById(R.id.txt_next_action);
            txt_next_time = (TextView) convertView.findViewById(R.id.txt_next_time);
            txt_outcome = (TextView) convertView.findViewById(R.id.txt_outcome);
            txt_purpose = (TextView) convertView.findViewById(R.id.txt_purpose);
            txt_with_whom = (TextView) convertView.findViewById(R.id.txt_with_whom);
            txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
            cardView = convertView.findViewById(R.id.cardView);
            img_followuptype = convertView.findViewById(R.id.img_followuptype);
            txt_nextActionplan = convertView.findViewById(R.id.txt_nextActionplan);
            txt_remark = convertView.findViewById(R.id.txt_remark);
        }
    }

  /*  @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_updated_call_history_lay, null);
            holder = new ViewHolder();
            holder.txt_call_datetime = (TextView) convertView.findViewById(R.id.txt_call_datetime);
            holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
            holder.txt_follow_type= (TextView) convertView.findViewById(R.id.txt_follow_type);
            holder.txt_nextdatetime= (TextView) convertView.findViewById(R.id.txt_nextdatetime);
            holder.txt_next_action= (TextView) convertView.findViewById(R.id.txt_next_action);
            holder.txt_outcome= (TextView) convertView.findViewById(R.id.txt_outcome);
            holder.txt_purpose= (TextView) convertView.findViewById(R.id.txt_purpose);
            holder.txt_with_whom= (TextView) convertView.findViewById(R.id.txt_with_whom);

            holder.txt_chat= (TextView) convertView.findViewById(R.id.txt_chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txt_call_datetime.setText(callHistoryArrayList.get(position).getModifiedDt());
        holder.txt_username.setText("Followup by : " + callHistoryArrayList.get(position).getUserName());
        holder.txt_follow_type.setText("Followup Type : " + callHistoryArrayList.get(position).getActionType());
        holder.txt_with_whom.setText("Whom : " + callHistoryArrayList.get(position).getContact());
        holder.txt_purpose.setText("Purpose : " + callHistoryArrayList.get(position).getPurpose());
        holder.txt_outcome.setText("Outcome : " + callHistoryArrayList.get(position).getOutcome());
        holder.txt_next_action.setText("Next action : " + callHistoryArrayList.get(position).getNextAction());
        holder.txt_nextdatetime.setText(callHistoryArrayList.get(position).getNextActionDateTime());

        holder.txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Call_id = callHistoryArrayList.get(position).getCallId();
                Intent intent=new Intent(context, HistoryConversationActivity.class);
                intent.putExtra("callid",Call_id);
                context.startActivity(intent);
            }
        });





       *//* if(outcomme.equalsIgnoreCase("Appointment")){
            holder.txt_full_message.setText(outcomme+" "+"fixed with "+" "+callHistoryArrayList.get(position).getContact()+ " on "+callHistoryArrayList.get(position).getNextActionDateTime()+" "+" for "+callHistoryArrayList.get(position).getPurpose());
        }
        if(outcomme.equalsIgnoreCase("CA")){
            String callpurpose=callHistoryArrayList.get(position).getPurpose();
            if(callpurpose.equalsIgnoreCase("null"))
            {
                callpurpose=callHistoryArrayList.get(position).getLatestRemark();
            }else {
                callpurpose=callHistoryArrayList.get(position).getPurpose();
            }
            holder.txt_full_message.setText(callpurpose+" "+ " so call is scheduled again on " +callHistoryArrayList.get(position).getNextActionDateTime());
        }
        if (outcomme.equalsIgnoreCase("Transfer To SE")){
            holder.txt_full_message.setText("The call has been transferd to " +callHistoryArrayList.get(position).getUserName()+ " on "+callHistoryArrayList.get(position).getNextActionDateTime());
        }

        if (outcomme.equalsIgnoreCase("Transfer To BOE")){
            holder.txt_full_message.setText("The call has been reassign to " +callHistoryArrayList.get(position).getUserName()+ " on "+callHistoryArrayList.get(position).getNextActionDateTime());
        }
        if(outcomme.equalsIgnoreCase("Call Close without order")){
            String callpurpose=callHistoryArrayList.get(position).getPurpose();
            if(callpurpose.equalsIgnoreCase("null"))
            {
                callpurpose=callHistoryArrayList.get(position).getLatestRemark();
            }else {
                callpurpose=callHistoryArrayList.get(position).getPurpose();
            }
            holder.txt_full_message.setText("The call has been closed without order due to "+ callpurpose + " on " +callHistoryArrayList.get(position).getNextActionDateTime());
        }

        if(outcomme.equalsIgnoreCase("Demo Completed")){
            holder.txt_full_message.setText("Demo has been completed on " +callHistoryArrayList.get(position).getNextActionDateTime());
        }

        if(outcomme.equalsIgnoreCase("Demo Reschedule")){
            String callpurpose=callHistoryArrayList.get(position).getPurpose();
            if(callpurpose.equalsIgnoreCase("null"))
            {
                callpurpose=callHistoryArrayList.get(position).getLatestRemark();
            }else {
                callpurpose=callHistoryArrayList.get(position).getPurpose();
            }
            holder.txt_full_message.setText(" Demo has been rescheduled due to "+ callpurpose + " and it will be given by "+callHistoryArrayList.get(position).getUserName()+" on " +callHistoryArrayList.get(position).getNextActionDateTime());
        }

        if(outcomme.equalsIgnoreCase("Order Lost")){
            String callpurpose=callHistoryArrayList.get(position).getPurpose();
            if(callpurpose.equalsIgnoreCase("null"))
            {
                callpurpose=callHistoryArrayList.get(position).getLatestRemark();
            }else {
                callpurpose=callHistoryArrayList.get(position).getPurpose();
            }
            holder.txt_full_message.setText("Order has been lost due to "+ callpurpose + " on "+callHistoryArrayList.get(position).getNextActionDateTime());
        }

        if(outcomme.equalsIgnoreCase("Reschedule")){
            String callpurpose=callHistoryArrayList.get(position).getPurpose();
            if(callpurpose.equalsIgnoreCase("null"))
            {
                callpurpose=callHistoryArrayList.get(position).getLatestRemark();
            }else {
                callpurpose=callHistoryArrayList.get(position).getPurpose();
            }
            holder.txt_full_message.setText(" Call has been rescheduled on "+ callHistoryArrayList.get(position).getNextActionDateTime() +" due to "+callpurpose);
        }


        if (outcomme.equalsIgnoreCase("Visit")){
            holder.txt_full_message.setText( callHistoryArrayList.get(position).getUserName() +" will Visit on "+ callHistoryArrayList.get(position).getNextActionDateTime());

        }
        if (outcomme.equalsIgnoreCase("Demo Request")){
            holder.txt_full_message.setText(callHistoryArrayList.get(position).getUserName() +" has been requested you to give demo on "+ callHistoryArrayList.get(position).getPurpose()+" on "+ callHistoryArrayList.get(position).getNextActionDateTime());

        }
*//*



     *//*  holder.txt_assignedby.setText(callHistoryArrayList.get(position).getUserName());
        holder.txt_followup.setText(callHistoryArrayList.get(position).getActionType());
        holder.txt_withwhoom.setText(callHistoryArrayList.get(position).getContact());
        holder.txt_purpose.setText(callHistoryArrayList.get(position).getPurpose());
        holder.txt_outcome.setText(callHistoryArrayList.get(position).getOutcome());
        holder.txt_nextaction.setText(callHistoryArrayList.get(position).getNextAction());
        holder.txt_next_datime.setText(callHistoryArrayList.get(position).getNextActionDateTime());
        holder.txt_user_remark.setText(callHistoryArrayList.get(position).getLatestRemark());*//*
        return convertView;
    }*/

    static class ViewHolder {
        TextView txt_call_datetime, txt_username, txt_full_message, txt_chat,
                txt_follow_type, txt_nextdatetime, txt_next_action, txt_outcome, txt_purpose, txt_with_whom;
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
