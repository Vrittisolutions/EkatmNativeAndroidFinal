package com.vritti.sales.OrderBookingNew;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.Utilities;
import com.squareup.picasso.Picasso;
import com.vritti.crm.classes.ProgressHUD;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.OrderBookingNew.Adapter.SpecialityAdapter;
import com.vritti.sales.OrderBookingNew.Bean.Business;
import com.vritti.sales.activity.MainActivity;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;
import com.vritti.sales.utils_tbuds.NetworkUtils;
import com.vritti.sales.utils_tbuds.StartSession_tbuds;
import com.vritti.sessionlib.CallbackInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SegmentSelection extends AppCompatActivity {
    Context parent;
    GridView gridbs;
    TextView txttitle;
    ArrayList<Business> businessArrayList;
    SpecialityAdapter spAdapter;
    static ProgressHUD progress;
    LinearLayout laysettings,layspeciality,layrestaurant,offersdisc;
    AutoCompleteTextView edtdistance;
    Button btnnshopbyspeciality,btnshopbymerch,btnsave,btnmakeitdef;
    LinearLayout showdialog;

    SharedPreferences sharedpreferences;
    public String restoredMobile, restoredusername, cvid, usertype,image_URL,CustomerID;

    //DatabaseHelper databaseHelper;
    private static DatabaseHandlers databaseHelper;
    SQLiteDatabase sql;

    String selBsId = "", selBsCode = "", callFrom = "", selBsDesc="";
    int scrollToPos = 0;
    String restPkId = "", restCode = "",restdesc="",restimgpath ="",restaliasname="";

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "", intentFrom = "";
    String dabasename="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segment_selection);

        final ActionBar ab = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.app_name)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        init();

        if (NetworkUtils.isNetworkAvailable(SegmentSelection.this)) {
            showdialog.setVisibility(View.VISIBLE);
            new StartSession_tbuds(SegmentSelection.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    //new DownloadShipToDataJSON().execute();
                    new DownloadBusinesssegmentJSON().execute();
                }

                @Override
                public void callfailMethod(String s) {

                }
            });
        } else {
            Toast.makeText(this, ""+getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
        }

        setListener();
    }

    public void init(){
        parent = SegmentSelection.this;

        txttitle = findViewById(R.id.txttitle);
        gridbs = findViewById(R.id.gridbs);
        laysettings = findViewById(R.id.laysettings);
        layspeciality = findViewById(R.id.layspeciality);
        btnshopbymerch = findViewById(R.id.btnshopbymerch);
        edtdistance = findViewById(R.id.edtdistance);
        btnnshopbyspeciality = findViewById(R.id.btnnshopbyspeciality);
        btnsave = findViewById(R.id.btnsave);
        btnmakeitdef = findViewById(R.id.btnmakeitdef);
        layrestaurant = findViewById(R.id.layrestaurant);
        offersdisc = findViewById(R.id.offersdisc);
        showdialog = findViewById(R.id.showdialog);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(SegmentSelection.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        databaseHelper = new DatabaseHandlers(parent, dabasename);
        sql = databaseHelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        mprogress=findViewById(R.id.toolbar_progress_App_bar);
        AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.LATITUDE = sharedpreferences.getString("Latitude","");
        AnyMartData.LONGITUDE = sharedpreferences.getString("Longitude","");

        businessArrayList = new ArrayList<Business>();

        Intent intent = getIntent();
        callFrom = intent.getStringExtra("callFrom");

        txttitle.setText(getResources().getString(R.string.whtbuy));

    }

    public void setListener(){

        gridbs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                scrollToPos = position;

               /* String selectedItem = businessArrayList.get(position).getSegmentCode();

                Snackbar snackbar = Snackbar.make(
                        layspeciality,
                        "Selected : " + selectedItem,
                        Snackbar.LENGTH_LONG
                );

                snackbar.getView().setBackgroundColor(Color.parseColor("#FF66729B"));
                snackbar.show();*/

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
                transitionDrawable.startTransition(100); // 600 Milliseconds

                selBsCode = businessArrayList.get(position).getSegmentCode();
                selBsId = businessArrayList.get(position).getPKBusiSegmentID();
                selBsDesc = businessArrayList.get(position).getSegmentDescription();

                AnyMartData.SpecImgPath = businessArrayList.get(position).getSegmntImgPath();
                SharedPreferences.Editor editorp = sharedpreferences.edit();
                editorp.putString("SpecImgPath", AnyMartData.SpecImgPath);
                editorp.commit();

                AnyMartData.selected_BSEGMENTID = selBsId;
                AnyMartData.selected_BSEGMENTCODE = selBsCode;

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("SelBSegId", selBsId);
                editor.putString("SelBSegCode", selBsCode);
                editor.putString("SelBSegDesc", selBsDesc);
                editor.putString("SHOPBYMODE", "ShopBySpeciality");
                editor.putBoolean("DirectSegmentSearch", true);
                editor.commit();

                Intent intent = new Intent(SegmentSelection.this,CategorySelectionActivity.class);
                intent.putExtra("BSegmentCode",selBsCode);
                intent.putExtra("BSegmentId",selBsId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();

            }
        });

    }

    class DownloadBusinesssegmentJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = CompanyURL + AnyMartData.api_GetFillCustomerSegment;
            try {
                res = Utility.OpenconnectionOrferbilling(url, SegmentSelection.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);

            showdialog.setVisibility(View.GONE);

            try {

                JSONArray jResults = new JSONArray(response);
                businessArrayList.clear();

                tcf.clearTable(SegmentSelection.this,DatabaseHandlers.TABLE_BUS_SEGMENT);

                if (jResults.length() > 0) {
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jsonObject = jResults.getJSONObject(i);
                        Business business = new Business();
                        business.setPKBusiSegmentID(jsonObject.getString("PKBusiSegmentID"));
                        business.setSegmentCode(jsonObject.getString("SegmentCode"));
                        business.setSegmntImgPath(jsonObject.getString("BusiImgPath"));
                        business.setSegmntImgPath(jsonObject.getString("MerchAliasName"));
                        business.setSegmentDescription(jsonObject.getString("SegmentDescription"));

                        String segmntcode = jsonObject.getString("SegmentCode");

                        if(jsonObject.getString("SegmentDescription").equalsIgnoreCase("Restaurants")){
                            restCode= jsonObject.getString("SegmentCode");
                            restdesc= jsonObject.getString("SegmentDescription");
                            restPkId=jsonObject.getString("PKBusiSegmentID");
                            restimgpath=jsonObject.getString("BusiImgPath");
                            restaliasname=jsonObject.getString("MerchAliasName");

                        }/*else if(jsonObject.getString("SegmentDescription").equalsIgnoreCase("Offers and promotion")){
                            offCode= jsonObject.getString("SegmentCode");
                            offdesc= jsonObject.getString("SegmentDescription");
                            offPkid=jsonObject.getString("PKBusiSegmentID");
                            offimgpath=jsonObject.getString("BusiImgPath");
                            offaliasname=jsonObject.getString("MerchAliasName");

                        }*/else if(jsonObject.getString("SegmentDescription").equalsIgnoreCase("Wholesale") ||
                                jsonObject.getString("SegmentDescription").equalsIgnoreCase("Electronics") ||
                                jsonObject.getString("SegmentDescription").equalsIgnoreCase("Fashion") ||
                                jsonObject.getString("SegmentDescription").equalsIgnoreCase("Household")||
                                jsonObject.getString("SegmentDescription").equalsIgnoreCase("Health and Medicine") ){

                        }else {
                            businessArrayList.add(business);
                        }

                        String BusiImgPath =  jsonObject.getString("BusiImgPath");

                        if(BusiImgPath.equalsIgnoreCase("") || BusiImgPath.equalsIgnoreCase(null)){
                            BusiImgPath = "";
                        }else {
                            BusiImgPath = CompanyURL+"/images/"+BusiImgPath;
                        }

                        try{
                            tcf.addSegmentMaster(jsonObject.getString("PKBusiSegmentID"),
                                    jsonObject.getString("SegmentCode"),BusiImgPath,
                                    jsonObject.getString("MerchAliasName"),
                                    jsonObject.getString("SegmentDescription"));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }


                getDataFromDatabase();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void getDataFromDatabase(){
        businessArrayList.clear();

        String qry = "Select * from "+DatabaseHandlers.TABLE_BUS_SEGMENT+" Order by SegmentCode ASC";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();
            do{
                String PKBusiSegmentID = c.getString(c.getColumnIndex("PKBusiSegmentID"));
                String SegmentCode = c.getString(c.getColumnIndex("SegmentCode"));
                String BusiImgPath = c.getString(c.getColumnIndex("BusiImgPath"));
                String MerchAliasName = c.getString(c.getColumnIndex("MerchAliasName"));
                String SegmentDescription = c.getString(c.getColumnIndex("SegmentDescription"));

                Business business = new Business();
                business.setPKBusiSegmentID(PKBusiSegmentID);
                business.setSegmentCode(SegmentCode);
                business.setSegmntImgPath(BusiImgPath);
                business.setMerchAlisName(MerchAliasName);
                business.setSegmentDescription(SegmentDescription);

                if(SegmentDescription.equalsIgnoreCase("Restaurants")){
                    restCode= SegmentCode;
                    restdesc= SegmentDescription;
                    restPkId=PKBusiSegmentID;
                    restimgpath=BusiImgPath;
                    restaliasname=MerchAliasName;

                }/*else if(SegmentDescription.equalsIgnoreCase("Offers and promotion")){
                    offCode= SegmentCode;
                    offdesc= SegmentDescription;
                    offPkid=PKBusiSegmentID;
                    offimgpath=BusiImgPath;
                    offaliasname=MerchAliasName;

                }*/else if(SegmentDescription.equalsIgnoreCase("Wholesale") ||
                        SegmentDescription.equalsIgnoreCase("Electronics") ||
                        SegmentDescription.equalsIgnoreCase("Fashion") ||
                        SegmentDescription.equalsIgnoreCase("Household")||
                        SegmentDescription.equalsIgnoreCase("Health and Medicine") ){

                }else {
                    businessArrayList.add(business);
                }

            }while (c.moveToNext());
        }

        spAdapter = new SpecialityAdapter(parent,businessArrayList);
        gridbs.setAdapter(spAdapter);
        setGridViewHeightBasedOnChildren(gridbs,3,businessArrayList.size());
    }

    public void setGridViewHeightBasedOnChildren(GridView gridView, int columns, int listsize) {
        try{
            ListAdapter listAdapter = gridView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int items = listAdapter.getCount();
            int rows = 0;

            View listItem = listAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();

            float x = 1;
            if( items > columns ){
                x = items/columns;

                if(listsize % 3 == 0){
                    rows = (int) (x);
                }else {
                    rows = (int) (x+1);
                }

                totalHeight *= rows;
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
