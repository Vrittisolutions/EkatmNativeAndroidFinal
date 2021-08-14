package com.vritti.databaselib.data;

import android.content.ContentValues;
import android.content.Context;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHandlers extends SQLiteOpenHelper {

    //  private static final int DATABASE_VERSION = 63;
    private static final int DATABASE_VERSION = 101;
    private static final String DATABASE_NAME = "MainInfo.db";

    public DatabaseHandlers(Context context, String name) {
        super(context, name, null, DATABASE_VERSION);

    }

    public DatabaseHandlers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static final String TABLE_GetItemList = "GetItemList";
    public static final String TABLE_GetItemListPI = "PIGetItemList";
    //------------------------------ Tables(main.db) --------------------------
    public static final String TABLE_LOGIN_SETTING = "tableSetting";
    public static final String TABLE_ENVMASTER = "envtable";
    public static final String TABLE_PLANTMASTER = "palnttable";

    public static final String TABLE_PLANTMASTERCRM = "plantjhsj";//for testing
    public static final String TABLE_PLANTMASTERvwb = "plantvwb";//for testing

    /*---------------------------Commom Table VWB & PM-----------------------------------------------------*/
    public static final String TABLE_CLIENTS = "clientname";
    public static final String TABLE_CLIENTCONTACT_DETAILS = "clientcontact";

    /* --------------------------------------Common table-------------------------------------*/

    public static final String TABLE_BIRTHDAY = "Birthday";
    public static final String TABLE_MYTEAM = "MyTeam";
    public static final String TABLE_ALL_MEMBERS = "AllMembers";
    public static final String TABLE_GPS_SEND_NOTIFICATION = "Gpsnotification";
    public static final String TABLE_LEAVE_REPORTING_TO = "LeaveReporting";
    public static final String TABLE_DATA_OFFLINE = "offlinedata";
    public static final String TABLE_MODE_OF_JOURNY = "ModeOfJourny";
    public static final String TABLE_CRM_CALL = "CRM_Call";
    public static final String TABLE_CALL_LOG = "CRM_CallLog";
    public static final String TABLE_MEETING = "Meeting";
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String TABLE_GPSRECORDS = "GPSrecords";
    public static final String TABLE_ADD_GPSRECORDS = "Add_GPSrecords";//(Local records)
    public static final String TABLE_GET_GPSRECORDS = "Get_GPSrecords";//(Server records)
    public static final String TABLE_GPS_REPORTINGTO = "Gpsreportingto";
    public static final String TABLE_FORM_DATA = "DatasheetFirmData";
    public static final String TABLE_DATASHEET_DATA = "DatasheetData";
    public static final String TABLE_DATASHEET_DATA_NEW = "DatasheetData_New";
    public static final String TABLE_BRANCH_NAME = "DatasheetBranchName";
    public static final String TABLE_FORM_NAME = "FormNameSahara";
    public static final String TABLE_DATASHEET_ANS = "DatasheetAns";
    public static final String TABLE_DATASHEET_ANS_COMMON = "DatasheetAns_Common";
    public static final String TABLE_DATASHEET_SELECTION = "Datasheet_Selection";
    public static final String TABLE_DATASHEET_SELECTION_COMMON = "Datasheet_Selection_Common";

    public static final String TABLE_EDIT_DATASHEET = "Edit_Datasheet";
    public static final String TABLE_ATTACHMENT_DETAILS = "attachment_Details";

    //Chatting Database
    public static final String TABLE_CHAT_CHATROOM_MEMBER_LIST = "ChatRoomMemberList";
    public static final String TABLE_CHAT_ROOMNAME_DISPLAY_LIST = "ChatRoomNameDisplay";
    public static final String TABLE_CHAT_GROUP_MESSAGE = "ChatgroupMessage";
    public static final String TABLE_CHAT_USER_LIST = "ChatUserList";
    public static final String TABLE_CHAT_CHATROOM_GROUP_LIST = "ChatRoomUserList";
    public static final String TABLE_GROUP_JSON = "GroupJson";

    /* --------------------------------------Vwb  table-------------------------------------*/
    public static final String TABLE_ACTIVITYMASTER = "AcivityMaster";
    public static final String TABLE_ACTIVITYMASTER_PAGING_ASSIGN = "ActivityMaster_AssignByMe";
    public static final String TABLE_ACTIVITYMASTER_PAGING_CLIENTPAGE = "ActivityMaster_Clients";
    public static final String TABLE_ZPREPORTS = "ZPReports";
    public static final String TABLE_ACTIVITYMASTER_PAGING = "AcivityMasterDemo";
    public static final String TABLE_NEXTAPPR = "nextappr";
    public static final String TABLE_ACTIVITYMASTER_TEAM = "AcivityMasterteam";
    public static final String TABLE_SETTING = "tablesetting";
    public static final String TABLE_NEXTAPPR_SAHARA = "nextApprSahara";
    public static final String TABLE_MYWORK = "MyWork";
    public static final String TABLE_MYWORKSPACE = "MyWorkspace";
    public static final String TABLE_MYCLIENTS = "MYClients";
    public static final String TABLE_MYTEAM_DEPT = "MyTeamDept";
    public static final String TABLE_MYSUBTEAM_DEPT = "MySubTeamDept";
    public static final String TABLE_REASON_OUTAGE = "ReasonOutage";
    public static final String TABLE_POST_INSERT_TIMESHEET = "PostInsertTimesheet";
    public static final String TABLE_WORKSPACE_LIST = "WorkspaceList";
    public static final String TABLE_ISBILLABLE_AMOUNT = "isbillable";
    public static final String TABLE_MAINGROUP_LIST = "MainGroupList";
    public static final String TABLE_SUBGROUP_LIST = "SubGroupList";
    public static final String TABLE_PROJECT_MEMBERS = "ProjectMembers";
    public static final String TABLE_TASK_ACTIVITY = "TasksActivity";
    public static final String TABLE_CLAIM_APPROVER = "ClaimApprover";
    public static final String TABLE_COST_CENTER = "TableCostCenter";
    public static final String TABLE_ACTIVITY_TRAIL = "Activitytraildetail";
    public static final String TABLE_APP_DOC_INFO = "AppDocInfo";
    public static final String TABLE_LEAVE_SUMMARY = "LeaveSummary";
    public static final String TABLE_LEAVE_RECORDS = "Leaverecords";
    public static final String TABLE_GetMainDtl_APPROVAL = "GetMainDtl_Approval";
    public static final String TABLE_WORKSPACEWISE_ACT_CNT = "WorkspacewiseActcnt";
    public static final String TABLE_BIND_FINAL_OUTCOME = "finaloutcome";
    public static final String TABLE_TICKET_UPDATION_DATA = "ticketupdate";
    public static final String TABLE_Setup_TICKET_UPDATION = "Setup_ticketupdate";
    public static final String TABLE_NATURE_Of_WORK = "WorkNature";
    public static final String TABLE_Claim_Detail_Approver = "claimdetailappr";
    public static final String TABLE_AFFECTED_CUSTOMER_INFO = "Affectedcustomerinfo";
    public static final String TABLE_GetRouteFrom = "GetRouteFrom";
    public static final String TABLE_GetRouteTo = "GetRouteTo";
    public static final String TABLE_GetBindCategory = "Getbindcategory";
    public static final String TABLE_TICKET_UPDATE_WAREHOUSE = "warehouse";
    public static final String TABLE_TICKET_UPDATE_LOCATION = "location";
    public static final String TABLE_TICKET_UPDATE_CODE = "tablecode";
    public static final String TABLE_TICKET_UPDATE_MATERIAL_Add = "materrialAdd";
    public static final String TABLE_ActivityGetGroupList = "Activity_GroupList";
    public static final String TABLE_EMPLOYEE_REQUEST_APPROVER = "EmployeeRequestApprover";
    public static final String TABLE_CLAIM_SUMMARY = "ClaimSummary";
    public static final String TABLE_TICKET_UPDATION_DATA_Vw = "ticketupdatevw";
    public static final String TABLE_CLAIM_NOTIFICATION = "Claimnotification";

    /* --------------------------------------CRM table-------------------------------------*/

    public static final String TABLE_SubTeam_Member = "SubTeam_Member";
    public static final String TABLE_Team_Member = "Team_Member";

    public static final String TABLE_CRM_CALL_PARTIAL = "CRM_Call_Partial";
    public static final String TABLE_CRM_CALL_TEAM = "CRM_Call_TEAM";
    public static final String TABLE_CRM_CALL_PARTIAL_TEAM = "CRM_Call_Partial_TEAM";
    public static final String TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM = "CRM_CollectionCall_Partial_TEAM";
    public static final String TABLE_CRM_CALL_OPP = "CRM_Call_opp";
    public static final String TABLE_CRM_CALL_PARTIAL_OPP = "CRM_Call_Partial_opp";
    public static final String TABLE_CALL_RATING = "CallRating";
    public static final String TABLE_APPOINTMENT = "Appointment";
    public static final String TABLE_OPPORTUNITIES = "Opportunities";
    public static final String TABLE_ENQUIRY = "EnquiryTable";
    public static final String TABLE_COLLECTION = "Collection";
    public static final String TABLE_REASON = "Reason";
    public static final String TABLE_NatureofCall = "NatureofCall";
    public static final String TABLE_InitiatedBy = "InitiatedBy";
    public static final String TABLE_With_whom = "With_whom";
    public static final String TABLE_Followup_reason = "Followup_reason";
    public static final String TABLE_Outcome = "Outcome";
    public static final String TABLE_Category = "Category";
    public static final String TABLE_REASON_Master = "ReasonMaster";
    public static final String TABLE_APPROVER = "Approver";
    public static final String TABLE_CurrencyMaster = "CurrencyMaster";
    public static final String TABLE_OrderType = "OrderType";
    public static final String TABLE_OrderTypeMaster = "OrderTypeMaster";
    public static final String TABLE_OrderTypeMaster_All = "OrderTypeMaster_All";
    public static final String TABLE_TMESEName = "TMESEName";
    public static final String TABLE_User_Type = "User_Type";
    public static final String TABLE_CITY = "City";
    public static final String TABLE_CITY_PROSPECT = "Prospect_City";
    public static final String TABLE_CITY_MASTER = "city_Master";
    public static final String TABLE_Firm = "Firm";
    public static final String TABLE_Product = "Product";
    public static final String TABLE_filterdata_prospect = "filterdata_prospect";
    public static final String TABLE_Setup_Prospect = "Setup_Prospect";
    public static final String TABLE_Teritory = "Teritory";
    public static final String TABLE_Territory = "territory_Prospect";
    public static final String TABLE_Business_segment = "Business_segment";
    public static final String TABLE_Prospectsource = "Prospectsource";
    public static final String TABLE_Reference = "Reference";
    public static final String TABLE_Referencetype = "Referencetype";
    public static final String TABLE_Consignee = "Consignee";
    public static final String TABLE_Entity = "Entity";
    public static final String TABLE_SE = "SE";
    public static final String TABLE_BOE = "BOE";
    public static final String TABLE_FollowUpTime = "FollowUpTime";
    public static final String TABLE_Campaign = "Campaign";

    public static final String TABLE_Product_Details = "ProductDetails";
    public static final String TABLE_SALES_FAMILY_PRODUCT = "salesfamilytable";
    public static final String TABLE_Travelplan = "Travelplan";
    public static final String TABLE_Executive = "Executive";
    public static final String TABLE_CALLHISTORY = "CallHistory";
    public static final String TABLE_CALLLISTDATA = "Calllistdata";
    public static final String TABLE_STATE = "Statelist";
    public static final String TABLE_CONTACT_DETAILS = "CONTACT_DETAILS_FETCH";
    public static final String TABLE_PRODUCT_DATA_FETCH = "Product_data_fetch";
    public static final String TABLE_FILLCONTROL_DATA_FETCH = "allremaining_data_fetch";
    public static final String TABLE_SHOW_CONTACT = "showcontact";
    public static final String TABLE_Feedback = "Feedback";
    public static final String TABLE_Callfilter = "Callfilter";
    public static final String TABLE_CRM_CALL_OPPORTUNITY = "CRM_Call_Opportunity";
    public static final String TABLE_CRM_CALL_FEEDBACK = "CRM_FeedbackCall_Opportunity";
    public static final String TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY = "CRM_Call_Collection";
    //Squlite Data
    public static final String TABLE_OPPORTUNITY_UPDATE = "SqliteOpportunity";
    public static final String TABLE_CRM_OPPOTUNITY_CALL_FILTER = "CRM_opprtunity_Call_Partial";
    public static final String TABLE_CUSTOMER = "Customer";
    public static final String TABLE_SUPPLIER = "Suppliertable";
    public static final String TABLE_SUPPLIER_PLANT = "Supplierplanttable";
    public static final String TABLE_PROVISINALLIST = "Provisional";
    public static final String TABLE_BANKNAME = "Bankname";
    public static final String TABLE_Calender = "Calender";
    public static final String TABLE_AddBy = "Addedby";
    //pradnya
    public static final String TABLE_PROSPECT_VALIDATIONS = "validationstable";
    public static final String TABLE_PROSPECT_SERIAL = "prospectserial";
    public static final String TABLE_COUNTRY = "country";
    public static final String TABLE_TICKET_CUSTOMER_LIST = "CUSTOMERlist";
    public static final String TABLE_TICKET_COUNT = "ticketcount";
    public static final String TABLE_TICKET_COUNT_MONTHWISE = "ticketmonthwise";//
    public static final String TABLE_TICKET_COUNT_DETAIL = "ticketdetailks";
    public static final String TABLE_PROMOTER = "Promoter";
    public static final String TABLE_PROMOTER_REPORT = "Promoterreport";
    public static final String TABLE_Product_Details_New = "ProductDetailsNew";
    //------------------------------PM --------------------------

    public static final String TABLE_SUPPORTTICKET_COUNT = "supportticketcount";

    // Inventory
    public static final String TABLE_EntityType = "EntityType";
    public static final String TABLE_Country_INVEN = "Country_INV";
    public static final String TABLE_City_INV = "City_INV";
    public static final String TABLE_Currency = "Currency";

    //-------------------------------------DeliveryApplication-------------------

    public static final String TABLE_DELIVERY_BOY = "deliveryBoyTable";

    //Added by Chetana Sales - CounterBilling
    /*------------------------------Sales Order Booking/ counter sales----------------------------------------------*/
    public static final String TABLE_ALL_CAT_SUBCAT_ITEMS = "getAllCatSubItem";
    public static final String TABLE_MERCHANT_AGAINST_ITEM = "getMerchantsAgainstItems";
    public static final String TABLE_MERCHANTS = "getMerchants";
    public static final String TABLE_PLACE_ORDER = "getPlaceOrder";
    public static final String TABLE_CART_ITEM = "getCartItems";
    public static final String TABLE_CART_ITEM_VOLUME_DISCOUNT = "getCartItemsVolumeDiscount";
    public static final String TABLE_MY_ADDRESS = "getAddress";
    public static final String TABLE_MY_ORDER_HISTORY = "MyOrderHistory";
    public static final String TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS = "AssignActivityDetails";
    public static final String TABLE_STATES_SALES = "getStates";
    public static final String TABLE_CITY_SALES = "getCities";
    public static final String TABLE_STATE_ENTITY = "stateEntity";
    public static final String TABLE_CITY_ENTITY = "cityEntity";
    public static final String TABLE_ENTITY_TERRITORY = "TerritoryEntity";
    public static final String TABLE_DISTRICT = "Prospect_City_sales";
    public static final String TABLE_TALUKA_ENTITY = "talukaEntity";
    public static final String TABLE_ENTITY_TYPE = "EntityType_sales";
    public static final String TABLE_ENTITY_CATEGORY = "EntityType_category";
    public static final String TABLE_ENTITY_SYSTEM_USERDATA = "EntityType_systemuser";
    public static final String TABLE_ENTITY_PAYMENT_TERMS = "EntityType_paymentTerms";
    public static final String TABLE_ENTITY_DELIVERY_TERMS = "EntityType_deliveryTerms";
    public static final String TABLE_ENTITY_TYPE_OF_SERVICE = "EntityType_typeOfService";
    public static final String TABLE_ENTITY_CLASS = "EntityType_class";
    public static final String TABLE_ENTITY_GROUP = "EntityType_group";
    public static final String TABLE_ENTITY_TAXCODE = "EntityType_Taxcode";
    public static final String TABLE_SALES_PRICELIST = "SalesPriceList";
    public static final String TABLE_TAXCLASS = "TaxClassTable";
    public static final String TABLE_ENTITY_BI_DATA = "Entity_BIData";
    public static final String TABLE_CONFIGURATION = "ConfigDropdown";
    public static final String TABLE_CHARGE = "Charge";
    public static final String TABLE_PAYMENT_TERMS = "PaymentTerms";
    public static final String TABLE_COMMISSION = "Commission";
    public static final String TABLE_CATEGORY_REIMB = "Reimb_category";
    public static final String TABLE_WARRANTY = "Warranty";
    public static final String TABLE_UOM_new = "UomSO";
    public static final String TABLE_MODEL = "ModelNo";
    public static final String TABLE_SALES_FAMILY = "SalesFamily";
    public static final String TABLE_PRICELIST = "PriceList";
    public static final String TABLE_SOHEADER = "SOHeader";
    public static final String TABLE_SODETAIL = "SODetail";
    public static final String TABLE_SOHistory = "SOHistory";
    public static final String TABLE_MY_ORDER_ACCEPTANCE = "OrderAcceptanceData";
    public static final String TABLE_BUS_SEGMENT = "BusSegment";
    public static final String TABLE_FAMILY_MASTERDATA = "FamilyMaster";
    public static final String TABLE_ALL_CAT_SUBCAT_ITEMS_new = "getAllCatSubItem_new";
    public static final String TABLE_CART_ITEM_new = "getCartItems_new";
    public static final String TABLE_PENDING_DELIVERY = "PendingDelivery";

    //Counter Billing
    public static final String TABLE_BLUETOOTH_ADDRESS = "Bluetooth_Address";
    public static final String TABLE_CUSTOMER_CB = "CUSTOMER_CB";
    public static final String TABLE_PRODUCT_CB = "Product_CB";
    public static final String TABLE_MARCHANT_ITEM_RUNI = "Marchant_Item_Runi";
    public static final String TABLE_PENDING_BALANCE = "pending_balance";
    public static final String TABLE_GET_REPORTS = "getReports";
    public static final String TABLE_ITEM_MRP = "Item_mrp";//TABLE_ITEM_MRP_Runi
    public static final String TABLE_CART_ITEM_CB = "Cart_Item_CB";
    public static final String TABLE_BILL_CB = "BILL_CB";
    public static final String TABLE_BILL_DETAILS = "BILL_DETAILS_CB";
    public static final String TABLE_GET_MARCHANT_PO = "getMarchantPO";
    public static final String TABLE_ITEM_MRP_Runi = "Item_mrp_Runi";
    public static final String TABLE_MARCHANT_ITEM = "Marchant_Item";//TABLE_MARCHANT_ITEM_RUNI
    public static final String TABLE_MY_ORDER = "getbookedO" + "rdVendor";
    public static final String TABLE_TRADE_DISCOUNT = "Trade_discount";
    public static final String TABLE_VOLUME_DISCOUNT = "Volume_discount";
    public static final String TABLE_PURCHASE_ITEM_CB = "PURCHASE_Item_CB";
    public static final String TABLE_PURCHASE_FINAL_ITEM_CB = "PURCHASE_FINAL_Item_CB";
    public static final String TABLE_ADD_ITMDTLS_FORBILL = "ItemDetailsForBill";
    public static final String TABLE_PROSPECT_VALIDATIONS_SALES = "validationstable_sales";
    public static final String TABLE_DELIVERY_AGENTS = "DeliveryAgents";
    public static final String TABLE_SHIPMENT_INVOICE = "Shipment_Invoice";
    public static final String TABLE_CONSIGNEES = "Consignee_Table";
    public static final String TABLE_SHIPTO_DETAILS = "ShipToDetails";
    public static final String TABLE_ADD_ITEMS_COUNTERBILL = "ItemsCodeMRP";
    public static final String TABLE_COMPANY_DETAILS = "CompanyDetails";


    /************************************Milk Run***************************************/

    public static final String TABLE_DELIVERY_MILK_RUN = "milkRunTable";


    //Expense Management

    public static final String TABLE_EXPENSE = "expense";


    //Hyva Project

    public static final String TABLE_PRINT = "Print";


    /************************************PI Generation Hyva***************************************/
    public static final String TABLE_LOCATION_PI = "hyvaPILocation";
    public static final String TABLE_PI_GENERATION = "physicalInventory";
    public static final String TABLE_UOM = "UomMaster";

    /*--------------------------------------------------------------------------------------------------------------*/


    /*-------------------------------------------------------------------------------------------------------------------*/

    //Alfa Laval

    public static final String TABLE_PUTAWAY_USER = "Table_Putaway_User";
    public static final String TABLE_PUTAWAY = "Table_Putaway_Detail";//TABLE_PUTAWAY-Detail
    public static final String TABLE_LOCATION_MASTER = "Table_Location";
    public static final String TABLE_PUTAWAY_PACKET_DETAIL = "Table_Putaway_Packet_Detail";
    public static final String TABLE_MRS = "Table_MRS";
    public static final String TABLE_MRS_DETAIL = "Table_MRS_Detail";
    public static final String TABLE_BOX = "Table_Box";
    public static final String TABLE_SECONDARY_BOX = "Table_sec_Box";
    public static final String TABLE_CARTAN_PICKLIST = "Table_cart_pick";
    public static final String TABLE_ITEM_PICKLIST = "Table_Item_pick";
    public static final String TABLE_ITEM_PICKLIST_SUGGLOT = "Table_Item_suglot";
    public static final String TABLE_GRN_PACKET = "GRN_packet";
    public static final String TABLE_GRNNO_PACKET = "Table_Packet";
    public static final String TABLE_GRN_POST_ITEM = "Table_GRNPostItem";
    public static final String TABLE_GRN_POST = "Table_GRNPost";

    /*************material requisition note************************/
    public static final String TABLE_PlantList = "Inventory_Plantlist";
    public static final String TABLE_MaterialItemList = "Inventory_MaterialItemList";
    public static final String TABLE_Suppliername = "Inventory_Suppliername";
    public static final String TABLE_WAREHOUSE = "Inventory_Warehouse";
    public static final String TABLE_Department = "Inventory_Department";
    public static final String TABLE_AddMaterialPoOrder = "Inventory_POrder";
    public static final String TABLE_WORK_ORDER = "Inventory_Workorder";
    public static final String TABLE_PODETAILS = "Inventory_Podetails";
    public static final String TABLE_LOCATION = "Inventory_Location";
    public static final String TABLE_PoitemDetails = "Inventory_PoitemDetails";


    /********************Material Issue***********************/
    public static final String TABLE_MRSList = "Inventory_MRSList";
    public static final String TABLE_GETALLUsers = "Inventory_GETAllUsers";


    /*--------------------------------------------------------------------------------------------------------------*/

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Factory.CREATE_TABLE_TABLE_GetItemList);
        db.execSQL(Factory.CREATE_TABLE_TABLE_PIGetItemList);
        //------------------------------ Tables(main.db) --------------------------
        db.execSQL(Factory.CREATE_TABLE_LOGIN_SETTING);
        db.execSQL(Factory.CREATE_TABLE_ENVMASTER);
        db.execSQL(Factory.CREATE_TABLE_PLANTMASTER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PLANTMASTERCRM);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_PLANTMASTERVWB);

        //------------------------------Common tables --------------------------
        db.execSQL(Factory.CREATE_CLIENTNAMEID_TABLE);
        db.execSQL(Factory.CREATE_ClIENTCONTACTDETAILS_TABLE);


        db.execSQL(Factory.CREATE_BIRTHDAY_TABLE);
        db.execSQL(Factory.CREATE_MYTEAM_TABLE);
        db.execSQL(Factory.CREATE_ALL_MEMBERS_TABLE);
        db.execSQL(Factory.CREATE_TABLE_GPS_SEND_NOTIFICATION);
        db.execSQL(Factory.CREATE_TABLE_LEAVE_REPORTING_TO);
        db.execSQL(Factory.CREATE_TABLE_DATA_OFFLINE);
        db.execSQL(Factory.CREATE_MODE_OF_JOURNY_TABLE);
        db.execSQL(Factory.CREATE_TABLE_CRM_CALL);
        db.execSQL(Factory.CREATE_TABLE_CALL_LOG);
        db.execSQL(Factory.CREATE_TABLE_MEETING);
        db.execSQL(Factory.CREATE_TABLE_NOTIFICATION);
        db.execSQL(Factory.CREATE_GPSRECORDS_TABLE);
        db.execSQL(Factory.CREATE_GPSRECORDS_ADD_TABLE);
        db.execSQL(Factory.CREATE_GPSRECORDS_GET_TABLE);
        db.execSQL(Factory.CREATE_GPSREPORTINGTO_TABLE);
        db.execSQL(Factory.CREATE_FORM_DATA_TABLE);
        db.execSQL(Factory.CREATE_DATASHEET_DATA_TABLE);
        db.execSQL(Factory.CREATE_DATASHEET_DATA_TABLE_NEW);
        db.execSQL(Factory.CREATE_BRANCH_NAME_TABLE);
        db.execSQL(Factory.CREATE_FORM_NAME_SAHARA);
        // db.execSQL(Factory.CREATE_TABLE_VIEW_REPORTS_ZP);
        db.execSQL(Factory.CREATE_TABLE_Datasheet_ANS);
        db.execSQL(Factory.CREATE_TABLE_Datasheet_ANS_COMMON);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_EDIT);
        db.execSQL(Factory.CREATE_TABLE_ATTACH_DETAILS_SAHARA);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_SELECTION);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_SELECTION_COMMON);
        db.execSQL(Factory.CREATE_TABLE_GROUP_JSON);
        db.execSQL(Factory.CREATE_TABLE_CHAT_CHATROOM_MEMBER_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_CHATROOM_GROUP_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_GROUP_MESSAGE);
        db.execSQL(Factory.CREATE_TABLE_CHAT_ROOMNAME_DISPLAY_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_USER_LIST);

        //------------------------------Vwb --------------------------

        db.execSQL(Factory.vwbFactory.CREATE_TABLE_SUPPLIER);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_SUPPLIER_PLANT);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NEXTAPPR);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTER_TABLE);//TABLE_ACTIVITYMASTER
        db.execSQL(Factory.vwbFactory.CREATE_ASSIGNBYMEPAGING_TABLE);//TABLE_ACTIVITYMASTER_ASSIGN
        db.execSQL(Factory.vwbFactory.CREATE_CLIENTPAGING_TABLE);//TABLE_ACTIVITYMASTER_CLIENT
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTERPAGING_TABLE);//TABLE_ACTIVITYMASTER
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTER_TEAM_TABLE);//TABLE_ACTIVITYMASTER_TEAM
        db.execSQL(Factory.vwbFactory.CREATE_MYWORK_TABLE);//TABLE_MYWORK
        db.execSQL(Factory.vwbFactory.CREATE_MYWORKSPACE_TABLE);//TABLE_MYWORKSPACE
        db.execSQL(Factory.vwbFactory.CREATE_MYCLIENTS_TABLE);//TABLE_MYCLIENTS
        db.execSQL(Factory.vwbFactory.CREATE_MYTEAM_DEPT_TABLE);//TABLE_MYTEAM_DEPT
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NEXTAPPR_SAHARA);//TABLE_NEXTAPPR_SAHARA
        db.execSQL(Factory.vwbFactory.CREATE_MYSUBTEAM_DEPT_TABLE);//TABLE_MYSUBTEAM_DEPT
        db.execSQL(Factory.vwbFactory.CREATE_POST_INSERT_TIMESHEET_TABLE);//TABLE_POST_INSERT_TIMESHEET
        db.execSQL(Factory.vwbFactory.CREATE_WORKSPACE_LIST_TABLE);//TABLE_WORKSPACE_LIST
        db.execSQL(Factory.vwbFactory.CREATE_SUBGROUP_LIST_TABLE);//TABLE_SUBGROUP_LIST
        db.execSQL(Factory.vwbFactory.CREATE_MAINGROUP_LIST_TABLE);//TABLE_MAINGROUP_LIST
        db.execSQL(Factory.vwbFactory.CREATE_PROJECT_MEMBERS_TABLE);//TABLE_PROJECT_MEMBERS
        db.execSQL(Factory.vwbFactory.CREATE_TASK_ACTIVITY_TABLE);//TABLE_TASK_ACTIVITY
        db.execSQL(Factory.vwbFactory.CREATE_CLAIM_APPROVER_TABLE);//TABLE_CLAIM_APPROVER
        db.execSQL(Factory.vwbFactory.CREATE_EMPREQUEST_APPROVER_TABLE);//TABLE_EMPREQUEST_APPROVER

        db.execSQL(Factory.vwbFactory.CREATE_TABLE_CLAIM_SUMMARY);//TABLE_EMPREQUEST_APPROVER
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_COST_CENTER);//TABLE_COST_CENTER
        db.execSQL(Factory.vwbFactory.CREATE_LEAVE_SUMMARY_TABLE);//TABLE_LEAVE_SUMMARY
        db.execSQL(Factory.vwbFactory.CREATE_LEAVE_RECORDS_TABLE);//TABLE_LEAVE_RECORDS
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITY_TRAIL_TABLE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_APP_DOC_INFO);//TABLE_APP_DOC_INFO
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_GetMainDtl_APPROVAL);//TABLE_GetMainDtl_APPROVAL
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_WORKSPACEWISE_ACT_CNT);//TABLE_WORKSPACEWISE_ACT_CNT
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_BINDFINALOUTCOME);//TABLE_BIND_FINAL_OUTCOME
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_UPDATION_DATA);//TABLE_TICKET_UPDATION_DATA
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_Setup_TICKET_UPDATION);//TABLE_Setup_TICKET_UPDATION
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NATURE_OF_WORK);//TABLE_NATURE_Of_WORK
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_CLAIM_DETAIL_Appr);//TABLE_Claim_Detail_Approver
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_REASON_OUTAGE);//TABLE_REASON_OUTAGE
        db.execSQL(Factory.vwbFactory.CREATE_AFFECTED_CUSTOMER_INFO);//TABLE_AFFECTED_CUSTOMER_INFO
        db.execSQL(Factory.vwbFactory.CREATE_GETROUTEFROM_INFO);//TABLE_GetRouteFrom
        db.execSQL(Factory.vwbFactory.CREATE_GETROUTETO_INFO);//TABLE_GetRouteTo
        db.execSQL(Factory.vwbFactory.CREATE_GETBINDCATEGORY_INFO);//TABLE_GetBindCategory
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_IS_BILLABLE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_WAREHOUSE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_LOCATION);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_CODE);//
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_MATERIAL_Add);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_UPDATION_DATA_Vw);
        db.execSQL(Factory.vwbFactory.CREATE_CLAIM_NOTIFICATION_TABLE);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIITY_GROUPLIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SUPPORTTICKET_COUNT);

        //------------------------------CRM --------------------------

        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROSPECT_VALIDATIONS);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROSPECT_SERIAL);

        db.execSQL(Factory.crmFactory.CREATE_TABLE_COUNTRY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SALES_FAMILY_PRODUCT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_ENQUIRY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROMOTER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROMOTER_REPORT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_CUSTOMER_LIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT_MONTHWISE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT_DETAIL);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_ADDBY_JSON);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Teritory);
        db.execSQL(Factory.crmFactory.CREATE_CALLHISTORY_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Reason);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_REASON_Master);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Calllistdata);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_TEAM_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_NatureofCall);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_APPROVER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_InitiatedBy);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_With_whom);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CurrencyMaster);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderTypeMaster);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Executive);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Category);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Followup_reason);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Outcome);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderTypeMaster_All);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TMESEName);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_PROSPECT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_MASTER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SHOW_CONTACT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CRM_CALL_PARTIAL_OPP);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CRM_CALL_OPP);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_User_Type);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CALL_RATING);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_APPOINTMENT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OPPORTUNITIES);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_filterdata_prospect);
        db.execSQL(Factory.crmFactory.CREATE_CRM_OPPOTUNITY_CALL_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_PARTIAL_TEAM_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_CRM_COLLECTIONCALL_PARTIAL_TEAM_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_PARTIAL_TABLE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FEEDBACK);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_COLLECTION);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Reference);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_StateList);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CONTACT_FETCH);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PRODUCTDATA_FETCH);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FILLCONTROL_FETCH);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderType);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_BOE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FollowUpTime);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Campaign);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SuspectSource);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product_Details);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Business_segment);
        db.execSQL(Factory.crmFactory.CREATE_SubTeam_Member);
        db.execSQL(Factory.crmFactory.CREATE_Team_Member);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_FEEDBACK);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_OPPORTUNITY);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_COllECTION_OPPORTUNITY);
        db.execSQL(Factory.crmFactory.CREATE_SQLITE_OPPORTUNITY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CUSTOMER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROVISINALLIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_BANKNAME);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CALENDER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Travelplan);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Firm);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Setup_Prospect);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Entity);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Consignee);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Referencetype);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product_Details_New);
        //------------------------------PM --------------------------
//Inventory
        db.execSQL(Factory.crmFactory.CREATE_TABLE_EntityType);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_COUNTRYLIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_INVEN);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Currency);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_PlantList);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_MATERIALITEMLIST);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_Suppliername);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_WAREHOUSE);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_DEPARTMENT);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_ADDMATERIALPOORDER);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_WORK_ORDER);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_PODETAILS);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_LOCATION);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_POITEMDETAILS);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_MRSList);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_GETALLUsers);


//------------------------------DeliveryBoy --------------------------
        db.execSQL(Factory.crmFactory.CREATE_TABLE_DeliveryBoy);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_MilkRun);

        /* ----------------------------Sales / Counter billing ----------------------------------*/
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MERCHANT_AGAINST_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MERCHANTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PLACE_ORDER);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ADDRESS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ORDER_HISTORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DELIVERY_AGENTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SHIPMENT_INVOICE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_STATES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CITIES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_STATE_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CITY_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DISTRICT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TALUKA_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TYPE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SALES_PRICELIST);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SHIPTO_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TAXCLASS);
        /*db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TERRITORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_CATEGORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_SYSTEM_USERDATA);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_PAYMENT_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_DELIVERY_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TYPE_OF_SERVICE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_CLASS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_GROUP);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TAXCODE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_BI_DATA);*/

        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CONFIGURATION);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CHARGE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PAYMENT_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_COMMISSION);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CATEGORY_REIMB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_WARRANTY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_UOM_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SOHEADER);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SODETAIL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SOHistory);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MODEL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SALESFAMILY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PRICELIST);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ORDER_ACCEPTANCE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BUS_SEGMENT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_FAMILY_MASTERDATA);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PENDING_DELIVERY);

        //Counter Billing
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BLUETOOTH_ADDRESS);
        db.execSQL(Factory.TbudsFactory.CREATE_CUSTOMER_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PRODUCT_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MARCHANT_ITEM_RUNI);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PENDING_BALANCE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_GET_REPORTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ITEM_MRP);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BILL_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BILL_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_GET_MARCHANT_PO);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ITEM_MRP_Runi);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MARCHANT_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TRADE_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_VOLUME_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PURCHASE_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PURCHASE_FINAL_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ADD_ITMDTLS_FORBILL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PROSPECT_VALIDATIONS_SALES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CONSIGNEES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ADD_ITEMS_COUNTERBILL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_COMPANY_DETAILS);

        /*---------------------------------------------------------------------------------------*/


        //Expense

        db.execSQL(Factory.crmFactory.CREATE_TABLE_EXPENSE);


        //HYVA project

        db.execSQL(Factory.crmFactory.CREATE_TABLE_PRINT_LABEL);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_LOCATION_PI);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_PI_GENERATION);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_UOM);


        /*----------------------------------------------------------------------*/

        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_PUTAWAY_USER);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GETPUTAWAY);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GetLoactionMaster);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GetTablePutawayPacketDetail);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_MRS);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_MRS_DETAIL);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_BOX);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_SECONDARY_BOX);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_CARTAN_PICKLIST);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRNPACKET);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_ITEM_PICKLIST);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRNNOPACKET);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRN_POST_ITEM);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRN_POST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //------------------------------ Tables(main.db) --------------------------

      /*  db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN_SETTING);
        db.execSQL(Factory.CREATE_TABLE_LOGIN_SETTING);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENVMASTER);
        db.execSQL(Factory.CREATE_TABLE_ENVMASTER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTMASTER);
        db.execSQL(Factory.CREATE_TABLE_PLANTMASTER);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTMASTERCRM);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PLANTMASTERCRM);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLANTMASTERvwb);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_PLANTMASTERVWB);*/

        //------------------------------common tables --------------------------

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetItemList);
        db.execSQL(Factory.CREATE_TABLE_TABLE_GetItemList);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetItemListPI);
        db.execSQL(Factory.CREATE_TABLE_TABLE_PIGetItemList);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIRTHDAY);
        db.execSQL(Factory.CREATE_BIRTHDAY_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLIENTS);
        db.execSQL(Factory.CREATE_CLIENTNAMEID_TABLE);

       /* db.execSQL("DROP TABLE IF EXISTS " +TABLE_CLIENTCONTACT_DETAILS );
        db.execSQL(Factory.CREATE_ClIENTCONTACTDETAILS_TABLE);
*/

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYTEAM);
        db.execSQL(Factory.CREATE_MYTEAM_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_MEMBERS);
        db.execSQL(Factory.CREATE_ALL_MEMBERS_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS_SEND_NOTIFICATION);
        db.execSQL(Factory.CREATE_TABLE_GPS_SEND_NOTIFICATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE_REPORTING_TO);
        db.execSQL(Factory.CREATE_TABLE_LEAVE_REPORTING_TO);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA_OFFLINE);
        db.execSQL(Factory.CREATE_TABLE_DATA_OFFLINE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODE_OF_JOURNY);
        db.execSQL(Factory.CREATE_MODE_OF_JOURNY_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL);
        db.execSQL(Factory.CREATE_TABLE_CRM_CALL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_LOG);
        db.execSQL(Factory.CREATE_TABLE_CALL_LOG);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEETING);
        db.execSQL(Factory.CREATE_TABLE_MEETING);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL(Factory.CREATE_TABLE_NOTIFICATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPSRECORDS);
        db.execSQL(Factory.CREATE_GPSRECORDS_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_GPSRECORDS);
        db.execSQL(Factory.CREATE_GPSRECORDS_ADD_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GET_GPSRECORDS);
        db.execSQL(Factory.CREATE_GPSRECORDS_GET_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS_REPORTINGTO);
        db.execSQL(Factory.CREATE_GPSREPORTINGTO_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_DATA);
        db.execSQL(Factory.CREATE_FORM_DATA_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_DATA);
        db.execSQL(Factory.CREATE_DATASHEET_DATA_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_DATA_NEW);
        db.execSQL(Factory.CREATE_DATASHEET_DATA_TABLE_NEW);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH_NAME);
        db.execSQL(Factory.CREATE_BRANCH_NAME_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORM_NAME);
        db.execSQL(Factory.CREATE_FORM_NAME_SAHARA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_ANS);
        db.execSQL(Factory.CREATE_TABLE_Datasheet_ANS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_ANS_COMMON);
        db.execSQL(Factory.CREATE_TABLE_Datasheet_ANS_COMMON);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_SELECTION);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_SELECTION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATASHEET_SELECTION_COMMON);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_SELECTION_COMMON);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDIT_DATASHEET);
        db.execSQL(Factory.CREATE_TABLE_DATASHEET_EDIT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTACHMENT_DETAILS);
        db.execSQL(Factory.CREATE_TABLE_ATTACH_DETAILS_SAHARA);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_JSON);
        db.execSQL(Factory.CREATE_TABLE_GROUP_JSON);

     /*   db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_CHATROOM_MEMBER_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_CHATROOM_MEMBER_LIST);*/

      /*  db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_CHATROOM_MEMBER_LIST);

        db.execSQL(Factory.CREATE_TABLE_CHAT_CHATROOM_MEMBER_LIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_CHATROOM_GROUP_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_CHATROOM_GROUP_LIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_GROUP_MESSAGE);
        db.execSQL(Factory.CREATE_TABLE_CHAT_GROUP_MESSAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_ROOMNAME_DISPLAY_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_ROOMNAME_DISPLAY_LIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_USER_LIST);
        db.execSQL(Factory.CREATE_TABLE_CHAT_USER_LIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_JSON);
        db.execSQL(Factory.CREATE_TABLE_GROUP_JSON);*/


        //------------------------------Vwb --------------------------


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER_PLANT);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_SUPPLIER_PLANT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPLIER);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_SUPPLIER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEXTAPPR);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NEXTAPPR);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYMASTER);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTER_TABLE);//TABLE_ACTIVITYMASTER

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYMASTER_PAGING_ASSIGN);
        db.execSQL(Factory.vwbFactory.CREATE_ASSIGNBYMEPAGING_TABLE);//TABLE_ACTIVITYMASTER

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYMASTER_PAGING_CLIENTPAGE);
        db.execSQL(Factory.vwbFactory.CREATE_CLIENTPAGING_TABLE);//TABLE_ACTIVITYMASTER


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYMASTER_PAGING);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTERPAGING_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITYMASTER_TEAM);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITYMASTER_TEAM_TABLE);//TABLE_ACTIVITYMASTER_TEAM


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEXTAPPR_SAHARA);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NEXTAPPR_SAHARA);

     /*   db.execSQL("DROP TABLE IF EXISTS " + TABLE_ZPREPORTS);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_VIEW_REPORTS_ZP);*/


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYWORK);
        db.execSQL(Factory.vwbFactory.CREATE_MYWORK_TABLE);//TABLE_MYWORK

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYWORKSPACE);
        db.execSQL(Factory.vwbFactory.CREATE_MYWORKSPACE_TABLE);//TABLE_MYWORKSPACE

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYCLIENTS);
        db.execSQL(Factory.vwbFactory.CREATE_MYCLIENTS_TABLE);//TABLE_MYWORKSPACE

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYTEAM_DEPT);
        db.execSQL(Factory.vwbFactory.CREATE_MYTEAM_DEPT_TABLE);//TABLE_MYTEAM_DEPT

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MYSUBTEAM_DEPT);
        db.execSQL(Factory.vwbFactory.CREATE_MYSUBTEAM_DEPT_TABLE);//TABLE_MYSUBTEAM

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST_INSERT_TIMESHEET);
        db.execSQL(Factory.vwbFactory.CREATE_POST_INSERT_TIMESHEET_TABLE);//TABLE_POST_INSERT_TIMESHEET

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACE_LIST);
        db.execSQL(Factory.vwbFactory.CREATE_WORKSPACE_LIST_TABLE);//TABLE_WORKSPACE_LIST

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBGROUP_LIST);
        db.execSQL(Factory.vwbFactory.CREATE_SUBGROUP_LIST_TABLE);//TABLE_SUBGROUP_LIST

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAINGROUP_LIST);
        db.execSQL(Factory.vwbFactory.CREATE_MAINGROUP_LIST_TABLE);//TABLE_MAINGROUP_LIST

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECT_MEMBERS);
        db.execSQL(Factory.vwbFactory.CREATE_PROJECT_MEMBERS_TABLE);//TABLE_PROJECT_MEMBERS

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK_ACTIVITY);
        db.execSQL(Factory.vwbFactory.CREATE_TASK_ACTIVITY_TABLE);//TABLE_TASK_ACTIVITY

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLAIM_APPROVER);
        db.execSQL(Factory.vwbFactory.CREATE_CLAIM_APPROVER_TABLE);//TABLE_CLAIM_APPROVER

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE_REQUEST_APPROVER);
        db.execSQL(Factory.vwbFactory.CREATE_EMPREQUEST_APPROVER_TABLE);//TABLE_EMPREQUEST_APPROVER

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLAIM_SUMMARY);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_CLAIM_SUMMARY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COST_CENTER);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_COST_CENTER);//TABLE_COST_CENTER

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE_SUMMARY);
        db.execSQL(Factory.vwbFactory.CREATE_LEAVE_SUMMARY_TABLE);//TABLE_LEAVE_SUMMARY

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVE_RECORDS);
        db.execSQL(Factory.vwbFactory.CREATE_LEAVE_RECORDS_TABLE);//TABLE_LEAVE_RECORDS

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITY_TRAIL);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIVITY_TRAIL_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP_DOC_INFO);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_APP_DOC_INFO);//TABLE_APP_DOC_INFO

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetMainDtl_APPROVAL);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_GetMainDtl_APPROVAL);//TABLE_GetMainDtl_APPROVAL

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACEWISE_ACT_CNT);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_WORKSPACEWISE_ACT_CNT);//TABLE_WORKSPACEWISE_ACT_CNT

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BIND_FINAL_OUTCOME);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_BINDFINALOUTCOME);//TABLE_BIND_FINAL_OUTCOME

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATION_DATA);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_UPDATION_DATA);//TABLE_TICKET_UPDATION_DATA

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Setup_TICKET_UPDATION);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_Setup_TICKET_UPDATION);//TABLE_Setup_TICKET_UPDATION

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NATURE_Of_WORK);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_NATURE_OF_WORK);//TABLE_NATURE_Of_WORK

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Claim_Detail_Approver);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_CLAIM_DETAIL_Appr);//TABLE_Claim_Detail_Approver

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_OUTAGE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_REASON_OUTAGE);//TABLE_REASON_OUTAGE

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AFFECTED_CUSTOMER_INFO);
        db.execSQL(Factory.vwbFactory.CREATE_AFFECTED_CUSTOMER_INFO);//TABLE_AFFECTED_CUSTOMER_INFO

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetRouteFrom);
        db.execSQL(Factory.vwbFactory.CREATE_GETROUTEFROM_INFO);//TABLE_GetRouteFrom

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetRouteTo);
        db.execSQL(Factory.vwbFactory.CREATE_GETROUTETO_INFO);//TABLE_GetRouteTo

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GetBindCategory);
        db.execSQL(Factory.vwbFactory.CREATE_GETBINDCATEGORY_INFO);//TABLE_GetBindCategory

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ISBILLABLE_AMOUNT);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_IS_BILLABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATE_WAREHOUSE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_WAREHOUSE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATE_LOCATION);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_LOCATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATE_CODE);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_CODE);//

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATE_MATERIAL_Add);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_TICKET_UPDATE_MATERIAL_Add);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_UPDATION_DATA_Vw);
        db.execSQL(Factory.vwbFactory.CREATE_TABLE_UPDATION_DATA_Vw);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLAIM_NOTIFICATION);
        db.execSQL(Factory.vwbFactory.CREATE_CLAIM_NOTIFICATION_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ActivityGetGroupList);
        db.execSQL(Factory.vwbFactory.CREATE_ACTIITY_GROUPLIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPPORTTICKET_COUNT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SUPPORTTICKET_COUNT);

        //------------------------------CRM --------------------------

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROSPECT_VALIDATIONS);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROSPECT_VALIDATIONS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROSPECT_SERIAL);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROSPECT_SERIAL);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_COUNTRY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_FAMILY_PRODUCT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SALES_FAMILY_PRODUCT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENQUIRY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_ENQUIRY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROMOTER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOTER_REPORT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROMOTER_REPORT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_CUSTOMER_LIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_CUSTOMER_LIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_COUNT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_COUNT_MONTHWISE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT_MONTHWISE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKET_COUNT_DETAIL);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TICKET_COUNT_DETAIL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AddBy);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_ADDBY_JSON);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Teritory);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Teritory);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Territory);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TERRITORY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALLHISTORY);
        db.execSQL(Factory.crmFactory.CREATE_CALLHISTORY_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Reason);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REASON_Master);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_REASON_Master);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALLLISTDATA);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Calllistdata);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_TEAM);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_TEAM_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NatureofCall);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_NatureofCall);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPROVER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_APPROVER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_InitiatedBy);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_InitiatedBy);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_With_whom);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_With_whom);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CurrencyMaster);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CurrencyMaster);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderTypeMaster);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderTypeMaster);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderType);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderType);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderTypeMaster_All);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OrderTypeMaster_All);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Executive);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Executive);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Category);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Category);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Followup_reason);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Followup_reason);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Outcome);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Outcome);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TMESEName);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_TMESEName);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_PROSPECT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_PROSPECT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_MASTER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_MASTER);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOW_CONTACT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SHOW_CONTACT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_PARTIAL_OPP);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CRM_CALL_PARTIAL_OPP);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_OPP);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CRM_CALL_OPP);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User_Type);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_User_Type);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALL_RATING);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CALL_RATING);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_APPOINTMENT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPPORTUNITIES);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_OPPORTUNITIES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_filterdata_prospect);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_filterdata_prospect);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_OPPOTUNITY_CALL_FILTER);
        db.execSQL(Factory.crmFactory.CREATE_CRM_OPPOTUNITY_CALL_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_PARTIAL_TEAM);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_PARTIAL_TEAM_TABLE);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM);
        db.execSQL(Factory.crmFactory.CREATE_CRM_COLLECTIONCALL_PARTIAL_TEAM_TABLE);



        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_PARTIAL);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_PARTIAL_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Feedback);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FEEDBACK);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_COLLECTION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Reference);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Reference);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_StateList);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACT_DETAILS);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CONTACT_FETCH);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_DATA_FETCH);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PRODUCTDATA_FETCH);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FILLCONTROL_DATA_FETCH);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FILLCONTROL_FETCH);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_BOE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FollowUpTime);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_FollowUpTime);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Campaign);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Campaign);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Prospectsource);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_SuspectSource);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Product);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Product_Details);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product_Details);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Business_segment);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Business_segment);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SubTeam_Member);
        db.execSQL(Factory.crmFactory.CREATE_SubTeam_Member);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Team_Member);
        db.execSQL(Factory.crmFactory.CREATE_Team_Member);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_FEEDBACK);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_FEEDBACK);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_OPPORTUNITY);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_OPPORTUNITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY);
        db.execSQL(Factory.crmFactory.CREATE_CRM_CALL_COllECTION_OPPORTUNITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPPORTUNITY_UPDATE);
        db.execSQL(Factory.crmFactory.CREATE_SQLITE_OPPORTUNITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CUSTOMER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROVISINALLIST);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PROVISINALLIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANKNAME);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_BANKNAME);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Calender);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CALENDER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Travelplan);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Travelplan);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Firm);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Firm);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Setup_Prospect);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Setup_Prospect);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Entity);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Entity);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Consignee);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Consignee);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Referencetype);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Referencetype);


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Product_Details_New);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Product_Details_New);



        //------------------------------PM --------------------------

        // Inventory
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EntityType);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_EntityType);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Country_INVEN);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_COUNTRYLIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_City_INV);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_CITY_INVEN);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Currency);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_Currency);

//------------------------------Delivery Module --------------------------
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_BOY);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_DeliveryBoy);

        //------------------------------Sales / Counter billing --------------------------//
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALL_CAT_SUBCAT_ITEMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MERCHANT_AGAINST_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MERCHANT_AGAINST_ITEM);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MERCHANTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MERCHANTS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE_ORDER);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PLACE_ORDER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM_VOLUME_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_ADDRESS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ADDRESS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_ORDER_HISTORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ORDER_HISTORY);

        //Counter billing
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLUETOOTH_ADDRESS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BLUETOOTH_ADDRESS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_CUSTOMER_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PRODUCT_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARCHANT_ITEM_RUNI);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MARCHANT_ITEM_RUNI);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING_BALANCE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PENDING_BALANCE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GET_REPORTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_GET_REPORTS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_MRP);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ITEM_MRP);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BILL_DETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILL_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BILL_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GET_MARCHANT_PO);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_GET_MARCHANT_PO);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_MRP_Runi);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ITEM_MRP_Runi);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARCHANT_ITEM);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MARCHANT_ITEM);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRADE_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TRADE_DISCOUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOLUME_DISCOUNT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_VOLUME_DISCOUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PURCHASE_ITEM_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PURCHASE_FINAL_ITEM_CB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PURCHASE_FINAL_ITEM_CB);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_ITMDTLS_FORBILL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ADD_ITMDTLS_FORBILL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROSPECT_VALIDATIONS_SALES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PROSPECT_VALIDATIONS_SALES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_AGENTS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DELIVERY_AGENTS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPMENT_INVOICE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SHIPMENT_INVOICE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONSIGNEES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CONSIGNEES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATES_SALES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_STATES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_SALES);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CITIES);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISTRICT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_DISTRICT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATE_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_STATE_ENTITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CITY_ENTITY);

        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_TERRITORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TERRITORY);*/

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TALUKA_ENTITY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TALUKA_ENTITY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_TYPE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TYPE);

       /* db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_CATEGORY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_CATEGORY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_SYSTEM_USERDATA);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_SYSTEM_USERDATA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_PAYMENT_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_PAYMENT_TERMS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_DELIVERY_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_DELIVERY_TERMS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_TAXCODE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TAXCODE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_GROUP);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_GROUP);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_TYPE_OF_SERVICE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_TYPE_OF_SERVICE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_BI_DATA);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_BI_DATA);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTITY_CLASS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ENTITY_CLASS);*/

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_PRICELIST);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SALES_PRICELIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHIPTO_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SHIPTO_DETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADD_ITEMS_COUNTERBILL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ADD_ITEMS_COUNTERBILL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAXCLASS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_TAXCLASS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY_DETAILS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_COMPANY_DETAILS);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONFIGURATION);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CONFIGURATION);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CHARGE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CHARGE);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PAYMENT_TERMS);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PAYMENT_TERMS);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_COMMISSION);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_COMMISSION);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CATEGORY_REIMB);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CATEGORY_REIMB);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_WARRANTY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_WARRANTY);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_UOM_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_UOM_new);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SOHEADER);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SOHEADER);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SODETAIL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SODETAIL);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SOHistory);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SOHistory);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MODEL);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MODEL);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SALES_FAMILY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_SALESFAMILY);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PRICELIST);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PRICELIST);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_MY_ORDER_ACCEPTANCE);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_MY_ORDER_ACCEPTANCE);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_BUS_SEGMENT);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_BUS_SEGMENT);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_FAMILY_MASTERDATA);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_FAMILY_MASTERDATA);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_ALL_CAT_SUBCAT_ITEMS_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS_new);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CART_ITEM_new);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_CART_ITEM_new);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PENDING_DELIVERY);
        db.execSQL(Factory.TbudsFactory.CREATE_TABLE_PENDING_DELIVERY);

        //--------------------------------------------------------------------------------//

        //------------------------------Milk Run Module --------------------------
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DELIVERY_MILK_RUN);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_MilkRun);


        //Expense Management
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_EXPENSE);

        //HYVA project
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRINT);
        db.execSQL(Factory.crmFactory.CREATE_TABLE_PRINT_LABEL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PI_GENERATION);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_PI_GENERATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_PI);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_LOCATION_PI);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UOM);
        db.execSQL(Factory.PIGenerationFactory.CREATE_TABLE_UOM);



        /*-------------------------------------------------------------------------------------*/

        //Alfa Laval


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUTAWAY_USER);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_PUTAWAY_USER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUTAWAY);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GETPUTAWAY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION_MASTER);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GetLoactionMaster);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUTAWAY_PACKET_DETAIL);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GetTablePutawayPacketDetail);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MRS);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_MRS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MRS_DETAIL);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_MRS_DETAIL);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOX);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_BOX);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECONDARY_BOX);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_SECONDARY_BOX);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTAN_PICKLIST);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_CARTAN_PICKLIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_PICKLIST);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_ITEM_PICKLIST);




        /***************Inventory***********************/

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PlantList);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_PlantList);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MaterialItemList);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_MATERIALITEMLIST);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Suppliername);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_Suppliername);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WAREHOUSE);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_WAREHOUSE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Department);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_DEPARTMENT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AddMaterialPoOrder);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_ADDMATERIALPOORDER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORK_ORDER);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_WORK_ORDER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PODETAILS);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_PODETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_LOCATION);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PoitemDetails);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_POITEMDETAILS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MRSList);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_MRSList);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GETALLUsers);
        db.execSQL(Factory.InventoryFactory.CREATE_TABLE_GETALLUsers);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRN_PACKET);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRNPACKET);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRNNO_PACKET);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRNNOPACKET);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRN_POST_ITEM);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRN_POST_ITEM);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRN_POST);
        db.execSQL(Factory.AlfaLavalFactory.CREATE_TABLE_GRN_POST);






    }

    /*************************************Inventory****************************************************/

    public int getPlantListcount() {
        String countQuery = "SELECT  * FROM "
                + TABLE_PlantList;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getSuppliernamecount() {
        String countQuery = "SELECT  * FROM "
                + TABLE_Suppliername;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getWarehousecount() {
        String countQuery = "SELECT  * FROM "
                + TABLE_WAREHOUSE;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public int getDepartmentcount() {
        String countQuery = "SELECT  * FROM "
                + TABLE_Department;
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

    public void deleteallMaterialitem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_AddMaterialPoOrder);
    }


    public void addCallLogsDetails(String userMasterId, String userName, String number, String start2, String end2,
                                   String duration, String outgoing, int count, String contactpersonname) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("UserMasterId", userMasterId);
        cv.put("UserMasterName", userName);
        cv.put("MobileNo", number);
        cv.put("StartTime", start2);
        cv.put("EndTime", end2);
        cv.put("Duration", duration);
        cv.put("MobileCallType", outgoing);
        cv.put("RowNo", count);
        cv.put("ContactPersonName", contactpersonname);
        long a = db.insert(TABLE_CALL_LOG, null, cv);
        Log.i("cnt", String.valueOf(a));
       /*  Intent i = new Intent(this, CRM_CallLogList.class);
        i.putExtra("MobileNumber",number);
        startActivity(i);*/


    }
}


