package com.vritti.databaselib.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.databaselib.other.org.apache.http.entity.mime.HttpMultipartMode;
import com.vritti.databaselib.other.org.apache.http.entity.mime.MultipartEntity;
import com.vritti.databaselib.other.org.apache.http.entity.mime.content.FileBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;



import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Admin-1 on 10/4/2016.
 */
public class Utility {
Context context;

    public static DefaultHttpClient httpClient = new DefaultHttpClient();

    public boolean isNet(Context context) {
        // TODO Auto-generated method stub
        ConnectivityManager cm = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        try{
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                return true;

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public void showProgress(RelativeLayout mlayout) {

        mlayout.setVisibility(View.VISIBLE);
    }

    public void hideProgress(RelativeLayout mlayout) {

        mlayout.setVisibility(View.GONE);
    }

    public void displayToast(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static String OpenConnection(String url) {
        String res = "";
        try {
            HttpGet httpget = new HttpGet(url);
            httpget.setHeader("Accept", "application/json");
            httpget.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);

        }catch (IOException e){
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static Object OpenPostConnection_test(String url, String FinalObj, Context mContext) {
        String res = null;
        Object response = "";
        InputStream inputStream = null;
        HttpPost httppost = new HttpPost(url);
        StringEntity se = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);
        try {
            se = new StringEntity("");
            httppost.setHeader("cache-control", "no-cache");
            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpClient.execute(httppost, responseHandler);

            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Error",e.getMessage());
            response = e.getMessage();

        }
        return response;
    }

    public static Object OpenPostConnection_test(String url, String FinalObj) {
        String res = "";
        Object response = null;
        try {
            URL url1 = new URL(url);
            HttpPost httppost = new HttpPost(String.valueOf(url1));
            StringEntity se = new StringEntity(FinalObj);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httppost, responseHandler);
            Log.i("Common Data", response + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            HttpPost httppost = new HttpPost(url.toString());
            StringEntity se = new StringEntity(FinalObj.toString());
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httppost, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Object OpenMultiPart(String url, File file) {
        String res = null;
        Object response = null;
        try {
            MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            HttpPost httppost = new HttpPost(url);
            multipartEntity.addPart("UploadedImage", new FileBody(file));
            httppost.setHeader("Connection", "Keep-Alive");
            httppost.setHeader("ENCTYPE", "multipart/form-data");
            httppost.setHeader("Accept", "application/json");
            httppost.setEntity(multipartEntity);
            ResponseHandler responseHandler = new BasicResponseHandler();
            response = httpClient.execute(httppost, responseHandler);
            Log.i("Common Data", response + "");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String OpenConnection(String url, Context mContext) {
        String res = "";
        InputStream inputStream = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);
        try {

            HttpGet httppost = new HttpGet(url.toString());
            httppost.setHeader("Accept", "application/json");
            //  httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Content-type", "application/json");

            HttpResponse response = null;


            response = httpClient.execute(httppost);
           /* inputStream = response.getEntity().getContent();
               String result;
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/


            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);
            long consumedNow = entity.getContentLength();
            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, consumed);
            editor.commit();
            Log.e("consumed data :", "" + consumedNow);
        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());

            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }

        return res;
    }

    /*Added by Chetana*/
    public static  String OpenconnectionOrferbilling(String url,Context mContext){
        String res = null;
        URLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            url = url.replaceAll(" ","%20");

            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpClient.execute(httppost);
            HttpEntity entity = response.getEntity();

            res = EntityUtils.toString(entity);
            Log.e("Response", res);

        } catch (Exception e) {
            res = "error";
            e.printStackTrace();
        }
        return res;
    }

    public static String OpenConnection_ekatm(String url,Context mContext) {
        String res = null;
        InputStream inputStream = null;
        url = url.replaceAll(" ","%20");
        try {
            HttpGet httppost = new HttpGet(url);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = null;
            response = httpClient.execute(httppost);

            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);

        } catch (IOException e) {
            e.printStackTrace();

        }

        return res;
    }


  /*  public static Object MultiOpenPostConnection(String url, File sourceFile , Context mContext ,String urls) {
        String res = null;
        Object response = null;
        InputStream inputStream = null;
        HttpPost httppost = new HttpPost(url);
        StringEntity se = null;
        String boundary = "*****";
        DataOutputStream dos = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);
        try {
           // se = new StringEntity(FinalObj);
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            // Don't use a Cached Copy

            httppost.setHeader("Connection", "Keep-Alive");
            httppost.setHeader("ENCTYPE", "multipart/form-data");
            httppost.setHeader("Content-Type", "multipart/form-data;boundary=" + boundary);
            httppost.setHeader("UploadedImage", urls);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpClient.execute(httppost, responseHandler);


            long consumedNow = httppost.getEntity().getContentLength();
            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, consumed);
            editor.commit();

            dos = new DataOutputStream(httppost.());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"UploadedImage\";filename=\""
                    + sourceFile + "\"" + lineEnd);
            // dos.writeBytes("Content-Disposition: form-data; name=\"UploadedImage\"; filename=\""+urls[0]+"\"\r\nContent-Type: image/jpeg\r\n\r\n\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
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


        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());
            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }


        return response;
    }*/
    public static Object OpenPostConnection(String url, String FinalObj, Context mContext) {
        String res = "";
        Object response = "";
        InputStream inputStream = null;
        HttpPost httppost = new HttpPost(url);
        StringEntity se = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);

        try {
          //  se = new StringEntity(FinalObj);
            se = new StringEntity(FinalObj, HTTP.UTF_8);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpClient.execute(httppost, responseHandler);

            long consumedNow = httppost.getEntity().getContentLength();
            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, consumed);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
            response = e.getMessage().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());
            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }
        return response;
    }

    public static void WriteErrLogFile(String Error) {
        SimpleDateFormat dff = new SimpleDateFormat("dd_MMM_yyyy");
        String Logfile = dff.format(new Date());
        String a = Environment.getExternalStorageDirectory().toString();
        File Logsdir = new File(Environment.getExternalStorageDirectory()
                + "/EkatmLogs");

        if (!Logsdir.exists()) {
            Logsdir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + "/EkatmLogs", Logfile + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("\n" + "*" + Error + "\n");
                myOutWriter.close();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.append("\n" + "*" + Error + "\n");
                myOutWriter.close();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            //redraw the container layout.
            // container.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("dialog_action", "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    public String getSharedPreference_URL(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String LOG_IN_URL = prefs.getString(WebUrlClass.MyPREFERENCES_URL_KEY, "");
        return LOG_IN_URL;
    }

    public String getSharedPreference_EnvMasterID(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_EnvMasterID_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_PlantID(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_PlantID_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_plantName(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_PlantName_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_UserloginID(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_LOGIN_KEY, "");
        return EnvMasterID;
    }


    public String getSharedPreference_UserMasterID(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_USERMASTER_ID_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_Designation(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String Designation = prefs.getString(WebUrlClass.MyPREFERENCES_Designation_KEY, "");
        return Designation;
    }

    public String getSharedPreference_Psw(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_PSW_KEY, "");
        return EnvMasterID;
    }


    public String getSharedPreference_Mobile(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_MOBILE_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_SettingKey(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_SETTING_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_isCRMUser(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_IS_CRMUSER_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_isGPSLocation(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_IS_GPS_LOCATION_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_isChatApplicable(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_IS_CHAT_APPLICABLE_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_getUsername(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_USERNAME_KEY, "");
        return EnvMasterID;
    }

    public String getSharedPreference_getFCMToken(Context mcontext) {
        SharedPreferences prefs = mcontext.getSharedPreferences(WebUrlClass.MyPREFERENCES, mcontext.MODE_PRIVATE);
        String EnvMasterID = prefs.getString(WebUrlClass.MyPREFERENCES_FIREBASE_TOKEN_KEY, "");
        return EnvMasterID;
    }


    public String getValue(Context mContext, String columnname, String settingkey) {
        String val = "";
        DatabaseHandlers db = new DatabaseHandlers(mContext);// basic database main.db
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select " + columnname + " from " + db.TABLE_LOGIN_SETTING + " where LogInKey='" + settingkey + "'", null);
        int Count = c.getCount();
        if (Count > 0) {
            c.moveToFirst();
            do {
                val = c.getString(c.getColumnIndex(columnname));

            } while (c.moveToNext());
        }
        db.close();
        sql.close();
        return val;
    }


    public static Object OpenPostConnection1(String url, String FinalObj, Context mContext) {
        String res = "";
        Object response = null;
        InputStream inputStream = null;
        HttpPost httppost = new HttpPost(url);
        StringEntity se = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);

        try {
            se = new StringEntity(FinalObj);
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");


            ResponseHandler responseHandler = new BasicResponseHandler();

            response = httpClient.execute(httppost, responseHandler);

            long consumedNow = httppost.getEntity().getContentLength();
            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, consumed);
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());
            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }
        return response;
    }


    public static String POSTOpenConnection(String url, Context mContext) {
        String res = "";
        InputStream inputStream = null;
        SharedPreferences sp = mContext.getSharedPreferences(WebUrlClass.PREFERENCE_DATA_CALCULATION, Context.MODE_PRIVATE);
        try {

            HttpPost httppost = new HttpPost(url.toString());
            httppost.setHeader("Accept", "application/json");
            //  httppost.setHeader("Content-type", "application/json");
            httppost.setHeader("Content-type", "application/json");

            HttpResponse response = null;


            response = httpClient.execute(httppost);
           /* inputStream = response.getEntity().getContent();
               String result;
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";*/


            HttpEntity entity = response.getEntity();
            res = EntityUtils.toString(entity);
            long consumedNow = entity.getContentLength();
            long consumed = 0;
            long check = sp.getLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, 0);
            if (check == 0) {
                consumed = consumedNow;
            } else {
                consumed = check;
                consumed += consumedNow;
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(WebUrlClass.PREFERENCE_CONSUMEDDATA_KEY, consumed);
            editor.commit();
            Log.e("consumed data :", "" + consumedNow);
        } catch (Exception e) {
            e.printStackTrace();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm:ss aa");
            String Ldate = sdf.format(new Date());
            StackTraceElement l = new Exception().getStackTrace()[0];
            System.out.println(l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber());

            String data = l.getClassName() + "/" + l.getMethodName()
                    + ":" + l.getLineNumber() + "	" + e.getMessage()
                    + " " + Ldate;
            WriteErrLogFile(data);
        }

        return res;
    }


}
