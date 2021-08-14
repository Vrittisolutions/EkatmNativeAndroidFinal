package com.vritti.sales.OrderBookingNew.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.DeliveryDetail_Multimerchant;
import com.vritti.sales.beans.MyCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OrdSummaryAdapter extends BaseAdapter {
    private ArrayList<MyCartBean> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
    boolean isViewOpen = false;

    public OrdSummaryAdapter(Context context, ArrayList<MyCartBean> list) {
        parent = context;
        arrayList = list;
        mInflater = LayoutInflater.from(parent);

    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ordersummary, null);
            holder = new ViewHolder();

            holder.txtamtpayble = (TextView)convertView.findViewById(R.id.txtamtpayble);
            holder.txtseller = (TextView)convertView.findViewById(R.id.txtseller);
            holder.txt_prodtotal = (TextView)convertView.findViewById(R.id.txt_prodtotal);
            holder.txt_yousave = (TextView)convertView.findViewById(R.id.txt_yousave);
            holder.txt_delcharges = (TextView)convertView.findViewById(R.id.txt_delcharges);
            holder.freedelaboveamt = (TextView)convertView.findViewById(R.id.freedelaboveamt);
            holder.freedelmaxdist = (TextView)convertView.findViewById(R.id.freedelmaxdist);
            holder.mindelkg = (TextView)convertView.findViewById(R.id.mindelkg);
            holder.mindelkm = (TextView)convertView.findViewById(R.id.mindelkm);
            holder.exprminutes = (TextView)convertView.findViewById(R.id.exprminutes);
            holder.exprschrge = (TextView)convertView.findViewById(R.id.exprschrge);
            holder.viewdtls = convertView.findViewById(R.id.viewdtls);
            holder.deliverydetails = convertView.findViewById(R.id.deliverydetails);
            holder.appl_del_charge = convertView.findViewById(R.id.appl_del_charge);
            holder.txtsorrynote = convertView.findViewById(R.id.txtsorrynote);
            holder.laydeldtldata = convertView.findViewById(R.id.laydeldtldata);
            holder.txtdist = convertView.findViewById(R.id.txtdist);
            holder.delchrge = convertView.findViewById(R.id.delchrge);
            holder.layfreedel = convertView.findViewById(R.id.layfreedel);
            holder.layexprs = convertView.findViewById(R.id.layexprs);
            holder.laycard = convertView.findViewById(R.id.laycard);
            holder.card_view = convertView.findViewById(R.id.card_view);
           // holder.txtpaynow = convertView.findViewById(R.id.txtpaynow);
            holder.remark = convertView.findViewById(R.id.remark);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtseller.setText(arrayList.get(position).getMerchantName());

        double amount = Double.parseDouble(String.valueOf(arrayList.get(position).getAmount()));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        holder.txt_prodtotal.setText(formatted +" ₹");
        holder.txt_yousave.setText("0.00" +" ₹");
       // holder.txt_delcharges.setText("0.00" +" ₹");
        float amt =0, delcharg = 0, payamt = 0;

        amt= arrayList.get(position).getAmount();
        delcharg = Float.parseFloat(arrayList.get(position).getAppliedDelCharges());

        if(delcharg == Float.parseFloat(arrayList.get(position).getExpressDelyChg())){
            holder.delchrge.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.layexprs.setBackgroundColor(Color.parseColor("#dcdcdc"));
            holder.layfreedel.setBackgroundColor(Color.parseColor("#ffffff"));
        }else if(delcharg > 0 || delcharg > 0.0 || delcharg > 0.00 ){
            holder.delchrge.setBackgroundColor(Color.parseColor("#dcdcdc"));
            holder.layexprs.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.layfreedel.setBackgroundColor(Color.parseColor("#ffffff"));
        }else{
            holder.delchrge.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.layexprs.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.layfreedel.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.txt_delcharges.setText(String.format("%.2f",delcharg)+" ₹");

        payamt = amt + delcharg;
        holder.txtamtpayble.setText(String.format("%.2f",payamt)+" ₹");

        if(arrayList.get(position).getFreeDelyMaxDist().contains(".0")){
            holder.freedelmaxdist.setText("Upto "+arrayList.get(position).getFreeDelyMaxDist().replace(".0","")+" km");
        }else {
            holder.freedelmaxdist.setText("Upto "+arrayList.get(position).getFreeDelyMaxDist()+" km");
        }

        if(arrayList.get(position).getMinDelyKm().contains(".0")){
            holder.mindelkm.setText(arrayList.get(position).getMinDelyKm().replace(".0","")+" ₹ per km");
        }else {
            holder.mindelkm.setText(arrayList.get(position).getMinDelyKm()+" ₹ per km");
        }

        if(arrayList.get(position).getMinDelyKg().contains(".0")){
            holder.mindelkg.setText(arrayList.get(position).getMinDelyKg().replace(".0","")+" ₹ per kg");

        }else {
            holder.mindelkg.setText(arrayList.get(position).getMinDelyKg()+" ₹ per kg");

        }

        String minutes = arrayList.get(position).getExprDelyWithinMin();
        if(arrayList.get(position).getExprDelyWithinMin().contains(".0")){
            minutes = arrayList.get(position).getExprDelyWithinMin().replace(".0","");
        }else if(arrayList.get(position).getExprDelyWithinMin().contains(".00")){
            minutes = arrayList.get(position).getExprDelyWithinMin().replace(".00","");
        }

        holder.exprminutes.setText("Within "+minutes+" minutes");

        double  freeamt = 0,exprschrge=0;

        if(arrayList.get(position).getFreeAboveAmt().equalsIgnoreCase("") ||
                arrayList.get(position).getFreeAboveAmt().equalsIgnoreCase(null)){
            freeamt = 0;
            holder.freedelaboveamt.setText("0.00");
        }else {
            freeamt = Double.parseDouble(arrayList.get(position).getFreeAboveAmt());
            holder.freedelaboveamt.setText("Above "+formatter.format(freeamt)+" ₹");
        }

        if(arrayList.get(position).getFreeAboveAmt().equalsIgnoreCase("") ||
                arrayList.get(position).getFreeAboveAmt().equalsIgnoreCase(null)){
            exprschrge = 0;
            holder.exprschrge.setText("0.00 ₹");
        }else {
            exprschrge = Double.parseDouble(arrayList.get(position).getExpressDelyChg());
            holder.exprschrge.setText(formatter.format(exprschrge)+" ₹");
        }

        holder.txtdist.setText(arrayList.get(position).getDistance()+" km");

        if(freeamt == 0 ||freeamt == 0.0 || exprschrge == 0 || exprschrge == 0.0){
            holder.laydeldtldata.setVisibility(View.GONE);
          //  holder.txtsorrynote.setVisibility(View.VISIBLE);
        }else {
            holder.laydeldtldata.setVisibility(View.GONE);
           // holder.txtsorrynote.setVisibility(View.GONE);
            /*try{
                float subtot = arrayList.get(position).getAmount();
                float custdist = Float.parseFloat(arrayList.get(position).getDistance());
                float custtotkg = Float.parseFloat(arrayList.get(position).getTotContentPerMerch());
                float freedelamt = Float.parseFloat(arrayList.get(position).getFreeAboveAmt());
                float exprdelchrge = Float.parseFloat(arrayList.get(position).getExpressDelyChg());
                float freetotdist =  Float.parseFloat(arrayList.get(position).getFreeDelyMaxDist());
                float mindelkg =  Float.parseFloat(arrayList.get(position).getMinDelyKg());
                float mindelkm =  Float.parseFloat(arrayList.get(position).getMinDelyKm());
                float exprdelmin =  Float.parseFloat(arrayList.get(position).getExprDelyWithinMin());

                TextView txtdelchrge = convertView.findViewById(R.id.txt_delcharges);
                txtdelchrge.setTag(arrayList.get(position));
                    //delivery charges will get applied
               if(subtot >= freedelamt && custdist <= freetotdist){
                        //free del charges applied
                        Toast.makeText(parent,"Free delivery service is applicable",Toast.LENGTH_SHORT).show();
                        txtdelchrge.setText("0.00 ₹");

                        arrayList.get(position).setAppliedDelCharges("0.00");

                   holder.delchrge.setBackgroundColor(Color.parseColor("#ffffff"));
                   holder.layexprs.setBackgroundColor(Color.parseColor("#ffffff"));
                   holder.layfreedel.setBackgroundColor(Color.parseColor("#dcdcdc"));

                } else if(Float.parseFloat(arrayList.get(position).getExpDelMinByCust()) <= exprdelmin &&
                       ((Float.parseFloat(arrayList.get(position).getExpDelMinByCust()) != 0.0) ||
                       (Float.parseFloat(arrayList.get(position).getExpDelMinByCust()) != 0))){
                   //express del charges
                   arrayList.get(position).setAppliedDelCharges(String.valueOf(exprschrge));
                   txtdelchrge.setText(String.format("%.2f",exprschrge)+" ₹");

                   holder.delchrge.setBackgroundColor(Color.parseColor("#ffffff"));
                   holder.layexprs.setBackgroundColor(Color.parseColor("#dcdcdc"));
                   holder.layfreedel.setBackgroundColor(Color.parseColor("#ffffff"));

                 }else if(subtot >= freedelamt && custdist >= freetotdist){
                        //del charges applied as per km
                      float diffkm = custdist - freetotdist;
                      float delchrge = diffkm * mindelkm;
                      delchrge = math(delchrge);

                      arrayList.get(position).setAppliedDelCharges(String.valueOf(delchrge));
                        txtdelchrge.setText(String.format("%.2f",delchrge)+" ₹");

                   holder.delchrge.setBackgroundColor(Color.parseColor("#dcdcdc"));
                   holder.layexprs.setBackgroundColor(Color.parseColor("#ffffff"));
                   holder.layfreedel.setBackgroundColor(Color.parseColor("#ffffff"));
                }

               //update applieddelcharges
                ((DeliveryDetail_Multimerchant)parent).updateDelChargesToAmt(arrayList);

            }catch (Exception e){
                e.printStackTrace();
            }*/
        }

        if(arrayList.get(position).getMerchantName().equalsIgnoreCase("Madam Home")){
            holder.remark.setVisibility(View.VISIBLE);
        }else {
            holder.remark.setVisibility(View.GONE);
        }

        if(arrayList.get(position).getIsMerchDelivery().equalsIgnoreCase("Y")){
            //show not providing delivery
            holder.txtsorrynote.setVisibility(View.GONE);
        } else {
            holder.txtsorrynote.setVisibility(View.VISIBLE);
        }

        if(arrayList.get(position).getUPIMerch().equalsIgnoreCase("") ||
                arrayList.get(position).getUPIMerch().equalsIgnoreCase("null") ||
                arrayList.get(position).getUPIMerch().equalsIgnoreCase(null)){
            //hide paynow
         //   holder.txtpaynow.setVisibility(View.GONE);
        } else {
            //show paynow
         //   holder.txtpaynow.setVisibility(View.VISIBLE);

          /* TextView txtpaynow = convertView.findViewById(R.id.txtpaynow);
            txtpaynow.setTag(arrayList.get(position));
*/
            float finalPayamt = payamt;
            TextView txtamtpayble = convertView.findViewById(R.id.txtamtpayble);
            txtamtpayble.setTag(arrayList.get(position));

          /*  holder.txtpaynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(parent instanceof DeliveryDetail_Multimerchant){
                        ((DeliveryDetail_Multimerchant)parent).payToMerchant(txtamtpayble.getText().toString(),
                                arrayList.get(position).getUPIMerch(),arrayList.get(position).getMerchantName(),
                                arrayList.get(position).getMerchantId());
                    }
                }
            });*/
        }

        ImageView viewdtls = convertView.findViewById(R.id.viewdtls);
        LinearLayout deldtls = convertView.findViewById(R.id.deliverydetails);
        viewdtls.setTag(arrayList.get(position));
        deldtls.setTag(arrayList.get(position));
        viewdtls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //deldtls.setVisibility(View.VISIBLE);
                //open dialog box and pass it values
                if(isViewOpen==false){
                    isViewOpen = true;
                //   deldtls.setVisibility(View.VISIBLE);
                    //((DeliveryDetail_Multimerchant)parent).getOrdsumlistData();
                }else {
                    isViewOpen = false;
                //    deldtls.setVisibility(View.GONE);
                    //((DeliveryDetail_Multimerchant)parent).getOrdsumlistData();
                }
            }
        });
        return convertView;
    }

    public static int math(float f) {
        int c = (int) ((f) + 0.5f);
        float n = f + 0.5f;
        return (n - c) % 2 == 0 ? (int) f : c;
    }

    private static class ViewHolder {
        TextView txtamtpayble,txtseller,txt_prodtotal,txt_yousave,txt_delcharges,freedelaboveamt,freedelmaxdist,mindelkg,
                mindelkm,exprminutes,exprschrge,txtviewdtls,appl_del_charge,txtsorrynote,txtdist,txtpaynow,remark;
        LinearLayout deliverydetails,laydeldtldata,layfreedel,delchrge,layexprs,laycard;
        ImageView viewdtls;
        CardView card_view;
    }

}