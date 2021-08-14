package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;

import com.vritti.vwb.Beans.CommonData;
import com.vritti.ekatm.R;


/**
 * Created by 300151 on 10/13/16.
 */
public class ProductAdapter extends BaseAdapter {
    ArrayList<CommonData>commonDataArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public ProductAdapter(Context context1, ArrayList<CommonData> commonDataArrayList) {
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
            convertView = mInflater.inflate(R.layout.vwb_custom_spinner_txt, null);
            holder = new ViewHolder();
            holder.txt_product = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_product.setText(commonDataArrayList.get(position).getItemDesc());

        return convertView;
    }

    static class ViewHolder {
        TextView txt_product;
    }
}
