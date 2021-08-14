package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;

import java.util.ArrayList;

/**
 * Created by sharvari on 4/21/2016.
 */
public class SubcategoryAdapter extends BaseAdapter {
    private ArrayList<AllCatSubcatItems> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
 //   private AddProductToCartInterface addProductToCartInterface;

    public SubcategoryAdapter(Context context,
                              ArrayList<AllCatSubcatItems> list
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
            convertView = mInflater.inflate(R.layout.tbuds_custom_category_row, null);
            holder = new ViewHolder();

            holder.subcategotyName = (TextView) convertView
                    .findViewById(R.id.textview_category_name);
            holder.itemcount = (TextView) convertView
                    .findViewById(R.id.txt_subcat_count);
            holder.imageview_cat_logo = (ImageView) convertView
                    .findViewById(R.id.imageview_cat_logo);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.subcategotyName.setText(arrayList.get(position).getSubCategoryName());
        holder.itemcount.setText("" + arrayList.get(position).getItemcount());

        Picasso.with(parent)
                .load("http://www.iconsdb.com/icons/preview/green/cart-43-xxl.png")
           //     .placeholder(R.drawable.default_img).error(R.drawable.error)      // optional
                .resize(60,60)                        // optional
                .into(holder.imageview_cat_logo);
        return convertView;
    }

    private static class ViewHolder {
        TextView subcategotyName, itemcount;
        ImageView imageview_cat_logo;
    }
}
