package com.vritti.vwb.Beans;

public class ApproverData {

    String UserMasterId,UserName,ApprLvl;

    public String getApprLvl() {
        return ApprLvl;
    }

    public void setApprLvl(String apprLvl) {
        ApprLvl = apprLvl;
    }

    public String getUserMasterId() {
        return UserMasterId;
    }

    public void setUserMasterId(String userMasterId) {
        UserMasterId = userMasterId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
    @Override
    public String toString() {
        return UserName;
    }
}
