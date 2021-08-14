package com.vritti.vwblib.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.vwblib.Beans.WorkspacewiseActCntBean;

import java.util.List;

import com.vritti.vwblib.R;
import com.vritti.vwblib.vworkbench.WorkspacewiseActDetailActivity;

/**
 * Created by 300151 on 2/8/2017.
 */
public class WorkspacewiseActCntAdapter extends BaseAdapter {
    List<WorkspacewiseActCntBean> ls_WorkspacewiseActCnt;
    Context context;
    private LayoutInflater mInflater;

    public WorkspacewiseActCntAdapter(Context context, List<WorkspacewiseActCntBean> ls_WorkspacewiseActCnt) {
        this.ls_WorkspacewiseActCnt = ls_WorkspacewiseActCnt;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return ls_WorkspacewiseActCnt.size();
    }

    @Override
    public Object getItem(int position) {
        return ls_WorkspacewiseActCnt.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_workspace_act_cnt, null);
            holder = new ViewHolder();
            holder.txt_workspace_name = (TextView) convertView.findViewById(R.id.txt_workspace_name);
            holder.txt_assigned = (TextView) convertView.findViewById(R.id.txt_assigned);
            holder.txt_complete = (TextView) convertView.findViewById(R.id.txt_complete);
            holder.txt_critical = (TextView) convertView.findViewById(R.id.txt_critical);
            holder.txt_overdue = (TextView) convertView.findViewById(R.id.txt_overdue);
            holder.txt_unapproved = (TextView) convertView.findViewById(R.id.txt_unapproved);
           // holder.txt_total = (TextView) convertView.findViewById(R.id.txt_total);
            holder.lay_assigned = (LinearLayout) convertView.findViewById(R.id.lay_assigned);
            holder.lay_complete = (LinearLayout) convertView.findViewById(R.id.lay_complete);
            holder.lay_critical = (LinearLayout) convertView.findViewById(R.id.lay_critical);
            holder.lay_overdue = (LinearLayout) convertView.findViewById(R.id.lay_overdue);
            holder.lay_unapproved = (LinearLayout) convertView.findViewById(R.id.lay_unapproved);

            holder.lay_assigned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","A");//A
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(pos).getProjectId());
                    intent.putExtra("ProjectName",ls_WorkspacewiseActCnt.get(pos).getProjectName());

                    context.startActivity(intent);
                }
            });
            holder.lay_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","Comp");
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(pos).getProjectId());
                    intent.putExtra("ProjectName",ls_WorkspacewiseActCnt.get(pos).getProjectName());
                    context.startActivity(intent);
                }
            });
            holder.lay_critical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","C");
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(pos).getProjectId());
                    intent.putExtra("ProjectName",ls_WorkspacewiseActCnt.get(pos).getProjectName());

                    context.startActivity(intent);
                }
            });
            holder.lay_unapproved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","Appr");
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(pos).getProjectId());
                    intent.putExtra("ProjectName",ls_WorkspacewiseActCnt.get(pos).getProjectName());
                    context.startActivity(intent);
                }
            });

           /* holder.txt_total.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","");
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(position).getProjectId());
                    context.startActivity(intent);
                }
            });*/
            holder.lay_overdue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();

                    Intent intent=new Intent(context, WorkspacewiseActDetailActivity.class);
                    intent.putExtra("Mode","O");
                    intent.putExtra("ProjectId",ls_WorkspacewiseActCnt.get(pos).getProjectId());
                    intent.putExtra("ProjectName",ls_WorkspacewiseActCnt.get(pos).getProjectName());
                    context.startActivity(intent);
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
        holder.lay_complete.setTag(position);

        holder.txt_workspace_name.setText(ls_WorkspacewiseActCnt.get(position).getProjectName());
        holder.txt_assigned.setText(ls_WorkspacewiseActCnt.get(position).getTotalAssigned());
        holder.txt_complete.setText(ls_WorkspacewiseActCnt.get(position).getComplete());
        holder.txt_overdue.setText(ls_WorkspacewiseActCnt.get(position).getTotalOverdue());
        holder.txt_unapproved.setText(ls_WorkspacewiseActCnt.get(position).getAwaitingActivities());
        holder.txt_critical.setText(ls_WorkspacewiseActCnt.get(position).getCriticalActivity());
       // holder.txt_total.setText(ls_WorkspacewiseActCnt.get(position).getOPENAct());
        return convertView;
    }

    static class ViewHolder {
        TextView txt_workspace_name, txt_total, txt_assigned, txt_overdue, txt_critical, txt_complete, txt_unapproved;
        LinearLayout lay_assigned, lay_overdue, lay_critical, lay_complete, lay_unapproved;

    }
}

