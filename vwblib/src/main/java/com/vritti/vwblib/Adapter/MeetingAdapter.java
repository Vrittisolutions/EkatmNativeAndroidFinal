package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.vwblib.Beans.MeetingBean;
import com.vritti.vwblib.R;


public class MeetingAdapter extends BaseAdapter {
    private static ArrayList<MeetingBean> meetingBeanArrayList;
    private LayoutInflater mInflater;
    Context context;

    public MeetingAdapter(Context context1, ArrayList<MeetingBean> lsBirthdayList1) {
        meetingBeanArrayList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return meetingBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return meetingBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_meeting, null);
            holder = new ViewHolder();
            holder.txtmeetingtitle = (TextView) convertView.findViewById(R.id.txtmeetingtitle);
            holder.txtmeetingdate = (TextView) convertView.findViewById(R.id.txtmeetingdate);
            // holder.Expand = (ImageButton) convertView.findViewById(R.id.Expand);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (meetingBeanArrayList.get(position).getMOMTitle().equalsIgnoreCase(" No meeting schedule")) {
            holder.txtmeetingtitle.setText(meetingBeanArrayList.get(position).getMOMTitle());
            holder.txtmeetingdate.setText("Added By " + meetingBeanArrayList.get(position).getMOMDate()
                    + " " + meetingBeanArrayList.get(position).getMeetTime());

        } else {
            holder.txtmeetingtitle.setText(meetingBeanArrayList.get(position).getMOMTitle());
        }


        return convertView;
    }

    static class ViewHolder {
        TextView txtmeetingtitle, txtmeetingdate;
        // ImageButton Expand;
        LinearLayout lay1, lay2;
    }
}

