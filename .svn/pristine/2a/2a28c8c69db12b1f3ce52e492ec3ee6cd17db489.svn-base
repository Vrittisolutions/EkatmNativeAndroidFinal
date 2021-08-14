package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.vwblib.R;


public class ActivityOptionAdapter extends BaseAdapter {

    String[] menus;
    int[] images;
    private LayoutInflater mInflater;
    Context context;

    public ActivityOptionAdapter(Context context1, String[] menu
    ) {
        menus = menu;
        // images = image;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return menus.length;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return menus[arg0];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(
                    R.layout.vwb_option_list, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView
                    .findViewById(R.id.tvtitleactivityadaptertitle);

          /*  holder.txthelp = (TextView) convertView
                    .findViewById(R.id.tvtitleactivityadapterdetail);*/

            holder.iv = (ImageView) convertView
                    .findViewById(R.id.ivadapteractivityaction);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // holder.iv.setBackgroundResource(images[position]);
        holder.txtName.setText(menus[position].toString());

       /* if (menus[position].toString().equals("Add Time Sheet Entry")) {

            holder.txthelp.setText("Fill up your time sheet.");

        } else if (menus[position].toString().equals("Update Activity Status")) {

            holder.txthelp.setText("Update your activity status.");

        } else if (menus[position].toString().equals("Activity Trail")) {

            holder.txthelp.setText("Complete trail of your activity.");

        } else if (menus[position].toString().equals("Activity Reassign")) {

            holder.txthelp.setText("Reassign your activity.");

        } else if (menus[position].toString().equals("Assign Activity")) {

            holder.txthelp.setText("assign your activity.");

        } else if (menus[position].toString().equals("Datasheet Entry")) {

            holder.txthelp.setText("Fill up your datasheet.");

        }*/

        return convertView;

    }

    static class ViewHolder {
        TextView txtName;
        TextView txthelp;
        ImageView iv;

    }
}
