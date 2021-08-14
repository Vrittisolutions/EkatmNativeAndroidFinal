package com.vritti.crmlib.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.ProductBean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

/**
 * Created by sharvari on 01-Mar-17.
 */

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.ViewHolder> {

    List<ProductBean> productBeanList;
    static Context context;
    static String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    static Utility ut;
    static DatabaseHandlers db;
    static CommonFunctionCrm cf;
    static SQLiteDatabase sql;

    public ProductDetailsAdapter(List<ProductBean> productBeanList1) {
        this.productBeanList = productBeanList1;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProduct;
        EditText edtqty;
        Button btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            ut = new Utility();
            cf = new CommonFunctionCrm(context);
            String settingKey = ut.getSharedPreference_SettingKey(context);
            String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(context, dabasename);
            CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
            PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
            LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
            Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            sql = db.getWritableDatabase();
            txtProduct = (TextView) itemView.findViewById(R.id.txtProduct);
            edtqty = (EditText) itemView.findViewById(R.id.edtqty);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.crm_custom_product_recycler, viewGroup, false);
        ViewHolder fpvh = new ViewHolder(v);
        return fpvh;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int j = position;

        holder.txtProduct.setText(productBeanList.get(position).getItemDesc());
        holder.edtqty.setText(productBeanList.get(position).getQnty());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name = productBeanList.get(j).getItemDesc();
                long a = sql.delete(db.TABLE_Product_Details,
                        "ItemDesc=?", new String[]{name});
                notifyDataSetChanged();
            }
        });


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return productBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
