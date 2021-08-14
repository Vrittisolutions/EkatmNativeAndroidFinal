package com.vritti.crmlib.classes;

import java.io.Serializable;

/**
 * Created by sharvari on 26-May-17.
 */

public class CallHistory implements Serializable {



    String CallHistoryId,CallId,CurrentCallOwner,ActionType,Contact,Purpose,NextAction,Telephone,NextActionDateTime,ModifiedDt,Outcome,UserName,OutcomeCode,LatestRemark;

    public String getCallHistoryId() {
        return CallHistoryId;
    }

    public void setCallHistoryId(String callHistoryId) {
        CallHistoryId = callHistoryId;
    }

    public String getCallId() {
        return CallId;
    }

    public void setCallId(String callId) {
        CallId = callId;
    }

    public String getCurrentCallOwner() {
        return CurrentCallOwner;
    }

    public void setCurrentCallOwner(String currentCallOwner) {
        CurrentCallOwner = currentCallOwner;
    }

    public String getActionType() {
        return ActionType;
    }

    public void setActionType(String actionType) {
        ActionType = actionType;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getNextAction() {
        return NextAction;
    }

    public void setNextAction(String nextAction) {
        NextAction = nextAction;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getNextActionDateTime() {
        return NextActionDateTime;
    }

    public void setNextActionDateTime(String nextActionDateTime) {
        NextActionDateTime = nextActionDateTime;
    }

    public String getModifiedDt() {
        return ModifiedDt;
    }

    public void setModifiedDt(String modifiedDt) {
        ModifiedDt = modifiedDt;
    }

    public String getOutcome() {
        return Outcome;
    }

    public void setOutcome(String outcome) {
        Outcome = outcome;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getOutcomeCode() {
        return OutcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        OutcomeCode = outcomeCode;
    }

    public String getLatestRemark() {
        return LatestRemark;
    }

    public void setLatestRemark(String latestRemark) {
        LatestRemark = latestRemark;
    }
}
