package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.SalesItemActivity;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.SaleItemBean;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.TaxGSTCalculationClass;

import java.util.ArrayList;
import java.util.Locale;

public class SaleItemListAdapter extends BaseAdapter {
    private static ArrayList<SaleItemBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<SaleItemBean> arraylist;
    private static ArrayList<SaleItemBean> arraylist_temp;
    private static ArrayList<SaleItemBean> arraylist_temp2;
    String key = "";
    private static ArrayList<TaxClassBean> taxList;
    private static ArrayList<String> taxListString;
    ArrayAdapter adapter;

    public SaleItemListAdapter(Context parent, ArrayList<SaleItemBean> saleitemList,
                               ArrayList<TaxClassBean> taxclslist,ArrayList<String> listTaxString) {
        this.parent = parent;
        this.list = saleitemList;
        arraylist = new ArrayList<SaleItemBean>();
        arraylist_temp = new ArrayList<SaleItemBean>();
        arraylist_temp2 = new ArrayList<SaleItemBean>();
        arraylist.addAll(saleitemList);
        arraylist_temp.addAll(saleitemList);
        arraylist_temp2.addAll(saleitemList);
        taxList = new ArrayList<TaxClassBean>();
        taxList = taxclslist;
        taxListString = listTaxString;
        mInflater = LayoutInflater.from(parent);
        adapter = new ArrayAdapter(parent, android.R.layout.simple_list_item_1,taxListString);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        int count;
        if (arraylist.size() > 0) {
            count = getCount();
        } else {
            //Toast.makeText(parent,"No items in Ordered Items list",Toast.LENGTH_SHORT).show();
            count = 1;
        }
        return count;

        //  return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sale_itemlist_adapter, null);
            holder = new ViewHolder();

            holder.txtitem = (TextView) convertView.findViewById(R.id.txtitem);
            holder.txtfinamt = convertView.findViewById(R.id.txtfinamt);
            holder.txttaxamt = convertView.findViewById(R.id.txttaxamt);
            holder.txtdiscamt = convertView.findViewById(R.id.txtdiscamt);
            holder.txtamt = convertView.findViewById(R.id.txtamt);
            holder.txtrate = convertView.findViewById(R.id.txtrate);
            holder.txtqty = convertView.findViewById(R.id.txtqty);
            holder.txtuom = convertView.findViewById(R.id.txtuom);
            holder.layout = convertView.findViewById(R.id.layout);
            holder.imgedt = convertView.findViewById(R.id.imgedt);
            holder.imgdelete = convertView.findViewById(R.id.imgdelete);
            holder.txttaxcls = convertView.findViewById(R.id.txttaxcls);
            holder.txttaxcls.setAdapter(adapter);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position%2 == 1){
            holder.layout.setBackgroundColor(Color.parseColor("#EDEDED"));
        }else {
            holder.layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        holder.txtqty.setTag(arraylist.get(position));
        holder.imgedt.setTag(arraylist.get(position));
        holder.txttaxcls.setTag(arraylist.get(position));

        holder.imgedt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.txtqty.setEnabled(true);
                holder.txtqty.setFocusable(true);
                holder.txttaxcls.setEnabled(true);
                holder.txttaxcls.setFocusable(true);
                holder.txtqty.requestFocus(holder.txtqty.getText().toString().length());
                arraylist.get(position).setEditedClicked(true);
            }
        });

        holder.imgdelete.setTag(arraylist.get(position));
        holder.imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist_temp = SalesItemActivity.listsaleItems;
                arraylist_temp2 = SalesItemActivity.listsaleItems;
                String soDtlId = arraylist.get(position).getSODetailId();

                if(SalesItemActivity.listsaleItems.get(position).getSODetailId().equals(soDtlId)){
                    arraylist.remove(position);
                }else {

                }

                SalesItemActivity.listsaleItems.clear();
                SalesItemActivity.listsaleItems.addAll(arraylist);
                //SalesItemActivity.listsaleItems.remove(position);
                notifyDataSetChanged();
                SalesItemActivity.setFinalValues();

                /*if(arraylist_temp2.get(position).getItemMasterId().equals(arraylist.get(position).getItemMasterId())){
                    if(SalesItemActivity.listsaleItems.get(position).getItemMasterId().equals(arraylist.get(position).getItemMasterId())){
                        SalesItemActivity.listsaleItems.remove(position);
                        notifyDataSetChanged();
                        SalesItemActivity.setFinalValues();
                    }
                }else {

                }*/
                /*SalesItemActivity.listsaleItems.remove(position);
                notifyDataSetChanged();
                SalesItemActivity.setFinalValues();*/
            }
        });

            holder.txtqty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    holder.txtqty.setTag(arraylist.get(pos));

                    float qty = 0.0f, rate = 0.0f, lineAmt = 0.0f, discPer = 0.0f, discAmt = 0.0f,discLineAmt = 0.0f, taxAmt = 0.0f, fAmt = 0.0f,
                            pctgVal = 0.0f;
                    String taxcls = arraylist.get(position).getTaxclass();

                    rate = Float.parseFloat(arraylist.get(position).getRate().toString());
                    lineAmt = Float.parseFloat(arraylist.get(position).getAmtLine().toString());
                    discAmt = Float.parseFloat(arraylist.get(position).getDiscAmt().toString());
                    //discAmt = Float.parseFloat(arraylist.get(position).getDiscAmtLine().toString());
                    taxAmt = Float.parseFloat(arraylist.get(position).getTaxAmtLine().toString());
                    fAmt = Float.parseFloat(arraylist.get(position).getFinalAmtLine().toString());
                    discPer =  Float.parseFloat(arraylist.get(position).getDiscPer());

                    if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                        qty = 0;
                    }else {
                        qty = Float.parseFloat(s.toString());
                    }

                    lineAmt = qty * rate;
                    discAmt = lineAmt * (discPer/100);
                    discLineAmt = lineAmt - discAmt;

                    pctgVal = TaxGSTCalculationClass.TaxGSTCalculationClass(taxcls,discLineAmt,"Adapter");
                    taxAmt = discLineAmt * (pctgVal/100);
                    fAmt = taxAmt + discLineAmt;

                    arraylist.get(pos).setDiscPer(String.format("%.2f",discPer));
                    arraylist.get(pos).setDiscAmt(String.format("%.2f",discAmt));
                    arraylist.get(pos).setDiscAmtLine(String.format("%.2f",discLineAmt));
                    arraylist.get(pos).setAmtLine(String.format("%.2f",lineAmt));
                    arraylist.get(pos).setTaxAmtLine(String.format("%.2f",taxAmt));
                    arraylist.get(pos).setFinalAmtLine(String.format("%.2f",fAmt));
                    arraylist.get(pos).setQty(String.format("%.2f",qty));
                    arraylist.get(pos).setTaxclass(taxcls);

                    holder.txtamt.setText(arraylist.get(pos).getAmtLine());
                    holder.txtamt.setText(arraylist.get(pos).getAmtLine());
                    holder.txtdiscamt.setText(arraylist.get(pos).getDiscAmt());
                    //holder.txtdiscamt.setText(arraylist.get(pos).getDiscAmtLine());
                    holder.txttaxamt.setText(arraylist.get(pos).getTaxAmtLine());
                    float val = Math.round(Float.parseFloat(arraylist.get(pos).getFinalAmtLine()));
                    holder.txtfinamt.setText(String.format("%.2f",val));
                    holder.txttaxcls.setText(arraylist.get(position).getTaxclass());

                    SalesItemActivity.setFinalValues();
                }
            });

        holder.txttaxcls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.txttaxcls.showDropDown();
                holder.txttaxcls.requestFocus();
                return false;
            }
        });

        holder.txttaxcls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                holder.txttaxcls.requestFocus();
                holder.txttaxamt.setTag(arraylist.get(pos));
                holder.txttaxcls.setTag(arraylist.get(pos));

                String a = taxListString.get(position).toString();

                holder.taxClsCode = taxList.get(position).getTaxClassCode().toString();
                holder.taxClsId = taxList.get(position).getTaxClassMasterId().toString();
                holder.taxClsName = taxList.get(position).getTaxClassDesc().toString();
                holder.txttaxcls.setText(taxList.get(position).getTaxClassDesc().toString());

                float qty = Float.parseFloat(arraylist.get(pos).getQty());
                float lineAmt = Float.parseFloat(arraylist.get(pos).getAmtLine());
                float disc_per = Float.parseFloat(arraylist.get(pos).getDiscPer());
                float discLineAmt = Float.parseFloat(arraylist.get(pos).getDiscAmtLine());
                float discAmt = Float.parseFloat(arraylist.get(pos).getDiscAmt());
                float taxAmt = 0f, taxTotalAmt = 0f, pctgval = 0.0f;

                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                taxAmt = discLineAmt * (pctgval/100);
                taxTotalAmt = discLineAmt + taxAmt;

                holder.txttaxamt.setText(String.format("%.2f",taxAmt));
                arraylist.get(pos).setTaxclass(holder.taxClsName);
                arraylist.get(pos).setTaxClsId(holder.taxClsId);
                arraylist.get(pos).setTaxAmtLine(String.format("%.2f",taxAmt));
                arraylist.get(pos).setFinalAmtLine(String.format("%.2f",taxTotalAmt));
            }
        });

        holder.txtitem.setText(arraylist.get(position).getItemdesc());
        holder.txtuom.setText(arraylist.get(position).getUOMCode());
        holder.txtqty.setText(arraylist.get(position).getQty());
        holder.txtrate.setText(arraylist.get(position).getRate());
        holder.txtamt.setText(arraylist.get(position).getAmtLine());
        holder.txtdiscamt.setText(arraylist.get(position).getDiscAmt());
        //holder.txtdiscamt.setText(arraylist.get(position).getDiscAmtLine());
        holder.txttaxamt.setText(arraylist.get(position).getTaxAmtLine());
        float val = Math.round(Float.parseFloat(arraylist.get(position).getFinalAmtLine()));
        holder.txtfinamt.setText(String.format("%.2f",val));
        holder.txttaxcls.setText(arraylist.get(position).getTaxclass());

        return convertView;
    }

    class ViewHolder{
        TextView txtitem,txtfinamt,txttaxamt,txtdiscamt,txtamt,txtrate,txtuom;
        EditText txtqty;
        LinearLayout layout;
        ImageView imgedt,imgdelete;
        AutoCompleteTextView txttaxcls;
        String taxClsId = "", taxClsCode = "",taxClsName = "";
    }

}
