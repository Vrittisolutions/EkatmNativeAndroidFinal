package com.vritti.AlfaLavaModule.bean;

/**
 * Created by Admin-1 on 2/14/2017.
 */

public class PutawaysPacketBean {
   String ItemCode,Itemdesc,
    ItemPlantId,
    PacketMasterId,
    GRNDetailId,
    GRNHeaderId,
    BalQty,
    PacketNo,
    LocationMasterId,
    LocationCode,
    LocationDesc,DoneFlag;

    public String getItemdesc() {
        return Itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        Itemdesc = itemdesc;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getItemPlantId() {
        return ItemPlantId;
    }

    public void setItemPlantId(String itemPlantId) {
        ItemPlantId = itemPlantId;
    }

    public String getPacketMasterId() {
        return PacketMasterId;
    }

    public void setPacketMasterId(String packetMasterId) {
        PacketMasterId = packetMasterId;
    }

    public String getGRNDetailId() {
        return GRNDetailId;
    }

    public void setGRNDetailId(String GRNDetailId) {
        this.GRNDetailId = GRNDetailId;
    }

    public String getGRNHeaderId() {
        return GRNHeaderId;
    }

    public void setGRNHeaderId(String GRNHeaderId) {
        this.GRNHeaderId = GRNHeaderId;
    }

    public String getBalQty() {
        return BalQty;
    }

    public void setBalQty(String balQty) {
        BalQty = balQty;
    }

    public String getPacketNo() {
        return PacketNo;
    }

    public void setPacketNo(String packetNo) {
        PacketNo = packetNo;
    }

    public String getLocationMasterId() {
        return LocationMasterId;
    }

    public void setLocationMasterId(String locationMasterId) {
        LocationMasterId = locationMasterId;
    }

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getLocationDesc() {
        return LocationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        LocationDesc = locationDesc;
    }

    public String getDoneFlag() {
        return DoneFlag;
    }

    public void setDoneFlag(String doneFlag) {
        DoneFlag = doneFlag;
    }
}
