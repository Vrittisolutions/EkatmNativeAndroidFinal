package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.ChargeDtlBean;
import com.vritti.sales.beans.SaleItemBean;

import java.util.ArrayList;

public class SOHistoryListAdapter extends BaseAdapter {
    private static ArrayList<SaleItemBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<SaleItemBean> arraylist;
    String key = "";

    public SOHistoryListAdapter(Context parent, ArrayList<SaleItemBean> soList) {
        this.parent = parent;
        this.list = soList;
        arraylist = new ArrayList<SaleItemBean>();
        arraylist.addAll(soList);
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.so_history_list, null);
            holder = new ViewHolder();

            holder.txtsono = (TextView) convertView.findViewById(R.id.txtsono);
            holder.txtcust = convertView.findViewById(R.id.txtcust);
            holder.txtsodate = convertView.findViewById(R.id.txtsodate);
            holder.txtamt = convertView.findViewById(R.id.txtamt);
            holder.layout = convertView.findViewById(R.id.layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtsono.setText(arraylist.get(position).getSoNo());
        holder.txtcust.setText(arraylist.get(position).getCustName());
        holder.txtsodate.setText(arraylist.get(position).getSoDate());
        holder.txtamt.setText(arraylist.get(position).getAmt());

        if(position%2 == 1){
            holder.layout.setBackgroundColor(Color.parseColor("#EDEDED"));
        }else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return convertView;
    }

    class ViewHolder{
        TextView txtsono,txtcust,txtsodate,txtamt;
        LinearLayout layout;
    }

}
