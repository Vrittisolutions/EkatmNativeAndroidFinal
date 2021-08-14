package com.vritti.sales.OrderBookingNew.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    private ArrayList<AllCatSubcatItems> arrayList;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;

    public CategoryAdapter(Context context, ArrayList<AllCatSubcatItems> list) {

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

        try{
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_category_row, null);
                holder = new ViewHolder();

                holder.card_view =  convertView.findViewById(R.id.card_view);
                holder.categotyName = (TextView) convertView.findViewById(R.id.textview_category_name);
                holder.subcatcount = (TextView) convertView.findViewById(R.id.txt_subcat_count);//txt_subcat_count
                holder.subcatcount.setVisibility(View.INVISIBLE);
                holder.imageview_cat_logo = (ImageView) convertView.findViewById(R.id.imageview_cat_logo);
                holder.rel_lay_grid =  convertView.findViewById(R.id.rel_lay_grid);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.categotyName.setText(arrayList.get(position).getCategoryName());
        holder.subcatcount.setText("" + arrayList.get(position).getSubcatcount());

         if(!arrayList.get(position).getCatImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getCatImgPath())
                        //.resize(50,55).into(holder.imgitm);
                        .into(holder.imageview_cat_logo);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getBusiSegImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(parent)
                        .load(arrayList.get(position).getBusiSegImgPath())
                        //.resize(50,55).into(holder.imgitm);
                        .into(holder.imageview_cat_logo);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


        /*if (position % 2 == 1) {
            //holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(Color.parseColor("#fee9ce"));
        } else {
            //holder.card_view.setBackgroundColor(Color.parseColor("#fef4e6"));
            holder.rel_lay_grid.setBackgroundColor(Color.parseColor("#fef4e6"));
        }*/

        if (position % 9 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            //holder.rel_lay_grid.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid9));
        }else if (position % 8 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid8));
        }else if (position % 7 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid7));
        }else if (position % 6 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid6));
        }else if (position % 5 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid5));
        }else if (position % 4 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid4));
        }else if (position % 3 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid3));
        }else if (position % 2 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid2));
        }else if (position % 1 == 0) {
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid1));
        }else if(position == 0){
            // holder.card_view.setBackgroundColor(Color.parseColor("#fee9ce"));
            holder.rel_lay_grid.setBackgroundColor(parent.getResources().getColor(R.color.grid1));
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView categotyName, subcatcount;
        ImageView imageview_cat_logo;
        CardView card_view;
        RelativeLayout rel_lay_grid;
    }


}