package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.AdvanceProvisionalData;
import com.vritti.ekatm.R;

import java.util.ArrayList;

/**
 * Created by sharvari on 06-Oct-17.
 */

public class AdvanceProvisionalListAdapter  extends BaseAdapter {
    ArrayList<AdvanceProvisionalData> provisionalDataArrayList;
    LayoutInflater mInflater;
    Context context;

    public AdvanceProvisionalListAdapter(Context context1, ArrayList<AdvanceProvisionalData> provisionalDataArrayList) {
        this.provisionalDataArrayList = provisionalDataArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return provisionalDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return provisionalDataArrayList.get(position);
    }

    @Override
                                                            public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View itemView, ViewGroup parent) {
        UserViewHolder userViewHolder;
        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.crm_provisional_item_lay_list, null);
            userViewHolder = new UserViewHolder();
            userViewHolder.txt_cust_name = (TextView) itemView.findViewById(R.id.txt_cust_name);
            userViewHolder.txt_bank_name = (TextView) itemView.findViewById(R.id.txt_bank_name);
            userViewHolder.txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
            userViewHolder.txt_tds_amount = (TextView) itemView.findViewById(R.id.txt_tds_amount);
            userViewHolder.txt_instrument_no = (TextView) itemView.findViewById(R.id.txt_instrument_no);
            userViewHolder.txt_deposite_date = (TextView) itemView.findViewById(R.id.txt_deposite_date);
            userViewHolder.txt_deposite_bank = (TextView) itemView.findViewById(R.id.txt_deposite_bank);
            itemView.setTag(userViewHolder);
        } else {
            userViewHolder = (UserViewHolder) itemView.getTag();
        }

        AdvanceProvisionalData advanceProvisionalData = provisionalDataArrayList.get(position);

        userViewHolder.txt_cust_name.setText(advanceProvisionalData.getCustomer_name());
        userViewHolder.txt_bank_name.setText(advanceProvisionalData.getBankName());
        String Amount=advanceProvisionalData.getAmount();
        String TdsAmount=advanceProvisionalData.getTDSAmount();
        String amount = Amount.split("\\.", 2)[0];
        String tdsamount = TdsAmount.split("\\.", 2)[0];
        userViewHolder.txt_tds_amount.setText(tdsamount);
        userViewHolder.txt_instrument_no.setText(advanceProvisionalData.getInstrumentNo());
        userViewHolder.txt_amount.setText(amount);
        String Date=advanceProvisionalData.getDepositedDt();
        String[] arr = Date.split("T");
        String DepositeDate = arr[0];
        userViewHolder.txt_deposite_date.setText(DepositeDate);
        userViewHolder.txt_deposite_bank.setText(advanceProvisionalData.getPaymentDepBank());

        return itemView;
    }

    static class UserViewHolder {
        public TextView txt_cust_name,txt_bank_name,txt_amount,txt_tds_amount,txt_instrument_no,txt_deposite_date,txt_deposite_bank;
    }
}