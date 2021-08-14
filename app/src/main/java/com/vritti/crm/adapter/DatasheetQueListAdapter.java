package com.vritti.crm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.crm.bean.Datasheet;
import com.vritti.ekatm.R;

import java.util.ArrayList;


/**
 * Created by 300151 on 12/6/2016.
 */


public class DatasheetQueListAdapter extends BaseAdapter {
    private static ArrayList<Datasheet> lsQueList;
    private LayoutInflater mInflater;
    Context context;

    public DatasheetQueListAdapter(Context context1, ArrayList<Datasheet> lsTeamList1) {
        lsQueList = lsTeamList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_custom_datasheet_que_item, null);
            holder = new ViewHolder();
            holder.tv_queText = (TextView) convertView.findViewById(R.id.tv_queText);
            holder.tv_Ans = (TextView) convertView.findViewById(R.id.tv_Ans);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_queText.setText(lsQueList.get(position).getQuesText().toString());
        if (lsQueList.get(position).getAnswer()==null) {
            holder.tv_Ans.setText(" ");
        } else {
            holder.tv_Ans.setText(lsQueList.get(position).getAnswer().toString());
        }


        return convertView;
    }

    static class ViewHolder {
        TextView tv_queText, tv_Ans;

    }
}

