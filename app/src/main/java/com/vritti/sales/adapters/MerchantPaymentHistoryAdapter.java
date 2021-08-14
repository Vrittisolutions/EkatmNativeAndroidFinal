package com.vritti.sales.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.MerchantPaymentHistory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Jerry on 12/19/2017.
 */

public class MerchantPaymentHistoryAdapter extends RecyclerView.Adapter<MerchantPaymentHistoryAdapter.AssistantViewHolder> {

    private ArrayList<MerchantPaymentHistory> itemReportArrayList = null;
    Context context;
    double currentTotal=0;
    double currentExpense;
    private String DateToStr="";

    public MerchantPaymentHistoryAdapter(Context context, ArrayList<MerchantPaymentHistory> itemReportArrayList) {
        this.context=context;
        this.itemReportArrayList = itemReportArrayList;
    }

    @Override
    public void onBindViewHolder(final AssistantViewHolder holder, int position) {
        final MerchantPaymentHistory itemReport = itemReportArrayList.get(position);
        holder.txtinvoice.setText(itemReport.getInvoiceNo());
        holder.txtcustomer.setText(itemReport.getCustomerName());

        String payMode = "";
        if(itemReport.getPaymentMode().equals("")){
            payMode = "Paid";
        }else if(itemReport.getPaymentMode().equals("COD")){
            payMode = "Cash";
        }else {
            payMode = "Online";
        }

        holder.txtpaymode.setText(payMode);

        float bal = Float.parseFloat(itemReport.getBalanceAmount()) - Float.parseFloat(itemReport.getDiscountAmount());

        holder.txtorg.setText("Amt : "+context.getResources().getString(R.string.rs)+" " + itemReport.getAmount());
        holder.txtamount.setText("Paid : "+context.getResources().getString(R.string.rs)+" " + itemReport.getApprovedAmount());
        holder.txtbal.setText("Bal. : "+context.getResources().getString(R.string.rs)+" " + bal);
        holder.txtdisc.setText("Disc. : "+context.getResources().getString(R.string.rs)+" " + itemReport.getDiscountAmount());
        String date=itemReport.getAddeddate();
        String[] arr = date.split("T");

        String Adddate=arr[0];

        String AddDate=formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", Adddate);

        holder.txtdate.setText(AddDate);

        if(position % 2 == 1){
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary1));
        }else {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.btncolordark));
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public AssistantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.merch_payment_history_report_lay, parent, false);
        return new AssistantViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if(itemReportArrayList==null)
        {
            itemReportArrayList = new ArrayList<MerchantPaymentHistory>();
        }
        return itemReportArrayList.size();
    }
    public class AssistantViewHolder extends RecyclerView.ViewHolder {

        TextView txtinvoice,txtdate,txtpaymode,txtcustomer,txtamount,txtbal,txtdisc,txtorg;
        View view;

        public AssistantViewHolder(View itemView) {
            super(itemView);

            try{
                if(itemView!=null) {

                    txtinvoice=itemView.findViewById(R.id.txtinvoice);
                    txtdate=itemView.findViewById(R.id.txtdate);
                    txtpaymode=itemView.findViewById(R.id.txtpaymode);
                    txtcustomer=itemView.findViewById(R.id.txtcustomer);
                    txtamount=itemView.findViewById(R.id.txtamount);
                    txtorg=itemView.findViewById(R.id.txtorg);
                    txtbal=itemView.findViewById(R.id.txtbal);
                    txtdisc=itemView.findViewById(R.id.txtdisc);
                    view=itemView.findViewById(R.id.view);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }
}