package com.vritti.sales.beans;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ProgressBar;

import com.vritti.crm.bean.CityBean;
import com.vritti.databaselib.data.DatabaseHandlers;
import com.vritti.databaselib.other.Utility;
import com.vritti.databaselib.other.WebUrlClass;
import com.vritti.ekatm.R;
import com.vritti.sales.data.AnyMartData;
import com.vritti.sales.data.AnyMartDatabaseConstants;

import org.json.JSONArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tbuds_commonFunctions {
    DatabaseHandlers dbhelper;
    Utility ut;
    SQLiteDatabase sql_db;
    ProgressBar mprogress;
    String PlantMasterId = "", LoginId = "", Password = "", CompanyURL = "", EnvMasterId = "",
            UserMasterId = "", UserName = "", MobileNo = "", Sourcetype = "";

    public Tbuds_commonFunctions(Context parent) {

        ut = new Utility();
        String settingKey = ut.getSharedPreference_SettingKey(parent);
        String dabasename = ut.getValue(parent, WebUrlClass.GET_DATABASE_NAME_KEY, settingKey);
        dbhelper = new DatabaseHandlers(parent, dabasename);
        sql_db = dbhelper.getWritableDatabase();
        CompanyURL = ut.getValue(parent, WebUrlClass.GET_COMPANY_URL_KEY, settingKey);
        EnvMasterId = ut.getValue(parent, WebUrlClass.GET_EnvMasterID_KEY, settingKey);
        PlantMasterId = ut.getValue(parent, WebUrlClass.GET_PlantID_KEY, settingKey);
        LoginId = ut.getValue(parent, WebUrlClass.GET_LOGIN_KEY, settingKey);
        Password =ut.getValue(parent, WebUrlClass.GET_PSW_KEY, settingKey);
        UserMasterId = ut.getValue(parent, WebUrlClass.GET_USERMASTERID_KEY, settingKey);
        UserName = ut.getValue(parent, WebUrlClass.GET_USERNAME_KEY, settingKey);
      //  mprogress=findViewById(R.id.toolbar_progress_App_bar);
    }

    public int getPListcount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_PRICELIST;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getShipTocount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_SHIPTO_DETAILS;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCompanyDtlscount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_COMPANY_DETAILS;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getEntitycount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_ENTITY_TYPE;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getSalesPriceListcount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_SALES_PRICELIST;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCounterBillItemcount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_ADD_ITEMS_COUNTERBILL;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int gettaxclscount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_TAXCLASS;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getcount_tempTable() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_ADD_ITMDTLS_FORBILL;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCitycount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_CITY_SALES;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getStatecount() {
        String countQuery = "SELECT  * FROM "
                + DatabaseHandlers.TABLE_STATES_SALES;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getStatecount_ENTITY() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_STATE_ENTITY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getDistrictCount_ENTITY() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_DISTRICT;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getTalukaCount_ENTITY() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_TALUKA_ENTITY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCityCount_ENTITY() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CITY_ENTITY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCommissionCnt() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_COMMISSION;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCatCnt() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CATEGORY_REIMB;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getConsigneeCnt() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CONSIGNEES;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getWarrantyCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_WARRANTY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getUOMNewCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_UOM_new;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getModelCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_MODEL;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getSFamilyCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_SALES_FAMILY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

   /* public int getScheduleCnt() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_SCHEDULE;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }*/

    public int getAllCatSubcatItemCount(Activity parent) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS;
        int count = 0;

        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getMerchantsAgainstItems() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM;
        int count = 0;

        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addStates(String stateID, String statename) {
        ContentValues cv = new ContentValues();
        cv.put("state_id", stateID);
        cv.put("state_name", statename);
        long a = sql_db.insert(DatabaseHandlers.TABLE_STATES_SALES, null, cv);
    }

    public void addCities(String cityID, String cityName, String stateId) {
        ContentValues cv = new ContentValues();
        cv.put("city_id", cityID);
        cv.put("city_name", cityName);
        cv.put("stateId", stateId);
        long a = sql_db.insert(DatabaseHandlers.TABLE_CITY_SALES, null, cv);
    }

    public void addMerchants(String MerchantId, String MerchantName, float qnty, int minqnty,
                             String offers, float price, String Product_name, String Freeitemid,
                             int Freeitemqty, String Freeitemname,String validfrom, String validto,
                             String Merchant_name_two, String MerchantAddress, String MerchantEmail,
                             String MerchantMobile) {

        ContentValues cv = new ContentValues();
        cv.put("MerchantId", MerchantId);
        cv.put("MerchantName", MerchantName);
        cv.put("qnty", qnty);
        cv.put("minqnty", minqnty);
        cv.put("offers", offers);
        cv.put("price", price);
        cv.put("Product_name", Product_name);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Freeitemname", Freeitemname);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("Merchant_Name_Two", Merchant_name_two);
        cv.put("MerchantAddress", MerchantAddress);
        cv.put("MerchantEmail", MerchantEmail);
        cv.put("MerchantMobile", MerchantMobile);
        long a = sql_db.insert(DatabaseHandlers.TABLE_MERCHANTS, null, cv);
    }

    public void addAllCatSubcatItems(String CategoryId, String CategoryName, String SubCategoryId,
                                     String SubCategoryName, String ItemMasterId, String ItemName, String ItemImgPath,
                                     String ItemMRP,String custVendorname, String TypeFixedPercent, String validfrom,
                                     String validto,String DisRate, String NetRate, String Freeitemid, String Freeitemqty,
                                     String Minqty, String Discratepercent,String DiscrateMRP, String PurDigit, String CustVendorMasterId,
                                     String PricelistId, String PricelistRate) {
        ContentValues cv = new ContentValues();
        cv.put("CategoryId", CategoryId);
        cv.put("CategoryName", CategoryName);
        cv.put("SubCategoryName", SubCategoryName);
        cv.put("ItemName", ItemName);
        cv.put("itemmasterid", ItemMasterId);
        cv.put("SubCategoryId", SubCategoryId);
        cv.put("ItemImgPath", ItemImgPath);
        cv.put("itemMRP",ItemMRP );
        cv.put("CustVendorMasterId", CustVendorMasterId);
        cv.put("custVendorname", custVendorname);
        cv.put("TypeFixedPercent", TypeFixedPercent);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("DisRate", DisRate);
        cv.put("NetRate", NetRate);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Minqty", Minqty);
        cv.put("Discratepercent", Discratepercent);
        cv.put("DiscrateMRP", DiscrateMRP);
        cv.put("PurDigit", PurDigit);
        cv.put("PricelistId", PricelistId);
        cv.put("PricelistRate", PricelistRate);

        long a = sql_db.insert(DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS, null, cv);
        Log.e("AllcatItem table - ",String.valueOf(a));
    }

    public void addCartItems(String MerchantId, String MerchantName, String qnty, String minqnty,
                             String offers, String price, String Product_name, String Amount, String Product_id,
                             String Freeitemid,String Freeitemqty, String Freeitemname, String validfrom, String validto,
                             String ItemImgPath) {


        ContentValues cv = new ContentValues();
        cv.put("MerchantId", MerchantId);
        cv.put("MerchantName", MerchantName);
        cv.put("qnty", qnty);
        cv.put("minqnty", minqnty);
        cv.put("offers", offers);
        cv.put("price", price);
        cv.put("Product_name", Product_name);
        cv.put("Amount", Amount);
        cv.put("Product_id", Product_id);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Freeitemname", Freeitemname);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("ItemImgPath", ItemImgPath);
        long q = sql_db.insert(DatabaseHandlers.TABLE_CART_ITEM, null, cv);

    }

    public int getCartItems() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCartItems1() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM_new;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getcartvolumediscountcount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM_VOLUME_DISCOUNT;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getMerchantCount() {
        String countQuery = "SELECT distinct MerchantName FROM "
                + DatabaseHandlers.TABLE_CART_ITEM;
        // + " WHERE MerchantName='" + Mname + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }
    public int getMerchantCount1() {
        String countQuery = "SELECT distinct MerchantName FROM "
                + DatabaseHandlers.TABLE_CART_ITEM_new;
        // + " WHERE MerchantName='" + Mname + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCartcount() {
        String countQuery = "SELECT * FROM "
                + DatabaseHandlers.TABLE_CART_ITEM;

        int count = 0;

        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addCartItemsVolumeDiscount(String MerchantId, String MerchantName, int minvalue,
                                           String netrate, String freeitemid, String freeitemname,
                                           String freeitemqnty,
                                           String validfrom, String validto,
                                           String CouponId, String discount, String FKVendorItemMasterId, String FKVendorProductmasterId) {

        ContentValues cv = new ContentValues();
        cv.put("MerchantId", MerchantId);
        cv.put("MerchantName", MerchantName);
        cv.put("minvalue", minvalue);
        cv.put("netrate", netrate);
        cv.put("freeitemid", freeitemid);
        cv.put("freeitem_name", freeitemname);
        cv.put("freeitemqnty", freeitemqnty);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("CouponId", CouponId);
        cv.put("discount", discount);
        cv.put("FKVendorItemMasterId",FKVendorItemMasterId);
        cv.put("FKVendorProductmasterId",FKVendorProductmasterId);

        long a= sql_db.insert(DatabaseHandlers.TABLE_CART_ITEM_VOLUME_DISCOUNT, null, cv);

        Log.e("item vol disc:",""+a);
    }

    public int getMerchantsAgainstItems_chkrecords(String Mid, String Pname) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM
                + " WHERE MerchantId='" + Mid + "' and Product_name='" + Pname + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addMerchantsAgainstItems(String MerchantId, String MerchantName, String qnty, String minqnty,
                                         String offers, String price, String Product_name, String Freeitemid,
                                         String Freeitemqty, String Freeitemname,
                                         String validfrom, String validto) {

        ContentValues cv = new ContentValues();
        cv.put("MerchantId", MerchantId);
        cv.put("MerchantName", MerchantName);
        cv.put("qnty", qnty);
        cv.put("minqnty", minqnty);
        cv.put("offers", offers);
        cv.put("price", price);
        cv.put("Product_name", Product_name);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Freeitemname", Freeitemname);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        long a = sql_db.insert(DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM, null, cv);
        Log.e("",""+a);
    }

    public void addPlaceOrder(String C_V_type, String schedule_date_time, String xml1,
                              String xml2,String placeorderDate, String isUploaded) {
        ContentValues cv = new ContentValues();
        cv.put("C_V_type", C_V_type);
        cv.put("schedule_date_time", schedule_date_time);   //delivery date
        cv.put("xml1", xml1);
        cv.put("xml2", xml2);
        cv.put("placeOrderDate",placeorderDate);
        cv.put("isUploaded", isUploaded);
        //add place order date column here

        long a = sql_db.insert(DatabaseHandlers.TABLE_PLACE_ORDER, null, cv);
    }

    public void addOrderHistory(String Address, String City, String ConsigneeName, String CustomerMasterId,
                                String ItemMasterId, String Mobile, String Qty, String Rate,
                                String SODate, String SOHeaderId, String DODisptch, String DORcvd, String status,String statusname,
                                String DoAck, String NetAmt, String ItemName, String LineAmt, String MerchantID,
                                String MerchantNAme,String SODetailId, String sono, String SOScheduleId,String DispQty,
                                String RecvQty, String AppvDt, String PurDigit, String DOrej, String DispatchNo,
                                String SalesHeaderId,String SalesDtlId, String DispNetAmnt,String ShipStatus, String ModifiedDt,
                                String placeOrderDate,String PrefDelFrmTime,String PrefDelToTime,String Latitude,String Longitude,
                                String OrgQty,String DeliveryTerms,String minordqty,String maxordqty,String distance,String UOMCode,
                                String outofstock,String mrp,String sellingrate,String range,String Brand,String Content,String ContentUOM,
                                String SellingUOM,String PackOfQty,String UPIMerch,String PaymentStatus,String PaymentMode,
                                String TransactionId,String AmountStatus,String TransactionDate,String MerchAddress,String merchant_Mobile,
                                String FreeAboveAmt,String FreeDelyMaxDist,String MinDelyKg,String MinDelyKm,String ExprDelyWithinMin,
                                String ExpressDelyChg,String IsDelivery, String MerchLattitude, String MerchLongitude) {
        ContentValues cv = new ContentValues();
        cv.put("Address", Address);
        cv.put("City", City);
        cv.put("ConsigneeName", ConsigneeName);
        cv.put("CustomerMasterId", CustomerMasterId);
        cv.put("ItemMasterId", ItemMasterId);
        cv.put("Mobile", Mobile);
        cv.put("Qty", Qty);
        cv.put("Rate", Rate);
        cv.put("SODate", SODate);
        cv.put("SOHeaderId", SOHeaderId);
        cv.put("DODisptch", DODisptch);
        cv.put("DORcvd", DORcvd);
        cv.put("status", status);
        cv.put("statusname", statusname);
        cv.put("DoAck", DoAck);
        cv.put("NetAmt", NetAmt);
        cv.put("ItemDesc", ItemName);
        cv.put("LineAmt", LineAmt);
        cv.put("merchantid", MerchantID);
        cv.put("merchantname", MerchantNAme);
        cv.put("SODetailId", SODetailId);
        cv.put("sono", sono);
        cv.put("SOScheduleId",SOScheduleId);
        cv.put("ShipmentQty", DispQty);
        cv.put("ClientRecQty", RecvQty);
        cv.put("AppvDt", AppvDt);
        cv.put("PurDigit", PurDigit);
        cv.put("DOrej", DOrej);
        cv.put("DispatchNo", DispatchNo);
        cv.put("SalesHeaderId", SalesHeaderId);
        cv.put("SalesDtlId", SalesDtlId);
        cv.put("DispNetAmnt", DispNetAmnt);
        cv.put("ShipStatus",ShipStatus);
        cv.put("OrdRcvdDate",ModifiedDt);
        cv.put("placeOrderDate", placeOrderDate);
        cv.put("PrefDelFrmTime", PrefDelFrmTime);
        cv.put("PrefDelToTime", PrefDelToTime);
        cv.put("Latitude", Latitude);
        cv.put("Longitude", Longitude);
        cv.put("OrgQty", OrgQty);
        cv.put("DeliveryTerms", DeliveryTerms);
        cv.put("minordqty", minordqty);
        cv.put("maxordqty", maxordqty);
        cv.put("distance", distance);
        cv.put("UOMCode", UOMCode);
        cv.put("outofstock", outofstock);
        cv.put("mrp", mrp);
        cv.put("sellingrate", sellingrate);
        cv.put("range", range);
        cv.put("Brand", Brand);
        cv.put("Content", Content);
        cv.put("ContentUOM", ContentUOM);
        cv.put("SellingUOM", SellingUOM);
        cv.put("PackOfQty", PackOfQty);
        cv.put("UPIMerch", UPIMerch);
        cv.put("PaymentStatus", PaymentStatus);
        cv.put("PaymentMode", PaymentMode);
        cv.put("TransactionId", TransactionId);
        cv.put("AmountStatus", AmountStatus);
        cv.put("TransactionDate", TransactionDate);
        cv.put("MerchAddress", MerchAddress);
        cv.put("merchant_Mobile", merchant_Mobile);
        cv.put("FreeAboveAmt", FreeAboveAmt);
        cv.put("FreeDelyMaxDist", FreeDelyMaxDist);
        cv.put("MinDelyKg", MinDelyKg);
        cv.put("MinDelyKm", MinDelyKm);
        cv.put("ExprDelyWithinMin", ExprDelyWithinMin);
        cv.put("ExpressDelyChg", ExpressDelyChg);
        cv.put("IsDelivery", IsDelivery);
        cv.put("MerchLattitude", MerchLattitude);
        cv.put("MerchLongitude", MerchLongitude);
        long h =  sql_db.insert(AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY, null, cv);
        Log.e("item vol disc:",""+ h);
    }

    public int getAddress(String Mob) {
        String countQuery = "SELECT type FROM " + DatabaseHandlers.TABLE_MY_ADDRESS
                + " WHERE Mobile='" + Mob + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getOrdHistory() {
        String countQuery = "SELECT * FROM " + AnyMartDatabaseConstants.TABLE_MY_ORDER_HISTORY;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getAddress_home(String type) {
        String countQuery = "SELECT  type FROM " + DatabaseHandlers.TABLE_MY_ADDRESS
                + " WHERE type='" + type + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getAddress_office(String type) {
        String countQuery = "SELECT type FROM " + DatabaseHandlers.TABLE_MY_ADDRESS
                + " WHERE type='" + type + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getAddress_other(String type) {
        String countQuery = "SELECT type FROM " + DatabaseHandlers.TABLE_MY_ADDRESS
                + " WHERE type='" + type + "'";
        int count = 0;

        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getAddress_current(String type) {
        String countQuery = "SELECT type FROM " + DatabaseHandlers.TABLE_MY_ADDRESS
                + " WHERE type='" + type + "'";
        int count = 0;

        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addAddress(String UserId,
                           String Mobile,
                           String PermanentAddress, String GpsLocationAddress, String Latitude, String Longitude,
                           String CurrentAddress, String OfficeAddress, String type) {
        ContentValues cv = new ContentValues();
        cv.put("UserId", UserId);

        cv.put("Mobile", Mobile);

        cv.put("PermanentAddress", PermanentAddress);
        cv.put("GpsLocationAddress", GpsLocationAddress);
        cv.put("Latitude", Latitude);
        cv.put("Longitude", Longitude);
        cv.put("CurrentAddress", CurrentAddress);
        cv.put("OfficeAddress", OfficeAddress);
        cv.put("type", type);
        long a = sql_db.insert(DatabaseHandlers.TABLE_MY_ADDRESS, null, cv);
    }

    public int getMerchantsAgainst_Items(String Pname) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM +
                " where Product_name='" + Pname + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getCartItems_chkrecords(String Mid, String Pname, String Pid) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM
                + " WHERE MerchantId='" + Mid + "' and Product_name='" + Pname + "'" +
                " and Product_id='" + Pid + "'";
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void clearTable(Context parent, String tablename) {
        sql_db.delete(tablename, null, null);
    }

    public static String updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }

    public static String convertDateDDMMYYYYToYYYYMMDD(String date) {
        String[] data = date.split("-");
        // String month = String.format("%02d", Integer.parseInt(data[1]));
        return data[2] + "-" + data[1] + "-" + data[0];
    }

    public static String convertTime12HrsTo24Hrs(String time) {
        String convertedTime = null;
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date date = parseFormat.parse(time);
            convertedTime = displayFormat.format(date);
        } catch (Exception e) {
        }

        return convertedTime;
    }

    /**************************** Counter Billing methods *************************************************/

    public void AddBluetooth(String address) {
       // SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Address", address);
        sql_db.insert(DatabaseHandlers.TABLE_BLUETOOTH_ADDRESS, null, cv);
    }

    public int getAllCustomerCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CUSTOMER_CB;
        int count = 0;
      //  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
          // cursor.close();
        }
        return count;
    }

    public void addCustomer(String FullName, String Mobile) {
      //  SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FullName", FullName);
        values.put("Mobile", Mobile);

        long q = sql_db.insert(DatabaseHandlers.TABLE_CUSTOMER_CB, null, values);
        Log.d("test", " values " + values);
    }

    public AllCatSubcatItems[] getCBItemList(String searchTerm) {
        AllCatSubcatItems[] list = null;
     //  SQLiteDatabase db = this.getReadableDatabase();
        String sql = "";
        sql += "SELECT * FROM " + DatabaseHandlers.TABLE_PRODUCT_CB;
        sql += " WHERE Product_name LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY Product_id DESC";
        sql += " LIMIT 0,5";

        Cursor cur = sql_db.rawQuery(sql, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            list = new AllCatSubcatItems[cur.getCount()];
            do {
                AllCatSubcatItems bean = new AllCatSubcatItems();
                bean.setItemName(cur.getString(cur.getColumnIndex("Product_name")));
                bean.setItemMasterId(cur.getString(cur.getColumnIndex("Product_id")));
                list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;
    }

    public AllCatSubcatItems[] getCBItemListRuni(String searchTerm) {
        AllCatSubcatItems[] list = null;
    //    SQLiteDatabase db = this.getReadableDatabase();
        String sql = "";
        sql += "SELECT * FROM " + DatabaseHandlers.TABLE_MARCHANT_ITEM_RUNI;//TABLE_MARCHANT_ITEM_RUNI   + "(ItemMasterId TEXT, ItemName TEXT)";
        sql += " WHERE ItemName LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY ItemMasterId DESC";
        sql += " LIMIT 0,5";

        Cursor cur = sql_db.rawQuery(sql, null);
        if (cur.getCount() > 0) {
            int x = 0;
            cur.moveToFirst();
            list = new AllCatSubcatItems[cur.getCount()];
            do {
                AllCatSubcatItems bean = new AllCatSubcatItems();
                bean.setItemName(cur.getString(cur.getColumnIndex("ItemName")));
                bean.setItemMasterId(cur.getString(cur.getColumnIndex("ItemMasterId")));
                list[x] = bean;
                x++;

            } while (cur.moveToNext());
        }
        return list;
    }

    public int getPendingBalanceCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_PENDING_BALANCE;
        int count = 0;
    //    SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
           // cursor.close();
        }
        return count;
    }

    public void addPendingBalance(String BillId, String FinalTotalBill, String Received,
                                  String Balance, String CustomerName, String Cust_mob, String date) {
     //   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BillId", BillId);
        values.put("FinalTotalBill", FinalTotalBill);
        values.put("Received", Received);
        values.put("Balance", Balance);
        values.put("CustomerName", CustomerName);
        values.put("Cust_mob", Cust_mob);
        values.put("date", date);
        long q = sql_db.insert(DatabaseHandlers.TABLE_PENDING_BALANCE, null, values);
        Log.d("test", " values " + values);
    }

    //TABLE_GET_REPORTS
    public int getReportsCount() {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_GET_REPORTS;
        int count = 0;
      //  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addReports(String salesheaderid, String customermobno, String customerusername,
                           String salestotal, String salesdiscounttotal,
                           String salesfinaltotal, String salesreceiptamt, String salesbalanceamt,
                           String salesaddeddt, String salesvendorid,
                           String purchaseitemName, String purchasemrp, String purchaseqty, String purchaseunit,
                           String purchaseamt, String purchaseVendorid,
                           String purchaseShopname, String purchaseAddeddt, String purchasetotamt, String purchaseitemid,
                           String salesitemname, String salesqty, String salesunit,
                           String salesrate, String salesdisc, String salesamount, String salesitemid) {
      //  SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("salesheaderid", salesheaderid);
        values.put("customermobno", customermobno);
        values.put("customerusername", customerusername);
        values.put("salesdiscounttotal", salesdiscounttotal);
        values.put("salestotal", salestotal);
        values.put("salesfinaltotal", salesfinaltotal);
        values.put("salesreceiptamt", salesreceiptamt);
        values.put("salesbalanceamt", salesbalanceamt);
        values.put("salesaddeddt", salesaddeddt);
        values.put("salesvendorid", salesvendorid);
        values.put("purchaseitemName", purchaseitemName);
        values.put("purchasemrp", purchasemrp);
        values.put("purchaseqty", purchaseqty);
        values.put("purchaseunit", purchaseunit);
        values.put("purchaseamt", purchaseamt);
        values.put("purchaseAddeddt", purchaseAddeddt);
        values.put("purchaseVendorid", purchaseVendorid);
        values.put("purchaseShopname", purchaseShopname);
        values.put("purchasetotamt", purchasetotamt);
        values.put("purchaseitemid", purchaseitemid);
        values.put("salesitemname", salesitemname);
        values.put("salesqty", salesqty);
        values.put("salesunit", salesunit);
        values.put("salesrate", salesrate);
        values.put("salesdisc", salesdisc);
        values.put("salesamount", salesamount);
        values.put("salesitemid", salesitemid);
        long q = sql_db.insert(DatabaseHandlers.TABLE_GET_REPORTS, null, values);
        Log.d("test", " values " + values);
    }

    public void deleteReports() {
      //  SQLiteDatabase db = this.getWritableDatabase();
        sql_db.execSQL("DELETE FROM " + DatabaseHandlers.TABLE_GET_REPORTS);
    }

    public int getCartItems_AgainstCustomer(String CustName, String CustMob) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM_CB + " WHERE Cust_Name='" + CustName
                + "' AND Cust_mob='" + CustMob + "'";
        int count = 0;
     //   SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
          // cursor.close();
        }
        return count;
    }

    public void addBillXml(String BillId, String xml1, String xml2, String isUploaded, String Date) {
        ContentValues values = new ContentValues();

        values.put("BillId", BillId);
        values.put("xml1", xml1);
        values.put("xml2", xml2);
        values.put("isUploaded", isUploaded);
        values.put("Date",Date);
        long q = sql_db.insert(DatabaseHandlers.TABLE_BILL_DETAILS, null, values);
        Log.d("test", " values " + values);
      //  db.close();
    }

    public void addBill_two(String BillPrintNo, String ItemName, String Itemid, String Rate,String MRP, String Qty,String TaxClass, String cgstLine, String sgstLine,
                            String DiscLineAmt,float ItemDiscount, Float Amount,String TotalBill, String Discount,String DiscOnNetAmtRS,
                            String FinalTotalBill, String Received, String Balance,String CustomerName, String CustMobno,
                            String TotalWithDiscountBill, String TaxinRupsTotalBill,String CGSTTotal, String SGSTTotal, String IGSTTotal,
                            String PayableAmt,String isUploaded, String isPrinted,String Date, String CustGSTN, String CompanyGSTN) {
        ContentValues values = new ContentValues();
        values.put("BillPrintNo",BillPrintNo);
        values.put("ItemName", ItemName);
        values.put("Itemid", Itemid);
        values.put("Rate", Rate);
        values.put("MRP", MRP);
        values.put("Qty", Qty);
        values.put("TaxClass",TaxClass);
        values.put("cgstLine",cgstLine);
        values.put("sgstLine",sgstLine);
        values.put("DiscLineAmt", DiscLineAmt);
        values.put("ItemDiscount", ItemDiscount);
        values.put("Amount", Amount);
        values.put("TotalBill", TotalBill);
        values.put("Discount", Discount);
        values.put("DiscOnNetAmtRS",DiscOnNetAmtRS);
        values.put("FinalTotalBill", FinalTotalBill);
        values.put("Received", Received);
        values.put("Balance", Balance);
        values.put("CustomerName", CustomerName);
        values.put("CustMobno", CustMobno);
        values.put("TotalWithDiscountBill",TotalWithDiscountBill);
        values.put("TaxinRupsTotalBill",TaxinRupsTotalBill);
        values.put("CGSTTotal",CGSTTotal);
        values.put("SGSTTotal",SGSTTotal);
        values.put("IGSTTotal",IGSTTotal);
        values.put("PayableAmt",PayableAmt);
        values.put("isUploaded", isUploaded);
        values.put("isPrinted", isPrinted);
        values.put("Date", Date);
        values.put("CustGSTN", CustGSTN);
        values.put("CompanyGSTN", CompanyGSTN);

        long q = sql_db.insert(DatabaseHandlers.TABLE_BILL_CB, null, values);
        Log.d("test", " values " + q+", "+ItemName);
        //  db.close();
    }

    public void updateBill_two(String ItemName, String Itemid, String Rate,String MRP, String Qty,String TaxClass, String cgstLine, String sgstLine, String DiscLineAmt,
                               float ItemDiscount,Float Amount,String TotalBill, String Discount,String DiscOnNetAmtRS,
                               String FinalTotalBill, String Received,String Balance,String CustomerName, String CustMobno,
                               String TotalWithDiscountBill, String TaxinRupsTotalBill,String CGSTTotal, String SGSTTotal,
                               String IGSTTotal,String PayableAmt,String isUploaded, String isPrinted,String Date, String bill_No) {
        ContentValues values = new ContentValues();
      //  values.put("ItemName", ItemName);
       // values.put("Itemid", Itemid);
        values.put("Rate", Rate);
        values.put("MRP", MRP);
        values.put("Qty", Qty);
        values.put("TaxClass",TaxClass);
        values.put("cgstLine",cgstLine);
        values.put("sgstLine",sgstLine);
        values.put("DiscLineAmt",DiscLineAmt);
        values.put("ItemDiscount", ItemDiscount);
        values.put("Amount", Amount);
        values.put("TotalBill", TotalBill);
        values.put("Discount", Discount);
        values.put("DiscOnNetAmtRS",DiscOnNetAmtRS);
        values.put("FinalTotalBill", FinalTotalBill);
       // values.put("Received", Received);
       // values.put("Balance", Balance);
       // values.put("CustomerName", CustomerName);
       // values.put("CustMobno", CustMobno);
        values.put("TotalWithDiscountBill",TotalWithDiscountBill);
        values.put("TaxinRupsTotalBill",TaxinRupsTotalBill);
        values.put("CGSTTotal",CGSTTotal);
        values.put("SGSTTotal",SGSTTotal);
        values.put("IGSTTotal",IGSTTotal);
        values.put("PayableAmt",PayableAmt);
        values.put("isUploaded", isUploaded);
        values.put("isPrinted", isPrinted);
        values.put("Date", Date);

        long q = sql_db.update(DatabaseHandlers.TABLE_BILL_CB,values,"BillPrintNo=? AND Itemid=?",new String[]{bill_No, Itemid});
        Log.d("test", " values " + q+", "+ItemName);
        //  db.close();
    }

    public void deleteCustomer(String CName) {
     //   SQLiteDatabase db = this.getWritableDatabase();
        sql_db.execSQL("DELETE FROM " + DatabaseHandlers.TABLE_CART_ITEM_CB + " WHERE Cust_Name='" + CName + "'");
       //db.close();
    }

    public void delete_Customer(String CName) {
    //    SQLiteDatabase db = this.getWritableDatabase();
        sql_db.execSQL("DELETE FROM " + DatabaseHandlers.TABLE_CUSTOMER_CB + " WHERE FullName='" + CName + "'");
       // db.close();
    }

    public int getItems_AgainstCustomer(String CustName, String CustMob, String Product_id) {
        String countQuery = "SELECT  * FROM " + DatabaseHandlers.TABLE_CART_ITEM_CB + " WHERE Cust_Name='" + CustName
                + "' AND Cust_mob='" + CustMob + "' and Product_id='" + Product_id + "'";
        int count = 0;
     //   SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addMarchantPO(MarchantPOBean bean) {
    //    SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Amt", bean.getAmt());
        values.put("ItemId", bean.getItemId());
        values.put("Item_Name", bean.getItemName());
        values.put("MRP", bean.getMRP());

        values.put("Qty", bean.getQty());
        values.put("ShopName", bean.getShopName());
        values.put("TotAmt", bean.getTotAmt());
        values.put("Unit", bean.getPOUnit());
        long a = sql_db.insert(DatabaseHandlers.TABLE_GET_MARCHANT_PO, null, values);
        Log.d("test", " values " + values);
       // db.close();
    }

    public void deleteItemsMRP_Runi() {
        String selectQuery = "DELETE FROM " + DatabaseHandlers.TABLE_ITEM_MRP_Runi;
     //   SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int size = cursor.getCount();
        }
    }

    public void deleteMarchantItems() {
        String selectQuery = "DELETE FROM " + DatabaseHandlers.TABLE_MARCHANT_ITEM;
    //    SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int size = cursor.getCount();
        }
    }

    //TABLE_GET_MARCHANT_PO
    public void deletePO() {
        String selectQuery = "DELETE FROM " + DatabaseHandlers.TABLE_GET_MARCHANT_PO;
    //    SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int size = cursor.getCount();
        }
    }

    public void addMarchantItem(MarchantItemBean bean) {
     //   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FKCategoryId", bean.getFKCategoryId());
        values.put("FKitemmasterid", bean.getFKitemmasterid());
        values.put("FKsubcategoryid", bean.getFKsubcategoryid());
        values.put("PKVendoritemRelation", bean.getPKVendoritemRelation());
        values.put("categoryname", bean.getCategoryname());
        values.put("itemname", bean.getItemname());
        values.put("subcategoryname", bean.getSubcategoryname());
        values.put("vendorid", bean.getVendorid());
        //  values.put("MRP", bean.getMRP());
        // Inserting Row
        long a = sql_db.insert(DatabaseHandlers.TABLE_MARCHANT_ITEM, null, values);
        Log.d("test", " values " + values);
       // db.close(); // Closing database connection
    }

    public void addItemMRP_Runi(MarchantPOBean bean) {
     //   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Itemid", bean.getItemId());
        values.put("Itemname", bean.getItemName());
        values.put("OMrpV",bean.getOldMRP());
        values.put("PurchaseMRP",bean.getPOMRP());
        values.put("NMrpV", bean.getMRP());
        values.put("QtyV", bean.getQty());
        values.put("PurchaseUnit",bean.getPOUnit());
        values.put("UnitV", bean.getUnit());
        values.put("pkpurchaseid",bean.getPurchaseID());
        values.put("IsUploaded", bean.getIsUploaded());

        long a = sql_db.insert(DatabaseHandlers.TABLE_ITEM_MRP_Runi, null, values);
        Log.d("test", " values " + values);
        //  db.close(); // Closing database connection
    }

    public int getProductCount(String Product_id) {
        String countQuery = "SELECT * FROM " + DatabaseHandlers.TABLE_PRODUCT_CB + " WHERE Product_id ='" + Product_id + "'";

        int count = 0;
     //   SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addMarchantItemCB(MarchantItemBean bean) {
     //   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Product_id", bean.getFKitemmasterid());
        values.put("Product_name", bean.getItemname());
        values.put("MerchantId", bean.getVendorid());
        values.put("MerchantName", bean.getMerchantName());
        values.put("qnty", bean.getQty());
        values.put("unit", bean.getUnit());
        values.put("minqnty", bean.getMinqnty());
        values.put("offers", bean.getOffers());
        values.put("price", bean.getPrice());
        values.put("MRP", bean.getMRP());
        values.put("Freeitemid", bean.getFreeitemid());
        values.put("Freeitemqty", bean.getFreeitemqty());
        values.put("Freeitemname", bean.getFreeitemname());
        values.put("validfrom", bean.getValidfrom());
        values.put("validto", bean.getValidto());
        long a = sql_db.insert(DatabaseHandlers.TABLE_PRODUCT_CB, null, values);
        Log.d("test", " values " + values);
        // db.close(); // Closing database connection
    }

    //TABLE_MARCHANT_ITEM_RUNI
    public boolean getMarchantItemRuniCount() {
    //    SQLiteDatabase db = this.getReadableDatabase();
        String que = "SELECT * FROM " + DatabaseHandlers.TABLE_MARCHANT_ITEM_RUNI;
        Cursor cur = sql_db.rawQuery(que, null);
        if (cur.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getPurchaseItems_AgainstVendor(String Vendor) {
        String countQuery = "SELECT * FROM " + DatabaseHandlers.TABLE_PURCHASE_ITEM_CB + " WHERE vendor='" + Vendor
                + "'";
        int count = 0;
      //  SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = sql_db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void addFinalPurchase(String ItemName, String Rate, String Qty,
                                 String ItemDiscount, String Product_id, Float Amount,
                                 String TotalBill, String Discount,
                                 String FinalTotalBill, String vendor,
                                 String isUploaded, String date, String unit) {
      //  SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Product_name", ItemName);
        values.put("price", Rate);
        values.put("qnty", Qty);
        values.put("DISCOUNT", ItemDiscount);
        values.put("Amount_pcb", Amount);
        values.put("SubTotal", TotalBill);
        values.put("TotalDiscount", Discount);
        values.put("FinalTotal", FinalTotalBill);
        values.put("Product_id", Product_id);
        values.put("vendor", vendor);
        values.put("isUploaded", isUploaded);
        values.put("Date", date);
        values.put("UNIT", unit);
        long q = sql_db.insert(DatabaseHandlers.TABLE_PURCHASE_FINAL_ITEM_CB, null, values);
        Log.d("test", " values " + values);
      //  db.close();
    }

    public List<MyCartBean> getPoCartItems(String vendor_name) {
        List<MyCartBean> list = new ArrayList<MyCartBean>();
     //   SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseHandlers.TABLE_PURCHASE_FINAL_ITEM_CB + " WHERE vendor='" + vendor_name + "'";
        Cursor cur = sql_db.rawQuery(sql, null);
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                MyCartBean bean = new MyCartBean();
                bean.setProduct_id(cur.getString(cur.getColumnIndex("Product_id")));
                bean.setProduct_name(cur.getString(cur.getColumnIndex("Product_name")));
                bean.setPrice(Float.valueOf(cur.getString(cur.getColumnIndex("price"))));
                bean.setQnty(Float.valueOf(cur.getString(cur.getColumnIndex("qnty"))));
                bean.setAmount(Float.valueOf(cur.getString(cur.getColumnIndex("Amount_pcb"))));
                bean.setUNIT(cur.getString(cur.getColumnIndex("UNIT")));
                list.add(bean);
            } while (cur.moveToNext());
        }
        return list;
    }

    public void deleteMarchantItemsRuni() {
        String selectQuery = "DELETE FROM " + DatabaseHandlers.TABLE_MARCHANT_ITEM_RUNI;
     //   SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = sql_db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            int size = cursor.getCount();
        }
    }

    public void addMarchantItemRuni(MarchantPOBean bean) {
     //   SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ItemMasterId", bean.getItemId());
        values.put("ItemName", bean.getItemName());
        long a = sql_db.insert(DatabaseHandlers.TABLE_MARCHANT_ITEM_RUNI, null, values);
        Log.d("test", " values " + values);
      //  db.close(); // Closing database connection
    }

    public void addPurchase(String Product_name, String Product_id, String Qty,String ItemDiscount,
                            String Amount, String price, String vendor, String unit) {
      //  SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Product_name", Product_name);
        values.put("price", price);
        values.put("qnty", Qty);
        values.put("DISCOUNT", ItemDiscount);
        values.put("Amount", Amount);
        values.put("Product_id", Product_id);
        values.put("vendor", vendor);
        values.put("UNIT", unit);
        long q = sql_db.insert(DatabaseHandlers.TABLE_PURCHASE_ITEM_CB, null, values);
        Log.d("test", " values " + values);
       // db.close();
    }

    public void additmforbilling(String itmcode,String itmdesc,String qnty, String mrp,String rate,String lineamt, String discount,
                                 String taxclass,String taxamtinrups,String total_incl_taxanddisc,
                                 String taxinprcntg, String totwithdisc,String isbilluploaded, String discinrups, String discamt){

        ContentValues values = new ContentValues();

        values.put("itmcode", itmcode);
        values.put("itmdesc", itmdesc);
        values.put("qnty", qnty);
        values.put("mrp", mrp);
        values.put("rate", rate);
        values.put("lineamt", lineamt);
        values.put("discount", discount);
        values.put("taxclass",taxclass);
        values.put("taxamtinrups", taxamtinrups);
        values.put("total_incl_taxanddisc", total_incl_taxanddisc);
        values.put("taxinprcntg", taxinprcntg);
        values.put("totwithdisc",totwithdisc);
        values.put("isbilluploaded",isbilluploaded);
        values.put("isdiscinrupees",discinrups);
        values.put("discamt",discamt);

        long q = sql_db.insert(DatabaseHandlers.TABLE_ADD_ITMDTLS_FORBILL, null, values);
        Log.d("test", " values " + values);
    }


    /*----------------------------------------Sales - Entity, prospect, SHipment, Delivery ---------------------------------------------------*/
    public void addProspSettings(ProspectSetting psetting/*ArrayList<ProspectSetting> psetting*/){

        ContentValues values = new ContentValues();
       // values.put("PKFieldID", psetting.getPKFieldID());     //auto_incrememnt id
        values.put("FKProspectHdrID", psetting.getFKProspectHdrID());
        values.put("ProspectField", psetting.getProspectField());
        values.put("IsVisible", psetting.isIsvisiblecheck());
        values.put("IsMandatory", psetting.isIsmandatorycheck());
        values.put("Caption", psetting.getCaption());
        values.put("Section", psetting.getSection());
        values.put("FieldType", psetting.getFieldType());
        values.put("PKUserId", psetting.getPKUserId());

        long q = sql_db.insert(DatabaseHandlers.TABLE_PROSPECT_VALIDATIONS_SALES, null, values);
        Log.d("test", " values " + values);
    }

    public int getCount_prospectSetting(){
        String countQuery = "SELECT * FROM "+ DatabaseHandlers.TABLE_PROSPECT_VALIDATIONS_SALES;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery,null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
        }
        return count;
    }

    public void insertShipment(String InvoiceNo, String SONO,String SOheaderId, String OrderTypeMasterId, String SOScheduleId, String ShipmentQty,
                               String  LineAmt, String Rate, String TaxAmtdtl, String TotWithtaxdtl, String DiscAmountDtl,
                               String TotAmntWithDisc, String BaseAmt_final, String TotalTaxAmt_final, String TotTaxWithNet_final,
                               String TotDisc_final, String NetTotal, String DiscPc,String AddedDt,String expDelDate, String CustName,
                               String CustId,String ShipToMasterId, String DelvAddress, String isActivityAssigned){

        ContentValues values = new ContentValues();
        values.put("InvoiceNo",InvoiceNo);
        values.put("SONO",SONO );
        values.put("SOheaderId", SOheaderId);
        values.put("OrderTypeMasterId",OrderTypeMasterId );
        values.put("SOScheduleId",SOScheduleId );
        values.put("ShipmentQty",ShipmentQty );
        values.put("LineAmt",LineAmt);
        values.put("Rate",Rate );
        values.put("TaxAmtdtl",TaxAmtdtl );
        values.put("TotWithtaxdtl",TotWithtaxdtl );
        values.put("DiscAmountDtl",DiscAmountDtl );
        values.put("TotAmntWithDisc",TotAmntWithDisc );
        values.put("BaseAmt_final",BaseAmt_final );
        values.put("TotalTaxAmt_final",TotalTaxAmt_final );
        values.put("TotTaxWithNet_final",TotTaxWithNet_final);
        values.put("TotDisc_final",TotDisc_final );
        values.put("NetTotal",NetTotal );
        values.put("DiscPc",DiscPc );
        values.put("AddedDt",AddedDt);
        values.put("expDelDate",expDelDate);
        values.put("CustName",CustName);
        values.put("CustId",CustId);
        values.put("ShipToMasterId",ShipToMasterId);
        values.put("DelvAddress",DelvAddress);
        values.put("isActivityAssigned","N");

        long q = sql_db.insert(DatabaseHandlers.TABLE_SHIPMENT_INVOICE, null, values);
        Log.d("test", " values " + values);
    }

    public int getShipmentCount(){
        String countQuery = "SELECT * FROM "+ DatabaseHandlers.TABLE_SHIPMENT_INVOICE;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery,null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
        }
        return count;
    }

    public void insertAssignedActivity(String InvoiceNo, String DelAgentID, String DelAgentLoginID, String DelAgentName, String DelAgentMobile,
                                       String DelAgentEmail, String AssignActivityDesc, String Sono, String DeliveryDate, String CustomerName,
                                       String DeliveryAddress, String Pref_DeliveryTime_From, String Pref_DeliveryTime_To, String AddedDt,
                                       String OrderTypeMasterId){
        ContentValues values = new ContentValues();
        values.put("InvoiceNo",InvoiceNo);
        values.put("DelAgentID",DelAgentID);
        values.put("DelAgentLoginID", DelAgentLoginID);
        values.put("DelAgentName",DelAgentName);
        values.put("DelAgentMobile",DelAgentMobile);
        values.put("DelAgentEmail",DelAgentEmail);
        values.put("AssignActivityDesc",AssignActivityDesc);
        values.put("Sono",Sono);
        values.put("DeliveryDate",DeliveryDate);
        values.put("CustomerName",CustomerName);
        values.put("DeliveryAddress",DeliveryAddress);
        values.put("Pref_DeliveryTime_From",Pref_DeliveryTime_From);
        values.put("Pref_DeliveryTime_To",Pref_DeliveryTime_To);
        values.put("OrderTypeMasterId",OrderTypeMasterId);
        values.put("AddedDt",AddedDt);
        values.put("isActivityCompleted","N");

        long q = sql_db.insert(DatabaseHandlers.TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS, null, values);
        Log.d("test", " values " + values);
    }

    public int getActivityCount(){
        String countQuery = "SELECT * FROM "+ DatabaseHandlers.TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS;
        int count = 0;
        Cursor cursor = sql_db.rawQuery(countQuery,null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
        }
        return count;
    }

    public void insertCustomer(String CustVendorMasterId, String CustVendorName, String Email, String Mobile, String Address,String AddedBy){
        ContentValues values = new ContentValues();
        values.put("CustVendorMasterId",CustVendorMasterId);
        values.put("CustVendorName",CustVendorName);
        values.put("Email", Email);
        values.put("Mobile",Mobile);
        values.put("Address",Address);
        values.put("AddedBy",AddedBy);

        long q = sql_db.insert(DatabaseHandlers.TABLE_CONSIGNEES, null, values);
        Log.d("test", " values " + values);
    }

    public void insertShipTo(String RowID,String Action,String ConsigneeName,String ContactPerson,String Address,String City,
                             String Phone,String Fax,String Mobile,String Country,String State,String ShipToMasterId,
                             String Latitude,String Longitude,String Distance,String RouteMasterId,String CityName,String CountryName,
                             String StateName,String GeoLocation,String GSTCode,String GSTState,String GSTStateName,String Rating,
                             String TANNo,String TANNoName,String PAN,String EmailId,String isBlocked,String TAN_GSTIN_Number){
        ContentValues values = new ContentValues();

        values.put("RowID",RowID);
        values.put("Action",Action);
        values.put("ConsigneeName", ConsigneeName);
        values.put("ContactPerson",ContactPerson);
        values.put("Address",Address);
        values.put("City",City);
        values.put("Phone",Phone);
        values.put("Fax", Fax);
        values.put("Mobile",Mobile);
        values.put("Country",Country);
        values.put("State",State);
        values.put("ShipToMasterId",ShipToMasterId);
        values.put("Latitude", Latitude);
        values.put("Longitude",Longitude);
        values.put("Distance",Distance);
        values.put("RouteMasterId",RouteMasterId);
        values.put("CityName",CityName);
        values.put("CountryName", CountryName);
        values.put("StateName",StateName);
        values.put("GeoLocation",GeoLocation);
        values.put("GSTCode",GSTCode);
        values.put("GSTState",GSTState);
        values.put("Rating", Rating);
        values.put("TANNo", TANNo);
        values.put("TANNoName", TANNoName);
        values.put("PAN", PAN);
        values.put("EmailId", EmailId);
        values.put("isBlocked", isBlocked);
        values.put("TAN_GSTIN_Number", TAN_GSTIN_Number);

        long q = sql_db.insert(DatabaseHandlers.TABLE_SHIPTO_DETAILS, null, values);
        Log.d("test", " values " + values);
    }

    public void insertItemsData_CounterBilling(String ItemPlantId, String ItemCode, String ItemDesc, String ItemMRP,
                                               String TAXClass, String TaxAmount, String DiscountAmount){
        ContentValues values = new ContentValues();
        values.put("ItemPlantId",ItemPlantId);
        values.put("ItemCode",ItemCode);
        values.put("ItemDesc", ItemDesc);
        values.put("ItemMRP",ItemMRP);
        values.put("TAXClass",TAXClass);
        values.put("TaxAmount",TaxAmount);
        values.put("DiscountAmount",DiscountAmount);

        long q = sql_db.insert(DatabaseHandlers.TABLE_ADD_ITEMS_COUNTERBILL, null, values);
        Log.d("test", " values " + values);
    }

    public void insertTaxClass(String TaxClassMasterId, String TaxClassCode, String TaxClassDesc){
        ContentValues values = new ContentValues();
        values.put("TaxClassMasterId",TaxClassMasterId);
        values.put("TaxClassCode",TaxClassCode);
        values.put("TaxClassDesc", TaxClassDesc);
        long q = sql_db.insert(DatabaseHandlers.TABLE_TAXCLASS, null, values);
        Log.d("test", " values " + values);
    }

    public void insertCommission(String CommTypeId, String TypeCode, String TypeDesc,String VouMasterId){
        ContentValues values = new ContentValues();
        values.put("CommTypeId",CommTypeId);
        values.put("TypeCode",TypeCode);
        values.put("TypeDesc", TypeDesc);
        values.put("VouMasterId", VouMasterId);
        long q = sql_db.insert(DatabaseHandlers.TABLE_COMMISSION, null, values);
        Log.d("test", " values " + values);
    }

    public void insertCategory(String PKUserCategoryId, String CategoryDesc){
        ContentValues values = new ContentValues();
        values.put("PKUserCategoryId",PKUserCategoryId);
        values.put("CategoryDesc",CategoryDesc);
        long q = sql_db.insert(DatabaseHandlers.TABLE_CATEGORY_REIMB, null, values);
        Log.d("test", " values " + values);
    }

    public void insertWarranty(String WarrantyMasterId, String WarrantyDesc, String WarrantyCode){
        ContentValues values = new ContentValues();
        values.put("WarrantyMasterId",WarrantyMasterId);
        values.put("WarrantyDesc",WarrantyDesc);
        values.put("WarrantyCode",WarrantyCode);
        long q = sql_db.insert(DatabaseHandlers.TABLE_WARRANTY, null, values);
        Log.d("test", " values " + values);
    }

    public void insertUOM_new(String UOMMasterId, String UOMCode, String UOMDesc,String UOMDigit){
        ContentValues values = new ContentValues();
        values.put("UOMMasterId",UOMMasterId);
        values.put("UOMCode",UOMCode);
        values.put("UOMDesc",UOMDesc);
        values.put("UOMDigit",UOMDigit);
        long q = sql_db.insert(DatabaseHandlers.TABLE_UOM_new, null, values);
        Log.d("test", " values " + values);
    }

    public void insertCompanyDetails(String Cid, String Cname, String Address){
        ContentValues values = new ContentValues();
        values.put("Cid",Cid);
        values.put("Cname",Cname);
        values.put("Address",Address);

        long q = sql_db.insert(DatabaseHandlers.TABLE_COMPANY_DETAILS, null, values);
    }

    public void insertBookedSO(String SOHeaderId, String SONo, String SODate, String CustomerMasterId,String ConsigneeName, String NetAmt){
        ContentValues values = new ContentValues();
        values.put("SOHeaderId",SOHeaderId);
        values.put("SONo",SONo);
        values.put("SODate",SODate);
        values.put("CustomerMasterId",CustomerMasterId);
        values.put("ConsigneeName",ConsigneeName);
        values.put("NetAmt",NetAmt);

        long q = sql_db.insert(DatabaseHandlers.TABLE_SOHistory, null, values);
    }

    public void insertSchedule(String startDate, String endDate, String custDoorDate, String qty, String isPeriodic, String schId){
        ContentValues values = new ContentValues();
        values.put("startDate",startDate);
        values.put("endDate",endDate);
        values.put("custDoorDate",custDoorDate);
        values.put("qty",qty);
        values.put("isPeriodic",isPeriodic);
        values.put("schId",schId);
       // long q = sql_db.insert(DatabaseHandlers.TABLE_SCHEDULE, null, values);
    }

    public void insertModel(String ConfigurationDetailId, String Configuration, String Type){
        ContentValues values = new ContentValues();
        values.put("ConfigurationDetailId",ConfigurationDetailId);
        values.put("Configuration",Configuration);
        values.put("Type",Type);
         long q = sql_db.insert(DatabaseHandlers.TABLE_MODEL, null, values);
    }

    public void insertPricelist(String PListHDRId, String PListCode, String PListDesc){
        ContentValues values = new ContentValues();
        values.put("PListHDRId",PListHDRId);
        values.put("PListCode",PListCode);
        values.put("PListDesc",PListDesc);
        long q = sql_db.insert(DatabaseHandlers.TABLE_PRICELIST, null, values);
    }

    public void insertSalesFamily(String FamilyId, String FamilyCode, String FamilyDesc){
        ContentValues values = new ContentValues();
        values.put("FamilyId",FamilyId);
        values.put("FamilyCode",FamilyCode);
        values.put("FamilyDesc",FamilyDesc);
        long q = sql_db.insert(DatabaseHandlers.TABLE_SALES_FAMILY, null, values);
    }

    /**/

    public  void insert_OpenOrdersData(Context context,String sono,String SOHeaderId , String SODetailId , String ConsigneeName ,
                                       String CustomerMasterId , String ItemDesc , String ItemMasterId , String Qty , String OrgQty ,
                                       String Rate , String LineAmt , String TotalOrderValue , String SODate , String DoAck,
                                       String Range,String mrp,String distance,String UOMDigit,String UOMCode,String DeliveryTerms,
                                       String Mobile,String Brand,String Content,String ContentUOM,String SellingUOM,String PackOfQty,
                                       String FreeAboveAmt,String FreeDelyMaxDist,String MinDelyKg,String MinDelyKm,String ExprDelyWithinMin,
                                       String ExpressDelyChg){

        ContentValues values = new ContentValues();
        values.put("sono",sono);
        values.put("SOHeaderId",SOHeaderId);
        values.put("SODetailId",SODetailId);
        values.put("ConsigneeName",ConsigneeName);
        values.put("CustomerMasterId",CustomerMasterId);
        values.put("ItemDesc",ItemDesc);
        values.put("ItemMasterId",ItemMasterId);
        values.put("Qty",Qty);
        values.put("OrgQty",OrgQty);
        values.put("Rate",Rate);
        values.put("LineAmt",LineAmt);
        values.put("TotalOrderValue",TotalOrderValue);
        values.put("SODate",SODate);
        values.put("DoAck",DoAck);
        values.put("Range",Range);
        values.put("MRP",mrp);
        values.put("distance",distance);
        values.put("UOMDigit",UOMDigit);
        values.put("UOMCode",UOMCode);
        values.put("DeliveryTerms",DeliveryTerms);
        values.put("Mobile",Mobile);
        values.put("Brand",Brand);
        values.put("Content",Content);
        values.put("ContentUOM",ContentUOM);
        values.put("SellingUOM",SellingUOM);
        values.put("PackOfQty",PackOfQty);
        values.put("FreeAboveAmt", FreeAboveAmt);
        values.put("FreeDelyMaxDist", FreeDelyMaxDist);
        values.put("MinDelyKg", MinDelyKg);
        values.put("MinDelyKm", MinDelyKm);
        values.put("ExprDelyWithinMin", ExprDelyWithinMin);
        values.put("ExpressDelyChg", ExpressDelyChg);
        long q = sql_db.insert(DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE, null, values);

    }

    /*new order booking*/
    public void addSegmentMaster(String PKBusiSegmentID,String SegmentCode,String BusiImgPath,String MerchAliasName,String SegmentDescription){

        ContentValues cv = new ContentValues();
        cv.put("PKBusiSegmentID", PKBusiSegmentID);
        cv.put("SegmentCode", SegmentCode);
        cv.put("BusiImgPath", BusiImgPath);
        cv.put("MerchAliasName", MerchAliasName);
        cv.put("SegmentDescription", SegmentDescription);

        sql_db.insert(DatabaseHandlers.TABLE_BUS_SEGMENT, null, cv);
    }

    public void addFamilyMaster(String CategoryId,String CategoryName,String SubCategoryId,String SubCategoryName,String Cat_flag,
                                String CatImgPath,String SubCatImgPath,String SubCatCount,String ItemCount){

        ContentValues cv = new ContentValues();
        cv.put("CategoryId", CategoryId);
        cv.put("CategoryName", CategoryName);
        cv.put("SubCategoryId", SubCategoryId);
        cv.put("SubCategoryName", SubCategoryName);
        cv.put("Cat_flag", Cat_flag);
        cv.put("CatImgPath", CatImgPath);
        cv.put("SubCatImgPath", SubCatImgPath);
        cv.put("SubCatCount", SubCatCount);
        cv.put("ItemCount", ItemCount);

        sql_db.insert(DatabaseHandlers.TABLE_FAMILY_MASTERDATA, null, cv);
    }

    public void addAllCatSubcatItems_new(String CategoryId, String CategoryName, String SubCategoryId,
                                     String SubCategoryName, String ItemMasterId, String ItemName, String ItemImgPath,String ItemMRP,
                                     String custVendorname, String TypeFixedPercent, String validfrom, String validto,
                                     String DisRate, String NetRate, String Freeitemid, String Freeitemqty, String Minqty, String Discratepercent,
                                     String DiscrateMRP, String PurDigit, String CustVendorMasterId, String PricelistId,
                                     String PricelistRate,String UOMCode,String Range,String OutOfStock,String MinOrdQty, String MaxOrdQty,
                                     String Distance,String Brand,String Content,String ContentUOM,String SellingUOM,String CatImgPath,
                                     String SubCatImgPath,String PackOfQty, String FreeAboveAmt, String FreeDelyMaxDist, String MinDelyKg,
                                     String MinDelyKm, String ExprDelyWithinMin,String ExpressDelyChg,String Open_slots, String OpenTime1,
                                     String OpenTime2, String CloseTime1, String CloseTime2,String IsDelivery,String UPI) {
        ContentValues cv = new ContentValues();
        cv.put("CategoryId", CategoryId);
        cv.put("CategoryName", CategoryName);
        cv.put("SubCategoryName", SubCategoryName);
        cv.put("ItemName", ItemName);
        cv.put("itemmasterid", ItemMasterId);
        cv.put("SubCategoryId", SubCategoryId);
        cv.put("ItemImgPath", ItemImgPath);
        cv.put("itemMRP",ItemMRP );
        cv.put("CustVendorMasterId", CustVendorMasterId);
        cv.put("custVendorname", custVendorname);
        cv.put("TypeFixedPercent", TypeFixedPercent);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("DisRate", DisRate);
        cv.put("NetRate", NetRate);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Minqty", Minqty);
        cv.put("Discratepercent", Discratepercent);
        cv.put("DiscrateMRP", DiscrateMRP);
        cv.put("PurDigit", PurDigit);
        cv.put("PricelistId", PricelistId);
        cv.put("PricelistRate", PricelistRate);
        cv.put("Cat_flag", "");
        cv.put("SubCat_flag", "");
        cv.put("UOMCode", UOMCode);
        cv.put("Range", Range);
        cv.put("MinOrdQty", MinOrdQty);
        cv.put("MaxOrdQty", MaxOrdQty);
        cv.put("Distance", Distance);
        cv.put("OutOfStock", OutOfStock);
        cv.put("Brand", Brand);
        cv.put("Content", Content);
        cv.put("ContentUOM", ContentUOM);
        cv.put("SellingUOM", SellingUOM);
        cv.put("CatImgPath", CatImgPath);
        cv.put("SubCatImgPath", SubCatImgPath);
        cv.put("PackOfQty", PackOfQty);
        cv.put("FreeAboveAmt", FreeAboveAmt);
        cv.put("FreeDelyMaxDist", FreeDelyMaxDist);
        cv.put("MinDelyKg", MinDelyKg);
        cv.put("MinDelyKm", MinDelyKm);
        cv.put("ExprDelyWithinMin", ExprDelyWithinMin);
        cv.put("ExpressDelyChg", ExpressDelyChg);
        cv.put("Open_slots", Open_slots);
        cv.put("OpenTime1", OpenTime1);
        cv.put("OpenTime2", OpenTime2);
        cv.put("CloseTime1", CloseTime1);
        cv.put("CloseTime2", CloseTime2);
        cv.put("IsDelivery", IsDelivery);
        cv.put("UPI", UPI);
        sql_db.insert(DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS_new, null, cv);
    }

    public void addCartItems_new(String MerchantId, String MerchantName, String qnty, String minqnty,
                             String offers, String price, String Product_name, String Amount, String Product_id,
                             String Freeitemid, String Freeitemqty, String Freeitemname, String validfrom, String validto,
                             String ItemImgPath,String CategoryId, String CategoryName, String SubCategoryId, String SubCategoryName,
                             String MRP,String MaxOrdQty,String Range,String OutOfStock,String Distance, String UomDigit,
                             String UOMCode,String Brand,String Content,String ContentUOM,String SellingUOM,String PackOfQty,
                             String FreeAboveAmt, String FreeDelyMaxDist, String MinDelyKg,
                             String MinDelyKm, String ExprDelyWithinMin,String ExpressDelyChg,String Open_slots, String OpenTime1,
                             String OpenTime2, String CloseTime1, String CloseTime2,String IsDelivery, String UPI) {

        ContentValues cv = new ContentValues();
        cv.put("MerchantId", MerchantId);
        cv.put("MerchantName", MerchantName);
        cv.put("qnty", qnty);
        cv.put("minqnty", minqnty);
        cv.put("offers", offers);
        cv.put("price", price);
        cv.put("Product_name", Product_name);
        cv.put("Amount", Amount);
        cv.put("Product_id", Product_id);
        cv.put("Freeitemid", Freeitemid);
        cv.put("Freeitemqty", Freeitemqty);
        cv.put("Freeitemname", Freeitemname);
        cv.put("validfrom", validfrom);
        cv.put("validto", validto);
        cv.put("ItemImgPath", ItemImgPath);
        cv.put("CategoryId", CategoryId);
        cv.put("CategoryName", CategoryName);
        cv.put("SubCategoryId", SubCategoryId);
        cv.put("SubCategoryName", SubCategoryName);
        cv.put("MRP", MRP);
        cv.put("MaxOrdQty", MaxOrdQty);
        cv.put("Range", Range);
        cv.put("OutOfStock", OutOfStock);
        cv.put("Distance", Distance);
        cv.put("UomDigit", UomDigit);
        cv.put("UOMCode", UOMCode);
        cv.put("Brand", Brand);
        cv.put("Content", Content);
        cv.put("ContentUOM", ContentUOM);
        cv.put("SellingUOM", SellingUOM);
        cv.put("PackOfQty", PackOfQty);
        cv.put("FreeAboveAmt", FreeAboveAmt);
        cv.put("FreeDelyMaxDist", FreeDelyMaxDist);
        cv.put("MinDelyKg", MinDelyKg);
        cv.put("MinDelyKm", MinDelyKm);
        cv.put("ExprDelyWithinMin", ExprDelyWithinMin);
        cv.put("ExpressDelyChg", ExpressDelyChg);
        cv.put("Open_slots", Open_slots);
        cv.put("OpenTime1", OpenTime1);
        cv.put("OpenTime2", OpenTime2);
        cv.put("CloseTime1", CloseTime1);
        cv.put("CloseTime2", CloseTime2);
        cv.put("AppliedDelCharges", "0");
        cv.put("IsDelivery", IsDelivery);
        cv.put("UPI", UPI);
        long q = sql_db.insert(DatabaseHandlers.TABLE_CART_ITEM_new, null, cv);
    }

    public void insertPendingDeliveries(String InvoiceNo, String InvoiceDt,String SODate, String ShipToMasterId, String OrderTypeMasterId,
                                        String SONo, String  ConsigneeName, String Address, String PrefDelFrmTime, String PrefDelToTime,
                                        String Mobile, String Latitude, String Longitude, String deliveryterms, String PaymentStatus,
                                        String TransactionId, String TransactionDate, String PaymentMode,String AmountStatus,String ItemDesc,
                                        String UOMCode, String Brand,String Content, String Qty, String Rate,String NetAmt){

        ContentValues values = new ContentValues();
        values.put("InvoiceNo",InvoiceNo);
        values.put("InvoiceDt",InvoiceDt );
        values.put("SODate", SODate);
        values.put("ShipToMasterId",ShipToMasterId );
        values.put("OrderTypeMasterId",OrderTypeMasterId );
        values.put("SONo",SONo );
        values.put("ConsigneeName",ConsigneeName);
        values.put("Address",Address);
        values.put("PrefDelFrmTime",PrefDelFrmTime);
        values.put("PrefDelToTime",PrefDelToTime );
        values.put("Mobile",Mobile);
        values.put("Latitude",Latitude);
        values.put("Longitude",Longitude);
        values.put("deliveryterms",deliveryterms);
        values.put("PaymentStatus",PaymentStatus);
        values.put("TransactionId",TransactionId);
        values.put("TransactionDate",TransactionDate);
        values.put("PaymentMode",PaymentMode);
        values.put("AmountStatus",AmountStatus);
        values.put("ItemDesc",ItemDesc);
        values.put("UOMCode",UOMCode);
        values.put("Brand",Brand);
        values.put("Content",Content);
        values.put("Qty",Qty);
        values.put("Rate",Rate);
        values.put("NetAmt",NetAmt);

        long q = sql_db.insert(DatabaseHandlers.TABLE_PENDING_DELIVERY, null, values);
        Log.d("test", " values " + values);
    }

    public void clearTable_fullpackd(Context parent, String tablename,String Status,String shipStatus,String calltype) {
        sql_db = dbhelper.getWritableDatabase();

        String qry = "";
        if(calltype.equalsIgnoreCase("FullPacked")){
            qry = "Delete from "+tablename+" WHERE status='"+Status+"' AND ShipStatus!='"+shipStatus+"'";
        }else {
            qry = "Delete from "+tablename+" WHERE status='"+Status+"' AND ShipStatus='"+shipStatus+"'";
        }

        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();

        }
    }

    public  void clearTable_OrdHistory(Context parent, String tablename,String Status) {
        sql_db = dbhelper.getWritableDatabase();

        String qry = "Delete from "+tablename+" WHERE status='"+Status+"'";
        Cursor c = sql_db.rawQuery(qry,null);
        if(c.getCount()>0){
            c.moveToFirst();

        }
    }


}
