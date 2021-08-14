package com.vritti.crmlib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ChatGroup;


/**
 * Created by pradnya on 10/13/16.
 */
public class ChatRoomMultipleAdapter extends BaseAdapter {
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    LayoutInflater mInflater;
    Context context;

    public ChatRoomMultipleAdapter(Context context1, ArrayList<ChatGroup> chatRoomDisplayArrayList) {
        this.chatRoomDisplayArrayList = chatRoomDisplayArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return chatRoomDisplayArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatRoomDisplayArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_multiple_group_item_list, null);
            holder = new ViewHolder();
            holder.txt_chatroom_name = (TextView) convertView.findViewById(R.id.txt_chatroom_name);
            holder.txt_created = (TextView) convertView.findViewById(R.id.txt_created);
            holder.len_closed = (LinearLayout) convertView.findViewById(R.id.len_closed);
            holder.text_cartcount = (TextView) convertView.findViewById(R.id.text_cartcount);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (chatRoomDisplayArrayList.get(position).getStatus().equals("Closed")){
            holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.sendLightColor));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            Date = Date.substring(0, 10);
            holder.txt_created.setTextColor(Color.BLACK);
            holder.txt_created.setText("Closed by " + Username + " , " + Date);
        }else {
            String Count=chatRoomDisplayArrayList.get(position).getCount();
            if (Count==null||Count.equals("0")){
                holder.text_cartcount.setVisibility(View.GONE);

            }else {
                if (Count.length() > 0) {
                    holder.text_cartcount.setVisibility(View.VISIBLE);
                    holder.text_cartcount.setText(Count);
                }
            }
            holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            Date = Date.substring(0, 10);
            holder.txt_created.setText("Created by " + Username + " , " + Date);
        }

        return convertView;
    }



    static class ViewHolder {
        TextView txt_chatroom_name,txt_created,text_cartcount;
        LinearLayout len_closed;

    }
}
