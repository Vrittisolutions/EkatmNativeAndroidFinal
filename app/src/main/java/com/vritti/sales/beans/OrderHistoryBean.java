package com.vritti.sales.beans;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by sharvari on 2/25/2016.
 */
public class OrderHistoryBean implements Comparable<OrderHistoryBean> {
    String Address, City, ConsigneeName, CustomerMasterId, ItemMasterId, Mobile,SODate, SOHeaderId, ItemDesc, SODetailID, SOScheduleID;
    String merchantid, merchantname, state, statusname, SalesHeaderId, DispatchNo, salesdtlno, Shipstatus ;
    String DODisptch, DORcvd, status, DoAck, DObkd, DOrej, placeOrderDate, DOApprvd, SONo, DOShortClose,
            categoryid, categoryname, subcatid, subcatname,ItemImgPath,UOMDigit, catImgPath = "", subCatImgPath = "",ShipToMasterId="",
            prfDelFrmTime="",prfDelToTime="",ApprQty="";
    float Qty, Rate, NetAmt, LineAmt, DispQty, RecvQty, Disp_lineamt, DispNetAmnt,rateinrangecase, mrpinrangecase;
    boolean isChecked;
    String OrgQty,DeliveryTerms,minordqty,maxordqty,distance,UOMCode = "",outofstock,sellingrate,range,YouSave,IsDelivery="N";
    float mrp;

    String Brand="", Content="", ContentUOM="",SellingUOM="",PackOfQty="0",UPIMerch="",PaymentStatus="",PaymentMode="",
            TransactionId="",AmountStatus="",TransactionDate="",MerchAddress="",merchant_Mobile="";

    String FreeAboveAmt="",FreeDelyMaxDist="",MinDelyKg="",MinDelyKm="",ExprDelyWithinMin="",ExpressDelyChg="",Open_slots="",
            OpenTime1="",CloseTime1="",OpenTime2="",CloseTime2="", MerchLatitude="",MerchLongitude="";
    String custLat="",custLng="";

    public OrderHistoryBean(ArrayList<OrderHistoryBean> arrayList) {
    }

    public OrderHistoryBean(){

    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getSubcatid() {
        return subcatid;
    }

    public void setSubcatid(String subcatid) {
        this.subcatid = subcatid;
    }

    public String getSubcatname() {
        return subcatname;
    }

    public void setSubcatname(String subcatname) {
        this.subcatname = subcatname;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getShipstatus() {
        return Shipstatus;
    }

    public void setShipstatus(String shipstatus) {
        Shipstatus = shipstatus;
    }

    public String getSalesdtlno() {
        return salesdtlno;
    }

    public void setSalesdtlno(String salesdtlno) {
        this.salesdtlno = salesdtlno;
    }

    public String getDOShortClose() { return DOShortClose; }

    public void setDOShortClose(String DOShortClose) { this.DOShortClose = DOShortClose; }

    public String getSONo() {
        return SONo;
    }

    public void setSONo(String SONo) {
        this.SONo = SONo;
    }

    public float getDispNetAmnt() {
        return DispNetAmnt;
    }

    public void setDispNetAmnt(float dispNetAmnt) {
        DispNetAmnt = dispNetAmnt;
    }

    public String getDispatchNo() {
        return DispatchNo;
    }

    public void setDispatchNo(String dispatchNo) {
        DispatchNo = dispatchNo;
    }

    public String getSalesHeaderId() {
        return SalesHeaderId;
    }

    public void setSalesHeaderId(String salesHeaderId) {
        SalesHeaderId = salesHeaderId;
    }

    public float getDisp_lineamt(){ return Disp_lineamt; }

    public void setDisp_lineamt(float disp_lineamt) { Disp_lineamt = disp_lineamt; }

    public float getDispQty() { return DispQty; }

    public void setDispQty(float dispQty) { DispQty = dispQty; }

    public float getRecvQty() { return RecvQty; }

    public void setRecvQty(float recvQty) { RecvQty = recvQty; }

    public String getSOScheduleID() {
        return SOScheduleID;
    }

    public void setSOScheduleID(String SOScheduleID) {
        this.SOScheduleID = SOScheduleID;
    }

    public String getSODetailID() { return SODetailID; }

    public void setSODetailID(String SODetailID) {
        this.SODetailID = SODetailID;
    }

    public String getItemDesc() {
        return ItemDesc;
    }

    public void setItemDesc(String itemDesc) {
        ItemDesc = itemDesc;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public float getNetAmt() {
        return NetAmt;
    }

    public void setNetAmt(float netAmt) {
        NetAmt = netAmt;
    }

    public float getLineAmt() {
        return LineAmt;
    }

    public void setLineAmt(float lineAmt) {
        LineAmt = lineAmt;
    }

    public String getDObkd() {
        return DObkd;
    }

    public void setDObkd(String DObkd) {
        this.DObkd = DObkd;
    }

    public String getDOrej() {
        return DOrej;
    }

    public void setDOrej(String DOrej) {
        this.DOrej = DOrej;
    }

    public String getPlaceOrderDate() {
        return placeOrderDate;
    }

    public String getDOApprvd() { return DOApprvd;  }

    public void setDOApprvd(String DOApprvd) { this.DOApprvd = DOApprvd; }

    public void setPlaceOrderDate(String placeOrderDate) {
        this.placeOrderDate = placeOrderDate;
    }

    public String getDODisptch() {
        return DODisptch;
    }

    public void setDODisptch(String DODisptch) {
        this.DODisptch = DODisptch;
    }

    public String getDORcvd() {
        return DORcvd;
    }

    public void setDORcvd(String DORcvd) {
        this.DORcvd = DORcvd;
    }

    public String getStatusname() { return statusname;  }

    public void setStatusname(String statusname) { this.statusname = statusname;   }

    public String getStatus() {   return status; }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDoAck() {  return DoAck;  }

    public void setDoAck(String doAck) {
        DoAck = doAck;
    }

    public String getSOHeaderId() {
        return SOHeaderId;
    }

    public void setSOHeaderId(String SOHeaderId) {
        this.SOHeaderId = SOHeaderId;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getConsigneeName() {
        return ConsigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        ConsigneeName = consigneeName;
    }

    public String getCustomerMasterId() {
        return CustomerMasterId;
    }

    public void setCustomerMasterId(String customerMasterId) {
        CustomerMasterId = customerMasterId;
    }

    public String getItemMasterId() {
        return ItemMasterId;
    }

    public void setItemMasterId(String itemMasterId) {
        ItemMasterId = itemMasterId;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public float getQty() {
        return Qty;
    }

    public void setQty(float qty) {
        Qty = qty;
    }

    public float getRate() {
        return Rate;
    }

    public void setRate(float rate) {
        Rate = rate;
    }

    public String getSODate() {
        return SODate;
    }

    public void setSODate(String SODate) {
        this.SODate = SODate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    @Override
    public int compareTo(@NonNull OrderHistoryBean o) {
        return 0;
    }

    public String getItemImgPath() { return ItemImgPath; }

    public void setItemImgPath(String itemImgPath) { ItemImgPath = itemImgPath; }

    public String getOrgQty() { return OrgQty; }

    public void setOrgQty(String orgQty) { OrgQty = orgQty; }

    public String getDeliveryTerms() { return DeliveryTerms; }

    public void setDeliveryTerms(String deliveryTerms) { DeliveryTerms = deliveryTerms; }

    public String getMinordqty() { return minordqty; }

    public void setMinordqty(String minordqty) { this.minordqty = minordqty; }

    public String getMaxordqty() { return maxordqty; }

    public void setMaxordqty(String maxordqty) { this.maxordqty = maxordqty; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getUOMCode() { return UOMCode; }

    public void setUOMCode(String UOMCode) { this.UOMCode = UOMCode; }

    public String getOutofstock() { return outofstock; }

    public void setOutofstock(String outofstock) { this.outofstock = outofstock; }

    public float getMrp() { return mrp; }

    public void setMrp(float mrp) { this.mrp = mrp; }

    public String getSellingrate() { return sellingrate; }

    public void setSellingrate(String sellingrate) { this.sellingrate = sellingrate; }

    public String getRange() { return range; }

    public void setRange(String range) { this.range = range; }

    public String getYouSave() {
        return YouSave;
    }

    public void setYouSave(String youSave) {
        YouSave = youSave;
    }

    public String getUOMDigit() { return UOMDigit; }

    public void setUOMDigit(String UOMDigit) { this.UOMDigit = UOMDigit; }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getContentUOM() {
        return ContentUOM;
    }

    public void setContentUOM(String contentUOM) {
        ContentUOM = contentUOM;
    }

    public String getSellingUOM() {
        return SellingUOM;
    }

    public void setSellingUOM(String sellingUOM) {
        SellingUOM = sellingUOM;
    }

    public String getPackOfQty() { return PackOfQty; }

    public void setPackOfQty(String packOfQty) { PackOfQty = packOfQty; }

    public String getUPIMerch() { return UPIMerch; }

    public void setUPIMerch(String UPIMerch) { this.UPIMerch = UPIMerch; }

    public String getPaymentStatus() { return PaymentStatus; }

    public void setPaymentStatus(String paymentStatus) { PaymentStatus = paymentStatus; }

    public String getPaymentMode() { return PaymentMode; }

    public void setPaymentMode(String paymentMode) { PaymentMode = paymentMode; }

    public String getTransactionId() { return TransactionId; }

    public void setTransactionId(String transactionId) { TransactionId = transactionId; }

    public String getAmountStatus() { return AmountStatus; }

    public void setAmountStatus(String amountStatus) { AmountStatus = amountStatus; }

    public String getTransactionDate() { return TransactionDate; }

    public void setTransactionDate(String transactionDate) { TransactionDate = transactionDate; }

    public String getMerchAddress() { return MerchAddress; }

    public void setMerchAddress(String merchAddress) { MerchAddress = merchAddress; }

    public String getMerchant_Mobile() { return merchant_Mobile; }

    public void setMerchant_Mobile(String merchant_Mobile) { this.merchant_Mobile = merchant_Mobile; }

    public String getFreeAboveAmt() {
        return FreeAboveAmt;
    }

    public void setFreeAboveAmt(String freeAboveAmt) {
        FreeAboveAmt = freeAboveAmt;
    }

    public String getFreeDelyMaxDist() {
        return FreeDelyMaxDist;
    }

    public void setFreeDelyMaxDist(String freeDelyMaxDist) {
        FreeDelyMaxDist = freeDelyMaxDist;
    }

    public String getMinDelyKg() {
        return MinDelyKg;
    }

    public void setMinDelyKg(String minDelyKg) {
        MinDelyKg = minDelyKg;
    }

    public String getMinDelyKm() {
        return MinDelyKm;
    }

    public void setMinDelyKm(String minDelyKm) {
        MinDelyKm = minDelyKm;
    }

    public String getExprDelyWithinMin() {
        return ExprDelyWithinMin;
    }

    public void setExprDelyWithinMin(String exprDelyWithinMin) {
        ExprDelyWithinMin = exprDelyWithinMin;
    }

    public String getExpressDelyChg() {
        return ExpressDelyChg;
    }

    public void setExpressDelyChg(String expressDelyChg) {
        ExpressDelyChg = expressDelyChg;
    }

    public String getOpen_slots() {
        return Open_slots;
    }

    public void setOpen_slots(String open_slots) {
        Open_slots = open_slots;
    }

    public String getOpenTime1() {
        return OpenTime1;
    }

    public void setOpenTime1(String openTime1) {
        OpenTime1 = openTime1;
    }

    public String getCloseTime1() {
        return CloseTime1;
    }

    public void setCloseTime1(String closeTime1) {
        CloseTime1 = closeTime1;
    }

    public String getOpenTime2() {
        return OpenTime2;
    }

    public void setOpenTime2(String openTime2) {
        OpenTime2 = openTime2;
    }

    public String getCloseTime2() {
        return CloseTime2;
    }

    public void setCloseTime2(String closeTime2) {
        CloseTime2 = closeTime2;
    }

    public String getCatImgPath() { return catImgPath; }

    public void setCatImgPath(String catImgPath) { this.catImgPath = catImgPath; }

    public String getSubCatImgPath() { return subCatImgPath; }

    public void setSubCatImgPath(String subCatImgPath) { this.subCatImgPath = subCatImgPath; }

    public String getIsDelivery() {
        return IsDelivery;
    }

    public void setIsDelivery(String isDelivery) { IsDelivery = isDelivery; }

    public String getMerchLatitude() { return MerchLatitude; }

    public void setMerchLatitude(String merchLatitude) { MerchLatitude = merchLatitude; }

    public String getMerchLongitude() { return MerchLongitude; }

    public void setMerchLongitude(String merchLongitude) { MerchLongitude = merchLongitude; }

    public String getShipToMasterId() {
        return ShipToMasterId;
    }

    public void setShipToMasterId(String shipToMasterId) {
        ShipToMasterId = shipToMasterId;
    }

    public String getPrfDelFrmTime() {
        return prfDelFrmTime;
    }

    public void setPrfDelFrmTime(String prfDelFrmTime) {
        this.prfDelFrmTime = prfDelFrmTime;
    }

    public String getPrfDelToTime() {
        return prfDelToTime;
    }

    public void setPrfDelToTime(String prfDelToTime) {
        this.prfDelToTime = prfDelToTime;
    }

    public String getApprQty() { return ApprQty; }

    public void setApprQty(String apprQty) { ApprQty = apprQty; }

    public float getRateinrangecase() { return rateinrangecase; }

    public void setRateinrangecase(float rateinrangecase) { this.rateinrangecase = rateinrangecase; }

    public float getMrpinrangecase() { return mrpinrangecase; }

    public void setMrpinrangecase(float mrpinrangecase) { this.mrpinrangecase = mrpinrangecase; }

    public String getCustLat() { return custLat; }

    public void setCustLat(String custLat) { this.custLat = custLat; }

    public String getCustLng() { return custLng; }

    public void setCustLng(String custLng) { this.custLng = custLng; }
}
