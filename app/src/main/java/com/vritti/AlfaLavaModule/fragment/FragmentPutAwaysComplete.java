package com.vritti.AlfaLavaModule.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vritti.AlfaLavaModule.activity.PutAwayDetails;
import com.vritti.AlfaLavaModule.adapter.Adp_Putaway_Frag_Complete;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;

/**
 * Created by Admin-1 on 10/14/2016.
 */
public class FragmentPutAwaysComplete extends Fragment {
    private RecyclerView recycler;
    private Adp_Putaway_Frag_Complete adapter;
    private ArrayList<PutAwaysBean> list;
    private String s,S_HDR;
    View Creatview;


    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;


    public FragmentPutAwaysComplete() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        s = PutAwayDetails.GRN_ID;
        S_HDR = PutAwayDetails.GRN_Header;
        Creatview = inflater.inflate(R.layout.alfa_fragment_putaway_complete, container, false);
        initView(Creatview);
        completed_Putaway();
        return Creatview;
    }

    private void completed_Putaway() {
        list.clear();

        userpreferences = getContext().getSharedPreferences(WebUrlClass.USERINFO, Context.MODE_PRIVATE);
        ut = new Utility();
        cf = new CommonFunction(getContext());
        String settingKey = ut.getSharedPreference_SettingKey(getContext());
        String dabasename = ut.getValue(getContext(), WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(getContext(), dabasename);
        sql = db.getWritableDatabase();
        CompanyURL = ut.getValue(getContext(), WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(getContext(), WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(getContext(), WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(getContext(), WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(getContext(), WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(getContext(), WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(getContext(), WebUrlClass.GET_USERNAME_KEY, settingKey);
        IsChatApplicable = ut.getValue(getContext(), WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);


        int count = 0;
      /*  Cursor c1 = sql.rawQuery("DELETE FROM '" + fd.TABLE_PUTAWAY + "'", null);
        c1.getCount();*/
        String y = "Y";
        try {
            Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_PUTAWAY + " WHERE GRNHeader ='"+ S_HDR +"' AND DoneFlag='"+ WebUrlClass.DoneFlag_Complete+"'", null);//WHERE GRNId =" + GRN_ID + "
            if (c.getCount() == 0) {

            } else {
                c.moveToFirst();
                do {
                    PutAwaysBean bean = new PutAwaysBean();
                    bean.setPutAwaySr(c.getInt(c.getColumnIndex("PutAwaysr")));
                    bean.setGRN_Number(c.getString(c.getColumnIndex("GRNNumber")));
                    bean.setGRN_Header(c.getString(c.getColumnIndex("GRNHeader")));
                    bean.setSuggPutAwayId(c.getString(c.getColumnIndex("SuggPutAwayId")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setLocationMasterId(c.getString(c.getColumnIndex("LocationMasterId")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setItemDesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    bean.setPutAwayQty(c.getString(c.getColumnIndex("PutAwayQty")));
                    bean.setFlgDone(c.getString(c.getColumnIndex("DoneFlag")));
                    bean.setFlgInsertUpdate(c.getString(c.getColumnIndex("InsertUpadate")));
                    list.add(bean);
                } while (c.moveToNext());
                adapter = new Adp_Putaway_Frag_Complete(list);
                recycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private void initView(View creatview) {
        recycler = (RecyclerView) creatview.findViewById(R.id.my_recycler_complete);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<PutAwaysBean>();

    }

    @Override
    public void onResume() {
        super.onResume();
        s = PutAwayDetails.GRN_ID;
        S_HDR = PutAwayDetails.GRN_Header;
        completed_Putaway();
    }
}
