package com.vritti.sales.OrderBookingNew.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.CartActivity_MultiMerchant;
import com.vritti.sales.beans.MyCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CartList_multimerchantAdapter extends BaseAdapter {
    private ArrayList<MyCartBean> arrayList;
    static ArrayList<MyCartBean> arrayList1;
    private static ArrayList<MyCartBean> arrayListFiltered;
    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;

    public CartList_multimerchantAdapter(Context context, ArrayList<MyCartBean> list) {
        parent = context;
        arrayList = list;
        mInflater = LayoutInflater.from(parent);
        this.arrayListFiltered= list;
        this.arrayList1=new ArrayList<>();
        this.arrayList1.addAll(list);
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

        try{
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.cartadapter_multiseller, null);
                holder = new ViewHolder();

                holder.laycard = convertView.findViewById(R.id.laycard);
                holder.laymrpselrate = convertView.findViewById(R.id.laymrpselrate);
                holder.layrangerate = convertView.findViewById(R.id.layrangerate);
                holder.layrangenote = convertView.findViewById(R.id.layrangenote);
                holder.layrangenote.setVisibility(View.GONE);
                holder.imgitm = convertView.findViewById(R.id.imgitm);
                holder.txtItemname = (TextView)convertView.findViewById(R.id.cartlist_itemname);
                holder.txtSubTotal = (TextView)convertView.findViewById(R.id.cartlist_subtotal);
                holder.product_qty = (EditText) convertView.findViewById(R.id.cartlist_qty);
                holder.txtseller = (TextView) convertView.findViewById(R.id.txtseller);
                holder.txt_rate = (TextView) convertView.findViewById(R.id.txt_rate);
                holder.txtmrp = (TextView) convertView.findViewById(R.id.txtmrp);
                holder.txt_yousave = (TextView) convertView.findViewById(R.id.txt_yousave);
                holder.btndelete =  convertView.findViewById(R.id.btndelete);
                holder.txtminordqty = convertView.findViewById(R.id.txtminordqty);
                holder.txtmaxordqty = convertView.findViewById(R.id.txtmaxordqty);
                holder.txtrange = convertView.findViewById(R.id.txtrange);
                holder.txtrangenote = convertView.findViewById(R.id.txtrangenote);
                holder.txtstocknote = convertView.findViewById(R.id.txtstocknote);
                holder.txtdist = convertView.findViewById(R.id.txtdist);
                holder.txtraterange = convertView.findViewById(R.id.txtraterange);
                holder.txtbrand = convertView.findViewById(R.id.txtbrand);
                holder.laymrp_1 = convertView.findViewById(R.id.laymrp_1);
                holder.layusave_1 = convertView.findViewById(R.id.layusave_1);
                holder.layminmax = convertView.findViewById(R.id.layminmax);
                holder.laybuy1get1 = convertView.findViewById(R.id.laybuy1get1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        final View finalConvertView = convertView;

        //holder.txtuom_code.setText(arrayListFiltered.get(position).getUOMCode());
        if(arrayListFiltered.get(position).getBrand().equalsIgnoreCase("")){
            holder.txtbrand.setVisibility(View.GONE);
        }else {
            holder.txtbrand.setVisibility(View.VISIBLE);
            holder.txtbrand.setText(arrayListFiltered.get(position).getBrand());
        }

        int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerdigit());
        if(decimal_digit==0){

            if(arrayListFiltered.get(position).getMinqnty().contains(".0")){
                holder.txtminordqty.setText(arrayListFiltered.get(position).getMinqnty().replace(".0",""));
            }else if(arrayListFiltered.get(position).getMinqnty().contains(".00")){
                holder.txtminordqty.setText(arrayListFiltered.get(position).getMinqnty().replace(".00",""));
            }else {
                holder.txtminordqty.setText(arrayListFiltered.get(position).getMinqnty());
            }

            if(arrayListFiltered.get(position).getMaxOrdQty().contains(".0")){
                holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty().replace(".0",""));
            }else if(arrayListFiltered.get(position).getMaxOrdQty().contains(".00")){
                holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty().replace(".00",""));
            }else {
                holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty());
            }

        }else {
            holder.txtminordqty.setText(arrayListFiltered.get(position).getMinqnty());
            holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty());
        }

        holder.txtrange.setText(arrayListFiltered.get(position).getRange());
        final String range = arrayListFiltered.get(position).getRange();
        final String distance = arrayListFiltered.get(position).getDistance();
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

        holder.product_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Float ss = null;
                float pc = 0.0f;
                TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.cartlist_subtotal);
                EditText edtqty = (EditText)finalConvertView.findViewById(R.id.cartlist_qty);

                edtqty.setTag(arrayList.get(position));

                if (((s.toString().trim() == "") || (s.toString() == null) || (s.toString().length() == 0))) {
                    arrayList.get(pos).setQnty(Float.valueOf(0));
                    // holder.txtSubTotal.setText("0 ₹");
                    txtTotal.setText("0");
                    arrayList.get(pos).setAmount(Float.valueOf(0));
                } else {
                    // int pos = (int) holder.Edit_productQty.getTag();
                    int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerdigit());

                    ss = Float.valueOf(s.toString());

                    float maxOrdLimit = Float.parseFloat(arrayListFiltered.get(pos).getMaxOrdQty());
                    float minOrdLimit = Float.parseFloat(arrayListFiltered.get(pos).getMinqnty());

                    if((ss > maxOrdLimit || ss < minOrdLimit) && (maxOrdLimit != 0 || maxOrdLimit != 0.0)){

                       // Toast.makeText(parent,""+parent.getResources().getString(R.string.maxordlimit),Toast.LENGTH_SHORT).show();
                        arrayListFiltered.get(pos).setAmount(Float.valueOf(0));
                        // Snackbar.make(view, context.getResources().getString(R.string.maxordlimit), Snackbar.LENGTH_SHORT).show();
                        if(edtqty.isFocusable()){
                            edtqty.setError(parent.getResources().getString(R.string.maxordlimit));
                        }else {
                            edtqty.setError(null);
                        }
                        //holder.edt_qty.setText("0");

                    }else {

                        if (decimal_digit == 0) {

                            edtqty.setInputType(InputType.TYPE_CLASS_NUMBER);

                            ss = Float.valueOf(s.toString());
                            // int q = Integer.parseInt(ss);
                            float p = Float.parseFloat(String.valueOf(ss));
                            arrayList.get(pos).setQnty(p);

                            if(range.equalsIgnoreCase("true")){
                                pc = arrayListFiltered.get(pos).getMrp();
                            }else {
                                pc = Float.parseFloat(arrayListFiltered.get(pos).getPrice());
                            }

                            float subtotal = Float.parseFloat(String.valueOf(ss)) * pc;
                            // float sbtotal = (arrayList.get(pos).getQnty())*100;

                            arrayList.get(pos).setAmount(subtotal);

                        } else {

                            edtqty.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, decimal_digit)});

                            ss = Float.valueOf(s.toString());
                            // int q = Integer.parseInt(ss);
                            float p = Float.parseFloat(String.valueOf(ss));
                            arrayList.get(pos).setQnty(p);

                            if(range.equalsIgnoreCase("true")){
                                pc = arrayListFiltered.get(pos).getMrp();
                            }else {
                                pc = Float.parseFloat(arrayListFiltered.get(pos).getPrice());
                            }

                            float subtotal = Float.parseFloat(String.valueOf(ss)) * pc;
                            // float sbtotal = (arrayList.get(pos).getQnty())*100;

                            arrayList.get(pos).setAmount(subtotal);

                        }

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                TextView txt_yousave = (TextView) finalConvertView.findViewById(R.id.txt_yousave);
                TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.cartlist_subtotal);
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {
                    arrayList.get(pos).setQnty((float) 0);
                    // holder.txtSubTotal.setText("0 ₹");
                    txtTotal.setText("0.00 ");
                    arrayList.get(pos).setAmount((float) 0);

                    float qty_= 0;
                    float youSave = (arrayList.get(position).getMrp() *qty_)  - (Float.parseFloat(arrayList.get(position).getPrice())*qty_);
                    arrayList.get(position).setYousave(youSave);
                    txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);
                }else
                {
                    // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
                    //holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
                    txtTotal.setText(arrayList.get(position).getAmount()+"");

                    float qty_= Float.parseFloat(s.toString());
                    float youSave = (arrayList.get(position).getMrp() *qty_)  - (Float.parseFloat(arrayList.get(position).getPrice())*qty_);
                    arrayList.get(position).setYousave(youSave);
                    txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);
                }

                //to calculate total of all products
                if(parent instanceof CartActivity_MultiMerchant){
                    ((CartActivity_MultiMerchant)parent).calculate_total();
                }

            }
        });

        holder.btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(parent instanceof CartActivity_MultiMerchant){
                   try{
                       ((CartActivity_MultiMerchant)parent).deleteItemFromCart(
                               arrayList.get(position).getProduct_id(),
                               arrayList.get(position).getMerchantId());
                   }catch (Exception e){
                       e.printStackTrace();
                   }
               }
            }
        });

        if(arrayList.get(position).getMerchantName().equalsIgnoreCase("") ||
                arrayList.get(position).getMerchantName().equalsIgnoreCase(null)){
            holder.txtseller.setVisibility(View.GONE);
        }else {
            holder.txtseller.setVisibility(View.VISIBLE);
            holder.txtseller.setText("By "+arrayList.get(position).getMerchantName() +" "
                    +parent.getResources().getString(R.string.distance)+" "+arrayList.get(position).getDistance()+" km");
        }

        holder.txtmrp.setText(String.format("%.2f",arrayList.get(position).getMrp()));

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

        //holder.txtcategory.setText(arrayList.get(position).getCategoryName());
        //holder.txtsubcategory.setText(arrayList.get(position).getSubCategoryName());
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
        //holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
        holder.txtSubTotal.setText(formatted);

        float price = Float.parseFloat(arrayListFiltered.get(pos).getPrice());
        float mrp = arrayListFiltered.get(pos).getMrp();

        if(range.equalsIgnoreCase("true")){
            holder.txtraterange.setText(String.format("%.2f",price) + " - "+ String.format("%.2f",mrp));

        }else {

            double amount1 = Double.parseDouble(arrayList.get(position).getPrice());
            formatter = new DecimalFormat("#,##,##,###.00");
            String formatted2 = formatter.format(amount1);
            holder.txt_rate.setText(formatted2);
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

      /*  holder.cart_Delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(parent instanceof ProductListActivity){
                            ((ProductListActivity)parent).showNewPrompt(pos);
                        }
                    }
                });*/

        if(arrayList.get(pos).getMrp() == Float.parseFloat(arrayList.get(pos).getPrice())){
            holder.laymrp_1.setVisibility(View.GONE);
            holder.layusave_1.setVisibility(View.INVISIBLE);
            holder.txt_rate.setVisibility(View.VISIBLE);
            /*holder.laymrp_1.setAlpha((float)0.2);
            holder.layusave_1.setAlpha((float)0.2);*/
        }else {
            holder.laymrp_1.setVisibility(View.VISIBLE);
            holder.layusave_1.setVisibility(View.VISIBLE);
            holder.txt_rate.setVisibility(View.VISIBLE);
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
                if(parent instanceof CartActivity_MultiMerchant){
                    ((CartActivity_MultiMerchant)parent).expandImage(arrayList.get(position).getItemImgPath(),
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
                txtminordqty,txtmaxordqty,txtrange,txtrangenote, txtstocknote, txtdist,txtraterange,txtuom_code,txtbrand;
        ImageView imgitm;
        EditText product_qty;
        TextView txtItemname, txtSubTotal,txtseller,txt_rate,txtmrp,txt_yousave;
        ImageView cart_Delete_row;
        Button btndelete;
        LinearLayout laycard, laymrpselrate, layrangerate,layrangenote,layusave_1,laymrp_1,layminmax,laybuy1get1;
    }

    public ArrayList<MyCartBean> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayListFiltered.clear();
        if (charText.length() == 0) {
            arrayListFiltered.addAll(arrayList1);
        } else {
            for (MyCartBean wp : arrayList1) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getBrand().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getMerchantName().toLowerCase(Locale.getDefault()).contains(charText) ) {
                    arrayListFiltered.add(wp);
                    ((CartActivity_MultiMerchant)parent).updateList(arrayList);
                }
            }
        }

        notifyDataSetChanged();
        return arrayList;
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}