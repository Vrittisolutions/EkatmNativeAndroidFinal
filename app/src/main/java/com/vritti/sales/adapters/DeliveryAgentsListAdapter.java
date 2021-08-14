package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.DeliveryAgentBean;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.util.ArrayList;

public class DeliveryAgentsListAdapter extends BaseAdapter {

    private Context parent;
    static ArrayList<DeliveryAgentBean> list_agents;
    //ArrayList<String> list_agents;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    boolean array[];

    public DeliveryAgentsListAdapter(Context context, ArrayList<DeliveryAgentBean> listDetails) {
        this.parent = context;
        this.list_agents = listDetails;
        mInflater = LayoutInflater.from(parent);
        array =new boolean[listDetails.size()];
    }

    /*public DeliveryAgentsListAdapter(Context context, ArrayList<String> listDetails) {
        this.parent = context;
        this.list_agents = listDetails;
        mInflater = LayoutInflater.from(parent);
    }*/

    @Override
    public int getCount() {
        return list_agents.size();
    }

    @Override
    public Object getItem(int position) {
        return  list_agents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final int pos = position;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.tbuds_delivery_agents, null);
            holder = new ViewHolder();

            holder.chkagent =convertView.findViewById(R.id.chkagent);
            holder.txtagentname = (TextView)convertView.findViewById(R.id.txtagentname);
            holder.txtagentid = (TextView)convertView.findViewById(R.id.txtagentid);
            holder.txtmobile = (TextView)convertView.findViewById(R.id.txtmobile);
            holder.txt_estimate = (TextView)convertView.findViewById(R.id.txt_estimate);
            holder.txtlocation = (TextView)convertView.findViewById(R.id.txtlocation);
            holder.txt_pendingshipments = (TextView)convertView.findViewById(R.id.txt_pendingshipments);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtagentname.setText(list_agents.get(position).getUserName());
        holder.txtagentid.setText(list_agents.get(position).getUserLoginId());
        holder.txtmobile.setText(list_agents.get(position).getMobile());
        //holder.txt_estimate.setText(list_agents.get(position).get());
        holder.txt_estimate.setVisibility(View.INVISIBLE);

        if(list_agents.get(position).getLocationName().equalsIgnoreCase("") ||
                list_agents.get(position).getLocationName().equalsIgnoreCase(null)){
            holder.txtlocation.setText(" No Location Found");
        }else {
            holder.txtlocation.setText(list_agents.get(position).getLocationName());
        }

        holder.txt_pendingshipments.setText(list_agents.get(position).getPendingShipments());

        holder.chkagent.setChecked(array[position]);
        holder.chkagent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                for (int i=0;i<array.length;i++){
                    if(i==position){
                        array[i]=true;
                        list_agents.get(i).setChecked(true);
                    }else{
                        array[i]=false;
                        list_agents.get(i).setChecked(false);
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public static ArrayList<DeliveryAgentBean> getAllSelectedAgentData() {
        ArrayList<DeliveryAgentBean> list_selected = new ArrayList<>();

        for (int i = 0; i < list_agents.size(); i++) {
            if (list_agents.get(i).isChecked()){
                list_selected.add(list_agents.get(i));
            }else {

            }
        }
        return list_selected;
    }

    public class ViewHolder{
        TextView txtagentname, txtagentid, txtmobile, txt_estimate, txtlocation, txt_pendingshipments;
        CheckBox chkagent;

    }

}
