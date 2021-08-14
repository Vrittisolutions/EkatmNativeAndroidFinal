package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.fragments.OpenOrdersFragment;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharvari on 2/25/2016.
 */
public class OpenOrderAdapter extends BaseAdapter {
    private Context parent;
    private ArrayList<OrderHistoryBean> arrayList;
    private ViewHolder holder;
    private Fragment fragment;
    String appCallFrom="";

    public OpenOrderAdapter(Context context, ArrayList<OrderHistoryBean> list, Fragment fragment, String appName) {
        // TODO Auto-generated constructor stub
        parent = context;
        arrayList = list;
        this.fragment=fragment;
        this.appCallFrom=appName;
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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        try{
            if (view == null) {
                holder = new ViewHolder();
                LayoutInflater inflater;
                try{
                    inflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.custom_order_history, viewGroup, false);
                }catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    holder.imgshare = view.findViewById(R.id.imgshare);
                    holder.title = (TextView) view.findViewById(R.id.txtMerchantname);
                    // holder.date = (TextView) view.findViewById(R.id.txtorderdate);
                    holder.amount = (TextView) view.findViewById(R.id.amt);
                    holder.id = (TextView) view.findViewById(R.id.txtorderid);
                    holder.imageview_placed = (ImageView) view.findViewById(R.id.imageview_placed);
                    holder.imageview_approved = (ImageView) view.findViewById(R.id.imageview_approved);
                    holder.imageview_dispatched = (ImageView) view.findViewById(R.id.imageview_dispatched);
                    holder.imageview_delivered = (ImageView) view.findViewById(R.id.imageview_delivered);

                    holder.txtplaced = (TextView) view.findViewById(R.id.txtplaced);
                    holder.txtapproved = (TextView)view.findViewById(R.id.txtapproved);
                    holder.txtdispatched = (TextView) view.findViewById(R.id.txtdispatched);
                    holder.txtdelivered = (TextView) view.findViewById(R.id.txtdelivered);
                    holder.txtseller = (TextView) view.findViewById(R.id.txtseller);
                    holder.txtremark =  view.findViewById(R.id.txtremark);
                    holder.txtremark.setVisibility(View.GONE);
                    // holder.ly=(LinearLayout)view.findViewById(R.id.ly);
                    view.setTag(holder);

                    holder.laypaynow = view.findViewById(R.id.laypaynow);
                    holder.layout_ordermain = (LinearLayout)view.findViewById(R.id.layout_ordermain);
                    holder.view2 = (View)view.findViewById(R.id.view2);
                    holder.switch_onoff = (Switch)view.findViewById(R.id.switch_onoff);
                    holder.switch_onoff.setVisibility(View.GONE);

                    holder.cancel_order = view.findViewById(R.id.cancel_order);
                    holder.cancel_order.setVisibility(View.VISIBLE);
                    holder.cancel_order.setVisibility(View.GONE);
                    holder.txtmobile = view.findViewById(R.id.txtmobile);
                    holder.imgpaid = view.findViewById(R.id.imgpaid);
                    holder.paytitle =  view.findViewById(R.id.paytitle);

                }catch (Exception e){
                    e.printStackTrace();
                }

                view.setTag(holder);

            } else {
                holder = (ViewHolder) view.getTag();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.cancel_order.setTag(position);

        if(appCallFrom.equals("C")){
            holder.txtseller.setText(arrayList.get(position).getMerchantname());
        }else {
            holder.txtseller.setText(arrayList.get(position).getConsigneeName());
        }
        holder.txtremark.setTag(arrayList.get(position));
        holder.txtmobile.setTag(arrayList.get(position));
        holder.txtmobile.setText(arrayList.get(position).getMerchant_Mobile());
        String DispatchNo = arrayList.get(position).getDispatchNo();

        holder.title.setText(arrayList.get(position).getConsigneeName());
       // holder.id.setText(arrayList.get(position).getSONo() + "/" + DispatchNo);
        holder.id.setText(arrayList.get(position).getSONo());
      //  holder.amount.setText( arrayList.get(position).getRate()+" ₹");

        double amount = Double.parseDouble(String.valueOf(arrayList.get(position).getNetAmt()));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        final String formatted = formatter.format(amount);
        holder.amount.setText(formatted + " ₹");

        arrayList.get(position).getSODate().substring(0, arrayList.get(position).getSODate().indexOf(":") - 1);

        if(arrayList.get(position).getPaymentStatus().equalsIgnoreCase("42")){
            try{
                if(arrayList.get(position).getTransactionId().equalsIgnoreCase("COD")){
                    holder.imgpaid.setImageResource(R.drawable.cod_2);
                    holder.laypaynow.setVisibility(View.GONE);
                    holder.imgpaid.setVisibility(View.VISIBLE);
                }else {
                    holder.imgpaid.setImageResource(R.drawable.paidstamp);
                    holder.laypaynow.setVisibility(View.GONE);
                    holder.imgpaid.setVisibility(View.VISIBLE);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            holder.imgpaid.setVisibility(View.GONE);
            holder.laypaynow.setVisibility(View.VISIBLE);
        }

        if(arrayList.get(position).getUPIMerch().equalsIgnoreCase("")){
                holder.laypaynow.setVisibility(View.VISIBLE);
                holder.paytitle.setText(parent.getResources().getString(R.string.cod));
            }else {
                holder.laypaynow.setVisibility(View.VISIBLE);
                holder.paytitle.setText(parent.getResources().getString(R.string.paynow));
            }

        if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
            if (arrayList.get(position).getStatus().equalsIgnoreCase("10") ||
                    /*arrayList.get(position).getStatus().equalsIgnoreCase("Created")*/
                    arrayList.get(position).getStatus().equalsIgnoreCase("20")
                    /*arrayList.get(position).getStatus().equalsIgnoreCase("Approved")*/) {
               // holder.cancel_order.setVisibility(View.VISIBLE);
                holder.txtremark.setVisibility(View.GONE);
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);

                if(arrayList.get(position).getStatus().equalsIgnoreCase("20")
                        /*arrayList.get(position).getStatus().equalsIgnoreCase("Approved")*/){
                    holder.imageview_approved.setImageResource(R.drawable.approved_new);
                    holder.txtapproved.setText(parent.getResources().getString(R.string.apprvd)
                            +" \n" + getdate(arrayList.get(position).getDOApprvd()));
                }else {
                    holder.imageview_approved.setImageResource(R.drawable.notapproved);
                    holder.txtapproved.setText(parent.getResources().getString(R.string.notapprvd));
                }

                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.imageview_dispatched.setImageResource(R.drawable.dispatched_normal_icon);
                holder.txtdispatched.setText(parent.getResources().getString(R.string.notpckgd));
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setImageResource(R.drawable.rcv_n6);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.notrcvd));

                if(arrayList.get(position).getStatus().equalsIgnoreCase("20")){
                    holder.layout_ordermain.setBackgroundColor(Color.parseColor("#E9F2F8"));
                    /*d4e5f1*/
                }else{
                    holder.layout_ordermain.setBackgroundColor(Color.parseColor("#ffffff"));
                }

            } else if (arrayList.get(position).getStatus().equalsIgnoreCase("40") ||
                    arrayList.get(position).getStatus().equalsIgnoreCase("35")) {
                holder.txtremark.setVisibility(View.GONE);

                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.approved_new);
                holder.txtapproved.setText(parent.getResources().getString(R.string.apprvd)+
                        " \n" + getdate(arrayList.get(position).getDOApprvd()));

                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.switch_onoff.setVisibility(View.GONE);
                if( arrayList.get(position).getStatus().equalsIgnoreCase("35")){
                    holder.txtdispatched.setText(parent.getResources().getString(R.string.partpckgd)+
                            " \n " + getdate(arrayList.get(position).getDODisptch()));

                }else if(arrayList.get(position).getStatus().equalsIgnoreCase("40")){
                    holder.imageview_dispatched.setImageResource(R.drawable.dispatch_new_green);
                    holder.txtdispatched.setText(parent.getResources().getString(R.string.pckcmpltd) +" \n"
                            + getdate(arrayList.get(position).getDODisptch()));
                }

                holder.txtdelivered.setVisibility(View.VISIBLE);
                if(arrayList.get(position).getShipstatus().equalsIgnoreCase("Received")){
                    holder.imageview_delivered.setImageResource(R.drawable.rcv6);
                    holder.txtdelivered.setText(parent.getResources().getString(R.string.rcvd)+" \n"
                            + getdate(arrayList.get(position).getDORcvd()));
                    holder.layout_ordermain.setBackgroundColor(Color.parseColor("#c4dea4"));
                }else if(arrayList.get(position).getShipstatus().equalsIgnoreCase("")){
                    holder.imageview_delivered.setImageResource(R.drawable.rcv_n6);
                    holder.txtdelivered.setText(parent.getResources().getString(R.string.notrcvd));
                    holder.layout_ordermain.setBackgroundColor(Color.parseColor("#e6e4da"));
                }else if(arrayList.get(position).getShipstatus().equalsIgnoreCase("10") ||
                        arrayList.get(position).getShipstatus().equalsIgnoreCase("20")){
                    holder.imageview_delivered.setImageResource(R.drawable.rcv_n6);
                    holder.txtdelivered.setText(parent.getResources().getString(R.string.notrcvd));
                    holder.layout_ordermain.setBackgroundColor(Color.parseColor("#e6e4da"));
                }

            } else if (arrayList.get(position).getStatus().equalsIgnoreCase("13")) {
                holder.txtremark.setVisibility(View.GONE);
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.approved_new);

                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.switch_onoff.setVisibility(View.GONE);

                holder.txtapproved.setText(parent.getResources().getString(R.string.apprvd)+
                        " \n " + getdate(arrayList.get(position).getDOApprvd()));
                holder.imageview_dispatched.setImageResource(R.drawable.dispatch_new_green);
                holder.txtdispatched.setText(parent.getResources().getString(R.string.pckcmpltd) +" \n"
                        + getdate(arrayList.get(position).getDODisptch()));
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setImageResource(R.drawable.rcv6);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.rcvd)+" \n" + getdate(arrayList.get(position).getDODisptch()));
                holder.layout_ordermain.setBackgroundColor(Color.parseColor("#c4dea4"));

            } else if (arrayList.get(position).getStatus().equalsIgnoreCase("14")) {
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.notapproved);
                holder.txtapproved.setText(parent.getResources().getString(R.string.notapprvd));
                holder.imageview_dispatched.setImageResource(R.drawable.dispatched_normal_icon);
                holder.imageview_delivered.setImageResource(R.drawable.ord_cancelled);
                holder.txtdispatched.setVisibility(View.VISIBLE);
                holder.txtdispatched.setText(parent.getResources().getString(R.string.notpckgd));
                holder.view2.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.closed));
                holder.txtremark.setText("Order gets overdue, No action on it.");
                holder.layout_ordermain.setBackgroundColor(Color.parseColor("#dcdcdc"));

            }else if (arrayList.get(position).getStatus().equalsIgnoreCase("90")) {
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.notapproved);
                holder.txtapproved.setText(parent.getResources().getString(R.string.notapprvd));
                holder.imageview_dispatched.setImageResource(R.drawable.dispatched_normal_icon);
                holder.imageview_delivered.setImageResource(R.drawable.ord_cancelled);
                holder.txtdispatched.setVisibility(View.VISIBLE);
                holder.txtdispatched.setText(parent.getResources().getString(R.string.notpckgd));
                holder.view2.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.cancld)+" ");
                holder.txtremark.setText(" You have cancelled this order");
                holder.layout_ordermain.setBackgroundColor(Color.parseColor("#dcdcdc"));

            } else if (arrayList.get(position).getStatus().equalsIgnoreCase("15")) {
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.notapproved);
                holder.imageview_dispatched.setImageResource(R.drawable.dispatched_normal_icon);
                holder.txtdispatched.setVisibility(View.GONE);
                holder.view2.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.rejctd)+" \n"+ getdate(arrayList.get(position).getDOrej()));
                                holder.layout_ordermain.setBackgroundColor(Color.parseColor("#ffc6b7"));
            }else if (arrayList.get(position).getStatus().equalsIgnoreCase("30")) {
                holder.imageview_placed.setImageResource(R.drawable.checkedfilled);
                holder.txtplaced.setText(parent.getResources().getString(R.string.placed)+" \n"+ getdate(arrayList.get(position).getDoAck()));
                holder.imageview_approved.setImageResource(R.drawable.notapproved);
                holder.txtapproved.setText(parent.getResources().getString(R.string.notapprvd));
                holder.imageview_dispatched.setImageResource(R.drawable.dispatched_normal_icon);
                holder.imageview_delivered.setImageResource(R.drawable.ord_cancelled);
                holder.txtdispatched.setVisibility(View.VISIBLE);
                holder.txtdispatched.setText(parent.getResources().getString(R.string.notpckgd));
                holder.view2.setVisibility(View.VISIBLE);
                holder.imageview_delivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setVisibility(View.VISIBLE);
                holder.txtdelivered.setText(parent.getResources().getString(R.string.cancld)+" ");
                holder.txtremark.setText(" Order disapproved by merchant");

                holder.layout_ordermain.setBackgroundColor(Color.parseColor("#dcdcdc"));
            }
        } else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")){

        }

        holder.switch_onoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch view1 = (Switch) v;
                boolean isSelected = ((Switch) view1).isChecked();
                arrayList.get(position).setChecked(isSelected);

                Toast.makeText(parent,"switch clicked of position "+position,Toast.LENGTH_SHORT).show();

            }
        });

       /* holder.laypaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OpenOrdersFragment)fragment).payToMerchant(arrayList.get(position).getUPIMerch(),
                        arrayList.get(position).getMerchantname(),formatted,arrayList.get(position).getMerchantid(),
                        arrayList.get(position).getSOHeaderId());
            }
        });*/

        holder.switch_onoff.setChecked(arrayList.get(position).isChecked());

        holder.imgshare.setTag(arrayList.get(position));
        holder.imgshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OpenOrdersFragment)fragment).share(arrayList.get(position).getSONo(), String.format("%.2f",arrayList.get(position).getNetAmt()),
                        arrayList.get(position).getPaymentStatus(), arrayList.get(position).getMerchantname(),
                        arrayList.get(position).getMerchant_Mobile(), arrayList.get(position).getMerchAddress(),
                        arrayList.get(position).getMerchLatitude(),arrayList.get(position).getMerchLongitude(),
                        arrayList.get(position).getSOHeaderId());
            }
        });

       return view;
    }

    public View getView(int position){
        View view = holder.layout_ordermain.getChildAt(position);
        return view;
    }

    private static class ViewHolder {
        TextView title, amount, date, id, txtremark,txtseller,txtmobile,paytitle;
        ImageView imageview_placed, imageview_approved, imageview_dispatched, imageview_delivered,imgpaid,imgshare;
        /*, view_dotted, view_dotted_successful;*/
        TextView txtplaced,txtapproved, txtdispatched, txtdelivered;
        LinearLayout ly, layout_ordermain,laypaynow;
        RadioButton radiobtn;
        View view2;
        Switch switch_onoff;
        Button cancel_order;
    }

    public String getdate(String d) {

        String finalDate = "";

        try{
            if (!(d.equals("") || d == null)) {
//6\/1\/2016 12:31:00 PM
           /* SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "MM-dd-yyyy hh:mm:ss a");*/
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "MM/dd/yyyy hh:mm:ss");
                Date myDate = null;
                try {
                    myDate = dateFormat.parse(d);
                    System.out.println("..........value of my date after conv" + myDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat timeFormat = new SimpleDateFormat("MMM dd HH:mm");
                finalDate = timeFormat.format(myDate);

            } else {
                finalDate = "";
            }
        }catch (Exception e){
            e.printStackTrace();

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


    private class Interface {
    }

}