package com.vritti.crmlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CustomerDetailBean;

public class AdapterCustmerDetail extends BaseAdapter {
    ArrayList<CustomerDetailBean> custArrayList;
    LayoutInflater mInflater;
    Context context;
    ArrayList<CustomerDetailBean> arraylist;

    public AdapterCustmerDetail(Context context1, ArrayList<CustomerDetailBean> chatUserArrayList) {
        this.custArrayList = chatUserArrayList;
        mInflater = LayoutInflater.from(context1);
        context = context1;
        this.arraylist = new ArrayList<CustomerDetailBean>();
        this.arraylist.addAll(chatUserArrayList);

    }

    @Override
    public int getCount() {
        return custArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return custArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.crm_item_cust_detail, null);
            holder = new ViewHolder();
            holder.txt_Act_name = (TextView) convertView.findViewById(R.id.txt_act_name);
            holder.txt_city = (TextView) convertView.findViewById(R.id.txt_city);
            holder.txt_startdate = (TextView) convertView.findViewById(R.id.txt_startdate);
            holder.txt_enddate = (TextView) convertView.findViewById(R.id.txt_enddate);
            holder.txt_expcomdate = (TextView) convertView.findViewById(R.id.txt_expcompletiondate);
            holder.txt_assingedby = (TextView) convertView.findViewById(R.id.txt_assignedby);
            holder.getTxt_assingedto = (TextView) convertView.findViewById(R.id.txt_assignedto);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_Act_name.setText(custArrayList.get(position).getActivityName());
        holder.txt_city.setText(custArrayList.get(position).getCity());
        String startdatelong = custArrayList.get(position).getStartDate();
        String enddatelong = custArrayList.get(position).getEndDate();
        String expctedcompdatelong = custArrayList.get(position).getExpectedComplete_Date();
        String startdate = getDateAdded(startdatelong);
        String enddate = getDateAdded(enddatelong);
        String expcom = getDateAdded(expctedcompdatelong);

        holder.txt_startdate.setText(startdate);
        holder.txt_enddate.setText(expcom);
        holder.txt_expcomdate.setText(expcom);

        String usrid = custArrayList.get(position).getUserMasterId();
        //String usrname = getUserName(context, usrid);
        String Issuedid = custArrayList.get(position).getIssuedTo();
       // String IssuedtoName = getUserName(context, Issuedid);
        holder.txt_assingedby.setText(usrid);
        holder.getTxt_assingedto.setText(Issuedid);
        return convertView;
    }

    public ArrayList<CustomerDetailBean> filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        custArrayList.clear();
        if (charText.length() == 0) {
            custArrayList.addAll(arraylist);
        } else {
            for (CustomerDetailBean wp : arraylist) {
                if (wp.getConsigneeName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    custArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
        return custArrayList;
    }


    static class ViewHolder {
        TextView txt_Act_name, txt_city, txt_startdate, txt_enddate, txt_expcomdate, txt_assingedby, getTxt_assingedto;

    }

    private String getDateAdded(String data) {
        String endDate = "";
        if (data.equalsIgnoreCase("") || data.equalsIgnoreCase(null)
                || data.equalsIgnoreCase("Null")
                || data.equalsIgnoreCase("null")
                || data == null) {
            endDate = "";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
            long Etime = Long.parseLong(EndDresults);
            Date EndDate = new Date(Etime);
            endDate = sdf.format(EndDate);
        }

        return endDate;
    }

   /* private String getUserName(Context mContext, String id) {
        String data = "";
        try {
            SQLiteDatabase sql = db.getWritableDatabase();
            String query = "SELECT * FROM '" + db.TABLE_CHAT_USER_LIST + "' WHERE UserMasterId='" + id + "'";

            Cursor cur = sql.rawQuery(query, null);
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                do {
                    data = cur.getString(cur.getColumnIndex("UserName"));

                } while (cur.moveToNext());
            } else {
                data = id;
            }

        } catch (Exception e) {
            e.printStackTrace();
            data = id;
        }
        return data;
    }*/

    private String getDateEnd(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String EndDresults = data.substring(data.indexOf("(") + 1, data.lastIndexOf(")"));
        long Etime = Long.parseLong(EndDresults);
        Date EndDate = new Date(Etime);
        String endDate = sdf.format(EndDate);
        return endDate;
    }
}
