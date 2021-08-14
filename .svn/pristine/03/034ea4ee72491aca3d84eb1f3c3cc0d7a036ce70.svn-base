package com.vritti.sales.CounterBilling;

/**
 * Created by 300151 on 7/20/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;

import java.util.ArrayList;

public class AutocompleteCustomArrayAdapter_Purchase extends ArrayAdapter<AllCatSubcatItems> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    AllCatSubcatItems data[] = null;
    ArrayList<String> ItemNameList ;

    public AutocompleteCustomArrayAdapter_Purchase(Context mContext, int layoutResourceId, AllCatSubcatItems[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{

            if(convertView==null){
                // inflate the layout
                LayoutInflater inflater = ((PurchaceActivity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            AllCatSubcatItems objectItem = data[position];
            //ItemNameList =  new ArrayList<String>();

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            String ItemName = objectItem.getItemName().toString();
            textViewItem.setText(ItemName);


            // in case you want to add some style, you can do something like:
            //textViewItem.setBackgroundColor(Color.CYAN);


        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}