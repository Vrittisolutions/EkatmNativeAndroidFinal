package com.vritti.crmlib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CallListdataInfo;

/**
 * Created by Admin-1 on 4/27/2017.
 */

public class CallsListAdapter extends RecyclerView.Adapter<CallsListAdapter.ViewHolder> {
    List<CallListdataInfo> callListdataInfoList;
    Context context;

    public CallsListAdapter(List<CallListdataInfo> callListdataInfoList, Context context) {
        this.callListdataInfoList = callListdataInfoList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.crm_opencalllist_item_list_lay, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtFName.setText(callListdataInfoList.get(position).getFirmname());

        System.out.println("Firmname :" + callListdataInfoList.get(position).getFirmname());
        String cityname = callListdataInfoList.get(position).getCityname();
        String product = callListdataInfoList.get(position).getProductname();
        String contactname = callListdataInfoList.get(position).getContactName();

        String Fulladdress = cityname + " - " + product + " " + "(" + contactname + ")";

        holder.txtAddress.setText(Fulladdress);

        holder.txt_reason.setText(callListdataInfoList.get(position).getLatestRemark());


        holder.txt_outcome.setText(callListdataInfoList.get(position).getOutcome());
        holder.txt_username.setText(callListdataInfoList.get(position).getUsername());
        holder.len_outcome.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return callListdataInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFName, txt_username;
        TextView txtcity, txtNumber;
        TextView txtItemdesc, txtEmail;
        TextView txt_outcome, txtAddress, txt_reason, txt_estimate;
        LinearLayout len_outcome;
        RelativeLayout lay_header;
        ImageView click;

        public ViewHolder(View view) {
            super(view);
            txtFName = (TextView) view.findViewById(R.id.txtFName);
            txt_username = (TextView) view.findViewById(R.id.txt_username);
            txtcity = (TextView) view.findViewById(R.id.txtcity);
            txtNumber = (TextView) view.findViewById(R.id.txtNumber);
            txtItemdesc = (TextView) view.findViewById(R.id.txtItemdesc);
            txtEmail = (TextView) view.findViewById(R.id.txtEmail);
            txtAddress = (TextView) view.findViewById(R.id.txtAddress);
            txt_reason = (TextView) view.findViewById(R.id.txt_reason);
            txt_estimate = (TextView) view.findViewById(R.id.txt_estimate);
            txt_outcome = (TextView) view.findViewById(R.id.txt_outcome);
            len_outcome = (LinearLayout) view.findViewById(R.id.len_outcome);
        }
    }
}
