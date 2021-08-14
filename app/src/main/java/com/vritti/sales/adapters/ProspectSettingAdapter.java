package com.vritti.sales.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.ProspectSetting;

import java.util.ArrayList;

public class ProspectSettingAdapter extends BaseAdapter {

    ArrayList<ProspectSetting> prospectlist;
    ArrayList<ProspectSetting> arraylist = new ArrayList<>();
    LayoutInflater mInflater;
    Context context;
    //int resource;

    public ProspectSettingAdapter(Context context, ArrayList<ProspectSetting> prospect) {
        this.context = context;
        this.prospectlist = prospect;
        mInflater = LayoutInflater.from(context);
      //  this.resource = resource;
    }

    @Override
    public int getCount() {
        return prospectlist.size();
    }

    @Override
    public Object getItem(int position) {
        return prospectlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_propect_setting, null);
            holder = new ViewHolder();
            holder.txt_details = (TextView)convertView.findViewById(R.id.txt_details);
            holder.ischeckmandatory= (AppCompatCheckBox)convertView.findViewById(R.id.ischeckmandatory);
            holder.ischeckvisible = (AppCompatCheckBox)convertView.findViewById(R.id.ischeckvisible);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_details.setText(prospectlist.get(position).getCaption());
        holder.ischeckmandatory.setChecked(prospectlist.get(position).isIsmandatorycheck());
        holder.ischeckvisible.setChecked(prospectlist.get(position).isIsvisiblecheck());

        holder.ischeckvisible.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                prospectlist.get(position).setIsvisiblecheck(isSelected);
            //    tempChatUserArrayList.add(chatUserArrayList.get(position));
            }
        });

        holder.ischeckmandatory.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                prospectlist.get(position).setIsmandatorycheck(isSelected);
                //    tempChatUserArrayList.add(chatUserArrayList.get(position));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView txt_details;
        AppCompatCheckBox ischeckmandatory,ischeckvisible;
    }

    public ArrayList<ProspectSetting> getAllVisibleChekedData() {
        ArrayList<ProspectSetting> list_visiblechk = new ArrayList<>();

        for (int i = 0; i < prospectlist.size(); i++) {
            if (prospectlist.get(i).isIsvisiblecheck())
                list_visiblechk.add(prospectlist.get(i));
        }
        return list_visiblechk;
    }

    public ArrayList<ProspectSetting> getAllmandatoryChekedData() {
        ArrayList<ProspectSetting> list_mndtrychk = new ArrayList<>();
        for (int i = 0; i < prospectlist.size(); i++) {
            if (prospectlist.get(i).isIsmandatorycheck())
                list_mndtrychk.add(prospectlist.get(i));
            
        }
        return list_mndtrychk;
    }
}
