package com.vritti.vwblib.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.vwblib.Beans.AffectedCustomer;
import com.vritti.vwblib.Beans.BirthdayBean;
import com.vritti.vwblib.Beans.ClaimNotification;
import com.vritti.vwblib.Beans.Datasheet;
import com.vritti.vwblib.Beans.GPSLocationTimeBean;
import com.vritti.vwblib.Beans.MaterialAddBean;
import com.vritti.vwblib.Beans.MyWorkspaceBean;
import com.vritti.vwblib.chat.ChatGroup;
import com.vritti.vwblib.chat.ChatGroupJson;
import com.vritti.vwblib.chat.ChatMessage;
import com.vritti.vwblib.chat.ChatUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommonFunction {
    String CompanyURL, EnvMasterId = "", LoginId = "", Password = "", PlantMasterId = "";
    Utility ut;
   static DatabaseHandlers db;
    Context mContext;


    public CommonFunction(Context context) {

        ut = new Utility();
        this.mContext = context;
        String settingKey = ut.getSharedPreference_SettingKey(mContext);
        String dabasename = ut.getValue(mContext, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        db = new DatabaseHandlers(mContext, dabasename);
        CompanyURL = ut.getValue(mContext, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(mContext, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        LoginId = ut.getValue(mContext, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password = ut.getValue(mContext, WebUrlClass.GET_PSW_KEY, settingKey);
        PlantMasterId = ut.getValue(mContext, WebUrlClass.GET_PlantID_KEY, settingKey);
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
                sql.close();
                return true;
            } else {
                c.close();
                sql.close();
                return false;
            }
        } catch (Exception e) {
            c.close();
            sql.close();
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
        sql.close();
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
        //long a = sql.insert(TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);
        // Log.e("",""+a);
        long a = sql.insert(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);


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
        sql.close();
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
}
