package com.vritti.sales.data;

import android.widget.ListView;

import com.vritti.sales.beans.AddCategoryBean;
import com.vritti.sales.beans.AddSubCategoryBean;
import com.vritti.sales.beans.AdditemBean;
import com.vritti.sales.beans.MarchantItemBean;
import com.vritti.sales.beans.MarchantPOBean;
import com.vritti.sales.beans.Merchants_against_items;
import com.vritti.sales.beans.MyCartBean;
import com.vritti.sales.beans.ProductListBean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AnyMartData {
    public static final String SENDER_ID = "43827645544";
    public static final String GOOGLE_API_KEY = "43827645544";
    public static final String DISPLAY_MESSAGE_ACTION = "com.vritti.orderbilling.data.DISPLAY_MESSAGE";
    public static final String EXTRA_MESSAGE = "message";
    public static final String MESSAGE_KEY = "message";
    public static final int SPLASH_TIME_OUT = 300;
    public static final String SESSION_TIME_OUT = "15";
    public static final String INSTANCE = " ";
    public static final String PINK = "#FC54FF";
    public static final String VERSION = "1";
    public static String CUSTOMER_TYPE = "V";
    public static final String VENDOR_TYPE = "C";
    public static final String OTP = "OTP";
    public static final String IS_AGENCY = "isAgency";
    public static final String PRODUCT_CATEGORY_ID = "PRODUCT CATEGORY ID";
    public static final String ENGLISH = "English";
    public static final String HINDI = "Hindi";
    public static final String MARATHI = "Marathi";
    public static final String ORDER_SUMMARY = "Order Summary";

    public static final String StatusApproved = "AllowOrdApprStatusPer_OB";
    public static final String StatusDelivered = "AllowOrdDelvStatusPer_OB";
    public static final String StatusReceived = "AllowOrdRecStatusPer_OB";

    public static String OType1 = "General";
    public static String OType2 = "Domestic";
    public static String OType3 = "Export";
    public static String OType4 = "Whosale";
    public static String OType5 = "Retail";

    public static String OTP_INPUT = null;
    public static String OTP_SMS = null;
    public static String USER_ID = null;
    public static String CV_ID = null;
    public static String DEVICE_ID = null;
    public static String SESSION_ID = null;
    public static String HANDLE = null;
    public static String MOBILE = null;
    public static String MOBILE_CUST = null;
    public static String REG_MOBILE = null;
    public static String CategoryID = null;
    public static String SUbcatID = null;
    public static String Permission_Flag = "";
    public static int Permission_Flag_cnt;
    public static boolean perflag = false;
    public static String Order_Type = "";
    public static String OrderTypeMasterId = "";
  /*  public static String LATITUDE = "0.0d";
    public static String LONGITUDE = "0.0d";*/

    public static String Appr_Perm_desc_id = "";
    public static String Del_Perm_desc_id = "";
    public static String Recv_Perm_desc_id = "";

    public static String Appr_Perm_desc = "";
    public static String Del_Perm_desc = "";
    public static String Recv_Perm_desc = "";

    public static String Appr_Perm_setupvalue = "";
    public static String Del_Perm_setupvalue = "";
    public static String Recv_Perm_setupvalue= "";

    public static String Appr_Perm_dispvalue = "";
    public static String Del_Perm_dispvalue = "";
    public static String Recv_Perm_dispvalue= "";

    public static final String MY_PREFS_NAME = "MyPrefs";

    public static final String SERVER_HOST_URL = "http://192.168.1.207:421/";
    /*http://vritti.co.in:420/api/geturl?key=PETROTEST*/
    public static final String SERVER_URL = "http://vritti.co.in:420/api/GetUrl?key=";
    //public static String URL = null;
    //public static String URL = "http://petrosoft.ekatm.com/Service1.svc";
    public static String URL = "http://bakery.ekatm.com:420/Service1.svc";
    //public static String URL = "http://192.168.1.143/OrderBilling/Service1.svc";
    public static String ADD_URL = "/Service1.svc";
    /*public static final String URL = "http://192.168.1.207:421/Service1.svc";*/

    //************************************API's BY CHETANA********************************************************//
    //***********************************CUSTOMER SIDE API's*****************************************************//

    public static final String API_GETSESSIONS_EKATM = "/api/LoginAPI/GetSessions";

    public static String MAIN_URL;

    //public static final String  MAIN_URL = "http://h207.ekatm.com/api/OrderBillingAPI/";
    // public static final String MAIN_URL = "http://192.168.1.143/api/OrderBillingAPI/";

    //public static String LOGO_URL = "/Ekatm/GetCompanyLogo";
    public static String LOGO_URL = "GetCompanyLogo";

    public static final String OTP_URL = "http://192.168.1.143/api/OrderBillingAPI/";

    //public static final String LOGIN_API_URL_MAIN = "http://h207.ekatm.com/Ekatm/Index/";

    public static final String METHOD_GET_STATESLIST = "getStatelist";

    public static final String METHOD_GET_CITYLIST = "getCitylist";

    public static final String METHOD_SESSION_ACTIVATE_1 = "sesActivate1";
    public static final String API_ACTION_SESSION_ACTIVATE_1 = "http://h207.ekatm.com/api/OrderBillingAPI/sesActivate1?Mobileno=&version=";
    public static final String SOAP_ACTION_SESSION_ACTIVATE_1 = "http://tempuri.org/IService1/sesActivate1";

    public static final String METHOD_SESSION_ACTIVATE_2 = "SesActivate2";
    public static final String API_ACTION_SESSION_ACTIVATE_2 = "http://h207.ekatm.com/api/OrderBillingAPI/sesActivate2?Mobileno=&SessionId=";
    public static final String SOAP_ACTION_SESSION_ACTIVATE_2 = "http://tempuri.org/IService1/SesActivate2";

    public static final String METHOD_SESSION_ACTIVATE_3 = "SesActivate3";
    public static final String API_ACTION_SESSION_ACTIVATE_3 =
            "http://h207.ekatm.com/api/OrderBillingAPI/sesActivate3?SessionId=&Handle=&strSessionTime=&Instance=";
    public static final String SOAP_ACTION_SESSION_ACTIVATE_3 = "http://tempuri.org/IService1/SesActivate3";

    public static final String METHOD_MOBILE_VERIFICATION = "getVerificationData";

    public static final String METHOD_USER_REGISTRATION = "Authentication_new";

    public static final String METHOD_MODULE_SETUP_VALUES = "getModuleSetupValue";

    public static final String METHOD_GENERATE_ORDER = "generateorder";
    public static final String SOAP_ACTION_GENERATE_ORDER = "http://tempuri.org/IService1/generateorder";

    public static final String METHOD_ORDER_HISTORY = "getorderhistory";
    public static final String SOAP_ACTION_ORDER_HISTORY = "http://tempuri.org/IService1/getorderhistory";

    public static final String METHOD_PENDING_ORDER_HISTORY = "getPendingrderHistory";

    public static final String METHOD_PENDING_ORDERS_SHIPMENTS = "getPendingordersforvendors";

    public static final String api_postOrderAcceptance = "PostOrderAcceptance";
    public static final String api_getOrderAcceptance_SOList = "GetOrderAcceptanceSO";
    //public static final String api_getCounterBillHistory = "/api/OrderBillingAPI/GetCounterBillHistory";
    public static final String api_getCounterBillHistory = "GetCounterBillHistory";

    //http://anymart1.ekatm.com/api/OrderBillingAPI/GetOrderAcceptanceSO?AppEnvMasterId=anymart1&PlantId=1&UserMasterId=55d200a2-05c9-483d-8143-f25d9aa59d30&Mobile=8421820765

    public static final String METHOD_CONFIRM_RCV_STATUS_QTY = "getrcvstatuscnfrm";
    public static final String METHOD_SHIPMENT_DETAILS = "getShipmentDtlData";

    public static final String METHOD_USER_UPDATE = "UpdateAuthentication_new";
    public static final String SOAP_ACTION_USER_UPDATE = "http://tempuri.org/IService1/UpdateAuthentication_new";

    public static final String METHOD_GET_USER_DETAILS = "getuserdetails";
    public static final String SOAP_ACTION_GET_USER_DETAILS = "http://tempuri.org/IService1/getuserdetails";

    //getNotificationMaster
    public static final String METHOD_NotificationMaster = "getNotificationMaster";
    public static final String SOAP_ACTION_NotificationMaster = "http://tempuri.org/IService1/getNotificationMaster";

    public static final String METHOD_GET_ALL_ADDRESS = "getAllAddressForOrder";
    public static final String SOAP_ACTION_GET_ALL_ADDRESS = "http://tempuri.org/IService1/getAllAddressForOrder";

    //InsertAddress
    public static final String METHOD_Insert_Address = "InsertAddress";
    public static final String SOAP_ACTION_Insert_Address = "http://tempuri.org/IService1/InsertAddress";

    public static final String METHOD_DEVICE_REGISTRATION = "getRegisterDevice";
    public static final String METHOD_GENERATE_OTP = "GetGenerateOTP";
    public static final String METHOD_GENERATE_NEW_OTP = "GetGenerateNewOTP";

    public static final String METHOD_DELETE_ORDER = "DeleteOrder";
    public static final String SOAP_ACTION_DELETE_ORDER   = "http://tempuri.org/IService1/DeleteOrder";

    /////////////////////////////// vendor //////////////////////////////////////////////////////////////////////
    public static final String METHOD_GET_VENDOR_ON_PRODUCTNAME = "getDiscountItemONProductName";
    public static final String SOAP_ACTION_VENDOR_ON_PRODUCTNAME = "http://tempuri.org/IService1/getDiscountItemONProductName";

    public static final String METHOD_GET_VOLUME_DISCOUNT = "getVolumeDiscountItemONProductName";
    public static final String SOAP_ACTION_VOLUME_DISCOUNT = "http://tempuri.org/IService1/getVolumeDiscountItemONProductName";

    public static final String METHOD_ORDER_HISTORY_SO_DETAILS = "getSODetail";

    public static final String METHOD_ORDER_HISTORY_SHIPMENT_DETAILS = "getSalesDetail";

    public static final String METHOD_GET_ORDER_DETAILS_TO_COPY_ORDER = "GetOrderDetail";

    public static final String METHOD_ADD_USERS = "addhierarchy";
    public static final String SOAP_ACTION_ADD_USERS = "http://tempuri.org/IService1/addhierarchy";

    public static String METHOD_ORDER = "getbookedOrdVendorheader";
    public static String SOAP_ACTION_ORDER_LIST = "http://tempuri.org/IService1/getbookedOrdVendorheader";

    public static final String METHOD_BILL_DETAILS = "OrderBookRuni";
    public static final String SOAP_ACTION_BILL_DETAILS = "http://tempuri.org/IService1/OrderBookRuni";

    public static final String METHOD_INSERT_VOLUME_DISCOUNT = "insertVolumeDiscount";
    public static final String SOAP_ACTION_INSERT_VOLUME_DISCOUNT = "http://tempuri.org/IService1/insertVolumeDiscount";

    public static final String METHOD_INSERT_TRADE_DISCOUNT = "insertTradeDiscount";
    public static final String SOAP_ACTION_INSERT_TRADE_DISCOUNT = "http://tempuri.org/IService1/insertTradeDiscount";

    ///////Vendor API's
 /*   public static final String METHOD_INSERT_ITEM_MRP_RUNI_VENDOR = "InsertMRPForItemsRuni";
    public static final String SOAP_ACTION_INSERT_ITEM_MRP_Runi_VENDOR = "http://tempuri.org/IService1/InsertMRPForItemsRuni";
*/
    public static final String METHOD_ITEMS_FOR_VENDOR = "getItemsForVendor";
    public static final String SOAP_ACTION_ITEMS_FOR_VENDOR = "http://tempuri.org/IService1/getItemsForVendor";

    public static final String METHOD_ITEMS_MRP_Runi = "getitemRPRuni";
    public static final String SOAP_ACTION_ITEMS_MRP_Runi = "http://tempuri.org/IService1/getitemRPRuni";

    public static final String METHOD_GET_MARCHANT_PO = "getPurchaseRuni";
    public static final String SOAP_ACTION_GET_MARCHANT_PO = "http://tempuri.org/IService1/getPurchaseRuni";

    public static final String METHOD_BALANCE = "getruniheader";
    public static final String SOAP_ACTION_BALANCE = "http://tempuri.org/IService1/getruniheader";

    public static final String METHOD_REPORTS = "getReport";
    public static final String SOAP_ACTION_REPORTS = "http://tempuri.org/IService1/getReport";

    public static final String METHOD_ITEMS_FOR_VENDOR_Runi = "getAllItemsRuni";
    public static final String SOAP_ACTION_ITEMS_FOR_VENDOR_Runi = "http://tempuri.org/IService1/getAllItemsRuni";

    //Vendor Side Navigation Drawer
    public static final String METHOD_INSERT_ITEM_AGAINST_VENDOR = "InsertItems";
    public static final String SOAP_ACTION_INSERT_ITEM_AGAINST_VENDOR = "http://tempuri.org/IService1/InsertItems";

    public static final String METHOD_INSERT_CATEGORY_AGAINST_VENDOR = "Insertcategory";
    public static final String SOAP_ACTION_INSERT_CATEGORY_AGAINST_VENDOR = "http://tempuri.org/IService1/Insertcategory";

    public static final String METHOD_INSERT_SUBCATEGORY_AGAINST_VENDOR = "Insertsubcategory";
    public static final String SOAP_ACTION_INSERT_SUBCATEGORY_AGAINST_VENDOR = "http://tempuri.org/IService1/Insertsubcategory";

    //getAllAddressForOrder
    public static final String METHOD_GET_COMBO_DISCOUNT = "insertComboDiscount";
    public static final String SOAP_ACTION_COMBO_DISCOUNT = "http://tempuri.org/IService1/insertComboDiscount";

    public static final String METHOD_GET_MERCHANT = "getItemWithOffers";
    public static final String SOAP_ACTION_GET_MERCHANT = "http://tempuri.org/IService1/getItemWithOffers";

    public static final String METHOD_DELETE_ITEM = "Deleteitem";
    public static final String SOAP_ACTION_DELETE_ITEM  = "http://tempuri.org/IService1/Deleteitem";

    public static final String METHOD_ROLE = "getallRoles";
    public static final String SOAP_ACTION_ROLE= "http://tempuri.org/IService1/getallRoles";

    public static final String METHOD_REGISTER = "registerRuni";
    public static final String SOAP_ACTION_REGISTER = "http://tempuri.org/IService1/registerRuni";

    public static final String METHOD_GET_REJECTED_DETAILS = "getOrderStatusDetails";
    public static final String SOAP_ACTION_GET_REJECTED_DETAILS = "http://tempuri.org/IService1/getOrderStatusDetails";

    //insertPurchaseRuni
    public static final String METHOD_INSERT_PO_AGAINST_VENDOR = "insertPurchaseRuni";
    public static final String SOAP_ACTION_PO_ITEM_AGAINST_VENDOR = "http://tempuri.org/IService1/insertPurchaseRuni";

    public static final String METHOD_INSERT_ITEM_MRP_VENDOR = "InsertMRPForItems";
    public static final String SOAP_ACTION_INSERT_ITEM_MRP_VENDOR = "http://tempuri.org/IService1/InsertMRPForItems";

    public static final String METHOD_INSERT_ITEM_MRP_RUNI_VENDOR = "InsertMRPForItemsRuni";
    public static final String SOAP_ACTION_INSERT_ITEM_MRP_Runi_VENDOR = "http://tempuri.org/IService1/InsertMRPForItemsRuni";

    public static final String METHOD_GET_VEHICLES = "GetVehicles";
    public static final String SOAP_ACTION_GET_VEHICLES = "http://tempuri.org/IService1/GetVehicles";

    public static final String METHOD_GET_PRODUCTS_RATE_LIST = "getMRPlist";
    public static final String SOAP_ACTION_GET_PRODUCTS_RATE_LIST = "http://tempuri.org/IService1/getMRPlist";

    //Vendors services
    public static final String METHOD_GET_PRODUCTS_LIST = "getProducts";
    public static final String SOAP_ACTION_GET_PRODUCTS_LIST = "http://tempuri.org/IService1/getProducts";

    public static final String METHOD_GET_PRODUCT_CATEGORY = "getAllItemsAnyDukaan";  //new name
    public static final String SOAP_ACTION_GET_PRODUCT_CATEGORY = "http://tempuri.org/IService1/getAllCatSubItem";

    public static final String METHOD_USER_LOCATION = "savegpslocation";
    public static final String SOAP_ACTION_USER_LOCATION = "http://tempuri.org/IService1/savegpslocation";

    public static final String METHOD_PETRO_USER_REGISTRATION = "PetroAuthentication";
    public static final String SOAP_ACTION_PETRO_USER_REGISTRATION = "http://tempuri.org/IService1/PetroAuthentication";

    public static final String METHOD_PETRO_OTP = "PetroOTP";
    public static final String SOAP_ACTION_PETRO_OTP = "http://tempuri.org/IService1/PetroOTP";

    public static String METHOD_REJECT_ORDER = "statusRejected";
    public static String METHOD_CONFIRM_ORDER = "statusAcknowledge";


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////OLD SERVICES ///////////////////////////////////////////////////////
    public static final String NAMESPACE = "http://tempuri.org/";

    //Not used
    public static final String METHOD_USER_LOGIN = "Userlogin";
    public static final String SOAP_ACTION_USER_LOGIN = "http://tempuri.org/IService1/Userlogin";

    public static final String SOAP_ACTION_USER_REGISTRATION = "http://tempuri.org/IService1/Authentication_new";

    public static final String METHOD_GET_AGENCY_LIST = "getAgencyList";
    public static final String SOAP_ACTION_GET_AGENCY_LIST = "http://tempuri.org/IService1/getAgencyList";

    public static final String METHOD_ITEMS_MRP = "getItemMrp";
    public static final String SOAP_ACTION_ITEMS_MRP = "http://tempuri.org/IService1/getItemMrp";

   /* public static String API_URL_ORDERBILLING = "http://h207.ekatm.com/api/OrderBillingAPI/";
    public static String API_GET_PRODUCT="getproduct";*/

    //--------------------------------------------------------------------------------------------------------//

    public static double totalAmount = 0;
    public static ArrayList<ProductListBean> productsList;
    public static ArrayList<Merchants_against_items> merchantsAgainstItemsArrayList;
    public static ArrayList<MyCartBean> myCartList;
    public static ListView cart_item_listview;
    public static MyCartBean myCartBean;
    //  public static checklistBean checklistBean;
    public static MarchantItemBean addMRPItemBean;
    public static AddCategoryBean addCatBean;
    public static AddSubCategoryBean addSubCatBean;
    //   public static ArrayList<checklistBean> checklistBeanArrayList;//AdditemBean
    public static ArrayList<AdditemBean> CombolistBeanArrayList;
    public static ArrayList<AddCategoryBean> CategorylistBeanArrayList;
    public static ArrayList<AddSubCategoryBean> SubCategorylistBeanArrayList;
    public static ArrayList<MarchantItemBean> AddMRPlistBeanArrayList;
    public static ArrayList<MarchantPOBean> AddMRP_PO_listBeanArrayList;
    public static HashMap<String, Integer> productQuantities;
    public static HashMap<String, Double> productRates;
    public static HashMap<String, Boolean> addProduct;
    public static boolean isSession;
    public static String FULLNAME;
    public static String EMAIL;
    public static String ADDRESS;
    public static String CITY = "Pune";
    public static String STATE;
    public static String PINCODE;
    public static String CUSTVENDTYPE;

    public static String MODULE="";

    public static String userloginid;
    public static String password;
    public static String role;
    public static JSONObject JMain;
    public static JSONObject J_Main = null;

    public static final int RESULT_OTP_LOGIN = 101;
    public static final int RESULT_AGENCY_SELECTION = 102;
    public static boolean isLanguage = false;
    public static boolean isAgency = false;
    public static String selectedLanguage;
    public static String selectedAgencyId;
    public static String selectedAgencyName;
    public static String selectedAgencyAddress;
    public static String selectedAgencyCity;
    public static String selectedAgencyContact;
    public static String selectedAgencyDistance;
    public static String selectedAgencyImage;
    public static String selectedCategoryName;
    public static int selectedOrderId;
    public static int currentOrderId;
    public static String serverOrderId;
    public static String xmlOrderSummary;
    public static String xmlOrderDetail;
    public static int AgencyName = 10;
    public static int Add_address = 11;
    public static int Edit_address = 12;
    public static final int REQUEST_ENABLE_BT = 11;
    public static final int REQUEST_CONNECT_DEVICE = 12;

    /*order booking new APIS*/
    public static String SpecImgPath = "";
    public static String CatImgPath = "";
    public static String SubCatImgPath = "";
    public static String SHOPBYMERCH = "";
    public static String SHOPBYMODE = "";
    public static String SHIPToAddr = "";
    public static String MerchantID = "";
    public static String MerchantName = "";
    public static String langCODE = "";
    public static String LANGUAGE = "";
    public static String LONGITUDE = "";
    public static String LATITUDE = "";
    public static String selected_BSEGMENTDESC;
    public static String selected_BSEGMENTID;
    public static String selected_BSEGMENTCODE;
    public static String selected_MERCHID;
    public static String SHIPTOMASTERID = "";
    public static String DynamicLOGO_Company_URL = "";
    public static String AppCode = "";

    public static final String api_GetFillCustomerSegment = "/api/SuspectMasterAPI/GetFillCustomerSegment";
    public static final String METHOD_GET_FAMILY_MASTER_MERCHANT = "getMerchFamilyData";
    public static final String METHOD_GET_PRODUCT_MULTISELLER = "GetMultiMerchItem";
    public static final String METHOD_MOBILE_VERIFICATION_new = "getVerificationData1";
    public static final String Get_CollectionReceiptData = "GetChargedAmountHistory";

}