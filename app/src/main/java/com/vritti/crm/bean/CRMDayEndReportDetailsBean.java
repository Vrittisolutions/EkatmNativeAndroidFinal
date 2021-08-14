package com.vritti.crm.bean;

import java.util.ArrayList;

public class CRMDayEndReportDetailsBean {

    String CountforVisittele,
            MMTele,
            MMEmail,
            CountforVisit,
            CountforVisit1,
            MMvisit,
            firmname,
            TotalHoursSpent,
            CallPurposeDesc,
            Historynotes,
            Outcome,
            ReasonDescription,
            CallPurposeDesc1,ContactName;
    int initiatedBy;
    String SchTime;
    ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList;

    public CRMDayEndReportDetailsBean(ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList) {
        this.crmDayEndReportDetailsBeanArrayList = crmDayEndReportDetailsBeanArrayList;

    }

    public CRMDayEndReportDetailsBean() {

    }

    public CRMDayEndReportDetailsBean(String details) {

    }



    public ArrayList<CRMDayEndReportDetailsBean> getCrmDayEndReportDetailsBeanArrayList() {
        return crmDayEndReportDetailsBeanArrayList;
    }

    public void setCrmDayEndReportDetailsBeanArrayList(ArrayList<CRMDayEndReportDetailsBean> crmDayEndReportDetailsBeanArrayList) {
        this.crmDayEndReportDetailsBeanArrayList = crmDayEndReportDetailsBeanArrayList;
    }

    public String getCountforVisittele() {
        return CountforVisittele;
    }

    public void setCountforVisittele(String countforVisittele) {
        CountforVisittele = countforVisittele;
    }

    public String getMMTele() {
        return MMTele;
    }

    public void setMMTele(String MMTele) {
        this.MMTele = MMTele;
    }

    public String getMMEmail() {
        return MMEmail;
    }

    public void setMMEmail(String MMEmail) {
        this.MMEmail = MMEmail;
    }

    public String getCountforVisit() {
        return CountforVisit;
    }

    public void setCountforVisit(String countforVisit) {
        CountforVisit = countforVisit;
    }

    public String getCountforVisit1() {
        return CountforVisit1;
    }

    public void setCountforVisit1(String countforVisit1) {
        CountforVisit1 = countforVisit1;
    }

    public String getMMvisit() {
        return MMvisit;
    }

    public void setMMvisit(String MMvisit) {
        this.MMvisit = MMvisit;
    }

    public String getFirmname() {
        return firmname;
    }

    public void setFirmname(String firmname) {
        this.firmname = firmname;
    }

    public String getTotalHoursSpent() {
        return TotalHoursSpent;
    }

    public void setTotalHoursSpent(String totalHoursSpent) {
        TotalHoursSpent = totalHoursSpent;
    }

    public String getCallPurposeDesc() {
        return CallPurposeDesc;
    }

    public void setCallPurposeDesc(String callPurposeDesc) {
        CallPurposeDesc = callPurposeDesc;
    }

    public String getHistorynotes() {
        return Historynotes;
    }

    public void setHistorynotes(String historynotes) {
        Historynotes = historynotes;
    }

    public String getOutcome() {
        return Outcome;
    }

    public void setOutcome(String outcome) {
        Outcome = outcome;
    }

    public String getReasonDescription() {
        return ReasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        ReasonDescription = reasonDescription;
    }

    public String getCallPurposeDesc1() {
        return CallPurposeDesc1;
    }

    public void setCallPurposeDesc1(String callPurposeDesc1) {
        CallPurposeDesc1 = callPurposeDesc1;
    }

    public String getSchTime() {
        return SchTime;
    }

    public void setSchTime(String schTime) {
        SchTime = schTime;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public int getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(int initiatedBy) {
        this.initiatedBy = initiatedBy;
    }
}
