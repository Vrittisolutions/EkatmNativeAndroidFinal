package com.vritti.crm.bean;

import java.io.Serializable;

/**
 * Created by sharvari on 14-Jul-17.
 */

public class ShowContact implements Serializable {


    String PKSuspContactDtlsID,ContactName,Designation,ContactPersonDept,Mobile,EmailId,IsPrimaryContact,FKSuspectId;

    public String getFKSuspectId() {
        return FKSuspectId;
    }

    public void setFKSuspectId(String FKSuspectId) {
        this.FKSuspectId = FKSuspectId;
    }

    public String getPKSuspContactDtlsID() {
        return PKSuspContactDtlsID;
    }

    public void setPKSuspContactDtlsID(String PKSuspContactDtlsID) {
        this.PKSuspContactDtlsID = PKSuspContactDtlsID;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getContactPersonDept() {
        return ContactPersonDept;
    }

    public void setContactPersonDept(String contactPersonDept) {
        ContactPersonDept = contactPersonDept;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getIsPrimaryContact() {
        return IsPrimaryContact;
    }

    public void setIsPrimaryContact(String isPrimaryContact) {
        IsPrimaryContact = isPrimaryContact;
    }
}
