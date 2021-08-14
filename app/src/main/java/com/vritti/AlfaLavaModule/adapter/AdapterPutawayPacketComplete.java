package com.vritti.AlfaLavaModule.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.AlfaLavaModule.Interface.ItemClick;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;


import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class AdapterPutawayPacketComplete extends RecyclerView.Adapter<AdapterPutawayPacketComplete.ViewHolder> {
    private ArrayList<PutawaysPacketBean> list;
    private Dialog mdialog;
    private EditText et_country;

    private View view;//Edt_quantity
    private EditText Edt_quantity;
    private EditText Edt_location;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    public AdapterPutawayPacketComplete(ArrayList<PutawaysPacketBean> countries) {
        this.list = countries;
    }

    @Override
    public AdapterPutawayPacketComplete.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alfa_item_putaway_packet_detail, viewGroup, false);
        userpreferences = viewGroup.getContext().getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(viewGroup.getContext());
        String settingKey = ut.getSharedPreference_SettingKey(viewGroup.getContext());
        String dabasename = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(viewGroup.getContext(), dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(viewGroup.getContext(), WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        return new AdapterPutawayPacketComplete.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterPutawayPacketComplete.ViewHolder holder, int position) {
        holder.tv_Location.setText(list.get(position).getLocationCode());
        holder.tv_Quantity.setText(list.get(position).getBalQty());
        holder.tv_item_code.setText(list.get(position).getItemCode());//tv_item_code
        holder.tv_Packet_no.setText(list.get(position).getPacketNo());//tv_item_code
        holder.tv_item.setText(list.get(position).getItemdesc());
    }
    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_Quantity;
        TextView tv_Location;
        TextView tv_Packet_no;
        TextView tv_item_code,tv_item;
        private ItemClick clickListener;


        public ViewHolder(View view) {
            super(view);
            tv_Quantity = (TextView) view.findViewById(R.id.edtQuantity);
            tv_Location = (TextView) view.findViewById(R.id.edtlocation);//tv_grn_no
            /*tv_Packet_no = (TextView) view.findViewById(R.id.tv_packet_display);
            tv_item_code = (TextView) view.findViewById(R.id.tv_itemname);
            tv_item = (TextView) view.findViewById(R.id.tv_packet_display);*/
            tv_item_code= (TextView) view.findViewById(R.id.tv_Packet);
            tv_item = (TextView) view.findViewById(R.id.tv_packet_display);
            tv_Packet_no = (TextView) view.findViewById(R.id.tv_itemname);
        }
    }


}