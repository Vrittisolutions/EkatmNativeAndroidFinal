package com.vritti.crm.bean;

import java.util.ArrayList;

public class EnterpriseBean {
    String FirmName , firmAlias , address , Countryid , Stateid, Cityid , Territoryid , gstn , TAN_No , BusDetailid , website , SuSpSourceId , notes,entityId,entityName ;

    ArrayList<EnterpriseBean> enterpriseBeanArrayList;

    public EnterpriseBean(ArrayList<EnterpriseBean> enterpriseBeanArrayList) {
        this.enterpriseBeanArrayList = enterpriseBeanArrayList;
    }

    public EnterpriseBean() {

    }


    public ArrayList<EnterpriseBean> getEnterpriseBeanArrayList() {
        return enterpriseBeanArrayList;
    }

    public void setEnterpriseBeanArrayList(ArrayList<EnterpriseBean> enterpriseBeanArrayList) {
        this.enterpriseBeanArrayList = enterpriseBeanArrayList;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getFirmAlias() {
        return firmAlias;
    }

    public void setFirmAlias(String firmAlias) {
        this.firmAlias = firmAlias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountryid() {
        return Countryid;
    }

    public void setCountryid(String countryid) {
        Countryid = countryid;
    }

    public String getStateid() {
        return Stateid;
    }

    public void setStateid(String stateid) {
        Stateid = stateid;
    }

    public String getCityid() {
        return Cityid;
    }

    public void setCityid(String cityid) {
        Cityid = cityid;
    }

    public String getTerritoryid() {
        return Territoryid;
    }

    public void setTerritoryid(String territoryid) {
        Territoryid = territoryid;
    }

    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    public String getTAN_No() {
        return TAN_No;
    }

    public void setTAN_No(String TAN_No) {
        this.TAN_No = TAN_No;
    }

    public String getBusDetailid() {
        return BusDetailid;
    }

    public void setBusDetailid(String busDetailid) {
        BusDetailid = busDetailid;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSuSpSourceId() {
        return SuSpSourceId;
    }

    public void setSuSpSourceId(String suSpSourceId) {
        SuSpSourceId = suSpSourceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }
}
