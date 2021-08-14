package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class CartonDetail implements Serializable {

    String CartonHeaderId,CartonCode,CartonDetailId,Qty,ItemCode,ItemDesc,PacketNo,RefId,PacketMasterId,ItemMasterId,SoScheduleId;
    String QCStatus="";

    public String getQCStatus() {
        return QCStatus;
    }

    public void setQCStatus(String QCStatus) {
        this.QCStatus = QCStatus;
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

    public String getPacketMasterId() {
        return PacketMasterId;
    }

    public void setPacketMasterId(String packetMasterId) {
        PacketMasterId = packetMasterId;
    }

    public String getCartonHeaderId() {
        return CartonHeaderId;
    }

    public void setCartonHeaderId(String cartonHeaderId) {
        CartonHeaderId = cartonHeaderId;
    }

    public String getCartonCode() {
        return CartonCode;
    }

    public void setCartonCode(String cartonCode) {
        CartonCode = cartonCode;
    }

    public String getCartonDetailId() {
        return CartonDetailId;
    }

    public void setCartonDetailId(String cartonDetailId) {
        CartonDetailId = cartonDetailId;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
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

    public String getPacketNo() {
        return PacketNo;
    }

    public void setPacketNo(String packetNo) {
        PacketNo = packetNo;
    }

    public String getRefId() {
        return RefId;
    }

    public void setRefId(String refId) {
        RefId = refId;
    }
}
