package com.vritti.sales.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;

public class ItemListAdapter_customer extends BaseAdapter implements Filterable {
    private ArrayList<AllCatSubcatItems> arrayList;
    ArrayList<AllCatSubcatItems> arrayList1;
    private ArrayList<AllCatSubcatItems> arrayListFiltered;
    private ArrayList<AllCatSubcatItems> arrayListFiltered_temp;
    private Context[] parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;
    //private AddProductToCartInterface addProductToCartInterface;

    private Activity activity;
    private Context context;
    TextWatcher textWatcher;
    TextView txtprice;
    float price;

    //////////////////////////////
  //  private DatabaseHelper databaseHelper;
    private ArrayList<Merchants_against_items> array_List;
   // public static ArrayList<AddProductsToCart> addProductsToCartArrayList;
    String product_id, product_name, product_img;

    public ItemListAdapter_customer(Context context, ArrayList<AllCatSubcatItems> list) {
        this.context= context;
        this.arrayList = list;
        mInflater = LayoutInflater.from(context);
        this.arrayListFiltered= list;
        this.arrayListFiltered_temp = list;
        this.arrayList1=new ArrayList<>();
        this.arrayList1.addAll(list);
    }

    @Override
    public int getCount() {
        //return arrayList.size();
        return arrayListFiltered.size();
    }

    @Override
    public Object getItem(int position) {
        //return arrayList.get(position);
        return arrayListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {

        int count;
        if (arrayList.size() > 0) {
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

    @SuppressWarnings("deprecation")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final int pos = position;
        AllCatSubcatItems pitems = (AllCatSubcatItems) getItem(position);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.tbuds_custom_product_row, null);
            holder = new ViewHolder();
            holder.bean = arrayList.get(position);
            array_List = new ArrayList<>();

            holder.itemCheckBox = (AppCompatCheckBox) convertView.findViewById(R.id.image_check);
            holder.itemCheckBox.setVisibility(View.GONE);

            holder.ItemName = (TextView) convertView.findViewById(R.id.txtitemname);
            holder.ItemPrice = (TextView)convertView.findViewById(R.id.txtitemprice);
            holder.Edit_productQty = (EditText)convertView.findViewById(R.id.edit_itemqty);
            holder.Edit_productQty.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder.TotalAmount = (TextView)convertView.findViewById(R.id.txt_subtotal);
            TextView txtprice = (TextView)convertView.findViewById(R.id.txtitemprice);

            /*holder.imageview_product_logo = (SquareImageView) convertView
                    .findViewById(R.id.Imageview);*/

            convertView.setTag(holder);

        } else {
            TextView txtprice = (TextView)convertView.findViewById(R.id.txtitemprice);

            holder = (ViewHolder)convertView.getTag();
           // holder.ItemPrice.setText("100 ₹");
        }

        /*holder.Edit_productQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                showNewPrompt(position);
            }
        });*/

        final View finalConvertView1 = convertView;
        EditText edtqty = (EditText)finalConvertView1.findViewById(R.id.edit_itemqty);

        holder.Edit_productQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Float ss = null;
                EditText edtqty = (EditText)finalConvertView1.findViewById(R.id.edit_itemqty);

                String itmname = arrayListFiltered.get(pos).getItemName();

                int pos1 = getIndex(itmname);

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {

                    arrayListFiltered.get(pos).setEdtQty(0);

                    holder.TotalAmount.setText("0 ₹");
                }
                else{

                    int decimal_digit = Integer.parseInt(arrayListFiltered.get(pos).getPerDigit());

                    //holder.Edit_productQty.setTag(arrayListFiltered.get(pos).getEdtQty());
                    holder.Edit_productQty.setTag(arrayListFiltered.get(pos));

                    if(decimal_digit == 0){

                        edtqty.setInputType(InputType.TYPE_CLASS_NUMBER);
                        ss = valueOf(s.toString());

                        if(s.toString().contains(".") && ss.toString().contains(".")){
                            //error message
                             edtqty.setInputType(InputType.TYPE_CLASS_NUMBER);
                         // edtqty.setError("Enter whole number");
                            //Toast.makeText(parent,"Enter whole number", Toast.LENGTH_SHORT).show();
                            
                        } else{

                            edtqty.setInputType(InputType.TYPE_CLASS_NUMBER);
                             float p = parseFloat(String.valueOf(ss));

                             arrayListFiltered.get(pos).setEdtQty(p);

                            float pc = arrayListFiltered.get(pos).getPrice();

                             float subtotal = parseFloat(String.valueOf(ss)) * pc;
                             //float sbtotal = (arrayList.get(pos).getEdtQty())*100;

                             arrayListFiltered.get(pos).setTotalAmount(subtotal);

                        }

                    } else{
                             edtqty.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        /*ss = valueOf(s.toString());

                        float p = parseFloat(String.valueOf(ss));

                        arrayListFiltered.get(pos).setEdtQty(p);

                        float pc = arrayListFiltered.get(pos).getPrice();

                        float subtotal = parseFloat(String.valueOf(ss)) * pc ;
                        //float sbtotal = (arrayList.get(pos).getEdtQty())*100;

                        arrayListFiltered.get(pos).setTotalAmount(subtotal);*/
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String itmname = arrayListFiltered.get(pos).getItemName();

                //method getID
                int pos1 = getIndex(itmname);

                TextView txtTotal = (TextView) finalConvertView1.findViewById(R.id.txt_subtotal);
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {

                    holder.TotalAmount.setText("0 ₹");
                    txtTotal.setText("0 ₹");
                }else
                {
                    double amount = Double.parseDouble(String.valueOf(arrayListFiltered.get(pos).getTotalAmount()));
                    DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
                    String formatted = formatter.format(amount);
                   //txtTotal.setText(arrayListFiltered.get(pos).getTotalAmount()+" ₹");
                    txtTotal.setText(formatted +" ₹");
                }

                if(!((holder.Edit_productQty.getText().toString().equalsIgnoreCase(""))&&
                        (holder.Edit_productQty.getText().toString().equalsIgnoreCase("0")))){
                   // holder.TotalAmount.setText(arrayList.get(position).getTotalAmount()+" ₹");
                }
            }
        });

        holder.ItemName.setText(arrayListFiltered.get(position).getItemName());

        float price = arrayListFiltered.get(pos).getPrice();

       // holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()));

        String val1 = String.valueOf(arrayListFiltered.get(pos).getEdtQty());

        if (val1.equals("0.0")){
            holder.Edit_productQty.setText("");

        }else {
            holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()));
        }

        double amount = Double.parseDouble(String.valueOf(price));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);

        holder.ItemPrice.setText(formatted+" ₹");

        Picasso.with(context)
                .load(arrayList.get(position).getItemImgPath())
               // .placeholder(R.drawable.bread_layer)      // optional
                .resize(60,60) ;                       // optional

                //.into(holder.imageview_product_logo);
                //"https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png"
                /*.error(R.drawable.error)*/

        return convertView;
    }

    public int getIndex(String itemName)
    {
        for (int i = 0; i < arrayList1.size(); i++)
        {
            AllCatSubcatItems auction = arrayList1.get(i);
            if (itemName.equals(auction.getItemName()))
            {
                return i;
            }
        }

        return -1;
    }

    public ArrayList<AllCatSubcatItems> getAllCatSubcatItemsList() {
        ArrayList<AllCatSubcatItems> list = new ArrayList<>();
        for (int i = 0; i < arrayList1.size(); i++) {
            /*//if (arrayList.get(i).getIsChecked())
            list.add(arrayList.get(i));*/
            boolean a1 = (arrayList1.get(i).getEdtQty() != 0);

                if(arrayList1.get(i).getEdtQty() != 0){
                    list.add(arrayList1.get(i));
                   // Toast.makeText(context,arrayList.get(i).getItemName()+" added to Ordered list",Toast.LENGTH_SHORT).show();
                }else{

                }
        }
        return list;
    }

   /* @Override
    public int getItemCount() {
        return arrayListFiltered.size();
    }*/

    public ArrayList<AllCatSubcatItems> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        arrayListFiltered_temp.clear();
       // arrayListFiltered.clear();
        if (charText.length() == 0) {
            //arrayListFiltered.addAll(arrayList1);
            arrayListFiltered_temp.addAll(arrayList1);
        } else {
            for (AllCatSubcatItems wp : arrayList1) {
                if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayListFiltered_temp.add(wp);
                   // arrayListFiltered.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return arrayListFiltered_temp;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
               // arrayListFiltered.clear();
                if (charString.length() == 0) {
                    arrayListFiltered.addAll(arrayList);
                } else {
                    ArrayList<AllCatSubcatItems> filteredList = new ArrayList<>();
                    for (AllCatSubcatItems row : arrayList) {
                        if (row.getItemName().toLowerCase(Locale.getDefault()).contains(charSequence)) {
                            arrayListFiltered.add(row);
                        }
                       /* if (row.getItemName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
                            filteredList.add(row);
                        }*/
                    }

                    notifyDataSetChanged();
                    //arrayListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayListFiltered = (ArrayList<AllCatSubcatItems>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private static class ViewHolder {

       public AppCompatCheckBox itemCheckBox;
        TextView ItemName;
        TextView ItemPrice;
        EditText Edit_productQty;
        TextView TotalAmount;
        AllCatSubcatItems bean;

        LinearLayout edt_layout;
       // SquareImageView imageview_product_logo;
        int id;
    }

    protected void showNewPrompt(final int position) {
        // TODO Auto-generated method stub
        final Dialog myDialog = new Dialog(context);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialogue_edit);
        myDialog.setCancelable(true);
        // myDialog.getWindow().setGravity(Gravity.BOTTOM);
        //  myDialog.setTitle("Complete Activity");

        final TextView quest = (TextView) myDialog.findViewById(R.id.textMsg);
        final EditText editText_dialog = (EditText)myDialog.findViewById(R.id.editqty_dialog);
        quest.setText("Enter quantity");

        Button btnyes = (Button) myDialog
                .findViewById(R.id.btn_yes);
        btnyes.setText("YES");
        btnyes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String ss = editText_dialog.getText().toString();
                int q = Integer.parseInt(ss);
                arrayList.get(position).setEdtQty(q);

                float pc = arrayList.get(position).getPrice();

                float subtotal = parseFloat(ss) * pc ;
                // float sbtotal = (arrayList.get(pos).getEdtQty())*(arrayList.get(pos).getPrice());
                // float sbtotal = (arrayList.get(position).getEdtQty())*100;

                arrayList.get(position).setTotalAmount(subtotal);

                myDialog.dismiss();
                // finish();
            }
        });

        Button btnno = (Button) myDialog
                .findViewById(R.id.btn_no);
        btnno.setText("NO");
        btnno.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                myDialog.dismiss();
                // finish();
            }
        });

        myDialog.show();

    }

}
