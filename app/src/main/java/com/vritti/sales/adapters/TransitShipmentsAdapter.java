package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.DeliveryAgentBean;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransitShipmentsAdapter extends BaseAdapter {

    private Context parent;
    static ArrayList<DeliveryAgentBean> lstTransit_Shipments;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;

    public TransitShipmentsAdapter(Context context, ArrayList<DeliveryAgentBean> listTransits) {
        this.parent = context;
        this.lstTransit_Shipments = listTransits;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return lstTransit_Shipments.size();
    }

    @Override
    public Object getItem(int position) {
        return lstTransit_Shipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.tbuds_transit_shipments, null);
            holder = new ViewHolder();

            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.txtinvoiceno = (TextView)convertView.findViewById(R.id.txtinvoiceno);
            holder.txtcustname = (TextView)convertView.findViewById(R.id.txtcustname);
            holder.txtactassigndate = (TextView)convertView.findViewById(R.id.txtactassigndate);
            holder.txtagentname = (TextView)convertView.findViewById(R.id.txtagentname);
            holder.txtagentmobile = (TextView)convertView.findViewById(R.id.txtagentmobile);
            holder.img_pdf = (ImageView)convertView.findViewById(R.id.img_pdf);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtinvoiceno.setText(lstTransit_Shipments.get(position).getInvoiceNo());
        holder.txtcustname.setText(lstTransit_Shipments.get(position).getConsigneeName());
        //holder.txtactassigndate.setText(lstTransit_Shipments.get(position).getActivityAssignedDate());
        holder.txtactassigndate.setText(Convertdate(lstTransit_Shipments.get(position).getInvoiceDt()));
        holder.txtagentname.setText(lstTransit_Shipments.get(position).getAgentName().trim());
        holder.txtagentmobile.setText(lstTransit_Shipments.get(position).getAgentMobNo().trim());

        if (position % 2 == 1) {
            holder.layout.setBackgroundColor(Color.parseColor("#EDEDED"));
        } else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        return convertView;
    }

    class ViewHolder{
        TextView txtinvoiceno, txtactassigndate, txtcustname, txtagentname, txtagentmobile;
        LinearLayout layout;
        ImageView img_pdf;
    }

    public String Convertdate(String date){
        String DateToStr = "";

        SimpleDateFormat Format = new SimpleDateFormat("dd MMM hh:mm a");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat format_new = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d1 = null;
        try {
            d1 = format_new.parse(date);
            DateToStr = Format.format(d1);
            System.out.println(DateToStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateToStr;
    }

    public String Convertdate_2(String date){
        String DateToStr = "";

        SimpleDateFormat Format = new SimpleDateFormat("dd MMM hh:mm a");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        Date d1 = null;
        try {
            d1 = format.parse(date);
            DateToStr = Format.format(d1);
            System.out.println(DateToStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateToStr;
    }

}
