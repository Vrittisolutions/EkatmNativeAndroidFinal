package com.vritti.vwblib.vworkbench;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwblib.R;
import com.vritti.vwblib.Services.BackgroundService;
import com.vritti.vwblib.classes.CommonFunction;

public class ActivitySetting extends AppCompatActivity {
    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;

    Toolbar toolbar;
    TextView mtext;
    String time;
    public final static int REQUEST_CODE = -1010101;
    public final static int REQUEST_CODE_SETTING = 2090;
    String  IsChatApplicable,IsGPSLocation;
    SharedPreferences userpreferences;
    ProgressDialog progressDialog;
    Button btn_clear_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vwb_setting);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.drawable.vworkbench);
        toolbar.setTitle(" vWorkbench");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        mtext = (TextView) findViewById(R.id.showtime);
        btn_clear_data=(Button) findViewById(R.id.btn_clear_data);

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
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);


        btn_clear_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearApplicationData();
            }
        });

        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_SETTING, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                time = c.getString(c.getColumnIndex("SettingValue"));
            } while (c.moveToNext());
        }
        mtext.setText(time + " min.");
    }

    public void setRefresh(View v) {
        CreateChangeSettingdialog();
    }


    public void Offlinedata(View v) {
        Intent intent = new Intent(getApplicationContext(), ActivityOfflineData.class);
        startActivity(intent);
    }

    public void Useddata(View v) {
        showUsagedialog();


    }

    public  void ServerSettingRefresh(View view){
        if (isnet()) {
            new StartSession(ActivitySetting.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadGetEnvJSON().execute();
                }

                @Override
                public void callfailMethod(String msg) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

    }


    class DownloadGetEnvJSON extends AsyncTask<Integer, Void, String> {
        String res;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ActivitySetting.this);
                    progressDialog.setMessage("Loading. Please wait...");
                    progressDialog.setIndeterminate(false);
                    // progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    // progressDialog.setContentView(R.layout.vwb_progress_lay);
                    progressDialog.setCancelable(true);

                }
                progressDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_getEnv;
            try {
                res = ut.OpenConnection(url, getApplicationContext());
                res = res.replaceAll("\\\\\\\\\\\"", "");
                res = res.replaceAll("\\\\", "");
                res = res.substring(1, res.length() - 1);


            } catch (Exception e) {
                e.printStackTrace();
                res = "Error";
            }
            return res;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (res.contains("AppEnvMasterId")) {
                try {
                    JSONArray jResults = new JSONArray(res);
                    for (int index = 0; index < jResults.length(); index++) {
                        JSONObject jorder = jResults.getJSONObject(index);
                        String data = jorder.getString("AppEnvMasterId");
                        IsChatApplicable = jorder.getString("IsChatApplicable");
                        if (jorder.has("IsGPSLocation")) {
                            IsGPSLocation=jorder.getString("IsGPSLocation");
                        }
                        SharedPreferences.Editor editor = userpreferences.edit();
                        editor.putString("chatapplicable", IsChatApplicable);
                        editor.putString("IsGpslocation",IsGPSLocation);
                        editor.commit();

                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }



            } else {
                Toast.makeText(getApplicationContext(), "Server not responding", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public void CreateChangeSettingdialog() {
        final Dialog dialog = new Dialog(ActivitySetting.this);
        dialog.setContentView(R.layout.vwb_dilog_refresh);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final EditText edt = (EditText) dialog.findViewById(R.id.time);
        edt.setText(time);
        edt.setSelection(0, edt.getText().length());
        dialog.setCancelable(false);
       /* dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);*/
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edt.getText().toString();
                if (edt.getText().length() > 0) {
                    SQLiteDatabase sql = db.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("SettingValue", data);
                    sql.update(db.TABLE_SETTING, contentValues, "SettingName=?", new String[]{"Rtime"});
                    Cursor c = sql.rawQuery("Select * from " + db.TABLE_SETTING, null);
                    if (c.getCount() > 0) {
                        c.moveToFirst();
                        do {
                            time = c.getString(c.getColumnIndex("SettingValue"));
                        } while (c.moveToNext());
                    }
                    mtext.setText(time + " min.");
                    stopService(new Intent(ActivitySetting.this, BackgroundService.class));
                    Toast.makeText(getApplicationContext(), "Refresh Time Updated Successfully", Toast.LENGTH_LONG).show();
                    backgroundRefresh();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Refresh Time", Toast.LENGTH_LONG).show();

                }

            }


        });

        dialog.show();


    }

    void backgroundRefresh() {
        int itime = 60;
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery("Select * from " + db.TABLE_SETTING, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                itime = c.getInt(c.getColumnIndex("SettingValue"));
            } while (c.moveToNext());
        }
        long aTime = 1000 * 60 * itime;
        Intent igpsalarm = new Intent(getApplicationContext(), BackgroundService.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(
                getApplicationContext(), 0, igpsalarm, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(piHeartBeatService);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), aTime, piHeartBeatService);
    }

    public void showUsagedialog() {
        final Dialog dialog = new Dialog(ActivitySetting.this);
        dialog.setContentView(R.layout.vwb_dialog_usage);
        Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final TextView edt = (TextView) dialog.findViewById(R.id.data);
        SharedPreferences sp = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        long check = sp.getLong("consumed", 0);
        String data = bytesIntoHumanReadable(check);
        edt.setText(data);
        dialog.setCancelable(false);
       /* dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);*/
        dialog.show();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CODE) {
            showUsagedialog();
        }else if(requestCode == REQUEST_CODE_SETTING){
            CreateChangeSettingdialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        startActivity(i);
        finish();
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

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED ");
                }
            }
        }
    }
    public  boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }else {
                  /*  Intent intent=new Intent(ActivitySetting.this,ut2.class);
                    startActivity(intent);
                    finish();*/
                }
            }

        }

        return dir.delete();
    }

}
