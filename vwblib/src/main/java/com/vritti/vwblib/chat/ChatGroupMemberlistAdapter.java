package com.vritti.vwblib.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import com.vritti.vwblib.R;


/**
 * Created by pradnya on 10/13/16.
 */
public class ChatGroupMemberlistAdapter extends BaseAdapter {
    ArrayList<ChatUser> chatUserArrayList;
    LayoutInflater mInflater;
    Context context;
    private ArrayList<ChatUser> arraylist;

    public ChatGroupMemberlistAdapter(Context context1, ArrayList<ChatUser> chatUserArrayList) {
        this.chatUserArrayList = chatUserArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return chatUserArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatUserArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_group_item_list, null);
            holder = new ViewHolder();
            holder.txt_user_name_data = (TextView) convertView.findViewById(R.id.user_name_data);
            holder.txt_admin = (TextView) convertView.findViewById(R.id.txt_admin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_user_name_data.setText(chatUserArrayList.get(position).getParticipantName());
        String CreatorId=chatUserArrayList.get(position).getCreater();
        String ParticipantId=chatUserArrayList.get(position).getParticipantId();



        if (CreatorId==null ||ParticipantId==null){

        }else {

            if (CreatorId.equals(ParticipantId)) {
                holder.txt_admin.setVisibility(View.VISIBLE);
            } else {
                holder.txt_admin.setVisibility(View.GONE);

            }
        }
        return convertView;
    }



    static class ViewHolder {

        TextView txt_user_name_data;
        TextView txt_admin;

    }
}
