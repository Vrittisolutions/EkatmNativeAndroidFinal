package com.vritti.sales.data;

public class AnyMartDatabaseConstants {
    public static final int DATABASE_VERSION = 21;

    public static final String DATABASE_NAME = "OrderBillingDB";
    public static String DATABASE__NAME_URL;

    public static final String TABLE_URL_COMPANYDOMAIN = "Company_URLs";
    public static final String TABLE_USER = "User";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_ALL_CAT_SUBCAT_ITEMS = "getAllCatSubItem";
    public static final String TABLE_ALL_CAT_SUBCAT_ITEMS_MARCHANT = "getAllCatSubItem_marchant";
    public static final String TABLE_MERCHANT_AGAINST_ITEM = "getMerchantsAgainstItems";
    public static final String TABLE_CART_ITEM = "getCartItems";
    public static final String TABLE_CART_ITEM_VOLUME_DISCOUNT = "getCartItemsVolumeDiscount";
    public static final String TABLE_PLACE_ORDER = "getPlaceOrder";
    public static final String TABLE_MY_ORDER_HISTORY = "MyOrderHistory";
    public static final String TABLE_MY_ORDER_HISTORY_HEADERDATA = "MyOrderHistory_headerdata";
    public static final String TABLE_VEHICLE = "getVehicles";
    public static final String TABLE_MY_ADDRESS = "getAddress";
    public static final String TABLE_MERCHANTS = "getMerchants";
    public static final String TABLE_NOTIFICATION = "getNotification";
    public static final String TABLE_STATE = "getStates";
    public static final String TABLE_CITY = "getCities";
    public static final String TABLE_CHECKDATA = "CheckedData";
    public static final String TABLE_SHIPMENT_DTL_STORE_DATA = "ShipmentDtldata";

    public static final String CREATE_TABLE_URL_COMPANYDOMAIN = "CREATE TABLE "
            + TABLE_URL_COMPANYDOMAIN
            + "(CompanyId INTEGER PRIMARY KEY AUTOINCREMENT, Url TEXT, LoginId TEXT, CustVendorMasterId TEXT, DBName TEXT)";

    public static final String CREATE_TABLE_USER = "CREATE TABLE "
            + TABLE_USER
            + "(UserId TEXT, FullName TEXT, Email TEXT, Mobile TEXT," +
            "Address TEXT,City TEXT,State TEXT,Pin TEXT,CustVendType TEXT , " +
            "PermanentAddress TEXT, GpsLocationAddress TEXT, Latitude TEXT, Longitude TEXT, " +
            " CurrentAddress  TEXT,OfficeAddress TEXT, type TEXT,  custvendormasterid TEXT," +
            "BQM_BanquetClientId TEXT, ContPerName TEXT, ContactNo TEXT, Designation TEXT)";

    public static final String CREATE_TABLE_CHECKDATA = "CREATE TABLE"
            + TABLE_CHECKDATA
            + "(ItemName TEXT, ItemID TEXT, isChecked TEXT, ItemQty TEXT)";


    public static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE "

            + TABLE_NOTIFICATION
            + "(notificationNumber INTEGER PRIMARY KEY AUTOINCREMENT, notification_desc TEXT," +
            "type TEXT)";

    public static final String CREATE_TABLE_STATES = "CREATE TABLE "
            + TABLE_STATE
            + "(state_id TEXT, state_name TEXT)";

    public static final String CREATE_TABLE_CITIES = "CREATE TABLE "
            + TABLE_CITY
            + "(city_id TEXT, city_name TEXT)";

    public static final String CREATE_TABLE_VEHICLE = "CREATE TABLE "
            + TABLE_VEHICLE
            + "(VehicleMasterId  TEXT, TransporterCodeId  TEXT," +
            "VehicleNumber TEXT)";


    public static final String CREATE_TABLE_MY_ADDRESS = "CREATE TABLE "

            + TABLE_MY_ADDRESS
            + "(UserId TEXT ,  Mobile TEXT," +
            "CustVendType TEXT ,PermanentAddress TEXT, GpsLocationAddress TEXT, Latitude TEXT, Longitude TEXT, " +
            " CurrentAddress  TEXT,OfficeAddress TEXT, type TEXT)";

    public static final String CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS = "CREATE TABLE "
            + TABLE_ALL_CAT_SUBCAT_ITEMS
            + "(CategoryId TEXT, CategoryName TEXT, SubCategoryName TEXT, ItemName TEXT," +
            "itemmasterid TEXT, SubCategoryId TEXT, ItemImgPath TEXT,ItemPrice TEXT, ItemQty TEXT, isChecked TEXT, itemMRP TEXT," +
            "custVendorname TEXT,TypeFixedPercent TEXT, validfrom TEXT, validto TEXT, DisRate TEXT, NetRate TXET, Freeitemid TEXT," +
            " Freeitemqty TEXT, Minqty TEXT,Discratepercent TEXT, DiscrateMRP TEXT, PurDigit TEXT, CustVendorMasterId TEXT, " +
            "PricelistId TEXT, PricelistRate TEXT)";

    public static final String CREATE_TABLE_CATEGORY_MARCHANT = "CREATE TABLE "
            + TABLE_ALL_CAT_SUBCAT_ITEMS_MARCHANT
            + "(CategoryId TEXT, CategoryName TEXT, SubCategoryName TEXT, " +
            "ItemName TEXT,ItemMasterId TEXT, SubCategoryId TEXT)";

    public static final String CREATE_TABLE_MERCHANT_AGAINST_ITEM = "CREATE TABLE "
            + TABLE_MERCHANT_AGAINST_ITEM
            + "(MerchantId TEXT , MerchantName TEXT, qnty TEXT, minqnty TEXT, offers TEXT,price TEXT," +
            " Product_name TEXT, Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT," +
            "validfrom TEXT, validto TEXT)";
    public static final String CREATE_TABLE_CART_ITEM = "CREATE TABLE "
            + TABLE_CART_ITEM
            + "(Cartid INTEGER PRIMARY KEY AUTOINCREMENT, MerchantId TEXT , MerchantName TEXT, qnty TEXT, " +
            "minqnty TEXT, offers TEXT,price TEXT, Product_name TEXT, Amount TEXT,Product_id TEXT " +
            ",Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT, validfrom TEXT, validto TEXT,ItemImgPath TEXT)";

    public static final String CREATE_TABLE_MERCHANTS = "CREATE TABLE "
            + TABLE_MERCHANTS
            + "(MerchantId TEXT , MerchantName TEXT, qnty integer, minqnty integer, offers TEXT,price integer," +
            " Product_name TEXT, Freeitemid TEXT, Freeitemqty  integer,Freeitemname TEXT," +
            "validfrom TEXT, validto TEXT," +
            " Merchant_Name_Two TEXT, MerchantAddress TEXT, MerchantEmail TEXT, MerchantMobile TEXT )";


    public static final String CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT = "CREATE TABLE "
            + TABLE_CART_ITEM_VOLUME_DISCOUNT
            + "(CVid INTEGER PRIMARY KEY AUTOINCREMENT, MerchantId TEXT , MerchantName TEXT, " +
            "minvalue integer, netrate TEXT,freeitemid TEXT,freeitem_name TEXT , freeitemqnty integer," +
            " validfrom TEXT, validto TEXT, CouponId TEXT ,discount TEXT,FKVendorItemMasterId TEXT, FKVendorProductmasterId TEXT )";

    public static final String CREATE_TABLE_PLACE_ORDER = "CREATE TABLE "
            + TABLE_PLACE_ORDER
            + "(Pid INTEGER PRIMARY KEY AUTOINCREMENT, C_V_type TEXT , schedule_date_time TEXT, xml1 TEXT, xml2 TEXT, placeOrderDate TEXT, isUploaded TEXT)";

    public static final String CREATE_TABLE_MY_ORDER_HISTORY = "CREATE TABLE "
            + TABLE_MY_ORDER_HISTORY
            + "(OrdHtryid INTEGER PRIMARY KEY AUTOINCREMENT, Address TEXT, City TEXT, ConsigneeName TEXT, CustomerMasterId TEXT," +
            " ItemMasterId TEXT, Mobile TEXT, Qty TEXT, Rate TEXT, SODate TEXT, SOHeaderId TEXT," +
            "DODisptch TEXT ,DORcvd TEXT,status TEXT, statusname TEXT, DoAck TEXT, NetAmt Text, ItemDesc TEXT, LineAmt TEXT, merchantid TEXT," +
            "merchantname TEXT, placeOrderDate TEXT, SODetailId TEXT, SOScheduleId TEXT, ShipmentQty TEXT, ClientRecQty TEXT, AppvDt TEXT," +
            " PurDigit TEXT, DOrej TEXT, DispatchNo TEXT, SalesHeaderId TEXT, SalesDtlId TEXT, DispNetAmnt TEXT, ShipStatus TEXT," +
            " OrdRcvdDate, sono TEXT)";/*SalesDtlId*/

    public static final String CREATE_TABLE_MY_ORDER_HISTORY_HEADERDATA = "CREATE TABLE "
            + TABLE_MY_ORDER_HISTORY_HEADERDATA
            + "(OrdHtryid INTEGER PRIMARY KEY AUTOINCREMENT, Address TEXT, City TEXT, ConsigneeName TEXT, CustomerMasterId TEXT," +
            " ItemMasterId TEXT, Mobile TEXT, Qty TEXT, Rate TEXT, SODate TEXT, SOHeaderId TEXT," +
            "DODisptch TEXT ,DORcvd TEXT,status TEXT, statusname TEXT, DoAck TEXT, NetAmt Text, ItemDesc TEXT, LineAmt TEXT, merchantid TEXT," +
            "merchantname TEXT, placeOrderDate TEXT, SODetailId TEXT, SOScheduleId TEXT, ShipmentQty TEXT, ClientRecQty TEXT, AppvDt TEXT," +
            " PurDigit TEXT, DOrej TEXT, DispatchNo TEXT, SalesHeaderId TEXT, sono TEXT)";

    public static final String CREATE_TABLE_SHIPMENT_DTL_STORE_DATA = "CREATE TABLE "
            + TABLE_SHIPMENT_DTL_STORE_DATA
            + "(OrdHtryid INTEGER PRIMARY KEY AUTOINCREMENT, Address TEXT, City TEXT, ConsigneeName TEXT, CustomerMasterId TEXT," +
            " ItemMasterId TEXT, Mobile TEXT, Qty TEXT, Rate TEXT, SODate TEXT, SOHeaderId TEXT," +
            "DODisptch TEXT ,DORcvd TEXT,status TEXT, statusname TEXT, DoAck TEXT, NetAmt Text, ItemDesc TEXT, LineAmt TEXT, merchantid TEXT," +
            "merchantname TEXT, placeOrderDate TEXT, SODetailId TEXT, SOScheduleId TEXT, ShipmentQty TEXT, ClientRecQty TEXT, AppvDt TEXT," +
            " PurDigit TEXT, DOrej TEXT, DispatchNo TEXT, SalesHeaderId TEXT, DispNetAmnt TEXT, sono TEXT)";



}
