package com.vritti.vwb.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.vritti.vwb.Beans.NotificationBean;
import com.vritti.ekatm.R;
import com.vritti.vwb.vworkbench.NotificationActivity;


public class NotificationAdapter extends BaseAdapter {
    private static ArrayList<NotificationBean> notificationBeanArrayList;
    private LayoutInflater mInflater;
    Context context;

    public NotificationAdapter(Context context1, ArrayList<NotificationBean> lsBirthdayList1) {
        notificationBeanArrayList = lsBirthdayList1;
        mInflater = LayoutInflater.from(context1);
        context = context1;
    }

    @Override
    public int getCount() {
        return notificationBeanArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return notificationBeanArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        int attachmentSize = 0;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vwb_custom_notification_list, null);
            holder = new ViewHolder();

            holder.headNotification = (TextView) convertView.findViewById(R.id.headNotification);
            holder.txtdate = (TextView) convertView.findViewById(R.id.txtdate);
            holder.download_file = convertView.findViewById(R.id.download_file);
            holder.txtName = convertView.findViewById(R.id.txtName);
            holder.details = convertView.findViewById(R.id.details);
            holder.seeMore = convertView.findViewById(R.id.seeMore);
            holder.attachmentCount = convertView.findViewById(R.id.attachmentCount);
            holder.attachmentLayout1 = convertView.findViewById(R.id.attachmentLayout1);
            //holder.attachmentLayout = convertView.findViewById(R.id.attachmentLayout);
            //holder.attachmentName = convertView.findViewById(R.id.attachmentName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.headNotification.setText(notificationBeanArrayList.get(position).getNotifTitle());
      /*  holder.txtdate.setText(Html.fromHtml( "<b>" + notificationBeanArrayList.get(position).getUserName()
                + " </b> " + " " + notificationBeanArrayList.get(position).getAddedDt()));
        */
        holder.txtdate.setText(notificationBeanArrayList.get(position).getAddedDt());
        holder.txtName.setText(notificationBeanArrayList.get(position).getUserName());
        if (notificationBeanArrayList.get(position).getNotifText().equals("")) {
            holder.details.setVisibility(View.GONE);
            holder.seeMore.setVisibility(View.GONE);
            /*holder.txtnotification1.setVisibility(View.GONE);
            holder.Expand.setVisibility(View.GONE);*/
        }
        else {
            holder.details.setVisibility(View.VISIBLE);
            Spanned text=Html.fromHtml(notificationBeanArrayList.get(position).getNotifText());
            holder.details.setText(text);
            if(holder.details.length()>20){
                holder.seeMore.setVisibility(View.VISIBLE);
            }else {
                holder.seeMore.setVisibility(View.GONE);
            }
            //holder.txtnotification1.setVisibility(View.VISIBLE);
           // holder.txtnotification1.setText(notificationBeanArrayList.get(position).getNotifText());
        }
        if (notificationBeanArrayList.get(position).getAttachment().equals("")) {
            /*holder.attachmentLayout1.setVisibility(View.GONE);
            holder.download_file.setVisibility(View.GONE);*/
            attachmentSize = 0;
            holder.attachmentCount.setText("0");
        } else {
            //holder.attachmentLayout.setVisibility(View.GONE);
            holder.download_file.setVisibility(View.VISIBLE);
            String attName = notificationBeanArrayList.get(position).getAttachment();
            attName = attName.replace("!", " , ");
            String[] attachmentGidList = notificationBeanArrayList.get(position).getAttachGuid().split("!");
            attachmentSize = attachmentGidList.length;
            holder.attachmentCount.setText(String.valueOf(attachmentSize));

        }


        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.seeMore.getText().toString().equals("See More")) {
                    holder.details.setMaxLines(10);
                    holder.seeMore.setText("See Less");
                }else {
                    holder.details.setMaxLines(2);
                    holder.seeMore.setText("See More");
                }
            }
        });
        final int finalAttachmentSize = attachmentSize;
        holder.download_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finalAttachmentSize != 0)
                ((NotificationActivity) context).downloadAttachment(position);
                else
                    ((NotificationActivity) context).sendResult1("No Attachment found!");
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView headNotification, txtdate, txtName  ;//,txtnotification1, txtnotification2, attachmentName;
       // ImageButton Expand;
       // LinearLayout lay1, lay2, attachmentLayout;
        RelativeLayout attachmentLayout1;
        LinearLayout download_file;
        TextView details ,seeMore , attachmentCount;

    }
}
