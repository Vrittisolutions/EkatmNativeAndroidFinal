package com.vritti.sales.beans;

public class ChargeDtlBean {
    String srNo, chargeDesc, calcMethod, qty, rate, chargeAmt,taxClsDesc, taxAmt, chargeHdrId;

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getChargeDesc() {
        return chargeDesc;
    }

    public void setChargeDesc(String chargeDesc) {
        this.chargeDesc = chargeDesc;
    }

    public String getCalcMethod() {
        return calcMethod;
    }

    public void setCalcMethod(String calcMethod) {
        this.calcMethod = calcMethod;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(String chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getTaxClsDesc() {
        return taxClsDesc;
    }

    public void setTaxClsDesc(String taxClsDesc) {
        this.taxClsDesc = taxClsDesc;
    }

    public String getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getChargeHdrId() {
        return chargeHdrId;
    }

    public void setChargeHdrId(String chargeHdrId) {
        this.chargeHdrId = chargeHdrId;
    }
}
