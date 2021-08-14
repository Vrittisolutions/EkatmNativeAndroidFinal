package com.vritti.AlfaLavaModule.activity.weight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.cartonlabel.CartonLabelHeaderListActivity;
import com.vritti.AlfaLavaModule.bean.CartonData;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class Adp_WeightCartonlListData extends RecyclerView.Adapter<Adp_WeightCartonlListData.ViewHolder> {
    private List<CartonData> mList;
    private static MyClickListener myClickListener;
    static Context context;


    public Adp_WeightCartonlListData(List<CartonData> list) {
        this.mList = list;
    }

    @Override
    public Adp_WeightCartonlListData.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alfa_item_putaway, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Adp_WeightCartonlListData.ViewHolder holder, int i) {
        holder.tv_Grn_no.setText("Carton no : " +  mList.get(i).getCartonCode());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<CartonData> dummyList) {
        mList = dummyList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_mo_no, tv_Grn_no, tv_total_count, tv_put_count, tv_date;
        private LinearLayout mLinear;

        public ViewHolder(final View view) {
            super(view);

            //  tv_Header = (TextView) view.findViewById(R.id.textView_grn_header);
            tv_Grn_no = (TextView) view.findViewById(R.id.tv_grn_no);
            tv_mo_no = (TextView) view.findViewById(R.id.tv_mo_no);
            mLinear = (LinearLayout) view.findViewById(R.id.card_item);
            context = itemView.getContext();
            Context aa = context;
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {


            ((WeightCartonHeaderListActivity) context).setdonumber
                    (mList.get(getPosition()).getCartonCode(),mList.get(getAdapterPosition()).getCartonHeaderId());




            /*String BoxTypeMasterId = mList.get(getPosition()).getBoxTypeMasterId();

                Intent intent = new Intent(itemView.getContext(), CreateSecondaryPackActivity.class);
                intent.putExtra("boxTypeMasterId", BoxTypeMasterId);
                context.startActivity(intent);*/
              }
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }
}