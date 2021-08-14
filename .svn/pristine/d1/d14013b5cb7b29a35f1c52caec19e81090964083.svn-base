package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.PacketNoEnquiryActivity;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.ekatm.R;

import java.util.List;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class Adp_PacketDisplay extends RecyclerView.Adapter<Adp_PacketDisplay.ViewHolder> {
    private List<Packet> mList;
    private static MyClickListener myClickListener;
    static Context context;


    public Adp_PacketDisplay(List<Packet> list) {
        this.mList = list;
    }

    @Override
    public Adp_PacketDisplay.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Adp_PacketDisplay.ViewHolder holder, int i) {
        holder.tv_Grn_no.setText(mList.get(i).getPacketNo());
        holder.tv_Grn_no.setGravity(Gravity.LEFT);
        holder.tv_Grn_no.setTextSize(16);
        holder.tv_Grn_no.setTextColor(Color.BLACK);
        holder.tv_Grn_no.setPadding(20,0,0,0);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(List<Packet> dummyList) {
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

            String Packetno=mList.get(getPosition()).getPacketNo();
            context.startActivity(new Intent(context, PacketNoEnquiryActivity.class).putExtra("header",Packetno).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

        }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
}