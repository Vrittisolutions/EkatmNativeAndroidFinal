package com.vritti.AlfaLavaModule.activity.grn;

import java.io.Serializable;

public class GRNPOST implements Serializable {

    String GRNNo,GRNHeaderId,GRNStatus,ItemCode,GRN_No,PacketNo,UOM,GRNDetailId,CustomerName,InvoiceNo;
    String PartyDCNo,SupplierId,LotNo;
    int ChallanQty=0,RejQty=0;
    String ItemMasterId;
    int Quantity=0;

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public int getRejQty() {
        return RejQty;
    }

    public void setRejQty(int rejQty) {
        RejQty = rejQty;
    }

    public String getGRNDetailId() {
        return GRNDetailId;
    }

    public void setGRNDetailId(String GRNDetailId) {
        this.GRNDetailId = GRNDetailId;
    }

    public int getChallanQty() {
        return ChallanQty;
    }

    public void setChallanQty(int challanQty) {
        ChallanQty = challanQty;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getPartyDCNo() {
        return PartyDCNo;
    }

    public void setPartyDCNo(String partyDCNo) {
        PartyDCNo = partyDCNo;
    }

    public String getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(String supplierId) {
        SupplierId = supplierId;
    }

    public String getLotNo() {
        return LotNo;
    }

    public void setLotNo(String lotNo) {
        LotNo = lotNo;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getGRN_No() {
        return GRN_No;
    }

    public void setGRN_No(String GRN_No) {
        this.GRN_No = GRN_No;
    }

    public String getPacketNo() {
        return PacketNo;
    }

    public void setPacketNo(String packetNo) {
        PacketNo = packetNo;
    }

    public String getGRNNo() {
        return GRNNo;
    }

    public void setGRNNo(String GRNNo) {
        this.GRNNo = GRNNo;
    }

    public String getGRNHeaderId() {
        return GRNHeaderId;
    }

    public void setGRNHeaderId(String GRNHeaderId) {
        this.GRNHeaderId = GRNHeaderId;
    }

    public String getGRNStatus() {
        return GRNStatus;
    }

    public void setGRNStatus(String GRNStatus) {
        this.GRNStatus = GRNStatus;
    }
}
