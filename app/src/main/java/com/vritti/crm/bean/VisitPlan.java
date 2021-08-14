package com.vritti.crm.bean;

import java.io.Serializable;

public class VisitPlan implements Serializable {

    String  FirmName,FollowupDate,FollowupComment,NextActionDateTime,NextAction,CityName;

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getFollowupDate() {
        return FollowupDate;
    }

    public void setFollowupDate(String followupDate) {
        FollowupDate = followupDate;
    }

    public String getFollowupComment() {
        return FollowupComment;
    }

    public void setFollowupComment(String followupComment) {
        FollowupComment = followupComment;
    }

    public String getNextActionDateTime() {
        return NextActionDateTime;
    }

    public void setNextActionDateTime(String nextActionDateTime) {
        NextActionDateTime = nextActionDateTime;
    }

    public String getNextAction() {
        return NextAction;
    }

    public void setNextAction(String nextAction) {
        NextAction = nextAction;
    }
}
