package com.vritti.databaselib.other;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;

/**
 * Created by Admin-1 on 11/11/2016.
 */
public class WebUrlClass {


    /*  ----------------------------  static strings common  ----------------------------------------- */

           public static final String APP_URL_PM = "https://app.simplifypractice.com";  // Production
           public static final String APP_URL_PM_NEPAL = "https://nepal.simplifypractice.com";  // Production
    //For Nepal PM

    //public static final String APP_URL_PM = "https://nepal.simplifypractice.com";  // Production

    // public static final String APP_URL_PM = "http://vritti1.simplifypractice.com"; // Testing



    /*  ---------------------------------APP version--------------------------------------------------- */

    public static String APP_CURRENT_VERSION = "", APP_NEW_VERSION = "";

    /*  ---------------------------------APP Name for FCM--------------------------------------------------- */

    public static final String AppNameFCM_EKATM = "Ekatm";
    public static final String AppNameFCM_PM = "PM";
    public static final String AppNameChat = "vWb";
    public static final String AppNameFCM_Delivery = "Delivery";//used for sourcetype
    public static final String AppNameFCM_MilkRun = "Milk Run";
    public static final String AppNameFCM_Sahara = "Sahara";
    public static String AppNameFCM_Alfa = "Alfa";
    /*  ---------------------------------APP Name for FCM--------------------------------------------------- */


    public static final String IMAGE_DIRECTORY_EKATM = "EkatmDCIM";
    public static final String IMAGE_DIRECTORY_PM = "PMDCIM";

    /*  ---------------------------------APP Name for OTP--------------------------------------------------- */
    public static final String AppNameOTP_ekatm = "Ekatm";
    public static final String AppNameOTP_PM = "Simplify Practice";

    public static final String setError = "error";

    /*  ---------------------------------APP Name for moduleselection--------------------------------------------------- */

    public static final String app_name_moduleselection_CRM = "CRM";
    public static final String app_name_moduleselection_Vwb = "Vworkbench";
    public static final String app_name_moduleselection_PM = "Simplify Practice";
    public static final String app_name_moduleselection_service = "Service";


    /* --------------------------------- Preferences-----------------------------------------------------*/


    public static final String ATTENDANCE_PREFERENCES = "Biometickattaindence";//Bimetric attaindence sharedpreference
    public static final String ATTENDANCE_PREFERENCES_ACTIVITY_KEY = "biometricactid";
    public static final String ATTENDANCE_PREFERENCES_ACTSTART_KEY = "biometricactstart";
    public static final String ATTENDANCE_PREFERENCES_DATE_KEY = "biometricdate";
    public static final String ATTENDANCE_PREFERENCES_COUNT_KEY = "biometriccount";


    public static final String PREFERENCE_DATA_CALCULATION = "prefrencedataused";// Appication data Usage calculation
    public static final String PREFERENCE_CONSUMEDDATA_KEY = "consumeddata";


    public static final String USERINFO = "UserInfo"; // usermaster preference
    public static final String USERINFO_YESTERDAY_DATE = "UserInfo_yesdate";
    public static final String USERINFO_OFF_DAY = "UserInfo_offday";
    public static final String USERINFO_SHORTCUTADGER_COUNT = "UserInfo_ShortcutBadger";
    public static final String USERINFO_TIMESHEET_ISTIMESlOT = "IsTimeslotBooked";
    public static final String USERINFO_USER_TYPE = "usertype";
    public static final String USERINFO_ISCOLLECTION_APPLICABLE = "IsSalesModule";


    public static final String MyPREFERENCES = "LoggingPrefs";// login preference
    public static final String SELECTED_COUNTRY = "SELECTED_COUNTRY";// login preference
    public static final String SELECTED_COUNTRY_NAME = "SELECTED_COUNTRY_NAME";// login preference
    public static final String MyPREFERENCES_URL_KEY = "Url";
    public static final String MyPREFERENCES_EnvMasterID_KEY = "EnvMasterID";
    public static final String MyPREFERENCES_PlantID_KEY = "Plantidkey";
    public static final String MyPREFERENCES_PlantName_KEY = "Plantnamekey";
    public static final String MyPREFERENCES_LOGIN_KEY = "login";
    public static final String MyPREFERENCES_PSW_KEY = "psw";
    public static final String MyPREFERENCES_MOBILE_KEY = "mobile";
    public static final String MyPREFERENCES_SETTING_KEY = "setting";
    public static final String MyPREFERENCES_SETTING_POSITION_KEY = "settingKRY";
    public static final String MyPREFERENCES_USERMASTER_ID_KEY = "UserMasterId";
    public static final String MyPREFERENCES_USERNAME_KEY = "Username";
    public static final String MyPREFERENCES_Designation_KEY = "Designation";



    public static final String MyPREFERENCES_IS_CHAT_APPLICABLE_KEY = "chatapplica";
    public static final String MyPREFERENCES_IS_GPS_LOCATION_KEY = "gpsloc";
    public static final String MyPREFERENCES_FIREBASE_TOKEN_KEY = "firbasetoken";
    public static final String MyPREFERENCES_IS_CRMUSER_KEY = "crmuser";
    public static final String MyPREFERENCES_IS_APPCODE = "AppCode";


    public static final String GET_COMPANY_URL_KEY = "CompanyURL";
    public static final String GET_EnvMasterID_KEY = "EnvId";
    public static final String GET_PlantID_KEY = "PlantID";
    public static final String GET_PlantName_KEY = "PlantName";
    public static final String GET_LOGIN_KEY = "UserLogInId";
    public static final String GET_PSW_KEY = "Password";
    public static final String GET_MOBILE_KEY = "Mobile";
    public static final String GET_DATABASE_NAME_KEY = "DatabaseName";
    public static final String GET_USERMASTERID_KEY = "UserMasterId";
    public static final String GET_USERNAME_KEY = "UserName";
    public static final String GET_ISCRMUSER_KEY = "IsCRMuser";
    public static final String GET_ISCHATAPPLICABLE_KEY = "IsChatApplicable";
    public static final String GET_ISGPSLOCATION_KEY = "IsGpsLocation";
    public static final String GET_BACKDATE_TIMESHEET_KEY = "BackDateTimesheet";
    public static final String GET_ANDROID_ID_KEY = "AndroidId";
    public static final String GET_IMEI_NUMBER_KEY = "IMEINumber";
    public static final String GET_FCM_TOKEN_KEY = "FCMToken";
    public static final String GET_Designation = "Designation";


    /*  ----------------------------Intent flag----------------------------------------- */

    public static final String INTENT_LOGIN_SCREEN_BACKFLAG = "intent_flag";
    public static final String VALUE_LOGIN_SCREEN_BACKFLAG = "1";

    public static final String INTENT_APP_VERSION = "intent_App_version";
    public static final String INTENT_APP_VERSION_NO = "intent_App_version_number";

    public static final String INTENT_SEND_OFFLINE_DATA_FLAG_KEY = "offlinedatakey";
    public static final String INTENT_SEND_OFFLINE_DATA_FLAG_VALUE = "InstantStartOfService";


    public static final String INTENT_ENO_SCREEN = "scrasdf";
    public static final String INTENT_ENO_EATERY1 = "eatery1";

    public static final String INTENT_ENO_EATERY2 = "eatery2";

    public static final String INTENT_ENO_MEDICAL = "medicalRSD";
    public static final String INTENT_ENO_HAATBAZAR = "haatbazaeeno";

    /* --------------------------------- URL-----------------------------------------------------*/

    public static final String api_checkifsc = "https://api.bank.codes/in-ifsc/json/1f206b29e35fa519205c2242b5a1ed4c/";


    public static final String api_checkEnv = "/api/LoginAPI/CheckAppEnvmaster";
    public static final String api_getPlants = "/api/LoginAPI/GetPlants";
    public static final String api_getEnv = "/api/LoginAPI/GetEnvis";
    public static final String api_GetIsValidUser = "/api/LoginAPI/GetIsValidUser";

    public static final String api_GetSessions = "/api/LoginAPI/GetSessions";

    public static final String api_getEnvPM = "/api/LoginAPI/GetChatApplicable";
    public static final String api_GETIsValidUserMobile = "/api/LoginAPI/GETIsValidUserMobile";
    public static final String api_GetUserMasterId = "/api/TimesheetAPI/GetUserMasterId";
    public static final String api_GetUserType = "/api/CallListAPI/getCheckUserPosition";
    public static final String api_POST_CRMCallListPaging = "/api/CallListAPI/POSTCRMCallListPaging";
    public static final String api_GetUserMasterIdAndroid = "/api/TimesheetAPI/GetUserMasterIdForAndroid";

    public static final String api_Birthday = "/api/BirthDayAPI/GetBirthDay";
    public static final String api_Notification = "/api/NotificationAPI/GetAllRecord";
    public static final String api_CheckSalesInstalled = "/api/CRMLeftSideBarAPI/CheckSalesInstalled";
    //GetAllRecord";   //GetNotification
    public static final String api_Meetings = "/api/MeetingAPI/GetCurrentWeekMeets";
    public static final String api_Mywork = "/api/myworkapi/GetMyWork";
    public static final String api_MyWorkspace = "/api/MyWorkSpacesapi/GetMyWorkSpaces";//cnt workspacecnt
    // public static final String api_MyTeam = "/api/CRMLeftSideBarAPI/getMyTeam";
    public static final String api_get_TeamMembers = "/api/_MyTeamDashboardAPI/getTeamMembers";//njhsd
    public static final String api_MyTeam = "/api/myteamapi/GetMyTeam";
    public  static final String api_myworkspacecnt = "/api/MyWorkSpacesapi/GetMyWorkSpacesCount";

    public static final String api_UserName = "/api/TopBarUserInfoAPI/Getuserinfo";
    public static final String api_CallRating = "/api/CRMLeftSideBarAPI/getCallRating";
    public static final String api_Appointment = "/api/CRMLeftSideBarAPI/getMyVisits";
    public static final String api_Opportunities = "/api/CRMLeftSideBarAPI/getMyCalls";
    public static final String api_Collection = "/api/CRMLeftSideBarAPI/getMyCollection";
    public static final String api_Get_Call = "/api/CallListAPI/POSTCRMCallList";
    public static final String api_save_Mocall_Record = "/api/CallListAPI/GetSaveUserMobileCallRecord";
    public static final String api_Get_Reason = "/api/CallListAPI/getReasonById";
    public static final String api_Save_Call_Rating = "/api/CallListAPI/POSTUpdateCallStatus";
    public static final String api_NatureOfCall = "/api/CallListAPI/getNatureOfCall";
    public static final String api_InitiatedBy = "/api/CallListAPI/getInitiatedBy";
    public static final String api_With_whom = "/api/CallListAPI/getFollowupWith";
    public static final String api_getFollowupReason = "/api/CallListAPI/getFollowupReason";
    public static final String api_Outcome = "/api/CallListAPI/getCRMOutome";
    public static final String api_getReason = "/api/CallListAPI/getReason";
    public static final String api_Category = "/api/CallListAPI/getUserByCRMCategory";
    public static final String api_getApprover = "/api/CallListAPI/getApprover";
    public static final String api_getCurrencyMaster = "/api/CallListAPI/getCurrencyMaster";
    public static final String api_getOrdertypefromcall = "/api/CallListAPI/getOrderTypeMasterByCallId";
    public static final String api_getOrdertype = "/api/CallListAPI/getOrderMaster";
    public static final String api_getReceivedby = "/api/CallListAPI/getTMESEName";
    public static final String api_getPostOppUpdate = "/api/CallListAPI/POSTInsertCallUpdate";
    public static final String api_DatasheetMode = "/api/DatasheetEntryAPI/GetDatasheetMode";
    public static final String api_Datasheet_GetData = "/api/DatasheetEntryAPI/GetData";
    public static final String api_save_datasheet = "/api/DatasheetEntryAPI/PostSaveDatasheet";
    public static final String api_reassign_datasheet="/api/TimesheetAPI/PostReassignActivity";
    public static final String api_edit_datasheet = "/api/DatasheetEntryAPI/GetDatasheetEditData";
    public static final String api_get_formData = "/api/DatasheetEntryAPI/GetformData";
    public static final String api_get_city = "/api/CRM_TravelPlanAPI/getCityMaster";
    public static final String api_post_travel_plan = "/api/CRM_TravelPlanAPI/POSTTravelPlan";
    public static final String api_TRAVEL_PLAN = "/api/CRM_TravelPlanAPI/getTravelPlan";
    public static final String api_TRAVEL_PLAN_Delete = "/api/CRM_TravelPlanAPI/POSTDeleteTravelPlan";
    public static final String api_get_Firm = "/api/SuspectMasterAPI/LoadSimilarFirmsNew";
    public static final String api_get_Product = "/api/SuspectMasterAPI/GetFillProduct";
    public static final String api_get_sales_family = "/api/SuspectMasterAPI/GetFillSalesFmaily";
    public static final String api_getdata = "/api/LoginAPI/GetModuleSetvalForGPS";
    public static final String api_get_Prospect_filter = "/api/SuspectMasterAPI/GetSuspectList";
    public static final String api_get_Prospect_ID = "/api/SuspectMasterAPI/getData";
    public static final String api_get_Default_Prospect = "/api/SuspectMasterAPI/getDefaultProspect";
    public static final String api_GetChkUser_list = "/api/AssignWorkAPI/GetChkUserlist";
    public static final String api_GetVisitandTravelPlan = "/api/CRMCallLogApi/GetVisitandTravelPlan";





    public static final String api_get_fill_entity = "/api/SuspectMasterAPI/GetFillEntityCustomer1";
    public static final String api_get_fill_consignee = "/api/SuspectMasterAPI/GetFillEntityConsignee";
    public static final String api_get_fill_territory = "/api/SuspectMasterAPI/GetFillTerritory";
    public static final String api_get_Businesssegment = "/api/SuspectMasterAPI/GetFillCustomerSegment";
    public static final String api_get_ProspectSource = "/api/SuspectMasterAPI/GetFillSuspectSource";
    public static final String api_get_ProspectCheckSerial= "/api/SuspectMasterAPI/DuplicateRecChkForEmamiAndroid";

    public static final String api_get_Prospect_validations = "/api/ProspectSettingAPI/getDatabyId";
    public static final String api_get_Referencetype = "/api/SuspectMasterAPI/GetFillLead";
    public static final String api_get_Reference = "/api/SuspectMasterAPI/GetFillLeadwiseCustomer";
    public static final String api_getCustomerListUserBy = "/api/SuspectMasterAPI/GetCustomerListUserBy";//?Usermasterid=46df9824-a95f-4a9c-8d4f-c1b320e6c918
    public static final String api_Post_Prospect = "/api/SuspectMasterAPI/POST";

    public static final String api_Post_Enquiry = "/api/SuspectMasterAPI/PostEnquiry";
    public static final String api_Get_Enuiry = "/api/SuspectMasterAPI/GetEnquiryHeaderList";

    public static final String api_Cancel_Enuiry = "/api/SuspectMasterAPI/CancelEnquiry";


    public static final String api_Get_SE = "/api/CRMCallAssignmentAPI/getSE";
    public static final String api_Get_Boe = "/api/CRMCallAssignmentAPI/getTME";
    public static final String api_Get_Followuptime = "/api/CRMCallAssignmentAPI/getFollowupTime";
    public static final String api_Get_Compaign = "/api/CRMCallAssignmentAPI/getCompaign";
    public static final String api_Get_Ordertype = "/api/CRMCallAssignmentAPI/getOrderType";
    public static final String api_getSuspectDetails = "/api/CRMCallAssignmentAPI/getSuspectDetails";
    public static final String api_getProductDetail = "/api/CRMCallAssignmentAPI/getProductDetail";
    public static final String api_getproductforEdit = "/api/CRMCallAssignmentAPI/getproductforEdit";

    public static final String api_post_save_call = "/api/CRMCallAssignmentAPI/POST";
    public static final String api_get_UserMaster = "/api/AssignWorkAPI/GetUnChkUserlist";
    public static final String api_Reassign_call = "/api/CallListAPI/getReassignCalls";
    public static final String api_RegId_post = "/api/PushNotificationAPI/PostDeviceMaster";
    public static final String api_GetCallHistory = "/api/CRMCallHistoryAPI/GetFillhistory";
    public static final String api_get_Statelist = "/api/SuspectMasterAPI/GetFillState";
    public static final String api_get_countrylist = "/api/SuspectMasterAPI/GetFillCountry";
    public static final String api_get_countrylistdata = "/api/VendorRegistrationAPI/getCountry";
    public static final String api_get_statelistdata = "/api/VendorRegistrationAPI/getState";
    public static final String api_get_districtlistdata = "/api/VendorRegistrationAPI/getDistrict";
    public static final String api_get_territorylistdata = "/api/VendorRegistrationAPI/getTaluka";


    public static final String api_get_GetFillGST = "/api/SuspectMasterAPI/GetFillGST";
    public static final String api_get_GetFillControls = "/api/SuspectMasterAPI/GetFillControls";
    public static final String api_get_GetProspectTypeID = "/api/SuspectMasterAPI/checkSuspectType";
    public static final String api_get_getFillContDet = "/api/SuspectMasterAPI/getFillContDet";
    //public static final String api_get_getFillProdDett ="/api/SuspectMasterAPI/getFillProdDet";//GetFillCountry
    public static final String api_get_getFillFamilyDet = "/api/SuspectMasterAPI/getFillFamilyDet";
    public static final String api_POSTAddContact = "/api/CallListAPI/POSTAddContact";
    public static final String api_getCustContactDetails = "/api/CallListAPI/getCustContactDetails";
    public static final String api_getSuspectContactDetails = "/api/CallListAPI/getSuspectContactDetails";

    public static final String api_POSTdeleteContact = "/api/CallListAPI/POSTdeleteContact";
    public static final String api_POSTdeleteCustContact = "/api/CallListAPI/POSTdeleteCustContact";

    public static final String api_POSTSaveEditContact = "/api/CallListAPI/POSTSaveEditContact";
    public static final String api_postGpsLocation = "/api/GroupMasterAPI/PostGps";
    public static final String Errormsg = "error";
    public static final String api_PostGpsNot = "/api/GroupMasterAPI/POSTInsert_FCM_GPS";
    public static final String api_Feedback = "/api/CRMLeftSideBarAPI/getMyFeedback";
    public static final String api_POSTSaveCollectionReceipt = "/api/CollectionReceiptAPI/POSTSaveCollectionReceipt";
    public static final String api_getCustCon = "/api/CollectionReceiptAPI/getCustCon";
    public static final String api_Leave_ReportingTo = "/api/MyLeaveAPI/getReportingTo";
    public static final String api_Leave_GetCheckGSTIN = "/api/CallListAPI/GetCheckGSTIN";///api/CallListAPI/GetProspectType
    public static final String api_Leave_GetProspectType = "/api/CallListAPI/GetProspectType";
    public static final String api_GenerateOTPAPI = "/api/GenerateOTPAPI/GetGenerateOTP";
    public static final String api_GetCallCount = "/api/CallListAPI/getCallCountAndroid";
    public static final String api_GetFillEntityCustomer = "/api/SuspectMasterAPI/GetFillEntityCustomer";
    public static final String api_GetSupplier = "/api/SupportStaffMasterAPI/GetSupplier";
    public static final String api_GetSupplierPlant = "/api/SupportStaffMasterAPI/GetPlant";
    public static final String api_GetFillConsignee = "/api/CollectionReceiptAPI/FillConsignee";
    public static final String api_GetLoadProvisionalList = "/api/CallListAPI/getLoadProvisionalList";
    public static final String api_GetBankname = "/api/CallListAPI/getBankName";
    public static final String api_GetAdvance = "/api/CollectionReceiptAPI/getAdvance";
    public static final String api_bindAllDropDowns = "/api/SuspectMasterAPI/bindAllDropDowns";
    public static final String api_GetCustList = "/api/CustServiceReportAPI/CustomerList";
    public static final String api_GetTicketCount = "/api/CustServiceReportAPI/TicketCount";
    public static final String api_GetTicketCountMonthreport = "/api/CustServiceReportAPI/TicketCountMonthWise";
    public static final String api_GetTicketDetail = "/api/CustServiceReportAPI/GetTicketData";
    public static final String api_GetFill_Data = "/api/SuspectMasterAPI/GetFill_Data";

    public static final String api_GetCreateChatRoom = "/api/ChatRoomApi/GetCreateChatRoom";
    public static final String api_GetUserList = "/api/ChatRoomApi/GetUserList";
    public static final String api_getUsersinChatRoom = "/api/ChatRoomApi/getUsersinChatRoom";
    public static final String api_PostAddUser = "/api/ChatRoomApi/PostAddUser";
    public static final String api_getChatRoomsForCall = "/api/ChatRoomApi/getChatRoomsForCall";
    public static final String api_GetUserListExcludingExisting = "/api/ChatRoomApi/GetUserListExcludingExisting";
    public static final String api_SendMessage = "/api/ChatRoomApi/SendMessage";
    public static final String api_GetExitChatRoom = "/api/ChatRoomApi/getExitChatRoom";
    public static final String api_GetRefreshMessages = "/api/ChatRoomApi/getRefreshMessages";
    public static final String api_GetRefreshChatRoom = "/api/ChatRoomApi/getRefreshChatRoom";
    public static final String api_DownloadImageImageAndroid = "/api/ChatRoomApi/DownloadImage";
    public static final String api_FileUpload = "/api/ChatRoomAPI/UploadFiles";
    public static final String api_FileUploadProspect = "/api/UploadFilesAPI/UploadFile";
    public static final String api_ChatRoomUserDelete = "/api/ChatRoomApi/ChatRoomUserDelete";
    public static final String api_EditChatRoomName = "/api/ChatRoomApi/EditChatRoomName";


    public static final String api_PostSaveAttachment = "/api/VendorRegistrationAPI/PostSaveAttachment";
    public static final String api_PostSaveAttachmentProspect = "/api/SuspectMasterAPI/GetAttachSave";


    public static final String api_GetFillAddedby = "/api/SuspectMasterAPI/GetFillAddedby";
    public static final String api_GetSaveDiscount = "/api/CallListAPI/GetSaveDiscount";
    public static final String api_getMySubTeam = "/api/CRMLeftSideBarAPI/getMySubTeam";




    public static final String api_PostUploadImageAndroid = "/api/TicketUpdationAPI/PostUploadImageAndroid";


    public static final String api_FillApprover = "/api/CallListAPI/FillApprover";
    public static final String api_GetPromoterList = "/api/SuspectMasterAPI/getVendorwisePromoterList";
    public static final String api_GetPromoterList2 = "/api/AdvancePaymentVoucherAPI/GetUserName";

    public static final String api_GetPromoterRecord = "/api/SuspectMasterAPI/getPromoterReportData";
 public static final String api_getTicketDataForAndroid = "/api/CustomerwiseTicketDetailsAPI/getTicketDataForAndroid";
 public static final String api_TicketCountForAndroid = "/api/CustomerwiseTicketDetailsAPI/TicketCountForAndroid";
 public static final String api_TicketCountMonthWiseForAndroid =  "/api/CustomerwiseTicketDetailsAPI/TicketCountMonthWiseForAndroid";
 public static final String api_MonthwiseTikcetDetailsForAndriod =  "/api/CustomerwiseTicketDetailsAPI/MonthwiseTikcetDetailsForAndriod";

 public static final String api_GetNotifyTypeDD = "/api/NotificationAPI/GetNotifyTypeDD";
 public static final String api_GetUnreadNotification = "/api/NotificationAPI/UnreadNotification";
 public static final String api_GetReadNotification = "/api/NotificationAPI/ReadNotification";
 public static final String api_GetAllNotices = "/api/notificationapi/GetAllNotices";


    public static final String Sharedpreference_Prospect = "prospect";
    public static final String Key_Enterprise = "enterprize";
    public static final String Key_Business = "business";
    public static final String Key_indivisual = "inudualdiv";
    public static final String Key_Default_enterprise = "Enterprise Prospect";
    public static final String Key_Default_business = "Small Business";
    public static final String Key_Default_individual = "Individual Prospect";
    public static final String Key_Default_Prospect = "defaultProspect";


    public static Boolean isRunningGPSsend = false;
    public static Boolean FlagDownloadgpsdetail = false;

    public static final String FlagisUploadedTrue = "YES";
    public static final String FlagisUploadedFalse = "NO";
    public static final String FlagisUploadedFailed = "Failed";
    public static final int POSTFLAG = 1;
    public static final int GETFlAG = 0;
    public static final int ATTACHMENTFlAG = 2;


    public static final String PROMOTIONAL = "promo";
    public static final String KEY_PROMOTIONAL_CITY = "CITY";
    public static final String KEY_PROMOTIONAL_VILLAGE = "VILLAGE";
    public static final String KEY_PROMOTIONAL_DISTRICT = "DISTRICT";
    public static final String KEY_PROMOTIONAL_STATE = "STATE";
    public static final String KEY_PROMOTIONAL_STATE_NAME = "STATEn";
    public static final String KEY_PROMOTIONAL_MOBILE = "MOBILE";


    public static final String ntf_msgCallAssign = "CALLTYPE";
    public static final String ntf_msgUserAdded = "User_Added";
    public static final String ntf_msgChat = "CHATMSG";

    public static final String ntf_Close_Chat = "Close Chat";
    public static final String ntf_Message_Deleted = "Message Deleted";

    public static final String ntf_GroupNameChange = "GroupNameChange";

    public static final String ntf_Business_Information = "Business_Information";

/* Milk Run*/
public static String statusPending = "Pending";
public static String statusStart = "Start";
public static String statusArrived = "Arrived";
public static String statusLoading = "Loading";
public static String statusComplete = "Complete";
public static String statusCancel = "Cancel";
/*
-------------------------------------------vwb-------------------------------
*/
    public static final String SENDER_ID = "215836600894";

    public static final String api_PostloadWorkData = "/api/TimesheetAPI/PostloadWorkData";
    public static final String api_PostloadWorkDataPaging = "/api/TimesheetAPI/PostloadWorkDataPaging";
    public static final String api_PostloadWorkData_Indexing = "/api/TimesheetAPI/GetTimeSheetPaging";
    public static final String api_GetTimeSheet = "/api/TimesheetAPI/GetTimeSheet";


    public static final String api_GetOTPServer = "/api/GenerateOTPAPI/GetGenerateOTP";
    //  public static final String api_Notification = "/api/NotificationAPI/GetNotification";

    public static final String api_valid_backdate_entry = "/api/TimesheetAPI/GetValidBackDatedEntry";
    public static final String api_PostInsertTimesheet = "/api/TimesheetAPI/PostInsertTimesheet";//GetInsertTimesheet
    public static final String api_getInsertTimesheet = "/api/TimesheetAPI/GetInsertTimesheet";
    public static final String api_Scrach_workspace = "/api/AssignWorkAPI/GetScratchWorkSpaceList";
    public static final String api_Workspace_list = "/api/AssignWorkAPI/GetWorkSpaceList";
    public static final String api_ISbillable_amt = "/api/AssignWorkAPI/GetActivityTypeDtls";
    public static final String api_MainGroup_list = "/api/AssignWorkAPI/GetMainGroupList";
    public static final String api_SubGroup_list = "/api/AssignWorkAPI/GetSubGroupList";
    public static final String api_MainGroup_list_bg = "/api/AssignWorkAPI/GetMainGroupListAndroid";
    public static final String api_SubGroup_list_bg = "/api/AssignWorkAPI/GetSubGroupListAndroid";
    public static final String api_GetUnChkUser_list = "/api/AssignWorkAPI/GetUnChkUserlist";
    public static final String api_PostInsertAct = "/api/AssignWorkAPI/PostInsertAct";
    public static final String api_NatureOfWork = "/api/AssignWorkAPI/GetNatureOfWorkList";

    public static final String api_GetTasks = "/api/MyClaimAPI/getTimeSheet";
    public static final String api_claim_approver = "/api/MyClaimAPI/getApproverList";
    public static final String api_leave_dochdrId="/api/DocInfoAPI/getDocAppInfo";
    public static final String api_advancepay_dochdrId="/api/AdvancePaymentVoucherAPI/getDocAppInfo";
    public static final String api_claim_cost_center = "/api/MyClaimAPI/GetCostCentre";
    public static final String api_getIfCRMUser = "/api/MyClaimAPI/getIfCRMUser";
    public static final String api_reassignActivity = "/api/TimesheetApi/PostReassignActivity";
    public static final String api_upload_Cliam = "/api/MyClaimAPI/PostInsertClaim";
   /* public static final String api_advancepay_dochdrId="/api/AdvancePaymentVoucherAPI/getDocAppInfo";
    public static final String api_leave_dochdrId="/api/DocInfoAPI/getDocAppInfo";
    public static final String api_claim_dochdrId = "/api/MyClaimAPI/getDocAppInfo";*/
    public static final String api_upload_Support_User = "/api/SupportStaffMasterAPI/SaveStaffData";
    public static final String api_get_Mode_of_journy = "/api/MyClaimAPI/getModeOfJourney";
    public static final String api_getLeaveSummary = "/api/MyLeaveAPI/getLeaveSummary";
    public static final String api_Leave_Records = "/api/MyLeaveAPI/getLeaveRecord";
    public static final String api_Leave_Getmob = "/api/MyLeaveAPI/getEmpContact";

    public static final String api_GetFillUser = "/api/AssetTransferAPI/GetFillUser";
    public static final String api_GetAssets = "/api/AssetTransferAPI/GetAllAssets";
    public static final String api_GetAssetsListforAndroid = "/api/AssetTransferAPI/GetAssetsListforAndroid";
    public static final String api_CheckTransferAsset = "/api/AssetTransferAPI/CheckTransferAsset";
    public static final String api_GetCreateDocument = "/api/AssetTransferAPI/GetCreateDocument";


    public static final String api_GetDocAppInfo = "/api/DocInfoAPI/getDocAppInfo";
    public static final String api_GetDocAppInfoMyCalim = "/api/MyClaimAPI/getDocAppInfo";
    public static final String api_apply_leave = "/api/MyLeaveAPI/InsertLeave";
    public static final String api_Cancel_leave = "/api/MyLeaveAPI/PostDeleteLeaveDetails";
    public static final String api_Edit_leave = "/api/MyLeaveAPI/PostEditLeaveDetails";
    public static final String api_change_workspace = "/api/TimesheetAPI/PostChangeWorkSpace";
    public static final String api_check_isSlotAllowed = "/api/TimesheetAPI/GetchkAllowtimesheet";
    public static final String api_change_activity_status = "/api/TimesheetAPI/PostActivityStatus";
    public static final String api_Approve_periodic = "/api/TimesheetAPI/PostApproveStatus";
    public static final String api_Disapprove_periodic = "/api/TimesheetAPI/PostActivityDisapprove";
    public static final String api_Reschedule = "/api/TimesheetAPI/getRescheduleDate";
    public static final String api_approval_GetMainDtl = "/api/DocInfoAPI/getMainDtl";///api/DocInfoAPI/getDocApprdtl
    public static final String api_approval_GetMainDtl2 = "/api/DocInfoAPI/getDocApprdtl";///
    public static final String api_approval_Getdiv = "/api/DocInfoAPI/getDivDtl";


    public static final String api_GetchkdocType = "/api/DocInfoAPI/GetchkdocType";
    public static final String api_getDtl = "/api/DocInfoAPI/getDtl";
    public static final String api_getnxtAppr = "/api/DocInfoAPI/getFillNextAppr";
    public static final String api_sendNextAppr = "/api/DocInfoAPI/getApproveFinalDocument";

    public static final String api_GetAcctivitylidt = "/api/DocInfoAPI/getAcctivitylidt";
    public static final String api_ApprivalDoc = "/api/DocInfoAPI/getApproveDoc";
    public static final String api_disapproveDoc = "/api/DocInfoAPI/getDispapproveDocument";//
    public static final String api_timesheetime = "/api/TimesheetAPI/GetToTimeValue";
    public static final String api_UploadAttechment = "/api/MyClaimAPI/PostUploadFile";//http://a207.ekatm.com/api/ChatRoomAPI/UploadFiles
    public static final String api_UploadAttechmentnew = "/api/UploadFilesAPI/UploadFileForAndroid";
    public static final String api_UploadAttechment1 = "http://a207.ekatm.com/api/ChatRoomAPI/UploadFiles";//api_FileUpload


    public static final String api_leave_approval_html = "/api/LeaveApprovalAPI/getLeaveApprovalHTML";
    public static final String api_RRF_approval_html = "/api/RecruitmentFormAPI/getgenerateHTML";
    public static final String api_RRF_approve = "/api/RecruitmentFormAPI/getapproveRRF";
    public static final String api_RRF_disapprove = "/api/RecruitmentFormAPI/getDisapproveRRF";

    public static final String api_leave_approval = "/api/LeaveApprovalAPI/InsertApprovedLeaves";
    public static final String api_leave_reject = "/api/LeaveApprovalAPI/InsertRejectLeave";
    public static final String api_activity_completion = "/api/DocInfoAPI/getActivityCompletion";
    public static final String api_claim_approval_HTML = "/api/MyClaimAPI/getgenerateHTML";
    public static final String api_getWorkspacewiseActCnt = "/api/WorkWithUserAPI/getWorkspacewiseactivity";
    public static final String api_getWorkspacewiseActCnt1 = "/api/WorkWithUserAPI/getWorkspacewiseactivity1";
    public static final String api_getActivityTrailDetails = "/api/ActivityTrailAPI/GetActTraiDetails";

    public static final String api_getFinalOutcome = "/api/TicketUpdationAPI/GetBindFinalOutcome";
    public static final String api_getTicketUpdationData = "/api/TicketUpdationAPI/Getfetchdatafirst";//GetWarehouse
    public static final String api_getSaveData = "/api/TicketUpdationAPI/GetSavedata";
    public static final String api_PostSavedata = "/api/TicketUpdationAPI/PostSavedata";
    public static final String api_GetBranchName = "/api/DatasheetAssignmentAPI/FillBranch";
    public static  final String api_GetSchoolList = "/api/DatasheetAssignmentAPI/FillMulClient";

    public static final String api_getWarehouse = "/api/TicketUpdationAPI/GetWarehouse";
    public static final String api_getTicketUpdationLocation = "/api/TicketUpdationAPI/GetLocation";
    public static final String api_getTicketUpdationcode = "/api/TicketUpdationAPI/GetCode";
    public static final String api_getTicketUpdationgetstock = "/api/TicketUpdationAPI/GetStock";
    public static final String api_PostAddMaterial = "/api/TicketUpdationAPI/AddMaterial";


    public static final String api_getSaveActivityTransferd = "/api/TicketUpdationAPI/GetSaveActivityTransfered";
    public static final String api_getPromisCount = "/api/TicketUpdationAPI/GetPromiseCount";////
    public static final String api_getfetchHr = "/api/TicketUpdationAPI/GetfetchHours";
    public static final String api_getBindAction = "/api/TicketUpdationAPI/GetBindTktActions";
    public static final String api_getsaveactivitytransferd = "/api/TicketUpdationAPI/GetSaveActivityTransfered";


    public static final String api_getDepartments = "/api/DepartmentwiseActivitiesAPI/getDepartments";
    public static final String api_getDeptID = "/api/DepartmentwiseActivitiesAPI/GetDeptId";


    public static final String api_getClaimRecord = "/api/MyClaimAPI/getClaimRecord";
    public static final String api_ClaimInsert = "/api/MyClaimAPI/PostInsertClaim";
    public static final String api_ClaimDisApprove = "/api/MyClaimAPI/PostRejectClaim";
    public static final String api_ClaimApprove = "/api/MyClaimAPI/PostApproveClaim";
    public static final String api_ClaimDisApprovebg = "/api/MyClaimApprovalApi/PostRejectClaim";
    public static final String api_ClaimApprovebg = "/api/MyClaimApprovalApi/PostApproveClaim";
    //public static final String api_GetUploadedAttachment = "/api/DocInfoAPI/GetUploadedAttachment";
    public static final String api_GetUploadedAttachment = "/api/UploadFilesAPI/GetUploadedAttachment";
    public static final String api_getDategps = "/api/GroupMasterAPI/GetDate";//GetDate(string UserMasterId)
    public static final String FCMurl = "/api/PushNotificationAPI/PostDeviceMaster";


    public static final String api_GetChkIsClientAffected = "/api/TicketUpdationAPI/GetChkIsClientAffected";
    public static final String api_GetPrevAffectedClientIfo = "/api/TicketRegisterAPI/GetPrevAffectedClientIfo";
    public static final String api_GetRouteFrom = "/api/TicketRegisterAPI/GetRouteFrom";
    public static final String api_GetChangeRouteTo = "/api/TicketRegisterAPI/GetChangeRouteTo";
    public static final String api_POSTSaveRoute = "/api/TicketRegisterAPI/POSTSaveRoute";

    public static final String api_GetBindTktCategry = "/api/TicketUpdationAPI/GetBindTktCategry";
    public static final String api_PostIndividualTkt = "/api/TicketRegisterAPI/PostIndividualTkt";

    public static final String api_ActivityGroup_list = "/api/AssignWorkAPI/GetGroupList";
    public static final String api_clientname = "/api/AssignWorkAPI/FillSupplier";
    public static final String api_clientmobileno = "/api/AssignWorkAPI/FillContact";
    public static final String api_addclientdetails = "/api/AssignWorkAPI/PostInsertEntityContact";
    public static final String api_GetApprover = "/api/AdvancePaymentVoucherAPI/getApprover";
 public static final String api_GetClaimSummary = "/api/MyClaimAPI/getDataMyClaim";

 public static final String api_GetUserName = "/api/AdvancePaymentVoucherAPI/GetUserName";
    public static final String api_getDocAppInfo = "/api/AdvancePaymentVoucherAPI/getDocAppInfo";
    public static final String api_PostInsertAdvanePayment = "/api/AdvancePaymentVoucherAPI/PostInsertAdvancePayment";
    public static final String api_GetOutstandingAmt = "/api/AdvancePaymentVoucherAPI/GetOutstandingAmt";
    public static final String api_GetAutoCompleteReported = "/api/TicketRegisterAPI/GetAutoCompleteReported";
    public static final String api_PostSearchCust = "/api/TicketRegisterAPI/PostSearchCust";
    public static final String api_GetProductList = "/api/TicketRegisterAPI/getProductList";
    public static final String api_GetModule = "/api/TicketRegisterAPI/getModule";
    public static final String api_GetNatureOfTkt = "/api/TicketRegisterAPI/getNatureOfTkt";
    public static final String api_GetActiveContract = "/api/TicketRegisterAPI/GetActiveContract";
    public static final String api_GetTktCategory = "/api/TicketRegisterAPI/getTktCategory";
    public static final String api_GetAssignToAuto = "/api/TicketRegisterAPI/getAssignToAuto";
    public static final String api_GetTktCategoryDtls = "/api/TicketRegisterAPI/getTktCategoryDtls";
    public static final String api_GetAutoCode = "/api/TicketRegisterAPI/GetAutoCode";
    public static final String api_GetUnit = "/api/TicketRegisterAPI/getUnit";
    public static final String api_GetWarrantyDtls = "/api/TicketRegisterAPI/getWarrantyDtls";
    public static final String api_GetMobileNo = "/api/TicketRegisterAPI/getMobileNo";
    public static final String api_PostInsertTkt = "/api/TicketRegisterAPI/PostInsertTkt";
    public static final String api_GetCheckIsOffDay = "/api/GroupMasterAPI/GetCheckIsOffDay";
    public static final String api_GetGPSDetailForOnDuty = "/api/MyLeaveAPI/getGPSDetailForOnDutyAndroid";
    public static final String api_GetGPSLocationForUser = "/api/MyClaimApprovalApi/GetGPSLocationForUser";
    public static final String api_GetTimesheetDetailsPnl = "/api/ActivityTrailAPI/GetTimesheetDetailsPnl";
    public static final String api_GetActDetailsPnl = "/api/ActivityTrailAPI/GetActDetailsPnl";
    public static final String api_GetActTraiDetails = "/api/ActivityTrailAPI/GetActTraiDetails";
    public static final String GetActTraiDetailsAndroid = "/api/ActivityTrailAPI/GetActTraiDetailsAndroid";

    public static final String api_PostActivityDisapprove = "/api/TimesheetAPI/PostActivityDisapprove";
    public static final String api_PostApproveStatus = "/api/TimesheetAPI/PostApproveStatus";
    public static final String api_getGpsLocation = "/api/TimesheetAPI/GetGpsCordinates";
    public static final String api_GetFyYear = "/api/AssignworkApi/GetFyYear";
 public static final String api_GetReviewDays = "/api/CRMCallAssignmentAPI/GetReviewDays";
    public static final String api_PostloadWorkDataWIP = "/api/TimesheetAPI/PostloadWorkDataWIP";

    public static final String FlagComplete = "Complete";
    public static final String FlagPause = "Pause";
    public static final String FlagCancel = "Cancelled";
    public static final String FlagWIP = "WIP";

    public static final String FlagIsVerifyAttaindence_yes = "Y";
    public static final String FlagIsVerifyAttaindence_no = "N";


    public static final String SHARED_TICKET_UPDATE_MATERIAL_SETTING = "dataandroid";
    public static final String KEY_TICKET_UPDATE_WAREHOUSE = "werehouse";
    public static final String KEY_TICKET_UPDATE_WAREHOUSE_ID = "werehouseid";
    public static final String KEY_TICKET_UPDATE_WAREHOUSE_POS = "warehousepos";
    public static final String KEY_TICKET_UPDATE_LOCATION = "locod";
    public static final String KEY_TICKET_UPDATE_LOCATION_ID = "locid";
    public static final String KEY_TICKET_UPDATE_LOCATION_POS = "locpos";


    public static final String ntf_msgActivityAssign = "ACTIVITY";

    public static final String ntf_msgActivityCompleted = "Activity Completed";
    public static final String ntf_msgActivityCancel = "Activity Cancel";
    public static final String ntf_msgACTIVITYReassign = "ACTIVITY Reassign";
    public static final String ntf_msgActivityReschedule= "Activity Reschedule";
    public static final String ntf_msgDeleteuser= "deleteuser";

    public static final String ntf_Payment_Transfer = "Payment Transfer";
    public static final String api_GetMySubteamCount = "/api/myteamapi/GetMySubTeam";
    public static final String api_vwb_getSubTeamMembers = "/api/DepartmentwiseActivitiesAPI/getDepartment";
  public static String get_claim_details = "/api/MyClaimAPI/getClaimRecord";
    public static String deleteClaimRecord = "/api/MyClaimAPI/PostDeleteClaim";
// public  static final String api_myworkspacecnt = "/api/MyWorkSpacesapi/GetMyWorkSpacesCount";
 public  static final String api_GetUserProfile = "/api/TimesheetAPI/GetUserProfile";


 // new Api
 public  static final String getApi_AttachmentPath = "/api/DownloadAttachapi/getdownloadFile?AttachGidId=";
    public static String api_POSTNotificationRead = "/api/NotificationAPI/POSTNotificationRead";
    public static String api_getLocationTypeENO = "/api/LMAPI/GetFillLocationType";
    public static String api_postSurvayData = "/api/TimesheetApi/PostSurvayData";
    public static String api_getHolidaylistforandroid = "/api/HolidayMasterAPI/getHolidaylistforandroid";
    public static String api_getChatRoomLiveLocation = "/api/ChatRoomAPI/getChatRoomLiveLocation";


    /*Notification Json Format*/
    /* {
        "MsgType": "",
            "Msg": {}
    }*/
    /*-------------------------------------------------crm---------------------------------------------*/
    //Inventory

    public static final String api_getState = "/api/VendorRegistrationAPI/getState";
    public static final String api_getCurrency = "/api/VendorRegistrationAPI/getCurrency";
    public static final String api_getPartyType = "/api/VendorRegistrationAPI/getPartyType";
    public static final String api_PostData = "/api/VendorRegistrationAPI/PostData";
    public static final String api_getDistrict = "/api/VendorRegistrationAPI/getDistrict";
    public static final String api_getTaluka = "/api/VendorRegistrationAPI/getTaluka";
    public static final String api_getCountry = "/api/VendorRegistrationAPI/getCountry";
    public static final String api_getCityMaster = "/api/VendorRegistrationAPI/getCityMaster";
    public static final String api_GetFillItemPurchaseGroup = "/api/VendorRegistrationAPI/GetFillItemPurchaseGroup";
    public static final String api_GetIndentDataToReport ="/api/IndentAPI/GetIndentDataToReport";

    public static final String api_GetOrderType = "/api/CommonPurchaseAPI/GetOrderType";
    public static final String api_GetDeparmenttCode = "/api/CommonPurchaseAPI/GetDepartCode";
    public static final String api_GetCurrency = "/api/CommonPurchaseAPI/GetCurrency";
    public static final String api_GetWorkSpaceList = "/api/IndentAPI/GetWorkSpaceList";
    public static final String api_GetWarehouseList = "/api/MaterialIssueAPI/GetWarehouseList";
    public static final String api_GetLocationsList = "/api/IndentAPI/GetLocationList";
    public static final String api_ItemList = "/api/CommonPurchaseAPI/GetItemList";
    public static final String api_Indent_GetSupplier = "/api/CommonPurchaseAPI/GetSupplier";
    public static final String api_GetLastItemRate = "/api/IndentAPI/GetLastItemRate";
    public static final String api_GetItemData = "/api/IndentAPI/GetItemData";
    public static final String api_GetList = "/api/IndentAPI/GetList";
    public static final String api_GetCheckPlant = "/api/IndentAPI/GetCheckPlant";
    public static final String api_POSTSaveIndent ="/api/IndentAPI/POSTSaveIndent";
    public static final String api_getApproverList ="/api/DocInfoAPI/getApproverList";
    public static final String api_POSTIndentApprove ="/api/CommonPurchaseAPI/POSTIndentApprove";
    public static final String api_GetMaterialBudget ="/api/IndentAPI/GetMaterialBudget";
    public static final String api_GetRemBughetAmt ="/api/IndentAPI/GetRemBughetAmt";

    public static String api_DeleteChatMsges = "/api/ChatRoomApi/DeleteChatMsges";
    public static String api_devery_details = "/api/OrderBillingAPI/getDeliveryDetails";
    public static String api_Get_IsDelieveryBoy = "/api/UserMasterAPI/getData?id";

    //*********************************************Sales shipment API************************************************************* //
    public static final String api_CustomerCode = "/api/EntityMasterAPI/getCode";
    public static String api_getSOList_consignee = "/api/ShipmentEntryAPI/GetSoList";
    public static String api_getItemsList_SO = "/api/ShipmentEntryAPI/GetListAsSo";
    public static String api_postShipment = "/api/ShipmentEntryAPI/PostShipment";
    public static String api_DeliveryAgentDetails = "/api/ShipmentEntryAPI/getDeliveryBoyDetails";
    public static String api_ShipmentEntryAssignActivityAPI = "/api/ShipmentEntryAPI/AssignActivityToDeliveryBoy";
    public static String api_GetItemsListForCounterBilling = "/api/ItemPurGrpItemMstRelAPI/GetItemCodeDesc";
    public static String api_GetPendingDeliveries = "/api/ShipmentEntryAPI/GetPendingDeliveryList";
    public static String api_getTransitShipments = "/api/ShipmentEntryAPI/GetTransitShipList";
    public static String api_getCompanyDetails = "/api/CompanyMasterAPI/GetCompanyDtl";
    public static final String api_postCounterBill= "/api/ShipmentEntryAPI/POSTCounterbillingData";

    /*-------------------------------------------------Entity---------------------------------------------*/
    // public static final String api_getCountry = "/api/VendorRegistrationAPI/getCountry";
    // public static final String api_get_statelistdata = "/api/VendorRegistrationAPI/getState";
    // public static final String api_getDistrict = "/api/VendorRegistrationAPI/getDistrict";
    // public static final String api_getTaluka = "/api/VendorRegistrationAPI/getTaluka";
    // public static final String api_getCityMaster = "/api/VendorRegistrationAPI/getCityMaster";
    public static final String api_getEntityType = "/api/EntityMasterAPI/getPartyType";
    public static final String api_getSalesPriceList = "/api/EntityMasterAPI/getPriseList";
    public static final String api_addNewEntityPost = "/api/EntityMasterAPI/Post";
    public static final String api_updateNewEntityPost = "/api/EntityMasterAPI/POSTUpdateEntity";
    public static final String api_getRecordCustomer = "/api/EntityMasterAPI/getRecordCust";
    public static final String api_getMultipleShipToData = "/api/EntityMasterAPI/getRecordShip";
    public static final String api_getTaxClassList = "/api/QuotationEntryAPI/GetTaxCode";
/*sales order*/
    public static final String api_getCustomerAutocomplete = "/api/SOEntryAPI/FillCustomer"; //?SearchText
    public static final String api_getDropDown_tc = "/api/SOEntryAPI/FillTermsandConditions";
    //"http://c207.ekatm.com/api/SOEntryAPI/FillTermsandConditions?ConfigDT=DeliveryTerms&ConfigID=InvoiceDeliveryBy&ConfigFR=Freight&ConfigDM=DispatchMode&ConfigST=Status&ConfigLD=LiqDmg&ConfigPG=PerGar&ConfigPD=PreDisInsp&ConfigIC=InspChrg&ConfigSD=SecDep"
    public static final String api_getChargeCode = "/api/SOEntryAPI/FillChargeCode";
    public static final String api_getPaymentTerms = "/api/SOEntryAPI/FillPaymentTerms";
    public static final String api_getCommissionType = "/api/SOEntryAPI/FillCommissionType";
    public static final String api_getPayableToName = "/api/SOEntryAPI/FillAgent";  //?searchtext=test&type=c
    public static final String api_getCategoryReimbursement = "/api/SOEntryAPI/FillCategory";
    public static final String api_getWarranty = "/api/SOEntryAPI/FillWarranty";
    public static final String api_getUOM_new = "/api/SOEntryAPI/FillUOM";
    public static final String api_getTaxClassCode = "/api/SOEntryAPI/FillTaxClassCode";    //?SODt=1/01/2020
    public static final String api_postSaveSO = "/api/SOEntryAPI/POSTSaveSO";    //?SODt=1/01/2020
    public static final String api_SessionSO = "/api/SOEntryAPI/CreateSession";

    public static final String api_getPricelistCode = "/api/SOContractAPI/GetPriceListCodes";   //?SearchText=pl/&contractdt=2020-02-19
    public static final String api_getPricelistItems = "/api/SoContractAPI/GetPriceListItems";   //?PriceListHdrID=9ec3fc1f-e2f5-439b-b0ee-e9c53ba751bb
    public static final String api_getPricelistDesc = "/api/SOContractAPI/GetPriceListDesc";   //?SearchText=Mahek
    public static final String api_getQuotationNo = "/api/SOContractAPI/GetQuotationNo";   //?SearchText=a&customerid=6c1c887b-34d5-4df9-b17c-7542607c2815
    public static final String api_getQuotationItems = "/api/SoContractAPI/GetQuotationItems"; //?BQT_QuotationHeaderId=1409
    public static final String api_getContractNo = "/api/SOContractAPI/GetContractNo";   //?SearchText=CC/
    public static final String api_getContractItems= "/api/SoContractAPI/GetContractItems";   //?SOContractHeaderId=05f7ac46-b6cd-49e9-8da8-3b7d85d559d7
    public static final String api_getModelNo = "/api/EkatmItemMasterAPI/GetModelNo";
    public static final String api_getModelSize = "/api/EkatmItemMasterAPI/GetModelSize";
    public static final String api_getSalesFamily = "/api/EkatmItemMasterAPI/GetParentsalesGroupId";    //?salesparentID=
    public static final String api_getSalesFamilyItems = "/api/QuotationEntryAPI/GetItemsAgainstSalesFamily";
    public static final String api_checkSOStatus= "/api/SOEntryAPI/CheckSOStatus";  //?CallId=
    public static final String api_getSOheaderEditData= "/api/SOEntryAPI/getEditdata";  //?CallId=
    public static final String api_getSODetailEditData= "/api/SOEntryAPI/EditItemDetail";  //?HeaderId=
    public static final String api_getReimbDtlEditData= "/api/SOEntryAPI/EditReimbursementDetail";  //?HeaderId=

    /****************************************************************** Milk Run******************************************************************************/
    public static String api_milk_get_trip_details = "/api/MilkRunApi/getTripDetail" ;
    public static String api_Get_DelieveryBoy_OTP = "/api/GenerateOTPAPI/DeliveryGenerateOTP";
    public static String changeTripDetailStatus= "/api/MilkRunApi/changeTripDetailStatus" ;

    public static final String api_claim_dochdrId = "/api/MyClaimAPI/getDocAppInfo";

    public static final String api_LeaveDate_Validate = "/api/MyLeaveAPI/getPreviousleave";
    public static final String api_PostEditAct = "/api/AssignWorkAPI/PostUpdatetAct";

    public static final String api_ValidateDate_Claim = "/api/MyClaimAPI/getDuplicateDate";
    public static final String api_EditTask = "/api/AssignWorkAPI/fetchAsCurrentData";
    //Expense management

    public static String api_Post_PostExpenseRecord = "/api/MyClaimAPI/PostExpenseRecord";
    public static String api_GetExpenseFromDateRange = "/api/MyClaimAPI/GetExpenseFromDateRange";
    public static String api_PostDltExpenseRecord = "/api/MyClaimAPI/PostDltExpenseRecord";



    // HYva Project

    public static final String api_GetBatchList= "/api/PIGenerationApi/GetBatchList";
    public static final String api_PostPIdetail= "/api/PIGenerationApi/PostPIdetail";
    public static final String api_UpdateRelaseStatus= "/api/PIGenerationApi/UpdateRelaseStatus";
    public static final String api_GetLocation= "/api/PIGenerationApi/GetLocation";








    public static final String api_deleteAttachment = "/api/UploadFilesAPI/POSTDeleteAttachment";
    public static final String api_GetTaskAutority = "/api/TaskAuthorityAPI/GetTaskAutority";



    public static final String api_checkAssignCount = "/api/DatasheetEntryAPI/CheckAssignCount";
    public static final String api_change_activity_status_Sahara = "/api/DatasheetEntryAPI/changeStatus";
    public static final String api_HierarchyCount_Sahara="/api/DatasheetEntryAPI/GetDetailsCount";
    public static final String api_getlastLevel = "/api/DatasheetEntryAPI/GetLastLevel";
    public static final String api_getReportingTo_sahara = "/api/DatasheetEntryAPI/GetReportingTo";
    public static final String api_getUploadedAttachment_Sahara = "/api/UploadFilesAPI/GetUploadedAttachment";
    public static final String api_saveApproval_Reassign="/api/TimesheetAPI/GetActReassignDtl";
    public static final String api_GetIssuedTo_Reassign="/api/DatasheetEntryAPI/GetAddedBy";
    public static final String api_getSaharaAppr = "/api/TimesheetAPI/PostReassignActivity";
    public static final String api_UpdateAppr_sahara = "/api/DatasheetEntryAPI/Approve";
    public static final String api_reassignActivitySelf = "/api/TimesheetApi/PostReassginActivityToSelf";
    public static final String api_UploadAttachment_Sahara = "/api/UploadFilesAPI/UploadFile";
    public static final String api_UploadAttachmentFinal = "/api/UploadFilesAPI/POSTAttachment";




    //Physical Inventory

    public static final String api_TagDetails = "/api/PIGenerationApi/GetBatchTagDetail";   //?TagNo=1
    public static final String api_DeleteTAG = "/api/PIGenerationApi/DeleteTagNo";   //PIDtlId, string UserName
    public static final String api_RangePrint = "/api/PIGenerationApi/TagReprintDetails";   //FromTagNo, ToTagNo
    public static final String api_AddLocationPI = "/api/PIGenerationApi/AddLocAndroid";
    public static final String api_UOM = "/api/InvReqEntryApi/GetUOM";
    public static final String api_ItemListAndroid = "/api/CommonPurchaseAPI/GetItemListAndroid";
    //  "http://hyvamumbai.ekatm.com/api/PIGenerationApi/GetBatchTagDetail?TagNo=00047"
    //  "http://hyvamumbai.ekatm.com/api/PIGenerationApi/TagReprintDetails?FromTagNo=00041&ToTagNo=00047"
    // "http://hyvamumbai.ekatm.com/api/PIGenerationApi/GetBatchList?UserMasterId=&TypeKey=C"
    //  "http://hyvamumbai.ekatm.com/api/PIGenerationApi/UpdateRelaseStatus?PIHdrId=&ReleaseStatus=20"





    //Alfa Laval

    public static String api_UploadDoDump = "/api/UploadOfflineALMTAPI/UploadDoDump";
    public static String api_OrderPacking = "/api/UploadOfflineALMTAPI/OrderPacking";
    public static String api_GetPicklistNO = "/api/PutPacketInCartonAPI/GetPicklistNO";
    public static String api_GetPick_ListNo = "/api/PickingConfirmationApi/GetPick_ListNo";
    public static String api_GetPostedPicklistNO = "/api/PutPacketInCartonAPI/GetPostedPicklistNO";
    public static String api_GetPackOrdNO = "/api/PutPacketInCartonAPI/GetPackOrdNO";
    public static String api_UpdateComputedWt = "/api/PutPacketInCartonAPI/UpdateComputedWt";
    public static String api_GetPrinterName = "/api/FixQRCodePrintLocationwiseAPi/GetPrinterName";
    public static String api_GetPackingList = "/api/AL_PackingListAPI/getPackingList";
    public static String api_GetShipInspList  = "/api/AL_MOPickLstAPI/getShipInspList";
    public static String api_PrintPacketListHHD  = "/api/AL_MOPickLstAPI/PrintPacketListHHD";
    public static String api_PrintShippingInspRptFromHHD  = "/api/AL_MOPickLstAPI/PrintShippingInspRptFromHHD";
    public static String api_printSummaryPacketList = "/api/AL_PackingListAPI/printSummaryPacketList";
    public static String api_UploadOfflineALMTAPIController = "/api/UploadOfflineALMTAPIController/ShipmentCreation";
    public static String api_AWBShipmentCreation = "/api/ShipCreationApi/AWBShipmentCreation";
    public static String api_RemovedPickedPacket = "/api/PickingConfirmationApi/RemovedPickedPacket";
    public static String api_getCartonHdr = "/api/PutPacketInCartonAPI/getCartonHdr";
    public static String api_getCartonDtl = "/api/PutPacketInCartonAPI/getCartonDtl";
    public static String api_UnPacking = "/api/PutPacketInCartonAPI/UnPacking";
    public static String api_getShipmentNo = "/api/ShipMent_CreationAPI/getShipmentNo";
    public static String api_CartonLoading = "/api/PutPacketInCartonAPI/CartonLoading";
    public static String api_PacketQC = "/api/PutPacketInCartonAPI/PacketQC";



    public static final String api_getGrnHeader ="/api/GRNSuggPutAwayApi/GetGRNHeaderId";
    public static final String api_getItem ="/api/GRNSuggPutAwayApi/GetItem";//GetGRNForUser
    public static final String api_getItemPacket ="/api/GRNSuggPutAwayApi/GetGRNPacketDetail";
    public static final String api_PostGRN = "/api/GRNSuggPutAwayApi/PostGRN";
    public static final String api_getPutawayListPending = "/api/GRNSuggPutAwayApi/GetGRNNoList";
    public static final String api_PostPutawaytoUser = "/api/GRNSuggPutAwayApi/GetUpdateGRNHdr";
    // public static final String api_getGRNForUser = "/api/GRNSuggPutAwayApi/GetGRNForUser";
    public static final String api_getGRNForUser = "/api/GRNSuggPutAwayApi/GetGRNNoListWithPacketAppl";

    public static final String api_getLocationCode = "/api/GRNSuggPutAwayApi/GetLocCode";

    public static final String api_getRessufleHID = "/api/ReshflPutAwayApi/GetResflHdrId";
    public static final String api_getRessufleRecords = "/api/ReshflPutAwayApi/GetEditRecord";
    public static final String api_getReshuffleListPending = "/api/ReshflPutAwayApi/GetReshuffleNoList";
    public static final String api_PostReshuffletoUser = "/api/ReshflPutAwayApi/GetUpdateReshuffleHdr";
    public static final String api_PostRessufle = "/api/ReshflPutAwayApi/POSTFinal";
    public static final String api_getRsfForUser = "/api/ReshflPutAwayApi/GetReshuffleForUser";

    public static String api_CheckPacketValidation = "/api/PickingConfirmationApi/CheckPacketValidation";
    public static String api_CheckPacketValidationNew = "/api/PickingConfirmationApi/CheckPacketValidationNew";
    public static String api_CheckPacketValidationAlfaLaval = "/api/PickingConfirmationApi/CheckPacketValidationAlfaLaval";
    public static String api_CheckPacketPackingValidation = "/api/PickingConfirmationApi/CheckPacketPackingValidation";
    public static String api_CheckPacketPackingValidationAlfa = "/api/PickingConfirmationApi/CheckPacketPackingValidationAlfa";
    public static String GetPickListPacketNoData = "/api/GRNEntryApi/GetPickListPacketNoData";
    public static String GetPackListPacketNoData = "/api/GRNEntryApi/GetPackListPacketNoData";
    public static String GetPacketEnquiryData = "/api/PickListConfirmApi/GetPacketEnquiryData";


    //GetUserWithWorkCount()GetUserWithWorkCount()
    public static final String api_getPickList = "/api/PickListConfirmApi/GetPicklist";///api/PickListConfirmApi/POST
    public static final String api_getPickList_New = "/api/PickListConfirmApi/GetPicklistforandr";///api/PickListConfirmApi/POST
    public static final String api_getPickDetail = "/api/PickListConfirmApi/GetPicklisDetails";///api/AssignWorkAPI/GetUnChkUserlist
    public static final String api_PostPickListtoUser = "/api/PickListConfirmApi/GetUpdatePicklistHdr";//PickListConfirmApi
    public static final String api_getPickListPending = "/api/PickListConfirmApi/GetPendingPicklist";//
    public static final String api_PostPickList = "/api/PickListConfirmApi/POST";

    public static final String api_GetUser = "/api/AssignWorkAPI/GetUnChkUserlist";
    public static final String api_GetUserWithCount = "/api/GRNSuggPutAwayApi/GetUserWithWorkCount";


    public static final String api_GetUserMAsterID = "/api/TimesheetAPI/GetUserMasterId";

    public static final String api_GetDesignation = "/api/UserMasterAPI/getDesginationDtls";

    public static final String api_GetTransporterName = "/api/ASNApi/GetTransporter";
    public static final String api_GetAsnNumber = "/api/ASNApi/GetASNNo";
    public static final String api_GetASNHeaderDetail = "/api/ASNApi/GetHeader";//GetTrans
    public static final String api_GetASNTransDetail = "/api/ASNApi/GetTrans";
    public static final String api_GetASNitemDetail = "/api/ASNApi/GetItem";
    public static final String api_PostGateEntry = "/api/ASNApi/POSTForGateEntry";

    public static final String api_GetASNExiting = "/api/GRNEntryApi/GetASNNo";


    public static final String MyPREFERENCES_DESIGNATION = "MyPrefdesig";
    public static final String MyPREFERENCES_DESIGNATION_KEY = "Designation";
    public static final String MyPREFERENCES_DESIGNATION_ID = "MyPrefdesigId";
    public static final String MyPREFERENCES_DESIGNATION_ID_KEY = "designationId";

    public static final String Intent_Pick_Header_KEY = "Pick_hdr";
    public static final String Intent_Pick_No_KEY = "Pick_no";
    public static final String Intent_Pick_So_KEY = "Pick_So";
    public static final String Intent_Pick_Cust_KEY = "Pick_Cust";

    public static final String Intent_Pick_Detail_Itemcode_KEY = "Pick_itemcode";
    public static final String Intent_Pick_Detail_Itemdesc_KEY = "Pick_itemdesc";
    public static final String Intent_Pick_Detail_Itemloc_KEY = "Pick_itemloc";
    public static final String Intent_Pick_Detail_ItemLoc_code_KEY = "Pick_itemloc_code";
    public static final String Intent_Pick_Detail_PickQty_code_KEY = "Pick_pickqty";
    public static final String Intent_Pick_Detail_Batch_No_KEY = "Pick_batch";


    public static Boolean isSessionActive = false;
    public static Boolean isValidUser = false;

    public static final String DoneFlag_Complete = "Y";
    public static final String DoneFlag_Default = "N";
    public static final String Flag_Update = "Update";
    public static final String Flag_Insert = "Insert";
    public static final String STRING_DESIGNATION = "Manager";
    public static final String STRING_DESIGNATION_ID = "1";

    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";//com.google.zxing.client.android


    public static final String PACKET_START_WITH = "P";
    public static final String PACKET_LOCATION_START_WITH = "L";
    public static String GetItem = "/api/GRNSuggPutAwayApi/GetItem";
    public static String GRN_Put_Away_List = "GRN_Put_Away_List";
    public static String api_getPacketListData = "/api/GRNPurchaseOrderAPI/getPacketListData";
    public static String api_GetLocCode = "/api/GRNSuggPutAwayApi/GetLocCode";
    public static String api_POSTInsertShippingInspection = "/api/PutPacketInCartonAPI/POSTInsertShippingInspection";

    public static String api_PrintLabelarray = "/api/FixQRCodePrintLocationwiseAPI/PrintLabelarray";
    public static String api_GetMRSHeader_ForAndr = "/api/MRSEntryAPI/GetMRSHeader_ForAndr";
    public static String api_GetMRSDtl_ForAndr = "/api/MRSEntryAPI/GetMRSDtl_ForAndr";
    public static String api_POSTInsertPutPacketInCartonHdr = "/api/PutPacketInCartonAPI/POSTInsertPutPacketInCartonHdr";
    public static String api_getAllData = "/api/BoxTypeMasterAPI/getAllData";
    public static String api_GetPacketSplitUpdate= "/api/RejectionReturnApi/PacketSplitUpdate";
    public static String api_CheckQtyForLot= "/api/ALCommonMthdAPI/CheckQtyForLot";
    public static String GetPutAwayDetails = "/api/PutawayScanAPI/GetScanLocation";
    public static String GetScanPacket = "/api/PutawayScanAPI/GetScanPacket";
    public static String api_POSTInsertPutPacketInCartonDtl = "/api/PutPacketInCartonAPI/POSTInsertPutPacketInCartonDtl";
    public static String api_getPackingOrdDetails = "/api/PutPacketInCartonAPI/getPackingOrdDetails";
    public static String api_POSTPutAwayApi = "/api/PutAwayApi/POST";
    public static String api_POSTPutAwayApiAndroid = "/api/PutAwayApi/POSTAndroid";
    public static String POSTAndroidUpdated = "/api/PutAwayApi/POSTAndroidUpdated";
    public static String POST_V2 = "/api/PutAwayApi/POST_V2";
    public static String api_GetPickingOrdDetails = "/api/PutPacketInCartonAPI/getPickingOrdDetails";
    public static String api_POSTValidatePacket = "/api/AL_ShippingPickListAPI/POSTValidatePacket";
    public static String api_GetCustomerDetails = "/api/TicketRegisterAPI/GetCustomerDetails";

    public static String PacketInquiry = "/api/PickingConfirmationApi/PacketInquiry";
    public static String PostPiDataForAndroid = "/api/PIGenerationApi/PostPiDataForAndroid";
    public static String AddPIDtl = "/api/PIGenerationApi/AddPIDtl";
    // New Methods - 26/12/20

    public static String api_GetPickItemGrp = "/api/PickingConfirmationApi/getPickItemGrp";
    public static String api_getPicklistDtl = "/api/PickingConfirmationApi/getPicklistDtl";
    public static String InsertStockCutOff = "/api/PutawayScanAPI/InsertStockCutOff";
    public static String PutAwayToPackingLoc = "/api/PutAwayApi/PutAwayToPackingLoc";
    public static String PickingReverse = "/api/Pick_ListAPI/PickingReverse";
    public static String GetFifoBreakReason = "/api/Pick_listAPI/GetFifoBreakReason";
    public static String CartonScaning = "/api/PutPacketInCartonAPI/CartonScaning";



    public static final String MyPREFERENCES_HEADER = "head";
    public static final String MyPREFERENCES_CODE= "cartancode";

    public static final String anydukaan_cancel_ordbycust= "Cancel Order";
    public static final String anydukaan_received_ordbycust= "Confirm Order";


    // DNA

    public static final String api_GetUserWiseWarehouse = "/api/TicketUpdationAPI/GetUserWiseWarehouse";
    public static final String api_GetUserWiseLocation = "/api/TicketUpdationAPI/GetUserWiseLocation";
    public static final String api_GetLocationQty = "/api/TicketUpdationAPI/GetLocationQty";
    public static final String api_GetAllPacketData = "/api/TicketUpdationAPI/GetAllPacketData";
    public static String api_GetUserMobileCallRecord="/api/CallListAPI/GetUserMobileCallRecord";
    public static final String api_POSTCrmDayReport = "/api/CRMLeftSideBarAPI/CrmDayReport";
    public static final String api_GetLicenceWiseModule = "/Ekatm/GetLicenceWiseModule";
    public static final String api_GetAttenDashData = "/api/AttendanceStatisticsAPI/GetAttenDashData";
    public static final String api_getRecordsforAttendance = "/api/WorkWithUserAPI/getRecordsforAttendance";
    public static final String api_getCreditDocAppInfo = "/api/CreditNoteCGAPI/getDocAppInfo";
    public static final String api_FillCustomerByName = "/api/CreditNoteCGAPI/FillCustomerByName";
    public static final String api_FillCustomerByCode = "/api/CreditNoteCGAPI/FillCustomerByCode";
    public static final String api_GetLasApprNCHold = "/api/CreditNoteCGAPI/GetLasApprNCHold";
    public static final String api_Post = "/api/CreditNoteCGAPI/Post";
    public static final String api_GetCountExpReg = "/api/MyClaimAPI/GetCountExpReg";

    public static final String api_getListProduct = "/api/CRMCallAssignmentAPI/getListProduct";
    public static final String api_getListProduct_1 = "/api/CRMCallAssignmentAPI/getListProduct";
   // https://vritti.ekatm.co.in/api/CRMCallAssignmentAPI/getListProduct?OrderTypeMasterId=
    public static final String api_getProductDetailEdit = "/api/CRMCallAssignmentAPI/getproductforEdit";
    public static final String api_PostUpdate = "/api/CRMCallAssignmentAPI/POSTUpdate";
    public static final String api_getOpportunityDetail= "/api/CRMCallAssignmentAPI/getOpportunityDetail";
    public static final String api_GetDetails= "/api/CreditNoteCGAPI/GetDetails";
    public static final String api_getFillAttendis= "/api/WorkWithUserAPI/getFillAttendis";
    public static String GetPendingGRN = "/api/GRNEntryAPI/GetPendingGRN";
    public static String PostToStock   = "/api/GRNEntryAPI/PostToStock";
    public static String GetGRNItemDetails = "/api/GRNEntryApi/GetGRNItemDetails";
    public static String checkValidPacketNo_Item = "/api/GRNEntryApi/checkValidPacketNo_Item";



    // NEW MERGE API


    public static String GetMyWorkMYTeamCombine = "/api/myworkapi/GetMyWorkMYTeamCombine";
    public static String getDetails = "/api/SEPerformanceDashboardAPI/getDetails";
    public static String ShowWidgetClaim = "/api/MyClaimAPI/ShowWidgetClaim";
    public static String GetTargetAndAchievement = "/api/IndividualTargetAchievementAPI/GetTargetAndAchievement";
    public static String GetFromToNextActionDate = "/api/CallListAPI/GetFromToNextActionDate";
    public static String GetScheduledata = "/api/CallListAPI/GetScheduledata";
    public static String fetchClaimLedger = "/api/MyClaimAPI/fetchClaimLedger";
    public static final String GetCallDetails = "/api/CRMCallLogAPI/GetCallDetails";
    public static final String GetCallUserList = "/api/CRMCallLogApi/GetCallUserList";
    public static final String api_remarks_sahara = "/api/DatasheetEntryAPI/GetRemarkDetails";

    //Activity Filter Paging

    public static final String api_getFilterActivityForAndroid = "/api/Timesheetapi/getFilterActivityForAndroid";
    public static final String api_Get_Carton_Packet_For_QR = "/api/PutPacketInCartonAPI/Get_Carton_Packet_For_QR";
    public static final String api_PackOrderPacking = "/api/PickingConfirmationApi/OrderPacking";


    public static final String api_GetRestartCallOnebyOne = "/api/CallListAPI/GetRestartCallOnebyOne";
    public static final String api_GetIgnoreCallOnebyOne = "/api/CallListAPI/GetIgnoreCallOnebyOne";


    public static final String api_SavePaidAmount = "/api/ShipmentEntryAPI/SavePaidAmount";
    //GetDeviceAuthentication
    public static final String api_GetDeviceAuthentication = "/api/LoginAPI/GetDeviceAuthentication";
    public static final String api_GetReviewDate = "/api/CallListAPI/GetReviewDate";

    // Used for send any Sourcetype
    public static final String api_UploadAttechmentDatasheet = "/api/UploadFilesAPI/UploadAttachment";


}
