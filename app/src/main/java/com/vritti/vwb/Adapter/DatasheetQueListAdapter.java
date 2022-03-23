package com.vritti.vwb.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.Beans.Datasheet;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.AddDatasheetActivityMain;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;

import java.util.ArrayList;


/**
 * Created by 300151 on 12/6/2016.
 */


public class DatasheetQueListAdapter extends BaseAdapter {
    private static ArrayList<Datasheet> lsQueList;
    private LayoutInflater mInflater;
    Context context;
    String EnvMasterId;

    public DatasheetQueListAdapter(Context context1, ArrayList<Datasheet> lsTeamList1,String envMasterId) {
        lsQueList = lsTeamList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.EnvMasterId = envMasterId;
    }



    @Override
    public int getCount() {
        return lsQueList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsQueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_datasheet_que_item, null);
            holder = new ViewHolder();
            holder.tv_queText = (TextView) convertView.findViewById(R.id.tv_queText);
            holder.tv_Ans = (TextView) convertView.findViewById(R.id.tv_Ans);
            holder.img_attachment = convertView.findViewById(R.id.img_attachment);
            holder.attachment_count = convertView.findViewById(R.id.attachment_count);
            holder.rel_attachment = convertView.findViewById(R.id.rel_attachment);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.img_attachment.setVisibility(View.VISIBLE);
        holder.tv_queText.setText(lsQueList.get(position).getQuesText().toString());
        if (lsQueList.get(position).getAnswer() == null) {
            holder.tv_Ans.setText(" ");
        } else {
            holder.tv_Ans.setText(lsQueList.get(position).getAnswer().toString());
        }

        if(lsQueList.get(position).getFilePathName() == null){
            holder.attachment_count.setText("0");
        }else{
            if(lsQueList.get(position).getFilePathName().size() > 0){
                holder.attachment_count.setText(String.valueOf(lsQueList.get(position).getFilePathName().size()));
            }else{
                holder.attachment_count.setText("0");
            }
        }
        holder.rel_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String pkCssDetailId = lsQueList.get(position)..getPkcssdtlsid();
                ((AddDatasheetActivityMain)context).attachmentDetailsShow(position);

            }
        });



//        if(EnvMasterId.equalsIgnoreCase("b207"))
//            holder.img_attachment.setVisibility(View.VISIBLE);
//        else
//            holder.img_attachment.setVisibility(View.GONE);



        return convertView;
    }

    public void update(ArrayList<Datasheet> datasheetlists) {
        lsQueList = datasheetlists;
        notifyDataSetChanged();
    }
    public void deleteChange(ArrayList<Datasheet> datasheetlists) {

        lsQueList = datasheetlists;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_queText, tv_Ans;
        ImageView img_attachment;
        TextView attachment_count;
        RelativeLayout rel_attachment;

    }
}
