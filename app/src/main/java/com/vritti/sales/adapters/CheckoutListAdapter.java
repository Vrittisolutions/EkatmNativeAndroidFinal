package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.ProductListActivity;
import com.vritti.sales.beans.MyCartBean;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by sharvari on 4/29/2016.
 */
public class CheckoutListAdapter extends BaseAdapter {
    private ArrayList<MyCartBean> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
   // private AddProductToCartInterface addProductToCartInterface;

    public CheckoutListAdapter(Context context,
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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_checkoutadapter, null);
            holder = new ViewHolder();

            holder.txtItemname = (TextView)convertView.findViewById(R.id.cartlist_itemname);
            holder.txtSubTotal = (TextView)convertView.findViewById(R.id.cartlist_subtotal);
            holder.product_qty = (EditText) convertView.findViewById(R.id.cartlist_qty);
            holder.cart_Delete_row = (ImageView)convertView.findViewById(R.id.cartlist_delete_row_1);
            holder.cart_Delete_row.setVisibility(View.INVISIBLE);

       //     holder.textview_product_sellers = (TextView) convertView.findViewById(R.id.textview_product_sellers);
          /*  holder.textview_product_offers = (TextView) convertView
                    .findViewById(R.id.textview_product_offers);*/
       //     holder.textview_product_rate = (TextView) convertView.findViewById(R.id.textview_product_rate);
            /*holder.edit_product_qty = (EditText) convertView
                    .findViewById(R.id.edit_product_qty);*/
           // holder.imageviewAddProduct = (ImageView) convertView.findViewById(R.id.button_product_add_to_cart);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

       /* holder.categotyName.setText(arrayList.get(position).getCategoryName());
        holder.subcatcount.setText("" + arrayList.get(position).getSubcatcount());*/

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
       // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
        holder.txtSubTotal.setText(formatted +" ₹");

        holder.cart_Delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(parent instanceof ProductListActivity){
                            ((ProductListActivity)parent).showNewPrompt(pos);
                        }
                    }
                });

       // holder.Image_Delete_row.setTag(arrayList.get(position));

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