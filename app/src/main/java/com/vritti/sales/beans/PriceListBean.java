package com.vritti.sales.beans;

import java.util.ArrayList;

public class PriceListBean {
    String pListDesc = "", pListHdrId = "", pListCode = "";
    String quotCode = "", quotHdrId = "", quotDate = "", quotDtlId = "",ItemClassificationId = "";
    String contractCode = "", contractHdrId = "",SOContractHDRId = "",SOContractDtlId = "";
    String PriceListHdrID = "",PriceListDtlID = "",ItemDesc = "",ItemPlantid = "",ItemCode = "",UOMCode = "", UOMMasterId = "",
           DiscountPer = "",BaseRate = "", lineAmt = "", discLineAmt = "",taxLineAmt = "",qtyLine = "",taxCls = "",taxclsId = "",
           discamt = "", taxamt = "",ItemMasterId = "",MRP="";
    String FamilyId = "", FamilyCode = "", FamilyDesc = "",SalesUnit = "";
    ArrayList<TaxClassBean> taxList = new ArrayList<TaxClassBean>();
    boolean ischecked;

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public ArrayList<TaxClassBean> getTaxList() {
        return taxList;
    }

    public void setTaxList(ArrayList<TaxClassBean> taxList) {
        this.taxList = taxList;
    }

    public String getSalesUnit() { return SalesUnit; }

    public void setSalesUnit(String salesUnit) { SalesUnit = salesUnit; }

    public String getpListDesc() {
        return pListDesc;
    }

    public void setpListDesc(String pListDesc) {
        this.pListDesc = pListDesc;
    }

    public String getpListHdrId() {
        return pListHdrId;
    }

    public void setpListHdrId(String pListHdrId) {
        this.pListHdrId = pListHdrId;
    }

    public String getpListCode() {
        return pListCode;
    }

    public void setpListCode(String pListCode) {
        this.pListCode = pListCode;
    }

    public String getPriceListHdrID() {
        return PriceListHdrID;
    }

    public void setPriceListHdrID(String priceListHdrID) {
        PriceListHdrID = priceListHdrID;
    }

    public String getPriceListDtlID() {
        return PriceListDtlID;
    }

    public void setPriceListDtlID(String priceListDtlID) {
        PriceListDtlID = priceListDtlID;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getItemPlantid() {
        return ItemPlantid;
    }

    public void setItemPlantid(String itemPlantid) {
        ItemPlantid = itemPlantid;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getUOMCode() {
        return UOMCode;
    }

    public void setUOMCode(String UOMCode) {
        this.UOMCode = UOMCode;
    }

    public String getUOMMasterId() {
        return UOMMasterId;
    }

    public void setUOMMasterId(String UOMMasterId) {
        this.UOMMasterId = UOMMasterId;
    }

    public String getDiscountPer() {
        return DiscountPer;
    }

    public void setDiscountPer(String discountPer) {
        DiscountPer = discountPer;
    }

    public String getBaseRate() {
        return BaseRate;
    }

    public void setBaseRate(String baseRate) {
        BaseRate = baseRate;
    }

    public String getLineAmt() {
        return lineAmt;
    }

    public void setLineAmt(String lineAmt) {
        this.lineAmt = lineAmt;
    }

    public String getDiscLineAmt() {
        return discLineAmt;
    }

    public void setDiscLineAmt(String discLineAmt) {
        this.discLineAmt = discLineAmt;
    }

    public String getTaxLineAmt() {
        return taxLineAmt;
    }

    public void setTaxLineAmt(String taxLineAmt) {
        this.taxLineAmt = taxLineAmt;
    }

    public String getQtyLine() {
        return qtyLine;
    }

    public void setQtyLine(String qtyLine) {
        this.qtyLine = qtyLine;
    }

    public String getTaxCls() { return taxCls; }

    public void setTaxCls(String taxCls) {
        this.taxCls = taxCls;
    }

    public String getTaxclsId() {
        return taxclsId;
    }

    public void setTaxclsId(String taxclsId) {
        this.taxclsId = taxclsId;
    }

    public String getDiscamt() {
        return discamt;
    }

    public void setDiscamt(String discamt) {
        this.discamt = discamt;
    }

    public String getTaxamt() { return taxamt; }

    public void setTaxamt(String taxamt) {
        this.taxamt = taxamt;
    }

    public String getItemMasterId() { return ItemMasterId; }

    public void setItemMasterId(String itemMasterId) { ItemMasterId = itemMasterId; }

    public String getFamilyId() { return FamilyId; }

    public void setFamilyId(String familyId) { FamilyId = familyId; }

    public String getFamilyCode() { return FamilyCode; }

    public void setFamilyCode(String familyCode) { FamilyCode = familyCode; }

    public String getFamilyDesc() { return FamilyDesc; }

    public void setFamilyDesc(String familyDesc) { FamilyDesc = familyDesc; }

    public String getContractCode() { return contractCode; }

    public void setContractCode(String contractCode) { this.contractCode = contractCode; }

    public String getContractHdrId() { return contractHdrId; }

    public void setContractHdrId(String contractHdrId) { this.contractHdrId = contractHdrId; }

    public String getSOContractHDRId() { return SOContractHDRId; }

    public void setSOContractHDRId(String SOContractHDRId) { this.SOContractHDRId = SOContractHDRId; }

    public String getSOContractDtlId() { return SOContractDtlId; }

    public void setSOContractDtlId(String SOContractDtlId) { this.SOContractDtlId = SOContractDtlId; }

    public String getQuotCode() { return quotCode; }

    public void setQuotCode(String quotCode) { this.quotCode = quotCode; }

    public String getQuotHdrId() { return quotHdrId; }

    public void setQuotHdrId(String quotHdrId) { this.quotHdrId = quotHdrId; }

    public String getQuotDtlId() { return quotDtlId; }

    public void setQuotDtlId(String quotDtlId) { this.quotDtlId = quotDtlId; }

    public String getQuotDate() { return quotDate; }

    public void setQuotDate(String quotDate) { this.quotDate = quotDate; }

    public String getItemClassificationId() {
        return ItemClassificationId;
    }

    public void setItemClassificationId(String itemClassificationId) { ItemClassificationId = itemClassificationId; }

    public String getMRP() { return MRP; }

    public void setMRP(String MRP) { this.MRP = MRP; }
}
