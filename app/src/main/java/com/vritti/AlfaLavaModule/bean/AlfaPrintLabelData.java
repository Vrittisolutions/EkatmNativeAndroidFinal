package com.vritti.AlfaLavaModule.bean;

import java.io.Serializable;

public class AlfaPrintLabelData implements Serializable {


    String Itemcode,Itemdesc,PacketNo,PacketQty,GRNNo,inverdno,barcode,GRNDate;


    public String getItemcode() {
        return Itemcode;
    }

    public void setItemcode(String itemcode) {
        Itemcode = itemcode;
    }

    public String getItemdesc() {
        return Itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        Itemdesc = itemdesc;
    }

    public String getPacketNo() {
        return PacketNo;
    }

    public void setPacketNo(String packetNo) {
        PacketNo = packetNo;
    }

    public String getPacketQty() {
        return PacketQty;
    }

    public void setPacketQty(String packetQty) {
        PacketQty = packetQty;
    }

    public String getGRNNo() {
        return GRNNo;
    }

    public void setGRNNo(String GRNNo) {
        this.GRNNo = GRNNo;
    }

    public String getInverdno() {
        return inverdno;
    }

    public void setInverdno(String inverdno) {
        this.inverdno = inverdno;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getGRNDate() {
        return GRNDate;
    }

    public void setGRNDate(String GRNDate) {
        this.GRNDate = GRNDate;
    }
}
