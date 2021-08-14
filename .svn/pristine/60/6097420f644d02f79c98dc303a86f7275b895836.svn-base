package com.vritti.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.chat.activity.GroupmemberShowActivity;
import com.vritti.chat.bean.ChatUser;
import com.vritti.ekatm.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatGroupMemberlistAdapter extends RecyclerView.Adapter<ChatGroupMemberlistAdapter.ChatGroupHolder> {
    Context context;
    ArrayList<ChatUser> chatUserArrayList;


    public ChatGroupMemberlistAdapter(Context context, ArrayList<ChatUser> chatUserArrayList) {
        this.context = context;
        this.chatUserArrayList = chatUserArrayList;
    }

    @NonNull
    @Override
    public ChatGroupHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_group_item_list, parent, false);

        return new ChatGroupHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGroupHolder holder, int position) {

        if (position % 2 == 0) {
            holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_one_color));
        } else {
            holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_two_color));
        }

        holder.txt_user_name_data.setText(chatUserArrayList.get(position).getParticipantName());
        String CreatorId = chatUserArrayList.get(position).getCreater();
        String ParticipantId = chatUserArrayList.get(position).getParticipantId();

        String imagepath = chatUserArrayList.get(position).getImagePath();

        if (imagepath == null) {
            holder.profileImag.setBackground(context.getResources().getDrawable(R.drawable.single_user_icon));
        } else {
            holder.profileImag.setBackground(null);
            holder.profileImag.setImageURI(imagepath);
        }
        String UserMasterId = chatUserArrayList.get(position).getUserMasterId();


        String ChatType = chatUserArrayList.get(position).getChatType();

        if (ChatType.equals("P")) {
            holder.txt_admin.setVisibility(View.GONE);

        } else {


            if (CreatorId == null || ParticipantId == null) {

            } else {

                if (CreatorId.equals(ParticipantId)) {
                    holder.txt_admin.setVisibility(View.VISIBLE);
                    holder.delete.setVisibility(View.GONE);
                } else {
                    holder.txt_admin.setVisibility(View.GONE);
                    if (CreatorId.equalsIgnoreCase(UserMasterId))
                        holder.delete.setVisibility(View.VISIBLE);
                    else
                        holder.delete.setVisibility(View.GONE);
                }
            }
        }
    }

    /*
if (CreatorId.equalsIgnoreCase(UserMasterId)) {

            holder.img_delete.setVisibility(View.VISIBLE);
        } else {
            holder.img_delete.setVisibility(View.GONE);
        }

        if (ChatType.equals("P")){
            holder.txt_admin.setVisibility(View.GONE);

        }else {


            if (CreatorId == null || ParticipantId == null) {

            } else {

                if (CreatorId.equals(ParticipantId)) {
                    holder.txt_admin.setVisibility(View.VISIBLE);
                    holder.img_delete.setVisibility(View.GONE);

                } else {
                    holder.txt_admin.setVisibility(View.GONE);

                }
            }
        }

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ParticipantId=chatUserArrayList.get(position).getParticipantId();
                String Chatroomid=chatUserArrayList.get(position).getChatRoomId();
                ((GroupmemberShowActivity) context).chatuserdelete(ParticipantId,Chatroomid, context);
                 notifyDataSetChanged();
            }
        });


    * */

    @Override
    public int getItemCount() {
        return chatUserArrayList.size();
    }

    public class ChatGroupHolder extends RecyclerView.ViewHolder {
        TextView txt_user_name_data;
        TextView txt_admin;
        RelativeLayout len;
        @BindView(R.id.profile_image)
        SimpleDraweeView profileImag;
        @BindView(R.id.delete)
        ImageView delete;

        public ChatGroupHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            txt_user_name_data = itemView.findViewById(R.id.user_name_data);
            txt_admin = itemView.findViewById(R.id.txt_admin);
            len = itemView.findViewById(R.id.len);

        }

        @OnClick(R.id.delete)
        void deleteUserName() {
            ((GroupmemberShowActivity) context).chatuserdelete(getAdapterPosition());
        }
    }
}
