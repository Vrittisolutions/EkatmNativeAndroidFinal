package com.vritti.sales.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.PendingDeliveries;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PendingDeliveryAdapter extends BaseAdapter {
/*test*/
    private Context parent;
    private Context context;
    static ArrayList<ShipmentEntryBean> lstPending_Deliveries;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;

    public PendingDeliveryAdapter(Context context, ArrayList<ShipmentEntryBean> listpending) {
        this.parent = context;
        this.context = context;
        this.lstPending_Deliveries = listpending;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return lstPending_Deliveries.size();
    }

    @Override
    public Object getItem(int position) {
        return lstPending_Deliveries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.tbuds_pending_deliveries, null);
            holder = new ViewHolder();

            holder.laydata = (LinearLayout)convertView.findViewById(R.id.laydata);
            holder.txtcustname = (TextView)convertView.findViewById(R.id.txtcustname);
            holder.txtexpdeldate = (TextView)convertView.findViewById(R.id.txtexpdeldate);
            holder.txtinvoiceno = (TextView)convertView.findViewById(R.id.txtinvoiceno);
            holder.txtinvoicedate = (TextView)convertView.findViewById(R.id.txtinvoicedate);
            holder.txtaddress = (TextView)convertView.findViewById(R.id.txtaddress);
            holder.img_pdf = (ImageView) convertView.findViewById(R.id.img_pdf);
            holder.delmode =  convertView.findViewById(R.id.delmode);
            holder.txtmobile =  convertView.findViewById(R.id.txtmobile);
            holder.img_location =  convertView.findViewById(R.id.img_location);
            holder.imgpaid = convertView.findViewById(R.id.imgpaid);
            holder.imgshare = convertView.findViewById(R.id.imgshare);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            holder.laydata.setBackgroundColor(Color.parseColor("#EDEDED"));
        } else {
            holder.laydata.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        if(lstPending_Deliveries.get(position).getAddedBy().equals(lstPending_Deliveries.get(position).getConsignee())){
            holder.txtcustname.setText(lstPending_Deliveries.get(position).getConsignee());
        }else {
            holder.txtcustname.setText(lstPending_Deliveries.get(position).getConsignee() + " Order placed by "+
                    lstPending_Deliveries.get(position).getAddedBy());
        }
        holder.txtcustname.setText(lstPending_Deliveries.get(position).getConsignee() + " Order placed by "+
                lstPending_Deliveries.get(position).getAddedBy());
        holder.txtexpdeldate.setText(Convertdate(lstPending_Deliveries.get(position).getScheduleDate()));
        holder.txtinvoiceno.setText(lstPending_Deliveries.get(position).getInvoiceNo());
        holder.txtinvoicedate.setText(Convertdate(lstPending_Deliveries.get(position).getInvoiceDate()));
        holder.txtaddress.setText(lstPending_Deliveries.get(position).getDeliveryAddress());
        holder.delmode.setText(lstPending_Deliveries.get(position).getDeliveryTerms());
        holder.txtmobile.setText(lstPending_Deliveries.get(position).getMobile());

        if(lstPending_Deliveries.get(position).getPaymentStatus().equalsIgnoreCase("42")){
            try{
                if(lstPending_Deliveries.get(position).getTransactionId().equalsIgnoreCase("COD")){
                    holder.imgpaid.setImageResource(R.drawable.cod_2);
                    holder.imgpaid.setVisibility(View.VISIBLE);
                }else {
                    holder.imgpaid.setImageResource(R.drawable.paidstamp);
                    holder.imgpaid.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            holder.imgpaid.setVisibility(View.GONE);
        }

        holder.txtmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirect to call

                if(context instanceof PendingDeliveries){
                    ((PendingDeliveries)context).MakeCall(lstPending_Deliveries.get(position).getMobile());
                }
            }
        });

        holder.img_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double lat = Double.parseDouble(lstPending_Deliveries.get(position).getLatitude());
                double lang = Double.parseDouble(lstPending_Deliveries.get(position).getLongitude());
               /* String uriMap = "http://maps.google.com/maps?q=loc:" + lat + "," + lang +
                        " (" + ((ChatModelObject) chatMessages.get(getAdapterPosition())).getChatMessage().getConsignee() + ")";*/
                String uriMap = "http://maps.google.com/maps?q=loc:" + lat + "," + lang;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                context.startActivity(intent);

                /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q="+lstPending_Deliveries.get(position).getDeliveryAddress()));
                context.startActivity(intent);*/
            }
        });

        holder.imgshare.setTag(lstPending_Deliveries.get(position));
        holder.imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (context instanceof PendingDeliveries) {
                    ((PendingDeliveries) context).share(lstPending_Deliveries.get(position).getSOno(),
                            String.format("%.2f",lstPending_Deliveries.get(position).getSubtotal_Amt()),
                            lstPending_Deliveries.get(position).getPaymentStatus(), lstPending_Deliveries.get(position).getConsignee(),
                            lstPending_Deliveries.get(position).getMobile(), lstPending_Deliveries.get(position).getDeliveryAddress(),
                            lstPending_Deliveries.get(position).getLatitude(), lstPending_Deliveries.get(position).getLongitude(),
                            lstPending_Deliveries.get(position).getSOHeaderID(), lstPending_Deliveries.get(position).getScheduleDate(),
                            lstPending_Deliveries.get(position).getPrefDelFrmTime(), lstPending_Deliveries.get(position).getPrefDelToTime());
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView txtcustname, txtexpdeldate, txtinvoiceno, txtinvoicedate, txtaddress,txtmobile,delmode;
        LinearLayout laydata;
        ImageView img_pdf,img_location,imgpaid,imgshare;
    }

    public String Convertdate(String date){
        String DateToStr = "";

        SimpleDateFormat Format = new SimpleDateFormat("dd MMM hh:mm a");//Feb 23 2016 12:16PM
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
