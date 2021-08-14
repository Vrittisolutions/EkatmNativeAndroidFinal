package com.vritti.crm.bean;

/**
 * Created by sharvari on 14-Feb-17.
 */

public class Teritorybean {
    String
            PKTerritoryId,
            TerritoryCode,
            TerritoryName,
            FKParentTerritoryId,
            AddedBy,
            AddedDt,
            ModifiedBy,
            ModifiedDt,
            IsDeleted,
            ManagerId;

    public Teritorybean(String pkTerritoryId, String TerritoryName) {
        this.PKTerritoryId = pkTerritoryId;
        this.TerritoryName = TerritoryName;
    }

    public Teritorybean() {

    }

    public String getPKTerritoryId() {
        return PKTerritoryId;
    }

    public void setPKTerritoryId(String PKTerritoryId) {
        this.PKTerritoryId = PKTerritoryId;
    }

    public String getTerritoryCode() {
        return TerritoryCode;
    }

    public void setTerritoryCode(String territoryCode) {
        TerritoryCode = territoryCode;
    }

    public String getTerritoryName() {
        return TerritoryName;
    }

    public void setTerritoryName(String territoryName) {
        TerritoryName = territoryName;
    }

    public String getFKParentTerritoryId() {
        return FKParentTerritoryId;
    }

    public void setFKParentTerritoryId(String FKParentTerritoryId) {
        this.FKParentTerritoryId = FKParentTerritoryId;
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

    public String getManagerId() {
        return ManagerId;
    }

    public void setManagerId(String managerId) {
        ManagerId = managerId;
    }

    @Override
    public String toString() {
        // return super.toString();
        return this.TerritoryName;
    }
}

