package com.vritti.sales.beans;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;

import com.vritti.sales.activity.ProductList_TabActivity;
import com.vritti.sales.adapters.ItemListAdapter_customer;
import com.vritti.sales.data.AnyMartDatabaseConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import static com.vritti.sales.beans.OrderedItems_Fragment.GetOrderedItemsInList;

@SuppressLint("ValidFragment")
public class NonOrdered_Fragment extends Fragment {

    String product_name;
    private static Context parent;
   // private static ArrayList<AllCatSubcatItems> arrayList_items;
    private ArrayList<Merchants_against_items> myJSONArray;;
    public Merchants_against_items bean;
    public static AllCatSubcatItems bean_item;
    public static String today;
    ProgressHUD progress;
    static ListView non_ordered_list;
    static ItemListAdapter_customer iAdapter;
    Button btn_addtoOrderedList;
    static String SubcatID;
    String PurDigit;
    private String[] newuser;
    String ItemName;
    int qty ;
    float amt = 0;
    String res = "";

    SharedPreferences sharedpreferences;
    Toolbar toolbar;
    String image_URL;
    SearchView searchView;
    String jsonData;

    Tbuds_commonFunctions tcf;
    Utility ut;
    private DatabaseHandlers databaseHelper;
    static SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    @SuppressLint("ValidFragment")
    public NonOrdered_Fragment(String jData,String subcatID,String purdigt) {
        this.jsonData = jData;
        this.SubcatID = subcatID;
        this.PurDigit = purdigt;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_non_ordered__fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tbuds_activity_non_ordered__fragment, container, false);

        parent = getActivity();

       /* try {
            ProductList_TabActivity.jsonArray = new JSONArray(jsonData);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        /*ProductList_TabActivity.arrayList_items = ProductList_TabActivity.getDataFromDataBase_itemList();

        iAdapter = new ItemListAdapter_customer(parent,ProductList_TabActivity.arrayList_items);
        NonOrdered_Fragment.non_ordered_list.setAdapter(iAdapter);*/

        sharedpreferences = getActivity().getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
      //  image_URL = sharedpreferences.getString("logopath",null);

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

        non_ordered_list = (ListView)view.findViewById(R.id.listView_nonordered) ;
        btn_addtoOrderedList = (Button)view.findViewById(R.id.addtocart);
        // non_ordered_list.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        //non_ordered_list.setStackFromBottom(true);

        SearchManager searchManager = (SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =(SearchView)view.findViewById(R.id.searchview);
        searchView.setVisibility(View.VISIBLE);
        searchView.setFocusable(true);// searchView is null
        searchView.setFocusableInTouchMode(true);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        getDataFromDataBase_itemList();

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
                iAdapter.filter(query);
                return true;
            }
        });

        btn_addtoOrderedList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ItemId = null;
                String ItemName = null;
                String Merchant_id = null;
                String Merchant_Name = null;
                float Rate;
                float ToatlAmount,qty;
                int i=0;

                ProductList_TabActivity.arrayList_items = ((ItemListAdapter_customer)
                        non_ordered_list.getAdapter()).getAllCatSubcatItemsList();
                if (ProductList_TabActivity.arrayList_items.size() > 0) {

                    JSONArray jsonArray1_new = new JSONArray();
                    AllCatSubcatItems items = ProductList_TabActivity.arrayList_items.get(i);

                    if (ProductList_TabActivity.arrayList_items.size() > 0) {

                        newuser = new String[ProductList_TabActivity.arrayList_items.size()];

                        for (i = 0; i < ProductList_TabActivity.arrayList_items.size(); i++) {

                            ItemName = ProductList_TabActivity.arrayList_items.get(i).getItemName();
                            ItemId = ProductList_TabActivity.arrayList_items.get(i).getItemMasterId();
                            qty = ProductList_TabActivity.arrayList_items.get(i).getEdtQty();
                            ToatlAmount = ProductList_TabActivity.arrayList_items.get(i).getTotalAmount();
                            Rate = ProductList_TabActivity.arrayList_items.get(i).getPrice();
                            Merchant_Name = ProductList_TabActivity.arrayList_items.get(i).getMerchant_name();
                            Merchant_id = ProductList_TabActivity.arrayList_items.get(i).getMerchant_id();

                            JSONObject jsonObject = new JSONObject();

                            try {

                                jsonObject.put("ItemName", ItemName);
                                jsonObject.put("ItemId", ItemId);
                                jsonObject.put("Qty", qty);
                                jsonObject.put("TotalAmount", ToatlAmount);
                                jsonObject.put("itemmrp",Rate);
                                jsonObject.put("custVendorname",Merchant_Name);
                                jsonObject.put("CustVendorMasterId",Merchant_id);

                                //price
                                ProductList_TabActivity.jsonArray.put(jsonObject);

                                newuser[i] = jsonObject.toString();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ////////////Add to ordered items list
                        System.out.println(ProductList_TabActivity.myJSONArray);

                        getDataFromDataBase_itemList(); //to display updated non-ordered lisr
                        GetOrderedItemsInList();        //to add item in ordered list
                        Toast.makeText(parent,"Item added to Ordered items list",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //non ordered items list
    public static void getDataFromDataBase_itemList() {

        // TODO Auto-generated method stub
        ProductList_TabActivity.myItemId = null;
        ProductList_TabActivity.myItemId = new String[ProductList_TabActivity.jsonArray.length()];

        for (int i = 0; i< ProductList_TabActivity.jsonArray.length(); i++) {

            JSONObject jOBJ = new JSONObject();

            try {

                ProductList_TabActivity.myItemId[i] = ProductList_TabActivity.jsonArray.getJSONObject(i).getString("ItemId");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ProductList_TabActivity.arrayList_items = new ArrayList<AllCatSubcatItems>();
        ProductList_TabActivity.arrayList_items.clear();
        String pId = "(";

        for(int j=0; j<ProductList_TabActivity.myItemId.length;j++){
            if(j!=0){
                pId = pId+",";
            }
            pId = pId + "'" + ProductList_TabActivity.myItemId[j] +"'";

        }
        pId = pId + ")";

        /*DatabaseHelper db1 = new DatabaseHelper(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        SQLiteDatabase db = db1.getWritableDatabase();*/

        int subcatcount = 0;
        int itemcount = 0;

        /*Cursor cursor1 = db.rawQuery("Select distinct CategoryId, CategoryName,SubCategoryName,SubCategoryId," +
                "itemmasterid,ItemName, ItemImgPath,itemMRP, CustVendorMasterId,custVendorname  from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + SubcatID + "' and" +
                " itemmasterid NOT IN "+pId , null);*/

        Cursor cursor1 = sql_db.rawQuery("Select * from "
                + AnyMartDatabaseConstants.TABLE_ALL_CAT_SUBCAT_ITEMS + " where SubCategoryId='" + SubcatID + "' and" +
                " itemmasterid NOT IN "+pId + " ORDER BY ItemName ASC ", null);
        Log.d("test", "" + cursor1.getCount());

        if (cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            try {
                do {
                    itemcount = cursor1.getCount();
                    String cat_name = cursor1.getString(cursor1.getColumnIndex("CategoryName"));
                    String cat_id = cursor1.getString(cursor1.getColumnIndex("CategoryId"));
                    String subcat_name = cursor1.getString(cursor1.getColumnIndex("SubCategoryName"));
                    String subcat_id = cursor1.getString(cursor1.getColumnIndex("SubCategoryId"));

                    //add price and all details too

                    bean_item = new AllCatSubcatItems();
                    bean_item.setCategoryId(cat_id);
                    bean_item.setCategoryName(cat_name);
                    bean_item.setSubCategoryName(subcat_name);
                    bean_item.setSubCategoryId(subcat_id);
                    bean_item.setItemMasterId(cursor1.getString(cursor1
                            .getColumnIndex("itemmasterid")));
                    bean_item.setPrice(Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("itemMRP"))));
                    bean_item.setItemName(cursor1.getString(cursor1
                            .getColumnIndex("ItemName")));
                    bean_item.setSubcatcount(subcatcount);
                    bean_item.setItemcount(itemcount);
                    bean_item.setItemImgPath(cursor1.getString(cursor1
                            .getColumnIndex("ItemImgPath")));
                    bean_item.setMerchant_id(cursor1.getString(cursor1
                            .getColumnIndex("CustVendorMasterId")));
                    bean_item.setMerchant_name(cursor1.getString(cursor1
                            .getColumnIndex("custVendorname")));
                    bean_item.setFreeitemid(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemid")));
                   /* bean.setFreeitemname(cursor1.getString(cursor1
                            .getColumnIndex("freeitemname")));*/
                    bean_item.setFreeitemqty(Float.parseFloat(cursor1.getString(cursor1
                            .getColumnIndex("Freeitemqty"))));
                    bean_item.setValidfrom(cursor1.getString(cursor1
                            .getColumnIndex("validfrom")));
                    bean_item.setValidto(cursor1.getString(cursor1
                            .getColumnIndex("validto")));
                    bean_item.setPerDigit(cursor1.getString(cursor1
                            .getColumnIndex("PurDigit")));

                    ProductList_TabActivity.arrayList_items.add(bean_item);

                } while (cursor1.moveToNext());
            } finally {
                cursor1.close();
            }
        }

        iAdapter = new ItemListAdapter_customer(parent,ProductList_TabActivity.arrayList_items);
        non_ordered_list.setAdapter(iAdapter);
    }
}