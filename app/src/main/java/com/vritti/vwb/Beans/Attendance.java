package com.vritti.vwb.Beans;

import java.io.Serializable;

public class Attendance implements Serializable {

    String LeaveCode="",Date1="",Date="",AttendanceCode="",IsApproved=""
            ,Remarks="",StartDt="",LeaveType="",Reason="",EndTime="",WorkHours="",
            MainDiff="",Calls="",Visits="",Mails="";
    String BioInTime="",BioDiff="",BioOutTime="";
    String PKAttendanceId="";

    public String getPKAttendanceId() {
        return PKAttendanceId;
    }

    public void setPKAttendanceId(String PKAttendanceId) {
        this.PKAttendanceId = PKAttendanceId;
    }

    public String getCalls() {
        return Calls;
    }

    public void setCalls(String calls) {
        Calls = calls;
    }

    public String getVisits() {
        return Visits;
    }

    public void setVisits(String visits) {
        Visits = visits;
    }

    public String getMails() {
        return Mails;
    }

    public void setMails(String mails) {
        Mails = mails;
    }

    public String getBioInTime() {
        return BioInTime;
    }

    public void setBioInTime(String bioInTime) {
        BioInTime = bioInTime;
    }

    public String getBioDiff() {
        return BioDiff;
    }

    public void setBioDiff(String bioDiff) {
        BioDiff = bioDiff;
    }

    public String getBioOutTime() {
        return BioOutTime;
    }

    public void setBioOutTime(String bioOutTime) {
        BioOutTime = bioOutTime;
    }

    public String getLeaveCode() {
        return LeaveCode;
    }

    public void setLeaveCode(String leaveCode) {
        LeaveCode = leaveCode;
    }

    public String getDate1() {
        return Date1;
    }

    public void setDate1(String date1) {
        Date1 = date1;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAttendanceCode() {
        return AttendanceCode;
    }

    public void setAttendanceCode(String attendanceCode) {
        AttendanceCode = attendanceCode;
    }

    public String getIsApproved() {
        return IsApproved;
    }

    public void setIsApproved(String isApproved) {
        IsApproved = isApproved;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getStartDt() {
        return StartDt;
    }

    public void setStartDt(String startDt) {
        StartDt = startDt;
    }

    public String getLeaveType() {
        return LeaveType;
    }

    public void setLeaveType(String leaveType) {
        LeaveType = leaveType;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getWorkHours() {
        return WorkHours;
    }

    public void setWorkHours(String workHours) {
        WorkHours = workHours;
    }

    public String getMainDiff() {
        return MainDiff;
    }

    public void setMainDiff(String mainDiff) {
        MainDiff = mainDiff;
    }
}
