package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.Customer;

import java.util.ArrayList;
import java.util.Locale;

import static org.apache.commons.lang3.StringUtils.split;

public class CustomerSelectionListAdapter extends BaseAdapter {
    private static ArrayList<Customer> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<Customer> arraylist;

    public CustomerSelectionListAdapter(Context parent, ArrayList<Customer> custSelectionList) {
        this.parent = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<Customer>();
        arraylist.addAll(custSelectionList);

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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = "", initial = "";
        try{
             name = list.get(position).getCustomer_name().toUpperCase();
             initial = String.valueOf(name.charAt(0));
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.txtinitial.setText(initial);
        holder.txtCustomer.setText(list.get(position).getCustomer_name());

        return convertView;
    }

    class ViewHolder{
        TextView txtCustomer,txtinitial;
        ImageView imgicon;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (Customer wp : arraylist) {
                if (wp.getCustomer_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                //if (wp.getCustomer_name().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
