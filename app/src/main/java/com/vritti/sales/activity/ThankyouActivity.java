package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.data.AnyMartData;

import java.text.DecimalFormat;

/**
 * Created by sharvari on 5/9/2016.
 */
public class ThankyouActivity extends AppCompatActivity {
    private Context parent;
    String res = "";
    TextView txtdelivery_address, txtdate, txttime_frm,txttime_to, text_totalamtpay, txtThanku;
    Button button_shop_more;
    String address, date, time,time_to, vehicle;
    float paybaleAmt;
    SharedPreferences sharedpreferences;
    public String restoredusername;

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tbuds_activity_thanku);

       /* final ActionBar ab = getSupportActionBar();
        getSupportActionBar().setTitle(" "+"Thank you");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);*/

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        //actionBarImage = (ImageView)findViewById(R.id.actionBarImage);
        toolbar.setTitle("Thank you");
        // toolbar.setTitleTextColor(0xffffff);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //Utilities.darkenStatusBar(this, R.color.colorPrimary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

     //   image_URL = sharedpreferences.getString("logopath",null);

        /*Picasso.with(ThankyouActivity.this)
                .load(image_URL)//Your image link url
                .into(actionBarImage);*/

        Picasso.with(ThankyouActivity.this)
                .load(image_URL)//Your image link url
                .into(new Target()
                {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from)
                    {
                        //BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
                        BitmapDrawable d = new BitmapDrawable(getResources(),
                                Bitmap.createScaledBitmap(bitmap, 110, 50, true));
                     //   ab.setIcon(d);
                     //   ab.setDisplayShowHomeEnabled(true);
                        //ab.setDisplayHomeAsUpEnabled(true);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable)
                    {
                        Log.e("bitmap failed","bitmap failed");
                    }
                });

        Bundle b = getIntent().getExtras();
        Intent intent = getIntent();
        if(AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
            address = intent.getStringExtra("User_Address");
        } else if(AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            vehicle = intent.getStringExtra("User_Vehicle");
        }

        final String payableAmount = b.getString(String.valueOf("PayableAmount"));
        date = b.getString(String.valueOf("date")); //delivery date
        time = b.getString(String.valueOf("time")); //delivery time
        time_to = b.getString(String.valueOf("time_to"));

        /*paybaleAmt = intent.getFloatExtra("PayableAmount", 0);
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");*/

        restoredusername = sharedpreferences.getString("username", null);
        txtThanku.setText("Thank you " + restoredusername + " !");
        if (AnyMartData.MODULE.equalsIgnoreCase("ORDERBILLING")) {
            txtdelivery_address.setText(address);
        } else if (AnyMartData.MODULE.equalsIgnoreCase("PETRO")) {
            txtdelivery_address.setText(vehicle);
        }
        txtdate.setText(date);
        txttime_frm.setText(time);
        txttime_to.setText(time_to);
       // text_totalamtpay.setText(paybaleAmt + " ₹");
        double amount = Double.parseDouble(payableAmount);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        //text_totalamtpay.setText(payableAmount + " ₹");
        text_totalamtpay.setText(formatted + " ₹");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("intentFrom","OrderPlaceComplete");
                startActivity(intent);
            }
        }, 3000);

        button_shop_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ThankyouActivity.this, MainActivity.class);
                //Intent intent1 = new Intent(ThankyouActivity.this, Sales_HomeSActivity.class);
                intent1.putExtra("intentFrom","OrderPlaceComplete");
                startActivity(intent1);
            //    overridePendingTransition(R.anim.enter_slide_in_down,R.anim.enter_slide_out_down);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ThankyouActivity.this, MainActivity.class);
        //Intent i = new Intent(ThankyouActivity.this, Sales_HomeSActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("intentFrom","OrderPlaceComplete");
        startActivity(i);
        finish();

    }

    private void initialize() {
        parent = ThankyouActivity.this;
        txtdelivery_address = (TextView) findViewById(R.id.txtdelivery_address);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime_frm = (TextView) findViewById(R.id.txttime_frm);
        txttime_to= (TextView) findViewById(R.id.txttime_to);
        text_totalamtpay = (TextView) findViewById(R.id.text_totalamtpay);
        button_shop_more = (Button) findViewById(R.id.button_shop_more);
        txtThanku = (TextView) findViewById(R.id.txtThanku);
    }

}
