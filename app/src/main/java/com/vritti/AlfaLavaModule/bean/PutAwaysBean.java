package com.vritti.AlfaLavaModule.bean;

/**
 * Created by Admin-1 on 9/22/2016.
 */

//{\"SuggPutAwayId\":\"6CB4AE88-2869-450A-B1AE-D3F0B8A20B1B\",\"GRNDetailId\":\"586\",
// \"LocationMasterId\":\"384\",\"ItemCode\":\"325723\",\"ItemDesc\":\"CHOCLAIRS 3.8G X 155 UNITS IN SN PTJAR\",
// \"LocationCode\":\"AR03B0P1\",\"PutAwayQty\":11.0000}
public class PutAwaysBean {
    int PutAwaySr;
    String GRN_Number;
    String GRN_Header;
    String IsPacket;
    String SuggPutAwayId;
    String GRNDetailId;
    String LocationMasterId;
    String ItemCode;
    String ItemDesc;
    String LocationCode;
    String PutAwayQty;
    String flgDone;
    String flgInsertUpdate;
    String CustVendorName,NoOfPacket,GRNQty,InverdTime,VehicleNo;

    public String getCustVendorName() {
        return CustVendorName;
    }

    public void setCustVendorName(String custVendorName) {
        CustVendorName = custVendorName;
    }

    public String getNoOfPacket() {
        return NoOfPacket;
    }

    public void setNoOfPacket(String noOfPacket) {
        NoOfPacket = noOfPacket;
    }

    public String getGRNQty() {
        return GRNQty;
    }

    public void setGRNQty(String GRNQty) {
        this.GRNQty = GRNQty;
    }

    public String getInverdTime() {
        return InverdTime;
    }

    public void setInverdTime(String inverdTime) {
        InverdTime = inverdTime;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public int getPutAwaySr() {
        return PutAwaySr;
    }

    public void setPutAwaySr(int putAwaySr) {
        PutAwaySr = putAwaySr;
    }

    public String getGRN_Number() {
        return GRN_Number;
    }

    public void setGRN_Number(String GRN_Number) {
        this.GRN_Number = GRN_Number;
    }

    public String getGRN_Header() {
        return GRN_Header;
    }

    public void setGRN_Header(String GRN_Header) {
        this.GRN_Header = GRN_Header;
    }

    public String getIsPacket() {
        return IsPacket;
    }

    public void setIsPacket(String isPacket) {
        IsPacket = isPacket;
    }

    public String getSuggPutAwayId() {
        return SuggPutAwayId;
    }

    public void setSuggPutAwayId(String suggPutAwayId) {
        SuggPutAwayId = suggPutAwayId;
    }

    public String getGRNDetailId() {
        return GRNDetailId;
    }

    public void setGRNDetailId(String GRNDetailId) {
        this.GRNDetailId = GRNDetailId;
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

    public String getPutAwayQty() {
        return PutAwayQty;
    }

    public void setPutAwayQty(String putAwayQty) {
        PutAwayQty = putAwayQty;
    }

    public String getFlgDone() {
        return flgDone;
    }

    public void setFlgDone(String flgDone) {
        this.flgDone = flgDone;
    }

    public String getFlgInsertUpdate() {
        return flgInsertUpdate;
    }

    public void setFlgInsertUpdate(String flgInsertUpdate) {
        this.flgInsertUpdate = flgInsertUpdate;
    }

    public String getLocationMasterId() {
        return LocationMasterId;
    }

    public void setLocationMasterId(String locationMasterId) {
        LocationMasterId = locationMasterId;
    }
}
