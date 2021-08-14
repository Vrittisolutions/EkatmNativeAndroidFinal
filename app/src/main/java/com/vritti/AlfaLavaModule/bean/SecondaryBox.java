package com.vritti.AlfaLavaModule.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SecondaryBox implements Serializable {

    String Pack_OrdHdrId,Pack_OrdDtlId,ItemCode,ItemDesc,WareHouseMasterId,WarehouseCode,
    Pack_OrdStatus,NoofHUToPack,NoofHUPacked,Pick_ListHdrId,Pick_ListDtlId,ItemMasterId,
     SoScheduleId,Flag;
    String LocationCode;
    int QtyToPack=0,QtyPacked=0;


    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public String getSoScheduleId() {
        return SoScheduleId;
    }

    public void setSoScheduleId(String soScheduleId) {
        SoScheduleId = soScheduleId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getPick_ListDtlId() {
        return Pick_ListDtlId;
    }

    public void setPick_ListDtlId(String pick_ListDtlId) {
        Pick_ListDtlId = pick_ListDtlId;
    }

    public String getPick_ListHdrId() {
        return Pick_ListHdrId;
    }

    public void setPick_ListHdrId(String pick_ListHdrId) {
        Pick_ListHdrId = pick_ListHdrId;
    }

    public String getPack_OrdHdrId() {
        return Pack_OrdHdrId;
    }

    public void setPack_OrdHdrId(String pack_OrdHdrId) {
        Pack_OrdHdrId = pack_OrdHdrId;
    }

    public String getPack_OrdDtlId() {
        return Pack_OrdDtlId;
    }

    public void setPack_OrdDtlId(String pack_OrdDtlId) {
        Pack_OrdDtlId = pack_OrdDtlId;
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

    public String getWareHouseMasterId() {
        return WareHouseMasterId;
    }

    public void setWareHouseMasterId(String wareHouseMasterId) {
        WareHouseMasterId = wareHouseMasterId;
    }

    public String getWarehouseCode() {
        return WarehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        WarehouseCode = warehouseCode;
    }

    public String getPack_OrdStatus() {
        return Pack_OrdStatus;
    }

    public void setPack_OrdStatus(String pack_OrdStatus) {
        Pack_OrdStatus = pack_OrdStatus;
    }

    public String getNoofHUToPack() {
        return NoofHUToPack;
    }

    public void setNoofHUToPack(String noofHUToPack) {
        NoofHUToPack = noofHUToPack;
    }

    public String getNoofHUPacked() {
        return NoofHUPacked;
    }

    public void setNoofHUPacked(String noofHUPacked) {
        NoofHUPacked = noofHUPacked;
    }


    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
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
