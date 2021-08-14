package com.vritti.crm.vcrm7;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

public class TeamsCallListActionActivity extends AppCompatActivity {

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;

    LinearLayout layAmt, textRequest, textReassign;
    String callid, firmname, calltype, SourceId, Usermasterid, callstatus, ExpectedValue,
            Amount, ExpectedCloserDate;
    TextView txtcall, textAmt;
    Toolbar toolbar_action;
    SQLiteDatabase sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_teams_call_list_action);
        init();
        Intent intent = getIntent();
        callid = intent.getStringExtra("callid");
        calltype = intent.getStringExtra("calltype");
        firmname = intent.getStringExtra("firmname");
        Usermasterid = intent.getStringExtra("Usermasterid");
        txtcall.setText(firmname);


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
       UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();
        getData();
        layAmt.setVisibility(View.GONE);
        if (calltype.equalsIgnoreCase("1")) {

            if (Float.parseFloat(ExpectedValue) > 0) {
                layAmt.setVisibility(View.VISIBLE);
                String k = ExpectedCloserDate.substring(0, ExpectedCloserDate.length() - 15);
                String[] v1 = {k};
                textAmt.setText("EV-" + ExpectedValue + " by " + v1[0]);
            } else {
                layAmt.setVisibility(View.GONE);
            }

        } else if (calltype.equalsIgnoreCase("2")) {
            if (Float.parseFloat(Amount) > 0) {
                layAmt.setVisibility(View.VISIBLE);
                textAmt.setText("CV-" + Amount);
            } else {
                layAmt.setVisibility(View.GONE);
            }


        } else {
            layAmt.setVisibility(View.GONE);
        }

        setListener();
    }

    public void init() {
        txtcall = (TextView) findViewById(R.id.txtcall);
        // textEdit = (LinearLayout) findViewById(R.id.textEdit);
        textReassign = (LinearLayout) findViewById(R.id.textReassign);

        toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar_action.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar_action);
        layAmt = (LinearLayout) findViewById(R.id.layAmt);
        textAmt = (TextView) findViewById(R.id.textAmt);

    }

    private void getData() {

        String query = "SELECT SouceId,CallStatus,Amount,ExpectedValue,ExpectedCloserDate FROM "
                + db.TABLE_CRM_CALL_TEAM + " WHERE CallId='" + callid + "'";
        Cursor cur = sql.rawQuery(query, null);

        if (cur.getCount() > 0) {

            cur.moveToFirst();
            SourceId = cur.getString(cur.getColumnIndex("SouceId"));
            callstatus = cur.getString(cur.getColumnIndex("CallStatus"));
            ExpectedValue = cur.getString(cur.getColumnIndex("ExpectedValue"));
            ExpectedCloserDate = cur.getString(cur.getColumnIndex("ExpectedCloserDate"));
            Amount = cur.getString(cur.getColumnIndex("Amount"));
        } else {

        }


    }

    public void setListener() {
        textReassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamsCallListActionActivity.this, ReassignCallsActivity.class);
                intent.putExtra("callid", callid);
                intent.putExtra("firmname", firmname);
                intent.putExtra("Usermasterid", Usermasterid);
                startActivity(intent);
                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
                TeamsCallListActionActivity.this.finish();
            }
        });

    }


}
