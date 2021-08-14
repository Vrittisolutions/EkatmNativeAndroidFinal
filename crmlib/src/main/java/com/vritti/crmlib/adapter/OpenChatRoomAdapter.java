package com.vritti.crmlib.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ChatRoomDisplay;


/**
 * Created by 300151 on 10/13/16.
 */
public class OpenChatRoomAdapter extends BaseAdapter {
    ArrayList<ChatRoomDisplay> chatRoomDisplayArrayList;
    LayoutInflater mInflater;
    Context context;

    public OpenChatRoomAdapter(Context context1, ArrayList<ChatRoomDisplay> chatRoomDisplayArrayList) {
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
            convertView = mInflater.inflate(R.layout.crm_open_close_con_item_list, null);
            holder = new ViewHolder();
            holder.txt_chatroom_name = (TextView) convertView.findViewById(R.id.txt_chatroom_name);
            holder.txt_created = (TextView) convertView.findViewById(R.id.txt_created);
            holder.card_view= (CardView) convertView.findViewById(R.id.card_view);
            holder.text_not_found= (TextView) convertView.findViewById(R.id.text_not_found);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (chatRoomDisplayArrayList.get(position).getStatus().equals("Created"))

        {
            holder.card_view.setVisibility(View.VISIBLE);
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatRoom_name());

            String Username = chatRoomDisplayArrayList.get(position).getUsername();
            String Date = chatRoomDisplayArrayList.get(position).getDate();
            holder.txt_created.setText("Created By " + Username + " , " + Date);
        }else {
            holder.text_not_found.setVisibility(View.VISIBLE);
        }
        return convertView;
    }



    static class ViewHolder {
        TextView txt_chatroom_name,txt_created,text_not_found;
        CardView card_view;

    }
}
