package com.vritti.vwb.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PutAwayDetail;
import com.vritti.ekatm.R;
import com.vritti.ekatm.bean.ModuleName;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {

    private ArrayList<ModuleName> list;
    Context context;


    public ModuleAdapter(Context context,ArrayList<ModuleName> countries) {
        this.list = countries;
        this.context=context;
    }

    @Override
    public ModuleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.module_item_lay, viewGroup, false);

        return new ModuleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ModuleAdapter.ViewHolder holder, int position) {

        ModuleName moduleName=list.get(position);
        String moduel=moduleName.getModuleName();
        if (moduel.equalsIgnoreCase("VWB")){
            holder.imageView.setImageResource(R.drawable.vwb_mob_icon);
            holder.imageView.setImageResource(R.drawable.expense_management);
            holder.imageView.setImageResource((R.drawable.conversation));
        }
        if (moduel.equalsIgnoreCase("CRM")){
            holder.imageView.setImageResource(R.drawable.crm_mob_icon);
        }
        if (moduel.equalsIgnoreCase("Inventory")){
            holder.imageView.setImageResource((R.drawable.inventory_mob_icon));
        }
        if (moduel.equalsIgnoreCase("Support")){
            holder.imageView.setImageResource((R.drawable.servie_desk));
        }
        if (moduel.equalsIgnoreCase("Sales")){
            holder.imageView.setImageResource((R.drawable.sales_mob_icon));
        }if (moduel.equalsIgnoreCase("WMS")){
            holder.imageView.setImageResource((R.drawable.icon_wms));
        }
        if (moduel.equalsIgnoreCase("PI")){
            holder.imageView.setImageResource((R.drawable.inventory_mob_icon));
        }
       /* if (moduleName.getImage()==0){
            holder.root.setVisibility(View.GONE);
        }else {
            holder.root.setVisibility(View.VISIBLE);

            holder.imageView.setImageResource(moduleName.getImage());
        }*/
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout root;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.ivLogo);
            root = view.findViewById(R.id.root);
            view.setTag(view);
        }
    }

}