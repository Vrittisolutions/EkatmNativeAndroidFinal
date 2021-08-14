package com.vritti.crm.bean;

public class EntityBean {
    String CustVendorMasterId,CustVendorName;

    public EntityBean(String custVendorName, String custVendorMasterId) {
        this.CustVendorName = custVendorName;
        this.CustVendorMasterId = custVendorMasterId;
    }

    public EntityBean() {
    }

    public String getCustVendorMasterId() {
        return CustVendorMasterId;
    }

    public void setCustVendorMasterId(String custVendorMasterId) {
        CustVendorMasterId = custVendorMasterId;
    }

    public String getCustVendorName() {
        return CustVendorName;
    }

    public void setCustVendorName(String custVendorName) {
        CustVendorName = custVendorName;
    }
}
