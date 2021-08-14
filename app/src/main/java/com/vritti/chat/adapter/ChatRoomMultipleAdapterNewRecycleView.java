package com.vritti.chat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.chat.activity.OpenChatroomActivity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.ekatm.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatRoomMultipleAdapterNewRecycleView extends RecyclerView.Adapter<ChatRoomMultipleAdapterNewRecycleView.ChatRoomMultipleAdapterNewRecycleViewHolder> {
    Context context;
    ArrayList<ChatGroup> chatRoomDisplayArrayList;
    /**/
    LayoutInflater mInflater;
    SimpleDateFormat sdf;
    private Date datestop;
    private String chatdate, ChatMessage;
    ArrayList<ChatGroup> arraylist;

    public ChatRoomMultipleAdapterNewRecycleView(Context context, ArrayList<ChatGroup> chatRoomDisplayArrayList) {
        this.context = context;
        this.chatRoomDisplayArrayList = chatRoomDisplayArrayList;
        this.arraylist = new ArrayList<ChatGroup>();
        this.arraylist.addAll(chatRoomDisplayArrayList);
    }

    @NonNull
    @Override
    public ChatRoomMultipleAdapterNewRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_multiple_group_item_list, parent, false);

        return new ChatRoomMultipleAdapterNewRecycleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomMultipleAdapterNewRecycleViewHolder holder, int position) {
        if (chatRoomDisplayArrayList.get(position).getStatus().equals("Closed")) {
            holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.sendLightColor));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom().trim());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mma");
            Date = Date.substring(Date.indexOf("(") + 1, Date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Date);
            java.util.Date date = new Date(timestamp);
            chatdate = sdf.format(date);
            Date = calculatediff(chatdate);
            holder.txt_created.setTextColor(Color.BLACK);
            holder.txt_created.setText("Closed by " + Username + " , " + Date);
            String Chat_pic = chatRoomDisplayArrayList.get(position).getChatType();
            if (Chat_pic == null) {
                holder.profile_image.setImageResource(R.drawable.group_2);
            } else {
                if (Chat_pic.equalsIgnoreCase("P")) {
                    holder.profile_image.setImageResource(R.drawable.single_user_icon);
                } else {
                    holder.profile_image.setImageResource(R.drawable.group_user_icon);
                }
            }
            ChatMessage = chatRoomDisplayArrayList.get(position).getChatMessage();
            holder.txt_chatroom_message.setText(ChatMessage);
        } else {
            if (position % 2 == 0) {
                holder.card_view.setBackgroundColor(context.getResources().getColor(R.color.card_one_color));
            } else {
                holder.card_view.setBackgroundColor(context.getResources().getColor(R.color.card_two_color));
            }

            String Count = chatRoomDisplayArrayList.get(position).getCount();
            if (Count == null || Count.equals("0")) {
                holder.text_cartcount.setVisibility(View.GONE);
                holder.text_cartcountLay.setVisibility(View.GONE);
                holder.txt_chatroom_message.setTextColor(context.getResources().getColor(R.color.dark_grey));


            } else {
                if (Count.length() > 0) {
                    holder.text_cartcount.setVisibility(View.VISIBLE);
                    holder.text_cartcountLay.setVisibility(View.VISIBLE);
                    holder.text_cartcount.setText(Count);
                    holder.txt_chatroom_message.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                }
            }
            // holder.len_closed.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.txt_chatroom_name.setText(chatRoomDisplayArrayList.get(position).getChatroom());
            String Username = chatRoomDisplayArrayList.get(position).getAddedBy();
            String Date = chatRoomDisplayArrayList.get(position).getStartTime();
            String Chat_pic = chatRoomDisplayArrayList.get(position).getChatType();

            if (Chat_pic == null) {
                holder.profile_image.setImageResource(R.drawable.group_2);
            } else {
                if (Chat_pic.equalsIgnoreCase("P")) {
                    holder.profile_image.setImageResource(R.drawable.single_user_icon);
                } else {
                    holder.profile_image.setImageResource(R.drawable.group_user_icon);
                }

            }
            sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.ENGLISH);
           // sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date = Date.substring(Date.indexOf("(") + 1, Date.lastIndexOf(")"));
            long timestamp = Long.parseLong(Date);
            Date date = new Date(timestamp);
            chatdate = sdf.format(date);
           // sdf.setTimeZone(TimeZone.getDefault());
            Date = calculatediff(chatdate);



            ChatMessage = chatRoomDisplayArrayList.get(position).getChatMessage();
            if (ChatMessage == null || ChatMessage.equals("null") || ChatMessage.equals("")) {
                holder.txt_chatroom_message.setVisibility(View.VISIBLE);
                holder.txt_chatroom_message.setText("Created by " + Username + " , " + Date);
                holder.img_file.setVisibility(View.GONE);

                //holder.txt_created.setVisibility(View.VISIBLE);
            }
            else {
                holder.txt_chatroom_message.setVisibility(View.VISIBLE);
                if (ChatMessage.contains(context.getResources().getString(R.string.google_map_key))) {
                    holder.txt_chatroom_message.setText(" Location");
                    holder.img_file.setColorFilter(context.getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);

                    holder.img_file.setVisibility(View.VISIBLE);
                    holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.file_location));
                } else if (Chat_pic.equalsIgnoreCase("P")) {


                    if (ChatMessage.contains("#File") || ChatMessage.contains("File")) {
                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.camera_1));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                        if (ChatMessage.contains("#File"))
                                holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava("File...."));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);

                    } else if (ChatMessage.contains("#contact")) {
                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.contact));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText("Contact");
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                    } else {
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                        holder.img_file.setVisibility(View.GONE);

                    }

                } else {

                    if (ChatMessage.contains("#File") || ChatMessage.contains("File")) {
                        holder.img_file.setVisibility(View.VISIBLE);
                        //ChatMessage.replace("#File" , " ");
                        // ChatMessage.replace("File" , " ");
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.camera_1));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                        if (ChatMessage.contains("#File"))
                            holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava("File...."));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);

                    } else if (ChatMessage.contains("#contact")) {
                        holder.img_file.setVisibility(View.VISIBLE);
                        holder.img_file.setImageDrawable(context.getResources().getDrawable(R.drawable.contact));
                        holder.img_file.setColorFilter(context.getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
                        holder.txt_chatroom_message.setText("Contact");
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                    } else {
                        holder.txt_chatroom_message.setText(StringEscapeUtils.unescapeJava(ChatMessage));
                        holder.txt_created.setVisibility(View.GONE);
                        holder.txt_chat_time.setVisibility(View.VISIBLE);
                        holder.txt_chat_time.setText(Date);
                        holder.img_file.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private String calculatediff(String datedb) {
        System.out.println("date db......................" + datedb);
        // TODO Auto-generated method stub

        int dif = 0;
        String return_value = "";
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
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

    private String[] splitfrom(String tf) {

        String k = tf.substring(11, tf.length() - 0);
        String[] v1 = {k};

        return v1;
    }

    @Override
    public int getItemCount() {
        return chatRoomDisplayArrayList.size();
    }

    public class ChatRoomMultipleAdapterNewRecycleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_chatroom_name)
        TextView txt_chatroom_name;
        @BindView(R.id.txt_created)
        TextView txt_created;
        @BindView(R.id.text_cartcount)
        TextView text_cartcount;
        @BindView(R.id.text_cartcountLay)
        RelativeLayout text_cartcountLay;

        @BindView(R.id.txt_chatroom_message)
        TextView txt_chatroom_message;
        @BindView(R.id.chat_time)
        TextView txt_chat_time;
        @BindView(R.id.len_closed)
        LinearLayout len_closed;
        @BindView(R.id.img_file)
        ImageView img_file;
        @BindView(R.id.profile_image)
        SimpleDraweeView profile_image;
        @BindView(R.id.card_view)
        RelativeLayout card_view;


        public ChatRoomMultipleAdapterNewRecycleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        void roeClick() {
            ((OpenChatroomActivity) context).setOnClickRow(getAdapterPosition());
        }
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
