package com.vritti.vwb.vworkbench;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.Constants;
import com.vritti.ekatm.R;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.MyTeamBean;
import com.vritti.vwb.Beans.MyWorkspaceBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VWBHomeActivity extends AppCompatActivity{


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;
    public String IsChatApplicable;

    LinearLayout lay_link;
    ImageView img_back;
    TextView txt_title;
    private String  settingKey="";
    private String dabasename="";
    LinearLayout lay_ticket, lay_not_acted, lay_overdue, lay_today, lay_critical, lay_assign_by_me, lay_unapprove, lay_unplanned;
    TextView tv_activity_status, tv_birthday_cnt, tv_username, tv_meeting_cnt, tv_notification_cnt;
    TextView tv_assign_by_me, tv_critical, tv_today, tv_overdue, tv_notacted, tv_ticket, tv_workspacecnt, tv_workcnt, tv_team_mem_cnt,
            tv_subteam_mem_cnt, tv_unapprove, tv_plusSign, tv_unplanned;
    LinearLayout layout_birthday, layout_meeting, layout_notification, lay_Myworkspace, lay_my_team, lay_mywork, lay_workspacewise_Act;
    LinearLayout ls_Team;
    ArrayList<MyTeamBean> lsMyteam=new ArrayList<>();
    List<MyWorkspaceBean> lsmyworkspace=new ArrayList<>();
    private ProgressDialog progressDialog;
    ImageView img_refresh, img_home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(VWBHomeActivity.this);
        setContentView(R.layout.vwb_home_menu_lay);

        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("");
        setSupportActionBar(toolbar_action);
        //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);



        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        settingKey = ut.getSharedPreference_SettingKey(context);
        dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);
        sql = db.getWritableDatabase();
        init();



        if (isnet()) {
            progressDialog = new ProgressDialog(VWBHomeActivity.this);
            progressDialog.setCancelable(true);
            if (!isFinishing()) {
                progressDialog.show();
            }
            progressDialog.setContentView(R.layout.crm_progress_lay);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            new StartSession(VWBHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {
                    new DownloadMyWorkDataJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(VWBHomeActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(VWBHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }



        if (isnet()) {
            new StartSession(VWBHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadTeamDataJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(VWBHomeActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(VWBHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }

        if (isnet()) {
            new StartSession(VWBHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadMyWorkspaceDataJSON().execute();
                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(VWBHomeActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(VWBHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }


        if (isnet()) {
            new StartSession(VWBHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new DownloadMyWorkDataJSON().execute();

                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(VWBHomeActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(VWBHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }


        if (isnet()) {
            new StartSession(VWBHomeActivity.this, new CallbackInterface() {
                @Override
                public void callMethod() {

                    new downloadworkspacecnt().execute();

                }

                @Override

                public void callfailMethod(String msg) {
                    ut.displayToast(VWBHomeActivity.this, msg);
                }
            });
        } else {
            ut.displayToast(VWBHomeActivity.this, "No Internet connection");
            //  Toast.makeText(ActivityMain.this, , Toast.LENGTH_LONG).show();
        }


    }

    private void init() {
        img_back = findViewById(R.id.img_back);
        txt_title = findViewById(R.id.txt_title);
        txt_title.setText(R.string.app_name_toolbar_Vwb);
        img_home = findViewById(R.id.img_home);

        lay_mywork = (LinearLayout) findViewById(R.id.lay_mywork);

        lay_Myworkspace = (LinearLayout) findViewById(R.id.lay_Myworkspace);
        lay_workspacewise_Act = (LinearLayout) findViewById(R.id.lay_workspacewise_Act);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_birthday_cnt = (TextView) findViewById(R.id.tv_birthday_cnt);
        tv_meeting_cnt = (TextView) findViewById(R.id.tv_meeting_cnt);
        tv_notification_cnt = (TextView) findViewById(R.id.tv_notification_cnt);
        layout_birthday = (LinearLayout) findViewById(R.id.layout_birthday);
        layout_notification = (LinearLayout) findViewById(R.id.layout_notification);
        layout_meeting = (LinearLayout) findViewById(R.id.layout_meeting);
        lay_my_team = (LinearLayout) findViewById(R.id.lay_my_team);
        lay_critical = (LinearLayout) findViewById(R.id.lay_critical);
        lay_assign_by_me = (LinearLayout) findViewById(R.id.lay_assign_by_me);
        lay_unapprove = (LinearLayout) findViewById(R.id.lay_unapprove);
        lay_not_acted = (LinearLayout) findViewById(R.id.lay_not_acted);
        lay_overdue = (LinearLayout) findViewById(R.id.lay_overdue);
        lay_today = (LinearLayout) findViewById(R.id.lay_today);
        lay_ticket = (LinearLayout) findViewById(R.id.lay_ticket);
        //ls_Myworkspace = (ListView) findViewById(R.id.ls_Myworkspace);
        // tv_new = (TextView) findViewById(R.id.tv_new);
        tv_critical = (TextView) findViewById(R.id.tv_critical);
        tv_notacted = (TextView) findViewById(R.id.tv_notacted);
        tv_overdue = (TextView) findViewById(R.id.tv_overdue);
        tv_today = (TextView) findViewById(R.id.tv_today);
        tv_ticket = (TextView) findViewById(R.id.tv_ticket);
        //  tv_unplanned = (TextView) findViewById(R.id.tv_unplanned);
        tv_workspacecnt = (TextView) findViewById(R.id.tv_workspacecnt);
        tv_assign_by_me = (TextView) findViewById(R.id.tv_assign_by_me);
        tv_workcnt = (TextView) findViewById(R.id.tv_workcnt);
        ls_Team = (LinearLayout) findViewById(R.id.ls_Team);
        tv_team_mem_cnt = (TextView) findViewById(R.id.tv_team_mem_cnt);
        tv_subteam_mem_cnt = (TextView) findViewById(R.id.tv_subteam_mem_cnt);
        tv_plusSign = (TextView) findViewById(R.id.tv_plusSign);
        tv_unapprove = (TextView) findViewById(R.id.tv_unapprove);


        SetListener();


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

    private void SetListener() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        img_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(VWBHomeActivity.this, VWBRightMenuActivity.class).
                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }


        });

        lay_workspacewise_Act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VWBHomeActivity.this, WorkspacewiseActCntActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });


        lay_my_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VWBHomeActivity.this, MyTeamActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        lay_critical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_critical.getText().toString().equals("0")) {

                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_critical");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_not_acted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_notacted.getText().toString().equals("0")) {
                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_noted");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_overdue.getText().toString().equals("0")) {
                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_overdue");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_today.getText().toString().equals("0")) {
                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_today");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_assign_by_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_assign_by_me.getText().toString().equals("0")) {

                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_assign_me");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
        lay_unapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tv_unapprove.getText().toString().equals("0")) {
                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_pending");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }
            }
        });

        lay_mywork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                intent.putExtra("activty","act_work");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
              /*  updateList_Paging();
                drawer_layout.closeDrawers();*/

            }
        });

        lay_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_ticket.getText().toString().equals("0")) {
                } else {
                    Intent intent = new Intent(VWBHomeActivity.this, ActivityMain.class);
                    intent.putExtra("activty","act_ticket");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
    }



    // API Call


    class DownloadMyWorkDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
          //  String url = CompanyURL + WebUrlClass.api_Mywork;
            String url = CompanyURL + WebUrlClass.GetMyWorkMYTeamCombine;
            try {
                res = ut.OpenConnection(url, VWBHomeActivity.this);
                response = res.toString();
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
                JSONObject  obj = new JSONObject(response);
                String Msgcontent=obj.getString("Work");
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(Msgcontent);
                sql.delete(db.TABLE_MYWORK, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYWORK, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }
                    long a = sql.insert(db.TABLE_MYWORK, null, values);

                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    //  ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
            }
            progressDialog.dismiss();
          //  getMyWorkOnly();
            getMyWork();

        }

    }
/*
    private void getMyWorkOnly() {
        String query = "SELECT * FROM " + db.TABLE_MYWORK;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_critical.setText(cur.getString(cur.getColumnIndex("Critical")));
            tv_notacted.setText(cur.getString(cur.getColumnIndex("NotActed")));
            tv_overdue.setText(cur.getString(cur.getColumnIndex("Overdue")));
            tv_ticket.setText(cur.getString(cur.getColumnIndex("Tickets")));
            String a = cur.getString(cur.getColumnIndex("Tickets"));
            if (a.equalsIgnoreCase("0")) {
                lay_ticket.setVisibility(View.GONE);
            } else {
                lay_ticket.setVisibility(View.VISIBLE);
            }
            tv_today.setText(cur.getString(cur.getColumnIndex("Today")));
            tv_workcnt.setText(cur.getString(cur.getColumnIndex("TotalCount")));
            tv_assign_by_me.setText(cur.getString(cur.getColumnIndex("AssByCount")));
            tv_unapprove.setText(cur.getString(cur.getColumnIndex("UnApproved")));

        }
    }
*/

    private void getMyWork() {
        String query = "SELECT * FROM " + db.TABLE_MYWORK;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_critical.setText(cur.getString(cur.getColumnIndex("Critical")));
            String c = cur.getString(cur.getColumnIndex("Critical"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_critical.setVisibility(View.GONE);
            } else {
                if (c.equalsIgnoreCase("0")) {
                    lay_critical.setVisibility(View.GONE);
                } else {
                    lay_critical.setVisibility(View.VISIBLE);
                }
            }
            tv_notacted.setText(cur.getString(cur.getColumnIndex("NotActed")));
            String n = cur.getString(cur.getColumnIndex("NotActed"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_not_acted.setVisibility(View.GONE);
            } else {
                if (n.equalsIgnoreCase("0")) {
                    lay_not_acted.setVisibility(View.GONE);
                } else {
                    lay_not_acted.setVisibility(View.GONE);
                }
            }

            tv_overdue.setText(cur.getString(cur.getColumnIndex("Overdue")));
            String o = cur.getString(cur.getColumnIndex("Overdue"));
            if (o.equalsIgnoreCase("0")) {
                lay_overdue.setVisibility(View.GONE);
            } else {
                lay_overdue.setVisibility(View.VISIBLE);
            }
            tv_ticket.setText(cur.getString(cur.getColumnIndex("Tickets")));
            String a = cur.getString(cur.getColumnIndex("Tickets"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_ticket.setVisibility(View.GONE);
            } else {
                if (a.equalsIgnoreCase("0")) {
                    lay_ticket.setVisibility(View.GONE);
                } else {
                    lay_ticket.setVisibility(View.VISIBLE);
                }
            }
            tv_today.setText(cur.getString(cur.getColumnIndex("Today")));
            String today = cur.getString(cur.getColumnIndex("Today"));
            if (today.equalsIgnoreCase("0")) {
                lay_today.setVisibility(View.GONE);
            } else {
                lay_today.setVisibility(View.VISIBLE);
            }


            tv_workcnt.setText(cur.getString(cur.getColumnIndex("TotalCount")));

            tv_assign_by_me.setText(cur.getString(cur.getColumnIndex("AssByCount")));
            String a1 = cur.getString(cur.getColumnIndex("AssByCount"));
            if (Constants.type == Constants.Type.Sahara) {
                lay_assign_by_me.setVisibility(View.VISIBLE);
            } else {
                if (a1.equalsIgnoreCase("0")) {
                    lay_assign_by_me.setVisibility(View.GONE);
                } else {
                    lay_assign_by_me.setVisibility(View.VISIBLE);
                }
            }
            tv_unapprove.setText(cur.getString(cur.getColumnIndex("UnApproved")));
            String a2 = cur.getString(cur.getColumnIndex("UnApproved"));

            if (Constants.type == Constants.Type.Sahara) {
                lay_unapprove.setVisibility(View.GONE);
            } else {
                if (a2.equalsIgnoreCase("0")) {
                    lay_unapprove.setVisibility(View.GONE);
                } else {
                    lay_unapprove.setVisibility(View.VISIBLE);
                }
            }


        }
    }

    private void getMyTeamData() {
        lsMyteam.clear();
        String query = "SELECT * FROM " + db.TABLE_MYTEAM;
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {
            lay_my_team.setVisibility(View.VISIBLE);

            tv_team_mem_cnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyTeamBean bean = new MyTeamBean();
                bean.setUserName(cur.getString(cur.getColumnIndex("UserName")));
                bean.setTotalOverdueActivities(cur.getString(cur.getColumnIndex("TotalOverdueActivities")));
                bean.setTotalCount(cur.getString(cur.getColumnIndex("TotalAssigned")));
                bean.setUserMasterId(cur.getString(cur.getColumnIndex("UserMasterId")));
                lsMyteam.add(bean);
            } while (cur.moveToNext());
            if (lsMyteam.size() > 0) {
                ls_Team.removeAllViews();
                if (lsMyteam.size() < 5) {
                    for (int k = 0; k < lsMyteam.size(); k++) {
                        add_viewTeam(k);
                    }
                } else {
                    for (int k = 0; k < 5; k++) {
                        add_viewTeam(k);
                    }
                }
            }
        }
    }
    class DownloadTeamDataJSON extends AsyncTask<Integer, Void, String> {
        Object res, res_subteam;
        String response, response_subteam;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            //to get count of team
            String url = CompanyURL + WebUrlClass.api_MyTeam;
            try {
                res = ut.OpenConnection(url, VWBHomeActivity.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                try {


                    JSONArray jResults = new JSONArray(response);
                    sql.delete(db.TABLE_MYTEAM, null,
                            null);
                    Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYTEAM, null);
                    int count = c.getCount();
                    String columnName, columnValue;
                    for (int i = 0; i < jResults.length(); i++) {
                        JSONObject jorder = jResults.getJSONObject(i);
                        for (int j = 0; j < c.getColumnCount(); j++) {

                            columnName = c.getColumnName(j);
                            columnValue = jorder.getString(columnName);
                            values.put(columnName, columnValue);

                        }

                        long a = sql.insert(db.TABLE_MYTEAM, null, values);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }

            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {

                } else {
                    //   ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
            }

            getMyTeamData();
            /*  Toast.makeText(ActivityMain.this,"Get Data",Toast.LENGTH_SHORT).show();*/

            //  new DownloadBirthdayDataJSON().execute();

        }

    }

    public void add_viewTeam(int i) {
        LayoutInflater layoutInflater = (LayoutInflater) VWBHomeActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_custom_myteam,
                null);
        TextView tv_memusername, tv_overdue, tv_total;
        LinearLayout team_member;

        tv_memusername = (TextView) baseView.findViewById(R.id.tv_memusername);
        tv_overdue = (TextView) baseView.findViewById(R.id.tv_overdue);
        tv_total = (TextView) baseView.findViewById(R.id.tv_total);
        team_member = (LinearLayout) baseView.findViewById(R.id.teammember);
        tv_memusername.setText(lsMyteam.get(i).getUserName());
        tv_total.setText("/" + lsMyteam.get(i).getTotalCount());
        tv_overdue.setText(lsMyteam.get(i).getTotalOverdueActivities());
        final int pos = i;
        team_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VWBHomeActivity.this, MyTeamMemberActivity.class);
                intent.putExtra("UsermasterID", lsMyteam.get(pos).getUserMasterId());
                intent.putExtra("Username", lsMyteam.get(pos).getUserName());
                startActivity(intent);
                finish();
            }
        });
        ls_Team.addView(baseView);
    }

    public class downloadworkspacecnt extends AsyncTask<Integer, Void, String> {
        Object res_subteam;
        String response_subteam;
        String[] resp;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Integer... params) {
            //to get count of team
            String url_subteam = CompanyURL + WebUrlClass.api_myworkspacecnt;

            try {
                res_subteam = ut.OpenConnection(url_subteam, VWBHomeActivity.this);
                response_subteam = res_subteam.toString().replaceAll("\\\\", "");
                response_subteam = response_subteam.replaceAll("u0026", "&");
                response_subteam = response_subteam.substring(1, response_subteam.length() - 1);
                String resp1 = response_subteam;


            } catch (Exception e) {
                e.printStackTrace();
                response_subteam = WebUrlClass.Errormsg;
            }


            return response_subteam;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);

            if (res != null) {
                try {
                    JSONArray jsonArray = new JSONArray(res);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String count = jsonObject.getString("OpenActivities");
                        tv_workspacecnt.setText(count);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                tv_workspacecnt.setText(0);

            }
        }


    }

    private void getWorkspaceData() {
        lsmyworkspace.clear();
        String query = "SELECT * FROM " + db.TABLE_MYWORKSPACE;
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<MyWorkspaceBean> ls = new ArrayList<MyWorkspaceBean>();
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            //tv_workspacecnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyWorkspaceBean bean = new MyWorkspaceBean();

                double number = cur.getDouble(cur.getColumnIndex("OnTimePerc"));

                int integer = (int) number;
                double decimal = (10 * number - 10 * integer) / 10;
                bean.setOnTime(integer + " %");
                bean.setOpenActivities(cur.getString(cur.getColumnIndex("OpenActivities")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                lsmyworkspace.add(bean);
            } while (cur.moveToNext());
        }
        if (lsmyworkspace.size() > 0) {
            lay_Myworkspace.removeAllViews();
            if (lsmyworkspace.size() > 0 && lsmyworkspace.size() < 5) {
                for (int k = 0; k < lsmyworkspace.size(); k++) {
                    addView_new(k);
                }
            } else if (lsmyworkspace.size() >= 5) {
                for (int k = 0; k < 5; k++) {
                    addView_new(k);
                }
            }
        }
        //getMyWork();
    }

    private void getWorkspaceDataOnly() {
        lsmyworkspace.clear();
        String query = "SELECT * FROM " + db.TABLE_MYWORKSPACE;
        Cursor cur = sql.rawQuery(query, null);
        ArrayList<MyWorkspaceBean> ls = new ArrayList<MyWorkspaceBean>();
        int cnt = cur.getCount();
        if (cur.getCount() > 0) {
            // tv_workspacecnt.setText("" + cur.getCount());
            cur.moveToFirst();
            do {
                MyWorkspaceBean bean = new MyWorkspaceBean();
                double number = cur.getDouble(cur.getColumnIndex("OnTimePerc"));
                int integer = (int) number;
                double decimal = (10 * number - 10 * integer) / 10;
                bean.setOnTime(integer + " %");
                bean.setOpenActivities(cur.getString(cur.getColumnIndex("OpenActivities")));
                bean.setProjectName(cur.getString(cur.getColumnIndex("ProjectName")));
                lsmyworkspace.add(bean);
            } while (cur.moveToNext());
        }
        if (lsmyworkspace.size() > 0) {
            lay_Myworkspace.removeAllViews();
            if (lsmyworkspace.size() > 0 && lsmyworkspace.size() < 5) {
                for (int k = 0; k < lsmyworkspace.size(); k++) {
                    addView_new(k);
                }
            } else if (lsmyworkspace.size() >= 5) {
                for (int k = 0; k < 5; k++) {
                    addView_new(k);
                }
            }
        }
    }

    public void addView_new(final int i) {

        LayoutInflater layoutInflater = (LayoutInflater) VWBHomeActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_custom_myworkspace, null);

        final TextView tv_Workspacename, tv_Openactivity, tv_onTime;
        final LinearLayout ln_workSpace;
        ln_workSpace = (LinearLayout) baseView.findViewById(R.id.workview);
        tv_Workspacename = (TextView) baseView.findViewById(R.id.tv_Workspacename);
        tv_Openactivity = (TextView) baseView.findViewById(R.id.tv_Openactivity);
        tv_onTime = (TextView) baseView.findViewById(R.id.tv_onTime);


        tv_Workspacename.setText(lsmyworkspace.get(i).getProjectName());
        tv_Openactivity.setText(lsmyworkspace.get(i).getOpenActivities());
        tv_onTime.setText(lsmyworkspace.get(i).getOnTime());

        ln_workSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String que = "SELECT ProjectId FROM " + db.TABLE_MYWORKSPACE + " WHERE ProjectName='" + lsmyworkspace.get(i).getProjectName() + "'";
                Cursor cur = sql.rawQuery(que, null);
                String ProjectId = null;
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    ProjectId = cur.getString(cur.getColumnIndex("ProjectId"));
                }
                Intent intent = new Intent(VWBHomeActivity.this, WorkspacewiseActDetailActivity.class);
                intent.putExtra("ProjectId", ProjectId);
                intent.putExtra("ProjectName", lsmyworkspace.get(i).getProjectName());
                startActivity(intent);
                finish();
            }
        });
        lay_Myworkspace.addView(baseView);

    }
    class DownloadMyWorkspaceDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showProgressDialog();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_MyWorkspace;
            try {
                res = ut.OpenConnection(url, VWBHomeActivity.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
               /* response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
              *//*  res = res.replaceAll("\\\\\"", "");
                res = res.replaceAll("\"", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MYWORKSPACE, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MYWORKSPACE, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);
                    }

                    long a = sql.insert(db.TABLE_MYWORKSPACE, null, values);
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    // ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                //  ut.displayToast(ActivityMain.this, "Server error...");
            }
            getWorkspaceData();
            getWorkspaceDataOnly();
        }

    }


    class DownloadValidBackDateDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;
        String ValidBackDate;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_valid_backdate_entry;
            try {
                res = ut.OpenConnection(url, VWBHomeActivity.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                response = response.substring(1, response.length() - 1);
                ValidBackDate = response;


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.setError;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (!(res.equalsIgnoreCase(WebUrlClass.setError))) {
               /* DatabaseHandlers db = new DatabaseHandlers(ActivityMain.this);
                SQLiteDatabase sql = db.getWritableDatabase();*/
                ContentValues cv = new ContentValues();
                cv.put(WebUrlClass.GET_BACKDATE_TIMESHEET_KEY, ValidBackDate);
                sql.update(db.TABLE_SETTING, cv, "LogInKey=?", new String[]{settingKey});

            } else {

            }


        }

    }

    class DownloadMeetingDataJSON extends AsyncTask<Integer, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... params) {
            String url = CompanyURL + WebUrlClass.api_Meetings;
            try {
                res = ut.OpenConnection(url, VWBHomeActivity.this);
                response = res.toString().replaceAll("\\\\\\\\\\\"", "");
                response = response.replaceAll("\\\\", "");
                response = response.replaceAll("u0026", "&");
                /*response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");*/
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_MEETING, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_MEETING, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_MEETING, null, values);
                    Log.e("sqlinsert :", "" + a);

                }


            } catch (Exception e) {
                e.printStackTrace();
                response = WebUrlClass.Errormsg;
            }
            return response;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            if (res != null) {
                if (!(res.equalsIgnoreCase(WebUrlClass.Errormsg))) {
                } else {
                    ///   ut.displayToast(ActivityMain.this, "Server error...");
                }
            } else {
                // ut.displayToast(ActivityMain.this, "Server error...");
            }
            UpdateMeetingOnly();

        }

    }

    private void UpdateMeetingOnly() {
        String query = "SELECT * FROM " + db.TABLE_MEETING;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            tv_meeting_cnt.setVisibility(View.VISIBLE);
            tv_meeting_cnt.setText(cur.getCount() + "");
        } else {
            tv_meeting_cnt.setText(0 + "");
        }


    }


}
