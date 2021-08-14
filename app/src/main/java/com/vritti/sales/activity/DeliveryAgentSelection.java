package com.vritti.sales.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.adapters.DeliveryAgentsListAdapter;
import com.vritti.sales.beans.DeliveryAgentBean;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class DeliveryAgentSelection extends AppCompatActivity {
    private Context parent;

    Toolbar toolbar;
    ProgressBar mprogress;
    ListView list_agents;

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    SharedPreferences sharedpreferences;
    ProgressBar progressBar;

    String usertype = "";
    String username = "";

    Button btnassign;
    SeekBar skbarfromtime, skbartotime;
    TextView txtinvoiceno, txtdelvdate, txtcustomer, txtaddress, seekBarValue_frm,seekBarValue_to ;
    String OrderType, OrderTypeMasterId, ConsigneeName, SONO_frmIntent, ShipToMasterId, InvoiceNo, DeliveryDate, DelvAddress, SODate,
            expDelDate, IntentFrom, prfDelFrmTime, prfDelToTime;

    ArrayList<DeliveryAgentBean> listAgents;
    ArrayList<DeliveryAgentBean> temp_listAgents = new ArrayList<>();
    DeliveryAgentsListAdapter agentAdapter;
 //   DeliveryAgentsListAdapter_RecyclerView agentAdapter;
    String[] agents = {"Test0","Test1","Test2","Test3","Test4","Test5"};
    JSONObject jMAin;
    String userMasterID, DelAgentLoginID, DelAgentName, DelAgentMobile, DelAgentEmail,
            AssignActivityDesc,Pref_DeliveryTime_From, Pref_DeliveryTime_To;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_delivery_agent_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Delivery Details");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        Intent intent = getIntent();
        IntentFrom = intent.getStringExtra("IntentFrom");
        InvoiceNo = intent.getStringExtra("InvoiceNo");
        DeliveryDate = intent.getStringExtra("DeliveryDate");
        ConsigneeName = intent.getStringExtra("ConsigneeName");
        DelvAddress = intent.getStringExtra("DelvAddress");
        SONO_frmIntent = intent.getStringExtra("SONO_frmIntent");
        OrderType = intent.getStringExtra("OrderType");
        ShipToMasterId = intent.getStringExtra("ShipToMasterId");
        SODate = intent.getStringExtra("SODate");
        OrderTypeMasterId = intent.getStringExtra("OrderTypeMasterId");
        prfDelFrmTime = intent.getStringExtra("prfDelFrmTime");
        prfDelToTime = intent.getStringExtra("prfDelToTime");

        Pref_DeliveryTime_From = prfDelFrmTime;
        Pref_DeliveryTime_To = prfDelToTime;

        try{
            String[] data1 = prfDelFrmTime.split(" ");
            String hours_ = data1[0].split(":")[0];
            // hours_ = String.format("%02d", hours_);
            String minutes_ = data1[0].split(":")[1];
            // minutes_ = String.format("%02d", minutes_);
            String AmPm = data1[1];

            int progress = 0, hours = 0;
            hours = Integer.valueOf(hours_);

            if(AmPm.equalsIgnoreCase("AM")){
                hours = hours;
            }else {
                hours = hours + 12;
            }
            progress = hours * 4;

            // progress = progress + (Integer.valueOf(minutes_) /(15));        //minutes = (progress % 4)*15;
            if(minutes_.equalsIgnoreCase("00")){
                progress = progress + 0;
            }else if(minutes_.equalsIgnoreCase("15")){
                progress = progress + 1;
            }else if(minutes_.equalsIgnoreCase("30")){
                progress = progress + 2;
            }else if(minutes_.equalsIgnoreCase("45")){
                progress = progress + 3;
            }
            skbarfromtime.setProgress(progress);
            seekBarValue_frm.setText("From  "+ hours +":"+ minutes_ + " " + AmPm + ":");
            Pref_DeliveryTime_From = String.valueOf(hours) +":"+ String.valueOf(minutes_)+ " "+AmPm;

            String[] data2 = prfDelToTime.split(" ");
            String hours_1 = data2[0].split(":")[0];
            String minutes_1 = data2[0].split(":")[1];
            String AmPm1 = data2[1];

            int hours2 = 0;
            hours2 = Integer.valueOf(hours_1);
            if(AmPm1.equalsIgnoreCase("AM")){
                hours2 = hours2;
            }else {
                hours2 = hours2 + 12;
            }
            progress = Integer.valueOf(hours2) * 4;

            // progress = progress + (Integer.valueOf(minutes_) /(15));        //minutes = (progress % 4)*15;
            if(minutes_.equalsIgnoreCase("00")){
                progress = progress + 0;
            }else if(minutes_.equalsIgnoreCase("15")){
                progress = progress + 1;
            }else if(minutes_.equalsIgnoreCase("30")){
                progress = progress + 2;
            }else if(minutes_.equalsIgnoreCase("45")){
                progress = progress + 3;
            }

            skbartotime.setProgress(progress);
            seekBarValue_to.setText("From  "+ hours2 +":"+ minutes_1 + " " + AmPm1 + ":");
            Pref_DeliveryTime_From = String.valueOf(hours2) +":"+ String.valueOf(minutes_1)+ " "+AmPm1;
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            expDelDate = SODate;

            txtinvoiceno.setText(InvoiceNo);
            txtdelvdate.setText(DeliveryDate);
            txtcustomer.setText(ConsigneeName);
            txtaddress.setText(DelvAddress);

        }catch (Exception e){
            e.printStackTrace();
        }
        //get delivery agents list
        new DownloadDeliveryAgents().execute();

        setListeners();
    }

    public void init(){
        parent = DeliveryAgentSelection.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle(R.string.app_name_toolbar_sales);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_Assgnwork);

        txtinvoiceno = (TextView)findViewById(R.id.txtinvoiceno);
        txtdelvdate = (TextView)findViewById(R.id.txtdelvdate);
        txtcustomer = (TextView)findViewById(R.id.txtcustomer);
        txtaddress = (TextView)findViewById(R.id.txtaddress);

        skbarfromtime = (SeekBar)findViewById(R.id.skbarfromtime);
        skbartotime = (SeekBar)findViewById(R.id.skbartotime);

        list_agents = (ListView)findViewById(R.id.list_agents);

         skbarfromtime.setMax(24 * 4);
        //seekBarValue_frm.setText("From 9:00");
        skbartotime.setMax(24 * 4);
        //seekBarValue_to.setText("From 18:00");

        btnassign = (Button)findViewById(R.id.btnassign);

        seekBarValue_frm = (TextView)findViewById(R.id.txt_time);
        seekBarValue_to = (TextView)findViewById(R.id.txt_time_to);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(DeliveryAgentSelection.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(parent, dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        MobileNo = ut.getValue(parent, WebUrlClass.GET_MOBILE_KEY, settingKey);
        AnyMartData.MerchantID = sharedpreferences.getString("MerchantID","");
        AnyMartData.MerchantName = sharedpreferences.getString("MerchantName","");
        AnyMartData.SHIPToAddr = sharedpreferences.getString("SHIPToAddr","");
        AnyMartData.SHIPTOMASTERID = sharedpreferences.getString("ShipToId","");
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");
        AnyMartData.CITY = sharedpreferences.getString("City","");
        AnyMartData.PINCODE = sharedpreferences.getString("Pincode","");
        AnyMartData.ADDRESS = sharedpreferences.getString("Address","");
        AnyMartData.selected_BSEGMENTDESC = sharedpreferences.getString("SelBSegDesc","");
        AnyMartData.selected_BSEGMENTCODE = sharedpreferences.getString("SelBSegCode","");
        AnyMartData.selected_BSEGMENTID = sharedpreferences.getString("SelBSegId","");
        AnyMartData.selected_MERCHID = sharedpreferences.getString("SelMerchId","");
        AnyMartData.SHOPBYMODE = sharedpreferences.getString("SHOPBYMODE","ShopBySpeciality");
        AnyMartData.STATE = sharedpreferences.getString("State","");
        AnyMartData.SpecImgPath = sharedpreferences.getString("SpecImgPath","");
        AnyMartData.USER_ID = sharedpreferences.getString("CustVendorMasterId","");

        AnyMartData.MODULE = "ORDERBILLING";
        AnyMartData.MOBILE = MobileNo;
        usertype = "C";

        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        AnyMartData.MAIN_URL = CompanyURL + "/api/OrderBillingAPI/";

        listAgents = new ArrayList<>();

    }

    public void setListeners(){

        skbarfromtime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
                int hours = progress / 4; // it will return hours.
                int minutes = (progress % 4) * 15; // here will be minutes.

               String hours_ = String.format("%02d", hours);
               String minutes_ = String.format("%02d", minutes);

                String AmPm = "";

                if(hours < 12){
                    AmPm = "AM";
                }else {
                    AmPm = "PM";
                }

               // String FromTime = "From  "+ String.valueOf(hours) +":"+ new DecimalFormat("##.##").format(String.valueOf(minutes)) + " " + AmPm + ":";

               //seekBarValue.setText(String.valueOf(progress));
                seekBarValue_frm.setText("From  "+ hours_ +":"+ minutes_ + " " + AmPm + ":");
              //  seekBarValue_frm.setText("From  "+ String.valueOf(hours) +":"+ String.valueOf(minutes) + " " + AmPm + ":");
                seekBarValue_frm.setTextColor(parent.getResources().getColor(R.color.colorPrimary1));
                Pref_DeliveryTime_From = String.valueOf(hours) +":"+ String.valueOf(minutes)+ " "+AmPm;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        skbartotime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged (SeekBar seekBar, int progress, boolean fromUser) {
                int hours = progress / 4; // it will return hours.
                int minutes = (progress % 4) * 15; // here will be minutes.

                String hours_ = String.format("%02d", hours);
                String minutes_ = String.format("%02d", minutes);

                String AmPm = "";

                if(hours < 12){
                    AmPm = "AM";
                }else {
                    AmPm = "PM";
                }

                //seekBarValue.setText(String.valueOf(progress));
                seekBarValue_to.setText("To  " + hours_ +":"+ minutes_ + " "+AmPm + ":");
              // seekBarValue_to.setText("To  " +String.valueOf(hours) +":"+ String.valueOf(minutes) + " "+AmPm + ":");
                seekBarValue_to.setTextColor(parent.getResources().getColor(R.color.colorPrimary1));
                Pref_DeliveryTime_To = String.valueOf(hours) +":"+ String.valueOf(minutes)+ " "+AmPm;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                temp_listAgents = DeliveryAgentsListAdapter.getAllSelectedAgentData();

                for(int i=0; i<temp_listAgents.size();i++){
                    if(temp_listAgents.get(i).isChecked()){
                        userMasterID = temp_listAgents.get(i).getUserMasterId();
                        DelAgentLoginID = temp_listAgents.get(i).getUserLoginId();
                        DelAgentName = temp_listAgents.get(i).getUserName();
                        DelAgentMobile = temp_listAgents.get(i).getMobile();
                        DelAgentEmail = temp_listAgents.get(i).getEmail();
                    }
                }

                //hit API to assign activity to delivery agent
                jMAin = new JSONObject();
                try {
                    jMAin.put("ShipmentNo",InvoiceNo);
                    jMAin.put("UserMasterId",userMasterID);
                    jMAin.put("ShiToMasterId",ShipToMasterId);
                    jMAin.put("SoDate",SODate);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AssignActivityDesc = InvoiceNo + " of " + ConsigneeName + "-" + DelAgentMobile +", " +
                        DelvAddress + ", assigned for delivery to " + DelAgentName +
                        ", within time ( " + Pref_DeliveryTime_From + " - " + Pref_DeliveryTime_To + " ) on "+ DeliveryDate;

                new AssignActivityToDeliveryAgents().execute();

            }
        });
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    class DownloadDeliveryAgents extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //       showProgressDialog();
            //  progressHUD2 = ProgressHUD.show(context, " ", false, false, null);
            //mprogress.setVisibility(View.VISIBLE);
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_DeliveryAgentDetails;
               // String url = "http://b207.ekatm.com/api/ShipmentEntryAPI/getDeliveryBoyDetails";

                res = ut.OpenConnection(url);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                    ContentValues values = new ContentValues();
                    jResults = new JSONArray(response);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            //parse it here and set to list and set list to adapter
            try{
                if(jResults != null){

                    for(int i=0; i<jResults.length();i++){
                        JSONObject jObj = jResults.getJSONObject(i);
                         UserMasterId = jObj.getString("UserMasterId");
                        String UserLoginId = jObj.getString("UserLoginId");
                        String UserName = jObj.getString("UserName");
                        String Email = jObj.getString("Email");
                        String Mobile = jObj.getString("Mobile");
                        String GPSDate = jObj.getString("GPSDate");
                        String LocationName = jObj.getString("LocationName");
                        String Latitude = jObj.getString("Latitude");
                        String Longitude = jObj.getString("Longitude");
                        String PendingShipments = jObj.getString("PendingShipments");

                        DeliveryAgentBean agentBean = new DeliveryAgentBean();
                        agentBean.setUserMasterId(UserMasterId);
                        agentBean.setUserLoginId(UserLoginId);
                        agentBean.setUserName(UserName);
                        agentBean.setEmail(Email);
                        agentBean.setMobile(Mobile);
                        agentBean.setGPSDate(GPSDate);
                        agentBean.setLocationName(LocationName);
                        agentBean.setLatitude(Latitude);
                        agentBean.setLongitude(Longitude);
                        agentBean.setPendingShipments(PendingShipments);

                        listAgents.add(agentBean);

                        Collections.sort(listAgents, new Comparator<DeliveryAgentBean>() {
                            @Override
                            public int compare(DeliveryAgentBean lhs, DeliveryAgentBean rhs) {
                                return lhs.getPendingShipments().compareTo(rhs.getPendingShipments());
                            }
                        });

                    }

                    agentAdapter = new DeliveryAgentsListAdapter(parent, listAgents);
                    list_agents.setAdapter(agentAdapter);
                    list_agents.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                   /* agentAdapter = new DeliveryAgentsListAdapter_RecyclerView(parent, listAgents);
                    list_agents.setAdapter(agentAdapter);*/

                }else {
                    Toast.makeText(parent,"No Data found", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class AssignActivityToDeliveryAgents extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        JSONArray jResults;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                if(IntentFrom.equals("ShipmentEntry")){
                    try{
                        SODate = Convertdate(SODate);
                    }catch (Exception e){e.printStackTrace();}
                }

                SODate = SODate.replace(" " , "%20");
                SODate = SODate.replace("/" , "%2F");
                SODate = SODate.replace(":" , "%3A");

                String url1 = CompanyURL + WebUrlClass.api_ShipmentEntryAssignActivityAPI +
                         "?ShipmentNo=" +InvoiceNo + "&UserMasterId=" +userMasterID + "&ShiToMasterId="+ShipToMasterId + "&SoDate="+SODate;

                String url = CompanyURL + WebUrlClass.api_ShipmentEntryAssignActivityAPI;

                url1 = url1.replace(" " , "%20");
                res = ut.OpenPostConnection_test(url1, "", parent);

                if (res != null) {
                    response = res.toString().replaceAll("\\\\", "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            //  mprogress.setVisibility(View.GONE);
            hideProgress();

            //parse it here and set to list and set list to adapter
            try{
                if(response == "" || response == null){
                    Toast.makeText(parent,"Activity assigned successfully", Toast.LENGTH_SHORT).show();

                    //store assigned activity

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM YYYY, hh:mm a");
                    String actAssignedDate = sdf.format(c.getTime());

                    tcf.insertAssignedActivity(InvoiceNo, userMasterID, DelAgentLoginID, DelAgentName, DelAgentMobile, DelAgentEmail,
                            AssignActivityDesc,SONO_frmIntent,expDelDate,ConsigneeName,DelvAddress,Pref_DeliveryTime_From, Pref_DeliveryTime_To,
                            actAssignedDate, OrderTypeMasterId);

                    Toast.makeText(parent,"Activity data inserted successfully", Toast.LENGTH_SHORT).show();

                   // sql.execSQL("UPDATE Shipment_Invoice SET isActivityAssigned = 'Y' WHERE InvoiceNo='" + InvoiceNo + "'" );
                    updateTable();

                    if(IntentFrom.equalsIgnoreCase("ShipmentEntry")){
                        Intent i = new Intent(parent, ShipmentAndInvoicing.class);
                        startActivity(i);
                        finish();
                    }else if(IntentFrom.equalsIgnoreCase("PendingDelivery")){
                        Intent i = new Intent(parent, PendingDeliveries.class);
                        startActivity(i);
                        finish();
                    }

                }else {
                    Toast.makeText(parent,"Unable to assign activity", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*Intent i = new Intent(this, MainActivity.class);
        i.putExtra("intentFrom", "ShipmentComplete");
        startActivity(i);*/

        if(IntentFrom.equalsIgnoreCase("ShipmentEntry")){
            Intent i = new Intent(this, ShipmentAndInvoicing.class);
            //  i.putExtra("intentFrom", "ShipmentComplete");
            startActivity(i);
            finish();
        }else if(IntentFrom.equalsIgnoreCase("PendingDelivery")){
            Intent i = new Intent(this, PendingDeliveries.class);
            //  i.putExtra("intentFrom", "ShipmentComplete");
            startActivity(i);
            finish();
        }
    }

    public void updateTable(){
       /* tcf.insertAssignedActivity(InvoiceNo, userMasterID, DelAgentLoginID, DelAgentName, DelAgentMobile, DelAgentEmail, AssignActivityDesc,
                SONO_frmIntent,SODate,ConsigneeName,DelvAddress,Pref_DeliveryTime_From, Pref_DeliveryTime_To, OrderType);*/
       try {
           sql.execSQL("UPDATE Shipment_Invoice SET isActivityAssigned = 'Y' WHERE InvoiceNo='" + InvoiceNo + "'" );
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    public String Convertdate(String date){

        String DateToStr="";

        if(date.contains("/Date")){
            date = date.replace("/Date(","");
            date = date.replace(")/","");
            Long timestamp = Long.valueOf(date);
            Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
            cal1.setTimeInMillis(timestamp);
            String date1 = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal1).toString();

            SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//Feb 23 2016 12:16PM
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date d1 = null;
            try {
                d1 = format.parse(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateToStr = Format.format(d1);
        }else {
            if(!date.equalsIgnoreCase("")){
                SimpleDateFormat Format = new SimpleDateFormat("MMM dd yyyy");//Feb 23 2016 12:16PM
                //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date d1 = null;
                try {
                    d1 = format.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DateToStr = Format.format(d1);
                System.out.println(DateToStr);
            }else {
                DateToStr = "";
            }
        }

        return DateToStr;
    }
}
