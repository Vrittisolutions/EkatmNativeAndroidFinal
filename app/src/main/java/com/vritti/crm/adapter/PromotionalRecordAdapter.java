package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.PromotionalracordBean;
import com.vritti.ekatm.R;

import java.util.ArrayList;

public class PromotionalRecordAdapter extends BaseAdapter {
    private static ArrayList<PromotionalracordBean> recordlist;
    private LayoutInflater mInflater;
    Context context;

    public PromotionalRecordAdapter(Context context1, ArrayList<PromotionalracordBean> lsBirthdayList1) {
        recordlist = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return recordlist.size();
    }

    @Override
    public Object getItem(int position) {
        return recordlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_itempromotionalrecord, null);
            holder = new ViewHolder();
            holder.txtpromotorid = (TextView) convertView.findViewById(R.id.txtpromoterID);
            holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
            holder.txtlocation = (TextView) convertView.findViewById(R.id.txtlocation);
            holder.txtd2dchilltaste = (TextView) convertView.findViewById(R.id.txtd2dchilltest);
            holder.txtsamplegiven = (TextView) convertView.findViewById(R.id.txtsamplegiven);
            holder.txthatchilltaste = (TextView) convertView.findViewById(R.id.txthatchilltaste);
            holder.txtsaleamt = (TextView) convertView.findViewById(R.id.txtsaleAmount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String d = recordlist.get(position).getUserLoginId();
        holder.txtpromotorid.setText(recordlist.get(position).getUserLoginId());
        holder.txtdate.setText(recordlist.get(position).getDt());
        holder.txtlocation.setText(recordlist.get(position).getLocation());
        holder.txtd2dchilltaste.setText(recordlist.get(position).getD2DChillTaste());
        holder.txtsamplegiven.setText(recordlist.get(position).getSampleGiven());
        holder.txthatchilltaste.setText(recordlist.get(position).getHChillTaste());
        holder.txtsaleamt.setText(recordlist.get(position).getSaleamount());

        return convertView;
    }

    static class ViewHolder {
        TextView txtpromotorid, txtdate, txtlocation, txtd2dchilltaste, txthatchilltaste, txtsaleamt, txtsamplegiven;

    }
}