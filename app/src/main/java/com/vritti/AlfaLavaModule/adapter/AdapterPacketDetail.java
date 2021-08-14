package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.PI.PacketScanDetails;
import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPacketDetail extends RecyclerView.Adapter<AdapterPacketDetail.ViewHolder> {

    private ArrayList<PutAwayDetail> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;
    Context context;

    public AdapterPacketDetail(ArrayList<PutAwayDetail> countries,Context context) {
        this.list = countries;
        this.context=context;
    }

    @Override
    public AdapterPacketDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.packet_scan_details_item, viewGroup, false);

        return new AdapterPacketDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPacketDetail.ViewHolder holder, int position) {
        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());//tv_item_code
        holder.tv_lot.setText(list.get(position).getLotNo());
        holder.tv_location.setText(list.get(position).getLocationCode());
        holder.tv_fifo.setText(list.get(position).getFIFODate());


        holder.card_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PacketScanDetails)context).clickitem();

            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getPacketNo(int position) {
        return list.get(position).getPacketNo();
    }

    public void update(ArrayList<PutAwayDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemcode;
        TextView tv_item_desc,tv_location,tv_fifo;
        TextView tv_lot;
        LinearLayout card_item;
         public ViewHolder(View view) {
             super(view);
             tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
             tv_item_desc= (TextView) view.findViewById(R.id.tv_item_desc);
             tv_lot = (TextView) view.findViewById(R.id.tv_lot);
             tv_location = (TextView) view.findViewById(R.id.tv_location);
             tv_fifo = (TextView) view.findViewById(R.id.tv_fifo);
             card_item = (LinearLayout) view.findViewById(R.id.card_item);
             view.setTag(view);
         }
    }

}