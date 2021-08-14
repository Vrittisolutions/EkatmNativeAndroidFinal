package com.vritti.vwblib.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import com.vritti.vwblib.Beans.MyTeamDeptBean;
import com.vritti.vwblib.R;
import com.vritti.vwblib.vworkbench.MyTeamMemberTimesheetActivity;

public class MyTeamActivityAdapter extends BaseAdapter {
    List<MyTeamDeptBean> myteancount;
    Context context;
    private LayoutInflater mInflater;
    String count;

    public MyTeamActivityAdapter(Context context, List<MyTeamDeptBean> Myteamcount) {
        this.myteancount = Myteamcount;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return myteancount.size();
    }

    @Override
    public Object getItem(int position) {
        return myteancount.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_my_teamdata, null);
            holder = new ViewHolder();
            holder.txt_user_name = (TextView) convertView.findViewById(R.id.txt_member_name);
            holder.txt_assigned = (TextView) convertView.findViewById(R.id.txt_assigned);
            holder.txt_critical = (TextView) convertView.findViewById(R.id.txt_critical);
            holder.txt_overdue = (TextView) convertView.findViewById(R.id.txt_overdue);
            holder.txt_unapproved = (TextView) convertView.findViewById(R.id.txt_unapproved);
            holder.lay_assigned = (LinearLayout) convertView.findViewById(R.id.lay_assigned);
            holder.lay_critical = (LinearLayout) convertView.findViewById(R.id.lay_critical);
            holder.lay_overdue = (LinearLayout) convertView.findViewById(R.id.lay_overdue);
            holder.lay_unapproved = (LinearLayout) convertView.findViewById(R.id.lay_unapproved);
            holder.img_view = (ImageView) convertView.findViewById(R.id.ismobile);

            holder.lay_assigned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    count=holder.txt_assigned.getText().toString();
                    int pos = (int) v.getTag();
                    if (count.equals("0")) {
                    }else {

                        Intent intent = new Intent(context, MyTeamMemberTimesheetActivity.class);
                        intent.putExtra("Mode", "A");
                        intent.putExtra("UserMasterId", myteancount.get(pos).getUserMasterId());
                        intent.putExtra("UserName", myteancount.get(pos).getUserName());

                        context.startActivity(intent);
                    }
                   // ((Activity)context).finish();


                }
            });

            holder.lay_critical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count=holder.txt_critical.getText().toString();

                    int pos = (int) v.getTag();
                    if (count.equals("0")) {
                    }else {

                        Intent intent = new Intent(context, MyTeamMemberTimesheetActivity.class);
                        intent.putExtra("Mode", "C");
                        intent.putExtra("UserMasterId", myteancount.get(pos).getUserMasterId());
                        intent.putExtra("UserName", myteancount.get(pos).getUserName());
                        context.startActivity(intent);
                        //  ((Activity)context).finish();
                    }

                }
            });
            holder.lay_unapproved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count=holder.txt_unapproved.getText().toString();

                    int pos = (int) v.getTag();
                    if (count.equals("0")) {
                    }else {

                        Intent intent = new Intent(context, MyTeamMemberTimesheetActivity.class);
                        intent.putExtra("Mode", "Appr");
                        intent.putExtra("UserMasterId", myteancount.get(pos).getUserMasterId());
                        intent.putExtra("UserName", myteancount.get(pos).getUserName());
                        context.startActivity(intent);
                    }
                   // ((Activity)context).finish();

                }
            });

            holder.lay_overdue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count=holder.txt_overdue.getText().toString();

                    int pos = (int) v.getTag();
                    if (count.equals("0")) {
                    }else {

                        Intent intent = new Intent(context, MyTeamMemberTimesheetActivity.class);
                        intent.putExtra("Mode", "O");
                        intent.putExtra("UserMasterId", myteancount.get(pos).getUserMasterId());
                        intent.putExtra("UserName", myteancount.get(pos).getUserName());
                        context.startActivity(intent);
                    }
                  //  ((Activity)context).finish();

                }
            });
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.lay_assigned.setTag(position);
        holder.lay_critical.setTag(position);
        holder.lay_overdue.setTag(position);
        holder.lay_unapproved.setTag(position);
        String s = myteancount.get(position).getMobileUser();
        if (s.equalsIgnoreCase("N")) {
            Drawable image = convertView.getResources().getDrawable(R.drawable.mobilefalse);
            holder.img_view.setBackground(image);
        } else {
            Drawable image = convertView.getResources().getDrawable(R.drawable.mobiletrue);
            holder.img_view.setBackground(image);
        }
        holder.txt_user_name.setText(myteancount.get(position).getUserName());
        holder.txt_assigned.setText(myteancount.get(position).getTotalAssigned());
        holder.txt_overdue.setText(myteancount.get(position).getTotalOverdueActivities());
        holder.txt_unapproved.setText(myteancount.get(position).getAwaitingActivities());
        holder.txt_critical.setText(myteancount.get(position).getCritical());
      //  convertView.setTag(holder);
        return convertView;
    }

    static class ViewHolder {
        TextView txt_user_name, txt_assigned, txt_overdue, txt_critical, txt_unapproved;
        ImageView img_view;
        LinearLayout lay_assigned, lay_overdue, lay_critical, lay_unapproved;

    }
}

