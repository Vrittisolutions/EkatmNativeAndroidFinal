package com.vritti.crmlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CustomerListBean;

public class AdapterCustmerList extends BaseAdapter {
    ArrayList<CustomerListBean> custArrayList;
    LayoutInflater mInflater;
    Context context;
    ArrayList<CustomerListBean> arraylist;

    public AdapterCustmerList(Context context1, ArrayList<CustomerListBean> chatUserArrayList) {
        this.custArrayList = chatUserArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<CustomerListBean>();
        this.arraylist.addAll(chatUserArrayList);

    }

    @Override
    public int getCount() {
        return custArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return custArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ChatUserlistAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_item_cust_list, null);
            holder = new ChatUserlistAdapter.ViewHolder();
            holder.txt_user_name_data = (TextView) convertView.findViewById(R.id.user_name_data);

            convertView.setTag(holder);
        } else {
            holder = (ChatUserlistAdapter.ViewHolder) convertView.getTag();
        }

        holder.txt_user_name_data.setText(custArrayList.get(position).getCustVendorName());





        return convertView;
    }

   /* public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        chatUserArrayList.clear();
        if (charText.length() == 0) {
            chatUserArrayList.addAll(arraylist);
        }
        else
        {
            for (ChatUser wp : arraylist)
            {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    chatUserArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    public ArrayList<CustomerListBean> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        custArrayList.clear();
        if (charText.length() == 0) {
            custArrayList.addAll(arraylist);
        }
        else
        {
            for (CustomerListBean wp : arraylist)
            {
                if (wp.getCustVendorName().toLowerCase(Locale.getDefault()).startsWith(charText))
                {
                    custArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return custArrayList;
    }


    static class ViewHolder {
        TextView txt_user_name_data;

    }
}