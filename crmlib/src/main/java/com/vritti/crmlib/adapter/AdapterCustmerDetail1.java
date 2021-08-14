package com.vritti.crmlib.adapter;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vritti.crmlib.Interface.OnLoadMoreListener;
import com.vritti.crmlib.R;
import com.vritti.crmlib.bean.CustomerDetailBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;



public class AdapterCustmerDetail1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<CustomerDetailBean> custArrayList;
    LayoutInflater mInflater;
    ArrayList<CustomerDetailBean> arraylist;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public AdapterCustmerDetail1(RecyclerView recyclerView, ArrayList<CustomerDetailBean> chatUserArrayList, Activity activity) {
        this.custArrayList = chatUserArrayList;
        this.activity = activity;
        this.arraylist = new ArrayList<CustomerDetailBean>();
        this.arraylist.addAll(chatUserArrayList);

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        return custArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return custArrayList == null ? 0 : custArrayList.size();
    }

    public void setLoaded() {
        isLoading = false;
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.crm_item_cust_detail, parent, false);
            return new UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.crm_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof UserViewHolder) {
            CustomerDetailBean detailBean = custArrayList.get(position);
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            userViewHolder.txt_Act_name.setText(detailBean.getActivityName());
            userViewHolder.txt_city.setText(detailBean.getCity());
            String startdatelong = detailBean.getStartDate();
            String enddatelong = detailBean.getEndDate();
            String expctedcompdatelong = detailBean.getExpectedComplete_Date();
            String startdate = getDateAdded(startdatelong);
            String enddate = getDateAdded(enddatelong);
            String expcom = getDateAdded(expctedcompdatelong);

            userViewHolder.txt_startdate.setText(startdate);
            userViewHolder.txt_enddate.setText(enddate);
            userViewHolder.txt_expcomdate.setText(expcom);

            String usrid = detailBean.getUserMasterId();
          ;
            String Issuedid = detailBean.getIssuedTo();

            userViewHolder.txt_assingedby.setText(usrid);
            userViewHolder.getTxt_assingedto.setText(Issuedid);
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    // "Loading item" ViewHolder
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    // "Normal item" ViewHolder
    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_Act_name, txt_city, txt_startdate, txt_enddate, txt_expcomdate, txt_assingedby, getTxt_assingedto;

        public UserViewHolder(View convertView) {
            super(convertView);


            txt_Act_name = (TextView) convertView.findViewById(R.id.txt_act_name);
            txt_city = (TextView) convertView.findViewById(R.id.txt_city);
            txt_startdate = (TextView) convertView.findViewById(R.id.txt_startdate);
            txt_enddate = (TextView) convertView.findViewById(R.id.txt_enddate);
            txt_expcomdate = (TextView) convertView.findViewById(R.id.txt_expcompletiondate);
            txt_assingedby = (TextView) convertView.findViewById(R.id.txt_assignedby);
            getTxt_assingedto = (TextView) convertView.findViewById(R.id.txt_assignedto);
        }
    }

    static class ViewHolder {


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

    /*private String getUserName(Context mContext, String id) {
        String data = "";
        try {
            DatabaseHandler db = new DatabaseHandler(mContext);
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
