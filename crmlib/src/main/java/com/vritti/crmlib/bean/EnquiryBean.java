package com.vritti.crmlib.bean;

import java.io.Serializable;

public class EnquiryBean implements Serializable {
    String EnquiryRegistryId,
            EnquiryDate ,
            RegistryById,
            AssignedToId,
            CustomerName,
            ContactName,
            ContactNumber,
            Email,
            EnquiryDetails,
            ActionTaken ,
            ReasonForCancellation,
            CallId,
            AddedBy,
            Addeddt,
            ModifiedBy,
            ModifiedDt,
            Status;

    public String getEnquiryRegistryId() {
        return EnquiryRegistryId;
    }

    public void setEnquiryRegistryId(String enquiryRegistryId) {
        EnquiryRegistryId = enquiryRegistryId;
    }

    public String getEnquiryDate() {
        return EnquiryDate;
    }

    public void setEnquiryDate(String enquiryDate) {
        EnquiryDate = enquiryDate;
    }

    public String getRegistryById() {
        return RegistryById;
    }

    public void setRegistryById(String registryById) {
        RegistryById = registryById;
    }

    public String getAssignedToId() {
        return AssignedToId;
    }

    public void setAssignedToId(String assignedToId) {
        AssignedToId = assignedToId;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEnquiryDetails() {
        return EnquiryDetails;
    }

    public void setEnquiryDetails(String enquiryDetails) {
        EnquiryDetails = enquiryDetails;
    }

    public String getActionTaken() {
        return ActionTaken;
    }

    public void setActionTaken(String actionTaken) {
        ActionTaken = actionTaken;
    }

    public String getReasonForCancellation() {
        return ReasonForCancellation;
    }

    public void setReasonForCancellation(String reasonForCancellation) {
        ReasonForCancellation = reasonForCancellation;
    }

    public String getCallId() {
        return CallId;
    }

    public void setCallId(String callId) {
        CallId = callId;
    }

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public String getAddeddt() {
        return Addeddt;
    }

    public void setAddeddt(String addeddt) {
        Addeddt = addeddt;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
