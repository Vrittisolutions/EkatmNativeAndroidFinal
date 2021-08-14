package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.vwblib.Beans.CommonData;
import com.vritti.vwblib.R;


/**
 * Created by 300151 on 10/13/16.
 */
public class ProductListAdapter extends BaseAdapter {
    ArrayList<CommonData>commonDataArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public ProductListAdapter(Context context1, ArrayList<CommonData> commonDataArrayList) {
        this.commonDataArrayList = commonDataArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;


    }

    @Override
    public int getCount() {
        return commonDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return commonDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_spinner_txt_1, null);
            holder = new ViewHolder();
            holder.txt_product = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (commonDataArrayList.get(position).getIsExpired().equals("1")){
            holder.txt_product.setText(commonDataArrayList.get(position).getItemDesc());
            holder.txt_product.setTextColor(Color.RED);
            holder.txt_product.setEnabled(true);

        }else {
            holder.txt_product.setText(commonDataArrayList.get(position).getItemDesc());
            holder.txt_product.setTextColor(Color.BLACK);

        }

        return convertView;
    }

    static class ViewHolder {
        TextView txt_product;
    }
}
