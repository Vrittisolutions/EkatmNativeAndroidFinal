package com.vritti.sales.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.CartActivity;
import com.vritti.sales.activity.ProductListActivity;
import com.vritti.sales.beans.MyCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sharvari on 4/29/2016.
 */

public class CartListAdapter extends BaseAdapter {
    private ArrayList<MyCartBean> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
 //   private AddProductToCartInterface addProductToCartInterface;

    public CartListAdapter(Context context,
                           ArrayList<MyCartBean> list
    ) {

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
            convertView = mInflater.inflate(R.layout.tbuds_cartadapter, null);
            holder = new ViewHolder();

            holder.txtItemname = (TextView)convertView.findViewById(R.id.cartlist_itemname);
            holder.txtSubTotal = (TextView)convertView.findViewById(R.id.cartlist_subtotal);
            holder.product_qty = (EditText) convertView.findViewById(R.id.cartlist_qty);
            holder.cart_Delete_row = (ImageView)convertView.findViewById(R.id.cartlist_delete_row_1);
            holder.cart_Delete_row.setVisibility(View.INVISIBLE);
     //       holder.textview_product_sellers = (TextView) convertView.findViewById(R.id.textview_product_sellers);
          /*  holder.textview_product_offers = (TextView) convertView
                    .findViewById(R.id.textview_product_offers);*/
     //       holder.textview_product_rate = (TextView) convertView.findViewById(R.id.textview_product_rate);
            /*holder.edit_product_qty = (EditText) convertView
                    .findViewById(R.id.edit_product_qty);*/
           // holder.imageviewAddProduct = (ImageView) convertView.findViewById(R.id.button_product_add_to_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final View finalConvertView = convertView;


        holder.product_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Float ss = null;
                TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.cartlist_subtotal);

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {
                    arrayList.get(pos).setQnty(Float.valueOf(0));
                    // holder.txtSubTotal.setText("0 ₹");
                    txtTotal.setText("0 ₹");
                    arrayList.get(pos).setAmount(Float.valueOf(0));
                } else {
                    // int pos = (int) holder.Edit_productQty.getTag();
                    int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerdigit());

                    holder.product_qty.setTag(arrayList.get(position));

                    if (decimal_digit == 0) {

                        holder.product_qty.setInputType(InputType.TYPE_CLASS_NUMBER);

                        ss = Float.valueOf(s.toString());
                        // int q = Integer.parseInt(ss);
                        float p = Float.parseFloat(String.valueOf(ss));
                        arrayList.get(pos).setQnty(p);

                        float pc = Float.parseFloat(arrayList.get(pos).getPrice());

                        float subtotal = Float.parseFloat(String.valueOf(ss)) * pc;
                        // float sbtotal = (arrayList.get(pos).getQnty())*100;

                        arrayList.get(pos).setAmount(subtotal);

                    } else {

                        holder.product_qty.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        ss = Float.valueOf(s.toString());
                        // int q = Integer.parseInt(ss);
                        float p = Float.parseFloat(String.valueOf(ss));
                        arrayList.get(pos).setQnty(p);

                        float pc = Float.parseFloat(arrayList.get(pos).getPrice());

                        float subtotal = Float.parseFloat(String.valueOf(ss)) * pc;
                        // float sbtotal = (arrayList.get(pos).getQnty())*100;

                        arrayList.get(pos).setAmount(subtotal);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

               TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.cartlist_subtotal);
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {
                    arrayList.get(pos).setQnty((float) 0);
                   // holder.txtSubTotal.setText("0 ₹");
                    txtTotal.setText("0 ₹");
                    arrayList.get(pos).setAmount((float) 0);
                }else
                {
                    // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
                    //holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
                    txtTotal.setText(arrayList.get(position).getAmount()+" ₹");
                }

                //to calculate total of all products
                if(parent instanceof CartActivity){
                    ((CartActivity)parent).calculate_total();
                }
            }
        });

        holder.txtItemname.setText(arrayList.get(position).getProduct_name());
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
        holder.txtSubTotal.setText(formatted +" ₹");

        holder.cart_Delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(parent instanceof ProductListActivity){
                            ((ProductListActivity)parent).showNewPrompt(pos);
                        }
                    }
                });

        return convertView;
    }

    private static class ViewHolder {
        TextView textview_product_sellers,textview_product_offers,textview_product_rate;
        ImageView imageviewAddProduct;
        EditText product_qty;
        TextView txtItemname, txtSubTotal;
        ImageView cart_Delete_row;
    }

}