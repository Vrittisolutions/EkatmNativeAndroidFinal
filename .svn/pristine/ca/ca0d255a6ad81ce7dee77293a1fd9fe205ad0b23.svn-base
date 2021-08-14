package com.vritti.vwblib.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vritti.vwblib.Beans.GPSLocationTimeBean;
import com.vritti.vwblib.R;


public class ReportingPersonsGPSLocationAdapter extends BaseAdapter {

	private static ArrayList<GPSLocationTimeBean> searchArrayList;

	private LayoutInflater mInflater;
	Context context;

	public ReportingPersonsGPSLocationAdapter(Context context1,
			ArrayList<GPSLocationTimeBean> results) {
		searchArrayList = results;
		mInflater = LayoutInflater.from(context1);
		context = context1;
	}

	@Override
	public int getCount() {
		return searchArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return searchArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.vwb_custom_gpslocation, null);
			holder = new ViewHolder();
			holder.txtlocationame = (TextView) convertView
					.findViewById(R.id.locationgps);

			holder.txtdatetime = (TextView) convertView
					.findViewById(R.id.datetime);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String s = searchArrayList.get(position).getLocationName();

		if (s.equals("")) {
			holder.txtlocationame.setText("No User Found..");
		} else {
			holder.txtlocationame.setText(searchArrayList.get(position)
					.getLocationName());
			holder.txtdatetime.setText(searchArrayList.get(position)
					.getAddedDt());
		}

		return convertView;
	}

	static class ViewHolder {

		TextView txtlocationame;
		TextView txtdatetime;

	}

}
