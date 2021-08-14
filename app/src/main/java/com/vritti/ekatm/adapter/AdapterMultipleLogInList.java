package com.vritti.ekatm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.ekatm.bean.BeanLogInsetting;

import java.util.ArrayList;

public class AdapterMultipleLogInList extends BaseAdapter {
    private Context context; //context
    private static  ArrayList<BeanLogInsetting> items; //data source of the list adapter

    //public constructor
    public AdapterMultipleLogInList(Context context, ArrayList<BeanLogInsetting> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); //returns total of items in the list
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); //returns list item at the specified position
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row

            convertView = LayoutInflater.from(context).
                    inflate(R.layout.list_item_lay, parent, false);


        // get current item to be displayed
        BeanLogInsetting currentItem = (BeanLogInsetting) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView)
                convertView.findViewById(R.id.text_view_item_name);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getCompanyURL());

        // returns the view for the current row
        return convertView;
    }
}