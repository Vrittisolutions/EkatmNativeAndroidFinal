package com.vritti.vwblib.Beans;

/**
 * Created by 300151 on 10/17/2016.
 */
public class MyTeamBean {
    String UserName;
    String UserMasterId;
    String CountOnTime;
    String TotalCount;
    String OnTimePerc;
    String TotalOverdueActivities;
    String TotalCritical,TotalUnapproved,TotalAssigned;

    public String getTotalCritical() {
        return TotalCritical;
    }

    public void setTotalCritical(String totalCritical) {
        TotalCritical = totalCritical;
    }

    public String getTotalUnapproved() {
        return TotalUnapproved;
    }

    public void setTotalUnapproved(String totalUnapproved) {
        TotalUnapproved = totalUnapproved;
    }

    public String getTotalAssigned() {
        return TotalAssigned;
    }

    public void setTotalAssigned(String totalAssigned) {
        TotalAssigned = totalAssigned;
    }

    public String getUserName() {
        return UserName;
    }

    public String getTotalOverdueActivities() {
        return TotalOverdueActivities;
    }

    public void setTotalOverdueActivities(String totalOverdueActivities) {
        TotalOverdueActivities = totalOverdueActivities;
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

    public String getCountOnTime() {
        return CountOnTime;
    }

    public void setCountOnTime(String countOnTime) {
        CountOnTime = countOnTime;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    public String getOnTimePerc() {
        return OnTimePerc;
    }

    public void setOnTimePerc(String onTimePerc) {
        OnTimePerc = onTimePerc;
    }
}
