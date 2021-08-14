package com.vritti.crm.vcrm7;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vritti.crm.bean.Country;
import com.vritti.crm.bean.FilteredProspect;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilteredProspectListActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;


    RecyclerView rv_filteredprospect;
    LinearLayoutManager llm;
    SQLiteDatabase sql;
    public static Context context;
    ProspectFilterAdapter prospectAdapter;
    List<FilteredProspect> prospectList;
    ProgressBar progressbar;
    SharedPreferences userpreferences;
    String Firmname, Address, Alias, No_of_office, No_of_employee, Notes;
    String CityName = "", CountryId = "", TerritoryId = "", StateId = "", CityId = "", Remark = "",
            firmAlias = "";
    String businessSegmentId = "", businessDetails = "", website = "", sourceOfProspect = "";
    ImageView img_add, img_refresh, img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_filtered_prospect_list);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        //  toolbar_action.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar_action.setTitle("");

        setSupportActionBar(toolbar_action);
        //     getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        context = FilteredProspectListActivity.this;
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        rv_filteredprospect = (RecyclerView) findViewById(R.id.rv_filteredprospect);
        llm = new LinearLayoutManager(getApplicationContext());
        rv_filteredprospect.setLayoutManager(llm);
        prospectList = new ArrayList<FilteredProspect>();
        progressbar = (ProgressBar) findViewById(R.id.progressbar_1);

        if (cf.getFilterProspectcount() > 0) {
            UpdatList();
        }

        txt_title = findViewById(R.id.txt_title);
        // img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Prospect Information");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    /*    adapter = new SOHeaderAdapter(soHeaderlist);
        rv.setAdapter(adapter);
        itemDecoration =
                new DividerItemDecoration(SoapHeaderActivity.this, LinearLayoutManager.VERTICAL);
        rv.addItemDecoration(itemDecoration);
        registerForContextMenu(rv);*/
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void UpdatList() {
        prospectList.clear();
        String query = "SELECT * FROM " + db.TABLE_filterdata_prospect;
        //     String query = "SELECT * FROM " + db.TABLE_filterdata_prospect;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                FilteredProspect prospect = new FilteredProspect();
                prospect.setPKSuspectId(cur.getString(cur.getColumnIndex("PKSuspectId")));
                prospect.setSuspectCode(cur.getString(cur.getColumnIndex("SuspectCode")));
                prospect.setFirmName(cur.getString(cur.getColumnIndex("FirmName")));
                prospect.setAddress(cur.getString(cur.getColumnIndex("Address")));
                prospect.setBusinessDetails(cur.getString(cur.getColumnIndex("BusinessDetails")));
                prospect.setFKBusiSegmentId(cur.getString(cur.getColumnIndex("FKBusiSegmentId")));
                prospect.setTurnover(cur.getString(cur.getColumnIndex("Turnover")));
                prospect.setNoOfEmployees(cur.getString(cur.getColumnIndex("NoOfEmployees")));
                prospect.setNoOfOffices(cur.getString(cur.getColumnIndex("NoOfOffices")));
                prospect.setFKCustomerId(cur.getString(cur.getColumnIndex("FKCustomerId")));
                prospect.setIsProspect(cur.getString(cur.getColumnIndex("IsProspect")));
                prospect.setIsLead(cur.getString(cur.getColumnIndex("IsLead")));
                prospect.setIsOrder(cur.getString(cur.getColumnIndex("IsOrder")));
                prospect.setProspectStatusChangedDate(cur.getString(cur.getColumnIndex("ProspectStatusChangedDate")));
                prospect.setProspectStatusChangedBy(cur.getString(cur.getColumnIndex("ProspectStatusChangedBy")));
                prospect.setLeadStatusChangedDate(cur.getString(cur.getColumnIndex("LeadStatusChangedDate")));
                prospect.setLeadStatusChangedBy(cur.getString(cur.getColumnIndex("LeadStatusChangedBy")));
                prospect.setOrderStatusChangedDate(cur.getString(cur.getColumnIndex("OrderStatusChangedDate")));
                prospect.setOrderStatusChangedBy(cur.getString(cur.getColumnIndex("OrderStatusChangedBy")));
                prospect.setMoveToArchieve(cur.getString(cur.getColumnIndex("MoveToArchieve")));
                prospect.setMoveToArchieveBy(cur.getString(cur.getColumnIndex("MoveToArchieveBy")));
                prospect.setReasonCode(cur.getString(cur.getColumnIndex("ReasonCode")));
                prospect.setAddedBy(cur.getString(cur.getColumnIndex("AddedBy")));
                prospect.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                prospect.setModifiedBy(cur.getString(cur.getColumnIndex("ModifiedBy")));
                prospect.setModifiedDt(cur.getString(cur.getColumnIndex("ModifiedDt")));
                prospect.setFKEnqSourceId(cur.getString(cur.getColumnIndex("FKEnqSourceId")));
                prospect.setCompanyURL(cur.getString(cur.getColumnIndex("CompanyURL")));
                prospect.setSourceName(cur.getString(cur.getColumnIndex("SourceName")));
                prospect.setPKSuspContactDtlsID(cur.getString(cur.getColumnIndex("PKSuspContactDtlsID")));
                prospect.setContactName(cur.getString(cur.getColumnIndex("ContactName")));
                prospect.setDesignation(cur.getString(cur.getColumnIndex("Designation")));
                prospect.setTelephone(cur.getString(cur.getColumnIndex("Telephone")));
                prospect.setMobile(cur.getString(cur.getColumnIndex("Mobile")));
                prospect.setFax(cur.getString(cur.getColumnIndex("Fax")));
                prospect.setEmailId(cur.getString(cur.getColumnIndex("EmailId")));
                //   prospect.setIsActive(cur.getString(cur.getColumnIndex("IsActive")));
                // prospect.setIsPrimaryContact(cur.getString(cur.getColumnIndex("IsPrimaryContact")));
                prospect.setFamilyDesc(cur.getString(cur.getColumnIndex("FamilyDesc")));
                prospect.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                prospect.setTerritoryName(cur.getString(cur.getColumnIndex("TerritoryName")));
                prospect.setLeadGivenBYId(cur.getString(cur.getColumnIndex("LeadGivenBYId")));
                prospect.setContactPersonDept(cur.getString(cur.getColumnIndex("ContactPersonDept")));
                prospect.setCustVendorName(cur.getString(cur.getColumnIndex("CustVendorName")));
                prospect.setFirmAlias(cur.getString(cur.getColumnIndex("FirmAlias")));
                prospect.setOpenCalls(cur.getString(cur.getColumnIndex("OpenCalls")));
                prospect.setCloseCalls(cur.getString(cur.getColumnIndex("CloseCalls")));
                prospect.setOrderReceived(cur.getString(cur.getColumnIndex("OrderReceived")));
                prospect.setOrderLost(cur.getString(cur.getColumnIndex("OrderLost")));
                prospect.setOrderRegrete(cur.getString(cur.getColumnIndex("OrderRegrete")));
                prospect.setCallCloseWithoutOrder(cur.getString(cur.getColumnIndex("CallCloseWithoutOrder")));
                prospect.setFKConsigneeId(cur.getString(cur.getColumnIndex("FKConsigneeId")));
                prospect.setLastCSSDate(cur.getString(cur.getColumnIndex("LastCSSDate")));
                prospect.setLastCSSRating(cur.getString(cur.getColumnIndex("LastCSSRating")));
                prospect.setFuturePlanDate(cur.getString(cur.getColumnIndex("FuturePlanDate")));
                prospect.setLastSODate(cur.getString(cur.getColumnIndex("LastSODate")));
                prospect.setLastSOScheduleDate(cur.getString(cur.getColumnIndex("LastSOScheduleDate")));
                prospect.setLastShipmentDate(cur.getString(cur.getColumnIndex("LastShipmentDate")));
                prospect.setFKPlantId(cur.getString(cur.getColumnIndex("FKPlantId")));
                prospect.setFirmTitle(cur.getString(cur.getColumnIndex("FirmTitle")));
                prospect.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                prospect.setTitle(cur.getString(cur.getColumnIndex("Title")));
                prospect.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                prospect.setLatestRemark(cur.getString(cur.getColumnIndex("LatestRemark")));
                prospect.setIsReseller(cur.getString(cur.getColumnIndex("IsReseller")));
                prospect.setSegmentDescription(cur.getString(cur.getColumnIndex("SegmentDescription")));
                prospect.setFKCountryId(cur.getString(cur.getColumnIndex("FKCountryId")));
                prospect.setFKStateId(cur.getString(cur.getColumnIndex("FKStateId")));
                prospect.setFKCityId(cur.getString(cur.getColumnIndex("FKCityId")));
                prospect.setFKTerritoryId(cur.getString(cur.getColumnIndex("FKTerritoryId")));


                prospectList.add(prospect);
            } while (cur.moveToNext());

            prospectAdapter = new ProspectFilterAdapter(prospectList, FilteredProspectListActivity.this);
            Log.e("Prospect List", String.valueOf(prospectList));
            rv_filteredprospect.setAdapter(prospectAdapter);

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    public class ProspectFilterAdapter extends RecyclerView.Adapter<ProspectFilterAdapter.Viewholder> {

        String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
                UserMasterId = "", UserName = "", MobileNo = "";
        Utility ut;
        DatabaseHandlers db;
        CommonFunctionCrm cf;
        Context context;

        List<FilteredProspect> prospectList;
        private String PKSuspectId, FinalJson;
        CommonObjectProperties commonObj;
        SharedPreferences userpreferences;
        SQLiteDatabase sql;
        String ProspectId = "";
        private int positon;

        public ProspectFilterAdapter(List<FilteredProspect> prospectList, Context mContext) {
            this.prospectList = prospectList;
            this.context = mContext;
            commonObj = new CommonObjectProperties();

            userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);

        }

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.crm_filter_item_list, parent, false);
            // itemView.setTag(viewType);
            return new Viewholder(itemView);
        }

        @Override
        public void onBindViewHolder(final Viewholder holder, final int position) {
          /*  if (position % 2 == 1) {
                holder.card_view.setBackgroundColor(Color.parseColor("#CBDFE2"));
            } else {
                holder.card_view.setBackgroundColor(Color.parseColor("#F1F6F7"));
            }
*/
            commonObj = new CommonObjectProperties();

            context = getApplicationContext();
            ut = new Utility();
            cf = new CommonFunctionCrm(context);
            String settingKey = ut.getSharedPreference_SettingKey(context);
            String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
            db = new DatabaseHandlers(context, dabasename);
            CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
            EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
            PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
            LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
            Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
            UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
            UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

            sql = db.getWritableDatabase();


            String CustVendorName = prospectList.get(position).getCustVendorName();
            String BusinessDetails = prospectList.get(position).getBusinessDetails();
            if (!CustVendorName.equals("")) {
                holder.txtItemdesc.setText("refered by :" + prospectList.get(position).getCustVendorName());
            }
            if (!BusinessDetails.equals("")) {
                holder.txtItemdesc.setText("Segment :" + prospectList.get(position).getSegmentDescription());

            }
            Firmname = prospectList.get(position).getFirmName();
            CityName = prospectList.get(position).getCityName();
            Log.e("City Name", CityName);


            holder.txtFName.setText(Firmname);
            holder.txtcity.setText(CityName);
            //   holder.txtItemdesc.setText(prospectList.get(position).getFamilyDesc());
            //  holder.txtcallclose.setText(prospectList.get(position).getCloseCalls() + " Close call");
            System.out.println(" CloseCall :" + prospectList.get(position).getCloseCalls());
            //holder.txtcallopen.setText(prospectList.get(position).getOpenCalls() + " Open call");
            System.out.println("OpenCall :" + prospectList.get(position).getOpenCalls());
            holder.txtAddress.setText(prospectList.get(position).getAddress());
            if (holder.llName.getVisibility() == View.VISIBLE) {
                holder.llName.setVisibility(View.GONE);
            }

            int opencall = Integer.parseInt(prospectList.get(position).getOpenCalls());

            System.out.println("Open :" + opencall);
            int closecall = Integer.parseInt(prospectList.get(position).getCloseCalls());
            System.out.println("Open-1 :" + closecall);

            if (opencall > 0) {
                holder.txtcallopen.setVisibility(View.VISIBLE);
                holder.txtcallopen.setText(prospectList.get(position).getOpenCalls() + " Open calls");

            } else {
                holder.txtcallopen.setVisibility(View.GONE);

            }
            if (closecall > 0) {
                holder.txtcallclose.setVisibility(View.VISIBLE);
                holder.txtcallclose.setText(prospectList.get(position).getCloseCalls() + " Close calls");

            } else {
                holder.txtcallclose.setVisibility(View.GONE);
            }


        }


        private boolean isnet() {
            // TODO Auto-generated method stub
            Context context1 = context.getApplicationContext();
            ConnectivityManager cm = (ConnectivityManager) context1
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                return true;
            } else {
                Toast.makeText(context1, "No internet connection", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return prospectList.size();
        }

        class Viewholder extends RecyclerView.ViewHolder {
            TextView txtFName, txtName;
            TextView txtcity, txtNumber;
            TextView txtItemdesc, txtEmail;
            TextView txtcallclose, txtcallopen, txtAddress;
            ImageView img_edit;
            LinearLayout llName;//,img_edit;
            CardView card_view;
            ImageView click;

            public Viewholder(View itemView) {
                super(itemView);
                txtFName = (TextView) itemView.findViewById(R.id.txtFName);
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                txtcity = (TextView) itemView.findViewById(R.id.txtcity);
                txtNumber = (TextView) itemView.findViewById(R.id.txtNumber);
                txtItemdesc = (TextView) itemView.findViewById(R.id.txtItemdesc);
                txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
                txtcallclose = (TextView) itemView.findViewById(R.id.txtcallclose);
                txtcallopen = (TextView) itemView.findViewById(R.id.txtcallopen);
                txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
                llName = (LinearLayout) itemView.findViewById(R.id.llName);
                card_view = itemView.findViewById(R.id.card_view);
                click = (ImageView) itemView.findViewById(R.id.click);
                img_edit = itemView.findViewById(R.id.img_edit);

                img_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        showProgressDialog();
                        PKSuspectId = prospectList.get(position).getPKSuspectId();
                        Firmname = prospectList.get(position).getFirmName();
                        firmAlias = prospectList.get(position).getFirmAlias();
                        Address = prospectList.get(position).getAddress();
                        No_of_office = prospectList.get(position).getNoOfOffices();
                        No_of_employee = prospectList.get(position).getNoOfEmployees();
                        Alias = prospectList.get(position).getFirmAlias();
                        Notes = prospectList.get(position).getRemark();
                        CountryId = prospectList.get(position).getFKCountryId();
                        StateId = prospectList.get(position).getFKStateId();
                        CityId = prospectList.get(position).getFKCityId();
                        TerritoryId = prospectList.get(position).getFKTerritoryId();
                        Remark = prospectList.get(position).getRemark();
                        businessSegmentId = prospectList.get(position).getFKBusiSegmentId();
                        businessDetails = prospectList.get(position).getBusinessDetails();
                        website = prospectList.get(position).getCompanyURL();
                        sourceOfProspect = prospectList.get(position).getFKEnqSourceId();

                        if (isnet()) {
                            new StartSession(FilteredProspectListActivity.this, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    // new DownloadContactFetchData().execute(PKSuspectId);
                                    ///api/SuspectMasterAPI/GetFill_Data?CallId=01b87af4-dc58-4b44-94b1-f760393d93ad
                                    new GetFillData().execute(PKSuspectId);

                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(context, ProspectEnterpriseActivity1.class);
                            intent.putExtra("PKSuspectId", PKSuspectId);
                            intent.putExtra("firmname", Firmname);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    }
                });

                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (llName.getVisibility() == View.VISIBLE) {
                            llName.setVisibility(View.GONE);
                        } else {
                            llName.setVisibility(View.VISIBLE);
                            txtName.setText(prospectList.get(position).getContactName());
                            txtNumber.setText(prospectList.get(position).getTelephone() + "/" +
                                    prospectList.get(position).getMobile());
                            txtEmail.setText(prospectList.get(position).getEmailId());
                        }


                    }
                });


                txtcallopen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        commonObj = new CommonObjectProperties();
                        JSONObject jsoncommonObj = commonObj.DataObj();
                        JSONObject jsonCallsObject;
                        PKSuspectId = prospectList.get(position).getPKSuspectId();
                        try {
                            jsonCallsObject = jsoncommonObj.getJSONObject("ProspectId");
                            jsonCallsObject.put("IsSet", true);
                            jsonCallsObject.put("value1", PKSuspectId);

                            jsonCallsObject = jsoncommonObj.getJSONObject("Isclose");
                            jsonCallsObject.put("IsSet", true);
                            jsonCallsObject.put("value1", "N");//close =Y
                            jsonCallsObject.put("Operator", "eq");

                            FinalJson = jsoncommonObj.toString();
                            if (isnet()) {
                                new StartSession(context, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadCallInformationData().execute(FinalJson);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                txtcallclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int position = getAdapterPosition();
                        commonObj = new CommonObjectProperties();

                        JSONObject jsoncommonObj = commonObj.DataObj();
                        JSONObject jsonCallsObject;
                        PKSuspectId = prospectList.get(position).getPKSuspectId();


                        try {
                            jsonCallsObject = jsoncommonObj.getJSONObject("ProspectId");
                            jsonCallsObject.put("IsSet", true);
                            jsonCallsObject.put("value1", PKSuspectId);

                            jsonCallsObject = jsoncommonObj.getJSONObject("Isclose");
                            jsonCallsObject.put("IsSet", true);
                            jsonCallsObject.put("value1", "Y");//close =Y
                            jsonCallsObject.put("Operator", "eq");

                            FinalJson = jsoncommonObj.toString();
                            if (isnet()) {
                                new StartSession(context, new CallbackInterface() {
                                    @Override
                                    public void callMethod() {
                                        new DownloadCallInformationData().execute(FinalJson);
                                    }

                                    @Override
                                    public void callfailMethod(String msg) {

                                    }
                                });

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
                itemView.setTag(itemView);
            }
        }

        class DownloadCallInformationData extends AsyncTask<String, Void, String> {
            Object res;
            String response = "error";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = CompanyURL + WebUrlClass.api_Get_Call;
                    if (res != null) {
                        res = ut.OpenPostConnection(url, FinalJson, FilteredProspectListActivity.this);
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String integer) {
                super.onPostExecute(integer);
                dismissProgressDialog();
                if (integer.contains("PKSuspectId")) {
                    try {
                        JSONArray jResults = null;
                        jResults = new JSONArray(response);
                        ContentValues values = new ContentValues();

                        sql.delete(db.TABLE_CALLLISTDATA, null,
                                null);
                        Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CALLLISTDATA, null);
                        int count = c.getCount();
                        String columnName, columnValue;
                        for (int i = 0; i < jResults.length(); i++) {
                            JSONObject jorder = jResults.getJSONObject(i);
                            for (int j = 0; j < c.getColumnCount(); j++) {

                                columnName = c.getColumnName(j);
                                columnValue = jorder.getString(columnName);
                                values.put(columnName, columnValue);

                            }

                            long a = sql.insert(db.TABLE_CALLLISTDATA, null, values);
                            Log.e("log data", "" + a);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, CallsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    //  overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                } else if (integer.contains("Search is returning so many records")) {
                    Toast toast = Toast.makeText(context, "Search is returning so many records..! Please refine your search..!", Toast.LENGTH_SHORT);
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(context, "No Record Found...", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }

        }

        class DownloadContactFetchData extends AsyncTask<String, Void, String> {
            Object res;
            String response = "error";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = CompanyURL + WebUrlClass.api_get_getFillContDet + "?PKSuspectId=" + params[0];
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    response = "error";

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String integer) {
                super.onPostExecute(integer);
                dismissProgressDialog();

                try {
                    JSONArray jResults = null;
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_CONTACT_DETAILS, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CONTACT_DETAILS, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnValue.equalsIgnoreCase(null)) {
                                columnValue = "";
                            }
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_CONTACT_DETAILS, null, values);
                        Log.e("log data", "" + a);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ut.isNet(FilteredProspectListActivity.this)) {
                    new StartSession(FilteredProspectListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadProductFetchData().execute(PKSuspectId);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }


        }

        //data getting is blank
        class DownloadProductFetchData extends AsyncTask<String, Void, String> {
            Object res;
            String response = "error";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = CompanyURL + WebUrlClass.api_get_getFillFamilyDet + "?PKSuspectId=" + params[0];
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String integer) {
                super.onPostExecute(integer);
                dismissProgressDialog();

                try {
                    JSONArray jResults = null;
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_PRODUCT_DATA_FETCH, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnValue.equalsIgnoreCase(null)) {
                                columnValue = "";
                            }
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_PRODUCT_DATA_FETCH, null, values);
                        Log.e("log data", "" + a);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ut.isNet(FilteredProspectListActivity.this)) {
                    new StartSession(FilteredProspectListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadFillcontrolFetchData().execute(PKSuspectId);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }


            }


        }

        class DownloadFillcontrolFetchData extends AsyncTask<String, Void, String> {
            Object res;
            String response = "error";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = CompanyURL + WebUrlClass.api_get_GetFillControls + "?PKSuspectId=" + params[0];
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    response = "error";

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String integer) {
                super.onPostExecute(integer);
                dismissProgressDialog();

                try {
                    JSONArray jResults = null;
                    jResults = new JSONArray(response);
                    ContentValues values = new ContentValues();

                    sql.delete(db.TABLE_FILLCONTROL_DATA_FETCH, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_FILLCONTROL_DATA_FETCH, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            if (columnValue.equalsIgnoreCase(null)) {
                                columnValue = "";
                            }
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_FILLCONTROL_DATA_FETCH, null, values);
                        Log.e("log data", "" + a);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ut.isNet(FilteredProspectListActivity.this)) {
                    new StartSession(FilteredProspectListActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadProspectTypeId().execute(PKSuspectId);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FilteredProspectListActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }

            }


        }

        class DownloadProspectTypeId extends AsyncTask<String, Void, String> {
            Object res;
            String response = "error";


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String url = CompanyURL + WebUrlClass.api_get_GetProspectTypeID + "?PKSuspectId=" + params[0];
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString().replaceAll("\\\\", "");
                        response = response.replaceAll("\\\\\\\\/", "");
                        response = response.substring(1, response.length() - 1);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    response = "error";

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String integer) {
                super.onPostExecute(integer);
                dismissProgressDialog();

                if (response.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(FilteredProspectListActivity.this, ProspectEnterpriseActivity1.class);
                    intent.putExtra("keymode", "Edit");
                    intent.putExtra("PKSuspectId", PKSuspectId);
                    intent.putExtra("firmname", Firmname);
                    intent.putExtra("firmAlias", firmAlias);
                    intent.putExtra("address", Address);
                    intent.putExtra("FKCountryId", CountryId);
                    intent.putExtra("FKStateId", StateId);
                    intent.putExtra("FKCityId", CityId);
                    intent.putExtra("FKTerritoryId", TerritoryId);
                    intent.putExtra("Age", Remark);
                    intent.putExtra("BusinessSegmentId", businessSegmentId);
                    intent.putExtra("website", website);
                    intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                    intent.putExtra("businessDetails", businessDetails);
                    //intent.putExtra("",CityName);
                    Log.e("Api response", response);
                    System.out.println("Firm" + Firmname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (response.equalsIgnoreCase("2")) {
                    Intent intent = new Intent(FilteredProspectListActivity.this, BusinessProspectusActivity.class);
                    intent.putExtra("PKSuspectId", PKSuspectId);
                    intent.putExtra("firmname", Firmname);
                    intent.putExtra("FKCountryId", CountryId);
                    intent.putExtra("FKStateId", StateId);
                    intent.putExtra("FKCityId", CityId);
                    intent.putExtra("FKTerritoryId", TerritoryId);
                    intent.putExtra("keymode", "Edit");
                    intent.putExtra("BusinessSegmentId", businessSegmentId);
                    intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                    intent.putExtra("Age", Remark);
                    System.out.println("Firm" + Firmname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (response.equalsIgnoreCase("3")) {
                    Intent intent = new Intent(FilteredProspectListActivity.this, IndividualProspectusActivity.class);
                    intent.putExtra("keymode", "Edit");
                    intent.putExtra("PKSuspectId", PKSuspectId);
                    intent.putExtra("firmname", Firmname);
                    System.out.println("Firm" + Firmname);
                    intent.putExtra("FKCountryId", CountryId);
                    intent.putExtra("FKStateId", StateId);
                    intent.putExtra("FKCityId", CityId);
                    intent.putExtra("FKTerritoryId", TerritoryId);
                    intent.putExtra("Address", Address);
                    intent.putExtra("Age", Remark);
                    intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                    /*intent.putExtra("",)*/
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(FilteredProspectListActivity.this, ProspectEnterpriseActivity1.class);
                    intent.putExtra("keymode", "Edit");
                    intent.putExtra("PKSuspectId", PKSuspectId);
                    intent.putExtra("firmname", Firmname);
                    intent.putExtra("firmAlias", firmAlias);
                    intent.putExtra("address", Address);
                    intent.putExtra("FKCountryId", CountryId);
                    intent.putExtra("FKStateId", StateId);
                    intent.putExtra("FKCityId", CityId);
                    intent.putExtra("FKTerritoryId", TerritoryId);
                    intent.putExtra("Age", Remark);
                    intent.putExtra("BusinessSegmentId", businessSegmentId);
                    intent.putExtra("website", website);
                    intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                    intent.putExtra("businessDetails", businessDetails);
                    //intent.putExtra("",CityName);
                    Log.e("Api response", response);
                    System.out.println("Firm" + Firmname);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }


        }

        private class GetFillData extends AsyncTask<String, Void, String> {
            String res = "", response = "",entityId="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                try {
                    //api/SuspectMasterAPI/GetFill_Data?CallId=01b87af4-dc58-4b44-94b1-f760393d93ad
                    String url = CompanyURL + WebUrlClass.api_GetFill_Data + "?CallId=" + strings[0];
                    res = ut.OpenConnection(url);
                    if (res != null) {
                        response = res.toString();
                        response = response.substring(1, response.length() - 1);

                        response = "{\"Activity\":\"" + response + "\n" + "\"}";
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    response = "error";

                } catch (Exception e) {
                    e.printStackTrace();
                    response = "error";
                }
                return response;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dismissProgressDialog();
                if (!response.equals("error")) {
                    JSONArray jResults = null;
                    JSONObject obj = null;
                    try {
                        obj = new JSONObject(response);

                        String Msgcontent = obj.getString("Activity");
                        JSONObject jsonObject = new JSONObject(Msgcontent);
                        JSONArray jsonArrayProduct = jsonObject.getJSONArray("SuspectProductDetail");
                        JSONArray jsonArrayAllData = jsonObject.getJSONArray("SuspectDetail");
                        JSONArray jsonArrayContact = jsonObject.getJSONArray("SuspectContactDetail");
                        JSONArray jsonArrayProspectType = jsonObject.getJSONArray("ProspectType");
                        // jResults = new JSONArray(Msgcontent);


                        if (jsonArrayContact.length() != 0) {
                            ContentValues values = new ContentValues();
                            sql.delete(db.TABLE_CONTACT_DETAILS, null,
                                    null);
                            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_CONTACT_DETAILS, null);
                            int count = c.getCount();
                            String columnName, columnValue;
                            for (int i = 0; i < jsonArrayContact.length(); i++) {
                                JSONObject jorder = jsonArrayContact.getJSONObject(i);
                                for (int j = 0; j < c.getColumnCount(); j++) {

                                    columnName = c.getColumnName(j);
                                    columnValue = jorder.getString(columnName);
                                    if (columnValue.equalsIgnoreCase(null)) {
                                        columnValue = "";
                                    }
                                    values.put(columnName, columnValue);

                                }

                                long a = sql.insert(db.TABLE_CONTACT_DETAILS, null, values);
                                Log.e("log data", "" + a);
                            }
                        }

                        if (jsonArrayProduct.length() != 0) {
                            ContentValues values = new ContentValues();
                            sql.delete(db.TABLE_PRODUCT_DATA_FETCH, null,
                                    null);
                            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PRODUCT_DATA_FETCH, null);
                            int count = c.getCount();
                            String columnName, columnValue;
                            for (int i = 0; i < jsonArrayProduct.length(); i++) {
                                JSONObject jorder = jsonArrayProduct.getJSONObject(i);
                                for (int j = 0; j < c.getColumnCount(); j++) {

                                    columnName = c.getColumnName(j);
                                    columnValue = jorder.getString(columnName);
                                    if (columnValue.equalsIgnoreCase(null)) {
                                        columnValue = "";
                                    }
                                    values.put(columnName, columnValue);

                                }

                                long a = sql.insert(db.TABLE_PRODUCT_DATA_FETCH, null, values);
                                Log.e("log data", "" + a);
                            }
                        }

                        if (jsonArrayAllData.length() != 0) {

                            ContentValues values = new ContentValues();

                            sql.delete(db.TABLE_FILLCONTROL_DATA_FETCH, null,
                                    null);
                            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_FILLCONTROL_DATA_FETCH, null);
                            int count = c.getCount();
                            String columnName, columnValue;
                            for (int i = 0; i < jsonArrayAllData.length(); i++) {
                                JSONObject jorder = jsonArrayAllData.getJSONObject(i);
                                for (int j = 0; j < c.getColumnCount(); j++) {

                                    columnName = c.getColumnName(j);
                                    columnValue = jorder.getString(columnName);
                                    if (columnValue.equalsIgnoreCase(null)) {
                                        columnValue = "";
                                    }
                                    values.put(columnName, columnValue);

                                }

                                long a = sql.insert(db.TABLE_FILLCONTROL_DATA_FETCH, null, values);
                                Log.e("log data", "" + a);
                            }


                        }

                        if(jsonArrayProspectType.length() != 0){
                            /*[{"PKSuspectid":"01b87af4-dc58-4b44-94b1-f760393d93ad","ProspectType":null}]*/
                            //  jsonArrayProspectType.getString("PKSuspectid");
                            JSONObject jsonObject1 = jsonArrayProspectType.getJSONObject(0);
                            PKSuspectId=jsonObject1.getString("PKSuspectid");
                            response = jsonObject1.getString("ProspectType");
                            if(jsonArrayAllData.length() != 0){
                                entityId = jsonArrayAllData.getJSONObject(0).getString("FKCustomerId");
                            }


                            if (response.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(FilteredProspectListActivity.this, ProspectEnterpriseActivity1.class);
                                intent.putExtra("keymode", "Edit");
                                intent.putExtra("PKSuspectId", PKSuspectId);
                                intent.putExtra("firmname", Firmname);
                                intent.putExtra("firmAlias", firmAlias);
                                intent.putExtra("address", Address);
                                intent.putExtra("FKCountryId", CountryId);
                                intent.putExtra("FKStateId", StateId);
                                intent.putExtra("FKCityId", CityId);
                                intent.putExtra("FKTerritoryId", TerritoryId);
                                intent.putExtra("Age", Remark);
                                intent.putExtra("BusinessSegmentId", businessSegmentId);
                                intent.putExtra("website", website);
                                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                                intent.putExtra("businessDetails", businessDetails);
                                intent.putExtra("EntityId", entityId);
                                //intent.putExtra("",CityName);
                                Log.e("Api response", response);
                                System.out.println("Firm" + Firmname);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else if (response.equalsIgnoreCase("2")) {
                                Intent intent = new Intent(FilteredProspectListActivity.this, BusinessProspectusActivity.class);
                                intent.putExtra("PKSuspectId", PKSuspectId);
                                intent.putExtra("firmname", Firmname);
                                intent.putExtra("FKCountryId", CountryId);
                                intent.putExtra("FKStateId", StateId);
                                intent.putExtra("FKCityId", CityId);
                                intent.putExtra("FKTerritoryId", TerritoryId);
                                intent.putExtra("keymode", "Edit");
                                intent.putExtra("BusinessSegmentId", businessSegmentId);
                                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                                intent.putExtra("Age", Remark);
                                System.out.println("Firm" + Firmname);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else if (response.equalsIgnoreCase("3")) {
                                Intent intent = new Intent(FilteredProspectListActivity.this, IndividualProspectusActivity.class);
                                intent.putExtra("keymode", "Edit");
                                intent.putExtra("PKSuspectId", PKSuspectId);
                                intent.putExtra("firmname", Firmname);
                                System.out.println("Firm" + Firmname);
                                intent.putExtra("FKCountryId", CountryId);
                                intent.putExtra("FKStateId", StateId);
                                intent.putExtra("FKCityId", CityId);
                                intent.putExtra("FKTerritoryId", TerritoryId);
                                intent.putExtra("Address", Address);
                                intent.putExtra("Age", Remark);
                                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                                /*intent.putExtra("",)*/
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(FilteredProspectListActivity.this, ProspectEnterpriseActivity1.class);
                                intent.putExtra("keymode", "Edit");
                                intent.putExtra("PKSuspectId", PKSuspectId);
                                intent.putExtra("firmname", Firmname);
                                intent.putExtra("firmAlias", firmAlias);
                                intent.putExtra("address", Address);
                                intent.putExtra("FKCountryId", CountryId);
                                intent.putExtra("FKStateId", StateId);
                                intent.putExtra("FKCityId", CityId);
                                intent.putExtra("FKTerritoryId", TerritoryId);
                                intent.putExtra("Age", Remark);
                                intent.putExtra("BusinessSegmentId", businessSegmentId);
                                intent.putExtra("website", website);
                                intent.putExtra("FKSourceOfProspect", sourceOfProspect);
                                intent.putExtra("businessDetails", businessDetails);
                                //intent.putExtra("",CityName);
                                Log.e("Api response", response);
                                System.out.println("Firm" + Firmname);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }

    }


    private void showProgressDialog() {


        // progressHUD = ProgressHUD.show(ProspectFilterActivity.this, "", false, false, null);

        progressbar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressDialog() {
        /*if (progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }*/

        if (progressbar != null && progressbar.isShown()) {
            progressbar.setVisibility(View.GONE);
        }
    }


}
