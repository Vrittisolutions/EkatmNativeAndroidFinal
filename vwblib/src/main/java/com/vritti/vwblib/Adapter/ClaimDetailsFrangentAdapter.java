package com.vritti.vwblib.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vritti.vwblib.Beans.ClaimDetailsBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.vworkbench.ClaimDetailActivity;
import com.vritti.vwblib.vworkbench.ClaimDetailsFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 300151 on 1/11/17.
 */
public  class ClaimDetailsFrangentAdapter extends BaseAdapter {
    private static List<ClaimDetailsBean> lsActivityList = new ArrayList<ClaimDetailsBean>();
    private LayoutInflater mInflater;
    Context context;

    public ClaimDetailsFrangentAdapter(Context context1, List<ClaimDetailsBean> lsActivityList1) {
        lsActivityList = lsActivityList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return lsActivityList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsActivityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {

        if (getCount() != 0)
            return getCount();

        return 1;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_claim_details_item, null);
            holder = new ViewHolder();
            holder.tv_claim_cancel = (TextView) convertView.findViewById(R.id.tv_claim_cancel);
            holder.tv_claim_edit = (TextView) convertView.findViewById(R.id.tv_claim_edit);
            holder.tv_Camount = (TextView) convertView.findViewById(R.id.tv_Total);
            holder.tv_travelling = (TextView) convertView.findViewById(R.id.tv_travelling);
            holder.tv_Cdate = (TextView) convertView.findViewById(R.id.tv_clim_date);
            holder.fromPlace = (TextView) convertView.findViewById(R.id.fromPlace);
            holder.ToPlace = (TextView) convertView.findViewById(R.id.ToPlace);
            holder.tv_food = (TextView) convertView.findViewById(R.id.tv_food);
            holder.tv_Local = (TextView) convertView.findViewById(R.id.tv_Local);
            holder.tv_lodging = (TextView) convertView.findViewById(R.id.tv_lodging);
            holder.tv_Maintenanace = (TextView) convertView.findViewById(R.id.tv_Maintenanace);
            holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
            holder.tv_Ph = (TextView) convertView.findViewById(R.id.tv_Ph);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);

            holder.tv_claim_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Delete claim")
                            .setMessage("Are you sure you want to delete this claim?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int pos = (Integer) v.getTag();
                                    lsActivityList.remove(pos);
                                    notifyDataSetChanged();
                                    ClaimDetailsFragment.adapter = new ClaimDetailsFrangentAdapter(context, ClaimDetailActivity.lsCalimDetails);
                                    ClaimDetailsFragment.lay_claim_details.setAdapter(ClaimDetailsFragment.adapter);


                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            holder.tv_claim_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ClaimDetailActivity.class);
                    intent.putExtra("Action", "Edit");
                    intent.putExtra("Position", position);
                    intent.putExtra("ClaimDate", lsActivityList.get(position).getClaimDate());
                    intent.putExtra("fromPlace", lsActivityList.get(position).getFromPlace());
                    intent.putExtra("ToPlace", lsActivityList.get(position).getToPlace());
                    intent.putExtra("food", lsActivityList.get(position).getTv_food());
                    intent.putExtra("Local", lsActivityList.get(position).getTv_Local());
                    intent.putExtra("lodging", lsActivityList.get(position).getTv_lodging());
                    intent.putExtra("Maintenanace", lsActivityList.get(position).getTv_Maintenanace());
                    intent.putExtra("mode", lsActivityList.get(position).getTv_mode());
                    intent.putExtra("Ph", lsActivityList.get(position).getTv_Ph());
                    intent.putExtra("travelling", lsActivityList.get(position).getTv_travelling());
                    intent.putExtra("distance", lsActivityList.get(position).getDistance());
                    context.startActivity(intent);


                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_Cdate.setText(lsActivityList.get(position).getClaimDate());
        holder.tv_Camount.setText(lsActivityList.get(position).getAmount());
        holder.fromPlace.setText("From: "+lsActivityList.get(position).getFromPlace());
        holder.ToPlace.setText("To: "+lsActivityList.get(position).getToPlace());
        holder.tv_food.setText(lsActivityList.get(position).getTv_food());
        holder.tv_Local.setText(lsActivityList.get(position).getTv_Local());
        holder.tv_lodging.setText(lsActivityList.get(position).getTv_lodging());
        holder.tv_Maintenanace.setText(lsActivityList.get(position).getTv_Maintenanace());
        holder.tv_mode.setText(lsActivityList.get(position).getTv_mode());
        holder.tv_Ph.setText(lsActivityList.get(position).getTv_Ph());
        holder.tv_travelling.setText(lsActivityList.get(position).getTv_travelling());
        holder.tv_distance.setText(lsActivityList.get(position).getDistance());

        holder.tv_claim_cancel.setTag(position);
        convertView.setTag(holder);
        return convertView;
    }

    private void ClaimDetetionDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.vwb_change_activity_status);
    }

    static class ViewHolder {
        public static TextView tv_Cdate, tv_Camount, fromPlace, ToPlace, tv_mode, tv_travelling, tv_lodging, tv_food, tv_Local, tv_Ph, tv_Maintenanace,tv_distance;
        public static TextView tv_claim_edit, tv_claim_cancel;
    }
}
