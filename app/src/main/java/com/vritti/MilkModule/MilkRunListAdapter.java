package com.vritti.MilkModule;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MilkRunListAdapter extends RecyclerView.Adapter<MilkRunListAdapter.MilkListHolder> {
    Context context;
    ArrayList<MilkDetailObject> milkDetailObjectArrayList;

    public MilkRunListAdapter(Context context, ArrayList<MilkDetailObject> milkDetailObjectArrayList) {
        this.context = context;
        this.milkDetailObjectArrayList = milkDetailObjectArrayList;
    }

    @NonNull
    @Override
    public MilkListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.milk_run_row, parent, false);
        return new MilkListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MilkListHolder holder, int position) {
        MilkDetailObject milkDetailObject = milkDetailObjectArrayList.get(position);
        holder.venderName.setText(milkDetailObject.getConsigneeName());
        holder.venderNumber.setText(milkDetailObject.getContactNo());
        holder.venderAddress.setText(milkDetailObject.getAddress());
        int lastPos = milkDetailObjectArrayList.size() - 1;
        String currentStatus = milkDetailObjectArrayList.get(position).getStatus();
        String nextStatus = "";
        String priveousStatus = "";
        if (position <= milkDetailObjectArrayList.size() - 1)
            if (position != lastPos)
                nextStatus = milkDetailObjectArrayList.get(position + 1).getStatus();
        if (position > 0)
            priveousStatus = milkDetailObjectArrayList.get(position - 1).getStatus();


        // Pending case
        if (milkDetailObject.getStatus().equals(WebUrlClass.statusPending)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.pending_icon));
            }
         //   ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.red)));

            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusPending) ||priveousStatus.equals(WebUrlClass.statusLoading) || priveousStatus.equals(WebUrlClass.statusStart) || priveousStatus.equals(WebUrlClass.statusArrived))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                 /*if (priveousStatus.equals(WebUrlClass.statusStart)||) {
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.orange));*/
                    //holder.v1.setBackgroundColor(context.getResources().getColor(R.color.orange));
              //  }
                 if (priveousStatus.equals(WebUrlClass.statusComplete) ) {
                   // holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                 if(nextStatus.equals(WebUrlClass.statusPending)){
                     holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                 }
                 if(priveousStatus.equals(WebUrlClass.statusCancel)){
                     holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                 }
                 if(nextStatus.equals(WebUrlClass.statusCancel)){
                     holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                 }

            }
            //  Start status
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusStart)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.ic_milk_start));
            }
           // ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.orange)));

            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.orange));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.orange));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusComplete))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                 if (nextStatus.equals(WebUrlClass.statusPending)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }

                if(priveousStatus.equals(WebUrlClass.statusCancel)){
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(nextStatus.equals(WebUrlClass.statusCancel)){
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }


            }

            // complete status
        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusComplete)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.complete_icon));
                holder.statusImage.setImageResource(R.drawable.complete_icon);
            }
          //  ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusComplete))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                 if (nextStatus.equals(WebUrlClass.statusComplete)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if (nextStatus.equals(WebUrlClass.statusStart) || nextStatus.equals(WebUrlClass.statusPending) || nextStatus.equals(WebUrlClass.statusArrived) || nextStatus.equals(WebUrlClass.statusLoading)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if(priveousStatus.equals(WebUrlClass.statusCancel)){
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(nextStatus.equals(WebUrlClass.statusCancel)){
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }

            }

        }
        // Arrive status
        else if (milkDetailObject.getStatus().equals(WebUrlClass.statusArrived)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.complete_icon));
                holder.statusImage.setImageResource(R.drawable.arrive_icon);
            }
          //  ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.arraive_color));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.arraive_color));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusComplete))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                 if (nextStatus.equals(WebUrlClass.statusComplete)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if (nextStatus.equals(WebUrlClass.statusStart)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if (nextStatus.equals(WebUrlClass.statusPending)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(priveousStatus.equals(WebUrlClass.statusCancel)){
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(nextStatus.equals(WebUrlClass.statusCancel)){
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
            }

        }
        // Loading

        else if (milkDetailObject.getStatus().equals(WebUrlClass.statusLoading)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               // holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.complete_icon));
                holder.statusImage.setImageResource(R.drawable.loading_icon);
            }
          //  ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.green)));

            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.loading_color));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.loading_color));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusComplete))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                 if (nextStatus.equals(WebUrlClass.statusComplete)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if (nextStatus.equals(WebUrlClass.statusStart)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                if (nextStatus.equals(WebUrlClass.statusPending)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(priveousStatus.equals(WebUrlClass.statusCancel)){
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(nextStatus.equals(WebUrlClass.statusCancel)){
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
            }

        } else if (milkDetailObject.getStatus().equals(WebUrlClass.statusCancel)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.cancel_icon));
            }
            if (position == 0) {
                holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else if (position == milkDetailObjectArrayList.size() - 1) {
                holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
            } else {
                if (priveousStatus.equals(WebUrlClass.statusComplete))
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.green));
                if (nextStatus.equals(WebUrlClass.statusComplete)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if (nextStatus.equals(WebUrlClass.statusStart)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if (nextStatus.equals(WebUrlClass.statusPending)) {
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(priveousStatus.equals(WebUrlClass.statusCancel)){
                    holder.v1.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
                if(nextStatus.equals(WebUrlClass.statusCancel)){
                    holder.v2.setBackgroundColor(context.getResources().getColor(R.color.red));
                }
            }
            //ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.red)));

        }else if (milkDetailObject.getStatus().equals(WebUrlClass.statusLoading)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.statusImage.setImageDrawable(context.getDrawable(R.drawable.loading_icon));
            }
            ImageViewCompat.setImageTintList(holder.statusImage, ColorStateList.valueOf(context.getResources().getColor(R.color.blue)));

        }
        if (position == 0) {
            holder.v2.setVisibility(View.VISIBLE);
            holder.v1.setVisibility(View.GONE);
        } else if (position == milkDetailObjectArrayList.size() - 1) {
            holder.v2.setVisibility(View.GONE);
            holder.v1.setVisibility(View.VISIBLE);
        } else {
            holder.v2.setVisibility(View.VISIBLE);
            holder.v1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return milkDetailObjectArrayList.size();
        //  return 5;
    }

    public class MilkListHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.venderName)
        TextView venderName;
        @BindView(R.id.venderNumber)
        TextView venderNumber;
        @BindView(R.id.venderAddress)
        TextView venderAddress;
        @BindView(R.id.statusImage)
        ImageView statusImage;
        @BindView(R.id.v1)
        View v1;
        @BindView(R.id.v2)
        View v2;


        public MilkListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.tripRow)
        void tripRow() {
            ((MilkRunLocationListActivity) context).selectUser(getAdapterPosition());
        }
    }
}
