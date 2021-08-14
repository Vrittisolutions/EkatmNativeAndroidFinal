package com.vritti.crmlib.bean;

public class CustomerListBean {

    String CustVendorMasterId,
            CustVendorCode,
            CustVendorName,
            ContactName,
    CityName;

    public String getCustVendorMasterId() {
        return CustVendorMasterId;
    }

    public void setCustVendorMasterId(String custVendorMasterId) {
        CustVendorMasterId = custVendorMasterId;
    }

    public String getCustVendorCode() {
        return CustVendorCode;
    }

    public void setCustVendorCode(String custVendorCode) {
        CustVendorCode = custVendorCode;
    }

    public String getCustVendorName() {
        return CustVendorName;
    }

    public void setCustVendorName(String custVendorName) {
        CustVendorName = custVendorName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }
}
