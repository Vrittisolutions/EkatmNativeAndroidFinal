package com.vritti.AlfaLavaModule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.ekatm.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPutawayPacketDetail extends RecyclerView.Adapter<AdapterPutawayPacketDetail.ViewHolder> {

    private ArrayList<PutawaysPacketBean> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;

    public AdapterPutawayPacketDetail(ArrayList<PutawaysPacketBean> countries) {
        this.list = countries;
    }

    @Override
    public AdapterPutawayPacketDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alfa_item_putaway_packet_detail, viewGroup, false);

        return new AdapterPutawayPacketDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPutawayPacketDetail.ViewHolder holder, int position) {
        holder.tv_Location.setText(list.get(position).getLocationCode());
        holder.tv_Quantity.setText(list.get(position).getBalQty());
        holder.tv_item_code.setText(list.get(position).getItemCode());//tv_item_code
        holder.tv_Packet_no.setText(list.get(position).getPacketNo());//tv_item_code
        holder.tv_item.setText(list.get(position).getItemdesc());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getPacketNo(int position) {
        return list.get(position).getPacketNo();
    }

    public void update(ArrayList<PutawaysPacketBean> list) {
        this.list = list;
    }


    public void update1(List<PutawaysPacketBean> dummyList) {
        list = (ArrayList<PutawaysPacketBean>) dummyList;
        notifyDataSetChanged();
    }

    public void updateLocation(List<PutawaysPacketBean> dummyList) {
        list = (ArrayList<PutawaysPacketBean>) dummyList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Quantity;
        TextView tv_Location;
        TextView tv_Packet_no;
        TextView tv_item_code,tv_item;
         public ViewHolder(View view) {
             super(view);
             tv_Quantity = (TextView) view.findViewById(R.id.edtQuantity);
             tv_Location = (TextView) view.findViewById(R.id.edtlocation);//tv_grn_no
             tv_item_code= (TextView) view.findViewById(R.id.tv_Packet);
             tv_item = (TextView) view.findViewById(R.id.tv_packet_display);
             tv_Packet_no = (TextView) view.findViewById(R.id.tv_itemname);
             view.setTag(view);
         }
    }

}