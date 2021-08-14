package com.vritti.crm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.crm.bean.ProspectContact;
import com.vritti.crm.vcrm7.ProspectEnterpriseActivity2;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Adapter.ActivityListMainAdapter_New;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactHolder> {
    ArrayList<ProspectContact> prospectContactArrayList;
    Context context;

    public ContactListAdapter(Context context, ArrayList<ProspectContact> lstContact) {
        this.prospectContactArrayList = lstContact;
        this.context = context;

    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crm_contact_list_row, parent, false);


        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        try {
            holder.nameCont.setText(prospectContactArrayList.get(position).getName());
            holder.desigCont.setText(prospectContactArrayList.get(position).getDesignation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return prospectContactArrayList.size();
    }

    public class ContactHolder extends RecyclerView.ViewHolder {
        TextView desigCont, nameCont;

        public ContactHolder(View itemView) {
            super(itemView);
            desigCont = itemView.findViewById(R.id.desigCont);
            nameCont = itemView.findViewById(R.id.nameCont);
        }
    }
}
