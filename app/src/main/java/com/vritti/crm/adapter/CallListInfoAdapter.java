package com.vritti.crm.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.crm.bean.CallListdataInfo;
import com.vritti.crm.vcrm7.CallDetailsHistoryActivity;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 300151 on 10/13/16.
 */
public class CallListInfoAdapter extends BaseAdapter {
    List<CallListdataInfo> callListdataInfoList;
    LayoutInflater mInflater;
    Context context;

    public CallListInfoAdapter(Context context1, ArrayList<CallListdataInfo> callListdataInfoList) {
        this.callListdataInfoList = callListdataInfoList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return callListdataInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return callListdataInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_opencalllist_item_list_lay, null);
            holder = new ViewHolder();
            holder.txtFName = (TextView) convertView.findViewById(R.id.firmname);
            holder.txt_username = (TextView) convertView.findViewById(R.id.txt_username);
            holder.txtAddress = (TextView) convertView.findViewById(R.id.txt_address);
            holder.txt_reason = (TextView) convertView.findViewById(R.id.txt_reason);
            holder.txt_estimate = (TextView) convertView.findViewById(R.id.txt_estimate);
            holder.txt_outcome = (TextView) convertView.findViewById(R.id.txt_outcome);
            holder.len_outcome = (LinearLayout) convertView.findViewById(R.id.len_outcome);
            holder.actiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
            holder.txt_viewhistory = (TextView) convertView.findViewById(R.id.txt_viewhistory);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtFName.setText(callListdataInfoList.get(position).getFirmname());

        System.out.println("Firmname :" + callListdataInfoList.get(position).getFirmname());
        String cityname = callListdataInfoList.get(position).getCityname();
        String product = callListdataInfoList.get(position).getProductname();
        String contactname = callListdataInfoList.get(position).getContactName();

        String Fulladdress = cityname + " - " + product + " " + "(" + contactname + ")";

        holder.txtAddress.setText(Fulladdress);

        String LatestRemark=callListdataInfoList.get(position).getLatestRemark();

        System.out.println("Remark :"+LatestRemark);

        if (!LatestRemark.equalsIgnoreCase("")) {
            holder.txt_reason.setVisibility(View.VISIBLE);
            holder.txt_reason.setText(callListdataInfoList.get(position).getLatestRemark());
        }

        String Outcome,Username;
        Outcome=callListdataInfoList.get(position).getOutcome();
        Username=callListdataInfoList.get(position).getUsername();

       if (Outcome!=null && Username!=null) {
           holder.len_outcome.setVisibility(View.VISIBLE);
           holder.txt_outcome.setText(callListdataInfoList.get(position).getOutcome());
           holder.txt_username.setText(callListdataInfoList.get(position).getUsername());
       }

        holder.actiondatetime.setText(callListdataInfoList.get(position).getNextActionDateTime());




        holder.txt_viewhistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CallDetailsHistoryActivity.class);
                intent.putExtra("callid", callListdataInfoList.get(position).getCallId());
                intent.putExtra("firmname", callListdataInfoList.get(position).getFirmname());
                intent.putExtra("table", "Call");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

             return convertView;
    }

    static class ViewHolder {
        TextView txtFName, txt_username,actiondatetime;
        TextView txt_outcome, txtAddress, txt_reason, txt_estimate,txt_viewhistory;
        LinearLayout len_outcome;

    }
}
