package com.vritti.inventory.physicalInventory.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.BatchList;
import com.vritti.inventory.physicalInventory.bean.LocationList;
import com.vritti.inventory.physicalInventory.bean.PartCodeName;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by 300151 on 10/13/16.
 */
public class LocationListAdapter extends BaseAdapter {
    ArrayList<LocationList> locationListArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;
    private ArrayList<LocationList> arraylist;

    public LocationListAdapter(Context context1, ArrayList<LocationList> locationListArrayList) {
        this.locationListArrayList = locationListArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

        arraylist = new ArrayList<LocationList>();
        arraylist.addAll(locationListArrayList);
    }

    @Override
    public int getCount() {
        return locationListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return locationListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder holder=new ViewHolder();
    if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_spinner_text, null);

            holder.txt_code = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(locationListArrayList.get(position).getLocationCode().equalsIgnoreCase("")){

        }else{
            holder.txt_code.setText(locationListArrayList.get(position).getLocationCode());
        }


        return convertView;
    }

    static class ViewHolder {
        TextView txt_code;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        locationListArrayList.clear();
        if (charText.length() == 0) {
            locationListArrayList.addAll(arraylist);
        } else {
            for (LocationList wp : arraylist) {
                if (wp.getLocationCode().toLowerCase(Locale.getDefault()).contains(charText)) {
                    locationListArrayList.add(wp);
                }
            }
        }

        notifyDataSetChanged();
    }
}
