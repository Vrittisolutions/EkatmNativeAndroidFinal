package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.TaxClassBean;

import java.util.ArrayList;
import java.util.Locale;

public class ItemCodeDescAdapter extends BaseAdapter {
    private static ArrayList<CounterbillingBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<CounterbillingBean> arraylist;
    String key = "";

    public ItemCodeDescAdapter(Context parent, ArrayList<CounterbillingBean> custSelectionList, String key) {
        this.parent = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<CounterbillingBean>();
        arraylist.addAll(custSelectionList);
        this.key = key;

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
            convertView = mInflater.inflate(R.layout.tbuds_customer_selection,
                    null);
            holder = new ViewHolder();

            holder.txtCustomer = (TextView) convertView.findViewById(R.id.txtCustomer);
            holder.txtinitial = (TextView) convertView.findViewById(R.id.txtinitial);
            holder.imgicon = convertView.findViewById(R.id.imgicon);
            holder.imgicon.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = "", initial = "";
        try{
            if(key.equalsIgnoreCase("ItemCode")){
                name = list.get(position).getItemCode().toUpperCase();
            }else if(key.equalsIgnoreCase("ItemDesc")){
                name = list.get(position).getItemDesc().toUpperCase();
            }

             initial = String.valueOf(name.charAt(0));
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.txtinitial.setText(initial);

        if(key.equalsIgnoreCase("ItemCode")){
            holder.txtCustomer.setText(list.get(position).getItemCode());
        }else if(key.equalsIgnoreCase("ItemDesc")){
            holder.txtCustomer.setText(list.get(position).getItemDesc());
        }

        return convertView;
    }

    class ViewHolder{
        TextView txtCustomer,txtinitial;
        LinearLayout imgicon;
    }

    public void filter_code(String charText, String key) {

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (CounterbillingBean wp : arraylist) {
                //if (wp.getTaxClassDesc().toLowerCase(Locale.getDefault()).contains(charText)) {

                if(key.equalsIgnoreCase("ItemCode")){
                    if (wp.getItemCode().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        list.add(wp);
                    }
                }else if(key.equalsIgnoreCase("ItemDesc")){
                    if (wp.getItemDesc().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        list.add(wp);
                    }
                }

            }
        }
        notifyDataSetChanged();
    }

}
