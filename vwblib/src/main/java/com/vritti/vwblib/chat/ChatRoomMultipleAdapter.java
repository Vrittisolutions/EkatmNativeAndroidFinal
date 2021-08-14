package com.vritti.vwblib.chat;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.vritti.vwblib.R;


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
            convertView = mInflater.inflate(R.layout.vwb_multiple_group_item_list, null);
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
            Date=calculatediff(Date);
          //  Date = Date.substring(0, 10);
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
            Date=calculatediff(Date);
          //  Date = Date.substring(0, 10);
            holder.txt_created.setText("Created by " + Username + " , " + Date);
        }

        return convertView;
    }

    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mma");
            Date datestop = sdf.parse(datedb);


            return_value = datedb;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("dialog_action", "dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(datedb);
            return_value = tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(datedb);
            return_value = "Yesterday";
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(datedb);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {

            String k = datedb.substring(0, 10);
           // String tm[] = splitfrom(return_value);
            return k;
        }

    }



    static class ViewHolder {
        TextView txt_chatroom_name,txt_created,text_cartcount;
        LinearLayout len_closed;

    }
    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

}
