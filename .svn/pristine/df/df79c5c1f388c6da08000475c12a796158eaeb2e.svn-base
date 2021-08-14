package com.vritti.crmlib.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.vritti.crmlib.R;
import com.vritti.crmlib.vcrm7.ProspectEnterpriseActivity;

public class ProductMultipleSelectionAdapter extends BaseAdapter {

    private ArrayList<String> mListItems;
    private LayoutInflater mInflater;
    private TextView mSelectedItems;
    private LinearLayout productList;
    private static int selectedCount = 0;
    private static String firstSelected = "";
    private ViewHolder holder;
    private Context c;
    private static String selected = "";    //shortened selected values representation

    public static String getSelected() {
        return selected;
    }

    /* public void setSelected(String selected) {
        MultipleSelectionAdapter.selected = selected;
    }
*/
    public ProductMultipleSelectionAdapter(Context context, ArrayList<String> items,
                                           TextView tv) {
        mListItems = new ArrayList<String>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;

        c = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_drop_down_list_row, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.SelectOption);
            holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mListItems.get(position));

        final int position1 = position;

        //whenever the checkbox is clicked the selected values textview is updated with new selected values
        holder.chkbox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setText(position1);


            }
        });

        if (ProspectEnterpriseActivity.checkSelected[position])
            holder.chkbox.setChecked(true);
        else
            holder.chkbox.setChecked(false);
        return convertView;
    }



    private void setText(int position1) {
        if (!ProspectEnterpriseActivity.checkSelected[position1]) {
            ProspectEnterpriseActivity.checkSelected[position1] = true;
            selectedCount++;
        } else {
            ProspectEnterpriseActivity.checkSelected[position1] = false;
            selectedCount--;
        }
     //   productList.removeAllViews();
        if (selectedCount > 0) {
            for (int i = 0; i < ProspectEnterpriseActivity.checkSelected.length; i++) {
                if (ProspectEnterpriseActivity.checkSelected[i] == true) {
                    firstSelected = firstSelected + mListItems.get(i) + " ,";

                    TextView tv1 = new TextView(c);
                    tv1.setText(mListItems.get(i));


                    if (ProspectEnterpriseActivity.lstProduct.contains(mListItems.get(i))) {
                        Log.d("test", "present");
                    } else {
                        ProspectEnterpriseActivity.lstProduct.add(mListItems.get(i));
                    }

                    tv1.setTextSize(13);

                  //  productList.addView(tv1);
                }
            }
            mSelectedItems.setText("--Select Product--");

        }
    }

    private class ViewHolder {
        TextView tv;
        CheckBox chkbox;
    }
}
