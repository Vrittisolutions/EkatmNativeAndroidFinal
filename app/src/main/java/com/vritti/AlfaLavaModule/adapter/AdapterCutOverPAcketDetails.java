package com.vritti.AlfaLavaModule.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterCutOverPAcketDetails extends RecyclerView.Adapter<AdapterCutOverPAcketDetails.ViewHolder> {

    private ArrayList<PutAwayDetail> list;
    private EditText et_country;
    private View view;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;

    public AdapterCutOverPAcketDetails(ArrayList<PutAwayDetail> countries) {
        this.list = countries;
    }

    @Override
    public AdapterCutOverPAcketDetails.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cutover_packet, viewGroup, false);

        return new AdapterCutOverPAcketDetails.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterCutOverPAcketDetails.ViewHolder holder, int position) {
        holder.txt_packet.setText(list.get(position).getPacketNo());

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getPacketNo(int position) {
        return list.get(position).getPacketNo();
    }

    public void update(ArrayList<PutAwayDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_packet;

         public ViewHolder(View view) {
             super(view);
             txt_packet = (TextView) view.findViewById(R.id.txt_packet);

             view.setTag(view);
         }
    }

}