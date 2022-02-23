package com.vritti.AlfaLavaModule.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.activity.packaging.ReceiptPackagingDOListActivity;
import com.vritti.AlfaLavaModule.bean.PicklistNO;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin-1 on 9/22/2016.
 */

public class Adp_PickOrderNo extends RecyclerView.Adapter<Adp_PickOrderNo.ViewHolder> {
    private List<PicklistNO> mList;
    private static MyClickListener myClickListener;
    static Context context;


    public Adp_PickOrderNo(List<PicklistNO> list) {
        this.mList = list;
    }

    @Override
    public Adp_PickOrderNo.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alfa_item_putaway, parent, false);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_putaway_new, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(Adp_PickOrderNo.ViewHolder holder, int i) {
        holder.tv_Grn_no.setText(mList.get(i).getPicklistNo());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void update(ArrayList<PicklistNO> dummyList) {
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


            ((ReceiptPackagingDOListActivity) context).setdonumber
                    (mList.get(getPosition()).getPicklistNo(),mList.get(getAdapterPosition()).getPick_listHdrId());




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