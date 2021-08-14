package com.vritti.crm.adapter;

/**
 * Created by sharvari on 18-Sep-19.
 */

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.crm.bean.NearBySearchData;
import com.vritti.crm.vcrm7.NearBySearch;
import com.vritti.ekatm.R;


import java.util.ArrayList;


/**
 * Created by Jerry on 12/19/2017.
 */

public class NearBySearchAdapter extends RecyclerView.Adapter<NearBySearchAdapter.AssistantViewHolder> {

    private ArrayList<NearBySearchData> nearBySearchDataArrayList = null;
    Context context;


    public NearBySearchAdapter(Context context, ArrayList<NearBySearchData> itemReportArrayList) {
        this.context=context;
        this.nearBySearchDataArrayList = itemReportArrayList;
    }

    @Override
    public void onBindViewHolder(final AssistantViewHolder holder, final int position) {
        final NearBySearchData nearBySearchData = nearBySearchDataArrayList.get(position);

        holder.txt_shopname.setText(nearBySearchData.getPlace_name());
        holder.txt_shop_address.setText(nearBySearchData.getVicinity());
        holder.txt_shoptype.setText(nearBySearchData.getType_of_shop());
        holder.check_shop.setChecked(nearBySearchDataArrayList.get(position).isSelected());

        if (nearBySearchData.getRating().equals("")){

        }else {
            holder.txt_rating.setRating(Float.parseFloat(nearBySearchData.getRating()));
        }
        try{
            Picasso.with(context)
                    .load(nearBySearchData.getIcon())
                    .into(holder.img_shop);

        }catch (Exception e){
            e.printStackTrace();
        }



        holder.check_shop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                boolean isSelected = ((AppCompatCheckBox) compoundButton).isChecked();
                nearBySearchDataArrayList.get(position).setSelected(isSelected);

            }
        });



    }

    public ArrayList<NearBySearchData> getArrayList(){
        ArrayList<NearBySearchData> list = new ArrayList<>();
        for(int i=0;i<nearBySearchDataArrayList.size();i++){
            if(nearBySearchDataArrayList.get(i).isSelected())
                list.add(nearBySearchDataArrayList.get(i));
        }
        return list;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.nearby_item_lay, parent, false);
        return new AssistantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(nearBySearchDataArrayList==null)
        {
            nearBySearchDataArrayList = new ArrayList<>();
        }
        return nearBySearchDataArrayList.size();
    }
    public class AssistantViewHolder extends RecyclerView.ViewHolder {

        TextView txt_shopname,txt_shoptype,txt_shop_address;
        ImageView img_shop;
        AppCompatRatingBar txt_rating;
        AppCompatCheckBox check_shop;


        public AssistantViewHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {

                txt_shopname=itemView.findViewById(R.id.txt_shopname);
                txt_shoptype=itemView.findViewById(R.id.txt_shoptype);
                txt_shop_address=itemView.findViewById(R.id.txt_shop_address);
                img_shop=itemView.findViewById(R.id.img_shop);
                txt_rating=itemView.findViewById(R.id.txt_rating);
                check_shop=itemView.findViewById(R.id.check_shop);


            }
        }
    }

}