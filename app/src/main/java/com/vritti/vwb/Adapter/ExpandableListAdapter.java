package com.vritti.vwb.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.vritti.ekatm.R;

import java.util.HashMap;
import java.util.List;


/**
 * Created by 300151 on 10/12/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<String> mListDataHeader;
    private LayoutInflater mInflater;
    private HashMap<String, List<String>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount = 0;

        childCount = this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .size();

        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {

        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("CHILD", mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition).toString());
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.vwb_listheader, null);
            holder = new ViewHolder();

            holder.tv_menu = (TextView) convertView
                    .findViewById(R.id.tv_menu);
            holder.tv_icon = (TextView) convertView.findViewById(R.id.tv_icon);

            convertView.setTag(holder.tv_icon);
            convertView.setTag(holder.tv_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_menu.setText(headerTitle);
        if (getChildrenCount(groupPosition) > 0) {
            if (isExpanded) {
                holder.tv_icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_forword_arrow));
            } else {
                holder.tv_icon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_expand_arrow));
            }

        }
        holder.tv_menu.setTag(groupPosition);
        holder.tv_icon.setTag(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_list_submenu, null);
            holder = new ViewHolder();
            holder.tv_submenu = (TextView) convertView
                    .findViewById(R.id.tv_submenu);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_submenu.setText(childText);
        holder.tv_submenu.setTag(childText);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class ViewHolder {
        TextView tv_menu, tv_icon, tv_submenu;
    }

}
