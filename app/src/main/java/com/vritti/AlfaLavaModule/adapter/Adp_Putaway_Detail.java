package com.vritti.AlfaLavaModule.adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.vritti.AlfaLavaModule.Interface.ItemClick;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.fragment.FragmentPutAwaysDetail;
import com.vritti.crm.adapter.FilteredProspectAdapter;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Admin-1 on 10/5/2016.
 */
public class Adp_Putaway_Detail extends RecyclerView.Adapter<Adp_Putaway_Detail.ViewHolder> {

    private ArrayList<PutAwaysBean> countries;
    private Dialog mdialog;
    private EditText et_country;
    private View view;//Edt_quantity
    private EditText Edt_quantity;
    private AutoCompleteTextView Edt_location;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    Context context;

    public Adp_Putaway_Detail(ArrayList<PutAwaysBean> countries,Context context) {
        this.countries = countries;
        this.context=context;
    }

    @Override
    public Adp_Putaway_Detail.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.alfa_item_putaway, viewGroup, false);

        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_Location.setText(countries.get(position).getLocationCode());
        holder.tv_Quantity.setText(countries.get(position).getPutAwayQty());
        holder.tv_item_discription.setText(countries.get(position).getItemDesc());//tv_item_code
        holder.tv_item_code.setText(countries.get(position).getItemCode());//tv_item_code

        holder.setClickListener(new ItemClick() {
            @Override
            public void onClick(View view, int Position, boolean isLongClick) {
                if (isLongClick) {
                  /*  Edit_dig(view.getContext(), position, countries.get(position).getLocationCode(),
                            countries.get(position).getPutAwayQty());*/

                    // Toast.makeText(view.getContext(), "#" + position + " - " + countries.get(position).getPutAwayQty() + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    String s = countries.get(Position).getSuggPutAwayId();
                   /* if (!s.equalsIgnoreCase("")) {
                        Toast.makeText(view.getContext(), "#" + Position + " - " + countries.get(Position).getPutAwayQty(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(view.getContext(), PutawayDetailEdit.class);
                        Bundle mBundle = new Bundle();
                        mBundle.putString("Grnno", countries.get(Position).getGRN_Number());
                        mBundle.putString("GrnHdr", countries.get(Position).getGRN_Header());
                        mBundle.putString("Loc", countries.get(Position).getLocationCode());
                        mBundle.putString("GrnDetail_id", countries.get(Position).getGRNDetailId());
                        mBundle.putString("doneflg", countries.get(Position).getFlgDone());
                        mBundle.putString("insertupdateflg", countries.get(Position).getFlgInsertUpdate());
                        mBundle.putString("itemcode", countries.get(Position).getItemCode());
                        mBundle.putString("Itemdesc", countries.get(Position).getItemDesc());
                        mBundle.putString("locmaster_id", countries.get(Position).getLocationMasterId());
                        mBundle.putString("qty", countries.get(Position).getPutAwayQty());
                        mBundle.putString("Suggput_id", countries.get(Position).getSuggPutAwayId());
                        intent.putExtras(mBundle);
                        view.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(view.getContext(), "Can not Edit item", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return countries.size();
    }

    public String getSuggested(int position) {
        return countries.get(position).getSuggPutAwayId();
    }

    public void addItem(int position, PutAwaysBean country) {
        ArrayList<PutAwaysBean> countrie = countries;
        countries.add(position + 1, country);
        notifyItemInserted(position + 1);
        notifyItemInserted(countries.size());
        notifyDataSetChanged();
        sql.execSQL("DELETE FROM " + db.TABLE_PUTAWAY);
        ArrayList<PutAwaysBean> countri = countries;
        for (int index = 0; index < countries.size(); index++) {
            PutAwaysBean bean = new PutAwaysBean();
            bean.setGRN_Number(countries.get(index).getGRN_Number());
            bean.setGRN_Header(countries.get(index).getGRN_Header());
            bean.setSuggPutAwayId(countries.get(index).getSuggPutAwayId());
            bean.setGRNDetailId(countries.get(index).getGRNDetailId());
            bean.setLocationMasterId(countries.get(index).getLocationMasterId());
            bean.setItemCode(countries.get(index).getItemCode());
            bean.setItemDesc(countries.get(index).getItemDesc());
            bean.setLocationCode(countries.get(index).getLocationCode());
            bean.setPutAwayQty(countries.get(index).getPutAwayQty());
            bean.setFlgDone(countries.get(index).getFlgDone());
            bean.setFlgInsertUpdate(countries.get(index).getFlgDone());
            cf.Insert_Putaways(bean);
        }
    }

    public void removeItem(int position) {
        int PutawaySr = countries.get(position).getPutAwaySr();

        ContentValues values = new ContentValues();
        values.put("DoneFlag", "Y");
        sql.update(db.TABLE_PUTAWAY, values, "PutAwaysr=?", new String[]{String.valueOf(PutawaySr)});
        try {
            Cursor c1 = sql.rawQuery("Select * From " + db.TABLE_PUTAWAY + " where PutAwaysr=?", new String[]{String.valueOf(PutawaySr)});
            int s = c1.getCount();
            String flag;
            c1.moveToFirst();
            do {
                flag = c1.getString(c1.getColumnIndex("DoneFlag"));
            } while (c1.moveToNext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        countries.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, countries.size());
    }


    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tv_Quantity;
        TextView tv_Location;
        TextView tv_item_discription;
        TextView tv_item_code;
        private ItemClick clickListener;


        public ViewHolder(View view) {
            super(view);
            tv_Quantity = (TextView) view.findViewById(R.id.edtQuantity);
            tv_Location = (TextView) view.findViewById(R.id.edtlocation);//tv_grn_no
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

    private ArrayList<String> getLocationArray() {
        ArrayList<String> data = new ArrayList<String>();
        data.clear();
        try {
            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_LOCATION_MASTER, null);
            int cnt = c.getCount();
            if (c != null && cnt > 0) {
                c.moveToFirst();
                do {
                    data.add(c.getString(c.getColumnIndex("LocationCode")));
                } while (c.moveToNext());

            }
        } catch (Exception e) {
            return null;
        }
        return data;
    }



    private String getLocation_ID(String loc) {
        ArrayList<String> data = new ArrayList<String>();

        String data1 = null;

        Cursor c = sql.rawQuery("Select * from " + db.TABLE_LOCATION_MASTER + " Where LocationCode='" + loc + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                data.add(c.getString(c.getColumnIndex("LocationMasterId")));
                data.add(c.getString(c.getColumnIndex("LocationCode")));
                data.add(c.getString(c.getColumnIndex("LocationDesc")));
                data.add(c.getString(c.getColumnIndex("WarehouseDescription")));
                data.add(c.getString(c.getColumnIndex("PlantName")));

            } while (c.moveToNext());
        }

        data.get(0);
        return data.get(0);


    }

    public void setFilter(ArrayList<PutAwaysBean> Countries) {
        countries = new ArrayList<PutAwaysBean>();
        countries.addAll(Countries);
        notifyDataSetChanged();
    }

}

