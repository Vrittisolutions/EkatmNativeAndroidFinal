package com.vritti.vwb.Adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.ClaimSummayBean;
import com.vritti.vwb.vworkbench.ClaimRecordActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClaimRecordAdapter extends RecyclerView.Adapter<ClaimRecordAdapter.ClaimRecordHolder> {
    Context context;
    ArrayList<ClaimSummayBean> summayBeanArrayList;


    public ClaimRecordAdapter(Context context, ArrayList<ClaimSummayBean> summayBeanArrayList) {
        this.context = context;
        this.summayBeanArrayList = summayBeanArrayList;
    }

    @NonNull
    @Override
    public ClaimRecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_claim_record_row, parent, false);
        // itemView.setTag(viewType);
        return new ClaimRecordHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimRecordHolder holder, int position) {
        if (position % 2 == 0) {
            holder.claim_row.setBackgroundColor(ContextCompat.getColor(context, R.color.row_even));
        } else
            holder.claim_row.setBackgroundColor(ContextCompat.getColor(context, R.color.row_odd));

        ClaimSummayBean summayBean = summayBeanArrayList.get(position);


       holder.claimDate.setText(summayBean.getFormatedDate());
                holder.recordId.setText(summayBean.getClaimCode());
        holder.status.setText(summayBean.getStatus().split(" ")[0]);
        GradientDrawable  background = (GradientDrawable)holder.status.getBackground();
        GradientDrawable drawable = (GradientDrawable) holder.statusLayout1.getBackground();

        if (background instanceof GradientDrawable) {
            if (summayBean.getStatus().equals("Created")) {
                holder.updateLayout.setVisibility(View.GONE);
                //((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, R.color.orange));
                background.setColor(ContextCompat.getColor(context, R.color.orange));
                drawable.setStroke(2, ContextCompat.getColor(context, R.color.orange));
            } else if (summayBean.getStatus().equals("Approved")) {
                holder.updateLayout.setVisibility(View.GONE);
                // ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, R.color.green));
                background.setColor(ContextCompat.getColor(context, R.color.green));
                drawable.setStroke(2, ContextCompat.getColor(context, R.color.green));
            } else if (summayBean.getStatus().equals("Rejected")) {
                holder.updateLayout.setVisibility(View.VISIBLE);
                //((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, R.color.red));
                background.setColor(ContextCompat.getColor(context, R.color.red));
                drawable.setStroke(2, ContextCompat.getColor(context, R.color.red));
            } else if (summayBean.getStatus().equals("Booked In Finance")) {
                holder.updateLayout.setVisibility(View.GONE);
                //((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, R.color.red));
                background.setColor(ContextCompat.getColor(context, R.color.purpale));
                drawable.setStroke(2, ContextCompat.getColor(context, R.color.purpale));
            }
        }
        GradientDrawable  background1 = (GradientDrawable)holder.claimAmt.getBackground();
        GradientDrawable drawable1 = (GradientDrawable) holder.claimLayout.getBackground();
        background1.setColor(ContextCompat.getColor(context, R.color.orange));
        drawable1.setStroke(2, ContextCompat.getColor(context, R.color.orange));
        holder.claimName.setText(summayBean.getProjectName());
        holder.deptDesc.setText(summayBean.getDeptDesc());
        holder.paidAmt.setText(summayBean.getPaidAmount());
        holder.claimAmt.setText(summayBean.getTotal());


    }

    @Override
    public int getItemCount() {
        return summayBeanArrayList.size();
    }

    public class ClaimRecordHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.claim_row)
        RelativeLayout claim_row;
        @BindView(R.id.statusLayout)
        RelativeLayout statusLayout;
        @BindView(R.id.recordId)
        TextView recordId;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.claimName)
        TextView claimName;
        @BindView(R.id.claimDate)
        TextView claimDate;
        @BindView(R.id.deptDesc)
        TextView deptDesc;
        @BindView(R.id.paidAmt)
        TextView paidAmt;
        @BindView(R.id.claimAmt)
        TextView claimAmt;

        @BindView(R.id.statusLayout1)
        RelativeLayout statusLayout1;

        @BindView(R.id.updateLayout)
        LinearLayout updateLayout;
        @BindView(R.id.claimLayout)
        LinearLayout claimLayout;


        public ClaimRecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.edit)
        void editBtn(){
            ((ClaimRecordActivity)context).editClaim(getAdapterPosition() , false);
        }
        @OnClick(R.id.delete)
        void deleteBtn(){
            ((ClaimRecordActivity)context).editClaim(getAdapterPosition(), true);
        }
    }
}
