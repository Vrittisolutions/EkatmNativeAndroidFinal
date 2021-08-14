package com.vritti.sales.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.vritti.ekatm.R;
import com.vritti.sales.Interface.AddItemReport;
import com.vritti.sales.beans.AdditemBean;

import java.util.ArrayList;

@SuppressLint({"InflateParams", "NewApi"})
public class ItemlistAdapter extends BaseAdapter {
    private ArrayList<AdditemBean> list;

    private Context parent;
    AdditemBean additemBean;
    private LayoutInflater mInflater;
    AddItemReport addItemReport;
    private String productId;
    int minteger = 0;
    ArrayList<Boolean> positionArray;


    public ItemlistAdapter(Context context,
                           ArrayList<AdditemBean> list, AddItemReport addItemReport) {

        parent = context;
        this.addItemReport = addItemReport;
        this.list = new ArrayList<AdditemBean>();
        this.list.addAll(list);
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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            //  LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.tbuds_custom_add_items, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.item);
            holder.name = (CheckBox) convertView.findViewById(R.id.chkitem);

            convertView.setTag(holder);

            holder.name.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    AdditemBean _state = (AdditemBean) cb.getTag();
                    _state.setSelected(cb.isChecked());
                   // list.add(_state);
                    addItemReport.addItemsReports(_state.getItemmasterid(),
                            _state.getItemname(),_state.getItemMrp(), _state.isSelected());
                    notifyDataSetChanged();

                }
            });

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AdditemBean state = list.get(position);

        holder.code.setText( state.getItemname());
        //    holder.name.setText(""+state.isSelected());
        holder.name.setChecked(state.isSelected());

        holder.name.setTag(state);

        return convertView;

    }

    private static class ViewHolder {
        TextView code;
        ImageView productImage;
        CheckBox name;
        Button buttonAdd;

    }
}