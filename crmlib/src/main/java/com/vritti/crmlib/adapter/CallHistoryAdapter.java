package com.vritti.crmlib.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.chat.HistoryConversationActivity;
import com.vritti.crmlib.classes.CallHistory;


/**
 * Created by 300151 on 10/13/16.
 */
public class CallHistoryAdapter extends BaseAdapter {
    ArrayList<CallHistory> callHistoryArrayList;
    LayoutInflater mInflater;
    Context context;

    public CallHistoryAdapter(Context context1, ArrayList<CallHistory> callHistoryArrayList) {
        this.callHistoryArrayList = callHistoryArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return callHistoryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return callHistoryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_updated_call_history_lay, null);
            holder = new ViewHolder();
            holder.txt_call_datetime = (TextView) convertView.findViewById(R.id.txt_call_datetime);
            holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
            holder.txt_full_message= (TextView) convertView.findViewById(R.id.txt_full_message);
            holder.txt_chat= (TextView) convertView.findViewById(R.id.txt_chat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txt_call_datetime.setText(callHistoryArrayList.get(position).getModifiedDt());
        holder.txt_username.setText(callHistoryArrayList.get(position).getUserName());

        holder.txt_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Call_id=callHistoryArrayList.get(position).getCallId();
                Intent intent=new Intent(context, HistoryConversationActivity.class);
                intent.putExtra("callid",Call_id);
                context.startActivity(intent);
            }
        });

        String outcomme=callHistoryArrayList.get(position).getOutcome();



        if(outcomme.equalsIgnoreCase("Appointment")){
            holder.txt_full_message.setText(outcomme+" "+"fixed with "+" "+callHistoryArrayList.get(position).getContact()+ " on "+callHistoryArrayList.get(position).getNextActionDateTime()+" "+" for "+callHistoryArrayList.get(position).getPurpose());
        }
        if(outcomme.equalsIgnoreCase("Call Again")){
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




      /*  holder.txt_assignedby.setText(callHistoryArrayList.get(position).getUserName());
        holder.txt_followup.setText(callHistoryArrayList.get(position).getActionType());
        holder.txt_withwhoom.setText(callHistoryArrayList.get(position).getContact());
        holder.txt_purpose.setText(callHistoryArrayList.get(position).getPurpose());
        holder.txt_outcome.setText(callHistoryArrayList.get(position).getOutcome());
        holder.txt_nextaction.setText(callHistoryArrayList.get(position).getNextAction());
        holder.txt_next_datime.setText(callHistoryArrayList.get(position).getNextActionDateTime());
        holder.txt_user_remark.setText(callHistoryArrayList.get(position).getLatestRemark());*/
        return convertView;
    }

    static class ViewHolder {
        TextView txt_call_datetime, txt_username,txt_full_message,txt_chat;
    }
}
