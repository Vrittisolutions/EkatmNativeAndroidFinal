package com.vritti.vwb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.AssetTransfer;
import com.vritti.vwb.vworkbench.AssetTransferActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by sharvari on 28-Jun-18.
 */

public class AssetTransferAdapter extends BaseAdapter {

    ArrayList<AssetTransfer> assetTransferArrayList;
    LayoutInflater mInflater;
    Context context;

    public AssetTransferAdapter(Context context1, ArrayList<AssetTransfer> assetTransferArrayList) {
        this.assetTransferArrayList = assetTransferArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;

    }

    @Override
    public int getCount() {
        return assetTransferArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return assetTransferArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_asset_lay, null);
            holder = new ViewHolder();

            holder.txt_asset_no=(TextView) convertView.findViewById(R.id.txt_asset_no);
            holder.txt_asset_type=(TextView)convertView.findViewById(R.id.txt_asset_type);
            holder.txt_invoice=(TextView)convertView.findViewById(R.id.txt_invoice_no);
            holder.txt_model_no=(TextView)convertView.findViewById(R.id.txt_model_no);
            holder.txt_dealer_name=(TextView)convertView.findViewById(R.id.txt_dealer_name);
            holder.txt_purchase_date=(TextView)convertView.findViewById(R.id.txt_purchase_date);
            holder.txt_warranty_date=(TextView)convertView.findViewById(R.id.txt_warranty_date);
            holder.txt_serial_no=(TextView)convertView.findViewById(R.id.txt_serial_no);
            holder.txt_transfer=(TextView)convertView.findViewById(R.id.txt_transfer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txt_asset_no.setText(assetTransferArrayList.get(position).getAssetNo());
        holder.txt_asset_type.setText(assetTransferArrayList.get(position).getAssetTypeDesc());
        holder.txt_invoice.setText(assetTransferArrayList.get(position).getInvoiceNo());
        holder.txt_model_no.setText(assetTransferArrayList.get(position).getModelNo());
        holder.txt_dealer_name.setText(assetTransferArrayList.get(position).getDealerName());
        holder.txt_purchase_date.setText(assetTransferArrayList.get(position).getDateOfPurchase());
        holder.txt_warranty_date.setText(assetTransferArrayList.get(position).getWarrantyDate());
        holder.txt_serial_no.setText(assetTransferArrayList.get(position).getSerialNo());



        holder.txt_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AssetId=assetTransferArrayList.get(position).getPKAssetId();
                context.startActivity(new Intent(context, AssetTransferActivity.class).putExtra("AssetId",AssetId).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });










        return convertView;
    }


    static class ViewHolder {
        TextView txt_asset_no,txt_asset_type,txt_invoice,txt_model_no,
                txt_dealer_name,txt_purchase_date,txt_warranty_date,txt_serial_no,txt_transfer;

    }
}
