package com.vritti.vwb.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.OfflineBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ActivityOfflineData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin-1 on 7/26/2017.
 */

public class OfflineActivityAdapter extends BaseAdapter {
    static String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    List<OfflineBean> mycount;
    static Context context;
    private LayoutInflater mInflater;
    private static ActivityOfflineData actdata;
    Utility ut;
    DatabaseHandlers db;


    public OfflineActivityAdapter(Context context, List<OfflineBean> Myteamcount) {
        this.mycount = Myteamcount;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        actdata = new ActivityOfflineData();
        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);


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
            convertView = mInflater.inflate(R.layout.vwb_custom_offlinedata, null);
            holder = new ViewHolder();
            holder.txt_remark = (TextView) convertView.findViewById(R.id.txtremark);
            holder.txt_date = (TextView) convertView.findViewById(R.id.txtdate);
            holder.img_menu = (ImageView) convertView.findViewById(R.id.vwb_optnmenu);
            holder.txt_status = (TextView) convertView.findViewById(R.id.txtstatus);
            holder.txt_status1 = (TextView) convertView.findViewById(R.id.txtstatus1);
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
                                int id = item.getItemId();
                               /* switch (item.getItemId()) {
                                    case R.id.send:
                                        sendRecord(mycount.get(pos).getRecordID(),context);
                                        break;
                                    case R.id.delete:
                                        deleteRecord(mycount.get(pos).getRecordID(), context);
                                        break;
                                }*/

                                if (id == R.id.send) {
                                    sendRecord(mycount.get(pos).getRecordID(), context);
                                } else if (id == R.id.delete) {
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
                                int id = item.getItemId();
                                /*switch (item.getItemId()) {
                                    case R.id.delete:
                                        deleteRecord(mycount.get(pos).getRecordID(), context);

                                        break;
                                }*/

                                if (id == R.id.delete) {
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
                              /*  switch (item.getItemId()) {
                                    case R.id.send:
                                        sendRecord(mycount.get(pos).getRecordID(), context);
                                        break;
                                    case R.id.delete:
                                        deleteRecord(mycount.get(pos).getRecordID(), context);
                                        break;
                                }*/
                                int id = item.getItemId();
                                if (id == R.id.send) {
                                    sendRecord(mycount.get(pos).getRecordID(), context);
                                } else if (id == R.id.delete) {
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

    public class ViewHolder {
        TextView txt_remark, txt_date, txt_status,txt_status1;
        ImageView img_menu;
    }

    private void deleteRecord(String RecID, Context context) {

        SQLiteDatabase sql = db.getWritableDatabase();
        sql.delete(db.TABLE_DATA_OFFLINE, "recordID=?",
                new String[]{RecID});
        sql.close();
        actdata.updateData();

    }

    private void sendRecord(String RecID, final Context context) {
        String RecordID = null;
        Object obj = null;
        int methodtype = 0;
        String Filename = "";

        String Url = null, remark, date, is, output = null;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor cursor = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE recordID=?",
                new String[]{RecID});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            try {
                RecordID = cursor.getString(cursor.getColumnIndex("recordID"));
                Url = cursor.getString(cursor.getColumnIndex("linkurl"));
                methodtype = cursor.getInt(cursor.getColumnIndex("methodtype"));
                if (methodtype == WebUrlClass.ATTACHMENTFlAG) {
                    Filename = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                } else {
                    obj = cursor.getString(cursor.getColumnIndex("parameter")); //for attachment it filename
                }
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

                            new SendTask().execute(url, finalS, Output, finalRecordID);
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

            } else if (methodtype == WebUrlClass.GETFlAG) {
                // String s = obj.toString();
                //  s = s.replace("\\\\", "");
                if (isInternetAvailable(context)) {
                    //      final String finalS = s;
                    final String url = Url;
                    final String Output = output;
                    final String finalRecordID = RecordID;
                    ActivityOfflineData.mProgress.setVisibility(View.VISIBLE);

                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new SendTaskGet().execute(url, "", Output, finalRecordID);
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
            } else if (methodtype == WebUrlClass.ATTACHMENTFlAG) {
                if (isInternetAvailable(context)) {
                    ActivityOfflineData.mProgress.setVisibility(View.VISIBLE);

                    new PostUploadImageMethod().execute(Url, Filename, output, RecordID);
                }

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
                res = ut.OpenPostConnection(url, params[1], context);

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
            SQLiteDatabase sql = db.getWritableDatabase();
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context, "Record send sucessfully", Toast.LENGTH_LONG).show();
                    deleteRecord(recid, context);
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else if (res.length() >= 36) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                } else {
                    Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                    int i = 0;
                    if ((c.getCount() > 0)) {
                        c.moveToFirst();
                        i = c.getInt(c.getColumnIndex("AttemptCount"));
                        i = i + 1;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
                    contentValues.put("AttemptCount", i);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                }

            } else {

                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});

                Toast.makeText(context, "Record not send due to server error", Toast.LENGTH_LONG).show();

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
            sql.close();
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
                res = ut.OpenConnection(url, context);
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
            SQLiteDatabase sql = db.getWritableDatabase();
            String res = result.get(0);
            String stdres = result.get(1);
            String recid = result.get(2);
            if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                if (res.equalsIgnoreCase(stdres)) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context, "Record send sucessfully", Toast.LENGTH_LONG).show();
                    deleteRecord(recid, context);
                    // Toast.makeText(getApplicationContext(),"Record send sucessfully",Toast.LENGTH_LONG).show();
                } else {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{recid});
                    Toast.makeText(context, "Can not perform this action as " + res, Toast.LENGTH_LONG).show();

                }

            } else {

                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + recid + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{recid});
                Toast.makeText(context, "Record not send due to server error", Toast.LENGTH_LONG).show();

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
            sql.close();
        }
    }

    public class PostUploadImageMethod extends AsyncTask<String, Void, ArrayList<String>> {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonimage = new JSONObject();
        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected ArrayList<String> doInBackground(String... urls) {

            try {
                HttpURLConnection conn = null;
                DataOutputStream dos = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1 * 1024 * 1024;
                File sourceFile = new File(urls[0]);

                String upLoadServerUri = CompanyURL + WebUrlClass.api_FileUpload;
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", urls[0]);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + urls[0] + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseMessage.equals("OK")) {


                    try {
                        jsonimage.put("File", urls[1]);
                        jsonArray.put(jsonimage);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                    if (serverResponseMessage.contains("Error")) {


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<String> Data = new ArrayList<>();
            Data.add(urls[0]);
            Data.add(urls[1]);
            Data.add(urls[2]);
            Data.add(urls[3]);

            return Data;

        }

        protected void onPostExecute(ArrayList<String> feed) {
            String path = feed.get(0);
            String Imagefilename = feed.get(1);
            String ActivityId = feed.get(2);
            String recid = feed.get(3);

            String Vendordata = "";
            if (Imagefilename != null) {
                JSONObject jsonObject = new JSONObject();
                JSONArray Idjsonarray = new JSONArray();
                try {
                    jsonObject.put("fileName", jsonArray);
                    jsonObject.put("ActivityId", ActivityId);

                    Vendordata = jsonObject.toString();
                    Vendordata = Vendordata.replaceAll("\\\\", "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (isInternetAvailable(context)) {
                    new SaveAttachment().execute(Vendordata, recid);
                } else {
                    //  Toast.makeText(getApplicationContext(), "No Internet Connetion", Toast.LENGTH_LONG);
                }
            }

        }
    }

    class SaveAttachment extends AsyncTask<String, Void, String> {
        String response;
        Object res;
        List<String> ls_pdf;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            ActivityOfflineData.mProgress.setVisibility(View.GONE);

            SQLiteDatabase sql = db.getWritableDatabase();
            if ((response != null) && (!response.equalsIgnoreCase(WebUrlClass.setError))) {
                if (response.equalsIgnoreCase("true")) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedTrue);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{integer});
                    deleteRecord(integer,context);
                    Toast.makeText(context, "Record send sucessfully", Toast.LENGTH_LONG).show();

                } else {
                    Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + integer + "'", null);
                    int i = 0;
                    if ((c.getCount() > 0)) {
                        c.moveToFirst();
                        i = c.getInt(c.getColumnIndex("AttemptCount"));
                        i = i + 1;
                    }
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
                    contentValues.put("AttemptCount", i);
                    sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                            new String[]{integer});

                }
            } else {

                Cursor c = sql.rawQuery("Select * From " + db.TABLE_DATA_OFFLINE + " Where recordID='" + integer + "'", null);
                int i = 0;
                if ((c.getCount() > 0)) {
                    c.moveToFirst();
                    i = c.getInt(c.getColumnIndex("AttemptCount"));
                    i = i + 1;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("isUploaded", WebUrlClass.FlagisUploadedFailed);
                contentValues.put("AttemptCount", i);
                sql.update(db.TABLE_DATA_OFFLINE, contentValues, "recordID=?",
                        new String[]{integer});


            }


            actdata.updateData();
            sql.close();


        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            url = CompanyURL + WebUrlClass.api_PostSaveAttachment;

            //url="http://192.168.1.53/api/TicketRegisterAPI/PostSaveAttachment";
            try {
                res = Utility.OpenPostConnection(url, params[0],context);
                response = res.toString();
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return params[1];
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
