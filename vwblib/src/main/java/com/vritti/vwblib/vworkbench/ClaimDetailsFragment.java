package com.vritti.vwblib.vworkbench;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.vritti.vwblib.Adapter.ClaimDetailsFrangentAdapter;
import com.vritti.vwblib.R;

/**
 * Created by 300151 on 11/18/2016.
 */
public class ClaimDetailsFragment extends Fragment {
    private View rootView;
    public static Button btn_add_claim;
    public static TextView tv_Cdate, tv_Camount, fromPlace, ToPlace, tv_mode, tv_travelling, tv_lodging, tv_food, tv_Local, tv_Ph, tv_Maintenanace;
    public static ListView lay_claim_details;
    public static Spinner claim_action;
    public static ImageView tv_claim_edit, tv_claim_cancel;
    public static ClaimDetailsFrangentAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.vwb_fragment_claim_details, container,
                false);

        InitView(rootView);
        setListner();
        //((BaseAdapter) lay_claim_details.getAdapter()).notifyDataSetChanged();
        if (ClaimDetailActivity.lsCalimDetails.size() > 0&&adapter!=null) {
            adapter = new ClaimDetailsFrangentAdapter(getContext(), ClaimDetailActivity.lsCalimDetails);
            lay_claim_details.setAdapter(adapter);
        }

        return rootView;

    }

    private void InitView(View rootView) {
        btn_add_claim = (Button) rootView.findViewById(R.id.btn_add_claim);
        lay_claim_details = (ListView) rootView.findViewById(R.id.lay_claim_details);

    }

    private void setListner() {
        btn_add_claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClaimDetailActivity.class);
                startActivityForResult(intent, 11);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {

            adapter = new ClaimDetailsFrangentAdapter(getContext(), ClaimDetailActivity.lsCalimDetails);
            lay_claim_details.setAdapter(adapter);

        }
    }

   /* public void addView_new(final int i) {
        String[] claimAction = {"Edit", "Cancel"};
        LayoutInflater layoutInflater = (LayoutInflater) getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View baseView = layoutInflater.inflate(R.layout.vwb_claim_details_item,
                null);
        // claim_action = (Spinner) baseView.findViewById(R.id.claim_action);
        tv_claim_cancel = (ImageView) baseView.findViewById(R.id.tv_claim_cancel);
        tv_claim_edit = (ImageView) baseView.findViewById(R.id.tv_claim_edit);
        tv_Camount = (TextView) baseView.findViewById(R.id.tv_Total);
        tv_travelling = (TextView) baseView.findViewById(R.id.tv_travelling);
        tv_Cdate = (TextView) baseView.findViewById(R.id.tv_clim_date);
        fromPlace = (TextView) baseView.findViewById(R.id.fromPlace);
        ToPlace = (TextView) baseView.findViewById(R.id.ToPlace);
        tv_food = (TextView) baseView.findViewById(R.id.tv_food);
        tv_Local = (TextView) baseView.findViewById(R.id.tv_Local);
        tv_lodging = (TextView) baseView.findViewById(R.id.tv_lodging);
        tv_Maintenanace = (TextView) baseView.findViewById(R.id.tv_Maintenanace);
        tv_mode = (TextView) baseView.findViewById(R.id.tv_mode);
        tv_Ph = (TextView) baseView.findViewById(R.id.tv_Ph);

        tv_Cdate.setText(ClaimDetailActivity.lsCalimDetails.get(i).getClaimDate());
        tv_Camount.setText(ClaimDetailActivity.lsCalimDetails.get(i).getAmount());
        fromPlace.setText(ClaimDetailActivity.lsCalimDetails.get(i).getFromPlace());
        ToPlace.setText(ClaimDetailActivity.lsCalimDetails.get(i).getToPlace());
        tv_food.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_food());
        tv_Local.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_Local());
        tv_lodging.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_lodging());
        tv_Maintenanace.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_Maintenanace());
        tv_mode.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_mode());
        tv_Ph.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_Ph());
        tv_travelling.setText(ClaimDetailActivity.lsCalimDetails.get(i).getTv_travelling());
       *//* ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, claimAction);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        claim_action.setAdapter(dataAdapter);
*//*

       *//* SimpleImageArrayAdapter adapter = new SimpleImageArrayAdapter(getContext(),
                new Integer[]{R.drawable.ic_info, R.drawable.ic_critical});
        claim_action.setAdapter(adapter);*//*

        tv_claim_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClaimDetailActivity.class);
                intent.putExtra("Action", "Edit");
                intent.putExtra("Position", i);
                intent.putExtra("ClaimDate", tv_Cdate.getText().toString());
                intent.putExtra("fromPlace", fromPlace.getText().toString());
                intent.putExtra("ToPlace", ToPlace.getText().toString());
                intent.putExtra("food", tv_food.getText().toString());
                intent.putExtra("Local", tv_Local.getText().toString());
                intent.putExtra("lodging", tv_lodging.getText().toString());
                intent.putExtra("Maintenanace", tv_Maintenanace.getText().toString());
                intent.putExtra("mode", tv_mode.getText().toString());
                intent.putExtra("Ph", tv_Ph.getText().toString());
                intent.putExtra("travelling", tv_travelling.getText().toString());
                startActivity(intent);
            }
        });

        lay_claim_details.addView(baseView);
    }*/


    @Override
    public void onResume() {
        super.onResume();

        adapter = new ClaimDetailsFrangentAdapter(getContext(), ClaimDetailActivity.lsCalimDetails);
        lay_claim_details.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
