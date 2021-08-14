package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vritti.vwb.Beans.GPSMyLocationBean;
import com.vritti.ekatm.R;

import java.util.ArrayList;


/**
 * Created by Admin-1 on 3/31/2017.
 */

public class GPSRecordsAdapter extends BaseAdapter {
    private static ArrayList<GPSMyLocationBean> searchArrayList;

    private LayoutInflater mInflater;
    Context context;

    public GPSRecordsAdapter(Context context1, ArrayList<GPSMyLocationBean> gpsrec) {
        searchArrayList = gpsrec;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return searchArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.vwb_item_gps_records, null);
            holder = new ViewHolder();
            holder.txtLocation = (TextView) convertView
                    .findViewById(R.id.txtlocation);
            holder.txtdate = (TextView) convertView
                    .findViewById(R.id.txtdate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtLocation.setText(searchArrayList.get(position).getLocationName());
        holder.txtdate.setText(searchArrayList.get(position).getAddedDT());
        return convertView;
    }

    static class ViewHolder {
        TextView txtLocation;
        TextView txtdate;

    }


}
