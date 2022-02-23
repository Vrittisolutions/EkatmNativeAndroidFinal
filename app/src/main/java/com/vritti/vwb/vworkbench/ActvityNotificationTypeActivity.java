package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.NotificationTypeAdapter;
import com.vritti.vwb.classes.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by sharvari on 15-Mar-19.
 */

public class ActvityNotificationTypeActivity extends AppCompatActivity  {

    private ListView recyclerView;
    private ArrayList<Item> arrayList;
    NotificationTypeAdapter notificationTypeAdapter;
    ProgressBar progressBar;
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    private SharedPreferences userpreferences;
    private String response,url,TypeCode="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_type_lay);


        ut = new Utility();
        cf = new CommonFunctionCrm(ActvityNotificationTypeActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(ActvityNotificationTypeActivity.this);
        String dabasename = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(ActvityNotificationTypeActivity.this, dabasename);
        CompanyURL = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(ActvityNotificationTypeActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);

         arrayList=new ArrayList<>();

        Intent intent=getIntent();
        TypeCode=intent.getStringExtra("TypeCode");

        recyclerView= (ListView) findViewById(R.id.recyclerView);
        notificationTypeAdapter = new NotificationTypeAdapter(ActvityNotificationTypeActivity.this, arrayList);
        recyclerView.setAdapter(notificationTypeAdapter);



       /* recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        notificationTypeAdapter = new NotificationTypeAdapter(ActvityNotificationTypeActivity.this, arrayList, this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(notificationTypeAdapter);
*/

        if (getIntent() != null) {
            String back = getIntent().getStringExtra("back");

            if (back==null){
                if (isnet()) {
                    showProgress();
                    new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadNotificationTypeJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }else {
            if (back.equals("1")) {

                if (isnet()) {
                    showProgress();
                    new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadNotificationTypeJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }




        }
        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String TypeName=arrayList.get(position).getTypeName();
                startActivityForResult(new Intent(ActvityNotificationTypeActivity.this,NotificationActivity.class)
                        .putExtra("TypeName",TypeName)
                        .putExtra("readType" , "0")
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) , 119);
            }
        });

    }

    /*public void setmessageread(final String typename, final String typecode,final String id) {




        if (isnet()) {
            showProgress();
            new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadReadNotificationJSON().execute(typename,typecode,id);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }



    }*/


    public void setmessageunread(final String typename, final String typecode,final String id) {



        if (isnet()) {
            showProgress();
            new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUnReadNotificationJSON().execute(typename,typecode,id);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }



    }

    public void setmessageRead(final String typename, final String typecode, final String id) {

        if (isnet()) {
            showProgress();
            new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadReadNotificationJSON().execute(typename,typecode,id);
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /*@Override
    public void onItemClick(Item item) {

        String TypeName=item.TypeName;
        startActivity(new Intent(ActvityNotificationTypeActivity.this,NotificationActivity.class).putExtra("TypeName",TypeName).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }*/


    class DownloadNotificationTypeJSON extends AsyncTask<String, Void, String> {

        Object res;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = CompanyURL + WebUrlClass.api_GetNotifyTypeDD + "?NotificationType=News";
                res = ut.OpenConnection(url, ActvityNotificationTypeActivity.this);
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


            if (response.equalsIgnoreCase("error")||integer.equals("[]")) {
            //    Toast.makeText(getApplicationContext(), "Server error occurred..try after sometime", Toast.LENGTH_LONG).show();
            }

                try {
                    JSONArray jResults = new JSONArray(response);

                    arrayList.clear();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {

                        JSONObject jorder = jResults.getJSONObject(i);

                        Item cust = new Item();
                        cust.setTypeName(jorder.getString("TypeName"));
                        cust.setTypeCode(jorder.getString("TypeCode"));
                        cust.setNotRead(jorder.getString("NotRead"));
                        cust.setReadNotification(jorder.getString("ReadNotification"));
                        cust.setPKNotificationId(jorder.getString("PKNotificationId"));

                        arrayList.add(cust);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                notificationTypeAdapter.notifyDataSetChanged();

                //updateCustList(CusID, flagloc);

            hideProgress();
        }


    }

    class DownloadReadNotificationJSON extends AsyncTask<String, Void, String> {

        Object res;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = CompanyURL + WebUrlClass.api_GetReadNotification + "?TypeName="+URLEncoder.encode(params[0],"UTF-8")+"&DateType="+params[1]+"&NotifyId="+params[2];
                res = ut.OpenConnection(url, getApplicationContext());
                response = res.toString();

                // response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                //response = response.replaceAll("\\\\", "");
                //response = response.replaceAll("u0026", "&");
                //response = response.substring(1, response.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            hideProgress();
            if (integer.equalsIgnoreCase("error")||integer.contains("[]")) {
                Toast.makeText(getApplicationContext(), "No Record found", Toast.LENGTH_LONG).show();
            }

            try {

                startActivityForResult(new Intent(ActvityNotificationTypeActivity.this,NotificationActivity.class)
                        .putExtra("readType" , "1")
                        .putExtra("TypeName" , response)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) , 119);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //updateCustList(CusID, flagloc);

        }


    }



    public class DownloadUnReadNotificationJSON extends AsyncTask<String, Void, String> {

        Object res;
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            showProgress();

        }

        @Override
        protected String doInBackground(String... params) {

            String name=params[0];
            final String code=params[1];
            final String nid=params[2];

            try {
                String url = CompanyURL + WebUrlClass.api_GetUnreadNotification + "?TypeName="+URLEncoder.encode(name,"UTF-8")+"&DateType="+code+"&NotifyId="+nid;
                res = ut.OpenConnection(url, ActvityNotificationTypeActivity.this);
                response = res.toString();
            //    response = res.toString().replaceAll("\\\\", "");
              //  response = response.replaceAll("\\\\\\\\/", "");
                //response = response.substring(1, response.length() - 1);


                if (response.equalsIgnoreCase("error")||response.equals("[]")) {

                }else{
                   /* new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new ReadNotificationJSON().execute(code,nid);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });*/
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


            hideProgress();
            if (response.equalsIgnoreCase("error")||integer.equals("[]")) {
                Toast.makeText(getApplicationContext(), "No Record found", Toast.LENGTH_LONG).show();

                hideProgress();
            }else {
                hideProgress();
                startActivityForResult(new Intent(ActvityNotificationTypeActivity.this,NotificationActivity.class)
                        .putExtra("readType" , "1")
                        .putExtra("TypeName" , response)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) , 119);


            }

            try {


            } catch (Exception e) {
                e.printStackTrace();
            }

            //updateCustList(CusID, flagloc);

        }


    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
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

    private class ReadNotificationJSON extends AsyncTask<String, Void, String>  {
        Object res;
        String response;
        @Override
        protected String doInBackground(String... strings) {
            final String code = strings[0];
            final String nid = strings[1];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Notifdtlsid" , code);
                jsonObject.put("NotifyId" , nid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String url = CompanyURL + WebUrlClass.api_POSTNotificationRead;// + "?TypeName="+URLEncoder.encode(name,"UTF-8")+"&DateType="+code+"&NotifyId="+nid;
                res = ut.OpenPostConnection(url, jsonObject.toString() , ActvityNotificationTypeActivity.this);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(requestCode == 119 && resultCode == 120){
                if (isnet()) {
                    showProgress();
                    new StartSession(ActvityNotificationTypeActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadNotificationTypeJSON().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }
    }
}
