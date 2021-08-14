package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.EnquiryBean;
import com.vritti.ekatm.R;

import java.util.ArrayList;

public class EnquiryAdapter extends BaseAdapter {
    private static ArrayList<EnquiryBean> EnquiryList;
    private LayoutInflater mInflater;
    Context context;

    public EnquiryAdapter(Context context1, ArrayList<EnquiryBean> lsBirthdayList1) {
        EnquiryList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return EnquiryList.size();
    }

    @Override
    public Object getItem(int position) {
        return EnquiryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_customenquiryitem, null);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.txtname);
            holder.txtmobile = (TextView) convertView.findViewById(R.id.txtmobile);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtname.setText(EnquiryList.get(position).getCustomerName());
        holder.txtmobile.setText(EnquiryList.get(position).getContactNumber());

        return convertView;
    }

    static class ViewHolder {
        TextView txtname, txtmobile;

    }
}
