package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.MRSDetailBean;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapteMRSDetail extends RecyclerView.Adapter<AdapteMRSDetail.ViewHolder> {

    private ArrayList<MRSDetailBean> list;
    private EditText et_country;
    private View view;
    Context context;
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;

    public AdapteMRSDetail(ArrayList<MRSDetailBean> countries,Context context) {
        this.list = countries;
        this.context=context;
    }

    @Override
    public AdapteMRSDetail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alfa_item_mrst_detail, viewGroup, false);

        return new AdapteMRSDetail.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_itemcode.setText(list.get(position).getItemCode());
        holder.tv_item_desc.setText(list.get(position).getItemDesc());
        holder.edt_heat.setText(list.get(position).getSuggLotNo());//tv_item_code
        holder.edt_req_quntity.setText(list.get(position).getReqQty());//tv_item_code
        holder.edt_uom.setText(list.get(position).getUOMCode());
        holder.tv_mono.setText(list.get(position).getMONo());

        String flag=list.get(position).getFlag();
        if (flag.equalsIgnoreCase(WebUrlClass.DoneFlag_Default)){
            holder.card_item.setBackgroundColor(context.getResources().getColor(R.color.barMeduimColor));
        }else {
            holder.card_item.setBackgroundColor(context.getResources().getColor(R.color.btnNext));

        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    public void update(ArrayList<MRSDetailBean> list) {
        this.list = list;
    }


    public void update(List<MRSDetailBean> dummyList) {
        list = (ArrayList<MRSDetailBean>) dummyList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemcode;
        TextView tv_item_desc;
        TextView edt_heat,edt_req_quntity;
        TextView edt_uom,tv_mono;
        LinearLayout card_item;
         public ViewHolder(View view) {
             super(view);
             tv_itemcode = (TextView) view.findViewById(R.id.tv_itemcode);
             tv_item_desc = (TextView) view.findViewById(R.id.tv_item_desc);//tv_grn_no
             edt_heat= (TextView) view.findViewById(R.id.edt_heat);
             edt_req_quntity = (TextView) view.findViewById(R.id.edt_req_quntity);
             edt_uom = (TextView) view.findViewById(R.id.edt_uom);
             tv_mono = (TextView) view.findViewById(R.id.tv_mono);
             card_item = (LinearLayout) view.findViewById(R.id.card_item);
             view.setTag(view);
         }
    }

}