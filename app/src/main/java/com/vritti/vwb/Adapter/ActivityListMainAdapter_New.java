package com.vritti.vwb.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.vworkbench.ActivityMain;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class ActivityListMainAdapter_New extends RecyclerView.Adapter<ActivityListMainAdapter_New.ActivityHolder> {
    Context context;
    ArrayList<ActivityBean> lsActivityList;
    ArrayList<ActivityBean> tempFilterList = new ArrayList<>();
    String actid, time, starttime, sp_date;
    int sp_count;
    String CompanyURL, MobileNo;
    SharedPreferences userpreferences;
    String EnvMasterId, LoginId, Password, PlantMasterId;
    SharedPreferences AtendanceSheredPreferance;
    SimpleDateFormat dfDate;
    String getdate, currentTime;
    String fromDoc="";

    public ActivityListMainAdapter_New(Context context, ArrayList<ActivityBean> lsActivityList) {
        this.context = context;
        this.lsActivityList = lsActivityList;
        tempFilterList.addAll(this.lsActivityList);
    }

    public ActivityListMainAdapter_New() {

    }

    public ActivityListMainAdapter_New(Context context, ArrayList<ActivityBean> lsActivityList,String fromDoc) {
        this.context = context;
        this.lsActivityList = lsActivityList;
        tempFilterList.addAll(this.lsActivityList);
        this.fromDoc= fromDoc;
    }

    @NonNull
    @Override
    public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vwb_custom_activity_list_v1, parent, false);
        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                Context.MODE_PRIVATE);
        AtendanceSheredPreferance = context.getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);


        return new ActivityHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ActivityHolder holder, final int position) {

        /*if((lsActivityList.size()-1) == position) {
            String reQuery = "N";
            ((ActivityMain) context).loadNextActivity(reQuery);
        }*/


        Date EndDate = null, Todaydate = null, StartDate = null;
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfdisplay = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyyy");
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
           // holder.tv_activityStatus.setBackgroundColor(Color.parseColor("#F05050"));
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.act_1)));

        } else if (lsActivityList.get(position).getPriorityIndex().equalsIgnoreCase("2")) {
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.act_2)));

         //   holder.tv_activityStatus.setBackgroundColor(Color.parseColor("#FF902B"));

        } else if (lsActivityList.get(position).getPriorityIndex().equalsIgnoreCase("3")) {
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.act_3)));

          //  holder.tv_activityStatus.setBackgroundColor(Color.parseColor("#27C24C"));
        } else {
            //holder.tv_activityStatus.setBackgroundColor(Color.parseColor("#27C24C"));
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.act_4)));

        }

        if (lsActivityList.get(position).getStatus().equalsIgnoreCase("PAUSED")) {
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.pause_white));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

        } else if (lsActivityList.get(position).getStatus().equalsIgnoreCase("WIP")) {
            holder.tv_activityStatus.setImageDrawable(context.getResources().getDrawable(R.drawable.wip_white));
            ImageViewCompat.setImageTintList(holder.tv_activityStatus, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

        }

       /* if (position % 2 == 1) {
            holder.cView.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            holder.cView.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }
*/
        if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Email")) {
            holder.lay_ticketDesc.setVisibility(View.GONE);
            holder.tv_SorceType.setImageDrawable(context.getResources().getDrawable(R.drawable.email_24));
            ImageViewCompat.setImageTintList(holder.tv_SorceType, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString().replaceFirst("^\\s*", ""));
        } else if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Support")) {
            holder.lay_ticketDesc.setVisibility(View.GONE);
          //  holder.tv_activityCode.setText(lsActivityList.get(position).getActivityCode());
            holder.tv_clientName.setVisibility(View.VISIBLE);
            holder.tv_clientName.setText(lsActivityList.get(position).getConsigneeName().replaceFirst("^\\s*", ""));
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityCode()+"-"+lsActivityList.get(position).getActivityName().toString().replaceFirst("^\\s*", ""));
            holder.tv_SorceType.setImageDrawable(context.getResources().getDrawable(R.drawable.ticket));
            ImageViewCompat.setImageTintList(holder.tv_SorceType, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        } else if (lsActivityList.get(position).getSourceType().equalsIgnoreCase("Datasheet")) {
            holder.lay_ticketDesc.setVisibility(View.GONE);
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString().replaceFirst("^\\s*", ""));;
            holder.tv_SorceType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_sheet));
            ImageViewCompat.setImageTintList(holder.tv_SorceType, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));

        } else {
            holder.tv_activity_desc.setText(lsActivityList.get(position).getActivityName().toString().replaceFirst("^\\s*", ""));;
            holder.tv_SorceType.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_activity));
            ImageViewCompat.setImageTintList(holder.tv_SorceType, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
            holder.lay_ticketDesc.setVisibility(View.GONE);
        }
        actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);

        if (lsActivityList.get(position).getConsigneeName().equals("")) {
            holder.tv_clientName.setVisibility(View.GONE);
        }else if (lsActivityList.get(position).getConsigneeName().equals("null")||lsActivityList.get(position).getConsigneeName().equals("null")) {
            holder.tv_clientName.setVisibility(View.GONE);
        }
        else {
            holder.tv_clientName.setVisibility(View.VISIBLE);
            holder.tv_clientName.setText(lsActivityList.get(position).getConsigneeName());
             }

        if (actid != null) {
            if (actid.equalsIgnoreCase(lsActivityList.get(position).getActivityId())) {
                holder.card_viewfill.setBackgroundColor(Color.parseColor("#87b2d3"));
            } else {
                if (position % 2 == 1) {
                    holder.card_viewfill.setBackgroundColor(Color.parseColor("#ffffff"));
                } else {
                    holder.card_viewfill.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        }

        holder.tv_clientName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(lsActivityList.get(position).getContMob().equals("") || lsActivityList.get(position).getContMob().equals("null"))) {
                    holder.tv_contact.setVisibility(View.VISIBLE);
                    holder.tv_contact.setText(lsActivityList.get(position).getContMob());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lsActivityList.size();

    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        lsActivityList.clear();
        if (charText.length() == 0) {
            lsActivityList.addAll(tempFilterList);
        }
        else
        {
            for (ActivityBean wp : lsActivityList)
            {
                if (wp.getActivityName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    lsActivityList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ActivityHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_activity_desc)
        TextView tv_activity_desc;
        @BindView(R.id.tv_assignedBy)
        TextView tv_assignedBy;
        @BindView(R.id.tv_endDate1)
        TextView tv_endDate1;
        @BindView(R.id.tv_ConsigneeName)
        TextView tv_ConsigneeName;
        @BindView(R.id.tv_activityCode)
        TextView tv_activityCode;
        @BindView(R.id.tv_workspace)
        TextView tv_workspace;
        @BindView(R.id.tv_hoursreq)
        TextView tv_hoursreq;
        @BindView(R.id.tv_hoursbook)
        TextView tv_hoursbook;
        @BindView(R.id.tv_endDate)
        TextView tv_endDate;
        @BindView(R.id.lay_ticketDesc)
        LinearLayout lay_ticketDesc;
       /* @BindView(R.id.lay_PriorityIndex)
        LinearLayout lay_PriorityIndex;*/
        @BindView(R.id.card_viewfill)
        CardView card_viewfill;
        @BindView(R.id.tv_SorceType)
        ImageView tv_SorceType;
        @BindView(R.id.tv_activityStatus)
        ImageView tv_activityStatus;
        @BindView(R.id.tv_clientName)
        TextView tv_clientName;
        @BindView(R.id.tv_contact)
        TextView tv_contact;


        public ActivityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }
        @OnClick(R.id.card_viewfill)
        void header(){
            ((ActivityMain)context).rowClick(getAdapterPosition());
        }
        @OnLongClick(R.id.card_viewfill)
        boolean longPress(){
            ((ActivityMain)context).longPress(getAdapterPosition());;
            return  true;
        }



    }
}
