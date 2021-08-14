package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.BoxmasterActivity;
import com.vritti.AlfaLavaModule.activity.PacketNoDisplayActivity;
import com.vritti.AlfaLavaModule.activity.PacketNoEnquiryActivity;
import com.vritti.AlfaLavaModule.activity.PickListDetailActivity;
import com.vritti.AlfaLavaModule.activity.picking.ItemWisePickListDetailActivity;
import com.vritti.AlfaLavaModule.bean.MRSDetailBean;
import com.vritti.AlfaLavaModule.bean.PickListDetail;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPicklistDetail extends RecyclerView.Adapter<AdapterPicklistDetail.ViewHolder> {
    private ArrayList<PickListDetail> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;
    Context context;

    double currentTotal=0;
    double currentQty;

    public AdapterPicklistDetail(ArrayList<PickListDetail> countries,Context context) {
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

        String Pick = String.valueOf(list.get(position).getQtyToPick()).split("\\.")[0];
        holder.tv_orderno.setText("Pick : " + Pick);

        String Picked = String.valueOf(list.get(position).getQtyPicked()).split("\\.")[0];


        if (Pick.equals(Picked)) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.alfa_green ));
        } else {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.barMeduimColor));

        }



        holder.tv_picked.setText("Picked : " + Picked);




        currentQty = Double.parseDouble(Pick);

        //currentExpense= Integer.parseInt(expenseData.getExp_amount());
        currentTotal = currentQty + currentTotal;


/*
        ((PickListDetailActivity) context).Qtytotal(currentTotal);
*/



        holder.tv_location.setVisibility(View.VISIBLE);
        holder.tv_location.setText("Location : " + list.get(position).getLocationCode());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String PicklistDtl=list.get(position).getPick_listDtlId();

                ((ItemWisePickListDetailActivity) context).showpacket(PicklistDtl);



               /* context.startActivity(new Intent(context, PacketNoEnquiryActivity.class)
                        .putExtra("header","1PDK279055S00001070")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));*/

            }
        });

    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void update(ArrayList<PickListDetail> list) {
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


            view.setTag(view);
        }
    }


}