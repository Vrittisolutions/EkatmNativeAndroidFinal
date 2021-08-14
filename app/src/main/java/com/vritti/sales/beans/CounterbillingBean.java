package com.vritti.sales.beans;

import java.util.Date;

/**
 * Created by 300151 on 7/19/2016.
 */
public class CounterbillingBean {

    String CustName;
    String MobileNo;

    boolean discinrupees = true;

    String ItemDesc, Qty, ItemMasterid, ItemCode, Unit, Tax_inPercntg,
        Tax_inRups,  Tax_onsubtotal, paymentmode, subtotal, Subtotal_inclTax, disc_ontotal, NetAmt,
        Recvd_amt, Bal_amt, Taxclass, ItemPlantId, cgstLine, sgstLine, custGSTN, cmpnyGSTN;

    float MRP,Discount, Rate, lineamt,totAmt_incltax_lineamt, discamt, dicountedTotal;
    long timestamp;

    String billNo, dateTime, billPaybleAmount;

    public float getDiscamt() {
        return discamt;
    }

    public void setDiscamt(float discamt) {
        this.discamt = discamt;
    }

    public boolean isDiscinrupees() {
        return discinrupees;
    }

    public void setDiscinrupees(boolean discinrupees) {
        this.discinrupees = discinrupees;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float discount) {
        Discount = discount;
    }

    public float getLineamt() {
        return lineamt;
    }

    public void setLineamt(float lineamt) {
        this.lineamt = lineamt;
    }

    public String getItemMasterid() {
        return ItemMasterid;
    }

    public void setItemMasterid(String itemMasterid) {
        ItemMasterid = itemMasterid;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public float getMRP() {
        return MRP;
    }

    public void setMRP(float MRP) {
        this.MRP = MRP;
    }

    public String getTax_inPercntg() {
        return Tax_inPercntg;
    }

    public void setTax_inPercntg(String tax_inPercntg) {
        Tax_inPercntg = tax_inPercntg;
    }

    public String getTax_inRups() {
        return Tax_inRups;
    }

    public void setTax_inRups(String tax_inRups) {
        Tax_inRups = tax_inRups;
    }

    public float getTotAmt_incltax_lineamt() {
        return totAmt_incltax_lineamt;
    }

    public void setTotAmt_incltax_lineamt(float totAmt_incltax_lineamt) {
        this.totAmt_incltax_lineamt = totAmt_incltax_lineamt;
    }

    public String getTax_onsubtotal() {
        return Tax_onsubtotal;
    }

    public void setTax_onsubtotal(String tax_onsubtotal) {
        Tax_onsubtotal = tax_onsubtotal;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getSubtotal_inclTax() {
        return Subtotal_inclTax;
    }

    public void setSubtotal_inclTax(String subtotal_inclTax) {
        Subtotal_inclTax = subtotal_inclTax;
    }

    public String getDisc_ontotal() {
        return disc_ontotal;
    }

    public void setDisc_ontotal(String disc_ontotal) {
        this.disc_ontotal = disc_ontotal;
    }

    public String getNetAmt() {
        return NetAmt;
    }

    public void setNetAmt(String netAmt) {
        NetAmt = netAmt;
    }

    public String getRecvd_amt() {
        return Recvd_amt;
    }

    public void setRecvd_amt(String recvd_amt) {
        Recvd_amt = recvd_amt;
    }

    public String getBal_amt() {
        return Bal_amt;
    }

    public void setBal_amt(String bal_amt) {
        Bal_amt = bal_amt;
    }

    public String getTaxclass() {
        return Taxclass;
    }

    public void setTaxclass(String taxclass) {
        Taxclass = taxclass;
    }

    public String getItemPlantId() {    return ItemPlantId;    }

    public void setItemPlantId(String itemPlantId) {    ItemPlantId = itemPlantId;    }

    public float getDicountedTotal() {    return dicountedTotal;    }

    public void setDicountedTotal(float dicountedTotal) {     this.dicountedTotal = dicountedTotal;  }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBillPaybleAmount() {
        return billPaybleAmount;
    }

    public void setBillPaybleAmount(String billPaybleAmount) { this.billPaybleAmount = billPaybleAmount; }

    public String getCgstLine() {
        return cgstLine;
    }

    public void setCgstLine(String cgstLine) {
        this.cgstLine = cgstLine;
    }

    public String getSgstLine() {
        return sgstLine;
    }

    public void setSgstLine(String sgstLine) {
        this.sgstLine = sgstLine;
    }

    public String getCustGSTN() {
        return custGSTN;
    }

    public void setCustGSTN(String custGSTN) {
        this.custGSTN = custGSTN;
    }

    public String getCmpnyGSTN() {
        return cmpnyGSTN;
    }

    public void setCmpnyGSTN(String cmpnyGSTN) {
        this.cmpnyGSTN = cmpnyGSTN;
    }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
