package com.vritti.AlfaLavaModule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPutHandDetail extends RecyclerView.Adapter<AdapterPutHandDetail.ViewHolder> {

    private ArrayList<PutAwayDetail> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;

    public AdapterPutHandDetail(ArrayList<PutAwayDetail> countries) {
        this.list = countries;
    }

    @Override
    public AdapterPutHandDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.putaway_hand_details, viewGroup, false);

        return new AdapterPutHandDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPutHandDetail.ViewHolder holder, int position) {
        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_orderno.setText("Order No:"+list.get(position).getOrderNo());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());//tv_item_code
        holder.tv_packet.setText(list.get(position).getPacketNo());//tv_item_code
        holder.tv_lot.setText(list.get(position).getLotNo());
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
        TextView tv_orderno;
        TextView tv_item_desc;
        TextView tv_packet,tv_lot;
         public ViewHolder(View view) {
             super(view);
             tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
             tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);//tv_grn_no
             tv_item_desc= (TextView) view.findViewById(R.id.tv_item_desc);
             tv_lot = (TextView) view.findViewById(R.id.tv_lot);
             tv_packet = (TextView) view.findViewById(R.id.tv_packet);
             view.setTag(view);
         }
    }

}