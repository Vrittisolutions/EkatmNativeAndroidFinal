package com.vritti.crm.bean;

import java.io.Serializable;

/**
 * Created by sharvari on 26-Sep-17.
 */

public class AdvanceProvisionalData implements Serializable {

    String Customer_name,CollectionReceiptMasterId,InvoiceNo,CustomerId,CallId,Amount,InstrumentNo,BankName,TDSAmount,Narration,ReceiptStatus,AddedBy,AddedDt,PaymentDepBank,DepositedDt;

    public String getCollectionReceiptMasterId() {
        return CollectionReceiptMasterId;
    }

    public void setCollectionReceiptMasterId(String collectionReceiptMasterId) {
        CollectionReceiptMasterId = collectionReceiptMasterId;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCallId() {
        return CallId;
    }

    public void setCallId(String callId) {
        CallId = callId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getInstrumentNo() {
        return InstrumentNo;
    }

    public void setInstrumentNo(String instrumentNo) {
        InstrumentNo = instrumentNo;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getTDSAmount() {
        return TDSAmount;
    }

    public void setTDSAmount(String TDSAmount) {
        this.TDSAmount = TDSAmount;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    public String getReceiptStatus() {
        return ReceiptStatus;
    }

    public void setReceiptStatus(String receiptStatus) {
        ReceiptStatus = receiptStatus;
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

    public String getPaymentDepBank() {
        return PaymentDepBank;
    }

    public void setPaymentDepBank(String paymentDepBank) {
        PaymentDepBank = paymentDepBank;
    }

    public String getDepositedDt() {
        return DepositedDt;
    }

    public void setDepositedDt(String depositedDt) {
        DepositedDt = depositedDt;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }
}
