package com.vritti.chat.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.chat.bean.ChatGroup;
import com.vritti.ekatm.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by pradnya on 10/13/16.
 */
public class ChatRoomMultipleAdapter extends BaseAdapter {
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    LayoutInflater mInflater;
    Context context;
    SimpleDateFormat sdf;
    private Date datestop;
    private String chatdate, ChatMessage;
    ArrayList<ChatGroup> arraylist;

    public ChatRoomMultipleAdapter(Context context1, ArrayList<ChatGroup> chatRoomDisplayArrayList) {
        this.chatRoomDisplayArrayList = chatRoomDisplayArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<ChatGroup>();
        this.arraylist.addAll(chatRoomDisplayArrayList);

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
    public int getItemViewType(int position) {
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
            holder.profile_image = (ImageView) convertView.findViewById(R.id.profile_image);
            holder.txt_chatroom_message = (TextView) convertView.findViewById(R.id.txt_chatroom_message);
            holder.txt_chat_time = (TextView) convertView.findViewById(R.id.chat_time);
            holder.img_file = (ImageView) convertView.findViewById(R.id.img_file);
            holder.rel = (RelativeLayout) convertView.findViewById(R.id.rel);
            holder.len = (LinearLayout) convertView.findViewById(R.id.len);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.len.setVisibility(View.VISIBLE);
        if (chatRoomDisplayArrayList.get(position).getStatus().equals("Closed")) {
            holder.rel.setVisibility(View.GONE);
           /* holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.sendLightColor));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma");
            Date = Date.substring(Date.indexOf("(") + 1, Date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Date);
            Date date = new Date(timestamp);
            chatdate = sdf.format(date);
            Date = calculatediff(chatdate);
            holder.txt_created.setTextColor(Color.BLACK);
            holder.txt_created.setText("Closed by " + Username + " , " + Date);
            String Chat_pic = chatRoomDisplayArrayList.get(position).getChatType();
            if (Chat_pic == null) {
                holder.profile_image.setImageResource(R.drawable.group_2);
            } else {
                if (Chat_pic.equalsIgnoreCase("P")) {
                    holder.profile_image.setImageResource(R.drawable.user_profile);
                } else {
                    holder.profile_image.setImageResource(R.drawable.group_2);
                }
            }
            ChatMessage = chatRoomDisplayArrayList.get(position).getChatMessage();
            holder.txt_chatroom_message.setText(ChatMessage);
     */   }
        else {
            String Count = chatRoomDisplayArrayList.get(position).getCount();
            if (Count == null || Count.equals("0")) {
                holder.text_cartcount.setVisibility(View.GONE);
                holder.txt_chatroom_message.setTextColor(context.getResources().getColor(R.color.dark_grey));


            } else {
                if (Count.length() > 0) {
                    holder.text_cartcount.setVisibility(View.VISIBLE);
                    holder.text_cartcount.setText(Count);
                    holder.txt_chatroom_message.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                }
            }
            holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            String Chat_pic = chatRoomDisplayArrayList.get(position).getChatType();

            if (Chat_pic == null) {
                holder.profile_image.setImageResource(R.drawable.group_user_icon);
            } else {
                if (Chat_pic.equalsIgnoreCase("P")) {
                    holder.profile_image.setImageResource(R.drawable.single_user_icon);
                } else {
                    holder.profile_image.setImageResource(R.drawable.group_user_icon);

                }
            }
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma");
            Date = Date.substring(Date.indexOf("(") + 1, Date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Date);
            Date date = new Date(timestamp);
            chatdate = sdf.format(date);
            Date = calculatediff(chatdate);

           /* Date = Date.substring(Date.indexOf("(") + 1, Date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Date);

            Date=calculatediff(timestamp);*/
            //  Date = Date.substring(0, 10);
            ChatMessage = chatRoomDisplayArrayList.get(position).getChatMessage();
            if (ChatMessage == null || ChatMessage.equals("null") || ChatMessage.equals("")) {
                holder.txt_chatroom_message.setVisibility(View.VISIBLE);
                holder.txt_chatroom_message.setText("Created by " + Username + " , " + Date);
                holder.img_file.setVisibility(View.GONE);

                //holder.txt_created.setVisibility(View.VISIBLE);
            } else {
                holder.txt_chatroom_message.setVisibility(View.VISIBLE);
                if (ChatMessage.contains(context.getResources().getString(R.string.google_map_key))) {
                    holder.txt_chatroom_message.setText(" Location");
                    holder.img_file.setColorFilter(context.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                    holder.img_file.setVisibility(View.VISIBLE);
                    holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.file_location));
                } else if (Chat_pic.equalsIgnoreCase("P")) {


                    if (ChatMessage.contains("File")) {
                        holder.img_file.setVisibility(View.VISIBLE);

                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.camera_1));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);

                    } else if(ChatMessage.contains("#contact")){
                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.contact));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText("Contact");
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                    }else {
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                        holder.img_file.setVisibility(View.GONE);

                    }

                } else {

                    if (ChatMessage.contains("File")) {
                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.camera_1));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);

                    } else if(ChatMessage.contains("#contact")){

                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.contact));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText("Contact");
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                    }else {
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                        holder.img_file.setVisibility(View.GONE);
                    }
                }
            }
        }
        return convertView;
    }


   /* private String calculatediff(long datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";

        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma");
            Date date = new Date(datedb);
            chatdate = sdf.format(date);

            datestop = sdf.parse(chatdate);

            *//*if (datedb.contains("-")) {
                sdf = new SimpleDateFormat("dd-MM-yyyy hh:mma");
                datestop = sdf.parse(datedb);

            } else{
                 sdf = new SimpleDateFormat("dd/MM/yyyy hh:mma");
                datestop = sdf.parse(datedb);
        }
*//*
            return_value = chatdate;

            int ihoy = (int) (datestop.getTime() / (1000 * 60 * 60 * 24));
            int idate = (int) (date.getTime() / (1000 * 60 * 60 * 24));
            dif = idate - ihoy;
            Log.d("dialog_action", "dialog_action" + dif);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (dif == 0) {
            String tm[] = splitfrom(chatdate);
            return_value = tm[0];
            return return_value;
        } else if (dif == 1) {
            String tm[] = splitfrom(chatdate);
            return_value = "Yesterday";
            return return_value;
        } else if (dif == -1) {
            String tm[] = splitfrom(chatdate);
            return_value = "Tomorrow" + tm[0];
            return return_value;
        } else {

            String k = chatdate.substring(0, 10);
           // String tm[] = splitfrom(return_value);
            return k;
        }

    }
*/

    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma");
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
            String date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", k);


            // String tm[] = splitfrom(return_value);
            return date_after;
        }

    }


    static class ViewHolder {
        TextView txt_chatroom_name, txt_created, text_cartcount, txt_chatroom_message, txt_chat_time;
        LinearLayout len_closed;
        RelativeLayout rel;
        ImageView profile_image, img_file;
        LinearLayout len;

    }

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    public ArrayList<ChatGroup> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        chatRoomDisplayArrayList.clear();
        if (charText.length() == 0) {
            chatRoomDisplayArrayList.addAll(arraylist);
        } else {
            for (ChatGroup wp : arraylist) {
                if (wp.getChatroom().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    chatRoomDisplayArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return chatRoomDisplayArrayList;
    }
}
