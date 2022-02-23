package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.ListData;
import com.vritti.crm.vcrm7.CountryListActivity;
import com.vritti.crm.vcrm7.FilterListActivity;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by pradnya on 10/13/16.
 */
public class ListDataAdapter extends BaseAdapter {
    ArrayList<ListData> arrayListFiltered;
    ArrayList<ListData> arrayList1;
    ArrayList<ListData> countryArrayList;
    LayoutInflater mInflater;
    Context context;

    public ListDataAdapter(Context context1, ArrayList<ListData> countryArrayList) {
        this.countryArrayList = countryArrayList;
        this.arrayListFiltered = new ArrayList<>();
        this.arrayList1 = new ArrayList<>();
        arrayListFiltered = countryArrayList;
        arrayList1.addAll(countryArrayList);
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return countryArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return countryArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_spinner_text, null);
            holder = new ViewHolder();
            holder.txt= (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt.setText(countryArrayList.get(position).getName());

        // (CleansingPermitActivity)
        return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }
    public ArrayList<ListData> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayListFiltered.clear();
        if (charText.length() == 0) {
            arrayListFiltered.addAll(arrayList1);
        } else {
            for (ListData wp : arrayList1) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayListFiltered.add(wp);
                    ((CountryListActivity)context).updateList(countryArrayList);
                }
            }
        }

        notifyDataSetChanged();
        return countryArrayList;
    }

    public ArrayList<ListData> filter1(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayListFiltered.clear();
        if (charText.length() == 0) {
            arrayListFiltered.addAll(arrayList1);
        } else {
            for (ListData wp : arrayList1) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayListFiltered.add(wp);
                    ((FilterListActivity)context).updateList(countryArrayList);
                }
            }
        }

        notifyDataSetChanged();
        return countryArrayList;
    }

}
