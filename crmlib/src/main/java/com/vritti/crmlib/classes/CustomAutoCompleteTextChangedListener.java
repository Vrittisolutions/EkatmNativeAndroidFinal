package com.vritti.crmlib.classes;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;




import com.vritti.crmlib.R;
import com.vritti.crmlib.adapter.AutocompleteCustomArrayAdapter;
import com.vritti.crmlib.bean.CityBean;
import com.vritti.crmlib.vcrm7.TravelPlanAddActivity;


/**
 * Created by 300151 on 7/19/2016.
 */
public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    CommonFunctionCrm cf;
    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
        cf=new CommonFunctionCrm(context);
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
            CityBean[] myObjs = cf.getCBItemList(userInput.toString());

            mainActivity.myAdapter = new AutocompleteCustomArrayAdapter(mainActivity, R.layout.crm_list_view_row_item, myObjs);

            mainActivity.myAutoComplete.setAdapter(mainActivity.myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}
