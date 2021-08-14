package com.vritti.sales.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.ekatm.R;
import com.vritti.sales.activity.SOApproveDetailActivity;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.util.ArrayList;

public class SOApprDetailsAdapter extends BaseAdapter {
    private static ArrayList<OrderHistoryBean> list;
    private Context parent;
    private LayoutInflater mInflater;
    private static ArrayList<OrderHistoryBean> arraylist;
    String key = "";

    public SOApprDetailsAdapter(Context parent, ArrayList<OrderHistoryBean> custSelectionList) {
        this.parent = parent;
        this.list = custSelectionList;
        arraylist = new ArrayList<OrderHistoryBean>();
        arraylist.addAll(custSelectionList);

        mInflater = LayoutInflater.from(parent);
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
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {

        final int pos = position;

        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.sales_soappr_details_adapter, null);
            holder = new ViewHolder();

            holder.txtitemname =  convertView.findViewById(R.id.txtitemname);
            holder.txtavalstock =  convertView.findViewById(R.id.txtavalstock);
            holder.txtordqty =  convertView.findViewById(R.id.txtordqty);
            holder.edtapprqty =  convertView.findViewById(R.id.edtapprqty);
            holder.edtapprqty.setSelection(holder.edtapprqty.getText().length());
            holder.txtrate =  convertView.findViewById(R.id.txtrate);
            holder.txtlineamt =  convertView.findViewById(R.id.txtlineamt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.edtapprqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                float orgqty = 0.0f, apprqty = 0.0f, lineAmt = 0.0f, rate = 0.0f;
                orgqty = Float.parseFloat(arraylist.get(pos).getOrgQty());
                rate = arraylist.get(pos).getRate();

                String apQty = s.toString();

                try{

                if(s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase(null)){
                    //lineAmt = orgqty * rate;
                    lineAmt = 0 * rate;
                    arraylist.get(pos).setLineAmt(lineAmt);
                    holder.txtlineamt.setText(String.format("%.2f",arraylist.get(pos).getLineAmt()));
                }else if(Float.parseFloat(s.toString()) > orgqty) {

                    Toast.makeText(parent,"Quantity should not be greater than requested quantity",Toast.LENGTH_SHORT).show();
                    holder.edtapprqty.setText(arraylist.get(pos).getOrgQty());
                    lineAmt = orgqty * rate;
                    arraylist.get(pos).setLineAmt(lineAmt);
                    arraylist.get(pos).setApprQty(String.format("%.2f",orgqty));
                    holder.txtlineamt.setText(String.format("%.2f",arraylist.get(pos).getLineAmt()));
                }else {
                    apprqty = Float.parseFloat(s.toString().trim());
                    lineAmt = apprqty * rate;
                    arraylist.get(pos).setLineAmt(lineAmt);
                    arraylist.get(pos).setApprQty(String.format("%.2f",apprqty));
                    holder.txtlineamt.setText(String.format("%.2f",arraylist.get(pos).getLineAmt()));
                }

                }catch (Exception e){
                    e.printStackTrace();
                }

                if(parent instanceof SOApproveDetailActivity){
                    ((SOApproveDetailActivity)parent).calculate_total();
                }
            }
        });

       holder.edtapprqty.setText(arraylist.get(position).getApprQty());
       holder.txtitemname.setText(arraylist.get(position).getItemDesc());
       holder.txtordqty.setText(arraylist.get(position).getOrgQty());
       holder.txtrate.setText(String.format("%.2f",arraylist.get(position).getRate()));
       holder.txtlineamt.setText(String.format("%.2f",arraylist.get(position).getLineAmt()));

        holder.edtapprqty.setSelection(holder.edtapprqty.getText().length());
        holder.edtapprqty.requestFocus(holder.edtapprqty.getText().length());

       return convertView;
    }

    class ViewHolder{
        TextView txtitemname,txtavalstock,txtordqty,txtrate,txtlineamt;
        EditText edtapprqty;
    }

    public static ArrayList<OrderHistoryBean> getAllSelectedSOData() {
        ArrayList<OrderHistoryBean> list_selected = new ArrayList<>();

        for (int i = 0; i < arraylist.size(); i++) {
            float appQty = Float.parseFloat(arraylist.get(i).getApprQty());

            list_selected.add(arraylist.get(i));

            /*if(appQty != 0  || appQty != 0.0F || appQty != 0.00 || appQty != 0.0){
                list_selected.add(arraylist.get(i));
            }else {

            }*/
        }
        return list_selected;
    }

}
