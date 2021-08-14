package com.vritti.vwb.Beans;

public class ClaimSummayBean {

    String ClaimHeaderId,
            Applicant,
            ClaimCode,
            Date,
            FormatedDate,
            ProjectName,
            plantid,
            Total,
            Status,
            EMPID,
            DeptDesc,
            PaidAmount,
            BalanceAmount;

    public String getClaimHeaderId() {
        return ClaimHeaderId;
    }

    public void setClaimHeaderId(String claimHeaderId) {
        ClaimHeaderId = claimHeaderId;
    }

    public String getApplicant() {
        return Applicant;
    }

    public void setApplicant(String applicant) {
        Applicant = applicant;
    }

    public String getClaimCode() {
        return ClaimCode;
    }

    public void setClaimCode(String claimCode) {
        ClaimCode = claimCode;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFormatedDate() {
        return FormatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        FormatedDate = formatedDate;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getPlantid() {
        return plantid;
    }

    public void setPlantid(String plantid) {
        this.plantid = plantid;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEMPID() {
        return EMPID;
    }

    public void setEMPID(String EMPID) {
        this.EMPID = EMPID;
    }

    public String getDeptDesc() {
        return DeptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        DeptDesc = deptDesc;
    }

    public String getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        BalanceAmount = balanceAmount;
    }
}
