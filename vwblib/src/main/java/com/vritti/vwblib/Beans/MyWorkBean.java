package com.vritti.vwblib.Beans;

/**
 * Created by 300151 on 10/14/2016.
 */
public class MyWorkBean {
    String Today;
    String Overdue;
    String Critical;
    String New;
    String NotActed;
    String Tickets;
    String TotalCount;

    public String getToday() {
        return Today;
    }

    public void setToday(String today) {
        Today = today;
    }

    public String getOverdue() {
        return Overdue;
    }

    public void setOverdue(String overdue) {
        Overdue = overdue;
    }

    public String getCritical() {
        return Critical;
    }

    public void setCritical(String critical) {
        Critical = critical;
    }

    public String getNew() {
        return New;
    }

    public void setNew(String aNew) {
        New = aNew;
    }

    public String getNotActed() {
        return NotActed;
    }

    public void setNotActed(String notActed) {
        NotActed = notActed;
    }

    public String getTickets() {
        return Tickets;
    }

    public void setTickets(String tickets) {
        Tickets = tickets;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }
}
