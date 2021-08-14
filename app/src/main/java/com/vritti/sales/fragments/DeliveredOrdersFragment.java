package com.vritti.sales.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.OpenOrderAdapter;
import com.vritti.sales.beans.OrderHistoryAPIClass;
import com.vritti.sales.beans.OrderHistoryBean;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONObject;

import java.util.ArrayList;

public class DeliveredOrdersFragment extends Fragment {
    private static Context parent;
    ListView listview_my_orders_history;
    static ProgressHUD progress;
    TextView txtnoordnote;

    private static DatabaseHandlers databaseHelper;
    static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    String Reason = "Cancel this order";
    private String image_URL;
    Dialog dialog;
    static String res = "";
    static OrderHistoryBean bean;
    private static String DateToStr;
    private static String OrdRCVDt_ModifiedDt;
    int scrollToPos = 0;
    String numTomakeCall="";

    static ArrayList<OrderHistoryBean> arrayList;
    static OpenOrderAdapter myOrderHistoryAdapter;
    AlertDialog.Builder dialogBuilder;
    JSONObject jsonPay;
    private String FinalJson;
    String merchNameToPay = "",merchidToPay="", sohdrIdToPay="",MerchNameToPay = "";
    String paySTATUS="41", PaymentMode="",TransactionId="";
    String appCallFrom="";

    OrderHistoryAPIClass ordClass;

    @SuppressLint("ValidFragment")
    public DeliveredOrdersFragment(String appName) {
        appCallFrom = appName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_open_orders_fragment);
    }

    public DeliveredOrdersFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parent = getActivity();

        View view = inflater.inflate(R.layout.sales_activity_open_orders_fragment, container, false);
        listview_my_orders_history = view.findViewById(R.id.listview_my_orders_history);
        txtnoordnote = view.findViewById(R.id.txtnoordnote);

        databaseHelper = new DatabaseHandlers(parent, AnyMartDatabaseConstants.DATABASE__NAME_URL);
        sql = databaseHelper.getWritableDatabase();

        arrayList = new ArrayList<OrderHistoryBean>();

        ordClass = new OrderHistoryAPIClass(parent);

        getDataFromServer();

        setListener();

        return view;
    }

    public void setListener(){

        listview_my_orders_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View view, final int position, long id) {

                scrollToPos = position;

                String OrderID = arrayList.get(position).getSOHeaderId();
                String status = arrayList.get(position).getStatus();
                String statusname = arrayList.get(position).getStatusname();
                String SalesHeaderId = arrayList.get(position).getSalesHeaderId();
                String ShipStatus = arrayList.get(position).getShipstatus();
                String TotalAmt = String.format("%.2f",arrayList.get(position).getNetAmt());
                String sono = arrayList.get(position).getSONo();
                String address = arrayList.get(position).getAddress();
                String UPI = arrayList.get(position).getUPIMerch();
                String Merchantname = arrayList.get(position).getMerchantname();
                String numTomakeCall = arrayList.get(position).getMerchant_Mobile();
                String MerchAddress = arrayList.get(position).getMerchAddress();
                String MerchId = arrayList.get(position).getMerchantid();
                String freedelamt = arrayList.get(position).getFreeAboveAmt();
                String delstatus = arrayList.get(position).getDeliveryTerms();
                String merchcustdist = arrayList.get(position).getDistance();
                String perkmchrg = arrayList.get(position).getMinDelyKm();
                String maxfreedeldist = arrayList.get(position).getFreeDelyMaxDist();
                String IsDelivery = arrayList.get(position).getIsDelivery();

              /*  Intent intent_go = new Intent(parent, OrderDetailsActivity.class);
                intent_go.putExtra("OrderID", OrderID);
                intent_go.putExtra("status",status);
                intent_go.putExtra("statusname", statusname);
                intent_go.putExtra("SOHeaderID", OrderID);
                intent_go.putExtra("ShipStatus",ShipStatus);
                intent_go.putExtra("TotalAmt",TotalAmt);
                intent_go.putExtra("OrderNumber",sono);
                intent_go.putExtra("DelvryAddress",address);
                intent_go.putExtra("upi",UPI);
                intent_go.putExtra("merhname",Merchantname);
                intent_go.putExtra("numTomakeCall",numTomakeCall);
                intent_go.putExtra("MerchAddress",MerchAddress);
                intent_go.putExtra("MerchId",MerchId);
                intent_go.putExtra("FreedelAmt",freedelamt);
                intent_go.putExtra("DeliveryStatus",delstatus);
                intent_go.putExtra("Distance",merchcustdist);
                intent_go.putExtra("ValPerKm",perkmchrg);
                intent_go.putExtra("MaxDistLimitFreeDel",maxfreedeldist);
                intent_go.putExtra("IsDelivery",IsDelivery);

                //intent_go.putExtra("SalesHeaderId", SalesHeaderId);
                startActivity(intent_go);*/
                //overridePendingTransition(R.anim.enter_right_to_left,R.anim.exit_left_to_right);
            }
        });
    }

    public void getDataFromServer() {
      //  GetOrderHistoryClass.getDataFromServer("OpenOrdersFragment",getContext());

        try{
            progress = ProgressHUD.show(parent,""+parent.getResources().getString(R.string.loading),
                    false,true, null);
            progress.setCanceledOnTouchOutside(true);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (NetworkUtils.isNetworkAvailable(parent)) {
            new StartSession_tbuds(parent, new CallbackInterface() {

                @Override
                public void callMethod() {

                    if(AnyMartData.SENDER_ID != "" || AnyMartData.SENDER_ID != null){
                        if((AnyMartData.HANDLE != "" || AnyMartData.HANDLE != null)){
                            arrayList = ordClass.getOrderHistory("40");
                            if(arrayList.size()>0){
                                myOrderHistoryAdapter = new OpenOrderAdapter(getActivity(), arrayList, DeliveredOrdersFragment.this,appCallFrom);
                                listview_my_orders_history.setAdapter(myOrderHistoryAdapter);
                                myOrderHistoryAdapter.notifyDataSetChanged();
                            }
                        }else {
                        }
                    }
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        } else {
        }



        try{
           progress.dismiss();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setListToAdapter(ArrayList<OrderHistoryBean> arrayList){
        ArrayList<OrderHistoryBean> arList = new ArrayList<OrderHistoryBean>();

        arList = arrayList;

       // getDataFromDatabase();

        myOrderHistoryAdapter = new OpenOrderAdapter(parent, arList, DeliveredOrdersFragment.this,appCallFrom);
        listview_my_orders_history.setAdapter(myOrderHistoryAdapter);

    }

    /*class AsyncTaskDeleteItems extends AsyncTask<String, Void, Void> {
        String responseString = "";
        String resp_deleteOrder = "";
        String sohdrId = "",posToRemove = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try{
                progress = ProgressHUD.show(parent,""+parent.getResources().getString(R.string.ord_cancel_request),
                        false,true, null);
                progress.setCanceledOnTouchOutside(true);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected Void doInBackground(String... params) {
            sohdrId = params[0];
            posToRemove = params[1];

            String url_deleteOrder = AnyMartData.MAIN_URL + AnyMartData.METHOD_DELETE_ORDER +
                    "?SOHeaderId="+params[0]+
                    "&Reason="+Reason+
                    "&handler="+ AnyMartData.HANDLE +
                    "&sessionid="+ AnyMartData.SESSION_ID ;

            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_deleteOrder, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);
                Log.e("Response", responseString);

                responseString = res.toString().replaceAll("^\"|\"$","")+ ","+params[0]+","+ params[1];
                resp_deleteOrder = responseString.replaceAll("\\\\","");

            } catch (Exception e) {
                resp_deleteOrder = "Error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try{
                progress.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }

            if (res.equalsIgnoreCase("Error")) {

            }else if(res.contains("ExceptionMessage")) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent,""+parent.getResources().getString(R.string.ord_canot_deleted),Toast.LENGTH_LONG).show();
                    }
                });

            }else if (res.equalsIgnoreCase("The order cannot be deleted")) {
                //ord_canot_deleted
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent,""+parent.getResources().getString(R.string.ord_canot_deleted),Toast.LENGTH_LONG).show();
                    }
                });

            } else if(res.equalsIgnoreCase("Cancelled")) {

                arrayList.remove(posToRemove);
                myOrderHistoryAdapter.notifyDataSetChanged();

                updateSo(sohdrId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(parent, ""+getResources().getString(R.string.your_ordnum)+" "
                                +getResources().getString(R.string.has_been_cancelled), Toast.LENGTH_LONG).show();
                    }
                });

                getDataFromDatabase();

            }
        }
    }*/

    public void updateSo(String soheaderid){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values1 = new ContentValues();
        values1.put("status","90");
        values1.put("statusname","ShortClosed" );

        db.update(AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY,
         values1, "SOHeaderId=?", new String[]{soheaderid});
    }

    public void HighLightGrid(View view){
        // Initialize a new color drawable array
        int mGridViewBGColor = Color.parseColor("#87b2d3");
        ColorDrawable[] colors = {
                new ColorDrawable(mGridViewBGColor), // Animation starting color
                new ColorDrawable(mGridViewBGColor) // Animation ending color
        };

        // Initialize a new transition drawable instance
        TransitionDrawable transitionDrawable = new TransitionDrawable(colors);

        // Set the clicked item background
        view.setBackground(transitionDrawable);

        // Finally, Run the item background color animation
        // This is the grid view item click effect
        transitionDrawable.startTransition(100); //600 Milliseconds
    }

    public  void MakeCall(String mobile){
        numTomakeCall = mobile;

        try{
            if (ActivityCompat.checkSelfPermission(DeliveredOrdersFragment.this.getActivity(), Manifest.permission.CALL_PHONE) ==
                    PackageManager.PERMISSION_GRANTED) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+91"+mobile));
                startActivity(callIntent);
            }
            else
            {
                ActivityCompat.requestPermissions(DeliveredOrdersFragment.this.getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*public void payToMerchant(String UPI, String MerchantName, String TotalAmt,String merchId, String sohdrId){

        final Dialog myDialog = new Dialog(parent);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.payment_options);
        myDialog.setCancelable(true);

        final LinearLayout lay_cod = myDialog.findViewById(R.id.lay_cod);
        final LinearLayout txt_payment = myDialog.findViewById(R.id.txt_payment); //itemname
        final ImageView imgcncl = myDialog.findViewById(R.id.imgcncl);

        if (UPI.equals("")){
            lay_cod.setVisibility(View.VISIBLE);
            txt_payment.setAlpha((float)0.5);
        }else {
            lay_cod.setVisibility(View.VISIBLE);
            txt_payment.setVisibility(View.VISIBLE);
            txt_payment.setAlpha((float)1);
        }

        imgcncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        lay_cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sohdrIdToPay = sohdrId;
                merchidToPay = merchId;

                paySTATUS="42";
                PaymentMode = "COD";

                createPaymentJSON("COD",paySTATUS,PaymentMode);

                myDialog.dismiss();
            }
        });

        txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sohdrIdToPay = sohdrId;
                merchidToPay = merchId;
                PaymentMode = "UPI";

                String transactionNote="Product purchase";
                String currencyUnit="INR";

                int randomPIN = (int)(Math.random()*9000)+1000;
                String val = ""+randomPIN;

                Uri uri = null;
                try {
                    uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchantName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+TotalAmt+"&cu="+currencyUnit+"&tr="+val);
                  *//*  uri = Uri.parse("upi://pay?pa="+UPI+"&pn="+ URLEncoder.encode(MerchantName,"UTF-8")
                            +"&tn="+ URLEncoder.encode(transactionNote,"UTF-8")+
                            "&am="+"1"+"&cu="+currencyUnit+"&tr="+val);*//*
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Log.d("Order", "onClick: uri: "+uri);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivityForResult(intent,1);

                myDialog.dismiss();
            }
        });

        myDialog.show();

    }

    public void openPopup(String status,String paySTATUS){
        dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.paymen_status_dialog, null);
        dialogBuilder.setView(dialogView);dialogBuilder.setCancelable(true);

        // set the custom dialog components - text, image and button
        final LinearLayout psuccess =  dialogView.findViewById(R.id.psuccess);
        psuccess.setVisibility(View.VISIBLE);
        final LinearLayout pfail =  dialogView.findViewById(R.id.pfail);
        pfail.setVisibility(View.GONE);
        final ImageView gifimgsuccess =  dialogView.findViewById(R.id.gifimgsuccess);
        final Button btntryagain =  dialogView.findViewById(R.id.btntryagain);
        btntryagain.setVisibility(View.GONE);

       *//* GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifimgsuccess);
        Glide.with(this).load(R.raw.paymentsuccessfulgif).into(imageViewTarget);*//*

        *//*Glide.with(this)
                .load(R.raw.paymentsuccessfulgif)
                .into(gifimgsuccess);*//*

        if(status.equalsIgnoreCase("SUCCESS")){
            psuccess.setVisibility(View.VISIBLE);
            pfail.setVisibility(View.GONE);
        }else {
            psuccess.setVisibility(View.GONE);
            pfail.setVisibility(View.VISIBLE);
        }

        dialogBuilder.setCancelable(true);
        final AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void createPaymentJSON(String txnId,String paySTATUS,String payMode){
        *//*{
        "TransactionId": "UPI608f070ee644467aa78d1ccf5c9ce39b",
                "PaymentStatus": "Paid",        --42 for Paid, 41 for Unpaid
                "TransactionDate": "2020-07-20",
                "SOHeaderId": "8cfa9b9a-3cd6-4187-9305-457df103a82a",
                "MerchantId": "7fa13696-8af5-4ec9-b4ef-6c26048c935f",
                "CustomerMasterId": "f1f082e7-f2d0-4f18-9a0a-516bbe7a8d27"
    }*//*
        TransactionId=txnId;

        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd"); //Feb 23 2016 12:16PM
        // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
        String _txnDate = format.format(curDate);
        System.out.println(_txnDate);

        jsonPay = new JSONObject();
        try {
            jsonPay.put("TransactionId",txnId);
            jsonPay.put("PaymentStatus",paySTATUS);
            jsonPay.put("TransactionDate",_txnDate);
            jsonPay.put("SOHeaderId",sohdrIdToPay);
            jsonPay.put("MerchantId",merchidToPay);
            jsonPay.put("CustomerMasterId",AnyMartData.USER_ID);
            jsonPay.put("PaymentMode",payMode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FinalJson=jsonPay.toString();

        sendPaymentStatusToServer();

    }

    public void sendPaymentStatusToServer(){
        if (NetworkUtils.isNetworkAvailable(getContext())) {
            new StartSession(getContext(), new CallbackInterface() {
                @Override
                public void callMethod() {
                    new PostPaymentStatus().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }

            });
        }
    }

    class PostPaymentStatus extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;
        Object res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String url_authentication = AnyMartData.MAIN_URL + AnyMartData.api_postPaySTatus;

            //url_authentication = url_authentication.replaceAll(" ","%20");

            String auth = url_authentication;
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {

                res = Utility.OpenPostConnection(url_authentication,FinalJson.toString().replaceAll("^\"|\"$",""));
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);
                res = responseString;

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (responseString.contains("true")) {
                //data send to server
                Toast.makeText(parent,""+getResources().getString(R.string.paysubsuss),Toast.LENGTH_SHORT).show();

                //update paymentstatus from table
                ContentValues values1 = new ContentValues();
                values1.put("PaymentStatus","42");
                //values1.put("PaymentMode",PaymentMode);
                values1.put("TransactionId",TransactionId);
                sql.update(AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY, values1, "SOHeaderId=?", new String[]{sohdrIdToPay});

            } else if (responseString.contains("false")) {
                //data not updated to server
                Toast.makeText(parent,""+getResources().getString(R.string.paysubfail),Toast.LENGTH_SHORT).show();
                //update paymentstatus from table
                ContentValues values1 = new ContentValues();
                values1.put("PaymentStatus","41");
                //values1.put("PaymentMode",PaymentMode);
                values1.put("TransactionId",TransactionId);
                sql.update(AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY, values1, "SOHeaderId=?", new String[]{sohdrIdToPay});

            }else {

            }

            getDataFromDatabase();
        }
    }*/

    public void share(String sono, String amt, String status, String merchName, String merchMob, String merchAddr,
                      String merchLat, String merchLng, String sohdrId){

        String payment = "";
        if(status.equalsIgnoreCase("42")){
            payment = "Paid";
        }else {
            payment = "Unpaid";
        }

        ArrayList<String> itemlist = new ArrayList<String>();
        itemlist.clear();

        String qry = "Select ItemDesc,Content,UOMCode,Brand,OrgQty,Rate,Qty,mrp,range from "+AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY+"" +
                " WHERE SOHeaderId='"+sohdrId+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                String itmname = c.getString(c.getColumnIndex("ItemDesc"));
                String content = c.getString(c.getColumnIndex("Content"));
                String UOMCode = c.getString(c.getColumnIndex("UOMCode"));
                String Brand = c.getString(c.getColumnIndex("Brand"));
                String OrgQty = c.getString(c.getColumnIndex("OrgQty"));
                float Rate = Float.parseFloat(c.getString(c.getColumnIndex("Rate")));
                String Qty = c.getString(c.getColumnIndex("Qty"));
                float mrp = Float.parseFloat(c.getString(c.getColumnIndex("mrp")));
                String range = c.getString(c.getColumnIndex("range"));

                if(content.contains(".0")){
                    content = content.replace(".0","");
                }else {
                    content = content;
                }

                String product="";
                if(range.equalsIgnoreCase("True")){
                     product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+String.format("%.2f",mrp);
                }else {
                     product = Brand+" - "+itmname+", "+content+" "+UOMCode+" - "+Qty+" x "+String.format("%.2f",Rate);
                }

                itemlist.add(product);

            }while (c.moveToNext());
        }

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain"); /*image/*,*/
            // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Intent.EXTRA_SUBJECT, "Any Dukaan" /*AnyMartData.MODULE + "App"*/);
            //String msg = "\n Let me recommend you Any Dukaan application\n\n";

            //content to send
            String msg = getResources().getString(R.string.greetings);
            msg += "\n"+getResources().getString(R.string.ordno)+" - "+sono;
            msg += "\n"+getResources().getString(R.string.ordamt)+"  - "+amt+" ₹";
            msg += "\n"+getResources().getString(R.string.status)+" - "+payment;
            msg += "\n\n"+getResources().getString(R.string.merchdtl);
            msg += "\n"+getResources().getString(R.string.name)+" - "+merchName;
            msg += "\n"+getResources().getString(R.string.mobile)+" - "+merchMob;
            msg += "\n"+getResources().getString(R.string.address)+" - "+merchAddr;
            String mapaddr = "http://maps.google.com/maps?q=loc:" +merchLat+ "," + merchLng;
            msg += "\n"+mapaddr;
            msg += "\n\n"+getResources().getString(R.string.prod)+" : ";
           // msg += "\nAata 1kg - Premium Chapati";
            for (int k=0; k<itemlist.size(); k++){
                msg += "\n"+itemlist.get(k).toString().trim();
            }
            //msg += "\n\n*************************";

            String url1= "<a href= 'https://play.google.com/store/apps/details?id=com.vritti.freshmart'>https://play.google.com/store/apps/details?id=com.vritti.freshmart</a>";
            //Uri linkurl = Uri.parse(url1);
            Uri imageUri = Uri.parse("android.resource://" + getActivity().getPackageName()+ "/drawable/" + "anydukan_2");
        //    i.putExtra(Intent.EXTRA_TEXT, msg + "\n" + Html.fromHtml(url1));
            i.putExtra(Intent.EXTRA_TEXT, msg+"\n\n"+getResources().getString(R.string.clickhere)+"\n"+Html.fromHtml(url1));
            //i.putExtra(Intent.EXTRA_STREAM, imageUri);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(i, getResources().getString(R.string.choseone)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        if(requestCode==0 && ActivityCompat.checkSelfPermission(DeliveredOrdersFragment.this.getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent(Intent.ACTION_DIAL);
            String p = "tel:+91"+numTomakeCall;
            i.setData(Uri.parse(p));
            startActivity(i);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onResume() {
        super.onResume();

       // getDataFromDatabase();

       /* listview_my_orders_history.smoothScrollToPosition(scrollToPos);
        listview_my_orders_history.setSelection(scrollToPos);*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("TAG", "onActivityResult: requestCode: "+requestCode);
        Log.d("TAG", "onActivityResult: resultCode: "+resultCode);
        //txnId=UPI20b6226edaef4c139ed7cc38710095a3&responseCode=00&ApprovalRefNo=null&Status=SUCCESS&txnRef=undefined
        //txnId=UPI608f070ee644467aa78d1ccf5c9ce39b&responseCode=ZM&ApprovalRefNo=null&Status=FAILURE&txnRef=undefined

        if(data!=null) {
            Log.d("TAG", "onActivityResult: data: " + data.getStringExtra("response"));
            String res = data.getStringExtra("response");
            String TxnId = data.getStringExtra("txnId");
            String pStatus = data.getStringExtra("Status");
            String search = "SUCCESS";
            if (pStatus.equalsIgnoreCase(search)) {
                Toast.makeText(getContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
                //call API to send transactionid and status
                paySTATUS="42";

            //    createPaymentJSON(TxnId,paySTATUS,PaymentMode);

             //   openPopup("SUCCESS","42");

            } else {
                Toast.makeText(getContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
                paySTATUS="41";

             //   createPaymentJSON(TxnId,paySTATUS,PaymentMode);

            //    openPopup("FAILURE","41");
            }
        }
    }

}
