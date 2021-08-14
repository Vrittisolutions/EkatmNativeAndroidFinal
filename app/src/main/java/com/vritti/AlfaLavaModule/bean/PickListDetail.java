package com.vritti.AlfaLavaModule.bean;

import com.google.gson.annotations.SerializedName;

public class PickListDetail {
    @SerializedName("Pick_ListHdrId")
    String Pick_ListHdrId;

    @SerializedName("PickListNo")
    String PickListNo;

    @SerializedName("Pick_ListDtlId")
    String Pick_ListDtlId;

    @SerializedName("SoScheduleId")
    String SoScheduleId;

    @SerializedName("QtyPicked")
    String QtyPicked;

    @SerializedName("QtyToPick")
    String QtyToPick;

    @SerializedName("StockDetailsId")
    String StockDetailsId;

    @SerializedName("Flag")
    String Flag;

    @SerializedName("QtyPickPosted")
    String QtyPickPosted;

    @SerializedName("ItemMasterId")
    String ItemMasterId;


    @SerializedName("Pick_listSuggLotId")
    String Pick_listSuggLotId;


    @SerializedName("Pick_listDtlId")
    String Pick_listDtlId;


    @SerializedName("ItemCode")
    String ItemCode;

    @SerializedName("ItemDesc")
    String ItemDesc;

    @SerializedName("LocationCode")
    String LocationCode;


    @SerializedName("DONumber")
    String DONumber;


    public String getDONumber() {
        return DONumber;
    }

    public void setDONumber(String DONumber) {
        this.DONumber = DONumber;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getPick_listSuggLotId() {
        return Pick_listSuggLotId;
    }

    public void setPick_listSuggLotId(String pick_listSuggLotId) {
        Pick_listSuggLotId = pick_listSuggLotId;
    }




    public String getPick_listDtlId() {
        return Pick_listDtlId;
    }

    public void setPick_listDtlId(String pick_listDtlId) {
        Pick_listDtlId = pick_listDtlId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getPick_ListHdrId() {
        return Pick_ListHdrId;
    }

    public void setPick_ListHdrId(String pick_ListHdrId) {
        Pick_ListHdrId = pick_ListHdrId;
    }

    public String getPickListNo() {
        return PickListNo;
    }

    public void setPickListNo(String pickListNo) {
        PickListNo = pickListNo;
    }

    public String getPick_ListDtlId() {
        return Pick_ListDtlId;
    }

    public void setPick_ListDtlId(String pick_ListDtlId) {
        Pick_ListDtlId = pick_ListDtlId;
    }

    public String getSoScheduleId() {
        return SoScheduleId;
    }

    public void setSoScheduleId(String soScheduleId) {
        SoScheduleId = soScheduleId;
    }

    public String getQtyPicked() {
        return QtyPicked;
    }

    public void setQtyPicked(String qtyPicked) {
        QtyPicked = qtyPicked;
    }

    public String getQtyToPick() {
        return QtyToPick;
    }

    public void setQtyToPick(String qtyToPick) {
        QtyToPick = qtyToPick;
    }

    public String getStockDetailsId() {
        return StockDetailsId;
    }

    public void setStockDetailsId(String stockDetailsId) {
        StockDetailsId = stockDetailsId;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getQtyPickPosted() {
        return QtyPickPosted;
    }

    public void setQtyPickPosted(String qtyPickPosted) {
        QtyPickPosted = qtyPickPosted;
    }
}
