package com.vritti.sales.CounterBilling;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.Sales_HomeSActivity;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Connectiondetector;
import com.vritti.sales.beans.MarchantPOBean;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.View.OnClickListener;
import static android.view.View.OnFocusChangeListener;
import static android.view.View.VISIBLE;

/**
 * Created by sharvari on 7/28/2016.
 */
public class PurchaceActivity extends AppCompatActivity {
    private Context parent;
    private boolean doubleBackToExitPressedOnce;
    public static ArrayList<AllCatSubcatItems> arrayList;
    boolean isInternetPresent;
    private AllCatSubcatItems bean;
    private String json;
    private GridView listview;

    Connectiondetector cd;
    EditText edRate;
    Button savebill;

    MenuItem m, refresh;
    ArrayAdapter<AllCatSubcatItems> myAdapter;
    public String restoredText;

    SharedPreferences sharedpreferences;
    private CoordinatorLayout coordinatorLayout;
    public static String Mobilenumber;
    String Subcatid, userId;
    SharedPreferences sharedpreferencesUserId;
    CustomAutoCompleteView myAutoComplete;
    MyCartBean myCartBean;
    ArrayList<MyCartBean> myCartBeanArrayList;
    private LinearLayout containerLayout_one;
    ImageButton btn_ok, btn_cancel;
    EditText edAmt, edQnty, edttxtTotalDiscount, edtVendor,edttxtReceived, edttxtRemaining;
    Spinner spinnerunit;
    TextView txtTotal, txtFinalTotal;
    String vendor;
    public String p_id;
    String xml, xmlPO;
    float amt = 0;
    boolean flag_israte = false;
    boolean flag_isamt = false;
    public String xml1, xml2;
    private StringBuilder sb, sb1;
    public String POflag = "";

    JSONObject Json_Main;
    JSONObject Json_Main_xmlPO;
    JSONArray sbArray,sb1array;
    String disc;
    String item_amt;
    double result;
    private JSONObject jsonMain;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_actpurchase);

        /*getSupportActionBar().setTitle("Purchase");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Purchase");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        sb = new StringBuilder();
        sb1 = new StringBuilder();
        cd = new Connectiondetector(PurchaceActivity.this);
        isInternetPresent = cd.isConnectingToInternet();

     //   databaseHandler = new DatabaseHandler(PurchaceActivity.this);
     //   sharedpreferencesUserId = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);
     //   sharedpreferences = getSharedPreferences(SplashActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userid", null);
        restoredText = sharedpreferences.getString("Mobileno", null);

        if (tcf.getMarchantItemRuniCount()) {
            getDataFromDataBase();
        } else {

            getDataFromServer();
        }
        /*}*/
        if (restoredText != null) {
            Mobilenumber = restoredText;
            AnyMartData.MOBILE = restoredText;
        }

        containerLayout_one.removeAllViews();
        for (int i = 0; i < tcf.getPurchaseItems_AgainstVendor(vendor) + 1; i++) {
            addView(i);
        }

        txtTotal.setText("" + amt );

        //calculate final total taking discount
        txtFinalTotal.setText("" + amt );

        savebill.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {

                    for (int i = 0; i < myCartBeanArrayList.size(); i++) {

                        tcf.addFinalPurchase(myCartBeanArrayList.get(i).getProduct_name(),
                                myCartBeanArrayList.get(i).getPrice(),
                                String.valueOf(myCartBeanArrayList.get(i).getQnty()),
                                myCartBeanArrayList.get(i).getDISCOUNT(),
                                myCartBeanArrayList.get(i).getProduct_id(),
                                myCartBeanArrayList.get(i).getAmount(),
                                txtTotal.getText().toString().trim(),
                                edttxtTotalDiscount.getText().toString().trim(),
                                txtFinalTotal.getText().toString().trim(),
                                edtVendor.getText().toString(), "No", getCurrentDate(),
                                myCartBeanArrayList.get(i).getUNIT());
                    }

                    clearTable(PurchaceActivity.this, dbhandler.TABLE_PURCHASE_ITEM_CB);

                    List<MyCartBean> list = tcf.getPoCartItems(vendor);
                    sb1array = new JSONArray();
                    sb1.setLength(0);
                    //sb1.append("<Header>");
                    for (int i = 0; i < list.size(); i++) {

                        String ItemId = list.get(i).getProduct_id();
                        String ItemName = list.get(i).getProduct_name();
                        String MRP = list.get(i).getPrice();
                        String Qty = String.valueOf(list.get(i).getQnty());
                        String Unit = list.get(i).getUNIT();
                        String TotAmt = txtFinalTotal.getText().toString().trim();
                        String ShopName = edtVendor.getText().toString();
                        Float Amt = list.get(i).getAmount();
                        String vendorid = vendor;

                      //  sb1.append("<Table>");
                        JSONObject sb1obj = new JSONObject();

                       /* sb1.append("<ItemName>" + ItemName + "</ItemName>");
                        sb1.append("<MRP>" + MRP + "</MRP>");
                        sb1.append("<Qty>" + Qty + "</Qty>");
                        sb1.append("<Unit>" + Unit + "</Unit>");
                        sb1.append("<Amt>" + "" + Amt + "</Amt>");
                        sb1.append("<vendorid>" + vendorid + "</vendorid>");
                        sb1.append("<ShopName>" + ShopName + "</ShopName>");
                        sb1.append("<TotAmt>" + TotAmt + "</TotAmt>");
                        sb1.append("<ItemId>" + ItemId + "</ItemId>");

                        sb1.append("</Table>");*/

                        try {
                            sb1obj.put("ItemName",ItemName);
                            sb1obj.put("MRP",MRP);
                            sb1obj.put("Qty",Qty);
                            sb1obj.put("Unit",Unit);
                            sb1obj.put("Amt",Amt);
                            sb1obj.put("vendorid",userId);
                            sb1obj.put("ShopName",ShopName);
                            sb1obj.put("TotAmt",TotAmt);
                            sb1obj.put("ItemId",ItemId);
                            sb1array.put(sb1obj);

                            //JsonMain = new JSONObject();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                   // sb1.append("</Header>");
                    //xmlPO = sb1.toString();
                    xmlPO = sb1array.toString();
                    try {
                        Json_Main_xmlPO = new JSONObject();
                        // JsonMain.put("HeaderData", sb1array);
                        Json_Main_xmlPO.put("HeaderData","" );
                        Json_Main_xmlPO.put("ItemData", sb1array);
                        AnyMartData.JMain = Json_Main_xmlPO;
                    }catch( JSONException e){
                        e.printStackTrace();
                    }

                    sb.setLength(0);
                    //sb.append("<Header>");
                    sbArray = new JSONArray();

                    for (int i = 0; i < list.size(); i++) {

                        String itemmasterid = list.get(i).getProduct_id();
                        String itemname = list.get(i).getProduct_name();
                        String vendorid = vendor;

                        JSONObject sbOBJ = new JSONObject();
                        /*sb.append("<Table>");

                        sb.append("<categoryid>" + " " + "</categoryid>");
                        sb.append("<categoryname>" + " " + "</categoryname>");
                        sb.append("<subcategoryid>" + " " + "</subcategoryid>");
                        sb.append("<subcategoryname>" + " " + "</subcategoryname>");
                        sb.append("<itemmasterid>" + itemmasterid + "</itemmasterid>");
                        sb.append("<itemname>" + itemname + "</itemname>");
                        sb.append("<vendorid>" + vendorid + "</vendorid>");

                        sb.append("</Table>");*/

                        try {
                            sbOBJ.put("categoryid","");
                            sbOBJ.put("categoryname","");
                            sbOBJ.put("subcategoryid","");
                            sbOBJ.put("subcategoryname","");
                            sbOBJ.put("itemmasterid",itemmasterid);
                            sbOBJ.put("itemname",itemname);
                            sbOBJ.put("vendorid",userId);
                            sbArray.put(sbOBJ);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    xml = sbArray.toString();
                    try {
                        Json_Main = new JSONObject();
                       // JsonMain.put("HeaderData", sb1array);
                        Json_Main.put("HeaderData", "");
                        Json_Main.put("ItemData", sbArray);
                        AnyMartData.JMain = Json_Main;
                    }catch( JSONException e){
                        e.printStackTrace();
                    }

                    if (!(xml==null)) {
                        if (cd.isConnectingToInternet()) {
                            if ((AnyMartData.SESSION_ID != null)
                                    && (AnyMartData.HANDLE != null)) {
                                new AddItemsToServer().execute();
                            } else {
                                new StartSession_tbuds(PurchaceActivity.this, new CallbackInterface() {

                                    @Override
                                    public void callMethod() {
                                        new AddItemsToServer().execute();
                                    }

                                    @Override
                                    public void callfailMethod(String s) {

                                    }
                                });
                            }
                        }
                    } else {

                    }
                }
            }
        });
    }

    private void getDataFromServer() {

        if (cd.isConnectingToInternet()) {
            if ((AnyMartData.SESSION_ID != null)
                    && (AnyMartData.HANDLE != null)) {
                new GetProductListRuni().execute();
            } else {
                new StartSession_tbuds(PurchaceActivity.this, new CallbackInterface() {

                    @Override
                    public void callMethod() {
                        new GetProductListRuni().execute();
                    }

                    @Override
                    public void callfailMethod(String s) {

                    }
                });
            }
        } else {
            Toast.makeText(getApplicationContext(), "No internet..", Toast.LENGTH_LONG).show();
        }
    }

    class GetProductListRuni extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_getProductListRuni = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressBar();
            // showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_getProductListRuni = AnyMartData.MAIN_URL + AnyMartData.METHOD_ITEMS_FOR_VENDOR_Runi +
                    "?handler="+ AnyMartData.HANDLE + "&sessionid="+ AnyMartData.SESSION_ID;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL urlGetProductListRuni = new URL(url_getProductListRuni);
                urlConnection = urlGetProductListRuni.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_GetProductListRuni = new StringBuffer();
                String line;
                StringBuilder str_build_GetProductListRuni = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_GetProductListRuni = stringBuff_GetProductListRuni.append(line);
                }
                responseString = stringBuff_GetProductListRuni.toString().replaceAll("^\"|\"$", "");
                resp_getProductListRuni = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_getProductListRuni);

            } catch (Exception e) {
                resp_getProductListRuni = "error";
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //  hideProgressBar();
            //  dismissProgressDialog();
            if (resp_getProductListRuni.equalsIgnoreCase("Session Expired")) {
                if (cd.isConnectingToInternet()) {
                    new StartSession_tbuds(PurchaceActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new GetProductListRuni().execute();
                        }

                        @Override
                        public void callfailMethod(String s) {

                        }
                    });
                }
            } else if (resp_getProductListRuni.equalsIgnoreCase("error")) {
                Toast.makeText(parent, "Server Error..", Toast.LENGTH_LONG)
                        .show();
            } else {
                json = resp_getProductListRuni;
                tcf.deleteMarchantItemsRuni();
                parseJson(json);
            }
        }
    }

    protected void parseJson(String json) {
        // dbHandler.deleteMarchantItems();
        //additemBeanArrayList.clear();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                MarchantPOBean bean = new MarchantPOBean();

                bean.setItemId(jsonArray.getJSONObject(i).getString(
                        "itemmasterid"));
                bean.setItemName(jsonArray.getJSONObject(i).getString(
                        "itemname"));
                tcf.addMarchantItemRuni(bean);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class AddItemsToServer extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_addItemToserver = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  showProgressBar();
            //  showProgressDialog();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_addItemToserver = null;

           /* url_addItemToserver = AnyMartData.MAIN_URL+
                    AnyMartData.METHOD_INSERT_ITEM_AGAINST_VENDOR+
                    "?sessionid="+AnyMartData.SESSION_ID+
                    "&handler="+AnyMartData.HANDLE+
                    "&xml="+Json_Main.toString().replaceAll("\\\\","");
                    //"&xml="+URLEncoder.encode(Json_Main.toString(),"UTF-8");*/

            try {
            url_addItemToserver = AnyMartData.MAIN_URL+
                    AnyMartData.METHOD_INSERT_ITEM_AGAINST_VENDOR+
                    "?sessionid="+ AnyMartData.SESSION_ID+
                    "&handler="+ AnyMartData.HANDLE+
                    //"&xml="+Json_Main.toString().replaceAll("\\\\","");
                "&xml="+URLEncoder.encode(Json_Main.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL urlAddItemToServer = new URL(url_addItemToserver);
                urlConnection = urlAddItemToServer.openConnection();

                /*URL url1 = new URL(AnyMartData.MAIN_URL+
                        AnyMartData.METHOD_INSERT_ITEM_AGAINST_VENDOR+
                        "?sessionid="+AnyMartData.SESSION_ID+
                        "&handler="+AnyMartData.HANDLE+
                        //"&xml="+Json_Main.toString().replaceAll("\\\\","");
                        "&xml="+URLEncoder.encode(Json_Main.toString(),"UTF-8"));

                urlConnection = url1.openConnection();*/

               /* urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");*/
              //  urlConnection.setRequestProperty("Accept", "JSON");


           /*     //HttpURLConnection httpURLConnection = (HttpURLConnection)urlAddItemToServer.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.connect();
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"), 8);
*/
                 bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_addItemToserver = new StringBuffer();
                String line;
                StringBuilder str_build_addItemToserver = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_addItemToserver = stringBuff_addItemToserver.append(line);
                }
                responseString = stringBuff_addItemToserver.toString().replaceAll("^\"|\"$", "");
                resp_addItemToserver = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_addItemToserver);

            } catch (Exception e) {
                resp_addItemToserver = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dismissProgressDialog();
            if (!resp_addItemToserver.equalsIgnoreCase("error")) {
                if (!(xmlPO == null)) {
                    if (cd.isConnectingToInternet()) {
                        if ((AnyMartData.SESSION_ID != null)
                                && (AnyMartData.HANDLE != null)) {
                            new AddPOToServer().execute();
                        } else {
                            new StartSession_tbuds(PurchaceActivity.this, new CallbackInterface() {

                                @Override
                                public void callMethod() {
                                    new AddPOToServer().execute();
                                }

                                @Override
                                public void callfailMethod(String s) {

                                }
                            });
                        }
                    }
                }
            }
        }
    }

    class AddPOToServer extends AsyncTask<Void, Void, Void> {
        String responseString = null;
        String resp_addPOToserver = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String url_addPOToserver = null;

            url_addPOToserver = AnyMartData.MAIN_URL + AnyMartData.METHOD_INSERT_PO_AGAINST_VENDOR +
                    "?sessionid="+ AnyMartData.SESSION_ID +
                    "&handler="+ AnyMartData.HANDLE+
                    "&xml="+Json_Main_xmlPO.toString().replaceAll("\\\\","");
                    //"&xml="+ URLEncoder.encode(Json_Main_xmlPO.toString(),"UTF-8");

           /* try {
                url_addPOToserver = AnyMartData.MAIN_URL + AnyMartData.METHOD_INSERT_PO_AGAINST_VENDOR +
                        "?sessionid="+AnyMartData.SESSION_ID +
                        "&handler="+AnyMartData.HANDLE+
                        //"&xml="+Json_Main_xmlPO.toString().replaceAll("\\\\","");
                       "&xml="+ URLEncoder.encode(Json_Main_xmlPO.toString(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                URL urlAddPOToserver = new URL(url_addPOToserver);
                urlConnection = urlAddPOToserver.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "JSON");
                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "utf-8"), 8);

                StringBuffer stringBuff_addPOToserver = new StringBuffer();
                String line;
                StringBuilder str_build_addPOToserver = new StringBuilder();
                while((line = bufferedReader.readLine())!=null){
                    stringBuff_addPOToserver = stringBuff_addPOToserver.append(line);
                }
                responseString = stringBuff_addPOToserver.toString().replaceAll("^\"|\"$", "");
                resp_addPOToserver = responseString.replaceAll("\\\\","");

                Log.e("Response", resp_addPOToserver);

            } catch (Exception e) {
                resp_addPOToserver = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // dismissProgressDialog();
            if (!resp_addPOToserver.equalsIgnoreCase("error")) {
                Toast.makeText(getApplicationContext(), "Added successfully..", Toast.LENGTH_LONG).show();
                /*Intent intent = new Intent(PurchaceActivity.this, MainActivity.class);
                startActivity(intent);*/
                Intent intent = new Intent(PurchaceActivity.this, Sales_HomeSActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public static void clearTable(Context parent, String tablename) {
     //   DatabaseHandler db = new DatabaseHandler(parent);
     //   SQLiteDatabase sql = db.getWritableDatabase();
        sql_db.delete(tablename, null, null);

        /*sql.close();
        db.close();*/
    }

    private void createXml() {
       /* xml1 = "<Header>";
        xml1 += "<Vendor>" + edtVendor.getText().toString().trim() + "</Vendor>";
        xml1 += "<Total>" + txtTotal.getText().toString().trim() + "</Total>";
        xml1 += "<DiscountTotal>" + edttxtTotalDiscount.getText().toString().trim() + "</DiscountTotal>";
        xml1 += "<FinalTotal>" + txtFinalTotal.getText().toString().trim() + "</FinalTotal>";

        xml1 += "</Header>";*/


        try {

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Vendor", edtVendor.getText().toString().trim());
            jsonObject.put("Total", txtTotal.getText().toString().trim());
            jsonObject.put("DiscountTotal", edttxtTotalDiscount.getText().toString().trim());
            jsonObject.put("FinalTotal", txtFinalTotal.getText().toString().trim());


            xml1 = jsonObject.toString();
            sb.setLength(0);

            // sb.append("<Detail>");
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < myCartBeanArrayList.size(); i++) {
           /* sb.append("<Table>");
            sb.append("<Itemname>" + myCartBeanArrayList.get(i).getProduct_name() + "</Itemname>");
            sb.append("<Qty>" + myCartBeanArrayList.get(i).getQnty() + "</Qty>");
            sb.append("<Unit>" + myCartBeanArrayList.get(i).getUNIT()
                    + "</Unit>");
            sb.append("<rate>" + myCartBeanArrayList.get(i).getPrice()
                    + "</rate>");
            sb.append("<Disc>" + myCartBeanArrayList.get(i).getDISCOUNT()
                    + "</Disc>");
            sb.append("<Amount>" + myCartBeanArrayList.get(i).getAmount()
                    + "</Amount>");

            sb.append("</Table>");
            sb.append("</Detail>");*/

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("Itemname", myCartBeanArrayList.get(i).getProduct_name());
                jsonObject1.put("Qty", myCartBeanArrayList.get(i).getQnty());
                jsonObject1.put("Unit", myCartBeanArrayList.get(i).getUNIT());
                jsonObject1.put("rate", myCartBeanArrayList.get(i).getPrice());
                jsonObject1.put("Disc", myCartBeanArrayList.get(i).getDISCOUNT());
                jsonObject1.put("Amount", myCartBeanArrayList.get(i).getAmount());

                jsonArray.put(jsonObject1);

                //xml2 = sb.toString();
                xml2 = jsonArray.toString();

                jsonMain = new JSONObject();
                jsonMain.put("HeaderData", jsonObject);
                jsonMain.put("ItemData", jsonArray);
                AnyMartData.JMain = jsonMain;

                String custMob = null;
                String custName = null;

               /* tcf.addBill_two(myCartBeanArrayList.get(i).getProduct_name(),
                        myCartBeanArrayList.get(i).getProduct_id(),
                        myCartBeanArrayList.get(i).getPrice(),
                        String.valueOf(myCartBeanArrayList.get(i).getQnty()),
                        Float.parseFloat(myCartBeanArrayList.get(i).getDISCOUNT()),
                        myCartBeanArrayList.get(i).getAmount(), txtTotal.getText().toString().trim(),
                        edttxtTotalDiscount.getText().toString().trim(), txtFinalTotal.getText().toString().trim(),
                        edttxtReceived.getText().toString().trim(), edttxtRemaining.getText().toString().trim(),
                        custName, custMob,  xml1, xml2, "No");*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        // TODO Auto-generated method stub

        if (txtTotal.getText().toString().equalsIgnoreCase("00.0")) {
            Toast.makeText(PurchaceActivity.this, "Fill all amount", Toast.LENGTH_LONG).show();

            return false;
        } else if (edttxtTotalDiscount.getText().toString().trim().equalsIgnoreCase("")) {
            edttxtTotalDiscount.setText("0");
            return true;
        } else {
            return true;
        }
    }

    private void addView(int i) {
        final int pos = i;
        LayoutInflater layoutInflater = (LayoutInflater) parent
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.tbuds_custom_purchase_item_list_cb,
                null);
        myAutoComplete = (CustomAutoCompleteView) baseView.findViewById(R.id.myautocomplete_purchace);
        edRate = (EditText) baseView.findViewById(R.id.edRate_purchace);
        btn_ok = (ImageButton) baseView.findViewById(R.id.btn_ok_purchace);
        edAmt = (EditText) baseView.findViewById(R.id.edAmt_purchace);
        edQnty = (EditText) baseView.findViewById(R.id.edQnty_purchace);
        btn_cancel = (ImageButton) baseView.findViewById(R.id.btn_cancel_purchace);
        spinnerunit = (Spinner) baseView.findViewById(R.id.spinnerunit_purchace);

        myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener_Purchase(this));

        AllCatSubcatItems myObject = null;
        AllCatSubcatItems[] ObjectItemData = new AllCatSubcatItems[1];
        myObject = new AllCatSubcatItems();
        ObjectItemData[0] = myObject;

        myAdapter = new AutocompleteCustomArrayAdapter_Purchase(this, R.layout.tbuds_list_view_row_item, ObjectItemData);
        myAutoComplete.setAdapter(myAdapter);

        int textLength = edQnty.getText().length();
        edQnty.setSelection(textLength, textLength);

        int textLength1 = myAutoComplete.getText().length();
        myAutoComplete.setSelection(textLength1, textLength1);
        myAutoComplete.setFocusable(true);

        if (tcf.getPurchaseItems_AgainstVendor(vendor)
                > 0
                && i < tcf.getPurchaseItems_AgainstVendor(vendor)
                ) {
            getDataFromDataBase();
            if (i < myCartBeanArrayList.size()) {
                myAutoComplete.setText("" + myCartBeanArrayList.get(i).getProduct_name());
                myAutoComplete.setFocusable(false);
                myAutoComplete.setClickable(false);
                myAutoComplete.setEnabled(false);

                edRate.setText("" + myCartBeanArrayList.get(i).getPrice());
                edRate.setFocusable(false);
                edRate.setClickable(false);
                edRate.setEnabled(false);

                edAmt.setText("" + myCartBeanArrayList.get(i).getAmount());
                edAmt.setFocusable(false);
                edAmt.setClickable(false);
                edAmt.setEnabled(false);

                edQnty.setText("" + myCartBeanArrayList.get(i).getQnty());
                edQnty.setFocusable(false);
                edQnty.setClickable(false);
                edQnty.setEnabled(false);


                int spinner_pos = 0;
                if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("gm")) {
                    spinner_pos = 1;
                } else if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("kg")) {
                    spinner_pos = 2;
                } else if (myCartBeanArrayList.get(i).getUNIT().equalsIgnoreCase("item")) {
                    spinner_pos = 3;
                }
                spinnerunit.setSelection(spinner_pos);
                btn_cancel.setVisibility(VISIBLE);
            }
        }
        setListeners(i);

        containerLayout_one.addView(baseView);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void getDataFromDataBase() {
        // TODO Auto-generated method stub
        myCartBeanArrayList.clear();
    //    DatabaseHandler db1 = new DatabaseHandler(parent);
    //    SQLiteDatabase db = db1.getWritableDatabase();
        Json_Main = AnyMartData.JMain;
        amt = 0;

        Cursor c11 = sql_db.rawQuery("Select distinct vendor,qnty," +
                "price,Product_name,Amount,Product_id,  " +
                "DISCOUNT,UNIT from "
                + dbhandler.TABLE_PURCHASE_ITEM_CB +
                "  where vendor ='" + vendor + "'", null);
        if (c11.getCount() > 0) {
            c11.moveToFirst();
            do {
                String qnty = c11.getString(c11.getColumnIndex("qnty"));
                String vendor = c11.getString(c11.getColumnIndex("vendor"));
                String price = c11.getString(c11.getColumnIndex("price"));
                String Product_name = c11.getString(c11.getColumnIndex("Product_name"));
                String Amount = c11.getString(c11.getColumnIndex("Amount"));
                String Product_id = c11.getString(c11.getColumnIndex("Product_id"));
                String DISCOUNT = c11.getString(c11.getColumnIndex("DISCOUNT"));
                String UNIT = c11.getString(c11.getColumnIndex("UNIT"));


                amt = amt + Float.parseFloat(Amount);

                myCartBean = new MyCartBean();
                myCartBean.setMerchantName(vendor);
                myCartBean.setQnty(Float.valueOf(qnty));
                myCartBean.setPrice(Float.valueOf(price));
                myCartBean.setProduct_name(Product_name);
                myCartBean.setAmount(Float.valueOf(Amount));
                myCartBean.setProduct_id(Product_id);
                if (DISCOUNT.equalsIgnoreCase("")) {
                    myCartBean.setDISCOUNT("0");
                } else {
                    myCartBean.setDISCOUNT(DISCOUNT);
                }
                myCartBean.setUNIT(UNIT);
                myCartBeanArrayList.add(myCartBean);
            } while (c11.moveToNext());
        }

        txtTotal.setText("" + amt );
        txtFinalTotal.setText("" + amt );

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void initialize() {
        parent = PurchaceActivity.this;

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        containerLayout_one = (LinearLayout) findViewById(R.id.linearLayoutCordinator);
        savebill = (Button) findViewById(R.id.savebill_purchase);
        myCartBeanArrayList = new ArrayList<MyCartBean>();
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtFinalTotal = (TextView) findViewById(R.id.txtFinalTotal);
        edtVendor = (EditText) findViewById(R.id.edtVendor);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(PurchaceActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(this);
        String dabasename = ut.getValue(this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(this, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);

        if(edtVendor.getText().toString().isEmpty())
        {
            edtVendor.setError("This field should not be blank");
        }else {

        }
        edttxtTotalDiscount = (EditText) findViewById(R.id.edttxtTotalDiscount);
    }


    private void setListeners(final int i) {

        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

           //     SQLiteDatabase db = databaseHandler.getWritableDatabase();
                RelativeLayout rl = (RelativeLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                myAutoComplete.setText(tv.getText().toString());

                String que = "SELECT ItemMasterId,ItemName FROM " + dbhandler.TABLE_MARCHANT_ITEM_RUNI +
                        " WHERE ItemName='" + myAutoComplete.getText().toString() + "'";
                Cursor cur = sql_db.rawQuery(que, null);
                if (cur.getCount() > 0) {
                    cur.moveToFirst();

                    p_id = cur.getString(cur.getColumnIndex("ItemMasterId"));

                }
            }
        });

        spinnerunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int textLengthamt = edAmt.getText().length();
        edAmt.setSelection(textLengthamt, textLengthamt);
        edAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

               /* if ((edtVendor.getText().toString().equalsIgnoreCase("")) ||
                        (myAutoComplete.getText().toString().equalsIgnoreCase("")) ||
                        (edQnty.getText().toString().equalsIgnoreCase("")) || (spinnerunit == null)) {

                    edAmt.setError("please enter Vendor and Item details");
                    edAmt.setClickable(false);
                    edAmt.setFocusable(false);

                } else if(!(edtVendor.getText().toString().equalsIgnoreCase("")) ||
                        !(myAutoComplete.getText().toString().equalsIgnoreCase("")) ||
                        !(edQnty.getText().toString().equalsIgnoreCase("")) || !(spinnerunit == null)) {
                    edAmt.setFocusable(true);
                    edAmt.setEnabled(true);
                    edAmt.setClickable(true);*/

                    if (((s.toString().trim() == "") || (s.toString() == null) || (s
                            .toString().length() == 0) || (s.toString().trim() == " "))) {

                    } else {
                        if (flag_israte == false) {

                            if (spinnerunit.getSelectedItem().equals("kg")) {

                                float subtotal = Float.parseFloat(edAmt.getText().toString())
                                        / Float.parseFloat(edQnty.getText().toString().trim());
                                edRate.setText(Math.round(subtotal) + "");//₹
                                edRate.setFocusable(false);
                                edRate.setClickable(false);
                                edRate.setEnabled(false);
                            } else if (spinnerunit.getSelectedItem().equals("gm")) {

                                float subtotal = Float.parseFloat(edAmt.getText().toString())
                                        / Float.parseFloat(edQnty.getText().toString().trim());
                                edRate.setText(Math.round(subtotal) + "");//₹
                                edRate.setFocusable(false);
                                edRate.setClickable(false);
                                edRate.setEnabled(false);
                            } else if (spinnerunit.getSelectedItem().equals("item")) {

                                float subtotal = Float.parseFloat(edAmt.getText().toString())
                                        / Float.parseFloat(edQnty.getText().toString().trim());
                                edRate.setText(Math.round(subtotal) + "");//₹
                                edRate.setFocusable(false);
                                edRate.setClickable(false);
                                edRate.setEnabled(false);
                            }
                        }
                    }

                }
            //}
        });

        edAmt.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
               /* if((edtVendor.getText().toString().equalsIgnoreCase("")) ||
                      (myAutoComplete.getText().toString().equalsIgnoreCase("")) ||
                        (edQnty.getText().toString().equalsIgnoreCase("")) || (spinnerunit == null)){

                    edAmt.setError("please enter Vendor and Item details");
                    edAmt.setClickable(false);
                    edAmt.setFocusable(false);

                }else {
                    edAmt.setFocusable(true);
                    edAmt.setEnabled(true);
                    edAmt.setClickable(true);*/

                    if (validate1()) {
                        if (hasFocus) {

                            if (edAmt.getText().toString().trim().equalsIgnoreCase("")) {

                            } else {
                                // Toast.makeText(getApplicationContext(), "got the focus on amt", Toast.LENGTH_LONG).show();
                                if (spinnerunit.getSelectedItem().equals("kg")) {

                                    /*float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            / Float.parseFloat(edQnty.getText().toString().trim());*/
                                    float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            * Float.parseFloat(edQnty.getText().toString().trim());

                                    edRate.setText(Math.round(subtotal) + "");//₹

                                    edRate.setClickable(false);
                                    edRate.setEnabled(false);

                                } else if (spinnerunit.getSelectedItem().equals("gm")) {

                                  /*  float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            / Float.parseFloat(edQnty.getText().toString().trim());*/
                                    float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            * Float.parseFloat(edQnty.getText().toString().trim());

                                    edRate.setText(Math.round(subtotal) + "");//₹

                                    edRate.setClickable(false);
                                    edRate.setEnabled(false);
                                } else if (spinnerunit.getSelectedItem().equals("item")) {

                                   /* float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            / Float.parseFloat(edQnty.getText().toString().trim());*/
                                    float subtotal = Float.parseFloat(edAmt.getText().toString())
                                            * Float.parseFloat(edQnty.getText().toString().trim());

                                    edRate.setText(Math.round(subtotal) + "");//₹

                                    edRate.setClickable(false);
                                    edRate.setEnabled(false);
                                }
                            }
                        }
                    }
                }
         //   }

        });


        int textLengthrate = edRate.getText().length();
        edRate.setSelection(textLengthrate, textLengthrate);
       /* edRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {

                } else {
                    if (flag_israte == false) {
                        flag_israte = true;


                        if (spinnerunit.getSelectedItem().equals("kg")) {

                            float subtotal = Float.parseFloat(edRate.getText().toString())
                                    * Float.parseFloat(edQnty.getText().toString().trim());
                            edAmt.setText(Math.round(subtotal) + "");//₹
                        } else if (spinnerunit.getSelectedItem().equals("gm")) {

                            float subtotal = Float.parseFloat(edRate.getText().toString())
                                    * Float.parseFloat(edQnty.getText().toString().trim());
                            edAmt.setText(Math.round(subtotal) + "");//₹
                        } else if (spinnerunit.getSelectedItem().equals("item")) {

                            float subtotal = Float.parseFloat(edRate.getText().toString())
                                    * Float.parseFloat(edQnty.getText().toString().trim());
                            edAmt.setText(Math.round(subtotal) + "");//₹


                        }

                    } else {
                        flag_israte = false;
                    }

                }
            }
        });*/
        edRate.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (validate1()) {
                    if (!hasFocus) {


                        if (edRate.getText().toString().trim().equalsIgnoreCase("")) {

                        } else {
                            //     Toast.makeText(getApplicationContext(), "got the focus on rate", Toast.LENGTH_LONG).show();
                            if (spinnerunit.getSelectedItem().equals("kg")) {

                                float subtotal = Float.parseFloat(edRate.getText().toString())
                                        * Float.parseFloat(edQnty.getText().toString().trim());
                                edAmt.setText(Math.round(subtotal) + "");//₹
                            } else if (spinnerunit.getSelectedItem().equals("gm")) {

                                float subtotal = Float.parseFloat(edRate.getText().toString())
                                        * Float.parseFloat(edQnty.getText().toString().trim());
                                edAmt.setText(Math.round(subtotal) + "");//₹
                            } else if (spinnerunit.getSelectedItem().equals("item")) {

                                float subtotal = Float.parseFloat(edRate.getText().toString())
                                        * Float.parseFloat(edQnty.getText().toString().trim());
                                edAmt.setText(Math.round(subtotal) + "");//₹


                            }
                        }

                    } else {
                      //  Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        int textLength = edQnty.getText().length();
        edQnty.setSelection(textLength, textLength);
        edQnty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {


                int pos = i;
                if (((s.toString().trim() == "") || (s.toString() == null) || (s
                        .toString().length() == 0) || (s.toString().trim() == " "))) {
                    //  edAmt.setText("0");
                } else {


                }
            }
        });

        edttxtTotalDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                double edDisc;
                String finaltotal = (txtTotal.getText().toString());
                disc = edttxtTotalDiscount.getText().toString();
                if (!disc.equalsIgnoreCase("") && disc != null && !disc.equalsIgnoreCase("Discount Amount")) {
                    edDisc = Integer.parseInt(disc);
                } else {
                    edDisc = 0;
                    txtFinalTotal.setText(finaltotal);
                }
                item_amt = txtTotal.getText().toString();
                String mrp = null;
                if (!item_amt.equalsIgnoreCase("")) {
                    mrp = (item_amt);
                }
                String a = edDisc + "";
                if (edDisc != 0 && !a.equalsIgnoreCase("")) {
                       double MRP= Double.parseDouble(mrp);
                       double Discount= Double.parseDouble(disc);
                                            double perValu = (MRP * Discount) / 100;
                        result = MRP-perValu;
                        String finalresult= String.valueOf(result);
                        txtFinalTotal.setText(finalresult + "");
                }else{
                    txtFinalTotal.setText(finaltotal);             }

            }
        });


        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // myAutoComplete.setFocusable(true);
                txtTotal.setText("" + amt );
                vendor = edtVendor.getText().toString().trim();
                if (edQnty.getText().toString().trim().length() > 0
                        && !(edQnty.getText().toString().trim().equalsIgnoreCase("0"))
                        && myAutoComplete.getText().toString().length() > 0
                        && !(vendor.equalsIgnoreCase(""))) {
                    Toast.makeText(PurchaceActivity.this, "Item added", Toast.LENGTH_LONG).show();
                    tcf.addPurchase(myAutoComplete.getText().toString().trim(), p_id,
                            edQnty.getText().toString().trim(),
                            "",
                            edAmt.getText().toString().trim(),
                            edRate.getText().toString().trim(), vendor,
                            spinnerunit.getSelectedItem().toString());
                    containerLayout_one.removeAllViews();
                    for (int i = 0; i < tcf.getPurchaseItems_AgainstVendor(vendor) + 1;
                         i++) {
                        addView(i);
                    }

                    amt = 0;
                    if (myCartBeanArrayList.size() > 0) {
                        for (int j = 0; j < myCartBeanArrayList.size(); j++) {
                            amt = amt + Float.parseFloat(String.valueOf(myCartBeanArrayList.get(j).getAmount()));

                        }
                    }
                    txtTotal.setText("" + amt  );
                    txtFinalTotal.setText("" + amt );

                } else {
                    Toast.makeText(PurchaceActivity.this, "Enter valid details", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = i;
                Float q = Float.valueOf(edQnty.getText().toString());
                if (pos <= myCartBeanArrayList.size()) {
                    if (!(myCartBeanArrayList.get(pos).getQnty().equals("0"))
                            && (myCartBeanArrayList.get(pos).getQnty().equals(" "))) {
                        Toast.makeText(PurchaceActivity.this, "Item deleted", Toast.LENGTH_LONG).show();

                     //   SQLiteDatabase db = databaseHandler.getWritableDatabase();
                        sql_db.execSQL("DELETE FROM " + dbhandler.TABLE_PURCHASE_ITEM_CB +
                                " WHERE vendor='" + vendor +
                                "' and Product_name='" + myCartBeanArrayList.get(pos).getProduct_name() + "'");
                      //  db.close();
                        containerLayout_one.removeAllViews();
                        for (int i = 0; i < tcf.getPurchaseItems_AgainstVendor(vendor)
                                + 1; i++) {
                            addView(i);
                        }
                    } else {
                        Toast.makeText(PurchaceActivity.this, "Enter Item details", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PurchaceActivity.this, "Enter Item details", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private boolean validate1() {
        // TODO Auto-generated method stub

        if (myAutoComplete.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(PurchaceActivity.this, "Enter Item Name", Toast.LENGTH_LONG).show();
            return false;

        } else if (edQnty.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(PurchaceActivity.this, "Enter Quantity", Toast.LENGTH_LONG).show();
            return false;

        } else if (spinnerunit.getSelectedItem().equals("Select unit")) {
            Toast.makeText(PurchaceActivity.this, "Select Unit", Toast.LENGTH_LONG).show();
            return false;

        } else {
            return true;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
