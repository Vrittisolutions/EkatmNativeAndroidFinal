package com.vritti.sales.beans;

public class StateBean {

    String PKStateId;
    String StateNo;
    String StateDesc;
    String FKCountryId;
    String IsDeleted;

    public StateBean() {

    }

    @Override
    public String toString() {
//        return super.toString();
        return  this.StateDesc;
    }

    String AddedBy;

    public StateBean(String PKStateId, String stateDesc) {
        this.PKStateId = PKStateId;
        this.StateDesc = stateDesc;
    }

    String AddedDt;
    String ModifiedBy;
    String ModifiedDt;

    public String getPKStateId() {
        return PKStateId;
    }

    public void setPKStateId(String PKStateId) {
        this.PKStateId = PKStateId;
    }

    public String getStateNo() {
        return StateNo;
    }

    public void setStateNo(String stateNo) {
        StateNo = stateNo;
    }

    public String getStateDesc() {
        return StateDesc;
    }

    public void setStateDesc(String stateDesc) {
        StateDesc = stateDesc;
    }

    public String getFKCountryId() {
        return FKCountryId;
    }

    public void setFKCountryId(String FKCountryId) {
        this.FKCountryId = FKCountryId;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getAddedDt() {
        return AddedDt;
    }

    public void setAddedDt(String addedDt) {
        AddedDt = addedDt;
    }

    public String getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedDt() {
        return ModifiedDt;
    }

    public void setModifiedDt(String modifiedDt) {
        ModifiedDt = modifiedDt;
    }

}
