package com.vritti.sales.beans;

public class ScheduleListBean {
    String ScheduleId = "", startDate = "", endDate = "", custDoorDate = "", qty = "";
    boolean isPeriodic;
    int schId;

    public ScheduleListBean(String startDate, String endDate, String custDoorDate, String qty, boolean isPeriodic) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.custDoorDate = custDoorDate;
        this.qty = qty;
        this.isPeriodic = isPeriodic;
    }

    public int getSchId() {
        return schId;
    }

    public void setSchId(int schId) {
        this.schId = schId;
    }

    public void setScheduleId(String scheduleId) {
        ScheduleId = scheduleId;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCustDoorDate() {
        return custDoorDate;
    }

    public String getQty() {
        return qty;
    }

    public boolean isPeriodic() {
        return isPeriodic;
    }
}
