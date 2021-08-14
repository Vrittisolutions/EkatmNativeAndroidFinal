package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.DOPackingScanDetails;
import com.vritti.AlfaLavaModule.activity.PacketNoDisplayActivity;
import com.vritti.AlfaLavaModule.activity.PickListDetailActivity;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterSecondaryDetail extends RecyclerView.Adapter<AdapterSecondaryDetail.ViewHolder> {

    private  Context context;
    private ArrayList<SecondaryBox> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;
    SharedPreferences userpreferences;
    double currentTotal=0;
    double currentQty;

    public AdapterSecondaryDetail(ArrayList<SecondaryBox> countries) {
        this.list = countries;

    }

    @Override
    public AdapterSecondaryDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.do_secondary_details, viewGroup, false);

        return new AdapterSecondaryDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterSecondaryDetail.ViewHolder holder, int position) {
        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());
        holder.tv_orderno.setText("Require : "+ list.get(position).getQtyToPack());
        String flag=list.get(position).getFlag();
     //   holder.tv_location.setText("Location: FT-C-38");

        String Pick = String.valueOf(list.get(position).getQtyToPack()).split("\\.")[0];
        holder.tv_orderno.setText("Require : " + Pick);

        String Packed = String.valueOf(list.get(position).getQtyPacked()).split("\\.")[0];
        holder.tv_picked.setText("Packed : "+ Packed);



        //   ((PickListDetailActivity) context).Qtytotal(currentTotal);

        if (Pick.equals(Packed)){
            holder.card_item.setBackgroundColor(context.getResources().getColor(R.color.alfa_green));
        }else {
            holder.card_item.setBackgroundColor(context.getResources().getColor(R.color.barMeduimColor));
        }



    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void update(ArrayList<SecondaryBox> list) {
        this.list = list;
        notifyDataSetChanged();
    }





    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView tv_itemcode;
        TextView tv_orderno;
        TextView tv_item_desc;
        TextView tv_packet,tv_lot,tv_location,tv_picked;
        LinearLayout card_item;
         public ViewHolder(View view) {
             super(view);
             tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
             tv_orderno = (TextView) view.findViewById(R.id.tv_orderno);//tv_grn_no
             tv_item_desc= (TextView) view.findViewById(R.id.tv_item_desc);
             tv_lot = (TextView) view.findViewById(R.id.tv_lot);
             tv_packet = (TextView) view.findViewById(R.id.tv_packet);
             card_item = (LinearLayout) view.findViewById(R.id.card_item);
             tv_location = (TextView) view.findViewById(R.id.tv_location);
             tv_picked = (TextView) view.findViewById(R.id.tv_picked);
             view.setOnClickListener(this);
             context = itemView.getContext();

             view.setTag(view);
         }

        @Override
        public void onClick(View v) {

            String ItemMasterId = list.get(getPosition()).getItemMasterId();
            String Pack_OrdHdrId = list.get(getPosition()).getPack_OrdHdrId();
            String SoScheduleId = list.get(getPosition()).getSoScheduleId();
            int Pack = list.get(getPosition()).getQtyToPack();
            int Packed = list.get(getPosition()).getQtyPacked();
            String ItemCode = list.get(getPosition()).getItemCode();

            if (Constants.type == Constants.Type.Alfa) {

                ((DOPackingScanDetails) context).createsecondary(ItemMasterId, Pack_OrdHdrId, SoScheduleId);
            }else {
             //   ((DOPackingScanDetails) context).createsecondary_1(ItemMasterId, Pack_OrdHdrId, SoScheduleId,Pack,Packed,ItemCode);
                  ((DOPackingScanDetails) context).showpackestscan();

            }


        }
    }

}