package com.vritti.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.chat.activity.UserListDisplayActivity;
import com.vritti.chat.bean.ChatUser;
import com.vritti.ekatm.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUserAdapter extends RecyclerView.Adapter<AddUserAdapter.AddUserHolder> {
    Context context;
    ArrayList<ChatUser> chatUserArrayList;

    public AddUserAdapter(Context context, ArrayList<ChatUser> chatUserArrayList) {
        this.context = context;
        this.chatUserArrayList = chatUserArrayList;
    }

    @NonNull
    @Override
    public AddUserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_group_item_list, parent, false);
        return new AddUserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddUserHolder holder, int position) {
        if (chatUserArrayList.size()>0) {
            holder.len.setVisibility(View.VISIBLE);
            holder.checkbox_user.setVisibility(View.VISIBLE);
            holder.user_name_data.setText(chatUserArrayList.get(position).getUsername());
            holder.checkbox_user.setChecked(chatUserArrayList.get(position).isSelected());
            if (position % 2 == 0) {
                holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_one_color));
            } else {
                holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_two_color));
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatUserArrayList.size();
    }



    public class AddUserHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.user_name_data)
        TextView user_name_data;
        TextView txt_admin;
        @BindView(R.id.len)
        RelativeLayout len;
      //  AppCompatCheckBox checkbox_user;
        @BindView(R.id.checkbox_user)
        AppCompatCheckBox checkbox_user;

        public AddUserHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick({R.id.checkbox_user })
        void chatUser(){
            ((UserListDisplayActivity)context).setUserSelectiom(checkbox_user.isChecked() , getAdapterPosition());
           /* chatUserArrayList.get(position).setSelected(isSelected);
            tempChatUserArrayList.add(chatUserArrayList.get(position));*/
        }
        @OnClick({ R.id.len , R.id.user_name_data})
        void chatnewUser(){
            if(checkbox_user.isChecked()){
                checkbox_user.setChecked(false);
            }else {
                checkbox_user.setChecked(true);
            }
            chatUser();
           /* chatUserArrayList.get(position).setSelected(isSelected);
            tempChatUserArrayList.add(chatUserArrayList.get(position));*/
        }
    }
}