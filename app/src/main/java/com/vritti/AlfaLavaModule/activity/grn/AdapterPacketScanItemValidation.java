package com.vritti.AlfaLavaModule.activity.grn;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;


import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPacketScanItemValidation extends RecyclerView.Adapter<AdapterPacketScanItemValidation.ViewHolder> {

    private ArrayList<GRNPOST> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;
    Context context;

    public AdapterPacketScanItemValidation(ArrayList<GRNPOST> countries, Context context) {
        this.list = countries;
        this.context=context;
    }

    @Override
    public AdapterPacketScanItemValidation.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grn_packet_item_validation, viewGroup, false);

        return new AdapterPacketScanItemValidation.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPacketScanItemValidation.ViewHolder holder, final int position) {
        holder.tv_grn_no.setText(list.get(position).getGRNNo());
        holder.tv_item_code.setText(list.get(position).getItemCode());//tv_item_code
        holder.challan_qty.setText("Challan Qty : "+ list.get(position).getChallanQty());//tv_item_code
        holder.txt_invoice.setText("Invoice no : "+list.get(position).getInvoiceNo());
        holder.txt_cust.setText(list.get(position).getCustomerName());
        int RejQt=list.get(position).getRejQty();

        if (RejQt>0){
            holder.tv_quantity.setText("Received : "+ list.get(position).getQuantity() +" / "+"Reject : "+ list.get(position).getRejQty());//tv_item_code
        }else {
            holder.tv_quantity.setText("Received : "+ list.get(position).getQuantity());//tv_item_code
        }


        if (list.size()>0){
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

        int challan=list.get(position).getChallanQty();
        int total=list.get(position).getRejQty()+list.get(position).getQuantity();

        if (list.get(position).getChallanQty()==list.get(position).getQuantity()){

            holder.card.setBackgroundColor(context.getResources().getColor(R.color.green));

        }else if (list.get(position).getChallanQty()==list.get(position).getRejQty()){

            holder.card.setBackgroundColor(context.getResources().getColor(R.color.green));
        }else if (challan==total){
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        else  {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.white));
        }

       holder.card.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ((GRNPOSTPACKETSacnDetails) context).showpackets(list.get(position).getGRNDetailId());

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

    public void update(ArrayList<GRNPOST> list) {
        this.list = list;
        notifyDataSetChanged();
    }





    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_grn_no;
        TextView tv_packet_no;
        TextView tv_item_code;
        TextView tv_quantity;
        TextView challan_qty;
        TextView txt_cust;
        TextView txt_invoice;
        CardView card;
         public ViewHolder(View view) {
             super(view);
             tv_grn_no = (TextView) view.findViewById(R.id.tv_grn_no);
             tv_packet_no = (TextView) view.findViewById(R.id.tv_packet_no);//tv_grn_no
             tv_item_code= (TextView) view.findViewById(R.id.tv_item_code);
             tv_quantity = (TextView) view.findViewById(R.id.tv_quantity);
             challan_qty = (TextView) view.findViewById(R.id.challan_qty);
             txt_invoice = (TextView) view.findViewById(R.id.txt_invoice);
             txt_cust = (TextView) view.findViewById(R.id.txt_cust);
             card = (CardView) view.findViewById(R.id.card);
             view.setTag(view);
         }
    }

}