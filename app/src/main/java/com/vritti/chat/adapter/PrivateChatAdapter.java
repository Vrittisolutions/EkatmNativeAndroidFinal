package com.vritti.chat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.chat.activity.PrivateChatActvity;
import com.vritti.chat.bean.ChatUser;
import com.vritti.chat.bean.DefaultUser;
import com.vritti.chat.bean.PrivateUser;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by pradnya on 10/13/16.
 */
public class PrivateChatAdapter extends RecyclerView.Adapter<PrivateChatAdapter.PrivateHolder> {
    Context context;
    ArrayList<PrivateUser> privateArrayList;
    ArrayList<PrivateUser> arraylist;

    public PrivateChatAdapter(Context context, ArrayList<PrivateUser> privateArrayList) {
        this.context = context;
        this.privateArrayList = privateArrayList;
        arraylist = privateArrayList;
    }

    @NonNull
    @Override
    public PrivateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_group_item_list, parent, false);

        return new PrivateHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivateHolder holder, int position) {
        if (privateArrayList.size()>0) {
            holder.len.setVisibility(View.VISIBLE);
            holder.txt_user_name_data.setText(privateArrayList.get(position).getUsername());
            if (position % 2 == 0) {
                holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_one_color));
            } else {
                holder.len.setBackgroundColor(context.getResources().getColor(R.color.card_two_color));
            }
        }
    }

    @Override
    public int getItemCount() {
        return privateArrayList.size();
    }

   /* public void filter(String charText) {
        // public ArrayList<PrivateUser> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        privateArrayList.clear();
        if (charText.length() == 0) {
            privateArrayList.addAll(arraylist);
        } else {
            for (PrivateUser wp : arraylist) {
                if (wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)) {
                    privateArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        //    return privateArrayList;


    }*/

    public class PrivateHolder extends RecyclerView.ViewHolder {
        TextView txt_user_name_data;
        TextView txt_admin;

        RelativeLayout len;
        AppCompatCheckBox checkbox_user;

        public PrivateHolder(View itemView) {
            super(itemView);
            txt_user_name_data = itemView.findViewById(R.id.user_name_data);
            txt_admin = itemView.findViewById(R.id.txt_admin);
            len = itemView.findViewById(R.id.len);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.len)
        void rowclick() {
            ((PrivateChatActvity) context).rowMessage(getAdapterPosition());
        }
    }
}
