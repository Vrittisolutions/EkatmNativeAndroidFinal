package com.vritti.vwb.vworkbench;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.NotificationAdapter;
import com.vritti.vwb.Beans.NotificationBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NotificationActivity extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView ls_notification;

    SQLiteDatabase sql;
    ArrayList<NotificationBean> notificationBeanArrayList;
    ProgressBar progressBar;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    String res, response,NotificationTypeId,PKNotifDtlsId;
    private final int MEGABYTE = 1024 * 1024;
    private String readType;
    private NotificationAdapter notificationAdapter;
    private int pos;

    // BirthdayMainAdapter BirthdayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_notification);
        InitView();

    }

    private void InitView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunction(context);
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
        notificationBeanArrayList = new ArrayList<NotificationBean>();
        ls_notification = (ListView) findViewById(R.id.ls_notification);
        progressBar = findViewById(R.id.toolbar_progress_App_bar);

        String type = ""; // readType 0 all , 1 read  , 2 unread.
        if (getIntent() != null) {
            readType = getIntent().getStringExtra("readType");
            type = getIntent().getStringExtra("TypeName");
            if (readType.equals("0"))
                UpdateBirthdayList(type);
            else if (readType.equals("1"))
                readAndUnReadData(type);

        }



        ls_notification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (readType.equals("1")){
                    NotificationTypeId = notificationBeanArrayList.get(position).getNotificationTypeId();
                    PKNotifDtlsId = notificationBeanArrayList.get(position).getPKNotifDtlsId();
                    pos=position;

                    new ReadNotificationJSON().execute(NotificationTypeId,PKNotifDtlsId);

                }


            }
        });
    }

    private void readAndUnReadData(String type) {
        try {
            JSONArray jResults = new JSONArray(type);
            for (int i = 0; i < jResults.length(); i++) {

                JSONObject jorder = jResults.getJSONObject(i);


                NotificationBean notificationBean = new NotificationBean();
                notificationBean.setNotificationTypeId(jorder.getString("NotificationTypeId"));
                notificationBean.setAddedDt(jorder.getString("AddedDt"));
                notificationBean.setNotifText(jorder.getString("NotifContent"));
                notificationBean.setNotifTitle(jorder.getString("NotifTitle"));
                notificationBean.setPKNotifDtlsId(jorder.getString("PKNotifDtlsId"));
                notificationBean.setTypeName(jorder.getString("TypeName"));
                notificationBean.setUserName(jorder.getString("UserName"));
                notificationBean.setAttachment(jorder.getString("Attachment"));
                notificationBean.setAttachGuid(jorder.getString("AttachGuid"));
                notificationBeanArrayList.add(notificationBean);
                notificationAdapter = new NotificationAdapter
                        (NotificationActivity.this, notificationBeanArrayList);
                ls_notification.setAdapter(notificationAdapter);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void UpdateBirthdayList(String type) {
        String query = "SELECT * FROM " + db.TABLE_NOTIFICATION + " WHERE  TypeName ='" + type + "'";
        ;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                NotificationBean notificationBean = new NotificationBean();
                notificationBean.setNotificationTypeId(cur.getString(cur.getColumnIndex("NotificationTypeId")));
                notificationBean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                notificationBean.setNotifText(cur.getString(cur.getColumnIndex("NotifText")));
                notificationBean.setNotifTitle(cur.getString(cur.getColumnIndex("NotifTitle")));
                notificationBean.setPKNotifDtlsId(cur.getString(cur.getColumnIndex("PKNotifDtlsId")));
                notificationBean.setTypeName(cur.getString(cur.getColumnIndex("TypeName")));
                notificationBean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                notificationBean.setAttachment(cur.getString(cur.getColumnIndex("Attachment")));
                notificationBean.setAttachGuid(cur.getString(cur.getColumnIndex("AttachGuid")));
                notificationBeanArrayList.add(notificationBean);

            } while (cur.moveToNext());
        } else {
           /* NotificationBean notificationBean = new NotificationBean();
            notificationBean.setNotificationTypeId("");
            notificationBean.setAddedDt("");
            notificationBean.setNotifText("");
            notificationBean.setNotifTitle("");
            notificationBean.setPKNotifDtlsId("");
            notificationBean.setTypeName("");
            notificationBean.setUserName("");
            notificationBeanArrayList.add(notificationBean);*/

        }
        notificationAdapter = new NotificationAdapter
                (NotificationActivity.this, notificationBeanArrayList);
        ls_notification.setAdapter(notificationAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();
        setResult(120 , intent);
        finish();

       /* startActivity(new Intent(NotificationActivity.this,ActvityNotificationTypeActivity.class)
        .putExtra("back","1"));
        finish();*/
    }

    public void downloadAttachment(int position) {
        String attachmentId = notificationBeanArrayList.get(position).getAttachGuid();
        String attachmentName = notificationBeanArrayList.get(position).getAttachment();

        final String[] attachmentGidList = attachmentId.split("!");
        final String[] attachmentNameList = attachmentName.split("!");

        // DownloadActivity downloadActivity = new DownloadActivity(this , attachmentGidList , attachmentNameList);
        if (isnet()) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    int i = 0;
                    for (String attachmentId : attachmentGidList) {
                        new DownloadAttachmentGetCordMethod().execute(attachmentId, attachmentNameList[i]);

                        i++;
                    }
                    // ((NotificationActivity)context).showPopUp(true);

                }

                @Override
                public void callfailMethod(String msg) {
                    ((NotificationActivity) context).showPopUp(false);
                }
            });

        }
    }

    private boolean isnet() {
        Context context1 = context.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context1
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void sendResult(String name) {
        Toast.makeText(this, "File" + name + "is Download completed", Toast.LENGTH_LONG).show();
    }

    public void sendResult1(String name) {
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
    }

    public void showPopUp(boolean b) {


        if (b)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    private class DownloadAttachmentGetCordMethod extends AsyncTask<String, Void, String> {

        String attachmentName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);

        }

        @Override
        protected String doInBackground(String... strings) {
            String attachmentId = strings[0];
            attachmentName = strings[1];
            String url = CompanyURL + WebUrlClass.getApi_AttachmentPath + attachmentId;
            try {
                res = ut.OpenConnection(url, context);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                // ((NotificationActivity)context).showPopUp(false);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Psth : ", s);
            callAgainApi(s, attachmentName);
        }
    }

    private void callAgainApi(final String path, final String attachmentName1) {
        if (isnet()) {
            String path1 = Environment.getExternalStorageDirectory()
                    .toString();
            File file = new File(path1 + "/" + "Vwb" + "/" + "File");
            if (file.exists()) {
                final File fileNew = new File(file + "/" + attachmentName1);
                if (fileNew.exists()) {
                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            pDialog.dismiss();
                            Toast.makeText(context, "File Already downloaded", Toast.LENGTH_SHORT).show();
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                            newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                context.startActivity(newIntent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                            }
                            //((NotificationActivity)context).sendResult(directory.getName());


                        }
                    });
                } else {
                    cllDownloadApi(path, attachmentName1);
                }
            } else {
                cllDownloadApi(path, attachmentName1);
            }

        }
    }

    private void cllDownloadApi(final String path, final String attachmentName1) {
        new StartSession(context, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadFileApi().execute(path, attachmentName1);
            }

            @Override
            public void callfailMethod(String msg) {
                // ((NotificationActivity)context).showPopUp(false);
            }
        });
    }

    private class DownloadFileApi extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String pathName = strings[0];
            final String fileName = strings[1];
            int count;
            File file=null;
            String urlStr = CompanyURL + "/Downloads/" + pathName + "/" + fileName;
            try {
                String path1 = Environment.getExternalStorageDirectory()
                        .toString();

                if (Constants.type == Constants.Type.Sahara){
                    file = new File(path1 + "/" + "Sahara" + "/" + "File");

                }else {
                     file = new File(path1 + "/" + "Ekatm" + "/" + "File");
                }
                if (!file.exists())
                    file.mkdirs();
                //   pdfFile = new File(file + "/" + fileName);
                // file1 = String.valueOf(pdfFile);


                try {
                    //pdfFile = File.createTempFile(filename /* prefix */,prefix, pdfFile /* directory */);

                    final File fileNew = new File(file + "/" + fileName);
                    fileNew.createNewFile();
                    //downloadFileInloacl(url , new File(file + "/" + fileNew));

                    try {
                        urlStr = urlStr.replaceAll(" ", "%20");
                        //final File directory =  new File(file + "/" + fileNew);
                        URL url = new URL(urlStr);

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        long total = 0;


                        FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
                        InputStream inputStream = urlConnection.getInputStream();
                        int totalSize = urlConnection.getContentLength();
                        int serverResponseCode = urlConnection.getResponseCode();
                        String serverResponseMessage = urlConnection.getResponseMessage();
                        byte[] buffer = new byte[MEGABYTE];
                        int bufferLength = 0;
                        while ((bufferLength = inputStream.read(buffer)) > 0) {
                            total += bufferLength;
                            fileOutputStream.write(buffer, 0, bufferLength);
                            onProgressUpdate("" + (int) ((total * 100) / lenghtOfFile));
                        }
                       /* while ((count = inputStream.read(buffer)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            onProgressUpdate(""+(int)((total*100)/lenghtOfFile));
                            fileOutputStream.write(buffer, 0, bufferLength);
                            // writing data to file

                        }*/
                        /*while ((bufferLength = inputStream.read(buffer)) > 0) {

                        }*/
                        fileOutputStream.close();


                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                String file=fileNew.getAbsolutePath();
                                if(file.contains("jpg")||file.contains("png")||file.contains("jpeg")||file.contains("JPG")||file.contains("PNG")||file.contains("JPEG")){
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(fileNew), "image/*");
                                    startActivity(intent);
                                }else{

                                    newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        context.startActivity(newIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                    }
                                    //((NotificationActivity)context).sendResult(directory.getName());
                                }

                            }
                        });


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        // ((NotificationActivity)context).showPopUp(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //((NotificationActivity)context).showPopUp(false);
                    }
                    //publishProgress(""+(int)((total*100)/lenghtOfFile));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                // ((NotificationActivity)context).showPopUp(false);
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissDialog(progress_bar_type);
        }
    }

    private void downloadFileInloacl(String fileUrl, final File directory) {

        try {
            fileUrl = fileUrl.replaceAll(" ", "%20");

            URL url = new URL(fileUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            int lenghtOfFile = urlConnection.getContentLength();


            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            InputStream inputStream = urlConnection.getInputStream();
            int totalSize = urlConnection.getContentLength();
            int serverResponseCode = urlConnection.getResponseCode();
            String serverResponseMessage = urlConnection.getResponseMessage();
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();


            Handler handler = new Handler(context.getMainLooper());
            handler.post(new Runnable() {
                public void run() {

                    ((NotificationActivity) context).sendResult(directory.getName());


                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //  ((NotificationActivity)context).showPopUp(false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // ((NotificationActivity)context).showPopUp(false);
        } catch (IOException e) {
            e.printStackTrace();
            //  ((NotificationActivity)context).showPopUp(false);
        } catch (Exception e) {
            e.printStackTrace();
            // ((NotificationActivity)context).showPopUp(false);
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
                jsonObject.put("NotificationTypeId" , code);
                jsonObject.put("NotifyId" , nid);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String url = CompanyURL + WebUrlClass.api_POSTNotificationRead;// + "?TypeName="+URLEncoder.encode(name,"UTF-8")+"&DateType="+code+"&NotifyId="+nid;
                res = ut.OpenPostConnection(url, jsonObject.toString() , NotificationActivity.this);
                response = res.toString().replaceAll("\\\\", "");

            } catch (Exception e) {
                e.printStackTrace();
                response = "error";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("false")){
                notificationBeanArrayList.remove(pos);

            }
        }
    }


}
