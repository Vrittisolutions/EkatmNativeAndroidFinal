package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.ProductList_TabActivity;
import com.vritti.sales.activity.SO_Periodic_ScheduleActivity;
import com.vritti.sales.beans.ConfigDropDownData;
import com.vritti.sales.beans.ScheduleListBean;

import java.util.ArrayList;
import java.util.Locale;

public class ScheduleListAdapter extends BaseAdapter {
    private static ArrayList<ScheduleListBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<ScheduleListBean> arraylist;

    public ScheduleListAdapter(Context parent, ArrayList<ScheduleListBean> schedulelist) {
        this.parent = parent;
        this.list = schedulelist;
        arraylist = new ArrayList<ScheduleListBean>();
        arraylist.addAll(schedulelist);

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.schedule_list_adapter,
                    null);
            holder = new ViewHolder();

            holder.txtstartdate = (TextView) convertView.findViewById(R.id.txtstartdate);
            holder.txtdeldate = (TextView) convertView.findViewById(R.id.txtdeldate);
            holder.txtdoordate = (TextView) convertView.findViewById(R.id.txtdoordate);
            holder.txtqty = (TextView) convertView.findViewById(R.id.txtqty);
            holder.imgdelete = (ImageView) convertView.findViewById(R.id.imgdelete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtstartdate.setText(list.get(position).getStartDate());
        holder.txtdeldate.setText(list.get(position).getEndDate());
        holder.txtdoordate.setText(list.get(position).getCustDoorDate());
        holder.txtqty.setText(list.get(position).getQty());

        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qty = list.get(position).getQty();
               // SO_Periodic_ScheduleActivity.jsonArray.remove(a);
              //  SO_Periodic_ScheduleActivity.listschedules.remove(position);
            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView txtstartdate,txtdeldate,txtdoordate,txtqty;
        ImageView imgdelete;
    }

}
