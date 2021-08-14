package com.vritti.vwb.Adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.vritti.chat.adapter.ChatUserlistAdapter;
import com.vritti.chat.bean.ChatUser;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.Item;
import com.vritti.vwb.vworkbench.ActvityNotificationTypeActivity;

import java.util.ArrayList;
import java.util.Locale;


public class NotificationTypeAdapter extends BaseAdapter {
     ArrayList<Item> itemArrayList;
    LayoutInflater mInflater;
    Context context;

    public NotificationTypeAdapter(Context context1, ArrayList<Item> itemArrayList) {
        this.itemArrayList = itemArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.notification_type_item_lay, null);
            holder = new ViewHolder();
            holder.txt_notification = (TextView) convertView.findViewById(R.id.txt_notification);
            holder.read_count = (TextView) convertView.findViewById(R.id.read_count);
            holder.unread_count = (TextView) convertView.findViewById(R.id.unread_count);
            holder.cardView=(CardView) convertView.findViewById(R.id.cardView);
            holder.len_read=(LinearLayout) convertView.findViewById(R.id.len_read);
            holder.len_unread=(LinearLayout) convertView.findViewById(R.id.len_unread);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_notification.setText(itemArrayList.get(position).getTypeName());
        holder.read_count.setText(itemArrayList.get(position).getReadNotification());
        holder.unread_count.setText(itemArrayList.get(position).getNotRead());

       /* holder.len_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Typename=itemArrayList.get(position).getTypeName();
                String Typecode=itemArrayList.get(position).getTypeCode();
                String id=itemArrayList.get(position).getPKNotificationId();

                if (holder.read_count.equals("0")){

                }else {
                    ((ActvityNotificationTypeActivity) context).setmessageread(Typename, Typecode, id);
                }
            }
        });*/
        holder.len_unread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Typename=itemArrayList.get(position).getTypeName();
                String Typecode=itemArrayList.get(position).getTypeCode();
                String id=itemArrayList.get(position).getPKNotificationId();

                if (holder.unread_count.getText().equals("0")){
                   Toast.makeText(context,"You don't have any unread business information",Toast.LENGTH_SHORT).show();

                }else {

                    ((ActvityNotificationTypeActivity) context).setmessageunread(Typename, Typecode, id);
                }
            }
        });
        holder.len_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Typename=itemArrayList.get(position).getTypeName().toString().trim();
                String Typecode=itemArrayList.get(position).getTypeCode();
                String id=itemArrayList.get(position).getPKNotificationId();

                if (holder.read_count.getText().equals("0")){
                    Toast.makeText(context,"You donot have any read business information",Toast.LENGTH_SHORT).show();

                }else {

                    ((ActvityNotificationTypeActivity) context).setmessageRead(Typename, Typecode, id);
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        private TextView txt_notification,read_count,unread_count;
        LinearLayout len_read,len_unread;
        CardView cardView;
    }


}
