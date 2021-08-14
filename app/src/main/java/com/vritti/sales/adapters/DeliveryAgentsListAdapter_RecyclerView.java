package com.vritti.sales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.ShipmentEntryActivity;
import com.vritti.sales.beans.DeliveryAgentBean;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Float.valueOf;

public class DeliveryAgentsListAdapter_RecyclerView extends
        RecyclerView.Adapter<DeliveryAgentsListAdapter_RecyclerView.AgentHolder> {

    private Context parent;
    ArrayList<DeliveryAgentBean> list_agents;
    //ArrayList<String> list_agents;
    private LayoutInflater mInflater;
    boolean array[];

    public DeliveryAgentsListAdapter_RecyclerView(Context context, ArrayList<DeliveryAgentBean> listDetails) {
        this.parent = context;
        this.list_agents = listDetails;
        mInflater = LayoutInflater.from(parent);
    }

    @NonNull
    @Override
    public AgentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tbuds_delivery_agents, parent, false);

        return new AgentHolder(itemView);
       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final AgentHolder holder, final int position) {
        DeliveryAgentBean agentBean = list_agents.get(position);

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

    }

    @Override
    public int getItemCount() {
        return list_agents.size();
    }

  /*  public static ArrayList<DeliveryAgentBean> getAllSelectedSOData() {
        ArrayList<DeliveryAgentBean> list_selected = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list_agents.get(i).isChecked()){
                list_selected.add(list_agents.get(i));
            }else {

            }
        }
        return list_selected;
    }*/

    public class AgentHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chkagent)
        CheckBox chkagent;

        @BindView(R.id.txtagentname)
        TextView txtagentname;

        @BindView(R.id.txtagentid)
        TextView txtagentid;

        @BindView(R.id.txtmobile)
        TextView txtmobile;

        @BindView(R.id.txt_estimate)
        TextView txt_estimate;

        @BindView(R.id.txtlocation)
        TextView txtlocation;

        @BindView(R.id.txt_pendingshipments)
        TextView txt_pendingshipments;

        public AgentHolder(View itemView) {
            super(itemView);
            chkagent =itemView.findViewById(R.id.chkagent);
            txtagentname = (TextView)itemView.findViewById(R.id.txtagentname);
            txtagentid = (TextView)itemView.findViewById(R.id.txtagentid);
            txtmobile = (TextView)itemView.findViewById(R.id.txtmobile);
            txt_estimate = (TextView)itemView.findViewById(R.id.txt_estimate);
            txtlocation = (TextView)itemView.findViewById(R.id.txtlocation);
            txt_pendingshipments = (TextView)itemView.findViewById(R.id.txt_pendingshipments);
            ButterKnife.bind(this , itemView);

        }

        @OnClick(R.id.chkagent)
        void setChkagent(){
            //checkselect.isChecked();
            ((ShipmentEntryActivity)parent).setChackPos(getAdapterPosition(),chkagent.isChecked());
        }
    }
}
