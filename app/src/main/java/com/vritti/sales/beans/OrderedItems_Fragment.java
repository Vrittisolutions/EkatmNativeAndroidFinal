package com.vritti.sales.beans;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.CartActivity;
import com.vritti.sales.activity.ProductList_TabActivity;
import com.vritti.sales.adapters.ItemListAdapter_customer;
import com.vritti.sales.data.AnyMartDatabaseConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import static com.vritti.sales.beans.NonOrdered_Fragment.getDataFromDataBase_itemList;
import static java.lang.String.valueOf;

@SuppressLint("ValidFragment")
public class OrderedItems_Fragment extends Fragment {

    String product_name;
    private static Context parent;
    //protected static ArrayList<Merchants_against_items> myJSONArray;

    protected static ArrayList<AllCatSubcatItems> arrayList_items;

    public static Merchants_against_items bean;
    public static AllCatSubcatItems bean_item;
    public static String today;
    ProgressHUD progress;

    String res = "";
    static String[] myItemId;
    static ListView ordered_list;
    static ListView non_ordered_list;
    static ProductListAdapter padapter;
    static ItemListAdapter_customer iAdapter;
    //ItemListAdapter_customer iAdapter;
    Button btn_confirm, btn_cancel;
    String SubcatID;
    static String PurDigit;
    ImageView img_delete_row;
    String ItemName;
    int qty ;
    int index =0;
    static TextView total_toPay;
    static float amt = 0;

    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    String image_URL;
    SearchView searchView;
    static String jsonData;
    String Jdata;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @SuppressLint("ValidFragment")
    public OrderedItems_Fragment(String jData, String subcatID, String purdigt) {

        this.jsonData = jData;
        this.SubcatID = subcatID;
        this.PurDigit = purdigt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ordered_items__fragment);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tbuds_activity_ordered_items__fragment, container, false);

        sharedpreferences = getActivity().getSharedPreferences(WebUrlClass.MyPREFERENCES,Context.MODE_PRIVATE);

       // image_URL = sharedpreferences.getString("logopath",null);

        parent = getActivity();
      //  databaseHelper = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(parent);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql_db = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
       // mprogress=findViewById(R.id.toolbar_progress_App_bar);

        img_delete_row = view.findViewById(R.id.img_delete_row_1);
        ordered_list = view.findViewById(R.id.listView_ordered);
        total_toPay = view.findViewById(R.id.total_topay);
        btn_confirm = view.findViewById(R.id.button_product_add_to_cart);
       // btn_cancel = view.findViewById(R.id.btncancel);
       // btn_cancel.setVisibility(View.GONE);

        non_ordered_list = (ListView)view.findViewById(R.id.listView_nonordered) ;

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =(SearchView)view.findViewById(R.id.searchview);
        searchView.setVisibility(View.VISIBLE);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        /*Bundle b = getActivity().getIntent().getExtras();
        String jsonData = b.getString("user");
        SubcatID = b.getString("SubCategoryId");
        PurDigit = b.getString("PurDigit");*/

        /*try {
            ProductList_TabActivity.jsonArray = new JSONArray(ProductList_TabActivity.jsonData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        ProductList_TabActivity.myJSONArray = new ArrayList<Merchants_against_items>();

        //ordered items list
        GetOrderedItemsInList();

        setListener();

        return view;
    }

    private void setListener() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                // filter recycler view when query submitted
                // adt.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                //adt.getFilter().filter(query);
                padapter.filter(query);
                return true;
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int i=0;

                //add items in cart
                sql_db = databaseHelper.getWritableDatabase();
                sql_db.execSQL("DROP TABLE IF EXISTS "
                        + AnyMartDatabaseConstants.TABLE_CART_ITEM);
                sql_db.execSQL(AnyMartDatabaseConstants.CREATE_TABLE_CART_ITEM);

                ProductList_TabActivity.myJSONArray = ((ProductListAdapter) ordered_list.getAdapter()).getMerchantAgainstItemsList();

                for(index =0; index < ProductList_TabActivity.myJSONArray.size(); index++) {

                    try{
                        tcf.addCartItems(ProductList_TabActivity.myJSONArray.get(index).getMerchant_id(),
                                ProductList_TabActivity.myJSONArray.get(index).getMerchant_name(),
                                valueOf(ProductList_TabActivity.myJSONArray.get(index).getQnty()),
                                valueOf(ProductList_TabActivity.myJSONArray.get(index).getMinqnty()),
                                ProductList_TabActivity.myJSONArray.get(index).getOffers(),
                                valueOf(ProductList_TabActivity.myJSONArray.get(index).getPrice()),
                                ProductList_TabActivity.myJSONArray.get(index).getProduct_name(),
                                valueOf(ProductList_TabActivity.myJSONArray.get(index).getAmount()),
                                ProductList_TabActivity.myJSONArray.get(index).getProductid(),
                                ProductList_TabActivity.myJSONArray.get(index).getFreeitemid(),
                                valueOf(ProductList_TabActivity.myJSONArray.get(index).getFreeitemqty()),
                                ProductList_TabActivity.myJSONArray.get(index).getFreeitemname(),
                                ProductList_TabActivity.myJSONArray.get(index).getValidfrom(),
                                ProductList_TabActivity.myJSONArray.get(index).getValidto(),
                                "" );
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                Cursor cursor1 = sql_db.rawQuery("Select * from "
                        + AnyMartDatabaseConstants.TABLE_CART_ITEM , null);
                Log.d("test", "" + cursor1.getCount());

                if (cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                }

                JSONArray jsonArray1 = new JSONArray();

                if (tcf.getCartItems() > 0) {

                    for ( i = 0; i < ProductList_TabActivity.myJSONArray.size(); i++) {

                        String  ItemName = ProductList_TabActivity.myJSONArray.get(i).getProduct_name();
                        String ItemId = ProductList_TabActivity.myJSONArray.get(i).getProductid();
                        float qty = ProductList_TabActivity.myJSONArray.get(i).getQnty();
                        float ToatlAmount = ProductList_TabActivity.myJSONArray.get(i).getAmount();
                        float Rate = ProductList_TabActivity.myJSONArray.get(i).getPrice();
                        String Merchant_Name = ProductList_TabActivity.myJSONArray.get(i).getMerchant_name();
                        String Merchant_id = ProductList_TabActivity.myJSONArray.get(i).getMerchant_id();
                        String perDigit = ProductList_TabActivity.myJSONArray.get(i).getPerDigit();
                        perDigit = PurDigit;

                        JSONObject jsonObject = new JSONObject();

                        try {

                            jsonObject.put("ItemName", ItemName);
                            jsonObject.put("ItemId", ItemId);
                            jsonObject.put("Qty", qty);
                            jsonObject.put("TotalAmount", ToatlAmount);
                            jsonObject.put("itemmrp", Rate);
                            jsonObject.put("custVendorname", Merchant_Name);
                            jsonObject.put("CustVendorMasterId", Merchant_id);
                            jsonObject.putOpt("PurDigit", perDigit);
                            //price
                            jsonArray1.put(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(parent, "Item added to cart", Toast.LENGTH_LONG).show();
                    Intent intent1 = new Intent(parent, CartActivity.class);
                    Bundle b = new Bundle();
                    b.putString("OrderedItems", jsonArray1.toString());
                    b.putString("SubCategoryId",SubcatID);
                    b.putString("PayableAmount", valueOf(amt));
                    b.putString("PurDigit",PurDigit);
                    intent1.putExtras(b);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                   // getActivity().overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                    finish();
                } else {
                    Toast.makeText(parent, "No items in cart", Toast.LENGTH_LONG).show();
                }
            }
        });

       /* btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(parent, com.vritti.orderbilling.customer.MainActivity.class);
                startActivity(intent1);
                getActivity().overridePendingTransition(R.anim.enter_slide_in_up,R.anim.enter_slide_out_up);
                finish();
            }
        });*/
    }

    private static void finish() {

    }

    static class EmployeeComparator implements Comparator<Merchants_against_items> {
        @Override
        public int compare(Merchants_against_items o1, Merchants_against_items o2) {
            return o1.getProduct_name().compareTo(o2.getProduct_name());
        }
    }

    //ordered items list
    public static void GetOrderedItemsInList(){
        // final ArrayList<Merchants_against_items> myJSONArray = new ArrayList<Merchants_against_items>();

        ProductList_TabActivity.myItemId = null;
        ProductList_TabActivity.myItemId = new String[ProductList_TabActivity.jsonArray.length()];

        ProductList_TabActivity.myJSONArray.clear();

        for (int i = 0; i< ProductList_TabActivity.jsonArray.length(); i++) {

            // if(arrayList_items.size() == 0){
            JSONObject jOBJ = new JSONObject();
            bean = new Merchants_against_items();

            try {
                ProductList_TabActivity.myItemId[i] = ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemId");
                bean.setProduct_name(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemName"));
                bean.setProductid(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemId"));
                bean.setQnty(Float.parseFloat(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("Qty")));
                bean.setAmount(Float.parseFloat(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("TotalAmount")));
                bean.setPrice(Float.parseFloat(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("itemmrp")));
                bean.setMerchant_name(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("custVendorname"));
                bean.setMerchant_id(ProductList_TabActivity.jsonArray.getJSONObject(i).getString("CustVendorMasterId"));
                bean.setPerDigit(PurDigit);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ProductList_TabActivity.myJSONArray.add(bean);
        }

        calculate_total();  //calculate total amount of all products

        Comparator<Merchants_against_items> myComparator = new Comparator<Merchants_against_items>() {
            public int compare(Merchants_against_items obj1,Merchants_against_items obj2) {
                return obj1.getProduct_name().compareTo(obj2.getProduct_name());
            }
        };

        Collections.sort(ProductList_TabActivity.myJSONArray, myComparator);

        padapter = new ProductListAdapter(parent,ProductList_TabActivity.myJSONArray);
        ordered_list.setAdapter(padapter);

        // Toast.makeText(parent,"Ordered List should not be empty",Toast.LENGTH_SHORT).show();
    }

    //calculate total of all products
    public static void calculate_total(){
        amt =0;
        for (int k = 0; k < ProductList_TabActivity.myJSONArray.size(); k++) {

            amt = amt + ProductList_TabActivity.myJSONArray.get(k).getAmount();
        }
        float payableAmount = amt;
        double amount = Double.parseDouble(valueOf(amt));
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
       // total_toPay.setText(String.valueOf(amt)+" ₹");
        total_toPay.setText(formatted+" ₹");
    }

    public static int getIndex(String itemName) {
        int i;
        int index_arr = 0;
        for (i = 0; i < ProductList_TabActivity.jsonArray.length(); i++) {
            try {

                String pname = ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemName");
                if (itemName.equals(pname)) {
                   index_arr = i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return index_arr;
    }

    public static void showNewPrompt(final int ind, final String itemName) {
        // TODO Auto-generated method stub

        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.tbuds_dialog_message);
        myDialog.setCancelable(true);

        // myDialog.getWindow().setGravity(Gravity.BOTTOM);
        // myDialog.setTitle("Complete Activity");

        final TextView quest = myDialog.findViewById(R.id.textMsg);
        quest.setText(Html.fromHtml("Do you really want to remove <b><i> "+
                ProductList_TabActivity.myJSONArray.get(ind).getProduct_name()+"</i></b> from Ordered list?"));

        Button btnyes = myDialog
                .findViewById(R.id.btn_yes);

        btnyes.setText("YES");
        btnyes.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            public void onClick(View v) {
                // TODO Auto-generated method stub
               /* try {
                   name = ProductList_TabActivity.jsonArray.getString(ind);

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/

               int a = getIndex(itemName);

                ProductList_TabActivity.jsonArray.remove(a);
                System.out.println(valueOf(ProductList_TabActivity.jsonArray));
                Toast.makeText(parent,ProductList_TabActivity.myJSONArray.get(ind).getProduct_name()+" removed from Ordered List",Toast.LENGTH_SHORT).show();

                ProductList_TabActivity.myItemId = null;
                ProductList_TabActivity.myItemId = new String[ProductList_TabActivity.jsonArray.length()];

                for (int i = 0; i< ProductList_TabActivity.jsonArray.length(); i++) {

                    JSONObject jOBJ = new JSONObject();

                    try {

                        ProductList_TabActivity.myItemId[i] = ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //myJSONArray.add(bean);
                }
                GetOrderedItemsInList();
                getDataFromDataBase_itemList();
                myDialog.dismiss();
            }
        });

        Button btnno = myDialog
                .findViewById(R.id.btn_no);
        btnno.setText("NO");
        btnno.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    ///////////////////////////////////////////////////adapter//////////////////////////////////////////////////////////////////////
    public static class ProductListAdapter extends BaseAdapter {
        private ArrayList<Merchants_against_items> arrayList;

        private Context parent;
        private LayoutInflater mInflater;
        private ViewHolder holder = null;
        private String productId;
        int minteger = 0;
        private Context context;
        TextView txtTotal;
        ArrayList<Merchants_against_items> arrayList1;
     //   private AddProductToCartInterface addProductToCartInterface;

        public ProductListAdapter(Context context,
                                  ArrayList<Merchants_against_items> list) {
            parent = context;
            arrayList = list;
            this.arrayList1=new ArrayList<>();
            this.arrayList1.addAll(list);
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

        @Override
        public int getViewTypeCount() {

            int count;
            if (arrayList.size() > 0) {
                count = getCount();
            } else {
                Toast.makeText(parent,"No matches found",Toast.LENGTH_SHORT).show();
                count = 1;
            }
            return count;

            // return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @SuppressWarnings("deprecation")
        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            final int pos = position;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.tbuds_custom_murchant_details, null);
                holder = new ViewHolder();
                holder.bean = arrayList.get(position);

                txtTotal = (TextView) convertView.findViewById(R.id.productlist_subtotal);
                holder.txtItemname = (TextView)convertView.findViewById(R.id.productlist_itemname);
                holder.txtSubTotal = (TextView)convertView.findViewById(R.id.productlist_subtotal);
                holder.edit_product_qty = (EditText) convertView.findViewById(R.id.productlist_edit_product_qty);
                holder.Image_Delete_row = (ImageView)convertView.findViewById(R.id.img_delete_row_1);
                holder.Image_Delete_row.setVisibility(View.VISIBLE);
            //    holder.textview_product_sellers = (TextView) convertView.findViewById(R.id.textview_product_sellers);
            //    holder.textview_product_rate = (TextView) convertView.findViewById(R.id.textview_product_rate);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final View finalConvertView = convertView;


            final View finalConvertView1 = convertView;
            holder.edit_product_qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Float ss = null;
                    TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.productlist_subtotal);
                    EditText edtqty = (EditText) finalConvertView1.findViewById(R.id.productlist_edit_product_qty);

                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0))) {
                        arrayList.get(pos).setQnty(0);
                        txtTotal.setText("0 ₹");
                        arrayList.get(pos).setAmount(0);
                    } else {
                        // int pos = (int) holder.Edit_productQty.getTag();
                        int decimal_digit = Integer.parseInt(arrayList.get(pos).getPerDigit());

                        holder.edit_product_qty.setTag(arrayList.get(position));

                        if (decimal_digit == 0) {

                            holder.edit_product_qty.setInputType(InputType.TYPE_CLASS_NUMBER);
                            ss = Float.valueOf(s.toString());


                            float p = Float.parseFloat(valueOf(ss));
                            arrayList.get(pos).setQnty(p);

                            float pc = arrayList.get(pos).getPrice();

                            float subtotal = Float.parseFloat(valueOf(ss)) * pc;
                            // float sbtotal = (arrayList.get(pos).getQnty())*100;

                            arrayList.get(pos).setAmount(subtotal);

                        } else{

                            edtqty.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

                            ss = Float.valueOf(s.toString());
                            // int q = Integer.parseInt(ss);
                            float p = Float.parseFloat(valueOf(ss));
                            arrayList.get(pos).setQnty(p);

                            float pc = arrayList.get(pos).getPrice();

                            float subtotal = Float.parseFloat(valueOf(ss)) * pc;
                            // float sbtotal = (arrayList.get(pos).getQnty())*100;

                            arrayList.get(pos).setAmount(subtotal);
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                    TextView txtTotal = (TextView) finalConvertView.findViewById(R.id.productlist_subtotal);
                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0))) {
                        arrayList.get(pos).setQnty(0);
                        txtTotal.setText("0 ₹");
                        arrayList.get(pos).setAmount(0);
                    }else
                    {
                        // holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
                        txtTotal.setText(arrayList.get(position).getAmount()+" ₹");
                    }

                    //to calculate total of all products
                /*if(parent instanceof ProductList_TabActivity){
                    ((ProductList_TabActivity)parent).calculate_total();
                }*/

                    calculate_total();

                }
            });

       /* holder.categotyName.setText(arrayList.get(position).getCategoryName());
        holder.subcatcount.setText("" + arrayList.get(position).getSubcatcount());*/

            holder.txtItemname.setText(arrayList.get(position).getProduct_name());
            String sval = valueOf(arrayList.get(position).getQnty());
            if(sval.contains(".0")){
                String s = ".0";
                if (sval.endsWith(".0")) {
                    sval =  sval.substring(0, sval.length() - s.length());
                }
            /*String arr[] = sval.split(".0");
            sval = arr[0];*/
            }
            //  holder.edit_product_qty.setText(String.valueOf(arrayList.get(position).getQnty()));
            holder.edit_product_qty.setText(sval);

            double amount = Double.parseDouble(valueOf(arrayList.get(position).getAmount()));
            DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
            String formatted = formatter.format(amount);
            //holder.txtSubTotal.setText(arrayList.get(position).getAmount()+" ₹");
            holder.txtSubTotal.setText(formatted+" ₹");


            holder.Image_Delete_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*if(parent instanceof ProductList_TabActivity){
                    ((ProductList_TabActivity)parent).showNewPrompt(pos);
                }*/

                String item = arrayList.get(pos).getProduct_name();
                    //showNewPrompt(position);
                    showNewPrompt(position,item);
                }
            });

            // holder.Image_Delete_row.setTag(arrayList.get(position));

            return convertView;
        }

        public ArrayList<Merchants_against_items> filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            arrayList.clear();
            if (charText.length() == 0) {
                arrayList.addAll(arrayList1);
            } else {
                for (Merchants_against_items wp : arrayList1) {
                    if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                        arrayList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
            return arrayList;
        }

        public ArrayList<Merchants_against_items> getMerchantAgainstItemsList() {
            ArrayList<Merchants_against_items> list = new ArrayList<>();
            for (int i = 0; i < arrayList.size(); i++) {
            /*//if (arrayList.get(i).getIsChecked())
            list.add(arrayList.get(i));*/
                boolean a1 = (arrayList.get(i).getQnty() != 0);

                if(arrayList.get(i).getQnty() != 0){
                    list.add(arrayList.get(i));
                    // Toast.makeText(context,arrayList.get(i).getProduct_name()+" added to Ordered list",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
            return list;
        }

        private class ViewHolder {
            TextView textview_product_sellers, textview_product_offers, textview_product_rate;
            ImageView imageviewAddProduct;
            EditText edit_product_qty;
            TextView txtItemname, txtSubTotal;
            ImageView Image_Delete_row;
            Merchants_against_items bean;
        }
    }
}
