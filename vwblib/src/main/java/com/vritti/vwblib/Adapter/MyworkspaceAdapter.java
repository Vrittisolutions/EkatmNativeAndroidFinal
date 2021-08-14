package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import com.vritti.vwblib.Beans.MyWorkspaceBean;
import com.vritti.vwblib.R;

/**
 * Created by 300151 on 10/17/2016.
 */

public class MyworkspaceAdapter extends BaseAdapter {
    private static ArrayList<MyWorkspaceBean> lsWorkspaceList;
    private LayoutInflater mInflater;
    Context context;

    public MyworkspaceAdapter(Context context1, ArrayList<MyWorkspaceBean> lsWorkspaceList1) {
        lsWorkspaceList = lsWorkspaceList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return lsWorkspaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsWorkspaceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_myworkspace, null);
            holder = new ViewHolder();
            holder.tv_Workspacename = (TextView) convertView.findViewById(R.id.tv_Workspacename);
            holder.tv_onTime = (TextView) convertView.findViewById(R.id.tv_onTime);
            holder.tv_Openactivity = (TextView) convertView.findViewById(R.id.tv_Openactivity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_Workspacename.setText(lsWorkspaceList.get(position).getProjectName());
        holder.tv_Openactivity.setText(lsWorkspaceList.get(position).getOpenActivities());
        holder.tv_onTime.setText(lsWorkspaceList.get(position).getOnTime()+"%");
        return convertView;
    }

    static class ViewHolder {
        TextView tv_Workspacename, tv_Openactivity, tv_onTime;

    }
}
