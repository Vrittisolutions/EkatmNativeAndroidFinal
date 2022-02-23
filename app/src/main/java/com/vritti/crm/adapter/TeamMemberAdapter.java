package com.vritti.crm.adapter;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.crm.bean.TeamMemberbean;
import com.vritti.crm.classes.CollectionCallCommonObjectProperties;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.classes.CommonObjectProperties;
import com.vritti.crm.classes.FeedbackCommonObjectProperties;
import com.vritti.crm.vcrm7.OpportunityActivity;
import com.vritti.crm.vcrm7.OpportunityActivity_V1;
import com.vritti.crm.vcrm7.ReportSelectionActivity;
import com.vritti.crm.vcrm7.SubTeamMemberActivity;
import com.vritti.crm.vcrm7.TeamMemberActivity;
import com.vritti.crm.vcrm7.TeamMemberOpportunityActivity;
import com.vritti.ekatm.R;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.sessionlib.CallbackInterface;
import com.vritti.sessionlib.StartSession;
import com.vritti.vwb.classes.commonObjectProperties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sharvari on 07-Mar-17.
 */

public class TeamMemberAdapter extends BaseAdapter {
    private static ArrayList<TeamMemberbean> TeamMemberBeanArrayList;
    private LayoutInflater mInflater;
    CommonObjectProperties commonObj;
    commonObjectProperties commonObjectProperties;
    SimpleDateFormat dfDate,dftime;
    String FinalObj;
    String obj;
    SharedPreferences userpreferences;;
    public static Boolean Activity_AssignByMe = false;
    public static Boolean Activity_Unapprove = false;

    String PlantMasterId ="", LoginId="", Password="", CompanyURL="", EnvMasterId="",
            UserMasterId="",UserName = "", MobileNo = "";
    Utility ut;
    DatabaseHandlers db;
    CommonFunctionCrm cf;
    Context context;
    SQLiteDatabase sql;
    JSONObject jsoncommonObj;
    JSONObject jsonObj;
    FeedbackCommonObjectProperties feedbackcommonObj;
    CollectionCallCommonObjectProperties collectioncallcommonObj;
    String Usermasterid="";
    private double hot=0,warm=0,collection=0;

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
       UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);

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
            convertView = mInflater.inflate(R.layout.crm_custom_teammember_v1, null);
            holder = new ViewHolder();
            holder.textassigned = (TextView) convertView.findViewById(R.id.textassigned);
            holder.textname = (TextView) convertView.findViewById(R.id.textname);

            holder.textoverdue = (TextView) convertView.findViewById(R.id.textoverdue);
            holder.texttoday = (TextView) convertView.findViewById(R.id.texttoday);
            holder.textcollection = (TextView) convertView.findViewById(R.id.textcollection);
            holder.texttomorrow = (TextView) convertView.findViewById(R.id.texttomorrow);
            holder.text_review = (TextView) convertView.findViewById(R.id.text_review);
            holder.txt_warm = (TextView) convertView.findViewById(R.id.txt_warm);

            holder.texthot = (TextView) convertView.findViewById(R.id.texthot);
            holder.img_team= (ImageView) convertView.findViewById(R.id.img_team);

            holder.len_assign=convertView.findViewById(R.id.len_assign);
            holder.len_overdue=convertView.findViewById(R.id.len_overdue);
            holder.len_today=convertView.findViewById(R.id.len_today);
            holder.len_tomo=convertView.findViewById(R.id.len_tomo);
            holder.len_overdue_collection=convertView.findViewById(R.id.len_overdue_collection);
            holder.len_collection=convertView.findViewById(R.id.len_collection);
            holder.len_hot=convertView.findViewById(R.id.len_hot);
            holder.len_warm=convertView.findViewById(R.id.len_warm);
            holder.card_viewfill=convertView.findViewById(R.id.card_viewfill);
            holder.txt_location=convertView.findViewById(R.id.txt_location);
            holder.img_mobile_app=convertView.findViewById(R.id.img_mobile_app);


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
        /*holder.textcollection.setText(TeamMemberBeanArrayList.get(position).getCollection()
        );*/

        String Collection=TeamMemberBeanArrayList.get(position).getCollection();
        String[] CollectionList = Collection.split("\\/");
        String Collcount = CollectionList [0];
        Collection = CollectionList [1];
        if (Collection.length()>8){
            collection=Double.parseDouble(Collection);
            collection =collection/100000;
            holder.textcollection.setText(Collcount+"/"+String.format("%.2f", collection));
        }else {
            holder.textcollection.setText(TeamMemberBeanArrayList.get(position).getCollection());
        }







        String Hot=TeamMemberBeanArrayList.get(position).getHot();
        String[] hotList = Hot.split("\\/");
        String count = hotList [0];
        Hot = hotList [1];
        if (Hot.length()>8){
            hot=Double.parseDouble(Hot);
            hot =hot/100000;
            holder.texthot.setText(count+"/"+String.format("%.2f", hot));
        }else {
            holder.texthot.setText(TeamMemberBeanArrayList.get(position).getHot());
        }

        /*holder.texthot.setText(TeamMemberBeanArrayList.get(position).getHot()
        );
        */

        holder.texttomorrow.setText(TeamMemberBeanArrayList.get(position).getTomorrow());
        holder.text_review.setText(TeamMemberBeanArrayList.get(position).getCallReview());

        String Warm=TeamMemberBeanArrayList.get(position).getWarm();
        String[] warmList = Warm.split("\\/");
        String wcount = warmList [0];
        Warm = warmList [1];
        if (Warm.length()>8){

            warm=Double.parseDouble(Warm);
            warm =warm/100000;
            holder.txt_warm.setText(wcount+"/"+String.format("%.2f", warm));
        }else {
            holder.txt_warm.setText(TeamMemberBeanArrayList.get(position).getWarm());
        }

       // holder.txt_warm.setText(TeamMemberBeanArrayList.get(position).getWarm());
        holder.txt_location.setText(TeamMemberBeanArrayList.get(position).getLocationName());
        String ISMobileApp=TeamMemberBeanArrayList.get(position).getISMobileApp();
        if (ISMobileApp.equalsIgnoreCase("NO")){
            ImageViewCompat.setImageTintList(holder.img_mobile_app, ColorStateList.valueOf(context.getResources().getColor(R.color.red)));
        }else {
            ImageViewCompat.setImageTintList(holder.img_mobile_app, ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        }

        String team = TeamMemberBeanArrayList.get(position).getReport();
        if (team.equals("Yes")){
            holder.img_team.setVisibility(View.VISIBLE);
        }else {
            holder.img_team.setVisibility(View.GONE);
        }

        holder.card_viewfill.setPreventCornerOverlap(false);


       /* holder.textcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserMasterId = TeamMemberBeanArrayList.get(i).getUserMasterId();
                String name = TeamMemberBeanArrayList.get(i).getCollection();
                
                if(holder.textcollection.getText().toString().equalsIgnoreCase("0.00 T (0)")){
                    Toast.makeText(context, "No Collection Found", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(context, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "collection");
                    intent.putExtra("UserMasterId",UserMasterId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            }

        });*/


       holder.textname.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Usermasterid = TeamMemberBeanArrayList.get(position).getUserMasterId();
               UserName = TeamMemberBeanArrayList.get(position).getUserName();
              context.startActivity(new Intent(context, ReportSelectionActivity.class)
              .putExtra("usermasterid",Usermasterid).putExtra("name",UserName).
                              setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
           }
       });

        holder.img_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView= (ImageView) view;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Usermasterid = TeamMemberBeanArrayList.get(position).getUserMasterId();
                        userpreferences = context.getSharedPreferences(WebUrlClass.USERINFO,
                                Context.MODE_PRIVATE);

                        if (isnet()) {
                            new StartSession(context, new CallbackInterface() {
                                @Override
                                public void callMethod() {
                                    new DownloadTeamJSON().execute(Usermasterid);
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


        holder.len_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
             //   obj = getObj_OPP("A", TeamMemberBeanArrayList.get(i).getUserMasterId());
                obj = getObj_OPP("A", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "A");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);



            }
        });
        holder.len_overdue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("O", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "O");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        holder.len_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("T", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "T");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        holder.len_tomo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("TO", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "TO");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        holder.len_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("C", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "C");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

               /* UserMasterId = TeamMemberBeanArrayList.get(i).getUserMasterId();
                String name = TeamMemberBeanArrayList.get(i).getCollection();

                if(holder.textcollection.getText().toString().equalsIgnoreCase("0.00 T (0)")){
                    Toast.makeText(context, "No Collection Found", Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(context, OpportunityActivity.class);
                    intent.putExtra("Opportunity", "collection");
                    intent.putExtra("UserMasterId",UserMasterId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }*/




            }
        });
        holder.len_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("H", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "H");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

            }
        });
        holder.len_warm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obj="";
                obj = getObj_OPP("W", TeamMemberBeanArrayList.get(i).getUserMasterId());
                Intent intent = new Intent(context, TeamMemberOpportunityActivity.class);
                intent.putExtra("Obj", obj);
                intent.putExtra("Type", "W");
                intent.putExtra("Username", TeamMemberBeanArrayList.get(i).getUserName());
                intent.putExtra("UserMasterId", TeamMemberBeanArrayList.get(i).getUserMasterId());
                context.startActivity(intent);
                ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

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
        TextView txt_location,textassigned, textname, textoverdue, texttoday, textcollection, texthot,texttomorrow,text_review,txt_warm;
        LinearLayout len_assign,len_overdue,len_today,len_tomo,len_overdue_collection,len_collection,len_warm,len_hot;
        ImageView img_team,img_mobile_app;
        CardView card_viewfill;
    }
    private String getObj_OPP(String Opportunity_type,String Usermasterid) {
        FinalObj = "";
        dfDate = new SimpleDateFormat("yyyy-MM-dd");
        dftime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        commonObj = new CommonObjectProperties();
        jsoncommonObj = commonObj.DataObj();


        try {

            jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
            jsonObj.put("IsSet", true);
            jsonObj.put("value1", Usermasterid);

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


        if (Opportunity_type.equalsIgnoreCase("A")) {
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }



        } else if (Opportunity_type.equalsIgnoreCase("new_opp")) {
           // txtopportunity_type.setText("New Opportunity");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                /*calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                // calendar.add(Calendar.DAY_OF_YEAR);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("AddedDt");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("overdue_opp")) {
           // txtopportunity_type.setText("Overdue Opportunity");

            try {
                String currentDateandTime = dftime.format(new Date());
                Date cdate = dftime.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                /*calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                // calendar.add(Calendar.DAY_OF_YEAR);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dftime.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("yesterday_opp")) {
            //txtopportunity_type.setText("Yesterday Opportunity");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", ""/*dfDate.format(newDate1)*/);
                //  jsonObj.put("Operator", "bet");


            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("today_opp") || Opportunity_type.equalsIgnoreCase("T")) {
            //txtopportunity_type.setText("Today Opportunity");
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
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("callagain_opp")) {
           // txtopportunity_type.setText("Call Again Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));


                jsonObj = jsoncommonObj.getJSONObject("OutcomeCode");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", "CA");
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("Tommorow_opp") ||  Opportunity_type.equalsIgnoreCase("TO")) {
           // txtopportunity_type.setText("Tomorrow Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
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
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Opportunity_type.equalsIgnoreCase("revived_opp")) {
           // txtopportunity_type.setText("Revived Opportunity");
            try {
               /* String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate1));
*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");


                jsonObj = jsoncommonObj.getJSONObject("Revived");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "eq");
                jsonObj.put("value1", "");
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (Opportunity_type.equalsIgnoreCase("this_week_opp")) {
           // txtopportunity_type.setText("This Week Opportunity");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("collection") || Opportunity_type.equalsIgnoreCase("C")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

           // txtopportunity_type.setText("Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, -7);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", false);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
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
        } else if (Opportunity_type.equalsIgnoreCase("overdue_collection")) {

            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

            //txtopportunity_type.setText("Overdue Collection");


            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                // calendar.add(Calendar.DAY_OF_YEAR, -7);*/
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("today_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

            //txtopportunity_type.setText("Today Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


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
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

           // txtopportunity_type.setText("Tomorrow Collection");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


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
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("week_overdue_collection")) {
            collectioncallcommonObj = new CollectionCallCommonObjectProperties();
            jsoncommonObj = collectioncallcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

            //txtopportunity_type.setText("This Week Collection");
            try {

                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "2");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Opportunity_type.equalsIgnoreCase("hot_call") || Opportunity_type.equalsIgnoreCase("H")) {
           // txtopportunity_type.setText("Hot");

            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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
                jsonObj.put("value1", "");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }


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
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Opportunity_type.equalsIgnoreCase("warm_call")||Opportunity_type.equalsIgnoreCase("W")) {
           // txtopportunity_type.setText("Warm");
            try {
                jsonObj = jsoncommonObj.getJSONObject("CallStatus");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "Warm");
                jsonObj.put("Operator", "eq");


            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", false);
                jsonObj.put("value1", "1");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (Opportunity_type.equalsIgnoreCase("feedback")) {

            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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
            //txtopportunity_type.setText("Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DATE, +30);
                Date newDate1 = calendar.getTime();

                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

                //  jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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
           // txtopportunity_type.setText("Overdue Feedback");

            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "<");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", "");

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("today_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

           // txtopportunity_type.setText("Today Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


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
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("tomorrow_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

            //txtopportunity_type.setText("Tomorrow Feedback");
            try {
                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(cdate);
                calendar.add(Calendar.DAY_OF_YEAR, +1);
                Date newDate1 = calendar.getTime();

               /* Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(cdate);
                calendar1.add(Calendar.DAY_OF_YEAR, 7);
                Date newDate2 = calendar1.getTime();*/


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
                jsonObj.put("value1", "3");
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Opportunity_type.equalsIgnoreCase("week_overdue_feedback")) {
            feedbackcommonObj = new FeedbackCommonObjectProperties();
            jsoncommonObj = feedbackcommonObj.DataObj();


            try {

                jsonObj = jsoncommonObj.getJSONObject("CurrentCallOwner");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", Usermasterid);

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

            //txtopportunity_type.setText("This Week Feedback");
            try {

                String currentDateandTime = dfDate.format(new Date());
                Date cdate = dfDate.parse(currentDateandTime);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                //   calendar.setTime(cdate);

                calendar.getFirstDayOfWeek();
                Date newDate1 = null, newDate2 = null;// = calendar.getTime();
                for (int i = 0; i < 7; i++) {
                    System.out.println(dfDate.format(calendar.getTime()));

                    if (i == 0) {
                        //  calendar.add(Calendar.DATE, 1);
                        newDate1 = calendar.getTime();
                        calendar.setTime(newDate1);
                    } else if (i == 6) {
                        calendar.add(Calendar.DATE, +6);
                        newDate2 = calendar.getTime();
                    }

                }


                jsonObj = jsoncommonObj.getJSONObject("NextActionDateTime");
                jsonObj.put("IsSet", true);
                jsonObj.put("Operator", "bet");
                jsonObj.put("value1", dfDate.format(newDate1));
                jsonObj.put("value2", dfDate.format(newDate2));

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                jsonObj = jsoncommonObj.getJSONObject("CallType");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 3);
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

    




/*    private String getObj(String a, String UserMasterId) {
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

            *//*try {
                jsonObj = jsoncommonObj.getJSONObject("TypeofCall");
                jsonObj.put("IsSet", true);
                jsonObj.put("value1", 2);
                jsonObj.put("Operator", "eq");

            } catch (Exception e) {
                e.printStackTrace();
            }*//*

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
    }*/

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
                        "?UserMasterId=" + URLEncoder.encode(Usermasterid, "UTF-8");

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
            try {
                if (response != null) {

                    context.startActivity(new Intent(context,
                            SubTeamMemberActivity.class).
                            putExtra("User", Usermasterid).
                            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | FLAG_ACTIVITY_NEW_TASK));

                    ((TeamMemberActivity) context).overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }

    }



}
