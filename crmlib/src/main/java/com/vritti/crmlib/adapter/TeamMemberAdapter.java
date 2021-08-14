package com.vritti.crmlib.adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.TeamMemberbean;
import com.vritti.crmlib.classes.CommonFunctionCrm;
import com.vritti.crmlib.classes.CommonObjectProperties;
import com.vritti.crmlib.vcrm7.SubTeamMemberActivity;
import com.vritti.crmlib.vcrm7.TeamMemberOpportunityActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;


import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sharvari on 07-Mar-17.
 */

public class TeamMemberAdapter extends BaseAdapter {
    private static ArrayList<TeamMemberbean> TeamMemberBeanArrayList;
    private LayoutInflater mInflater;
    CommonObjectProperties commonObj;
    SimpleDateFormat dfDate;
    String FinalObj;
    String obj;
    SharedPreferences userpreferences;;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;

    public TeamMemberAdapter(Context context, ArrayList<TeamMemberbean> TeamMemberBeanArrayList) {
        this.TeamMemberBeanArrayList = TeamMemberBeanArrayList;
        mInflater = LayoutInflater.from(context);
        this.context = context;

        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);

        sql = db.getWritableDatabase();

    }

    @Override
    public int getCount() {
        return TeamMemberBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return TeamMemberBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_custom_teammember, null);
            holder = new ViewHolder();
            holder.textassigned = (TextView) convertView.findViewById(R.id.textassigned);
            holder.textname = (TextView) convertView.findViewById(R.id.textname);

            holder.textoverdue = (TextView) convertView.findViewById(R.id.textoverdue);
            holder.texttoday = (TextView) convertView.findViewById(R.id.texttoday);
            holder.textcollection = (TextView) convertView.findViewById(R.id.textcollection);
            holder.texttomorrow = (TextView) convertView.findViewById(R.id.texttomorrow);

            holder.texthot = (TextView) convertView.findViewById(R.id.texthot);
            holder.img_team= (ImageView) convertView.findViewById(R.id.img_team);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int i = position;
        holder.textassigned.setText(TeamMemberBeanArrayList.get(position).getAssigned());
        holder.textname.setText(TeamMemberBeanArrayList.get(position).getUserName()
        );
        holder.textoverdue.setText(TeamMemberBeanArrayList.get(position).getOverdue()
        );
        holder.texttoday.setText(TeamMemberBeanArrayList.get(position).getToday()
        );
        holder.textcollection.setText(TeamMemberBeanArrayList.get(position).getCollection()
        );
        holder.texthot.setText(TeamMemberBeanArrayList.get(position).getHot()
        );
        holder.texttomorrow.setText(TeamMemberBeanArrayList.get(position).getTomorrow()
        );
        String team=TeamMemberBeanArrayList.get(position).getReport();
        if (team.equals("Yes")){
            holder.img_team.setVisibility(View.VISIBLE);
        }else {
            holder.img_team.setVisibility(View.GONE);
        }


        holder.img_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView= (ImageView) view;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserMasterId=TeamMemberBeanArrayList.get(position).getUserMasterId();
                        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                                Context.MODE_PRIVATE);
                        CompanyURL = userpreferences.getString("CompanyURL", null);

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTeamJSON().execute(UserMasterId);
                                }

                                @Override
                                public void callfailMethod(String msg) {

                                }
                            });

                        }
                    }
                });

            }
        });


        holder.textassigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("A", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "A");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
            }
        });
        holder.textoverdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("O", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "O");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
            }
        });
        holder.texttoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("T", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "T");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
            }
        });
        holder.texttomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("TO", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "TO");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
            }
        });
        holder.textcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("C", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "C");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);

            }
        });
        holder.texthot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj("H", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "H");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
            }
        });


        return convertView;
    }
    private boolean isnet() {
        // TODO Auto-generated method stub

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }


    static class ViewHolder {
        TextView textassigned, textname, textoverdue, texttoday, textcollection, texthot,texttomorrow;
        ImageView img_team;
    }

    private String getObj(String a, String UserMasterId) {
        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        commonObj = new CommonObjectProperties();
        JSONObject jsoncommonObj = commonObj.DataObj();
        JSONObject jsonObj;


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", UserMasterId);

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            jsonObj = jsoncommonObj.getJSONObject("Isclose");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", "N");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObj = jsoncommonObj.getJSONObject("IsPartial");
            jsonObj.put("IsSet", false);
            jsonObj.put("value1", "P");
            jsonObj.put("Operator", "eq");


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (a.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("O")) {

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (a.equalsIgnoreCase("T")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("C")) {
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();
           //     calendar.getFirstDayOfWeek();
                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 2);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (a.equalsIgnoreCase("H")) {

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Hot");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 1);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

        }
        FinalObj = jsoncommonObj.toString();
        FinalObj = FinalObj.replaceAll("\\\\", "");
        return FinalObj;
    }

    class DownloadTeamJSON extends AsyncTask<String, Void, String> {
        Object res;
        String response;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait data loading...");
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = CompanyURL + WebUrlClass.api_get_TeamMembers +
                        "?UserMasterId=" + URLEncoder.encode(UserMasterId, "UTF-8");

                res = ut.OpenConnection(url);
                response = res.toString().replaceAll("\\\\", "");
                response = response.replaceAll("\\\\\\\\/", "");
                response = response.substring(1, response.length() - 1);
                ContentValues values = new ContentValues();
                JSONArray jResults = new JSONArray(response);
                sql.delete(db.TABLE_SubTeam_Member, null,
                        null);
                Cursor c = sql.rawQuery("SELECT * FROM " + db.TABLE_SubTeam_Member, null);
                int count = c.getCount();
                String columnName, columnValue;
                for (int i = 0; i < jResults.length(); i++) {
                    JSONObject jorder = jResults.getJSONObject(i);
                    for (int j = 0; j < c.getColumnCount(); j++) {

                        columnName = c.getColumnName(j);
                        columnValue = jorder.getString(columnName);
                        values.put(columnName, columnValue);

                    }

                    long a = sql.insert(db.TABLE_SubTeam_Member, null, values);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            if (response != null) {

                context.startActivity(new Intent(context, SubTeamMemberActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|FLAG_ACTIVITY_NEW_TASK));

            }


        }

    }

}
