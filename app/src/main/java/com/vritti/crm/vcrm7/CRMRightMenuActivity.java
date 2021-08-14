package com.vritti.crm.vcrm7;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vritti.ekatm.R;

public class CRMRightMenuActivity extends AppCompatActivity {

    TextView txtHome, txtServiceReport, txt_clientmaster,txt_CRMReport;
    TextView txtExecutivePerformance;
    TextView txtProspectMaster;
    TextView txtCallLogs;
    TextView txtTravelPlan, txtoffline, txtpromotional;
    TextView txtEnquiryform, txtpromotinalform;
    TextView txtEnquiryFormSetting,txt_provisional,txt_chatroom;
    ImageView img_add,img_refresh,img_back;
    TextView txt_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_lay_right_menu_new);
        Toolbar toolbar_action = (Toolbar) findViewById(R.id.toolbar);
        toolbar_action.setTitle("");
        setSupportActionBar(toolbar_action);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_title=findViewById(R.id.txt_title);
        img_add=findViewById(R.id.img_add);
        img_back=findViewById(R.id.img_back);

        txt_title.setText("Menu List");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtHome = (TextView)findViewById(R.id.txtHome);
        txtTravelPlan = (TextView)findViewById(R.id.txtTravelPlan);
        txtoffline = (TextView)findViewById(R.id.txtofflinedata);//txtpromotional
        txtpromotional = (TextView)findViewById(R.id.txtpromotional);//txtpromotional

        // txtExecutivePerformance = (TextView)findViewById(R.id.txtExecutivePerformance);
        txtProspectMaster = (TextView)findViewById(R.id.txtProspectMaster);
        txtCallLogs = (TextView)findViewById(R.id.txtCallLogs);
        //  txtCRMReport = (TextView)findViewById(R.id.txt_CRMReport);
        //txt_clientmaster =(TextView )findViewById(R.id.txt_clientmaster);
        // txtEntityMaster = (TextView)findViewById(R.id.txtEntityMaster);
        // txtCityMaster = (TextView)findViewById(R.id.txtCityMaster);
        //  txtBusinessSegMaster = (TextView)findViewById(R.id.txtBusinessSegMaster);
        //  txtTeritoryMaster = (TextView)findViewById(R.id.txtTeritoryMaster);
        //  txtReportlist = (TextView)findViewById(R.id.txtReportlist);
        txtEnquiryform = (TextView)findViewById(R.id.txtEnquiryform);
        txtpromotinalform = (TextView)findViewById(R.id.txtPromotinalForm);
        txt_provisional = (TextView)findViewById(R.id.txt_provisional);
        txtEnquiryFormSetting = (TextView)findViewById(R.id.txtEnquiryFormSetting);
        txt_chatroom = (TextView)findViewById(R.id.txt_chatroom);
        txtServiceReport = (TextView)findViewById(R.id.txt_service_report);
        //   txtEntityMaster = (TextView)findViewById(R.id.txt_entitymaster);
        txt_CRMReport = (TextView)findViewById(R.id.txt_CRMReport);

        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, CallListActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);            }
        });


        /*    txtEntityMaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, EntityMasterSelectionMainActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            });

            txtCRMReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this, CRMDayEndReport.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                }
            });*/

          /*  txt_clientmaster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(CallListActivity.this,ClientMasterFilterActivity.class);
                    startActivity(intent);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
            });
*/

        txtTravelPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, TravelPlanShowFormActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        txtProspectMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, ProspectFilterActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });

        txtCallLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, CRM_CallLogList.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        txtEnquiryform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, AddEnquiry.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

            }
        });
        txtpromotinalform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, PromotionalFormActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();

                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });
        txt_provisional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, AdvanceProvisionalReceiptDisplayActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });
        txtEnquiryFormSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, PromotionalFormSettingActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        txtoffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRMRightMenuActivity.this, ActivityOfflineData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        txtpromotional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRMRightMenuActivity.this, ActivityPromotionalFormselection.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        txt_CRMReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CRMRightMenuActivity.this, CRMDayEndReport.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

        txtServiceReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CRMRightMenuActivity.this, ActivityServiceReportCustomerList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);
            }
        });

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
