package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.PutAwayDetails;
import com.vritti.AlfaLavaModule.activity.PutAwayPacketDetails;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.ekatm.R;


import java.util.List;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class Adp_Putaways extends RecyclerView.Adapter<Adp_Putaways.ViewHolder> {
    private List<PutAwaysBean> mList;
    private static MyClickListener myClickListener;
    static Context context;


    public Adp_Putaways(List<PutAwaysBean> list) {
        this.mList = list;
    }

    @Override
    public Adp_Putaways.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alfa_item_putaway, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Adp_Putaways.ViewHolder holder, int i) {
        holder.tv_Grn_no.setGravity(Gravity.CENTER);
        holder.tv_Grn_no.setText(mList.get(i).getGRN_Number());

      /* holder.tv_Header.setText(mList.get(i).getGrnheaderid());
        holder.tv_total_count.setText(mList.get(i).getTotalcount());
        holder.tv_put_count.setText(mList.get(i).getPutCount());
        holder.tv_date.setText(mList.get(i).getAddeddt());*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(List<PutAwaysBean> dummyList) {
        mList = dummyList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_Header, tv_Grn_no, tv_total_count, tv_put_count, tv_date;
        private LinearLayout mLinear;

        public ViewHolder(final View view) {
            super(view);

            //  tv_Header = (TextView) view.findViewById(R.id.textView_grn_header);
            tv_Grn_no = (TextView) view.findViewById(R.id.tv_grn_no);
            mLinear = (LinearLayout) view.findViewById(R.id.card_item);
            context = itemView.getContext();
            Context aa = context;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            String s = mList.get(getPosition()).getGRN_Number();
            String isPacket = mList.get(getPosition()).getIsPacket();
            if (isPacket.equalsIgnoreCase("0")) {
                Intent intent = new Intent(itemView.getContext(), PutAwayDetails.class);
                intent.putExtra("GRN_No", mList.get(getPosition()).getGRN_Number());
                intent.putExtra("GRN_header", mList.get(getPosition()).getGRN_Header());
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(itemView.getContext(), PutAwayPacketDetails.class);
                intent.putExtra("GRN_No", mList.get(getPosition()).getGRN_Number());
                intent.putExtra("GRN_header", mList.get(getPosition()).getGRN_Header());
                context.startActivity(intent);
            }
        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
}