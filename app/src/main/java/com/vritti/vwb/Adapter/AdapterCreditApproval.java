package com.vritti.vwb.Adapter;

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
import com.vritti.vwb.Beans.CreditApproval;

import java.util.List;

import static com.vritti.crm.vcrm7.TravelPlanShowActivity.formateDateFromstring;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class AdapterCreditApproval extends RecyclerView.Adapter<AdapterCreditApproval.ViewHolder> {
    private List<CreditApproval> mList;
    static Context context;


    public AdapterCreditApproval(List<CreditApproval> list) {
        this.mList = list;
    }

    @Override
    public AdapterCreditApproval.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_approval_item_lay, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(AdapterCreditApproval.ViewHolder holder, int i) {

        holder.txt_amount.setText("Rs."+mList.get(i).getCreditApprovalAmt());
        holder.txt_custname.setText(mList.get(i).getCustVendorName());
        holder.txt_approve.setText(mList.get(i).getApprovalStatus());
        String date=mList.get(i).getAddedDt();
        String[] namesList = date.split("T");
        String name1 = namesList [0];


        String date_after = formateDateFromstring("yyyy-MM-dd", "dd/MM/yyyy", name1);

        holder.txt_date.setText(date_after);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(List<CreditApproval> dummyList) {
        mList = dummyList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txt_approve, txt_custname, txt_amount,txt_date;
        private LinearLayout mLinear;

        public ViewHolder(final View view) {
            super(view);

            txt_approve = (TextView) view.findViewById(R.id.txt_approve);
            txt_custname = (TextView) view.findViewById(R.id.txt_custname);
            txt_amount = (TextView) view.findViewById(R.id.txt_amount);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            context = itemView.getContext();
            Context aa = context;

        }

        }

}