package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.Customer;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ShipToListAdapter extends BaseAdapter {
    private static ArrayList<Customer> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<Customer> arraylist;

    public ShipToListAdapter(Context parent, ArrayList<Customer> custSelectionList) {
        this.parent = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<Customer>();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_shipto_selection,
                    null);
            holder = new ViewHolder();

            holder.txtAddress = (TextView) convertView.findViewById(R.id.txtCustomer);
            holder.txtarea = (TextView)convertView.findViewById(R.id.txtarea);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtAddress.setText(list.get(position).getShipToAddress());
        holder.txtarea.setText(list.get(position).getCity_state_pin_Country());

        return convertView;
    }

    class ViewHolder{
        TextView txtAddress, txtarea;
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (Customer wp : arraylist) {
                if (wp.getCustomer_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
