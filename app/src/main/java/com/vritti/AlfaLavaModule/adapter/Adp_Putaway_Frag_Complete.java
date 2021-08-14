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
import android.widget.Toast;


import com.vritti.AlfaLavaModule.Interface.ItemClick;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 10/17/2016.
 */
public class Adp_Putaway_Frag_Complete extends RecyclerView.Adapter<Adp_Putaway_Frag_Complete.ViewHolder> {



    private ArrayList<PutAwaysBean> countries;
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

    public Adp_Putaway_Frag_Complete(ArrayList<PutAwaysBean> countries) {
        this.countries = countries;
    }

    @Override
    public Adp_Putaway_Frag_Complete.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alfa_item_putaway_details, viewGroup, false);

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

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_Location.setText(countries.get(position).getLocationCode());
        holder.tv_Quantity.setText(countries.get(position).getPutAwayQty());
        holder.tv_item_discription.setText(countries.get(position).getItemDesc());//tv_item_code
        holder.tv_item_code.setText(countries.get(position).getItemCode());
        holder.setClickListener(new ItemClick() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                   /* Edit_dig(view.getContext(), position, countries.get(position).getLocationCode(), countries.get(position).getPutAwayQty());*/
                    Toast.makeText(view.getContext(), "#" + position + " - " + countries.get(position).getPutAwayQty() + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "#" + position + " - " + countries.get(position).getPutAwayQty(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return countries.size();
    }

    public void addItem(PutAwaysBean country) {
        countries.add(country);
        notifyItemInserted(countries.size());
    }

   /* public void removeItem(int position) {
        int PutawaySr = countries.get(position).getPutAwaySr();
        SQLiteDatabase sql = Db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("DoneFlag", "Y");
        sql.update(fd.TABLE_PUTAWAY, values, "PutAwaysr=?", new String[]{String.valueOf(PutawaySr)});
        Cursor c1 = sql.rawQuery("Select * From " + fd.TABLE_PUTAWAY+ " where PutAwaysr=?", new String[]{String.valueOf(PutawaySr)});
        int s = c1.getCount();
        countries.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, countries.size());
    }*/

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_Quantity;
        TextView tv_Location;
        TextView tv_item_discription;
        TextView tv_item_code;
        private ItemClick clickListener;


        public ViewHolder(View view) {
            super(view);
            tv_Quantity = (TextView) view.findViewById(R.id.edtQuantity);
            tv_Location = (TextView) view.findViewById(R.id.edtlocation);
            tv_item_discription = (TextView) view.findViewById(R.id.tv_itemname);
            tv_item_code = (TextView) view.findViewById(R.id.tv_itemcode);
            view.setTag(view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });*/

        }

        public void setClickListener(ItemClick itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }

    }


}
