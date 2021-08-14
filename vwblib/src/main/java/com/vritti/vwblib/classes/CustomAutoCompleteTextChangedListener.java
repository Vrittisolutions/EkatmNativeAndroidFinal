package com.vritti.vwblib.classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Adapter.AutocompleteReassignAdapter;
import com.vritti.vwblib.vworkbench.ReassignActivity;

import com.vritti.vwblib.R;

/**
 * Created by 300151 on 7/19/2016.
 */
public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    String CompanyURL, EnvMasterId = "", LoginId = "", Password = "", PlantMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context mContext;
    CommonFunction cf;

    public CustomAutoCompleteTextChangedListener(Context context) {
        this.mContext = context;
        ut = new Utility();
        cf = new CommonFunction(context);
        String settingKey = ut.getSharedPreference_SettingKey(mContext);
        String dabasename = ut.getValue(mContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(mContext, dabasename);
        CompanyURL = ut.getValue(mContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(mContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        LoginId = ut.getValue(mContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(mContext, WebUrlClass.GET_PSW_KEY, settingKey);
        PlantMasterId = ut.getValue(mContext, WebUrlClass.GET_PlantID_KEY, settingKey);

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

            // if you want to see in the logcat what the user types
            // Log.e(TAG, "User input: " + userInput);

            ReassignActivity mainActivity = ((ReassignActivity) mContext);

            // update the adapater
            mainActivity.autocompleteAdapter.notifyDataSetChanged();

            // get suggestions from the database
            String[] myObjs = cf.getAllUserList(userInput.toString());

            mainActivity.autocompleteAdapter = new AutocompleteReassignAdapter(mainActivity, R.layout.vwb_list_view_row_item, myObjs);

            mainActivity.myAutoComplete.setAdapter(mainActivity.autocompleteAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
