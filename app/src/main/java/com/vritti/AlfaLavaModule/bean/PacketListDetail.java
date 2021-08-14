package com.vritti.AlfaLavaModule.bean;

import com.google.gson.annotations.SerializedName;

public class PacketListDetail {
    @SerializedName("Pack_OrdHdrId")
    String Pack_OrdHdrId;

    @SerializedName("Pick_ListHdrId")
    String Pick_ListHdrId;

    @SerializedName("Pack_OrdDtlId")
    String Pack_OrdDtlId;

    @SerializedName("Pick_ListDtlId")
    String Pick_ListDtlId;

    @SerializedName("ItemMasterId")
    String ItemMasterId;

    @SerializedName("QtyToPack")
    int QtyToPack;

    @SerializedName("QtyPacked")
    int QtyPacked;

    @SerializedName("Flag")
    String Flag;

    @SerializedName("ItemCode")
    String ItemCode;

    @SerializedName("ItemDesc")
    String ItemDesc;

    @SerializedName("SoScheduleId")
    String SoScheduleId;

    @SerializedName("LocationCode")
    String LocationCode;

    @SerializedName("PackOrderNo")
    String PackOrderNo;


    public String getPackOrderNo() {
        return PackOrderNo;
    }

    public void setPackOrderNo(String packOrderNo) {
        PackOrderNo = packOrderNo;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }


    public String getSoScheduleId() {
        return SoScheduleId;
    }

    public void setSoScheduleId(String soScheduleId) {
        SoScheduleId = soScheduleId;
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

    public String getPack_OrdHdrId() {
        return Pack_OrdHdrId;
    }

    public void setPack_OrdHdrId(String pack_OrdHdrId) {
        Pack_OrdHdrId = pack_OrdHdrId;
    }

    public String getPick_ListHdrId() {
        return Pick_ListHdrId;
    }

    public void setPick_ListHdrId(String pick_ListHdrId) {
        Pick_ListHdrId = pick_ListHdrId;
    }

    public String getPack_OrdDtlId() {
        return Pack_OrdDtlId;
    }

    public void setPack_OrdDtlId(String pack_OrdDtlId) {
        Pack_OrdDtlId = pack_OrdDtlId;
    }

    public String getPick_ListDtlId() {
        return Pick_ListDtlId;
    }

    public void setPick_ListDtlId(String pick_ListDtlId) {
        Pick_ListDtlId = pick_ListDtlId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public int getQtyToPack() {
        return QtyToPack;
    }

    public void setQtyToPack(int qtyToPack) {
        QtyToPack = qtyToPack;
    }

    public int getQtyPacked() {
        return QtyPacked;
    }

    public void setQtyPacked(int qtyPacked) {
        QtyPacked = qtyPacked;
    }
}
