package com.vritti.crm.bean;

/**
 * Created by sharvari on 06-Feb-17.
 */

public class CityBean {

String   PKCityID  , CityName ,FKTalukaId , FKStateId , FKDistrictId ,
        PinCode ,
        FKCountryId  ,FKTerritoryId ,AddedBy ,AddedDt , ModifiedBy ,
        ModifiedDt ,IsDeleted , TerritoryName ;

    public String getPKCityID() {
        return PKCityID;
    }

    public void setPKCityID(String PKCityID) {
        this.PKCityID = PKCityID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getFKTalukaId() {
        return FKTalukaId;
    }

    public void setFKTalukaId(String FKTalukaId) {
        this.FKTalukaId = FKTalukaId;
    }

    public String getFKStateId() {
        return FKStateId;
    }

    public void setFKStateId(String FKStateId) {
        this.FKStateId = FKStateId;
    }

    public String getFKDistrictId() {
        return FKDistrictId;
    }

    public void setFKDistrictId(String FKDistrictId) {
        this.FKDistrictId = FKDistrictId;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getFKCountryId() {
        return FKCountryId;
    }

    public void setFKCountryId(String FKCountryId) {
        this.FKCountryId = FKCountryId;
    }

    public String getFKTerritoryId() {
        return FKTerritoryId;
    }

    public void setFKTerritoryId(String FKTerritoryId) {
        this.FKTerritoryId = FKTerritoryId;
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

    public String getTerritoryName() {
        return TerritoryName;
    }

    public void setTerritoryName(String territoryName) {
        TerritoryName = territoryName;
    }
}
