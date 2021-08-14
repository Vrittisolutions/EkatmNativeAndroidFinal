package com.vritti.crm.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.crm.bean.FilteredProspect;
import com.vritti.ekatm.R;

import java.util.List;

/**
 * Created by sharvari on 10-Feb-17.
 */

public class FilteredProspectAdapter extends RecyclerView.Adapter<FilteredProspectAdapter.FPViewHolder> {

    List<FilteredProspect> prospectList;
    static Context context;

    public FilteredProspectAdapter(List<FilteredProspect> prospectList) {
        this.prospectList = prospectList;
    }
    public interface ItemClick {
        void  onClick(View view, int position, boolean isLongClick);

    }
    public static class FPViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener  {
        TextView txtFName, txtName;
        TextView txtcity, txtNumber;
        TextView txtItemdesc, txtEmail;
        TextView txtcallclose, txtcallopen, txtAddress;
        LinearLayout lay_header, llName;
        ImageView click;
        private ItemClick  clickListener;
        public FPViewHolder(View itemView) {
            super(itemView);
            //context = itemView.getContext();

            txtFName = (TextView) itemView.findViewById(R.id.txtFName);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtcity = (TextView) itemView.findViewById(R.id.txtcity);
            txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
            txtItemdesc = (TextView) itemView.findViewById(R.id.txtItemdesc);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtcallclose = (TextView) itemView.findViewById(R.id.txtcallclose);
            txtcallopen = (TextView) itemView.findViewById(R.id.txtcallopen);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            llName = (LinearLayout) itemView.findViewById(R.id.llName);
            lay_header = (LinearLayout) itemView.findViewById(R.id.lay_header);
            click = (ImageView) itemView.findViewById(R.id.click);
            itemView.setTag(itemView);

        }

        public void setClickListener(ItemClick itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }

    @Override
    public FPViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.crm_custom_filtered_prospect, viewGroup, false);
        FPViewHolder fpvh = new FPViewHolder(v);
        return fpvh;

    }

    @Override
    public void onBindViewHolder( final FPViewHolder holder, final int position) {
       //  int j = position;
        if (position % 2 == 1) {
            holder.lay_header.setBackgroundColor(Color.parseColor("#CBDFE2"));
        } else {
            holder.lay_header.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
        holder.txtFName.setText(prospectList.get(position).getFirmName());
        holder.txtcity.setText(prospectList.get(position).getCityName());
        holder.txtItemdesc.setText(prospectList.get(position).getFamilyDesc());
        holder.txtcallclose.setText(prospectList.get(position).getOpenCalls() + " Close call");
        holder.txtcallopen.setText(prospectList.get(position).getCloseCalls() + " Open call");
        holder.txtAddress.setText(prospectList.get(position).getAddress());
holder.setClickListener(new ItemClick() {
    @Override
    public void onClick(View view, int position, boolean isLongClick) {

    }
});
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.llName.getVisibility() == View.VISIBLE) {
                    holder.llName.setVisibility(View.GONE);
                } else {
                    holder.llName.setVisibility(View.VISIBLE);
                    holder.txtName.setText(prospectList.get(position).getContactName());
                    holder.txtNumber.setText(prospectList.get(position).getTelephone() + "/" +
                            prospectList.get(position).getMobile());
                    holder.txtEmail.setText(prospectList.get(position).getEmailId());
                }


            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return prospectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
