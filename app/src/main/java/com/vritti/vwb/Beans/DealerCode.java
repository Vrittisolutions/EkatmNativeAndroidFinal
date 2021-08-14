package com.vritti.vwb.Beans;

import java.io.Serializable;

public class DealerCode implements Serializable {

    String UserMasterId,UserName,Condition,UserCode;

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

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userCode) {
        UserCode = userCode;
    }

    @Override
    public String toString() {
        return this.UserCode;
    }
}
