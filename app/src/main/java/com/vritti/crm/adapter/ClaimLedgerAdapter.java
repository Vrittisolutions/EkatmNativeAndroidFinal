package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.crm.bean.Ledger;
import com.vritti.crm.bean.Schedule;
import com.vritti.crm.vcrm7.FollowupActivity;
import com.vritti.ekatm.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * Created by 300151 on 10/13/16.
 */
public class ClaimLedgerAdapter extends RecyclerView.Adapter<ClaimLedgerAdapter.callHolder> {
    ArrayList<Ledger> ledgerArrayList;
    LayoutInflater mInflater;
    Context context;

    public ClaimLedgerAdapter(Context context1, ArrayList<Ledger> ledgerArrayList) {
        this.ledgerArrayList = ledgerArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }


    @NonNull
    @Override
    public callHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_clalim_ledger, parent, false);


        return new callHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaimLedgerAdapter.callHolder holder, @SuppressLint("RecyclerView") final int pos) {



        holder.txt_transaction.setText(ledgerArrayList.get(pos).getTransNo().replace(" ",""));
        holder.txt_credit.setText("CR :-"+ledgerArrayList.get(pos).getCredit().replace(" ",""));
        holder.txt_debit.setText("DR :-"+ledgerArrayList.get(pos).getDebit().replace(" ",""));
        holder.txt_tot.setText(ledgerArrayList.get(pos).getDebit().replace(" ",""));
        holder.txt_narration.setText(ledgerArrayList.get(pos).getTransNarrative());
        holder.txt_transaction.setText(ledgerArrayList.get(pos).getTransNo());
        String formattedDate = formateDateFromstring("yyyy-MM-dd", "dd/MM/yyyy",ledgerArrayList.get(pos).getEffectiveDate());
        holder.txt_date.setText(formattedDate);

    }


    @Override
    public int getItemCount() {
        return ledgerArrayList.size();
    }

    public class callHolder extends RecyclerView.ViewHolder {
        TextView txt_transaction,txt_debit,txt_date,txt_credit,txt_tot,txt_narration;
        CardView cardView;
        ImageView img_followuptype;

        public callHolder(View convertView) {
            super(convertView);
            cardView = convertView.findViewById(R.id.cardView);
            txt_transaction = convertView.findViewById(R.id.txt_transaction);
            txt_debit = convertView.findViewById(R.id.txt_debit);
            txt_date = convertView.findViewById(R.id.txt_date);
            txt_credit = convertView.findViewById(R.id.txt_credit);
            txt_tot = convertView.findViewById(R.id.txt_tot);
            txt_narration = convertView.findViewById(R.id.txt_narration);
        }
    }


    static class ViewHolder {
        TextView txt_call_datetime, txt_username, txt_full_message, txt_chat,
                txt_follow_type, txt_nextdatetime, txt_next_action, txt_outcome, txt_purpose, txt_with_whom;
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

}
