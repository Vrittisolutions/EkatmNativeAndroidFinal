package com.vritti.AlfaLavaModule.activity.packing_qc;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.CartonDetail;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterQCCartonDetail extends RecyclerView.Adapter<AdapterQCCartonDetail.ViewHolder> {
    private ArrayList<CartonDetail> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;
    Context context;

    double currentTotal=0;
    double currentQty;

    public AdapterQCCartonDetail(ArrayList<CartonDetail> countries, Context context) {
        this.list = countries;
        this.context=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.do_secondary_details, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());

        String Pick = String.valueOf(list.get(position).getQty()).split("\\.")[0];
        holder.tv_orderno.setText("Qty : " + Pick);




        currentQty = Double.parseDouble(Pick);

        //currentExpense= Integer.parseInt(expenseData.getExp_amount());
        currentTotal = currentQty + currentTotal;


/*
        ((PickListDetailActivity) context).Qtytotal(currentTotal);
*/



        holder.tv_location.setVisibility(View.VISIBLE);
        holder.tv_location.setText("Packet : " + list.get(position).getPacketNo());
        holder.tv_location.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));


    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void update(ArrayList<CartonDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }





    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemcode;
        TextView tv_orderno;
        TextView tv_item_desc;
        TextView tv_packet,tv_lot,tv_location,tv_picked;
        LinearLayout card;
        public ViewHolder(View view) {
            super(view);
            tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
            tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);//tv_grn_no
            tv_item_desc= (TextView) view.findViewById(R.id.tv_item_desc);
            tv_lot = (TextView) view.findViewById(R.id.tv_lot);
            tv_packet = (TextView) view.findViewById(R.id.tv_packet);
            tv_location = (TextView) view.findViewById(R.id.tv_location);
            tv_picked = (TextView) view.findViewById(R.id.tv_picked);
            card = (LinearLayout) view.findViewById(R.id.card_item);

            tv_picked.setVisibility(View.GONE);
            tv_location.setVisibility(View.VISIBLE);

            view.setTag(view);
        }
    }


}