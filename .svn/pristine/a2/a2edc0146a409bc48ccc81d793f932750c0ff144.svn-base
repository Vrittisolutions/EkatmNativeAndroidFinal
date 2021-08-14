package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.vwblib.Beans.NotificationBean;
import com.vritti.vwblib.R;


public class NotificationAdapter extends BaseAdapter {
    private static ArrayList<NotificationBean> notificationBeanArrayList;
    private LayoutInflater mInflater;
    Context context;

    public NotificationAdapter(Context context1, ArrayList<NotificationBean> lsBirthdayList1) {
        notificationBeanArrayList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return notificationBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_notification_list, null);
            holder = new ViewHolder();
            holder.headNotification = (TextView) convertView.findViewById(R.id.headNotification);
            holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
            holder.txtnotification1 = (TextView) convertView.findViewById(R.id.txtnotification1);
            holder.txtnotification2 = (TextView) convertView.findViewById(R.id.txtnotification2);
            holder.Expand = (ImageButton) convertView.findViewById(R.id.Expand);
            holder.lay1=(LinearLayout) convertView.findViewById(R.id.lay1);
            holder.lay2=(LinearLayout) convertView.findViewById(R.id.lay2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.headNotification.setText(notificationBeanArrayList.get(position).getNotifTitle());
        holder.txtdate.setText("Added By " + notificationBeanArrayList.get(position).getUserName()
                + "  " + notificationBeanArrayList.get(position).getAddedDt());
        holder.txtnotification1.setText(notificationBeanArrayList.get(position).getNotifText());
        holder.txtnotification2.setText(notificationBeanArrayList.get(position).getNotifText());
        holder.Expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.lay2.getVisibility() == View.VISIBLE) {
                    holder.lay1.setVisibility(View.VISIBLE);
                    holder.lay2.setVisibility(View.GONE);
                    holder.Expand.setBackground(context.getResources()
                            .getDrawable(R.drawable.expand));
                } else {
                    holder.lay1.setVisibility(View.GONE);
                    holder.lay2.setVisibility(View.VISIBLE);
                    holder.Expand.setBackground(context.getResources()
                            .getDrawable(R.drawable.collapse));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView headNotification, txtdate, txtnotification1, txtnotification2;
        ImageButton Expand;
        LinearLayout lay1, lay2;
    }
}
