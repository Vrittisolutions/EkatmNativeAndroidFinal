package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;

import java.util.ArrayList;

public class PrintBillDetailsAdapter extends BaseAdapter {

    ArrayList<CounterbillingBean> cBillList;
    ArrayList<CounterbillingBean> arraylist = new ArrayList<>();
    LayoutInflater mInflater;
    Context context;

    public PrintBillDetailsAdapter(Context context, ArrayList<CounterbillingBean> billlist) {
        this.context = context;
        this.cBillList = billlist;
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

        final int pos = position;
        final ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_bill_printed_details_dapter, null);
            holder = new ViewHolder();

            holder.txtitmname = (TextView)convertView.findViewById(R.id.txtitmname);
            holder.txtqty = (TextView)convertView.findViewById(R.id.txtqty);
            holder.txtdisc = (TextView)convertView.findViewById(R.id.txtdisc);
            holder.txtamt = (TextView)convertView.findViewById(R.id.txtamt);
            holder.txtrate = (TextView)convertView.findViewById(R.id.txtrate);
            holder.txtcgst = (TextView)convertView.findViewById(R.id.txtcgst);
            holder.txtsgst = (TextView)convertView.findViewById(R.id.txtsgst);
            holder.txtmrp = (TextView)convertView.findViewById(R.id.txtmrp);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtitmname.setText(cBillList.get(position).getItemDesc());
        holder.txtqty.setText(cBillList.get(position).getQty());
        holder.txtdisc.setText(String.format("%.02f",cBillList.get(position).getDiscamt()));
        //holder.txtamt.setText(String.format("%.02f",cBillList.get(position).getTotAmt_incltax_lineamt()));
        holder.txtamt.setText(String.format("%.02f",cBillList.get(position).getDicountedTotal()));
        holder.txtmrp.setText(String.format("%.02f",cBillList.get(position).getMRP()));
        holder.txtcgst.setText(cBillList.get(position).getCgstLine());
        holder.txtsgst.setText(cBillList.get(position).getSgstLine());
        holder.txtrate.setText(String.format("%.02f",cBillList.get(position).getRate()));

        if(String.format("%.02f",cBillList.get(position).getDiscamt()).equalsIgnoreCase("0.00") ||
                String.format("%.02f",cBillList.get(position).getDiscamt()).equalsIgnoreCase("0.0")){
                holder.txtdisc.setTextColor(Color.GRAY);
        }else {
            holder.txtdisc.setTextColor(Color.BLACK);
        }

        if(cBillList.get(position).getCgstLine().equalsIgnoreCase("0.0")){
            holder.txtcgst.setTextColor(Color.GRAY);
        }else {
            holder.txtcgst.setTextColor(parent.getResources().getColor(R.color.colorPrimary1));
        }

        if(cBillList.get(position).getSgstLine().equalsIgnoreCase("0.0")){
            holder.txtsgst.setTextColor(Color.GRAY);
        }else {
            holder.txtsgst.setTextColor(parent.getResources().getColor(R.color.colorPrimary1));
        }

        return convertView;
    }

    static class ViewHolder {
       TextView txtitmname, txtqty, txtdisc, txtamt, txtrate, txtcgst, txtsgst,txtmrp;
    }
}
