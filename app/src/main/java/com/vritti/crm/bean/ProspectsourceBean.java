package com.vritti.crm.bean;

/**
 * Created by sharvari on 14-Feb-17.
 */

public class ProspectsourceBean {
  String  PKSuspSourceId ,
            SourceName ,
            AddedBy ,
            AddedDt ,
            ModifiedBy ,
            ModifiedDt ,
            IsDeleted ;

    public String getPKSuspSourceId() {
        return PKSuspSourceId;
    }

    public void setPKSuspSourceId(String PKSuspSourceId) {
        this.PKSuspSourceId = PKSuspSourceId;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String sourceName) {
        SourceName = sourceName;
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

    public String getIsDeleted() {
        return IsDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        IsDeleted = isDeleted;
    }
    @Override
    public String toString() {
        // return super.toString();
        return this.SourceName;
    }
}