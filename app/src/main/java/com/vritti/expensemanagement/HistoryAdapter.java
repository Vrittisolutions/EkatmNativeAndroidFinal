package com.vritti.expensemanagement;

/**
 * Created by sharvari on 18-Sep-19.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.vritti.ekatm.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Jerry on 12/19/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.AssistantViewHolder> {

    private ArrayList<ExpenseData> expenseDataArrayList = null;
    Context context;
    double currentTotal=0;
    double currentExpense;

    public HistoryAdapter(Context context, ArrayList<ExpenseData> expenseDataArrayList) {
        this.context=context;
        this.expenseDataArrayList = expenseDataArrayList;
    }

    @Override
    public void onBindViewHolder(final AssistantViewHolder holder, final int position) {
        final ExpenseData expenseData = expenseDataArrayList.get(position);


        if (expenseData.getAmount().equals("")){
            holder.txt_exp.setText("\u20B9 " + "0");
            holder.txt_tot_exp.setText("\u20B9 " + currentTotal);

        }else {

            currentExpense = Double.parseDouble(expenseData.getAmount());
            //currentExpense= Integer.parseInt(expenseData.getExp_amount());
            currentTotal = currentExpense + currentTotal;

          //  holder.txt_tot_exp.setText("\u20B9 " + currentTotal);

              holder.txt_tot_exp.setText("\u20B9 " + expenseData.getTot_prev_exp());

           // holder.txt_exp.setText("\u20B9 " + expenseData.getAmount());
            double d=Double.parseDouble(expenseData.getAmount());
            holder.txt_exp.setText(String.format("%.2f", d));
        }
        holder.txt_category.setText(expenseData.getCat_name());

        if (expenseData.getExpType()==null){
            holder.txt_subcategory.setText(expenseData.getExpType());

        }else {
            if (expenseData.getExpType().equals("")) {
                holder.txt_subcategory.setText(expenseData.getExpType());
            } else {

                if (expenseData.getVehicleType()==null){

                }else {

                        holder.txt_subcategory.setText(expenseData.getExpType() + "   -   " + expenseData.getVehicleType());
                    }


               /* if (expenseData.getTravelMode()==null){

                }else {
                    if (expenseData.getVehicleType().equalsIgnoreCase("Bus") || expenseData.getVehicleType().equalsIgnoreCase("Train") || expenseData.getVehicleType().equalsIgnoreCase("Taxi") || expenseData.getVehicleType().equalsIgnoreCase("Airplane")) {
                        holder.txt_subcategory.setText(expenseData.getExpType() + "   -   " + expenseData.getTravelMode());
                    }
                }
         */   }
        }
        holder.txtDate.setText(expenseData.getExpDate());
        if (expenseData==null){
            holder.txt_pay_mode.setText("Cash");
        }else {
            holder.txt_pay_mode.setText(expenseData.getPaymentMode());
        }
        String sub_category=expenseDataArrayList.get(position).getExpType();

        if (sub_category==null) {
        }else {
            if (sub_category.equalsIgnoreCase("Travelling")) {
                holder.len_source.setVisibility(View.VISIBLE);
                holder.txt_source.setText(expenseData.getFromLocation());
                holder.txt_dest.setText(expenseData.getToLocation());
                holder.txt_km.setText(expenseData.getDistance());
            } else {
                holder.len_source.setVisibility(View.GONE);
                holder.txt_subcategory.setText(expenseData.getExpType());


            }

        }
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Id=expenseData.getExpRecordId();
                ((HistoryActivity) context).chatuserdelete(Id);



            }
        });
        holder.img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditExpensesActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable) expenseDataArrayList);
                intent.putExtra("BUNDLE",args);
                intent.putExtra("pos",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });



        // If the message is a received message.




    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.history_item, parent, false);
        return new AssistantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(expenseDataArrayList==null)
        {
            expenseDataArrayList = new ArrayList<ExpenseData>();
        }
        return expenseDataArrayList.size();
    }
    public class AssistantViewHolder extends RecyclerView.ViewHolder {


        TextView txtDate,txt_category,txt_subcategory,txt_exp,txt_tot_exp,txt_pay_mode,txt_source,txt_dest,txt_km;
        LinearLayout len_source;
        ImageView img_edit,img_delete;

        public AssistantViewHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {
                txtDate = (TextView) itemView.findViewById(R.id.txtDate);
                txt_subcategory = (TextView) itemView.findViewById(R.id.txt_subcategory);
                txt_category = (TextView) itemView.findViewById(R.id.txt_category);
                txt_exp = (TextView) itemView.findViewById(R.id.txt_exp);
                txt_tot_exp = (TextView) itemView.findViewById(R.id.txt_tot_exp);
                txt_pay_mode = (TextView) itemView.findViewById(R.id.txt_pay_mode);
                txt_source = (TextView) itemView.findViewById(R.id.txt_source);
                txt_dest = (TextView) itemView.findViewById(R.id.txt_dest);
                txt_km = (TextView) itemView.findViewById(R.id.txt_km);
                len_source = (LinearLayout) itemView.findViewById(R.id.len_source);
                img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
                img_edit = (ImageView) itemView.findViewById(R.id.img_edit);


            }
        }
    }



}