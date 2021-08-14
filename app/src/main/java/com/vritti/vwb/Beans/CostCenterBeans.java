package com.vritti.vwb.Beans;

public class CostCenterBeans {
    String RecId , CostCtrMasterId,CostCtrCode,CostCtrDesc,Proportionate,IsActive,IsDeleted,SecId,
            AddedBy,AddedDt,ModifiedBy,ModifiedDt;

    public CostCenterBeans(String costCtrDesc, String costCtrMasterId) {
        this.CostCtrDesc = costCtrDesc;
        this.CostCtrMasterId = costCtrMasterId;
    }

    public String getRecId() {
        return RecId;
    }

    public void setRecId(String recId) {
        RecId = recId;
    }

    public String getCostCtrMasterId() {
        return CostCtrMasterId;
    }

    public void setCostCtrMasterId(String costCtrMasterId) {
        CostCtrMasterId = costCtrMasterId;
    }

    public String getCostCtrCode() {
        return CostCtrCode;
    }

    public void setCostCtrCode(String costCtrCode) {
        CostCtrCode = costCtrCode;
    }

    public String getCostCtrDesc() {
        return CostCtrDesc;
    }

    public void setCostCtrDesc(String costCtrDesc) {
        CostCtrDesc = costCtrDesc;
    }

    public String getProportionate() {
        return Proportionate;
    }

    public void setProportionate(String proportionate) {
        Proportionate = proportionate;
    }

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }

    public String getSecId() {
        return SecId;
    }

    public void setSecId(String secId) {
        SecId = secId;
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

    @Override
    public String toString() {
        return CostCtrDesc;
    }
}
