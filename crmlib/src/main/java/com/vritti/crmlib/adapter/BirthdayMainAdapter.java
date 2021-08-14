package com.vritti.crmlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.BirthdayBean;


/**
 * Created by 300151 on 10/13/16.
 */
public class BirthdayMainAdapter extends BaseAdapter {
    private static ArrayList<BirthdayBean> lsBirthdayList;
    private LayoutInflater mInflater;
    Context context;

    public BirthdayMainAdapter(Context context1, ArrayList<BirthdayBean> lsBirthdayList1) {
        lsBirthdayList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return lsBirthdayList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsBirthdayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_custom_birthday_list, null);
            holder = new ViewHolder();
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.tv_bod = (TextView) convertView.findViewById(R.id.tv_bod);
            holder.img_user = (ImageView) convertView.findViewById(R.id.img_user);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_userName.setText(lsBirthdayList.get(position).getUserName());
        holder.tv_bod.setText(lsBirthdayList.get(position).getDtDay());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_userName, tv_bod;
        ImageView img_user;
    }
}
