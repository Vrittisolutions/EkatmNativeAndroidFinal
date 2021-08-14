package com.vritti.chat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vritti.ekatm.R;
import com.vritti.vwb.CommonClass.AppCommon;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddImageWithTextForChat  extends Activity {

    @BindView(R.id.sdv_Image)
    SimpleDraweeView sdv_Image;
    @BindView(R.id.img_message)
    EditText img_message;

    String imageUri = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_message);
        ButterKnife.bind(this);
        if(getIntent()!= null){
            imageUri = getIntent().getStringExtra("imageUri");
        }

        if(imageUri!= null){

            sdv_Image.setController( AppCommon.getDraweeController(sdv_Image , imageUri , 400));
           // sdv_Image.setImageURI(imageUri);
        }else {
            onBackPressed();
        }
    }
    @OnClick(R.id.sendMessage)
    void sendMessage(){
        Intent intent = new Intent();
        intent.putExtra("messages", img_message.getText().toString().trim());
        setResult(986, intent);
        finish();
    }
}
