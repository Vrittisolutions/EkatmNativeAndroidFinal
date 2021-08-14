package com.vritti.crm.bean;

import java.util.ArrayList;

public class CallLogsDetails {
    String userMasterId,userName,number,StartTime,EndTime,duration,callType,rowNo,contactPersonName,customerName,Firmname,Username="";;
    ArrayList<CallLogsDetails> callLogsDetailsArrayList;

    public CallLogsDetails(ArrayList<CallLogsDetails> callLogsDetailsArrayList) {
        this.callLogsDetailsArrayList = callLogsDetailsArrayList;

    }

    public CallLogsDetails() {

    }

    public ArrayList<CallLogsDetails> getCallLogsDetailsArrayList() {
        return callLogsDetailsArrayList;
    }

    public void setCallLogsDetailsArrayList(ArrayList<CallLogsDetails> callLogsDetailsArrayList) {
        this.callLogsDetailsArrayList = callLogsDetailsArrayList;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getUserMasterId() {
        return userMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        this.userMasterId = userMasterId;
    }

    public String getUserName() {
        return userName;
    }

    public String getRowNo() {
        return rowNo;
    }

    public void setRowNo(String rowNo) {
        this.rowNo = rowNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getFirmname() {
        return Firmname;
    }

    public void setFirmname(String firmname) {
        Firmname = firmname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
