package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.vwb.Beans.AffectedCustomer;
import com.vritti.ekatm.R;

import java.util.ArrayList;


/**
 * Created by 300151 on 10/13/16.
 */
public class AdapterAffectedCustomer extends BaseAdapter {
    ArrayList<AffectedCustomer> affectedCustomerArrayList;
    LayoutInflater mInflater;
    Context context;

    public AdapterAffectedCustomer(Context context1, ArrayList<AffectedCustomer> affectedCustomerArrayList) {
        this.affectedCustomerArrayList = affectedCustomerArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return affectedCustomerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return affectedCustomerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_affected_customer_lay, null);
            holder = new ViewHolder();
            holder.txt_ticket_no = (TextView) convertView.findViewById(R.id.txt_ticket_no);
            holder.txt_client_name = (TextView) convertView.findViewById(R.id.txt_client_name);
            holder.txt_reported_by= (TextView) convertView.findViewById(R.id.txt_reported_by);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_ticket_no.setText(affectedCustomerArrayList.get(position).getCustTicketNo());
        holder.txt_client_name.setText(affectedCustomerArrayList.get(position).getActivityName());
        holder.txt_reported_by.setText(affectedCustomerArrayList.get(position).getReportedBy());
       return convertView;
    }

    static class ViewHolder {
        TextView txt_ticket_no, txt_client_name,txt_reported_by;
    }
}
