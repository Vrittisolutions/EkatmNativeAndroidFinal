package com.vritti.AlfaLavaModule.PI;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.inventory.physicalInventory.bean.BatchList;

import java.util.ArrayList;


/**
 * Created by 300151 on 10/13/16.
 */
public class BatchListAdapter extends BaseAdapter {
    ArrayList<BatchList> batchListArrayList ;
    LayoutInflater mInflater;
    Context context;
    AlertDialog alertDialog;

    public BatchListAdapter(Context context1, ArrayList<BatchList> batchListArrayList) {
        this.batchListArrayList = batchListArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return batchListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return batchListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder holder=new ViewHolder();
    if (convertView == null) {
            convertView = mInflater.inflate(R.layout.batchlist_adapter_xml, null);

        holder.txtbatchno = (TextView) convertView.findViewById(R.id.txtbatchno);
        holder.txtwhname = (TextView) convertView.findViewById(R.id.txtwhname);
        holder.txttagseries = (TextView) convertView.findViewById(R.id.txttagseries);
        holder.txtcounter = (TextView) convertView.findViewById(R.id.txtcounter);
        holder.txtauditor = (TextView) convertView.findViewById(R.id.txtauditor);
        holder.txttagseries.setVisibility(View.GONE);
        holder.txtauditor.setVisibility(View.GONE);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txtbatchno.setText(batchListArrayList.get(position).getCode());
        holder.txtwhname.setText(batchListArrayList.get(position).getWarehouseCode()); //dispwhname
        holder.txtcounter.setText(batchListArrayList.get(position).getCountingResponsible());

        return convertView;
    }

    static class ViewHolder {
        TextView txtbatchno,txtwhname, txttagseries,txtcounter, txtauditor;
    }
}