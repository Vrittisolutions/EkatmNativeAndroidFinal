package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.SOApproveActivity;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.OrderHistoryBean;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SOApprListAdapter extends BaseAdapter {
    private static ArrayList<OrderHistoryBean> list;
    private Context parent;
    private Context context;
    private LayoutInflater mInflater;
    private static ArrayList<OrderHistoryBean> arraylist;
    String key = "";
    private String DateToStr;

    public SOApprListAdapter(Context parent, ArrayList<OrderHistoryBean> custSelectionList) {
        this.parent = parent;
        this.context = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<OrderHistoryBean>();
        arraylist.addAll(custSelectionList);

        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sales_soappr_adapter, null);
            holder = new ViewHolder();

            holder.txtsono =  convertView.findViewById(R.id.txtsono);
            holder.txtamt =  convertView.findViewById(R.id.txtamt);
            holder.txtsodate =  convertView.findViewById(R.id.txtsodate);
            holder.txtdeldate =  convertView.findViewById(R.id.txtdeldate);
            holder.txtcust =  convertView.findViewById(R.id.txtcust);
            holder.delmode =  convertView.findViewById(R.id.delmode);
            holder.txtmobile =  convertView.findViewById(R.id.txtmobile);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String DoAck = arraylist.get(position).getDoAck();
        String DoDelDate = arraylist.get(position).getSODate();

        double amount = Double.parseDouble(String.valueOf(arraylist.get(position).getNetAmt()));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        holder.txtamt.setText(formatted +" ₹");

        if(!DoAck.equalsIgnoreCase("")){
            holder.txtsodate.setText(Convertdate(DoAck));
        }else {
            holder.txtsodate.setText("");
        }

        if(!DoDelDate.equalsIgnoreCase("")){
            holder.txtdeldate.setText(Convertdate(DoDelDate));
        }else {
            holder.txtdeldate.setText("");
        }

        holder.txtsono.setText(arraylist.get(position).getSONo());
        holder.txtcust.setText(arraylist.get(position).getConsigneeName());
        holder.delmode.setText(arraylist.get(position).getDeliveryTerms());
        holder.txtmobile.setText(arraylist.get(position).getMobile());

        holder.txtmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //redirect to call

                if(context instanceof SOApproveActivity){
                    ((SOApproveActivity)context).MakeCall(arraylist.get(position).getMobile());
                }
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView txtsono,txtamt,txtsodate,txtdeldate,txtcust,txtmobile,delmode;
    }

    public String Convertdate(String date){

        //SimpleDateFormat Format = new SimpleDateFormat("dd-MM-yyyy");//Feb 23 2016 12:16PM
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//Feb 23 2016 12:16PM
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date d1 = null;
        try {
            d1 = format.parse(date);
            //DateToStr = toFormat.format(date);
            DateToStr = Format.format(d1);
            // DateToStr = format.format(d1);
            System.out.println(DateToStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return DateToStr;
    }

}
