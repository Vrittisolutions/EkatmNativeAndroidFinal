package com.vritti.vwb.Adapter;

import android.content.Context;
import android.icu.util.IslamicCalendar;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.ClaimDetailsBean;
import com.vritti.vwb.vworkbench.ClaimNewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class ClaimDetailsRecycleAdapter extends RecyclerView.Adapter<ClaimDetailsRecycleAdapter.ClaimHolder> {
    List<ClaimDetailsBean> claimDetailsBeanList ;
    Context context;
    public ClaimDetailsRecycleAdapter(Context context, List<ClaimDetailsBean> lsCalimDetails) {
        claimDetailsBeanList = lsCalimDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ClaimHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_claim_details_item, parent, false);
        // itemView.setTag(viewType);
        return new ClaimHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimHolder holder, int position) {
        holder.tv_Cdate.setText(claimDetailsBeanList.get(position).getClaimDate());
       holder.tv_Camount.setText(claimDetailsBeanList.get(position).getAmount());
        holder.fromPlace.setText(claimDetailsBeanList.get(position).getFromPlace());
        holder.ToPlace.setText(claimDetailsBeanList.get(position).getToPlace());
        holder.tv_food.setText(claimDetailsBeanList.get(position).getTv_food());
        holder.tv_Local.setText(claimDetailsBeanList.get(position).getTv_Local());
        holder.tv_lodging.setText(claimDetailsBeanList.get(position).getTv_lodging());
        holder.tv_Maintenanace.setText(claimDetailsBeanList.get(position).getTv_Maintenanace());
        holder.tv_mode.setText(claimDetailsBeanList.get(position).getTv_mode());
        holder.tv_Ph.setText(claimDetailsBeanList.get(position).getTv_Ph());
        holder.tv_travelling.setText(claimDetailsBeanList.get(position).getTv_travelling());
        holder.tv_distance.setText(claimDetailsBeanList.get(position).getDistance());
        holder.tv_Camount.setText(claimDetailsBeanList.get(position).getAmount());
        if(claimDetailsBeanList.get(position).getClaimDate().equals("All Total")){
            holder.ToPlace.setVisibility(View.GONE);
            holder.fromPlace.setVisibility(View.GONE);
            holder.tv_mode.setVisibility(View.GONE);
            holder.to.setVisibility(View.GONE);
            holder.by.setVisibility(View.GONE);
            holder.tv_claim_edit.setVisibility(View.GONE);
            holder.tv_claim_cancel.setVisibility(View.GONE);

        }else {
            holder.ToPlace.setVisibility(View.VISIBLE);
            holder.fromPlace.setVisibility(View.VISIBLE);
            holder.tv_mode.setVisibility(View.VISIBLE);
            holder.to.setVisibility(View.VISIBLE);
            holder.by.setVisibility(View.VISIBLE);
            holder.tv_claim_edit.setVisibility(View.VISIBLE);
            holder.tv_claim_cancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return claimDetailsBeanList.size();
    }

    public class ClaimHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_clim_date)
        TextView tv_Cdate;
        @BindView(R.id.tv_Total)
        TextView tv_Camount;
        @BindView(R.id.fromPlace)
        TextView fromPlace;
        @BindView(R.id.ToPlace)
        TextView ToPlace;
        @BindView(R.id.tv_mode_new)
        TextView tv_mode;
        @BindView(R.id.tv_travelling)
        TextView tv_travelling;
        @BindView(R.id.tv_lodging)
        TextView tv_lodging;
        @BindView(R.id.tv_food)
        TextView tv_food;
        @BindView(R.id.tv_Local)
        TextView tv_Local;
        @BindView(R.id.tv_Ph)
        TextView tv_Ph;
        @BindView(R.id.tv_Maintenanace)
        TextView tv_Maintenanace;
        @BindView(R.id.tv_distance)
        TextView tv_distance;
        @BindView(R.id.tv_claim_edit)
        TextView tv_claim_edit;
        @BindView(R.id.tv_claim_cancel)
        TextView tv_claim_cancel;
        @BindView(R.id.to)
        TextView to;
        @BindView(R.id.byText)
        TextView by;


        public ClaimHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this , itemView);
        }
        @OnClick(R.id.tv_claim_cancel)
        void deleteClaim()
        {
            ((ClaimNewActivity)context).deleteClaim(getAdapterPosition());
        }
        @OnClick(R.id.tv_claim_edit)
        void editClaim()
        {
            ((ClaimNewActivity)context).editClaim(getAdapterPosition());
        }
    }
}
