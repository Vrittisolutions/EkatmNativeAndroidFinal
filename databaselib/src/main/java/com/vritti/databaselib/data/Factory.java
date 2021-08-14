package com.vritti.databaselib.data;

import com.vritti.databaselib.data.DatabaseHandlers;


public class Factory {

    public static final String CREATE_TABLE_TABLE_PIGetItemList = "CREATE TABLE " + DatabaseHandlers.TABLE_GetItemListPI +
            "(ItemCode TEXT,ItemDesc TEXT,ItemMasterId TEXT,ItemPlantId TEXT,PurUnit TEXT,SalesUnit" +
            " TEXT,ConvFactor TEXT,StockUnit TEXT,WareHouseMasterId TEXT,LocationMasterId TEXT,WarehouseCode TEXT,LocationCode TEXT)";

    public static final String CREATE_TABLE_TABLE_GetItemList = "CREATE TABLE " + DatabaseHandlers.TABLE_GetItemList +
            "(ItemCode TEXT,ItemDesc TEXT,ItemMasterId TEXT,ItemPlantId TEXT,PurUnit TEXT,SalesUnit TEXT,ConvFactor TEXT,LocationCode TEXT)";

    public static final String CREATE_TABLE_LOGIN_SETTING = "CREATE TABLE " + DatabaseHandlers.TABLE_LOGIN_SETTING +
            "(LogInKey TEXT,CompanyURL TEXT,EnvId TEXT,PlantID TEXT," +
            "PlantName TEXT,UserLogInId TEXT,UserMasterId TEXT,UserName TEXT," +
            "Password TEXT,Mobile TEXT,DatabaseName TEXT," +
            "IsCRMuser TEXT,IsChatApplicable TEXT,IsGpsLocation TEXT," +
            "BackDateTimesheet TEXT,AndroidId TEXT,IMEINumber TEXT,FCMToken TEXT,Designation Text,Material TEXT)";


    public static final String CREATE_TABLE_ENVMASTER = "CREATE TABLE " + DatabaseHandlers.TABLE_ENVMASTER
            + "(AppEnvMasterId TEXT)";


    public static final String CREATE_TABLE_PLANTMASTER = "CREATE TABLE " + DatabaseHandlers.TABLE_PLANTMASTER
            + "(PlantMasterId TEXT,PlantName TEXT)";

    public static final String CREATE_BIRTHDAY_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_BIRTHDAY + "(UserMasterID TEXT,UserLoginId TEXT,Title TEXT,UserName TEXT,DOB TEXT,Email TEXT,Mobile TEXT,DOJ TEXT,DtDay TEXT,ImagePath TEXT)";

    public static final String CREATE_CLIENTNAMEID_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CLIENTS +
            "(ClientName TEXT,ClientId TEXT)";


    public static final String CREATE_ClIENTCONTACTDETAILS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CLIENTCONTACT_DETAILS +
            "(Contactpersonname TEXT,EntityContactInfoId TEXT,ClientId TEXT)";

    // public  static final String CREATE_ClIENTCONTACTDETAILS_TABLE= "CREATE TABLE "+ DatabaseHandlers.TABLE_CLIENTCONTACT_DETAILS + "(Contactpersonname TEXT,EntityContactInfoId TEXT,ClientId TEXT)";

    // public  static final String CREATE_ClIENTCONTACTDETAILS_TABLE= "CREATE TABLE "+ DatabaseHandlers.TABLE_CLIENTCONTACT_DETAILS + "(Contactpersonname TEXT,EntityContactInfoId TEXT,ClientId TEXT)";



  /*  public static final String CREATE_MYTEAM_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_MYTEAM + " (userMasterId TEXT,UserLoginId TEXT,UserName TEXT," +
            "ClosedCalls TEXT,TotalCalls TEXT, Percent TEXT,Assigned TEXT,Overdue TEXT)";*/

    public static final String CREATE_MYTEAM_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_MYTEAM + " (UserName TEXT,UserMasterId TEXT,TotalAssigned TEXT," +
            "TotalOverdueActivities TEXT,Latitude TEXT,Longitude TEXT,LocationName TEXT,ImagePath TEXT," +
            "Gender TEXT,DayDiff TEXT)";

    public static final String CREATE_ALL_MEMBERS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ALL_MEMBERS + " (UserName TEXT,UserMasterId TEXT)";

    public static final String CREATE_TABLE_GPS_SEND_NOTIFICATION = "CREATE TABLE " + DatabaseHandlers.TABLE_GPS_SEND_NOTIFICATION + "(GId INTEGER PRIMARY KEY AUTOINCREMENT," +
            "        ToUsermatserID TEXT," +
            "        FromUserMasterID TEXT," +
            "        Date TEXT," +
            "        MSG TEXT,isUploaded TEXT)";

    public static final String CREATE_TABLE_LEAVE_REPORTING_TO = "CREATE TABLE " + DatabaseHandlers.TABLE_LEAVE_REPORTING_TO + " (UserLoginId TEXT,UserName TEXT,UserMasterId TEXT)";

    public static final String CREATE_TABLE_DATA_OFFLINE = "CREATE TABLE " + DatabaseHandlers.TABLE_DATA_OFFLINE +
            "(recordID INTEGER PRIMARY KEY AUTOINCREMENT,linkurl TEXT,parameter TEXT,methodtype TEXT," +
            "remark TEXT,output TEXT,AddedDt TEXT,isUploaded TEXT,AttemptCount INTEGER,AttachmentPath TEXT,AttachmentFileName TEXT)";

    public static final String CREATE_TABLE_CALL_LOG = "CREATE TABLE " + DatabaseHandlers.TABLE_CALL_LOG +
            "(UserMasterId TEXT,UserMasterName TEXT,MobileNo TEXT,StartTime TEXT,EndTime TEXT,Duration TEXT,MobileCallType TEXT," +
            "ContactPersonName TEXT,RowNo TEXT,CustomerName TEXT)";


    public static final String CREATE_MODE_OF_JOURNY_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MODE_OF_JOURNY + "(Type1 TEXT,Cd TEXT,Desc_r TEXT,LongDesc TEXT)";

    public static final String CREATE_TABLE_CRM_CALL = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL + "(SrNo TEXT," +
            "CallId TEXT," +
            "CallStart TEXT," +
            "ProductId TEXT," +
            "Product TEXT," +
            "CallStatus TEXT," +
            "Outcome TEXT," +
            "NextActionDateTime TEXT," +
            "LatestRemark TEXT," +
            "CurrentCallOwner TEXT," +
            "UserName TEXT," +
            "DemoRequired TEXT," +
            "DemoStatus TEXT," +
            "PrebidRequired TEXT," +
            "PrebidStatus TEXT," +
            "SpecialPrizeRequest TEXT," +
            "SpecialPrizeStatus TEXT," +
            "ContractReviewRequired TEXT," +
            "ContractStatus TEXT," +
            "UserLoginId TEXT," +
            "Followups TEXT," +
            "EkatmUserMasterId TEXT," +
            "NextAction TEXT," +
            "ProspectId TEXT," +
            "Compaign TEXT," +
            "ContactName TEXT," +
            "Designation TEXT," +
            "Telephone TEXT," +
            "Mobile TEXT," +
            "Email TEXT," +
            "IsActive TEXT," +
            "IsPrimaryContact TEXT," +
            "CRMNoofDays TEXT," +
            "Isclose TEXT," +
            "SpecialPrizeApprover TEXT," +
            "PresalesExecutiveId TEXT," +
            "PresalesSupportRequired TEXT," +
            "PresalesSupportDetails TEXT," +
            "PresalesSupportDueDate TEXT," +
            "DemoDatetime TEXT," +
            "QuotationNo TEXT," +
            "QuotationDate TEXT," +
            "QuotationValue TEXT," +
            "OrderStatus TEXT," +
            "OrderReferenceId TEXT," +
            "OrderReceivedDate TEXT," +
            "OrderValue TEXT," +
            "OrderLostReasonCode TEXT," +
            "OrderLostDetails TEXT," +
            "TeleCallCount TEXT," +
            "MeetingCount TEXT," +
            "DemoCount TEXT," +
            "QuotationCount TEXT," +
            "DemoAssignedTo TEXT," +
            "ContractResponsibility TEXT," +
            "ExpectedPrize TEXT," +
            "OrderRecievedBy TEXT," +
            "OrderPONo TEXT," +
            "OrderPOValue TEXT," +
            "DemoReasonCode TEXT," +
            "OutcomeCode TEXT," +
            "EmailCount TEXT," +
            "VisitCount TEXT," +
            "TotalHoursSpent TEXT," +
            "PrebidAssignedTo TEXT," +
            "CallCloseReason TEXT," +
            "CallCloseDetails TEXT," +
            "OrderRegradeReason TEXT," +
            "OrderRegradeReasonDetails TEXT," +
            "QuotationAssignedTo TEXT," +
            "QuotationRequest TEXT," +
            "QuotationStatus TEXT," +
            "CallRatingChangeReason TEXT," +
            "ExpectedValue TEXT," +
            "ExpectedCloserDate TEXT," +
            "OverdueDays TEXT," +
            "DueDays TEXT," +
            "PresalesSupportedBy TEXT," +
            "OrderLostFlag TEXT," +
            "OrderRegretFlag TEXT," +
            "CallCloseWihoutOrderFlag TEXT," +
            "CallLife TEXT," +
            "PresalesSupportStatus TEXT," +
            "OrderRegrteApprover TEXT," +
            "OrderLostApprover TEXT," +
            "CallCloseApprover TEXT," +
            "ContactId TEXT," +
            "SEId TEXT," +
            "BackOfficeExecutiveId TEXT," +
            "PKSuspectId TEXT," +
            "FirmName TEXT," +
            "FKCityId TEXT," +
            "IsProspect TEXT," +
            "IsLead TEXT," +
            "IsOrder TEXT," +
            "MoveToArchieve TEXT," +
            "MoveToArchieveBy TEXT," +
            "FKTerritoryId TEXT," +
            "FKEnqSourceId TEXT," +
            "CompanyURL TEXT," +
            "SourceName TEXT," +
            "CityName TEXT," +
            "TerritoryName TEXT," +
            "LeadGivenBYId TEXT," +
            "ContactPersonDept TEXT," +
            "CustVendorName TEXT," +
            "FirmAlias TEXT," +
            "OpenCalls TEXT," +
            "CloseCalls TEXT," +
            "OrderReceived TEXT," +
            "OrderLost TEXT," +
            "OrderRegrete TEXT," +
            "CallCloseWithoutOrder TEXT," +
            "Source TEXT," +
            "SouceId TEXT," +
            "Alias TEXT," +
            "CallType TEXT," +
            "RescheduleCount TEXT," +
            "CollectMode TEXT," +
            "InstrNo TEXT," +
            "Instrdate TEXT," +
            "BankName TEXT," +
            "Amount TEXT," +
            "AddedBy TEXT," +
            "AddedDt TEXT," +
            "ModifiedBy TEXT," +
            "ModifiedDt TEXT," +
            "AssignedBy TEXT," +
            "AssignTochannel TEXT," +
            "ReportingUserId TEXT," +
            "ReportingName TEXT," +
            "ReportingToEkatmUserId TEXT," +
            "ReportingToEmail TEXT," +
            "FKPlantId TEXT," +
            "Next_Action_Date TEXT," +
            "ExecutiveName TEXT" +
            ")";


    /*String CREATE_CRM_CALL_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL +
            "        (CallId  TEXT ," +
            "        Mobile  TEXT ," +
            "        CityName  TEXT ," +
            "        ContactName TEXT ," +
            "        CallStart  TEXT ," +
            "        ProductId  TEXT ," +
            "        CallStatus  TEXT ," +
            "        NextActionDateTime  TEXT ," +
            "        CurrentCallOwner  TEXT ," +
            "        UserName  TEXT ," +
            "        DemoRequired  TEXT ," +
            "        DemoStatus  TEXT ," +
            "        PrebidStatus  TEXT ," +
            "        ContractReviewRequired  TEXT ," +
            "        ContractStatus  TEXT ," +
            "        NextAction  TEXT ," +
            "        ProspectId  TEXT ," +
            "        Isclose  TEXT ," +
            "        SpecialPrizeApprover  TEXT ," +
            "        PresalesExecutiveId  TEXT ," +
            "        QuotationNo  TEXT ," +
            "        QuotationValue  TEXT ," +
            "        OrderReceivedDate  TEXT ," +
            "        OrderLostDetails  TEXT ," +
            "        DemoReasonCode  TEXT ," +
            "        OutcomeCode1  TEXT ," +
            "        EmailCount  TEXT ," +
            "        PrebidAssignedTo  TEXT ," +
            "        CallCloseReason  TEXT ," +
            "        CallCloseDetails  TEXT ," +
            "        QuotationAssignedTo  TEXT ," +
            "        ExpectedValue  TEXT ," +
            "        ExpectedCloserDate  TEXT ," +
            "        OrderLostFlag  TEXT ," +
            "        OrderRegretFlag  TEXT ," +
            "        CallCloseWihoutOrderFlag  TEXT ," +
            "        OrderRegrteApprover  TEXT ," +
            "        OrderLostApprover  TEXT ," +
            "        CallCloseApprover  TEXT ," +
            "        BackOfficeExecutiveId  TEXT ," +
            "        FirmName  TEXT ," +
            "        CompanyURL  TEXT ," +
            "        CustVendorName  TEXT ," +
            "        OrderReceived  TEXT ," +
            "        Source  TEXT ," +
            "        SouceId  TEXT ," +
            "        Alias  TEXT ," +
            "        CallType  TEXT ," +
            "        LatestRemark TEXT," +   //ChatCount,ChatRoomId
            "        Product TEXT," +
              *//*  "        ChatCount TEXT,"+
                "        ChatRoomId TEXT,"+*//*
            "        FKPlantId TEXT )";*/


    public static final String CREATE_TABLE_MEETING = "CREATE TABLE " + DatabaseHandlers.TABLE_MEETING +
            "(MOMId TEXT,MOMDate TEXT,MOMTitle TEXT,MeetTime TEXT,MeetVenue TEXT)";


    public static final String CREATE_TABLE_NOTIFICATION = "CREATE TABLE "
            + DatabaseHandlers.TABLE_NOTIFICATION
            + "(PKNotifDtlsId TEXT, " +
            "NotifText TEXT , NotifTitle TEXT, "
            + "NotificationTypeId TEXT ,TypeName TEXT ,UserName TEXT,AddedDt TEXT,Attachment TEXT,AttachGuid TEXT)";


    public static final String CREATE_GPSRECORDS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_GPSRECORDS
            + "(GPSID TEXT,MobileNo TEXT,latitude TEXT,longitude TEXT,"
            + "locationName TEXT,AddedDt TEXT,num TEXT)";

    public static final String CREATE_GPSRECORDS_ADD_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_ADD_GPSRECORDS
            + "(GPSID INTEGER PRIMARY KEY AUTOINCREMENT,MobileNo TEXT,latitude TEXT,longitude TEXT,"
            + "locationName TEXT,GpsAddedDt TEXT,UserMasterID TEXT,isUploaded TEXT)";


    public static final String CREATE_GPSRECORDS_GET_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_GET_GPSRECORDS
            + "( GPSID TEXT," +
            "        FKUserMasterId TEXT," +
            "        MobileNo TEXT," +
            "        latitude TEXT," +
            "        longitude TEXT," +
            "        locationName TEXT," +
            "        AddedDt TEXT)";

    public static final String CREATE_GPSREPORTINGTO_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_GPS_REPORTINGTO
            + "(UserMasterId TEXT,UserLoginId TEXT,UserName TEXT,Title TEXT,"
            + "UserPassword TEXT,HintQuestion TEXT,Answer TEXT,DeptMasterId TEXT,"
            + "Email TEXT,Mobile TEXT,ExtNo TEXT,LocationId TEXT,ReportingTo TEXT,"
            + "Phone TEXT,DOB TEXT,DOJ TEXT,IsActive TEXT,IsReportingUsingExcel TEXT,"
            + "DesignationId TEXT,EmpID TEXT,FirstName TEXT,LastName TEXT,"
            + "ISUProfileExpire TEXT,UProfileExpDate TEXT,PlantMasterId TEXT,"
            + "ActvDactvDt TEXT,CRMCode TEXT,CRMCategory TEXT,CRMNoofDays TEXT,"
            + "PswModifiedDt TEXT,LastLoginDt TEXT,IsDisabled TEXT,FailedLoginAttempt TEXT,"
            + "CreationLevel TEXT,UserLevel TEXT,IsDeleted TEXT,AddedBy TEXT,"
            + "AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT,IsCompleteRight TEXT,"
            + "AllowEditingDays TEXT,MiddleName TEXT,ChangePass1stLogin TEXT,"
            + "IsClientContact TEXT,Active TEXT,Gender TEXT,IsSysUser TEXT,"
            + "MobileActive TEXT,RegularWKOff TEXT,AlternetWkOff TEXT,"
            + "AlternetWkOffVal TEXT,EffectLeaveDate TEXT,IsBuyer TEXT,SeptDate TEXT,"
            + "InTime TEXT,OutTime TEXT,InOutFlg TEXT,IsAttendanceRecordMandatory TEXT,ConsiderWeeklyOff TEXT)";

    public static final String CREATE_FORM_DATA_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_FORM_DATA + " (PKCssHeaderId TEXT," +
            "   FKCustomerId TEXT," +
            "   FKCssFormsId TEXT," +
            "   FKFeedbackById TEXT," +
            "   FeedBackDate TEXT," +
            "   FeedBackStarttime TEXT," +
            "   FeedBackEndtime TEXT," +
            "   TimeSpent TEXT," +
            "   OverallRating TEXT," +
            "   AddedBy TEXT," +
            "   AddedDt TEXT," +
            "   ModifiedBy TEXT," +
            "   ModifiedDt TEXT," +
            "   IsDeleted TEXT," +
            "   FeedBackCallId TEXT," +
            "   Notes TEXT," +
            "   TotalRating TEXT," +
            "   Percentage TEXT," +
            "   Grade TEXT," +
            "   IsRequestToCustomer TEXT," +
            "   IsRequestSentTo TEXT," +
            "   MailFormatId TEXT," +
            "   IsResponseGiven TEXT," +
            "   LastReminderDate TEXT," +
            "   ReminderSentCount TEXT," +
            "   FKDatasheetSchId TEXT," +
            "   FreqDescriptor TEXT," +
            "   FKActivityId TEXT," +
            "   Status TEXT," +
            "   ScheduleDate TEXT," +
            "   AllowDatasheetEntryOn TEXT," +
            "   SaveActivityAction TEXT," +
            "   ItemLevelApproval TEXT," +
            "   AssignNew TEXT," +
            "   Attachment TEXT)";

   /* public static final String CREATE_DATASHEET_DATA_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_DATASHEET_DATA + "(" +
            "QuesText  TEXT, " +
            "         FKQuesId  TEXT, " +
            "         PKCssFormsQuesID  TEXT," +
            "         Weightage  TEXT," +
            "         IsResponseMandatory  TEXT," +
            "         AddedBy  TEXT, " +
            "         AddedDt  TEXT, " +
            "         ModifiedBy  TEXT," +
            "         ModifiedDt  TEXT, " +
            "         SequenceNo  TEXT," +
            "         IsDeleted  TEXT," +
            "         FkCssFormsId  TEXT, " +
            "         CSSFormsDesc  TEXT, " +
            "         SelectionText  TEXT," +
            "         SelectionValue  TEXT, " +
            "         ValueMin  TEXT," +
            "         ValueMax  TEXT," +
            "         Notes  TEXT, " +
            "         MaxValueText  TEXT, " +
            "         PKFormQuesDtls  TEXT, " +
            "         FKPrimaryQuesId  TEXT, " +
            "         FKSecondaryQuesId  TEXT," +
            "         IfResponseId1  TEXT," +
            "         IsBranching  TEXT," +
            "         ExpectedResponse  TEXT, " +
            "         IfResponseId  TEXT," +
            "         DisableQuesStr  TEXT, " +
            "         GroupID  TEXT," +
            "         GroupName  TEXT," +
            "         QuesCode  TEXT, " +
            "         SelectionType  TEXT," +
            "         ControlWidth  TEXT, " +
            "         MaxNoOfResponses  TEXT," +
            "         MaxExpectedResponse  TEXT, " +
            "         ResponseType  TEXT, " +
            "         ResponseValue  TEXT ," +
            "FormId TEXT," +
            "Answer TEXT," +
            "Answer_Value TEXT, detailid TEXT,flag TEXT)";*/


    public static final String CREATE_DATASHEET_DATA_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_DATASHEET_DATA + "( QuesText  TEXT, " +
            "         FKQuesId  TEXT, " +
            "         PKCssFormsQuesID  TEXT," +
            "         Weightage  TEXT," +
            "         IsResponseMandatory  TEXT," +
            "         AddedBy  TEXT, " +
            "         AddedDt  TEXT, " +
            "         ModifiedBy  TEXT," +
            "         ModifiedDt  TEXT, " +
            "         SequenceNo  TEXT," +
            "         IsDeleted  TEXT," +
            "         FkCssFormsId  TEXT, " +
            "         CSSFormsDesc  TEXT, " +
            "         SelectionText  TEXT," +
            "         SelectionValue  TEXT, " +
            "         ValueMin  TEXT," +
            "         ValueMax  TEXT," +
            "         Notes  TEXT, " +
            "         MaxValueText  TEXT, " +
            "         PKFormQuesDtls  TEXT, " +
            "         FKPrimaryQuesId  TEXT, " +
            "         FKSecondaryQuesId  TEXT," +
            "         IfResponseId1  TEXT," +
            "         IsBranching  TEXT," +
            "         ExpectedResponse  TEXT, " +
            "         IfResponseId  TEXT," +
            "         DisableQuesStr  TEXT, " +
            "         GroupID  TEXT," +
            "         GroupName  TEXT," +
            "         QuesCode  TEXT, " +
            "         SelectionType  TEXT," +
            "         ControlWidth  TEXT, " +
            "         MaxNoOfResponses  TEXT," +
            "         MaxExpectedResponse  TEXT, " +
            "         ResponseType  TEXT, " +
            "         ResponseValue  TEXT ,FormId TEXT,PKCssDtlsID TEXT, " +
            "IsSingleAttachment TEXT)";


    public static final String CREATE_DATASHEET_DATA_TABLE_NEW = "CREATE TABLE " + DatabaseHandlers.TABLE_DATASHEET_DATA_NEW +
            "( PKCssHeaderId  TEXT, " +
            "         FKCustomerId  TEXT, " +
            "         FKCssFormsId  TEXT," +
            "         FeedBackCallId  TEXT," +
            "         Responsebycustomer  TEXT," +
            "         PKCssDtlsID  TEXT, " +
            "         PKCSSFormsQuesId  TEXT, " +
            "         FKQuesId  TEXT," +
            "         Weightage  TEXT, " +
            "         IsResponseMandatory  TEXT," +
            "         SequenceNo  TEXT," +
            "         ExpectedResponse  TEXT, " +
            "         IsBranching  TEXT, " +
            "         IfResponseId  TEXT," +
            "         DisableQuesStr  TEXT, " +
            "         GroupId  TEXT," +
            "         GroupName  TEXT," +
            "         MaxNoOfResponses  TEXT, " +
            "         MaxExpectedResponse  TEXT, " +
            "         ResponseType  TEXT, " +
            "         QuesCode  TEXT, " +
            "         QuesText  TEXT," +
            "         QM_ResponseType  TEXT," +
            "         ValueMin  TEXT," +
            "         ValueMax  TEXT, " +
            "         SelectionText  TEXT," +
            "         SelectionValue  TEXT, " +
            "         Notes  TEXT," +
            "         MaxValueText  TEXT," +
            "         QM_SelectionType  TEXT, " +
            "         ControlWidth  TEXT," +
            "         IsExtMaster  TEXT, " +
            "         ExtMasterName  TEXT," +
            "         IssuedTo  TEXT, " +
            "         IsApprove  TEXT, " +
            "         CSSFormsDesc  TEXT ,AttachCount TEXT)";


    public static final String CREATE_TABLE_Datasheet_ANS = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_ANS
            + "( DId INTEGER PRIMARY KEY AUTOINCREMENT , PKCssFormsQuesID TEXT, FKQuesId TEXT , ResponseByCustomer TEXT, "
            + "SelectionValue TEXT ,Flag int ,FormId TEXT )";

    public static final String CREATE_TABLE_Datasheet_ANS_COMMON = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_ANS_COMMON
            + "( DId INTEGER PRIMARY KEY AUTOINCREMENT , PKCssFormsQuesID TEXT, FKQuesId TEXT , ResponseByCustomer TEXT, "
            + "SelectionValue TEXT ,Flag int ,FormId TEXT )";



 /*   public static final String CREATE_TABLE_Datasheet_ANS_COMMON = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_ANS_COMMON
            + "( DId INTEGER PRIMARY KEY AUTOINCREMENT , PKCssFormsQuesID TEXT, FKQuesId TEXT , ResponseByCustomer TEXT, "
            + "SelectionValue TEXT ,Flag int ,FormId TEXT )";*/


    public static final String CREATE_BRANCH_NAME_TABLE = "CREATE TABLE "
            + DatabaseHandlers.TABLE_BRANCH_NAME
            + "(PKBranchId TEXT,BranchName TEXT)";


    public static final String CREATE_FORM_NAME_SAHARA = "CREATE TABLE "
            + DatabaseHandlers.TABLE_FORM_NAME
            + "(PKCssFormsId TEXT,CSSFormsCode TEXT,CSSFormsDesc TEXT)";


    public static final String CREATE_TABLE_DATASHEET_SELECTION = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_SELECTION
            + "( sid INTEGER PRIMARY KEY AUTOINCREMENT , selectionid INTEGER," +
            " selectionText TEXT ,selectionValue TEXT,FormId TEXT,FKQuesId TEXT)";

    public static final String CREATE_TABLE_DATASHEET_SELECTION_COMMON = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_SELECTION_COMMON
            + "( sid INTEGER PRIMARY KEY AUTOINCREMENT , selectionid INTEGER," +
            " selectionText TEXT ,selectionValue TEXT,FormId TEXT,FKQuesId TEXT)";





  /*  public static final String CREATE_TABLE_DATASHEET_SELECTION_COMMON = "CREATE TABLE "
            + DatabaseHandlers.TABLE_DATASHEET_SELECTION_COMMON
            + "( sid INTEGER PRIMARY KEY AUTOINCREMENT , selectionid INTEGER," +
            " selectionText TEXT ,selectionValue TEXT,FormId TEXT,FKQuesId TEXT)";*/


    public static final String CREATE_TABLE_DATASHEET_EDIT = "CREATE TABLE " + DatabaseHandlers.TABLE_EDIT_DATASHEET + "(QuesText TEXT," +
            "        FKQuesId TEXT ," +
            "        PKCssFormsQuesID TEXT ," +
            "        Weightage TEXT," +
            "        IsResponseMandatory TEXT ," +
            "        AddedBy TEXT ," +
            "        AddedDt TEXT ," +
            "        ModifiedBy TEXT," +
            "        ModifiedDt TEXT," +
            "        SequenceNo TEXT," +
            "        IsDeleted TEXT ," +
            "        FkCssFormsId TEXT," +
            "        CSSFormsDesc TEXT," +
            "        SelectionText TEXT," +
            "        SelectionValue TEXT," +
            "        ValueMin TEXT ," +
            "        ValueMax TEXT ," +
            "        Notes TEXT ," +
            "        MaxValueText TEXT," +
            "        PKFormQuesDtls TEXT," +
            "        FKPrimaryQuesId TEXT," +
            "        FKSecondaryQuesId TEXT," +
            "        IfResponseId1 TEXT ," +
            "        IsBranching TEXT ," +
            "        ExpectedResponse TEXT," +
            "        IfResponseId TEXT ," +
            "        DisableQuesStr TEXT," +
            "        GroupID TEXT ," +
            "        GroupName TEXT," +
            "        QuesCode TEXT ," +
            "        SelectionType TEXT," +
            "        ControlWidth TEXT ," +
            "        MaxNoOfResponses TEXT," +
            "        MaxExpectedResponse TEXT," +
            "        ResponseType TEXT ," +
            "        ResponseValue TEXT," +
            "        PKCssHeaderId TEXT," +
            "        FKCustomerId TEXT ," +
            "        FKCssFormsId1 TEXT," +
            "        FKFeedbackById TEXT," +
            "        FeedBackDate TEXT ," +
            "        FeedBackStarttime TEXT," +
            "        FeedBackEndtime TEXT ," +
            "        TimeSpent TEXT ," +
            "        OverallRating TEXT," +
            "        AddedBy1 TEXT," +
            "        AddedDt1 TEXT," +
            "        ModifiedBy1 TEXT ," +
            "        ModifiedDt1 TEXT ," +
            "        IsDeleted1 TEXT ," +
            "        FeedBackCallId TEXT," +
            "        Notes1 TEXT ," +
            "        TotalRating TEXT ," +
            "        Percentage TEXT ," +
            "        Grade TEXT ," +
            "        IsRequestToCustomer TEXT," +
            "        IsRequestSentTo TEXT ," +
            "        MailFormatId TEXT ," +
            "        IsResponseGiven TEXT," +
            "        LastReminderDate TEXT," +
            "        ReminderSentCount TEXT," +
            "        FKDatasheetSchId TEXT ," +
            "        FreqDescriptor TEXT," +
            "        FKActivityId TEXT ," +
            "        Status TEXT ," +
            "        ScheduleDate TEXT ," +
            "        AllowDatasheetEntryOn TEXT," +
            "        SaveActivityAction TEXT ," +
            "        ItemLevelApproval TEXT ," +
            "        Attachment TEXT ," +
            "        PKCssDtlsID TEXT," +
            "        FKQuesId1 TEXT ," +
            "        FKCssHeaderId TEXT," +
            "        ResponseByCustomer TEXT," +
            "        SelectionValue1 TEXT ," +
            "        AddedBy2 TEXT," +
            "        AddedDt2 TEXT," +
            "        ModifiedBy2 TEXT," +
            "        ModifiedDt2 TEXT," +
            "        IsDeleted2 TEXT ," +
            "        Remarks TEXT ," +
            "        Note TEXT ," +
            "        FeedBackCallId1 TEXT ," +
            "        FKCssFormsId2 TEXT ," +
            "        Responsebycustomer1 TEXT," +
            "        PKCssDtlsID1 TEXT,SourceId TEXT,FormId TEXT,IsApproved TEXT,AttachmentCount TEXT,IsSingleAttachment TEXT)";
    //,IsSingleAttachment TEXT


    public static final String CREATE_TABLE_ATTACH_DETAILS_SAHARA = "CREATE TABLE " + DatabaseHandlers.TABLE_ATTACHMENT_DETAILS + "" +
            "(PkAttachId TEXT," +
            "AttachGuid TEXT," +
            "AttachFilename TEXT," +
            "Path TEXT," +
            "ActivityId TEXT," +
            "AddedBy TEXT," +
            "ModifiedBy TEXT," +
            "ModifiedDt TEXT," +
            "AddedDt TEXT," +
            "IsDeleted TEXT," +
            "Sourcetype TEXT," +
            "GPSId TEXT," +
            "AttachmentType TEXT," +
            "Latitude TEXT," +
            "Longitude TEXT," +
            "AttachmentCode TEXT," +
            "AttachmentDesc TEXT)";


    //-------------------------------------chat---------------------------------

    public static final String CREATE_TABLE_CHAT_CHATROOM_MEMBER_LIST = "CREATE TABLE " + DatabaseHandlers.TABLE_CHAT_CHATROOM_MEMBER_LIST + "(ChatRoomId TEXT," +
            "ChatRoomName TEXT,ChatRoomStatus TEXT,StartTime TEXT,Creator TEXT,ParticipantId TEXT,ChatSourceId TEXT,AddedBy TEXT,ParticipantName TEXT,Message TEXT,UserMasterId TEXT,Count TEXT,ChatType TEXT,ImagePath TEXT)";

    public static final String CREATE_TABLE_CHAT_ROOMNAME_DISPLAY_LIST = "CREATE TABLE " + DatabaseHandlers.TABLE_CHAT_ROOMNAME_DISPLAY_LIST + "(ChatRoomId TEXT," +
            "ChatRoomName TEXT,ChatRoomStatus TEXT,StartTime TEXT,AddedBy TEXT,UserMasterId TEXT)";

    public static final String CREATE_TABLE_CHAT_GROUP_MESSAGE = "CREATE TABLE " + DatabaseHandlers.TABLE_CHAT_GROUP_MESSAGE + "(ChatRoomId TEXT," +
            "UserMasterId TEXT,Message TEXT,MessageDate TEXT,UserName TEXT,Status TEXT,MessageType TEXT,MessageId TEXT PRIMARY KEY,ChatRoomName TEXT,Attachment TEXT,IsDownloaded TEXT)";

    public static final String CREATE_TABLE_CHAT_USER_LIST = "CREATE TABLE " + DatabaseHandlers.TABLE_CHAT_USER_LIST + "(UserMasterId TEXT PRIMARY KEY," +
            "UserName TEXT)";

    public static final String CREATE_TABLE_CHAT_CHATROOM_GROUP_LIST = "CREATE TABLE " + DatabaseHandlers.TABLE_CHAT_CHATROOM_GROUP_LIST + "(ChatRoomId TEXT," +
            "ChatRoomName TEXT,ChatRoomStatus TEXT,StartTime TEXT,Creator TEXT,AddedBy TEXT,UserMasterId TEXT,Count TEXT,ChatSourceId TEXT,ChatType TEXT,ChatMessage TEXT)";

    public static final String CREATE_TABLE_GROUP_JSON = "CREATE TABLE " + DatabaseHandlers.TABLE_GROUP_JSON + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,FinalJson TEXT)";

    class vwbFactory {

        public static final String CREATE_TABLE_SUPPLIER = "CREATE TABLE " + DatabaseHandlers.TABLE_SUPPLIER + "(CustVendorMasterId TEXT,CustVendorName TEXT)";
        public static final String CREATE_TABLE_SUPPLIER_PLANT = "CREATE TABLE " + DatabaseHandlers.TABLE_SUPPLIER_PLANT + "(PlantMasterId TEXT,PlantName TEXT)";
        public static final String CREATE_TABLE_PLANTMASTERVWB = "CREATE TABLE " + DatabaseHandlers.TABLE_PLANTMASTERvwb
                + "(PlantId TEXT)";

        public static final String CREATE_SETTING_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_SETTING + "(SettingName TEXT,SettingValue TEXT)";

        public static final String CREATE_PLANTMASTER_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_PLANTMASTER + "(PlantMasterId TEXT,PlantName TEXT)";
        public static final String CREATE_TABLE_NEXTAPPR = "CREATE TABLE " + DatabaseHandlers.TABLE_NEXTAPPR + "(UserName TEXT,UserMasterId TEXT," +
                "ApprLvl TEXT,Email TEXT)";


        public static final String CREATE_TABLE_NEXTAPPR_SAHARA = "CREATE TABLE " + DatabaseHandlers.TABLE_NEXTAPPR_SAHARA +
                "(ActivityId TEXT," +
                "ActivityName TEXT," +
                "ProjectName TEXT," +
                "IssuedTo TEXT,PKModuleMastId TEXT,UserMasterId TEXT," +
                "UserName TEXT,ProjectCode TEXT,ProjectStatus TEXT,Assigned_By TEXT," +
                "DeptMasterId TEXT,SourceType TEXT,SourceId TEXT,AddedBy TEXT," +
                "ProjectTypeId TEXT,UnitId TEXT,PriorityId TEXT,HoursRequired TEXT,ActivityTypeId TEXT,ProjectId TEXT,StartDt TEXT,EndDt TEXT,ActualStartDate TEXT," +
                "ActualEndDate TEXT,DueDate TEXT,ExpectedComplete_Date TEXT,ActivityCode TEXT,Reason TEXT)";

    /*    public static final String CREATE_TABLE_VIEW_REPORTS_ZP = "CREATE TABLE " + DatabaseHandlers.TABLE_ZPREPORTS +
                "(ActivityId TEXT," +
                "ActivityName TEXT," +
                "ProjectName TEXT," +
                "IssuedTo TEXT,PKModuleMastId TEXT,UserMasterId TEXT," +
                "UserName TEXT,ProjectCode TEXT,ProjectStatus TEXT,Assigned_By TEXT," +
                "DeptMasterId TEXT,SourceType TEXT,SourceId TEXT,AddedBy TEXT," +
                "ProjectTypeId TEXT,UnitId TEXT,PriorityId TEXT,HoursRequired TEXT,ActivityTypeId TEXT,ProjectId TEXT,StartDt TEXT,EndDt TEXT,ActualStartDate TEXT," +
                "ActualEndDate TEXT,DueDate TEXT,ExpectedComplete_Date TEXT,ActivityCode TEXT,Reason TEXT)";*/


        public static final String CREATE_ACTIVITYMASTER_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER +
                "(IsChargable TEXT," +
                "        ActivityName TEXT," +
                "        AssignedById TEXT," +
                "        Assigned_By TEXT," +
//                "        IssuedToName TEXT," +
                "        ActivityId TEXT PRIMARY KEY," +
                "        StartDate TEXT," +
                "        EndDate TEXT," +
                "        ExpectedCompleteDate TEXT," +
                "        ExpectedComplete_Date TEXT," +
                "        ModifiedBy TEXT," +
                "        Modified_By TEXT," +
                "        FormatStDt TEXT," +
                "        StartDt TEXT," +
                "        EndDt TEXT," +
                "        FormatEndDt TEXT," +
                "        Status TEXT," +
                "        ProjectId TEXT," +
                "        PAllowUsrTimeSlotHrs TEXT," +
                "        IsActivityMandatory TEXT," +
                "        IsDelayedActivityAllowed TEXT," +
                "        Cd TEXT," +
                "        UnitId TEXT," +
                "        PKModuleMastId TEXT," +
                "        PriorityName TEXT," +
                "        Colour TEXT," +
                "        PriorityIndex TEXT," +
                "        TotalHoursBooked TEXT," +
                "        AddedDt TEXT," +
                "        UserMasterId TEXT," +
                "        ModifiedDt TEXT," +
                //  "        AssignedById1 TEXT," +
                "        IsDeleted TEXT," +
                "        IsApproved TEXT," +
                //  "        IsChargable1 TEXT," +
                //   "        ActivityTypeId TEXT," +
                "        IsApproval TEXT," +
                "        HoursRequired TEXT," +
                "        AttachmentName TEXT," +
                // "        AttachmentContent TEXT," +
                //   "        ModifiedDt1 TEXT," +
                "        SourceType TEXT," +
                "        SourceId TEXT," +
                "        UnitName TEXT," +
                "        UnitDesc TEXT," +
                "        ModuleName TEXT," +
                //   "        ActivityName1 TEXT," +
                "        Remarks TEXT," +
                "        ProjectCode TEXT," +
                "        ProjectName TEXT," +
                "        UserName TEXT," +
                //   "        ExpectedComplete_Date1 TEXT," +
                "        DeptDesc TEXT," +
                "        DeptMasterId TEXT," +
                "        CompletionIntimate TEXT," +
                "        ActivityCode TEXT," +
                //   "        ModifiedBy1 TEXT," +
                "        ReassignedBy TEXT," +
                "        ReassignedDt TEXT," +
                "        ActualCompletionDate TEXT," +
                "        WarrantyCode TEXT," +
                "        TicketCategory TEXT," +
                "        IsEndTime TEXT," +
                "        IsCompActPresent TEXT," +
                "        CompletionActId TEXT," +
                "        TktCustReportedBy TEXT," +
                "        TktCustApprovedBy TEXT," +
                "        IsSubActivity TEXT," +
                "        ParentActId TEXT," +
                "        ConsigneeName TEXT," +
                "        ContMob TEXT," +
                "        ActivityTypeName TEXT," +
                "        CompActName TEXT)";

        public static final String CREATE_ASSIGNBYMEPAGING_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER_PAGING_ASSIGN +
                "(IsChargable TEXT," +
                "        ActivityName TEXT," +
                "        AssignedById TEXT," +
                "        Assigned_By TEXT," +
                "        IssuedToName TEXT," +
                "        ActivityId TEXT PRIMARY KEY," +
                "        StartDate TEXT," +
                "        EndDate TEXT," +
                "        ExpectedCompleteDate TEXT," +
                "        ExpectedComplete_Date TEXT," +
                "        ModifiedBy TEXT," +
                "        Modified_By TEXT," +
                "        FormatStDt TEXT," +
              /*  "        StartDt TEXT," +
                "        EndDt TEXT," +*/
                "        FormatEndDt TEXT," +
                "        Status TEXT," +
                "        ProjectId TEXT," +
                "        PAllowUsrTimeSlotHrs TEXT," +
              /*  "        IsActivityMandatory TEXT," +
                "        IsDelayedActivityAllowed TEXT," +
              */"        Cd TEXT," +
                "        UnitId TEXT," +
                "        PKModuleMastId TEXT," +
                "        PriorityName TEXT," +
                "        Colour TEXT," +
                "        PriorityIndex TEXT," +
                "        TotalHoursBooked TEXT," +
                "        AddedDt TEXT," +
                "        UserMasterId TEXT," +
                "        ModifiedDt TEXT," +
                "        AssignedById1 TEXT," +
                "        IsDeleted TEXT," +
                "        IsApproved TEXT," +
                /* "        IsChargable1 TEXT," +*/
                "        ActivityTypeId TEXT," +
                "        IsApproval TEXT," +
                "        HoursRequired TEXT," +
                "        AttachmentName TEXT," +
                /* "        AttachmentContent TEXT," +*/
                /*  "        ModifiedDt1 TEXT," +*/
                "        SourceType TEXT," +
                "        SourceId TEXT," +
                "        UnitName TEXT," +
                "        UnitDesc TEXT," +
                "        ModuleName TEXT," +
                //"        ActivityName1 TEXT," +
                "        Remarks TEXT," +
                "        ProjectCode TEXT," +
                "        ProjectName TEXT," +
                "        UserName TEXT," +
                //  "        ExpectedComplete_Date1 TEXT," +
                "        DeptDesc TEXT," +
                "        DeptMasterId TEXT," +
                "        CompletionIntimate TEXT," +
                "        ActivityCode TEXT," +
                "        ModifiedBy1 TEXT," +
                "        ReassignedBy TEXT," +
                "        ReassignedDt TEXT," +
                "        ActualCompletionDate TEXT," +
                "        WarrantyCode TEXT," +
                "        TicketCategory TEXT," +
                "        IsEndTime TEXT," +
                "        IsCompActPresent TEXT," +
                "        CompletionActId TEXT," +
                "        TktCustReportedBy TEXT," +
                "        TktCustApprovedBy TEXT," +
                "        IsSubActivity TEXT," +
                "        ParentActId TEXT," +
                "        ConsigneeName TEXT," +
                "        ContMob TEXT," +
                "        ActivityTypeName TEXT," +
                "        SubActCount TEXT)";
        // "        CompActName TEXT,ROWNo TEXT,EndDateAct TEXT,SubActCount TEXT)";


        public static final String CREATE_CLIENTPAGING_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER_PAGING_CLIENTPAGE +
                "(IsChargable TEXT," +
                "ActivityName TEXT," +
                "AssignedById TEXT," +
                "Assigned_By TEXT," +
                "IssuedToName TEXT," +
                "DatasheetStatus TEXT," +
                "UserMasterId TEXT," +
                "ReportingTo TEXT," +
                "SubActCount TEXT," +
                "SubActStaus TEXT," +
                "ActivityId TEXT," +
                "StartDate TEXT," +
                "EndDate TEXT," +
                "ExpectedCompleteDate TEXT," +
                "ModifiedBy TEXT," +
                "Modified_By TEXT," +
                "FormatStDt TEXT," +
                "FormatEndDt TEXT," +
                "Status TEXT," +
                "ProjectId TEXT," +
                "PAllowUsrTimeSlotHrs TEXT," +
                "IsDelayedActivityAllowed TEXT," +
                "Cd TEXT," +
                "UnitId TEXT," +
                "PKModuleMastId TEXT," +
                "PriorityName TEXT," +
                "Colour TEXT," +
                "PriorityIndex TEXT," +
                "TotalHoursBooked TEXT," +
                "AddedDt TEXT," +
                "ModifiedDt TEXT," +
                "AssignedById1 TEXT" +
                "IsDeleted TEXT," +
                "IsApproved TEXT," +
                "IsChargable1 TEXT," +
                "ActivityTypeId TEXT," +
                "IsApproval TEXT," +
                "HoursRequired TEXT," +
                "AttachmentName TEXT," +
                "AttachmentContent TEXT," +
                "ModifiedDt1 TEXT," +
                "SourceType TEXT," +
                "SourceId TEXT," +
                "UnitName TEXT," +
                "UnitDesc TEXT," +
                "ModuleName TEXT," +
                "ActivityName1 TEXT," +
                "Remarks TEXT," +
                "ProjectCode TEXT," +
                "ProjectName TEXT," +
                "UserName TEXT," +
                "ExpectedComplete_Date TEXT," +
                "DeptDesc TEXT," +
                "DeptMasterId TEXT," +
                "CompletionIntimate TEXT," +
                "ActivityCode TEXT ," +
                "ModifiedBy1 TEXT," +
                "ReassignedBy TEXT," +
                "ReassignedDt TEXT," +
                "ActualCompletionDate TEXT," +
                "WarrantyCode TEXT," +
                "TicketCategory TEXT," +
                "IsEndTime TEXT," +
                "IsCompActPresent TEXT," +
                "CompletionActId TEXT," +
                "TktCustReportedBy TEXT," +
                "TktCustApprovedBy TEXT," +
                "IsSubActivity TEXT," +
                "ParentActId TEXT," +
                "ConsigneeName TEXT," +
                "ContMob TEXT," +
                "ActivityTypeName TEXT," +
                "FYCode TEXT ," +
                "TotalCount TEXT)";


        public static final String CREATE_ACTIVITYMASTERPAGING_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER_PAGING +
                "(IsChargable TEXT," +
                "        ActivityName TEXT," +
                "        AssignedById TEXT," +
                "        Assigned_By TEXT," +
                /*"        IssuedToName TEXT," +*/
                "        ActivityId TEXT PRIMARY KEY," +
                "        StartDate TEXT," +
                "        EndDate TEXT," +
                "        ExpectedCompleteDate TEXT," +
                "        ExpectedComplete_Date TEXT," +
                "        ModifiedBy TEXT," +
                "        Modified_By TEXT," +
                "        FormatStDt TEXT," +
              /*  "        StartDt TEXT," +
                "        EndDt TEXT," +*/
                "        FormatEndDt TEXT," +
                "        Status TEXT," +
                "        ProjectId TEXT," +
                "        PAllowUsrTimeSlotHrs TEXT," +
              /*  "        IsActivityMandatory TEXT," +
                "        IsDelayedActivityAllowed TEXT," +
              */"        Cd TEXT," +
                "        UnitId TEXT," +
                "        PKModuleMastId TEXT," +
                "        PriorityName TEXT," +
                "        Colour TEXT," +
                "        PriorityIndex TEXT," +
                "        TotalHoursBooked TEXT," +
                "        AddedDt TEXT," +
                "        UserMasterId TEXT," +
                "        ModifiedDt TEXT," +
                "        AssignedById1 TEXT," +
                "        IsDeleted TEXT," +
                "        IsApproved TEXT," +
                /* "        IsChargable1 TEXT," +*/
                "        ActivityTypeId TEXT," +
                "        IsApproval TEXT," +
                "        HoursRequired TEXT," +
                "        AttachmentName TEXT," +
                /* "        AttachmentContent TEXT," +*/
                /*  "        ModifiedDt1 TEXT," +*/
                "        SourceType TEXT," +
                "        SourceId TEXT," +
                "        UnitName TEXT," +
                "        UnitDesc TEXT," +
                "        ModuleName TEXT," +
                //"        ActivityName1 TEXT," +
                "        Remarks TEXT," +
                "        ProjectCode TEXT," +
                "        ProjectName TEXT," +
                "        UserName TEXT," +
                //  "        ExpectedComplete_Date1 TEXT," +
                "        DeptDesc TEXT," +
                "        DeptMasterId TEXT," +
                "        CompletionIntimate TEXT," +
                "        ActivityCode TEXT," +
                "        ModifiedBy1 TEXT," +
                "        ReassignedBy TEXT," +
                "        ReassignedDt TEXT," +
                "        ActualCompletionDate TEXT," +
                "        WarrantyCode TEXT," +
                "        TicketCategory TEXT," +
                "        IsEndTime TEXT," +
                "        IsCompActPresent TEXT," +
                "        CompletionActId TEXT," +
                "        TktCustReportedBy TEXT," +
                "        TktCustApprovedBy TEXT," +
                "        IsSubActivity TEXT," +
                "        ParentActId TEXT," +
                "        ConsigneeName TEXT," +
                "        ContMob TEXT," +
                "        ActivityTypeName TEXT," +
                "        SubActCount TEXT)";
        // "        CompActName TEXT,ROWNo TEXT,EndDateAct TEXT,SubActCount TEXT)";



      /*  public static final String CREATE_ACTIVITYMASTERPAGING_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER_PAGING +
                "(IsChargable TEXT," +
                "        ActivityName TEXT," +
                "        AssignedById TEXT," +
                "        Assigned_By TEXT," +
                *//*"        IssuedToName TEXT," +*//*
                "        ActivityId TEXT PRIMARY KEY," +
                "        StartDate TEXT," +
                "        EndDate TEXT," +
                "        ExpectedCompleteDate TEXT," +
                "        ExpectedComplete_Date TEXT," +
                "        ModifiedBy TEXT," +
                "        Modified_By TEXT," +
                "        FormatStDt TEXT," +
                "        StartDt TEXT," +
                "        EndDt TEXT," +
                "        FormatEndDt TEXT," +
                "        Status TEXT," +
                "        ProjectId TEXT," +
                "        PAllowUsrTimeSlotHrs TEXT," +
                "        IsActivityMandatory TEXT," +
                "        IsDelayedActivityAllowed TEXT," +
                "        Cd TEXT," +
                "        UnitId TEXT," +
                "        PKModuleMastId TEXT," +
                "        PriorityName TEXT," +
                "        Colour TEXT," +
                "        PriorityIndex TEXT," +
                "        TotalHoursBooked TEXT," +
                "        AddedDt TEXT," +
                "        UserMasterId TEXT," +
                "        ModifiedDt TEXT," +
                "        AssignedById1 TEXT," +
                "        IsDeleted TEXT," +
                "        IsApproved TEXT," +
                *//* "        IsChargable1 TEXT," +*//*
                "        ActivityTypeId TEXT," +
                "        IsApproval TEXT," +
                "        HoursRequired TEXT," +
                "        AttachmentName TEXT," +
                *//* "        AttachmentContent TEXT," +*//*
         *//*  "        ModifiedDt1 TEXT," +*//*
                "        SourceType TEXT," +
                "        SourceId TEXT," +
                "        UnitName TEXT," +
                "        UnitDesc TEXT," +
                "        ModuleName TEXT," +
                "        ActivityName1 TEXT," +
                "        Remarks TEXT," +
                "        ProjectCode TEXT," +
                "        ProjectName TEXT," +
                "        UserName TEXT," +
                "        ExpectedComplete_Date1 TEXT," +
                "        DeptDesc TEXT," +
                "        DeptMasterId TEXT," +
                "        CompletionIntimate TEXT," +
                "        ActivityCode TEXT," +
                "        ModifiedBy1 TEXT," +
                "        ReassignedBy TEXT," +
                "        ReassignedDt TEXT," +
                "        ActualCompletionDate TEXT," +
                "        WarrantyCode TEXT," +
                "        TicketCategory TEXT," +
                "        IsEndTime TEXT," +
                "        IsCompActPresent TEXT," +
                "        CompletionActId TEXT," +
                "        TktCustReportedBy TEXT," +
                "        TktCustApprovedBy TEXT," +
                "        IsSubActivity TEXT," +
                "        ParentActId TEXT," +
                "        ConsigneeName TEXT," +
                "        ContMob TEXT," +
                "        ActivityTypeName TEXT," +
                "        CompActName TEXT,ROWNo TEXT,EndDateAct TEXT,SubActCount TEXT)";


*/


             /*   "ActivityName": "Permanent Document list-ZP sahara test-Kendra B  Haveli ",
                        "AssignedById": "aa369d65-9a30-4aef-b17b-19638fbf9884",
                        "Assigned_By": "Haveli BEO",
                        "ActivityId": "FBF0F77E-CFB3-49BB-88EB-754731183D32",
                        "StartDate": "/Date(1581618600000)/",
                        "EndDate": "/Date(1582050600000)/",
                        "EndDateTime": "/Date(1582050600000)/",
                        "ExpectedCompleteDate": "/Date(1582050600000)/",
                        "ExpectedComplete_Date": "/Date(1582050600000)/",
                        "ModifiedBy": "aa369d65-9a30-4aef-b17b-19638fbf9884",
                        "Modified_By": "Haveli BEO",
                        "FormatStDt": "14-Feb-20 ",
                        "FormatEndDt": "19-Feb-20 ",
                        "Status": "ASSIGNED",
                        "ProjectId": "4da672a5-fd46-4590-b321-850dd99f5141",
                        "PAllowUsrTimeSlotHrs": null,
                        "Cd": 13,
                        "UnitId": "e08c922a-086c-4562-951b-740aa31c6102",
                        "PKModuleMastId": "8f6c80db-90ab-4ea8-8cca-4884761b078e",
                        "PriorityName": "Critical",
                        "Colour": "#fcdada",
                        "PriorityIndex": 1,
                        "TotalHoursBooked": 0.00,
                        "AddedDt": "/Date(1581618600000)/",
                        "UserMasterId": "2a145e8e-9b4b-4dcc-938c-de9b35344e20",
                        "AssignedById1": "aa369d65-9a30-4aef-b17b-19638fbf9884",
                        "IsDeleted": "N",
                        "IsApproved": " ",
                        "IsChargable": false,
                        "ActivityTypeId": "",
                        "IsApproval": false,
                        "HoursRequired": "2",
                        "AttachmentName": null,
                        "ModifiedDt": "/Date(1581618600000)/",
                        "SourceType": "Datasheet",
                        "SourceId": "f80f9e97-b554-4c34-9be6-e5d519c09055",
                        "UnitName": "-",
                        "UnitDesc": "-",
                        "ModuleName": "-",
                        "Remarks": "",
                        "ProjectCode": "PRJAshtapur BEO",
                        "ProjectName": "Personal for Ashtapur BEO",
                        "UserName": "Kendra B  Haveli ",
                        "DeptDesc": "Block Education Officer",
                        "DeptMasterId": "FBA4A9BF-672E-4A41-A6FC-AAB68FD1558B",
                        "CompletionIntimate": null,
                        "ActivityCode": " ",
                        "ModifiedBy1": "aa369d65-9a30-4aef-b17b-19638fbf9884",
                        "ReassignedBy": null,
                        "ReassignedDt": null,
                        "ActualCompletionDate": null,
                        "WarrantyCode": " ",
                        "TicketCategory": " ",
                        "IsEndTime": "N",
                        "IsCompActPresent": null,
                        "SODetailId": "",
                        "CompletionActId": null,
                        "TktCustReportedBy": " ",
                        "TktCustApprovedBy": null,
                        "IsSubActivity": null,
                        "ParentActId": null,
                        "ConsigneeName": "",
                        "ContMob": "",
                        "ActivityTypeName": null,
                        "AllowDate": null,
                        "SubActCount": 0,
                        "SubActStaus": null,
                        "FYCode": "",
                        "TotalCount": 40*/


        public static final String CREATE_ACTIVITYMASTER_TEAM_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITYMASTER_TEAM + "(IsChargable TEXT," +
                "        ActivityName TEXT," +
                "        AssignedById TEXT," +
                "        Assigned_By TEXT," +
                "        IssuedToName TEXT," +
                "        SubActCount TEXT," +
                "        SubActStaus TEXT," +
                "        ActivityId TEXT," +
                "        StartDate TEXT," +
                "        EndDate TEXT," +
                "        ExpectedCompleteDate TEXT," +
                "        ExpectedComplete_Date TEXT," +
                "        ModifiedBy TEXT," +
                "        Modified_By TEXT," +
                "        FormatStDt TEXT," +
                // "        StartDt TEXT," +
                //"        EndDt TEXT," +
                "        FormatEndDt TEXT," +
                "        Status TEXT," +
                "        ProjectId TEXT," +
                "        PAllowUsrTimeSlotHrs TEXT," +
                //  "        IsActivityMandatory TEXT," +
                "        IsDelayedActivityAllowed TEXT," +
                "        Cd TEXT," +
                "        UnitId TEXT," +
                "        PKModuleMastId TEXT," +
                "        PriorityName TEXT," +
                "        Colour TEXT," +
                "        PriorityIndex TEXT," +
                "        TotalHoursBooked TEXT," +
                "        AddedDt TEXT," +
                "        UserMasterId TEXT," +
                "        ModifiedDt TEXT," +
                "        AssignedById1 TEXT," +
                "        IsDeleted TEXT," +
                "        IsApproved TEXT," +
                "        IsChargable1 TEXT," +
                "        ActivityTypeId TEXT," +
                "        IsApproval TEXT," +
                "        HoursRequired TEXT," +
                "        AttachmentName TEXT," +
                "        AttachmentContent TEXT," +
                "        ModifiedDt1 TEXT," +
                "        SourceType TEXT," +
                "        SourceId TEXT," +
                "        UnitName TEXT," +
                "        UnitDesc TEXT," +
                "        ModuleName TEXT," +
                "        ActivityName1 TEXT," +
                "        Remarks TEXT," +
                "        ProjectCode TEXT," +
                "        ProjectName TEXT," +
                "        UserName TEXT," +
                //   "        ExpectedComplete_Date1 TEXT," +
                "        DeptDesc TEXT," +
                "        DeptMasterId TEXT," +
                "        CompletionIntimate TEXT," +
                "        ActivityCode TEXT," +
                "        ModifiedBy1 TEXT," +
                "        ReassignedBy TEXT," +
                "        ReassignedDt TEXT," +
                "        ActualCompletionDate TEXT," +
                "        WarrantyCode TEXT," +
                "        TicketCategory TEXT," +
                "        IsEndTime TEXT," +
                "        IsCompActPresent TEXT," +
                "        CompletionActId TEXT," +
                "        TktCustReportedBy TEXT," +
                "        TktCustApprovedBy TEXT," +
                "        IsSubActivity TEXT," +
                "        ParentActId TEXT," +
                "        ConsigneeName TEXT," +
                "        ContMob TEXT," +
                "        ActivityTypeName TEXT)";
        //  "        CompActName TEXT)";

        public static final String CREATE_ACTIVITY_TRAIL_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ACTIVITY_TRAIL + "(ToUserName TEXT," +
                "            ByUserName TEXT," +
                "            AddedDt TEXT," +
                "            Desc_r TEXT," +
                "            ActivityTrailId TEXT," +
                "            ActivityId TEXT," +
                "            Assigned_Count TEXT," +
                "            ForUserMasterId TEXT," +
                "            Action TEXT," +
                "            Narration TEXT," +
                "            Status TEXT," +
                "            PrecedingActivityTrailId TEXT," +
                "            AddedDt1 TEXT," +
                "            AddedBy TEXT," +
                "            PreActivityStatus TEXT," +
                "            NxtActivityStatus TEXT," +
                "            SourceType TEXT," +
                "            UserMasterId TEXT," +
                "            UserLoginId TEXT," +
                "            UserName TEXT," +
                "            Title TEXT," +
                "            UserPassword TEXT," +
                "            HintQuestion TEXT," +
                "            Answer TEXT," +
                "            DeptMasterId TEXT," +
                "            Email TEXT," +
                "            Mobile TEXT," +
                "            ExtNo TEXT," +
                "            LocationId TEXT," +
                "            ReportingTo TEXT," +
                "            Phone TEXT," +
                "            DOB TEXT," +
                "            DOJ TEXT," +
                "            IsActive TEXT," +
                "            IsReportingUsingExcel TEXT," +
                "            DesignationId TEXT," +
                "            EmpID TEXT," +
                "            FirstName TEXT," +
                "            LastName TEXT," +
                "            ISUProfileExpire TEXT," +
                "            UProfileExpDate TEXT," +
                "            PlantMasterId TEXT," +
                "            ActvDactvDt TEXT," +
                "            CRMCode TEXT," +
                "            CRMCategory TEXT," +
                "            CRMNoofDays TEXT," +
                "            PswModifiedDt TEXT," +
                "            LastLoginDt TEXT," +
                "            IsDisabled TEXT," +
                "            FailedLoginAttempt TEXT," +
                "            CreationLevel TEXT," +
                "            UserLevel TEXT," +
                "            IsDeleted TEXT," +
                "            AddedBy1 TEXT," +
                "            AddedDt2 TEXT," +
                "            ModifiedBy TEXT," +
                "            ModifiedDt TEXT," +
                "            IsCompleteRight TEXT," +
                "            AllowEditingDays TEXT," +
                "            MiddleName TEXT," +
                "            ChangePass1stLogin TEXT," +
                "            IsClientContact TEXT," +
                "            Active TEXT," +
                "            Gender TEXT," +
                "            IsSysUser TEXT," +
                "            MobileActive TEXT," +
                "            RegularWKOff TEXT," +
                "            AlternetWkOff TEXT," +
                "            AlternetWkOffVal TEXT," +
                "            EffectLeaveDate TEXT," +
                "            IsBuyer TEXT," +
                "            SeptDate TEXT," +
                "            InTime TEXT," +
                "            OutTime TEXT," +
                "            InOutFlg TEXT," +
                "            FkShiftMasterId TEXT," +
                "            IsAttendanceRecordMandatory TEXT," +
                "            ConsiderWeeklyOff TEXT," +
                "            IsIntDeptEmp TEXT," +
                "            MembershipNo TEXT," +
                "            FKUserCategoryId TEXT," +
                "            PerState TEXT," +
                "            PerCountry TEXT," +
                "            PreCountry TEXT," +
                "            PreCode TEXT," +
                "            PerCode TEXT," +
                "            PerAddr TEXT," +
                "            PreAddr TEXT," +
                "            PerCity TEXT," +
                "            PreCity TEXT," +
                "            AllowUsrTimeSlotHrs TEXT," +
                "            SameAsPresent TEXT," +
                "            UseShift TEXT," +
                "            PreState TEXT," +
                "            IsHRExecutive TEXT," +
                "            EntityContactInfoId TEXT," +
                "            BQM_BanquetClientId TEXT," +
                "            ContPerName TEXT," +
                "            Designation TEXT," +
                "            EmailId TEXT," +
                "            ContactNo TEXT," +
                "            InfluentialLevel TEXT," +
                "            IsDefault TEXT," +
                "            IsDeleted1 TEXT," +
                "            ContactType TEXT," +
                "            IsVWBLoginAllowed TEXT," +
                "            IsTimeAllow TEXT," +
                "            LoginId TEXT," +
                "            Password TEXT," +
                "            IsApprovalAllowed TEXT," +
                "            EmailVerificationKey TEXT," +
                "            IsActAllocAllowed TEXT," +
                "            UserMasterId1 TEXT," +
                "            UserLoginId1 TEXT," +
                "            UserName1 TEXT," +
                "            Title1 TEXT," +
                "            UserPassword1 TEXT," +
                "            HintQuestion1 TEXT," +
                "            Answer1 TEXT," +
                "            DeptMasterId1 TEXT," +
                "            Email1 TEXT," +
                "            Mobile1 TEXT," +
                "            ExtNo1 TEXT," +
                "            LocationId1 TEXT," +
                "            ReportingTo1 TEXT," +
                "            Phone1 TEXT," +
                "            DOB1 TEXT," +
                "            DOJ1 TEXT," +
                "            IsActive1 TEXT," +
                "            IsReportingUsingExcel1 TEXT," +
                "            DesignationId1 TEXT," +
                "            EmpID1 TEXT," +
                "            FirstName1 TEXT," +
                "            LastName1 TEXT," +
                "            ISUProfileExpire1 TEXT," +
                "            UProfileExpDate1 TEXT," +
                "            PlantMasterId1 TEXT," +
                "            ActvDactvDt1 TEXT," +
                "            CRMCode1 TEXT," +
                "            CRMCategory1 TEXT," +
                "            CRMNoofDays1 TEXT," +
                "            PswModifiedDt1 TEXT," +
                "            LastLoginDt1 TEXT," +
                "            IsDisabled1 TEXT," +
                "            FailedLoginAttempt1 TEXT," +
                "            CreationLevel1 TEXT," +
                "            UserLevel1 TEXT," +
                "            IsDeleted2 TEXT," +
                "            AddedBy2 TEXT," +
                "            AddedDt3 TEXT," +
                "            ModifiedBy1 TEXT," +
                "            ModifiedDt1 TEXT," +
                "            IsCompleteRight1 TEXT," +
                "            AllowEditingDays1 TEXT," +
                "            MiddleName1 TEXT," +
                "            ChangePass1stLogin1 TEXT," +
                "            IsClientContact1 TEXT," +
                "            Active1 TEXT," +
                "            Gender1 TEXT," +
                "            IsSysUser1 TEXT," +
                "            MobileActive1 TEXT," +
                "            SeptDate1 TEXT," +
                "            Type1 TEXT," +
                "            Cd TEXT," +
                "            Desc_r1 TEXT," +
                "            LongDesc TEXT," +
                "            Fixed TEXT," +
                "            IsDeleted3 TEXT)";


        // public static final String CREATE_MYWORK_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYWORK + "(Today TEXT,Overdue TEXT,Critical TEXT,New TEXT,NotActed TEXT,Tickets TEXT,TotalCount TEXT,AssByCount TEXT,UnApproved TEXT)";
        public static final String CREATE_MYWORK_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYWORK + "(Today TEXT,Overdue TEXT,Critical TEXT,NotActed TEXT,Tickets TEXT,TotalCount TEXT,AssByCount TEXT,UnApproved TEXT)";
        public static final String CREATE_MYWORKSPACE_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYWORKSPACE + " (ProjectName TEXT,ProjectId TEXT,OnTime TEXT,OpenActivities TEXT,OnTimePerc TEXT)";
        public static final String CREATE_MYCLIENTS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYCLIENTS +
                " (ClientCount TEXT,ShipToMasterId TEXT,CustVendorMasterId TEXT,EmployeeId TEXT,ConsigneeName TEXT,OpenActivities TEXT,OverDue TEXT" +
                ",WaitingApproval TEXT,Critical TEXT)";


      /*  public static final String CREATE_MYTEAM_DEPT_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYTEAM_DEPT + " ( UserMasterId TEXT," +
                "        UserLoginId TEXT," +
                "        UserName TEXT," +
                "        Title TEXT," +
                "        UserPassword TEXT," +
                "        HintQuestion TEXT," +
                "        Answer TEXT," +
                "        DeptMasterId TEXT," +
                "        Email TEXT," +
                "        Mobile TEXT," +
                "        ExtNo TEXT," +
                "        LocationId TEXT," +
                "        ReportingTo TEXT," +
                "        Phone TEXT," +
                "        DOB TEXT," +
                "        DOJ TEXT," +
                "        IsActive TEXT," +
                "        IsReportingUsingExcel TEXT," +
                "        DesignationId TEXT," +
                "        EmpID TEXT," +
                "        FirstName TEXT," +
                "        LastName TEXT," +
                "        ISUProfileExpire TEXT," +
                "        UProfileExpDate TEXT," +
                "        PlantMasterId TEXT," +
                "        ActvDactvDt TEXT," +
                "        CRMCode TEXT," +
                "        CRMCategory TEXT," +
                "        CRMNoofDays TEXT," +
                "        PswModifiedDt TEXT," +
                "        LastLoginDt TEXT," +
                "        IsDisabled TEXT," +
                "        FailedLoginAttempt TEXT," +
                "        CreationLevel TEXT," +
                "        UserLevel TEXT," +
                "        IsDeleted TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifiedBy TEXT," +
                "        ModifiedDt TEXT," +
                "        IsCompleteRight TEXT," +
                "        AllowEditingDays TEXT," +
                "        MiddleName TEXT," +
                "        ChangePass1stLogin TEXT," +
                "        IsClientContact TEXT," +
                "        Active TEXT," +
                "        Gender TEXT," +
                "        IsSysUser TEXT," +
                "        MobileActive TEXT," +
                "        RegularWKOff TEXT," +
                "        AlternetWkOff TEXT," +
                "        AlternetWkOffVal TEXT," +
                "        EffectLeaveDate TEXT," +
                "        IsBuyer TEXT," +
                "        SeptDate TEXT," +
                "        InTime TEXT," +
                "        OutTime TEXT," +
                "        InOutFlg TEXT," +
                "        FkShiftMasterId TEXT," +
                "        IsAttendanceRecordMandatory TEXT," +
                "        ConsiderWeeklyOff TEXT," +
                "        IsIntDeptEmp TEXT," +
                "        MembershipNo TEXT," +
                "        FKUserCategoryId TEXT," +
                "        TotalAssigned TEXT," +
                "        MobileUser TEXT," +
                "        TotalOverdueActivities TEXT," +
                "        AwaitingActivities TEXT," +
                "        Critical TEXT," +
                "        Report TEXT)";*/

        public static final String CREATE_MYTEAM_DEPT_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYTEAM_DEPT +
                "(UserMasterId TEXT,UserName TEXT,TotalAssigned TEXT,TotalCompleted1" +
                " ,MobileUser TEXT,TotalOverdueActivities TEXT,AwaitingActivities TEXT," +
                "Critical TEXT,Report TEXT,Today TEXT,LocationName TEXT)";

        public static final String CREATE_MYSUBTEAM_DEPT_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MYSUBTEAM_DEPT +
                "(UserMasterId TEXT,UserName TEXT,TotalAssigned TEXT" +
                " ,MobileUser TEXT,TotalOverdueActivities TEXT,AwaitingActivities TEXT," +
                "Critical TEXT,Report TEXT,LocationName TEXT)";


        public static final String CREATE_POST_INSERT_TIMESHEET_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_POST_INSERT_TIMESHEET + " (ActivityId TEXT,workDesc TEXT,forDate TEXT,fromTime TEXT,toTime TEXT,isUpload TEXT)";
        public static final String CREATE_WORKSPACE_LIST_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_WORKSPACE_LIST + " (ProjectName TEXT,PrjRoleId TEXT,MembersCanCreate TEXT,PrjEmpId TEXT,EmployeeId TEXT,ProjectId TEXT,IsActive TEXT,IsDeleted TEXT)";
        public static final String CREATE_MAINGROUP_LIST_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_MAINGROUP_LIST + " (PKModuleMastId TEXT,ModuleName TEXT,ProjectId TEXT)";
        public static final String CREATE_SUBGROUP_LIST_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_SUBGROUP_LIST + " (UnitId TEXT,UnitDesc TEXT,PKModuleMastId TEXT)";


        public static final String CREATE_PROJECT_MEMBERS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_PROJECT_MEMBERS + " (UserName TEXT,UserMasterId TEXT,prjMstId TEXT)";

        public static final String CREATE_TASK_ACTIVITY_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_TASK_ACTIVITY + " (NewActFlag TEXT,ActivityName TEXT,AssignedById TEXT,Assigned_By TEXT,ActivityId TEXT,StartDate TEXT,EndDate TEXT,ExpectedCompleteDate TEXT,ExpectedComplete_Date TEXT,ModifiedBy TEXT,Modified_By TEXT,FormatStDt TEXT,StartDt TEXT,EndDt TEXT,FormatEndDt TEXT,Status TEXT,ProjectId TEXT,IsActivityMandatory TEXT,IsDelayedActivityAllowed TEXT,Cd TEXT,UnitId TEXT,PKModuleMastId TEXT,PriorityName TEXT,Colour TEXT,PriorityIndex TEXT,TotalHoursBooked TEXT,AddedDt TEXT,UserMasterId TEXT,AssignedById1 TEXT,IsDeleted TEXT,IsApproved TEXT,IsChargable TEXT,ActivityTypeId TEXT,IsApproval TEXT,HoursRequired TEXT,AttachmentName TEXT,ModifiedDt TEXT,SourceType TEXT,SourceId TEXT,UnitName TEXT,UnitDesc TEXT,ModuleName TEXT,ActivityName1 TEXT,Remarks TEXT,ProjectCode TEXT,ProjectName TEXT,UserName TEXT,ExpectedComplete_Date1 TEXT,DeptDesc TEXT,DeptMasterId TEXT,CompletionIntimate TEXT,ActivityCode TEXT,ModifiedBy1 TEXT,ReassignedBy TEXT,ReassignedDt TEXT,ActualCompletionDate TEXT,WarrantyCode TEXT,TicketCategory TEXT,IsEndTime TEXT,IsCompActPresent TEXT,CompletionActId TEXT,TktCustReportedBy TEXT,TktCustApprovedBy TEXT,IsSubActivity TEXT,ParentActId TEXT,ConsigneeName TEXT,ContMob TEXT,ActivityTypeName TEXT,CompActName TEXT)";


        public static final String CREATE_CLAIM_APPROVER_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CLAIM_APPROVER + "(UserMasterId TEXT,UserLoginId TEXT,UserName TEXT)";

        public static final String CREATE_EMPREQUEST_APPROVER_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_EMPLOYEE_REQUEST_APPROVER + "(UserMasterId TEXT,UserName TEXT)";

        public static final String CREATE_TABLE_CLAIM_SUMMARY = "CREATE TABLE " + DatabaseHandlers.TABLE_CLAIM_SUMMARY + "(ClaimHeaderId TEXT," +
                "Applicant TEXT," +
                "ClaimCode TEXT," +
                "Date TEXT," +
                "FormatedDate TEXT," +
                "ProjectName TEXT," +
                "plantid TEXT," +
                "Total TEXT," +
                "Status TEXT," +
                "EMPID TEXT," +
                "DeptDesc TEXT," +
                "PaidAmount TEXT," +
                "BalanceAmount TEXT)";

        public static final String CREATE_TABLE_COST_CENTER = "CREATE TABLE " + DatabaseHandlers.TABLE_COST_CENTER + "( RecId TEXT," +
                "        CostCtrMasterId TEXT," +
                "        CostCtrCode TEXT," +
                "        CostCtrDesc TEXT," +
                "        Proportionate TEXT," +
                "        IsActive TEXT," +
                "        IsDeleted TEXT," +
                "        SecId TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifiedBy TEXT," +
                "        ModifiedDt TEXT)";


        public static final String CREATE_LEAVE_SUMMARY_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_LEAVE_SUMMARY + " (LeaveCode TEXT, " +
                "        OpenBal TEXT," +
                "        Credit TEXT, " +
                "        Consumed TEXT, " +
                "        Balance TEXT, " +
                "        OpenBalPer TEXT," +
                "        CreditPer TEXT, " +
                "        ConsumedPer TEXT," +
                "        BalancePer TEXT)";

        public static final String CREATE_LEAVE_RECORDS_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_LEAVE_RECORDS + " (MLId TEXT," +
                "    StartDt TEXT," +
                "    EndDt TEXT," +
                "    StartDate TEXT," +
                "    EndDate TEXT," +
                "    Status TEXT," +
                "    Reason TEXT," +
                "    Address TEXT," +
                "    Contact TEXT," +
                "    LeaveType TEXT," +
                "    ApprovedDt TEXT," +
                "    LeaveCount TEXT," +
                "    HalfLeaveOption TEXT," +
                "    HalfLeaveOptionTo TEXT," +
                "    ApproverRemark TEXT)";


        public static final String CREATE_TABLE_APP_DOC_INFO = "CREATE TABLE " + DatabaseHandlers.TABLE_APP_DOC_INFO + "(  PlantMasterId TEXT ," +
                "        PlantName TEXT," +
                "        Address TEXT ," +
                "        City TEXT ," +
                "        State TEXT ," +
                "        Country TEXT," +
                "        Phone TEXT," +
                "        Email TEXT ," +
                "        URL TEXT," +
                "        ContactPerson TEXT," +
                "        ContactNo TEXT ," +
                "        CSTNo TEXT ," +
                "        BSTNo TEXT," +
                "        ExciseNo TEXT," +
                "        TIN TEXT," +
                "        CreationLevel TEXT," +
                "        UserLevel TEXT," +
                "        IsDeleted TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifiedBy TEXT," +
                "        ModifiedDt TEXT," +
                "        ApplicationThemeColor TEXT ," +
                "        LeftPanelBackColor TEXT ," +
                "        ExciseApplicable TEXT ," +
                "        PrefixText TEXT ," +
                "        StateMasterId TEXT ," +
                "        IsActive TEXT," +
                "        ClaimApproval TEXT," +
                "        LeaveApproval TEXT ," +
                "        LeaveCancellation TEXT," +
                "        ExtraWorkApproval TEXT ," +
                "        DocApprMthdId TEXT," +
                "        Code TEXT ," +
                "        Description TEXT," +
                "        IsActive1 TEXT ," +
                "        IsDeleted1 TEXT ," +
                "        SecId TEXT ," +
                "        AddedBy1 TEXT ," +
                "        AddedDt1 TEXT," +
                "        ModifiedBy1 TEXT," +
                "        ModifiedDt1 TEXT," +
                "        PlantId TEXT," +
                "        URL1 TEXT," +
                "        ValRngId TEXT," +
                "        DocApprMthdId1 TEXT," +
                "        ValueFrom TEXT," +
                "        ValueTo TEXT," +
                "        IsActive2 TEXT," +
                "        IsDeleted2 TEXT ," +
                "        SecId1 TEXT ," +
                "        AddedBy2 TEXT," +
                "        AddedDt2 TEXT," +
                "        ModifiedBy2 TEXT," +
                "        ModifiedDt2 TEXT ," +
                "        ApproverSource TEXT ," +
                "        DocApprMthdId2 TEXT," +
                "        URL2 TEXT ," +
                "        ApproverSource1 TEXT)";

        public static final String CREATE_TABLE_GetMainDtl_APPROVAL = "CREATE TABLE " + DatabaseHandlers.TABLE_GetMainDtl_APPROVAL + "(Description TEXT,URL TEXT,DocApprMthdId TEXT,DocValue TEXT,DocApprHdrId TEXT,DocApprDtlId TEXT,SourceType TEXT,SourceId TEXT,FilePath TEXT)";


        public static final String CREATE_TABLE_WORKSPACEWISE_ACT_CNT = "CREATE TABLE " + DatabaseHandlers.TABLE_WORKSPACEWISE_ACT_CNT + "(ProjectId TEXT,ProjectName TEXT,Complete TEXT,OpenAct TEXT,TotalAssigned TEXT,TotalOverdue TEXT,AwaitingActivities TEXT,CriticalActivity TEXT)";

        public static final String CREATE_TABLE_BINDFINALOUTCOME = "CREATE TABLE " + DatabaseHandlers.TABLE_BIND_FINAL_OUTCOME + "(PKOutcomeId TEXT , Outcome TEXT)";

        // public static final String CREATE_TABLE_BINDFINALOUTCOME = "CREATE TABLE " + DatabaseHandlers.TABLE_BIND_FINAL_OUTCOME + "(PKOutcom TEXT" +
        //   "eId TEXT,Outcome TEXT)";

        //   public static final String CREATE_TABLE_BINDFINALOUTCOME = "CREATE TABLE " + DatabaseHandlers.TABLE_BIND_FINAL_OUTCOME + "(PKOutcomeId TEXT,Outcome TEXT)";

        public static final String CREATE_TABLE_UPDATION_DATA = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATION_DATA + "(AllowEditingDays TEXT, " +
                "ActivityId TEXT," +
                "ActivityName TEXT," +
                "UserMasterId TEXT," +
                "UnitId TEXT," +
                "ActivityTypeId TEXT," +
                "StartDate TEXT," +
                "EndDate TEXT," +
                "DueDate TEXT," +
                "IssuedTo TEXT," +
                "Status TEXT," +
                "Remarks TEXT," +
                "IsDeleted TEXT," +
                "AddedBy TEXT," +
                "AddedDt TEXT," +
                "ModifiedBy TEXT," +
                "ModifiedDt TEXT," +
                "HoursRequired TEXT," +
                "ProposedUserId TEXT," +
                "PriorityId TEXT," +
                "ActualStartDate TEXT," +
                "ActualEndDate TEXT," +
                "IsApproval TEXT," +
                "IsApproved TEXT," +
                "ChargedAmount TEXT," +
                "ApprovedAmount TEXT," +
                "IsChargable TEXT, " +
                "Reason TEXT, " +
                "ApprovalDt TEXT, " +
                "PeriodicBillId TEXT, " +
                "CompletionIntimate TEXT, " +
                "Assigned_Count TEXT, " +
                "AttachmentName TEXT," +
                "SourceType TEXT," +
                "SourceId TEXT, " +
                "ExpectedComplete_Date TEXT, " +
                "ActivityCode TEXT, " +
                "ExecutionAmount TEXT, " +
                "ActualCompletionDate TEXT, " +
                "ReassignedBy TEXT," +
                "ReassignedDt TEXT, " +
                "PKSubActivityId TEXT, " +
                "ParentActId TEXT, " +
                "IsSubActivity TEXT, " +
                "TicketCategory TEXT, " +
                "WarrantyCode TEXT, " +
                "IsCompActPresent TEXT," +
                "CompletionActId TEXT, " +
                "TktCustReportedBy TEXT, " +
                "TktCustApprovedBy TEXT, " +
                "WarrantyId TEXT, " +
                "IsFromContract TEXT," +
                "ActivityRecId TEXT, " +
                "DateOfResumtion TEXT," +
                "StatusAliasId TEXT," +
                "AttachmentContent TEXT" +
                "IsUnplanned TEXT," +
                "ItemDesc TEXT, " +
                "ItemMasterId TEXT, " +
                "ConsigneeName TEXT," +
                "Address TEXT, " +
                "Mobile TEXT, " +
                "Email TEXT, " +
                "ReportedByEmail TEXT, " +
                "ContMob TEXT," +
                "ReportingToName TEXT," +
                "ReportingToEmail TEXT)";

        public static final String CREATE_TABLE_Setup_TICKET_UPDATION = "CREATE TABLE " + DatabaseHandlers.TABLE_Setup_TICKET_UPDATION +
                "(Key TEXT,value TEXT)";

        public static final String CREATE_TABLE_NATURE_OF_WORK = "CREATE TABLE " + DatabaseHandlers.TABLE_NATURE_Of_WORK + "(ActivityTypeId TEXT," +
                "    ActivityTypeName TEXT," +
                "    ActivityTypeRemarks TEXT," +
                "    SkillSetId TEXT," +
                "    SkillSetLevel TEXT," +
                "    IsDeleted TEXT," +
                "    AddedBy TEXT," +
                "    ModifiedBy TEXT," +
                "    ModifiedDt TEXT," +
                "    AddedDt TEXT," +
                "    IsApproval TEXT," +
                "    IsChargable TEXT," +
                "    IsPrivate TEXT," +
                "    Executionchargeable TEXT," +
                "    Billable_to_client TEXT," +
                "    IntimateCompletion TEXT,ProjectId TEXT)";

        public static final String CREATE_TABLE_CLAIM_DETAIL_Appr = "CREATE TABLE " + DatabaseHandlers.TABLE_Claim_Detail_Approver + "(EMPID TEXT," +
                "        Applicant TEXT," +
                "        CostCtrMasterId TEXT," +
                "        Date TEXT," +
                "        AuthorizedById TEXT," +
                "        ProjectId TEXT," +
                "        ActivityId TEXT," +
                "        Purpose TEXT," +
                "        HeaderTotal TEXT," +
                "        Advance TEXT," +
                "        Balance TEXT," +
                "        PaidAmount TEXT," +
                "        Remark TEXT," +
                "        ClaimCode TEXT," +
                "        Approved TEXT," +
                "        ClaimHeaderId TEXT," +
                "        Dt TEXT," +
                "        FromLocation TEXT," +
                "        ToLocation TEXT," +
                "        Exp1 TEXT," +
                "        Exp2 TEXT," +
                "        Exp3 TEXT," +
                "        Exp4 TEXT," +
                "        Exp5 TEXT," +
                "        Exp6 TEXT," +
                "        Exp7 TEXT," +
                "        Exp8 TEXT," +
                "        Exp9 TEXT," +
                "        Exp10 TEXT," +
                "        Distance TEXT," +
                "        Total TEXT," +
                "        Mode TEXT," +
                "        ModeName TEXT," +
                "        ClaimDetailId TEXT," +
                "        Status TEXT," +
                "        PlantName TEXT," +
                "        CostCtrDesc TEXT)";

        public static final String CREATE_TABLE_REASON_OUTAGE = "CREATE TABLE " + DatabaseHandlers.TABLE_REASON_OUTAGE +
                "(ActionMasterId TEXT,ActionCode TEXT,ActionDesc TEXT,IsDeleted TEXT,AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT)";

        public static final String CREATE_AFFECTED_CUSTOMER_INFO = "CREATE TABLE " + DatabaseHandlers.TABLE_AFFECTED_CUSTOMER_INFO +
                "(ActivityName TEXT,ActivityCode TEXT,CustTicketNo TEXT,CustVendorName TEXT,ReportedBy TEXT,PKActAffectedCust TEXT,Status TEXT)";

        public static final String CREATE_GETROUTEFROM_INFO = "CREATE TABLE " + DatabaseHandlers.TABLE_GetRouteFrom +
                "(PKRouteMasterId TEXT,SequenceNo TEXT,ItemPlantId TEXT,Point TEXT)";

        public static final String CREATE_GETROUTETO_INFO = "CREATE TABLE " + DatabaseHandlers.TABLE_GetRouteTo +
                "(PKRouteMasterId TEXT,SequenceNo TEXT,ItemPlantId TEXT,Point TEXT)";
        public static final String CREATE_GETBINDCATEGORY_INFO = "CREATE TABLE " + DatabaseHandlers.TABLE_GetBindCategory +
                "(PKProblemCategoryMaster TEXT,Code TEXT,Category TEXT,AddedBy TEXT,AddedDt TEXT)";

        public static final String CREATE_TABLE_IS_BILLABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_ISBILLABLE_AMOUNT +
                "(IsApproval TEXT,IsChargable TEXT,Executionchargeable TEXT,Billable_to_client TEXT,IntimateCompletion TEXT,NAtureofworkID TEXT)";

        public static final String CREATE_TABLE_TICKET_UPDATE_WAREHOUSE = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATE_WAREHOUSE + "(WareHouseMasterId TEXT,WarehouseDescription TEXT)";

        public static final String CREATE_TABLE_TICKET_UPDATE_LOCATION = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATE_LOCATION + "(LocationMasterId TEXT,LocationDesc TEXT,WareHouseMasterId TEXT)";

        public static final String CREATE_TABLE_TICKET_UPDATE_CODE = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATE_CODE + "(ItemCode TEXT,ItemDesc TEXT," +
                "ItemMasterId TEXT,ItemPlantId TEXT,StockUnit TEXT)";

        public static final String CREATE_TABLE_TICKET_UPDATE_MATERIAL_Add = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATE_MATERIAL_Add + "(IDMAT INTEGER PRIMARY KEY AUTOINCREMENT,ActivityID TEXT,warehouse TEXT," +
                "warehouseid TEXT,location TEXT,locationid TEXT,item_code TEXT,Itemplantid TEXT," +
                "discription TEXT,quant TEXT,fieldname TEXT,totalquant TEXT,cosumedquant TEXT,remark TEXT,UOM TEXT,IsUpload TEXT)";


        public static final String CREATE_TABLE_UPDATION_DATA_Vw = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_UPDATION_DATA_Vw + "(AllowEditingDays TEXT, " +
                "ActivityId TEXT," +
                "ActivityName TEXT," +
                "UserMasterId TEXT," +
                "UnitId TEXT," +
                "ActivityTypeId TEXT," +
                "StartDate TEXT," +
                "EndDate TEXT," +
                "DueDate TEXT," +
                "IssuedTo TEXT," +
                "Status TEXT," +
                "Remarks TEXT," +
                "IsDeleted TEXT," +
                "AddedBy TEXT," +
                "AddedDt TEXT," +
                "ModifiedBy TEXT," +
                "ModifiedDt TEXT," +
                "HoursRequired TEXT," +
                "ProposedUserId TEXT," +
                "PriorityId TEXT," +
                "ActualStartDate TEXT," +
                "ActualEndDate TEXT," +
                "IsApproval TEXT," +
                "IsApproved TEXT," +
                "ChargedAmount TEXT," +
                "ApprovedAmount TEXT," +
                "IsChargable TEXT, " +
                "Reason TEXT, " +
                "ApprovalDt TEXT, " +
                "PeriodicBillId TEXT, " +
                "CompletionIntimate TEXT, " +
                "Assigned_Count TEXT, " +
                "AttachmentName TEXT," +
                "SourceType TEXT," +
                "SourceId TEXT, " +
                "ExpectedComplete_Date TEXT, " +
                "ActivityCode TEXT, " +
                "ExecutionAmount TEXT, " +
                "ActualCompletionDate TEXT, " +
                "ReassignedBy TEXT," +
                "ReassignedDt TEXT, " +
                "PKSubActivityId TEXT, " +
                "ParentActId TEXT, " +
                "IsSubActivity TEXT, " +
                "TicketCategory TEXT, " +
                "WarrantyCode TEXT, " +
                "IsCompActPresent TEXT," +
                "CompletionActId TEXT, " +
                "TktCustReportedBy TEXT, " +
                "TktCustApprovedBy TEXT, " +
                "WarrantyId TEXT, " +
                "IsFromContract TEXT," +
                "ActivityRecId TEXT, " +
                "DateOfResumtion TEXT," +
                "StatusAliasId TEXT," +
                "AttachmentContent TEXT" +
                "IsUnplanned TEXT," +
                "ItemDesc TEXT, " +
                "ItemMasterId TEXT, " +
                "ConsigneeName TEXT," +
                "Address TEXT, " +
                "Mobile TEXT, " +
                "Email TEXT, " +
                "ReportedByEmail TEXT, " +
                "ContMob TEXT," +
                "ReportingToName TEXT," +
                "ReportingToEmail TEXT," +
                "StartDate1 TEXT," +
                "EndDate1 TEXT," +
                "DueDate1 TEXT," +
                "AddedDt1 TEXT," +
                "ModifiedDt1 TEXT," +
                "ActualStartDate1 TEXT," +
                "ActualEndDate1 TEXT," +
                "ApprovalDt1 TEXT," +
                "ExpectedComplete_Date1 TEXT," +
                "ActualCompletionDate1 TEXT," +
                "ReassignedDt1 TEXT)";


        public static final String CREATE_CLAIM_NOTIFICATION_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CLAIM_NOTIFICATION + "(Data TEXT)";


        public static final String CREATE_ACTIITY_GROUPLIST = "CREATE TABLE " + DatabaseHandlers.TABLE_ActivityGetGroupList
                + "(PKUserGroupId TEXT,UserGroupName TEXT,IsDeleted TEXT)";

    }

    class crmFactory {

        public static final String CREATE_TABLE_Product_Details_New = "CREATE TABLE " + DatabaseHandlers.TABLE_Product_Details_New +
                "(FkProductId TEXT,ItemDesc TEXT,Quantity TEXT)";

        public static final String CREATE_TABLE_PLANTMASTERCRM = "CREATE TABLE " + DatabaseHandlers.TABLE_PLANTMASTERCRM
                + "(PlantId TEXT)";


        public static final String CREATE_TABLE_FILLCONTROL_FETCH = "CREATE TABLE " + DatabaseHandlers.TABLE_FILLCONTROL_DATA_FETCH +
                "(PKSuspectId TEXT," +
                "        SuspectCode TEXT," +
                "        FirmName TEXT," +
                "        Address TEXT," +
                "        FKCityId TEXT," +
                "        FKStateId TEXT," +
                "        GSTCode TEXT," +
                "        GSTState TEXT," +
                "        FKCountryId TEXT," +
                "        BusinessDetails TEXT," +
                "        FKBusiSegmentId TEXT," +
                "        Turnover TEXT," +
                "        NoOfEmployees TEXT," +
                "        NoOfOffices TEXT," +
                "        FKCustomerId TEXT," +
                "        IsProspect TEXT," +
                "        IsLead TEXT," +
                "        IsOrder TEXT," +
                "        ProspectStatusChangedDate TEXT," +
                "        ProspectStatusChangedBy TEXT," +
                "        LeadStatusChangedDate TEXT," +
                "        LeadStatusChangedBy TEXT," +
                "        OrderStatusChangedDate TEXT," +
                "        OrderStatusChangedBy TEXT," +
                "        MoveToArchieve TEXT," +
                "        MoveToArchieveBy TEXT," +
                "        ReasonCode TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifiedBy TEXT," +
                "        ModifiedDt TEXT," +
                "        FKTerritoryId TEXT," +
                "        FKEnqSourceId TEXT," +
                "        CompanyURL TEXT," +
                "        SourceName TEXT," +
                "        PKProspectProductDtlsID TEXT," +
                "        PKSuspContactDtlsID TEXT," +
                "        ContactName TEXT," +
                "        Designation TEXT," +
                "        Telephone TEXT," +
                "        Mobile TEXT," +
                "        Fax TEXT," +
                "        EmailId TEXT," +
                "        IsActive TEXT," +
                "        IsPrimaryContact TEXT," +
                "        FKProductId TEXT," +
                "        ItemCode TEXT," +
                "        ItemDesc TEXT," +
                "        TechnicalDesc TEXT," +
                "        CityName TEXT," +
                "        TerritoryName TEXT," +
                "        LeadGivenBYId TEXT," +
                "        ContactPersonDept TEXT," +
                "        ItemMasterId TEXT," +
                "        DateofBirth TEXT," +
                "        EmailMsgId TEXT," +
                "        FirmAlias TEXT," +
                "        MoveToArchieveDt TEXT," +
                "        Notes TEXT," +
                "        MoveToArchieveReason TEXT," +
                "        MoveToArchieveNotes TEXT," +
                "        FKConsigneeId TEXT," +
                "        FKPlantId TEXT," +
                "        FirmTitle TEXT," +
                "        Remark TEXT," +
                "        Title TEXT," +
                "        IsReseller TEXT," +
                "        BusEstDate TEXT," +
                "        PANNo TEXT," +
                "        VATNo TEXT," +
                "        TINNo TEXT," +
                "        TANNo TEXT," +
                "        SystemsAvailable TEXT," +
                "        NoOfTechEmployees TEXT," +
                "        NoOfSalesEmployees TEXT," +
                "        BusinessType TEXT," +
                "        Docpath TEXT," +
                "        FileName TEXT," +
                "        Description TEXT," +
                "        EdjQualificationSales TEXT," +
                "        EdjQualificationTech TEXT," +
                "        CurrencyMasterId TEXT," +
                "        CurrencyDesc TEXT," +
                "        PBT TEXT," +
                "        PAT TEXT," +
                "        Network TEXT," +
                "        Borrowings TEXT," +
                "        Rating TEXT )";

        public static final String CREATE_TABLE_CONTACT_FETCH = "CREATE TABLE " + DatabaseHandlers.TABLE_CONTACT_DETAILS +
                "(PKSuspContactDtlsID TEXT," +
                "        ContactName TEXT," +
                "        Designation TEXT," +
                "        Telephone TEXT," +
                "        Mobile TEXT," +
                "        WhatsAppNo TEXT," +
                "        Fax TEXT," +
                "        EmailId TEXT," +
                "        IsActive TEXT," +
                "        IsPrimaryContact TEXT," +
                "        AddedDt TEXT," +
                "        FKSuspectId TEXT," +
                "        ContactPersonDept TEXT," +
                "        DateofBirth TEXT," +
                "        Title TEXT," +
                "        Gender TEXT," +
                "        SpouseName TEXT," +
                "        AnniversaryDate TEXT," +
                "        MaritalStatus TEXT," +
                "        Qualification TEXT," +
                "        Experience TEXT)";


        public static final String CREATE_TABLE_PROSPECT_VALIDATIONS = "CREATE TABLE " + DatabaseHandlers.TABLE_PROSPECT_VALIDATIONS +
                "(PKFieldID TEXT," +
                "        FKProspectHdrID TEXT," +
                "        ProspectField TEXT," +
                "        IsVisible TEXT," +
                "        IsMandatory TEXT,Caption TEXT,Section TEXT,FieldType TEXT)";

        public static final String CREATE_TABLE_PROSPECT_SERIAL = "CREATE TABLE " + DatabaseHandlers.TABLE_PROSPECT_SERIAL +
                "(SerialNumner TEXT)";


        public static final String CREATE_TABLE_COUNTRY = "CREATE TABLE " + DatabaseHandlers.TABLE_COUNTRY +
                "(PKCountryId TEXT," +
                "        CountryCode TEXT," +
                "        CountryName TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifyBy TEXT," +
                "        ModifiedDt TEXT," +
                "        IsDeleted TEXT)";

        /*public static final String CREATE_TABLE_COUNTRY = "CREATE TABLE " + DatabaseHandlers.TABLE_COUNTRY +
                "(PKCountryId TEXT," +
                "        CountryCode TEXT," +
                "        CountryName TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifyBy TEXT," +
                "        ModifiedDt TEXT," +
                "        IsDeleted TEXT," +
                "        MobileNoDigits)";*/

        public static final String CREATE_TABLE_SALES_FAMILY_PRODUCT = "CREATE TABLE " + DatabaseHandlers.TABLE_SALES_FAMILY_PRODUCT +
                "(FamilyId TEXT," +
                " FamilyDesc TEXT)";

//commented by sayali
      /*  public static final String CREATE_TABLE_SALES_FAMILY_PRODUCT = "CREATE TABLE " + DatabaseHandlers.TABLE_SALES_FAMILY_PRODUCT +
                "(FamilyId TEXT," +
                "        FamilyCode TEXT," +
                "        FamilyDesc TEXT," +
                "        IsDeleted TEXT," +
                "        AddedBy TEXT," +
                "        AddedDt TEXT," +
                "        ModifiedBy TEXT," +
                "        ModifiedDt TEXT," +
                "        MailSettingDays TEXT," +
                "        RescheduleCount TEXT," +
                "        ExpectedClosureDaysRescheduled TEXT," +
                "        OpenCallDays TEXT," +
                "        ConsecutiveOutcomeCount TEXT," +
                "        ProjectId TEXT," +
                "        ControllerId TEXT)";*/


        public static final String CREATE_CRM_CALL_PARTIAL_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_PARTIAL   //,ChatRoomId TEXT,ChatCount TEXT
                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT," +
                "CityName TEXT,Mobile TEXT,Product TEXT,FamilyDesc TEXT,SourceName TEXT,EmailId TEXT,Address TEXT" +
                ")";


        public static final String CREATE_TABLE_Outcome = "CREATE TABLE "
                + DatabaseHandlers.TABLE_Outcome + "(Outcome TEXT,Code TEXT ,PKOutcomeId TEXT,Outcome1 TEXT,IsActivity TEXT," +
                "IsEmail TEXT,Code1 TEXT,ActivityTypeId TEXT,ActivityFunctionality TEXT,EmailFunctionality TEXT," +
                "OutcomeType TEXT,ShowOthers TEXT,MilestoneSeqNo TEXT,LeadTime TEXT,IsRequest TEXT,IsApprover TEXT)";


        public static final String CREATE_TABLE_FEEDBACK = "CREATE TABLE "
                + DatabaseHandlers.TABLE_Feedback + "(New TEXT," +
                "Assigned TEXT," +
                "Overdue TEXT," +
                "Today TEXT," +
                "Tomorrow TEXT," +
                "ThisWeek TEXT," +
                "CurrentCallOwner TEXT," +
                "UserLoginId TEXT," +
                "UserName TEXT," +
                "New1 TEXT," +
                "Assigned1 TEXT," +
                "Overdue1 TEXT," +
                "Today1 TEXT," +
                "Tomorrow1 TEXT," +
                "TotalCollection TEXT," +
                "OverdueCollection TEXT," +
                "NewCollection TEXT," +
                "TodayCollection TEXT," +
                "ThisWeekCollection TEXT)";

        public static final String CREATE_TABLE_COLLECTION = "CREATE TABLE "
                + DatabaseHandlers.TABLE_COLLECTION + "(New TEXT,Assigned TEXT,Overdue TEXT,Today TEXT,Tomorrow TEXT," +
                "ThisWeek TEXT,Hot TEXT,Warm TEXT, TotalCollection TEXT, OverdueCollection TEXT," +
                "TodayCollection TEXT,ThisWeekCollection TEXT,TomorrowCollection TEXT,NewCollection TEXT)";

        public static final String CREATE_TABLE_ENQUIRY = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ENQUIRY + "(EnquiryRegistryId TEXT," +
                "EnquiryDate TEXT," +
                "RegistryById TEXT," +
                "AssignedToId TEXT," +
                "CustomerName TEXT," +
                "ContactName TEXT," +
                "ContactNumber TEXT," +
                "Email TEXT," +
                "EnquiryDetails TEXT," +
                "ActionTaken TEXT," +
                "ReasonForCancellation TEXT," +
                "CallId TEXT," +
                "AddedBy TEXT," +
                "Addeddt TEXT," +
                "ModifiedBy TEXT," +
                "ModifiedDt TEXT," +
                "Status TEXT)";


        public static final String CREATE_TABLE_PROMOTER = "CREATE TABLE " + DatabaseHandlers.TABLE_PROMOTER +
                "(UsermasterId TEXT," +
                "UserLoginId TEXT," +
                "UserName TEXT)";

        public static final String CREATE_TABLE_PROMOTER_REPORT = "CREATE TABLE " + DatabaseHandlers.TABLE_PROMOTER_REPORT +
                "(UserMasterId  TEXT," +
                " UserLoginId  TEXT," +
                " Location  TEXT," +
                " Dt  TEXT," +
                " D2DChillTaste  TEXT," +
                " HChillTaste  TEXT," +
                " SampleGiven  TEXT," +
                "SalesAmount TEXT)";

        public static final String CREATE_TABLE_TICKET_CUSTOMER_LIST = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_CUSTOMER_LIST +
                "(CustVendorMasterId TEXT," +
                "CustVendorCode TEXT," +
                "CustVendorName TEXT," +
                "ContactName TEXT," +
                "CityName TEXT)";
        public static final String CREATE_TABLE_TICKET_COUNT = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_COUNT +
                "(ConsigneeName TEXT," +
                "CustomerId TEXT," +
                "City TEXT," +
                "SourceId TEXT," +
                "AssignedTkt TEXT," +
                "CompletedTkt TEXT," +
                "OnTimeCompletedTkt TEXT)";

        public static final String CREATE_TABLE_TICKET_COUNT_MONTHWISE = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_COUNT_MONTHWISE +
                "(ConsigneeName  TEXT," +
                " CustomerId  TEXT," +
                " City  TEXT," +
                " SourceId  TEXT," +
                " Jan  TEXT," +
                " Feb  TEXT," +
                " Mar  TEXT," +
                " Apr  TEXT," +
                " May  TEXT," +
                " June  TEXT," +
                " July  TEXT," +
                " Aug  TEXT," +
                " Sep  TEXT," +
                " Oct  TEXT," +
                " Nov  TEXT," +
                " Dec  TEXT,monthYear TEXT)";

        public static final String CREATE_TABLE_TICKET_COUNT_DETAIL = "CREATE TABLE " + DatabaseHandlers.TABLE_TICKET_COUNT_DETAIL +
                "(rowcnt  TEXT," +
                "  ConsigneeName  TEXT," +
                "  CustomerId  TEXT," +
                "  City  TEXT," +
                "  ActivityId  TEXT," +
                "  ActivityName  TEXT," +
                "  UserMasterId  TEXT," +
                "  StartDate  TEXT," +
                "  EndDate  TEXT," +
                "  ModifiedDt  TEXT," +
                "  ExpectedComplete_Date  TEXT," +
                "  IssuedTo  TEXT," +
                "  Status  TEXT," +
                "  ActualStartDate  TEXT," +
                "  ActualEndDate  TEXT," +
                "  SourceId  TEXT," +
                "  SourceType  TEXT," +
                "  IsApproved  TEXT," +
                "  AddedDt  TEXT,TicketType TEXT)";
        //Calender

        public static final String CREATE_TABLE_CALENDER = "CREATE TABLE " + DatabaseHandlers.TABLE_Calender + "(NextActionDateTime TEXT," +
                "CountCollection TEXT,CountFeedBack TEXT,CountSales TEXT)";

        //Bankname data fetch

        public static final String CREATE_TABLE_BANKNAME = "CREATE TABLE " + DatabaseHandlers.TABLE_BANKNAME + "(BankMasterId TEXT," +
                "BankName TEXT)";

        //Provisional
        public static final String CREATE_TABLE_PROVISINALLIST = "CREATE TABLE " + DatabaseHandlers.TABLE_PROVISINALLIST + "(CollectionReceiptMasterId TEXT," +
                "InvoiceNo TEXT,Amount TEXT,InstrumentNo TEXT,BankName TEXT,TDSAmount TEXT,AddedBy TEXT,AddedDt TEXT,DepositedBank TEXT,DepositedDate TEXT,Narration TEXT)";


        //Customer

        public static final String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + DatabaseHandlers.TABLE_CUSTOMER + "(CustVendorMasterId TEXT,CustVendorName TEXT)";


        public static final String CREATE_SQLITE_OPPORTUNITY = "CREATE TABLE " + DatabaseHandlers.TABLE_OPPORTUNITY_UPDATE + "(Opportunity_update TEXT,UpdateData TEXT)";


        //Call_Collection
        public static final String CREATE_CRM_CALL_COllECTION_OPPORTUNITY = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_COLLECTION_CALL_OPPORTUNITY
                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT," +
                "CityName TEXT,Mobile TEXT,InvoiceNo TEXT,Amount TEXT,InvoiceDt TEXT," +
                "UnAllocatedCash TEXT,ProvisionalCount TEXT,BalVal TEXT,NextAction TEXT" +
                ")";//,PKSuspectId TEXT//,Product TEXT


        //Call_opportunity
        public static final String CREATE_CRM_CALL_OPPORTUNITY = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_OPPORTUNITY

                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT,CityName TEXT,Mobile TEXT,Product TEXT,Address TEXT,lat TEXT,Long TEXT,ExpectedValue TEXT,EmailId TEXT,NextAction TEXT,NextMilestone TEXT,ExpectedCloserDate TEXT" +
                ")";//,PKSuspectId TEXT


        public static final String CREATE_CRM_CALL_FEEDBACK = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_FEEDBACK
                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,ProspectId TEXT,LatestRemark TEXT,CityName TEXT,Mobile TEXT)";

               /* "isPartial TEXT,CallType TEXT ,ProspectId TEXT,LatestRemark TEXT,CityName TEXT,Mobile TEXT,SourceName TEXT," +
                "SouceId TEXT)";*/

        //,PKSuspectId TEXT
        //SourceName


        public static final String CREATE_TABLE_Travelplan = "CREATE TABLE " + DatabaseHandlers.TABLE_Travelplan +
                "(PKTravelPlanId TEXT, Date TEXT,Notes TEXT,CityName TEXT," +
                " ExecutiveId TEXT, VisitPlan TEXT ,CityId TEXT ,AddedBy TEXT" +
                ",AddedDt TEXT,UserName TEXT,SortDate TEXT,IsDeleted TEXT)";


      /*  public static final String CREATE_Team_Member = "CREATE TABLE " + DatabaseHandlers.TABLE_Team_Member +
                "(UserMasterId TEXT, UserLoginId TEXT,UserName TEXT,Assigned TEXT," +
                " Overdue TEXT, Today TEXT ,Hot TEXT ,Collection TEXT,Tomorrow TEXT,Report TEXT)";*/

        public static final String CREATE_Team_Member = "CREATE TABLE " + DatabaseHandlers.TABLE_Team_Member +
                "(UserMasterId TEXT, UserLoginId TEXT,UserName TEXT,Assigned TEXT," +
                " Overdue TEXT, Today TEXT ,Hot TEXT ,Collection TEXT,Tomorrow TEXT,Report TEXT,CRMCategory TEXT," +
                "CallReview TEXT,LocationName TEXT,ISMobileApp TEXT,Warm TEXT)";


        public static final String CREATE_SubTeam_Member = "CREATE TABLE " + DatabaseHandlers.TABLE_SubTeam_Member +
                "(UserMasterId TEXT, UserLoginId TEXT,UserName TEXT,Assigned TEXT," +
                " Overdue TEXT, Today TEXT ,Hot TEXT ,Collection TEXT,Tomorrow TEXT,Report TEXT,CRMCategory TEXT," +
                "CallReview TEXT,LocationName TEXT,ISMobileApp TEXT,Warm TEXT )";


        public static final String CREATE_TABLE_Firm = "CREATE TABLE " + DatabaseHandlers.TABLE_Firm +
                "(Id TEXT,Name TEXT)";


        public static final String CREATE_TABLE_Setup_Prospect = "CREATE TABLE " + DatabaseHandlers.TABLE_Setup_Prospect +
                "(Key TEXT,value TEXT)";


        public static final String CREATE_TABLE_Entity = "CREATE TABLE " + DatabaseHandlers.TABLE_Entity +
                "(CustVendorMasterId TEXT,CustVendorName TEXT)";


        public static final String CREATE_TABLE_Consignee = "CREATE TABLE " + DatabaseHandlers.TABLE_Consignee +
                "(ShipToMasterId TEXT,ConsigneeName TEXT)";


        public static final String CREATE_TABLE_Referencetype = "CREATE TABLE " + DatabaseHandlers.TABLE_Referencetype +
                "(CustVendor TEXT,CustVendorCode TEXT)";


        public static final String CREATE_TABLE_Reference = "CREATE TABLE " + DatabaseHandlers.TABLE_Reference +
                "(CustVendorMasterId TEXT,CustVendorName TEXT)";

        public static final String CREATE_TABLE_StateList = "CREATE TABLE " + DatabaseHandlers.TABLE_STATE +
                "(PKStateId TEXT,StateDesc TEXT,StateNo TEXT,FKCountryId TEXT,IsDeleted TEXT,AddedBy TEXT,AddedDt TEXT," +
                "ModifiedBy TEXT,ModifiedDt TEXT)";


       /* public static final String CREATE_TABLE_StateList = "CREATE TABLE " + DatabaseHandlers.TABLE_STATE +
                "(PKStateId TEXT,StateDesc TEXT,StateNo TEXT,StateDesc TEXT,FKCountryId TEXT,IsDeleted TEXT" +
                ",AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT)";*/


        public static final String CREATE_TABLE_PRODUCTDATA_FETCH = "CREATE TABLE " + DatabaseHandlers.TABLE_PRODUCT_DATA_FETCH +
                "(PKProspectProductDtlsID TEXT,FKSuspectId TEXT,FKProductId TEXT,PurchaseMode TEXT,FirmName TEXT,Address TEXT," +
                "BusinessDetails TEXT,FKBusiSegmentId TEXT,Turnover TEXT,NoOfEmployees TEXT,NoOfOffices TEXT,FirmAlias TEXT,Notes TEXT,CompanyURL TEXT)";


        public static final String CREATE_TABLE_OrderType = "CREATE TABLE " + DatabaseHandlers.TABLE_OrderType +
                "(OrderTypeMasterId TEXT," +
                "    Code TEXT," +
                "    Description TEXT," +
                "   IsDeleted TEXT," +
                "   IsShipInvRequired TEXT" +
                "    )";


        public static final String CREATE_TABLE_SE = "CREATE TABLE " + DatabaseHandlers.TABLE_SE +
                "(vWBUsermasterId TEXT ,    UserLoginId TEXT,     UserName TEXT, " +
                "   DesigId TEXT,     Designation TEXT,     UMRT TEXT,    ReportingUserId TEXT," +
                "    ReportingName TEXT,     ReportingToEmail TEXT,     EkatmUserMasterId TEXT,   " +
                "  DeptCode TEXT,     Department TEXT,     Active TEXT,     PKDeptMasterid  TEXT,  " +
                "  CRMNoofDays TEXT,     Mobile TEXT,      CRMCategory  TEXT, " +
                "    ReportingToEkatmUserId TEXT,     IsActive TEXT,      PlantName TEXT )";


        public static final String CREATE_TABLE_BOE = "CREATE TABLE " + DatabaseHandlers.TABLE_BOE +
                "(vWBUsermasterId TEXT ,    UserLoginId TEXT,     UserName TEXT, " +
                "   DesigId TEXT,     Designation TEXT,     UMRT TEXT,    ReportingUserId TEXT," +
                "    ReportingName TEXT,     ReportingToEmail TEXT,     EkatmUserMasterId TEXT,   " +
                "  DeptCode TEXT,     Department TEXT,     Active TEXT,     PKDeptMasterid  TEXT,  " +
                "  CRMNoofDays TEXT,     Mobile TEXT,      CRMCategory  TEXT, " +
                "    ReportingToEkatmUserId TEXT,     IsActive TEXT,      PlantName TEXT )";


        public static final String CREATE_TABLE_FollowUpTime = "CREATE TABLE " + DatabaseHandlers.TABLE_FollowUpTime +
                "(FollowUpTime TEXT )";


        public static final String CREATE_TABLE_Campaign = "CREATE TABLE " + DatabaseHandlers.TABLE_Campaign +
                "( PKCampaignId TEXT, CampaignName TEXT )";


        public static final String CREATE_TABLE_SuspectSource = "CREATE TABLE " + DatabaseHandlers.TABLE_Prospectsource +
                "(PKSuspSourceId TEXT," +
                "                SourceName TEXT," +
                "                AddedBy TEXT," +
                "                ModifiedBy TEXT,IsDeleted TEXT)";


        public static final String CREATE_TABLE_Business_segment = "CREATE TABLE " + DatabaseHandlers.TABLE_Business_segment +
                "(PKBusiSegmentID TEXT," +
                "                SegmentCode TEXT," +
                "                SegmentDescription TEXT," +
                "                FKParentBusiSegmentID TEXT)";


        public static final String CREATE_TABLE_Product = "CREATE TABLE " + DatabaseHandlers.TABLE_Product +
                "(ItemMasterId TEXT,ItemDesc TEXT,ItemClassificationId TEXT,ItemCode TEXT," +
                "FamilyId TEXT,FamilyDesc TEXT,ItemCodeDesc TEXT,ItemPlantId TEXT" +
                ",PlantId TEXT)";


        public static final String CREATE_TABLE_Product_Details = "CREATE TABLE " + DatabaseHandlers.TABLE_Product_Details +
                "(ItemMasterId TEXT,ItemDesc TEXT,ItemClassificationId TEXT,ItemCode TEXT," +
                "FamilyId TEXT,FamilyDesc TEXT,ItemCodeDesc TEXT,ItemPlantId TEXT" +
                ",PlantId TEXT,Qnty TEXT)";


        public static final String CREATE_TABLE_Teritory = "CREATE TABLE " + DatabaseHandlers.TABLE_Teritory +
                "(PKTerritoryId TEXT,TerritoryCode TEXT,TerritoryName TEXT,FKParentTerritoryId TEXT," +
                "AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT" +
                ",IsDeleted TEXT,ManagerId TEXT)";

        public static final String CREATE_TABLE_TERRITORY = "CREATE TABLE " + DatabaseHandlers.TABLE_Territory +
                "(PKTalukaId TEXT,TalukaNo TEXT,TalukaDesc TEXT,FKDistrictId TEXT,IsDeleted TEXT,AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT)";


        public static final String CREATE_TABLE_filterdata_prospect = "CREATE TABLE " + DatabaseHandlers.TABLE_filterdata_prospect +
                "(PKSuspectId  TEXT ," +
                "    SuspectCode  TEXT ," +
                "    FirmName  TEXT , " +
                "    Address  TEXT ," +
                "    FKCityId  TEXT ," +
                "    BusinessDetails  TEXT , " +
                "    FKBusiSegmentId  TEXT ," +
                "    Turnover  TEXT ," +
                "    NoOfEmployees  TEXT ," +
                "    NoOfOffices  TEXT ," +
                "    FKCustomerId  TEXT , " +
                "    IsProspect  TEXT , " +
                "    IsLead  TEXT ," +
                "    IsOrder  TEXT ," +
                "    ProspectStatusChangedDate  TEXT ," +
                "    ProspectStatusChangedBy  TEXT ," +
                "    LeadStatusChangedDate  TEXT ," +
                "    LeadStatusChangedBy  TEXT ," +
                "    OrderStatusChangedDate  TEXT ," +
                "    OrderStatusChangedBy  TEXT ," +
                "    MoveToArchieve  TEXT ," +
                "    MoveToArchieveBy  TEXT , " +
                "    ReasonCode  TEXT ," +
                "    AddedBy  TEXT , " +
                "    AddedDt  TEXT ," +
                "    ModifiedBy  TEXT , " +
                "    ModifiedDt  TEXT , " +
                "    FKTerritoryId  TEXT ," +
                "    FKEnqSourceId  TEXT , " +
                "    CompanyURL  TEXT , " +
                "    SourceName  TEXT ," +
                "    PKSuspContactDtlsID  TEXT ," +
                "    ContactName  TEXT , " +
                "    Designation  TEXT ," +
                "    Telephone  TEXT ," +
                "    Mobile  TEXT ," +
                "    Fax  TEXT ," +
                "    EmailId  TEXT ," +
                // "    IsActive  TEXT ,   " +  commented by sayali as no value for isactive from api /api/SuspectMasterAPI/GetSuspectList
                //"    IsPrimaryContact  TEXT ," +
                "    FamilyDesc  TEXT ," +
                "    CityName  TEXT ," +
                "    TerritoryName  TEXT ," +
                "    LeadGivenBYId  TEXT ," +
                "    ContactPersonDept  TEXT ," +
                "    CustVendorName  TEXT ," +
                "    FirmAlias  TEXT ," +
                "    OpenCalls  TEXT , " +
                "    CloseCalls  TEXT ," +
                "    OrderReceived  TEXT ," +
                "    OrderLost  TEXT ," +
                "    OrderRegrete  TEXT ," +
                "    CallCloseWithoutOrder  TEXT ," +
                "    FKConsigneeId  TEXT ," +
                "    LastCSSDate  TEXT ," +
                "    LastCSSRating  TEXT ," +
                "    FuturePlanDate  TEXT ," +
                "    LastSODate  TEXT ," +
                "    LastSOScheduleDate  TEXT ," +
                "    LastShipmentDate  TEXT ," +
                "    FKPlantId  TEXT ,   " +
                "    FirmTitle  TEXT ," +
                "    Remark  TEXT ,   " +
                "    Title  TEXT ," +
                "    UserName  TEXT ," +
                "    LatestRemark  TEXT ," +
                "    SegmentDescription," +
                "    IsReseller TEXT," +
                "    FKCountryId TEXT," +
                "    FKStateId TEXT)";

        /*," +   "    PinCode TEXT*/
        public static final String CREATE_CRM_OPPOTUNITY_CALL_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_OPPOTUNITY_CALL_FILTER
                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                /*"ChatCount TEXT,"+
                "ChatRoomId TEXT,"+*/
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT,CityName TEXT," +
                "Mobile TEXT,Product TEXT,Address TEXT,ExpectedValue TEXT" +
                ")";

        public static final String CREATE_CRM_CALL_PARTIAL_TEAM_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_PARTIAL_TEAM
                + "(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT,CityName TEXT,Mobile TEXT,Product TEXT,Address TEXT,lat TEXT,Long TEXT,ExpectedValue TEXT,EmailId TEXT,NextAction TEXT,NextMilestone TEXT,ExpectedCloserDate TEXT" +
                ")";

                /*+ "(FirmName TEXT, " +
                "CityName TEXT,CallId TEXT," +
                "NextActionDateTime TEXT," +
                "Mobile TEXT, CallType TEXT ,CallStatus TEXT,ContactName TEXT,CurrentCallOwner TEXT,UserName TEXT,Address TEXT,ExpectedValue TEXT,EmailId TEXT,NextAction TEXT)";
*/


        // Collection Call
        public static final String CREATE_CRM_COLLECTIONCALL_PARTIAL_TEAM_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_COLLECTIONCALL_PARTIAL_TEAM
                +"(FirmName TEXT, " +
                "CallId TEXT,ContactName TEXT," +
                "NextActionDateTime TEXT," +
                "isPartial TEXT,CallType TEXT ,CallStatus TEXT,ProspectId TEXT,LatestRemark TEXT," +
                "CityName TEXT,Mobile TEXT,InvoiceNo TEXT,Amount TEXT,InvoiceDt TEXT," +
                "UnAllocatedCash TEXT,ProvisionalCount TEXT,BalVal TEXT,NextAction TEXT)";
        /*+ "(FirmName TEXT, " +
                "CityName TEXT,CallId TEXT," +
                "NextActionDateTime TEXT," +
                "Mobile TEXT, CallType TEXT ,CallStatus TEXT,ContactName TEXT,CurrentCallOwner TEXT,UserName TEXT)";*/


        public static final String CREATE_TABLE_User_Type = "CREATE TABLE " + DatabaseHandlers.TABLE_User_Type +
                "(UserMasterId TEXT,UserName TEXT,CRMCategoryMstID TEXT,Code TEXT)";


        public static final String CREATE_TABLE_CALL_RATING = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CALL_RATING + "(Assigned TEXT,Overdue TEXT,Today TEXT,Tomorrow TEXT," +
                "ThisWeek TEXT,Hot TEXT,Warm TEXT,HotCount TEXT,HotValue TEXT,WarmCount TEXT, WarmValue TEXT)";


        public static final String CREATE_TABLE_APPOINTMENT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_APPOINTMENT + "(AppointmentDate TEXT,FormattedAppointmentTime TEXT,AppointmentTime TEXT,FirmName TEXT," +
                "CityName TEXT,DayOfVisit TEXT)";


        public static final String CREATE_TABLE_OPPORTUNITIES = "CREATE TABLE "
                + DatabaseHandlers.TABLE_OPPORTUNITIES + "(Assigned TEXT,Overdue TEXT,Today TEXT,Tomorrow TEXT," +
                "ThisWeek TEXT,Hot TEXT,Warm TEXT,New TEXT,Yesterday TEXT,CallAgain TEXT,Revived TEXT)";


        public static final String CREATE_CALLHISTORY_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CALLHISTORY + "(CallHistoryId TEXT,CallId TEXT,CurrentCallOwner TEXT,ActionType TEXT,Contact TEXT,Purpose TEXT,NextAction TEXT,NextActionDateTime TEXT,ModifiedDt TEXT,Outcome TEXT,UserName TEXT,OutcomeCode TEXT,LatestRemark TEXT)";


        public static final String CREATE_TABLE_Reason = "CREATE TABLE "
                + DatabaseHandlers.TABLE_REASON + "(PKReasonID TEXT,ReasonDescription TEXT,FKReasonCategoryID TEXT,ReasonCode TEXT)";


        public static final String CREATE_TABLE_REASON_Master = "CREATE TABLE "
                + DatabaseHandlers.TABLE_REASON_Master + "(PKReasonID TEXT,ReasonDescription TEXT,FKReasonCategoryID TEXT,ReasonCode TEXT)";


        public static final String CREATE_TABLE_Calllistdata = "CREATE TABLE " + DatabaseHandlers.TABLE_CALLLISTDATA
                + "(FirmName TEXT, " +
                "CityName TEXT," +
                "Product TEXT," +
                "AssignedBy TEXT,ContactName TEXT,Outcome TEXT,LatestRemark TEXT,UserName TEXT,NextActionDateTime TEXT,CallId TEXT)";


        public static final String CREATE_CRM_CALL_TEAM_TABLE = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_TEAM +
                "(SrNo  TEXT ," +
                "        CallId  TEXT ," +
                "        CallStart  TEXT ," +
                "        ProductId  TEXT ," +
                "        Product  TEXT ," +
                "        CallStatus  TEXT ," +
                "        Outcome  TEXT ," +
                "        NextActionDateTime  TEXT ," +
                "        LatestRemark  TEXT ," +
                "        CurrentCallOwner  TEXT ," +
                "        UserName  TEXT ," +
                "        DemoRequired  TEXT ," +
                "        DemoStatus  TEXT ," +
                "        PrebidRequired  TEXT ," +
                "        PrebidStatus  TEXT ," +
                "        SpecialPrizeRequest  TEXT ," +
                "        SpecialPrizeStatus  TEXT ," +
                "        ContractReviewRequired  TEXT ," +
                "        ContractStatus  TEXT ," +
                "        UserLoginId  TEXT ," +
                "        Followups  TEXT ," +
                "        EkatmUserMasterId  TEXT ," +
                "        NextAction  TEXT ," +
                "        ProspectId  TEXT ," +
                "        Compaign  TEXT ," +
                "        ContactName  TEXT ," +
                "        Designation  TEXT ," +
                "        Telephone  TEXT ," +
                "        Mobile  TEXT ," +
                "        Email  TEXT ," +
                "        IsActive  TEXT ," +
                "        IsPrimaryContact  TEXT ," +
                "        CRMNoofDays  TEXT ," +
                "        Isclose  TEXT ," +
                "        SpecialPrizeApprover  TEXT ," +
                "        PresalesExecutiveId  TEXT ," +
                "        PresalesSupportRequired  TEXT ," +
                "        PresalesSupportDetails  TEXT ," +
                "        PresalesSupportDueDate  TEXT ," +
                "        DemoDatetime TEXT ," +
                "        QuotationNo  TEXT ," +
                "        QuotationDate  TEXT ," +
                "        QuotationValue  TEXT ," +
                "        OrderStatus  TEXT ," +
                "        OrderReferenceId  TEXT ," +
                "        OrderReceivedDate  TEXT ," +
                "        OrderValue  TEXT ," +
                "        OrderLostReasonCode  TEXT ," +
                "        OrderLostDetails  TEXT ," +
                "        TeleCallCount  TEXT ," +
                "        MeetingCount  TEXT ," +
                "        DemoCount  TEXT ," +
                "        QuotationCount  TEXT ," +
                "        DemoAssignedTo  TEXT ," +
                "        ContractResponsibility  TEXT ," +
                "        ExpectedPrize  TEXT ," +
                "        OrderRecievedBy  TEXT ," +
                "        OrderPONo  TEXT ," +
                "        OrderPOValue  TEXT ," +
                "        DemoReasonCode  TEXT ," +
                "        OutcomeCode  TEXT ," +
                "        EmailCount  TEXT ," +
                "        VisitCount  TEXT ," +
                "        TotalHoursSpent  TEXT ," +
                "        PrebidAssignedTo  TEXT ," +
                "        CallCloseReason  TEXT ," +
                "        CallCloseDetails  TEXT ," +
                "        OrderRegradeReason  TEXT ," +
                "        OrderRegradeReasonDetails  TEXT ," +
                "        QuotationAssignedTo  TEXT ," +
                "        QuotationRequest  TEXT ," +
                "        QuotationStatus  TEXT ," +
                "        CallRatingChangeReason  TEXT ," +
                "        ExpectedValue  TEXT ," +
                "        ExpectedCloserDate  TEXT ," +
                "        OverdueDays  TEXT ," +
                "        DueDays  TEXT ," +
                "        PresalesSupportedBy  TEXT ," +
                "        OrderLostFlag  TEXT ," +
                "        OrderRegretFlag  TEXT ," +
                "        CallCloseWihoutOrderFlag  TEXT ," +
                "        CallLife  TEXT ," +
                "        PresalesSupportStatus  TEXT ," +
                "        OrderRegrteApprover  TEXT ," +
                "        OrderLostApprover  TEXT ," +
                "        CallCloseApprover  TEXT ," +
                "        ContactId  TEXT ," +
                "        SEId  TEXT ," +
                "        BackOfficeExecutiveId  TEXT ," +
                "        PKSuspectId  TEXT ," +
                "        FirmName  TEXT ," +
                "        FKCityId  TEXT ," +
                "        IsProspect  TEXT ," +
                "        IsLead  TEXT ," +
                "        IsOrder  TEXT ," +
                "        MoveToArchieve  TEXT ," +
                "        MoveToArchieveBy  TEXT ," +
                "        FKTerritoryId  TEXT ," +
                "        FKEnqSourceId  TEXT ," +
                "        CompanyURL  TEXT ," +
                "        SourceName   TEXT ," +
                "        CityName  TEXT ," +
                "        TerritoryName  TEXT ," +
                "        LeadGivenBYId  TEXT ," +
                "        ContactPersonDept  TEXT ," +
                "        CustVendorName  TEXT ," +
                "        FirmAlias  TEXT ," +
                "        OpenCalls  TEXT ," +
                "        CloseCalls  TEXT ," +
                "        OrderReceived  TEXT ," +
                "        OrderLost  TEXT ," +
                "        OrderRegrete  TEXT ," +
                "        CallCloseWithoutOrder  TEXT ," +
                "        Source  TEXT ," +
                "        SouceId  TEXT ," +
                "        Alias  TEXT ," +
                "        CallType  TEXT ," +
                "        RescheduleCount  TEXT ," +
                "        CollectMode   TEXT ," +
                "        InstrNo   TEXT ," +
                "        Instrdate   TEXT ," +
                "        BankName   TEXT ," +
                "        Amount  TEXT ," +
                "        AddedBy   TEXT ," +
                "        AddedDt   TEXT ," +
                "        ModifiedBy   TEXT ," +
                "        ModifiedDt    TEXT ," +
                "        AssignedBy  TEXT ," +
                "        AssignTochannel  TEXT ," +
                "        ReportingUserId  TEXT ," +
                "        ReportingName  TEXT ," +
                "        ReportingToEkatmUserId  TEXT ," +
                "        ReportingToEmail  TEXT ," +
                "        FKPlantId TEXT )";


        public static final String CREATE_TABLE_NatureofCall = "CREATE TABLE "
                + DatabaseHandlers.TABLE_NatureofCall + "(PKNatureofCall TEXT,NatureofCall TEXT)";

        public static final String CREATE_TABLE_APPROVER = "CREATE TABLE "
                + DatabaseHandlers.TABLE_APPROVER + "(UserMasterID TEXT,UserName TEXT)";

        public static final String CREATE_TABLE_InitiatedBy = "CREATE TABLE "
                + DatabaseHandlers.TABLE_InitiatedBy + "(PKInitiatedBy TEXT,InitiatedBy TEXT)";


        public static final String CREATE_TABLE_With_whom = "CREATE TABLE "
                + DatabaseHandlers.TABLE_With_whom + "(ContactName TEXT,PKSuspContactDtlsID TEXT,Telephone TEXT,Mobile TEXT)";


        public static final String CREATE_TABLE_Followup_reason = "CREATE TABLE "
                + DatabaseHandlers.TABLE_Followup_reason + "(PKCallPurposeId TEXT,CallPurposeDesc TEXT)";


        public static final String CREATE_TABLE_Category = "CREATE TABLE "
                + DatabaseHandlers.TABLE_Category + "(UserMasterId TEXT,UserName TEXT,CRMCategory TEXT)";


        public static final String CREATE_TABLE_Executive = "CREATE TABLE "
                + DatabaseHandlers.TABLE_Executive + "(UserMasterId TEXT,UserName TEXT,CRMCategory TEXT)";


        public static final String CREATE_TABLE_CurrencyMaster = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CurrencyMaster + "(CurrencyMasterId TEXT,CurrDesc TEXT)";


        public static final String CREATE_TABLE_OrderTypeMaster = "CREATE TABLE "
                + DatabaseHandlers.TABLE_OrderTypeMaster + "(OrderTypeMasterId TEXT,Description TEXT)";


        public static final String CREATE_TABLE_OrderTypeMaster_All = "CREATE TABLE "
                + DatabaseHandlers.TABLE_OrderTypeMaster_All + "(Description TEXT,OrderTypeMasterId TEXT)";
//commented by sayali
        public static final String CREATE_TABLE_TMESEName = "CREATE TABLE "
                + DatabaseHandlers.TABLE_TMESEName + "(EkatmUserMasterId TEXT,UserName TEXT,ReportingName TEXT,ReportingToEmail TEXT)";

       /* public static final String CREATE_TABLE_TMESEName = "CREATE TABLE "
                + DatabaseHandlers.TABLE_TMESEName + "(UserMasterId TEXT,UserName TEXT)";
*/
        public static final String CREATE_TABLE_CITY = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CITY
                + "(PKCityID TEXT , CityName TEXT,FKTalukaId TEXT, FKStateId TEXT, FKDistrictId TEXT," +
                " PinCode TEXT," +
                " FKCountryId TEXT ,FKTerritoryId TEXT,AddedBy TEXT,AddedDt TEXT, ModifiedBy TEXT," +
                "ModifiedDt TEXT,IsDeleted TEXT, TerritoryName TEXT)";

        public static final String CREATE_TABLE_CITY_PROSPECT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CITY_PROSPECT
                + "(PKDistrictId TEXT,DistrictNo TEXT,DistrictDesc TEXT," +
                " FKStateId TEXT," +
                " IsDeleted TEXT ,AddedBy TEXT,AddedDt TEXT, ModifiedBy TEXT)";

        public static final String CREATE_TABLE_CITY_MASTER = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CITY_MASTER
                + "(PKCityID TEXT,CityName TEXT,FKTalukaId TEXT,FKDistrictId TEXT,FKStateId TEXT,FKCountryId TEXT," +
                "PinCode TEXT,FKTerritoryId TEXT,AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT,IsDeleted TEXT)";


        public static final String CREATE_TABLE_CRM_CALL_OPP = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_OPP +
                "(SrNo  TEXT ," +
                "        CallId  TEXT ," +
                "        CallStart  TEXT ," +
                "        ProductId  TEXT ," +
                "        Product  TEXT ," +
                "        CallStatus  TEXT ," +
                "        Outcome  TEXT ," +
                "        NextActionDateTime  TEXT ," +
                "        LatestRemark  TEXT ," +
                "        CurrentCallOwner  TEXT ," +
                "        UserName  TEXT ," +
                "        DemoRequired  TEXT ," +
                "        DemoStatus  TEXT ," +
                "        PrebidRequired  TEXT ," +
                "        PrebidStatus  TEXT ," +
                "        SpecialPrizeRequest  TEXT ," +
                "        SpecialPrizeStatus  TEXT ," +
                "        ContractReviewRequired  TEXT ," +
                "        ContractStatus  TEXT ," +
                "        UserLoginId  TEXT ," +
                "        Followups  TEXT ," +
                "        EkatmUserMasterId  TEXT ," +
                "        NextAction  TEXT ," +
                "        ProspectId  TEXT ," +
                "        Compaign  TEXT ," +
                "        ContactName  TEXT ," +
                "        Designation  TEXT ," +
                "        Telephone  TEXT ," +
                "        Mobile  TEXT ," +
                "        Email  TEXT ," +
                "        IsActive  TEXT ," +
                "        IsPrimaryContact  TEXT ," +
                "        CRMNoofDays  TEXT ," +
                "        Isclose  TEXT ," +
                "        SpecialPrizeApprover  TEXT ," +
                "        PresalesExecutiveId  TEXT ," +
                "        PresalesSupportRequired  TEXT ," +
                "        PresalesSupportDetails  TEXT ," +
                "        PresalesSupportDueDate  TEXT ," +
                "        DemoDatetime TEXT ," +
                "        QuotationNo  TEXT ," +
                "        QuotationDate  TEXT ," +
                "        QuotationValue  TEXT ," +
                "        OrderStatus  TEXT ," +
                "        OrderReferenceId  TEXT ," +
                "        OrderReceivedDate  TEXT ," +
                "        OrderValue  TEXT ," +
                "        OrderLostReasonCode  TEXT ," +
                "        OrderLostDetails  TEXT ," +
                "        TeleCallCount  TEXT ," +
                "        MeetingCount  TEXT ," +
                "        DemoCount  TEXT ," +
                "        QuotationCount  TEXT ," +
                "        DemoAssignedTo  TEXT ," +
                "        ContractResponsibility  TEXT ," +
                "        ExpectedPrize  TEXT ," +
                "        OrderRecievedBy  TEXT ," +
                "        OrderPONo  TEXT ," +
                "        OrderPOValue  TEXT ," +
                "        DemoReasonCode  TEXT ," +
                "        OutcomeCode  TEXT ," +
                "        EmailCount  TEXT ," +
                "        VisitCount  TEXT ," +
                "        TotalHoursSpent  TEXT ," +
                "        PrebidAssignedTo  TEXT ," +
                "        CallCloseReason  TEXT ," +
                "        CallCloseDetails  TEXT ," +
                "        OrderRegradeReason  TEXT ," +
                "        OrderRegradeReasonDetails  TEXT ," +
                "        QuotationAssignedTo  TEXT ," +
                "        QuotationRequest  TEXT ," +
                "        QuotationStatus  TEXT ," +
                "        CallRatingChangeReason  TEXT ," +
                "        ExpectedValue  TEXT ," +
                "        ExpectedCloserDate  TEXT ," +
                "        OverdueDays  TEXT ," +
                "        DueDays  TEXT ," +
                "        PresalesSupportedBy  TEXT ," +
                "        OrderLostFlag  TEXT ," +
                "        OrderRegretFlag  TEXT ," +
                "        CallCloseWihoutOrderFlag  TEXT ," +
                "        CallLife  TEXT ," +
                "        PresalesSupportStatus  TEXT ," +
                "        OrderRegrteApprover  TEXT ," +
                "        OrderLostApprover  TEXT ," +
                "        CallCloseApprover  TEXT ," +
                "        ContactId  TEXT ," +
                "        SEId  TEXT ," +
                "        BackOfficeExecutiveId  TEXT ," +
                "        PKSuspectId  TEXT ," +
                "        FirmName  TEXT ," +
                "        FKCityId  TEXT ," +
                "        IsProspect  TEXT ," +
                "        IsLead  TEXT ," +
                "        IsOrder  TEXT ," +
                "        MoveToArchieve  TEXT ," +
                "        MoveToArchieveBy  TEXT ," +
                "        FKTerritoryId  TEXT ," +
                "        FKEnqSourceId  TEXT ," +
                "        CompanyURL  TEXT ," +
                "        SourceName   TEXT ," +
                "        CityName  TEXT ," +
                "        TerritoryName  TEXT ," +
                "        LeadGivenBYId  TEXT ," +
                "        ContactPersonDept  TEXT ," +
                "        CustVendorName  TEXT ," +
                "        FirmAlias  TEXT ," +
                "        OpenCalls  TEXT ," +
                "        CloseCalls  TEXT ," +
                "        OrderReceived  TEXT ," +
                "        OrderLost  TEXT ," +
                "        OrderRegrete  TEXT ," +
                "        CallCloseWithoutOrder  TEXT ," +
                "        Source  TEXT ," +
                "        SouceId  TEXT ," +
                "        Alias  TEXT ," +
                "        CallType  TEXT ," +
                "        RescheduleCount  TEXT ," +
                "        CollectMode   TEXT ," +
                "        InstrNo   TEXT ," +
                "        Instrdate   TEXT ," +
                "        BankName   TEXT ," +
                "        Amount  TEXT ," +
                "        AddedBy   TEXT ," +
                "        AddedDt   TEXT ," +
                "        ModifiedBy   TEXT ," +
                "        ModifiedDt    TEXT ," +
                "        AssignedBy  TEXT ," +
                "        AssignTochannel  TEXT ," +
                "        ReportingUserId  TEXT ," +
                "        ReportingName  TEXT ," +
                "        ReportingToEkatmUserId  TEXT ," +
                "        ReportingToEmail  TEXT ," +
                "        FKPlantId TEXT )";


        public static final String CREATE_TABLE_CRM_CALL_PARTIAL_OPP = "CREATE TABLE " + DatabaseHandlers.TABLE_CRM_CALL_PARTIAL_OPP
                + "(FirmName TEXT, " +
                "NextActionDateTime TEXT,LatestRemark TEXT," +
                "AssignedBy TEXT, isPartial TEXT,Mobile TEXT, CallType TEXT ,CallStatus TEXT,ContactName TEXT)";


        public static final String CREATE_TABLE_SHOW_CONTACT = "CREATE TABLE " + DatabaseHandlers.TABLE_SHOW_CONTACT +
                "(PKSuspContactDtlsID TEXT,ContactName TEXT,Designation TEXT,Mobile TEXT,EmailId TEXT,IsPrimaryContact TEXT,FKSuspectId TEXT)";

        //Addded By filter
        public static final String CREATE_TABLE_ADDBY_JSON = "CREATE TABLE " + DatabaseHandlers.TABLE_AddBy + "(UserName TEXT,UserLoginId TEXT)";


        public static final String CREATE_TABLE_SUPPORTTICKET_COUNT = "CREATE TABLE " + DatabaseHandlers.TABLE_SUPPORTTICKET_COUNT +
                "(ConsigneeName TEXT," +
                "CustomerId TEXT," +
                "City TEXT," +
                "SourceId TEXT," +
                "AssignedTkt TEXT," +
                "CompletedTkt TEXT," +
                "OnTimeCompletedTkt TEXT,TodaysOpenTicket TEXT,OpenticketCount,ClosedTicketCount TEXT)";

        //Inventory
        public static final String CREATE_TABLE_EntityType = "CREATE TABLE " + DatabaseHandlers.TABLE_EntityType +
                "(EntityTypeMasterId TEXT,EntityType TEXT)";

        public static final String CREATE_TABLE_COUNTRYLIST = "CREATE TABLE " + DatabaseHandlers.TABLE_Country_INVEN +
                "(PKCountryId TEXT,CountryCode TEXT,CountryName TEXT)";

        public static final String CREATE_TABLE_CITY_INVEN = "CREATE TABLE " + DatabaseHandlers.TABLE_City_INV +
                "(PKCityID TEXT,CityName TEXT,FKCountryId TEXT)";

        public static final String CREATE_TABLE_Currency = "CREATE TABLE " + DatabaseHandlers.TABLE_Currency +
                "(CurrencyMasterId TEXT,CurrCode TEXT)";


        public static final String CREATE_TABLE_DeliveryBoy = "CREATE TABLE " + DatabaseHandlers.TABLE_DELIVERY_BOY +
                "(ConsigneeName TEXT," +
                "InvoiceNo TEXT PRIMARY KEY," +
                "Address TEXT," +
                "Mobile TEXT," +
                "TotNetAmnt TEXT," +
                "Latitude TEXT," +
                "Longitude TEXT," +
                "OrderDt TEXT," +
                "WarehouseDescription TEXT," +
                "PrefDelFrmTime TEXT," +
                "PrefDelToTime TEXT," +
                "Status TEXT)";

        public static final String CREATE_TABLE_MilkRun = "CREATE TABLE " + DatabaseHandlers.TABLE_DELIVERY_MILK_RUN +
                "(ConsigneeName TEXT," +
                "ContactNo TEXT ," +
                "Address TEXT," +
                "Latitude TEXT," +
                "Longitude TEXT," +
                "ShipToMasterId TEXT," +
                "ShipToMasterId1 TEXT," +
                "SequneceNumber TEXT," +
                "tripdetailid TEXT," +
                "DriverContact TEXT," +
                "UserName TEXT," +
                "Status TEXT," +
                "ActivityId TEXT," +
                "Vehicleno TEXT," +
                "TripDetailStatus TEXT," +
                "TripHeaderStatus TEXT," +
                "TripHeaderId TEXT)";


        //Expense Management


        public static final String CREATE_TABLE_EXPENSE = "CREATE TABLE " + DatabaseHandlers.TABLE_EXPENSE +
                "(ExpRecordId TEXT,UserMasterId TEXT,cat_name TEXT,ExpType TEXT," +
                "Amount TEXT,PaymentMode TEXT,TravelMode TEXT," +
                "ExpDate TEXT,attachment TEXT,Remark TEXT,Distance TEXT," +
                "FromLocation TEXT,ToLocation TEXT,VehicleType TEXT,LinkTo TEXT,LinkId TEXT,Path TEXT)";

        public static final String CREATE_TABLE_PRINT_LABEL = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PRINT
                + "(PIDtlId TEXT, PIHdrId TEXT, ItemPlantId TEXT,Location TEXT,Weight TEXT," +
                "ActualQty TEXT,AddedBy TEXT,Printed TEXT,TagNo TEXT,Mode TEXT,Flag TEXT)";
    }

    public class InventoryFactory {

        public static final String CREATE_TABLE_PlantList = "CREATE TABLE " + DatabaseHandlers.TABLE_PlantList +
                "(PlantMasterId TEXT,PlantName TEXT)";

        public static final String CREATE_TABLE_MATERIALITEMLIST = "CREATE TABLE " + DatabaseHandlers.TABLE_MaterialItemList +
                "(ItemMasterId TEXT,ItemCode TEXT,ItemDesc TEXT,ItemPlantId TEXT)";

        public static final String CREATE_TABLE_Suppliername = "CREATE TABLE " + DatabaseHandlers.TABLE_Suppliername +
                "(CustVendorMasterId TEXT,CustVendorCode TEXT,CustVendor TEXT,CustVendorName TEXT,CurrencyMasterId TEXT,CVSType TEXT)";

        public static final String CREATE_TABLE_WAREHOUSE = "CREATE TABLE " + DatabaseHandlers.TABLE_WAREHOUSE +
                "(WareHouseMasterId TEXT,WarehouseDescription TEXT)";

        public static final String CREATE_TABLE_DEPARTMENT = "CREATE TABLE " + DatabaseHandlers.TABLE_Department +
                "(PKDeptMasterId TEXT,DeptCode TEXT,DeptDesc TEXT)";

        public static final String CREATE_TABLE_ADDMATERIALPOORDER = "CREATE TABLE " + DatabaseHandlers.TABLE_AddMaterialPoOrder +
                "(MRSDetailId TEXT,ItemMasterId TEXT,ItemCode TEXT,ItemDesc TEXT,UOMDesc TEXT,UOMMasterId TEXT," +
                "StockQuantity TEXT,ReqQty TEXT,Remark TEXT,lblhdn TEXT,MODetailId TEXT,SupplierId TEXT,RouteName TEXT," +
                "SegmentType TEXT,RouteFrom TEXT,RouteTo TEXT,Address TEXT,WarehouseName TEXT,LocationMasterName TEXT)";

        public static final String CREATE_TABLE_WORK_ORDER = "CREATE TABLE " + DatabaseHandlers.TABLE_WORK_ORDER +
                "(POHeaderId TEXT,PONo TEXT)";


        public static final String CREATE_TABLE_PODETAILS = "CREATE TABLE " + DatabaseHandlers.TABLE_PODETAILS +
                "(MODetailId TEXT,ComponentMasterId TEXT,ItemCode TEXT,ItemDesc TEXT,RouteName TEXT,SegmentType TEXT," +
                "RouteFrom TEXT,RouteTo TEXT,RouteNameDesc TEXT,RoutePointFrom TEXT,RoutePointTo TEXT)";

        public static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + DatabaseHandlers.TABLE_LOCATION +
                "(LocationMasterId TEXT,LocationDesc TEXT)";

        public static final String CREATE_TABLE_POITEMDETAILS = "CREATE TABLE " + DatabaseHandlers.TABLE_PoitemDetails +
                "(ReqdQty TEXT,BalQty TEXT,MODetailId TEXT,ComponentMasterId TEXT,UOMDesc TEXT,UOMMasterId TEXT)";


        public static final String CREATE_TABLE_MRSList = "CREATE TABLE " + DatabaseHandlers.TABLE_MRSList +
                "(MRSHeaderId TEXT,MRSNO TEXT)";

        public static final String CREATE_TABLE_GETALLUsers = "CREATE TABLE " + DatabaseHandlers.TABLE_GETALLUsers +
                "(UserMasterId TEXT,UserLoginId TEXT,UserName TEXT)";


    }

    class PMFactory {

        public static final String CREATE_TABLE_PLANTMASTERVWB = "CREATE TABLE " + DatabaseHandlers.TABLE_PLANTMASTERvwb
                + "(PlantId TEXT)";

    }

    public class TbudsFactory {

        public static final String CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS
                + "(CategoryId TEXT, CategoryName TEXT, SubCategoryName TEXT, ItemName TEXT," +
                "itemmasterid TEXT, SubCategoryId TEXT, ItemImgPath TEXT,ItemPrice TEXT, ItemQty TEXT, isChecked TEXT, itemMRP TEXT," +
                "custVendorname TEXT,TypeFixedPercent TEXT, validfrom TEXT, validto TEXT, DisRate TEXT, NetRate TXET, Freeitemid TEXT," +
                " Freeitemqty TEXT, Minqty TEXT,Discratepercent TEXT, DiscrateMRP TEXT, PurDigit TEXT, CustVendorMasterId TEXT, " +
                "PricelistId TEXT, PricelistRate TEXT)";

        public static final String CREATE_TABLE_MERCHANT_AGAINST_ITEM = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MERCHANT_AGAINST_ITEM
                + "(MerchantId TEXT , MerchantName TEXT, qnty TEXT, minqnty TEXT, offers TEXT,price TEXT," +
                " Product_name TEXT, Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT," +
                "validfrom TEXT, validto TEXT)";

        public static final String CREATE_TABLE_MERCHANTS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MERCHANTS
                + "(MerchantId TEXT , MerchantName TEXT, qnty integer, minqnty integer, offers TEXT,price integer," +
                " Product_name TEXT, Freeitemid TEXT, Freeitemqty  integer,Freeitemname TEXT," +
                "validfrom TEXT, validto TEXT," +
                " Merchant_Name_Two TEXT, MerchantAddress TEXT, MerchantEmail TEXT, MerchantMobile TEXT )";

        public static final String CREATE_TABLE_PLACE_ORDER = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PLACE_ORDER
                + "(Pid INTEGER PRIMARY KEY AUTOINCREMENT, C_V_type TEXT , schedule_date_time TEXT, xml1 TEXT, xml2 TEXT, placeOrderDate TEXT, isUploaded TEXT)";

        public static final String CREATE_TABLE_CART_ITEM = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CART_ITEM
                + "(Cartid INTEGER PRIMARY KEY AUTOINCREMENT, MerchantId TEXT , MerchantName TEXT, qnty TEXT, " +
                "minqnty TEXT, offers TEXT,price TEXT, Product_name TEXT, Amount TEXT,Product_id TEXT " +
                ",Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT, validfrom TEXT, validto TEXT,ItemImgPath TEXT)";

        public static final String CREATE_TABLE_CART_ITEM_VOLUME_DISCOUNT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CART_ITEM_VOLUME_DISCOUNT
                + "(CVid INTEGER PRIMARY KEY AUTOINCREMENT, MerchantId TEXT , MerchantName TEXT, " +
                "minvalue integer, netrate TEXT,freeitemid TEXT,freeitem_name TEXT , freeitemqnty integer," +
                " validfrom TEXT, validto TEXT, CouponId TEXT ,discount TEXT,FKVendorItemMasterId TEXT, FKVendorProductmasterId TEXT )";

        public static final String CREATE_TABLE_MY_ADDRESS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MY_ADDRESS
                + "(UserId TEXT ,  Mobile TEXT," +
                "CustVendType TEXT ,PermanentAddress TEXT, GpsLocationAddress TEXT, Latitude TEXT, Longitude TEXT, " +
                " CurrentAddress  TEXT,OfficeAddress TEXT, type TEXT)";

        public static final String CREATE_TABLE_MY_ORDER_HISTORY = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MY_ORDER_HISTORY
                + "(OrdHtryid INTEGER PRIMARY KEY AUTOINCREMENT, Address TEXT, City TEXT, ConsigneeName TEXT, CustomerMasterId TEXT," +
                " ItemMasterId TEXT, Mobile TEXT, Qty TEXT, Rate TEXT, SODate TEXT, SOHeaderId TEXT," +
                "DODisptch TEXT ,DORcvd TEXT,status TEXT, statusname TEXT, DoAck TEXT, NetAmt Text, ItemDesc TEXT, LineAmt TEXT, merchantid TEXT," +
                "merchantname TEXT, placeOrderDate TEXT, SODetailId TEXT, SOScheduleId TEXT, ShipmentQty TEXT, ClientRecQty TEXT, AppvDt TEXT," +
                " PurDigit TEXT, DOrej TEXT, DispatchNo TEXT, SalesHeaderId TEXT, SalesDtlId TEXT, DispNetAmnt TEXT, ShipStatus TEXT," +
                " OrdRcvdDate, sono TEXT)";/*SalesDtlId*/

        public static final String CREATE_TABLE_BLUETOOTH_ADDRESS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_BLUETOOTH_ADDRESS
                + "(Address TEXT)";

        public static final String CREATE_CUSTOMER_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CUSTOMER_CB
                + "(FullName TEXT, Mobile TEXT" +
                ")";

        public static final String CREATE_TABLE_PRODUCT_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PRODUCT_CB
                + "(MerchantId TEXT , MerchantName TEXT, qnty REAL,unit TEXT, minqnty integer," +
                " offers TEXT,price REAL," +
                " Product_name TEXT,Product_id TEXT,MRP TEXT, Freeitemid TEXT," +
                " Freeitemqty  integer,Freeitemname TEXT," +
                "validfrom TEXT, validto TEXT)";

        public static final String CREATE_TABLE_MARCHANT_ITEM_RUNI = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MARCHANT_ITEM_RUNI
                + "(ItemMasterId TEXT, ItemName TEXT)";

        public static final String CREATE_TABLE_PENDING_BALANCE = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PENDING_BALANCE
                + "( PbId INTEGER PRIMARY KEY AUTOINCREMENT , BillId TEXT,FinalTotalBill TEXT,Received TEXT" +
                ",Balance TEXT,CustomerName TEXT,Cust_mob TEXT,date TEXT)";

        public static final String CREATE_TABLE_GET_REPORTS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_GET_REPORTS
                + "( SId INTEGER PRIMARY KEY AUTOINCREMENT , salesheaderid TEXT, customermobno TEXT," +
                " customerusername TEXT ," +
                " salestotal TEXT, salesdiscounttotal TEXT," +
                "  salesfinaltotal TEXT, salesreceiptamt TEXT, salesbalanceamt TEXT," +
                " salesaddeddt TEXT, salesvendorid TEXT," +
                "  purchaseitemName TEXT, purchasemrp TEXT, purchaseqty TEXT, purchaseunit TEXT, " +
                "purchaseamt TEXT, purchaseVendorid TEXT," +
                "  purchaseShopname TEXT,purchaseAddeddt TEXT," +
                "  purchasetotamt TEXT, purchaseitemid TEXT, salesitemname TEXT, salesqty TEXT, salesunit TEXT, " +
                "  salesrate TEXT, salesdisc TEXT, salesamount TEXT, salesitemid TEXT)";

        public static final String CREATE_TABLE_ITEM_MRP = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ITEM_MRP
                + "(ItemMRP TEXT, Subcategoryid TEXT, categoryid TEXT, " +
                "categoryname TEXT,itemmasterid TEXT,itemname TEXT,subcategoryname TEXT,IsUploaded TEXT)";

        public static final String CREATE_TABLE_BILL_DETAILS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_BILL_DETAILS
                + "(BillId TEXT, xml1 TEXT, xml2 TEXT, isUploaded TEXT, Date TEXT)"; /*, isPrinted TEXT*/

        public static final String CREATE_TABLE_BILL_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_BILL_CB
                + "(BillId INTEGER PRIMARY KEY AUTOINCREMENT,BillPrintNo TEXT, ItemName TEXT,Itemid TEXT, Rate TEXT,MRP TEXT," +
                "Qty TEXT,TaxClass TEXT, cgstLine TEXT, sgstLine TEXT,DiscLineAmt TEXT, ItemDiscount TEXT, Amount TEXT, TotalBill TEXT, Discount TEXT," +
                " DiscOnNetAmtRS TEXT, FinalTotalBill TEXT,Received TEXT ,Balance TEXT, CustomerName TEXT , CustMobno TEXT," +
                " TotalWithDiscountBill TEXT, TaxinRupsTotalBill TEXT,CGSTTotal TEXT, SGSTTotal TEXT, IGSTTotal TEXT,PayableAmt TEXT, " +
                "isUploaded TEXT, isPrinted TEXT, Date TEXT, CustGSTN TEXT, CompanyGSTN TEXT)";

        public static final String CREATE_TABLE_CART_ITEM_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CART_ITEM_CB
                + "(Cartid INTEGER PRIMARY KEY AUTOINCREMENT, Cust_mob TEXT , Cust_Name TEXT, qnty TEXT, " +
                "minqnty TEXT, offers TEXT,price TEXT, Product_name TEXT, Amount TEXT,Product_id TEXT " +
                ",Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT, validfrom TEXT, validto TEXT," +
                " DISCOUNT TEXT,UNIT TEXT,UNITV TEXT)";

        public static final String CREATE_TABLE_GET_MARCHANT_PO = "CREATE TABLE "
                + DatabaseHandlers.TABLE_GET_MARCHANT_PO
                + "(Amt TEXT,ItemId TEXT,Item_Name TEXT,MRP TEXT,Qty TEXT,ShopName TEXT,TotAmt TEXT,Unit TEXT,purchaseID TEXT," +
                "vendorid TEXT)";

        public static final String CREATE_TABLE_ITEM_MRP_Runi = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ITEM_MRP_Runi+ "(Itemid TEXT, Itemname TEXT, NMrpV TEXT, " +
                "OMrpV TEXT,PurchaseMRP TEXT, QtyV TEXT,PurchaseUnit TEXT, UnitV TEXT, pkpurchaseid TEXT, PurchaseQty TEXT, " +
                "IsUploaded TEXT)";

        public static final String CREATE_TABLE_MARCHANT_ITEM = "CREATE TABLE "
                + DatabaseHandlers.TABLE_MARCHANT_ITEM
                + "(FKCategoryId TEXT, FKitemmasterid TEXT, FKsubcategoryid TEXT," +
                " PKVendoritemRelation TEXT,categoryname TEXT,itemname TEXT," +
                "subcategoryname TEXT,vendorid TEXT)";

        public static final String CREATE_TABLE_TRADE_DISCOUNT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_TRADE_DISCOUNT
                + "(IsUploaded TEXT,FKVendorProductmasterId TEXT, validfrom TEXT, validto TEXT, FKVendorId TEXT,FKVendorItemMasterId TEXT,VendorItemname TEXT,TypeFixedPercent TEXT,MRP TEXT,DisRate TEXT,NetRate TEXT,CouponId TEXT,Freeitemid TEXT,Freeitemqty TEXT,Minqty TEXT,Discratepercent TEXT,DiscrateMRP TEXT,Minvalue TEXT)";

        public static final String CREATE_TABLE_VOLUME_DISCOUNT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_VOLUME_DISCOUNT
                + "(Discratepercent1 TEXT,IsUploaded TEXT,FKVendorProductmasterId TEXT, FKVendorId TEXT, FKVendorItemMasterId TEXT, VendorItemname TEXT,TypeFixedPercent TEXT,MRP TEXT,DisRate TEXT,NetRate TEXT,CouponId TEXT,Freeitemid TEXT,Freeitemqty TEXT,Minqty TEXT,ValidFrom TEXT,Validto TEXT,Discratepercent TEXT,DiscrateMRP TEXT,Minvalue TEXT,disrateMRP TEXT,CouponCode TEXT,DisratePercentCoupon TEXT,DisrateMRPCoupon TEXT,ValidFromCoupon TEXT,ValidToCoupon TEXT,DisTypeCoupon TEXT,Couponqty TEXT,Couponitemasterid TEXT)";

        public static final String CREATE_TABLE_PURCHASE_ITEM_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PURCHASE_ITEM_CB
                + "(Pid INTEGER PRIMARY KEY AUTOINCREMENT, vendor TEXT ,qnty TEXT, " +
                "price TEXT, Product_name TEXT, Amount TEXT,Product_id TEXT, " +
                " DISCOUNT TEXT,UNIT TEXT)";

        public static final String CREATE_TABLE_PURCHASE_FINAL_ITEM_CB = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PURCHASE_FINAL_ITEM_CB
                + "(Pid INTEGER PRIMARY KEY AUTOINCREMENT, vendor TEXT ,qnty TEXT, " +
                "price TEXT, Product_name TEXT, Amount_pcb TEXT,Product_id TEXT, " +
                " DISCOUNT TEXT,UNIT TEXT,SubTotal TEXT, TotalDiscount TEXT, FinalTotal TEXT," +
                " isUploaded TEXT,Date TEXT)";

        public static final String CREATE_TABLE_ADD_ITMDTLS_FORBILL = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ADD_ITMDTLS_FORBILL
                + "(itmcode TEXT, itmdesc TEXT, qnty TEXT, mrp TEXT, rate TEXT,lineamt TEXT, discount TEXT, taxclass TEXT, taxamtinrups TEXT," +
                " total_incl_taxanddisc TEXT, taxinprcntg TEXT, totwithdisc TEXT, isbilluploaded TEXT, isdiscinrupees TEXT, discamt TEXT)";

        public static final String CREATE_TABLE_PROSPECT_VALIDATIONS_SALES = "CREATE TABLE "
                + DatabaseHandlers.TABLE_PROSPECT_VALIDATIONS_SALES +
                "(PKFieldID INTEGER PRIMARY KEY AUTOINCREMENT, FKProspectHdrID TEXT, ProspectField TEXT, IsVisible TEXT, IsMandatory TEXT,Caption TEXT,Section TEXT," +
                "FieldType TEXT, PKUserId TEXT)";

        public static final String CREATE_TABLE_DELIVERY_AGENTS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_DELIVERY_AGENTS +
                "(DelAgentID TEXT, DelAgentName TEXT, DelAgentMobile TEXT, DelAgentLocation TEXT, PendingShipments TEXT, EstimatedTime TEXT," +
                "isSelect TEXT)";

        public static final String CREATE_TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_DELAGENTS_ACTIVITY_ASSIGN_LIST_DETAILS +
                "(SeqNo INTEGER PRIMARY KEY AUTOINCREMENT,InvoiceNo TEXT, DelAgentID TEXT, DelAgentLoginID TEXT, DelAgentName TEXT, DelAgentMobile TEXT, DelAgentEmail TEXT, " +
                "AssignActivityDesc TEXT, Sono TEXT, DeliveryDate TEXT, CustomerName TEXT, DeliveryAddress TEXT," +
                "Pref_DeliveryTime_From TEXT, Pref_DeliveryTime_To TEXT,OrderTypeMasterId TEXT, AddedDt TEXT, isActivityCompleted TEXT)";

        public static final String CREATE_TABLE_SHIPMENT_INVOICE = "CREATE TABLE "
                + DatabaseHandlers.TABLE_SHIPMENT_INVOICE +
                "(InvoiceNo TEXT, SONO TEXT, SOheaderId TEXT, OrderTypeMasterId TEXT,SOScheduleId TEXT, ShipmentQty TEXT, LineAmt TEXT, Rate TEXT, TaxAmtdtl TEXT," +
                "TotWithtaxdtl TEXT, DiscAmountDtl TEXT, TotAmntWithDisc TEXT, BaseAmt_final TEXT, TotalTaxAmt_final TEXT, TotTaxWithNet_final TEXT," +
                "TotDisc_final TEXT, NetTotal TEXT, DiscPc TEXT, AddedDt TEXT, expDelDate TEXT, CustName TEXT, CustId TEXT, ShipToMasterId TEXT," +
                "DelvAddress TEXT, isActivityAssigned TEXT)";

        public static final String CREATE_TABLE_CONSIGNEES = "CREATE TABLE "+ DatabaseHandlers.TABLE_CONSIGNEES +
                "(CustVendorMasterId TEXT, CustVendorName TEXT, Email TEXT, Mobile TEXT, Address TEXT,AddedBy TEXT)";

        public static final String CREATE_TABLE_STATES = "CREATE TABLE "
                + DatabaseHandlers.TABLE_STATES_SALES
                + "(state_id TEXT, state_name TEXT)";

        public static final String CREATE_TABLE_CITIES = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CITY_SALES
                + "(city_id TEXT, city_name TEXT, stateId TEXT)";

        public static final String CREATE_TABLE_DISTRICT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_DISTRICT
                + "(PKDistrictId TEXT,DistrictNo TEXT,DistrictDesc TEXT," +
                " FKStateId TEXT," +
                " IsDeleted TEXT ,AddedBy TEXT,AddedDt TEXT, ModifiedBy TEXT)";

        public static final String CREATE_TABLE_TALUKA_ENTITY = "CREATE TABLE "
                + DatabaseHandlers.TABLE_TALUKA_ENTITY
                + "(PKTalukaId TEXT, TalukaNo TEXT, TalukaDesc TEXT, FKDistrictId TEXT, IsDeleted TEXT, AddedBy TEXT, AddedDt TEXT," +
                "ModifiedBy TEXT, ModifiedDt TEXT)";

        public static final String CREATE_TABLE_STATE_ENTITY = "CREATE TABLE " + DatabaseHandlers.TABLE_STATE_ENTITY +
                "(PKStateId TEXT,StateDesc TEXT,StateNo TEXT,FKCountryId TEXT,IsDeleted TEXT,AddedBy TEXT,AddedDt TEXT," +
                "ModifiedBy TEXT,ModifiedDt TEXT)";

        public static final String CREATE_TABLE_CITY_ENTITY  = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CITY_ENTITY
                +"(PKCityID TEXT,CityName TEXT,FKTalukaId TEXT,FKDistrictId TEXT,FKStateId TEXT,FKCountryId TEXT," +
                "PinCode TEXT,FKTerritoryId TEXT,AddedBy TEXT,AddedDt TEXT,ModifiedBy TEXT,ModifiedDt TEXT,IsDeleted TEXT)";

        public static final String CREATE_TABLE_ENTITY_TYPE = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ENTITY_TYPE
                +"(EntityTypeMasterId TEXT, EntityTypeCode TEXT, EntityType TEXT, IsDeleted TEXT, AddedBy TEXT, AddedDt TEXT, ModifiedBy TEXT, "
                + "ModifiedDt TEXT, PriceListClassificationId TEXT, Entity TEXT, EntityAs TEXT, IsPartner TEXT, AccountMasterId TEXT)";

        public static final String CREATE_TABLE_SALES_PRICELIST = "CREATE TABLE "
                + DatabaseHandlers.TABLE_SALES_PRICELIST
                +"(PriceLstDesc TEXT, PriceListHdrID TEXT)";

        public static final String CREATE_TABLE_SHIPTO_DETAILS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_SHIPTO_DETAILS
                +"(RowID TEXT, Action TEXT, ConsigneeName TEXT, ContactPerson TEXT, Address TEXT, City TEXT, Phone TEXT, Fax TEXT, Mobile TEXT," +
                " Country TEXT, State TEXT, ShipToMasterId TEXT, Latitude TEXT, Longitude TEXT, Distance TEXT, RouteMasterId TEXT, CityName TEXT," +
                " CountryName TEXT, StateName TEXT, GeoLocation TEXT, GSTCode TEXT, GSTState TEXT, GSTStateName TEXT, Rating TEXT, TANNo TEXT," +
                " TANNoName TEXT, PAN TEXT, EmailId TEXT, isBlocked TEXT, TAN_GSTIN_Number TEXT)";

        public static final String CREATE_TABLE_ADD_ITEMS_COUNTERBILL = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ADD_ITEMS_COUNTERBILL
                + "(ItemPlantId TEXT, ItemCode TEXT, ItemDesc TEXT, ItemMRP TEXT, TAXClass TEXT, TaxAmount TEXT, DiscountAmount TEXT)";

        public static final String CREATE_TABLE_TAXCLASS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_TAXCLASS
                + "(TaxClassMasterId TEXT, TaxClassCode TEXT, TaxClassDesc TEXT)";

        public static final String CREATE_TABLE_COMPANY_DETAILS = "CREATE TABLE "
                + DatabaseHandlers.TABLE_COMPANY_DETAILS
                + "(Cid TEXT, Cname TEXT, Address TEXT)";

        public static final String CREATE_TABLE_CONFIGURATION = "CREATE TABLE "
                +DatabaseHandlers.TABLE_CONFIGURATION
                +"(ConfigurationDetailId TEXT,Configuration TEXT,ConfigurationName TEXT)";

        public static final String CREATE_TABLE_CHARGE = "CREATE TABLE "
                +DatabaseHandlers.TABLE_CHARGE
                +"(ChargeMasterId TEXT,ChargeCode TEXT,ChargeDesc TEXT)";

        public static final String CREATE_TABLE_PAYMENT_TERMS = "CREATE TABLE "
                +DatabaseHandlers.TABLE_PAYMENT_TERMS
                +"(TermsCode TEXT,TermsDescription TEXT,PymtSettTermMasterId TEXT,IsDeleted TEXT,CreditDays TEXT,BaseDate TEXT)";

        public static final String CREATE_TABLE_COMMISSION = "CREATE TABLE "
                +DatabaseHandlers.TABLE_COMMISSION
                +"(CommTypeId TEXT,TypeCode TEXT,TypeDesc TEXT,VouMasterId TEXT)";

        public static final String CREATE_TABLE_CATEGORY_REIMB = "CREATE TABLE "
                +DatabaseHandlers.TABLE_CATEGORY_REIMB
                +"(PKUserCategoryId TEXT,CategoryDesc TEXT)";

        public static final String CREATE_TABLE_WARRANTY = "CREATE TABLE "
                +DatabaseHandlers.TABLE_WARRANTY
                +"(WarrantyMasterId TEXT,WarrantyDesc TEXT,WarrantyCode TEXT)";

        public static final String CREATE_TABLE_UOM_new = "CREATE TABLE "+DatabaseHandlers.TABLE_UOM_new
                + "(UOMMasterId TEXT,UOMCode TEXT,UOMDesc TEXT,UOMDigit TEXT)";

        public static final String CREATE_TABLE_SOHistory = "CREATE TABLE "+DatabaseHandlers.TABLE_SOHistory
                +"(SOHeaderId TEXT,SONo TEXT,SODate TEXT,CustomerMasterId TEXT,ConsigneeName TEXT,NetAmt TEXT)";

        public static final String CREATE_TABLE_MY_ORDER_ACCEPTANCE = "CREATE TABLE " + DatabaseHandlers.TABLE_MY_ORDER_ACCEPTANCE
                + "(sono TEXT,SOHeaderId TEXT,SODetailId TEXT,ConsigneeName TEXT,CustomerMasterId TEXT,ItemDesc TEXT,ItemMasterId TEXT,Qty TEXT," +
                "OrgQty TEXT,Rate TEXT,LineAmt TEXT,TotalOrderValue TEXT,SODate TEXT,DoAck TEXT,Range TEXT,MRP TEXT,distance TEXT," +
                "UOMDigit TEXT,UOMCode TEXT,DeliveryTerms TEXT,Mobile TEXT,Brand TEXT,Content TEXT,ContentUOM TEXT,SellingUOM TEXT,PackOfQty TEXT," +
                "FreeAboveAmt TEXT,FreeDelyMaxDist TEXT,MinDelyKg TEXT,MinDelyKm TEXT,ExprDelyWithinMin TEXT,ExpressDelyChg TEXT)";

        public static final String CREATE_TABLE_SOHEADER = "CREATE TABLE "+DatabaseHandlers.TABLE_SOHEADER
                + "(SOHeaderId TEXT,SONo TEXT,SODate TEXT,CustomerMasterId TEXT,ShipToMasterId TEXT,ConsigneeName TEXT,Address TEXT,City TEXT,"+
                "State TEXT,Country TEXT,Phone TEXT,Fax TEXT,Mobile TEXT,ContactPerson TEXT,CustOrderPONo TEXT,QuotationNo TEXT,OrderTypeMasterId TEXT,"+
                "CurrencyMasterId TEXT,DeliveryTerms TEXT,CreditTerm TEXT,CreditDays TEXT,TotalOrderValue TEXT,TotalOrderQty TEXT,CreationLevel TEXT,"+
                "UserLevel TEXT,AddedBy TEXT,AddedDt TEXT,SOHeaderStatus TEXT,PlantMasterId TEXT,CustOrderPODt TEXT,SOtype TEXT,DeliveryId TEXT,"+
                "NotifyId TEXT,EventFrmDt TEXT,EventToDt TEXT,TotalGrossAmt TEXT,BasicAmt TEXT,TotTaxAmt TEXT,TotChargeAmt TEXT,TotDiscAmt TEXT,"+
                "NetAmt TEXT,BillToId TEXT,PriceListHdrID TEXT,PriceListDtlID TEXT,ExRate TEXT,BasicAmtFC TEXT,TotTaxAmtFC TEXT,TotChargeAmtFC TEXT,"+
                "TotDiscAmtFC TEXT,NetAmtFC TEXT,OrderReceivedDate TEXT,ProjectId TEXT,SOContractId TEXT,DOBkd TEXT,DODisptch TEXT,DORcvd TEXT,"+
                "DORej TEXT,DOack TEXT,PrefDelFrmTime TEXT,PrefDelToTime TEXT,WareHouseMasterId TEXT,Latitude TEXT,Longitude TEXT,GracePeriod TEXT,"+
                "SecurityDepAmt TEXT,SecurityDepMode TEXT,PerfGarunteePer TEXT,PerfGarunteeMode TEXT,RoadPermit TEXT,LiqDamges TEXT,PreDispatch TEXT,"+
                "InspectionChgs TEXT)";

        public static final String CREATE_TABLE_SODETAIL = "CREATE TABLE "+DatabaseHandlers.TABLE_SODETAIL
                + "(SOHeaderId TEXT,SODetailId TEXT,SeqNo TEXT,ItemMasterId TEXT,Qty TEXT,Rate TEXT,PlantId TEXT,WareHouseId TEXT,"+
                "ShipToId TEXT,ConsigneeName TEXT,Mobile TEXT,DeliveryDt TEXT,DeliveryTerms TEXT,CreationLevel TEXT,UserLevel TEXT,AddedBy TEXT,"+
                "AddedDt TEXT,SODetailStatus TEXT,UOMMasterId TEXT,TaxClassMasterId TEXT,LineAmt TEXT,FLineAmt TEXT,"+
                "LineTaxes TEXT,FLineTaxes TEXT,LineCharges TEXT,FLineCharges TEXT,LineTotal TEXT,FLineTotal TEXT,Remark TEXT,"+
                "DiscPC TEXT,DiscAmount TEXT,QuotationDtlId TEXT,FDiscAmount TEXT,OrgQty TEXT,RetQty TEXT,Startdt TEXT,Enddt TEXT,"+
                "WarrantyCode TEXT,SegmentId TEXT,RouteFrom TEXT,RouteTo TEXT,RecStartDate TEXT,RecEndDate TEXT,RecurDaysCount TEXT,"+
                "RecurWeeksCount TEXT,IsSunday TEXT,IsMonday TEXT,IsTuesday TEXT,IsWednesday TEXT,IsThursday TEXT,IsFriday TEXT,"+
                "IsSaturday TEXT,EveryMonthCount TEXT,MonthlyDayNo TEXT,MonthlyMonth TEXT,MonthlyWeek TEXT,MonthlyDay TEXT,RecurYearCount TEXT,"+
                "YearlyMonthName TEXT,YearlyWeek TEXT,YearlyDay TEXT,YearlyMonth TEXT,TypeOfPeriod TEXT,Occurrences TEXT,IsNoEndDate TEXT,"+
                "IsProRata TEXT,ProFigure TEXT,ProUnit TEXT,BillingCategoryId TEXT,PeriodicEndDate TEXT,merchantid TEXT,merchantname TEXT,"+
                "AllowPartShipment TEXT,PriceListHdrId TEXT,SalesFamilyHdrId TEXT,BQT_QuotationHeaderId TEXT,ContractHdrId TEXT)";

        public static final String CREATE_TABLE_MODEL = "CREATE TABLE "+DatabaseHandlers.TABLE_MODEL
                +"(ConfigurationDetailId TEXT,Configuration TEXT,Type TEXT)";

        public static final String CREATE_TABLE_SALESFAMILY = "CREATE TABLE "+DatabaseHandlers.TABLE_SALES_FAMILY
                +"(FamilyId TEXT,FamilyCode TEXT,FamilyDesc TEXT)";

        public static final String CREATE_TABLE_PRICELIST= "CREATE TABLE "+DatabaseHandlers.TABLE_PRICELIST
                +"(PListHDRId TEXT,PListCode TEXT,PListDesc TEXT)";

        /*new order booking tables*/

        public static final String CREATE_TABLE_BUS_SEGMENT = "CREATE TABLE "
                + DatabaseHandlers.TABLE_BUS_SEGMENT
                + "(PKBusiSegmentID TEXT,SegmentCode TEXT,BusiImgPath TEXT,MerchAliasName TEXT,SegmentDescription TEXT)";

        public static final String CREATE_TABLE_FAMILY_MASTERDATA = "CREATE TABLE "
                + DatabaseHandlers.TABLE_FAMILY_MASTERDATA
                + "(CategoryId TEXT,CategoryName TEXT,SubCategoryName TEXT,SubCategoryId TEXT,Cat_flag TEXT,SubCat_flag TEXT," +
                "CatImgPath TEXT,SubCatImgPath TEXT,SubCatCount TEXT,ItemCount TEXT)";

        public static final String CREATE_TABLE_ALL_CAT_SUBCAT_ITEMS_new = "CREATE TABLE "
                + DatabaseHandlers.TABLE_ALL_CAT_SUBCAT_ITEMS_new
                + "(CategoryId TEXT, CategoryName TEXT, SubCategoryName TEXT, ItemName TEXT," +
                "itemmasterid TEXT, SubCategoryId TEXT, ItemImgPath TEXT,ItemPrice TEXT, ItemQty TEXT, isChecked TEXT, itemMRP TEXT," +
                "custVendorname TEXT,TypeFixedPercent TEXT, validfrom TEXT, validto TEXT, DisRate TEXT, NetRate TXET, Freeitemid TEXT," +
                " Freeitemqty TEXT, Minqty TEXT,Discratepercent TEXT, DiscrateMRP TEXT, PurDigit TEXT, CustVendorMasterId TEXT, " +
                "PricelistId TEXT, PricelistRate TEXT,Cat_flag TEXT,SubCat_flag TEXT,UOMCode TEXT,Range TEXT,MinOrdQty TEXT,MaxOrdQty TEXT," +
                "Distance TEXT,OutOfStock TEXT,Brand TEXT,Content TEXT,ContentUOM TEXT,SellingUOM TEXT,CatImgPath TEXT, SubCatImgPath TEXT," +
                "PackOfQty TEXT,FreeAboveAmt TEXT,FreeDelyMaxDist TEXT,MinDelyKg TEXT,MinDelyKm TEXT,ExprDelyWithinMin TEXT,ExpressDelyChg TEXT," +
                "Open_slots TEXT,OpenTime1 TEXT,CloseTime1 TEXT,OpenTime2 TEXT,CloseTime2 TEXT,IsDelivery TEXT,UPI TEXT)";

        public static final String CREATE_TABLE_CART_ITEM_new = "CREATE TABLE "
                + DatabaseHandlers.TABLE_CART_ITEM_new
                + "(Cartid INTEGER PRIMARY KEY AUTOINCREMENT, MerchantId TEXT , MerchantName TEXT, qnty TEXT, " +
                "minqnty TEXT, offers TEXT,price TEXT, Product_name TEXT, Amount TEXT,Product_id TEXT " +
                ",Freeitemid TEXT, Freeitemqty  TEXT,Freeitemname TEXT, validfrom TEXT, validto TEXT,ItemImgPath TEXT,CategoryId TEXT," +
                "CategoryName TEXT,SubCategoryId TEXT,SubCategoryName TEXT,MRP TEXT,MaxOrdQty TEXT,Range TEXT,OutOfStock TEXT," +
                "Distance TEXT,UomDigit TEXT,UOMCode TEXT,Brand TEXT,Content TEXT,ContentUOM TEXT,SellingUOM TEXT,PackOfQty TEXT," +
                "FreeAboveAmt TEXT,FreeDelyMaxDist TEXT,MinDelyKg TEXT,MinDelyKm TEXT,ExprDelyWithinMin TEXT,ExpressDelyChg TEXT," +
                "Open_slots TEXT,OpenTime1 TEXT,CloseTime1 TEXT,OpenTime2 TEXT,CloseTime2 TEXT,AppliedDelCharges TEXT,IsDelivery TEXT,UPI TEXT)";

        public static final String CREATE_TABLE_PENDING_DELIVERY = "CREATE TABLE "+DatabaseHandlers.TABLE_PENDING_DELIVERY
                +"(InvoiceNo TEXT,InvoiceDt TEXT,SODate TEXT,ShipToMasterId TEXT,OrderTypeMasterId TEXT,SONo TEXT,ConsigneeName TEXT,Address TEXT," +
                "PrefDelFrmTime TEXT,PrefDelToTime TEXT,Mobile TEXT,Latitude TEXT,Longitude TEXT,deliveryterms TEXT,PaymentStatus TEXT," +
                "TransactionId TEXT,TransactionDate TEXT,PaymentMode TEXT,AmountStatus TEXT,ItemDesc TEXT,UOMCode TEXT,Brand TEXT," +
                "Content TEXT,Qty TEXT,Rate TEXT,NetAmt TEXT)";

    }

    class PIGenerationFactory {
        public static final String CREATE_TABLE_LOCATION_PI = "CREATE TABLE " + DatabaseHandlers.TABLE_LOCATION_PI
                + "(LocationMasterId TEXT,LocationCode TEXT,LocationDesc TEXT,WarehouseMasterId TEXT,AddedBy TEXT)";

        public static final String CREATE_TABLE_PI_GENERATION = "CREATE TABLE " + DatabaseHandlers.TABLE_PI_GENERATION
                + "(PIDtlId TEXT,PIHdrId TEXT,ItemPlantId TEXT,LocationMasterID TEXT,Weight TEXT,ActualQty TEXT,AddedBy TEXT,Printed TEXT,"
                + "TagNo TEXT,Mode TEXT,CountedBy TEXT,TAGDescription TEXT,VerifyBy TEXT,ItemCode TEXT,ItemDesc TEXT,LocationCode TEXT)";

        public static final String CREATE_TABLE_UOM = "CREATE TABLE " + DatabaseHandlers.TABLE_UOM
                + "(UOMMasterId TEXT, UOMCode TEXT)";


    }

    class AlfaLavalFactory {
        public static final String CREATE_TABLE_PUTAWAY_USER = "CREATE TABLE " + DatabaseHandlers.TABLE_PUTAWAY_USER
                + "(GRNNo TEXT," + "GRNHeaderId TEXT,IsPacketapl TEXT)";

        public static final String CREATE_TABLE_GETPUTAWAY = "CREATE TABLE " + DatabaseHandlers.TABLE_PUTAWAY
                + "(PutAwaysr INTEGER PRIMARY KEY AUTOINCREMENT,GRNNumber TEXT," +
                "GRNHeader TEXT,SuggPutAwayId TEXT,GRNDetailId TEXT,LocationMasterId TEXT," +
                "ItemCode INTEGER,ItemDesc INTEGER,LocationCode TEXT,PutAwayQty TEXT," +
                "DoneFlag TEXT,InsertUpadate TEXT)";

        public static final String CREATE_TABLE_GetLoactionMaster = "CREATE TABLE " + DatabaseHandlers.TABLE_LOCATION_MASTER
                + "(LocationMasterId TEXT,LocationCode TEXT,LocationDesc INTEGER,WarehouseDescription TEXT,PlantName TEXT)";


        public static final String CREATE_TABLE_GetTablePutawayPacketDetail =
                "CREATE TABLE " + DatabaseHandlers.TABLE_PUTAWAY_PACKET_DETAIL
                        + "(ItemDesc TEXT,ItemCode TEXT," +
                        "ItemPlantId TEXT," +
                        "PacketMasterId TEXT," +
                        "GRNDetailId TEXT," +
                        "GRNHeaderId TEXT," +
                        "BalQty TEXT," +
                        "PacketNo TEXT," +
                        "LocationMasterId TEXT," +
                        "LocationCode TEXT," +
                        "LocationDesc TEXT,DoneFlag TEXT)";

        public static final String CREATE_TABLE_MRS =
                "CREATE TABLE " + DatabaseHandlers.TABLE_MRS
                        + "(MRSHeaderId TEXT,MRSNO TEXT," +
                        "MRSDate TEXT," +
                        "PlantMasterId TEXT," +
                        "MRSType TEXT," +
                        "StatusId TEXT," +
                        "WareHouseMasterId TEXT," +
                        "MONo TEXT," +
                        "WarehouseCode TEXT)";

        public static final String CREATE_TABLE_MRS_DETAIL =
                "CREATE TABLE " + DatabaseHandlers.TABLE_MRS_DETAIL
                        + "(MRSDetailId TEXT,MRSHeaderId TEXT," +
                        "ItemMasterId TEXT," +
                        "ItemCode TEXT," +
                        "ItemDesc TEXT," +
                        "ReqQty TEXT," +
                        "IssuedQty TEXT," +
                        "UOMCode TEXT," +
                        "MODetailId TEXT," +
                        "MONo TEXT," +
                        "Flag TEXT," +
                        "HeatNo TEXT)";
        public static final String CREATE_TABLE_BOX =
                "CREATE TABLE " + DatabaseHandlers.TABLE_BOX
                        + "(BoxTypeMasterId TEXT,BoxCode TEXT," +
                        "BoxName TEXT)";


        public static final String CREATE_TABLE_SECONDARY_BOX =
                "CREATE TABLE " + DatabaseHandlers.TABLE_SECONDARY_BOX
                        + "(Pack_OrdHdrId TEXT,Pack_OrdDtlId TEXT," +
                        "ItemCode TEXT,ItemDesc TEXT," +
                        "WareHouseMasterId TEXT,WarehouseCode TEXT," +
                        "Pack_OrdStatus TEXT,QtyToPack TEXT," +
                        "QtyPacked TEXT,NoofHUToPack TEXT," +
                        "Pick_ListHdrId TEXT,Pick_ListDtlId TEXT," +
                        "ItemMasterId TEXT,SoScheduleId TEXT," +
                        "NoofHUPacked TEXT,Flag TEXT,PackOrderNo TEXT)";

        public static final String CREATE_TABLE_CARTAN_PICKLIST =
                "CREATE TABLE " + DatabaseHandlers.TABLE_CARTAN_PICKLIST
                        + "(Pick_ListHdrId TEXT,PickListNo TEXT," +
                        "Pick_ListDtlId TEXT,SoScheduleId TEXT," +
                        "ItemMasterId TEXT,QtyPicked TEXT," +
                        "QtyToPick TEXT,StockDetailsId TEXT," +
                        "Flag TEXT," +
                        "ItemCode TEXT,ItemDesc TEXT," +
                        "QtyPickPosted TEXT,DONumber TEXT,LocationCode Text,BatchNo TEXT)";

        public static final String CREATE_TABLE_GRNPACKET =
                "CREATE TABLE " + DatabaseHandlers.TABLE_GRN_PACKET
                        + "(PacketNo TEXT)";

        public static final String CREATE_TABLE_ITEM_PICKLIST =
                "CREATE TABLE " + DatabaseHandlers.TABLE_ITEM_PICKLIST
                        + "(Pick_ListHdrId TEXT,PickListNo TEXT, "+
                        "ItemMasterId TEXT,QtyPicked TEXT," +
                        "QtyToPick TEXT," +
                        "Flag TEXT," +
                        "ItemCode TEXT,ItemDesc TEXT," +
                        "DONumber TEXT)";

        public static final String CREATE_TABLE_GRNNOPACKET =
                "CREATE TABLE " + DatabaseHandlers.TABLE_GRNNO_PACKET
                        + "(PacketNo TEXT,GRNHeaderId TEXT)";

        public static final String CREATE_TABLE_GRN_POST_ITEM = "CREATE TABLE " +DatabaseHandlers.TABLE_GRN_POST_ITEM
                + "(GRNNo TEXT," +
                "GRNHeaderId TEXT," +
                "GRNDetailId TEXT," +
                "ItemMasterId TEXT," +
                "ItemCode TEXT," +
                "Quantity TEXT," +
                "RejQty TEXT," +
                "InvoiceNo TEXT," +
                "CustomerName TEXT," +
                "ChallanQty TEXT)";

        public static final String CREATE_TABLE_GRN_POST = "CREATE TABLE " + DatabaseHandlers.TABLE_GRN_POST
                + "(GRNNo TEXT," +
                "GRNHeaderId TEXT," +
                "PacketNo TEXT," +
                "ItemCode TEXT," +
                "Quantity TEXT," +
                "ChallanQty TEXT," +
                "UOM TEXT)";

    }



    }



