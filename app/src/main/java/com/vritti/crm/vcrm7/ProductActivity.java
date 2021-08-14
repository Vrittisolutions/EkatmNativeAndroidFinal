package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.adapter.ProductDetailsAdapter;
import com.vritti.crm.bean.ProductBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    LinearLayout laytxt;
    public  static List<ProductBean> productBeanList = new ArrayList<ProductBean>();
    ProductBean productBean;
    RecyclerView rv;
    LinearLayoutManager llm;
    ProductDetailsAdapter adapter;
    RecyclerView.ItemDecoration itemDecoration;
    SQLiteDatabase sql;
    public static Context context;
    List<String> lstProduct = new ArrayList<String>();
    AutoCompleteTextView EdtProd;
    EditText editqnty;
    String pid;
    SharedPreferences userpreferences;
    Button buttonSave, buttonCancel;
    LinearLayout lst;
    ArrayList<String> Productionitems = new ArrayList<String>();
    String Productid,product_name;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_product);
        init();
        context = ProductActivity.this;
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        displayProductDetails();
       /* if (cf.getProuctcount() > 0) {
            displayProduct();
        } else {

        }*/

        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadProductJSON().execute();

                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }

        EdtProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String query = "SELECT distinct FamilyDesc,FamilyId" +
                        " FROM " + db.TABLE_Product +
                        " WHERE FamilyDesc='" + EdtProd.getText().toString() + "'";
                Cursor cur = sql.rawQuery(query, null);

                if (cur.getCount() > 0) {

                    cur.moveToFirst();
                    do {

                        Productid = cur.getString(cur.getColumnIndex("FamilyId"));
                        product_name = cur.getString(cur.getColumnIndex("FamilyDesc"));

                    } while (cur.moveToNext());

                } else {
                    Productid = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    if (cf.getProuctdetailcount(product_name) > 0) {
                        ContentValues values = new ContentValues();
                        values.put("Qnty", editqnty.getText().toString().trim());
                        long a = sql.update(db.TABLE_Product_Details, values,
                                "ItemDesc=?", new String[]{product_name});
                    } else {
                        ContentValues values = new ContentValues();
                        values.put("ItemDesc",product_name );
                        values.put("Qnty", editqnty.getText().toString().trim());
                        values.put("ItemMasterId", getpid(Productid));
                        long a = sql.insert(db.TABLE_Product_Details, null, values);
                    }
                }
                editqnty.setText("");
                displayProductDetails();

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductActivity.this.finish();
            }
        });
    }

    public boolean validate() {
        // TODO Auto-generated method stub
        if ((EdtProd.getText().toString().equalsIgnoreCase("") ||
                EdtProd.getText().toString().equalsIgnoreCase(" ") ||
                EdtProd.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter product name", Toast.LENGTH_LONG).show();
            return false;
        } else if ((editqnty.getText().toString().equalsIgnoreCase("") ||
                editqnty.getText().toString().equalsIgnoreCase(" ") ||
                editqnty.getText().toString().equalsIgnoreCase(null))) {

            Toast.makeText(context, "Enter quantity", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void init() {
        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        img_add.setVisibility(View.VISIBLE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));

        txt_title.setText("Add Product");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        editqnty = (EditText) findViewById(R.id.editqnty);
        EdtProd = (AutoCompleteTextView) findViewById(R.id.EdtProd);
        //rv = (RecyclerView) findViewById(R.id.recyclerproduct);
        // llm = new LinearLayoutManager(getApplicationContext());
        //  rv.setLayoutManager(llm);
        laytxt = (LinearLayout) findViewById(R.id.laytxt);
        laytxt.setVisibility(View.GONE);
        //  rv.setVisibility(View.GONE);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        lst = (LinearLayout) findViewById(R.id.lst);
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else {
            Toast.makeText(context,"No internet connection",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void displayProductDetails() {

        productBeanList.clear();
        String countQuery = "SELECT  ItemDesc,Qnty FROM "
                + db.TABLE_Product_Details;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                productBean = new ProductBean();
                productBean.setItemDesc(cursor.getString(cursor.getColumnIndex("ItemDesc")));
                if (cursor.getString(cursor.getColumnIndex("Qnty")).equalsIgnoreCase("")) {
                    productBean.setQnty("1");
                } else {
                    productBean.setQnty(cursor.getString(cursor.getColumnIndex("Qnty")));
                }

                productBeanList.add(productBean);

            } while (cursor.moveToNext());
        }
        lst.setVisibility(View.VISIBLE);


        lst.removeAllViews();
        if(productBeanList.size()>0){
            for (int i = 0; i < productBeanList.size(); i++) {
                addViewList(i);
            }
        }

    }


    private void addViewList(int i) {
        TextView txtProduct;
        TextView edtqty;
        ImageView btn_delete;
        LayoutInflater layoutInflater = (LayoutInflater) ProductActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final int position = i;


        final View convertView = layoutInflater.inflate(R.layout.crm_custom_product_recycler,
                null);
        txtProduct = (TextView) convertView.findViewById(R.id.txtProduct);
        edtqty = (TextView) convertView.findViewById(R.id.edtqty);
        btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);


        txtProduct.setText(productBeanList.get(position).getItemDesc());
        edtqty.setText(productBeanList.get(position).getQnty());

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name;
                name = productBeanList.get(position).getItemDesc();
                long a = sql.delete(db.TABLE_Product_Details,
                        "ItemDesc=?", new String[]{name});
                displayProductDetails();

            }
        });


        lst.addView(convertView);
    }

    class DownloadProductJSON extends AsyncTask<Integer, Void, Integer> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //   showProgressDialog();
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_get_Product;
            try {
                res = ut.OpenConnection(url);
                if (res!=null) {
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                    ContentValues values = new ContentValues();
                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_Product, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_Product, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_Product, null, values);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //   dismissProgressDialog();

            if (response.contains("")) {
                displayProduct();

            }
        }

    }

    private void displayProduct() {



        Productionitems.clear();
        String query = "SELECT distinct FamilyId,FamilyDesc" +
                " FROM " + db.TABLE_Product;
        Cursor cur = sql.rawQuery(query, null);
        //   lstReferenceType.add("Select");
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {


                Productionitems.add(cur.getString(cur.getColumnIndex("FamilyDesc")));

            } while (cur.moveToNext());

        }
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
        MySpinnerAdapter customDept = new MySpinnerAdapter(ProductActivity.this,
                R.layout.crm_custom_spinner_txt, Productionitems);
        EdtProd.setAdapter(customDept);//SF0006_ADATSOFT
        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);

        Collections.sort(Productionitems, String.CASE_INSENSITIVE_ORDER);
    }
    private String getpid(String Pname){
        String countQ = "SELECT  FamilyId FROM "
                + db.TABLE_Product + " WHERE FamilyDesc='" + Pname+"'";
        Cursor c = sql.rawQuery(countQ, null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            pid = c.getString(c.getColumnIndex("FamilyId"));

        }
        return pid;
    }
    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:


        private MySpinnerAdapter(Context context, int resource,
                                 List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView,
                    parent);
            //view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position,
                    convertView, parent);
            //  view.setTypeface(font);
            return view;
        }

    }
}
