package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.Country;
import com.vritti.crm.vcrm7.BusinessProspectusActivity;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by pradnya on 10/13/16.
 */
public class CountryAdapter extends BaseAdapter {
    ArrayList<Country> countryArrayList;
    LayoutInflater mInflater;
    Context context;

    public CountryAdapter(Context context1, ArrayList<Country> countryArrayList) {
        this.countryArrayList = countryArrayList;
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

        holder.txt.setText(countryArrayList.get(position).getCountryName());

        // (CleansingPermitActivity)
        return convertView;
    }



    static class ViewHolder {
        TextView txt;

    }


}
