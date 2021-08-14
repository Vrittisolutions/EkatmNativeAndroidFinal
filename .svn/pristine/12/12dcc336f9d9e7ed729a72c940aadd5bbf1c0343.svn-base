package com.vritti.chat.activity;

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
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.CommonFunction;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

    Spinner spinner_attachment;
    WebView webview_attachment;
    SharedPreferences userpreferences;
    String ActivityId, SourceId;

    SQLiteDatabase sql;
    ProgressBar mprogress;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_attachments_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("  Attachments");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setLogo(R.drawable.vworkbench);
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
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        spinner_attachment=(Spinner) findViewById(R.id.spinner_attachment);
        webview_attachment= (WebView) findViewById(R.id.webview_attachment);
        mprogress = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
        Intent intent=getIntent();
        SourceId = intent.getStringExtra("SourceId");
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






    }
    class DownloadUploadAttachment extends AsyncTask<String, Void, String> {
        String res;
        List<String> ls_pdf;

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
                    ls_pdf = new ArrayList<String>();
                    for (int i = 0; i < jResults.length(); i++) {
                        ls_pdf.add(jResults.getJSONObject(i).getString("AttachFilename"));
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AttachmentsActivity.this, android.R.layout.simple_spinner_item, ls_pdf);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_attachment.setAdapter(dataAdapter);
                    }
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

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = null;
            try {
                url = CompanyURL + WebUrlClass.api_GetUploadedAttachment + "?activityId=" + URLEncoder.encode(SourceId, "UTF-8");
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
}
