package com.vritti.sales.OrderBookingNew;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.activity.Sales_HomeSActivity;
import com.vritti.sales.beans.Tbuds_commonFunctions;
import com.vritti.sales.data.AnyMartData;

import java.io.BufferedReader;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sharvari on 5/9/2016.
 */

public class ThankyouActivity_Multimerchant extends AppCompatActivity {
    private Context parent;
    String res = "";
    TextView txtdelivery_address, txtdate, txttime_frm,txttime_to, text_totalamtpay, txtThanku;
    Button button_shop_more;
    ImageView imglogo;
    String address, date, time,time_to, vehicle, delmode;
    float paybaleAmt;
    SharedPreferences sharedpreferences;
    public String restoredusername;

    Toolbar toolbar;
    ImageView actionBarImage;
    String image_URL;

    ListView list_ord_summary;
    String DateToStr = "";

    Utility ut;
    static DatabaseHandlers db;
    Tbuds_commonFunctions tcf;
    static String settingKey = "";
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";
    String IsChatApplicable, IsGPSLocation;
    public static SQLiteDatabase sql;
    String imageURL="",json="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanku_multimerchant);

        final ActionBar ab = getSupportActionBar();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<small>"+getResources().getString(R.string.thankyou)+"</small>"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }

        initialize();

        //getlogoif not available
        if(AnyMartData.DynamicLOGO_Company_URL.equalsIgnoreCase("")){

            new GetCompanyLogo().execute();

        }else {
            //setlogo here
            try{
                Picasso.with(parent)
                        .load(AnyMartData.DynamicLOGO_Company_URL)//Your image link url
                        .into(imglogo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);

        Bundle b = getIntent().getExtras();
        Intent intent = getIntent();
        address = intent.getStringExtra("User_Address");

        final String payableAmount = b.getString(String.valueOf("PayableAmount"));
        date = b.getString(String.valueOf("date")); //delivery date
        time = b.getString(String.valueOf("time")); //delivery time
        time_to = b.getString(String.valueOf("time_to"));
        delmode = b.getString(String.valueOf("delmode"));

        restoredusername = sharedpreferences.getString("username", null);
        txtThanku.setText(""+getResources().getString(R.string.thankyou)+" " + restoredusername + " !");
        txtdelivery_address.setText(address);

        date = dateconvert(date);

        if(delmode.equalsIgnoreCase("Door Step")){
            txtdate.setText(getResources().getString(R.string.doorstep)+" "+date +" "+getResources().getString(R.string.fm)+" "+time+" "+
                    getResources().getString(R.string.to)+" "+time_to);
        }else {
            txtdate.setText(getResources().getString(R.string.takeaway)+" "+date +" "+getResources().getString(R.string.fm)+" "+time+" "+
                    getResources().getString(R.string.to)+" "+time_to);
        }
        txttime_frm.setText(time);
        txttime_to.setText(time_to);

        double amount = Double.parseDouble(payableAmount);
        DecimalFormat formatter = new DecimalFormat("#,##,##,###.00");
        String formatted = formatter.format(amount);
        text_totalamtpay.setText("₹ "+formatted);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ThankyouActivity_Multimerchant.this, Sales_HomeSActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        },3500);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent1 = new Intent(ThankyouActivity_Multimerchant.this, Sales_HomeSActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);
        finish();

    }

    private void initialize() {
        parent = ThankyouActivity_Multimerchant.this;
        txtdelivery_address = (TextView) findViewById(R.id.txtdelivery_address);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime_frm = (TextView) findViewById(R.id.txttime_frm);
        txttime_to= (TextView) findViewById(R.id.txttime_to);
        text_totalamtpay = (TextView) findViewById(R.id.text_totalamtpay);
        button_shop_more = (Button) findViewById(R.id.button_shop_more);
        txtThanku = (TextView) findViewById(R.id.txtThanku);

        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES, Context.MODE_PRIVATE);
        ut = new Utility();
        tcf = new Tbuds_commonFunctions(ThankyouActivity_Multimerchant.this);
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
        //AnyMartData.MAIN_URL = sharedpreferences.getString("CompanyURL",null);
        AnyMartData.DynamicLOGO_Company_URL = sharedpreferences.getString("DynamicLOGOCompany","");
    }


    public String dateconvert(String Date_to_convert){

        SimpleDateFormat Format = new SimpleDateFormat("dd MMM yyyy");//Feb 23 2016 12:16PM
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date d1;

        try {
            //d1 = format.parse(DoAck);
            d1 = format.parse(Date_to_convert);
            //DateToStr = toFormat.format(date);
            DateToStr = Format.format(d1);
            // DateToStr = format.format(d1);
            System.out.println(DateToStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DateToStr;
    }

    class GetCompanyLogo extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = "";
        // String compURL_logo = "http://"+ enterurl.getText().toString().trim();
        String url = "http://Bakery"+".ekatm.com";
        //http://anymart1.ekatm.com/api/OrderBillingAPI/GetCompanyLogo?companyUrl=http://anymart1.ekatm.com

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Authenticating User...");
            progressDialog.show();*/
            /*progress = ProgressHUD.show(parent,
                    "Authenticating ...", false, true, null);*/
        }

        @Override
        protected Void doInBackground(Void... params) {//h207.ekatm.com/Ekatm/GetCompanyLogo
            // String url_logo = AnyMartData.MAIN_URL + AnyMartData.LOGO_URL + "?companyUrl="+AnyMartData.CompanyURL;
            String url_logo = AnyMartData.MAIN_URL + "GetCompanyLogo" + "?companyUrl="+CompanyURL;

            String auth = url_logo.replaceAll(" ","");
            URLConnection urlConnection = null;
            BufferedReader bufferedReader = null;

            try {
                res = Utility.OpenconnectionOrferbilling(url_logo, parent);
                int a = res.getBytes().length;
                res = res.replaceAll("\\\\", "");
                // res = res.replaceAll("\"", "");
                // res = res.replaceAll(" ", "");
                responseString = res.toString().replaceAll("^\"|\"$", "");
                Log.e("Response", responseString);

                res = responseString;
                Log.e("logopath", responseString);

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            // progress.dismiss();

            if (responseString.equalsIgnoreCase("Session Expired")) {
                //no logo found

            }else if (responseString.contains("Message")) {

                responseString="";
                imageURL="";

            }else if (!responseString.equalsIgnoreCase("error")) {

                //call another api to get logo path
                json = responseString;
                imageURL = CompanyURL+"/images/"+ responseString;

                //setlogo here
                try{
                    Picasso.with(parent)
                            .load(imageURL)//Your image link url
                            .into(imglogo);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }else {

                responseString="";
                imageURL="";

                Toast.makeText(parent, ""+getResources().getString(R.string.servererror), Toast.LENGTH_LONG)
                        .show();
            }


            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("DynamicLOGOCompany", imageURL);
            editor.commit();

        }
    }

}
