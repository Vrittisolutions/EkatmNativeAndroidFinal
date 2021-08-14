package com.vritti.sales.beans;

import java.io.Serializable;

public class MerchantPaymentHistory implements Serializable {

    String  PaymentMode,Amount,CustomerName,addeddate,InvoiceNo, ActivityName, ModifiedDt,ApprovedAmount="",DiscountAmount="",
            BalanceAmount="",addedbyid="",addedbyname="";

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getCustomerName() { return CustomerName; }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddeddate() {
        return addeddate;
    }

    public void setAddeddate(String addeddate) { this.addeddate = addeddate; }

    public String getInvoiceNo() { return InvoiceNo; }

    public void setInvoiceNo(String invoiceNo) { InvoiceNo = invoiceNo; }

    public String getActivityName() { return ActivityName; }

    public void setActivityName(String activityName) { ActivityName = activityName; }

    public String getModifiedDt() { return ModifiedDt; }

    public void setModifiedDt(String modifiedDt) { ModifiedDt = modifiedDt; }

    public String getApprovedAmount() { return ApprovedAmount; }

    public void setApprovedAmount(String approvedAmount) { ApprovedAmount = approvedAmount; }

    public String getDiscountAmount() { return DiscountAmount; }

    public void setDiscountAmount(String discountAmount) { DiscountAmount = discountAmount; }

    public String getBalanceAmount() { return BalanceAmount; }

    public void setBalanceAmount(String balanceAmount) { BalanceAmount = balanceAmount; }

    public String getAddedbyid() {
        return addedbyid;
    }

    public void setAddedbyid(String addedbyid) {
        this.addedbyid = addedbyid;
    }

    public String getAddedbyname() {
        return addedbyname;
    }

    public void setAddedbyname(String addedbyname) {
        this.addedbyname = addedbyname;
    }
}
