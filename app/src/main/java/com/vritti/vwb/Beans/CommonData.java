package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 16-Apr-18.
 */

public class CommonData implements Serializable {


    String ItemDesc,ItemMasterId,IsExpired,ExpDate,StartDate;

    public CommonData() {

    }

    public CommonData(String itemDesc, String itemMasterId) {
        ItemDesc = itemDesc;
        ItemMasterId = itemMasterId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(String isExpired) {
        IsExpired = isExpired;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }
}
