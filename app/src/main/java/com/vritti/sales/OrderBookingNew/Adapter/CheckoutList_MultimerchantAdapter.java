package com.vritti.sales.OrderBookingNew.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.CheckoutActivity_Multimerchant;
import com.vritti.sales.beans.MyCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CheckoutList_MultimerchantAdapter extends BaseAdapter {
    private ArrayList<MyCartBean> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;

    public CheckoutList_MultimerchantAdapter(Context context,
                                             ArrayList<MyCartBean> list) {
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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;

        try{
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.checkoutadapter, null);
                holder = new ViewHolder();

                holder.imgitm = convertView.findViewById(R.id.imgitm);
                holder.txtItemname = (TextView)convertView.findViewById(R.id.cartlist_itemname);
                holder.txtSubTotal = (TextView)convertView.findViewById(R.id.cartlist_subtotal);
                holder.product_qty =  convertView.findViewById(R.id.cartlist_qty);
                holder.txtsubcategory = (TextView) convertView.findViewById(R.id.txtsubcategory);
                holder.txtcategory = (TextView) convertView.findViewById(R.id.txtcategory);
                holder.rate = (TextView) convertView.findViewById(R.id.rate);
                holder.txtmrp = (TextView) convertView.findViewById(R.id.txtmrp);
                holder.txt_yousave = (TextView) convertView.findViewById(R.id.txt_yousave);
                holder.txtseller = (TextView) convertView.findViewById(R.id.txtseller);
                holder.txtdist = (TextView) convertView.findViewById(R.id.txtdist);
                holder.laysellerdist =  convertView.findViewById(R.id.laysellerdist);
                holder.laydist =  convertView.findViewById(R.id.laydist);
                holder.laysellerdist.setVisibility(View.VISIBLE);
                holder.txtdist.setVisibility(View.VISIBLE);
                holder.txtseller.setVisibility(View.VISIBLE);
                holder.laydist.setVisibility(View.GONE);
                holder.txtrange = convertView.findViewById(R.id.txtrange);
                holder.txtrangenote = convertView.findViewById(R.id.txtrange);
                holder.layrangerate = convertView.findViewById(R.id.layrangerate);
                holder.txtraterange = convertView.findViewById(R.id.txtraterange);
                holder.laymrpselrate = convertView.findViewById(R.id.laymrpselrate);
                holder.txtminordqty = convertView.findViewById(R.id.txtminordqty);
                holder.txtmaxordqty = convertView.findViewById(R.id.txtmaxordqty);
                holder.txtbrand = convertView.findViewById(R.id.txtbrand);
                holder.laymrp_1 = convertView.findViewById(R.id.laymrp_1);
                holder.layusave_1 = convertView.findViewById(R.id.layusave_1);
                holder.layminmax = convertView.findViewById(R.id.layminmax);
                holder.laybuy1get1 = convertView.findViewById(R.id.laybuy1get1);

               /* holder.textview_product_sellers = (TextView) convertView.findViewById(R.id.textview_product_sellers);
                holder.textview_product_rate = (TextView) convertView.findViewById(R.id.textview_product_rate);*/

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(arrayList.get(position).getBrand().equalsIgnoreCase("")){
            holder.txtbrand.setVisibility(View.GONE);
        }else {
            holder.txtbrand.setVisibility(View.VISIBLE);
            holder.txtbrand.setText(arrayList.get(position).getBrand());
        }

        int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerdigit());
        if(decimal_digit==0){

            if(arrayList.get(position).getMinqnty().contains(".0")){
                holder.txtminordqty.setText(arrayList.get(position).getMinqnty().replace(".0",""));
            }else if(arrayList.get(position).getMinqnty().contains(".00")){
                holder.txtminordqty.setText(arrayList.get(position).getMinqnty().replace(".00",""));
            }else {
                holder.txtminordqty.setText(arrayList.get(position).getMinqnty());
            }

            if(arrayList.get(position).getMaxOrdQty().contains(".0")){
                holder.txtmaxordqty.setText(arrayList.get(position).getMaxOrdQty().replace(".0",""));
            }else if(arrayList.get(position).getMaxOrdQty().contains(".00")){
                holder.txtmaxordqty.setText(arrayList.get(position).getMaxOrdQty().replace(".00",""));
            }else {
                holder.txtmaxordqty.setText(arrayList.get(position).getMaxOrdQty());
            }

        }else {
            holder.txtmaxordqty.setText(arrayList.get(position).getMaxOrdQty());
            holder.txtminordqty.setText(arrayList.get(position).getMinqnty());
        }

        //holder.txtuom_code.setText(arrayList.get(position).getUOMCode());

        if(arrayList.get(position).getPackOfQty().equalsIgnoreCase("0") ||
                arrayList.get(position).getPackOfQty().equalsIgnoreCase("1") ||
                arrayList.get(position).getPackOfQty().equalsIgnoreCase("0.0") ||
                arrayList.get(position).getPackOfQty().equalsIgnoreCase("1.0")){

            if(arrayList.get(position).getBrand().equalsIgnoreCase("")||
                    arrayList.get(position).getBrand().equalsIgnoreCase(null) ||
                    arrayList.get(position).getBrand().equalsIgnoreCase("null")){
                holder.txtItemname.setText(/*arrayList.get(position).getBrand()+""+*/arrayList.get(position).getProduct_name()+ ", "
                        +arrayList.get(position).getContent().replace(".0","")+" "+arrayList.get(position).getUOMCode());
            }else {
                holder.txtItemname.setText(/*arrayList.get(position).getBrand()+" "+*/arrayList.get(position).getProduct_name()+ ", "
                        +arrayList.get(position).getContent().replace(".0","")+" "+arrayList.get(position).getUOMCode());
            }

        }else {

            if(arrayList.get(position).getBrand().equalsIgnoreCase("")||
                    arrayList.get(position).getBrand().equalsIgnoreCase(null) ||
                    arrayList.get(position).getBrand().equalsIgnoreCase("null")){
                holder.txtItemname.setText(/*arrayList.get(position).getBrand()+""+*/arrayList.get(position).getProduct_name() + ", "+parent.getResources().getString(R.string.combo1)+" "
                        +arrayList.get(position).getContent().replace(".0","")+
                        " "+arrayList.get(position).getUOMCode()+
                        " x "+arrayList.get(position).getPackOfQty());
            }else {
                holder.txtItemname.setText(/*arrayList.get(position).getBrand()+" "+*/arrayList.get(position).getProduct_name() + ", "+parent.getResources().getString(R.string.combo1)+" "
                        +arrayList.get(position).getContent().replace(".0","")+
                        " "+arrayList.get(position).getUOMCode()+
                        " x "+arrayList.get(position).getPackOfQty());
            }
        }

        if(arrayList.get(position).getProduct_name().contains("Buy") &&
                (arrayList.get(position).getProduct_name().contains("Free") ||
                        arrayList.get(position).getProduct_name().contains("free"))){
            holder.laybuy1get1.setVisibility(View.VISIBLE);
        }else {
            holder.laybuy1get1.setVisibility(View.GONE);
        }

        holder.txtcategory.setText(arrayList.get(position).getCategoryName());
        holder.txtsubcategory.setText(arrayList.get(position).getSubCategoryName());
        final String range = arrayList.get(position).getRange();
        final String distance = arrayList.get(position).getDistance();
        holder.txtdist.setText(distance);

        if(range.equalsIgnoreCase("true")){
            holder.laymrpselrate.setVisibility(View.GONE);
            holder.layrangerate.setVisibility(View.VISIBLE);

            holder.txtrange.setText(parent.getResources().getText(R.string.yes));
            holder.txtrangenote.setVisibility(View.VISIBLE);
            holder.txtrangenote.setText(""+parent.getResources().getText(R.string.rangenote));
        }else {
            holder.laymrpselrate.setVisibility(View.VISIBLE);
            holder.layrangerate.setVisibility(View.GONE);

            holder.txtrange.setText(parent.getResources().getText(R.string.no));
            holder.txtrangenote.setVisibility(View.GONE);
            //holder.txtrangenote.setText(""+context.getResources().getText(R.string.rangenote));
        }

        String sval = String.valueOf(arrayList.get(position).getQnty());
        if(sval.contains(".0")){
            String s = ".0";
            if (sval.endsWith(".0")) {
                sval =  sval.substring(0, sval.length() - s.length());
            }
            /*String arr[] = sval.split(".0");
            sval = arr[0];*/
        }

        holder.product_qty.setText(sval);

        double amount = Double.parseDouble(String.valueOf(arrayList.get(position).getAmount()));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
       // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
        holder.txtSubTotal.setText(formatted);

        double amount1 = Double.parseDouble(String.valueOf(arrayList.get(position).getPrice()));
        formatter = new DecimalFormat("#,##,##,###.00");
        String formatted2 = formatter.format(amount1);
       // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
        holder.rate.setText(formatted2);

        holder.txtmrp.setText(String.format("%.2f",arrayList.get(position).getMrp()));

        float price2 = Float.parseFloat(arrayList.get(pos).getPrice());
        float mrp2 = arrayList.get(pos).getMrp();

        if(range.equalsIgnoreCase("true")){
            holder.txtraterange.setText(String.format("%.2f",price2) + " - "+ String.format("%.2f",mrp2));
        }

        float qty_= Float.parseFloat(holder.product_qty.getText().toString());
        float youSave = (arrayList.get(position).getMrp() *qty_)  - (Float.parseFloat(arrayList.get(position).getPrice())*qty_);
        if(arrayList.get(position).getRange().equalsIgnoreCase("true")){
            youSave = 0;
        }else {
            youSave = youSave;
        }
        arrayList.get(position).setYousave(youSave);
        holder.txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);

        holder.txtseller.setText("By "+arrayList.get(position).getMerchantName() +" "
                +parent.getResources().getString(R.string.distance)+" "+arrayList.get(position).getDistance()+" km");

        if(arrayList.get(pos).getMrp() == Float.parseFloat(arrayList.get(pos).getPrice())){
            holder.laymrp_1.setVisibility(View.GONE);
            holder.layusave_1.setVisibility(View.INVISIBLE);
            holder.rate.setVisibility(View.VISIBLE);
            /*holder.laymrp_1.setAlpha((float)0.2);
            holder.layusave_1.setAlpha((float)0.2);*/
        }else {
            holder.laymrp_1.setVisibility(View.VISIBLE);
            holder.layusave_1.setVisibility(View.VISIBLE);
            holder.rate.setVisibility(View.VISIBLE);
           /* holder.laymrp_1.setAlpha((float)1);
            holder.layusave_1.setAlpha((float)1);*/
        }

        if((arrayList.get(position).getMinqnty().equalsIgnoreCase("0") ||
                arrayList.get(position).getMinqnty().equalsIgnoreCase("0.0"))
                && (arrayList.get(position).getMaxOrdQty().equalsIgnoreCase("0") ||
                arrayList.get(position).getMaxOrdQty().equalsIgnoreCase("0.0"))){
            holder.layminmax.setVisibility(View.GONE);
        }else {
            holder.layminmax.setVisibility(View.VISIBLE);
        }

        if(!arrayList.get(position).getItemImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getItemImgPath())
                        //.resize(50,55).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();

            }
        }else if(!arrayList.get(position).getSubCatImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getSubCatImgPath())
                        //.resize(60,60).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getCatImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getCatImgPath())
                        //.resize(60,60).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getBusiSegImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getBusiSegImgPath())
                        //.resize(50,55).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        holder.imgitm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(parent instanceof CheckoutActivity_Multimerchant){
                    ((CheckoutActivity_Multimerchant)parent).expandImage(arrayList.get(position).getItemImgPath(),
                            arrayList.get(position).getProduct_name(),
                            arrayList.get(position).getBrand(),
                            arrayList.get(position).getContent().replace(".0",""),
                            arrayList.get(position).getUOMCode(),
                            arrayList.get(position).getPackOfQty());
                }
                return false;
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView textview_product_sellers,textview_product_rate,txtcategory,txtsubcategory,
                txtseller,txtdist,txtuom_code,txtraterange, txtmaxordqty,txtminordqty, txtrange,txtrangenote,txtbrand,product_qty;
        TextView txtItemname, txtSubTotal,rate,txt_yousave,txtmrp;
        ImageView imgitm;
        LinearLayout laysellerdist,laydist,laymrpselrate, layrangerate,layrangenote,layusave_1,laymrp_1,layminmax,laybuy1get1;
    }

}