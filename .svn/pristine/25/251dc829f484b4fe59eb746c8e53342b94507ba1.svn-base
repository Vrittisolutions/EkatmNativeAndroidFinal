package com.vritti.crm.classes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

import com.vritti.crm.adapter.AutocompleteCustomArrayAdapter;
import com.vritti.crm.bean.CityBean;
import com.vritti.crm.vcrm7.TravelPlanAddActivity;
import com.vritti.ekatm.R;


/**
 * Created by 300151 on 7/19/2016.
 */
public class CustomAutoCompleteTextChangedListener_city implements TextWatcher {

    CommonFunctionCrm dbHandler;
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener_city(Context context){
        this.context = context;
        dbHandler=new CommonFunctionCrm(context);
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

        try{

            // if you want to see in the logcat what the user types
           // Log.e(TAG, "User input: " + userInput);

            TravelPlanAddActivity mainActivity = ((TravelPlanAddActivity) context);

            // update the adapater
            mainActivity.myAdapter.notifyDataSetChanged();

            // get suggestions from the database
            CityBean[] myObjs = dbHandler.getCBItemList(userInput.toString());

            mainActivity.myAdapter = new AutocompleteCustomArrayAdapter(mainActivity, R.layout.crm_list_view_row_item, myObjs);

            mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
