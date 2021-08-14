package com.vritti.crmlib.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vritti.crmlib.bean.BirthdayBean;
import com.vritti.crmlib.bean.BusinessSegmentbean;
import com.vritti.crmlib.bean.ChatGroup;
import com.vritti.crmlib.bean.ChatGroupJson;
import com.vritti.crmlib.bean.ChatMessage;
import com.vritti.crmlib.bean.ChatUser;
import com.vritti.crmlib.bean.CityBean;
import com.vritti.crmlib.bean.Datasheet;
import com.vritti.crmlib.bean.Firmbean;
import com.vritti.crmlib.bean.PartialCallList;
import com.vritti.crmlib.bean.ProductBean;
import com.vritti.crmlib.bean.ProspectsourceBean;
import com.vritti.crmlib.bean.Teritorybean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonFunctionCrm {
    String CompanyURL, EnvMasterId = "", LoginId = "", Password = "", PlantMasterId = "";
    Utility ut;
    DatabaseHandlers db;
    Context mContext;


    public CommonFunctionCrm(Context context) {

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
    public void deleteDatasheetSelection(int selectionid, String selectionText,
                                         String selectionValue) {
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("DELETE FROM " + db.TABLE_DATASHEET_SELECTION);

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
        Log.d("crm_dialog_action", " values " + values);

    }

    //Id TEXT,Name
    public void addfirm(String id, String name) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Id", id);
        values.put("Name", name);
        long a = sql.insert(db.TABLE_Firm, null, values);


    }

    public CityBean[] getCBItemList(String searchTerm) {
        CityBean[] list = null;
        SQLiteDatabase sql = db.getReadableDatabase();
        String sqry = "";
        sqry += "SELECT * FROM " + db.TABLE_CITY;
        sqry += " WHERE CityName LIKE '%" + searchTerm + "%'";
        sqry += " ORDER BY CityName DESC";
        sqry += " LIMIT 0,5";
        Cursor cur = sql.rawQuery(sqry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            list = new CityBean[cur.getCount()];
            do {
                CityBean bean = new CityBean();
                bean.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                bean.setPKCityID(cur.getString(cur.getColumnIndex("PKCityID")));
                list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<BusinessSegmentbean> getBusinessSegmentbean() {
        List<BusinessSegmentbean> list = new ArrayList<BusinessSegmentbean>();

        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_Business_segment;
        // sql += " WHERE CityName LIKE '%" + searchTerm + "%'";
        //sql += " ORDER BY CityName DESC";
        // sql += " LIMIT 0,5";
        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            //   list = new Firmbean[cur.getCount()];
            do {
                BusinessSegmentbean bean = new BusinessSegmentbean();
                bean.setSegmentDescription(cur.getString(cur.getColumnIndex("SegmentDescription")));

                list.add(bean);
                //list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<CityBean> getCitybean() {
        List<CityBean> list = new ArrayList<CityBean>();
        //  Firmbean[] list = null;
        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_CITY;
        // sql += " WHERE CityName LIKE '%" + searchTerm + "%'";
        //sql += " ORDER BY CityName DESC";
        // sql += " LIMIT 0,5";
        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            //   list = new Firmbean[cur.getCount()];
            do {
                CityBean bean = new CityBean();
                bean.setCityName(cur.getString(cur.getColumnIndex("CityName")));
                bean.setPKCityID(cur.getString(cur.getColumnIndex("PKCityID")));
                list.add(bean);
                //list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public Firmbean[] getFirmbeanList() {
        Firmbean[] list = null;
        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_Firm;
        // sql += " WHERE Name LIKE '%" + searchTerm + "%'";
        qry += " ORDER BY Name DESC";
        qry += " LIMIT 0,5";
        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            list = new Firmbean[cur.getCount()];
            do {
                Firmbean bean = new Firmbean();
                bean.setName(cur.getString(cur.getColumnIndex("Name")));
                bean.setId(cur.getString(cur.getColumnIndex("Id")));
                list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<Firmbean> getFirmbean() {
        List<Firmbean> list = new ArrayList<Firmbean>();

        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_Firm;

        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();

            do {
                Firmbean bean = new Firmbean();
                bean.setName(cur.getString(cur.getColumnIndex("Name")));
                bean.setId(cur.getString(cur.getColumnIndex("Id")));
                list.add(bean);

                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<Teritorybean> getTeritorybean() {
        List<Teritorybean> list = new ArrayList<Teritorybean>();
        list.clear();
        //  Firmbean[] list = null;
        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_Teritory;

        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();

            do {
                Teritorybean bean = new Teritorybean();
                bean.setTerritoryName(cur.getString(cur.getColumnIndex("TerritoryName")));
                //bean.setId(cur.getString(cur.getColumnIndex("Id")));
                list.add(bean);

                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<ProspectsourceBean> getProspectsourceBean() {
        List<ProspectsourceBean> list = new ArrayList<ProspectsourceBean>();
        //  Firmbean[] list = null;
        SQLiteDatabase sql = db.getReadableDatabase();
        String qry = "";
        qry += "SELECT * FROM " + db.TABLE_Prospectsource;

        Cursor cur = sql.rawQuery(qry, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();

            do {
                ProspectsourceBean bean = new ProspectsourceBean();
                bean.setSourceName(cur.getString(cur.getColumnIndex("SourceName")));
                //bean.setId(cur.getString(cur.getColumnIndex("Id")));
                list.add(bean);

                x++;

            } while (cur.moveToNext());
        }
        return list;

    }

    public List<ProductBean> getProduct() {
        SQLiteDatabase sql = db.getWritableDatabase();

        List<ProductBean> list = new ArrayList<ProductBean>();
        String[] allColumns = {"ItemMasterId", "ItemDesc"};
        Cursor cursor = sql.query(db.TABLE_Product, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ProductBean item = new ProductBean(cursor.getString(0), cursor.getString(1));
            list.add(item);
            cursor.moveToNext();
        }

        cursor.close();
        return list;
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

    public void addDatasheet(int sequenceNo, String FKQuesId, String weightage, String quesText,
                             String isResponseMandatory, String selectionText, String selectionValue,
                             String valueMin, String valueMax, String notes, String maxValueText,
                             String FKPrimaryQuesId, String FKSecondaryQuesId, String isBranching,
                             String expectedResponse, String ifResponseId, String disableQuesStr, String groupID,
                             String groupName, String quesCode, String selectionType, String controlWidth,
                             String maxNoOfResponses, String maxExpectedResponse, String responseType,
                             String responseValue, String answer, String answer_value, String detailid,
                             String formId, String PKCssFormsQuesID, int flag) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("SequenceNo", sequenceNo);
        values.put("FKQuesId", FKQuesId);
        values.put("Weightage", weightage);
        values.put("QuesText", quesText);
        values.put("IsResponseMandatory", isResponseMandatory);
        values.put("SelectionText", selectionText);
        values.put("SelectionValue", selectionValue);
        values.put("ValueMin", valueMin);
        values.put("ValueMax", valueMax);
        values.put("Notes", notes);
        values.put("MaxValueText", maxValueText);
        values.put("FKPrimaryQuesId", FKPrimaryQuesId);
        values.put("FKSecondaryQuesId", FKSecondaryQuesId);
        values.put("IsBranching", isBranching);
        values.put("ExpectedResponse", expectedResponse);
        values.put("IfResponseId", isResponseMandatory);
        values.put("DisableQuesStr", disableQuesStr);
        values.put("GroupID", groupID);
        values.put("GroupName", groupName);
        values.put("QuesCode", quesCode);
        values.put("SelectionType", selectionType);
        values.put("ControlWidth", controlWidth);
        values.put("MaxNoOfResponses", maxNoOfResponses);
        values.put("MaxExpectedResponse", maxExpectedResponse);
        values.put("ResponseType", responseType);
        values.put("ResponseValue", responseValue);
        values.put("answer", answer);
        values.put("answer_value", answer_value);
        values.put("detailid", detailid);
        values.put("FormId", formId);
        values.put("PKCssFormsQuesID", PKCssFormsQuesID);
        values.put("flag", flag);

        sql.insert(db.TABLE_DATASHEET_DATA, null, values);

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

    public int check_DatasheetQueAnsList(String FormId, String FKQuesId) {
        String countQuery = "SELECT * FROM  " + db.TABLE_DATASHEET_DATA + " WHERE FormId='"
                + FormId + "' AND FKQuesId='" + FKQuesId + "'";
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

    public int check_source() {
        String countQuery = "SELECT * FROM  " + db.TABLE_Prospectsource;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_BusinessSegment() {
        String countQuery = "SELECT * FROM  " + db.TABLE_Business_segment;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_Teritory() {
        String countQuery = "SELECT * FROM  " + db.TABLE_Teritory;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_City() {
        String countQuery = "SELECT * FROM  " + db.TABLE_CITY;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_setup() {
        String countQuery = "SELECT * FROM  " + db.TABLE_Setup_Prospect;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int check_firm(String searchTerm) {

        String Qrery = "";
        Qrery += "SELECT * FROM " + db.TABLE_Firm;
        Qrery += " WHERE Name LIKE '%" + searchTerm + "%'";
        //  sql += " ORDER BY Name DESC";
        //  String countQuery = "SELECT * FROM  " + TABLE_Firm;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(Qrery, null);
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
        String countQuery = "SELECT * FROM " + db.TABLE_GPS_REPORTINGTO + " ORDER BY UserName ASC";
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


    public void AddCallHistory(CallHistory callHistory) {

// ModifiedDt TEXT,Outcome TEXT,UserName TEXT,OutcomeCode TEXT,LatestRemark TEXT)";


        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CallHistoryId", callHistory.getCallHistoryId());//CallHistoryId , callHistory, CallHistoryId
        cv.put("CallId", callHistory.getCallId());
        cv.put("CurrentCallOwner", callHistory.getCurrentCallOwner());
        cv.put("ActionType", callHistory.getActionType());
        cv.put("Contact", callHistory.getContact());
        cv.put("Purpose", callHistory.getPurpose());
        cv.put("NextAction", callHistory.getNextAction());
        cv.put("NextActionDateTime", callHistory.getNextActionDateTime());
        cv.put("ModifiedDt", callHistory.getModifiedDt());
        cv.put("Outcome", callHistory.getOutcome());
        cv.put("UserName", callHistory.getUserName());
        cv.put("OutcomeCode", callHistory.getOutcomeCode());
        cv.put("LatestRemark", callHistory.getLatestRemark());
        long a = sql.insert(db.TABLE_CALLHISTORY, null, cv);

    }

    /*public void AddCalenderView(CalendarCollection calendarCollection) {

// ModifiedDt TEXT,Outcome TEXT,UserName TEXT,OutcomeCode TEXT,LatestRemark TEXT)";


        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NextActionDateTime", calendarCollection.getNextActionDateTime());
        cv.put("CountCollection", calendarCollection.getCountCollection());
        cv.put("CountFeedBack", calendarCollection.getCountFeedBack());
        cv.put("CountSales", calendarCollection.getCountSales());
        long a = sql.insert(TABLE_Calender, null, cv);

    }
*/
    public void AddPartialCall(PartialCallList partialCallList) {

        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("FirmName", partialCallList.getFirmname());
        cv.put("CityName", partialCallList.getCityname());
        cv.put("TerritoryName", partialCallList.getCityterritoryname());
        cv.put("CallId", partialCallList.getCallId());
        cv.put("ProductName", partialCallList.getProductname());
        cv.put("ActionIcon", partialCallList.getActionicon());
        cv.put("actiondatetime", partialCallList.getActiondatetime());
        cv.put("AssignedBy", partialCallList.getAssignedby());
        cv.put("isPartial", partialCallList.getIsPartial());
        cv.put("Mobile", partialCallList.getMobileno());
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
    }


    public void AddPlantMaster(String PlantMasterId, String PlantName) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PlantMasterId", PlantMasterId);
        cv.put("PlantName", PlantName);
        long a = sql.insert(db.TABLE_PLANTMASTER, null, cv);
    }

    public int getCallListPartialcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_OPPOTUNITY_CALL_FILTER;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallTeamPartialcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL_PARTIAL_TEAM;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getCallIdcount(String cid) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL_PARTIAL + " WHERE CallId='" + cid + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getCallListcount(String callid) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL + " WHERE CallId='" + callid + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallListTeamcount(String callid) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL_TEAM + " WHERE CallId='" + callid + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallListOppcount(String callid) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL_OPP + " WHERE CallId='" + callid + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getNatureOfcallcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_NatureofCall;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getInitiatedbycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_InitiatedBy;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getwhomwithcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_With_whom;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getFollowupreasoncount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Followup_reason;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getOutcomecount(String calltype) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Outcome + " WHERE OutcomeType='" + calltype + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public String getOutcomecode(String outcome) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Outcome + " WHERE Outcome='" + outcome + "'";
        String data = "";
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                data = cursor.getString(cursor.getColumnIndex("Code"));
            } while (cursor.moveToNext());
        }
        return data;
    }

    public String getOutcomeIsapprover(String code) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Outcome + " WHERE Code='" + code + "'";
        String data = "";
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                data = cursor.getString(cursor.getColumnIndex("IsApprover"));
            } while (cursor.moveToNext());
        }
        return data;
    }

    public int getCategorycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Category;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getExecutivecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Executive;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getReasonMastercount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_REASON_Master;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getReasoncount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_REASON;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getFilterProspectcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_filterdata_prospect;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallInfocount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CALLLISTDATA;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getApprovercount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_APPROVER;
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
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CurrencyMaster;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getOrdertypecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_OrderTypeMaster;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getOrdertypeMasterAllcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_OrderTypeMaster_All;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getTMESNamecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_TMESEName;
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
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CITY;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getReferencetypecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Referencetype;
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
                + db.TABLE_STATE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCountrycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_COUNTRY;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getEntitycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Entity;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getSEcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_SE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getBOEcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_BOE;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getFollowUpTimecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_FollowUpTime;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCampaigncount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Campaign;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getOrderTypecount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_OrderType;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getProuctcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Product;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getProuctdetailcount(String ItemDesc) {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Product_Details + " WHERE ItemDesc='" + ItemDesc + "'";
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public int getProuctdetail_count() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_Product_Details;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallhistorycount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CALLHISTORY;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCallcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CRM_CALL;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCustomercount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_CUSTOMER;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }



    public int getshowContactCallcount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_SHOW_CONTACT;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
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

    public void OpportunityUpdate(String Json, String update) {
        SQLiteDatabase sql = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Opportunity_update", Json);
        values.put("UpdateData", update);
        long a = sql.insert(db.TABLE_OPPORTUNITY_UPDATE, null, values);
        Log.e("data", "insert" + a);
    }

    public int getBankdatacount() {
        String countQuery = "SELECT  * FROM "
                + db.TABLE_BANKNAME;
        int count = 0;
        SQLiteDatabase sql = db.getReadableDatabase();
        Cursor cursor = sql.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCalendercount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_Calender;
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
                + db.TABLE_CHAT_ROOMNAME_DISPLAY_LIST;
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
        cv.put("AddedBy", chatUser.getUsername());
        cv.put("ParticipantName", chatUser.getParticipantName());
        cv.put("Message", chatUser.getMessage());
        long a = sql.insert(db.TABLE_CHAT_CHATROOM_MEMBER_LIST, null, cv);
        Log.e("", "" + a);

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
        long a = sql.insertWithOnConflict(db.TABLE_CHAT_GROUP_MESSAGE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        Log.e("", "" + a);

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

    public int getAddbycount() {
        String countQuery = "SELECT * FROM "
                + db.TABLE_AddBy;
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
