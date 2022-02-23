package com.vritti.expensemanagement;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.mp4parser.srt.SrtParser;
import com.vritti.chat.activity.AddGroupActivity;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.SendOfflineData;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Adapter.AssetTransferAdapter;
import com.vritti.vwb.Beans.AssetTransfer;
import com.vritti.vwb.ImageWithLocation.FileUtils;
import com.vritti.vwb.classes.CommonFunction;
import com.vritti.vwb.vworkbench.AssetTransferListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sharvari on 19-Sep-19.
 */

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ArrayList<ExpenseData>expenseDataArrayList;
    TextView txt_record,txt_fromdate,txt_todate,txt_submit;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",Attachment="",LinkId="";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    static Context context;
    SQLiteDatabase sql;
    Button btn_upload;
    String[] user;
    JSONArray jsonArray = new JSONArray();
    ImageView img_from_date,img_to_date;
    private String FromDate,Todate;
    private String FinalJson,Position;
    private ProgressDialog progressDialog;

    double finTotExp_ = 0, tot_exp_prev=0, exp_row_curr = 0, prev_tot_exp_to_save=0, exp_old=0;

    ImageView img_back,history,microphone;
    private String formattedDate="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.report_list);
        txt_record=findViewById(R.id.txt_record);
        context = HistoryActivity.this;
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();



        img_back=findViewById(R.id.img_back);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        btn_upload=findViewById(R.id.btn_upload);
        txt_fromdate=findViewById(R.id.txt_fromdate);
        txt_todate=findViewById(R.id.txt_todate);
        txt_submit=findViewById(R.id.txt_submit);
        img_from_date=findViewById(R.id.img_from_date);
        img_to_date=findViewById(R.id.img_to_date);


        btn_upload.setVisibility(View.GONE);
        expenseDataArrayList=new ArrayList<>();

        historyAdapter=new HistoryAdapter(HistoryActivity.this,expenseDataArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(historyAdapter);


        long date1 = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateString = sdf.format(date1);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        txt_fromdate.setText(sdf.format(cal.getTime()));
        txt_todate.setText(dateString);


        img_from_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                               String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                               /* FromDate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;*/
                                txt_fromdate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });

        img_to_date.setOnClickListener(new View.OnClickListener() {
            int year, month, day;

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(HistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker datePicker, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //    datePicker.setMinDate(c.getTimeInMillis());
                                String  date = dayOfMonth + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + year;
                               /* Todate = year + "-"
                                        + String.format("%02d", (monthOfYear + 1))
                                        + "-" + dayOfMonth;*/
                                txt_todate.setText(date);
                            }
                        }, year, month, day);
                datePickerDialog.setTitle("Select Date");

                datePickerDialog.show();
            }
        });


        txt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tot_exp_prev = 0;finTotExp_=0;prev_tot_exp_to_save=0;exp_old=0;exp_row_curr=0;

               String from=txt_fromdate.getText().toString();
                Log.d("From",from);
               String To=txt_todate.getText().toString();
                Log.d("From_To",To);
                Todate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", To);
                Log.d("From_To_T",Todate);
                FromDate = formateDateFromstring("dd-MM-yyyy", "yyyy-MM-dd", from);
                Log.d("From_To_F",FromDate);

                if (isnet()) {
                    new StartSession(HistoryActivity.this, new CallbackInterface() {
                        @Override
                        public void callMethod() {
                            new DownloadExpenseList().execute();
                        }

                        @Override
                        public void callfailMethod(String msg) {

                        }


                    });

                }


            }
        });
        getdata();





    }
    public void chatuserdelete(String adapterPosition) {
        Position=adapterPosition;
        JSONObject obj = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            obj.put("ExpenseId", adapterPosition);
            jsonArray.put(obj);
            JSONObject jsonObject1 = new JSONObject();
            try {
                jsonObject1.put("Expense", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

             FinalJson = jsonObject1.toString();

            if (ut.isNet(context)) {


                new StartSession(HistoryActivity.this, new CallbackInterface() {
                    @Override
                    public void callMethod()
                    {
                        new DeleteRecord().execute();
                    }

                    @Override
                    public void callfailMethod(String msg) {
                        ut.displayToast(HistoryActivity.this, msg);
                    }
                });
            } else {
                ut.displayToast(HistoryActivity.this, "No Internet Connetion");
            }

         /*   String remark1 = "Expense deleted successfully";
            String url = CompanyURL + WebUrlClass.api_PostDltExpenseRecord;
            String op = "true";
            CreateOfflineExpense(url, FinalJson, WebUrlClass.POSTFLAG, remark1, op);*/
           /* sql.delete(db.TABLE_EXPENSE, "ExpRecordId=?", new String[]{String.valueOf(adapterPosition)});
            Toast.makeText(HistoryActivity.this, "Record delete successfully", Toast.LENGTH_SHORT).show();*/
          //  getdata();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getTotSum(String tabName, String colName){
        double finalTot = 0;
        String qry = "Select SUM("+colName+") as TotalExp from "+tabName;
        Cursor cq = sql.rawQuery(qry,null);
        if(cq.getCount()>0){
            cq.moveToFirst();
            do{
                try {
                    finalTot = Double.parseDouble(cq.getString(cq.getColumnIndex("TotalExp")));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cq.moveToNext());

        }

        return finalTot;

    }

    private void getdata() {

        finTotExp_ = getTotSum(db.TABLE_EXPENSE,"Amount");
        prev_tot_exp_to_save = finTotExp_;

            expenseDataArrayList.clear();
            String query = "SELECT * FROM " + db.TABLE_EXPENSE + " order by ExpDate desc";
            Cursor cur = sql.rawQuery(query, null);
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                do {
                    ExpenseData expenseData = new ExpenseData();
                    expenseData.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                   // expenseData.setCat_name(cur.getString(cur.getColumnIndex("cat_name")));
                    expenseData.setCat_name(cur.getString(cur.getColumnIndex("cat_name")));
                    expenseData.setExpType(cur.getString(cur.getColumnIndex("ExpType")));

                        formattedDate = formateDateFromstring("yyyy-MM-dd HH:mm", "dd-MM-yyyy hh:mm aa", cur.getString(cur.getColumnIndex("ExpDate")));

                    expenseData.setExpDate(formattedDate);
                    String exp=cur.getString(cur.getColumnIndex("Amount"));
                    exp=exp.replace("\u20B9","");
                    expenseData.setAmount(exp);

                    exp_row_curr = Double.parseDouble(exp);//12

                    if(expenseDataArrayList.size()==0){
                        tot_exp_prev = finTotExp_;
                        exp_old = exp_old + exp_row_curr;
                        prev_tot_exp_to_save = exp_old;        //to get current records expense
                    }else {
                        tot_exp_prev = finTotExp_ - prev_tot_exp_to_save;
                        exp_old = exp_old + exp_row_curr;
                        prev_tot_exp_to_save = exp_old;        //to get prev records expense
                    }

                    expenseData.setTot_prev_exp(tot_exp_prev);

                    expenseData.setPaymentMode(cur.getString(cur.getColumnIndex("PaymentMode")));
                    expenseData.setFromLocation(cur.getString(cur.getColumnIndex("FromLocation")));
                    expenseData.setToLocation(cur.getString(cur.getColumnIndex("ToLocation")));
                    expenseData.setDistance(cur.getString(cur.getColumnIndex("Distance")));
                    expenseData.setExpRecordId(cur.getString(cur.getColumnIndex("ExpRecordId")));
                    expenseData.setRemark(cur.getString(cur.getColumnIndex("Remark")));
                    expenseData.setAttachment(cur.getString(cur.getColumnIndex("attachment")));
                    expenseData.setLinkId(cur.getString(cur.getColumnIndex("LinkId")));
                    expenseData.setLinkTo(cur.getString(cur.getColumnIndex("LinkTo")));
                    expenseData.setTravelMode(cur.getString(cur.getColumnIndex("TravelMode")));
                    expenseData.setVehicleType(cur.getString(cur.getColumnIndex("VehicleType")));
                    expenseData.setPath(cur.getString(cur.getColumnIndex("Path")));
                    expenseData.setExpRecordId(cur.getString(cur.getColumnIndex("ExpRecordId")));

                    //local sum of exp = finalTot
                    //var prevrow exp , finaltot - prevrowexp = currentrowexptot
                    //set to tot_prev_exp

                   /* if (cf.CheckifExpensePresent(db.TABLE_EXPENSE, "ExpRecordId", expenseData.getExpRecordId())) {

                        cf.AddExpenseDetails(expenseData);

                    }*/
                    expenseDataArrayList.add(expenseData);
                } while (cur.moveToNext());

                   historyAdapter.notifyDataSetChanged();
            }else {
                txt_record.setVisibility(View.VISIBLE);
            }
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // startActivity(new Intent(HistoryActivity.this,AddExpenseActivity_V1.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

    }
    public String getdate(String exp_date)
    {
        //Format of the date defined in the input String
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        //Desired format: 24 hour format: Change the pattern as per the need
        DateFormat outputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm ");
        Date date = null;
        String output = null;
        try{
            //Converting the input String to Date
            date= df.parse(exp_date);
            //Changing the format of date and storing it in String
            output = outputformat.format(date);
            //Displaying the date
            System.out.println(output);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        return output;
    }


    public class PostUploadImageMethodProspect extends AsyncTask<String, String, String> {
        JSONObject jsonimage = new JSONObject();
        private Exception exception;
        String params;
        //   ProgressDialog SPdialog;
        Object res = null;
        String response = null;
        String Imagefilename;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... urls) {

            try {

               // Imagefilename =urls[0].substring(urls[0].lastIndexOf("/")+1);

                File f = new File(urls[0]);

                Object ActivityID = urls[1];//AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
                String upLoadServerUri = CompanyURL + WebUrlClass.api_UploadAttechmentnew + "?AppEnvMasterId=" + EnvMasterId +"&ActivityId="+ ActivityID;

                //File imgfile=new File(getRealPathFromUri(HistoryActivity.this,f));

                response = String.valueOf(Utility.OpenMultiPart(upLoadServerUri ,f ));

                if (response!= null && (!response.equals(""))) {
                    try {
                        jsonimage.put("File",urls[0]);
                        jsonimage.put("ActivityId", urls[1]);
                        jsonimage.put("GUID", response);
                        jsonArray.put(jsonimage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            return response;

        }

        protected void onPostExecute(String feed) {

            String Vendordata = "";
            if (Attachment != null) {



            }

        }
    }

    private boolean isnet() {
        // TODO Auto-generated method stub
        Context context = this.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void CreateOfflineExpense(final String url, final String parameter,
                                             final int method, final String remark, final String op) {
        //final DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        long a = cf.addofflinedata(url, parameter, method, remark, op);
        if (a != -1) {
            Toast.makeText(getApplicationContext(), "Record saved successfully", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(), SendOfflineData.class);
            intent1.putExtra(WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_KEY, WebUrlClass.INTENT_SEND_OFFLINE_DATA_FLAG_VALUE);

            startService(intent1);
        } else {
            Toast.makeText(getApplicationContext(), "Data not Saved", Toast.LENGTH_LONG).show();
        }

    }


    class DownloadExpenseList extends AsyncTask<String, Void, String> {

        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(HistoryActivity.this);
                progressDialog.setMessage("Loading. Please wait...");
                progressDialog.setIndeterminate(false);
                //  progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //  progressDialog.setContentView(R.layout.vwb_progress_lay);
                progressDialog.setCancelable(true);

            }
            progressDialog.show();


        }
        @Override
        protected String doInBackground(String... params) {
            //String url = CompanyURL + WebAPIUrl.api_GetRefreshChatRoom + "?ApplicationCode="+WebAPIUrl.AppNameFCM;

          String url = CompanyURL + WebUrlClass.api_GetExpenseFromDateRange + "?fromdate="+FromDate+"&todate="+Todate;
            try {
                res = ut.OpenConnection(url,HistoryActivity.this);
                response = res.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response.equalsIgnoreCase("[]")){

                txt_record.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }else {
                if (response != null) {
                    JSONArray jResults = new JSONArray();
                    try {
                        sql.delete(db.TABLE_EXPENSE, null,
                                null);
                        txt_record.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        jResults = new JSONArray(response);
                        ContentValues values = new ContentValues();
                        expenseDataArrayList.clear();
                        if (jResults.length() > 0) {
                            for (int i = 0; i < jResults.length(); i++) {
                                JSONObject jsonObject = jResults.getJSONObject(i);
                                ExpenseData expenseData = new ExpenseData();

                                expenseData.setUserMasterId(UserMasterId);
                                // expenseData.setCat_name(cur.getString(cur.getColumnIndex("cat_name")));
                                expenseData.setCat_name("Official");
                                expenseData.setExpType(jsonObject.getString("ExpType"));

                                String ExpDate=jsonObject.getString("ExpDate");
                                String[] arr1 = ExpDate.split("T");
                                String date = arr1[0];
                                String time = arr1[1];
                                String t = "";
                                try {
                                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                    final Date dateObj = sdf.parse(time);
                                    System.out.println(dateObj);
                                   // t=new SimpleDateFormat("KK:mm").format(dateObj);
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }




                                String ex=date+" "+time;



                                expenseData.setExpDate(ex);
                                expenseData.setAmount(jsonObject.getString("Amount"));
                                expenseData.setPaymentMode(jsonObject.getString("Paymentmode"));
                                expenseData.setFromLocation(jsonObject.getString("FromLocation"));
                                expenseData.setToLocation(jsonObject.getString("ToLocation"));
                                expenseData.setDistance(jsonObject.getString("Distance"));
                                expenseData.setExpRecordId(jsonObject.getString("ExpRecordId"));
                                expenseData.setAttachment("");
                                expenseData.setLinkId("");
                                expenseData.setLinkTo("");
                                expenseData.setPath("");
                                expenseData.setRemark(jsonObject.getString("Remark"));
                                expenseData.setTravelMode(jsonObject.getString("TravelMode"));
                                expenseData.setVehicleType(jsonObject.getString("VehicleType"));

                               if (cf.CheckifExpensePresent(db.TABLE_EXPENSE, "ExpRecordId", expenseData.getExpRecordId())) {
                                   cf.AddExpenseDetails(expenseData);

                               } else {
                                }


                            }
                            getdata();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }
    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {

        }

        return outputDate;

    }

    class DeleteRecord extends AsyncTask<String , Void, String> {
        String url,response;
        Object res;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(HistoryActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);

            }
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (integer.contains("True")) {
                sql.delete(db.TABLE_EXPENSE, "ExpRecordId=?", new String[]{String.valueOf(Position)});
                Toast.makeText(HistoryActivity.this, "Record delete successfully", Toast.LENGTH_SHORT).show();
                getdata();
            } else {
                Toast.makeText(HistoryActivity.this, "Already claim book", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {

                url = CompanyURL + WebUrlClass.api_PostDltExpenseRecord;
                try {
                res = ut.OpenPostConnection(url,FinalJson, HistoryActivity.this);
                response=res.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }


}
