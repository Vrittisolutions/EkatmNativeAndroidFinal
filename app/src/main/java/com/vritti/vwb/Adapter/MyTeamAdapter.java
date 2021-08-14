package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.vwb.Beans.MyTeamBean;

import java.util.ArrayList;

import com.vritti.ekatm.R;

/**
 * Created by 300151 on 10/17/2016.
 */

public class MyTeamAdapter extends BaseAdapter {
    private static ArrayList<MyTeamBean> lsTeamList;
    private LayoutInflater mInflater;
    Context context;

    public MyTeamAdapter(Context context1, ArrayList<MyTeamBean> lsTeamList1) {
        lsTeamList = lsTeamList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return lsTeamList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsTeamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_myteam, null);
            holder = new ViewHolder();
            holder.tv_memusername = (TextView) convertView.findViewById(R.id.tv_memusername);
            holder.tv_total = (TextView) convertView.findViewById(R.id.tv_total);
            holder.tv_overdue = (TextView) convertView.findViewById(R.id.tv_overdue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_memusername.setText(lsTeamList.get(position).getUserName());
        holder.tv_total.setText(lsTeamList.get(position).getTotalCount()+"/");
        holder.tv_overdue.setText(lsTeamList.get(position).getTotalOverdueActivities());
        // holder.tv_OnTimePerc.setText(lsTeamList.get(position).getOnTimePerc()+"%");




        return convertView;
    }

    static class ViewHolder {
        TextView tv_memusername, tv_overdue, tv_total;

    }
}
