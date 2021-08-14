package com.vritti.inventory.physicalInventory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;

import java.util.ArrayList;
import java.util.Locale;

public class PartNameAdapter extends BaseAdapter implements Filterable {

    private ArrayList<PartCodeName> list;
    private Context parent;
    private LayoutInflater mInflater;
    private ArrayList<PartCodeName> arraylist;

    public PartNameAdapter(Context parent,
                           ArrayList<PartCodeName> pcodeBeanslist) {
        this.parent = parent;
        this.list = pcodeBeanslist;
        arraylist = new ArrayList<PartCodeName>();
        arraylist.addAll(pcodeBeanslist);

        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.partcodename, null);
            holder = new ViewHolder();

            holder.textname = (TextView) convertView.findViewById(R.id.txtcode);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textname.setText(list.get(position).getPartName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    static class ViewHolder {
        TextView textname;

    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (PartCodeName wp : arraylist) {
                if (wp.getPartName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }

    public void filter_startwith(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (PartCodeName wp : arraylist) {
                if (wp.getPartCode().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();

    }


}
