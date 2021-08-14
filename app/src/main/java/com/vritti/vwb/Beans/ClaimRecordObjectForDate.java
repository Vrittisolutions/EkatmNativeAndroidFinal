package com.vritti.vwb.Beans;

import java.util.ArrayList;

public class ClaimRecordObjectForDate {
    //ArrayList<ClaimSummayBean> claimSummayBeanArrayList;
    ArrayList<String> dateList;

    public ClaimRecordObjectForDate(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public ArrayList<String> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }
}
