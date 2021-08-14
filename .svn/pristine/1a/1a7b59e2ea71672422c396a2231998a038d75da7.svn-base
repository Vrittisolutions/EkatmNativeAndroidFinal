package com.vritti.sales.CounterBilling;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ProgressBar;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.beans.AllCatSubcatItems;
import com.vritti.sales.beans.Tbuds_commonFunctions;

/**
 * Created by 300151 on 7/19/2016.
 */
public class CustomAutoCompleteTextChangedListener_Purchase implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    static Tbuds_commonFunctions tcf;
    Utility ut;
    static SQLiteDatabase sql_db;
    DatabaseHandlers dbhandler;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    public CustomAutoCompleteTextChangedListener_Purchase(Context context){
        this.context = context;

        ut = new Utility();
        tcf = new Tbuds_commonFunctions(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhandler = new DatabaseHandlers(context, dabasename);
        sql_db = dbhandler.getWritableDatabase();
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

            try {
                PurchaceActivity mainActivity = ((PurchaceActivity) context);

                // update the adapater
                mainActivity.myAdapter.notifyDataSetChanged();

                // get suggestions from the database
                AllCatSubcatItems[] myObjs = tcf.getCBItemListRuni(userInput.toString());

                mainActivity.myAdapter = new AutocompleteCustomArrayAdapter_Purchase
                        (mainActivity, R.layout.tbuds_list_view_row_item, myObjs);

                mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);


            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
