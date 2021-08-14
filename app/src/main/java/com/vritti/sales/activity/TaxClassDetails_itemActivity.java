package com.vritti.sales.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;
/*created by Chetana Salunkhe 12/02/2020*/
public class TaxClassDetails_itemActivity extends AppCompatActivity {
    private Context parent;
    Toolbar toolbar;
    ListView list_taxdtls;
    TextView txtsgstval, txtcgstval, txtigstval, txttaxamt_sgst, txttaxamt_cgst, txttaxamt_igst, txt_fsgst, txt_fcgst, txt_figst,
            txtcodecgst,txtcodesgst,txtcodeigst;
    LinearLayout layigst, laysgcgst;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "",Indentamount,ItemPlantId="";
    DatabaseHandlers db;
    CommonFunction cf;
    Utility ut;
    SQLiteDatabase sql;

    String TaxClassName = "",TaxClassMasterId = "";
    Float TaxPctgVal = 0.0f, TaxAmt = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_class_details_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary1));
        }
        
        init();

        setData();
    }
    
    public void init(){  
        parent = TaxClassDetails_itemActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // toolbar.setLogo(R.mipmap.ic_toolbar_logo_vwb);  //attach sales logo
        toolbar.setTitle("Tax Details");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        list_taxdtls = findViewById(R.id.list_taxdtls);
        layigst = findViewById(R.id.layigst);
        laysgcgst = findViewById(R.id.laysgcgst);
        txt_fsgst = findViewById(R.id.txt_fsgst);
        txt_fcgst = findViewById(R.id.txt_fcgst);
        txt_figst = findViewById(R.id.txt_figst);
        txttaxamt_sgst = findViewById(R.id.txttaxamt_sgst);
        txttaxamt_cgst = findViewById(R.id.txttaxamt_cgst);
        txttaxamt_igst = findViewById(R.id.txttaxamt_igst);
        txtsgstval = findViewById(R.id.txtsgstval);
        txtcgstval = findViewById(R.id.txtcgstval);
        txtigstval = findViewById(R.id.txtigstval);
        txtcodeigst = findViewById(R.id.txtcodeigst);
        txtcodecgst = findViewById(R.id.txtcodecgst);
        txtcodesgst = findViewById(R.id.txtcodesgst);

        ut = new Utility();
        cf = new CommonFunction(TaxClassDetails_itemActivity.this);
        String settingKey = ut.getSharedPreference_SettingKey(TaxClassDetails_itemActivity.this);
        String dabasename = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(TaxClassDetails_itemActivity.this, dabasename);
        CompanyURL = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(TaxClassDetails_itemActivity.this, WebUrlClass.GET_USERNAME_KEY, settingKey);
        sql = db.getWritableDatabase();

        Intent intent = getIntent();
        TaxClassName = intent.getStringExtra("TaxClassName");
        TaxAmt = Float.valueOf(intent.getStringExtra("TaxAmt"));
        TaxPctgVal = Float.valueOf(intent.getStringExtra("TaxPctgVal"));
        TaxClassMasterId = intent.getStringExtra("TaxClassMasterId");

    }

    public void setData(){

        String taxClsCode = "";

        String qry = "Select TaxClassCode from "+db.TABLE_TAXCLASS+" WHERE TaxClassMasterId='"+TaxClassMasterId+"'";
        Cursor c = sql.rawQuery(qry,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            do{
                taxClsCode = c.getString(c.getColumnIndex("TaxClassCode"));

            }while (c.moveToNext());
        }

        if(TaxClassName.contains("IGST") || TaxClassName.contains("igst")){
            layigst.setVisibility(View.VISIBLE);
            laysgcgst.setVisibility(View.GONE);

            txttaxamt_igst.setText(String.format("%.2f",TaxAmt));
            txt_figst.setText(String.format("%.2f",TaxAmt));

            txtigstval.setText(String.valueOf(TaxPctgVal));
            txtcodeigst.setText(taxClsCode);
        }else {
            layigst.setVisibility(View.GONE);
            laysgcgst.setVisibility(View.VISIBLE);

            txttaxamt_sgst.setText(String.format("%.2f",(TaxAmt/2)));
            txt_fsgst.setText(String.format("%.2f",(TaxAmt/2)));
            txttaxamt_cgst.setText(String.format("%.2f",(TaxAmt/2)));
            txt_fcgst.setText(String.format("%.2f",(TaxAmt/2)));

            txtsgstval.setText(String.valueOf(TaxPctgVal/2));
            txtcgstval.setText(String.valueOf(TaxPctgVal/2));

            txtcodecgst.setText(taxClsCode);
            txtcodesgst.setText(taxClsCode);
        }

    }

}
