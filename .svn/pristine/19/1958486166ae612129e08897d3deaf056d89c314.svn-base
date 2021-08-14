package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.PendingBillReport;

import java.util.ArrayList;

/**
 * Created by sharvari on 8/31/2016.
 */
public class CustomerReceivableAdapter extends BaseAdapter {
    private ArrayList<PendingBillReport> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
    PendingBillReport pendingBillReport;

    public CustomerReceivableAdapter(Context context,
                                     ArrayList<PendingBillReport> list) {
        parent = context;
        arrayList = list;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        if (convertView == null) {


            convertView = mInflater.inflate(R.layout.tbuds_custom_balance_grid, null);
            holder = new ViewHolder();

            holder.txtmob = (TextView) convertView
                    .findViewById(R.id.txtmob);
            holder.txtamt = (TextView) convertView
                    .findViewById(R.id.txtamt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (arrayList.get(position).getTotalPnding() == 0) {

        } else {
            holder.txtmob.setText("Mobile No. :" + arrayList.get(position).getCust_mob());
            holder.txtamt.setText("Receivable : " + arrayList.get(position).getTotalPnding());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView txtmob, txtamt;


    }


}
