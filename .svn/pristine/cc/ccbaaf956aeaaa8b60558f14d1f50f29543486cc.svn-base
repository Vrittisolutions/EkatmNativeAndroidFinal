package com.vritti.AlfaLavaModule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPacketEnquiryDetail extends RecyclerView.Adapter<AdapterPacketEnquiryDetail.ViewHolder> {

    private ArrayList<Packet> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;

    public AdapterPacketEnquiryDetail(ArrayList<Packet> countries) {
        this.list = countries;
    }

    @Override
    public AdapterPacketEnquiryDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.packet_enquiry_details, viewGroup, false);

        return new AdapterPacketEnquiryDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPacketEnquiryDetail.ViewHolder holder, int position) {
        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());//tv_item_code
        holder.tv_packet.setText(list.get(position).getPacketNo());//tv_item_code
        holder.tv_loc_type.setText(list.get(position).getLocationType());
        holder.tv_loc_code.setText(list.get(position).getLocationCode());
        holder.tv_bal_qty.setText(list.get(position).getBalQty());
        holder.tv_moved.setText(list.get(position).getMovedQty());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getPacketNo(int position) {
        return list.get(position).getPacketNo();
    }

    public void update(ArrayList<Packet> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemcode;
        TextView tv_orderno;
        TextView tv_item_desc;
        TextView tv_moved;
        TextView tv_loc_code;
        TextView tv_loc_type;
        TextView tv_packet,tv_bal_qty;
         public ViewHolder(View view) {
             super(view);
             tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
             tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);//tv_grn_no
             tv_item_desc= (TextView) view.findViewById(R.id.tv_item_desc);
             tv_bal_qty = (TextView) view.findViewById(R.id.tv_bal_qty);
             tv_packet = (TextView) view.findViewById(R.id.tv_packet);
             tv_moved = (TextView) view.findViewById(R.id.tv_moved);
             tv_loc_code = (TextView) view.findViewById(R.id.tv_loc_code);
             tv_loc_type = (TextView) view.findViewById(R.id.tv_loc_type);
             view.setTag(view);
         }
    }

}