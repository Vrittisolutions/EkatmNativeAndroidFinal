package com.vritti.expensemanagement;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.vritti.ekatm.R;

/**
 * Created by sharvari on 27-Sep-19.
 */

public class ImageActivity extends Activity {


    ImageView img_doc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.img_lay);

        img_doc=findViewById(R.id.img_doc);

        String image=getIntent().getStringExtra("Image");


        img_doc.setImageURI(Uri.parse(image));

    }
}
