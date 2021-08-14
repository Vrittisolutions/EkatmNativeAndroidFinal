package com.vritti.vwb.Beans;

import java.io.Serializable;

/**
 * Created by sharvari on 28-Jun-18.
 */

public class AssetTransfer implements Serializable {

    String AssetNo,AssetTypeDesc,InvoiceNo,ModelNo,DealerName,DateOfPurchase,WarrantyDate,SerialNo,PKAssetId;

    public String getAssetNo() {
        return AssetNo;
    }

    public void setAssetNo(String assetNo) {
        AssetNo = assetNo;
    }

    public String getAssetTypeDesc() {
        return AssetTypeDesc;
    }

    public void setAssetTypeDesc(String assetTypeDesc) {
        AssetTypeDesc = assetTypeDesc;
    }

    public String getInvoiceNo() {
        return InvoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        InvoiceNo = invoiceNo;
    }

    public String getModelNo() {
        return ModelNo;
    }

    public void setModelNo(String modelNo) {
        ModelNo = modelNo;
    }

    public String getDealerName() {
        return DealerName;
    }

    public void setDealerName(String dealerName) {
        DealerName = dealerName;
    }

    public String getDateOfPurchase() {
        return DateOfPurchase;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        DateOfPurchase = dateOfPurchase;
    }

    public String getWarrantyDate() {
        return WarrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        WarrantyDate = warrantyDate;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getPKAssetId() {
        return PKAssetId;
    }

    public void setPKAssetId(String PKAssetId) {
        this.PKAssetId = PKAssetId;
    }
}
