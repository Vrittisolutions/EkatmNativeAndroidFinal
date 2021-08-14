package com.vritti.vwb.Adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.MyTeamBean;



public class GPSReportingToAdapter extends BaseAdapter {

	private static  ArrayList<MyTeamBean> searchArrayList;

	private LayoutInflater mInflater;
	Context context;

	public GPSReportingToAdapter(Context context1,
								 ArrayList<MyTeamBean> lsMyteam)
	 {
		searchArrayList = lsMyteam;
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

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.vwb_custom_gps_reporting_to_item, null);
			holder = new ViewHolder();
			holder.txtreportingusers = (TextView) convertView
					.findViewById(R.id.reporteesname);
			holder.txtreportingtotalActivity = (TextView) convertView
					.findViewById(R.id.reporteeAssignrd);
			holder.txtreportingoverdue = (TextView) convertView
					.findViewById(R.id.reporteeoverdue);
			holder.txt_location_name=(TextView) convertView.findViewById(R.id.txt_location_name);
			holder.profile_image= (ImageView) convertView.findViewById(R.id.profile_image);


			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//Collections.sort(searchArrayList, new SortComparator());
		String s = searchArrayList.get(position).getUserName();

		if (s.equals("")) {
			holder.txtreportingusers.setText("No Team Found..");
		} else {
			holder.txtreportingusers.setText(searchArrayList.get(position).getUserName());
			holder.txtreportingtotalActivity.setText(searchArrayList.get(position).getTotalCount());
			holder.txtreportingoverdue.setText(searchArrayList.get(position).getTotalOverdueActivities());
			holder.txt_location_name.setText(searchArrayList.get(position).getLocationName());
			String Profileimage=searchArrayList.get(position).getImagePath();
			Glide.with(context).load(Profileimage).apply(RequestOptions.circleCropTransform()).into(holder.profile_image);

		}
		return convertView;
	}

	static class ViewHolder {
		TextView txtreportingusers;
		TextView txtreportingtotalActivity;
		TextView txtreportingoverdue,txt_location_name;
		ImageView profile_image;
	}

	/*public class SortComparator implements Comparator {

		public int compare(Object o1, Object o2) {
			reportingtogpshelper p1 = (reportingtogpshelper) o1;
			reportingtogpshelper p2 = (reportingtogpshelper) o2;

			return p1.getUserName().compareTo(p2.getUserName());
		}
	}*/
}