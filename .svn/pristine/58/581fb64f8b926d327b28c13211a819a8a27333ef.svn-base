package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.CounterbillingBean;

import java.util.ArrayList;

/**
 * Created by 300151 on 7/19/2016.
 */
public class AdapterCounterBilling extends BaseAdapter {
    private ArrayList<CounterbillingBean> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
    CounterbillingBean counterbillingBean;
  //  private AddProductToCartInterface addProductToCartInterface;

    public AdapterCounterBilling(Context context,
                                 ArrayList<CounterbillingBean> list
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        if (convertView == null) {


            convertView = mInflater.inflate(R.layout.tbuds_custome_counter_billing, null);
            holder = new ViewHolder();

            holder.ItemName = (TextView) convertView
                    .findViewById(R.id.textview_customer_name);
            holder.Mobno = (TextView) convertView
                    .findViewById(R.id.textview_Mobno);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ItemName.setText(arrayList.get(position).getCustName());

        holder.Mobno.setText(arrayList.get(position).getMobileNo());
        return convertView;
    }

    private static class ViewHolder {
        TextView ItemName, Mobno;
        ImageView imageview_product_logo;
    }


}
