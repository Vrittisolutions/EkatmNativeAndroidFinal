package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.SaharaRemarksDetailsListObj;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SaharaRemarkAdapter extends RecyclerView.Adapter<SaharaRemarkAdapter.RemarkHolder> {

    ArrayList<SaharaRemarksDetailsListObj> saharaRemarksDetailsListObjs;
    Context context;
    String designation;


    public SaharaRemarkAdapter(Context context, ArrayList<SaharaRemarksDetailsListObj> listObjs, String designation) {
        this.context = context;
        this.saharaRemarksDetailsListObjs = listObjs;
        this.designation = designation;
    }

    @NonNull
    @Override
    public RemarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sahara_remarks,parent,false);

        return new RemarkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RemarkHolder holder, int position) {

        String descr = saharaRemarksDetailsListObjs.get(position).getDescr();
        String username = saharaRemarksDetailsListObjs.get(position).getUsername();
        String isApprDisappr = saharaRemarksDetailsListObjs.get(position).getIsApprDisAppr();
        String remark1 = saharaRemarksDetailsListObjs.get(position).getRemark();
        String date = saharaRemarksDetailsListObjs.get(position).getAddeddt();

        String date1="";
        if(date != null) {
            date1 = formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd/MM/yyyy hh:mm", date);
            // String append_String = datasheetlists.get(pos).getDescr() +"-" + datasheetlists.get(pos).getUsername()+" , "+ datasheetlists.get(pos).getIsApprDisAppr() +" on " + date1;
        }

        if(designation.equalsIgnoreCase("school")){
            holder.txt_remark.setVisibility(View.GONE);
            String append = descr + " Remark : " + remark1 +" on "+date1;
            holder.txt_Info.setText(append);
            //School Remark : s1 on 31/01/2020
        }else {
            String append_Info = descr + " , " + isApprDisappr + " on " + date1;
            String remark = "Remark : " + remark1;
            holder.txt_Info.setText(append_Info);
            holder.txt_remark.setText(remark);
        }
    }

    @Override
    public int getItemCount() {
        return saharaRemarksDetailsListObjs.size();
    }

    public class RemarkHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_Info)
        TextView txt_Info;
        @BindView(R.id.txt_remark)
        TextView txt_remark;


        public RemarkHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }
}