package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.UserList;

import java.util.ArrayList;



/**
 * Created by 300151 on 10/13/16.
 */
public class UserListAdapter extends BaseAdapter {
    ArrayList<UserList> userListArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public UserListAdapter(Context context1, ArrayList<UserList> userListArrayList) {
        this.userListArrayList = userListArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;


    }

    @Override
    public int getCount() {
        return userListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userListArrayList.get(position);
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

            holder.txt_product = (TextView) convertView.findViewById(R.id.txt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_product.setText(userListArrayList.get(position).getUserName());

        return convertView;
    }

    static class ViewHolder {
        TextView txt_product;
    }
}
