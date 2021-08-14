package com.vritti.vwb.vworkbench;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

public class EnoSamplingScreen2 extends AppCompatActivity {
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "";

    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    Context context;
    Toolbar toolbar;
    ProgressBar mProgrss;
    TextView txtHeader;
    Button btn_eatery1, btn_eatry2, btn_medicalstore, btn_haatbazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eno_sampling_screen2);
        init();

        context = EnoSamplingScreen2.this;
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

        setListner();
        Intent i = getIntent();
        String Screen = i.getStringExtra(WebUrlClass.INTENT_ENO_SCREEN);
        if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY1)) {
            btn_eatery1.setEnabled(true);
            btn_eatry2.setEnabled(false);
            btn_medicalstore.setEnabled(false);
            btn_haatbazar.setEnabled(false);
        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY2)) {
            btn_eatery1.setEnabled(false);
            btn_eatry2.setEnabled(true);
            btn_medicalstore.setEnabled(false);
            btn_haatbazar.setEnabled(false);
        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_MEDICAL)) {
            btn_eatery1.setEnabled(false);
            btn_eatry2.setEnabled(false);
            btn_medicalstore.setEnabled(true);
            btn_haatbazar.setEnabled(false);
        } else if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_HAATBAZAR)) {
            btn_eatery1.setEnabled(false);
            btn_eatry2.setEnabled(false);
            btn_medicalstore.setEnabled(false);
            btn_haatbazar.setEnabled(true);
        }
        if (Screen.equalsIgnoreCase(WebUrlClass.INTENT_ENO_EATERY1)) {

            String A1 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET1();
            String A2 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET1();
            String A3 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET1();
            String B1 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET2();
            String B2 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET2();
            String B3 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityET2();
            String C1 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityMS();
            String C2 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityMS();
            String C3 = EnoSamplingScreen1.enoFinalList.get(0)
                    .getTotalQuantityMS();


            if (((A1.equalsIgnoreCase("")))
                    && ((A2.equalsIgnoreCase("")))
                    && ((A3.equalsIgnoreCase("")))) {
                btn_eatery1.setEnabled(true);
                btn_eatry2.setEnabled(false);
                btn_medicalstore.setEnabled(false);
                btn_haatbazar.setEnabled(false);

            } else if (((B1.equalsIgnoreCase("")))
                    && ((B2.equalsIgnoreCase("")))
                    && ((B3.equalsIgnoreCase("")))) {
                btn_eatery1.setEnabled(false);
                btn_eatry2.setEnabled(true);
                btn_medicalstore.setEnabled(false);
                btn_haatbazar.setEnabled(false);

            } else if (((C1.equalsIgnoreCase("")))
                    && ((C2.equalsIgnoreCase("")))
                    && ((C3.equalsIgnoreCase("")))) {
                btn_eatery1.setEnabled(false);
                btn_eatry2.setEnabled(false);
                btn_medicalstore.setEnabled(true);
                btn_haatbazar.setEnabled(false);

            } else {
                btn_eatery1.setEnabled(false);
                btn_eatry2.setEnabled(false);
                btn_medicalstore.setEnabled(false);
                btn_haatbazar.setEnabled(true);
            }
        }

    }


    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);
        toolbar.setTitle(R.string.app_name_toolbar_Vwb);
        setSupportActionBar(toolbar);
        mProgrss = (ProgressBar) findViewById(R.id.toolbar_progress_logging);
        txtHeader = (TextView) findViewById(R.id.txt_header);
        btn_eatery1 = (Button) findViewById(R.id.btn_eateries1);
        btn_eatry2 = (Button) findViewById(R.id.btn_eateries2);
        btn_medicalstore = (Button) findViewById(R.id.btn_medicalstore);
        btn_haatbazar = (Button) findViewById(R.id.btn_hatazar);
    }

    public void setListner() {

        btn_eatery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EnoSamplingScreen2.this, EnoSamplingScreen3.class);
                i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_EATERY1);
                startActivity(i);
            }
        });

        btn_eatry2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EnoSamplingScreen2.this, EnoSamplingScreen3.class);
                i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_EATERY2);
                startActivity(i);

            }
        });

        btn_medicalstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EnoSamplingScreen2.this, EnoSamplingScreen3.class);
                i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_MEDICAL);
                startActivity(i);
            }
        });

        btn_haatbazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EnoSamplingScreen2.this, EnoSamplingScreen3.class);
                i.putExtra(WebUrlClass.INTENT_ENO_SCREEN, WebUrlClass.INTENT_ENO_HAATBAZAR);
                startActivity(i);
            }
        });
    }

    private void showProgress() {
        mProgrss.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        mProgrss.setVisibility(View.GONE);
    }
}
