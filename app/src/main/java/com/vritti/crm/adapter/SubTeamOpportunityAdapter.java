package com.vritti.crm.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vritti.crm.bean.PartialCallList;
import com.vritti.crm.vcrm7.ContactActivity;
import com.vritti.crm.vcrm7.OpportunityUpdateActivity;
import com.vritti.crm.vcrm7.SubMemberOpportunityActivity;
import com.vritti.crm.vcrm7.TeamMemberOpportunityActivity;
import com.vritti.ekatm.R;

import java.util.ArrayList;
import java.util.List;

public class SubTeamOpportunityAdapter extends RecyclerView.Adapter<SubTeamOpportunityAdapter.OpportunityHolder> {
    Context context;
    ArrayList<PartialCallList> partialCallListArrayList;
    List<android.location.Address> Listaddress;
    String Address="";
    double Lat=0,Lng=0;


    public SubTeamOpportunityAdapter(Context context, ArrayList<PartialCallList> partialCallLists) {
        this.context = context;
        this.partialCallListArrayList = partialCallLists;
    }

    @NonNull
    @Override
    public OpportunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.crm_callslist_partial_lay, parent, false);

        return new OpportunityHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OpportunityHolder holder, final int i) {

      /*  if((partialCallListArrayList.size()) == i){

            ((OpportunityActivity)context).loadNextActivity(partialCallListArrayList.size());

        }else {
            */

        if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("1")) {
            //Hot-Red,Warm-Green,Cold-Purple
            if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Cold")) {
                holder.laycall_type.setBackgroundColor(Color.parseColor("#8B008B"));
                holder.img_appotunity_update.setImageResource(R.drawable.ic_cube);
            } else if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Hot")) {
                holder.laycall_type.setBackgroundColor(Color.parseColor("#EF4F4F"));
                holder.img_appotunity_update.setImageResource(R.drawable.img_hot_call);
            } else if (partialCallListArrayList.get(i).getCallStatus().equalsIgnoreCase("Warm")) {
                holder.laycall_type.setBackgroundColor(Color.parseColor("#26C14B"));
                holder.img_appotunity_update.setImageResource(R.drawable.img_warm_call);
            }
        } else if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("2")) {
            holder.laycall_type.setBackgroundColor(Color.parseColor("#3366FF"));
        } else if (partialCallListArrayList.get(i).getCallType().equalsIgnoreCase("3")) {
            holder.laycall_type.setBackgroundColor(Color.parseColor("#FF1493"));
        }


        holder.txtfirmname.setText(partialCallListArrayList.get(i).getFirmname());
        String Date = partialCallListArrayList.get(i).getActiondatetime();
        String latestRemark = partialCallListArrayList.get(i).getLatestRemark();
        if (!(latestRemark == null || latestRemark.equals("null") || latestRemark.equals(""))) {
            holder.tv_latestremark.setText(" For " + partialCallListArrayList.get(i).getLatestRemark());

        }
        holder.txtactiondatetime.setText(Date);
        if (partialCallListArrayList.get(i).getEmailid().equalsIgnoreCase("")){
            holder.txt_email.setVisibility(View.GONE);
        }else {
            holder.txt_email.setText(partialCallListArrayList.get(i).getEmailid());
        }
        String  NextAction=partialCallListArrayList.get(i).getNextAction();

        if (NextAction.equalsIgnoreCase("Email")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.email));
        }else if (NextAction.equalsIgnoreCase("Telephone")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }else if (NextAction.equalsIgnoreCase("Visit")){
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.visit));
        }else {
            holder.img_nextaction.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_calling));
        }

        String Mobile = partialCallListArrayList.get(i).getMobileno();
        String City = partialCallListArrayList.get(i).getCityname();
        String Contactname = partialCallListArrayList.get(i).getContactName();
        String Product = partialCallListArrayList.get(i).getProduct();
        if (City.equals("null")) {
            City = "";
        }
        if (Product==null||Product.equals("null")) {
            Product = "";
        }
        String appenContact = Contactname + "-" + Mobile;
        if (appenContact.equals("-")) {
            appenContact = "No Contact Available";
        }
        String appendCityProduct = City + "-" + Product;
        if (appendCityProduct.equals("-")) {
            appendCityProduct = "";
        }


        String Concatdata = appendCityProduct + "(" + appenContact + ")";


        holder.tvcall.setText(Concatdata);

        //}


        if (i % 2 == 1) {
            holder.realcolors.setBackgroundColor(Color.parseColor("#DBE8EA"));
        } else {
            holder.realcolors.setBackgroundColor(Color.parseColor("#F1F6F7"));
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
            holder.txt_expvalue.setVisibility(View.VISIBLE);
            holder.txt_expvalue.setText("EV-"+"\u20B9"+ExpValue);
        }


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



    }

    @Override
    public int getItemCount() {
        return partialCallListArrayList.size();
    }

    public class OpportunityHolder extends RecyclerView.ViewHolder {

        LinearLayout realcolors, laycall_type,len_action;
        TextView txtfirmname, txtcityname, tv_latestremark, txtactiondatetime, tvcall, txt_chat,txtaddress,txt_expvalue,txt_email;
        ImageView img_action, img_appotunity_update,img_contact,img_nextaction;


        public OpportunityHolder(View convertView) {
            super(convertView);

            realcolors = (LinearLayout) convertView.findViewById(R.id.realcolors);
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

            img_action.setVisibility(View.GONE);
            img_contact.setVisibility(View.GONE);

            txt_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SubMemberOpportunityActivity)context).ChatClick(getAdapterPosition(),partialCallListArrayList);
                }
            });

            img_appotunity_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SubMemberOpportunityActivity)context).OpportunityUpdate(getAdapterPosition(),partialCallListArrayList);
                }
            });

            tvcall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SubMemberOpportunityActivity)context).tvCallMethd(getAdapterPosition(),partialCallListArrayList);
                }
            });

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


                }
            });

            img_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SubMemberOpportunityActivity)context).ActionClick(getAdapterPosition(),partialCallListArrayList,v);
                }
            });

            img_contact.setOnClickListener(new View.OnClickListener() {
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
