package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterCBillingList extends BaseAdapter {
    private Context parent;
    ArrayList<CounterbillingBean> listdtls;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;

    public AdapterCBillingList(Context context, ArrayList<CounterbillingBean> listDetails) {
        this.parent = context;
        this.listdtls = listDetails;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return listdtls.size();
    }

    @Override
    public Object getItem(int position) {
        return listdtls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.tbuds_custom_item_list_cb, null);
            holder = new ViewHolder();

            holder.txtdesc = (TextView)convertView.findViewById(R.id.txtdesc);
            holder.txtqty = (TextView)convertView.findViewById(R.id.txtqty);
            holder.txtrate = (TextView)convertView.findViewById(R.id.txtrate);
            holder.txtMRP = (TextView)convertView.findViewById(R.id.txtMRP);
            holder.txtamt = (TextView)convertView.findViewById(R.id.txtamt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtdesc.setText(listdtls.get(position).getItemDesc());
        holder.txtqty.setText(listdtls.get(position).getQty());
        holder.txtrate.setText(String.format("%.02f",listdtls.get(position).getRate()));
        holder.txtMRP.setText(String.format("%.02f",listdtls.get(position).getMRP()));
        //holder.txtamt.setText(String.format("%.02f",listdtls.get(position).getTotAmt_incltax_lineamt()));
        holder.txtamt.setText(String.format("%.02f",listdtls.get(position).getDicountedTotal()));

        return convertView;
    }

    public class ViewHolder{

        TextView txtdesc, txtqty, txtrate, txtMRP, txtamt;
    }
}