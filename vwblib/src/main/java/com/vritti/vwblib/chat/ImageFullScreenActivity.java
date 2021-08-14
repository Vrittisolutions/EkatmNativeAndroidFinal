package com.vritti.vwblib.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import com.vritti.vwblib.R;

/**
 * Created by sharvari on 27-Mar-18.
 */

public class ImageFullScreenActivity extends AppCompatActivity {


    ImageView img_full;
    Button btn_continue;
    Intent intent;

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.vwb_image_fullscreen_lay);
    getSupportActionBar().setTitle("Image Screen");

    img_full=(ImageView)findViewById(R.id.img_full);
    btn_continue=(Button)findViewById(R.id.btn_continue);


    intent=getIntent();

    String Path=intent.getStringExtra("share_image_path");
    String Imgename=intent.getStringExtra("share_imagename");
    String Call_Callid = intent.getStringExtra("callid");
    String Call_CallType = intent.getStringExtra("call_type");
    String ChatRoomId = intent.getStringExtra("ChatRoomid");
    String ChatRoomName = intent.getStringExtra("Chatroomname");
    String  Create_check = intent.getStringExtra("value_chat");
    String CStatus = intent.getStringExtra("status");

    File imgFile = new File(Path);
    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    img_full.setImageBitmap(myBitmap);





}


}
