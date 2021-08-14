package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.Beans.Holiday;

import java.util.ArrayList;


/**
 * Created by 300151 on 10/13/16.
 */
public class HolidayAdapter extends BaseAdapter {
    ArrayList<Holiday>holidayArrayList;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public HolidayAdapter(Context context1, ArrayList<Holiday> holidayArrayList) {
        this.holidayArrayList = holidayArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        }

    @Override
    public int getCount() {
        return holidayArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return holidayArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.holiday_item, null);
            holder = new ViewHolder();
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_day = (TextView) convertView.findViewById(R.id.txt_day);
            holder.txt_date = (TextView) convertView.findViewById(R.id.txt_date);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_name.setText(holidayArrayList.get(position).getHolidayReason());
        holder.txt_day.setText(holidayArrayList.get(position).getDayName());
        holder.txt_date.setText(holidayArrayList.get(position).getShortHolidayDate());

        return convertView;
    }

    static class ViewHolder {
        TextView txt_name,txt_day,txt_date;
    }
}
