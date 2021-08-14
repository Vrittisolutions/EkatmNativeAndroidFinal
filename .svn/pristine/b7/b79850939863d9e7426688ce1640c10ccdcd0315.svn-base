package com.vritti.crm.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.OfflineBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.vcrm7.ActivityOfflineData;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Admin-1 on 7/26/2017.
 */

public class OfflineActivityAdapter extends BaseAdapter {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    static Context context;

    List<OfflineBean> mycount;
    private LayoutInflater mInflater;
    private static ActivityOfflineData actdata;
    SQLiteDatabase sql;
    public OfflineActivityAdapter(Context context, List<OfflineBean> Myteamcount) {
        this.mycount = Myteamcount;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        actdata = new ActivityOfflineData();

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
    }

    @Override
    public int getCount() {
        return mycount.size();
    }

    @Override
    public Object getItem(int position) {
        return mycount.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_custom_offlinedata, null);
            holder = new ViewHolder();
            holder.txt_remark = (TextView) convertView.findViewById(R.id.txtremark);
            holder.txt_date = (TextView) convertView.findViewById(R.id.txtdate);
            holder.img_menu = (ImageView) convertView.findViewById(R.id.optnmenu);
            holder.txt_status = (TextView) convertView.findViewById(R.id.txtstatus);
            holder.img_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int pos = (int) view.getTag();
                    if (mycount.get(pos).getIsUploaded().equalsIgnoreCase(WebUrlClass.FlagisUploadedFalse)) {
                        PopupMenu popup = new PopupMenu(context, view);
                        popup.inflate(R.menu.menu_offline);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()== R.id.send) {
                                    sendRecord(mycount.get(pos).getRecordID(),context);
                                }else  if (item.getItemId()== R.id.delete){
                                    deleteRecord(mycount.get(pos).getRecordID(), context);
                                }

                                return false;
                            }
                        });
                        popup.show();
                    } else if (mycount.get(pos).getIsUploaded().equalsIgnoreCase(WebUrlClass.FlagisUploadedTrue)) {
                        PopupMenu popup = new PopupMenu(context, view);
                        popup.inflate(R.menu.menu_offline1);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()== R.id.delete) {
                                    deleteRecord(mycount.get(pos).getRecordID(), context);
                                }

                                return false;
                            }
                        });
                        popup.show();
                    } else if (mycount.get(pos).getIsUploaded().equalsIgnoreCase(WebUrlClass.FlagisUploadedFailed)) {
                        PopupMenu popup = new PopupMenu(context, view);
                        popup.inflate(R.menu.menu_offline);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if(item.getItemId()== R.id.send) {
                                    sendRecord(mycount.get(pos).getRecordID(),context);
                                }else  if (item.getItemId()== R.id.delete){
                                    deleteRecord(mycount.get(pos).getRecordID(), context);
                                }

                                return false;
                            }
                        });
                        popup.show();
                    }
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img_menu.setTag(position);
        holder.txt_remark.setText(mycount.get(position).getRemark());
        holder.txt_date.setText(mycount.get(position).getAddedDt());
        if (mycount.get(position).getIsUploaded().equalsIgnoreCase(WebUrlClass.FlagisUploadedTrue)) {
            holder.txt_status.setText("Y");
            holder.txt_status.setTextColor(Color.GREEN);
        } else if (mycount.get(position).getIsUploaded().equalsIgnoreCase(WebUrlClass.FlagisUploadedFalse)) {
            holder.txt_status.setText("N");
            holder.txt_status.setTextColor(Color.RED);

        } else {
            holder.txt_status.setText("F");
            holder.txt_status.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView txt_remark, txt_date, txt_status;
        ImageView img_menu;
    }

    private void deleteRecord(String RecID, Context context) {

        sql.delete(db.TABLE_DATA_OFFLINE, "recordID=?",
                new String[]{RecID});
       // sql.close();
        actdata.updateData();

    }

    private void sendRecord(String RecID, final Context context) {
        String  RecordID = null;
        Object obj = null;
        int methodtype = 0;
        String Url = null, remark, date, is,output = null;
        //SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE recordID=?",
                new String[]{RecID});
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            try {
                RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                Url = cursor.getString(cursor.getColumnIndex("linkurl"));
                obj = cursor.getString(cursor.getColumnIndex("parameter"));
                methodtype = cursor.getInt(cursor.getColumnIndex("methodtype"));
                remark = cursor.getString(cursor.getColumnIndex("remark"));//output
                date = cursor.getString(cursor.getColumnIndex("AddedDt"));
                output = cursor.getString(cursor.getColumnIndex("output"));
                is = cursor.getString(cursor.getColumnIndex("isUploaded"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (methodtype == WebUrlClass.POSTFLAG) {
                String s = obj.toString();
                s = s.replace("\\\\", "");
                if (isInternetAvailable(context)) {
                    final String finalS = s;
                    final String url = Url;
                    final String Output = output;
                    final String finalRecordID = RecordID;
                    ActivityOfflineData.mProgress.setVisibility(View.VISIBLE);

                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                         new SendTask().execute(url, finalS,Output, finalRecordID);
                        }

                        @Override
                        public void callfailMethod(String msg) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            ActivityOfflineData.mProgress.setVisibility(View.GONE);

                        }
                    });

                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                }
            } else if (methodtype == WebUrlClass.GETFlAG) {
            String s = obj.toString();
            s = s.replace("\\\\", "");
            if (isInternetAvailable(context)) {
                final String finalS = s;
                final String url = Url;
                final String Output = output;
                final String finalRecordID = RecordID;
                ActivityOfflineData.mProgress.setVisibility(View.GONE);

                new StartSession(context, new CallbackInterface() {
                    @Override
                    public void callMethod() {
                        new SendTaskGet().execute(url,"",Output, finalRecordID);
                    }
                    @Override
                    public void callfailMethod(String msg) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                        ActivityOfflineData.mProgress.setVisibility(View.GONE);

                    }
                });
            } else {
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }
    public class SendTask extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenPostConnection(url, params[1],context);

                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            data.add(params[3]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ActivityOfflineData.mProgress.setVisibility(View.GONE);
           // SQLiteDatabase sql = db.getWritableDatabase();
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context,"Record send sucessfully",Toast.LENGTH_LONG).show();
                    deleteRecord(recid,context);
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context,"Can not perform this action as "+res,Toast.LENGTH_LONG).show();

                }

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                Toast.makeText(context,"Record not send due to server error",Toast.LENGTH_LONG).show();

            }
            actdata.updateData();
           // sql.close();
        }
    }

    public class SendTaskGet extends AsyncTask<String, Void, ArrayList<String>> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {

            try {
                String url = params[0];
                res = ut.OpenConnection(url,context);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            ArrayList<String> data = new ArrayList<String>();
            data.add(response);
            data.add(params[2]);
            data.add(params[3]);
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<String> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            ActivityOfflineData.mProgress.setVisibility(View.GONE);
           // SQLiteDatabase sql = db.getWritableDatabase();
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context,"Record send sucessfully",Toast.LENGTH_LONG).show();
                    deleteRecord(recid,context);
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context,"Can not perform this action as "+res,Toast.LENGTH_LONG).show();

                }

            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                Toast.makeText(context,"Record not send due to server error",Toast.LENGTH_LONG).show();

            }
           /* if (res.equalsIgnoreCase(stdres)){
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
               *//* sql.delete(db.TABLE_DATA_OFFLINE, "isUploaded=?",
                        new String[]{ WebUrlClass.FlagisUploadedTrue});*//*
                Toast.makeText(context,"Record send sucessfully",Toast.LENGTH_LONG).show();
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                Toast.makeText(context,"Record not send",Toast.LENGTH_LONG).show();

            }*/
            actdata.updateData();
           // sql.close();
        }
    }
    public static boolean isInternetAvailable(Context parent) {
        ConnectivityManager cm = (ConnectivityManager) parent
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
}
