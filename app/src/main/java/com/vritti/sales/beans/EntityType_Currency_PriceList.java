package com.vritti.sales.beans;

public class EntityType_Currency_PriceList {
    String EntityTypeMasterId, EntityTypeCode, EntityType, IsDeleted, AddedBy, AddedDt, ModifiedBy, ModifiedDt, PriceListClassificationId, Entity,
            EntityAs, IsPartner, AccountMasterId,CurrCode;


    String CurrencyMasterId, CurrDesc;

    String PriceListHdrID, PriceLstDesc;

    public EntityType_Currency_PriceList(){

    }

    public EntityType_Currency_PriceList(String PriceLstDesc, String PriceListHdrID) {
    }

    public String getEntityTypeMasterId() {
        return EntityTypeMasterId;
    }

    public void setEntityTypeMasterId(String entityTypeMasterId) {
        EntityTypeMasterId = entityTypeMasterId;
    }

    public String getEntityTypeCode() {
        return EntityTypeCode;
    }

    public void setEntityTypeCode(String entityTypeCode) {
        EntityTypeCode = entityTypeCode;
    }

    public String getEntityType() {
        return EntityType;
    }

    public void setEntityType(String entityType) {
        EntityType = entityType;
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

    public String getPriceListClassificationId() {
        return PriceListClassificationId;
    }

    public void setPriceListClassificationId(String priceListClassificationId) {
        PriceListClassificationId = priceListClassificationId;
    }

    public String getEntity() {
        return Entity;
    }

    public void setEntity(String entity) {
        Entity = entity;
    }

    public String getEntityAs() {
        return EntityAs;
    }

    public void setEntityAs(String entityAs) {
        EntityAs = entityAs;
    }

    public String getIsPartner() {
        return IsPartner;
    }

    public void setIsPartner(String isPartner) {
        IsPartner = isPartner;
    }

    public String getAccountMasterId() {
        return AccountMasterId;
    }

    public void setAccountMasterId(String accountMasterId) {
        AccountMasterId = accountMasterId;
    }

    public String getCurrencyMasterId() {
        return CurrencyMasterId;
    }

    public void setCurrencyMasterId(String currencyMasterId) {
        CurrencyMasterId = currencyMasterId;
    }

    public String getCurrDesc() {
        return CurrDesc;
    }

    public void setCurrDesc(String currDesc) {
        CurrDesc = currDesc;
    }

    public String getPriceListHdrID() {
        return PriceListHdrID;
    }

    public void setPriceListHdrID(String priceListHdrID) {
        PriceListHdrID = priceListHdrID;
    }

    public String getPriceLstDesc() {
        return PriceLstDesc;
    }

    public void setPriceLstDesc(String priceLstDesc) {
        PriceLstDesc = priceLstDesc;
    }

    public String getCurrCode() {
        return CurrCode;
    }

    public void setCurrCode(String currCode) {
        CurrCode = currCode;
    }
}