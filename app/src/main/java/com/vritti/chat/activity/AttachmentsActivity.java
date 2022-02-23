package com.vritti.chat.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.BuildConfig;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AttachmentAdapter;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.EditDatasheetActivityMain;
import com.vritti.vwb.vworkbench.NotificationActivity;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

import static com.vritti.vwb.vworkbench.NotificationActivity.progress_bar_type;

/**
 * Created by sharvari on 05-Apr-18.
 */

public class AttachmentsActivity extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    ListView spinner_attachment;
    WebView webview_attachment;
    SharedPreferences userpreferences;
    String ActivityId, SourceId;

    SQLiteDatabase sql;
    ProgressBar mprogress;
    private MediaPlayer mediaPlayer;
    ArrayList<Customer> ls_pdf;
    AttachmentAdapter attachmentAdapter;
    private String FileName,Path,attachmentId;
    private final int MEGABYTE = 1024 * 1024;
    private ProgressDialog pDialog;
    private String path1="";


    String res, response,NotificationTypeId,PKNotifDtlsId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_attachments_lay);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  Attachments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.vworkbench);
        setSupportActionBar(toolbar);

        context = AttachmentsActivity.this;
        ut = new Utility();
        cf = new CommonFunction(context);
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
        spinner_attachment= (ListView) findViewById(R.id.spinner_attachment);
        webview_attachment= (WebView) findViewById(R.id.webview_attachment);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        Intent intent=getIntent();
        SourceId = intent.getStringExtra("SourceId");
        ActivityId = intent.getStringExtra("ActivityId");
        mediaPlayer=new MediaPlayer();

        if (isnet()) {
            new StartSession(AttachmentsActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadUploadAttachment().execute();
                }

                @Override
                public void callfailMethod(String msg) {

                }
            });
        }




        spinner_attachment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileName = ls_pdf.get(position).getFileName();
                Path = ls_pdf.get(position).getPath();
                attachmentId=ls_pdf.get(position).getAttachGuid();


                if (isnet()) {
                    new StartSession(context, new CallbackInterface() {
                        @Override
                        public void callMethod() {

                                new DownloadAttachmentGetCordMethod().execute(attachmentId, FileName);


                            // ((NotificationActivity)context).showPopUp(true);

                        }

                        @Override
                        public void callfailMethod(String msg) {
                            ((NotificationActivity) context).showPopUp(false);
                        }
                    });

                }
               // downloadFile(FileName,true);

            }
        });


    }
    class DownloadUploadAttachment extends AsyncTask<String, Void, String> {
        String res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            dismissProgress();
            try {
                JSONArray jResults = new JSONArray(res);
                if (jResults.length() > 0) {
                    ls_pdf = new ArrayList<>();
                    for (int i = 0; i < jResults.length(); i++) {
                        Customer customer=new Customer();
                        customer.setFileName(jResults.getJSONObject(i).getString("AttachFilename"));
                        customer.setPath(jResults.getJSONObject(i).getString("Path"));
                        customer.setAttachGuid(jResults.getJSONObject(i).getString("AttachGuid"));
                        ls_pdf.add(customer);
                    }
                    attachmentAdapter=new AttachmentAdapter(AttachmentsActivity.this,ls_pdf);
                    spinner_attachment.setAdapter(attachmentAdapter);

/*
                    spinner_attachment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String FileName = spinner_attachment.getSelectedItem().toString();
                            String extension = FileName.substring(FileName.indexOf(".") + 1);
                            if (extension.equalsIgnoreCase("docx")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }else if(extension.equalsIgnoreCase("pdf")){
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }else if(extension.equalsIgnoreCase("xlsx")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }
                            else if(extension.equalsIgnoreCase("png")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }
                            else if(extension.equalsIgnoreCase("jpg")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }
                            else if(extension.equalsIgnoreCase("htm")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }
                                showPDF(FileName);
                            }

                            else if (extension.equalsIgnoreCase("mp3")) {
                                if (mediaPlayer.isPlaying()){
                                    mediaPlayer.pause();
                                }

                                    playFile(FileName);

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
*/

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
               // url = CompanyURL + WebUrlClass.api_GetUploadedAttachment + "?activityId=" + URLEncoder.encode(SourceId, "UTF-8");
                url = CompanyURL + WebUrlClass.api_GetUploadedAttachment +
                        "?activityId=" + URLEncoder.encode(ActivityId, "UTF-8")+"&SourceType=Activity";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            res = ut.OpenConnection(url, getApplicationContext());
            res = res.replaceAll("\\\\", "");
            res=res.toString();
           // res = res.substring(1, res.length() - 1);

            return "";
        }
    }
    private void showProgress() {

        mprogress.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mprogress.setVisibility(View.GONE);

    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    private void showPDF(String url) {
        try {


            webview_attachment.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    showProgress();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    dismissProgress();
                }
            });
           /* webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setInitialScale(0);
            webView.getSettings().setBuiltInZoomControls(true);*/
            webview_attachment.getSettings().setJavaScriptEnabled(true);
            if (url.contains(".htm")) {
                url = CompanyURL + "/attachments/" + url;
                webview_attachment.loadUrl(url);
            }else {
                url = CompanyURL + "/attachments/" + url;
                url = "https://docs.google.com/viewer?url=" + url;
                webview_attachment.loadUrl(url);
            }

        } catch (ActivityNotFoundException e) {

        }
    }
    private void playFile(String url) {

// Added in API level 8
        webview_attachment.getSettings().setPluginState(WebSettings.PluginState.ON);

        url = CompanyURL + "/Attachments/" + url;
        mediaPlayer = MediaPlayer.create(this, Uri.parse(url));
        mediaPlayer.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer!=null) {
            mediaPlayer.pause();
        }
    }

    public void downloadFile(String attachmentName1, boolean isDownload) {
        if (isDownload) {
            if (isnet()) {
                String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        .toString();
                File file = new File(path1 + "/" + "Attachment" + "/" + "File");
                if (file.exists()) {
                    final File fileNew = new File(file + "/" + attachmentName1);
                    if (fileNew.exists()) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                // urlGetMimeType(fileNew.getAbsolutePath());
                                Toast.makeText(context, "File Already downloaded", Toast.LENGTH_SHORT).show();

                                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(fileNew).toString());
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                String file = fileNew.getAbsolutePath();

                                if (file.contains("jpg") || file.contains("png") || file.contains("jpeg") || file.contains("JPG") ||
                                        file.contains("PNG") || file.contains("JPEG")) {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);
                                    intent.setDataAndType(Uri.fromFile(fileNew), "image/*");
                                    startActivity(intent);
                                } else {
                                    newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    try {
                                        context.startActivity(newIntent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                    }
                                }

                               /* MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                                newIntent.setDataAndType(Uri.fromFile(fileNew), mimeType);
                                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                try {
                                    context.startActivity(newIntent);
                                } catch (ActivityNotFoundException e) {
                                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                }*/
                                //((NotificationActivity)context).sendResult(directory.getName());


                            }
                        });
                    } else {
                        cllDownloadApi(Path, attachmentName1);
                    }
                } else {
                    cllDownloadApi(Path, attachmentName1);
                }
            }

        } else {


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
                // ((NotificationActivity)context).showPhhopUp(false);
            }
        });
    }
    private class DownloadFileApi extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);

        }

        @Override
        protected String doInBackground(String... strings) {

            String pathName = strings[0];
            final String fileName = strings[1];
            File file = null;
            String urlStr = null;
            urlStr = CompanyURL + "/Downloads/" + EnvMasterId + "/" + fileName;
            try {

               if (urlStr.contains(".jpg") || urlStr.contains(".jpeg") || urlStr.contains(".png")
               ||urlStr.contains(".PNG")||urlStr.contains(".JPEG")||urlStr.contains(".JPG")) {
                   path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                           .toString();
               }else {
                   path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                           .toString();
               }
                if (Constants.type == Constants.Type.Sahara) {
                    file = new File(path1 + "/" + "Sahara");

                } else if (Constants.type == Constants.Type.ZP) {
                    file = new File(path1 + "/" + "ZP");

                } else {
                    file = new File(path1 + "/" + "Attachment");
                }
                if (!file.exists())
                {
                    // Make it, if it doesn't exit
                    boolean success = file.mkdirs();

                    if (!success)
                    {
                        file = null;
                    }
                }

                try {

                    final File fileNew = new File(file + "/" + fileName);
                    if(fileNew.exists()){

                    }else {
                        fileNew.createNewFile();
                    }



                    try {
                        urlStr=urlStr.replaceAll(" ","%20");
                        URL url = new URL(urlStr);
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(false);
                        urlConnection.setReadTimeout(5000);
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        long total = 0;

                        InputStream inputStream = urlConnection.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
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
                        fileOutputStream.close();
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                urlGetMimeType(fileNew.getAbsolutePath());
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

    private void urlGetMimeType(String url) {
        Uri uri = Uri.fromFile(new File(url));

        Toast.makeText(AttachmentsActivity.this,"Download Location :"+url.toString(),Toast.LENGTH_LONG).show();

      //  Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, new File(url));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file
            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

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
            String path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString();
            File file = new File(path1 + "/" + "Attachment");
            if (file.exists()) {
                final File fileNew = new File(file + "/" + attachmentName1);
                if (fileNew.exists()) {

                    Handler handler = new Handler(context.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            pDialog.dismiss();
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            newIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            String mimeType = myMime.getMimeTypeFromExtension(fileNew.getAbsolutePath());
                            String file=fileNew.getAbsolutePath();
                            if(file.contains("jpg")||file.contains("png")||file.contains("jpeg")||file.contains("JPG")||file.contains("PNG")||file.contains("JPEG")){
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setDataAndType(Uri.fromFile(fileNew), "image/*");
                                startActivity(intent);
                            }else{
                                newIntent.setDataAndType(Uri.fromFile(fileNew), fileNew.getAbsolutePath().substring(fileNew.getAbsolutePath().lastIndexOf(".")));
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

                } else {
                    callDownloadApi(path, attachmentName1);
                }
            } else {
                callDownloadApi(path, attachmentName1);
            }

        }
    }

    private void callDownloadApi(final String path, final String attachmentName1) {
        new StartSession(context, new CallbackInterface() {
            @Override
            public void callMethod() {
                new DownloadAttachmentFileApi().execute(path, attachmentName1);
            }

            @Override
            public void callfailMethod(String msg) {
                // ((NotificationActivity)context).showPopUp(false);
            }
        });
    }

    private class DownloadAttachmentFileApi extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            String pathName = strings[0];
            final String fileName = strings[1];
            int count;
            File file=null;
            String urlStr = CompanyURL + "/Downloads/" + pathName + "/" + fileName;
            try {
                if (urlStr.contains(".jpg") || urlStr.contains(".jpeg") || urlStr.contains(".png")
                        ||urlStr.contains(".PNG")||urlStr.contains(".JPEG")||urlStr.contains(".JPG")) {
                    path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            .toString();
                }else {
                    path1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                            .toString();
                }

                if (Constants.type == Constants.Type.Sahara){
                    file = new File(path1 + "/" + "Sahara" + "/" + "File");

                }else {
                    file = new File(path1 + "/" + "Attachment");
                }
                if (!file.exists())
                    file.mkdirs();


                try {
                    //pdfFile = File.createTempFile(filename /* prefix */,prefix, pdfFile /* directory */);

                    final File fileNew = new File(file + "/" + URLEncoder.encode(fileName,"UTF-8"));
                    fileNew.createNewFile();

                    try {
                        urlStr = urlStr.replaceAll(" ", "%20");
                        //final File directory =  new File(file + "/" + fileNew);
                        URL url = new URL(urlStr);

                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setDoOutput(false);
                        urlConnection.connect();
                        int lenghtOfFile = urlConnection.getContentLength();
                        long total = 0;

                        InputStream inputStream = urlConnection.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(fileNew);
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
                                    //newIntent.setDataAndType(Uri.fromFile(fileNew), "/*");
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

                          /*      MimeTypeMap myMime = MimeTypeMap.getSingleton();
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
                        });*/


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
}
