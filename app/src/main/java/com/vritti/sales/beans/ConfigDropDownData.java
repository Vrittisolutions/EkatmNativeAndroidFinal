package com.vritti.sales.beans;

public class ConfigDropDownData {

    String ConfigurationDetailId = "",Configuration="",ConfigurationName="",TermsCode = "",TermsDescription="",PymtSettTermMasterId = "",
            CreditDays = "", BaseDate = "",IsDeleted = "";    //terms&conditions
    String ChargeMasterId = "", ChargeCode = "", ChargeDesc = "";   //charge
    String CommTypeId = "",TypeCode="",TypeDesc="",VouMasterId="";  //commission
    String PKUserCategoryId = "", CategoryDesc = "";    //reimbursement
    String WarrantyMasterId = "", WarrantyDesc = "", WarrantyCode = ""; //directitem
    String UOMMasterId = "", UOMCode = "", UOMDesc = "",UOMDigitString = ""; //directitem
    String type = "";

    public ConfigDropDownData(){

    }

    public ConfigDropDownData(String _UOMMasterId,String _UOMCode,String _UOMDesc,String _UOMDigitString){
        this.UOMMasterId = _UOMMasterId;
        this.UOMCode = _UOMCode;
        this.UOMDesc = _UOMDesc;
        this.UOMDigitString = _UOMDigitString;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUOMMasterId() { return UOMMasterId; }

    public String getUOMCode() { return UOMCode; }

    public String getUOMDesc() { return UOMDesc; }

    public String getUOMDigitString() { return UOMDigitString; }

    public String getConfigurationDetailId() {
        return ConfigurationDetailId;
    }

    public void setConfigurationDetailId(String configurationDetailId) {
        ConfigurationDetailId = configurationDetailId;
    }

    public String getConfiguration() {
        return Configuration;
    }

    public void setConfiguration(String configuration) {
        Configuration = configuration;
    }

    public String getConfigurationName() {
        return ConfigurationName;
    }

    public void setConfigurationName(String configurationName) {
        ConfigurationName = configurationName;
    }

    public String getChargeMasterId() {
        return ChargeMasterId;
    }

    public void setChargeMasterId(String chargeMasterId) {
        ChargeMasterId = chargeMasterId;
    }

    public String getChargeCode() {
        return ChargeCode;
    }

    public void setChargeCode(String chargeCode) {
        ChargeCode = chargeCode;
    }

    public String getChargeDesc() {
        return ChargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        ChargeDesc = chargeDesc;
    }

    public String getTermsCode() {
        return TermsCode;
    }

    public void setTermsCode(String termsCode) {
        TermsCode = termsCode;
    }

    public String getTermsDescription() {
        return TermsDescription;
    }

    public void setTermsDescription(String termsDescription) { TermsDescription = termsDescription; }

    public String getPymtSettTermMasterId() {
        return PymtSettTermMasterId;
    }

    public void setPymtSettTermMasterId(String pymtSettTermMasterId) { PymtSettTermMasterId = pymtSettTermMasterId; }

    public String getCreditDays() {
        return CreditDays;
    }

    public void setCreditDays(String creditDays) {
        CreditDays = creditDays;
    }

    public String getBaseDate() {
        return BaseDate;
    }

    public void setBaseDate(String baseDate) {
        BaseDate = baseDate;
    }

    public String getIsDeleted() { return IsDeleted; }

    public void setIsDeleted(String isDeleted) { IsDeleted = isDeleted; }

    public String getCommTypeId() { return CommTypeId; }

    public void setCommTypeId(String commTypeId) { CommTypeId = commTypeId; }

    public String getTypeCode() { return TypeCode; }

    public void setTypeCode(String typeCode) { TypeCode = typeCode; }

    public String getTypeDesc() { return TypeDesc; }

    public void setTypeDesc(String typeDesc) { TypeDesc = typeDesc; }

    public String getVouMasterId() { return VouMasterId; }

    public void setVouMasterId(String vouMasterId) { VouMasterId = vouMasterId; }

    public String getPKUserCategoryId() { return PKUserCategoryId; }

    public void setPKUserCategoryId(String PKUserCategoryId) { this.PKUserCategoryId = PKUserCategoryId; }

    public String getCategoryDesc() { return CategoryDesc; }

    public void setCategoryDesc(String categoryDesc) { CategoryDesc = categoryDesc; }

    public String getWarrantyMasterId() { return WarrantyMasterId; }

    public void setWarrantyMasterId(String warrantyMasterId) { WarrantyMasterId = warrantyMasterId; }

    public String getWarrantyDesc() { return WarrantyDesc; }

    public void setWarrantyDesc(String warrantyDesc) { WarrantyDesc = warrantyDesc; }

    public String getWarrantyCode() { return WarrantyCode; }

    public void setWarrantyCode(String warrantyCode) { WarrantyCode = warrantyCode; }
}
