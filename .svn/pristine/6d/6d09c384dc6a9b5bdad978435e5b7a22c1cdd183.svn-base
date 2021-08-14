 package com.vritti.crm.adapter;

 import android.content.Context;
 import android.support.v7.widget.RecyclerView;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.BaseAdapter;
 import android.widget.TextView;

 import com.vritti.crm.bean.DatasheetList;
 import com.vritti.crm.vcrm7.DatasheetDisplayList;
 import com.vritti.ekatm.R;

 import java.util.ArrayList;

 public class DataSheetListAdapter extends BaseAdapter{

     LayoutInflater mInflater;
     ArrayList<DatasheetList> datasheetDisplayLists;
     Context context;


     public DataSheetListAdapter(Context context1, ArrayList<DatasheetList> datasheetDisplayLists) {

         this.datasheetDisplayLists = datasheetDisplayLists;
         mInflater = LayoutInflater.from(context1);
         context = context1;
     }

     @Override
     public int getCount() {
         return datasheetDisplayLists.size();
     }

     @Override
     public Object getItem(int position) {
         return datasheetDisplayLists.get(position);
     }

     @Override
     public long getItemId(int position) {
         return 0;
     }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {

         final ViewHolder holder;
         if (convertView == null) {
             convertView = mInflater.inflate(R.layout.datasheet_list, null);
             holder = new ViewHolder();
             holder.txt_datasheet_name= (TextView) convertView.findViewById(R.id.txt_datasheet_name);

             convertView.setTag(holder);
         } else {
             holder = (ViewHolder) convertView.getTag();
         }

         holder.txt_datasheet_name.setText(datasheetDisplayLists.get(position).getCSSFormsDesc());
         
         return convertView;
     }

     static class ViewHolder {
         TextView txt_datasheet_name;

     }
 }
