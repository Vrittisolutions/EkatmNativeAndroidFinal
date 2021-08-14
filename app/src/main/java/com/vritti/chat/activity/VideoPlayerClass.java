package com.vritti.chat.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.VideoView;

import com.vritti.ekatm.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoPlayerClass extends Activity {

    @BindView(R.id.videoView1)
    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        ButterKnife.bind(this);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        String path = "";
        if(getIntent() != null){
            path = getIntent().getStringExtra("fileUrl");
        }
        //String path = "android.resource://" + getPackageName() + "/" + R.raw.simply_shop_user;
        videoView.setVideoURI(Uri.parse(path));
        videoView.requestFocus();
        videoView.setMediaController(mediaController);
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //setScreen();
            }
        });
    }
}
