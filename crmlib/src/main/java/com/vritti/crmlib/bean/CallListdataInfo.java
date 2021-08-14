package com.vritti.crmlib.bean;

/**
 * Created by sharvari on 14-Dec-16.
 */



public class CallListdataInfo {

    String firmname,  cityname , cityterritoryname ,  CallId ,  productname  , actionicon   , actiondatetime ,  assignedby , isPartial,  mobileno;
String CallType,CallStatus,ContactName,Outcome,Username,LatestRemark,NextActionDateTime;

    public String getContactName() {
        return ContactName;
    }

    public String getNextActionDateTime() {
        return NextActionDateTime;
    }

    public void setNextActionDateTime(String nextActionDateTime) {
        NextActionDateTime = nextActionDateTime;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getFirmname() {
        return firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCityterritoryname() {
        return cityterritoryname;
    }

    public void setCityterritoryname(String cityterritoryname) {
        this.cityterritoryname = cityterritoryname;
    }

    public String getCallId() {
        return CallId;
    }

    public void setCallId(String callId) {
        CallId = callId;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getActionicon() {
        return actionicon;
    }

    public void setActionicon(String actionicon) {
        this.actionicon = actionicon;
    }

    public String getActiondatetime() {
        return actiondatetime;
    }

    public void setActiondatetime(String actiondatetime) {
        this.actiondatetime = actiondatetime;
    }

    public String getAssignedby() {
        return assignedby;
    }

    public void setAssignedby(String assignedby) {
        this.assignedby = assignedby;
    }

    public String getIsPartial() {
        return isPartial;
    }

    public void setIsPartial(String isPartial) {
        this.isPartial = isPartial;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

    public String getCallStatus() {
        return CallStatus;
    }

    public void setCallStatus(String callStatus) {
        CallStatus = callStatus;
    }

    public String getOutcome() {
        return Outcome;
    }

    public void setOutcome(String outcome) {
        Outcome = outcome;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getLatestRemark() {
        return LatestRemark;
    }

    public void setLatestRemark(String latestRemark) {
        LatestRemark = latestRemark;
    }
}
