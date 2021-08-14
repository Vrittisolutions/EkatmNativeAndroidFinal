package com.vritti.crmlib.bean;

/**
 * Created by 300151 on 10/17/2016.
 */
public class MyTeamBean {
    String UserName,
            UserMasterId,
            UserLoginId,
            ClosedCalls,
            TotalCalls, Percent,Assigned,Overdue;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

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

    public String getClosedCalls() {
        return ClosedCalls;
    }

    public void setClosedCalls(String closedCalls) {
        ClosedCalls = closedCalls;
    }

    public String getTotalCalls() {
        return TotalCalls;
    }

    public void setTotalCalls(String totalCalls) {
        TotalCalls = totalCalls;
    }

    public String getPercent() {
        return Percent;
    }

    public void setPercent(String percent) {
        Percent = percent;
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
}
