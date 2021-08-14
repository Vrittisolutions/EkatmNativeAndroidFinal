package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;

import java.util.ArrayList;

public class NonPrintedBillListAdapter extends BaseAdapter {

    ArrayList<CounterbillingBean> cBillList;
    ArrayList<CounterbillingBean> arraylist = new ArrayList<>();
    LayoutInflater mInflater;
    Context context;

    public NonPrintedBillListAdapter(Context context, ArrayList<CounterbillingBean> prospect) {
        this.context = context;
        this.cBillList = prospect;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cBillList.size();
    }

    @Override
    public Object getItem(int position) {
        return cBillList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_nonprinted_bill_adapter, null);
            holder = new ViewHolder();

            holder.txtbillno = (TextView)convertView.findViewById(R.id.txtbillno);
            holder.txtdate = (TextView)convertView.findViewById(R.id.txtdate);
            holder.txtbillamt = (TextView)convertView.findViewById(R.id.txtbillamt);
            holder.txtcustname = (TextView)convertView.findViewById(R.id.txtcustname);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtcustname.setText(cBillList.get(position).getCustName());
        holder.txtbillno.setText(cBillList.get(position).getBillNo());
        holder.txtdate.setText(cBillList.get(position).getDateTime());
        holder.txtbillamt.setText(cBillList.get(position).getBillPaybleAmount());

        return convertView;
    }

    static class ViewHolder {
       TextView txtbillno, txtdate, txtbillamt,txtcustname;
    }
}
