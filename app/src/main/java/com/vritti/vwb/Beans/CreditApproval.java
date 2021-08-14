package com.vritti.vwb.Beans;

import java.io.Serializable;

public class CreditApproval implements Serializable {

    String CustVendorName,AddedDt,ApprovalStatus,CreditApprovalAmt,PKCreditId;

    public String getCustVendorName() {
        return CustVendorName;
    }

    public void setCustVendorName(String custVendorName) {
        CustVendorName = custVendorName;
    }

    public String getAddedDt() {
        return AddedDt;
    }

    public void setAddedDt(String addedDt) {
        AddedDt = addedDt;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getCreditApprovalAmt() {
        return CreditApprovalAmt;
    }

    public void setCreditApprovalAmt(String creditApprovalAmt) {
        CreditApprovalAmt = creditApprovalAmt;
    }

    public String getPKCreditId() {
        return PKCreditId;
    }

    public void setPKCreditId(String PKCreditId) {
        this.PKCreditId = PKCreditId;
    }
}
