package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.BusinessInformation;
import com.vritti.vwb.classes.Item;
import com.vritti.vwb.vworkbench.ActvityNotificationTypeActivity;

import java.util.ArrayList;


public class BusinessNotificationTypeAdapter extends BaseAdapter {
     ArrayList<BusinessInformation> itemArrayList;
    LayoutInflater mInflater;
    Context context;

    public BusinessNotificationTypeAdapter(Context context1, ArrayList<BusinessInformation> itemArrayList) {
        this.itemArrayList = itemArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.business_notification_type_item_lay, null);
            holder = new ViewHolder();
            holder.txt_notification = (TextView) convertView.findViewById(R.id.txt_notification);
            holder.read_count = (TextView) convertView.findViewById(R.id.read_count);
            holder.unread_count = (ImageView) convertView.findViewById(R.id.unread_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_notification.setText(itemArrayList.get(position).getTypeName());
        holder.read_count.setText(itemArrayList.get(position).getTotalNotification());
     //   holder.unread_count.setText(itemArrayList.get(position).getNotRead());





        return convertView;
    }

    static class ViewHolder {
        private TextView txt_notification,read_count;
                ImageView unread_count;
        LinearLayout len_read,len_unread;
        CardView cardView;
    }


}
