package com.vritti.sales.OrderBookingNew.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.ItemlistActivityNewBooking;
import com.vritti.sales.beans.AddProductsToCart;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Merchants_against_items;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;

public class ItemListAdapter_customer_new extends BaseAdapter implements Filterable {
    private ArrayList<AllCatSubcatItems> arrayList;
    ArrayList<AllCatSubcatItems> arrayList1;
    private ArrayList<AllCatSubcatItems> arrayListFiltered;
    private Context[] parent;
    private LayoutInflater mInflater;
    private ViewHolder holder = null;
    private String productId;
    int minteger = 0;

    private Activity activity;
    private Context context;
    TextWatcher textWatcher;
    TextView txtprice;
    float price;

    //////////////////////////////
    private DatabaseHandlers databaseHelper;
    private ArrayList<Merchants_against_items> array_List;
    String product_id, product_name, product_img;

    public ItemListAdapter_customer_new(Context context, ArrayList<AllCatSubcatItems> list) {
        this.context= context;
        this.arrayList = list;
        mInflater = LayoutInflater.from(context);
        this.arrayListFiltered= list;
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

        try{
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.custom_product_row, null);
                holder = new ViewHolder();
                holder.bean = arrayList.get(position);
                array_List = new ArrayList<>();

                holder.layrel = convertView.findViewById(R.id.layrel);
                holder.imgzoomview = convertView.findViewById(R.id.imgzoomview);
                holder.laycard = convertView.findViewById(R.id.laycard);
                holder.laymrpselrate = convertView.findViewById(R.id.laymrpselrate);
                holder.layrangerate = convertView.findViewById(R.id.layrangerate);
                holder.layrangenote = convertView.findViewById(R.id.layrangenote);
                holder.layrangenote.setVisibility(View.GONE);
                holder.imgitm = convertView.findViewById(R.id.imgitm);
                holder.ItemName = (TextView) convertView.findViewById(R.id.txtitemname);
                holder.ItemPrice = (TextView)convertView.findViewById(R.id.txtitemprice);
                holder.Edit_productQty = (EditText)convertView.findViewById(R.id.edit_itemqty);
                holder.Edit_productQty.setInputType(InputType.TYPE_CLASS_NUMBER);
                holder.TotalAmount = (TextView)convertView.findViewById(R.id.txt_subtotal);
                holder.btn_multiseller = convertView.findViewById(R.id.btn_multiseller);
                holder.txtmrp = convertView.findViewById(R.id.txtmrp);
                holder.txt_yousave = convertView.findViewById(R.id.txt_yousave);
                holder.txtminordqty = convertView.findViewById(R.id.txtminordqty);
                holder.txtmaxordqty = convertView.findViewById(R.id.txtmaxordqty);
                holder.txtrange = convertView.findViewById(R.id.txtrange);
                holder.txtrangenote = convertView.findViewById(R.id.txtrangenote);
                holder.txtstocknote = convertView.findViewById(R.id.txtstocknote);
                holder.txtdist = convertView.findViewById(R.id.txtdist);
                holder.txtraterange = convertView.findViewById(R.id.txtraterange);
                //holder.txtuom_code = convertView.findViewById(R.id.txtuom_code);
                holder.txtseller = convertView.findViewById(R.id.txtseller);
                holder.txtbrand = convertView.findViewById(R.id.txtbrand);
                holder.laymrp_1 = convertView.findViewById(R.id.laymrp_1);
                holder.layusave_1 = convertView.findViewById(R.id.layusave_1);
                holder.layminmax = convertView.findViewById(R.id.layminmax);
                holder.laybuy1get1 = convertView.findViewById(R.id.laybuy1get1);

                /*holder.imageview_product_logo = (SquareImageView) convertView.findViewById(R.id.Imageview);*/

                convertView.setTag(holder);

            } else {
                TextView txtprice = (TextView)convertView.findViewById(R.id.txtitemprice);

                holder = (ViewHolder)convertView.getTag();
                // holder.ItemPrice.setText("100 ₹");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        /*holder.Edit_productQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                showNewPrompt(position);
            }
        });*/

        final View finalConvertView1 = convertView;

        try{
           // holder.txtuom_code.setText(arrayListFiltered.get(position).getUOMcode());
            holder.txtseller.setText("By "+arrayListFiltered.get(position).getMerchant_name() +" "
                    +context.getResources().getString(R.string.distance)+" "+arrayListFiltered.get(position).getDistance()+" km");

            int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerDigit());
            if(decimal_digit==0){
                if(arrayListFiltered.get(position).getMinOrdQty().contains(".0")){
                    holder.txtminordqty.setText(arrayListFiltered.get(position).getMinOrdQty().replace(".0",""));
                }else if(arrayListFiltered.get(position).getMinOrdQty().contains(".00")){
                    holder.txtminordqty.setText(arrayListFiltered.get(position).getMinOrdQty().replace(".00",""));
                }else {
                    holder.txtminordqty.setText(arrayListFiltered.get(position).getMinOrdQty());
                }

                if(arrayListFiltered.get(position).getMaxOrdQty().contains(".0")){
                    holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty().replace(".0",""));
                }else if(arrayListFiltered.get(position).getMaxOrdQty().contains(".00")){
                    holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty().replace(".00",""));
                }else {
                    holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty());
                }
            }else {
                holder.txtminordqty.setText(arrayListFiltered.get(position).getMinOrdQty());
                holder.txtmaxordqty.setText(arrayListFiltered.get(position).getMaxOrdQty());
            }

            holder.txtrange.setText(arrayListFiltered.get(position).getRange());
            final String range = arrayListFiltered.get(position).getRange();
            final String distance = arrayListFiltered.get(position).getDistance();
            holder.txtdist.setText(distance);

            if(range.equalsIgnoreCase("false")){
                holder.laymrpselrate.setVisibility(View.VISIBLE);
                holder.layrangerate.setVisibility(View.GONE);

                holder.txtrange.setText(context.getResources().getText(R.string.no));
                holder.txtrangenote.setVisibility(View.GONE);
                //holder.txtrangenote.setText(""+context.getResources().getText(R.string.rangenote));
            }else {
                holder.laymrpselrate.setVisibility(View.GONE);
                holder.layrangerate.setVisibility(View.VISIBLE);

                holder.txtrange.setText(context.getResources().getText(R.string.yes));
                holder.txtrangenote.setVisibility(View.VISIBLE);
                holder.txtrangenote.setText(""+context.getResources().getText(R.string.rangenote));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        EditText edtqty = (EditText)finalConvertView1.findViewById(R.id.edit_itemqty);

        holder.Edit_productQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Float ss = null;
                float pc = 0;
                EditText edtqty = (EditText)finalConvertView1.findViewById(R.id.edit_itemqty);

                String itmname = arrayListFiltered.get(pos).getItemName();

                int pos1 = getIndex(itmname);

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {

                    arrayListFiltered.get(pos).setEdtQty(0);

                    holder.TotalAmount.setText("0.00 "); //₹
                }
                else{

                    int decimal_digit = Integer.parseInt(arrayListFiltered.get(pos).getPerDigit());

                    edtqty.setTag(arrayListFiltered.get(pos));

                    ss = Float.valueOf(s.toString());

                    float maxOrdLimit = Float.parseFloat(arrayListFiltered.get(pos).getMaxOrdQty());
                    float minOrdLimit = Float.parseFloat(arrayListFiltered.get(pos).getMinOrdQty());

                        ss = valueOf(s.toString());

                        if((ss > maxOrdLimit || ss < minOrdLimit) && (maxOrdLimit != 0 || maxOrdLimit != 0.0)){

                            //   Toast.makeText(context,""+context.getResources().getString(R.string.maxordlimit),Toast.LENGTH_SHORT).show();
                            arrayListFiltered.get(pos).setTotalAmount(0);
                            // Snackbar.make(view, context.getResources().getString(R.string.maxordlimit), Snackbar.LENGTH_SHORT).show();
                            if(edtqty.isFocusable()){
                                edtqty.setError(context.getResources().getString(R.string.maxordlimit));
                            }else {
                                edtqty.setError(null);
                            }

                        }else {
                            if(decimal_digit == 0){
                                edtqty.setInputType(InputType.TYPE_CLASS_NUMBER);
                            }else {
                                edtqty.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, decimal_digit)});
                            }
                                float p = parseFloat(String.valueOf(ss));
                                arrayListFiltered.get(pos).setEdtQty(p);

                            if(arrayListFiltered.get(pos).getRange().equalsIgnoreCase("true")){
                                pc = arrayListFiltered.get(pos).getMrp();
                            }else {
                                pc = arrayListFiltered.get(pos).getPrice();
                            }

                                float subtotal = parseFloat(String.valueOf(ss)) * pc ;
                                //float sbtotal = (arrayList.get(pos).getEdtQty())*100;
                                arrayListFiltered.get(pos).setTotalAmount(subtotal);
                        }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String itmname = arrayListFiltered.get(pos).getItemName();

                //method getID
                int pos1 = getIndex(itmname);

                TextView txt_yousave = (TextView) finalConvertView1.findViewById(R.id.txt_yousave);
                TextView txtTotal = (TextView) finalConvertView1.findViewById(R.id.txt_subtotal);
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0))) {

                    holder.TotalAmount.setText("0.00 "); //₹
                    txtTotal.setText("0.00 ");//₹

                    float qty_= 0;
                    float youSave = (arrayListFiltered.get(position).getMrp() *qty_)  - (arrayListFiltered.get(position).getPrice()*qty_);

                    if(arrayListFiltered.get(position).getRange().equalsIgnoreCase("true")){
                        youSave = 0;
                    }else {
                        youSave = youSave;
                    }

                    arrayListFiltered.get(pos).setYouSave(youSave);
                    txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);

                }else
                {
                    double amount = Double.parseDouble(String.valueOf(arrayListFiltered.get(pos).getTotalAmount()));
                    DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
                    String formatted = formatter.format(amount);
                   //txtTotal.setText(arrayListFiltered.get(pos).getTotalAmount()+" ₹");
                    txtTotal.setText(formatted );

                    float qty_= Float.parseFloat(s.toString());
                    float youSave = (arrayListFiltered.get(position).getMrp() *qty_)  - (arrayListFiltered.get(position).getPrice()*qty_);

                    if(arrayListFiltered.get(position).getRange().equalsIgnoreCase("true")){
                        youSave = 0;
                    }else {
                        youSave = youSave;
                    }

                    arrayListFiltered.get(pos).setYouSave(youSave);
                    txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);
                }

                if(!((holder.Edit_productQty.getText().toString().equalsIgnoreCase(""))&&
                        (holder.Edit_productQty.getText().toString().equalsIgnoreCase("0")))){
                   // holder.TotalAmount.setText(arrayList.get(position).getTotalAmount()+" ₹");
                }
            }
        });

        //holder.txtuom_code.setText(arrayListFiltered.get(position).getUOMcode());
        if(arrayListFiltered.get(position).getPackOfQty().equalsIgnoreCase("0") ||
                arrayListFiltered.get(position).getPackOfQty().equalsIgnoreCase("1") ||
                arrayListFiltered.get(position).getPackOfQty().equalsIgnoreCase("0.0") ||
                arrayListFiltered.get(position).getPackOfQty().equalsIgnoreCase("1.0")){

            if(arrayListFiltered.get(position).getBrand().equalsIgnoreCase("")||
                    arrayListFiltered.get(position).getBrand().equalsIgnoreCase(null) ||
                    arrayListFiltered.get(position).getBrand().equalsIgnoreCase("null")){
                holder.ItemName.setText(/*arrayListFiltered.get(position).getBrand()+""+*/arrayListFiltered.get(position).getItemName() + ", "
                        +arrayListFiltered.get(position).getContent().replace(".0","")+" "+arrayListFiltered.get(position).getUOMcode());
            }else {
                holder.ItemName.setText(/*arrayListFiltered.get(position).getBrand()+" "+*/arrayListFiltered.get(position).getItemName() + ", "
                        +arrayListFiltered.get(position).getContent().replace(".0","")+" "+arrayListFiltered.get(position).getUOMcode());
            }
        }else {
            if(arrayListFiltered.get(position).getBrand().equalsIgnoreCase("")||
                    arrayListFiltered.get(position).getBrand().equalsIgnoreCase(null) ||
                    arrayListFiltered.get(position).getBrand().equalsIgnoreCase("null")){
                holder.ItemName.setText(/*arrayListFiltered.get(position).getBrand()+""+*/arrayListFiltered.get(position).getItemName() + ", "+context.getResources().getString(R.string.combo1)+" "
                        +arrayListFiltered.get(position).getContent().replace(".0","")+
                        " "+arrayListFiltered.get(position).getUOMcode()+
                        " x "+arrayListFiltered.get(position).getPackOfQty());
            }else {
                holder.ItemName.setText(/*arrayListFiltered.get(position).getBrand()+" "+*/arrayListFiltered.get(position).getItemName() + ", "+context.getResources().getString(R.string.combo1)+" "
                        +arrayListFiltered.get(position).getContent().replace(".0","")+
                        " "+arrayListFiltered.get(position).getUOMcode()+
                        " x "+arrayListFiltered.get(position).getPackOfQty());
            }
        }

        if(arrayListFiltered.get(position).getItemName().contains("Buy") &&
                (arrayListFiltered.get(position).getItemName().contains("Free") ||
                        arrayListFiltered.get(position).getItemName().contains("free"))){
            holder.laybuy1get1.setVisibility(View.VISIBLE);
        }else {
            holder.laybuy1get1.setVisibility(View.GONE);
        }

        if(arrayListFiltered.get(position).getBrand().equalsIgnoreCase("")){
            holder.txtbrand.setVisibility(View.GONE);
        }else {
            holder.txtbrand.setVisibility(View.VISIBLE);
            holder.txtbrand.setText(arrayListFiltered.get(position).getBrand());
        }

        float price = arrayListFiltered.get(pos).getPrice();

       // holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()));

        String val1 = String.valueOf(arrayListFiltered.get(pos).getEdtQty());

        if (val1.equals("0.0")){
            holder.Edit_productQty.setText("");
        }else {

            int decimal_digit = Integer.parseInt(arrayListFiltered.get(pos).getPerDigit());

            if(decimal_digit==0){
                if(String.valueOf(arrayListFiltered.get(pos).getEdtQty()).contains(".0")){
                    holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()).replace(".0",""));
                }else  if(String.valueOf(arrayListFiltered.get(pos).getEdtQty()).contains(".00")){
                    holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()).replace(".00",""));
                }else{
                    holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()));
                }
            }else {
                holder.Edit_productQty.setText(String.valueOf(arrayListFiltered.get(pos).getEdtQty()));
            }

            float qty_= Float.parseFloat(holder.Edit_productQty.getText().toString());
            float youSave = (arrayListFiltered.get(position).getMrp() *qty_)  - (arrayListFiltered.get(position).getPrice()*qty_);
            arrayListFiltered.get(pos).setYouSave(youSave);
            holder.txt_yousave.setText(String.format("%.2f",youSave)/*+" ₹"*/);
        }

        double amount = Double.parseDouble(String.valueOf(price));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);

        holder.ItemPrice.setText(formatted);

        float mrp = arrayListFiltered.get(pos).getMrp();
        double amount2 = mrp;
        String formattedmrp = formatter.format(amount2);
        holder.txtmrp.setText(formattedmrp);

        holder.txtraterange.setText(String.format("%.2f",price) + " - "+ String.format("%.2f",mrp));

        if(arrayListFiltered.get(position).getOutOfStock().equalsIgnoreCase("N")){
           // holder.txtstock.setText(""+context.getResources().getString(R.string.instock));
            //holder.txtstock.setText("In stock");
            holder.laycard.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.laycard.setAlpha((float)1);
            holder.laycard.setFocusable(true);
            holder.laycard.setEnabled(true);
            holder.Edit_productQty.setEnabled(true);
            holder.txtstocknote.setVisibility(View.GONE);
            holder.imgitm.setEnabled(true);
            holder.imgitm.setFocusable(true);
            holder.imgitm.setClickable(true);
        }else {
          //  holder.txtstock.setText(""+context.getResources().getString(R.string.outofstock));
            //holder.txtstock.setText("Out of stock");
            holder.laycard.setBackgroundColor(Color.parseColor("#dcdcdc"));
            holder.laycard.setAlpha((float)0.3);
            holder.laycard.setFocusable(false);
            holder.laycard.setEnabled(false);
            holder.Edit_productQty.setEnabled(false);
            holder.txtstocknote.setVisibility(View.VISIBLE);
            holder.imgitm.setEnabled(false);
            holder.imgitm.setFocusable(false);
            holder.imgitm.setClickable(false);
        }

        if(arrayListFiltered.get(pos).getMrp() == arrayListFiltered.get(pos).getPrice()){
            holder.laymrp_1.setVisibility(View.GONE);
            holder.layusave_1.setVisibility(View.INVISIBLE);
            holder.ItemPrice.setVisibility(View.VISIBLE);
            /*holder.laymrp_1.setAlpha((float)0.2);
            holder.layusave_1.setAlpha((float)0.2);*/
        }else {
            holder.laymrp_1.setVisibility(View.VISIBLE);
            holder.layusave_1.setVisibility(View.VISIBLE);
            holder.ItemPrice.setVisibility(View.VISIBLE);
           /* holder.laymrp_1.setAlpha((float)1);
            holder.layusave_1.setAlpha((float)1);*/
        }

        if((arrayListFiltered.get(position).getMinOrdQty().equalsIgnoreCase("0") ||
                arrayListFiltered.get(position).getMinOrdQty().equalsIgnoreCase("0.0"))
        && (arrayListFiltered.get(position).getMaxOrdQty().equalsIgnoreCase("0") ||
                arrayListFiltered.get(position).getMaxOrdQty().equalsIgnoreCase("0.0"))){
            holder.layminmax.setVisibility(View.GONE);
        }else {
            holder.layminmax.setVisibility(View.VISIBLE);
        }
        /*holder.btn_multiseller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Multimerchant_ProductListActivity.class);
                intent.putExtra("ItemMasterID",arrayListFiltered.get(position).getItemMasterId());
                intent.putExtra("ItemDesc",arrayListFiltered.get(position).getItemName());
                intent.putExtra("ItemImgPath",arrayListFiltered.get(position).getItemImgPath());
                context.startActivity(intent);
            }
        });*/

        holder.imgitm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(context instanceof ItemlistActivityNewBooking){

                    ((ItemlistActivityNewBooking)context).expandImage(arrayList.get(position).getItemImgPath(),
                            arrayList.get(position).getItemName(),
                            arrayList.get(position).getBrand(),
                            arrayList.get(position).getContent().replace(".0",""),
                            arrayList.get(position).getUOMcode(),
                            arrayList.get(position).getPackOfQty());
                }
                return false;
            }
        });

        if(!arrayList.get(position).getItemImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(context)
                        .load(arrayList.get(position).getItemImgPath())
                        //.resize(60,60).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getSubCatImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(context)
                        .load(arrayList.get(position).getSubCatImgPath())
                        //.resize(60,60).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getCatImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(context)
                        .load(arrayList.get(position).getCatImgPath())
                        //.resize(60,60).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }else if(!arrayList.get(position).getBusiSegImgPath().equalsIgnoreCase("")){
            try{
                Picasso.with(context)
                        .load(arrayList.get(position).getBusiSegImgPath())
                        //.resize(50,55).into(holder.imgitm);
                        .into(holder.imgitm);

            }catch (Exception e){
                e.printStackTrace();
            }
        }

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
        for (int i = 0; i < arrayList.size(); i++) {
            /*//if (arrayList.get(i).getIsChecked())
            list.add(arrayList.get(i));*/
            boolean a1 = (arrayList.get(i).getEdtQty() != 0);

                if(arrayList.get(i).getEdtQty() != 0){
                    list.add(arrayList.get(i));
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
        arrayListFiltered.clear();
        if (charText.length() == 0) {
            arrayListFiltered.addAll(arrayList1);
        } else {
            for (AllCatSubcatItems wp : arrayList1) {
                if (wp.getItemName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getBrand().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getBaseRate().toLowerCase(Locale.getDefault()).contains(charText) ||
                        String.valueOf(wp.getMrp()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        /*wp.getMrp().toLowerCase(Locale.getDefault()).contains(charText)*/
                    arrayListFiltered.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return arrayListFiltered;
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

       ImageView imgitm,imgzoomview;
        RelativeLayout layrel;
        TextView ItemName,ItemPrice,TotalAmount,txtmrp,txt_yousave,txtseller,
                txtminordqty,txtmaxordqty,txtrange,txtrangenote, txtstocknote, txtdist,txtraterange,txtuom_code,txtbrand;
        EditText Edit_productQty;
        AllCatSubcatItems bean;
        Button btn_multiseller;
        LinearLayout laycard, laymrpselrate, layrangerate,layrangenote,layusave_1,laymrp_1,layminmax,laybuy1get1;
       // SquareImageView imageview_product_logo;
        int id;
    }

    class DecimalDigitsInputFilter implements InputFilter {
        private Pattern mPattern;
        DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
            mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
        }
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

}
