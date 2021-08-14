package com.vritti.sales.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.ShipmentAndInvoicing;
import com.vritti.sales.beans.OrderHistoryBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sharvari on 2/25/2016.
 */
public class OpenOrderListAdapter extends BaseAdapter {
    private Context parent;
    private ArrayList<OrderHistoryBean> arrayList;
    private ViewHolder holder;
    private String DateToStr;

    public OpenOrderListAdapter(Context context, List<OrderHistoryBean> list) {
        // TODO Auto-generated constructor stub
        parent = context;
        arrayList = (ArrayList<OrderHistoryBean>) list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.tbuds_openorderhistory, viewGroup, false);

            holder.date = (TextView) view.findViewById(R.id.doack_1);
            holder.amount = (TextView) view.findViewById(R.id.amount);
            holder.id = (TextView) view.findViewById(R.id.orderid_1);
            holder.txtsodate = (TextView) view.findViewById(R.id.txtsodate);
            holder.txtstatus = (ImageView) view.findViewById(R.id.txtstatus);
            holder.txtcustname = (TextView) view.findViewById(R.id.txtcustname);
            holder.txtmobile = (TextView) view.findViewById(R.id.txtmobile);
            holder.delmode = (TextView) view.findViewById(R.id.delmode);
            holder.imgpaid = view.findViewById(R.id.imgpaid);
            holder.imgshare = view.findViewById(R.id.imgshare);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        String DoAck = arrayList.get(position).getDoAck();
        String SONo = arrayList.get(position).getSONo();

        if(!DoAck.equalsIgnoreCase("") || !DoAck.equals(null) || !DoAck.equals("null")){
            DateToStr = Convertdate(DoAck);
        }else {
            DateToStr = "";
        }

        holder.id.setText(arrayList.get(position).getSONo());

        double amount = Double.parseDouble(String.valueOf(arrayList.get(position).getNetAmt()));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);

        holder.amount.setText(formatted +" ₹");
        holder.date.setText(DateToStr);

        try{
            holder.txtsodate.setText(Convertdate(arrayList.get(position).getSODate()));
        }catch (Exception e){
            e.printStackTrace();
            holder.txtsodate.setText("");
        }
        //holder.txtstatus.setText(arrayList.get(position).getStatusname());

        if(arrayList.get(position).getStatusname().equalsIgnoreCase("Approved")){
            holder.txtstatus.setImageResource(R.drawable.tbuds_checkedfilled);
        }else {
            holder.txtstatus.setImageResource(R.drawable.tbuds_half_dispatch);
        }

        holder.txtcustname.setText(arrayList.get(position).getConsigneeName());
        holder.txtmobile.setText(arrayList.get(position).getMobile());
        holder.delmode.setText(arrayList.get(position).getDeliveryTerms());

        //arrayList.get(position).getSODate().substring(0, arrayList.get(position).getSODate().indexOf(":") - 1);

        if(arrayList.get(position).getPaymentStatus().equalsIgnoreCase("42")){
            try{
                if(arrayList.get(position).getTransactionId().equalsIgnoreCase("COD")){
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
                if(parent instanceof ShipmentAndInvoicing){
                    ((ShipmentAndInvoicing)parent).MakeCall(arrayList.get(position).getMobile());
                }
            }
        });

        holder.imgshare.setTag(arrayList.get(position));
        holder.imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parent instanceof ShipmentAndInvoicing) {
                    ((ShipmentAndInvoicing) parent).share(arrayList.get(position).getSONo(),
                            String.format("%.2f", arrayList.get(position).getNetAmt()),
                            arrayList.get(position).getPaymentStatus(), arrayList.get(position).getConsigneeName(),
                            arrayList.get(position).getMobile(), arrayList.get(position).getAddress(),
                            arrayList.get(position).getCustLat(), arrayList.get(position).getCustLng(), arrayList.get(position).getSOHeaderId(),
                            arrayList.get(position).getSODate(),
                            arrayList.get(position).getPrfDelFrmTime(),arrayList.get(position).getPrfDelToTime());
                }
            }
        });

        return view;
    }

    private static class ViewHolder {
        TextView amount, date, id, txtsodate,txtcustname,txtmobile,delmode;
        ImageView txtstatus,imgpaid,imgshare;
        LinearLayout ly;
    }

    public String getdate(String d) {

        String finalDate;
        if (!(d.equals("") || d == null)) {
//6\/1\/2016 12:31:00 PM
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MM/dd/yyyy hh:mm:ss a");
            Date myDate = null;
            try {
                myDate = dateFormat.parse(d);
                System.out.println("..........value of my date after conv"
                        + myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd HH:mm");
            finalDate = timeFormat.format(myDate);

        } else {
            finalDate = "";
        }

        return finalDate;

    }

    private String[] splitDT(String tf) {
        // TODO Auto-generated method stub
        String finalDate;
        if (!(tf.equals("") || tf == null)) {
//Jun 10 2016  4:16PM
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MMM dd yyyy hh:mma");
            Date myDate = null;
            try {
                myDate = dateFormat.parse(tf);
                System.out.println("..........value of my date after conv"
                        + myDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM yyyy");
            finalDate = timeFormat.format(myDate);

        } else {
            finalDate = "";
        }

        String[] v = {finalDate};

        return v;
    }

    public String Convertdate(String date){

        if(date.contains("/Date")){
            date = date.replace("/Date(","");
            date = date.replace(")/","");
            Long timestamp = Long.valueOf(date);
            Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
            cal1.setTimeInMillis(timestamp);
            String date1 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal1).toString();

            SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");

            Date d1 = null;
            try {
                d1 = format.parse(date1);
                //DateToStr = toFormat.format(date);
                DateToStr = Format.format(d1);
                // DateToStr = format.format(d1);
                System.out.println(DateToStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else {
            SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//Feb 23 2016 12:16PM
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date d1 = null;
            try {
                d1 = format.parse(date);
                //DateToStr = toFormat.format(date);
                DateToStr = Format.format(d1);
                // DateToStr = format.format(d1);
                System.out.println(DateToStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return DateToStr;
    }

}