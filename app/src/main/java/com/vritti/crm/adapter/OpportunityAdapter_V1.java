package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vritti.chat.activity.MultipleGroupActivity;
import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.classes.CommonFunctionCrm;
import com.vritti.crm.vcrm7.CRM_Callslist_Partial;
import com.vritti.crm.vcrm7.CallListActionActivity;
import com.vritti.crm.vcrm7.CallListActivity;
import com.vritti.crm.vcrm7.CallListLoggingTimeActivity;
import com.vritti.crm.vcrm7.ContactActivity;
import com.vritti.crm.vcrm7.OpportunityActivity;
import com.vritti.crm.vcrm7.OpportunityActivity_V1;
import com.vritti.crm.vcrm7.OpportunityUpdateActivity;
import com.vritti.crm.vcrm7.OpportunityUpdateActivity_New;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.ekatm.services.ForegroundService;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.vworkbench.LoggingTimeActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.vritti.crm.adapter.VisitPlanAdapter.formateDateFromstring;

public class OpportunityAdapter_V1 extends RecyclerView.Adapter<OpportunityAdapter_V1.OpportunityHolder> {
    private final Utility ut;
    private final DatabaseHandlers db;
    Context context;
    ArrayList<PartialCallList> partialCallListArrayList;
    List<android.location.Address> Listaddress;
    String Address="";
    double Lat=0,Lng=0;
    CommonFunctionCrm cf;

    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "",IsChatApplicable="";
    private String Mobile="",flag="",Contact="";
    String starttime = "", endtime = "", duration = "", rowNo = "";
    private String Year="";
    private String date_after="";

    SharedPreferences AtendanceSheredPreferance;


    String actid, time, Starttime, sp_date;
    String getdate, currentTime;
    private int backToposition;
    private String Opportunity_type="";

    public OpportunityAdapter_V1(Context context,ArrayList<PartialCallList> partialCallLists,String flag,
                                 String Contact,String starttime,String endtime,String duration,String rowNo) {
        this.context = context;
        this.partialCallListArrayList = partialCallLists;
        this.flag=flag;
        this.Contact=Contact;
        this.starttime=starttime;
        this.endtime=endtime;
        this.duration=duration;
        this.rowNo=rowNo;
        ut = new Utility();
        cf = new CommonFunctionCrm(context);
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(context, WebUrlClass.GET_USERNAME_KEY, settingKey);
        //UserMasterId = ut.getValue(context, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        IsChatApplicable = ut.getValue(context, WebUrlClass.GET_ISCHATAPPLICABLE_KEY, settingKey);

        AtendanceSheredPreferance = context.getSharedPreferences(WebUrlClass.ATTENDANCE_PREFERENCES,
                Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public OpportunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.crm_callslist_partial_lay_v1, parent, false);

        return new OpportunityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityHolder holder, @SuppressLint("RecyclerView") final int i) {

      /*  if((partialCallListArrayList.size()) == i){

            ((OpportunityActivity)context).loadNextActivity(partialCallListArrayList.size());

        }else {
            */
        try {

            if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("1")) {
                //Hot-Red,Warm-Green,Cold-Purple
                if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
             /*   holder.callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                ImageViewCompat.setImageTintList(holder.callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.cold)));
             */
                    holder.callrating.setImageResource(R.drawable.ic_cube);
                } else if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                    // holder.callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                    //  ImageViewCompat.setImageTintList(holder.callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.hot)));

                    holder.callrating.setImageResource(R.drawable.img_hot_call);
                } else if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                    //   holder.callrating.setImageDrawable(context.getResources().getDrawable(R.drawable.square));
                    // ImageViewCompat.setImageTintList(holder.callrating, ColorStateList.valueOf(context.getResources().getColor(R.color.warm)));
                    holder.callrating.setImageResource(R.drawable.img_warm_call);
                }
            } else if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("2")) {
                holder.callrating.setBackgroundColor(Color.parseColor("#3366FF"));
            } else if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("3")) {
                holder.callrating.setBackgroundColor(Color.parseColor("#FF1493"));
            }
            String City = partialCallListArrayList.get(i).getCityname();

            holder.txtfirmname.setText(partialCallListArrayList.get(i).getFirmname() + ", " + City);
            String Date = partialCallListArrayList.get(i).getActiondatetime();
            long date1 = System.currentTimeMillis();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            String dateString = sdf.format(date1);
            if (Date.contains(dateString)) {
            } else {
                holder.txt_expvalue.setTextColor(context.getResources().getColor((R.color.red)));
            }
            Log.d("DateDisplay", Date);
            String latestRemark = partialCallListArrayList.get(i).getLatestRemark();
            if (!(latestRemark == null || latestRemark.equals("null") || latestRemark.equals(""))) {
                holder.tv_latestremark.setText("For " + partialCallListArrayList.get(i).getLatestRemark());

            }
            holder.txtactiondatetime.setText(Date);
            if (partialCallListArrayList.get(i).getEmailid().equalsIgnoreCase("")) {
                holder.txt_email.setVisibility(View.GONE);
            } else {
                holder.txt_email.setText(partialCallListArrayList.get(i).getEmailid());
            }
            String NextAction = partialCallListArrayList.get(i).getNextAction();
            if (NextAction.equalsIgnoreCase("Email")) {
                holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.email_24));
                holder.img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

            } else if (NextAction.equalsIgnoreCase("Telephone")) {
                holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
            } else if (NextAction.equalsIgnoreCase("Visit")) {
                holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.visit24));
                holder.img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary
                ), android.graphics.PorterDuff.Mode.MULTIPLY);
            } else {
                holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
            }


            Mobile = partialCallListArrayList.get(i).getMobileno();

            String Contactname = partialCallListArrayList.get(i).getContactName();
            String Product = partialCallListArrayList.get(i).getProduct();
            if (City.equals("null")) {
                City = "";
            }
            if (Product == null || Product.equals("null")) {
                Product = "";
            }
            String appenContact = Contactname + ", " + Mobile;
            if (appenContact.equals(" , ")) {
                appenContact = "No Contact Available";
            }
            String appendCityProduct = Product;
            if (appendCityProduct.equals("null")) {
                appendCityProduct = "";
            }


            String Concatdata = appendCityProduct + "\n" + appenContact;

            if (appendCityProduct.equalsIgnoreCase("")){
                holder.tvcall.setText(appenContact);
            }else {
                holder.tvcall.setText(Concatdata);
            }


            //}


            if (i % 2 == 1) {
                //holder.realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
            } else {
                //holder.realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
            }

            Address = partialCallListArrayList.get(i).getAddress();
            holder.txtaddress.setText(Address);

       /* Lat=partialCallLists.get(position).getLat();
        Long=partialCallLists.get(position).getLong();
       */
            Geocoder coder = new Geocoder(context);
            try {
                Listaddress = coder.getFromLocationName(Address, 5);
                android.location.Address location = Listaddress.get(0);
                Lat = location.getLatitude();
                Lng = location.getLongitude();

            } catch (Exception e) {
                e.printStackTrace();
            }


            String ExpValue = partialCallListArrayList.get(i).getExpectedValue();
            if (ExpValue.equals("0.00") || ExpValue.equals("0.0") || ExpValue.equals("0")) {

            } else {
                String dt = partialCallListArrayList.get(i).getExpectedCloserDate();
                String[] namesList = dt.split("T");
                String name1 = namesList [0];

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                Year= String.valueOf(year);
                if (name1.contains(Year)){
                    date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM", name1);
                }else {
                    date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", name1);
                }
                holder.txt_expvalue.setVisibility(View.VISIBLE);
                double d = Double.parseDouble(ExpValue);

                if (date_after.contains("1901")){
                    holder.txt_expvalue.setText("Expected Order of Rs." + String.format("%.2f", d));

                }else {
                    holder.txt_expvalue.setText("Expected Order of Rs." + String.format("%.2f", d) + " by " + date_after);

                }

//                holder.txt_expvalue.setText("Expected Order of Rs." + String.format("%.2f", d) + " by "+date_after);



                //holder.txt_expvalue.setText("EV-"+ExpValue);

            }


            actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, "");
            starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, "");
            if (actid != null||actid.equalsIgnoreCase("")) {
                if (actid.equalsIgnoreCase(partialCallListArrayList.get(i).getCallId())) {
                    holder.len_call.setBackgroundColor(Color.CYAN);
                } else {
                    if (i % 2 == 1) {
                        holder.card_viewfill.setBackgroundColor(Color.parseColor("#DBE8EA"));
                    } else {
                        holder.card_viewfill.setBackgroundColor(Color.parseColor("#F1F6F7"));
                    }
                }
            }



            String milestone = partialCallListArrayList.get(i).getNextMilestone();
            holder.milestone.setText(milestone);

            holder.txtaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Lat == 0 & Lng == 0) {

                    } else {

                        try {
                            Address = partialCallListArrayList.get(i).getAddress();
                            Geocoder coder = new Geocoder(context);
                            Listaddress = coder.getFromLocationName(Address, 5);
                            android.location.Address location = Listaddress.get(0);
                            Lat = location.getLatitude();
                            Lng = location.getLongitude();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String geoUri = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng + " (" + partialCallListArrayList.get(i).getFirmname() + ")";
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                        if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(mapIntent);

                   /* String uriMap = "http://maps.google.com/maps?q=loc:" + Lat + "," + Lng;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriMap));
                    startActivity(intent);
*/
                        }
                    }
                }
            });

            holder.card_viewfill.setPreventCornerOverlap(false);

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return partialCallListArrayList.size();
    }

    public class OpportunityHolder extends RecyclerView.ViewHolder {

        LinearLayout  laycall_type,len_action,len_call,len_callslist;
        public TextView txtfirmname, txtcityname, tv_latestremark, txtactiondatetime,
                tvcall, txt_chat,txtaddress,txt_expvalue,txt_email,milestone;
        ImageView img_action, img_appotunity_update,img_contact,img_nextaction,callrating;
        RelativeLayout realcolors;
        CardView card_viewfill;


        public OpportunityHolder(View convertView) {
            super(convertView);

            realcolors = (RelativeLayout) convertView.findViewById(R.id.realcolors);
            txtfirmname = (TextView) convertView.findViewById(R.id.firmname);
            txtcityname = (TextView) convertView.findViewById(R.id.city);
            tv_latestremark = (TextView) convertView.findViewById(R.id.tv_latestremark);
            //   spinner_action = (TextView) convertView.findViewById(R.id.spinner_action);
            img_action = (ImageView) convertView.findViewById(R.id.btn_action);
            //    btn_action = (Button) convertView.findViewById(R.id.btn_action);
            txtactiondatetime = (TextView) convertView.findViewById(R.id.actiondatetime);
            tvcall = (TextView) convertView.findViewById(R.id.tvcall);
            laycall_type = (LinearLayout) convertView.findViewById(R.id.laycall_type);
            img_appotunity_update = (ImageView) convertView.findViewById(R.id.img_appotunity_update);
             txt_chat = (TextView) convertView.findViewById(R.id.txt_chat);
            txtaddress = (TextView) convertView.findViewById(R.id.txtaddress);
            txt_expvalue = (TextView) convertView.findViewById(R.id.txt_expvalue);
            len_action = (LinearLayout) convertView.findViewById(R.id.len_action);
            img_contact =convertView.findViewById(R.id.img_contact);
            txt_email =convertView.findViewById(R.id.txt_email);
            img_nextaction =convertView.findViewById(R.id.img_nextaction);
            callrating = convertView.findViewById(R.id.callrating);
            len_call = (LinearLayout)convertView.findViewById(R.id.lencall);
            len_callslist = (LinearLayout)convertView.findViewById(R.id.len_callslist);
            milestone = (TextView) convertView.findViewById(R.id.milestone);
            card_viewfill = (CardView) convertView.findViewById(R.id.card_viewfill);


            len_callslist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CRM_Callslist_Partial.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((OpportunityActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }
            });



            len_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (flag.equalsIgnoreCase("1")){

                        ((OpportunityActivity_V1)context).expense(getAdapterPosition(),partialCallListArrayList.get(getPosition()).getCallId(), partialCallListArrayList.get(getAdapterPosition()).getFirmname());
                    }
                    else if (flag.equalsIgnoreCase("2")) {
                        Intent intent = new Intent(context, ContactActivity.class);
                        intent.putExtra("firm", partialCallListArrayList.get(getAdapterPosition()).getFirmname());
                        intent.putExtra("date", partialCallListArrayList.get(getAdapterPosition()).getActiondatetime());
                        intent.putExtra("action", partialCallListArrayList.get(getAdapterPosition()).getNextAction());
                        intent.putExtra("status", partialCallListArrayList.get(getAdapterPosition()).getCallStatus());
                        intent.putExtra("call", tvcall.getText().toString());
                        intent.putExtra("remark", tv_latestremark.getText().toString());
                        String Call_ProspectId = partialCallListArrayList.get(getPosition()).getPKSuspectId();
                        String Call_CallType = partialCallListArrayList.get(getPosition()).getCallType();
                        String Call_Callid = partialCallListArrayList.get(getPosition()).getCallId();
                        String SourceId = partialCallListArrayList.get(getPosition()).getSourceId();
                        intent.putExtra("callid", Call_Callid);
                        intent.putExtra("call_prospect", Call_ProspectId);
                        intent.putExtra("call_type", Call_CallType);
                        intent.putExtra("call_type_1", "Crm_Opportunity");
                        intent.putExtra("projmasterId", "");
                        intent.putExtra("AssignBy", UserName);
                        intent.putExtra("AssignById", UserMasterId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("mile", partialCallListArrayList.get(getAdapterPosition()).getNextMilestone());
                        intent.putExtra("mobile", partialCallListArrayList.get(getAdapterPosition()).getMobileno());
                        intent.putExtra("evalue", partialCallListArrayList.get(getAdapterPosition()).getExpectedValue());
                        intent.putExtra("type", "Callfromcalllogs");
                        intent.putExtra("starttime", starttime);
                        intent.putExtra("endtime", endtime);
                        intent.putExtra("duration", duration);
                        intent.putExtra("rowNo", rowNo);
                        if (flag.equalsIgnoreCase("2")){
                            intent.putExtra("callmob", Contact);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((OpportunityActivity_V1) context).overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);
                    }
                    else {

                        Intent intent = new Intent(context, CRM_Callslist_Partial.class);
                        intent.putExtra("firm", partialCallListArrayList.get(getAdapterPosition()).getFirmname());
                        intent.putExtra("date", partialCallListArrayList.get(getAdapterPosition()).getActiondatetime());
                        intent.putExtra("action", partialCallListArrayList.get(getAdapterPosition()).getNextAction());
                        intent.putExtra("status", partialCallListArrayList.get(getAdapterPosition()).getCallStatus());
                        intent.putExtra("call", tvcall.getText().toString());
                        intent.putExtra("remark", tv_latestremark.getText().toString());
                        String Call_ProspectId = partialCallListArrayList.get(getPosition()).getPKSuspectId();
                        String Call_CallType = partialCallListArrayList.get(getPosition()).getCallType();
                        String Call_Callid = partialCallListArrayList.get(getPosition()).getCallId();
                        String SourceId = partialCallListArrayList.get(getPosition()).getSourceId();
                        intent.putExtra("callid", Call_Callid);
                        intent.putExtra("call_prospect", Call_ProspectId);
                        intent.putExtra("call_type", Call_CallType);
                        intent.putExtra("call_type_1", "Crm_Opportunity");
                        intent.putExtra("projmasterId", "");
                        intent.putExtra("AssignBy", UserName);
                        intent.putExtra("AssignById", UserMasterId);
                        intent.putExtra("SourceId", SourceId);
                        intent.putExtra("mile", partialCallListArrayList.get(getAdapterPosition()).getNextMilestone());
                        intent.putExtra("mobile", partialCallListArrayList.get(getAdapterPosition()).getMobileno());
                        intent.putExtra("evalue", partialCallListArrayList.get(getAdapterPosition()).getExpectedValue());
                        if (flag.equalsIgnoreCase("2")){
                            intent.putExtra("callmob", Contact);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        ((OpportunityActivity_V1) context).overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

                    }

                }
            });

            txt_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (IsChatApplicable.equalsIgnoreCase("true")) {

                        Intent intent = new Intent(context, MultipleGroupActivity.class);
                        intent.putExtra("firm",partialCallListArrayList.get(getAdapterPosition()).getFirmname());
                        intent.putExtra("date",partialCallListArrayList.get(getAdapterPosition()).getActiondatetime());
                        intent.putExtra("action",partialCallListArrayList.get(getAdapterPosition()).getNextAction());
                        intent.putExtra("status",partialCallListArrayList.get(getAdapterPosition()).getCallStatus());
                        intent.putExtra("call",tvcall.getText().toString());
                        intent.putExtra("remark",tv_latestremark.getText().toString());
                        String  Call_ProspectId = partialCallListArrayList.get(getPosition()).getPKSuspectId();
                        String Call_CallType = partialCallListArrayList.get(getPosition()).getCallType();
                        String Call_Callid = partialCallListArrayList.get(getPosition()).getCallId();
                        String SourceId = partialCallListArrayList.get(getPosition()).getSourceId();
                        intent.putExtra("callid", Call_Callid);
                        intent.putExtra("call_prospect", Call_ProspectId);
                        intent.putExtra("call_type", Call_CallType);
                        intent.putExtra("call_type", "Crm_Opportunity");
                        intent.putExtra("projmasterId", "");
                        intent.putExtra("AssignBy", UserName);
                        intent.putExtra("AssignById", UserMasterId);
                        intent.putExtra("chat", IsChatApplicable);
                        intent.putExtra("SourceId", SourceId);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context. startActivity(intent);
                        ((OpportunityActivity_V1)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);
                    } else {
                        Toast.makeText(context, "Chat module not installed", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            img_appotunity_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OpportunityActivity)context).OpportunityUpdate(getAdapterPosition(),partialCallListArrayList);
                    ((OpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);
                }
            });

           /* tvcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OpportunityActivity)context).tvCallMethd(getAdapterPosition(),partialCallListArrayList);
                }
            });
*/
            laycall_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String Partial = partialCallListArrayList.get(getAdapterPosition()).getIsPartial();

                        Intent intent = new Intent(context, OpportunityUpdateActivity.class);
                        intent.putExtra("callid", partialCallListArrayList.get(getAdapterPosition()).getCallId());
                        intent.putExtra("firmname", partialCallListArrayList.get(getAdapterPosition()).getFirmname());
                        intent.putExtra("calltype", partialCallListArrayList.get(getAdapterPosition()).getCallType());
                        intent.putExtra("table", "Call");
                        intent.putExtra("ProspectId", partialCallListArrayList.get(getAdapterPosition()).getPKSuspectId());
                        context.startActivity(intent);
                    ((OpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);


                }
            });

            img_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((OpportunityActivity)context).ActionClick(getAdapterPosition(),partialCallListArrayList,v);
                    ((OpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);



                }
            });

            img_nextaction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String  Call_ProspectId = partialCallListArrayList.get(getPosition()).getPKSuspectId();
                    String Call_CallType = partialCallListArrayList.get(getPosition()).getCallType();
                    String Call_Callid = partialCallListArrayList.get(getPosition()).getCallId();

                    Intent intent = new Intent(context, ContactActivity.class);
                    intent.putExtra("callid", Call_Callid);
                    intent.putExtra("call_prospect", Call_ProspectId);
                    intent.putExtra("call_type", Call_CallType);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((OpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);

                }
            });

            len_call.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                   longPress(getAdapterPosition());

                    return false;
                }
            });


        }
    }

    @Override
    public int getItemViewType(int position) {
        //returns 1 or 2 based on data type
        return position;
    }



    public void longPress(int position) {
        String s = partialCallListArrayList.get(position).getFirmname();
        actid = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTIVITY_KEY, null);
        starttime = AtendanceSheredPreferance.getString(WebUrlClass.ATTENDANCE_PREFERENCES_ACTSTART_KEY, null);
        //  String id = lsActivityList.get(position).getActid();
       String oppo= partialCallListArrayList.get(position).getCallType();
       if (oppo.equalsIgnoreCase("1")){
           Opportunity_type="Opportunity";
       }else {
           Opportunity_type="Collection";

       }


        if (actid != null) {

            if (!(actid.equalsIgnoreCase(partialCallListArrayList
                    .get(position).getCallId()))) {


            } else if (starttime != null) {



                Intent serviceIntent = new Intent(context, ForegroundService.class);
                serviceIntent.putExtra("inputExtra", partialCallListArrayList.get(position).getFirmname());
                serviceIntent.putExtra("id", partialCallListArrayList.get(position).getCallId());
                serviceIntent.putExtra("f", "Start");
                serviceIntent.putExtra("time",starttime);
                serviceIntent.putExtra("module","CRM");

                serviceIntent.putExtra("Opportunity",Opportunity_type);
                ContextCompat.startForegroundService(context, serviceIntent);

               /* serviceIntent.setClass(context, OpportunityUpdateActivity_New.class);
                serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                serviceIntent.putExtra("Flag", "End");
                serviceIntent.putExtra("callid", partialCallListArrayList.get(position).getCallId());
                serviceIntent.putExtra("firmname", partialCallListArrayList.get(position).getFirmname());
                serviceIntent.putExtra("calltype", partialCallListArrayList.get(position).getCallType());
                serviceIntent.putExtra("table", "Call");
                serviceIntent.putExtra("ProspectId", partialCallListArrayList.get(position).getPKSuspectId());
                */


                Intent intent = new Intent(context, OpportunityUpdateActivity_New.class);
                intent.putExtra("firm", partialCallListArrayList.get(position).getFirmname());
                intent.putExtra("firmname", partialCallListArrayList.get(position).getFirmname());
                intent.putExtra("date", partialCallListArrayList.get(position).getActiondatetime());
                intent.putExtra("action", partialCallListArrayList.get(position).getNextAction());
                intent.putExtra("status", partialCallListArrayList.get(position).getCallStatus());
                intent.putExtra("call", Mobile);
                intent.putExtra("remark", partialCallListArrayList.get(position).getLatestRemark());
                String Call_ProspectId = partialCallListArrayList.get(position).getPKSuspectId();
                String Call_CallType = partialCallListArrayList.get(position).getCallType();
                String Call_Callid = partialCallListArrayList.get(position).getCallId();
                String SourceId = partialCallListArrayList.get(position).getSourceId();
                intent.putExtra("callid", Call_Callid);
                intent.putExtra("call_prospect", Call_ProspectId);
                intent.putExtra("ProspectId", Call_ProspectId);
                intent.putExtra("call_type", Call_CallType);
                intent.putExtra("calltype", Call_CallType);
                intent.putExtra("call_type_1", "Crm_Opportunity");
                intent.putExtra("projmasterId", "");
                intent.putExtra("AssignBy", UserName);
                intent.putExtra("AssignById", UserMasterId);
                intent.putExtra("SourceId", SourceId);
                intent.putExtra("mile", partialCallListArrayList.get(position).getNextMilestone());
                intent.putExtra("mobile", partialCallListArrayList.get(position).getMobileno());
                intent.putExtra("evalue", partialCallListArrayList.get(position).getExpectedValue());
                if (flag.equalsIgnoreCase("2")){
                    intent.putExtra("callmob", Contact);
                }
                intent.putExtra("Start", starttime);
                intent.putExtra("Flag", "End");
                intent.putExtra("table", "Call");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                /*Intent sintent = new Intent(context, ForegroundService.class);
                context.stopService(sintent);*/
                ((OpportunityActivity_V1) context).overridePendingTransition(R.anim.slide_right_to_left, R.anim.slide_left_to_right);

            }
        } else {
            if (Starttime != null) {
                Intent myIntent = new Intent();
                myIntent.setClass(context, CallListLoggingTimeActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("ActivityId",
                        partialCallListArrayList.get(position).getCallId());
                myIntent.putExtra("ActivityName", partialCallListArrayList
                        .get(position).getFirmname());
                myIntent.putExtra("Flag", "End");
                myIntent.putExtra("Opportunity",Opportunity_type);
                context.startActivity(myIntent);

            } else {
                Intent myIntent = new Intent();
                myIntent.setClass(context, CallListLoggingTimeActivity.class);
                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myIntent.putExtra("ActivityId",
                        partialCallListArrayList.get(position).getCallId());
                myIntent.putExtra("ActivityName", partialCallListArrayList
                        .get(position).getFirmname());
                myIntent.putExtra("Opportunity",Opportunity_type);
                if (EnvMasterId.contains("eno") || EnvMasterId.contains("dabur")) {
                    myIntent.putExtra("Flag", "End");
                } else {
                    myIntent.putExtra("Flag", "Start");
                }
                context.startActivity(myIntent);
            }
        }


    }


}
