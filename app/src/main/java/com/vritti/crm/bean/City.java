package com.vritti.crm.bean;

public class City {

    String PKDistrictId;
    String DistrictNo;
    String DistrictDesc;
    String FKStateId;
    String IsDeleted;
    String PKCityID;
    String CityName;
    String FKCountryId;



    public City(String PKDistrictId, String districtDesc) {
        this.PKDistrictId = PKDistrictId;
        this.DistrictDesc = districtDesc;
       // this.FKStateId = FKStateId;
    }

    public City() {
    }

    String AddedBy;
    String AddedDt;
    String ModifiedBy;
    String ModifiedDt;



    @Override
    public String toString() {
        /*return super.toString();*/
        return  this.DistrictDesc;
    }


    public String getPKDistrictId() {
        return PKDistrictId;
    }


    public void setPKDistrictId(String PKDistrictId) {
        this.PKDistrictId = PKDistrictId;
    }

    public String getDistrictNo() {
        return DistrictNo;
    }

    public void setDistrictNo(String districtNo) {
        DistrictNo = districtNo;
    }

    public String getDistrictDesc() {
        return DistrictDesc;
    }

    public void setDistrictDesc(String districtDesc) {
        DistrictDesc = districtDesc;
    }

    public String getFKStateId() {
        return FKStateId;
    }

    public void setFKStateId(String FKStateId) {
        this.FKStateId = FKStateId;
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