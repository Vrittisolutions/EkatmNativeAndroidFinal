package com.vritti.vwb.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.vritti.AlfaLavaModule.activity.grn.GRNPOST;
import com.vritti.AlfaLavaModule.bean.Packet;
import com.vritti.AlfaLavaModule.bean.PacketListDetail;
import com.vritti.AlfaLavaModule.bean.PickListDetail;
import com.vritti.AlfaLavaModule.bean.PutAwaysBean;
import com.vritti.AlfaLavaModule.bean.SecondaryBox;
import com.vritti.chat.bean.ChatGroup;
import com.vritti.chat.bean.ChatGroupJson;
import com.vritti.chat.bean.ChatMessage;
import com.vritti.chat.bean.ChatUser;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.expensemanagement.ExpenseData;
import com.vritti.vwb.Beans.ActivityBean;
import com.vritti.vwb.Beans.AffectedCustomer;
import com.vritti.vwb.Beans.BirthdayBean;
import com.vritti.vwb.Beans.ClaimNotification;
import com.vritti.vwb.Beans.Customer;
import com.vritti.vwb.Beans.Datasheet;
import com.vritti.vwb.Beans.GPSLocationTimeBean;
import com.vritti.vwb.Beans.MaterialAddBean;
import com.vritti.vwb.Beans.MyWorkspaceBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommonFunction {
    String CompanyURL, EnvMasterId = "", LoginId = "", Password = "", PlantMasterId = "";
    Utility ut;
   static DatabaseHandlers db;
    Context context;

    public CommonFunction(Context context) {

        ut = new Utility();
        this.context = context;
        String settingKey = ut.getSharedPreference_SettingKey(context);
        String dabasename = ut.getValue(context, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(context, dabasename);
        CompanyURL = ut.getValue(context, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(context, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        LoginId = ut.getValue(context, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(context, WebUrlClass.GET_PSW_KEY, settingKey);
        PlantMasterId = ut.getValue(context, WebUrlClass.GET_PlantID_KEY, settingKey);
    }

    public int getGetItemcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_GetItemList;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getPIGetItemcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_GetItemListPI;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public void DeleteAllRecord(String TABLENAME) {
        SQLiteDatabase sql = db.getWritableDatabase();

        Cursor cursor = sql.rawQuery("DELETE FROM '" + TABLENAME + "'", null);
        int i = cursor.getCount();
        Log.e("Deleted table count"," "+i);

    }

    public int getActivityMasterCount() {
        SQLiteDatabase
                sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_ACTIVITYMASTER;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getMYWORKSPACE() {
        SQLiteDatabase
                sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_MYWORKSPACE;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getActivityMasterCount_Paging() {
        SQLiteDatabase
                sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_ACTIVITYMASTER_PAGING;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getBranchNameCount() {
        SQLiteDatabase
                sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_BRANCH_NAME;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public int getSuppliercount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_SUPPLIER;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public boolean addcontactdetails(Customer customer){
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ContactPersonName",customer.getMobile());
        cv.put("EntityContactInfoId",customer.getEntityContactInfoId());
        cv.put("ClientId",customer.getClient_id());

        long a = sql.insert(db.TABLE_CLIENTCONTACT_DETAILS, null, cv);

        Log.e("gpsrecord", "" + a);

        return true;
    }

    public int getSupplierPlantcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_SUPPLIER_PLANT
                ;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getWorkspaceList() {
        SQLiteDatabase
                sql = db.getWritableDatabase();
        String countQuery = "SELECT  * FROM " + db.TABLE_WORKSPACE_LIST;
        int count = 0;
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void AddClient(Customer customer){
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ClientName",customer.getClient_name());
        cv.put("ClientId",customer.getClient_id());

        long a = sql.insert(db.TABLE_CLIENTS, null, cv);

    }
    /*public boolean addcontactdetails(Customer customer){
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ContactPersonName",customer.getMobile());
        cv.put("EntityContactInfoId",customer.getEntityContactInfoId());
        cv.put("ClientId",customer.getClient_id());

        long a = sql.insert(db.TABLE_CLIENTCONTACT_DETAILS, null, cv);

        Log.e("gpsrecord", "" + a);

        return true;
    }*/


    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            //redraw the container layout.
            // container.requestLayout();

            return true;

        } else {
            return false;
        }

    }

    public Boolean isPresentToLocal(Context mContext, String TABLENAME) {
        SQLiteDatabase sql = null;
        Cursor c = null;

        try {

           sql = db.getWritableDatabase();
            c = sql.rawQuery("SELECT * FROM " + TABLENAME, null);
            if (c.getCount() > 0) {
                c.close();
                //sql.close();
                return true;
            } else {
                c.close();
               // sql.close();
                return false;
            }
        } catch (Exception e) {
            c.close();
            //sql.close();
            return false;
        }
    }

   /* public int getNotification() {
        String countQuery = "SELECT  * FROM ";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }*/

    public String[] getAllUserList(String searchTerm) {
        String[] list = null;
        SQLiteDatabase Sql = db.getReadableDatabase();
        String sql = "";
        sql += "SELECT * FROM " + db.TABLE_ALL_MEMBERS;
        sql += " WHERE UserName LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY UserName";
        sql += " LIMIT 0,5";
        Cursor cur = Sql.rawQuery(sql, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            list = new String[cur.getCount()];
            do {
                list[x] = cur.getString(cur.getColumnIndex("UserName"));
                String aa = cur.getString(cur.getColumnIndex("UserMasterId"));
                x++;

            } while (cur.moveToNext());
        }
        return list;
    }

    public int check_setup() {
        String countQuery = "SELECT * FROM  " + db.TABLE_Setup_TICKET_UPDATION;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    /* public String[] getAllMembers() {

      String[] ls = null;
      String query = "SELECT * FROM " + TABLE_ALL_MEMBERS;
      SQLiteDatabase sql = this.getReadableDatabase();
      Cursor cur = sql.rawQuery(query, null);
      if (cur.getCount() > 0) {
      cur.moveToFirst();
      ls = new String[cur.getCount()];
      for (int i = 0; i < cur.getCount(); i++) {
        ls[i] = cur.getString(cur.getColumnIndex("UserName"));
      }

      }
      return ls;

    }*/

    public void adduserdatagps(GPSLocationTimeBean Tb) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GPSID", Tb.getGPSID());
        values.put("MobileNo", Tb.getMobileNo());
        values.put("latitude", Tb.getLatitude());
        values.put("longitude", Tb.getLongitude());
        values.put("locationName", Tb.getLocationName());
        values.put("AddedDt", Tb.getAddedDt());
        values.put("num", Tb.getNum());
        long a = sql.insert(db.TABLE_GPSRECORDS, null, values);
        Log.e("gpsrecord", "" + a);
    }

    public long addofflinedata(String url, String parameter,
                               int methodtype, String remark, String op) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("linkurl", url);
        values.put("parameter", parameter);
        values.put("methodtype", methodtype);
        values.put("remark", remark);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date date = new Date();
        String currentdate = format.format(date);
        values.put("AddedDt", currentdate);
        values.put("output", op);
        values.put("isUploaded", WebUrlClass.FlagisUploadedFalse);
        values.put("AttemptCount", 0);
        values.put("AttachmentPath", "");
        values.put("AttachmentFileName", "");
        long a = sql.insert(db.TABLE_DATA_OFFLINE, null, values);
        String cnt = a + "";
        return a;
    }

    public void addTicketUpdationMaterialadd(MaterialAddBean Tb) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ActivityID", Tb.getActivityID());
        values.put("warehouse", Tb.getWarehouse());
        values.put("warehouseid", Tb.getWarehouseid());
        values.put("location", Tb.getLocation());
        values.put("locationid", Tb.getLocationid());
        values.put("item_code", Tb.getItem_code());
        values.put("Itemplantid", Tb.getItemplantid());
        values.put("discription", Tb.getDiscription());
        values.put("quant", Tb.getQuant());
        values.put("fieldname", Tb.getFieldname());
        values.put("totalquant", Tb.getTotalquant());
        values.put("cosumedquant", Tb.getCosumedquant());
        values.put("remark", Tb.getRemark());
        values.put("UOM", Tb.getUOM());
        values.put("IsUpload", Tb.getIsUpload());
        long a = sql.insert(db.TABLE_TICKET_UPDATE_MATERIAL_Add, null, values);
        Log.e("matadd", "" + a);
    }

    public ArrayList<MaterialAddBean> getadddata(String ActivityID) {
        ArrayList<MaterialAddBean> data = new ArrayList<MaterialAddBean>();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_TICKET_UPDATE_MATERIAL_Add + " where ActivityID='"
                        + ActivityID + "'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                MaterialAddBean bean = new MaterialAddBean();
                bean.setMatAddID(c.getString(c.getColumnIndex("IDMAT")));
                bean.setActivityID(c.getString(c.getColumnIndex("ActivityID")));
                bean.setWarehouse(c.getString(c.getColumnIndex("warehouse")));
                bean.setWarehouseid(c.getString(c.getColumnIndex("warehouseid")));
                bean.setLocation(c.getString(c.getColumnIndex("location")));
                bean.setLocationid(c.getString(c.getColumnIndex("locationid")));
                bean.setItem_code(c.getString(c.getColumnIndex("item_code")));
                bean.setItemplantid(c.getString(c.getColumnIndex("Itemplantid")));
                bean.setDiscription(c.getString(c.getColumnIndex("discription")));
                bean.setQuant(c.getString(c.getColumnIndex("quant")));
                bean.setTotalquant(c.getString(c.getColumnIndex("fieldname")));
                bean.setCosumedquant(c.getString(c.getColumnIndex("totalquant")));
                bean.setFieldname(c.getString(c.getColumnIndex("cosumedquant")));
                bean.setRemark(c.getString(c.getColumnIndex("remark")));
                bean.setUOM(c.getString(c.getColumnIndex("UOM")));

                bean.setIsUpload(c.getString(c.getColumnIndex("IsUpload")));
                data.add(bean);
            } while (c.moveToNext());
        }
        return data;

    }

    public ArrayList<MaterialAddBean> getadddatanotuploaded(String ActivityID) {
        ArrayList<MaterialAddBean> data = new ArrayList<MaterialAddBean>();
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery(
                "SELECT * FROM " + db.TABLE_TICKET_UPDATE_MATERIAL_Add + " where ActivityID='"
                        + ActivityID + "' AND IsUpload='N'", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                MaterialAddBean bean = new MaterialAddBean();
                bean.setMatAddID(c.getString(c.getColumnIndex("IDMAT")));
                bean.setActivityID(c.getString(c.getColumnIndex("ActivityID")));
                bean.setWarehouse(c.getString(c.getColumnIndex("warehouse")));
                bean.setWarehouseid(c.getString(c.getColumnIndex("warehouseid")));
                bean.setLocation(c.getString(c.getColumnIndex("location")));
                bean.setLocationid(c.getString(c.getColumnIndex("locationid")));
                bean.setItem_code(c.getString(c.getColumnIndex("item_code")));
                bean.setItemplantid(c.getString(c.getColumnIndex("Itemplantid")));
                bean.setDiscription(c.getString(c.getColumnIndex("discription")));
                bean.setQuant(c.getString(c.getColumnIndex("quant")));
                bean.setTotalquant(c.getString(c.getColumnIndex("fieldname")));
                bean.setCosumedquant(c.getString(c.getColumnIndex("totalquant")));
                bean.setFieldname(c.getString(c.getColumnIndex("cosumedquant")));
                bean.setRemark(c.getString(c.getColumnIndex("remark")));
                bean.setUOM(c.getString(c.getColumnIndex("UOM")));
                bean.setIsUpload(c.getString(c.getColumnIndex("IsUpload")));
                data.add(bean);
            } while (c.moveToNext());
        }
        return data;

    }

    public String getreportingmobno(String reporteesname1) {
        String mobile = "";
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c = sql.rawQuery(
                "SELECT Mobile FROM " + db.TABLE_GPS_REPORTINGTO + " where UserName='"
                        + reporteesname1 + "' ORDER BY UserName ASC", null);
        if (c.moveToFirst()) {
            do {
                mobile = c.getString(0);
            } while (c.moveToNext());
        }
        return mobile;
    }

    public int cntAllMember() {
        int cnt = 0;
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM " + db.TABLE_ALL_MEMBERS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            cnt = cur.getCount();
        }
        return cnt;
    }


    public String GetUsermaterID(String UserName) {
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM '" + db.TABLE_ALL_MEMBERS + "' WHERE UserName='" + UserName + "'";
        String data = "";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("UserMasterId"));

            } while (cur.moveToNext());
        }
        return data;
    }

    public String GetUserName(String UserID) {
        SQLiteDatabase sql = db.getWritableDatabase();
        String query = "SELECT * FROM '" + db.TABLE_ALL_MEMBERS + "' WHERE UserMasterId='" + UserID + "'";
        String data = "";
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {

            cur.moveToFirst();
            do {
                data = cur.getString(cur.getColumnIndex("UserName"));

            } while (cur.moveToNext());
        }
        return data;
    }

    public String[] getAllMembers() {
        SQLiteDatabase sql = db.getWritableDatabase();
        String[] UnChkUser_list = null;

        String query = "SELECT * FROM " + db.TABLE_ALL_MEMBERS;
        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            int x = 0;
            UnChkUser_list = new String[cur.getCount()];
            cur.moveToFirst();
            do {
                UnChkUser_list[x] = cur.getString(cur.getColumnIndex("UserName"));
                x++;
            } while (cur.moveToNext());
        }

        return UnChkUser_list;
    }

    public int check_GPSrecords_Add(String dt) {
        String countQuery = "SELECT distinct AddedDt FROM  " + db.TABLE_ADD_GPSRECORDS + " WHERE AddedDt='"
                + dt + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int check_gps_reportingto() {
        String countQuery = "SELECT * FROM " + db.TABLE_MYTEAM + " ORDER BY UserName ASC";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_claim_approver() {
        String countQuery = "SELECT * FROM " + db.TABLE_CLAIM_APPROVER;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_emprequest_claim_approver() {
        String countQuery = "SELECT * FROM " + db.TABLE_EMPLOYEE_REQUEST_APPROVER;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_gpsrecorts(String reporteeMobno) {
        String countQuery = "SELECT * FROM GPSrecords WHERE MobileNo='" + reporteeMobno + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addGps(String MobileNo, String latitude, String longitude,
                       String locationName, String GPSaddedDate, String UserMasterID, String isUploaded) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MobileNo", MobileNo);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("locationName", locationName);
        values.put("GpsAddedDt", GPSaddedDate);
        values.put("UserMasterID", UserMasterID);
        values.put("isUploaded", isUploaded);
        long a = sql.insert(db.TABLE_ADD_GPSRECORDS, null, values);
        Log.e("data", "insert" + a);
       // sql.close();
    }


   /* public void AddAppEnvMaster(String AppEnvMasterId) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AppEnvMasterId", AppEnvMasterId);
        long a = sql.insert(db.TABLE_APPENVMASTER, null, cv);
    }*/

    public void AddPlantMaster(String PlantMasterId, String PlantName) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PlantMasterId", PlantMasterId);
        cv.put("PlantName", PlantName);
        long a = sql.insert(db.TABLE_PLANTMASTER, null, cv);
    }

    public void AddBirthday(BirthdayBean bean1) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("DOB", bean1.getDOB());
        cv.put("DOJ", bean1.getDOJ());
        cv.put("DtDay", bean1.getDtDay());
        cv.put("Email", bean1.getEmail());
        cv.put("ImagePath", bean1.getImagePath());
        cv.put("Mobile", bean1.getMobile());
        cv.put("Title", bean1.getTitle());
        cv.put("UserLoginId", bean1.getUserLoginId());
        cv.put("UserMasterID", bean1.getUserMasterID());
        cv.put("UserName", bean1.getUserName());
        long a = sql.insert(db.TABLE_BIRTHDAY, null, cv);

    }

    public void AddMyworkspace(MyWorkspaceBean bean) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("OnTime", bean.getOnTime());
        cv.put("OnTimePerc", bean.getOnTimePerc());
        cv.put("OpenActivities", bean.getOpenActivities());
        cv.put("ProjectId", bean.getProjectId());
        cv.put("ProjectName", bean.getProjectName());
        long a = sql.insert(db.TABLE_MYWORKSPACE, null, cv);

    }
    //Nilesh Code

    public void AddAffectedCustomerInfo(AffectedCustomer affectedCustomer) {

// ModifiedDt TEXT,Outcome TEXT,UserName TEXT,OutcomeCode TEXT,LatestRemark TEXT)";


        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ActivityName", affectedCustomer.getActivityName());
        cv.put("ActivityCode", affectedCustomer.getActivityCode());
        cv.put("CustTicketNo", affectedCustomer.getCustTicketNo());
        cv.put("CustVendorName", affectedCustomer.getCustVendorName());
        cv.put("ReportedBy", affectedCustomer.getReportedBy());
        long a = sql.insert(db.TABLE_AFFECTED_CUSTOMER_INFO, null, cv);

    }

    public int getRoutefromcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_GetRouteFrom;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getbindcategorycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_GetBindCategory;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getroutefromcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_GetRouteFrom;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getChatusercount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CHAT_USER_LIST;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getChatRoomNamecount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CHAT_CHATROOM_GROUP_LIST;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getChatRoomUsercount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CHAT_CHATROOM_MEMBER_LIST;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void AddGroupMember(ChatUser chatUser) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ChatRoomId", chatUser.getChatRoomId());
        cv.put("ChatRoomName", chatUser.getChatroom());
        cv.put("ChatRoomStatus", chatUser.getStatus());
        cv.put("StartTime", chatUser.getStartTime());
        cv.put("Creator", chatUser.getCreater());
        cv.put("ParticipantId", chatUser.getParticipantId());
        cv.put("ChatSourceId", chatUser.getChatSourceId());
        cv.put("AddedBy", chatUser.getAddedBy());
        cv.put("ParticipantName", chatUser.getParticipantName());
        cv.put("Message", chatUser.getMessage());
        cv.put("UserMasterId", chatUser.getUserMasterId());
        cv.put("Count", chatUser.getCount());
        cv.put("ChatType",chatUser.getChatType());
        cv.put("ImagePath",chatUser.getImagePath());
        //long a = sql.insert(TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);
        // Log.e("",""+a);
        long a = sql.insert(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);
        Log.e("",""+a);

        /*if (CheckifRecordPresent(TABLE_CHAT_CHATROOM_MEMBER_LIST, "ParticipantId","ParticipantName", chatUser.getParticipantId(),chatUser.getParticipantName())) {
            long a = sql.insert(TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);
            Log.e("", "" + a);
        }*/


    }


    public Boolean CheckifRecordPresent(String Table, String Column, String Column1, String Value, String Value1) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "' AND " + Column1 + "='" + Value1 + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {

            return false;
        }
    }



    public Boolean CheckChatroomRecordPresent(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void AddGroupMessage(ChatMessage chatMessage) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserMasterId", chatMessage.getUserMasterId());
        cv.put("ChatRoomId", chatMessage.getChatRoomId());
        cv.put("Message", chatMessage.getMessage());
        cv.put("MessageDate", chatMessage.getMessageDate());
        cv.put("UserName", chatMessage.getUsername());
        cv.put("Status", chatMessage.getStatus());
        cv.put("MessageType", chatMessage.getMessageType());
        cv.put("MessageId", chatMessage.getMessageId());
        cv.put("Attachment", chatMessage.getAttachment());
        cv.put("IsDownloaded", chatMessage.getIsDownloaded());
        cv.put("ChatRoomName", "");

        long a = sql.insert(db.TABLE_CHAT_GROUP_MESSAGE, null, cv);
        // long a = sql.insertWithOnConflict(TABLE_CHAT_GROUP_MESSAGES, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("", "" + a);

    }


    public void AddGrouMessageJson(ChatGroupJson chatGroupJson) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Finaljson", chatGroupJson.getFinalJsonGroup());
        cv.put("ID", chatGroupJson.getMessage_id());
        long a = sql.insertWithOnConflict(db.TABLE_GROUP_JSON, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("", "" + a);

    }

    public int getChatmessagecount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CHAT_GROUP_MESSAGE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getActivityGroupList() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_ActivityGetGroupList;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getAllMember() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_ALL_MEMBERS;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getWorkspacecount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_MYWORK;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void AddClaimPayment(ClaimNotification claimNotification) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Data", claimNotification.getMessage());
        long a = sql.insert(db.TABLE_CLAIM_NOTIFICATION, null, cv);
        // long a = sql.insertWithOnConflict(TABLE_CHAT_GROUP_MESSAGES, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("", "" + a);

    }

    public void AddGroupList(ChatGroup chatGroup) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ChatRoomId", chatGroup.getChatRoomId());
        cv.put("ChatRoomName", chatGroup.getChatroom());
        cv.put("ChatRoomStatus", chatGroup.getStatus());
        cv.put("StartTime", chatGroup.getStartTime());
        cv.put("Creator", chatGroup.getCreater());
        cv.put("AddedBy", chatGroup.getAddedBy());
        cv.put("UserMasterId", chatGroup.getUserMasterId());
        cv.put("Count", chatGroup.getCount());
        cv.put("ChatSourceId",chatGroup.getChatSourceId());
        cv.put("ChatType",chatGroup.getChatType());
        cv.put("ChatMessage",chatGroup.getChatMessage());

        long a = sql.insert(db.TABLE_CHAT_CHATROOM_GROUP_LIST, null, cv);
        Log.e("", "" + a);

    }

    public void UpdateGroupList(ChatGroup chatGroup) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ChatRoomId", chatGroup.getChatRoomId());
        cv.put("ChatRoomName", chatGroup.getChatroom());
        cv.put("ChatRoomStatus", chatGroup.getStatus());
        cv.put("StartTime", chatGroup.getStartTime());
        cv.put("Creator", chatGroup.getCreater());
        cv.put("AddedBy", chatGroup.getAddedBy());
        cv.put("UserMasterId", chatGroup.getUserMasterId());
        cv.put("Count", chatGroup.getCount());
        cv.put("ChatSourceId",chatGroup.getChatSourceId());
        cv.put("ChatType",chatGroup.getChatType());
        cv.put("ChatMessage",chatGroup.getChatMessage());
        long a = sql.update(db.TABLE_CHAT_CHATROOM_GROUP_LIST, cv, "ChatRoomId=?", new String[]{chatGroup.getChatRoomId()});

        // long a = sql.insert(TABLE_CHAT_CHATROOM_GROUP_LIST, null, cv);
        Log.e("", "" + a);

    }

    public int getUserlistcount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CHAT_USER_LIST;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public void deleteDatasheetSelection(int selectionid, String selectionText,
                                         String selectionValue) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM " + db.TABLE_DATASHEET_SELECTION);
        // sql.delete(TABLE_Datasheet, "OrderId" + " = ?",
        // new String[] { String.valueOf(order.getId()) });
      //  sql.close();
    }


    public void addDatasheetSelection(int i, String selectionText, String selectionValue, String FormId, String FKQuesId) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("DId", datasheet.getId());
        values.put("selectionid", i);
        values.put("selectionText", selectionText);
        values.put("selectionValue", selectionValue);
        values.put("FormId", FormId);
        values.put("FKQuesId", FKQuesId);

//FormId TEXT,FKQuesId        // Inserting Row
        long a = sql.insert(db.TABLE_DATASHEET_SELECTION, null, values);
        Log.d("test", " values " + values);

    }

    public void addGpsNotification(String FromUserMAster, String toUsermaster, String date, String msg) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("DId", datasheet.getId());
        values.put("ToUsermatserID", toUsermaster);
        values.put("FromUserMasterID", FromUserMAster);
        values.put("Date", date);
        values.put("MSG", msg);
        values.put("isUploaded", "No");
        long a = sql.insert(db.TABLE_GPS_SEND_NOTIFICATION, null, values);
        Log.d("test", " values " + values);
        sql.close();
    }


    public void addDatasheetANS(Datasheet datasheet) {
        SQLiteDatabase sql = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        // values.put("DId", datasheet.getId());
        values.put("PKCssFormsQuesID", datasheet.getPKCssFormsQuesID());
        values.put("FKQuesId", datasheet.getFKQuesId());
        values.put("ResponseByCustomer", datasheet.getExpectedResponse());
        values.put("SelectionValue", datasheet.getSelectionValue());
        values.put("Flag", datasheet.getFlag());
        values.put("FormId", datasheet.getFormId());

        // Inserting Row
        sql.insert(db.TABLE_DATASHEET_ANS, null, values);

    }

    public void RefreshDefault() {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SettingName", "Rtime");
        values.put("SettingValue", "60");
        sql.insert(db.TABLE_SETTING, null, values);

    }

    public int check_GPSrecords(String dt) {
        String countQuery = "SELECT distinct AddedDt FROM  " + db.TABLE_GPSRECORDS + " WHERE AddedDt='"
                + dt + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_DatasheetQueList(String FormId) {
        String countQuery = "SELECT * FROM  " + db.TABLE_DATASHEET_DATA + " WHERE FormId='"
                + FormId + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_LeaveMode() {
        String countQuery = "SELECT * FROM  " + db.TABLE_APP_DOC_INFO;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int check_ModeOFJourny() {
        String countQuery = "SELECT * FROM  " + db.TABLE_MODE_OF_JOURNY;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_datasheetSelection(String FormId, String FKQuesId) {
        String countQuery = "SELECT * FROM  " + db.TABLE_DATASHEET_SELECTION + " WHERE FormId='" + FormId + "' AND FKQuesId='" + FKQuesId + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getStatecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_REASON_OUTAGE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addNotification(String notification_desc, String type) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("notification_desc", notification_desc);
        cv.put("type", type);
        //long a = sql.insert(TABLE_NOTIFICATION, null, cv);
    }

    public String[] getProjectMembers(String ProjectId, String searchItem) {
        SQLiteDatabase sql = db.getWritableDatabase();
        String[] ChkUser_list = null;
        String query = "SELECT * FROM " + db.TABLE_PROJECT_MEMBERS + " WHERE prjMstId='" + ProjectId + "' AND UserName LIKE '%" + searchItem + "%' ORDER BY UserName LIMIT 0,5";


        Cursor cur = sql.rawQuery(query, null);
        if (cur.getCount() > 0) {
            ChkUser_list = new String[cur.getCount()];
            int x = 0;
            cur.moveToFirst();
            do {
                ChkUser_list[x] = cur.getString(cur.getColumnIndex("UserName"));
                x++;
            } while (cur.moveToNext());
        }

        return ChkUser_list;

    }

    public int getcountryecount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_Country_INVEN;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public int getCitycount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_City_INV;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCurrencycount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_Currency;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public int getEntityTyptcount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_EntityType;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getTeamMebercount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_ALL_MEMBERS;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public void AddExpenseDetails(ExpenseData expenseData) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ExpRecordId",expenseData.getExpRecordId());
        contentValues.put("UserMasterId",expenseData.getUserMasterId());
        contentValues.put("cat_name",expenseData.getCat_name());
        contentValues.put("ExpType",expenseData.getExpType());
        contentValues.put("Amount",expenseData.getAmount());
        contentValues.put("PaymentMode",expenseData.getPaymentMode());
        contentValues.put("TravelMode",expenseData.getTravelMode());
        contentValues.put("ExpDate ",expenseData.getExpDate());
        contentValues.put("FromLocation",expenseData.getFromLocation());
        contentValues.put("ToLocation ",expenseData.getToLocation());
        contentValues.put("attachment",expenseData.getAttachment());
        contentValues.put("Remark",expenseData.getRemark());
        contentValues.put("Distance",expenseData.getDistance());
        contentValues.put("VehicleType",expenseData.getVehicleType());
        contentValues.put("LinkTo",expenseData.getLinkTo());
        contentValues.put("LinkId",expenseData.getLinkId());
        contentValues.put("Path",expenseData.getPath());
        long a = sql.insert(db.TABLE_EXPENSE, null, contentValues);
        Log.e("data", String.valueOf(a));
    }
    public void UpdateExpenseDetails(ExpenseData expenseData, String exp_id) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ExpRecordId",expenseData.getExpRecordId());
        contentValues.put("UserMasterId",expenseData.getUserMasterId());
        contentValues.put("cat_name",expenseData.getCat_name());
        contentValues.put("ExpType",expenseData.getExpType());
        contentValues.put("Amount",expenseData.getAmount());
        contentValues.put("PaymentMode",expenseData.getPaymentMode());
        contentValues.put("TravelMode",expenseData.getTravelMode());
        contentValues.put("ExpDate ",expenseData.getExpDate());
        contentValues.put("FromLocation",expenseData.getFromLocation());
        contentValues.put("ToLocation ",expenseData.getToLocation());
        contentValues.put("attachment",expenseData.getAttachment());
        contentValues.put("Remark",expenseData.getRemark());
        contentValues.put("Distance",expenseData.getDistance());
        contentValues.put("VehicleType",expenseData.getVehicleType());
        contentValues.put("LinkTo",expenseData.getLinkTo());
        contentValues.put("LinkId",expenseData.getLinkId());
        contentValues.put("Path",expenseData.getPath());
        long a = sql.update(db.TABLE_EXPENSE,contentValues, "ExpRecordId=?", new String[]{exp_id});
        Log.e("data", String.valueOf(a));
    }


    /***************************PI Generation Hyva***********************************************/
    public void insertPIData(/*String PIDtlId, String PIHdrId, String ItemPlantId, String LocationMasterID,
                                String Weight,String ActualQty,String AddedBy, String Printed,String TagNo,
                                String Mode,String CountedBy, String TAGDescription, String VerifyBy*/
            JSONObject jsonObject, String username, String ItemCode, String ItemDesc, String LocationCode){

        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        try {
            cv.put("PIDtlId", jsonObject.getString("PIDtlId"));
            cv.put("PIHdrId", jsonObject.getString("PIHdrId"));
            cv.put("ItemPlantId", jsonObject.getString("ItemPlantId"));
            cv.put("LocationMasterID", jsonObject.getString("Location"));
            cv.put("Weight", jsonObject.getString("Weight"));
            cv.put("ActualQty", jsonObject.getString("ActualQty"));
            cv.put("AddedBy", jsonObject.getString("AddedBy"));
            cv.put("Printed", jsonObject.getString("Printed"));
            cv.put("TagNo", jsonObject.getString("TagNo"));
            cv.put("Mode", jsonObject.getString("Mode"));
            cv.put("CountedBy", jsonObject.getString("CountedBy"));
            cv.put("TAGDescription", jsonObject.getString("TAGDescription"));
            cv.put("VerifyBy",username);
            cv.put("ItemCode",ItemCode);
            cv.put("ItemDesc",ItemDesc);
            cv.put("LocationCode",LocationCode);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        long q = sql.insert(DatabaseHandlers.TABLE_PI_GENERATION, null, cv);
        Log.e("q ",String.valueOf(q));

    }

    public void insertUOM(String UOMMasterId, String UOMCode){
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UOMMasterId", UOMMasterId);
        cv.put("UOMCode", UOMCode);

        long q = sql.insert(DatabaseHandlers.TABLE_UOM, null, cv);
        Log.e("q ",String.valueOf(q));
    }

    public void insertLocationPI(String LocationMasterId,String LocationCode,String LocationDesc,String WarehouseMasterId,String AddedBy){
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("LocationMasterId", LocationMasterId);
        cv.put("LocationCode", LocationCode);
        cv.put("LocationDesc", LocationDesc);
        cv.put("WarehouseMasterId", WarehouseMasterId);
        cv.put("AddedBy", AddedBy);

        long q = sql.insert(DatabaseHandlers.TABLE_LOCATION_PI, null, cv);
        Log.e("q ",String.valueOf(q));

    }
    public int getLstInsertedCount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_GetItemList;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public void insertItemMasterData(Context context,String ItemCode, String ItemDesc, String ItemMasterId, String ItemPlantId, String PurUnit,
                                     String SalesUnit, String ConvFactor, String StockUnit, String WareHouseMasterId, String LocationMasterId,
                                     String WarehouseCode, String LocationCode){
        SQLiteDatabase sql = db.getReadableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ItemCode", ItemCode);
        cv.put("ItemDesc", ItemDesc);
        cv.put("ItemMasterId", ItemMasterId);
        cv.put("ItemPlantId", ItemPlantId);
        cv.put("PurUnit", PurUnit);
        cv.put("SalesUnit", SalesUnit);
        cv.put("ConvFactor", ConvFactor);
        cv.put("StockUnit", StockUnit);
        cv.put("WareHouseMasterId", WareHouseMasterId);
        cv.put("LocationMasterId", LocationMasterId);
        cv.put("WarehouseCode", WarehouseCode);
        cv.put("LocationCode", LocationCode);

        long q = sql.insert(DatabaseHandlers.TABLE_GetItemListPI, null, cv);
        Log.e("q ",String.valueOf(q));
        //  showToast(String.valueOf(q),context);
    }

    public void ActivityPaging(ActivityBean activityBean) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsChargable",activityBean.getIsChargable());
        cv.put("ActivityName",activityBean.getActivityName());
        cv.put("AssignedById",activityBean.getAssignedById());
        cv.put("Assigned_By",activityBean.getAssigned_By());
        cv.put("ActivityId",activityBean.getActivityId());
        cv.put("StartDate",activityBean.getStartDate());
        cv.put("EndDate",activityBean.getEndDate());
        cv.put("ExpectedCompleteDate",activityBean.getExpectedCompleteDate());
        cv.put("ExpectedComplete_Date",activityBean.getExpectedComplete_Date());
        cv.put("ModifiedBy",activityBean.getModifiedBy());
        cv.put("Modified_By",activityBean.getModified_By());
        cv.put("FormatStDt",activityBean.getFormatStDt());
//        cv.put("StartDt",activityBean.getStartDt());
  //      cv.put("EndDt",activityBean.getEndDt());
        cv.put("Status",activityBean.getStatus());
        cv.put("ProjectId",activityBean.getProjectID());
        cv.put("PAllowUsrTimeSlotHrs",activityBean.getPAllowUsrTimeSlotHrs());
    //    cv.put("IsActivityMandatory",activityBean.getIsActivityMandatory());
      //  cv.put("IsDelayedActivityAllowed",activityBean.getIsDelayedActivityAllowed());
        cv.put("Cd",activityBean.getCd());
        cv.put("UnitId",activityBean.getUnitId());
        cv.put("PKModuleMastId",activityBean.getPKModuleMastId());
        cv.put("PriorityName",activityBean.getPriorityName());
        cv.put("Colour",activityBean.getColour());
        cv.put("PriorityIndex",activityBean.getPriorityIndex());
        cv.put("TotalHoursBooked",activityBean.getTotalHoursBooked());
        cv.put("AddedDt",activityBean.getAddedDt());
        cv.put("UserMasterId",activityBean.getUserMasterId());
        cv.put("ModifiedDt",activityBean.getModifiedDt());
        cv.put("IsDeleted",activityBean.getIsDeleted());
        cv.put("IsApproved",activityBean.getIsApproved());
        cv.put("IsApproval",activityBean.getIsApproval());
        cv.put("HoursRequired",activityBean.getHoursRequired());
        cv.put("AttachmentName",activityBean.getAttachmentName());
        cv.put("SourceType",activityBean.getSourceType());
        cv.put("SourceId",activityBean.getSourceId());
        cv.put("UnitName",activityBean.getUnitName());
        cv.put("UnitDesc",activityBean.getUnitDesc());
        cv.put("ModuleName",activityBean.getModuleName());
        cv.put("Remarks",activityBean.getRemarks());
        cv.put("ProjectCode",activityBean.getProjectCode());
        cv.put("ProjectName",activityBean.getProjectName());
        cv.put("UserName",activityBean.getUserName());
        cv.put("DeptDesc",activityBean.getDeptDesc());
        cv.put("DeptMasterId",activityBean.getDeptMasterId());
        cv.put("CompletionIntimate",activityBean.getCompletionIntimate());
        cv.put("ActivityCode",activityBean.getActivityCode());
        cv.put("ReassignedBy",activityBean.getReassignedBy());
        cv.put("ReassignedDt",activityBean.getReassignedDt());
        cv.put("ActualCompletionDate",activityBean.getActualCompletionDate());
        cv.put("WarrantyCode",activityBean.getWarrantyCode());
        cv.put("TicketCategory",activityBean.getTicketCategory());
        cv.put("IsEndTime",activityBean.getIsEndTime());
        cv.put("IsCompActPresent",activityBean.getIsCompActPresent());
        cv.put("CompletionActId",activityBean.getCompletionActId());
        cv.put("TktCustReportedBy",activityBean.getTktCustReportedBy());
        cv.put("IsSubActivity",activityBean.getIsSubActivity());
        cv.put("ParentActId",activityBean.getParentActId());
        cv.put("ConsigneeName",activityBean.getConsigneeName());
        cv.put("ContMob",activityBean.getContMob());
        cv.put("ActivityTypeName",activityBean.getActivityTypeName());
        //cv.put("CompActName",activityBean.getCompActName());

        long a = sql.insert(db.TABLE_ACTIVITYMASTER_PAGING, null, cv);
        Log.e("", "" + a);

    }

    /*---------------------------------------------------------------------------*/
    //Alfa laval

    public void Insert_Putaways(PutAwaysBean bean) {
        try {
            SQLiteDatabase SQl = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("GRNNumber", bean.getGRN_Number());
            values.put("GRNHeader", bean.getGRN_Header());
            values.put("SuggPutAwayId", bean.getSuggPutAwayId());
            values.put("GRNDetailId", bean.getGRNDetailId());
            values.put("LocationMasterId", bean.getLocationMasterId());
            values.put("ItemCode", bean.getItemCode());
            values.put("ItemDesc", bean.getItemDesc());
            values.put("LocationCode", bean.getLocationCode());
            values.put("PutAwayQty", bean.getPutAwayQty());
            values.put("DoneFlag", bean.getFlgDone());
            values.put("InsertUpadate", bean.getFlgInsertUpdate());
            long a = SQl.insert(db.TABLE_PUTAWAY, null, values);
            Log.e("Insert TABLE_PUTAWAY", "" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   public void InsertPackOrderDetails(PacketListDetail secondaryBox) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Pack_OrdHdrId", secondaryBox.getPack_OrdHdrId());
        cv.put("Pack_OrdDtlId", secondaryBox.getPack_OrdDtlId());
        cv.put("QtyToPack", secondaryBox.getQtyToPack());
        cv.put("QtyPacked", secondaryBox.getQtyPacked());
        cv.put("Pick_ListHdrId", secondaryBox.getPick_ListHdrId());
        cv.put("Pick_ListDtlId", secondaryBox.getPick_ListDtlId());
        cv.put("ItemMasterId", secondaryBox.getItemMasterId());
        cv.put("ItemCode", secondaryBox.getItemCode());
        cv.put("ItemDesc", secondaryBox.getItemDesc());
        cv.put("SoScheduleId",secondaryBox.getSoScheduleId());
        cv.put("Flag",secondaryBox.getFlag());
        cv.put("PackOrderNo",secondaryBox.getPackOrderNo());

        long a = sql.insert(db.TABLE_SECONDARY_BOX, null, cv);
        Log.e("", "" + a);

    }

   /* Pick_ListHdrId TEXT,PickListNo TEXT," +
            "Pick_ListDtlId TEXT,SoScheduleId TEXT," +
            "ItemMasterId TEXT,QtyPicked TEXT," +
            "QtyToPick TEXT,StockDetailsId TEXT," +
            "Flag TEXT," +
            "QtyPickPosted TEXT*/

    public void InsertPickingOrderDetails(PickListDetail listDetail) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Pick_ListHdrId", listDetail.getPick_ListHdrId());
        cv.put("PickListNo", listDetail.getPickListNo());
        cv.put("Pick_ListDtlId", listDetail.getPick_listDtlId());
       // cv.put("SoScheduleId", listDetail.getSoScheduleId());
        cv.put("ItemMasterId", listDetail.getItemMasterId());
        cv.put("QtyPicked", listDetail.getQtyPicked());
        cv.put("QtyToPick", listDetail.getQtyToPick());
        cv.put("StockDetailsId", listDetail.getStockDetailsId());
        cv.put("ItemCode", listDetail.getItemCode());
        cv.put("ItemDesc", listDetail.getItemDesc());
        cv.put("DONumber",listDetail.getDONumber());
        cv.put("LocationCode",listDetail.getLocationCode());
        cv.put("Flag", listDetail.getFlag());
        cv.put("Pick_listSuggLotId", listDetail.getPick_listSuggLotId());

        long a = sql.insert(db.TABLE_CARTAN_PICKLIST, null, cv);
        Log.e("", "" + a);

    }


    public void InsertPick_ListSuggLot(PickListDetail listDetail) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Pick_ListHdrId", listDetail.getPick_ListHdrId());
        cv.put("PickListNo", listDetail.getPickListNo());
        cv.put("Pick_ListDtlId", listDetail.getPick_ListDtlId());
        cv.put("SoScheduleId", listDetail.getSoScheduleId());
        cv.put("ItemMasterId", listDetail.getItemMasterId());
        cv.put("QtyPicked", listDetail.getQtyPicked());
        cv.put("QtyToPick", listDetail.getQtyToPick());
        cv.put("StockDetailsId", listDetail.getStockDetailsId());
        cv.put("ItemCode", listDetail.getItemCode());
        cv.put("ItemDesc", listDetail.getItemDesc());
        cv.put("DONumber",listDetail.getDONumber());
        cv.put("LocationCode",listDetail.getLocationCode());
        // cv.put("Pick_listSuggLotId", listDetail.getPick_listSuggLotId());
        //cv.put("Pick_listDtlId", listDetail.getPick_listDtlId());
        cv.put("Flag", listDetail.getFlag());

        long a = sql.insert(db.TABLE_CARTAN_PICKLIST, null, cv);
        Log.e("", "" + a);

    }
    public int CheckPicklistCount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CARTAN_PICKLIST;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public Boolean CheckifExpensePresent(String Table, String Column,String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {

            return false;
        }
    }

    public Boolean CheckifRecordPresentForChat(String Table, String Column, String Value) {
        SQLiteDatabase sql = db.getWritableDatabase();
        Cursor c1 = sql.rawQuery("SELECT * FROM " + Table, null);
        Cursor c = sql.rawQuery("SELECT * FROM " + Table + " WHERE " + Column + "='" + Value + "'", null);
        int a1 = c1.getCount();
        int a = c.getCount();
        if (a == 0) {
            return true;
        } else {

            return false;
        }
    }

    public void Insert_GRNPACKETNO(Packet bean) {

        try {
            SQLiteDatabase SQl = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("PacketNo", bean.getPacketNo());
            long a = SQl.insert(db.TABLE_GRN_PACKET, null, values);
            Log.e("Insert TABLE_GRNPACKET", "" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 public void insertConfiguration(String ConfigurationDetailId, String Configuration, String ConfigurationName){
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ConfigurationDetailId", ConfigurationDetailId);
        cv.put("Configuration", Configuration);
        cv.put("ConfigurationName", ConfigurationName);

        long q = sql.insert(DatabaseHandlers.TABLE_CONFIGURATION, null, cv);
        Log.e("q ",String.valueOf(q));

    }

    public void insertCharge(String ChargeMasterId, String ChargeCode, String ChargeDesc){
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ChargeMasterId", ChargeMasterId);
        cv.put("ChargeCode", ChargeCode);
        cv.put("ChargeDesc", ChargeDesc);

        long q = sql.insert(DatabaseHandlers.TABLE_CHARGE, null, cv);
        Log.e("q ",String.valueOf(q));

    }

    public void insertPaymentTerms(String TermsCode, String TermsDescription, String PymtSettTermMasterId,String IsDeleted,
                                   String CreditDays, String BaseDate){
        SQLiteDatabase sql = db.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("TermsCode", TermsCode);
        cv.put("TermsDescription", TermsDescription);
        cv.put("PymtSettTermMasterId", PymtSettTermMasterId);
        cv.put("IsDeleted", IsDeleted);
        cv.put("CreditDays", CreditDays);
        cv.put("BaseDate", BaseDate);

        long q = sql.insert(DatabaseHandlers.TABLE_PAYMENT_TERMS, null, cv);
        Log.e("q ",String.valueOf(q));

    }
 public int getConfigurationcount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_CONFIGURATION;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getChargeCount() {
        String countQuery = "SELECT * FROM " + db.TABLE_CHARGE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getPaytermsCount() {
        String countQuery = "SELECT * FROM " + db.TABLE_PAYMENT_TERMS;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }



    public void InsertItemPickingOrderDetails(PickListDetail listDetail) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Pick_ListHdrId", listDetail.getPick_ListHdrId());
        cv.put("PickListNo", listDetail.getPickListNo());
        cv.put("ItemMasterId", listDetail.getItemMasterId());
        cv.put("QtyPicked", listDetail.getQtyPicked());
        cv.put("QtyToPick", listDetail.getQtyToPick());
        cv.put("ItemCode", listDetail.getItemCode());
        cv.put("ItemDesc", listDetail.getItemDesc());
        cv.put("Flag", listDetail.getFlag());

        long a = sql.insert(db.TABLE_ITEM_PICKLIST, null, cv);
        Log.e("", "" + a);

    }

    public void Insert_GRNPACKETS(GRNPOST bean) {

        try {
            SQLiteDatabase SQl = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("PacketNo", bean.getPacketNo());
            long a = SQl.insert(db.TABLE_GRNNO_PACKET, null, values);
            Log.e("Insert TABLE_GRNPACKET", "" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Insert_GRNPACKET(GRNPOST bean) {

        try {
            SQLiteDatabase SQl = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("GRNNo", bean.getGRNNo());
            values.put("GRNHeaderId", bean.getGRNHeaderId());
            values.put("PacketNo", bean.getPacketNo());
            values.put("ItemCode", bean.getItemCode());
            values.put("Quantity", bean.getQuantity());
            values.put("UOM", bean.getUOM());

            long a = SQl.insert(db.TABLE_GRN_POST, null, values);
            Log.e("Insert TABLE_GRNPOST", "" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Insert_GRNPACKETITEM(GRNPOST bean) {

        try {
            SQLiteDatabase SQl = db.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("GRNNo", bean.getGRNNo());
            values.put("GRNHeaderId", bean.getGRNHeaderId());
            values.put("ItemCode", bean.getItemCode());
            values.put("Quantity", bean.getQuantity());
            values.put("ChallanQty", bean.getChallanQty());
            values.put("GRNDetailId", bean.getGRNDetailId());
            values.put("RejQty", bean.getRejQty());
            values.put("InvoiceNo", bean.getInvoiceNo());
            values.put("CustomerName", bean.getCustomerName());

            long a = SQl.insert(db.TABLE_GRN_POST_ITEM, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getOfflineDataCnt() {

        // String countQuery = "SELECT * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded=?" + new String[]{WebUrlClass.FlagisUploadedFalse};
        String countQuery = "SELECT  * FROM " + db.TABLE_DATA_OFFLINE + " WHERE isUploaded= 'NO'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

}
