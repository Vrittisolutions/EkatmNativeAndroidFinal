package com.vritti.sales.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.common.logging.FLog;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.ShipmentEntryActivity;
import com.vritti.sales.beans.ShipmentEntryBean;
import com.vritti.sales.data.AnyMartData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Float.valueOf;

public class ShipmentEntryAdapter_RecyclerView extends
        RecyclerView.Adapter<ShipmentEntryAdapter_RecyclerView.ShipmentHolder> {

     Context parent;
    static ArrayList<ShipmentEntryBean> list_shipments;
    private LayoutInflater mInflater;
   // private ViewHolder holder = null;

    public ShipmentEntryAdapter_RecyclerView(Context context, ArrayList<ShipmentEntryBean> listDetails) {
        this.parent = context;
        this.list_shipments = listDetails;
        mInflater = LayoutInflater.from(parent);
    }

    @NonNull
    @Override
    public ShipmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tbuds_shipment_entry_adapter, parent, false);

        return new ShipmentHolder(itemView);
       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final ShipmentHolder holder, final int position) {

        try {
            ShipmentEntryBean shipmentEntryBean = list_shipments.get(position);

            String[] addedDt = (list_shipments.get(position).getSailingDate()).split("-");
            String date1 = addedDt[0];
            String date2 = addedDt[1];

            String[] scheduleDt = (list_shipments.get(position).getScheduleDate()).split("-");
            String date3 = scheduleDt[0];
            String date4 = scheduleDt[1];

            holder.txtitmcode.setText(list_shipments.get(position).getItemCode());

            if(list_shipments.get(position).getPackOfQty().equalsIgnoreCase("0") ||
                    list_shipments.get(position).getPackOfQty().equalsIgnoreCase("1") ||
                    list_shipments.get(position).getPackOfQty().equalsIgnoreCase("0.0") ||
                    list_shipments.get(position).getPackOfQty().equalsIgnoreCase("1.0")){

                holder.txtitmdesc.setText(list_shipments.get(position).getBrand()+" "+list_shipments.get(position).getItemDesc()+ ", "+
                        list_shipments.get(position).getContent().replace(".0","")
                        + " "+ list_shipments.get(position).getUOMCode().replace("null",""));
            }else {
                holder.txtitmdesc.setText(list_shipments.get(position).getBrand()+" "+list_shipments.get(position).getItemDesc() + ", "
                        +parent.getResources().getString(R.string.combo1)+" "
                        +list_shipments.get(position).getContent().replace(".0","")+
                        " "+list_shipments.get(position).getUOMCode().replace("null","")+
                        " x "+list_shipments.get(position).getPackOfQty());
            }

            holder.txtaddeddt.setText(date1 + " " + date2);
            holder.txtschdate.setText(date3 + " " + date4);

            if(list_shipments.get(position).getQty().contains(".0")){
                holder.txtschqty.setText(list_shipments.get(position).getQty().replace(".0",""));
            }else if(list_shipments.get(position).getQty().contains(".00")){
                holder.txtschqty.setText(list_shipments.get(position).getQty().replace(".00",""));
            }else {
                holder.txtschqty.setText(list_shipments.get(position).getQty());
            }

            if(list_shipments.get(position).getShipmentQty().contains(".0")){
                holder.txtshipmentqty.setText(list_shipments.get(position).getShipmentQty().replace(".0",""));
                holder.edtqty.setText(list_shipments.get(position).getShipmentQty().replace(".0",""));
                list_shipments.get(position).setEdtQty(Float.valueOf(list_shipments.get(position).getShipmentQty().replace(".0","")));
                list_shipments.get(position).setChecked(true);

                float qty = valueOf(list_shipments.get(position).getShipmentQty());
                // float p = ss;
                float rate = Float.parseFloat(list_shipments.get(position).getRate());
                float Amt = 0;       //rate * edtqty

                Amt = qty * rate ;       //rate * edtqty
                list_shipments.get(position).setSubtotal_Amt(Amt);

            }else if(list_shipments.get(position).getShipmentQty().contains(".00")){
                holder.txtshipmentqty.setText(list_shipments.get(position).getShipmentQty().replace(".00",""));
                holder.edtqty.setText(list_shipments.get(position).getShipmentQty().replace(".00",""));
                list_shipments.get(position).setChecked(true);
                list_shipments.get(position).setEdtQty(Float.valueOf(list_shipments.get(position).getShipmentQty().replace(".0","")));

                float qty = valueOf(list_shipments.get(position).getShipmentQty());
                // float p = ss;
                float rate = Float.parseFloat(list_shipments.get(position).getRate());
                float Amt = 0;       //rate * edtqty

                Amt = qty * rate ;       //rate * edtqty
                list_shipments.get(position).setSubtotal_Amt(Amt);

            }else {
                holder.txtshipmentqty.setText(list_shipments.get(position).getShipmentQty());
                holder.edtqty.setText(list_shipments.get(position).getShipmentQty());
                list_shipments.get(position).setChecked(true);
                list_shipments.get(position).setEdtQty(Float.valueOf(list_shipments.get(position).getShipmentQty().replace(".0","")));

                float qty = valueOf(list_shipments.get(position).getShipmentQty());
                // float p = ss;
                float rate = Float.parseFloat(list_shipments.get(position).getRate());
                float Amt = 0;       //rate * edtqty

                Amt = qty * rate ;       //rate * edtqty
                list_shipments.get(position).setSubtotal_Amt(Amt);

            }

       /* if(shipmentEntryBean.isEnterValue()){
            holder.checkselect.setVisibility(View.VISIBLE);

        }else {
            holder.checkselect.setVisibility(View.INVISIBLE);
        }*/

            holder.edtqty.addTextChangedListener(new TextWatcher() {
                String remQty = list_shipments.get(position).getShipmentQty();
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Float qty = null;
                    // holder.edtqty.setTag(list_shipments.get(position));


                    if (((s.toString().trim() == "") || (s.toString() == null) || (s.toString().length() == 0))) {

                        list_shipments.get(position).setEdtQty(0.0F);

                    }else {
                        holder.edtqty.setTag(list_shipments.get(position));

                        if(Float.parseFloat(remQty) < Float.parseFloat(s.toString())){
                            holder.edtqty.setError(""+parent.getResources().getString(R.string.entshpqty));
                        }else {
                            qty = valueOf(s.toString());
                            // float p = ss;
                            float rate = Float.parseFloat(list_shipments.get(position).getRate());
                            float Amt = 0;       //rate * edtqty

                            Amt = qty * rate ;       //rate * edtqty
                            list_shipments.get(position).setSubtotal_Amt(Amt);
                            list_shipments.get(position).setEdtQty(qty);
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //     holder.checkselect.setTag(list_shipments.get(position));

                    if (((s.toString().trim() == "") || (s.toString() == null) || (s.toString() == "0") || (s.toString().length() == 0))) {
                        //        holder.checkselect.setVisibility(View.INVISIBLE);
                        // holder.checkselect.setChecked(false);
                        list_shipments.get(position).setChecked(false);
                    }else {
                        if(Float.parseFloat(remQty) < Float.parseFloat(s.toString())){
                            holder.edtqty.setError("Entered shipment quantity is greater than actual shipment quantity");
                            //            holder.checkselect.setVisibility(View.INVISIBLE);
                            // holder.checkselect.setChecked(false);
                            list_shipments.get(position).setChecked(false);
                        }else {
                            //            holder.checkselect.setVisibility(View.VISIBLE);
                            //  holder.checkselect.setChecked(true);
                            list_shipments.get(position).setChecked(true);
                        }
                    }

                    holder.edtqty.setSelection(holder.edtqty.getText().toString().length());
                }
            });

            holder.edtqty.setSelection(holder.edtqty.getText().toString().length());

            if(position % 2 == 1){
                holder.view.setBackgroundColor(parent.getResources().getColor(R.color.colorPrimary1));
            }else {
                holder.view.setBackgroundColor(parent.getResources().getColor(R.color.btncolordark));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list_shipments.size();
    }

    public static ArrayList<ShipmentEntryBean> getAllSelectedSOData() {

        ArrayList<ShipmentEntryBean> list_selected = new ArrayList<>();
        try{

            for (int i = 0; i < list_shipments.size(); i++) {
          /*  if (list_shipments.get(i).isChecked()){
                list_selected.add(list_shipments.get(i));*/

                //   boolean a1 = (Float.parseFloat(list_shipments.get(i).getEdtQty()) != 0);

                if(list_shipments.get(i).getEdtQty() != 0  || list_shipments.get(i).getEdtQty() != 0.0F){
                    list_selected.add(list_shipments.get(i));
                }else {

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list_selected;
    }

    public class ShipmentHolder extends RecyclerView.ViewHolder {
        TextView txtshipmentqty,txtitmcode,txtitmdesc,txtaddeddt,txtschdate,txtschqty;
        EditText edtqty;
        View view;

        public ShipmentHolder(View itemView) {
            super(itemView);

            if(itemView!=null) {

                txtshipmentqty = (TextView) itemView.findViewById(R.id.txtshipmentqty);
                txtitmcode = (TextView) itemView.findViewById(R.id.txtitmcode);
                txtitmdesc = (TextView) itemView.findViewById(R.id.txtitmdesc);
                txtaddeddt = (TextView) itemView.findViewById(R.id.txtaddeddt);
                txtschdate = (TextView) itemView.findViewById(R.id.txtschdate);
                txtschqty = (TextView) itemView.findViewById(R.id.txtschqty);
                edtqty = (EditText) itemView.findViewById(R.id.edt_qty);
                view = itemView.findViewById(R.id.view);
            }
        }

    }

}
