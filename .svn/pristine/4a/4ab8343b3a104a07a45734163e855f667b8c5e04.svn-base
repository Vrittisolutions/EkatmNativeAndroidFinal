package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.vwblib.Beans.Customer;
import com.vritti.vwblib.R;

import java.util.ArrayList;


/**
 * Created by 300151 on 10/13/16.
 */
public class CustomerAdapter extends BaseAdapter {
    ArrayList<Customer> customerArrayList;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public CustomerAdapter(Context context1, ArrayList<Customer> customerArrayList) {
        this.customerArrayList = customerArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        }

    @Override
    public int getCount() {
        return customerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return customerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_spinner_txt, null);
            holder = new ViewHolder();
            holder.txt_attachment = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_attachment.setText(customerArrayList.get(position).getCustomer_name());

        return convertView;
    }

    static class ViewHolder {
        TextView txt_attachment;
    }
}
