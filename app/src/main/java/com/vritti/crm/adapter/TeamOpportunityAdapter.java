package com.vritti.crm.adapter;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
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
import com.vritti.crm.vcrm7.ContactActivity;
import com.vritti.crm.vcrm7.TeamMemberOpportunityActivity;
import com.vritti.crm.vcrm7.OpportunityUpdateActivity;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.vwb.Beans.ActivityBean;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.vritti.crm.adapter.VisitPlanAdapter.formateDateFromstring;

public class TeamOpportunityAdapter extends RecyclerView.Adapter<TeamOpportunityAdapter.OpportunityHolder> {
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
    private String date_after="";
    private String Year="";
    private String exp_date="";

    public TeamOpportunityAdapter(Context context, ArrayList<PartialCallList> partialCallLists) {
        this.context = context;
        this.partialCallListArrayList = partialCallLists;
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

            ((TeamMemberOpportunityActivity)context).loadNextActivity(partialCallListArrayList.size());

        }else {
            */

        if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
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
            holder.callrating.setImageResource(R.drawable.ic_cube);
        } else if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("3"))
        {
            holder.callrating.setImageResource(R.drawable.ic_cube);
        }
        String City = partialCallListArrayList.get(i).getCityname();

        holder.txtfirmname.setText(partialCallListArrayList.get(i).getFirmname()+", "+City);
        String Date = partialCallListArrayList.get(i).getActiondatetime();
        String latestRemark = partialCallListArrayList.get(i).getLatestRemark();
        if (!(latestRemark == null || latestRemark.equals("null") || latestRemark.equals(""))) {
            holder.tv_latestremark.setText("For " + partialCallListArrayList.get(i).getLatestRemark());

        }
        holder.txtactiondatetime.setText(Date);
        if (partialCallListArrayList.get(i).getEmailid().equalsIgnoreCase("")){
            holder.txt_email.setVisibility(View.GONE);
        }else {
            holder.txt_email.setText(partialCallListArrayList.get(i).getEmailid());
        }
        String  NextAction=partialCallListArrayList.get(i).getNextAction();
        if (NextAction.equalsIgnoreCase("Email")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.email_24));
            holder.img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else if (NextAction.equalsIgnoreCase("Telephone")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.visit24));
            holder.img_nextaction.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary
            ), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else {
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.call_24));
        }


        String Mobile = partialCallListArrayList.get(i).getMobileno();

        String Contactname = partialCallListArrayList.get(i).getContactName();
        String Product = partialCallListArrayList.get(i).getProduct();
        if (City.equals("null")) {
            City = "";
        }
        if (Product==null||Product.equals("null")) {
            Product = "";
        }
        String appenContact = Contactname + ", " +  Mobile;
        if (appenContact.equals(" , ")) {
            appenContact = "No Contact Available";
        }
        String appendCityProduct =  Product;
        if (appendCityProduct.equals("null")) {
            appendCityProduct = "";
        }


        String Concatdata = appendCityProduct +"\n"+appenContact;


        holder.tvcall.setText(Concatdata);

        //}


        if (i % 2 == 1) {
            //holder.realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            //holder.realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
        }

        Address=partialCallListArrayList.get(i).getAddress();
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

        }catch (Exception e){
            e.printStackTrace();
        }


        String ExpValue=partialCallListArrayList.get(i).getExpectedValue();
        if (ExpValue.equals("0.00")||ExpValue.equals("0.0")||ExpValue.equals("0")){

        }else {

            String dt = partialCallListArrayList.get(i).getExpectedCloserDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                dt = dt.substring(dt.indexOf("(") + 1, dt.lastIndexOf(")"));
                long timestamp = Long.parseLong(dt);
                java.util.Date date = new Date(timestamp);
                exp_date = sdf.format(date);
            }catch (Exception e){
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            Year= String.valueOf(year);
            if (date_after.contains(Year)){
                date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM", exp_date);
            }else {
                date_after = formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", exp_date);
            }
            holder.txt_expvalue.setVisibility(View.VISIBLE);
            double d = Double.parseDouble(ExpValue);

            if (date_after.contains("1901")){
                holder.txt_expvalue.setText("Expected Order of Rs." + String.format("%.2f", d));

            }else {
                holder.txt_expvalue.setText("Expected Order of Rs." + String.format("%.2f", d) + " by " + date_after);

            }
            //holder.txt_expvalue.setText("EV-"+ExpValue);

        }

        String milestone=partialCallListArrayList.get(i).getNextMilestone();
        holder.milestone.setText(milestone);

        holder.txtaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lat==0&Lng==0){

                }else {

                    try {
                         Address=partialCallListArrayList.get(i).getAddress();
                        Geocoder coder = new Geocoder(context);
                        Listaddress = coder.getFromLocationName(Address, 5);
                        android.location.Address location = Listaddress.get(0);
                        Lat = location.getLatitude();
                        Lng = location.getLongitude();

                    }catch (Exception e){
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

    }

    @Override
    public int getItemCount() {
        return partialCallListArrayList.size();
    }

    public class OpportunityHolder extends RecyclerView.ViewHolder {

        LinearLayout  laycall_type,len_action,len_call,len_callslist;
        TextView txtfirmname, txtcityname, tv_latestremark, txtactiondatetime,
                tvcall, txt_chat,txtaddress,txt_expvalue,txt_email,milestone;
        ImageView img_action, img_appotunity_update,img_contact,img_nextaction,callrating;
        RelativeLayout realcolors;
        CardView card_viewfill;



        public OpportunityHolder(View convertView) {
            super(convertView);

            realcolors = (RelativeLayout) convertView.findViewById(R.id.realcolors);
            card_viewfill = (CardView) convertView.findViewById(R.id.card_viewfill);
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


            len_callslist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CRM_Callslist_Partial.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((TeamMemberOpportunityActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);

                }
            });



            len_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, CRM_Callslist_Partial.class);
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
                    intent.putExtra("call_type_1", "Crm_Opportunity");
                    intent.putExtra("projmasterId", "");
                    intent.putExtra("AssignBy", UserName);
                    intent.putExtra("AssignById", UserMasterId);
                    intent.putExtra("SourceId", SourceId);
                    intent.putExtra("mile", partialCallListArrayList.get(getAdapterPosition()).getNextMilestone());
                    intent.putExtra("mobile", partialCallListArrayList.get(getAdapterPosition()).getMobileno());
                    intent.putExtra("evalue", partialCallListArrayList.get(getAdapterPosition()).getExpectedValue());

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    ((TeamMemberOpportunityActivity) context).overridePendingTransition(R.anim.slide_right_to_left,R.anim.slide_left_to_right);


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
                        ((TeamMemberOpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);
                    } else {
                        Toast.makeText(context, "Chat module not installed", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            img_appotunity_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TeamMemberOpportunityActivity)context).OpportunityUpdate(getAdapterPosition(),partialCallListArrayList);
                    ((TeamMemberOpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);
                }
            });

           /* tvcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TeamMemberOpportunityActivity)context).tvCallMethd(getAdapterPosition(),partialCallListArrayList);
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
                    ((TeamMemberOpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);


                }
            });

            img_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TeamMemberOpportunityActivity)context).ActionClick(getAdapterPosition(),partialCallListArrayList,v);
                    ((TeamMemberOpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);



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
                    ((TeamMemberOpportunityActivity)  context).overridePendingTransition(R.anim.slide_up,R.anim.no_anim);

                }
            });

        }
    }

    @Override
    public int getItemViewType(int position) {
        //returns 1 or 2 based on data type
        return position;
    }





}
