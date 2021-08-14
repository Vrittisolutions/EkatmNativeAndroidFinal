package com.vritti.crmlib.adapter;

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
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.FilteredProspect;
import com.vritti.crmlib.classes.CallHistory;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.CommonObjectProperties;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;


/**
 * Created by Admin-1 on 4/27/2017.
 */

public class ProspectFilterAdapter extends RecyclerView.Adapter<ProspectFilterAdapter.Viewholder> {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    static Context context;

    List<FilteredProspect> prospectList;
    private String PKSuspectId,FinalJson;
    CommonObjectProperties commonObj;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;

    public ProspectFilterAdapter(List<FilteredProspect> prospectList,Context mContext) {
        this.prospectList = prospectList;
        this.context= mContext;
        commonObj = new CommonObjectProperties();
        /*db = new DatabaseHandler(context);
        sql = db.getWritableDatabase();
        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        CompanyURL = userpreferences.getString("CompanyURL", null);*/
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
        if (position % 2 == 1) {
            holder.lay_header.setBackgroundColor(Color.parseColor("#CBDFE2"));
        } else {
            holder.lay_header.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }

        commonObj = new CommonObjectProperties();


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

        sql = db.getWritableDatabase();


        holder.txtFName.setText(prospectList.get(position).getFirmName());
        holder.txtcity.setText(prospectList.get(position).getCityName());
        holder.txtItemdesc.setText(prospectList.get(position).getFamilyDesc());
      //  holder.txtcallclose.setText(prospectList.get(position).getCloseCalls() + " Close call");
        System.out.println(" CloseCall :"+prospectList.get(position).getCloseCalls());
        //holder.txtcallopen.setText(prospectList.get(position).getOpenCalls() + " Open call");
        System.out.println("OpenCall :"+prospectList.get(position).getOpenCalls());
        holder.txtAddress.setText(prospectList.get(position).getAddress());
        if (holder.llName.getVisibility() == View.VISIBLE) {
            holder.llName.setVisibility(View.GONE);
        }

         int opencall= Integer.parseInt(prospectList.get(position).getOpenCalls());

        System.out.println("Open :"+opencall);
         int closecall= Integer.parseInt(prospectList.get(position).getCloseCalls());
        System.out.println("Open-1 :"+closecall);

        if (opencall>0){
            holder.txtcallopen.setVisibility(View.VISIBLE);
            holder.txtcallopen.setText(prospectList.get(position).getOpenCalls() + " Open calls");

        }else {
            holder.txtcallopen.setVisibility(View.GONE);

        }
        if (closecall>0){
            holder.txtcallclose.setVisibility(View.VISIBLE);
            holder.txtcallclose.setText(prospectList.get(position).getCloseCalls() + " Close calls");

        }else {
            holder.txtcallclose.setVisibility(View.GONE);
        }


        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.llName.getVisibility() == View.VISIBLE) {
                    holder.llName.setVisibility(View.GONE);
                } else {
                    holder.llName.setVisibility(View.VISIBLE);
                    holder.txtName.setText(prospectList.get(position).getContactName());
                    holder.txtNumber.setText(prospectList.get(position).getTelephone() + "/" +
                            prospectList.get(position).getMobile());
                    holder.txtEmail.setText(prospectList.get(position).getEmailId());
                }


            }
        });


        holder.txtcallopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonObj = new CommonObjectProperties();

                 JSONObject jsoncommonObj = commonObj.DataObj();
                 JSONObject jsonCallsObject;
                 PKSuspectId=prospectList.get(position).getPKSuspectId();


                try {
                    jsonCallsObject = jsoncommonObj.getJSONObject("ProspectId");
                    jsonCallsObject.put("IsSet", true);
                    jsonCallsObject.put("value1", PKSuspectId);

                    jsonCallsObject = jsoncommonObj.getJSONObject("Isclose");
                    jsonCallsObject.put("IsSet", true);
                    jsonCallsObject.put("value1", "N");//close =Y
                    jsonCallsObject.put("Operator", "eq");

                    FinalJson=jsoncommonObj.toString();
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


        holder.txtcallclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonObj = new CommonObjectProperties();

                JSONObject jsoncommonObj = commonObj.DataObj();
                JSONObject jsonCallsObject;
                PKSuspectId=prospectList.get(position).getPKSuspectId();


                try {
                    jsonCallsObject = jsoncommonObj.getJSONObject("ProspectId");
                    jsonCallsObject.put("IsSet", true);
                    jsonCallsObject.put("value1", PKSuspectId);

                    jsonCallsObject = jsoncommonObj.getJSONObject("Isclose");
                    jsonCallsObject.put("IsSet", true);
                    jsonCallsObject.put("value1", "Y");//close =Y
                    jsonCallsObject.put("Operator", "eq");

                    FinalJson=jsoncommonObj.toString();
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

    static class Viewholder extends RecyclerView.ViewHolder{
        TextView txtFName, txtName;
        TextView txtcity, txtNumber;
        TextView txtItemdesc, txtEmail;
        TextView txtcallclose, txtcallopen, txtAddress;
        LinearLayout llName;
        RelativeLayout lay_header;
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
            lay_header = (RelativeLayout) itemView.findViewById(R.id.lay_header);
            click = (ImageView) itemView.findViewById(R.id.click);
            itemView.setTag(itemView);
        }
    }

    class DownloadCallInformationData extends AsyncTask<String, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_Get_Call;
                    res = ut.OpenPostConnection(url,FinalJson);
                    response = res.toString().replaceAll("\\\\", "");
                    response = response.replaceAll("\\\\\\\\/", "");
                    response = response.substring(1, response.length() - 1);
                 } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
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
                Intent intent = new Intent(context, CallHistory.class);
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

}
