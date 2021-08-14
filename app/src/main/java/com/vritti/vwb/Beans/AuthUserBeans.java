package com.vritti.vwb.Beans;

public class AuthUserBeans {
    String UserMasterId,UserLoginId,UserName;

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
    @Override
    public String toString() {
        return UserName;
    }
}
