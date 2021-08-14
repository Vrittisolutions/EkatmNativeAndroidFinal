package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by 300151 on 10/7/2016.
 */
public class ActivityBean implements Serializable {
    String id ;
    String ActivityName, Assigned_By, ActivityId, ConsigneeName="", ActivityCode, Status, TotalHoursBooked,

             FormatStDt, FormatEndDt, ProjectName, PriorityIndex, HoursRequired, StartDate, EndDate, SourceType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndDateAct() {
        return EndDateAct;
    }

    public void setEndDateAct(String endDateAct) {
        EndDateAct = endDateAct;
    }

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    String IssuedToName = " ",EndDateAct="";
    String rowNo;

    String ProjectID,PAllowUsrTimeSlotHrs,ContMob;

    String  IsChargable ,
            AssignedById,
            SubActCount ,
            SubActStaus ,
            ExpectedCompleteDate,
            ExpectedComplete_Date ,
            ModifiedBy ,
            Modified_By,
            StartDt ,
            EndDt ,

            IsActivityMandatory ,
            IsDelayedActivityAllowed ,
            Cd ,
            UnitId ,
            PKModuleMastId ,
            PriorityName ,
            Colour ,
            AddedDt ,
            UserMasterId ,
            ModifiedDt ,
            AssignedById1 ,
            IsDeleted ,
            IsApproved ,
            IsChargable1 ,
            ActivityTypeId ,
            IsApproval ,
            AttachmentName ,
            AttachmentContent ,
            ModifiedDt1 ,
            SourceId ,
            UnitName ,
            UnitDesc ,
            ModuleName ,
            ActivityName1 ,
            Remarks ,
            ProjectCode,
            UserName ,
            ExpectedComplete_Date1 ,
            DeptDesc ,
            DeptMasterId ,
            CompletionIntimate ,
            ModifiedBy1 ,
            ReassignedBy ,
            ReassignedDt ,
            ActualCompletionDate ,
            WarrantyCode ,
            TicketCategory ,
            IsEndTime ,
            IsCompActPresent ,
            CompletionActId ,
            TktCustReportedBy ,
            TktCustApprovedBy ,
            IsSubActivity ,
            ParentActId ,
            ActivityTypeName ,
            CompActName,Notification ;


    public String getContMob() {
        return ContMob;
    }

    public void setContMob(String contMob) {
        ContMob = contMob;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getTotalHoursBooked() {
        return TotalHoursBooked;
    }

    public void setTotalHoursBooked(String totalHoursBooked) {
        TotalHoursBooked = totalHoursBooked;
    }




    public String getPAllowUsrTimeSlotHrs() {
        return PAllowUsrTimeSlotHrs;
    }

    public void setPAllowUsrTimeSlotHrs(String PAllowUsrTimeSlotHrs) {
        this.PAllowUsrTimeSlotHrs = PAllowUsrTimeSlotHrs;
    }

    public String getIssuedToName() {
        return IssuedToName;
    }

    public void setIssuedToName(String issuedToName) {
        IssuedToName = issuedToName;
    }

    public String getAssigned_By() {
        return Assigned_By;
    }

    public void setAssigned_By(String assigned_By) {
        Assigned_By = assigned_By;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getActivityCode() {
        return ActivityCode;
    }

    public void setActivityCode(String activityCode) {
        ActivityCode = activityCode;
    }



    public String getActivityId() {
        return ActivityId;
    }

    public void setActivityId(String activityId) {
        ActivityId = activityId;
    }

    public String getFormatStDt() {
        return FormatStDt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setFormatStDt(String formatStDt) {
        FormatStDt = formatStDt;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getSourceType() {
        return SourceType;
    }

    public void setSourceType(String sourceType) {
        SourceType = sourceType;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getFormatEndDt() {
        return FormatEndDt;
    }

    public void setFormatEndDt(String formatEndDt) {
        FormatEndDt = formatEndDt;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getPriorityIndex() {
        return PriorityIndex;
    }

    public void setPriorityIndex(String priorityIndex) {
        PriorityIndex = priorityIndex;
    }

    public String getHoursRequired() {
        return HoursRequired;
    }

    public void setHoursRequired(String hoursRequired) {
        HoursRequired = hoursRequired;
    }

    public String getIsChargable() {
        return IsChargable;
    }

    public void setIsChargable(String isChargable) {
        IsChargable = isChargable;
    }

    public String getAssignedById() {
        return AssignedById;
    }

    public void setAssignedById(String assignedById) {
        AssignedById = assignedById;
    }

    public String getSubActCount() {
        return SubActCount;
    }

    public void setSubActCount(String subActCount) {
        SubActCount = subActCount;
    }

    public String getSubActStaus() {
        return SubActStaus;
    }

    public void setSubActStaus(String subActStaus) {
        SubActStaus = subActStaus;
    }

    public String getExpectedCompleteDate() {
        return ExpectedCompleteDate;
    }

    public void setExpectedCompleteDate(String expectedCompleteDate) {
        ExpectedCompleteDate = expectedCompleteDate;
    }

    public String getExpectedComplete_Date() {
        return ExpectedComplete_Date;
    }

    public void setExpectedComplete_Date(String expectedComplete_Date) {
        ExpectedComplete_Date = expectedComplete_Date;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModified_By() {
        return Modified_By;
    }

    public void setModified_By(String modified_By) {
        Modified_By = modified_By;
    }

    public String getStartDt() {
        return StartDt;
    }

    public void setStartDt(String startDt) {
        StartDt = startDt;
    }

    public String getEndDt() {
        return EndDt;
    }

    public void setEndDt(String endDt) {
        EndDt = endDt;
    }



    public String getIsActivityMandatory() {
        return IsActivityMandatory;
    }

    public void setIsActivityMandatory(String isActivityMandatory) {
        IsActivityMandatory = isActivityMandatory;
    }

    public String getIsDelayedActivityAllowed() {
        return IsDelayedActivityAllowed;
    }

    public void setIsDelayedActivityAllowed(String isDelayedActivityAllowed) {
        IsDelayedActivityAllowed = isDelayedActivityAllowed;
    }

    public String getCd() {
        return Cd;
    }

    public void setCd(String cd) {
        Cd = cd;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getPKModuleMastId() {
        return PKModuleMastId;
    }

    public void setPKModuleMastId(String PKModuleMastId) {
        this.PKModuleMastId = PKModuleMastId;
    }

    public String getPriorityName() {
        return PriorityName;
    }

    public void setPriorityName(String priorityName) {
        PriorityName = priorityName;
    }

    public String getColour() {
        return Colour;
    }

    public void setColour(String colour) {
        Colour = colour;
    }

    public String getAddedDt() {
        return AddedDt;
    }

    public void setAddedDt(String addedDt) {
        AddedDt = addedDt;
    }

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getModifiedDt() {
        return ModifiedDt;
    }

    public void setModifiedDt(String modifiedDt) {
        ModifiedDt = modifiedDt;
    }

    public String getAssignedById1() {
        return AssignedById1;
    }

    public void setAssignedById1(String assignedById1) {
        AssignedById1 = assignedById1;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getIsApproved() {
        return IsApproved;
    }

    public void setIsApproved(String isApproved) {
        IsApproved = isApproved;
    }

    public String getIsChargable1() {
        return IsChargable1;
    }

    public void setIsChargable1(String isChargable1) {
        IsChargable1 = isChargable1;
    }

    public String getActivityTypeId() {
        return ActivityTypeId;
    }

    public void setActivityTypeId(String activityTypeId) {
        ActivityTypeId = activityTypeId;
    }

    public String getIsApproval() {
        return IsApproval;
    }

    public void setIsApproval(String isApproval) {
        IsApproval = isApproval;
    }

    public String getAttachmentName() {
        return AttachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        AttachmentName = attachmentName;
    }

    public String getAttachmentContent() {
        return AttachmentContent;
    }

    public void setAttachmentContent(String attachmentContent) {
        AttachmentContent = attachmentContent;
    }

    public String getModifiedDt1() {
        return ModifiedDt1;
    }

    public void setModifiedDt1(String modifiedDt1) {
        ModifiedDt1 = modifiedDt1;
    }

    public String getSourceId() {
        return SourceId;
    }

    public void setSourceId(String sourceId) {
        SourceId = sourceId;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public String getUnitDesc() {
        return UnitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        UnitDesc = unitDesc;
    }

    public String getModuleName() {
        return ModuleName;
    }

    public void setModuleName(String moduleName) {
        ModuleName = moduleName;
    }

    public String getActivityName1() {
        return ActivityName1;
    }

    public void setActivityName1(String activityName1) {
        ActivityName1 = activityName1;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(String projectCode) {
        ProjectCode = projectCode;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getExpectedComplete_Date1() {
        return ExpectedComplete_Date1;
    }

    public void setExpectedComplete_Date1(String expectedComplete_Date1) {
        ExpectedComplete_Date1 = expectedComplete_Date1;
    }

    public String getDeptDesc() {
        return DeptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        DeptDesc = deptDesc;
    }

    public String getDeptMasterId() {
        return DeptMasterId;
    }

    public void setDeptMasterId(String deptMasterId) {
        DeptMasterId = deptMasterId;
    }

    public String getCompletionIntimate() {
        return CompletionIntimate;
    }

    public void setCompletionIntimate(String completionIntimate) {
        CompletionIntimate = completionIntimate;
    }

    public String getModifiedBy1() {
        return ModifiedBy1;
    }

    public void setModifiedBy1(String modifiedBy1) {
        ModifiedBy1 = modifiedBy1;
    }

    public String getReassignedBy() {
        return ReassignedBy;
    }

    public void setReassignedBy(String reassignedBy) {
        ReassignedBy = reassignedBy;
    }

    public String getReassignedDt() {
        return ReassignedDt;
    }

    public void setReassignedDt(String reassignedDt) {
        ReassignedDt = reassignedDt;
    }

    public String getActualCompletionDate() {
        return ActualCompletionDate;
    }

    public void setActualCompletionDate(String actualCompletionDate) {
        ActualCompletionDate = actualCompletionDate;
    }

    public String getWarrantyCode() {
        return WarrantyCode;
    }

    public void setWarrantyCode(String warrantyCode) {
        WarrantyCode = warrantyCode;
    }

    public String getTicketCategory() {
        return TicketCategory;
    }

    public void setTicketCategory(String ticketCategory) {
        TicketCategory = ticketCategory;
    }

    public String getIsEndTime() {
        return IsEndTime;
    }

    public void setIsEndTime(String isEndTime) {
        IsEndTime = isEndTime;
    }

    public String getIsCompActPresent() {
        return IsCompActPresent;
    }

    public void setIsCompActPresent(String isCompActPresent) {
        IsCompActPresent = isCompActPresent;
    }

    public String getCompletionActId() {
        return CompletionActId;
    }

    public void setCompletionActId(String completionActId) {
        CompletionActId = completionActId;
    }

    public String getTktCustReportedBy() {
        return TktCustReportedBy;
    }

    public void setTktCustReportedBy(String tktCustReportedBy) {
        TktCustReportedBy = tktCustReportedBy;
    }

    public String getTktCustApprovedBy() {
        return TktCustApprovedBy;
    }

    public void setTktCustApprovedBy(String tktCustApprovedBy) {
        TktCustApprovedBy = tktCustApprovedBy;
    }

    public String getIsSubActivity() {
        return IsSubActivity;
    }

    public void setIsSubActivity(String isSubActivity) {
        IsSubActivity = isSubActivity;
    }

    public String getParentActId() {
        return ParentActId;
    }

    public void setParentActId(String parentActId) {
        ParentActId = parentActId;
    }

    public String getActivityTypeName() {
        return ActivityTypeName;
    }

    public void setActivityTypeName(String activityTypeName) {
        ActivityTypeName = activityTypeName;
    }

    public String getCompActName() {
        return CompActName;
    }

    public void setCompActName(String compActName) {
        CompActName = compActName;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}
