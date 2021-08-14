package com.vritti.crm.vcrm7;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.vritti.ekatm.R;
import com.vritti.databaselib.other.WebUrlClass;

public class PromotionalFormSettingActivity extends AppCompatActivity {
    CheckBox checkBoxCity, checkBoxAddress, checkBoxEmail, checkBoxAge,
            checkBoxOccupation, checkBoxPurchasemode, checkBoxBuyplan,
            checkBoxProduct, checkBoxRemark;
    String City="", Address="", Email="", Age="",
            Occupation="", Purchasemode="", Buyplan="",
            Product="", Remark="";
    Button button_add_enqsetting, button_cancel_enq_setting;
    SharedPreferences sharedpreferences;

    String spCity="", spAddress="", spEmail="", spAge="",
            spOccupation="", spPurchasemode="", spBuyplan="",
            spProduct="", spRemark="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_activity_enquiry_form_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // toolbar.setLogo(R.mipmap.ic_toolbar_logo_crm);
        toolbar.setTitle(R.string.app_name_toolbar_CRM);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        spCity = sharedpreferences.getString("City", "");
        spAddress = sharedpreferences.getString("Address", "");
        spEmail = sharedpreferences.getString("Email", "");
        spOccupation = sharedpreferences.getString("Occupation", "");
        spPurchasemode = sharedpreferences.getString("Purchasemode", "");
        spBuyplan = sharedpreferences.getString("Buyplan", "");
        spProduct = sharedpreferences.getString("Product", "");
        spRemark = sharedpreferences.getString("Remark", "");
        spAge = sharedpreferences.getString("Age", "");
        if (spCity.equalsIgnoreCase("Checked")) {
            checkBoxCity.setChecked(true);
            City = "Checked";
        } else {
            checkBoxCity.setChecked(false);
        }
      /*  checkBoxCity, checkBoxAddress, checkBoxEmail, checkBoxAge,
                checkBoxOccupation, checkBoxPurchasemode, checkBoxBuyplan,
                checkBoxProduct, checkBoxRemark;*/
        if (spAddress.equalsIgnoreCase("Checked")) {
            checkBoxAddress.setChecked(true);
            Address = "Checked";
        } else {
            checkBoxAddress.setChecked(false);
        }

        if (spEmail.equalsIgnoreCase("Checked")) {
            checkBoxEmail.setChecked(true);
            Email = "Checked";
        } else {
            checkBoxEmail.setChecked(false);
        }

        if (spAge.equalsIgnoreCase("Checked")) {
            checkBoxAge.setChecked(true);
            Age = "Checked";
        } else {
            checkBoxAge.setChecked(false);
        }
         /*  checkBoxCity, checkBoxAddress, checkBoxEmail, checkBoxAge,
                checkBoxOccupation, checkBoxPurchasemode, checkBoxBuyplan,
                checkBoxProduct, checkBoxRemark;*/

        if (spOccupation.equalsIgnoreCase("Checked")) {
            checkBoxOccupation.setChecked(true);
            Occupation = "Checked";
        } else {
            checkBoxOccupation.setChecked(false);
        }

        if (spPurchasemode.equalsIgnoreCase("Checked")) {
            checkBoxPurchasemode.setChecked(true);
            Purchasemode = "Checked";
        } else {
            checkBoxPurchasemode.setChecked(false);
        }

        if (spBuyplan.equalsIgnoreCase("Checked")) {
            checkBoxBuyplan.setChecked(true);
            Buyplan = "Checked";
        } else {
            checkBoxBuyplan.setChecked(false);
        }

        if (spProduct.equalsIgnoreCase("Checked")) {
            checkBoxProduct.setChecked(true);
            Product = "Checked";
        } else {
            checkBoxProduct.setChecked(false);
        }

        if (spRemark.equalsIgnoreCase("Checked")) {
            checkBoxRemark.setChecked(true);
            Remark = "Checked";
        } else {
            checkBoxRemark.setChecked(false);
        }


        setlistener();
    }

    private void init() {
        checkBoxCity = (CheckBox) findViewById(R.id.checkBoxCity);
        checkBoxAddress = (CheckBox) findViewById(R.id.checkBoxAddress);
        checkBoxEmail = (CheckBox) findViewById(R.id.checkBoxEmail);
        checkBoxAge = (CheckBox) findViewById(R.id.checkBoxAge);
        checkBoxOccupation = (CheckBox) findViewById(R.id.checkBoxOccupation);
        checkBoxPurchasemode = (CheckBox) findViewById(R.id.checkBoxPurchasemode);
        checkBoxBuyplan = (CheckBox) findViewById(R.id.checkBoxBuyplan);
        checkBoxProduct = (CheckBox) findViewById(R.id.checkBoxProduct);
        checkBoxRemark = (CheckBox) findViewById(R.id.checkBoxRemark);
        button_cancel_enq_setting = (Button) findViewById(R.id.button_cancel_enq_setting);
        button_add_enqsetting = (Button) findViewById(R.id.button_add_enqsetting);
        sharedpreferences = getSharedPreferences(WebUrlClass.MyPREFERENCES,
                Context.MODE_PRIVATE);
    }

    private void setlistener() {
        button_add_enqsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  spCity, spAddress, spEmail, spAge,
                        spOccupation, spPurchasemode, spBuyplan,
                        spProduct, spRemark*/

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("City", City);
                editor.putString("Address", Address);
                editor.putString("Email", Email);
                editor.putString("Age", Age);
                editor.putString("Occupation", Occupation);
                editor.putString("Purchasemode", Purchasemode);
                editor.putString("Buyplan", Buyplan);
                editor.putString("Product", Product);
                editor.putString("Remark", Remark);
                editor.commit();
                onBackPressed();
            }
        });
        button_cancel_enq_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        checkBoxCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    City = "Checked";
                } else {
                    City = "Unchecked";
                }
            }
        });
        checkBoxAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Address = "Checked";
                } else {
                    Address = "Unchecked";
                }
            }
        });
        checkBoxEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Email = "Checked";
                } else {
                    Email = "Unchecked";
                }
            }
        });

        checkBoxAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Age = "Checked";
                } else {
                    Age = "Unchecked";
                }
            }
        });

        checkBoxOccupation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Occupation = "Checked";
                } else {
                    Occupation = "Unchecked";
                }
            }
        });

        checkBoxPurchasemode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Purchasemode = "Checked";
                } else {
                    Purchasemode = "Unchecked";
                }
            }
        });
//checkBoxCity, checkBoxAddress, checkBoxEmail,
        // checkBoxAge, checkBoxOccupation, checkBoxPurchasemode,
        // checkBoxBuyplan, checkBoxProduct, checkBoxRemark

        //City, Address, Email, Age,
        // Occupation, Purchasemode, Buyplan,
        //         Product, Remark;
        checkBoxBuyplan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Buyplan = "Checked";
                } else {
                    Buyplan = "Unchecked";
                }
            }
        });

        checkBoxProduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Product = "Checked";
                } else {
                    Product = "Unchecked";
                }
            }
        });

        checkBoxRemark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    Remark = "Checked";
                } else {
                    Remark = "Unchecked";
                }
            }
        });

    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
       /* Intent i = new Intent(PromotionalFormSettingActivity.this, CallListActivity.class);
        startActivity(i);*/
        PromotionalFormSettingActivity.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}

