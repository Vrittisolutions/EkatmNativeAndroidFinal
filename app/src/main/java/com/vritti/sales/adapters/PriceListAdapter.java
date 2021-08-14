package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.PriceListBean;

import java.util.ArrayList;
import java.util.Locale;

public class PriceListAdapter extends BaseAdapter {
    private static ArrayList<PriceListBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<PriceListBean> arraylist;
    String key = "";

    public PriceListAdapter(Context parent, ArrayList<PriceListBean> custSelectionList,String key) {
        this.parent = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<PriceListBean>();
        arraylist.addAll(custSelectionList);
        this.key = key;
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
            convertView = mInflater.inflate(R.layout.pricelist_adapter,
                    null);
            holder = new ViewHolder();

            holder.txtpcode = (TextView) convertView.findViewById(R.id.txtpcode);
            holder.txtpdesc = (TextView) convertView.findViewById(R.id.txtpdesc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       /* String name = "", initial = "";
        try{
            if(key.equalsIgnoreCase("PriceListCode")){
                name = list.get(position).getpListCode().toUpperCase();
            }else if(key.equalsIgnoreCase("PriceListDesc")){
                name = list.get(position).getpListDesc().toUpperCase();
            }

             initial = String.valueOf(name.charAt(0));
        }catch (Exception e){
            e.printStackTrace();
        }*/

        holder.txtpcode.setText(list.get(position).getpListCode());
        holder.txtpdesc.setText(list.get(position).getpListDesc());

        /*if(key.equalsIgnoreCase("PriceListCode")){
            holder.txtpcode.setText(list.get(position).getpListCode());
            holder.txtpdesc.setVisibility(View.GONE);
        }else if(key.equalsIgnoreCase("PriceListDesc")){
            holder.txtpdesc.setText(list.get(position).getpListDesc());
            holder.txtpcode.setVisibility(View.GONE);
        }
*/
        return convertView;
    }

    class ViewHolder{
        TextView txtpcode,txtpdesc;
    }

    public void filter(String charText,String key) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (PriceListBean wp : arraylist) {
                //if (wp.getTaxClassDesc().toLowerCase(Locale.getDefault()).contains(charText)) {
                if(key.equalsIgnoreCase("PriceListCode")){
                    if (wp.getpListCode().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        list.add(wp);
                    }
                }else if(key.equalsIgnoreCase("PriceListDesc")){
                    if (wp.getpListDesc().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        list.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
