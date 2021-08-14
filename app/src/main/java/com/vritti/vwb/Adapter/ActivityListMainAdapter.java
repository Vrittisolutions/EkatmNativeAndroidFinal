package com.vritti.vwb.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.chat.bean.ChatGroup;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ActivityMain;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Created by 300151 on 10/7/2016.
 */

public class ActivityListMainAdapter extends BaseAdapter {
    private static ArrayList<ActivityBean> lsActivityList;
    private LayoutInflater mInflater;
    Context context;
    String actid, time, starttime, sp_date;
    int sp_count;
    String CompanyURL, MobileNo;
    SharedPreferences userpreferences;
    String EnvMasterId, LoginId, Password, PlantMasterId;
    SharedPreferences AtendanceSheredPreferance;
    SimpleDateFormat dfDate;
    String getdate, currentTime;
    ArrayList<ActivityBean> arraylist;

    public ActivityListMainAdapter(Context context1, ArrayList<ActivityBean> lsActivityList1) {
        lsActivityList = lsActivityList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<ActivityBean>();
        this.arraylist.addAll(lsActivityList);
        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        AtendanceSheredPreferance = context.getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);
        dfDate = new SimpleDateFormat("dd MMM yyyy");// 25 Oct 2016
        getdate = dfDate.format(new Date());// 17 Apr 2014
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Calendar cl = Calendar.getInstance();
        currentTime = dateFormat.format(cl.getTime());
        // startServiceGPS();
        actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
        }

    @Override
    public int getCount() {
        return lsActivityList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsActivityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        Date EndDate = null, Todaydate = null, StartDate = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfdisplay = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_activity_list, null);
            holder = new ViewHolder();
            // holder.tv_action = (TextView) convertView.findViewById(R.id.tv_action);
            holder.tv_activity_desc = (TextView) convertView
                    .findViewById(R.id.tv_activity_desc);
            holder.tv_ConsigneeName = (TextView) convertView.findViewById(R.id.tv_ConsigneeName);
            holder.tv_activityCode = (TextView) convertView.findViewById(R.id.tv_activityCode);
            holder.tv_workspace = (TextView) convertView.findViewById(R.id.tv_workspace);
            holder.tv_hoursreq = (TextView) convertView.findViewById(R.id.tv_hoursreq);
            holder.tv_hoursbook = (TextView) convertView.findViewById(R.id.tv_hoursbook);
            // holder.tv_PriorityIndex = (TextView) convertView.findViewById(R.id.tv_PriorityIndex);
            holder.lay_PriorityIndex = (LinearLayout) convertView.findViewById(R.id.lay_PriorityIndex);
            holder.tv_endDate = (TextView) convertView.findViewById(R.id.tv_endDate);
            //  holder.tv_activity_status = (TextView) convertView.findViewById(R.id.tv_activity_status);
            holder.tv_SorceType = (ImageView) convertView.findViewById(R.id.tv_SorceType);
            holder.tv_activityStatus = (ImageView) convertView.findViewById(R.id.tv_activityStatus);
            holder.lay_ticketDesc = (LinearLayout) convertView.findViewById(R.id.lay_ticketDesc);
            holder.tv_endDate1 = (TextView) convertView.findViewById(R.id.tv_endDate1);
            holder.tv_assignedBy = (TextView) convertView.findViewById(R.id.tv_assignedBy);
            holder.lencall = (LinearLayout) convertView.findViewById(R.id.lencall);
            convertView.setTag(holder.lay_PriorityIndex);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String jsonEDate = lsActivityList.get(position).getEndDate();
        String jsonSDate = lsActivityList.get(position).getStartDate();
        String endDate = "", todayDate, startDate = "",endDatedisplay = "",startDatedisplay = "";
        try {
            String EndDresults = jsonEDate.substring(jsonEDate.indexOf("(") + 1, jsonEDate.lastIndexOf(")"));
            long Etime = Long.parseLong(EndDresults);
            String StarDresult = jsonSDate.substring(jsonSDate.indexOf("(") + 1, jsonSDate.lastIndexOf(")"));
            long Stime = Long.parseLong(StarDresult);
            EndDate = new Date(Etime);
            Todaydate = new Date();


            todayDate = sdf.format(Todaydate);
            endDate = sdf.format(EndDate);
            endDatedisplay = sdfdisplay.format(EndDate);
            EndDate = sdf.parse(endDate);
            Todaydate = sdf.parse(todayDate);
            StartDate = new Date(Stime);
            startDate = sdf.format(StartDate);
            startDatedisplay = sdfdisplay.format(EndDate);

            StartDate = sdf.parse(startDate);
            System.out.println("Result Date: " + endDate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (EndDate.before(Todaydate)) {
            holder.tv_endDate.setText("Delayed");
            holder.tv_endDate.setTextColor(Color.parseColor("#F05050"));
        } else if (StartDate.equals(Todaydate) && EndDate.equals(Todaydate)) {
            holder.tv_endDate.setText("Ends");
            holder.tv_endDate.setTextColor(Color.parseColor("#27C24C"));
        } else if (StartDate.before(Todaydate) && EndDate.equals(Todaydate)) {
            holder.tv_endDate.setText("Ends on Today");
            holder.tv_endDate.setTextColor(Color.parseColor("#27C24C"));
        } else if (StartDate.before(Todaydate) && EndDate.after(Todaydate)) {
            holder.tv_endDate.setText("Ends on " + endDatedisplay);
            holder.tv_endDate.setTextColor(Color.parseColor("#27C24C"));
        }else if (StartDate.equals(Todaydate) && EndDate.after(Todaydate)) {
            holder.tv_endDate.setText("Ends on " + endDatedisplay);
            holder.tv_endDate.setTextColor(Color.parseColor("#27C24C"));
        }else if (StartDate.after(Todaydate) && EndDate.after(Todaydate)) {
            holder.tv_endDate.setText("Starts on " +startDatedisplay);
            holder.tv_endDate.setTextColor(Color.parseColor("#FF902B"));
        }

        String endD = "";
        Date date;
        if (ActivityMain.Activity_AssignByMe) {
            if (lsActivityList.get(position).getIssuedToName().contains(" ")) {
                holder.tv_assignedBy.setText("to " + lsActivityList.get(position).getIssuedToName()
                        .substring(0, lsActivityList.get(position).getIssuedToName().lastIndexOf(" ")));
            } else {
                holder.tv_assignedBy.setText("to " + lsActivityList.get(position).getIssuedToName());
            }

        } else if(ActivityMain.Activity_Unapprove == true){
            holder.tv_assignedBy.setText("by " + lsActivityList.get(position)
                    .getIssuedToName().substring(0, lsActivityList.get(position).getIssuedToName().lastIndexOf(" ")));

        } else {
            if (lsActivityList.get(position).getAssigned_By().contains(" ")) {
                holder.tv_assignedBy.setText("by " + lsActivityList.get(position).getAssigned_By().substring(0, lsActivityList.get(position).getAssigned_By().lastIndexOf(" ")));
            } else {
                holder.tv_assignedBy.setText("by " + lsActivityList.get(position).getAssigned_By());
            }
           /* holder.tv_assignedBy.setText("by " +lsActivityList.get(position).getAssigned_By());
            //lsActivityList.get(position).getAssigned_By().substring(0, lsActivityList.get(position).getAssigned_By().lastIndexOf(" "))*/
        }

        holder.tv_workspace.setText("Under " + lsActivityList.get(position).getProjectName().toString());
        endD = sdf1.format(EndDate);
        holder.tv_endDate1.setText(endD);

        if (!lsActivityList.get(position).getTotalHoursBooked().toString().equalsIgnoreCase("0")) {
            holder.tv_hoursbook.setText(lsActivityList.get(position).getTotalHoursBooked().toString() + " of ");
            holder.tv_hoursreq.setText(lsActivityList.get(position).getHoursRequired().toString());
        } else {
            holder.tv_hoursbook.setText("");
            holder.tv_hoursreq.setText(lsActivityList.get(position).getHoursRequired().toString() + " hrs");
        }

        if (lsActivityList.get(position).getPriorityIndex().equalsIgnoreCase("1")) {
            holder.lay_PriorityIndex.setBackgroundColor(Color.parseColor("#F05050"));
        } else if (lsActivityList.get(position).getPriorityIndex().equalsIgnoreCase("2")) {
            holder.lay_PriorityIndex.setBackgroundColor(Color.parseColor("#FF902B"));
        } else if (lsActivityList.get(position).getPriorityIndex().equalsIgnoreCase("3")) {
            holder.lay_PriorityIndex.setBackgroundColor(Color.parseColor("#27C24C"));
        } else {
            holder.lay_PriorityIndex.setBackgroundColor(Color.parseColor("#27C24C"));
        }

        if (lsActivityList.get(position).getStatus().equalsIgnoreCase("PAUSED")) {
            holder.tv_activityStatus.setBackgroundResource(R.drawable.pause_white);
        } else if (lsActivityList.get(position).getStatus().equalsIgnoreCase("WIP")) {
            holder.tv_activityStatus.setBackgroundResource(R.drawable.wip_white);
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }

        if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Email")) {
            holder.lay_ticketDesc.setVisibility(View.GONE);
            holder.tv_SorceType.setBackgroundResource(R.drawable.email_icon_white);
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString());
        } else if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Support")) {
            holder.lay_ticketDesc.setVisibility(View.VISIBLE);
            holder.tv_activityCode.setText(lsActivityList.get(position).getActivityCode());
            holder.tv_ConsigneeName.setText(" - " + lsActivityList.get(position).getConsigneeName() + "-" + lsActivityList.get(position).getContMob());
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString());
            holder.tv_SorceType.setBackgroundResource(R.drawable.ticket_white);
        } else if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Datasheet")) {
            holder.lay_ticketDesc.setVisibility(View.GONE);
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString());
            holder.tv_SorceType.setBackgroundResource(R.drawable.datasheet_icon2_white);
        } else {
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString());
            holder.lay_ticketDesc.setVisibility(View.GONE);
        }


        if (actid != null) {

            if (actid.equalsIgnoreCase(lsActivityList.get(position).getActivityId())) {
               /* if (lsActivityList.get(i).getsupportdesc()
                        .equalsIgnoreCase("")) {
                    txtsupportdesc.setVisibility(View.GONE);
                } else {
                    txtsupportdesc.setText(lsActivityList.get(i)
                            .getsupportdesc());
                }*/
                holder.lencall.setBackgroundColor(Color.CYAN);

                //  txtName.setText(lsActivityList.get(position).getActivityName());
               /* txtCityState.setText(lsActivityList.get(position).getCityState());
                txtPhone.setText("Priority : "
                        + lsActivityList.get(i).getPhone());*/

            } else {
                holder.lencall.setBackground(context.getResources().getDrawable(R.drawable.button_background));
              /*  if (lsActivityList.get(i).getsupportdesc()
                        .equalsIgnoreCase("")) {
                    txtsupportdesc.setVisibility(View.GONE);
                } else {
                    txtsupportdesc.setText(lsActivityList.get(i)
                            .getsupportdesc());
                }
                Log.e("getview", "else support pos : " + i + " val: "
                        + txtsupportdesc.getText());
                txtName.setText(lsActivityList.get(i).getActivitydesc());
                txtCityState.setText(lsActivityList.get(i).getCityState());
                txtPhone.setText("Priority : "
                        + lsActivityList.get(i).getPhone());*/

            }

        } else {
            holder.lencall.setBackground(context.getResources().getDrawable(R.drawable.button_background));
/*
            if (lsActivityList.get(i).getsupportdesc().equalsIgnoreCase("")) {
                txtsupportdesc.setVisibility(View.GONE);
            } else {
                txtsupportdesc.setText(lsActivityList.get(i)
                        .getsupportdesc());
            }
            Log.e("getview", "else support pos : " + i + " val: "
                    + txtsupportdesc.getText());
            txtName.setText(lsActivityList.get(i).getActivitydesc());
            txtCityState.setText(lsActivityList.get(i).getCityState());
            txtPhone.setText("Priority : "
                    + lsActivityList.get(i).getPhone());
*/

        }


        holder.tv_endDate.setTag(lsActivityList.get(position));
        holder.lay_PriorityIndex.setTag(lsActivityList.get(position));
        convertView.setTag(holder);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_activity_desc, tv_assignedBy, tv_endDate1, tv_ConsigneeName,
                  tv_activityCode, tv_workspace, tv_hoursreq, tv_hoursbook, tv_PriorityIndex, tv_endDate, tv_activity_status, tv_action;
        LinearLayout lay_ticketDesc, lay_PriorityIndex,lencall;
        ImageView tv_SorceType, tv_activityStatus;
    }

    public ArrayList<ActivityBean> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lsActivityList.clear();
        if (charText.length() == 0) {
            lsActivityList.addAll(arraylist);
        }
        else
        {
            for (ActivityBean wp : arraylist)
            {
                if (wp.getActivityName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    lsActivityList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return lsActivityList;
    }
}
