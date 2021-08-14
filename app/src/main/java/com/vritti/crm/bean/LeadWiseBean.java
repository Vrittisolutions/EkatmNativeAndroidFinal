package com.vritti.crm.bean;

public class LeadWiseBean {

    String CustVendorMasterId,CustVendorName,Mobile,Email,Address;

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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public String toString() {
        return this.CustVendorName;
    }
}
