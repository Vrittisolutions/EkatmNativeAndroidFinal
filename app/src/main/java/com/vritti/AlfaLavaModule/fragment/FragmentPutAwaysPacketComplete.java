package com.vritti.AlfaLavaModule.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.vritti.AlfaLavaModule.activity.PutAwayPacketDetails;
import com.vritti.AlfaLavaModule.adapter.AdapterPutawayPacketComplete;
import com.vritti.AlfaLavaModule.bean.PutawaysPacketBean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.classes.CommonFunction;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPutAwaysPacketComplete.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPutAwaysPacketComplete#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPutAwaysPacketComplete extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recycler;
    private AdapterPutawayPacketComplete adapter;
    private ArrayList<PutawaysPacketBean> list;
    private String s, S_HDR;
    View Creatview;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "", UserMasterId = "", UserName = "", IsChatApplicable = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunction cf;
    SharedPreferences userpreferences;
    SQLiteDatabase sql;




    private OnFragmentInteractionListener mListener;

    public FragmentPutAwaysPacketComplete() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentPutAwaysPacketComplete.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentPutAwaysPacketComplete newInstance(String param1, String param2) {
        FragmentPutAwaysPacketComplete fragment = new FragmentPutAwaysPacketComplete();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        s = PutAwayPacketDetails.GRN_ID_Packet;
        S_HDR = PutAwayPacketDetails.GRN_Header_Packet;

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

        Creatview = inflater.inflate(R.layout.alfa_fragment_fragment_put_aways_packet_complete, container, false);
        initView(Creatview);
        completed_Putaway();
        return Creatview;
    }
    private void initView(View creatview) {
        recycler = (RecyclerView) creatview.findViewById(R.id.my_recycler_complete);
        recycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(layoutManager);
        list = new ArrayList<PutawaysPacketBean>();
    }
    private void completed_Putaway() {
        list.clear();

        int count = 0;
        String y = "Y";
        try {//TABLE_PUTAWAY_PACKET_DETAIL
            //Cursor c = sql.rawQuery("SELECT * FROM " + fd.TABLE_PUTAWAY_PACKET_DETAIL + " WHERE GRNHeaderId='" + S_HDR + "' AND DoneFlag='" + WebUrlClass.DoneFlag_Complete + "'", null);//WHERE GRNId =" + GRN_ID + "
            Cursor c = sql.rawQuery("SELECT *   FROM " + db.TABLE_PUTAWAY_PACKET_DETAIL, null);
            if (c.getCount() == 0) {
                c.close();
            } else {
                c.moveToFirst();
                do {
                    PutawaysPacketBean bean = new PutawaysPacketBean();
                    bean.setItemdesc(c.getString(c.getColumnIndex("ItemDesc")));
                    bean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
                    bean.setItemPlantId(c.getString(c.getColumnIndex("ItemPlantId")));
                    bean.setPacketMasterId(c.getString(c.getColumnIndex("PacketMasterId")));
                    bean.setGRNDetailId(c.getString(c.getColumnIndex("GRNDetailId")));
                    bean.setGRNHeaderId(c.getString(c.getColumnIndex("GRNHeaderId")));
                    bean.setBalQty(c.getString(c.getColumnIndex("BalQty")));
                    bean.setPacketNo(c.getString(c.getColumnIndex("PacketNo")));
                    bean.setLocationCode(c.getString(c.getColumnIndex("LocationCode")));
                    bean.setLocationMasterId(c.getString(c.getColumnIndex("LocationMasterId")));
                    bean.setLocationDesc(c.getString(c.getColumnIndex("LocationDesc")));
                    bean.setDoneFlag(c.getString(c.getColumnIndex("DoneFlag")));
                    if(c.getString(c.getColumnIndex("DoneFlag")).equals(WebUrlClass.DoneFlag_Complete))
                     list.add(bean);
                } while (c.moveToNext());
            }
            adapter = new AdapterPutawayPacketComplete(list);
            recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        s = PutAwayPacketDetails.GRN_ID_Packet;
        S_HDR = PutAwayPacketDetails.GRN_Header_Packet;
        completed_Putaway();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
