package com.vritti.sales.beans;

public class SaleItemBean {
    String itemcode, itemdesc, taxclass, qty, rate, amtLine,discPer, discAmt, discAmtLine, taxAmtLine,finalAmtLine;
    String productAmt, totdiscAmt, prodTaxAmt,chargeAmt,chargeTaxAmt,totalAmount;
    String soNo, custId, custName, amt,soDate,SOHeaderId;
    String type;
    String taxClsId;
    boolean isEditedClicked;
    String ChildId="",SeqNo="",GLBItemDtlId="",SODetailId="",ItemMasterId="",WarrantyCode="",ProUnit="",ItemProcessId="",
            Description="",ItemClassificationId="",BillingCategoryId="",SegmentId="",RouteFrom="",RouteTo="",ProFigure="",
            PriceListHdrId="",SalesFamilyHdrId="",BQT_QuotationHeaderId="",ContractHdrId="",UOMMasterId = "",UOMCode= "";
    String ScheduleDate = "", ExVendorDate = "", BalQty = "", FinalDeliverDate = "", ItemProcessCode = "";
    String RecStartDate = "",PeriodicEndDate="",RecEndDate="",ItemSrNo="",ItemSize="0",RecurDaysCount="",RecurWeeksCount="",srno="",
            IsSunday="",IsMonday="",IsTuesday="",IsWednesday="",IsFriday="",IsThursday="",IsSaturday="",EveryMonthCount="",
            MonthlyDayNo="",MonthlyMonth="",MonthlyWeek="",MonthlyDay="",YearlyMonthName="",YearlyWeek="",YearlyDay="",YearlyMonth="",
            TypeOfPeriod="",RecurYearCount="",IsNoEndDate="",Occurrences="",IsProRata = "",AllowPartShipment="",MRP="";

    public boolean isEditedClicked() { return isEditedClicked; }

    public void setEditedClicked(boolean editedClicked) { isEditedClicked = editedClicked; }

    public String getTaxClsId() { return taxClsId; }

    public void setTaxClsId(String taxClsId) { this.taxClsId = taxClsId; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSoNo() {
        return soNo;
    }

    public String getSOHeaderId() { return SOHeaderId; }

    public void setSOHeaderId(String SOHeaderId) { this.SOHeaderId = SOHeaderId; }

    public void setSoNo(String soNo) { this.soNo = soNo; }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getSoDate() {
        return soDate;
    }

    public void setSoDate(String soDate) {
        this.soDate = soDate;
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getItemdesc() {
        return itemdesc;
    }

    public void setItemdesc(String itemdesc) {
        this.itemdesc = itemdesc;
    }

    public String getTaxclass() {
        return taxclass;
    }

    public void setTaxclass(String taxclass) {
        this.taxclass = taxclass;
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

    public String getAmtLine() {
        return amtLine;
    }

    public void setAmtLine(String amtLine) {
        this.amtLine = amtLine;
    }

    public String getDiscAmtLine() {
        return discAmtLine;
    }

    public void setDiscAmtLine(String discAmtLine) {
        this.discAmtLine = discAmtLine;
    }

    public String getTaxAmtLine() {
        return taxAmtLine;
    }

    public void setTaxAmtLine(String taxAmtLine) {
        this.taxAmtLine = taxAmtLine;
    }

    public String getFinalAmtLine() {
        return finalAmtLine;
    }

    public void setFinalAmtLine(String finalAmtLine) {
        this.finalAmtLine = finalAmtLine;
    }

    public String getProductAmt() {
        return productAmt;
    }

    public void setProductAmt(String productAmt) {
        this.productAmt = productAmt;
    }

    public String getTotdiscAmt() {
        return totdiscAmt;
    }

    public void setTotdiscAmt(String totdiscAmt) {
        this.totdiscAmt = totdiscAmt;
    }

    public String getProdTaxAmt() {
        return prodTaxAmt;
    }

    public void setProdTaxAmt(String prodTaxAmt) {
        this.prodTaxAmt = prodTaxAmt;
    }

    public String getChargeAmt() {
        return chargeAmt;
    }

    public void setChargeAmt(String chargeAmt) {
        this.chargeAmt = chargeAmt;
    }

    public String getChargeTaxAmt() {
        return chargeTaxAmt;
    }

    public void setChargeTaxAmt(String chargeTaxAmt) {
        this.chargeTaxAmt = chargeTaxAmt;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscPer() { return discPer; }

    public String getChildId() {
        return ChildId;
    }

    public void setChildId(String childId) {
        ChildId = childId;
    }

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    public String getGLBItemDtlId() {
        return GLBItemDtlId;
    }

    public void setGLBItemDtlId(String GLBItemDtlId) {
        this.GLBItemDtlId = GLBItemDtlId;
    }

    public String getSODetailId() {
        return SODetailId;
    }

    public void setSODetailId(String SODetailId) {
        this.SODetailId = SODetailId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getWarrantyCode() {
        return WarrantyCode;
    }

    public void setWarrantyCode(String warrantyCode) {
        WarrantyCode = warrantyCode;
    }

    public String getProUnit() {
        return ProUnit;
    }

    public void setProUnit(String proUnit) {
        ProUnit = proUnit;
    }

    public String getItemProcessId() {
        return ItemProcessId;
    }

    public void setItemProcessId(String itemProcessId) {
        ItemProcessId = itemProcessId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getItemClassificationId() {
        return ItemClassificationId;
    }

    public void setItemClassificationId(String itemClassificationId) {
        ItemClassificationId = itemClassificationId;
    }

    public String getBillingCategoryId() {
        return BillingCategoryId;
    }

    public void setBillingCategoryId(String billingCategoryId) {
        BillingCategoryId = billingCategoryId;
    }

    public String getSegmentId() {
        return SegmentId;
    }

    public void setSegmentId(String segmentId) {
        SegmentId = segmentId;
    }

    public String getRouteFrom() {
        return RouteFrom;
    }

    public void setRouteFrom(String routeFrom) {
        RouteFrom = routeFrom;
    }

    public String getRouteTo() {
        return RouteTo;
    }

    public void setRouteTo(String routeTo) {
        RouteTo = routeTo;
    }

    public String getProFigure() {
        return ProFigure;
    }

    public void setProFigure(String proFigure) {
        ProFigure = proFigure;
    }

    public String getPriceListHdrId() {
        return PriceListHdrId;
    }

    public void setPriceListHdrId(String priceListHdrId) {
        PriceListHdrId = priceListHdrId;
    }

    public String getSalesFamilyHdrId() {
        return SalesFamilyHdrId;
    }

    public void setSalesFamilyHdrId(String salesFamilyHdrId) {
        SalesFamilyHdrId = salesFamilyHdrId;
    }

    public String getBQT_QuotationHeaderId() {
        return BQT_QuotationHeaderId;
    }

    public void setBQT_QuotationHeaderId(String BQT_QuotationHeaderId) {
        this.BQT_QuotationHeaderId = BQT_QuotationHeaderId;
    }

    public String getContractHdrId() {
        return ContractHdrId;
    }

    public void setContractHdrId(String contractHdrId) {
        ContractHdrId = contractHdrId;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getExVendorDate() {
        return ExVendorDate;
    }

    public void setExVendorDate(String exVendorDate) {
        ExVendorDate = exVendorDate;
    }

    public String getBalQty() {
        return BalQty;
    }

    public void setBalQty(String balQty) {
        BalQty = balQty;
    }

    public String getFinalDeliverDate() {
        return FinalDeliverDate;
    }

    public void setFinalDeliverDate(String finalDeliverDate) {
        FinalDeliverDate = finalDeliverDate;
    }

    public String getItemProcessCode() {
        return ItemProcessCode;
    }

    public void setItemProcessCode(String itemProcessCode) {
        ItemProcessCode = itemProcessCode;
    }

    public String getRecStartDate() {
        return RecStartDate;
    }

    public void setRecStartDate(String recStartDate) {
        RecStartDate = recStartDate;
    }

    public String getPeriodicEndDate() {
        return PeriodicEndDate;
    }

    public void setPeriodicEndDate(String periodicEndDate) {
        PeriodicEndDate = periodicEndDate;
    }

    public String getRecEndDate() {
        return RecEndDate;
    }

    public void setRecEndDate(String recEndDate) {
        RecEndDate = recEndDate;
    }

    public String getItemSrNo() {
        return ItemSrNo;
    }

    public void setItemSrNo(String itemSrNo) {
        ItemSrNo = itemSrNo;
    }

    public String getItemSize() {
        return ItemSize;
    }

    public void setItemSize(String itemSize) {
        ItemSize = itemSize;
    }

    public String getRecurDaysCount() {
        return RecurDaysCount;
    }

    public void setRecurDaysCount(String recurDaysCount) {
        RecurDaysCount = recurDaysCount;
    }

    public String getRecurWeeksCount() {
        return RecurWeeksCount;
    }

    public void setRecurWeeksCount(String recurWeeksCount) {
        RecurWeeksCount = recurWeeksCount;
    }

    public String getSrno() {
        return srno;
    }

    public void setSrno(String srno) {
        this.srno = srno;
    }

    public String getIsSunday() {
        return IsSunday;
    }

    public void setIsSunday(String isSunday) {
        IsSunday = isSunday;
    }

    public String getIsMonday() {
        return IsMonday;
    }

    public void setIsMonday(String isMonday) {
        IsMonday = isMonday;
    }

    public String getIsTuesday() {
        return IsTuesday;
    }

    public void setIsTuesday(String isTuesday) {
        IsTuesday = isTuesday;
    }

    public String getIsWednesday() {
        return IsWednesday;
    }

    public void setIsWednesday(String isWednesday) {
        IsWednesday = isWednesday;
    }

    public String getIsFriday() {
        return IsFriday;
    }

    public void setIsFriday(String isFriday) {
        IsFriday = isFriday;
    }

    public String getIsThursday() {
        return IsThursday;
    }

    public void setIsThursday(String isThursday) {
        IsThursday = isThursday;
    }

    public String getIsSaturday() {
        return IsSaturday;
    }

    public void setIsSaturday(String isSaturday) {
        IsSaturday = isSaturday;
    }

    public String getEveryMonthCount() {
        return EveryMonthCount;
    }

    public void setEveryMonthCount(String everyMonthCount) {
        EveryMonthCount = everyMonthCount;
    }

    public String getMonthlyDayNo() {
        return MonthlyDayNo;
    }

    public void setMonthlyDayNo(String monthlyDayNo) {
        MonthlyDayNo = monthlyDayNo;
    }

    public String getMonthlyMonth() {
        return MonthlyMonth;
    }

    public void setMonthlyMonth(String monthlyMonth) {
        MonthlyMonth = monthlyMonth;
    }

    public String getMonthlyWeek() {
        return MonthlyWeek;
    }

    public void setMonthlyWeek(String monthlyWeek) {
        MonthlyWeek = monthlyWeek;
    }

    public String getMonthlyDay() {
        return MonthlyDay;
    }

    public void setMonthlyDay(String monthlyDay) {
        MonthlyDay = monthlyDay;
    }

    public String getYearlyMonthName() {
        return YearlyMonthName;
    }

    public void setYearlyMonthName(String yearlyMonthName) {
        YearlyMonthName = yearlyMonthName;
    }

    public String getYearlyWeek() {
        return YearlyWeek;
    }

    public void setYearlyWeek(String yearlyWeek) {
        YearlyWeek = yearlyWeek;
    }

    public String getYearlyDay() {
        return YearlyDay;
    }

    public void setYearlyDay(String yearlyDay) {
        YearlyDay = yearlyDay;
    }

    public String getYearlyMonth() {
        return YearlyMonth;
    }

    public void setYearlyMonth(String yearlyMonth) {
        YearlyMonth = yearlyMonth;
    }

    public String getTypeOfPeriod() {
        return TypeOfPeriod;
    }

    public void setTypeOfPeriod(String typeOfPeriod) {
        TypeOfPeriod = typeOfPeriod;
    }

    public String getRecurYearCount() {
        return RecurYearCount;
    }

    public void setRecurYearCount(String recurYearCount) {
        RecurYearCount = recurYearCount;
    }

    public String getIsNoEndDate() {
        return IsNoEndDate;
    }

    public void setIsNoEndDate(String isNoEndDate) {
        IsNoEndDate = isNoEndDate;
    }

    public String getOccurrences() {
        return Occurrences;
    }

    public void setOccurrences(String occurrences) {
        Occurrences = occurrences;
    }

    public String getUOMMasterId() {
        return UOMMasterId;
    }

    public void setUOMMasterId(String UOMMasterId) {
        this.UOMMasterId = UOMMasterId;
    }

    public String getIsProRata() {
        return IsProRata;
    }

    public void setIsProRata(String isProRata) {
        IsProRata = isProRata;
    }

    public String getAllowPartShipment() {
        return AllowPartShipment;
    }

    public void setAllowPartShipment(String allowPartShipment) { AllowPartShipment = allowPartShipment; }

    public void setDiscPer(String discPer) { this.discPer = discPer; }

    public String getUOMCode() { return UOMCode; }

    public void setUOMCode(String UOMCode) { this.UOMCode = UOMCode; }

    public String getDiscAmt() { return discAmt; }

    public void setDiscAmt(String discAmt) { this.discAmt = discAmt; }

    public String getMRP() { return MRP; }

    public void setMRP(String MRP) { this.MRP = MRP; }
}
