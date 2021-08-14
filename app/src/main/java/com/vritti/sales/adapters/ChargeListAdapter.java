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

public class ChargeListAdapter extends BaseAdapter {
    private static ArrayList<ChargeDtlBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<ChargeDtlBean> arraylist;
    String key = "";

    public ChargeListAdapter(Context parent, ArrayList<ChargeDtlBean> chargeList) {
        this.parent = parent;
        this.list = chargeList;
        arraylist = new ArrayList<ChargeDtlBean>();
        arraylist.addAll(chargeList);
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
            convertView = mInflater.inflate(R.layout.charge_list_adapter,
                    null);
            holder = new ViewHolder();

            holder.txtsrno = (TextView) convertView.findViewById(R.id.txtitem);
            holder.txtchargedesc = convertView.findViewById(R.id.txtfinamt);
            holder.txtcalc = convertView.findViewById(R.id.txttaxamt);
            holder.txtqty = convertView.findViewById(R.id.txtdiscamt);
            holder.txtrate = convertView.findViewById(R.id.txtamt);
            holder.txtamt = convertView.findViewById(R.id.txtrate);
            holder.txttaxamt = convertView.findViewById(R.id.txtqty);
            holder.layout = convertView.findViewById(R.id.layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtsrno.setText(arraylist.get(position).getSrNo());
        holder.txtchargedesc.setText(arraylist.get(position).getChargeDesc());
        holder.txtcalc.setText(arraylist.get(position).getCalcMethod());
        holder.txtqty.setText(arraylist.get(position).getQty());
        holder.txtrate.setText(arraylist.get(position).getRate());
        holder.txttaxamt.setText(arraylist.get(position).getTaxAmt());
        holder.txtamt.setText(arraylist.get(position).getChargeAmt());

        if(position%2 == 1){
            holder.layout.setBackgroundColor(Color.parseColor("#EDEDED"));
        }else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return convertView;
    }

    class ViewHolder{
        TextView txtsrno,txtchargedesc,txtcalc,txtqty,txtrate,txtamt,txttaxamt;
        LinearLayout layout;
    }

}
