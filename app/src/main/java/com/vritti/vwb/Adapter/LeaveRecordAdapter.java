package com.vritti.vwb.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.Beans.LeaveRecordBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.ApplyLeaveMainActivity;
import com.vritti.vwb.vworkbench.LeaveRecords;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Admin-1 on 6/19/2017.
 */

public class LeaveRecordAdapter extends BaseAdapter {
    private static ArrayList<LeaveRecordBean> lsList;
    private LayoutInflater mInflater;
    Context context;
    String MLID;
    Utility ut;


    public LeaveRecordAdapter(Context context1, ArrayList<LeaveRecordBean> lsList1) {
        lsList = lsList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        ut = new Utility();
    }

    @Override
    public int getCount() {
        return lsList.size();
    }

    @Override
    public Object getItem(int position) {
        return lsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_record_list, null);
            holder = new ViewHolder();
            holder.tv_leavetype = (TextView) convertView.findViewById(R.id.lvtype);
            holder.tv_Start = (TextView) convertView.findViewById(R.id.Strartdate);
            holder.tv_End = (TextView) convertView.findViewById(R.id.End_date);
            holder.tv_balanced = (TextView) convertView.findViewById(R.id.lv_taken);
            holder.tv_mob = (TextView) convertView.findViewById(R.id.mob);
            holder.tv_status = (TextView) convertView.findViewById(R.id.Status);//reason
            holder.tv_reson = (TextView) convertView.findViewById(R.id.reason);//reason
            holder.tv_menu = (ImageView) convertView.findViewById(R.id.optnmenu);
            holder.tv_remark = (TextView) convertView.findViewById(R.id.remarl);
            holder.tv_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    int pos = (int) v.getTag();
                    if (lsList.get(pos).getStatus().equalsIgnoreCase("Approved")) {
                        final String mlid = lsList.get(pos).getMLId();
                        final String leave = lsList.get(pos).getLeaveType();
                        PopupMenu popup = new PopupMenu(context, v);
                        popup.inflate(R.menu.menu_popup);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
//                                switch (item.getItemId()) {
//                                    case R.id.edit:
//
//                                        break;
//                                    case R.id.cancel:
//                                        MLID =mlid;
//                                        CancelLeave(mlid);
//                                        break;
//                                }

                                if (item.getItemId() == R.id.edit) {
                                    MLID = mlid;
                                    EditLeave(mlid);
                                } else if (item.getItemId() == R.id.cancel) {
                                    MLID = mlid;
                                    CancelLeave(mlid);
                                }
                                return false;
                            }
                        });
                        popup.show();

                    } else if (lsList.get(pos).getStatus().equalsIgnoreCase("Pending")) {
                        PopupMenu popup = new PopupMenu(context, v);
                        popup.inflate(R.menu.menu_popup1);
                        final String mlid = lsList.get(pos).getMLId();

                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                              /*  switch (item.getItemId()) {
                                    case  R.id.cancel:
                                        MLID =mlid;
                                        CancelLeave(mlid);
                                        break;
                                }*/

                                if (item.getItemId() == R.id.cancel) {
                                    MLID = mlid;
                                    CancelLeave(mlid);
                                }
                                return false;
                            }
                        });
                        popup.show();
                    }

                }
            });
            convertView.setTag(R.id.optnmenu, holder.tv_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_menu.setTag(position); // This line is important.
        holder.tv_leavetype.setText(lsList.get(position).getLeaveType());
        holder.tv_Start.setText(lsList.get(position).getStartDate());
        holder.tv_End.setText(lsList.get(position).getEndDate());
        holder.tv_balanced.setText(lsList.get(position).getLeaveCount());
        holder.tv_mob.setText(lsList.get(position).getContact());
        holder.tv_status.setText(lsList.get(position).getStatus());
        holder.tv_reson.setText(lsList.get(position).getReason());
        if (lsList.get(position).getStatus().equalsIgnoreCase("Rejected")) {
            holder.tv_menu.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_menu.setVisibility(View.VISIBLE);
        }
        if (lsList.get(position).getStatus().equalsIgnoreCase("Pending")) {
            holder.tv_status.setTextColor(Color.RED);
        } else {
            holder.tv_status.setTextColor(Color.GREEN);
        }
        if (lsList.get(position).getApproverRemark().equalsIgnoreCase("")) {
            holder.tv_remark.setVisibility(View.GONE);
        } else {
            holder.tv_remark.setVisibility(View.VISIBLE);
            holder.tv_remark.setText("");
            holder.tv_remark.setText("Remark: " + lsList.get(position).getApproverRemark());
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#F1F6F7"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#DBE8EA"));
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_leavetype, tv_balanced, tv_Start, tv_End, tv_mob, tv_status, tv_reson, tv_remark;
        ImageView tv_menu;

    }

    class PostLeaveCancel extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            if (response.contains("Leave Cancellation request sent!")) {
                Toast.makeText(context, "Leave cancellation request sent!", Toast.LENGTH_LONG).show();

            }else {
                Toast.makeText(context, "Leave cancellation request  not sent!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = LeaveRecords.CompanyURL + WebUrlClass.api_Cancel_leave;
            try {
                res = ut.OpenPostConnection(url, params[0], context);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
                response= WebUrlClass.setError;
            }


            return response;
        }
    }

    class PostLeaveEdit extends AsyncTask<String, Void, String> {
        Object res;
        String response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            LeaveRecords.mprogress.setVisibility(View.GONE);
            if (response.contains("StartDt")) {
                Intent intent = new Intent(context, ApplyLeaveMainActivity.class);
                intent.putExtra("response", integer);
                intent.putExtra("mLId", MLID);
                context.startActivity(intent);

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = LeaveRecords.CompanyURL + WebUrlClass.api_Edit_leave;
            try {
                res = ut.OpenPostConnection(url, params[0], context);
                response = res.toString().replaceAll("\\\\", "");
                response = response.substring(1, response.length() - 1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }

    private void EditLeave(final String MLID) {
        if (ut.isNet(context)) {
            LeaveRecords.mprogress.setVisibility(View.VISIBLE);
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("MLId", MLID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String input = jobj.toString().replaceAll("\\\\", "");
                    new PostLeaveEdit().execute(input);

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(context, msg);
                    LeaveRecords.mprogress.setVisibility(View.GONE);

                }
            });
        } else {
            ut.displayToast(context, "No Internet Connetion");
        }

    }

    private void CancelLeave(final String MLID) {
        if (ut.isNet(context)) {
            new StartSession(context, new CallbackInterface() {
                @Override
                public void callMethod() {
                    JSONObject jobj = new JSONObject();
                    try {
                        jobj.put("MLId", MLID);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String input = jobj.toString().replaceAll("\\\\", "");
                    new PostLeaveCancel().execute(input);

                }

                @Override
                public void callfailMethod(String msg) {
                    ut.displayToast(context, msg);
                }
            });
        } else {
            ut.displayToast(context, "No Internet Connetion");
        }
    }

}

