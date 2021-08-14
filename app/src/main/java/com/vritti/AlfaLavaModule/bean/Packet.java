package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class Packet implements Serializable {

    String PacketNo,HeaderId;

    String BalQty,MovedQty,ItemCode,ItemDesc,LocationCode,LocationType;

    public String getBalQty() {
        return BalQty;
    }

    public void setBalQty(String balQty) {
        BalQty = balQty;
    }

    public String getMovedQty() {
        return MovedQty;
    }

    public void setMovedQty(String movedQty) {
        MovedQty = movedQty;
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

    public String getLocationCode() {
        return LocationCode;
    }

    public void setLocationCode(String locationCode) {
        LocationCode = locationCode;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    public String getHeaderId() {
        return HeaderId;
    }

    public void setHeaderId(String headerId) {
        HeaderId = headerId;
    }

    public String getPacketNo() {
        return PacketNo;
    }

    public void setPacketNo(String packetNo) {
        PacketNo = packetNo;
    }
}
