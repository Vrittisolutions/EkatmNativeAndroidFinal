package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.vwb.Beans.LeaveBean;
import com.vritti.ekatm.R;

/**
 * Created by Admin-1 on 6/7/2017.
 */

public class LeaveSummaryAdapter extends BaseAdapter {
    private static ArrayList<LeaveBean> lsList;
    private LayoutInflater mInflater;
    Context context;

    public LeaveSummaryAdapter(Context context1, ArrayList<LeaveBean> lsList1) {
        lsList = lsList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return lsList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_summary_list, null);
            holder = new ViewHolder();
            holder.tv_balanced = (TextView) convertView.findViewById(R.id.lv_available);
            holder.tv_credited = (TextView) convertView.findViewById(R.id.lv_Credited);
            holder.tv_consumed = (TextView) convertView.findViewById(R.id.lv_consumed);
            holder.tv_opening = (TextView) convertView.findViewById(R.id.lv_opening);
            holder.tv_leavetype = (TextView) convertView.findViewById(R.id.lv_leavetyoe);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_balanced.setText(lsList.get(position).getBalance());
        holder.tv_credited.setText(lsList.get(position).getCredit());
        holder.tv_consumed.setText(lsList.get(position).getConsumed());
        holder.tv_opening.setText(lsList.get(position).getOpenBal());
        holder.tv_leavetype.setText(lsList.get(position).getLeaveCode());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_balanced,tv_credited,tv_consumed,tv_opening,tv_leavetype;

    }
}

