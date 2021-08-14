package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vritti.ekatm.R;
import com.vritti.vwb.classes.TimesheetLog;

/**
 * Created by sharvari on 28-Jun-18.
 */

public class TimeSheetLogAdapter extends BaseAdapter {

    ArrayList<TimesheetLog> timesheetLogArrayList;
    LayoutInflater mInflater;
    Context context;

    public TimeSheetLogAdapter(Context context1, ArrayList<TimesheetLog> timesheetLogArrayList) {
        this.timesheetLogArrayList = timesheetLogArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return timesheetLogArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return timesheetLogArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_timesheet_log_item_lay, null);
            holder = new ViewHolder();

            holder.txt_description=convertView.findViewById(R.id.txt_description);
            holder.txt_addedby=convertView.findViewById(R.id.txt_addedby);
            holder.txt_activity_type=convertView.findViewById(R.id.txt_activity_type);
            holder.txt_work_date=convertView.findViewById(R.id.txt_work_date);
            holder.txt_from=convertView.findViewById(R.id.txt_from);
            holder.txt_to=convertView.findViewById(R.id.txt_to);
            holder.txt_hrs=convertView.findViewById(R.id.txt_hrs);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txt_description.setText(timesheetLogArrayList.get(position).getRemarks());
        holder.txt_addedby.setText(timesheetLogArrayList.get(position).getUserName());
        holder.txt_activity_type.setText(timesheetLogArrayList.get(position).getActivityTypeName());
        String work=timesheetLogArrayList.get(position).getWorkDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
        work = work.substring(work.indexOf("(") + 1, work.lastIndexOf(")"));
        long DOB_date = Long.parseLong(work);
        Date DOBDate = new Date(DOB_date);
        work = sdf.format(DOBDate);
        holder.txt_work_date.setText(work);

        String TimeFrom=timesheetLogArrayList.get(position).getTimeFrom();
        sdf = new SimpleDateFormat("hh:mm aa");
        TimeFrom= TimeFrom.substring(TimeFrom.indexOf("(") + 1, TimeFrom.lastIndexOf(")"));
        DOB_date = Long.parseLong(TimeFrom);
        DOBDate = new Date(DOB_date);
        TimeFrom = sdf.format(DOBDate);
        holder.txt_from.setText(TimeFrom);


        String TimeTo=timesheetLogArrayList.get(position).getTimeTo();
        sdf = new SimpleDateFormat("hh:mm aa");
        TimeTo = TimeTo.substring(TimeTo.indexOf("(") + 1, TimeTo.lastIndexOf(")"));
        DOB_date = Long.parseLong(TimeTo);
        DOBDate = new Date(DOB_date);
        TimeTo = sdf.format(DOBDate);
        holder.txt_to.setText(TimeTo);
        holder.txt_hrs.setText(timesheetLogArrayList.get(position).getHours());







        return convertView;
    }


    static class ViewHolder {
        TextView txt_hrs,txt_to,txt_from,txt_work_date,txt_activity_type,txt_addedby,txt_description;

    }
}
