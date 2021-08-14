package com.vritti.crmlib.bean;

import java.io.Serializable;

/**
 * Created by sharvari on 07-Mar-17.
 */

public class TeamMemberbean implements Serializable {
    String UserMasterId, UserLoginId, UserName,Assigned,Overdue,Today,Hot,Collection,Tomorrow,Report;

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getUserLoginId() {
        return UserLoginId;
    }

    public void setUserLoginId(String userLoginId) {
        UserLoginId = userLoginId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAssigned() {
        return Assigned;
    }

    public void setAssigned(String assigned) {
        Assigned = assigned;
    }

    public String getOverdue() {
        return Overdue;
    }

    public void setOverdue(String overdue) {
        Overdue = overdue;
    }

    public String getToday() {
        return Today;
    }

    public void setToday(String today) {
        Today = today;
    }

    public String getHot() {
        return Hot;
    }

    public void setHot(String hot) {
        Hot = hot;
    }

    public String getCollection() {
        return Collection;
    }

    public void setCollection(String collection) {
        Collection = collection;
    }

    public String getTomorrow() {
        return Tomorrow;
    }

    public void setTomorrow(String tomorrow) {
        Tomorrow = tomorrow;

    }

    public String getReport() {
        return Report;
    }

    public void setReport(String report) {
        Report = report;
    }
}

