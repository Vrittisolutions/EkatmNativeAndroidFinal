package com.vritti.crm.vcrm7;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.crm.adapter.OfflineActivityAdapter;
import com.vritti.crm.bean.OfflineBean;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import java.util.ArrayList;
import java.util.List;

public class ActivityOfflineData extends AppCompatActivity {

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    SharedPreferences userpreferences;
    SharedPreferences timesheetpreferences;
    private Toolbar toolbar;
    static TextView mtextcount;
    static ListView teamlist;
    public static Context mContext;
    private List<OfflineBean> MyData;
    String DeptID;
    public static ProgressBar mProgress;

    ImageView img_add,img_refresh,img_back;
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_offline_data);
        initObj();
        initView();
        setListner();
        updateData();
    }
    private void initObj() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        img_add.setVisibility(View.GONE);
        img_add.setImageDrawable(getResources().getDrawable(R.drawable.save_icon));

        txt_title.setText("Data Sync Status");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mContext = ActivityOfflineData.this;
        context = getApplicationContext();
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

        MyData = new ArrayList<OfflineBean>();
        userpreferences = getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);


    }
    private void initView() {
        teamlist = (ListView) findViewById(R.id.list_offdata);
        mProgress = (ProgressBar) findViewById(R.id.toolbar_progress_set);
        mtextcount = (TextView) findViewById(R.id.mTxtcount);
    }

    private void setListner() {

    }
    public void updateData() {
        List<OfflineBean> MyData = new ArrayList<OfflineBean>();
        MyData.clear();
        SQLiteDatabase sql = db.getWritableDatabase();
        String que = "SELECT * FROM " + db.TABLE_DATA_OFFLINE;
        Cursor cur = sql.rawQuery(que, null);
        mtextcount.setText(""+cur.getCount());
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                OfflineBean bean = new OfflineBean();
                bean.setRecordID(cur.getString(cur.getColumnIndex("recordID")));
                bean.setLinkurl(cur.getString(cur.getColumnIndex("linkurl")));
                bean.setParameter(cur.getString(cur.getColumnIndex("parameter")));
                bean.setOutput(cur.getString(cur.getColumnIndex("output")));
                bean.setRemark(cur.getString(cur.getColumnIndex("remark")));
                bean.setAddedDt(cur.getString(cur.getColumnIndex("AddedDt")));
                bean.setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
                bean.setMethodtype(cur.getString(cur.getColumnIndex("methodtype")));
                MyData.add(bean);
            } while (cur.moveToNext());

        }
        OfflineActivityAdapter myActivityAdapter = new OfflineActivityAdapter(mContext, MyData);
        teamlist.setAdapter(myActivityAdapter);
        sql.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_leave_records, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
