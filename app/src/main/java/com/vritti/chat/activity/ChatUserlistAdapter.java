package com.vritti.chat.activity;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.chat.bean.ChatUser;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.Locale;


/**
 * Created by pradnya on 10/13/16.
 */
public class ChatUserlistAdapter extends BaseAdapter {
    static ArrayList<ChatUser> chatUserArrayList;
    LayoutInflater mInflater;
    Context context;
    ArrayList<ChatUser> arraylist;

    public ChatUserlistAdapter(Context context1, ArrayList<ChatUser> chatUserArrayList) {
        this.chatUserArrayList = chatUserArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<ChatUser>();
        this.arraylist.addAll(chatUserArrayList);

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
            convertView = mInflater.inflate(R.layout.vwb_user_item_list, null);
            holder = new ViewHolder();
            holder.txt_user_name_data = (TextView) convertView.findViewById(R.id.user_name_data);
            holder.checkbox_user= (AppCompatCheckBox) convertView.findViewById(R.id.checkbox_user);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.checkbox_user.setText(chatUserArrayList.get(position).getUsername());
        holder.checkbox_user.setChecked(chatUserArrayList.get(position).isSelected());




        holder.checkbox_user.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox)v).isChecked();
                chatUserArrayList.get(position).setSelected(isSelected);

            }
        });




        return convertView;
    }
    public ArrayList<ChatUser> getChatUserArrayList(){
        ArrayList<ChatUser> list = new ArrayList<>();
        for(int i=0;i<chatUserArrayList.size();i++){
            if(chatUserArrayList.get(i).isSelected())
                list.add(chatUserArrayList.get(i));
        }
        return list;
    }
    public ArrayList<ChatUser> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        chatUserArrayList.clear();
        if (charText.length() == 0) {
            chatUserArrayList.addAll(arraylist);
        }
        else
        {
            for (ChatUser wp : arraylist)
            {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).startsWith(charText))
                {
                    chatUserArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return chatUserArrayList;
    }


    static class ViewHolder {
        TextView txt_user_name_data;
        AppCompatCheckBox checkbox_user;

    }


}
