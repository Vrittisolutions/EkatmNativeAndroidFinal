package com.vritti.sales.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;

import java.util.ArrayList;

/**
 * Created by  chetanaon 2/21/2018.
 */
public class listcopyAdapter extends BaseAdapter {
    private ArrayList<AllCatSubcatItems> arrayList;
    private ArrayList<AllCatSubcatItems> arrayList_1;

    private Context parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
  //  private AddProductToCartInterface addProductToCartInterface;

    public listcopyAdapter(Context context,
                           ArrayList<AllCatSubcatItems> list
    ) {
        parent = context;
        arrayList = list;
        mInflater = LayoutInflater.from(parent);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_copylist, null);
            holder = new ViewHolder();

          //  holder.itemCheckBox = (AppCompatCheckBox) convertView.findViewById(R.id.imgcheck);
            holder.txtWishList = (TextView)convertView.findViewById(R.id.txtsonocopy);
            holder.txtcopydatetime = (TextView)convertView.findViewById(R.id.txtcopydatetime);
           /* holder.txtqty = (TextView)convertView.findViewById(R.id.wishqtytotal);
            holder.txtprice = (TextView)convertView.findViewById(R.id.wishprice_1);
            holder.txttotalamt = (TextView)convertView.findViewById(R.id.wishtotalAmt);*/
          //  holder.Image_Delete_row = (ImageView)convertView.findViewById(R.id.img_delete_row_1);
           // holder.Image_Delete_row.setVisibility(View.INVISIBLE);;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String sono = arrayList.get(position).getSono();
        String DoAck = arrayList.get(position).getDoAck();
        //float price = arrayList.get(position).getPrice();

       /* String sval = String.valueOf(arrayList.get(position).getEdtQty());

        try{

            if(sval.contains(".0")){
                String s = ".0";
                if (sval.endsWith(".0")) {
                    sval =  sval.substring(0, sval.length() - s.length());
                }
                *//*String arr[] = sval.split(".0");
                sval = arr[0];*//*
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        String qty = sval;
        Float amountTotal = arrayList.get(position).getTotalAmount();*/

        holder.txtWishList.setText(sono);
        holder.txtcopydatetime.setText(DoAck);
        /*holder.txtprice.setText(price+" ₹");
        holder.txtqty.setText(qty);
        holder.txttotalamt.setText(amountTotal+" ₹");*/

      /*  holder.Image_Delete_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if(parent instanceof WishlistActivity){
                            ((WishlistActivity)parent).showNewPrompt(pos);
                        }
                    }
                });*/

      /*  holder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = ((AppCompatCheckBox) v).isChecked();
                arrayList.get(pos).setChecked(isSelected);

            }
        });*/

        /*holder.id = position;
        holder.itemCheckBox.setChecked(arrayList.get(position).getIsChecked());*/

        return convertView;
    }

    public ArrayList<AllCatSubcatItems> getCartItemsList() {
        ArrayList<AllCatSubcatItems> list = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getIsChecked()){
                list.add(arrayList.get(i));
            }
        }
        return list;
    }

    private static class ViewHolder {
       // public AppCompatCheckBox itemCheckBox;
       // ImageView imageviewAddProduct;
        TextView txtWishList, txtcopydatetime;
        //ImageView Image_Delete_row;
        //int id;
    }
}