package com.vritti.crm.vcrm7;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.ekatm.R;

public class CreateNewOppActivity extends AppCompatActivity {

    ImageView opp_icon;
    LinearLayout ln_opportunity;
    String Opportunity_type="";
    TextView txt_opp_text;
    Button btn_create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_opp);

        initView();


        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewOppActivity.this, ProspectFilterActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
        opp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewOppActivity.this, ProspectFilterActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        opp_icon = findViewById(R.id.img_create_opp);
        txt_opp_text = findViewById(R.id.txt_opp_text);
        btn_create = findViewById(R.id.btn_create);

       // ln_opportunity = findViewById(R.id.ln_opportunity);

        Intent intent = getIntent();
        if (getIntent().hasExtra("newopp")) {
            String Opp = getIntent().getStringExtra("newopp");

            if (Opp.equals("new")){
                txt_opp_text.setText("You do not have new opportunity.");
            }else if (Opp.equals("oppoverdue")){
                txt_opp_text.setText("You do not have overdue opportunities.");
            }else if (Opp.equals("opp_yes")){
                txt_opp_text.setText("You do not have yesterday opportunities.");
            }else if (Opp.equals("opp_today")){
                txt_opp_text.setText("You do not have opportunity for today.");
            }else if (Opp.equals("opp_tomorow")){
                txt_opp_text.setText("You do not have opportunity for tomorrow.");
            }else if (Opp.equals("opp_week")){
                txt_opp_text.setText("You do not have opportunity for this week.");
            }
            if (Opp.equals("newCollection")){
                txt_opp_text.setText("You do not have new collection.");
            }else if (Opp.equals("overdue_collection")){
                txt_opp_text.setText("You do not have overdue collection.");
            }else if (Opp.equals("today_overdue_collection")){
                txt_opp_text.setText("You do not have collection for today.");
            }else if (Opp.equals("tomorrow_overdue_collection")){
                txt_opp_text.setText("You do not have collection for tomorrow.");
            }else if (Opp.equals("week_overdue_collection")){
                txt_opp_text.setText("You do not have collection for this week.");
            }

        }


        //Opportunity_type = intent.getStringExtra("Opportunity_type");




    }
}
