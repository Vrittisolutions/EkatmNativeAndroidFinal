package com.vritti.chat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ortiz.touchview.TouchImageView;
import com.vritti.ekatm.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by sharvari on 27-Mar-18.
 */

public class ImageFullScreenActivity extends AppCompatActivity {


   TouchImageView img_full;
    Button btn_continue;
    Intent intent;
   WebView webView;
    ProgressBar toolbar_progress_App_bar;
    //private ScaleGestureDetector mScaleGestureDetector;
   // private float mScaleFactor = 1.0f;
   // private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_image_fullscreen_lay);

        img_full = findViewById(R.id.img_full);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        webView = (WebView) findViewById(R.id.web_pdf);
        toolbar_progress_App_bar = (ProgressBar) findViewById(R.id.toolbar_progress_App_bar);
       // fullImage = findViewById(R.id.fullImage);

       btn_continue.setVisibility(View.GONE);
        intent = getIntent();

        String Path = intent.getStringExtra("share_image_path");
        String Imgename = intent.getStringExtra("share_imagename");
        String Call_Callid = intent.getStringExtra("callid");
        String Call_CallType = intent.getStringExtra("call_type");
        String ChatRoomId = intent.getStringExtra("ChatRoomid");
        String ChatRoomName = intent.getStringExtra("Chatroomname");
        String Create_check = intent.getStringExtra("value_chat");
        String CStatus = intent.getStringExtra("status");


        // webView.loadUrl("http://vritti.ekatm.com/Attachments/View Attachment/240.pdf");


        if (Path.contains(".pdf") || Path.contains(".doc") || Path.contains(".docx")

                || Path.contains(".ppt") || Path.contains(".xls") || Path.contains(".xlsx") || Path.contains(".pptx")) {
           webView.setVisibility(View.VISIBLE);
            webView.setWebViewClient(new myWebClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setDisplayZoomControls(false);
            toolbar_progress_App_bar.setVisibility(View.VISIBLE);

            String url = null;
            try {
                url = "https://docs.google.com/viewer?url=" + URLEncoder.encode(Path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            webView.loadUrl(url);

        } else {


            if (Path.contains("http:") || Path.contains("https:")) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(ImageFullScreenActivity.this);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            img_full.setVisibility(View.VISIBLE);
            Glide.with(ImageFullScreenActivity.this)
                    .load(Path)
                    .apply(RequestOptions.placeholderOf(circularProgressDrawable))
                    .into(img_full);



            } else {
                img_full.setVisibility(View.VISIBLE);
                File imgFile = new File(Path);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
               img_full.setImageBitmap(myBitmap);
            }
        }


    }

    public class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

           // toolbar_progress_App_bar.setVisibility(View.GONE);
        }

    }

}
