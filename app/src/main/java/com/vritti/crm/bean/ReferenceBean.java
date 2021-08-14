package com.vritti.crm.bean;

public class ReferenceBean {

    String CustVendor,CustVendorCode;

    public String getCustVendor() {
        return CustVendor;
    }

    public void setCustVendor(String custVendor) {
        CustVendor = custVendor;
    }

    public String getCustVendorCode() {
        return CustVendorCode;
    }

    public void setCustVendorCode(String custVendorCode) {
        CustVendorCode = custVendorCode;
    }

    @Override
    public String toString() {
        return this.CustVendorCode;
    }
}
