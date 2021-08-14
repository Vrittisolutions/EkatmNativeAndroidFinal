package com.vritti.vwb.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vritti.ekatm.R;


/**
 * Created by Admin-1 on 10/31/2017.
 */

public class ActivityTrailDetailAdapter extends RecyclerView.Adapter<ActivityTrailDetailAdapter.DataObjectHolder> {

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);*/

      //  DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return null;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            Log.i("LOG_TAG", "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //  myClickListener.onItemClick(getPosition(), v);
        }
    }
}
