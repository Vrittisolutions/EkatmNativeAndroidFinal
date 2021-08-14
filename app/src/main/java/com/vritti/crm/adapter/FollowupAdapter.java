package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.chat.activity.HistoryConversationActivity;
import com.vritti.crm.bean.Schedule;
import com.vritti.crm.classes.CallHistory;
import com.vritti.crm.vcrm7.FollowupActivity;
import com.vritti.crm.vcrm7.OpportunityActivity;
import com.vritti.ekatm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.vritti.crm.vcrm7.CountryListActivity.COUNTRY;


/**
 * Created by 300151 on 10/13/16.
 */
public class FollowupAdapter extends RecyclerView.Adapter<FollowupAdapter.callHolder> {
    ArrayList<Schedule> callHistoryArrayList;
    LayoutInflater mInflater;
    Context context;

    public FollowupAdapter(Context context1, ArrayList<Schedule> callHistoryArrayList) {
        this.callHistoryArrayList = callHistoryArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }


    @NonNull
    @Override
    public callHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.follow_schedule_time, parent, false);


        return new callHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowupAdapter.callHolder holder, @SuppressLint("RecyclerView") final int pos) {



        String formattedDate = formateDateFromstring("MM/dd/yyyy", "dd/MM/yyyy",callHistoryArrayList.get(pos).getTheDate());
        holder.txt_date.setText(formattedDate);
        holder.txt_day.setText(callHistoryArrayList.get(pos).getDOW());
        holder.txt_visit.setText(callHistoryArrayList.get(pos).getVisit());
        holder.txt_mail.setText(callHistoryArrayList.get(pos).getEmail());
        holder.txt_call.setText(callHistoryArrayList.get(pos).getTelephone());
        if (callHistoryArrayList.get(pos).getVisitPlan().equals("")){
            holder.fromPlace.setText("No visit plan available");
        }else {
            holder.fromPlace.setText(callHistoryArrayList.get(pos).getVisitPlan());
        }
        if (callHistoryArrayList.get(pos).getTravelPlan().equals("")){
            holder.txt_visit_plan.setText("No travel plan available");
        }else {
            holder.txt_visit_plan.setText(callHistoryArrayList.get(pos).getTravelPlan());
        }

        holder.cardView.setPreventCornerOverlap(false);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formattedDate = formateDateFromstring("MM/dd/yyyy", "dd/MM/yyyy",callHistoryArrayList.get(pos).getTheDate());

                ((FollowupActivity)context).ActionClick(formattedDate);

            }
        });
    }


    @Override
    public int getItemCount() {
        return callHistoryArrayList.size();
    }

    public class callHolder extends RecyclerView.ViewHolder {
        TextView txt_date,txt_mail,txt_call,txt_visit,fromPlace,txt_visit_plan,txt_day;
        CardView cardView;
        ImageView img_followuptype;

        public callHolder(View convertView) {
            super(convertView);
            cardView = convertView.findViewById(R.id.cardView);
            txt_date = convertView.findViewById(R.id.txt_date);
            txt_mail = convertView.findViewById(R.id.txt_mail);
            txt_call = convertView.findViewById(R.id.txt_call);
            txt_visit = convertView.findViewById(R.id.txt_visit);
            fromPlace = convertView.findViewById(R.id.fromPlace);
            txt_visit_plan = convertView.findViewById(R.id.txt_visit_plan);
            txt_day = convertView.findViewById(R.id.txt_day);
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
