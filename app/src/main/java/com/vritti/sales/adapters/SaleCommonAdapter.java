package com.vritti.sales.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.PriceListBean;
import com.vritti.sales.beans.SaleItemBean;
import com.vritti.sales.beans.TaxClassBean;
import com.vritti.sales.beans.TaxGSTCalculationClass;

import java.util.ArrayList;
import java.util.Locale;

public class SaleCommonAdapter extends BaseAdapter {
    private static ArrayList<PriceListBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<PriceListBean> arraylist;
    private static ArrayList<TaxClassBean> taxList;
    private static ArrayList<String> taxListString;
    String key = "", type = "";
    ArrayAdapter adapter;

    public SaleCommonAdapter(Context parent, ArrayList<PriceListBean> saleitemList, String callFrom,
                             ArrayList<TaxClassBean> taxclslist,ArrayList<String> listTaxString) {
        this.parent = parent;
        this.list = saleitemList;
        arraylist = new ArrayList<PriceListBean>();
        arraylist.addAll(saleitemList);
        taxList = new ArrayList<TaxClassBean>();
        taxList = taxclslist;
        taxListString = listTaxString;
        mInflater = LayoutInflater.from(parent);
        adapter = new ArrayAdapter(parent, android.R.layout.simple_list_item_1,taxListString);

        this.type = callFrom;
    }

    @Override
    public int getCount() {
        return list.size();    }

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
            convertView = mInflater.inflate(R.layout.sale_common_adapter, null);
            holder = new ViewHolder();

            holder.chkbox = (AppCompatCheckBox) convertView.findViewById(R.id.chkbox);
            holder.txtitem = (TextView) convertView.findViewById(R.id.txtitem);
            holder.txtitemcode = (TextView) convertView.findViewById(R.id.txtitemcode);
            holder.txttaxamt = convertView.findViewById(R.id.txttaxamt);
            holder.txtdiscamt = convertView.findViewById(R.id.txtdiscamt);
            holder.txtamt = convertView.findViewById(R.id.txtamt);
            holder.txtrate = convertView.findViewById(R.id.txtrate);
            holder.edtqty = convertView.findViewById(R.id.edtqty);
            holder.edtdisc = convertView.findViewById(R.id.edtdisc);
            holder.layout = convertView.findViewById(R.id.layout);
            holder.txttaxcls = convertView.findViewById(R.id.txttaxcls);
            holder.txtuom = convertView.findViewById(R.id.txtuom);
            holder.txttaxcls.setAdapter(adapter);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(type.equalsIgnoreCase("Contract")){
            //qty, tax editable others non editable
            holder.txtrate.setEnabled(false);
            holder.txtrate.setFocusable(false);
            holder.edtdisc.setEnabled(false);
            holder.edtdisc.setFocusable(false);
            holder.txtrate.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border_grey));
            holder.edtdisc.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border_grey));
        }else if(type.equalsIgnoreCase("PriceList")){
            //qty, disc, tax editable
            holder.txtrate.setEnabled(false);
            holder.txtrate.setFocusable(false);
            holder.edtdisc.setEnabled(true);
            holder.edtdisc.setFocusable(true);
            holder.txtrate.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border_grey));
            holder.edtdisc.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border));
        }else if(type.equalsIgnoreCase("SalesFamily") || type.equalsIgnoreCase("Quotation")){
            //qty, disc, tax editable
            holder.txtrate.setEnabled(true);
            holder.txtrate.setFocusable(true);
            holder.edtdisc.setEnabled(true);
            holder.edtdisc.setFocusable(true);
            holder.txtrate.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border));
            holder.edtdisc.setBackgroundDrawable(parent.getResources().getDrawable(R.drawable.border));
        }

        holder.txttaxcls.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.txttaxcls.showDropDown();
                holder.txttaxcls.requestFocus();
                return false;
            }
        });

        if(type.equalsIgnoreCase("SalesFamily") || type.equalsIgnoreCase("Quotation")){
            holder.txtrate.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    float rate = 0.0f, qty = 0f, disc_per = 0f, lineAmt = 0f, discLineAmt = 0f, discAmt = 0f, taxAmt = 0f,
                            taxTotalAmt = 0f, pctgval = 0.0f;
                    qty = Float.parseFloat(holder.edtqty.getText().toString());
                    String taxCls = holder.txttaxcls.getText().toString();
                    holder.taxClsName = taxCls;
                    disc_per = Float.parseFloat(arraylist.get(pos).getDiscountPer());
                    holder.edtdisc.setText(String.format("%.2f",disc_per));

                    if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                        rate = 0;
                        disc_per = 0.00f;
                        discLineAmt = 0.00f;
                        lineAmt = qty * rate;
                        holder.txtamt.setText(String.format("%.2f",lineAmt));
                    }else {
                        holder.edtqty.setTag(arraylist.get(pos));
                        holder.txtrate.setTag(arraylist.get(pos));
                        holder.txtamt.setTag(arraylist.get(pos));
                        holder.txttaxamt.setTag(arraylist.get(pos));

                        if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                            rate = 0;
                        }else {
                            rate = Float.parseFloat(s.toString());
                        }

                        lineAmt = rate * qty;
                        holder.txtamt.setText(String.format("%.2f",lineAmt));

                        if(holder.edtdisc.getText().toString().equalsIgnoreCase("") ||
                                holder.edtdisc.getText().toString().equalsIgnoreCase(null) ||
                                holder.edtdisc.getText().toString().equalsIgnoreCase("0")){
                            pctgval = 0.0f;
                            disc_per = 0.00f;
                            discAmt = 0.0f;
                            discLineAmt = lineAmt - discAmt;

                            if(taxCls.equalsIgnoreCase("") || taxCls.equalsIgnoreCase(null)){
                                pctgval = 0.0f;
                                taxAmt = 0.0f;
                                //pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                                //taxAmt = discLineAmt * (pctgval/100);
                                taxTotalAmt = discLineAmt + taxAmt;
                            }else {
                                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                                taxAmt = discLineAmt * (pctgval/100);
                                taxTotalAmt = discLineAmt + taxAmt;
                            }

                        }else {
                            disc_per = Float.parseFloat(holder.edtdisc.getText().toString());
                            discAmt = lineAmt * (disc_per/100);
                            discLineAmt = lineAmt - discAmt;

                            if(taxCls.equalsIgnoreCase("") || taxCls.equalsIgnoreCase(null)){
                                pctgval = 0.0f;
                                taxAmt = 0.0f;
                                taxTotalAmt = discLineAmt + taxAmt;
                            }else {
                                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                                taxAmt = discLineAmt * (pctgval/100);
                                taxTotalAmt = discLineAmt + taxAmt;
                            }
                        }

                        holder.txtdiscamt.setText(String.format("%.2f",discAmt));
                        holder.txttaxamt.setText(String.format("%.2f",taxAmt));

                        arraylist.get(pos).setDiscountPer(String.format("%.2f",disc_per));
                        arraylist.get(pos).setDiscamt(String.format("%.2f",discAmt));
                        arraylist.get(pos).setDiscLineAmt(String.format("%.2f",discLineAmt));
                        arraylist.get(pos).setTaxamt(String.format("%.2f",taxAmt));
                        arraylist.get(pos).setTaxLineAmt(String.format("%.2f",taxTotalAmt));
                        arraylist.get(pos).setQtyLine(String.valueOf(qty));
                        arraylist.get(pos).setLineAmt(String.format("%.2f",lineAmt));
                        arraylist.get(pos).setBaseRate(String.format("%.2f",rate));

                    }
                }
            });
        }

        holder.edtqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                final float rate = Float.parseFloat(arraylist.get(pos).getBaseRate());
                float qty = 0f, disc_per = 0f, lineAmt = 0f, discLineAmt = 0f, discAmt = 0f, taxAmt = 0f, taxTotalAmt = 0f,
                pctgval = 0.0f;
                String taxCls = holder.txttaxcls.getText().toString();
                holder.taxClsName = taxCls;
                disc_per = Float.parseFloat(arraylist.get(pos).getDiscountPer());
                holder.edtdisc.setText(String.format("%.2f",disc_per));

                if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                    qty = 0;
                    disc_per = 0.00f;
                    discAmt = 0.0f;
                    discLineAmt = 0.00f;
                    lineAmt = 0f;
                    holder.txtamt.setText(String.format("%.2f",lineAmt));
                }else {
                    holder.edtqty.setTag(arraylist.get(pos));
                    holder.txtamt.setTag(arraylist.get(pos));
                    holder.txttaxamt.setTag(arraylist.get(pos));

                    if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                        qty = 0;
                    }else {
                        qty = Float.parseFloat(s.toString());
                    }

                    lineAmt = rate * qty;
                    holder.txtamt.setText(String.format("%.2f",lineAmt));

                    if(holder.edtdisc.getText().toString().equalsIgnoreCase("") ||
                            holder.edtdisc.getText().toString().equalsIgnoreCase(null) ||
                            holder.edtdisc.getText().toString().equalsIgnoreCase("0")){
                        pctgval = 0.0f;
                        disc_per = 0.00f;
                        discAmt = 0.0f;
                        discLineAmt = lineAmt - discAmt;

                        if(taxCls.equalsIgnoreCase("") || taxCls.equalsIgnoreCase(null)){
                            pctgval = 0.0f;
                            taxAmt = 0.0f;
                            //pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                            //taxAmt = discLineAmt * (pctgval/100);
                            taxTotalAmt = discLineAmt + taxAmt;
                        }else {
                            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                            taxAmt = discLineAmt * (pctgval/100);
                            taxTotalAmt = discLineAmt + taxAmt;
                        }

                    }else {
                        disc_per = Float.parseFloat(holder.edtdisc.getText().toString());
                        discAmt = lineAmt * (disc_per/100);
                        discLineAmt = lineAmt - discAmt;

                        if(taxCls.equalsIgnoreCase("") || taxCls.equalsIgnoreCase(null)){
                            pctgval = 0.0f;
                            taxAmt = 0.0f;
                            taxTotalAmt = discLineAmt + taxAmt;
                        }else {
                            pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                            taxAmt = discLineAmt * (pctgval/100);
                            taxTotalAmt = discLineAmt + taxAmt;
                        }
                    }

                    holder.txtdiscamt.setText(String.format("%.2f",discAmt));
                    holder.txttaxamt.setText(String.format("%.2f",taxAmt));

                    arraylist.get(pos).setDiscountPer(String.format("%.2f",disc_per));
                    arraylist.get(pos).setDiscamt(String.format("%.2f",discAmt));
                    arraylist.get(pos).setDiscLineAmt(String.format("%.2f",discLineAmt));
                    arraylist.get(pos).setTaxamt(String.format("%.2f",taxAmt));
                    arraylist.get(pos).setTaxLineAmt(String.format("%.2f",taxTotalAmt));
                    arraylist.get(pos).setQtyLine(String.valueOf(qty));
                    arraylist.get(pos).setLineAmt(String.format("%.2f",lineAmt));
                    arraylist.get(pos).setBaseRate(String.format("%.2f",rate));
                }
            }
        });

        holder.edtdisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final float rate = Float.parseFloat(arraylist.get(pos).getBaseRate());
                final float qty = Float.parseFloat(arraylist.get(pos).getQtyLine());
                final float lineAmt = Float.parseFloat(arraylist.get(pos).getLineAmt());

                float disc_per = 0f, discLineAmt = 0f, discAmt = 0f, taxAmt = 0f, taxTotalAmt = 0f, pctgval = 0.0f;
                String taxCls = holder.txttaxcls.getText().toString();
                holder.taxClsName = taxCls;
                //disc_per = Float.parseFloat(arraylist.get(pos).getDiscountPer());
                //holder.edtdisc.setText(String.format("%.2f",disc_per));

                if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                    disc_per = 0f;
                    discAmt = 0f;
                    discLineAmt = 0.00f;
                    holder.txtdiscamt.setText(String.format("%.2f",discAmt));
                }else {
                    holder.edtdisc.setTag(arraylist.get(pos));
                    holder.txtdiscamt.setTag(arraylist.get(pos));
                    holder.txttaxamt.setTag(arraylist.get(pos));

                    if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                        disc_per = 0;
                    }else {
                        disc_per = Float.parseFloat(s.toString());
                    }

                    discAmt = lineAmt * (disc_per/100);
                    discLineAmt = lineAmt - discAmt;

                    if(taxCls.equalsIgnoreCase("") || taxCls.equalsIgnoreCase(null)){
                        pctgval = 0.0f;
                        taxAmt = 0.0f;
                        taxTotalAmt = discLineAmt;
                    }else {
                        pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                        taxAmt = discLineAmt * (pctgval/100);
                        taxTotalAmt = discLineAmt + taxAmt;
                    }

                    holder.txtdiscamt.setText(String.format("%.2f",discAmt));
                    holder.txttaxamt.setText(String.format("%.2f",taxAmt));

                    arraylist.get(pos).setDiscountPer(String.format("%.2f",disc_per));
                    arraylist.get(pos).setDiscamt(String.format("%.2f",discAmt));
                    arraylist.get(pos).setDiscLineAmt(String.format("%.2f",discLineAmt));
                    arraylist.get(pos).setTaxamt(String.format("%.2f",taxAmt));
                    arraylist.get(pos).setTaxLineAmt(String.format("%.2f",taxTotalAmt));
                    arraylist.get(pos).setQtyLine(String.valueOf(qty));
                    arraylist.get(pos).setLineAmt(String.format("%.2f",lineAmt));
                    arraylist.get(pos).setBaseRate(String.format("%.2f",rate));
                }

                //notifyDataSetChanged();
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

                float qty = Float.parseFloat(arraylist.get(pos).getQtyLine());
                float lineAmt = Float.parseFloat(arraylist.get(pos).getLineAmt());
                float disc_per = Float.parseFloat(arraylist.get(pos).getDiscountPer());
                float discLineAmt = Float.parseFloat(arraylist.get(pos).getDiscLineAmt());
                float discAmt = Float.parseFloat(arraylist.get(pos).getDiscamt());
                float taxAmt = 0f, taxTotalAmt = 0f, pctgval = 0.0f;

                pctgval = TaxGSTCalculationClass.TaxGSTCalculationClass(holder.taxClsName,discLineAmt,"Adapter");
                taxAmt = discLineAmt * (pctgval/100);
                taxTotalAmt = discLineAmt + taxAmt;

                holder.txttaxamt.setText(String.format("%.2f",taxAmt));
                arraylist.get(pos).setTaxCls(holder.taxClsName);
                arraylist.get(pos).setTaxclsId(holder.taxClsId);
                arraylist.get(pos).setTaxamt(String.format("%.2f",taxAmt));
                arraylist.get(pos).setTaxLineAmt(String.format("%.2f",taxTotalAmt));
            }
        });

        holder.chkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arraylist.get(pos).setIschecked(true);
            }
        });

        holder.txtitem.setText(arraylist.get(position).getItemDesc());
        holder.txtuom.setText(arraylist.get(position).getUOMCode());
        holder.txtitemcode.setText(arraylist.get(position).getItemCode());
        holder.edtqty.setText(arraylist.get(position).getQtyLine());
        holder.txtrate.setText(arraylist.get(position).getBaseRate());
        holder.txtamt.setText(arraylist.get(position).getLineAmt());
        //holder.txtdiscamt.setText(arraylist.get(position).getDiscLineAmt());
        holder.txtdiscamt.setText(arraylist.get(position).getDiscamt());
        //holder.txttaxamt.setText(arraylist.get(position).getTaxLineAmt());
        holder.edtdisc.setText(arraylist.get(position).getDiscountPer());
        holder.txttaxcls.setText(arraylist.get(position).getTaxCls());
        holder.txttaxamt.setText(arraylist.get(position).getTaxamt());

        return convertView;
    }

    class ViewHolder{
        TextView txttaxamt,txtdiscamt,txtamt,txtitemcode,txtitem,txtuom;
        EditText edtqty,edtdisc,txtrate;
        LinearLayout layout;
        AppCompatCheckBox chkbox;
        AutoCompleteTextView txttaxcls;
        String taxClsId = "", taxClsCode = "",taxClsName = "";
    }

    public ArrayList<PriceListBean> getAllCheckedItems() {
        ArrayList<PriceListBean> list = new ArrayList<>();
        for (int i = 0; i < arraylist.size(); i++) {
           /* if (arrayList.get(i).getIsChecked())
            list.add(arrayList.get(i));*/
           if(arraylist.get(i).isIschecked()){
               list.add(arraylist.get(i));
           }
           /* if(arraylist.get(i).getEdtQty() != 0){
                list.add(arraylist.get(i));
                // Toast.makeText(context,arrayList.get(i).getItemName()+" added to Ordered list",Toast.LENGTH_SHORT).show();
            }else{

            }*/
        }
        return list;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        } else {
            for (PriceListBean wp : arraylist) {
                if(key.equalsIgnoreCase("PriceListCode")){
                    if (wp.getpListCode().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        list.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
