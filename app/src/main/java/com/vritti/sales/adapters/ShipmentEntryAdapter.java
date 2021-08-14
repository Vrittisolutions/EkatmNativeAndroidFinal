package com.vritti.sales.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.CounterbillingBean;
import com.vritti.sales.beans.ProspectSetting;
import com.vritti.sales.beans.ShipmentEntryBean;

import java.util.ArrayList;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;

public class ShipmentEntryAdapter extends BaseAdapter {

    private Context parent;
    static ArrayList<ShipmentEntryBean> list_shipments;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;

    public ShipmentEntryAdapter(Context context, ArrayList<ShipmentEntryBean> listDetails) {
        this.parent = context;
        this.list_shipments = listDetails;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return list_shipments.size();
    }

    @Override
    public Object getItem(int position) {
        return list_shipments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {  return getCount();   }

    @Override
    public int getItemViewType(int position) {  return position;  }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        ShipmentEntryBean pitems = (ShipmentEntryBean) getItem(position);

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.tbuds_shipment_entry_adapter, null);
            holder = new ViewHolder();

            holder.checkselect = (CheckBox)convertView.findViewById(R.id.chkbox);
            holder.checkselect.setVisibility(View.VISIBLE);
            holder.edtqty = (EditText) convertView.findViewById(R.id.edt_qty);
            holder.txtsono = (TextView) convertView.findViewById(R.id.txtsono);
            holder.txtitmcode = (TextView)convertView.findViewById(R.id.txtitmcode);
            holder.txtitmdesc = (TextView)convertView.findViewById(R.id.txtitmdesc);
            holder.txtaddeddt = (TextView)convertView.findViewById(R.id.txtaddeddt);
            holder.txtschdate = (TextView)convertView.findViewById(R.id.txtschdate);
            holder.txtschqty = (TextView)convertView.findViewById(R.id.txtschqty);
            holder.txtshipmentqty = (TextView)convertView.findViewById(R.id.txtshipmentqty);

            convertView.setTag(holder);
            holder.checkselect.setTag(list_shipments.get(position));
            holder.edtqty.setTag(list_shipments.get(position));

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.edtqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Float qty = null;
                holder.edtqty.setTag(list_shipments.get(position));

                if (((s.toString().trim() == "") || (s.toString() == null) || (s.toString().length() == 0))) {

                    list_shipments.get(position).setEdtQty(0.0F);

                }else {
                    holder.edtqty.setTag(list_shipments.get(position));

                    qty = valueOf(s.toString());
                   // float p = ss;
                    float rate = Float.parseFloat(list_shipments.get(position).getRate());
                    float Amt = qty * rate ;       //rate * edtqty
                    list_shipments.get(position).setSubtotal_Amt(Amt);
                    list_shipments.get(position).setEdtQty(qty);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.checkselect.setTag(list_shipments.get(position));
                holder.edtqty.setTag(list_shipments.get(position));

               /* if (((s.toString().trim() == "") || (s.toString() == null) || (s.toString() == "0") || (s.toString().length() == 0))) {
                    holder.checkselect.setVisibility(View.INVISIBLE);
                }else {
                    holder.checkselect.setVisibility(View.VISIBLE);
                }*/
            }
        });

        String[] addedDt = (list_shipments.get(position).getSailingDate()).split("-");
        String date1 = addedDt[0];
        String date2 = addedDt[1];

        String[] scheduleDt = (list_shipments.get(position).getScheduleDate()).split("-");
        String date3 = scheduleDt[0];
        String date4 = scheduleDt[1];

        holder.txtsono.setText(list_shipments.get(position).getSOno());
        holder.txtitmcode.setText(list_shipments.get(position).getItemCode());
        holder.txtitmdesc.setText(list_shipments.get(position).getItemDesc());
        holder.txtaddeddt.setText(/*list_shipments.get(position).getSailingDate()*/date1 + " " + date2);
        holder.txtschdate.setText(/*list_shipments.get(position).getScheduleDate()*/date3 + " " + date4);
        holder.txtschqty.setText(list_shipments.get(position).getQty());
        holder.txtshipmentqty.setText(list_shipments.get(position).getShipmentQty());

        holder.checkselect.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                list_shipments.get(position).setChecked(isSelected);
            }
        });

        return convertView;
    }

    public class ViewHolder{

        CheckBox checkselect;
        EditText edtqty;
        TextView txtsono, txtitmcode,txtitmdesc, txtaddeddt,txtschdate, txtschqty, txtshipmentqty;

    }

    public static ArrayList<ShipmentEntryBean> getAllSelectedSOData() {
        ArrayList<ShipmentEntryBean> list_selected = new ArrayList<>();

        for (int i = 0; i < list_shipments.size(); i++) {
            if (list_shipments.get(i).isChecked() &&
                    (/*list_shipments.get(i).getEdtQty() != ("") ||*/
                            list_shipments.get(i).getEdtQty() != null)){
                list_selected.add(list_shipments.get(i));
            }else {

            }
        }
        return list_selected;
    }
}
