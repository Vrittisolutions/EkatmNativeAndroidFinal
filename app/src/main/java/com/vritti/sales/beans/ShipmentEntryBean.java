package com.vritti.sales.beans;

public class ShipmentEntryBean {

    private String Consignee, SOno, ItemCode, ItemDesc, AddedDt, SODate_expDelDate, ScheduleQty, ShipmentQty, OrderType, SOHeaderID;
    private String invoiceNo,invoiceDate, ShipToMasterId, deliveryAddress;

    private String SeqNo,SOScheduleId, SODetailId, CustOrderPONo, SOHeaderStatus, ScheduleDate, SailingDate /*AddedDt*/,TaxClassMasterId, Qty, Rate,
            MOQty,ActualMOQty, ItemProcessId, Plant, WareHouse, LineAmt, LineTaxes, LineTotal, DiscPC, DiscAmount, PlantId, WareHouseId,
            ItemMasterId, UOMCode, ReqQty, SalesUnit, PurUnit, WareHouseId1, LocationMasterId, StockUnit, UOMMasterId, OrderTypeMasterId,
            Brand="",Content="",ContentUOM="",SellingUOM="",PackOfQty="",Latitude="",Longitude="",AddedBy="";

    private String PrefDelToTime,PrefDelFrmTime, DeliveryTerms = "", Mobile = "";
    String PaymentStatus="",PaymentMode="",TransactionId="",AmountStatus="",TransactionDate="";

    private Float edtQty = 0.0F;
    private Float Subtotal_Amt;

    private boolean isChecked , isEnterValue;

    public String getAddedBy() {
        return AddedBy;
    }

    public void setAddedBy(String addedBy) {
        AddedBy = addedBy;
    }

    public boolean isEnterValue() {
        return isEnterValue;
    }

    public void setEnterValue(boolean enterValue) {
        isEnterValue = enterValue;
    }

    public boolean isChecked() {  return isChecked;   }

    public void setChecked(boolean checked) {  isChecked = checked;  }

    public Float getEdtQty() {  return edtQty;  }

    public void setEdtQty(Float edtQty) {  this.edtQty = edtQty;  }

    public Float getSubtotal_Amt() {   return Subtotal_Amt;    }

    public void setSubtotal_Amt(Float subtotal) {    Subtotal_Amt = subtotal;    }

    public String getConsignee() {
        return Consignee;
    }

    public void setConsignee(String consignee) {
        Consignee = consignee;
    }

    public String getSOno() {
        return SOno;
    }

    public void setSOno(String SOno) {
        this.SOno = SOno;
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

    public String getAddedDt() {
        return AddedDt;
    }

    public void setAddedDt(String addedDt) {
        AddedDt = addedDt;
    }

    public String getSODate_expDelDate() {
        return SODate_expDelDate;
    }

    public void setSODate_expDelDate(String SODate_expDelDate) {
        this.SODate_expDelDate = SODate_expDelDate;
    }

    public String getScheduleQty() {
        return ScheduleQty;
    }

    public void setScheduleQty(String scheduleQty) {
        ScheduleQty = scheduleQty;
    }

    public String getShipmentQty() {
        return ShipmentQty;
    }

    public void setShipmentQty(String shipmentQty) {
        ShipmentQty = shipmentQty;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getSOHeaderID() {
        return SOHeaderID;
    }

    public void setSOHeaderID(String SOHeaderID) {
        this.SOHeaderID = SOHeaderID;
    }

    public String getSeqNo() {
        return SeqNo;
    }

    public void setSeqNo(String seqNo) {
        SeqNo = seqNo;
    }

    public String getSOScheduleId() {
        return SOScheduleId;
    }

    public void setSOScheduleId(String SOScheduleId) {
        this.SOScheduleId = SOScheduleId;
    }

    public String getSODetailId() {
        return SODetailId;
    }

    public void setSODetailId(String SODetailId) {
        this.SODetailId = SODetailId;
    }

    public String getCustOrderPONo() {
        return CustOrderPONo;
    }

    public void setCustOrderPONo(String custOrderPONo) {
        CustOrderPONo = custOrderPONo;
    }

    public String getSOHeaderStatus() {
        return SOHeaderStatus;
    }

    public void setSOHeaderStatus(String SOHeaderStatus) {
        this.SOHeaderStatus = SOHeaderStatus;
    }

    public String getScheduleDate() {
        return ScheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        ScheduleDate = scheduleDate;
    }

    public String getSailingDate() {
        return SailingDate;
    }

    public void setSailingDate(String sailingDate) {
        SailingDate = sailingDate;
    }

    public String getTaxClassMasterId() {
        return TaxClassMasterId;
    }

    public void setTaxClassMasterId(String taxClassMasterId) {
        TaxClassMasterId = taxClassMasterId;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) { Qty = qty; }

    public String getRate() { return Rate; }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getMOQty() {
        return MOQty;
    }

    public void setMOQty(String MOQty) {
        this.MOQty = MOQty;
    }

    public String getActualMOQty() {
        return ActualMOQty;
    }

    public void setActualMOQty(String actualMOQty) {
        ActualMOQty = actualMOQty;
    }

    public String getItemProcessId() {
        return ItemProcessId;
    }

    public void setItemProcessId(String itemProcessId) {
        ItemProcessId = itemProcessId;
    }

    public String getPlant() {
        return Plant;
    }

    public void setPlant(String plant) {
        Plant = plant;
    }

    public String getWareHouse() {
        return WareHouse;
    }

    public void setWareHouse(String wareHouse) {
        WareHouse = wareHouse;
    }

    public String getLineAmt() {
        return LineAmt;
    }

    public void setLineAmt(String lineAmt) {
        LineAmt = lineAmt;
    }

    public String getLineTaxes() {
        return LineTaxes;
    }

    public void setLineTaxes(String lineTaxes) {
        LineTaxes = lineTaxes;
    }

    public String getLineTotal() {
        return LineTotal;
    }

    public void setLineTotal(String lineTotal) {
        LineTotal = lineTotal;
    }

    public String getDiscPC() {
        return DiscPC;
    }

    public void setDiscPC(String discPC) {
        DiscPC = discPC;
    }

    public String getDiscAmount() {
        return DiscAmount;
    }

    public void setDiscAmount(String discAmount) {
        DiscAmount = discAmount;
    }

    public String getPlantId() {
        return PlantId;
    }

    public void setPlantId(String plantId) {
        PlantId = plantId;
    }

    public String getWareHouseId() {
        return WareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        WareHouseId = wareHouseId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getUOMCode() {
        return UOMCode;
    }

    public void setUOMCode(String UOMCode) {
        this.UOMCode = UOMCode;
    }

    public String getReqQty() {
        return ReqQty;
    }

    public void setReqQty(String reqQty) {
        ReqQty = reqQty;
    }

    public String getSalesUnit() {
        return SalesUnit;
    }

    public void setSalesUnit(String salesUnit) {
        SalesUnit = salesUnit;
    }

    public String getPurUnit() {
        return PurUnit;
    }

    public void setPurUnit(String purUnit) {
        PurUnit = purUnit;
    }

    public String getWareHouseId1() {
        return WareHouseId1;
    }

    public void setWareHouseId1(String wareHouseId1) {
        WareHouseId1 = wareHouseId1;
    }

    public String getLocationMasterId() {
        return LocationMasterId;
    }

    public void setLocationMasterId(String locationMasterId) {
        LocationMasterId = locationMasterId;
    }

    public String getStockUnit() {
        return StockUnit;
    }

    public void setStockUnit(String stockUnit) {
        StockUnit = stockUnit;
    }

    public String getUOMMasterId() {
        return UOMMasterId;
    }

    public void setUOMMasterId(String UOMMasterId) {
        this.UOMMasterId = UOMMasterId;
    }

    public String getOrderTypeMasterId() {
        return OrderTypeMasterId;
    }

    public void setOrderTypeMasterId(String orderTypeMasterId) {
        OrderTypeMasterId = orderTypeMasterId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getShipToMasterId() {  return ShipToMasterId;  }

    public void setShipToMasterId(String shipToMasterId) {   ShipToMasterId = shipToMasterId;  }

    public String getDeliveryAddress() {  return deliveryAddress;  }

    public void setDeliveryAddress(String deliveryAddress) {   this.deliveryAddress = deliveryAddress;  }

    public String getPrefDelToTime() {  return PrefDelToTime;   }

    public void setPrefDelToTime(String prefDelToTime) {    PrefDelToTime = prefDelToTime;   }

    public String getPrefDelFrmTime() {   return PrefDelFrmTime;   }

    public void setPrefDelFrmTime(String prefDelFrmTime) {    PrefDelFrmTime = prefDelFrmTime;   }

    public String getDeliveryTerms() { return DeliveryTerms; }

    public void setDeliveryTerms(String deliveryTerms) { DeliveryTerms = deliveryTerms; }

    public String getMobile() { return Mobile; }

    public void setMobile(String mobile) { Mobile = mobile; }

    public String getBrand() { return Brand; }

    public void setBrand(String brand) { Brand = brand; }

    public String getContent() { return Content; }

    public void setContent(String content) { Content = content; }

    public String getContentUOM() { return ContentUOM; }

    public void setContentUOM(String contentUOM) { ContentUOM = contentUOM; }

    public String getSellingUOM() { return SellingUOM; }

    public void setSellingUOM(String sellingUOM) { SellingUOM = sellingUOM; }

    public String getPackOfQty() { return PackOfQty; }

    public void setPackOfQty(String packOfQty) { PackOfQty = packOfQty; }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getPaymentMode() {
        return PaymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        PaymentMode = paymentMode;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getAmountStatus() {
        return AmountStatus;
    }

    public void setAmountStatus(String amountStatus) {
        AmountStatus = amountStatus;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }
}
