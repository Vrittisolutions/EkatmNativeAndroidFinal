package com.vritti.ekatm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.ekatm.bean.BeanLogInsetting;
import com.vritti.ekatm.bean.ItemObject;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater lInflater;
    private List<BeanLogInsetting> listStorage;


    public CustomAdapter(Context context, List<BeanLogInsetting> customizedListView) {
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = lInflater.inflate(R.layout.listview_with_text_image, parent, false);

            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }

        if (Constants.type == Constants.Type.Vwb) {
            listViewHolder.textInListView.setText(listStorage.get(position).getCompanyURL());
        } else if (Constants.type == Constants.Type.PM) {
            listViewHolder.textInListView.setText(listStorage.get(position).getEnvId());
        }


        return convertView;
    }

    static class ViewHolder {

        TextView textInListView;
        ImageView imageInListView;
    }
}